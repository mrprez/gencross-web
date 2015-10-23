package com.mrprez.gencross.web.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverProxy implements WebDriver {
	
	private final WebDriver webDriver;
	private long waitTime = 500;
	private List<ExpectedCondition<Boolean>> waitConditionList = new ArrayList<ExpectedCondition<Boolean>>();
	
	
	
	public WebDriverProxy(WebDriver webDriver) {
		super();
		this.webDriver = webDriver;
	}
	
	public WebDriverProxy(WebDriver webDriver, long waitTime) {
		super();
		this.webDriver = webDriver;
		this.waitTime = waitTime;
	}
	
	
	public void waitLoading() {
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try{
			for (ExpectedCondition<Boolean> condition : waitConditionList) {
				(new WebDriverWait(webDriver, 60, waitTime)).until(condition);
			}
		}catch(UnhandledAlertException unhandledAlertException){
			System.out.println("Alert open: "+unhandledAlertException.getAlertText());
		}
	}
	
	
	public void addWaitCondition(ExpectedCondition<Boolean> waitCondition) {
		waitConditionList.add(waitCondition);
	}


	public void get(String url) {
		webDriver.get(url);
		waitLoading();
	}
	
	
	public String getCurrentUrl() {
		return webDriver.getCurrentUrl();
	}


	public String getTitle() {
		return webDriver.getTitle();
	}


	public List<WebElement> findElements(By by) {
		List<WebElement> result = new ArrayList<WebElement>();
		for(WebElement foundElement : webDriver.findElements(by)){
			result.add(new WebElementProxy(foundElement, this));
		}
		return result;
	}


	public WebElement findElement(By by) {
		return new WebElementProxy(webDriver.findElement(by), this);
	}


	public String getPageSource() {
		return webDriver.getPageSource();
	}


	public void close() {
		webDriver.close();
	}


	public void quit() {
		webDriver.quit();
	}


	public Set<String> getWindowHandles() {
		return webDriver.getWindowHandles();
	}


	public String getWindowHandle() {
		return webDriver.getWindowHandle();
	}


	public TargetLocator switchTo() {
		return webDriver.switchTo();
	}


	public Navigation navigate() {
		return webDriver.navigate();
	}


	public Options manage() {
		return webDriver.manage();
	}


	public long getWaitTime() {
		return waitTime;
	}


	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}


	public WebDriver getWebDriver() {
		return webDriver;
	}

	

}
