package com.mrprez.gencross.web.selenium;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.mrprez.gencross.web.tester.MailTester;
import com.mrprez.gencross.web.tester.PageTester;
import com.mrprez.gencross.web.utils.StreamProcessManager;
import com.mrprez.gencross.web.utils.WebDriverProxy;

/**
 * Cette classe est la base de tous les tests JUnit Selenium. Elle est responsable du
 * démarrage et de l'arrêt du server Tomcat. Elle va initialiser le PageTester
 * et le MailTester, reinitialiser la base de données, lancer le ChromeDriver.
 * Pour mettre de coté tous les masques avant de lancer le test (pour générer
 * les nouveaux masques), il faut décommenter les lignes dans la méthode
 * loadProperties(Properties).
 * 
 */
public abstract class WebAbstractTest {
	private static final String JDK_PATH_VAR_NAME = "jdk.path";
	private static final String MAIL_PATH = "mail.path";
	private static final String FAIL_FILE_NAME = "FAIL.html";
	

	protected String name;

	private Properties properties;
	private File root;
	private File tomcatDir;
	protected File resourceDir;
	protected File workDir;
	protected String baseUrl;
	protected String context;

	private Process tomcatProcess;
	
	protected WebDriverProxy driver;
	protected PageTester pageTester;
	protected MailTester mailTester;

	public WebAbstractTest(String name) throws IOException {
		super();
		System.out.println("\nSTART test "+name);
		this.name = name;
		properties = new Properties();

		if (System.getProperty("propertiesFile") != null) {
			System.out.println("load properties file: " + System.getProperty("propertiesFile"));
			properties.load(new FileReader(System.getProperty("propertiesFile")));
		} else {
			System.out.println("load default properties file.");
			properties.load(ClassLoader.getSystemResourceAsStream("tests.properties"));
		}

		for (Object key : properties.keySet()) {
			System.out.println(key + "=" + getProperty(key.toString()));
		}

		init();
	}
	
	protected abstract void processTest() throws Exception;
	

	@Test
	public void test() throws Exception{
		try{
			processTest();
		}catch(Exception e){
			File failFile = new File(workDir, FAIL_FILE_NAME);
			Writer writer = new OutputStreamWriter(new FileOutputStream(failFile), "UTF-8");
			try {
				writer.write(driver.getPageSource());
			} finally {
				writer.close();
			}
			throw e;
		}
	}
	
	
	protected String getProperty(String name) {
		return getProperty(name, null);
	}

	protected String getProperty(String name, String defaultValue) {
		String value;
		if (System.getProperties().containsKey(name)) {
			value = System.getProperty(name);
		} else if (properties.containsKey(name)) {
			value = properties.getProperty(name);
		} else {
			value = defaultValue;
		}
		while (value != null && value.matches(".*[$][{].+[}].*")) {
			int begin = value.indexOf("${");
			int end = value.indexOf("}", begin);
			String subPropertyKey = value.substring(begin + 2, end);
			String subPropertyValue = getProperty(subPropertyKey);
			value = value.substring(0, begin) + subPropertyValue + value.substring(end + 1);
		}
		return value;
	}

	private void init() throws IOException {
		root = new File(getProperty("basedir") + "/src/it/resources");
		resourceDir = new File(root, name);
		workDir = new File(getProperty("project.build.directory") + "/seleniumTestResult", name);
		workDir.mkdirs();
		
		tomcatDir = new File(getProperty("tomcat.path"));

		baseUrl = getProperty("base.url");
		context = getProperty("project.name");

		pageTester = new PageTester(resourceDir, workDir);
		pageTester.addReplacementRule("jsessionid=[0-9A-F]{32}", "jsessionid=00000000000000000000000000000000");
		pageTester.addReplacementRule("<style id=\"wrc-middle-css\" type=\"text/css\">.*?</style>", "");
		pageTester.addReplacementRule("<script id=\"wrc-script-middle_window\" type=\"text/javascript\" language=\"JavaScript\">.*?</script>", "");
		pageTester.addReplacementRule("style=\"\" ", "");
		pageTester.addReplacementRule("style=\"-webkit-user-select: none;\" ", "");
		pageTester.addReplacementRule("cd_frame_id_=\"[0-9a-f]+\" ", "");
		pageTester.addReplacementRule("style=\"zoom: 1;\" ", "");
		pageTester.addReplacementRule("//&lt;!\\[CDATA\\[ ", "");
		pageTester.addReplacementRule(" //\\]\\]&gt;</script>", "</script>");
		pageTester.addReplacementRule("<tbody align=\"left\">", "<tbody>");
		pageTester.addReplacementRule("<thead align=\"left\">", "<thead>");
		
		mailTester = new MailTester(resourceDir, workDir, getProperty(MAIL_PATH));
		mailTester.deleteMailFile();
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("setup");
		Thread.sleep(15000);
		initDatabase();

		deployWebapp();

		launchTomcat();

		launchWebDriver();

	}

