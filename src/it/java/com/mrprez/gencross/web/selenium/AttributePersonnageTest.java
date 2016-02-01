package com.mrprez.gencross.web.selenium;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.Select;

public class AttributePersonnageTest extends WebAbstractTest {
	
	
	public AttributePersonnageTest() throws IOException {
		super("AttributePersonnage");
	}

	
	@Override
	public void processTest() throws Exception {
		driver.get(baseUrl + context);
		pageTester.testPage(driver, "loginPage");
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("azerty");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("azerty");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "login1");
		driver.findElement(By.cssSelector("span.menu")).click();
		driver.get(baseUrl + context + "/Create.action");
		pageTester.testPage(driver, "createPersonnage");
		new Select(driver.findElement(By.id("Create!create_selectedPersonnageTypeName"))).selectByVisibleText("Changelin");
		driver.findElement(By.id("Create!create_personnageName")).clear();
		driver.findElement(By.id("Create!create_personnageName")).sendKeys("Test attibution");
		new Select(driver.findElement(By.id("gameMasterList"))).selectByVisibleText("mrprez");
		driver.findElement(By.id("Create!create_0")).click();
		pageTester.testPage(driver, "createPersonnageSuccess");
		driver.findElement(By.id("disconnect")).click();
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("mrprez");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("hlttclm");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "login2");
		driver.findElement(By.xpath("(//img[@alt='Fermer'])[2]")).click();
		pageTester.testPage(driver, "attribute1");
		new Select(driver.findElement(By.id("AttributePlayer!attribute_newPlayerName"))).selectByVisibleText("qwerty");
		driver.findElement(By.id("AttributePlayer!attribute_0")).click();
		pageTester.testPage(driver, "attributeSuccess1");
		driver.findElement(By.linkText("Retourner à la liste des Personnages.")).click();
		driver.findElement(By.id("disconnect")).click();
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("azerty");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("azerty");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "login3");
		driver.findElement(By.id("disconnect")).click();
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("qwerty");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("qwerty");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "login4");
		driver.findElement(By.cssSelector("a[title=\"Attribuer un MJ\"] > img")).click();
		pageTester.testPage(driver, "attribute2");
		new Select(driver.findElement(By.id("AttributeGameMaster!attribute_newGameMasterName"))).selectByVisibleText("azerty");
		driver.findElement(By.id("AttributeGameMaster!attribute_0")).click();
		pageTester.testPage(driver, "attributeSuccess2");
		driver.findElement(By.cssSelector("img")).click();
		driver.findElement(By.id("disconnect")).click();
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("mrprez");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("hlttclm");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "login5");
		driver.findElement(By.id("disconnect")).click();
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("azerty");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("azerty");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "login6");
		
		driver.findElement(By.cssSelector("img[alt=Supprimer]")).click();
		pageTester.testPage(driver, "deletePersonnage");
		driver.findElement(By.cssSelector("#modalDivButton > button")).click();
		pageTester.testPage(driver, "deleteSuccess1");
		
		driver.findElement(By.id("disconnect")).click();
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("qwerty");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("qwerty");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "login7");
		driver.findElement(By.cssSelector("a[title=\"Attribuer un MJ\"] > img")).click();
		pageTester.testPage(driver, "attribute3");
		driver.findElement(By.cssSelector("span.menu")).click();
		driver.findElement(By.linkText("Liste des personnages")).click();
		driver.findElement(By.id("List!deletePersonnage_0")).click();
		
		driver.findElement(By.cssSelector("img[alt=Supprimer]")).click();
		pageTester.testPage(driver, "deletePersonnage2");
		driver.findElement(By.cssSelector("#modalDivButton > button")).click();
		
		pageTester.testPage(driver, "deleteSuccess2");
		mailTester.test("mail");
		
		
	}

	
}
