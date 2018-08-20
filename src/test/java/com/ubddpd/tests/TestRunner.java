package com.ubddpd.tests;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/resources/runtime_features",
		glue = { "com.ubddpd" },
		monochrome = true, 
		plugin = {
                "json:target/test_reports/testresult.json",
                "html:target/test_reports/html",
                "pretty"
        }
		// tags = { "@scenario_all_employees" }
)

public class TestRunner {

}
