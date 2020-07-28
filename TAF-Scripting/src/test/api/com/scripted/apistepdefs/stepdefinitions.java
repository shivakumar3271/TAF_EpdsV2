package com.scripted.apistepdefs;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Listeners;

import com.scripted.api.RequestParams;
import com.scripted.api.RestAssuredWrapper;
import com.scripted.reporting.AllureListener;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.specification.RequestSpecification;

@Listeners({ AllureListener.class })
public class stepdefinitions {
	RequestParams Attwrapper = new RequestParams();
	RestAssuredWrapper raWrapper = new RestAssuredWrapper();

	@Given("I have sample Get API")
	public void i_have_sample_get_api() throws Throwable {

	}

	@When("I send a {string} Request with {string} properties")
	public void i_send_a_something_request_with_something_properties(String strMethod, String strPropFileName)
			throws Throwable {
		raWrapper.setAPIFileProName(strPropFileName + ".properties");
		RequestSpecification reqSpec = raWrapper.CreateRequest(Attwrapper);
		raWrapper.sendRequest(strMethod, reqSpec);
	}

	@Then("I should get reposnse code {int}")
	public void i_should_get_reposnse_code_something(int strCode) throws Throwable {
		raWrapper.valResponseCode(strCode);
	}

	@And("the response should contain:")
	public void the_response_should_contain(DataTable respTable) throws Throwable {
		List<Map<String, String>> resplist = respTable.asMaps(String.class, String.class);
		for (int i = 0; i < resplist.size(); i++) {
			String jsonPath = resplist.get(i).get("JsonPath");
			String expVal = resplist.get(i).get("ExpectedVal");

			if (expVal.matches("-?(0|[1-9]\\d*)")) {
				int intExpVal = Integer.parseInt(expVal);
				raWrapper.valJsonResponseVal(jsonPath, intExpVal);
			} else {
				raWrapper.valJsonResponseVal(jsonPath, expVal);
			}

		}

	}
}
