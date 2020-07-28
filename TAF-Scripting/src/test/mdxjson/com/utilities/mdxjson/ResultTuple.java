package com.utilities.mdxjson;

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

	public int hashCode() {
		int hashcode = 0;
		hashcode = fieldName.hashCode();
		hashcode += actual.hashCode();
		hashcode += expected.hashCode();
		hashcode += result.hashCode();
		hashcode += comments.hashCode();
		return hashcode;
	}

	public boolean equals(Object obj) {
		System.out.println("In equals");
		if (obj instanceof ResultTuple) {
			ResultTuple rt = (ResultTuple) obj;
			return (rt.fieldName.equals(this.fieldName) && rt.actual.equals(this.actual)
					&& rt.expected.equals(this.expected) && rt.result.equals(this.result)
					&& rt.comments.equals(this.comments));
		} else {
			return false;
		}
	}
}
