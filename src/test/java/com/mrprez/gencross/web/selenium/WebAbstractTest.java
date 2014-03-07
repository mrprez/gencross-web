package com.mrprez.gencross.web.selenium;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.mrprez.gencross.web.tester.MailTester;
import com.mrprez.gencross.web.tester.PageTester;

/**
 * Cette classe est la base de tous les tests JUnit web.
 * Elle est responsable du démarrage et de l'arrêt du server Tomcat.
 * Elle va initialiser le PageTester et le MailTester, reinitialiser la base de données, lancer le ChromeDriver.
 * Pour mettre de coté tous les masques avant de lancer le test (pour générer les nouveaux masques), il faut décommenter les lignes dans la méthode loadProperties(Properties).
 * 
 */
public abstract class WebAbstractTest {
	private static final String JDK_PATH_VAR_NAME = "jdk.path";
	private static final String SELENIUM_SERVER_PATH_VAR_NAME = "selenium.path";
	private static final String MAIL_PATH = "mail.path";
	
	protected String name;
	
	private Properties properties;
	private int seleniumNodePort = 5555;
	private File root;
	private File tomcatDir;
	protected File resourceDir;
	protected String baseUrl;
	protected String context;
	
	private Process tomcatProcess;
	private Process seleniumHubProcess;
	private Process seleniumNodeProcess;
	
	protected WebDriver driver;
	protected PageTester pageTester;
	protected MailTester mailTester;
	
	
	public WebAbstractTest(String name) throws IOException {
		super();
		this.name = name;
		properties = new Properties();
		
		if(System.getProperty("propertiesFile") != null){
			System.out.println("load properties file: "+System.getProperty("propertiesFile"));
			properties.load(new FileReader(System.getProperty("propertiesFile")));
		} else {
			System.out.println("load default properties file.");
			properties.load(ClassLoader.getSystemResourceAsStream("tests.properties"));
		}
		
		for(Object key : properties.keySet()){
			System.out.println( key+"="+properties.getProperty(key.toString()));
		}
		
		init();
	}
	
	protected String getProperty(String name){
		return getProperty(name, null);
	}
	
	protected String getProperty(String name, String defaultValue){
		String value;
		if(System.getProperties().containsKey(name)){
			value = System.getProperty(name);
		} else if(properties.containsKey(name)){
			value = properties.getProperty(name);
		} else {
			value = defaultValue;
		}
		while(value!=null && value.matches(".*[$][{].+[}].*")){
			int begin = value.indexOf("${");
			int end = value.indexOf("}", begin);
			String subPropertyKey = value.substring(begin + 2, end);
			String subPropertyValue = getProperty(subPropertyKey);
			value = value.substring(0, begin) + subPropertyValue + value.substring(end + 1);
		}
		return value;
	}
	
