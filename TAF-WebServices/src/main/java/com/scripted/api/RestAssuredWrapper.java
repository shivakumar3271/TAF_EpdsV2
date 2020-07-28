package com.scripted.api;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

public class RestAssuredWrapper {

	GetProp getPropertiesInMap = new GetProp();
	HashMap<String, Object> propMap = new HashMap<>();
	Map<String, Object> ObjMap = new HashMap<>();

	public String filename = null;
	public static Response apiResponse = null;
	public static String url = null;
	public static String uri = null;
	public static String queryParam = null;
	public static RequestSpecification restSpec = RestAssured.given();
	public static Logger LOGGER = Logger.getLogger(RestAssuredWrapper.class);

	public RequestSpecification CreateRequest(RequestParams raWrapper) {
		try {
			propMap = getPropertiesInMap.getPropValues("src/main/resources/apiproperties/" + filename);
			ObjMap = ConvertObjectToMap(raWrapper);
			boolean isPropMapEmpty = propMap.isEmpty();

			if (isPropMapEmpty == false) {
				for (Iterator<?> it = propMap.entrySet().iterator(); it.hasNext();) {
					HashMap.Entry reqParams = (HashMap.Entry) it.next();
					if (reqParams.getKey().equals("uri")) {
						uri = reqParams.getValue().toString();
					}
					if (reqParams.getKey().equals("url")) {
						url = reqParams.getValue().toString();
					}
					if (reqParams.getKey().equals("proxyAndPort")) {
						if (!reqParams.getValue().toString().isEmpty()) {
							String proxyPort = reqParams.getValue().toString();
							String[] splitproxyPort = proxyPort.split(":");
							int port = Integer.parseInt(splitproxyPort[1]);
							restSpec.proxy(splitproxyPort[0], port);
						}
					}
					if (reqParams.getKey().equals("contenttype")) {
						if (reqParams.getValue().toString().contains("json")) {
							restSpec.contentType(ContentType.JSON);
						}
						if (reqParams.getValue().toString().contains("xml")) {
							restSpec.contentType(ContentType.XML);
						}
					}
					if (reqParams.getKey().equals("accept")) {
						if (reqParams.getValue().toString().contains("json")) {
							restSpec.accept(ContentType.JSON);
						}
						if (reqParams.getValue().toString().contains("xml")) {
							restSpec.accept(ContentType.XML);
						}
					}

					if (reqParams.getKey().equals("restMethodType")) {
						if (reqParams.getValue().toString().equals("POST")
								|| reqParams.getValue().toString().equals("PUT")) {
							restSpec.body(raWrapper.getJsonbody().toString());
						}
					}
				}

			}
			LOGGER.info("Successfully added headers");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to create request :" + e);
			Assert.fail("Error while trying to create request" + e);
		}
		return restSpec;
	}

	public void setAPIFileProName(String fileName) {
		this.filename = fileName;
	}

	public static Map<String, Object> ConvertObjectToMap(RequestParams raWrapper)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> pomclass = raWrapper.getClass();
		pomclass = raWrapper.getClass();
		java.lang.reflect.Method[] methods = raWrapper.getClass().getMethods();

