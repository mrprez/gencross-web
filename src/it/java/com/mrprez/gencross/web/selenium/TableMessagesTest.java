package com.mrprez.gencross.web.selenium;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

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
		driver.addWaitCondition(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver input) {
				if(input.getCurrentUrl().contains("EditTable")){
					return input.getPageSource().contains("/js/ckeditor/plugins/styles/styles/default.js");
				}
				return true;
			}
		});
	}


	@Override
	public void processTest() throws Exception {
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
		driver.findElement(By.id("removeMessageImg_1")).click();
		pageTester.testPage(driver, "editTable3");
		driver.findElement(By.cssSelector("#modalDivButton > button")).click();
		pageTester.testPage(driver, "editTable4");
		
		mailTester.test("mail");
	}

	
}
