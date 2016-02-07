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
		driver.findElement(By.id("Create!create_personnageName")).sendKeys("Test Export 1");
		driver.findElement(By.id("Create!create_roleLes deux")).click();
		driver.findElement(By.id("Create!create_0")).click();
		driver.findElement(By.id("span_2")).click();
		driver.findElement(By.cssSelector("#li_2_1 > img.editImg.editPropertyImg")).click();
		driver.findElement(By.cssSelector("#form_2_1 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_2_1 > button.plusButton")).click();
		driver.findElement(By.id("form_2_1")).findElement(By.xpath("button[3]")).click();
		
		driver.get(baseUrl + context + "/Create.action");
		new Select(driver.findElement(By.id("Create!create_selectedPersonnageTypeName"))).selectByVisibleText("Pavillon Noir");
		driver.findElement(By.id("Create!create_personnageName")).clear();
		driver.findElement(By.id("Create!create_personnageName")).sendKeys("Test Export 2");
		driver.findElement(By.id("Create!create_roleMaître de jeux")).click();
		new Select(driver.findElement(By.id("playerList"))).selectByVisibleText("azerty");
		driver.findElement(By.id("Create!create_0")).click();
		driver.findElement(By.id("span_2")).click();
		driver.findElement(By.cssSelector("#li_2_3 > img.editImg.editPropertyImg")).click();
		driver.findElement(By.cssSelector("#form_2_3 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_2_3 > button.plusButton")).click();
		driver.findElement(By.id("form_2_3")).findElement(By.xpath("button[3]")).click();
		
		driver.get(baseUrl + context + "/Create.action");
		new Select(driver.findElement(By.id("Create!create_selectedPersonnageTypeName"))).selectByVisibleText("Pavillon Noir");
		driver.findElement(By.id("Create!create_personnageName")).clear();
		driver.findElement(By.id("Create!create_personnageName")).sendKeys("Test Export 3");
		driver.findElement(By.id("Create!create_roleMaître de jeux")).click();
		driver.findElement(By.id("Create!create_0")).click();
		driver.findElement(By.id("span_2")).click();
		driver.findElement(By.cssSelector("#li_2_4 > img.editImg.editPropertyImg")).click();
		driver.findElement(By.cssSelector("#form_2_4 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_2_4 > button.plusButton")).click();
		driver.findElement(By.id("form_2_4")).findElement(By.xpath("button[3]")).click();
		
		driver.get(baseUrl + context + "/TableList.action");
		driver.findElement(By.id("TableList!addTable_tableName")).clear();
		driver.findElement(By.id("TableList!addTable_tableName")).sendKeys("Table Export");
		new Select(driver.findElement(By.id("TableList!addTable_tableType"))).selectByVisibleText("Pavillon Noir");
		driver.findElement(By.id("TableList!addTable_0")).click();
		pageTester.testPage(driver, "createTableSuccess");
		driver.findElement(By.linkText("Table Export")).click();
		pageTester.testPage(driver, "table1");
		new Select(driver.findElement(By.id("bindPersonnageForm_personnageId"))).selectByVisibleText("Test Export 1");
		driver.findElement(By.id("bindPersonnageForm_0")).click();
		new Select(driver.findElement(By.id("bindPersonnageForm_personnageId"))).selectByVisibleText("Test Export 2");
		driver.findElement(By.id("bindPersonnageForm_0")).click();
		new Select(driver.findElement(By.id("bindPersonnageForm_personnageId"))).selectByVisibleText("Test Export 3");
		driver.findElement(By.id("bindPersonnageForm_0")).click();
		pageTester.testPage(driver, "table2");
		driver.findElement(By.id("EditTable!addPersonnage_personnageName")).sendKeys("Test Export 4");
		driver.findElement(By.id("EditTable!addPersonnage_0")).click();
		pageTester.testPage(driver, "table3");
		
	}
	
	
}
