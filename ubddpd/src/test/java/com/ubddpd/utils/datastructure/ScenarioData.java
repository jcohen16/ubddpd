package com.ubddpd.utils.datastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScenarioData {

	private String featureName;
	private String scenarioName;
	private List<String> parameters;
	private HashMap<String,String> iterationNames;
	private HashMap<String,String> runData;
	//HashMap of (Iteration Num and (HashMap of Parameter and Value)) For data read in from current Excel File
	private HashMap<String, HashMap<String, String>> parameterData; 

	public ScenarioData(String fN, String sN) {
		this.featureName = fN;
		this.scenarioName = sN;
		this.parameters = new ArrayList<String>();
		this.parameterData = new HashMap<String, HashMap<String, String>>();
	}
	
	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public HashMap<String,String> getIterationNames() {
		return iterationNames;
	}
	
	public void setIterationName(HashMap<String,String> iterationNames) {
		this.iterationNames =  iterationNames;
	}
	
	public HashMap<String, String> getRunData() {
		return runData;
	}

	public void setRunData(HashMap<String, String> runData) {
		this.runData = runData;
	}
	
	public List<String> getParameters() {
		return parameters;
	}

	public void addParameter(String parameter) {
		this.parameters.add(parameter);
	}
	
	public HashMap<String, HashMap<String,String>> getParameterData() {
		return parameterData;
	}

	public void setParameterData(HashMap<String, HashMap<String,String>> parameterData) {
		this.parameterData = parameterData;
	}
	
	public void addNewParameterToData(String newParam){
		for(int i = 0; i < this.parameterData.size(); i++){
			this.parameterData.get("Iteration " + (i+1)).put(newParam, "");
		}
		this.parameters.add(newParam);
	}
}
