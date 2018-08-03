package com.ubddpd.utils;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class JiraClient {
	
	private PreemptiveBasicAuthScheme authScheme;
	
	public JiraClient() {
		RestAssured.registerParser("text/html", Parser.JSON);
	}
	
	public void setHost(String host) {
		RestAssured.baseURI = host;
	}
	
	public void setAuthentication(String username, String password) {
		authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName(username);
        authScheme.setPassword(password);
        RestAssured.authentication = authScheme;
	}

    public static void main(String[] args){
        JiraClient client = new JiraClient();
        client.setHost("https://pramerica-jira.prudential.com");
         client.setAuthentication("", "");
        Map<String, List<String>> tests = client.getTestsFromSet("AGENTS-706");
        System.out.println("Completed");
    }

    public Map<String, List<String>> getTestsFromSet(String testSetId) {
    	Map<String, List<String>> result = new HashMap<String, List<String>>();
        Response restResponse = given().get("/rest/raven/1.0/api/testset/" + testSetId + "/test");
        List<Map<String, Object>> tests = restResponse.getBody().as(List.class);
        for(Map<String, Object> test : tests) {
        	String testId = (String)test.get("key");
        	String testDescription = getTestDescription(testId);
        	String descriptionParts[] = testDescription.split("\n");
        	String featureName = descriptionParts[0];
        	List<String> scenarioList = null;
        	if(result.containsKey(featureName)) {
        		scenarioList = (List<String>)result.get(featureName);
        	} else {
        		scenarioList = new ArrayList<String>();
        	}
        	scenarioList.add(descriptionParts[1]);
        }
        return result;
    }

    private String getTestDescription(String testId) {
        Response restResponse = given().get("/rest/api/2/issue/" + testId);
        Map response = restResponse.getBody().as(Map.class);
        Map responseFields = (Map)response.get("fields");
        return (String)responseFields.get("description");
    }
}
