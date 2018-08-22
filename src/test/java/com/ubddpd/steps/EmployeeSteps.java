package com.ubddpd.steps;

import com.ubddpd.Employee;
import com.ubddpd.EmployeeServiceClient;
import com.ubddpd.EmployeesPage;
import com.ubddpd.HomePage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;

public class EmployeeSteps {

	HomePage homePage = new HomePage();
	EmployeesPage employeesPage = new EmployeesPage();
	EmployeeServiceClient employeeService = new EmployeeServiceClient();
	Logger resultsLogger = LogManager.getLogger("resultsLogger");
	
	@Given("^I open the Employees site$")
	public void loginToWebServer() throws Throwable {
		homePage.open("webserver_home_url");
	}

	@Then("^the page heading should be '(.*)'$")
	public void validatePageHeading(String expected){
		String actual = homePage.getHeading();
		Assert.assertEquals("Heading was not as expected", expected, actual);
	}

	@Given("^I click on the link to View All Employees$")
	public void clickViewAllEmployees(){
		homePage.clickViewAllEmployees();
	}

	@Then("^the list of Employees should be as expected$")
	public void validateListOfEmployees() throws Exception {
		List<Employee> expectedEmployees = employeeService.getAllEmployees();
		List<Employee> actualEmployees = employeesPage.getEmployeesList();
		Assert.assertEquals("Number of employees different from expected", expectedEmployees.size(), actualEmployees.size());
		for(int i=0; i<expectedEmployees.size(); i++){
			Employee expected = expectedEmployees.get(i);
			Employee actual = actualEmployees.get(i);
			Assert.assertEquals("Employee " + (i+1) + " not as expected", expected, actual);
		}
	}

	@Then("^the title of Employee with '(.*)' '(.*)' is '(.*)'$")
	public void validateEmployeeTitle(String key1, String value1, String title) throws Exception {
		List<Employee> expectedEmployees = employeeService.getEmployeesByName(key1, value1);
		Assert.assertEquals("Number of employees different from expected", 1, expectedEmployees.size());
		Assert.assertEquals("Title returned by service is not as expected", title, expectedEmployees.get(0).title);
		System.out.println();
	}
}
