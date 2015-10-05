package com.mrprez.gencross.web.tester;

public class CompareFailure {
	private String expected;
	private String actual;
	private String testName;
	
	
	public CompareFailure(String expected, String actual, String testName) {
		super();
		this.expected = expected;
		this.actual = actual;
		this.testName = testName;
	}
	
	public String getExpected() {
		return expected;
	}
	public void setExpected(String expected) {
		this.expected = expected;
	}
	public String getActual() {
		return actual;
	}
	public void setActual(String actual) {
		this.actual = actual;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	
}
