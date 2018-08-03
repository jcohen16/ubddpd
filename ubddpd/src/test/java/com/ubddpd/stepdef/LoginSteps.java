package com.ubddpd.stepdef;

import com.ubddpd.tests.Setup;
import com.ubddpd.homepage.B2BHomePage;
import com.ubddpd.homepage.HomePageTutorial;
import com.ubddpd.login.LoginPage;
import com.ubddpd.login.Logout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static com.ubddpd.tests.Setup.takeScreenshot;
import static com.ubddpd.tests.Setup.getTestConfig;

public class LoginSteps {
	
	Logger resultsLogger = LogManager.getLogger("resultsLogger");
	
	@Given("^the user is logged in to B2B using (.*)$")
	public void loginToB2B(String userId) throws Throwable {
		B2BHomePage homePage = new B2BHomePage();
		homePage.open("businesscenter_url");
		
		LoginPage loginPage = new LoginPage();
		loginPage.doLogin(userId, (String)getTestConfig().get("password"));
		takeScreenshot();
	}

	@Then("^the user closes the tutorial popup if it appears$")
	public void closeTutorial() throws Throwable {
		HomePageTutorial tut = new HomePageTutorial();
		tut.closeUpPop();
		takeScreenshot();
	}


	@And("^user clicks sub plan of the plan (.*)$")
	public void clickSubPlan(String plan) throws Throwable {
		B2BHomePage homepage=new B2BHomePage();
		homepage.selectSubPlan(plan);
		Setup.takeScreenshot();
	}
	
	@Then("^user clicks manage plan link of plan (.*)$")
	public void clickManagePlan(String plan) throws Throwable {
		B2BHomePage homepage=new B2BHomePage();
		homepage.clickManagedPlan(plan);
		Setup.takeScreenshot();
	}
	
	@Then("^the user logs out$")
	public void doLogout() throws Throwable {
		Logout logout = new Logout();
		logout.doLogout();
		Setup.takeScreenshot();
	}
	
	
}