	private void deployWebapp() throws IOException {
		File webappsDir = new File(tomcatDir, "webapps");

		File targetDir = new File(getProperty("project.build.directory"));
		File warFile = new File(targetDir, context + ".war");

		File deployedWar = new File(webappsDir, context + ".war");
		for(int i=0; i<10; i++){
			if(FileUtils.deleteQuietly(new File(webappsDir,context))){
				break;
			}
		}
		deployedWar.delete();
		FileUtils.copyFile(warFile, deployedWar);

	}

	/**
	 * Delete the current database files then copy the init database script in
	 * its repository
	 * 
	 * @throws IOException
	 */
	private void initDatabase() throws IOException{
		System.out.println("initDatabase");
		File databaseRep = new File(getProperty("databaseRep"));
		
		File databaseInitScript = new File(resourceDir, "selenium-test.script");
		File databaseInitLobs = new File(resourceDir, "selenium-test.lobs");
		
		FileFilter seleniumDbFileFilter = new PrefixFileFilter("selenium-test.");
		for(File oldSeleniumDbFile : databaseRep.listFiles(seleniumDbFileFilter)){
			oldSeleniumDbFile.delete();
		}
		
		FileUtils.copyFileToDirectory(databaseInitScript, databaseRep);
		if(databaseInitLobs.exists()){
			FileUtils.copyFileToDirectory(databaseInitLobs, databaseRep);
		}
	}

	private void launchTomcat() throws IOException {
		System.out.println("launchTomcat");

		StringBuilder tomcatClasspath = new StringBuilder();
		tomcatClasspath.append(tomcatDir.getAbsolutePath()).append("/bin/bootstrap.jar").append(File.pathSeparator);
		tomcatClasspath.append(tomcatDir.getAbsolutePath()).append("/bin/tomcat-juli.jar");

		List<String> command = new ArrayList<String>();
		command.add(getProperty(JDK_PATH_VAR_NAME) + "\\bin\\java.exe");
		command.add("-Dcatalina.base=" + tomcatDir.getAbsolutePath());
		command.add("-Dcatalina.home=" + tomcatDir.getAbsolutePath());
		command.add("-Djava.endorsed.dirs=" + tomcatDir.getAbsolutePath() + File.separator + "endorsed");
		command.add("-classpath");
		command.add(tomcatClasspath.toString());
		command.add("org.apache.catalina.startup.Bootstrap");
		command.add("start");
		System.out.println("Tomcat cmd: " + StringUtils.join(command, " "));
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		tomcatProcess = processBuilder.start();
		new StreamProcessManager(tomcatProcess.getInputStream(), System.out, "[Tomcat]").start();
		new StreamProcessManager(tomcatProcess.getErrorStream(), System.err, "[Tomcat]").start();

	}

	
	private void launchWebDriver() throws InterruptedException, MalformedURLException {
		
		WebDriver webDriver;
		if(getProperty("webdriver.chrome.driver")!=null){
			System.setProperty("webdriver.chrome.driver", getProperty("webdriver.chrome.driver"));
			webDriver = new ChromeDriver();
		}else{
			webDriver = new HtmlUnitDriver(BrowserVersion.CHROME);
			((HtmlUnitDriver)webDriver).setJavascriptEnabled(true);
		}
		
		webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		
		driver = new WebDriverProxy(webDriver, 500);
		
		driver.addWaitCondition(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return !driver.findElements(By.id("footer")).isEmpty();
			}
		});
	}

	@After
	public void tearDown() throws Exception {
		recordScreen("finalScreenShot");

		try {
			if (tomcatProcess != null) {
				tomcatProcess.destroy();
				tomcatProcess.waitFor();
				System.out.println("tomcat process ended with exit value: "+tomcatProcess.exitValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (driver != null) {
				driver.quit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Assert.assertTrue("Some templates are missing", pageTester.isAllTemplatePresent());
		Assert.assertTrue("Mail template is missing", mailTester.isAllTemplatePresent());

	}

	protected void recordScreen(String name) throws InterruptedException, AWTException, IOException {
		File imgFile = new File(pageTester.getWorkDir(), name + ".jpg");
		if (imgFile.exists()) {
			imgFile.delete();
		}
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension d = tool.getScreenSize();
		Rectangle rect = new Rectangle(d);
		Robot robot = new Robot();
		Thread.sleep(2000);
		BufferedImage img = robot.createScreenCapture(rect);
		ImageIO.write(img, "jpeg", imgFile);
		tool.beep();
	}
	


}
