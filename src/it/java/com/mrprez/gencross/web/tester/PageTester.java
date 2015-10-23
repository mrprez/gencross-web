package com.mrprez.gencross.web.tester;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.openqa.selenium.WebDriver;
import org.w3c.tidy.Tidy;

public class PageTester extends TemplateTester {
	private static String HTML_EXTENSION = ".htm";
	
	
	public PageTester(File maskRepository, File workDir) {
		super(maskRepository, workDir);
	}


	
	public void testPage(WebDriver driver, String testName) throws IOException, InterruptedException, DocumentException {
		test(driver.getPageSource(), testName, HTML_EXTENSION);
	}

	

	@Override
	protected String formatSource(String source) throws IOException {
		try {
			String text = source.replaceAll("[&]lt[;]", "<");
			text = text.replaceAll("[&]gt[;]", ">");
			
			Tidy tidy = new Tidy();
			tidy.setXHTML(true);
			tidy.setQuoteAmpersand(false);
			org.w3c.dom.Document untidyDocument = tidy.parseDOM(new StringReader(text), new StringWriter());
			
			StringWriter stringWriter = new StringWriter();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.METHOD, "xhtml");
			transformer.transform(new DOMSource(untidyDocument), new StreamResult(stringWriter));
			
			Document document = DocumentHelper.parseText(stringWriter.toString().replaceAll("[>][ ]+", ">"));
			StringWriter compactStringWriter = new StringWriter();
			XMLWriter compactXmlWriter = new XMLWriter(compactStringWriter, OutputFormat.createCompactFormat());
			compactXmlWriter.write(document);
			
			document = DocumentHelper.parseText(compactStringWriter.toString());
			StringWriter prettyStringWriter = new StringWriter();
			XMLWriter prettyXmlWriter = new XMLWriter(prettyStringWriter, new OutputFormat("\t", true, "UTF-8"));
			prettyXmlWriter.write(document);
			
			return super.formatSource(prettyStringWriter.toString());
			
		} catch (TransformerException e) {
			throw new IOException(e);
		} catch (DocumentException e) {
			throw new IOException(e);
		}
	}
	
	
	
	

}
