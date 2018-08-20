package com.ubddpd.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExcelHelper {

	private static File input;
	static Logger logger = LogManager.getLogger("resultsLogger");

	public ExcelHelper(File file) throws FileNotFoundException {
		input = file;
	}

	public StringBuffer writeExamplesFromXls(String sheetName, String featureName, String scenarioName)
			throws IOException, InvalidFormatException {
		
		// Feature File Name Column Index
		final int FEATURE_FILE_CELL_INDEX = 0;
		// Scenario Name Column Index
		final int SCENARIO_NAME_CELL_INDEX = 1;
		//Iteration Name Column Index
		final int ITERATION_NAME_CELL_INDEX = 3;
		// Column index after feature file, scenario name, iteration columns
		final int POST_DESC_INDEX = 5;
		// Header name for post description index column
		final String POST_DESC_HEADER = "Run";

		StringBuffer str = new StringBuffer();
		XSSFWorkbook workbook = new XSSFWorkbook(input.getPath());
		XSSFSheet sheet = workbook.getSheet(sheetName);
		int rows = sheet.getLastRowNum() + 1;

		str.append("    Examples:\n");
		str.append("      ");

		boolean paramFlag = false;
		int cols = 0;

		for (int x = 0; x < rows; x++) {

			// skip if row is null
			if (null == sheet.getRow(x) || null == sheet.getRow(x).getCell(POST_DESC_INDEX)) {
				continue;
			}

			// check if data under POST_DESC_HEADER column is empty since, we don't want to
			// run com.ubddpd.tests with empty data on POST_DESC_HEADER
			if (null == (sheet.getRow(x).getCell(POST_DESC_INDEX)))
				logger.fatal(POST_DESC_HEADER + "'s value is empty at row " + (x + 1) + " in the Excel sheet "
						+ sheetName + ".xlsx");

			// check if row is scenario header row or data row
			if (sheet.getRow(x).getCell(POST_DESC_INDEX).toString().trim().equals(POST_DESC_HEADER)) {
				paramFlag = true;
			}

			if (paramFlag) {
				cols = sheet.getRow(x).getLastCellNum();
				if (sheet.getRow(x + 1).getCell(FEATURE_FILE_CELL_INDEX).toString().equals(featureName)
						&& sheet.getRow(x + 1).getCell(SCENARIO_NAME_CELL_INDEX).toString().equals(scenarioName)) {
					str.append("|");
					for (int y = POST_DESC_INDEX+1; y < cols; y++) {
						try {
							str.append(sheet.getRow(x).getCell(y, Row.CREATE_NULL_AS_BLANK).toString()
									.replace(".0", "").trim());
							str.append("|");

						} catch (Exception e) {
							logger.fatal("Problem with cell " + x + ":" + y);
							e.printStackTrace();
						}
					}
					str.append("\n");
					str.append("      ");
					paramFlag = false;
					continue;
				}
				paramFlag = false;
			}

			if (null != sheet.getRow(x).getCell(FEATURE_FILE_CELL_INDEX)) {
				if (sheet.getRow(x).getCell(FEATURE_FILE_CELL_INDEX).toString().equals(featureName)
						&& sheet.getRow(x).getCell(SCENARIO_NAME_CELL_INDEX).toString().equals(scenarioName)) {
					
					str.append("#" + sheet.getRow(x).getCell(ITERATION_NAME_CELL_INDEX).toString().trim() + "\n      "); //Add a comment saying what iteration this data line is for
					
					if (sheet.getRow(x).getCell(POST_DESC_INDEX).toString().trim().equalsIgnoreCase("N"))
						str.append("#");
					
					str.append("|");

					for (int y = POST_DESC_INDEX+1; y < cols; y++) {
						try {
							str.append(sheet.getRow(x).getCell(y, Row.CREATE_NULL_AS_BLANK).toString()
									.replace(".0", "").trim());
							str.append("|");

						} catch (Exception e) {
							logger.fatal("Problem with cell " + x + ":" + y);
							e.printStackTrace();
						}
					}
					str.append("\n");
					str.append("      ");
				}
			}
		}
		return str;
	}
}