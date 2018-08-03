package com.ubddpd.homepage;

import java.io.IOException;

import com.ubddpd.tests.Setup;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;


public class ManagePlan extends Setup {
	
	public ManagePlan() throws IOException {
		super();
	}

	public String ManagePlan_LinkToSelect;

	/**
	 * Link to Select from Manage Plan Dropdown
	 */
	public void selectLinkFromManagePlan(String link) {
		link = link.replace("&amp;", "&");
		driver.findElement(By.xpath("//a[contains(text(),'" + link + "')]")).click();
	}
	
	/**
	 * Select Inquiry Type
	 */
	public void selectInquiryType(String inquryType) {
		Select dropdown = new Select(driver.findElement(By.name("selinquirytype")));
		dropdown.selectByVisibleText(inquryType);
	}

	/**
	 * Send Participant Name or Account number
	 */
	public void sendKeys(String input) {
		driver.findElement(By.xpath("//div[@class='managePlanFormSection']//input")).sendKeys(input);
	}

	/**
	 * Click Go button
	 */
	public void clickGo() {
		driver.findElement(By.xpath("//button[contains(text(),'GO')]")).click();
	}
	
//	public String ManagePlan_LinkToSelect ; 
//	public String ManagePlan_InquiryType; 
//	public String ManagePlan_ParticipantNameOrAccNumber;
//	public String ManagePlan_ButtonToSelect;
//
//	public void act_B2B_HomePage_ManagePlan() throws Exception 
//	{
//		WebElementsUtils webUtils = new WebElementsUtils();
//
//		String[] paramNames = new String[]{"ManagePlan_LinkToSelect", "ManagePlan_InquiryType","ManagePlan_ParticipantNameOrAccNumber",
//		"ManagePlan_ButtonToSelect"};
//
//		String[] paramValues = AutomationFrameworkTest.getTestParamsValues(paramNames, index, Thread.currentThread().getStackTrace()[1].getMethodName());
//		ManagePlan_LinkToSelect =  paramValues[Arrays.asList(paramNames).indexOf("ManagePlan_LinkToSelect")];
//		ManagePlan_InquiryType =  paramValues[Arrays.asList(paramNames).indexOf("ManagePlan_InquiryType")];
//		ManagePlan_ParticipantNameOrAccNumber =  paramValues[Arrays.asList(paramNames).indexOf("ManagePlan_ParticipantNameOrAccNumber")];
//		ManagePlan_ButtonToSelect =  paramValues[Arrays.asList(paramNames).indexOf("ManagePlan_ButtonToSelect")];
//
//		ManagePlan_LinkToSelect = ManagePlan_LinkToSelect.replace("&amp;", "&");
//
//
//		//Link to Select from Manage Plan Dropdown
//		if(ManagePlan_LinkToSelect != null && ManagePlan_LinkToSelect != "")
//		{
//
//			webUtils.clickElement(driver, By.xpath("//a[contains(text(),'"+ ManagePlan_LinkToSelect +"')]"), "ManagePlan_LinkToSelect");
//		}
//
//
//
//		//Inquiry Type
//		webUtils.selectValueFromDropDown(driver, By.name("selinquirytype"), ManagePlan_InquiryType, "Drop down Value");
//
//		Thread.sleep(1500);
//
//		if(ManagePlan_ParticipantNameOrAccNumber != null && ManagePlan_ParticipantNameOrAccNumber != "")
//		{
//			//Participant Name Or Account Number
//
//			webUtils.sendText(driver, By.xpath("//div[@class='managePlanFormSection']//input"), ManagePlan_ParticipantNameOrAccNumber); 
//		}
//		
//		Thread.sleep(1500);
//		
//		//Select Go button
//		if(ManagePlan_ButtonToSelect.equalsIgnoreCase("Go"))
//		{
//			webUtils.clickElement(driver, By.xpath("//button[contains(text(),'GO')]"), "Go Button");
//		}
//
//		Thread.sleep(4000);
//	}

}