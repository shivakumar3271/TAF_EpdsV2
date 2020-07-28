package com.scripted.testscripts;

import com.scripted.api.RequestParams;
import com.scripted.api.RestAssuredWrapper;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class SamplePUTApiTest {

	public static void main(String[] args) {		
		RequestParams Attwrapper = new RequestParams();
		RestAssuredWrapper raWrapper = new RestAssuredWrapper();
		Attwrapper.setJsonbody("putApiJsonReq");
		raWrapper.setAPIFileProName("samplePUTApi.properties");
		RequestSpecification reqSpec = raWrapper.CreateRequest(Attwrapper);		
		raWrapper.sendRequest("Put",reqSpec);
		raWrapper.valResponseCode(200);
		raWrapper.valJsonResponseVal("name","morpheus");
		raWrapper.valJsonResponseVal("job","zion resident");
	}
}
