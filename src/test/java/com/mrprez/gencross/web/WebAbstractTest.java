package com.mrprez.gencross.web;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
	public static final String JDK_PATH_VAR_NAME = "jdk.path";
	public static final String TOMCAT_PATH_VAR_NAME = "tomcat.path";
	public static final String CATALINA_HOME_VAR_NAME = "catalina.home";
	public static final String CATALINA_BASE_VAR_NAME = "catalina.base";
	public static final String CATALINA_REF_VAR_NAME = "catalina.reference";
	public static final String JAVA_ENDORSED_DIRS_VAR_NAME = "java.endorsed.dirs";
	public static final String WTP_DEPLOY_VAR_NAME = "wtp.deploy";
	public static final String GENCROSS_CLASSES_VAR_NAME = "gencross.classes";
	public static final String MAVEN_REPO_VAR_NAME = "maven.repo";
	public static final String PLUGIN_CLASSPATH_VAR_NAME = "plugin.classpath";
	public static final String DATABASE_VAR_NAME = "database";
	public static final String RESOURCES_ROOT_VAR_NAME = "resources.root";
	public static final String DEFAULT_DATABASE_INIT_FILE_NAME = "gencross-test";
	public static final String DATABASE_INIT_VAR_NAME = "database.initScript";
	public static final String BASE_URL_VAR_NAME = "base.url";
	public static final String CONTEXT_VAR_NAME = "web.context";
	public static final String PAGE_SAVE_DIR_VAR_NAME = "page.save.dir";
	public static final String WEBDRIVER_CHROME_DRIVER_VAR_NAME = "webdriver.chrome.driver";
	public static final String WEBAPP_SRC_VAR_NAME = "webapp.src";
	public static final String WEBAPP_CLASSES_VAR_NAME = "webapp.classes";
	public static final String WEBAPP_LIB_VAR_NAME = "webapp.lib";
	public static final String WEBAPP_PARAM_TEST_FILES_VAR_NAME = "webapp.paramTestFiles";
	public static final String SELENIUM_SERVER_PATH_VAR_NAME = "selenium.path";
	public static final String SELENIUM_NODE_USER_VAR_NAME = "selenium.node.user";
	public static final String SELENIUM_NODE_PROCESS_DIR_VAR_NAME = "selenium.node.process.directory";
	public static final String MAIL_PATH = "mail.path";
	
	public static final String PROPERTIES_FILE_VAR_NAME = "propertiesFile";
	public static final String DEFAULT_PROPERTIES_FILE = "tests.properties";
	
	protected String name;
	
	private Properties properties;
	private String tomcatMainClass = "org.apache.catalina.startup.Bootstrap";
	private String tomcatCmd = "start";
	private String webInfFileName = "WEB-INF";
	private String classesFileName = "classes";
	private String libFileName = "lib";
	private int seleniumNodePort = 5555;
	private File root;
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
		Properties properties = new Properties();
		
		if(System.getProperty(PROPERTIES_FILE_VAR_NAME) != null){
			System.out.println("load properties file: "+System.getProperty(PROPERTIES_FILE_VAR_NAME));
			properties.load(new FileReader(System.getProperty(PROPERTIES_FILE_VAR_NAME)));
		} else {
			System.out.println("load default properties file.");
			properties.load(ClassLoader.getSystemResourceAsStream(DEFAULT_PROPERTIES_FILE));
		}
		
		for(Object key : properties.keySet()){
			System.out.println("\t"+key+"="+properties.getProperty(key.toString()));
		}
		
		init(properties);
		
	}
	
	public WebAbstractTest(String name, Properties properties) throws IOException {
		super();
		this.name = name;
		init(properties);
	}
	
	protected String getProperty(String name){
		return getProperty(name, null);
	}
	
	protected String getProperty(String name, String defaultValue){
		System.out.println(name+"?");
		String value;
		if(System.getProperties().containsKey(name)){
			System.out.print("System  => ");
			value = System.getProperty(name);
		} else if(properties.containsKey(name)){
			System.out.print("File    => ");
			value = properties.getProperty(name);
		} else {
			System.out.print("Default => ");
			value = defaultValue;
		}
		while(value!=null && value.matches(".*[$][{].+[}].*")){
			int begin = value.indexOf("${");
			int end = value.indexOf("}", begin);
			String subPropertyKey = value.substring(begin + 2, end);
			String subPropertyValue = getProperty(subPropertyKey);
			value = value.substring(0, begin) + subPropertyValue + value.substring(end + 1);
		}
		System.out.println(name+"="+value);
		return value;
	}
	
	private void init(Properties properties) throws IOException{
		this.properties = properties;
		
		root = new File(getProperty(RESOURCES_ROOT_VAR_NAME));
		resourceDir = new File(root, name);
		
		/*File oldResourceDir = new File(resourceDir.getParentFile(), resourceDir.getName()+"_old");
		if(oldResourceDir.exists()){
			throw new RuntimeException(oldResourceDir.getAbsolutePath()+" already exist");
		}
		resourceDir.renameTo(oldResourceDir);
		resourceDir = new File(root, name);
		resourceDir.mkdir();
		new File(oldResourceDir, databaseInitScript.getName()).renameTo(databaseInitScript);*/
		
		baseUrl = getProperty(BASE_URL_VAR_NAME);
		context = getProperty(CONTEXT_VAR_NAME);
		
		pageTester = new PageTester(name, root.getAbsolutePath(), getProperty(PAGE_SAVE_DIR_VAR_NAME));
		pageTester.setIgnoreWhiteSpace(true);
		
		mailTester = new MailTester(name, root.getAbsolutePath(), getProperty(PAGE_SAVE_DIR_VAR_NAME), getProperty(MAIL_PATH));
		mailTester.deleteMailFile();
		
	}
	
	
	
	@Before
	public void setUp() throws Exception {
		System.out.println("setup");
		Thread.sleep(1000);
		initDatabase();
		
		initCatalina();
		
		deployWebapp();
		
		launchTomcat();
		
		launchSeleniumHub();
		
		launchSeleniumNode();
		
		int startTryNb = 0;
		while(driver == null){
			try{
				driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.chrome());
			}catch (WebDriverException e) {
				startTryNb++;
				if(startTryNb>100){
					throw e;
				}
				Thread.sleep(1000);
			}
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		pageTester.addReplacementRule("jsessionid=[0-9A-F]{32}", "jsessionid=00000000000000000000000000000000");
		pageTester.addReplacementRule("<style id=\"wrc-middle-css\" type=\"text/css\">.*?</style>", "");
		pageTester.addReplacementRule("<script id=\"wrc-script-middle_window\" type=\"text/javascript\" language=\"JavaScript\">.*?</script>", "");
		pageTester.addWaitCondition(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return !driver.findElements(By.id("footer")).isEmpty();
			}
		});
	}
	
	private void deployWebapp() throws IOException {
		File catalinaBase = new File(getProperty(CATALINA_BASE_VAR_NAME));
		File wtpwebapps = new File(catalinaBase, "wtpwebapps");
		File webappDir = new File(wtpwebapps, context);
		webappDir.mkdir();
		FileUtils.cleanDirectory(webappDir);
		FileUtils.copyDirectory(new File(getProperty(WEBAPP_SRC_VAR_NAME)), webappDir);
		File webInfDir = new File(webappDir, webInfFileName);
		File classDir = new File(webInfDir, classesFileName);
		FileUtils.copyDirectory(new File(getProperty(WEBAPP_CLASSES_VAR_NAME)), classDir);
		
		String paramTestFiles[] = getProperty(WEBAPP_PARAM_TEST_FILES_VAR_NAME).split(";");
		for(int i=0; i<paramTestFiles.length; i++){
			FileUtils.copyFileToDirectory(new File(paramTestFiles[i]), classDir);
		}
		
		File libDir = new File(webInfDir, libFileName);
		FileUtils.copyDirectory(new File(getProperty(WEBAPP_LIB_VAR_NAME)), libDir);

	}
	
	/**
	 * Delete the current database files then copy the init database script in its repository
	 * @throws IOException 
	 */
	private void initDatabase() throws IOException{
		System.out.println("initDatabase");
		String databasePath = getProperty(DATABASE_VAR_NAME);
		File databaseInitScript = new File(resourceDir, getProperty(DATABASE_INIT_VAR_NAME));
		File databaseInitlobs = new File(databaseInitScript.getAbsolutePath().replace(".script", ".lobs"));
		
		File databaseRep = new File(databasePath).getParentFile();
		String databaseName = new File(databasePath).getName();
		File databaseFiles[] = databaseRep.listFiles();
		for(int i=0; i<databaseFiles.length; i++){
			File databaseFile = databaseFiles[i];
			if(databaseFile.getName().startsWith(databaseName)){
				databaseFile.delete();
			}
		}
		
		FileUtils.copyFileToDirectory(databaseInitScript, databaseRep);
		if(databaseInitlobs.exists()){
			FileUtils.copyFileToDirectory(databaseInitlobs, databaseRep);
		}
	}
	
	private void initCatalina() throws IOException, InterruptedException {
		System.out.println("initCatalina");
		File catalinaBase = new File(getProperty(CATALINA_BASE_VAR_NAME));
		while(catalinaBase.exists()){
			try{
				FileUtils.deleteDirectory(catalinaBase);
			}catch (FileNotFoundException fnfe) {
				System.out.println("" + new Date() + ": " + fnfe.getMessage());
				String fileName = fnfe.getMessage().replace("File does not exist: ", "");
				System.out.println(fileName + " exist? " + (new File(fileName).exists() ? "true" : "false"));
				Thread.sleep(1000);
			}catch (IOException ioe){
				ioe.printStackTrace();
				Thread.sleep(1000);
			}
		}
		FileUtils.copyDirectory(new File(getProperty(CATALINA_REF_VAR_NAME)), catalinaBase);
		replaceVariableInFile(new File(new File(catalinaBase, "conf"), "server.xml"));
		
		File wtpwebapps = new File(catalinaBase, "wtpwebapps");
		new File(wtpwebapps, context).mkdirs();
	}
	
	private void launchTomcat() throws IOException{
		System.out.println("launchTomcat");
		
		String tomcatClasspath = getProperty(TOMCAT_PATH_VAR_NAME) + "\\bin\\bootstrap.jar"
				+ File.pathSeparator + getProperty(JDK_PATH_VAR_NAME) + "\\lib\\tools.jar"
				+ File.pathSeparator + getProperty(GENCROSS_CLASSES_VAR_NAME)
				+ File.pathSeparator + getProperty(MAVEN_REPO_VAR_NAME) + "\\dom4j\\dom4j\\1.6.1\\dom4j-1.6.1.jar"
				+ File.pathSeparator + getProperty(PLUGIN_CLASSPATH_VAR_NAME);
		
		System.out.println("tomcatClasspath="+tomcatClasspath);
		List<String> command = new ArrayList<String>();
		command.add(getProperty(JDK_PATH_VAR_NAME)+"\\bin\\java.exe");
		command.add("-D"+CATALINA_BASE_VAR_NAME+"="+getProperty(CATALINA_BASE_VAR_NAME));
		command.add("-D"+CATALINA_HOME_VAR_NAME+"="+getProperty(CATALINA_HOME_VAR_NAME));
		command.add("-D"+WTP_DEPLOY_VAR_NAME+"="+getProperty(CATALINA_BASE_VAR_NAME)+File.separator+"wtpwebapps");
		command.add("-D"+JAVA_ENDORSED_DIRS_VAR_NAME+"="+getProperty(TOMCAT_PATH_VAR_NAME)+File.separator+"endorsed");
		command.add("-classpath");
		command.add(tomcatClasspath);
		command.add(tomcatMainClass);
		command.add(tomcatCmd);
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
		String seleniumNodeUser = getProperty(SELENIUM_NODE_USER_VAR_NAME);
		if(seleniumNodeUser!=null && !seleniumNodeUser.trim().isEmpty()){
			command.add("runas");
			command.add("/user:"+seleniumNodeUser);
		}
		command.add(getProperty(JDK_PATH_VAR_NAME)+"\\bin\\java.exe");
		command.add("-jar");
		command.add(getProperty(SELENIUM_SERVER_PATH_VAR_NAME));
		command.add("-role");
		command.add("node");
		command.add("-hub");
		command.add("http://localhost:4444/grid/register");
		command.add("-Dwebdriver.chrome.driver="+getProperty(WEBDRIVER_CHROME_DRIVER_VAR_NAME));
		for(String cmdArg : command){
			System.out.print(cmdArg + " ");
		}
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.directory(new File(getProperty(SELENIUM_NODE_PROCESS_DIR_VAR_NAME)));
		seleniumNodeProcess = processBuilder.start();
		new InputStreamRedirector(seleniumNodeProcess.getInputStream(), System.out, "[SeleniumNode]").start();
		new InputStreamRedirector(seleniumNodeProcess.getErrorStream(), System.err, "[SeleniumNode]").start();
		
	}
	
	
	@After
	public void tearDown() throws Exception {
		recordScreen("finalScreenShot");
		
		try{
			tomcatProcess.destroy();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			driver.quit();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			seleniumHubProcess.destroy();
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
