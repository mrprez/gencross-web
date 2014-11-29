package com.mrprez.gencross.web.tester;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

public class PageTester extends TemplateTester {
	private static String TIMEOUT_SUFFIX = "_TIMEOUT";
	private static String HTML_EXTENSION = ".htm";
	
	private List<ExpectedCondition<Boolean>> waitConditionList = new ArrayList<ExpectedCondition<Boolean>>();
	private long sleepTime = 1000;
	
	
	public PageTester(String maskGroup, String maskGroupRepositoryPath, String workDirPath) {
		super(maskGroup, maskGroupRepositoryPath, workDirPath);
	}


	public void testPage(WebDriver driver, String testName) throws IOException, InterruptedException, DocumentException {
		testPage(driver, testName, sleepTime);
	}

	public void testPage(WebDriver driver, String testName, long sleepTime) throws IOException, InterruptedException, DocumentException {
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


	@Override
	protected String formatSource(String source) throws IOException {
		try {
			StringReader sourceReader = new StringReader(source);
			StringWriter tidyWriter = new StringWriter();
			Tidy tidy = new Tidy();
			tidy.setXHTML(true);
			Document document = tidy.parseDOM(sourceReader, tidyWriter);
			
			StringWriter stringWriter = new StringWriter();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
			
			return super.formatSource(stringWriter.toString());
			
		} catch (TransformerException e) {
			throw new IOException(e);
		}
	}
	
	
	

}
