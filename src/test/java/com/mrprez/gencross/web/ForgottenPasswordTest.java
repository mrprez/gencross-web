package com.mrprez.gencross.web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;

public class ForgottenPasswordTest extends WebAbstractTest {
	private static final String passwordMailLinePrefix = "Votre nouveau mot de passe: ";
	
	
	public ForgottenPasswordTest() throws IOException {
		super("ForgottenPassword");
		mailTester.addReplacementRule("Votre nouveau mot de passe: .{8}", "Votre nouveau mot de passe: XXXXXXXX");
	}

	@Test
	public void testForgottenPassword() throws Exception {
		driver.get(baseUrl + "gencross-web/List.action");
		pageTester.testPage(driver, "Login1");
		driver.findElement(By.linkText("Mot de passe oublié")).click();
		pageTester.testPage(driver, "ForgottenPassword");
		driver.findElement(By.id("ForgottenPassword_username")).clear();
		driver.findElement(By.id("ForgottenPassword_username")).sendKeys("azerty");
		driver.findElement(By.id("ForgottenPassword_0")).click();
		pageTester.testPage(driver, "ForgottenPasswordSuccess");
		mailTester.test("mail");
		String password = extractNewPassword();
		driver.findElement(By.linkText("Retour")).click();
		pageTester.testPage(driver, "Login2");
		driver.findElement(By.id("Login_password")).clear();
		driver.findElement(By.id("Login_password")).sendKeys(password);
		driver.findElement(By.id("usernameField")).clear();
		driver.findElement(By.id("usernameField")).sendKeys("azerty");
		driver.findElement(By.id("Login_0")).click();
		pageTester.testPage(driver, "Welcome");
		
	}
	
	private String extractNewPassword() throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(mailTester.getMailFile()));
		try{
			String line;
			while((line=reader.readLine()) != null){
				if(line.startsWith(passwordMailLinePrefix)){
					return line.substring(passwordMailLinePrefix.length());
				}
			}
			return null;
		}finally{
			reader.close();
		}
		
	}
	
	
}
