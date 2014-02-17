package com.mrprez.gencross.web;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;

public class UserTest extends WebAbstractTest {
	

	public UserTest() throws IOException {
		super("User");
	}

	@Test
	public void testUser() throws Exception {
		driver.get(baseUrl + context);
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("batman");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("robin");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "loginFail1");
		driver.findElement(By.linkText("S'inscrire")).click();
		driver.findElement(By.id("subscriptionForm_username")).clear();
		driver.findElement(By.id("subscriptionForm_username")).sendKeys("batman");
		driver.findElement(By.id("subscriptionForm_password")).clear();
		driver.findElement(By.id("subscriptionForm_password")).sendKeys("robin");
		driver.findElement(By.id("subscriptionForm_confirmPassword")).clear();
		driver.findElement(By.id("subscriptionForm_confirmPassword")).sendKeys("robin");
		driver.findElement(By.id("subscriptionForm_mail")).clear();
		driver.findElement(By.id("subscriptionForm_mail")).sendKeys("batman@got.us");
		driver.findElement(By.id("subscriptionForm_0")).click();
		pageTester.testPage(driver, "subscriptionSuccess");
		driver.findElement(By.cssSelector("#accountManagementMenu > span.menu")).click();
		driver.findElement(By.linkText("Changer son mot de passe.")).click();
		driver.get(baseUrl+context+"/ChangePassword.action");
		pageTester.testPage(driver, "changePassword");
		driver.findElement(By.id("ChangePassword!changePassword_password")).clear();
		driver.findElement(By.id("ChangePassword!changePassword_password")).sendKeys("catwoman");
		driver.findElement(By.id("ChangePassword!changePassword_newPassword")).clear();
		driver.findElement(By.id("ChangePassword!changePassword_newPassword")).sendKeys("alfred");
		driver.findElement(By.id("ChangePassword!changePassword_confirm")).clear();
		driver.findElement(By.id("ChangePassword!changePassword_confirm")).sendKeys("alfred");
		driver.findElement(By.id("ChangePassword!changePassword_0")).click();
		pageTester.testPage(driver, "changePasswordFail1");
		driver.findElement(By.id("ChangePassword!changePassword_password")).clear();
		driver.findElement(By.id("ChangePassword!changePassword_password")).sendKeys("robin");
		driver.findElement(By.id("ChangePassword!changePassword_newPassword")).clear();
		driver.findElement(By.id("ChangePassword!changePassword_newPassword")).sendKeys("alfred");
		driver.findElement(By.id("ChangePassword!changePassword_confirm")).clear();
		driver.findElement(By.id("ChangePassword!changePassword_confirm")).sendKeys("catwoman");
		driver.findElement(By.id("ChangePassword!changePassword_0")).click();
		pageTester.testPage(driver, "changePasswordFail2");
		driver.findElement(By.id("ChangePassword!changePassword_password")).clear();
		driver.findElement(By.id("ChangePassword!changePassword_password")).sendKeys("robin");
		driver.findElement(By.id("ChangePassword!changePassword_newPassword")).clear();
		driver.findElement(By.id("ChangePassword!changePassword_newPassword")).sendKeys("alfred");
		driver.findElement(By.id("ChangePassword!changePassword_confirm")).clear();
		driver.findElement(By.id("ChangePassword!changePassword_confirm")).sendKeys("alfred");
		driver.findElement(By.id("ChangePassword!changePassword_0")).click();
		pageTester.testPage(driver, "changePasswordSuccess");
		driver.findElement(By.cssSelector("img")).click();
		pageTester.testPage(driver, "welcomePage");
		driver.findElement(By.id("disconnect")).click();
		pageTester.testPage(driver, "disconnect");
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("batman");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("robin");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "loginFail2");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("alfred");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "loginSuccess1");
		driver.findElement(By.cssSelector("#accountManagementMenu > span.menu")).click();
		driver.get(baseUrl+context+"/ChangeMail.action");
		pageTester.testPage(driver, "changeMailAddress1");
		driver.findElement(By.id("disconnect")).click();
		pageTester.testPage(driver, "disconnect");
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("mrprez");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("hlttclm");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "loginSuccess2");
		driver.findElement(By.cssSelector("#administrationMenu > span.menu")).click();
		driver.get(baseUrl+context+"/ListUser.action");
		pageTester.testPage(driver, "userList1");
		driver.findElement(By.id("disconnect")).click();
		pageTester.testPage(driver, "disconnect");
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("batman");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("alfred");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "loginSuccess3");
		driver.findElement(By.cssSelector("#accountManagementMenu > span.menu")).click();
		driver.get(baseUrl + context + "/ChangeMail.action");
		pageTester.testPage(driver, "changeMailAddress2", 2000);
		driver.findElement(By.id("ChangeMail!changeMail_mail")).clear();
		driver.findElement(By.id("ChangeMail!changeMail_mail")).sendKeys("batman.gotham.us");
		pageTester.testPage(driver, "changeMailAddressFail");
		driver.findElement(By.id("ChangeMail!changeMail_0")).click();
		driver.findElement(By.id("ChangeMail!changeMail_mail")).clear();
		driver.findElement(By.id("ChangeMail!changeMail_mail")).sendKeys("batman@gotham.us");
		driver.findElement(By.id("ChangeMail!changeMail_0")).click();
		pageTester.testPage(driver, "changeMailAddressSuccess");
		driver.findElement(By.id("disconnect")).click();
		pageTester.testPage(driver, "disconnect");
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("mrprez");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys("hlttclm");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "loginSuccess4");
		driver.findElement(By.cssSelector("#administrationMenu > span.menu")).click();
		driver.findElement(By.linkText("Liste des utilisateurs")).click();
		driver.get(baseUrl+context+"/ListUser.action");
		pageTester.testPage(driver, "userList2");
		driver.findElement(By.id("disconnect")).click();
		pageTester.testPage(driver, "disconnect");
		
	}

	
}
