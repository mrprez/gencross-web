package com.mrprez.gencross.web.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class StreamProcessManager extends Thread {
	private InputStream inputStream;
	private PrintStream printStream;
	private String header;
	private Boolean ready;
	private String readyPattern;

	
	public StreamProcessManager(InputStream inputStream, PrintStream printStream, String header) {
		super();
		this.inputStream = inputStream;
		this.printStream = printStream;
		this.header = header;
	}
	

	public void run() {
		try {
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = reader.readLine()) != null) {
				printStream.println(header + " " + line);
				if(readyPattern!=null &&  ! ready.booleanValue()){
					if(line.matches(readyPattern)){
						ready = true;
					}
				}
				
			}
			printStream.println(header + " ** termination **");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setReadyPattern(String readyPattern){
		ready = false;
		this.readyPattern = readyPattern;
	}
	
	public Boolean idReady(){
		return ready;
	}
}
