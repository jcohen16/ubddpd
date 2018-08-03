package com.ubddpd.login;

import java.io.IOException;

import com.ubddpd.tests.Setup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends Setup {
	
	// @FindBy(xpath = "//input[@name='user'] | //input[@name='USER']")
	@FindBy(id = "txtloginspusrname")
	WebElement usernameField;
	
	// @FindBy(xpath = "//input[@name='password'] | //input[@name='PASSWORD']")
	@FindBy(id = "txtloginsppwd")
	WebElement passwordField;
	
	// @FindBy(xpath = "//button[@id='loginspbtn'] | //input[@id='com.ubddpd.login-button'] | //button[@data-qa='Login Button']")
	@FindBy(id = "loginspbtn")
	WebElement loginButton;

	private static Logger log = LogManager.getLogger("resultsLogger");
	
	public LoginPage() throws IOException {
		super();
	}

	public void doLogin(String username, String password) throws Exception {

		try {
			// driver.findElement(By.xpath("//a[@name = 'overridelink']")).click();
			driver.navigate().to("javascript:document.getElementById('overridelink').click()");
		} catch (NoSuchElementException nse) {
			log.info("Override link does not exists, continuing.");
		}
		
		WebElement usernameField = driver.findElement(By.id("txtloginspusrname"));
        WebElement passwordField  = driver.findElement(By.id("txtloginsppwd"));
        WebElement loginButton = driver.findElement(By.id("loginspbtn"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();		
		
//		// TODO: revisit to see if we still need the code from here to the end of the method
//		List<WebElement> secQ2 = driver.findElements(By.name("answer"));
//
//		if (secQ2.size() > 0) {
//			driver.findElement(By.name("answer")).sendKeys("pru123");
//			Thread.sleep(1000);
//			driver.findElement(By.xpath("//button[contains(text(), 'Next')]")).click();
//		}
//
//		// webUtils.waitForLoad(driver);
//		List<WebElement> popup = driver.findElements(By.linkText("No, thanks. I'll do this later."));
//		if (popup.size() > 0) {
//			driver.findElement(By.linkText("No, thanks. I'll do this later.")).click();
//		}
//
//		// webUtils.waitForLoad(driver);
//		List<WebElement> secQ = driver.findElements(By.name("answer1"));
//
//		if (secQ.size() > 0) {
//			driver.findElement(By.name("answer1")).sendKeys("pru123");
//			driver.findElement(By.name("nextButton")).click();
//		}
		
		// make sure you are logged in before exiting
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Logout')]")));
	}

}