	private void init() throws IOException{
		root = new File(getProperty("basedir")+"/src/test/resources");
		resourceDir = new File(root, name);
		tomcatDir = new File( getProperty("tomcat.path") );
		
		baseUrl = getProperty("base.url");
		context = getProperty("project.name");
		
		pageTester = new PageTester(name, root.getAbsolutePath(), getProperty("project.build.directory")+"/seleniumTestResult");
		pageTester.setIgnoreWhiteSpace(true);
		pageTester.addReplacementRule("jsessionid=[0-9A-F]{32}", "jsessionid=00000000000000000000000000000000");
		pageTester.addReplacementRule("<style id=\"wrc-middle-css\" type=\"text/css\">.*?</style>", "");
		pageTester.addReplacementRule("<script id=\"wrc-script-middle_window\" type=\"text/javascript\" language=\"JavaScript\">.*?</script>", "");
		pageTester.addWaitCondition(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return !driver.findElements(By.id("footer")).isEmpty();
			}
		});
		
		mailTester = new MailTester(name, root.getAbsolutePath(), getProperty("project.build.directory")+"/seleniumTestResult", getProperty(MAIL_PATH));
		mailTester.deleteMailFile();
	}
	
	
	
	@Before
	public void setUp() throws Exception {
		System.out.println("setup");
		Thread.sleep(10000);
		initDatabase();
		
		deployWebapp();
		
		launchTomcat();
		
		launchSeleniumHub();
		
		launchSeleniumNode();
		
		launchRemoteWebDriver();
		
	}
	
		
	private void deployWebapp() throws IOException {
		File webappsDir = new File( tomcatDir, "webapps" );
		
		File targetDir = new File( getProperty("project.build.directory") );
		File warFile = new File( targetDir, context+".war" );
		
		File deployedWar = new File( webappsDir, context+".war" );
		deployedWar.delete();
		FileUtils.copyFile(warFile, deployedWar);
		
	}
	
	/**
	 * Delete the current database files then copy the init database script in its repository
	 * @throws IOException 
	 */
	private void initDatabase() throws IOException{
		System.out.println("initDatabase");
		File databaseRep = new File(getProperty("databaseRep"));
		
		File databaseInitScript = new File(resourceDir, "selenium-test.script");
		File databaseInitLobs = new File(resourceDir, "selenium-test.lobs");
		
		File initScriptFile = new File(databaseRep, databaseInitScript.getName());
		File initLobsFile = new File(databaseRep, databaseInitLobs.getName());
		if(initScriptFile.exists()){
			boolean deleteSuccess = initScriptFile.delete();
			if( ! deleteSuccess ){
				throw new IOException("Cannot delete "+initScriptFile.getAbsolutePath());
			}
		}
		if(initLobsFile.exists()){
			initLobsFile.delete();
		}
		
		FileUtils.copyFileToDirectory(databaseInitScript, databaseRep);
		if(databaseInitLobs.exists()){
			FileUtils.copyFileToDirectory(databaseInitLobs, databaseRep);
		}
	}
	
	private void launchTomcat() throws IOException{
		System.out.println("launchTomcat");
		
		StringBuilder tomcatClasspath = new StringBuilder();
		tomcatClasspath.append(tomcatDir.getAbsolutePath()).append("/bin/bootstrap.jar").append(File.pathSeparator);
		tomcatClasspath.append(tomcatDir.getAbsolutePath()).append("/bin/tomcat-juli.jar");
		
		List<String> command = new ArrayList<String>();
		command.add(getProperty(JDK_PATH_VAR_NAME)+"\\bin\\java.exe");
		command.add("-Dcatalina.base="+tomcatDir.getAbsolutePath());
		command.add("-Dcatalina.home="+tomcatDir.getAbsolutePath());
		command.add("-Djava.endorsed.dirs="+tomcatDir.getAbsolutePath()+File.separator+"endorsed");
		command.add("-classpath");
		command.add(tomcatClasspath.toString());
		command.add("org.apache.catalina.startup.Bootstrap");
		command.add("start");
		System.out.println("Tomcat cmd: "+StringUtils.join(command, " "));
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		tomcatProcess = processBuilder.start();
		new InputStreamRedirector(tomcatProcess.getInputStream(), System.out, "[Tomcat]").start();
		new InputStreamRedirector(tomcatProcess.getErrorStream(), System.err, "[Tomcat]").start();
		
	}
	
	private void launchSeleniumHub() throws IOException{
		System.out.println("launchSeleniumHub");
		
		List<String> command = new ArrayList<String>();
		command.add(getProperty(JDK_PATH_VAR_NAME)+"\\bin\\java.exe");
		command.add("-jar");
		command.add(getProperty(SELENIUM_SERVER_PATH_VAR_NAME));
		command.add("-role");
		command.add("hub");
		System.out.println("Selenium hub cmd: "+StringUtils.join(command, " "));
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		seleniumHubProcess = processBuilder.start();
		new InputStreamRedirector(seleniumHubProcess.getInputStream(), System.out, "[SeleniumHub]").start();
		new InputStreamRedirector(seleniumHubProcess.getErrorStream(), System.err, "[SeleniumHub]").start();
		
	}
	
	private void launchSeleniumNode() throws IOException{
		System.out.println("launchSeleniumNode");
		
		if(!isPortAvailable(seleniumNodePort)){
			System.out.println("Port "+seleniumNodePort+" is bound. A selenium node have to be started on this port");
			return;
		}
		
		List<String> command = new ArrayList<String>();
		command.add(getProperty(JDK_PATH_VAR_NAME)+"\\bin\\java.exe");
		command.add("-jar");
		command.add(getProperty(SELENIUM_SERVER_PATH_VAR_NAME));
		command.add("-role");
		command.add("node");
		command.add("-hub");
		command.add("http://localhost:4444/grid/register");
		command.add("-Dwebdriver.chrome.driver="+getProperty("webdriver.chrome.driver"));
		System.out.println("Selenium node cmd: "+StringUtils.join(command, " "));
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		seleniumNodeProcess = processBuilder.start();
		new InputStreamRedirector(seleniumNodeProcess.getInputStream(), System.out, "[SeleniumNode]").start();
		new InputStreamRedirector(seleniumNodeProcess.getErrorStream(), System.err, "[SeleniumNode]").start();
	}
	
	private void launchRemoteWebDriver() throws InterruptedException, MalformedURLException{
		int startTryNb = 0;
		while(driver == null){
			try{
				driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.chrome());
			}catch (WebDriverException e) {
				startTryNb++;
				if(startTryNb>10){
					throw e;
				}
				Thread.sleep(1000);
			}
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
	
	@After
	public void tearDown() throws Exception {
		recordScreen("finalScreenShot");
		
		try{
			if(tomcatProcess!=null){
				tomcatProcess.destroy();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			if(driver!=null){
				driver.quit();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			if(seleniumHubProcess!=null){
				seleniumHubProcess.destroy();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			if(seleniumNodeProcess != null){
				seleniumNodeProcess.destroy();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Assert.assertTrue("Some templates are missing", pageTester.isAllTemplatePresent());
		Assert.assertTrue("Mail template is missing", mailTester.isAllTemplatePresent());
		
	}
	
	protected void recordScreen(String name) throws InterruptedException, AWTException, IOException{
		File imgFile = new File(pageTester.getWorkDir(), name+".jpg");
		if(imgFile.exists()){
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
	
	private class InputStreamRedirector extends Thread {
		private InputStream inputStream;
		private PrintStream printStream;
		private String header;

		public InputStreamRedirector(InputStream inputStream, PrintStream printStream, String header) {
			super();
			this.inputStream = inputStream;
			this.printStream = printStream;
			this.header = header;
		}
		
		public void run(){
			try{
				String line;
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				while((line = reader.readLine()) != null){
					printStream.println(header + " " + line);
				}
				printStream.println(header+" ** termination **");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void replaceVariableInFile(File file) throws IOException {
		File tempFile = new File(file.getAbsolutePath()+"_temp");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		try{
			String line;
			while((line=reader.readLine()) != null) {
				if(line.matches(".*[$][{].+[}].*")){
					int begin = line.indexOf("${");
					int end = line.indexOf("}", begin);
					String subPropertyKey = line.substring(begin + 2, end);
					String subPropertyValue = getProperty(subPropertyKey);
					line = line.substring(0, begin) + subPropertyValue + line.substring(end + 1);
				}
				writer.write(line);
				writer.newLine();
			}
		}finally{
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(writer);
		}
		file.delete();
		tempFile.renameTo(file);
	}
	
	
	private boolean isPortAvailable(int port) throws IOException{
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(port);
		}catch (SocketException e) {
			return false;
		}finally{
			if(serverSocket != null && !serverSocket.isClosed()){
				serverSocket.close();
			}
		}
		return true;
	}
	
	
	
}
