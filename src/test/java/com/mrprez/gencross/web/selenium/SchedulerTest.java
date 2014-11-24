package com.mrprez.gencross.web.selenium;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;


public class SchedulerTest extends WebAbstractTest {
	
	/**
	 * Max time (in second) to wait that a job is finished.
	 */
	private static final int MAX_JOB_TIME = 120;
	
	private File saveRepository;

	public SchedulerTest() throws IOException {
		super("Scheduler");
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		pageTester.addReplacementRule("[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2}", "00/00/0000 00:00:00");
		
		saveRepository = new File(getProperty("project.build.directory")+"/personnageSaves");
		saveRepository.mkdir();
		File oldRepository = new File(saveRepository, "old");
		oldRepository.mkdir();
		
	}
	
	@Test
	public void testSchedulerScreen() throws Exception {
		driver.get(baseUrl + context);
		pageTester.testPage(driver, "login");
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("mrprez");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("hlttclm");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "welcome0");
		driver.get(baseUrl + context + "/Create.action");
		pageTester.testPage(driver, "createPersonnage");
		new Select(driver.findElement(By.id("Create!create_selectedPersonnageTypeName"))).selectByVisibleText("INS");
		driver.findElement(By.id("Create!create_personnageName")).clear();
		driver.findElement(By.id("Create!create_personnageName")).sendKeys("Person1");
		driver.findElement(By.id("Create!create_roleMaître de jeux")).click();
		new Select(driver.findElement(By.id("playerList"))).selectByVisibleText("azerty");
		driver.findElement(By.id("Create!create_0")).click();
		pageTester.testPage(driver, "personnageCreated1");
		driver.get(baseUrl + context + "/Create.action");
		pageTester.testPage(driver, "createPersonnage");
		new Select(driver.findElement(By.id("Create!create_selectedPersonnageTypeName"))).selectByVisibleText("Changelin");
		driver.findElement(By.id("Create!create_personnageName")).clear();
		driver.findElement(By.id("Create!create_personnageName")).sendKeys("Perso2");
		driver.findElement(By.id("Create!create_0")).click();
		pageTester.testPage(driver, "personnageCreated2");
		driver.findElement(By.cssSelector("#li_4 > img.editImg.editPropertyImg")).click();
		new Select(driver.findElement(By.id("form_4_newValue"))).selectByVisibleText("Elemental");
		driver.findElement(By.cssSelector("#form_4 > button[type=\"button\"]")).click();
		driver.get(baseUrl + context + "/TableList.action");
		pageTester.testPage(driver, "tableList0");
		driver.findElement(By.id("TableList!addTable_tableName")).clear();
		driver.findElement(By.id("TableList!addTable_tableName")).sendKeys("Table INS");
		new Select(driver.findElement(By.id("TableList!addTable_tableType"))).selectByVisibleText("INS");
		driver.findElement(By.id("TableList!addTable_0")).click();
		driver.findElement(By.linkText("Table INS")).click();
		driver.findElement(By.id("bindPersonnageForm_0")).click();
		
		driver.get(baseUrl + context + "/JobProcessing.action");
		pageTester.testPage(driver, "job0");
		
		driver.findElement(By.id("schedule-send-personnage-job")).click();
		int waitedSeconds = 0;
		while(driver.getPageSource().contains("Running...") && waitedSeconds<MAX_JOB_TIME){
			pageTester.testPage(driver, "jobRunning1");
			Thread.sleep(1000);
			waitedSeconds++;
			driver.findElement(By.cssSelector("img[alt=\"Recharger la page\"]")).click();
		}
		pageTester.testPage(driver, "jobFinished1");
		mailTester.test("mail1");
		
		driver.findElement(By.id("schedule-save-personnage-job")).click();
		waitedSeconds = 0;
		while(driver.getPageSource().contains("Running...") && waitedSeconds<MAX_JOB_TIME){
			pageTester.testPage(driver, "jobRunning2");
			Thread.sleep(1000);
			waitedSeconds++;
			driver.findElement(By.cssSelector("img[alt=\"Recharger la page\"]")).click();
		}
		pageTester.testPage(driver, "jobFinished2");
		
		driver.findElement(By.id("schedule-get-table-mails-job")).click();
		waitedSeconds = 0;
		while(driver.getPageSource().contains("Running...") && waitedSeconds<MAX_JOB_TIME){
			pageTester.testPage(driver, "jobRunning3");
			Thread.sleep(1000);
			waitedSeconds++;
			driver.findElement(By.cssSelector("img[alt=\"Recharger la page\"]")).click();
		}
		pageTester.testPage(driver, "jobFinished3");
		mailTester.test("mail2");

		
		driver.get(baseUrl + context + "/List");
		pageTester.testPage(driver, "welcome1");
		driver.findElement(By.linkText("Person1")).click();
		pageTester.testPage(driver, "editPerso1");
		driver.findElement(By.cssSelector("img.editImg.editPropertyImg")).click();
		new Select(driver.findElement(By.id("form_0_newValue"))).selectByVisibleText("Baal - Prince de la Guerre");
		driver.findElement(By.cssSelector("button[type=\"button\"]")).click();
		driver.get(baseUrl + context + "/JobProcessing.action");
		pageTester.testPage(driver, "jobFinished3");
		
		driver.findElement(By.id("schedule-send-personnage-job")).click();
		waitedSeconds = 0;
		while(driver.getPageSource().contains("Running...") && waitedSeconds<MAX_JOB_TIME){
			pageTester.testPage(driver, "jobRunning4");
			Thread.sleep(1000);
			waitedSeconds++;
			driver.findElement(By.cssSelector("img[alt=\"Recharger la page\"]")).click();
		}
		pageTester.testPage(driver, "jobFinished3");
		mailTester.test("mail3");

		
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		
		saveRepository.delete();
	}
	
	

}
