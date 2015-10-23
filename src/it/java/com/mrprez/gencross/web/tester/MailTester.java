package com.mrprez.gencross.web.tester;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MailTester extends TemplateTester {
	private static final String MAIL_FILE_EXTENSION = ".txt";
	
	private File mailFile;
	

	public MailTester(File maskRepository, File workDir, String mailPath) {
		super(maskRepository, workDir);
		mailFile = new File(mailPath);
		super.addReplacementRule("[0-9]{2}:[0-9]{2}:[0-9]{2} [0-9]{2}/[0-9]{2}/[0-9]{4}", "00:00:00 00/00/0000");
	}
	
	public void test(String testName) throws IOException, InterruptedException {
		super.test(readMailFile(), testName, MAIL_FILE_EXTENSION);
	}
	
	private String readMailFile() throws IOException{
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(mailFile));
		StringBuilder sb = new StringBuilder((int) mailFile.length());
		try{
			int i;
			while((i=is.read()) >= 0){
				sb.append((char)i);
			}
		}finally{
			is.close();
		}
		return sb.toString();
	}

	public File getMailFile() {
		return mailFile;
	}
	
	public void deleteMailFile(){
		if(mailFile.exists()){
			mailFile.delete();
		}
	}
	
	

}
