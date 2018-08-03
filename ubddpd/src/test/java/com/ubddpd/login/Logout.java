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

public class Logout extends Setup {
	
	@FindBy(xpath = "//a[contains(text(), 'Logout')]")
	WebElement logout;

	private static Logger log = LogManager.getLogger("resultsLogger");
	
	public Logout() throws IOException {
		super();
	}

	public void doLogout() throws Exception {
		driver.switchTo().defaultContent();
		logout.click();
		
		try {
			driver.findElement(By.xpath("//a[@name = 'overridelink']")).click();
		} catch (NoSuchElementException nse) {
			log.info("Override link does not exists, continuing.");
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='user'] | //input[@name='USER']")));
	}

}
