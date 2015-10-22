package com.mrprez.gencross.web.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

public class WebElementProxy implements WebElement {
	
	private final WebElement webElement;
	private long waitTime;
	

	public WebElementProxy(WebElement webElement, long waitTime) {
		super();
		this.webElement = webElement;
		this.waitTime = waitTime;
	}
	
	
	


	public WebElement getWebElement() {
		return webElement;
	}


	public long getWaitTime() {
		return waitTime;
	}


	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}





	public void click() {
		webElement.click();
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}





	public void submit() {
		webElement.submit();
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}





	public void sendKeys(CharSequence... keysToSend) {
		webElement.sendKeys(keysToSend);
	}





	public void clear() {
		webElement.clear();
	}





	public String getTagName() {
		return webElement.getTagName();
	}





	public String getAttribute(String name) {
		return webElement.getAttribute(name);
	}





	public boolean isSelected() {
		return webElement.isSelected();
	}





	public boolean isEnabled() {
		return webElement.isEnabled();
	}





	public String getText() {
		return webElement.getText();
	}





	public List<WebElement> findElements(By by) {
		List<WebElement> result = new ArrayList<WebElement>();
		for(WebElement foundElement : webElement.findElements(by)){
			result.add(new WebElementProxy(foundElement, waitTime));
		}
		return result;
	}





	public WebElement findElement(By by) {
		return new WebElementProxy(webElement.findElement(by), waitTime);
	}





	public boolean isDisplayed() {
		return webElement.isDisplayed();
	}





	public Point getLocation() {
		return webElement.getLocation();
	}





	public Dimension getSize() {
		return webElement.getSize();
	}





	public String getCssValue(String propertyName) {
		return webElement.getCssValue(propertyName);
	}

//	@Override
//	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		if(method.getName().equals("click")){
//			Thread.sleep(2000);
//			webElement.click();
//			return null;
//		}else{
//			return method.invoke(webElement, args);
//		}
//	}

}
