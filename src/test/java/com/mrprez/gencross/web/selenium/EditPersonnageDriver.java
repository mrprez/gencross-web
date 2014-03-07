package com.mrprez.gencross.web.selenium;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EditPersonnageDriver implements WebDriver {
	private WebDriver driver;
	private long waitTime;
	private int maxRetry;
	
	
	public EditPersonnageDriver(WebDriver driver, long waitTime, int maxRetry){
		super();
		this.driver = driver;
		this.waitTime = waitTime;
		this.maxRetry = maxRetry;
	}
	
	@Override
	public WebElement findElement(By by) throws NoSuchElementException {
		NoSuchElementException exception = null;
		for(int i=0; i<=maxRetry; i++) {
			try {
				return driver.findElement(by);
			} catch (NoSuchElementException nsee) {
				exception = nsee;
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
		throw exception;
	}

	@Override
	public void get(String url) {
		driver.get(url);
	}

	@Override
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	@Override
	public String getTitle() {
		return driver.getTitle();
	}

	@Override
	public List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	@Override
	public String getPageSource() {
		return driver.getPageSource();
	}

	@Override
	public void close() {
		driver.close();
	}

	@Override
	public void quit() {
		driver.quit();
	}

	@Override
	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	@Override
	public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	@Override
	public TargetLocator switchTo() {
		return driver.switchTo();
	}

	@Override
	public Navigation navigate() {
		return driver.navigate();
	}

	@Override
	public Options manage() {
		return driver.manage();
	}
	
	

}
