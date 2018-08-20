package com.ubddpd.utils;

import com.ubddpd.tests.Setup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.ubddpd.utils.datastructure.ScenarioData;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestDataExtractor {

	
	// Feature File Name Column Index
	public static int FEATURE_FILE_CELL_INDEX = 0;
	// Scenario Name Column Index
	public static int SCENARIO_NAME_CELL_INDEX = 1;
	// Iteration Num Column Index
	public static int ITERATION_NUM_CELL_INDEX = 2;
	// Iteration Name Column Index
	public static int ITERATION_NAME_CELL_INDEX = 3;				
	//Last Run Status Column Index
	public static int LAST_RUN_STATUS_CELL_INDEX = 4;
	// Column index after feature file, scenario name, iteration columns
	public static int POST_DESC_INDEX = 5;
	// Header name for post description index column
	public static String POST_DESC_HEADER = "Run";
	
	static Logger logger = LogManager.getLogger("rabiddLogger");

	/*
	 * Don't run from here any more. Run from ProjectChooser.java
	 */

	public static void main(String[] args) throws IOException {
		try {
			System.out.println("Resource Path Set: " + args[0]);
		} catch (Exception e) {
			logger.fatal("Test Data Extraction cancelled: No argument passed to Test Data Extractor. "
					+ "Check ProjectChooser.java is passing a value or make sure you're not running this java file");
			return;
		}
		
		String RESOURCE_PATH = args[0];
				
		if(RESOURCE_PATH.equals("")) {
			logger.fatal("Test Data Extraction cancelled: Resource path not defined. Check ProjectChooser.java");
			return;
		}
		
		for (String s : Utils.retrieveFunctionalities(RESOURCE_PATH)) {
			try {
				extractTestData(s);
			} catch (Exception e) {
				logger.fatal("Check Feature file (and it's path) and test data folder.");
				e.printStackTrace();
			}
		}
		
		logger.info("Test data extraction has been completed.");
	}

	public static void extractTestData(String functionality) throws IOException {

		String featureFilesName = Setup.RESOURCE_PATH + "/features/" + functionality;
		String testdataFiles = Setup.RESOURCE_PATH + "/testdata/" + functionality;
		final File featureFolder = new File(featureFilesName);
		final File testDataFolder = new File(testdataFiles);
		File[] featureFiles = featureFolder.listFiles();

		List<ScenarioData> scenarioDataOld = new ArrayList<ScenarioData>();
		List<ScenarioData> scenarioDataNew = new ArrayList<ScenarioData>();
		int scenarioCount = 0;
		Workbook wb = null;
		String line = new String();

		logger.info("Processing: " + functionality + ".xlsx");
		// check if file already exists for this functionality
		File file = new File(testDataFolder.toString() + "/" + functionality + ".xlsx");
		if (file.exists()) {

			// Read existing excel data into ScenarioData
			try {
				wb = new XSSFWorkbook(testDataFolder.toString() + "/" + functionality + ".xlsx");
			} catch (IOException e) {
				e.printStackTrace();
			}

			Sheet sheet = wb.getSheet(functionality);
			int rows = sheet.getLastRowNum() + 1;
			HashMap<String, HashMap<String, String>> pDataSet = new HashMap<String, HashMap<String, String>>();
			HashMap<String, String> iterationNames = new HashMap<String,String>();
			HashMap<String, String> runData = new HashMap<String,String>();
			HashMap<String, String> paramValues = null;

			boolean parameterRow = true;
			ScenarioData curr = new ScenarioData("", "");
			// Read rows
			for (int i = 0; i < rows + 1; i++) {
				paramValues = new HashMap<String, String>();
				// Break if row is empty to avoid exception
				if (sheet.getRow(i) == null || sheet.getRow(i).getCell(POST_DESC_INDEX) == null) {
					curr.setParameterData(pDataSet);
					curr.setIterationName(iterationNames);
					curr.setRunData(runData);
					pDataSet = new HashMap<String, HashMap<String, String>>();
					iterationNames = new HashMap<String,String>();
					runData = new HashMap<String,String>();
					scenarioDataOld.add(curr);
					continue;
				}

				Row row = sheet.getRow(i);
				int cols = row.getLastCellNum();

				// Check for break line row (Next Scenario)
				if (row.getCell(POST_DESC_INDEX) != null) {

					if (row.getCell(POST_DESC_INDEX).toString().trim().equals(POST_DESC_HEADER)) {
						parameterRow = true;
					}
				}
				// Get parameters on first row for this scenario
				if (parameterRow) {
					curr = new ScenarioData("", ""); // Set up new Scenario Data
					// pRow = row; //Set the row which contains the parameters
					for (int j = POST_DESC_INDEX; j < cols; j++) {
						if (row.getCell(j) == null)
							continue;
						if (!row.getCell(j).toString().equals(""))
							curr.addParameter(row.getCell(j).toString().trim());
					}
					parameterRow = false;
					continue;
				}

				// Start Scenario Row
				if (!parameterRow) {
					curr.setFeatureName(row.getCell(FEATURE_FILE_CELL_INDEX).toString().trim());
					curr.setScenarioName(row.getCell(SCENARIO_NAME_CELL_INDEX).toString().trim());
					
					logger.info("Processing: " + curr.getFeatureName() + " (Feature Name), " + curr.getScenarioName()
							+ " (Scenario Name) > " + functionality + ".xlsx");
					
					String iterationNum = row.getCell(ITERATION_NUM_CELL_INDEX).toString().trim();
					String iterationName = row.getCell(ITERATION_NAME_CELL_INDEX).toString().trim();
					String runStatus = row.getCell(LAST_RUN_STATUS_CELL_INDEX).toString().trim();
					// Generate list of parameter and value pairs
					for (int j = POST_DESC_INDEX; j < cols; j++) {
						paramValues.put(curr.getParameters().get(j - POST_DESC_INDEX),
								row.getCell(j, Row.CREATE_NULL_AS_BLANK).toString().replace(".0", "")
										.trim());
					}
					pDataSet.put(iterationNum, paramValues);
					iterationNames.put(iterationNum, iterationName);
					runData.put(iterationNum, runStatus);
				}
			}
		}

		String featureFileName = "";
		ScenarioData currScenario = null;
		Row row = null;

		// For NEW Data, check all feature files
		for (File featureFile : featureFiles) {
			if (featureFile.isDirectory())
				continue;
			// open Feature file
			try {
				BufferedReader br = new BufferedReader(new FileReader(featureFile));
				// go through Feature file line by line and store the data for each scenario in
				// ScenarioData
				while ((line = br.readLine()) != null) {

					// create sheetname and first row
					// skip if Scenario Outline is commented
					if (line.contains("Feature:")) {
						featureFileName = line.substring(line.indexOf(":") + 1).trim();
					}

					if (line.contains("Scenario Outline") && !line.replaceAll("\\s", "").contains("#ScenarioOutline")) {
						scenarioDataNew
								.add(new ScenarioData(featureFileName, line.substring(line.indexOf(":") + 1).trim()));
						currScenario = scenarioDataNew.get(scenarioCount++);
						currScenario.addParameter(POST_DESC_HEADER);
					}

					// extract values between < and > and put them on a cell
					if (line.matches(".*<.*>.*")) {
						// Skip commented lines and Login step
						if (line.trim().startsWith("#"))
							continue;
						Pattern p = Pattern.compile("<.*?>");
						Matcher m = p.matcher(line);

						while (m.find()) {
							// only add unique parameters
							if (!currScenario.getParameters().contains(m.group(0).substring(1, m.group(0).length() - 1).toString()))
								currScenario.addParameter(m.group(0).substring(1, m.group(0).length() - 1));

						}
					}

				}

				br.close();
			} catch (Exception exp) {
				exp.printStackTrace();
				break;
			}
		}

		if (scenarioDataOld.size() != 0) {
			// Add old scenario data to new
			for (int i = 0; i < scenarioDataNew.size(); i++) {
				ScenarioData newScen = scenarioDataNew.get(i);
				ScenarioData oldScen = null;
				boolean found = false;
				// Compare parameter lists
				for (int k = 0; k < scenarioDataOld.size(); k++) {
					oldScen = scenarioDataOld.get(k);
					if (oldScen.getScenarioName().equals(newScen.getScenarioName())) {
						found = true;
						break;
					}
				}
				if (!found) {
					scenarioDataOld.add(newScen);
					continue;
				}
				for (int j = 0; j < scenarioDataNew.get(i).getParameters().size(); j++) {
					String newParameter = scenarioDataNew.get(i).getParameters().get(j);
					if (!oldScen.getParameters().contains(newParameter))
						oldScen.addNewParameterToData(newParameter);
				}
			}
			scenarioDataNew = scenarioDataOld;
		}

		try {
			int rowCount = 0;
			wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet(functionality);
			for (ScenarioData d : scenarioDataNew) {
				// Store Parameter Data/List for ease of use
				HashMap<String, HashMap<String, String>> paramData = d.getParameterData();
				HashMap<String, String> iterationList = d.getIterationNames();
				HashMap<String, String> runData = d.getRunData();
				List<String> params = d.getParameters();
				row = sheet.createRow(rowCount++);
				int paramCount = 0;

				// Enter Parameters for this Scenario
				for (String s : params) {
					// Make Headers
					if (rowCount == 1) {
						row.createCell(FEATURE_FILE_CELL_INDEX ).setCellValue("Feature File");
						row.createCell(SCENARIO_NAME_CELL_INDEX ).setCellValue("Scenario Name");
						row.createCell(ITERATION_NUM_CELL_INDEX).setCellValue("Iteration Num");
						row.createCell(ITERATION_NAME_CELL_INDEX).setCellValue("Iteration Name");
						row.createCell(LAST_RUN_STATUS_CELL_INDEX).setCellValue("Last Run Status");
					}
					row.createCell(POST_DESC_INDEX + paramCount++).setCellValue(s);
				}

				// No Param data, newly added feature
				if (paramData.size() == 0) {
					row = sheet.createRow(rowCount++);
					row.createCell(FEATURE_FILE_CELL_INDEX ).setCellValue(d.getFeatureName());
					row.createCell(SCENARIO_NAME_CELL_INDEX ).setCellValue(d.getScenarioName());
					row.createCell(ITERATION_NUM_CELL_INDEX).setCellValue("Iteration 1");
					row.createCell(ITERATION_NAME_CELL_INDEX).setCellValue(" ");
					row.createCell(LAST_RUN_STATUS_CELL_INDEX).setCellValue("NO RUN");
					row.createCell(POST_DESC_INDEX).setCellValue("N");
				}

				// New Row for Scenario/Iteration Section
				for (int i = 0; i < paramData.size(); i++) {
					row = sheet.createRow(rowCount++);
					String iteration = "Iteration " + (i + 1); // Iteration to update
					String iterationName = iterationList.get(iteration);
					String runStatus = runData.get(iteration);
					row.createCell(FEATURE_FILE_CELL_INDEX ).setCellValue(d.getFeatureName());
					row.createCell(SCENARIO_NAME_CELL_INDEX ).setCellValue(d.getScenarioName());
					row.createCell(ITERATION_NUM_CELL_INDEX).setCellValue(iteration);
					row.createCell(ITERATION_NAME_CELL_INDEX).setCellValue(iterationName);
					row.createCell(LAST_RUN_STATUS_CELL_INDEX).setCellValue(runStatus);
					for (int j = POST_DESC_INDEX; j < paramData.get(iteration).size() + POST_DESC_INDEX; j++) {
						row.createCell(j).setCellValue(paramData.get(iteration).get(params.get(j - POST_DESC_INDEX)));
					}
				}

				// Break row for next Scenario
				rowCount++;
			}

			FileOutputStream fileOut = new FileOutputStream(testDataFolder.toString() + "/" + functionality + ".xlsx",
					false);
			wb.write(fileOut);
			logger.info("Processing Completed: " + functionality  + ".xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
