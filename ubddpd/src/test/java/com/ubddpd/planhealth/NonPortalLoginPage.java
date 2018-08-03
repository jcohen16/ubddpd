package com.ubddpd.planhealth;

import com.ubddpd.tests.Setup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class NonPortalLoginPage extends Setup {
	
	@FindBy(id = "external")
	private WebElement externalUserType;

	@FindBy(id = "internal")
	private WebElement internalUserType;
	
	@FindBy(id = "univlPlanId")
	private WebElement universalPlanId;
	
	@FindBy(id = "prodFamCd")
	private WebElement productFamilyCode;
	
//	@FindBy(className = "btn btn-primary")
//	private WebElement sendButton;
	
	@FindBy(id = "yesUserImpersonated")
	private WebElement yesUserImpersonated;
	
	@FindBy(id = "noUserImpersonated")
	private WebElement noUserImpersonated;
	
	@FindBy(id = "userLoginId")
	private WebElement userLoginId;
	
	@FindBy(id = "ssoId")
	private WebElement ssoId;
	
	@FindBy(id = "yesAdvisor")
	private WebElement yesAdvisor;
	
	@FindBy(id = "noAdvisor")
	private WebElement noAdvisor;
	
	@FindBy(id = "yesSponsor")
	private WebElement yesSponsor;
	
	@FindBy(id = "noSponsor")
	private WebElement noSponsor;
	
	@FindBy(id = "yesStableValue")
	private WebElement yesStableValue;
	
	@FindBy(id = "noStableValue")
	private WebElement noStableValue;
	
	public NonPortalLoginPage() throws Exception {
        super();
    }
	
	public void loginNonPortalSite(String userXid, String userSsoId, String univPlanId) {
		loginNonPortalSiteUserType("Sponsor", userXid, userSsoId, univPlanId);
	}
	
	public void loginNonPortalSiteUserType(String userType, String userXid, String userSsoId, String univPlanId) {
		externalUserType.click();
		noUserImpersonated.click();
		userLoginId.sendKeys(userXid);
		ssoId.sendKeys(userSsoId);
		universalPlanId.sendKeys(univPlanId);
		productFamilyCode.sendKeys("02");
		
		WebElement advisorOption = noAdvisor;
		if(userType.equals("Advisor")) {
			advisorOption = yesAdvisor;
		}
		advisorOption.click();
		yesSponsor.click();
		noStableValue.click();
		
		WebElement sendButton = driver.findElement(By.className("btn-primary"));
		sendButton.click();
	}

}
