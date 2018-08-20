package com.ubddpd.tests;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.Map;

import static com.ubddpd.tests.TestRunnerWrapper.currFunctionality;

public class Hooks {

    static WebDriver driver;
    static Map<String, Object> testConfig;
    static String browser;
    static Logger log = LogManager.getLogger("resultsLogger");

    private Setup setup;

    @Before
    public void doSetup(Scenario sc) throws Exception {
        log.info("Starting Scenario: " + sc.getName());
        Setup setup = new Setup();
        setSetup(setup);
        setup.setScenario(sc);
        driver = setup.launchBrowser();
    }

    @After
    public void cleanup(Scenario sc) throws Exception {
        setup.cleanup(sc, Setup.RESOURCE_PATH, currFunctionality);
        log.info("Scenario " + sc.getName() + " completed. Status: " + sc.getStatus());
    }

    public void setSetup(Setup setup) {
        this.setup = setup;
    }

}
