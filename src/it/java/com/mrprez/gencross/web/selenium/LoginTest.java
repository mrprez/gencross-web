package com.mrprez.gencross.web.selenium;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;

import com.mrprez.gencross.web.selenium.WebAbstractTest;


public class LoginTest extends WebAbstractTest {
	
	
	public LoginTest() throws IOException {
		super("Login");
	}

	
	@Test
	public void test() throws Exception {
		driver.get(baseUrl + "gencross-web");
		//pageTester.testPage(driver, "login1");
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("batman");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("robin");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "loginFail1");
		driver.get(baseUrl + "gencross-web/Subscription.action");
		driver.findElement(By.id("subscriptionForm_0")).click();
		pageTester.testPage(driver, "subscription");
		driver.findElement(By.id("subscriptionForm_username")).clear();
		driver.findElement(By.id("subscriptionForm_username")).sendKeys("batman");
		driver.findElement(By.id("subscriptionForm_password")).clear();
		driver.findElement(By.id("subscriptionForm_password")).sendKeys("robin");
		driver.findElement(By.id("subscriptionForm_confirmPassword")).clear();
		driver.findElement(By.id("subscriptionForm_confirmPassword")).sendKeys("robinwood");
		driver.findElement(By.id("subscriptionForm_mail")).clear();
		driver.findElement(By.id("subscriptionForm_mail")).sendKeys("batman@mail.com");
		driver.findElement(By.id("subscriptionForm_0")).click();
		pageTester.testPage(driver, "subscriptionFail1");
		driver.findElement(By.id("subscriptionForm_password")).clear();
		driver.findElement(By.id("subscriptionForm_password")).sendKeys("robin");
		driver.findElement(By.id("subscriptionForm_confirmPassword")).clear();
		driver.findElement(By.id("subscriptionForm_confirmPassword")).sendKeys("robin");
		driver.findElement(By.id("subscriptionForm_mail")).clear();
		driver.findElement(By.id("subscriptionForm_mail")).sendKeys("batman.mail.com");
		driver.findElement(By.id("subscriptionForm_0")).click();
		pageTester.testPage(driver, "subscriptionFail2");
		driver.findElement(By.id("subscriptionForm_password")).clear();
		driver.findElement(By.id("subscriptionForm_password")).sendKeys("robin");
		driver.findElement(By.id("subscriptionForm_confirmPassword")).clear();
		driver.findElement(By.id("subscriptionForm_confirmPassword")).sendKeys("robin");
		driver.findElement(By.id("subscriptionForm_mail")).clear();
		driver.findElement(By.id("subscriptionForm_mail")).sendKeys("batman@mail.com");
		driver.findElement(By.id("subscriptionForm_0")).click();
		pageTester.testPage(driver, "welcome1");
		driver.findElement(By.id("disconnect")).click();
		pageTester.testPage(driver, "login2");
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("batman");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("robinwood");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "loginFail");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("robin");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "welcome2");
		driver.findElement(By.id("disconnect")).click();
		pageTester.testPage(driver, "login3");
		driver.findElement(By.linkText("Mot de passe oublié")).click();
		pageTester.testPage(driver, "forgottenPassword");
		driver.findElement(By.id("ForgottenPassword_username")).clear();
		driver.findElement(By.id("ForgottenPassword_username")).sendKeys("catwoman");
		driver.findElement(By.id("ForgottenPassword_0")).click();
		pageTester.testPage(driver, "forgottenPasswordFail");
		driver.findElement(By.id("ForgottenPassword_username")).clear();
		driver.findElement(By.id("ForgottenPassword_username")).sendKeys("batman");
		driver.findElement(By.id("ForgottenPassword_0")).click();
		pageTester.testPage(driver, "forgottenPasswordSuccess");
		driver.findElement(By.linkText("Retour")).click();
		pageTester.testPage(driver, "login4");
		
	}

	
}
