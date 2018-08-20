package com.ubddpd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class EmployeeServiceClient {

    Logger resultsLogger = LogManager.getLogger("resultsLogger");

    public void setHost(String host){
        RestAssured.baseURI = host;
    }

    public List<Employee> getAllEmployees() throws Exception {
        Response employeeResponse = RestAssured.given().get("/api/empdb/employees");
        ResponseBody responseBody = employeeResponse.getBody();
        String responseStr = responseBody.asString();
        resultsLogger.info("Employee Service Returned Response: "+ responseStr);
        Map<String, List<Employee>> responseMap = new ObjectMapper().readValue(responseStr,
                new TypeReference<Map<String, List<Employee>>>(){});
        return responseMap.get("emps");
    }

    public List<Employee> getEmployeesByName(String name) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("employeeName", name);
        Response employeeResponse = RestAssured.given().params(params).get("/api/empdb/employee");
        ResponseBody responseBody = employeeResponse.getBody();
        String responseStr = responseBody.asString();
        resultsLogger.info("Employee Service Returned Response: "+ responseStr);
        Map<String, List<Employee>> responseMap = new ObjectMapper().readValue(responseStr,
                new TypeReference<Map<String, List<Employee>>>(){});
        return responseMap.get("emp");
    }
}
