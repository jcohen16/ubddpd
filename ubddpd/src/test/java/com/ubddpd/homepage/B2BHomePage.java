package com.ubddpd.homepage;

import java.io.IOException;
import java.util.*;

import com.ubddpd.tests.Setup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class B2BHomePage extends Setup {

	@FindBy(xpath=".//*[contains(@id,'ManagePlanButton')]/button)")
	private WebElement managePlan;

	public B2BHomePage() throws IOException {
		super();
	}
	
	public void open(String prop) {
		driver.get((String)getTestConfig().get(prop));
	}

	/**
	 * clicks list view button
	 */
	public void changeToListView() {
		driver.findElement(By.id("listViewTrigger")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class = 'icon listImgActive iconToggleListImg']")));
	}

	public void clickManagedPlan(String plan) {
		
		//TODO: replace with proper wait method
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// To select the managed plan
		WebElement parentNode = driver.findElement(By.id("layoutContainers"));// finds elements inside the container
		List<WebElement> rows = parentNode.findElements(By.tagName("tr"));

		String rowstr;
		plan = plan.replace("&amp;", "&");

		for (int i = 0; i < rows.size(); i++) {
			rowstr = rows.get(i).getText();
			if (rowstr.contains(plan)) {
				// selects the manage plan button
				WebElement button = rows.get(i).findElement(By.tagName("button"));
				// To click the manage plan button
				button.click();
				wait.until(ExpectedConditions.invisibilityOfElementWithText(By.xpath("//button"), plan));
				break;
			}
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id = 'dcListManagePlanDiv0'] | //div[@id = 'dcCardManagePlanDiv0']")));
		
	}

	public void selectSubPlan(String subPlan) {
		driver.findElement(By.xpath("//td//a[contains(text(),'" + subPlan + "')]/../../following-sibling::tr//td//img"))
				.click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//img[@src = '/B2B/web/img/loading-wt.gif']")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td//a[contains(text(),'" + subPlan + "')]/../../following-sibling::tr//td//img[@src = '/B2B/web/img/toggle-arrow-down.png']")));
	}
	
	public void selectPlanDropDown(String plan) {
			driver.findElement(By.id("myplans-menu")).click();
			driver.findElement(By.xpath("//li/a[contains(text(),'" + plan + "')]")).click();
	}

	public void clickManagePlanDropDown() {
		// ManagePlan Button Click for single Plan (Internal Sponsor)
		wait.until(ExpectedConditions.visibilityOf(managePlan));
		managePlan.click();		
	}
	
	public void doubleClickCrossPlans() {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.id("jumptoCrossSummary"));
		action.doubleClick(element).perform();
	}
	
	public void doubleClickPlans() {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.id("jumptoPlans"));
		action.doubleClick(element).perform();
	}
	
	public void doubleClickAlerts() {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.id("jumptoAlerts"));
		action.doubleClick(element).perform();
	}
	
	public void doubleClickTools() {
		Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.id("jumptoTools"));
		action.doubleClick(element).perform();
	}
	
	public void jumpToAlerts() {
		
		WebElement element = driver.findElement(By.id("jumptoAlerts"));
		element.click();
	}
	
     public void selectAlertsTab(String alertTabName) throws Exception {
		
    	if(alertTabName.equalsIgnoreCase("Messages"))
    	{
		WebElement element = driver.findElement(By.xpath("//div[contains(@id, 'leftList') and (contains(., 'Messages'))]"));
		element.click();
		//Thread.sleep(8000);
    	}

    	if(alertTabName.equalsIgnoreCase("Payroll Changes"))
    	{
		WebElement element = driver.findElement(By.xpath("//div[contains(@id, 'leftList') and (contains(., 'Payroll Changes'))]"));
		element.click();
		
		Thread.sleep(13000);
    	}
    	
	}
     

     public void selectPayrollChangesAlertPlan(String planName) throws Exception {
    	 
    	 planName = planName.replace("&amp;", "&");
    	 
    	 WebElement payrollChangesAlertPlan = driver.findElement(By.xpath("//p[contains(text(),'"+ planName +"')]/following-sibling::a"));
    	 payrollChangesAlertPlan.click();
		  Thread.sleep(9000);
     	    	 
 	}

     public void clickPlanLink(String planName){
         WebElement planLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                 By.linkText((planName))));
         planLink.click();
     }
     
     public List<String> getFooterDisclaimerLines(){
         List<String> result = new ArrayList<String>();
         WebElement footerLinksDiv = driver.findElement(By.className("b2bFooter-links"));
         List<WebElement> disclaimerElements = footerLinksDiv.findElements(By.xpath("following-sibling::*"));
         for(int i=0; i<disclaimerElements.size(); i++){
             result.add(disclaimerElements.get(i).getText());
         }
         return result;
     }
}
