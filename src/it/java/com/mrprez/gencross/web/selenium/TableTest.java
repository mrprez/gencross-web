package com.mrprez.gencross.web.selenium;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import com.mrprez.gencross.web.tester.PageTester;

public class TableTest extends WebAbstractTest{
	private PageTester editPersonnageTester;
	

	public TableTest() throws IOException {
		super("Table");
		
		pageTester.addReplacementRule("[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2}", "00/00/0000 00:00:00");
		
		editPersonnageTester = new PageTester(resourceDir, workDir);
		editPersonnageTester.addReplacementRule("[0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3} [0-9]{2}/[0-9]{2}/[0-9]{4}", "00:00:00,000 00/00/0000");
		editPersonnageTester.addReplacementRule("[0-9]{2}:[0-9]{2} [0-9]{2}/[0-9]{2}", "00:00 00/00");
	}
	

	@Override
	public void processTest() throws Exception {
		driver.get(baseUrl + context + "/Login.action");
		pageTester.testPage(driver, "login");
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("mrprez");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("hlttclm");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "personnageList0");
		driver.get(baseUrl + context + "/Create.action");
		pageTester.testPage(driver, "createPersonnage");
		new Select(driver.findElement(By.id("Create!create_selectedPersonnageTypeName"))).selectByVisibleText("INS");
		driver.findElement(By.id("Create!create_personnageName")).clear();
		driver.findElement(By.id("Create!create_personnageName")).sendKeys("Table INS 1");
		driver.findElement(By.id("Create!create_0")).click();
		pageTester.testPage(driver, "createPersonnageSuccess1");
		driver.get(baseUrl + context + "/Create.action");
		pageTester.testPage(driver, "createPersonnage");
		new Select(driver.findElement(By.id("Create!create_selectedPersonnageTypeName"))).selectByVisibleText("INS");
		driver.findElement(By.id("Create!create_personnageName")).clear();
		driver.findElement(By.id("Create!create_personnageName")).sendKeys("Table INS 2");
		driver.findElement(By.id("Create!create_roleMaître de jeux")).click();
		new Select(driver.findElement(By.id("playerList"))).selectByVisibleText("azerty");
		driver.findElement(By.id("Create!create_0")).click();
		pageTester.testPage(driver, "createPersonnageSuccess2");
		driver.get(baseUrl + context + "/Create.action");
		pageTester.testPage(driver, "createPersonnage");
		new Select(driver.findElement(By.id("Create!create_selectedPersonnageTypeName"))).selectByVisibleText("INS");
		driver.findElement(By.id("Create!create_personnageName")).clear();
		driver.findElement(By.id("Create!create_personnageName")).sendKeys("Table INS 3");
		driver.findElement(By.id("Create!create_roleLes deux")).click();
		driver.findElement(By.id("Create!create_0")).click();
		pageTester.testPage(driver, "createPersonnageSuccess3");
		driver.get(baseUrl + context + "/TableList.action");
		pageTester.testPage(driver, "tableList0");
		driver.findElement(By.id("TableList!addTable_tableName")).clear();
		driver.findElement(By.id("TableList!addTable_tableName")).sendKeys("Table INS");
		new Select(driver.findElement(By.id("TableList!addTable_tableType"))).selectByVisibleText("INS");
		driver.findElement(By.id("TableList!addTable_0")).click();
		pageTester.testPage(driver, "createTableSuccess");
		driver.findElement(By.linkText("Table INS")).click();
		pageTester.testPage(driver, "editTable00");
		driver.findElement(By.id("bindPersonnageForm_0")).click();
		pageTester.testPage(driver, "editTable01");
		driver.findElement(By.id("bindPersonnageForm_0")).click();
		pageTester.testPage(driver, "editTable02");
		driver.findElement(By.id("EditTable!addPersonnage_personnageName")).clear();
		driver.findElement(By.id("EditTable!addPersonnage_personnageName")).sendKeys("Table INS 4");
		driver.findElement(By.id("EditTable!addPersonnage_0")).click();
		pageTester.testPage(driver, "editTable03");
		driver.findElement(By.cssSelector("img[alt=\"Attribuer à un joueur\"]")).click();
		pageTester.testPage(driver, "attributePlayer1");
		new Select(driver.findElement(By.id("AttributePlayerInTable!attribute_newPlayerName"))).selectByVisibleText("azerty");
		driver.findElement(By.id("AttributePlayerInTable!attribute_0")).click();
		pageTester.testPage(driver, "editTable04");
		driver.findElement(By.id("transformInPnjForm_0")).click();
		pageTester.testPage(driver, "editTable05");
		driver.findElement(By.cssSelector("img")).click();
		pageTester.testPage(driver, "personnageList1");
		driver.get(baseUrl+context+"/TableList.action");
		driver.findElement(By.linkText("Table INS")).click();
		pageTester.testPage(driver, "editTable05");
		driver.findElement(By.cssSelector("#pjList > tbody > tr > td.genCrossTable > a[title=\"Exclure de la table\"] > img[alt=\"Exclure de la table\"]")).click();
		pageTester.testPage(driver, "editTable06");
		driver.findElement(By.cssSelector("img")).click();
		pageTester.testPage(driver, "personnageList2");
		driver.get(baseUrl + context + "/TableList.action");
		pageTester.testPage(driver, "tableList1");
		driver.findElement(By.linkText("Table INS")).click();
		pageTester.testPage(driver, "editTable06");
		driver.findElement(By.id("bindPersonnageForm_0")).click();
		pageTester.testPage(driver, "editTable07");
		driver.findElement(By.id("EditTable!addPersonnage_personnageName")).clear();
		driver.findElement(By.id("EditTable!addPersonnage_personnageName")).sendKeys("Table INS 5");
		driver.findElement(By.id("EditTable!addPersonnage_0")).click();
		pageTester.testPage(driver, "editTable08");
		driver.findElement(By.id("EditTable!addPersonnage_personnageName")).clear();
		driver.findElement(By.id("EditTable!addPersonnage_personnageName")).sendKeys("Table INS 6");
		driver.findElement(By.id("EditTable!addPersonnage_0")).click();
		driver.findElement(By.id("EditTable!addPersonnage_personnageName")).clear();
		driver.findElement(By.id("EditTable!addPersonnage_personnageName")).sendKeys("Table INS 7");
		driver.findElement(By.id("EditTable!addPersonnage_0")).click();
		pageTester.testPage(driver, "editTable09");
		driver.findElement(By.xpath("(//img[@alt='Attribuer à un joueur'])[2]")).click();
		pageTester.testPage(driver, "attributePlayer2");
		new Select(driver.findElement(By.id("AttributePlayerInTable!attribute_newPlayerName"))).selectByVisibleText("azerty");
		driver.findElement(By.id("AttributePlayerInTable!attribute_0")).click();
		pageTester.testPage(driver, "editTable10");
		
