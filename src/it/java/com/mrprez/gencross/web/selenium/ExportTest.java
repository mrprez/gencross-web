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
		
		createPersonnage("Test Export 1", "mrprez");
		addToProperty(2, new int[]{2, 1});
		addProperty("Droit", new int[]{5, 3});
		
		createPersonnage("Test Export 2", "azerty");
		addToProperty(2, new int[]{2, 3});
		addProperty("Géographie", new int[]{5, 3});
		
		createPersonnage("Test Export 3", null);
		addToProperty(2, new int[]{2, 4});
		
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
		driver.findElement(By.id("EditTable!addPersonnage_personnageName")).sendKeys("Test Export 5");
		driver.findElement(By.id("EditTable!addPersonnage_0")).click();
		pageTester.testPage(driver, "table3");
		
		driver.findElement(By.linkText("Test Export 4")).click();
		addToProperty(2, new int[]{2, 5});
		addProperty("Histoire", new int[]{5, 3});
		
		driver.get(baseUrl + context + "/TableList.action");
		driver.findElement(By.linkText("Table Export")).click();
		pageTester.testPage(driver, "table3");
		driver.findElement(By.id("MultiExport")).click();
		pageTester.testPage(driver, "export1");
		driver.findElement(By.id("MultiExport_MultiExport!export")).click();
		pageTester.testPage(driver, "export2");
		driver.findElement(By.id("MultiExport!export")).findElement(By.xpath("ul[1]/span/input[@type='checkbox']")).click();
		driver.findElement(By.id("exportedPnjList-3")).click();
		driver.findElement(By.id("MultiExport!export")).findElement(By.xpath("ul[2]/span/input[@type='checkbox']")).click();
		driver.findElement(By.id("MultiExport!export_MultiExport!export")).click();
		pageTester.testPage(driver, "export3");
		driver.findElement(By.linkText("Retour")).click();
		pageTester.testPage(driver, "export1");
		driver.findElement(By.id("MultiExport")).findElement(By.xpath("ul[1]/span/input[@type='checkbox']")).click();
		driver.findElement(By.id("exportedPnjList-3")).click();
		driver.findElement(By.id("MultiExport")).findElement(By.xpath("ul[2]/span/input[@type='checkbox']")).click();
		driver.findElement(By.id("MultiExport_MultiExport!exportCsv")).click();
	}
	
	
	private void createPersonnage(String name, String player){
		driver.get(baseUrl + context + "/Create.action");
		new Select(driver.findElement(By.id("Create!create_selectedPersonnageTypeName"))).selectByVisibleText("Pavillon Noir");
		driver.findElement(By.id("Create!create_personnageName")).clear();
		driver.findElement(By.id("Create!create_personnageName")).sendKeys(name);
		driver.findElement(By.id("Create!create_roleMaître de jeux")).click();
		if(player!=null){
			if(player.equals("mrprez")){
				driver.findElement(By.id("Create!create_roleLes deux")).click();
			}else{
				new Select(driver.findElement(By.id("playerList"))).selectByVisibleText(player);
			}
		}
		driver.findElement(By.id("Create!create_0")).click();	
	}
	
	
	private void addProperty(String name, int[] propertyPath){
		StringBuilder idSuffix = new StringBuilder();
		for(int propertyNb : propertyPath){
			idSuffix.append("_").append(propertyNb);
			driver.findElement(By.id("span"+idSuffix)).click();
		}
		driver.findElement(By.cssSelector("#subProperties"+idSuffix+" > li.addProperty > span.addProperty")).click();
		new Select(driver.findElement(By.id("addPropertyFromOptionForm"+idSuffix+"_optionProperty"))).selectByVisibleText(name);
		driver.findElement(By.id("addPropertyFromOptionForm"+idSuffix)).findElement(By.xpath("button[1]")).click();
	}
	
	
	private void addToProperty(int addNumber, int[] propertyPath){
		StringBuilder idSuffix = new StringBuilder();
		for(int propertyNb : propertyPath){
			idSuffix.append("_").append(propertyNb);
			driver.findElement(By.id("span"+idSuffix)).click();
		}
		driver.findElement(By.cssSelector("#li"+idSuffix+" > img.editImg.editPropertyImg")).click();
		driver.findElement(By.cssSelector("#form"+idSuffix+" > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form"+idSuffix+"> button.plusButton")).click();
		driver.findElement(By.id("form"+idSuffix)).findElement(By.xpath("button[3]")).click();
	}
	
}
