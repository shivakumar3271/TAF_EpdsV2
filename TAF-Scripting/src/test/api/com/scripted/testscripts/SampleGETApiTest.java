package com.scripted.testscripts;

import com.scripted.api.RequestParams;
import com.scripted.api.RestAssuredWrapper;

import io.restassured.specification.RequestSpecification;

public class SampleGETApiTest {

	public static void main(String[] args) {		
		RequestParams Attwrapper = new RequestParams();
		RestAssuredWrapper raWrapper = new RestAssuredWrapper();
		raWrapper.setAPIFileProName("sampleGETApi.properties");
		RequestSpecification reqSpec = raWrapper.CreateRequest(Attwrapper);
		raWrapper.sendRequest("Get",reqSpec);
		raWrapper.valResponseCode(200);
		raWrapper.valJsonResponseVal("data.id",2);
		raWrapper.valJsonResponseVal("data.email","janet.weaver@reqres.in");
		raWrapper.valJsonResponseVal("data.first_name","Janet");
		raWrapper.valJsonResponseVal("data.last_name","Weaver");
		raWrapper.valJsonResponseVal("data.avatar","https://s3.amazonaws.com/uifaces/faces/twitter/josephstein/128.jpg");		
	}
}
