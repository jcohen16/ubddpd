package com.ubddpd;

import com.ubddpd.tests.Setup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends Setup {

	public void open(String prop) throws Exception {
		String url = (String)(Setup.testConfig.get(prop));
		driver.get(url);
	}

	public String getHeading(){
		WebElement headingElem = driver.findElement(By.id("heading"));
		return headingElem.getText();
	}

	public void clickViewAllEmployees(){
		WebElement link = driver.findElement(By.id("allEmployeesLink"));
		link.click();
	}
}
