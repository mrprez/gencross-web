package com.mrprez.gencross.web;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class ParamsTest extends WebAbstractTest {

	public ParamsTest() throws IOException {
		super("Params");
	}

	@Test
	public void test() throws Exception {
		driver.get(baseUrl + "gencross-web");
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("mrprez");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("hlttclm");
		driver.findElement(By.id("Login_0")).click();
		driver.findElement(By.cssSelector("#administrationMenu > span.menu")).click();
		driver.get(baseUrl + "gencross-web/Params.action");
		pageTester.testPage(driver, "params1");
		driver.findElement(By.xpath("(//img[@alt='Edit'])[4]")).click();
		pageTester.testPage(driver, "params2");
		driver.findElement(By.id("paramForm_save_adress_newValue")).clear();
		driver.findElement(By.id("paramForm_save_adress_newValue")).sendKeys("newSaveAdresse");
		driver.findElement(By.id("paramForm_save_adress_0")).click();
		pageTester.testPage(driver, "params3");
		driver.findElement(By.xpath("(//img[@alt='Edit'])[3]")).click();
		pageTester.testPage(driver, "params4");
		driver.findElement(By.id("paramForm_migration_active_newValuetrue")).click();
		driver.findElement(By.id("paramForm_migration_active_0")).click();
		pageTester.testPage(driver, "params5");
		driver.findElement(By.xpath("(//img[@alt='Edit'])[2]")).click();
		pageTester.testPage(driver, "params6");
		driver.findElement(By.id("dateField_last_send_date")).click();
		driver.findElement(By.linkText("13")).click();
		new Select(driver.findElement(By.id("paramForm_last_send_date_hour"))).selectByVisibleText("05");
		new Select(driver.findElement(By.id("paramForm_last_send_date_minutes"))).selectByVisibleText("00");
		new Select(driver.findElement(By.id("paramForm_last_send_date_seconds"))).selectByVisibleText("00");
		driver.findElement(By.id("paramForm_last_send_date_milliSeconds")).clear();
		driver.findElement(By.id("paramForm_last_send_date_milliSeconds")).sendKeys("333");
		pageTester.testPage(driver, "params7");
		driver.findElement(By.id("paramForm_last_send_date_0")).click();
		pageTester.testPage(driver, "params8");
	}

	
}
