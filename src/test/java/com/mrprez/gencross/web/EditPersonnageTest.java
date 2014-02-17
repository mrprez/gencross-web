package com.mrprez.gencross.web;

import java.io.IOException;
import java.text.DecimalFormat;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

public class EditPersonnageTest extends WebAbstractTest {
	private int editTestNumber = 0;
	
	public EditPersonnageTest() throws IOException {
		super("EditPersonnage");
		pageTester.addReplacementRule("[0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3} [0-9]{2}/[0-9]{2}/[0-9]{4}", "00:00:00,000 00/00/0000");
		pageTester.addReplacementRule("[0-9]{2}:[0-9]{2} [0-9]{2}/[0-9]{2}", "00:00 00/00");
		pageTester.addWaitCondition(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver webDriver) {
				return !webDriver.findElement(By.id("waitMask")).isDisplayed();
			}
		});
		pageTester.addWaitCondition(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver webDriver) {
				return !webDriver.getPageSource().contains("waitLine");
			}
		});
		pageTester.addWaitCondition(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver webDriver) {
				return !webDriver.getPageSource().contains("waitImg");
			}
		});
	}

	@Test
	public void testEditPersonnage() throws Exception {
		driver.get(baseUrl + context);
		
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("azerty");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("azerty");
		driver.findElement(By.id("Login_0")).click();
		driver.findElement(By.cssSelector("span.menu")).click();
		driver.get(baseUrl + context + "/Create.action");
		new Select(driver.findElement(By.id("Create!create_selectedPersonnageTypeName"))).selectByVisibleText("INS");
		driver.findElement(By.id("Create!create_roleMaître de jeux")).click();
		driver.findElement(By.id("Create!create_personnageName")).clear();
		driver.findElement(By.id("Create!create_personnageName")).sendKeys("Test INS");
		driver.findElement(By.id("Create!create_0")).click();
		testEditPage();
		driver.findElement(By.cssSelector("img.editImg.editPropertyImg")).click();
		new Select(driver.findElement(By.id("form_0_newValue"))).selectByVisibleText("Baal - Prince de la Guerre");
		driver.findElement(By.cssSelector("button[type=\"button\"]")).click();
		testEditPage();
		driver.findElement(By.id("nextPhaseButton")).click();
		testEditPage();
		driver.findElement(By.id("span_3")).click();
		driver.findElement(By.cssSelector("#li_3_0 > img.editImg.editPropertyImg")).click();
		driver.findElement(By.cssSelector("button.plusButton")).click();
		driver.findElement(By.cssSelector("button.plusButton")).click();
		driver.findElement(By.cssSelector("button.plusButton")).click();
		driver.findElement(By.cssSelector("button.plusButton")).click();
		driver.findElement(By.cssSelector("button.plusButton")).click();
		driver.findElement(By.cssSelector("button.plusButton")).click();
		driver.findElement(By.xpath("(//button[@type='button'])[4]")).click();
		testEditPage();
		driver.findElement(By.cssSelector("#li_3_1 > img.editImg.editPropertyImg")).click();
		driver.findElement(By.cssSelector("#form_3_1 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_3_1 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_3_1 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_3_1 > button.plusButton")).click();
		driver.findElement(By.xpath("(//button[@type='button'])[7]")).click();
		testEditPage();
		driver.findElement(By.cssSelector("#li_3_5 > img.editImg.editPropertyImg")).click();
		driver.findElement(By.cssSelector("#form_3_5 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_3_5 > button.plusButton")).click();
		driver.findElement(By.xpath("(//button[@type='button'])[19]")).click();
		testEditPage();
		driver.findElement(By.id("span_5")).click();
		driver.findElement(By.id("span_5_1")).click();
		driver.findElement(By.cssSelector("span.addProperty")).click();
		driver.findElement(By.id("addFreePropertyForm_5_1_newProperty")).clear();
		testEditPage();
		driver.findElement(By.id("addFreePropertyForm_5_1_newProperty")).sendKeys("Nunjaku");
		testEditPage();
		driver.findElement(By.cssSelector("#addFreePropertyForm_5_1 > button[type=\"button\"]")).click();
		testEditPage();
		driver.findElement(By.cssSelector("#li_5_1_0 > img.editImg.editPropertyImg")).click();
		driver.findElement(By.cssSelector("#form_5_1_0 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1_0 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1_0 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1_0 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1_0 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1_0 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1_0 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1_0 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1_0 > button.minusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1_0 > button.minusButton")).click();
		driver.findElement(By.xpath("(//button[@type='button'])[28]")).click();
		testEditPage();
		driver.findElement(By.cssSelector("#li_5_3 > img.editImg.editPropertyImg")).click();
		driver.findElement(By.cssSelector("#form_5_3 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_3 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_3 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_3 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_3 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_3 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_3 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_3 > button.plusButton")).click();
		driver.findElement(By.xpath("(//button[@type='button'])[34]")).click();
		testEditPage();
		driver.findElement(By.cssSelector("#li_5_1 > img.editImg.editPropertyImg")).click();
		driver.findElement(By.cssSelector("#form_5_1 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1 > button.plusButton")).click();
		driver.findElement(By.cssSelector("#form_5_1 > button.plusButton")).click();
		driver.findElement(By.xpath("(//button[@type='button'])[25]")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [getAlert]]
		
	}
	
	private void testEditPage() throws IOException, InterruptedException{
		DecimalFormat format = new DecimalFormat("00");
		pageTester.testPage(driver, "editPersonnage"+format.format(editTestNumber++));
	}

	
}
