package com.ubddpd.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubddpd.utils.TestDataExtractor;
import cucumber.api.Scenario;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ubddpd.utils.Utils.getFileContentsAsString;

public class Setup {

	public static WebDriver driver;
	public static WebDriverWait wait;
	public static Map<String, Object> testConfig;
	public static String browser;
	public static String RESOURCE_PATH = "src/test/resources";

	private static Scenario scenario; 
	private static Logger log = LogManager.getLogger("resultsLogger");

	public Setup(){
		try{
			init();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	private void init() throws Exception {
		String configFileContents = getFileContentsAsString(RESOURCE_PATH + "/conf/test_config.json");
		testConfig = new ObjectMapper().readValue(configFileContents, new TypeReference<Map<String, Object>>() {});
	}

	public void setScenario(Scenario sc) {
		scenario = sc;
	}

	public WebDriver launchBrowser() {
		browser = (String) testConfig.get("browser");
		if (browser.equalsIgnoreCase("Chrome")) {
			driver = getChromeDriver();
		} else if (browser.equalsIgnoreCase("Firefox")) {
			driver = getFirefoxDriver();
		} else if (browser.equalsIgnoreCase("Internet Explorer")) {
			driver = getIEDriver();
		}
		wait = new WebDriverWait(driver, Long.valueOf(testConfig.get("wait").toString()));

		//driver.manage().timeouts().implicitlyWait(Long.valueOf(testConfig.get("implicit_wait").toString()),
		//		TimeUnit.SECONDS);

		driver.manage().window().maximize();

		return driver;
	}

	public void cleanup(Scenario sc, String resourcePath, String functionality) throws Exception {

		Workbook wb = null;
		
		if (sc.isFailed()) {
			// take screenshot if Scenario failed
			takeScreenshot();
		} 

		//Report status to test data sheet
		//Get Feature File Name 
		String ffName = getFeatureFileNameFromScenarioId(sc).toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
		String scenarioName = sc.getName();
		
		File testDataFile = new File(resourcePath + "/testdata/" + functionality + "/" + functionality + ".xlsx");
		
		// Read existing excel data into ScenarioData
		try {
			InputStream inp = new FileInputStream(resourcePath + "/testdata/" + functionality + "/" + functionality + ".xlsx");
		    wb = WorkbookFactory.create(inp);
		} catch (IOException e) {
				e.printStackTrace();
		}
		
		try {
			Sheet sheet = wb.getSheet(functionality);
			int rows = sheet.getLastRowNum() + 1;
		
			int yCount = 0;
			for(int i = 0; i < rows; i++) {
				
				//Check for null row/cell
				if(sheet.getRow(i) == null || sheet.getRow(i).getCell(TestDataExtractor.LAST_RUN_STATUS_CELL_INDEX) == null) {
					continue;
				}
			
				Row row = sheet.getRow(i);
				String ffNameConverted = sheet.getRow(i).getCell(TestDataExtractor.FEATURE_FILE_CELL_INDEX).toString().trim().toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
				String scenarioNameTest = sheet.getRow(i).getCell(TestDataExtractor.SCENARIO_NAME_CELL_INDEX).toString().trim();
				int iterationNum = Integer.parseInt(getIterationNumFromScenarioId(sc));
				iterationNum-=1; //Subtract 1 to get actual
				
				/*
				 * This will check for the Feature name/Scenario Name match, then check if there's a Y in the run column. Then it compares the iteration # to the number of Y's to see if it should put something there.
				 * test this
				 */
				if(sheet.getRow(i).getCell(TestDataExtractor.FEATURE_FILE_CELL_INDEX).toString().trim().toLowerCase().replaceAll("[^a-zA-Z0-9]", "").equals(ffName) && 
						sheet.getRow(i).getCell(TestDataExtractor.SCENARIO_NAME_CELL_INDEX).toString().trim().equals(scenarioName)) {
					if(sheet.getRow(i).getCell(TestDataExtractor.POST_DESC_INDEX).toString().trim().equals("Y")) {
						yCount++;
						if(yCount == iterationNum)
							sheet.getRow(i).getCell(TestDataExtractor.LAST_RUN_STATUS_CELL_INDEX).setCellValue(sc.getStatus());
					}
				}
			}
		
			FileOutputStream fileOut = new FileOutputStream(resourcePath + "/testdata/" + functionality + "/" + functionality + ".xlsx",
				false);
			wb.write(fileOut);
		} catch (Exception e){
			e.printStackTrace();
		}
		// don't close the browser if firefoxConfig.reuse == true
		if (Boolean.valueOf(testConfig.get("closeBrowserAfterRun").toString())) {
			log.info("closeBrowserAfterRun is set to 'true', driver will be closed.");
			driver.quit();
		} else
			log.info("closeBrowserAfterRun is either not set or 'false', driver will NOT be closed");

	}

	private WebDriver getFirefoxDriver() {
		Map<String, Object> firefoxConfig = (Map<String, Object>) testConfig.get("firefoxConfig");
//		System.setProperty("webdriver.firefox.bin", (String) firefoxConfig.get("webdriver.firefox.bin"));
		
		// check if reuse (from test_config.json) is enabled
		if (Boolean.valueOf(firefoxConfig.get("reuse").toString())) {
			log.info("firefoxConfig.reuse is set to 'true', reusing FF session if available.");

			final String firepath = "./../ubddpd/res/plugins/firepath-0.9.7.1-fx.xpi";

			File addOnFile = new File(firepath);
			FirefoxProfile updtdProfile = new FirefoxProfile();
			
			try {
				updtdProfile.addExtension(addOnFile);
			} catch (IOException io) {
				log.info("Could not load plugins, skipping those");
				io.printStackTrace();
			}
			
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability(FirefoxDriver.PROFILE, updtdProfile);
			
			//try an existing FF session.  Create a new one if exception occurs then exit.   
			try { 
				 driver = new RemoteWebDriver(new URL("http://localhost:7055/hub"), capabilities);
			} catch (Exception e1) {
				log.info("Driver failed!  Is runSeleniumServer.bat (./res/bat/) running?  If it is, please disregard.  raBiDD will try to create a new FF-Selenium session.");
				try {
					driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
				} catch (MalformedURLException m) {
					log.fatal("Malformed URL");
					m.printStackTrace();
				}
				log.info("FF is now running.");
			}
		} else {
			log.info("Creating Firefox session");
			driver =  new FirefoxDriver();
		}
		return driver;
	}

	private WebDriver getChromeDriver() {
		Map<String, Object> chromeConfig = (Map<String, Object>) testConfig.get("chromeConfig");
		System.setProperty("webdriver.chrome.driver", (String) chromeConfig.get("webdriver.chrome.driver"));
		ChromeOptions chromeOpt = new ChromeOptions();
		chromeOpt.setExperimentalOption("useAutomationExtension", false);
		chromeOpt.setBinary((String) chromeConfig.get("binaryPath"));
		return new ChromeDriver(chromeOpt);
	}

	private WebDriver getIEDriver() {
		Map<String, Object> ieConfig = (Map<String, Object>) testConfig.get("ieConfig");
		System.setProperty("webdriver.ie.driver", (String) ieConfig.get("webdriver.ie.driver"));

		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, true);
		capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

		return new InternetExplorerDriver(capabilities);
	}

	public static WebDriver getDriver() {
		return driver;
	}

	public static WebDriverWait getWebDriverWait() {
		return wait;
	}

	public static void takeScreenshot() throws IOException {
		// check if page has finished loaded before taking a screenshot
		waitForPage();

		// TODO: replace with a proper wait
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		byte[] screenBytes = FileUtils.readFileToByteArray(screenshot);
		scenario.embed(screenBytes, "image/png");
	}
	
	public static void waitForPage() {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		if (js.executeScript("return document.readyState").toString().equals("complete")) {
			return;
		}

		for (int i = 0; i <  Integer.valueOf(testConfig.get("wait").toString()); i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// check page state
			if ((boolean) js.executeScript("return (document.readyState == 'complete' && jQuery.active == 0)")) {
				break;
			}
		}
	}
	
	private String getFeatureFileNameFromScenarioId(Scenario sc) {
	    String featureName = "";
	    String rawFeatureName = sc.getId().split(";")[0].replace("-"," ");
	    featureName = rawFeatureName.substring(0, 1).toUpperCase() + rawFeatureName.substring(1);

	    return featureName;
	}
	
	private String getIterationNumFromScenarioId(Scenario sc) {
	    String iterNum = sc.getId().split(";")[3].replace("-"," ");

	    return iterNum;
	}

}
