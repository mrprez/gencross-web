package com.mrprez.gencross.web.selenium;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;

import com.mrprez.gencross.web.selenium.WebAbstractTest;

public class TableMessagesTest extends WebAbstractTest {
	
	

	public TableMessagesTest() throws IOException {
		super("TableMessages");
		
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		File dummyImapFolder = new File(mailTester.getMailFile().getParentFile(), "tableMessageMail");
		dummyImapFolder.mkdirs();
		FileUtils.copyFileToDirectory(new File(resourceDir, "INBOX"), dummyImapFolder);
	}


	@Test
	public void testTableMessages() throws Exception {
		driver.get(baseUrl + "gencross-web/List.action");
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("test");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("test");
		driver.findElement(By.id("Login_0")).click();
		driver.findElement(By.cssSelector("span.menu")).click();
		driver.get(baseUrl + "gencross-web/TableList.action");
		pageTester.testPage(driver, "tableList");
		driver.findElement(By.id("TableList!addTable_tableName")).clear();
		driver.findElement(By.id("TableList!addTable_tableName")).sendKeys("Tests messages");
		driver.findElement(By.id("TableList!addTable_0")).click();
		pageTester.testPage(driver, "tableCreated");
		driver.findElement(By.linkText("Tests messages")).click();
		pageTester.testPage(driver, "editTable0");
		driver.findElement(By.id("addMessageForm_addMessage")).click();
		pageTester.testPage(driver, "editTable1");
		driver.findElement(By.id("EditTable!refreshMessages_0")).click();
		pageTester.testPage(driver, "editTable2");
		driver.findElement(By.id("EditTable!removeMessage_0")).click();
		try{
			Thread.sleep(1000);
			Alert alert = driver.switchTo().alert();
			driver.switchTo().alert().accept();
			Assert.assertEquals("Voulez-vous supprimer ce message?", alert.getText());
			alert.accept();
		}catch(WebDriverException wde){
			System.err.println("WebDriverException:"+wde.getMessage());
			wde.printStackTrace();
			driver.get(baseUrl+context+"/EditTable!removeMessage.action?id=1&messageId=2");
		}
		pageTester.testPage(driver, "editTable3");
		
		mailTester.test("mail");
	}

	
}
