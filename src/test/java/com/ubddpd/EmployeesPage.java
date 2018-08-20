package com.ubddpd;

import com.ubddpd.tests.Setup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.*;

public class EmployeesPage extends Setup {

	public List<Employee> getEmployeesList(){
		List<Employee> result = new ArrayList<>();

		WebElement employeesTable = driver.findElement(By.id("table"));
		List<WebElement> employeeRows = employeesTable.findElements(By.tagName("tr"));
		for(WebElement row : employeeRows){
			Employee emp = new Employee();
			WebElement idCell = row.findElement(By.id("id"));
			WebElement nameCell = row.findElement(By.id("name"));
			WebElement titleCell = row.findElement(By.id("title"));
			emp.id = idCell.getText();
			emp.name = nameCell.getText();
			emp.title = titleCell.getText();
			result.add(emp);
		}

		return result;
	}
}
