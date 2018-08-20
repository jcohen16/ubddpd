package com.ubddpd.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import com.ubddpd.utils.ExcelHelper;
import com.ubddpd.utils.Utils;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

public class TestRunnerWrapper {

	public static String currFunctionality = "";
	public static String currIterationNum = "";
	static Logger log = LogManager.getLogger("resultsLogger");

	public static void main(String[] args) throws Exception {

		boolean runFlag = true;

		// delete all runtime feature files since we are creating new ones
		delAllRuntimeFeatFiles();

		File currentPath = new File(".");
		String absPath = currentPath.getAbsolutePath();

		// loop through all functionalities and setup feature files (combine feature
		// files and test data to produce runtime feature files) for each
		for (String s : Utils.retrieveFunctionalities(Setup.RESOURCE_PATH))
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

	protected static void delAllRuntimeFeatFiles() {
		String runtimePath = Setup.RESOURCE_PATH + "/runtime_features/";
		File runtimeFolder = new File(runtimePath);
		if(runtimeFolder.isDirectory()) {

			File[] files = runtimeFolder.listFiles();

			log.info("Deleting all Runtime Feature files.");
			for (File f : files)
				f.delete();
			log.info("All Runtime Features files have been deleted.");
		}
	}

	protected static boolean setupFeatureFile(String functionality) throws IOException {

		currFunctionality = functionality.substring(0,functionality.length()-1);
		
		final String EXAMPLES_TAG = "#@Examples";

		String srcFeatureFilePath = Setup.RESOURCE_PATH + "/features/" + functionality;
		String runtimeFeatFilePath = Setup.RESOURCE_PATH + "/runtime_features/";
		String testDataFilePath = Setup.RESOURCE_PATH + "/testdata/" + functionality;
		final File srcFeatureFolder = new File(srcFeatureFilePath);
		final File runtimeFeatFldr = new File(runtimeFeatFilePath);
		File[] srcFeatureFiles = srcFeatureFolder.listFiles();
		String srcFeatureLine = new String();
		boolean ret = true;
		int scenarioCount = 0;
		ArrayList<String> scenarioNames = new ArrayList<String>();
		String featureFileName = "";

		if(!runtimeFeatFldr.exists()){
			runtimeFeatFldr.mkdir();
		}

		if (srcFeatureFiles == null) {
			log.fatal("'functionality.csv' is either empty or functionality entries are not properly spelled.");
		} 
		// loop through all feature files
		for (File srcFeatureFile : srcFeatureFiles) {
			if (srcFeatureFile.isDirectory())
				continue;

			
			StringBuffer featureLineBuffer = new StringBuffer();

			// open Feature and Excel files
			try {
				PrintWriter runtimeFeatFile = new PrintWriter(runtimeFeatFldr + "/Runtime_"
						+ srcFeatureFile.toString().substring(srcFeatureFile.toString().lastIndexOf("\\") + 1));
				BufferedReader br = new BufferedReader(new FileReader(srcFeatureFile));
				ExcelHelper excelHelper = new ExcelHelper(
						new File(testDataFilePath + functionality.substring(0, functionality.length() - 1) + ".xlsx"));

				log.info("Processing: " + (srcFeatureFile.toString().substring(srcFeatureFile.toString().lastIndexOf("\\") + 1) + " | "
						+ functionality.substring(0, functionality.length() - 1) + ".xlsx"));

				// go through Feature file line by line
				while ((srcFeatureLine = br.readLine()) != null) {
					
					boolean containsExamplesTag = srcFeatureLine.replaceAll("\\s", "").toUpperCase().contains(EXAMPLES_TAG.toUpperCase())
							|| srcFeatureLine.replaceAll("\\s", "").toLowerCase().contains(EXAMPLES_TAG.toLowerCase()); 

					boolean commentedScenarioFlag = srcFeatureLine.replaceAll("\\s", "").contains("#ScenarioOutline:");
					
					if (srcFeatureLine.contains("Feature:") && !srcFeatureLine.replaceAll("\\s", "").contains("#Feature:")) {
						featureFileName = srcFeatureLine.substring(srcFeatureLine.indexOf(":") + 1, srcFeatureLine.length()).trim();
					}

					else if (srcFeatureLine.contains("Scenario Outline:") && !srcFeatureLine.replaceAll("\\s", "").contains("#ScenarioOutline:")) {
						commentedScenarioFlag = false;
						
						String testDataSheetName = functionality.substring(0, functionality.length() - 1);
						if (testDataSheetName.length() > 30) {
							throw new Exception();
						}
						
						String scenarioName = srcFeatureLine.substring(srcFeatureLine.indexOf(":") + 1, 
								srcFeatureLine.length()).trim(); 
						scenarioNames.add(scenarioName);
						
						featureLineBuffer.append(excelHelper.writeExamplesFromXls(testDataSheetName, featureFileName,
								scenarioNames.get(scenarioCount)));
						scenarioCount++;
					}
					
					// replace #@Example with actual test data
					else if (containsExamplesTag && !commentedScenarioFlag) {
						srcFeatureLine = featureLineBuffer.toString();
						featureLineBuffer = new StringBuffer();
					}
					// print those lines to runtime feature file
					runtimeFeatFile.println(srcFeatureLine);
				}
				runtimeFeatFile.flush();
				br.close();
				runtimeFeatFile.close();
				log.info("Processing completed: " + (srcFeatureFile.toString().substring(srcFeatureFile.toString().lastIndexOf("\\") + 1) + " | "
						+ functionality.substring(0, functionality.length() - 1) + ".xlsx"));
			} catch (Exception exp) {
				exp.printStackTrace();
				ret = false;
				break;
			}
		}
		return ret;
	}

	protected static void createReport() {
		DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
		Date dateobj = new Date();
		String projectName = "testresult";
		String testReports = "target/test_reports/";
		File reportOutputDirectory = new File(testReports + df.format(dateobj));
		List<String> jsonFiles = new ArrayList<>();
		jsonFiles.add(testReports + projectName + ".json");

		Configuration configuration = new Configuration(reportOutputDirectory, projectName);
		// addidtional metadata presented on main page
		configuration.addClassifications("Platform", "Windows");
//		configuration.addClassifications("Browser", "Firefox");
//		configuration.addClassifications("Branch", "release/1.0");

		
		ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
		reportBuilder.generateReports();
	}

	protected static void runTests() {
		JUnitCore junit = new JUnitCore();
		Result result = junit.run(TestRunner.class);
		for (Failure failure : result.getFailures()) {
			log.fatal(failure.getMessage());
			log.fatal(failure.getTrace());
		}
	}

}