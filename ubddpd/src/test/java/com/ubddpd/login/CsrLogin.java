package com.ubddpd.login;

import java.io.IOException;

import com.ubddpd.tests.Setup;
import org.openqa.selenium.By;

public class CsrLogin extends Setup {

	public CsrLogin() throws IOException{
		super();
	}

	public void doInternalSponsorAdvisorLogin(String url, String planId) throws Exception {

		if (!url.equals("")) {
			driver.get(url);
		}

		driver.findElement(By.linkText("Admin Home Page")).click();
		driver.findElement(By.linkText("Internal Sponsor/Advisor Center Website Access")).click();
		driver.findElement(By.name("planId")).sendKeys(planId);
		driver.findElement(By.name("submit")).click();; // Submit
	}

	public void doWoboLogin(String url, String ipSearchWOBOUserId) throws Exception {

		if (!url.equals("")) {
			driver.get(url);
		}

		driver.findElement(By.linkText("Admin Home Page")).click();
		driver.findElement(By.linkText("Work on Behalf of Sponsor/Advisor/Stable Value Users")).click();
		driver.findElement(By.name("ipSearchWOBOUserId")).sendKeys(ipSearchWOBOUserId);
		driver.findElement(By.name("ImpersonationSubmit")).click();
		driver.findElement(By.name("userId")).click();
		driver.findElement(By.name("ImpersonationNext")).click();

	}

	// // variables as strings
	// String[] paramNames = new String[] { "userID", "password", "URL",
	// "BrowserType", "LoginType", "PlanID" };
	//
	// String[] paramValues =
	// AutomationFrameworkTest.getTestParamsValues(paramNames, index,
	// Thread.currentThread().getStackTrace()[1].getMethodName());
	// userID = paramValues[Arrays.asList(paramNames).indexOf("userID")];
	// URL = paramValues[Arrays.asList(paramNames).indexOf("URL")];
	// password = paramValues[Arrays.asList(paramNames).indexOf("password")];
	// BrowserType = paramValues[Arrays.asList(paramNames).indexOf("BrowserType")];
	// LoginType = paramValues[Arrays.asList(paramNames).indexOf("LoginType")];
	// PlanID = paramValues[Arrays.asList(paramNames).indexOf("PlanID")];
	//
	// WebElementsUtils webUtils = new WebElementsUtils();
	//
	// if (!URL.equals("")) {
	// driver.get(URL);
	// }
	//
	// List<WebElement> Cert = driver.findElements(By.id("overridelink"));
	//
	// if (Cert.size() > 0) {
	// driver.navigate().to("javascript:document.getElementById('overridelink').click()");
	// }
	//
	// // Internal Sponsor Login
	// if (LoginType.equalsIgnoreCase("InternalSponsorAdvisor")) {
	// webUtils.clickElement(driver, By.linkText("Admin Home Page"));
	//
	// webUtils.clickElement(driver, By.linkText("Internal Sponsor/Advisor Center
	// Website Access"));
	//
	// webUtils.sendText(driver, By.name("planId"), PlanID); // Enter plan ID
	//
	// webUtils.clickElement(driver, By.name("submit")); // Submit
	//
	// // WOBO Login
	// } else if (LoginType.equals("WOBO")) {
	// webUtils.clickElement(driver, By.linkText("Admin Home Page"));
	//
	// webUtils.clickElement(driver, By.linkText("Work on Behalf of
	// Sponsor/Advisor/Stable Value Users"));
	//
	// webUtils.sendText(driver, By.name("ipSearchWOBOUserId"), userID); // Enter
	// User name
	//
	// webUtils.clickElement(driver, By.name("ImpersonationSubmit")); // Search for
	// user
	//
	// webUtils.clickElement(driver, By.name("userId")); // Click radio for user
	// available
	//
	// // Thread.sleep(30000);
	//
	// webUtils.clickElement(driver, By.name("ImpersonationNext")); // Go to user
	// }
	//
	// }

}
