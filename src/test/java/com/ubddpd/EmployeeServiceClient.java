package com.ubddpd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubddpd.tests.Setup;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.*;

public class EmployeeServiceClient {

    Logger resultsLogger = LogManager.getLogger("resultsLogger");

    public void setHost(String host){
        RestAssured.baseURI = host;
    }

    public List<Employee> getAllEmployees() throws Exception {
        setHost((String)Setup.testConfig.get("webservice_host_url"));
        Response employeeResponse = RestAssured.given().get("/api/empdb/employees");
        ResponseBody responseBody = employeeResponse.getBody();
        String responseStr = responseBody.asString();
        resultsLogger.info("Employee Service Returned Response: "+ responseStr);
        Map<String, List<Employee>> responseMap = new ObjectMapper().readValue(responseStr,
                new TypeReference<Map<String, List<Employee>>>(){});
        return responseMap.get("emps");
    }

    public List<Employee> getEmployeesByName(String key1, String val1) throws Exception {
        setHost((String)Setup.testConfig.get("webservice_host_url"));
        Map<String, String> params = new HashMap<>();

        FileInputStream fis = new FileInputStream(Setup.RESOURCE_PATH + "/testdata/Employees/Employees.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        fis.close();
        XSSFSheet worksheet = workbook.getSheet("EmployeeService");
        int numRows = worksheet.getPhysicalNumberOfRows();
        boolean sectionBegan = false;
        for(int i=0; i<numRows; i++){
            XSSFRow row = worksheet.getRow(i);
            if(row==null){
                sectionBegan = false;
                continue;
            }

            XSSFCell keyCell = row.getCell(0);
            XSSFCell valueCell = row.getCell(1);
            if(keyCell == null || valueCell==null){
                sectionBegan = false;
                continue;
            }

            String key = keyCell.getStringCellValue();
            String value = valueCell.getStringCellValue();
            if(key.equals("Service") && value.equals("getEmployeeByName")){
                sectionBegan = true;
                continue;
            }
            if(sectionBegan){
                if(key == null || key.trim().equals("")){
                    sectionBegan = false;
                    continue;
                }
                else{
                    String valueTxt = value;
                    if(key1 != null && valueTxt.equals(key1)){
                        valueTxt = valueTxt.replaceAll("\\[", "\\\\[");
                        valueTxt = valueTxt.replaceAll("\\]", "\\\\]");
                        value = value.replaceAll(valueTxt, val1);
                    }
                }
            }
            params.put(key, value);
        }

        String path = params.remove("Path");
        Response employeeResponse = RestAssured.given().params(params).get(path);
        ResponseBody responseBody = employeeResponse.getBody();
        String responseStr = responseBody.asString();
        resultsLogger.info("Employee Service Returned Response: "+ responseStr);
        Map<String, List<Employee>> responseMap = new ObjectMapper().readValue(responseStr,
                new TypeReference<Map<String, List<Employee>>>(){});
        return responseMap.get("emp");
    }
}
