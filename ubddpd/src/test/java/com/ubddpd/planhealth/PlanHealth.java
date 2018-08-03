package com.ubddpd.planhealth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ubddpd.utils.SoapUIClient;
import com.ubddpd.tests.Setup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.fasterxml.jackson.databind.ObjectMapper;


public class PlanHealth extends Setup {

    private Map<String, Object> assetAllocationDetails = null;

    public PlanHealth() throws Exception {
        super();
    }
    
    public String getTitle() {
    	return driver.getTitle();
    }
    
    public WebElement getPlanNameElement() {
		return driver.findElement(By.className("planTitle"));
	}
    
    public String getPlanNameTag() {
    	WebElement element = getPlanNameElement();
    	return element.getTagName();
    }
    
    public String getPlanHealthTag() {
    	WebElement element = getPlanHealthElement();
    	return element.getTagName();
    }
    
    public String getPageTitleTag() {
    	WebElement element = getPageTitleElement();
    	element = element.findElement(By.xpath("//parent::*[@class = 'fs14rem']"));
    	return element.getTagName();
    }
    
    public String getPlanName() {
    	return getPlanNameElement().getText();
    }
	
	public WebElement getManagePlanButtonElement() {
		return driver.findElement(By.id("managePlanButton"));
	}
	
	public WebElement getViewDropdownElement() {
		return driver.findElement(By.id("planSummaryDrp"));
	}
	
	public WebElement getSelectPlanDropdownElement() {
		return driver.findElement(By.id("navbarDropdown"));
	}
	
	public WebElement getPageTitleElement() {
		return driver.findElement(By.id("pageTitle"));
	}
	
	public String getPageTitle() {
		return getPageTitleElement().getText();
	}

	public WebElement getPlanHealthElement() {
		return driver.findElement(By.className("semiBoldF"));
	}
	
	public String getPlanHealth() {
		return getPlanHealthElement().getText();
	}
	
	public WebElement getHeaderLink(String linkText) {
		WebElement element = driver.findElement(By.className("topLinksMain"));
		return element.findElement(By.linkText(linkText));
	}
	
	public boolean headerLinkPresent(String link) {
		return getHeaderLink(link) != null;
    }
	
