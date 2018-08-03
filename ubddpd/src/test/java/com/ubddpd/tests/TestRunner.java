package com.ubddpd.tests;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "./res/runtime_features", 
		glue = { "com.pru" }, 
		monochrome = true, 
		plugin = {
                "json:./res/testreports/testresult.json",
                "html:./res/testreports/html",
                "pretty"
        }
		// tags = { "@scenario_old_asset_allocations" }
// dryRun = true
)

public class TestRunner {

}
