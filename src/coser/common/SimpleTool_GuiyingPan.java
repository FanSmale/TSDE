/*
 * @(#)SimpleTool_GuiyingPan.java
 *
 */

package coser.common;

import java.io.*;

/**
 * Frequently used methods by Guiying.
 * <p>
 * Author: <b>Guiying Pan</b> kentwingpan@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organizaion: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Progress: OK. Copied from Hydrosimu.<br>
 * Written time: March 09, 2011. <br>
 * Last modify time: August 17, 2011.
 */
public class SimpleTool_GuiyingPan extends SimpleTool {

	/**
	 ************************* 
	 * Print a boolean array. Simply for test.
	 * 
	 * @param paraBooleanArray
	 *            The given boolean array.
	 ************************* 
	 */
	public static void printBooleanArray(boolean[] paraBooleanArray) {
		for (int i = 0; i < paraBooleanArray.length; i++) {
			if (paraBooleanArray[i]) {
				System.out.print("" + 1 + ",");
			} else {
				System.out.print("" + 0 + ",");
			}// Of if
		}// Of for i
		System.out.println();
	}// Of printBooleanArray

	/**
	 ************************* 
	 * Print a double array. Simply for test.
	 * 
	 * @param paraDoubleArray
	 *            The given double array.
	 ************************* 
	 */
	public static void printDoubleArray(double[] paraDoubleArray) {
		for (int i = 0; i < paraDoubleArray.length; i++) {
			System.out.print(paraDoubleArray[i] + " ");
		}// Of for i
		System.out.println();
	}// Of printDoubleArray

	/**
	 ************************* 
	 * Print a integer array. Simply for test.
	 * 
	 * @param paraIntegerArray
	 *            The given double array.
	 ************************* 
	 */
	public static void printIntegerArray(int[] paraIntegerArray) {
		for (int i = 0; i < paraIntegerArray.length; i++) {
			System.out.print(paraIntegerArray[i] + " ");
		}// Of for i
		System.out.println();
	}// Of printIntegerArray

	/**
	 ************************* 
	 * Print a long array. Simply for test.
	 * 
	 * @param paraLongArray
	 *            The given long array.
	 ************************* 
	 */
	public static void printLongArray(long[] paraLongArray) {
		for (int i = 0; i < paraLongArray.length; i++) {
			System.out.print(paraLongArray[i] + " ");
		}// Of for i
		System.out.println();
	}// Of printLongArray

	/**
	 ************************* 
	 * Print all reducts. Simply for test.
	 * 
	 * @param paraAllReducts
	 *            The given array to print.
	 ************************* 
	 */
	public static void printAllReducts(boolean[][] paraAllReducts) {
		for (int i = 0; i < paraAllReducts.length; i++) {
			System.out.println();
			for (int j = 0; j < paraAllReducts[0].length; j++) {
				if (paraAllReducts[i][j]) {
					System.out.print("" + 1 + ",");
				} else {
					System.out.print("" + 0 + ",");
				}
			}// Of for j
		}// Of for i
	}// Of printAllReducts

	/**
	 *************************** 
	 * Write a message to a new file.
	 * 
	 * @param paraFilename
	 *            The given filename.
	 * @param paraMessage
	 *            The givean message string.
	 *************************** 
	 */
	public static void writeFile(String paraFilename, String paraMessage)
			throws Exception {

		File resultFile = new File(paraFilename);
		if (resultFile.exists()) {
			resultFile.delete();
		}
		resultFile.createNewFile();
		PrintWriter writer = new PrintWriter(new FileOutputStream(resultFile));
		writer.print(paraMessage);
		writer.flush();
		writer.close();
	}// Of writeFile

	/**
	 *************************** 
	 * Read a int value from a given string. <br>
	 * 
	 * @param paraString
	 *            The given string.
	 * @return A int value.
	 *************************** 
	 */
	public static int parseIntValue(String paraString) throws Exception {
		// char separator = ' ';
		int tempValue = 0;
		String tempString = new String(paraString);
		String currentString = null;
		// String remainingString = new String(paraString);
		currentString = tempString.substring(tempString.indexOf(":") + 1)
				.trim();
		tempValue = Integer.parseInt(currentString);
		return tempValue;
	}// Of parseIntValue

}// Of class SimpleTool_GuiyingPan
