package com.utilities.epds;

// This class is used to hold the actual/ expected result values for reporting.
public class ResultTuple {

	public String fieldName;
	public String actual;
	public String expected;
	public String result;
	public String comments;

	public ResultTuple(String fieldName, String actual, String expected, String result, String comments) {

		this.fieldName = fieldName;
		this.actual = actual;
		this.expected = expected;
		this.result = result;
		this.comments = comments;
	}
}
