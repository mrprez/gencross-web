package com.mrprez.gencross.web.tester;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;

public abstract class TemplateTester {
	private final static String REGEXP_PREFFIX = "REGEXP:";
	protected final static String FAIL_SUFFIX = "_FAIL";
	
	protected Map<String, String> replacementRules = new HashMap<String, String>();
	protected File maskRepository;
	protected File workDir;
	private boolean allTemplatePresent = true;
	private boolean ignoreWhiteSpace = false;
		
	
	public TemplateTester(String maskGroup, String maskGroupRepositoryPath, String workDirPath){
		super();
		maskRepository = new File(maskGroupRepositoryPath, maskGroup);
		
		new File(workDirPath).mkdir();
		workDir = new File(workDirPath, maskGroup);
		workDir.mkdir();
	}
	
	
	protected void test(String source, String testName, String extension) throws IOException, InterruptedException {
		System.out.println("Template file="+maskRepository+"/"+testName+extension);
		File template = new File(maskRepository, testName+extension);
		
		try{
			source = formatSource(source);
		}catch(IOException ioe){
			writeFailFile(source, testName, extension);
			throw ioe;
		}
		
		if (!template.exists()) {
			allTemplatePresent = false;
			writeTemplate(source, testName, extension);
			return;
		}
		
		writeFailFile(source, testName, extension);
		BufferedReader templateReader = new BufferedReader(new InputStreamReader(new FileInputStream(template), "UTF-8"));
		BufferedReader sourceReader = new BufferedReader(new StringReader(source));
		try {
			String sourceLine;
			String templateLine;
			while ((sourceLine = getNextLine(sourceReader)) != null
					&& (templateLine = getNextLine(templateReader)) != null) {
				if (templateLine.startsWith(REGEXP_PREFFIX)) {
					templateLine = templateLine.substring(REGEXP_PREFFIX.length());
					if(ignoreWhiteSpace){
						templateLine = templateLine.trim();
					}
					Assert.assertTrue("\"" + sourceLine + "\"\n doesn't match:\n\"" + templateLine + "\"",
							sourceLine.matches(templateLine));
				} else {
					Assert.assertEquals(templateLine.trim(), sourceLine.trim());
				}
			}
			if (sourceReader.readLine() != null) {
				Assert.fail("Source plus longue que le template");
			}
			if (templateReader.readLine() != null) {
				Assert.fail("Template plus long que la source");
			}
			deleteFailFile(testName, extension);
		} finally {
			templateReader.close();
			sourceReader.close();
		}
	}
	
	protected String formatSource(String source) throws IOException{
		BufferedReader sourceReader = new BufferedReader(new StringReader(source));
		StringWriter stringWriter = new StringWriter();
		BufferedWriter writer = new BufferedWriter(stringWriter);
		try{
			String sourceLine;
			while ((sourceLine = getNextLine(sourceReader)) != null){
				for (String pattern : replacementRules.keySet()) {
					sourceLine = sourceLine.replaceAll(pattern, replacementRules.get(pattern));
				}
				if(ignoreWhiteSpace){
					sourceLine = sourceLine.trim();
				}
				writer.write(sourceLine);
				writer.newLine();
			}
		}finally{
			sourceReader.close();
			writer.close();
		}
		return stringWriter.toString();
	}
	
	private String getNextLine(BufferedReader reader) throws IOException{
		if(!ignoreWhiteSpace){
			return reader.readLine();
		}
		
		String line;
		do{
			line = StringUtils.trim(reader.readLine());
		}while(line!=null && StringUtils.isBlank(line));
		
		return line;
	}
	
	protected void deleteFailFile(String testName, String extension) {
		new File(workDir, testName + FAIL_SUFFIX + extension).delete();
	}
	
	protected void writeFailFile(String source, String testName, String extension) throws IOException, InterruptedException {
		deleteFailFile(testName, extension);
		File failFile = new File(workDir, testName + FAIL_SUFFIX + extension);
		Writer writer = new OutputStreamWriter(new FileOutputStream(failFile),"UTF-8");
		try {
			writer.write(source);
		} finally {
			writer.close();
		}
		Assert.assertTrue(failFile.exists());
	}

	protected void writeTemplate(String source, String testName, String extension) throws IOException {
		File template = new File(workDir, testName+extension);
		Writer writer = new OutputStreamWriter(new FileOutputStream(template), "UTF-8");
		for (String pattern : replacementRules.keySet()) {
			source = source.replaceAll(pattern, replacementRules.get(pattern));
		}
		try {
			writer.write(source);
		} finally {
			writer.close();
		}
	}

	public boolean isAllTemplatePresent() {
		return allTemplatePresent;
	}

	public File getMaskRepository() {
		return maskRepository;
	}


	public File getWorkDir() {
		return workDir;
	}

	public void addReplacementRule(String regex, String replacement){
		replacementRules.put(regex, replacement);
	}

	public boolean isIgnoreWhiteSpace() {
		return ignoreWhiteSpace;
	}

	public void setIgnoreWhiteSpace(boolean ignoreWhiteSpace) {
		this.ignoreWhiteSpace = ignoreWhiteSpace;
	}
	

}
