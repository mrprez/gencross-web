package com.mrprez.gencross.web.tester;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageTester extends TemplateTester {
	private static String TIMEOUT_SUFFIX = "_TIMEOUT";
	private static String HTML_EXTENSION = ".htm";
	
	private List<ExpectedCondition<Boolean>> waitConditionList = new ArrayList<ExpectedCondition<Boolean>>();
	private long sleepTime = 300;

	
	public PageTester(String maskGroup, String maskGroupRepositoryPath, String workDirPath) {
		super(maskGroup, maskGroupRepositoryPath, workDirPath);
	}


	public void testPage(WebDriver driver, String testName) throws IOException, InterruptedException {
		testPage(driver, testName, sleepTime);
	}

	public void testPage(WebDriver driver, String testName, long sleepTime) throws IOException, InterruptedException {
		Thread.sleep(sleepTime);
		try {
			for (ExpectedCondition<Boolean> condition : waitConditionList) {
				(new WebDriverWait(driver, 30)).until(condition);
			}
		} catch (TimeoutException te) {
			writeTimeoutTest(driver.getPageSource(), testName);
			throw te;
		}
		test(driver.getPageSource(), testName, HTML_EXTENSION);
	}

	

	private void writeTimeoutTest(String source, String testName) throws IOException {
		File failFile = new File(workDir, testName + TIMEOUT_SUFFIX + HTML_EXTENSION);
		Writer writer = new OutputStreamWriter(new FileOutputStream(failFile), "UTF-8");
		try {
			writer.write(source);
		} finally {
			writer.close();
		}
		Assert.assertTrue(failFile.exists());
	}

	public void addWaitCondition(ExpectedCondition<Boolean> waitCondition) {
		waitConditionList.add(waitCondition);
	}

}
