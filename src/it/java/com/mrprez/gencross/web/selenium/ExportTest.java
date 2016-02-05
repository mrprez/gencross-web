package com.mrprez.gencross.web.selenium;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class ExportTest extends WebAbstractTest {

	public ExportTest() throws IOException {
		super("Export");
	}
	

	@Override
	public void processTest() throws Exception {
		driver.get(baseUrl + "gencross-web");
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("mrprez");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("hlttclm");
		driver.findElement(By.id("Login_0")).click();
		driver.get(baseUrl + context + "/Create.action");
		new Select(driver.findElement(By.id("Create!create_selectedPersonnageTypeName"))).selectByVisibleText("Pavillon Noir");
		driver.findElement(By.id("Create!create_personnageName")).clear();
		driver.findElement(By.id("Create!create_personnageName")).sendKeys("Test Export");
		driver.findElement(By.id("Create!create_0")).click();
		driver.findElement(By.id("span_2")).click();
		driver.findElement(By.cssSelector("#li_2_1 > img.editImg.editPropertyImg")).click();
		driver.findElement(By.cssSelector("#form_2_1 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_2_1 > button.plusButton")).click();
		driver.findElement(By.id("form_2_1")).findElement(By.linkText("Valider")).click();
		
	}

	
}