		mailTester.test("mail");
		
		driver.findElement(By.linkText("Table INS 2")).click();
	    driver.findElement(By.cssSelector("img.editImg.editPropertyImg")).click();
	    driver.findElement(By.cssSelector("button[type=\"button\"]")).click();
	    editPersonnageTester.testPage(driver, "editPerso1");
	    driver.findElement(By.id("nextPhaseButton")).click();
	    editPersonnageTester.testPage(driver, "editPerso2");
	    driver.findElement(By.id("span_9")).click();
	    driver.findElement(By.cssSelector("#subProperties_9 > li.addProperty > span.addProperty")).click();
	    new Select(driver.findElement(By.id("addPropertyFromOptionForm_9_optionProperty"))).selectByVisibleText("Adhérence");
	    driver.findElement(By.cssSelector("#addPropertyFromOptionForm_9 > button[type=\"button\"]")).click();
	    editPersonnageTester.testPage(driver, "editPerso3");
	    driver.findElement(By.id("span_3")).click();
	    driver.findElement(By.cssSelector("#li_3_1 > img.editImg.editPropertyImg")).click();
	    driver.findElement(By.cssSelector("#form_3_1 > button.minusButton")).click();
	    driver.findElement(By.cssSelector("#form_3_1 > button.plusButton")).click();
	    driver.findElement(By.cssSelector("#form_3_1 > button.plusButton")).click();
	    driver.findElement(By.cssSelector("#form_3_1 > button.plusButton")).click();
	    driver.findElement(By.xpath("(//button[@type='button'])[7]")).click();
	    editPersonnageTester.testPage(driver, "editPerso4");
	    driver.findElement(By.id("span_5")).click();
	    driver.findElement(By.cssSelector("#li_5_11 > img.editImg.editPropertyImg")).click();
	    driver.findElement(By.cssSelector("#form_5_11 > img.foldImg")).click();
	    driver.findElement(By.cssSelector("#li_5_11 > a.motherPropertyName > img.expandImg")).click();
	    driver.findElement(By.cssSelector("#subProperties_5_11 > li.addProperty > span.addProperty")).click();
	    driver.findElement(By.id("addFreePropertyForm_5_11_newProperty")).clear();
	    driver.findElement(By.id("addFreePropertyForm_5_11_newProperty")).sendKeys("Arme de poing");
	    driver.findElement(By.cssSelector("#addFreePropertyForm_5_11 > button[type=\"button\"]")).click();
	    editPersonnageTester.testPage(driver, "editPerso5");
	    driver.findElement(By.linkText("Table INS")).click();
	    pageTester.testPage(driver, "editTable11");
		
	}




	

	
}
