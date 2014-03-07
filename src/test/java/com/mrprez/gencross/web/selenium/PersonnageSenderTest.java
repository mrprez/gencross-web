package com.mrprez.gencross.web.selenium;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.mrprez.gencross.web.selenium.WebAbstractTest;


public class PersonnageSenderTest extends WebAbstractTest {

	public PersonnageSenderTest() throws IOException {
		super("PersonnageSender");
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		pageTester.addReplacementRule("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}", "0000-00-00 00:00:00");
		
		// TODO tester la crontab
		/*
		FileUtils.moveFile(new File("M:/workspace/gencross-web/src/main/resources/jobs.xml"), new File("M:/workspace/gencross-web/src/main/resources/jobs_save.xml"));
		
		BufferedReader reader = new BufferedReader(new FileReader("M:/workspace/gencross-web/src/test/resources/PersonnageSender/jobs.xml_template"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("M:/workspace/gencross-web/src/test/resources/jobs.xml"));
		try{
			String line;
			while((line=reader.readLine()) != null){
				if(line.contains("${cron-expression}")){
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.MINUTE, 10); 
					calendar.add(Calendar.SECOND, 20);
					line = line.replace("${cron-expression}", ""+calendar.get(Calendar.SECOND)+" "+calendar.get(Calendar.MINUTE)+" * * * ?");
				}
				writer.write(line);
				writer.newLine();
			}
		}finally{
			reader.close();
			writer.close();
		}*/
		
	}
	
	@Test
	public void testSender() throws Exception {
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
		driver.get(baseUrl + context + "/JobProcessing.action");
		pageTester.testPage(driver, "job0");
		Thread.sleep(2*1000);
		driver.findElement(By.linkText("Exécuter")).click();
		Thread.sleep(2*1000);
		driver.get(baseUrl + context + "/JobProcessing.action");
		pageTester.testPage(driver, "jobRunning");
		Thread.sleep(10*1000);
		driver.findElement(By.cssSelector("img[alt=\"Recharger la page\"]")).click();
		pageTester.testPage(driver, "jobRunning");
		Thread.sleep(35*1000);
		driver.get(baseUrl + context + "/JobProcessing.action");
		pageTester.testPage(driver, "jobFinished");
		mailTester.test("mail1");
		driver.get(baseUrl + context + "/List");
		pageTester.testPage(driver, "welcome1");
		driver.findElement(By.linkText("Person1")).click();
		pageTester.testPage(driver, "editPerso1");
		driver.findElement(By.cssSelector("img.editImg.editPropertyImg")).click();
		new Select(driver.findElement(By.id("form_0_newValue"))).selectByVisibleText("Baal - Prince de la Guerre");
		driver.findElement(By.cssSelector("button[type=\"button\"]")).click();
		driver.get(baseUrl + context + "/JobProcessing.action");
		pageTester.testPage(driver, "jobFinished");
		Thread.sleep(2*1000);
		driver.findElement(By.linkText("Exécuter")).click();
		Thread.sleep(2*1000);
		driver.get(baseUrl + context + "/JobProcessing.action");
		pageTester.testPage(driver, "jobRunning");
		Thread.sleep(30*1000);
		driver.findElement(By.cssSelector("img[alt=\"Recharger la page\"]")).click();
		pageTester.testPage(driver, "jobFinished");
		mailTester.test("mail2");
		
	}

	@Override
	public void tearDown() throws Exception {
		/*new File("M:/workspace/gencross-web/src/test/resources/jobs.xml").delete();
		FileUtils.moveFile(new File("M:/workspace/gencross-web/src/main/resources/jobs_save.xml"), new File("M:/workspace/gencross-web/src/main/resources/jobs.xml"));
		*/
		super.tearDown();
	}
	
	

}
