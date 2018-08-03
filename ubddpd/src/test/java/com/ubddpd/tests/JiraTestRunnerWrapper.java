package com.ubddpd.tests;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubddpd.utils.JiraClient;
import com.ubddpd.utils.Utils;

public class JiraTestRunnerWrapper extends TestRunnerWrapper {
	
	private static Map<String, List<String>> jiraTestSet;
	
	public JiraTestRunnerWrapper() {
		super();
	}
	
	public static void main(String[] args) throws Exception {

		boolean runFlag = true;

		// delete all runtime feature files since we are creating new ones
		delAllRuntimeFeatFiles();
		
		// setJiraTestSet("AGENTS-706");

		// loop through all functionalities and setup feature files (combine feature
		// files and test data to produce runtime feature files) for each
		for (String s : Utils.retrieveFunctionalities(RESOURCE_PATH))
			if (!setupFeatureFile(s + "/"))
				runFlag = false;

		// if all feature files are valid, then run the test.
		if (runFlag) {
			log.info("Running com.ubddpd.tests");
			runTests();
			log.info("Done running com.ubddpd.tests");
		}
		
		createReport();
	}
	
	private static void setJiraTestSet(String testSetId) throws Exception {
		JiraClient client = new JiraClient();
		
		ObjectMapper mapper = new ObjectMapper();
		String configFileContents = Utils.getFileContentsAsString("./res/conf/test_config.json");
		Map<String, Object> testConfig = mapper.readValue(configFileContents, new TypeReference<Map<String, Object>>() {
		});
		
		Map<String, Object> jiraConfig = (Map<String, Object>)testConfig.get("jiraConfig");
        client.setHost((String)jiraConfig.get("host"));
        client.setAuthentication((String)jiraConfig.get("username"), (String)jiraConfig.get("password"));
        jiraTestSet = client.getTestsFromSet(testSetId);
	}
	
}
