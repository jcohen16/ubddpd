package com.ubddpd.homepage;

import java.io.IOException;

import com.ubddpd.tests.Setup;
import org.junit.Assert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePageTutorial extends Setup {

	@FindBy(xpath = "//button[contains(.,'NEXT')]")
	private WebElement next;

	@FindBy(xpath = "//button[contains(.,'START TOUR')]")
	private WebElement start;
	
	@FindBy(xpath = "//button[contains(.,'FINISH')]")
	private WebElement finish;

	@FindBy(xpath = "//button[contains(.,'PREVIOUS')]")
	private WebElement previous;

	@FindBy(id = "dontShowTour")
	private WebElement dontShowTour;
	
	//@FindBy(xpath = "//*[@class='popover-title'")
	//private WebElement tutorialTitle;

	
	//@FindBy(cssSelector = "BODY")
	//private WebElement cssselector;
	
	
	
	
	private static Logger log = LogManager.getLogger("resultsLogger");

	public HomePageTutorial() throws IOException {
		super();
	}

	public void clickNext() throws Exception {
		
		if(start.isDisplayed())
		{
			start.click();
		}
		next.click();
	}

	public void clickStart() throws Exception {
		start.click();
	}

	public void clickPrevious() throws Exception {
		previous.click();
	}

	public void closeUpPop() throws Exception {
		try {
			// dontShowTour.click();
			WebElement closePopupButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.id("dontShowInSession")));
	        closePopupButton.click();
		} catch (NoSuchElementException n) {
			log.info("No popup to close, continuing");
		}
		
	}
	
	public void clickFinish() throws Exception {
		finish.click();
	}

	public void clickDontShowAgain() throws Exception {
			dontShowTour.click();
	}

	public void verifyTutorialTitle(String expectedTitle) throws Exception {

		//WebElement welcome=driver.findElement(By.cssSelector("BODY"));
		WebElement tutorialTitle=driver.findElement(By.xpath("//*[@class='popover-title']"));
		
		String actualTitle=tutorialTitle.getText();
		Assert.assertTrue("Expected Welcome Tutorial Title Not found",actualTitle.contains(expectedTitle));
		
}
	
}
