package com.mrprez.gencross.web.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebDriverProxy implements WebDriver {
	
	private final WebDriver webDriver;
	private long waitTime = 500;
	
	
	public WebDriverProxy(WebDriver webDriver) {
		super();
		this.webDriver = webDriver;
	}
	
	public WebDriverProxy(WebDriver webDriver, long waitTime) {
		super();
		this.webDriver = webDriver;
		this.waitTime = waitTime;
	}


	public void get(String url) {
		webDriver.get(url);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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
			result.add(new WebElementProxy(foundElement, waitTime));
		}
		return result;
	}


	public WebElement findElement(By by) {
		return new WebElementProxy(webDriver.findElement(by), waitTime);
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


	
	
//	@Override
//	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		if(method.getName().equals("findElement")){
//			WebElementProxy webElementProxy = new WebElementProxy((WebElement) method.invoke(webDriver, args));
//			return Proxy.newProxyInstance(WebElement.class.getClassLoader(), new Class[] { WebElement.class }, webElementProxy);
//		}
//		
//		if(method.getName().equals("findElements")){
//			List<?> webElements = (List<?>) method.invoke(webDriver, args);
//			List<WebElement> result = new ArrayList<WebElement>(webElements.size());
//			for(Object webElement : webElements){
//				WebElementProxy webElementProxy = new WebElementProxy((WebElement) webElement);
//				result.add((WebElement) Proxy.newProxyInstance(WebElement.class.getClassLoader(), new Class[] { WebElement.class }, webElementProxy));
//			}
//			return result;
//		}
//		
//		if(method.getName().equals("get")){
//			method.invoke(webDriver, args);
//			Thread.sleep(2000);
//		}
//		
//		return method.invoke(webDriver, args);
//	}
	

}