		Map<String, Object> map = new HashMap<String, Object>();
		for (java.lang.reflect.Method m : methods) {
			if (m.getName().startsWith("get") && !m.getName().startsWith("getClass")) {
				Object value = (Object) m.invoke(raWrapper);
				map.put(m.getName().substring(3), (Object) value);
			}
		}
		return map;
	}

	public void sendRequest(String method, RequestSpecification reqSpec) {
		try {
			if (method.contains("Get")) {
				reqSpec.relaxedHTTPSValidation();
				Response response = reqSpec.when().get(uri);
				setReponse(response);
			}
			if (method.contains("Post")) {
				Response response = reqSpec.post(uri);
				setReponse(response);
			}
			if (method.contains("Put")) {
				Response response = reqSpec.put(uri);
				setReponse(response);
			}
			LOGGER.info("Successfully send the request and got the response");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Fail to send the request, Exception :" + e);
			Assert.fail("Fail to send the request, Exception :" + e);
		}

	}

	public void sendGetRequestWithParams(RequestSpecification reqSpec) {
		try {
			reqSpec.relaxedHTTPSValidation();
			Response response = reqSpec.when().get(uri);
			String responseBody = response.getBody().asString();
			setReponse(response);
			LOGGER.info("Successfully send the request and got the response");
		} catch (Exception e) {
			LOGGER.error("Fail to send the request, Exception :" + e);
			Assert.fail("Fail to send the request, Exception :" + e);
		}
	}

	private void setReponse(Response response) {
		this.apiResponse = response;
		LOGGER.info("Response : " + apiResponse.asString());
	}

	public void valResponseCode(int respCode) {
		try {
			int statusCode = apiResponse.getStatusCode();
			System.out.println("Response Code : " + statusCode);
			assertEquals(respCode, statusCode);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Response code, Exception :" + e);
			Assert.fail("Response code, Exception :" + e);
		}
	}

	public void valJsonResponseVal(String jsonPath, Object expValue) {

		try {
			String strVal = null;
			int intVal;
			boolean bVal;
			double dVal;
			JsonPath jsonPathEvaluator = apiResponse.jsonPath();
			Object obj = jsonPathEvaluator.get(jsonPath);
			String getObjType = obj.getClass().getTypeName().toString();
			if (getObjType.contains("String")) {
				strVal = jsonPathEvaluator.get(jsonPath);
				Assert.assertEquals("Mismatch in the response - JsonPath : " + jsonPath, expValue, strVal);
				LOGGER.info("Expected value " + expValue + " is matching with the actual : " + strVal);
			}
			if (getObjType.contains("Integer")) {
				intVal = jsonPathEvaluator.get(jsonPath);
				Assert.assertEquals("Mismatch in the response - JsonPath : " + jsonPath, expValue, intVal);
				LOGGER.info("Expected value " + expValue + " is matching with the actual : " + intVal);
			}
			if (getObjType.contains("Boolean")) {
				bVal = jsonPathEvaluator.get(jsonPath);
				Assert.assertEquals("Mismatch in the response - JsonPath : " + jsonPath, expValue, bVal);
				LOGGER.info("Expected value " + expValue + " is matching with the actual : " + bVal);
			}
			if (getObjType.contains("Double")) {
				dVal = jsonPathEvaluator.get(jsonPath);
				Assert.assertEquals("Mismatch in the response - JsonPath : " + jsonPath, expValue, dVal);
				LOGGER.info("Expected value " + expValue + " is matching with the actual : " + dVal);
			}
		} catch (Exception e) {
			LOGGER.error("Error in validating the response, Exception :" + e);
			Assert.fail("Error in validating the response, Exception :" + e);
		}
	}

	public void valXmlResponseVal(String xmlPath, Object expValue) {
		try {
			String strVal = null;
			int intVal;
			boolean bVal;
			double dVal;
			XmlPath xmlPathEvaluator = apiResponse.xmlPath();
			Object obj = xmlPathEvaluator.get(xmlPath);
			String getObjType = obj.getClass().getTypeName().toString();
			if (getObjType.contains("String")) {
				strVal = xmlPathEvaluator.get(xmlPath);
				Assert.assertEquals("Mismatch in the response - XmlPath : ", expValue, strVal);
				LOGGER.info("Expected value " + expValue + " is matching with the actual : " + strVal);
			}
			if (getObjType.contains("Integer")) {
				intVal = xmlPathEvaluator.get(xmlPath);
				Assert.assertEquals("Mismatch in the response - XmlPath : ", expValue, intVal);
				LOGGER.info("Expected value " + expValue + " is matching with the actual : " + intVal);
			}
			if (getObjType.contains("Boolean")) {
				bVal = xmlPathEvaluator.get(xmlPath);
				Assert.assertEquals("Mismatch in the response - XmlPath : ", expValue, bVal);
				LOGGER.info("Expected value " + expValue + " is matching with the actual : " + bVal);
			}
			if (getObjType.contains("Double")) {
				dVal = xmlPathEvaluator.get(xmlPath);
				Assert.assertEquals("Mismatch in the response - XmlPath : ", expValue, dVal);
				LOGGER.info("Expected value " + expValue + " is matching with the actual : " + dVal);
			}
		} catch (Exception e) {
			LOGGER.error("Error in validating the response, Exception :" + e);
			Assert.fail("Error in validating the response, Exception :" + e);
		}
	}

	public void setGetQueryParams(HashMap<String, String> params) {
		restSpec.params(params);
	}
}
