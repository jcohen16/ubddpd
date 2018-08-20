package com.ubddpd.utils;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Utils {

	public static String getFileContentsAsString(String filename) throws IOException {
		InputStream is = new FileInputStream(filename);
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));

		String line = buf.readLine();
		StringBuilder sb = new StringBuilder();

		while (line != null) {
			sb.append(line).append("\n");
			line = buf.readLine();
		}
		
		buf.close();
		return sb.toString();
	}

	public static ArrayList<String> retrieveFunctionalities(String resPath) throws IOException {

		String textFile = resPath + "/features/functionality.csv";
		BufferedReader tf = new BufferedReader(new FileReader(textFile));
		String line = "";
		ArrayList<String> functionalities = new ArrayList<String>();
		while ((line = tf.readLine()) != null) {
			functionalities.add(line.trim());
		}
		tf.close();
		return functionalities;
	}
	
	public static String getTimeStamp() {
		String TimeStamp;
		Timestamp time = new Timestamp(System.currentTimeMillis());
		TimeStamp = String.valueOf(time.getTime());
		return TimeStamp;
	} 
	
}
