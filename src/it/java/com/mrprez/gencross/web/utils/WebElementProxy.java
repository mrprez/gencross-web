package com.mrprez.gencross.web.utils;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class WebElementProxy implements WebElement {
	
	private final WebElement webElement;
	private WebDriverProxy webDriverProxy;
	

	public WebElementProxy(WebElement webElement, WebDriverProxy webDriverProxy) {
		super();
		this.webElement = webElement;
		this.webDriverProxy = webDriverProxy;
	}
	
	


	public WebElement getWebElement() {
		return webElement;
	}


	public void click() {
		webElement.click();
		webDriverProxy.waitLoading();
	}


	public void submit() {
		webElement.submit();
		webDriverProxy.waitLoading();
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
			result.add(new WebElementProxy(foundElement, webDriverProxy));
		}
		return result;
	}





	public WebElement findElement(By by) {
		return new WebElementProxy(webElement.findElement(by), webDriverProxy);
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




	@Override
	public <X> X getScreenshotAs(OutputType<X> arg0) throws WebDriverException {
		return webElement.getScreenshotAs(arg0);
	}




	@Override
	public Rectangle getRect() {
		return webElement.getRect();
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