	public boolean headerDropdownPresent(String dropdown){
        boolean result = false;
        try{
            switch(dropdown){
                case "View":
                    result = getViewDropdownElement() != null;
                    break;
                case "Select Plan":
                    result = getSelectPlanDropdownElement() != null;
                    break;
                case "Manage Plan":
                    result = getManagePlanButtonElement() != null;
                    break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
	
	public WebElement getPrudentialLogo() {
		return driver.findElement(By.xpath("//img[contains (@src, 'assets/images/logo.gif')]"));
	}

	public boolean prudentialLogoPresent(){
		return getPrudentialLogo() != null;
    }
	
	public String getLogoText() {
		return driver.findElement(By.className("logoText")).getText();
	}

	public void clickManagePlanButton(){
    	getManagePlanButtonElement().click();
    }

	public void clickSelectPlan(){
    	getSelectPlanDropdownElement().click();
    }
    
    public void clickView() {
    	getViewDropdownElement().click();
    }
    
    public String getHeaderText(){
        WebElement topLinks = driver.findElement(By.className("topLinksMain"));
        WebElement textSpan = topLinks.findElement(By.tagName("span"));
        return textSpan.getText();
    }

    // Old Page had a different ID for the button, and doesn't work via selenium if you only click on it once
    public void clickManagePlanButtonOldPage(){
        WebElement button =  driver.findElement(By.id("headerManagePlanButton"));
        button.click();
        button.click();
        button.click();
    }
    
    public void clickYourSetting(){
        WebElement button = driver.findElement(By.xpath("//a[@class = 'btn btn-default btn-outline-dark settingsBtn dropdown-toggle']"));
        button.click();
    }
    
    public void clickMngPlanLinkUnderHeader(String link, String header){
        WebElement planHealthLink = driver.findElement(By.linkText(link));
        planHealthLink.click();
    }

    public boolean headerTextPresentOldPage(String text){
        boolean result = false;
        try{
            WebElement headerDiv = driver.findElement(By.id("b2bHeader-com.ubddpd.utils"));
            WebElement textSpan = headerDiv.findElement(By.tagName("span"));
            result = text.equals(textSpan.getText());
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public boolean footerLinkPresent(String link) {
        boolean result = false;
        try{
            if(link.equals("Accessibility Help"))
                result = driver.findElement(By.partialLinkText(link)) != null;
            else
                result = driver.findElement(By.xpath("//ul[@class = 'bottomLinks']/li/a[text() = '" + link + "']"))!= null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  result;
    }

    public List<String> getFooterDisclaimerLines(){
        List<String> result = new ArrayList<String>();
        WebElement footerLinksDiv = driver.findElement(By.className("bottomLinks"));
        List<WebElement> disclaimerElements = footerLinksDiv.findElements(By.xpath("following-sibling::*"));
        for(WebElement element : disclaimerElements){
            result.add(element.getText());
        }
        return result;
    }

    public boolean managePlanMenuLinksPresentUnderHeading(String heading, String link){
        boolean result = false;
        try{
            if(link.equals("Payroll Changes")) {
                result = driver.findElement(By.partialLinkText(link)) != null;
            }else {
                result = driver.findElement(By.xpath("//h3[text() = '" + heading + "']/following-sibling::a[@class = 'dropdown-item' and text() = '" + link + "']")) != null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean managePlanMenuDropdownPresent(String heading, String label){
        return driver.findElement(By.xpath("//h3[text() = '" + heading + "']/following-sibling::div/form/label[text() = '" + label + "']/following-sibling::p/select")) != null;
    }

    public boolean managePlanMenuTextboxPresent(String heading, String label){
        return driver.findElement(By.xpath("//h3[text() = '" + heading + "']/following-sibling::div/form/label[text() = '"+ label +"']/following-sibling::p/input")) != null;
    }

    public boolean managePlanMenuButtonPresent(String heading, String label){
        return driver.findElement(By.xpath("//h3[text() = '" + heading + "']/following-sibling::div/form/button[text() = '" + label + "']")) != null;
    }

    public boolean managePlanMenuTextHeadingPresent(String heading, String label){
        return driver.findElement(By.xpath("//h3[text() = '" + heading + "']/following-sibling::div/div/h4[text() = '" + label + "']")) != null;
    }

    public boolean managePlanMenuTextPresent(String heading, String text){
        boolean result = false;
        try {
            List<WebElement> element = driver.findElements(By.xpath("//h3[text() = '" + heading + "']/following-sibling::div/div/p"));
            for (WebElement i : element) {
                if (text.equals(i.getText())) {
                    result = true;
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean moduleWithHeadingPresent(String heading){
        boolean result = false;
        WebElement moduleElement = getModule(heading);
        WebElement element = null;
        try{
            switch(heading){
                case "Financial Wellness":
                    element = moduleElement.findElement(By.id("financialWellnessTitle"));
                    break;
                case "Participation Rate":
                    element = moduleElement.findElement(By.id("participantRateTitle"));
                    break;
                case "Avg. Participant Balance":
                    element = moduleElement.findElement(By.id("avgParticipantBalanceTitle"));
                    break;
                case "Avg. Participant Achievements":
                    element = moduleElement.findElement(By.id("avgParticipantAchievementsTitle"));
                    break;
                default:
                    throw new Exception("Invalid Input");
            }
            result = element.getText().equals(heading);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean checkModuleForHeadingText(String text, String module){
        boolean result = false;
        WebElement moduleElement = getModule(module);
        WebElement element = null;
        try{
            switch (module){
                case "Financial Wellness":
                    element = moduleElement.findElement(By.id("asOfFinancialWellnessDate"));
                    break;
                case "Participation Rate":
                case "Avg. Participant Balance":
                    element = moduleElement.findElement(By.className("float-right"));
                    break;
                case "Avg. Participant Achievements":
                    element = moduleElement.findElement(By.id("avgParticipantAchievementsAsOf"));
                    break;
            }
            result = element.getText().equals(text);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean linkPresentInModule(String link, String module){
        boolean result = false;
        WebElement moduleElement = getModule(module);
        try{
            switch (module){
                case "Financial Wellness":
                    // getFinancialDetailsLink() != null && getFinancialWellnessArrow() != null;
                    WebElement linkElem =  moduleElement.findElement(By.id("financialWellDetails"));
                    result = linkElem != null;
                    if(result) {
                        result = moduleElement.findElement(By.id("financialWellDetails")) != null;
                    }
                    break;
                case "Participation Rate":
                    result = moduleElement.findElement(By.id("participantRateDetails")) !=null &
                            moduleElement.findElement(By.xpath("//span[@class = 'fa fa-chevron-circle-right']")) != null;
                    break;
                case "Avg. Participant Balance":
                    result = moduleElement.findElement(By.id("avgParticipantBalanceDetails")) !=null &
                            moduleElement.findElement(By.xpath("//span[@class = 'fa fa-chevron-circle-right']")) != null;
                    break;
                case "Avg. Participant Achievements":
                    result = moduleElement.findElement(By.id("avgParticipantAchievementsDetails")) !=null &
                            moduleElement.findElement(By.xpath("//span[@class = 'fa fa-chevron-circle-right']")) != null;
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void clickLinkInModule(String link, String module){
        WebElement moduleElement = getModule(module);
        WebElement webLink = null;
        try{
            switch (link){
                case "Financial Wellness Details":
                    webLink = moduleElement.findElement(By.id("financialWellDetails"));
                    break;
                case "Rate Details":
                    webLink = moduleElement.findElement(By.id("participantRateDetails"));
                    break;
                case "Balance Details":
                    webLink = moduleElement.findElement(By.id("avgParticipantBalanceDetails"));
                    break;
                case "Achievement Details":
                    webLink = moduleElement.findElement(By.id("avgParticipantAchievementsDetails"));
                    break;
            }
            webLink.click();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean moduleGraphHeadingPresent(String module, String heading){
        WebElement moduleElement = getModule(module);
        WebElement element = null;
        boolean result = false;
        try {
            switch(heading){
                case "Average Income Replacement":
                    element = moduleElement.findElement(By.id("avgIncomeReplacementTitle"));
                    break;
                case "On Track to replace 80% of Retirement Income":
                    element = driver.findElement(By.id("retirmentIncomeTitle"));
                    heading = "On Track to replace 80% of" + "\n" + "Retirement Income";
                    break;
            }
            result = element.getText().equals(heading);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean graphPresentInModule(String module, String graph){
        WebElement moduleElement = getModule(module);
        boolean result = false;
        try {
            switch(graph){
                case "Avg Income Replacement":
                    result = moduleElement.findElement(By.id("avgIncomeReplacementGuage")) != null;
                    break;
                case "On Track to replace 80% of Retirement Income":
                    result = moduleElement.findElement(By.id("retirementIncomeGuage")) != null;
                    break;
                case "Financial Wellness":
                    result = moduleElement.findElement(By.id("financialWellnessGraph")) != null;
                    break;
                case "Achievement Details":
                    result = moduleElement.findElement(By.id("avgParticipantAchievementsGraph")) != null;
                    break;
                case "Participation Rate":
                    result = moduleElement.findElement(By.id("participantRateGraph")) != null;
                    break;
                case "Avg. Balance":
                    result = moduleElement.findElement(By.id("avgParticipantBalanceGraph")) != null;
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getYourSettingItems(){
        List<String> result = new ArrayList<String>();
        List<WebElement> elements = driver.findElements(By.xpath("//a[@class = 'btn btn-default btn-outline-dark settingsBtn dropdown-toggle']/following-sibling::div//li"));
        for(WebElement element: elements){
            String text[] = element.getText().split("\n");
            result.add(text[0]);
        }
        return result;
    }

    public void clickYourSettingItems(String itemName){
        WebElement element = driver.findElement(By.xpath("//span[text() = '" + itemName + "']/following-sibling::span[@class = 'showHideButton']"));
        element.click();
    }

    public boolean checkYourSettingStatus(String itemName, String itemStatus){
        boolean result = false;
        String text;
        try{
            List<WebElement> elements = driver.findElements(By.xpath("//a[@class = 'btn btn-default btn-outline-dark settingsBtn dropdown-toggle']/following-sibling::div//li"));
            for(WebElement element: elements){
                text = element.getText();
                if(text.contains(itemName)) {
                    result = text.contains(itemStatus);
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean containerWithHeading(String containerHeading){
        boolean result = false;
        try{
            switch(containerHeading){
                case "Participation Rate":
                    result = driver.findElement(By.id("participantRateTitle")) != null;
                    break;
                case "Avg Contribution Rate":
                    result = driver.findElement(By.id("avgContributionRateTitle")) != null;
                    break;
                case "Avg Participant Balance":
                    result = driver.findElement(By.id("avgParticipantBalanceTitle")) != null;
                    break;
                case "Avg Income Replacement":
                    result = driver.findElement(By.id("avgIncomeReplacementTitle")) != null;
                    break;
                case "Retirement Income Calculator":
                    result = driver.findElement(By.id("retirementIncomeCalculatorTitle")) != null;
                    break;
                case "Avg. Participant Achievements":
                    result = driver.findElement(By.id("avgParticipantAchievementsTitle")) != null;
                    break;
                case "Active Loans":
                    result = driver.findElement(By.id("activeLoansTitle")) != null;
                    break;
                case "Cash Flow":
                    result = driver.findElement(By.id("cashFlowTitle")) != null;
                    break;
                case "Asset Allocation":
                    result = driver.findElement(By.id("assetAllocatonTitle")) != null;
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String dropdownWithSelectedOptionPresent(String dropdown){
        String selectedOption = "";
        switch(dropdown){
            case "View":
                selectedOption = new Select(driver.findElement(By.id("planSummaryDrp"))).getFirstSelectedOption().getText();
                break;
            }
        return selectedOption;
    }

    public void clickDropdown(String dropdown){
        WebElement element;
        switch(dropdown){
            case "View":
                element = driver.findElement(By.id("planSummaryDrp"));
                element.click();
                break;
        }
    }

    public void selectDropdownOptionByText(String option, String dropdown){
        Select dropdownElement;
        switch(dropdown){
            case "View":
                dropdownElement = new Select(driver.findElement(By.id("planSummaryDrp")));
                dropdownElement.selectByVisibleText(option);
                break;
        }
    }

    public String getDropdownOptions(String dropdown){
        WebElement dropdownElement = null;
        switch(dropdown) {
            case "View":
                dropdownElement = driver.findElement(By.id("planSummaryDrp"));
                break;
            case "Select Plan":
                dropdownElement = driver.findElement(By.xpath("//div['dropdown-menu dropdown-menu-right selectPlanDrop show']/ul"));
                break;
        }
        return dropdownElement.getText();
    }

    public boolean moduleOnOldPagePresent(String module){
        boolean result = false;
        try{
            switch(module){
                case "Asset Allocation":
                    WebElement moduleElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("assetAllocationSummary")));
                    result = moduleElem != null;
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean assetAllocationFieldsOnOldPagePresent(String field, String fieldType){
        boolean result = false;
        WebElement assetAllocationSummary = driver.findElement(By.className("assetAllocationSummary"));
        try{
            switch(fieldType){
                case"Heading Text":
                    result = assetAllocationSummary.findElement(By.xpath("//descendant::h2[text() = '" + field + "']" )) != null;
                    break;
                case "Text":
                    result = assetAllocationSummary.findElement(By.xpath("//descendant::div[text() = '" + field + "']" )) != null;
                    break;
                case "Link Image":
                    result = assetAllocationSummary.findElement(By.xpath("//descendant::a[@href = '/wps/mypoc?uri=locator:rssp.com.ubddpd.planhealth.assetallocation']" )) != null;
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String getAssetAllocModuleTitle(){
        WebElement assetAllocationSummary = driver.findElement(By.className("assetAllocationSummary"));
        String summaryTitleTxt = assetAllocationSummary.findElement(By.tagName("h2")).getText();
        return summaryTitleTxt;
    }

    public String getAssetAllocAsOfDate(){
        WebElement assetAllocationSummary = driver.findElement(By.className("assetAllocationSummary"));
        WebElement asOfDate = assetAllocationSummary.findElement(By.className("rz-asof"));
        return asOfDate.getText();
    }

    public boolean assetAllocArrowIconPresent(String envBaseUrl){
        boolean result = false;
        WebElement assetAllocationSummary = driver.findElement(By.className("assetAllocationSummary"));
        try{
            WebElement arrowIcon = assetAllocationSummary.findElement(By.tagName("a"));
            String actualAttribValue = arrowIcon.getAttribute("href");
            result = actualAttribValue.equals(envBaseUrl + "/wps/mypoc?uri=locator:rssp.com.ubddpd.planhealth.assetallocation");
        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String getAssetAllocHeadingTxt(){
        WebElement assetAllocationSummary = driver.findElement(By.className("assetAllocationSummary"));
        WebElement title = assetAllocationSummary.findElement(By.className("rz-key"));
        return title.getText();
    }

    public List<String> assetAllocationSummaryOnOldPagePresent(){
        List<String> result = new ArrayList<String>();
        WebElement assertAllocation = driver.findElement(By.className("assetAllocationSummary"));
        List<WebElement> elements = assertAllocation.findElements(By.xpath("//descendant::div[@class = 'keyRow']"));
        for(WebElement i: elements){
            result.add(i.getText());
        }
        return result;
    }

    public boolean moduleDataLoaded(String module){
        boolean result = false;
        try{
            switch(module){
                case "Asset Allocation":
                    WebElement assertAllocation = driver.findElement(By.className("assetAllocationSummary"));
                    result = assertAllocation.getText().contains("There was an error loading the data.");
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<String> checkAssetAllocationColor(){
        List<String> colorCode = new ArrayList<String>();
        WebElement assetAllocation = driver.findElement(By.className("assetAllocationSummary"));
        List<WebElement> elements = assetAllocation.findElements(By.xpath("//descendant::div[@class = 'keyContainer']"));
        for(WebElement i: elements){
            colorCode.add(i.getAttribute("Style"));
        }
        return colorCode;
    }
	
	public void setESBAssetAllocationDetails(String planId) throws Exception {
        SoapUIClient client = new SoapUIClient();
        client.openProject("res/soapui_scripts/CIServiceAutomation-soapui-project.xml");
        client.setRestService("AssetAllocationDetail");
        client.setRestResource("AssetAllocationDetail");
        client.setRestMethod("AssetAllocationDetail");
        client.setRestRequest("AssetAllocationDetail");
        Map<String, String> params = new HashMap<>();
        params.put("planNumber", planId);
        String response = client.getResponse(params);
        client.closeProject();

        ObjectMapper mapper = new ObjectMapper();
        assetAllocationDetails = mapper.readValue(response, Map.class);
    }

    public String getESBAssetAllocAsOfDate(){
        List<Map<String, Object>> data = (List<Map<String, Object>>)assetAllocationDetails.get("Data");
        String asOfDate = null;
        for(Map<String, Object> datum : data){
            if(datum.containsKey("key")){
                String key = (String)datum.get("key");
                if(key.equals("AsOfDate")){
                    asOfDate = (String)datum.get("value");
                }
            }
        }
        String dateParts[] = asOfDate.split("/");
        String year = dateParts[2].substring(2);
        String result = dateParts[0] + "/" + dateParts[1] + "/" + year;
        return result;
    }

    public Map<String, String> getExpectedAssetAllocSummaries(){
        Map<String, String> expectedSummaries = new HashMap<>();
        List<Map<String, Object>> assetData = (List<Map<String, Object>>)assetAllocationDetails.get("Data");
        for(Map<String, Object> assetDatum : assetData){
            Set<Map.Entry<String, Object>> entrySet = assetDatum.entrySet();
            for(Map.Entry<String, Object> entry : entrySet){
                if(entry.getKey().equals("AssetClassDetail")){
                    List<Map<String, Object>> assetClassDetailList = (List<Map<String, Object>>)entry.getValue();
                    for(Map<String, Object> assetClassDetail : assetClassDetailList){
                        String assetClassName = (String)assetClassDetail.get("AssetClassName");
                        String assetClassPct = "";
                        List<Map<String, Object>> infoList = (List<Map<String, Object>>)assetClassDetail.get("Info");
                        for(Map<String, Object> info : infoList){
                            if("HoldingsPct".equals(info.get("key"))){
                                assetClassPct = info.get("value") + "%";
                                break;
                            }
                        }
                        expectedSummaries.put(assetClassName, assetClassPct + " " + assetClassName);
                    }
                }
            }
        }
        return expectedSummaries;
    }

    public WebElement getModule(String moduleName){
        WebElement module = null;
        switch (moduleName){
            case "Financial Wellness":
                module = driver.findElement(By.id("financialWellnessContainer"));
                break;
            case "Participation Rate":
                module = driver.findElement(By.id("participantRateContainer"));
                break;
            case "Avg. Participant Balance":
                module = driver.findElement(By.id("avgParticipantBalanceContainer"));
                break;
            case "Avg. Participant Achievements":
                module = driver.findElement(By.id("avgParticipantAchievementsContainer"));
                break;
        }
        return module;
    }

    public WebElement getGraphInModule(String graph, WebElement module){
        WebElement graphElement = null;
        switch (graph){
            case "Financial Wellness":
                graphElement = module.findElement(By.id("financialWellnessGraph"));
                break;
            case "Participation Rate":
                graphElement = module.findElement(By.id("participantRateGraph"));
            case "Avg. Balance":
                graphElement = module.findElement(By.id("avgParticipantBalanceGraph"));
                break;
            case "Achievement Details":
                graphElement = module.findElement(By.id("avgParticipantAchievementsGraph"));
                break;
        }
        return graphElement;
    }

    public List<String> checkGraphLabelInModule(String graph, String module){
        List<String> result = new ArrayList<String>();
        WebElement moduleElement = getModule(module);
        WebElement graphElement = getGraphInModule(graph, moduleElement);
        List<WebElement> element = graphElement.findElements(By.tagName("text"));
        for(WebElement i : element){
            System.out.println("----------");
            System.out.println(i.getText());
            result.add(i.getText());
        }
        return result;
    }
    
    public String getPlanId(String planName) {
    	int openParensIndex = planName.indexOf("(");
    	int closeParansIndex = planName.indexOf(")", openParensIndex);
    	return planName.substring(openParensIndex + 1, closeParansIndex);
    }
}
