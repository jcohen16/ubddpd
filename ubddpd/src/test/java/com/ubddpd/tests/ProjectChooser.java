package com.ubddpd.tests;

import com.ubddpd.utils.TestDataExtractor;

import java.io.File;
import java.io.IOException;

public class ProjectChooser {

	public static void main(String[] args) throws IOException {
		//Get the appropriate Project Path
		File thisFile = new File("./");
		String thisProject = thisFile.getAbsolutePath();
		thisProject = thisProject.substring(0, thisProject.lastIndexOf("\\"));
		thisProject = thisProject.substring(thisProject.lastIndexOf("\\")+1);
		
		String resourcePath = "./../"+thisProject+"/res/";
		
		TestDataExtractor.main(new String[]{resourcePath});
		
	}
}
