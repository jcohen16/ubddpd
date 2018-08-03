package com.ubddpd.stepdef;

import com.ubddpd.tests.Setup;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import com.ubddpd.homepage.B2BHomePage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import com.ubddpd.planhealth.NonPortalLoginPage;
import com.ubddpd.planhealth.PlanHealth;
import com.ubddpd.planhealth.PlanHealthUtils;
import com.ubddpd.utils.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class PlanHealthSteps {

	private Map<String, Object> testConfig;
    private PlanHealth planHealthPage;
    private B2BHomePage homePage;
    private NonPortalLoginPage nonPortalLoginPage;
    private String planName;

    public PlanHealthSteps() throws Exception {
        testConfig = Setup.getTestConfig();
    }
    
    private void setPlanHealthPage(){
        if(planHealthPage == null){
        	try {
        		planHealthPage = new PlanHealth();
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
    }

    private void setHomePage() {
        if(homePage == null){
        	try {
        		homePage = new B2BHomePage();	
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
    }
    
    private void setNonPortalLoginPage() {
        if(nonPortalLoginPage == null){
        	try {
        		nonPortalLoginPage = new NonPortalLoginPage();	
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
    }
    
    @Given("^I login to Plan Health Non-Portal page site as User '(.*)' with SSO Id '(.*)' and Universal Plan Id '(.*)'$")
    public void loginNonPortalSite(String userXid, String userSsoId, String univPlanId) throws IOException {
    	B2BHomePage homePage = new B2BHomePage();
    	homePage.open("plan_health_non_portal_url");
    	
    	setNonPortalLoginPage();
    	nonPortalLoginPage.loginNonPortalSite(userXid, userSsoId, univPlanId);
    }
    
    @Given("^I login to Plan Health Non-Portal page site as User Type '(.*)' with XID '(.*)' with SSO Id '(.*)' and Universal Plan Id '(.*)'$")
    public void loginNonPortalSiteUserType(String userType, String userXid, String userSsoId, String univPlanId) throws IOException {
    	B2BHomePage homePage = new B2BHomePage();
    	homePage.open("plan_health_non_portal_url");
    	
    	setNonPortalLoginPage();
    	nonPortalLoginPage.loginNonPortalSiteUserType(userType, userXid, userSsoId, univPlanId);
    }

    @Then("^I validate the Page Header and Footer elements including '(.*)' '(.*)' and '(.*)' '(.*)'$")
    public void validatePageHeaderPerFeatureScenario(String key1, String val1, String key2, String val2) throws Exception {
        setPlanHealthPage();
        FileInputStream fis = new FileInputStream("res/testdata/PlanHealth/PlanHealth.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        fis.close();
        XSSFSheet worksheet = workbook.getSheet("CommonComponents");
        int numRows = worksheet.getPhysicalNumberOfRows();
        List<List<String>> expectedFields = new ArrayList<>();
        for(int i=0; i<numRows; i++){
            XSSFRow row = worksheet.getRow(i);
            XSSFCell cell = row.getCell(0);
            String firstCellTxt = cell.getStringCellValue();
            if(!firstCellTxt.equals("Common Components") && !firstCellTxt.equals("Application Header") 
            	&& !firstCellTxt.equals("Field Text") && !firstCellTxt.isEmpty()){
                List<String> newList = new ArrayList<>();
                String fieldTxt = firstCellTxt;
                if(fieldTxt.equals(key1)) {
                	fieldTxt = val1;
                } else if(fieldTxt.equals(key2)) {
                	fieldTxt = val2;
                }
                newList.add(fieldTxt);
                newList.add(row.getCell(1).getStringCellValue());
                expectedFields.add(newList);
            }
        }
        validateHeaderAgainstList(expectedFields);
    }
    
    @Then("^I validate the Page Header and Footer elements including the title for '(.*)' '(.*)', '(.*)' '(.*)' and '(.*)' '(.*)'$")
    public void validatePageHeaderPerFeatureScenario3KeyValue(String key1, String val1, String key2, 
    		String val2, String key3, String val3) throws Exception {
    	setPlanHealthPage();
        FileInputStream fis = new FileInputStream("res/testdata/PlanHealth/PlanHealth.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        fis.close();
        XSSFSheet worksheet = workbook.getSheet("CommonComponents");
        int numRows = worksheet.getPhysicalNumberOfRows();
        List<List<String>> expectedFields = new ArrayList<>();
        for(int i=0; i<numRows; i++){
            XSSFRow row = worksheet.getRow(i);
            XSSFCell cell = row.getCell(0);
            String firstCellTxt = cell.getStringCellValue();
            if(!firstCellTxt.equals("Common Components") && !firstCellTxt.equals("Application Header") 
            	&& !firstCellTxt.equals("Field Text") && !firstCellTxt.isEmpty()){
                List<String> newList = new ArrayList<>();
                String fieldTxt = firstCellTxt;
                if(fieldTxt.contains(key1)) {
                	key1 = key1.replaceAll("\\[", "\\\\[");
                	key1 = key1.replaceAll("\\]", "\\\\]");
                	fieldTxt = fieldTxt.replaceAll(key1, val1);
                } else if(fieldTxt.contains(key2)) {
                	key2 = key2.replaceAll("\\[", "\\\\[");
                	key2 = key2.replaceAll("\\]", "\\\\]");
                	fieldTxt = fieldTxt.replaceAll(key2, val2);
                }
                else if(fieldTxt.contains(key3)) {
                	key3 = key3.replaceAll("\\[", "\\\\[");
                	key3 = key3.replaceAll("\\]", "\\\\]");
                	fieldTxt = fieldTxt.replaceAll(key3, val3);
                }
                newList.add(fieldTxt);
                newList.add(row.getCell(1).getStringCellValue());
                expectedFields.add(newList);
            }
        }
        validateHeaderAgainstList(expectedFields);
    }
    

    @Then("^the H1 tag plan name '(.*)' appears under Prudential logo$")
    public void validatePlanNameOnPage(String planName){
        setPlanHealthPage();
        Assert.assertEquals(planName + " plan name not present", planName, planHealthPage.getPlanName());
        Assert.assertEquals("Plan name is not in H1 tag", "h1", planHealthPage.getPlanNameTag());
    }

    @Then("^H1 tag '(.*)' header appears under the plan name$")
    public void validatePlanHealthOnPage(String heading) {
    	setPlanHealthPage();
    	Assert.assertEquals(heading + " page title not present", heading, planHealthPage.getPlanHealth());
        Assert.assertEquals("Plan health is not in H1 tag", "h1", planHealthPage.getPlanHealthTag());
    }
    
    @Then("^the (.*) drop down appears under the 'Plan Health' header$")
    public void validateViewDropdownOnPage(String dropdown) {
    	setPlanHealthPage();
        Assert.assertTrue("View dropdown not present under Plan Health heading", planHealthPage.headerDropdownPresent(dropdown));
    }

    @Then("^the H2 Tag '(.*)' appears under the View drop down$")
    public void validatePageTitle(String heading){
        setPlanHealthPage();
        Assert.assertEquals(heading + " page title not present", heading, planHealthPage.getPageTitle());
        Assert.assertEquals("Plan Summary is not in H2 tag", "h2", planHealthPage.getPageTitleTag());
    }
    
    private void validateHeaderAgainstList(List<List<String>> data){
        for (int i = 0; i < data.size(); i++){
            switch(data.get(i).get(1)){
                case "Header Link":
                    Assert.assertTrue(data.get(i).get(0) + " link not present", planHealthPage.headerLinkPresent(data.get(i).get(0)));
                    break;
                case "Dropdown":
                    Assert.assertTrue(data.get(i).get(0) + " dropdown not displayed", planHealthPage.headerDropdownPresent(data.get(i).get(0)));
                    break;
                case "Header Text":
                    Assert.assertEquals(data.get(i).get(0) + " text not displayed", data.get(i).get(0), planHealthPage.getHeaderText());
                    break;
                case "Plan Name":
                    Assert.assertEquals(data.get(i).get(0) + " plan name not displayed", data.get(i).get(0), planHealthPage.getPlanName());
                    break;
                case "Plan Health":
                	Assert.assertEquals(data.get(i).get(0) + " plan name not displayed", data.get(i).get(0), planHealthPage.getPlanHealth());
                    break;
                case "Image Link":
                    Assert.assertTrue(data.get(i).get(0) + " not displayed", planHealthPage.prudentialLogoPresent());
                    break;
                case "Logo Text":
                    Assert.assertEquals(data.get(i).get(0) + " not displayed", data.get(i).get(0), planHealthPage.getLogoText());
                    break;
            }
        }
    }
    
    //----------------------
    //----Old code below----
    //----------------------
    
    @Given("^I click on the link to Plan '(.*)'$")
    public void clickPlanLink(String planName){
        setHomePage();
        // setB2bPlan(planName);
        this.planName = planName;
        homePage.clickPlanLink(planName);
    }

    @Given("^I click on the '(.*)' button$")
    public void clickButton(String buttonName){
        setPlanHealthPage();
        switch (buttonName){
            case "Manage Plan":
                planHealthPage.clickManagePlanButton();
                break;
            case "Your Settings":
                planHealthPage.clickYourSetting();
                break;
            case "Select Plan":
                planHealthPage.clickSelectPlan();
                break;
        }
    }

    @Given("^I click on the '(.*)' button on the old Plan Health Page$")
    public void clickButtonOldPage(String buttonName){
        setPlanHealthPage();
        if(buttonName.equals("Manage Plan")){
            planHealthPage.clickManagePlanButtonOldPage();
        }
    }

    @Given("^I click on the '(.*)' link under the '(.*)' header$")
    public void clickMngPlanLinkUnderHeader(String link, String header){
        setPlanHealthPage();
        planHealthPage.clickMngPlanLinkUnderHeader(link, header);
    }

    @Then("^the '(.*)' page should appear$")
    public void validatePageAppearsWithTitle(String pageTile){
    	setPlanHealthPage();
        Assert.assertEquals("Incorrect page title appeared", pageTile, planHealthPage.getTitle());
    }

    @Then("^header should display following items$")
    public void validateApplicationHeaderItems(DataTable arg) {
        setPlanHealthPage();
        List<List<String>> data = arg.raw();
        validateHeaderAgainstList(data);
    }

    @Then("^footer should display following items$")
    public void validateApplicationFooterItems(DataTable arg) {
        setPlanHealthPage();
        List<List<String>> data = arg.raw();
        for (int i = 0; i < data.size(); i++) {
            Assert.assertTrue(data.get(i).get(0) + " link not present", planHealthPage.footerLinkPresent(data.get(i).get(0)));
        }
    }

    @Then("^the disclaimer text should be as expected$")
    public void validateFooterDisclaimerText() throws Exception {
        setPlanHealthPage();
        String expectedText = Utils.getFileContentsAsString("src/test/resources/data/footer_disclaimer.txt");
        String expectedLines[] = expectedText.split("\n");

        List<String> actualTextList = planHealthPage.getFooterDisclaimerLines();
        String actualLines[] = actualTextList.toArray(new String[actualTextList.size()]);

        for(int i=0; i<expectedLines.length; i++){
            String expectedLine = expectedLines[i];
            String actualLine = actualLines[i];
            if(!expectedLine.isEmpty()){
                Assert.assertEquals("Footer Disclaimer line " + (i+1) + " is not as expected", expectedLine, actualLine);
            }
        }
    }

    @Given("^user navigate to Dev URL$")
    public void navigateToDevURL() throws IOException {
    	B2BHomePage homePage = new B2BHomePage();
        homePage.open("dev_url");
    }

    @Then("^following link displayed under the '(.*)' header$")
    public void validateManagePlanLinkUnderHeader(String heading, DataTable table){
        setPlanHealthPage();
        List<List<String>> data = table.raw();
        for (int i = 0; i < data.size(); i++) {
            Assert.assertTrue(data.get(i).get(0) + " link not present", planHealthPage.managePlanMenuLinksPresentUnderHeading(heading, data.get(i).get(0)));
        }
    }

    @Then("^following fields displayed under the '(.*)' header$")
    public void validateManagePlanFieldUnderHeader(String heading, DataTable table) {
        setPlanHealthPage();
        List<List<String>> data = table.raw();
        for (int i = 0; i < data.size(); i++) {
            switch (data.get(i).get(1)) {
                case "Dropdown":
                    Assert.assertTrue(data.get(i).get(0) + " dropdown not present", planHealthPage.managePlanMenuDropdownPresent(heading, data.get(i).get(0)));
                    break;
                case "Text":
                    Assert.assertTrue(data.get(i).get(0) + " text not present", planHealthPage.managePlanMenuTextPresent(heading, data.get(i).get(0)));
                    break;
                case "Text box":
                    Assert.assertTrue(data.get(i).get(0) + " text box not present", planHealthPage.managePlanMenuTextboxPresent(heading, data.get(i).get(0)));
                    break;
                case "Button":
                    Assert.assertTrue(data.get(i).get(0) + " button not present", planHealthPage.managePlanMenuButtonPresent(heading, data.get(i).get(0)));
                    break;
                case "Text heading":
                    Assert.assertTrue(data.get(i).get(0) + " text heading not present", planHealthPage.managePlanMenuTextHeadingPresent(heading, data.get(i).get(0)));
                    break;
            }
        }
    }

    @Then("^the disclaimers on the Sponsor Center Page should match the expected$")
    public void validateSponsorCenterDisclaimers() throws Exception {
        String expectedText = Utils.getFileContentsAsString("src/test/resources/data/footer_disclaimer.txt");
        String expectedLines[] = expectedText.split("\n");

        setHomePage();
        List<String> actualTextList = homePage.getFooterDisclaimerLines();
        String actualLines[] = actualTextList.toArray(new String[actualTextList.size()]);

        for(int i=0; i<expectedLines.length; i++){
            String expectedLine = expectedLines[i];
            String actualLine = actualLines[i];
            Assert.assertEquals("Footer Disclaimer line " + (i+1) + " is not as expected", expectedLine, actualLine);
        }
    }

    @Then("^module with header '(.*)' is displayed on the page$")
    public void validateModuleWithHeading(String moduleHeading){
        setPlanHealthPage();
        Assert.assertTrue("Module with heading: " + moduleHeading + " not present", planHealthPage.moduleWithHeadingPresent(moduleHeading));
    }

    @Then("^text '(.*)' is displayed in module '(.*)'")
    public void validateModuleHeadingText(String text, String module){
        setPlanHealthPage();
        Assert.assertTrue("Module: " + module + " does not contain text: " + text, planHealthPage.checkModuleForHeadingText(text, module));
    }

    @Then("^link '(.*)' is displayed in module '(.*)'$")
    public void validateModuleLinks(String link, String module){
        setPlanHealthPage();
        Assert.assertTrue("Container link: " + link + " not present", planHealthPage.linkPresentInModule(link, module));
    }

    @When("^I click on the link '(.*)' in '(.*)' module")
    public void clickLink(String link, String module){
        setPlanHealthPage();
        planHealthPage.clickLinkInModule(link, module);
    }

    @Then("^module '(.*)' contains following graphs headings:$")
    public void validateGraphHeadingInModule(String module, DataTable table){
        setPlanHealthPage();
        List<List<String>> data = table.raw();
        for (int i = 0; i < data.size(); i++) {
            Assert.assertTrue(data.get(i).get(0) + " graph heading not present in module " + module, planHealthPage.moduleGraphHeadingPresent(module, data.get(i).get(0)));
        }
    }

    @Then("^module '(.*)' contains following graph:$")
    public void validateGraphInModule(String module, DataTable table){
        setPlanHealthPage();
        List<List<String>> data = table.raw();
        for (int i = 0; i < data.size(); i++) {
            Assert.assertTrue(data.get(i).get(0) + " graph not present", planHealthPage.graphPresentInModule(module, data.get(i).get(0)));
        }
    }

    @Then("^Financial Wellness bar graph contains:$")
    public void validateBarGraph(DataTable table){
        setPlanHealthPage();
        List<List<String>> data = table.raw();
    }

    
    @Then("^(.*) dropdown is displayed$")
    public void validateDropdownOnPage(String dropDown){
        setPlanHealthPage();
        Assert.assertTrue(dropDown + " dropdown not present", planHealthPage.headerDropdownPresent(dropDown));
    }

    @Then("^I should see following items under Your Settings:$")
    public void validateYourSettingsItems(DataTable table){
        setPlanHealthPage();
        List<String> actualYourSettings = planHealthPage.getYourSettingItems();
        List<List<String>> expectedYourSettings = table.raw();

        for(int i=0; i<expectedYourSettings.size(); i++){
            String expectedYourSetting = expectedYourSettings.get(i).get(0);
            String actualYourSetting = actualYourSettings.get(i);
            Assert.assertEquals("Your Settings item " + (i) + " is not as expected", expectedYourSetting, actualYourSetting);
        }
    }

    @Given("^the '(.*)' option is set to '(.*)'$")
    public void validateYourSettings(String itemName, String itemStatus) {
        setPlanHealthPage();
        Assert.assertTrue("Your Settings item " + itemName + " is not in status " + itemStatus, planHealthPage.checkYourSettingStatus(itemName, itemStatus));
    }

    @When("^I change the '(.*)' option to '(.*)'$")
    public void changeYourSettings(String itemName, String changeStatusTo) {
        setPlanHealthPage();
        planHealthPage.clickYourSettingItems(itemName);
    }

    @Then("^container '(.*)' should (.*) visible$")
    public void validateContainerPresence(String itemName, String visiblityStatus) {
        setPlanHealthPage();
        switch(visiblityStatus){
            case "not be":
                Assert.assertFalse("Your Settings item " + itemName + " is visible", planHealthPage.containerWithHeading(itemName));
                break;
            case "be":
                Assert.assertTrue("Your Settings item " + itemName + " is not visible", planHealthPage.containerWithHeading(itemName));
                break;
        }
    }

    @Then("^(.*) dropdown is displayed with selected option '(.*)'$")
    public void validateDropdownWithSelectedOption(String dropdown, String selectedOption){
        setPlanHealthPage();
        Assert.assertEquals("dropdown " + dropdown + " is not visible with selected option " + selectedOption,  selectedOption, planHealthPage.dropdownWithSelectedOptionPresent(dropdown));
    }

    @When("^user click on the '(.*)' dropdown$")
    public void selectDropdown(String dropdown){
        setPlanHealthPage();
        planHealthPage.clickDropdown(dropdown);
    }

    @When("^user select '(.*)' from (.*) dropdown$")
    public void selectOptioByTextInDropdown(String option, String dropdown){
        setPlanHealthPage();
        planHealthPage.selectDropdownOptionByText(option, dropdown);
    }

    @Then("^following options are displayed under (.*) dropdown:$")
    public void validateDropdownOptions(String dropdown, DataTable table){
        setPlanHealthPage();
        String options = planHealthPage.getDropdownOptions(dropdown);
        String actualOptions[] = options.split("\n");
        List<List<String>> expectedOptions = table.raw();
        for(int i=0; i<expectedOptions.size(); i++){
            String expectedOption = expectedOptions.get(i).get(0);
            String actualOption = actualOptions[i];
            Assert.assertEquals("Dropdown " + dropdown + " does not have option " + expectedOption, expectedOption, actualOption);
        }
    }

    @Then("^'(.*)' module is displayed on old Plan Health Page$")
    public void validateModuleOldPage(String module){
        setPlanHealthPage();
        Assert.assertTrue("Module: " + module + " is not displayed on old Plan Health Page.", planHealthPage.moduleOnOldPagePresent(module));
        Assert.assertFalse("There was an error loading the data.", planHealthPage.moduleDataLoaded(module));
    }

    @Then("^the title of the Asset Allocation module is '(.*)'$")
    public void validateAssetAllocModuleTitle(String expectedTitle){
        setPlanHealthPage();
        String actualTitle = planHealthPage.getAssetAllocModuleTitle();
        Assert.assertEquals("Asset Allocation Module title was not as expected", expectedTitle, actualTitle);
    }

    @Then("^there is an arrow icon in the top header$")
    public void validateArrowIconPresent(){
        setPlanHealthPage();
        String url = (String)testConfig.get("businesscenter_url");
        String envBaseUrl = url.substring(0, url.lastIndexOf("/"));
        boolean iconIsPresent = planHealthPage.assetAllocArrowIconPresent(envBaseUrl);
        Assert.assertTrue("Asset Allocation Arrow Icon not present as expected to be", iconIsPresent);
    }

    @Then("^there is a text heading with the text '(.*)'")
    public void validateAssetAllocHeadingTxt(String expectedTxt){
        setPlanHealthPage();
        String actualTxt = planHealthPage.getAssetAllocHeadingTxt();
        Assert.assertEquals("Asset Allocation Module heading not as expected", expectedTxt, actualTxt);
    }

    @Then("^following items are displayed under Asset Allocation module on old Plan Health Page$")
    public void validateAssetAllocationOldPage(DataTable table){
        setPlanHealthPage();
        List<List<String>> data = table.raw();
        for(int i=0; i<data.size(); i++){
            Assert.assertTrue("Field: " + data.get(i).get(0) + " - " + data.get(i).get(1) +
                    " is not displayed under Asset Allocation module on old Plan Health Page.",
                    planHealthPage.assetAllocationFieldsOnOldPagePresent(data.get(i).get(0), data.get(i).get(1)));
        }
    }

    @Given("^I store the Asset Allocation Details for Plan '(.*)' based on the ESB Service$")
    public void setAssetAllocationDetails(String planName) throws Exception {
    	setPlanHealthPage();
        String planId = planHealthPage.getPlanId(planName);
        planHealthPage.setESBAssetAllocationDetails(planId);
    }
    
    @Then("^a text field showing 'As of MM/DD/YY' appears with the same date returned by ESB$")
    public void validateAssetAllocAsOfDate(){
        String expectedTxt = "As of " + planHealthPage.getESBAssetAllocAsOfDate();
        String actualTxt = planHealthPage.getAssetAllocAsOfDate();
        Assert.assertEquals("Asset Allocation As of Date not as expected", expectedTxt, actualTxt);
    }

    @Then("the Asset Allocation module has the expected values based on ESB and colors for this Plan on the old Plan Health Page$")
    public void validateAssetAllocationModuleOldPage(DataTable table) throws Exception {
        setPlanHealthPage();
        List<List<String>> data = table.raw();
        List<String> actualSummaries = planHealthPage.assetAllocationSummaryOnOldPagePresent();
        List<String> actualColorCodes = planHealthPage.checkAssetAllocationColor();
        Map<String, String> expectedSummaries = planHealthPage.getExpectedAssetAllocSummaries();
        for(int i=0; i<data.size(); i++){
            List<String> expectedDatum = data.get(i);
            String assetClassName = expectedDatum.get(0);
            String expectedSummary = expectedSummaries.get(assetClassName);
            String expectedColor = expectedDatum.get(1);
            String actualSummary = actualSummaries.get(i);
            String actualColor = actualColorCodes.get(i);
            Assert.assertEquals("Summary: " + expectedSummary + " is not displayed under Assert Allocation" +
                    " module on old Plan Health Page as expected.", expectedSummary, actualSummary);
            Assert.assertEquals("Color code validation failed for element" + (i+1), expectedColor,
                    PlanHealthUtils.getColorCode(actualColor));
        }
    }

    @Then("^graph '(.*)' have following fields in '(.*)' module$")
    public void validateGraphLabelInModule(String graph, String module, DataTable table){
        setPlanHealthPage();
        List<String> actualFields = planHealthPage.checkGraphLabelInModule(graph, module);
        List<List<String>> expectedFields = table.raw();
        for(int i = 0; i < expectedFields.size(); i++){
            String actualField = actualFields.get(i);
            String expectedField = expectedFields.get(i).get(0);
            Assert.assertEquals("Label: " + actualField + " not present", actualField, expectedField);
        }
    }
}
