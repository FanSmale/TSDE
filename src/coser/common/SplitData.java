package coser.common;

/********************************
 * Summary: Split data according attribute. Given an integer matrix. Author:
 * XiangjuLi Written time: October 20, 2014. ******************************
 */
public class SplitData {
	/**
	 * Attribute value maximal combination.
	 */
	public static int MAX_length = 800;

	/**
	 ************************* 
	 * Split datas according the attributes
	 * 
	 * @param paraData
	 *            The given integer matrix ***********************
	 */
	public static int[][][] splitDataOnce(int[][][] paraData) {

		int numAttributes = paraData[0][0].length - 1;
		int[][][] resultData;

		// Used to store the result for a short time
		int[][][] currentResultData = new int[MAX_length][][];

		// Used to store the need split data for a short time
		int[][][] currentTempSplitData = new int[MAX_length][][];

		// Used to store the need split data
		int[][][] currentSplitData = paraData;

		// Used to store the split result of every data for a short time
		int[][][] tempSplitData;

		// Mark the number of pure data
		int temp = 0;

		// Mark the number of need split data
		int currentTemp = 0;

		// Split data with attributes
		for (int i = 0; i < numAttributes; i++) {
			currentTemp = 0;
			// Split every data
			for (int j = 0; j < currentSplitData.length; j++) {
				// If the data is pure
				if (checkIfPure(currentSplitData[j], numAttributes)) {
					currentResultData[temp] = currentSplitData[j];
					temp++;
					continue;
				} else {
					// If the current attribute useful
					if (checkIfPure(currentSplitData[j], i)) {
						// storage used to do the next attribute division
						currentTempSplitData[currentTemp] = currentSplitData[j];
						currentTemp++;
					} else {// Split result the data[j] use attribute i
						tempSplitData = splitDataOnce(currentSplitData[j], i);
						// Check the data decision which used to next wheel
						// split
						for (int k = 0; k < tempSplitData.length; k++) {
							if (checkIfPure(tempSplitData[k], numAttributes)) {
								// Store pure data
								currentResultData[temp] = tempSplitData[k];
								temp++;
								continue;
							} else {
								// Store for next split
								currentTempSplitData[currentTemp] = tempSplitData[k];
								currentTemp++;
							}// of else-3
						}// of for k
					}// of else -2
				}// of else-1
			}// of for j

			// If every data is pure then return null
			if (currentTemp == 0) {
				return null;
			}// of if

			// copy array for next split wirh next attribute
			currentSplitData = new int[currentTemp][][];
			for (int j = 0; j < currentTemp; j++) {
				currentSplitData[j] = currentTempSplitData[j];
			}// of for j
		}// of for i

		// Copy the result
		resultData = new int[temp + currentTemp][][];
		for (int i = 0; i < temp; i++) {
			resultData[i] = currentResultData[i];
		}// of for i
		for (int i = 0; i < currentTemp; i++) {
			resultData[temp + i] = currentSplitData[i];
		}// of for i
		return resultData;
	}// of splitDataOnce

	/**
	 ************************* 
	 * Split data according given attributes
	 * 
	 * @param paraIndex
	 *            The given attribute index ***********************
	 */
	public static int[][][] splitDataOnce(int[][] paraData, int paraIndex) {

		int[][][] resultData;
		// The max numberthrough split according given attribute
		int tempBags = maxValueOfColum(paraData, paraIndex) + 1;
		int[][][] tempData = new int[tempBags][paraData.length][paraData[0].length];
		boolean[] ifExistBags = new boolean[tempBags];

		// Store rhe rows of every bags
		int[] arryIndex = new int[tempBags];
		int tempRows = 0;
		for (int i = 0; i < paraData.length; i++) {
			int tempValue = paraData[i][paraIndex];
			tempData[tempValue] = addDataItem(paraData, i, tempData[tempValue],
					arryIndex[tempValue]);
			arryIndex[tempValue]++;
			// Compute the number of split bags
			if (!ifExistBags[tempValue]) {
				ifExistBags[tempValue] = true;
				tempRows++;
			}// of if
		}// of for i

		// Copy the result
		resultData = new int[tempRows][][];
		int temp = 0;
		for (int i = 0; i < tempBags; i++) {
			if (arryIndex[i] != 0) {
				resultData[temp] = new int[arryIndex[i]][tempData[i][0].length];
				for (int j = 0; j < arryIndex[i]; j++) {
					resultData[temp][j] = tempData[i][j];
				}// of for j
				temp++;
			}// of if
		}// of for i
		return resultData;
	}// of splitDataOnce

	/**
	 ************************* 
	 * Check if the data pure for the attribute with paraIndex
	 * 
	 * @param paraData
	 *            The given data
	 * @param paraIndex
	 *            The given attribute index ***********************
	 */
	public static boolean checkIfPure(int[][] paraData, int paraIndex) {
		boolean returnResult = true;

		// If remain one line
		if (paraData.length == 1)
			return returnResult;
		// System.out.println("paraIndex = " + paraIndex);
		int temp = paraData[0][paraIndex];
		for (int i = 1; i < paraData.length; i++) {
			if (paraData[i][paraIndex] != temp) {
				returnResult = false;
				break;
			}// of if
		}// of for i
		return returnResult;
	}// of checkIfPureData

	/**
	 ************************* 
	 * Find the max value of the column
	 * 
	 * @param paraData
	 *            The given data
	 * @param paraIndex
	 *            The given attribute index ***********************
	 */
	public static int maxValueOfColum(int[][] paraData, int paraIndex) {
		int maxValue = paraData[0][paraIndex];
		for (int i = 1; i < paraData.length; i++) {
			if (paraData[i][paraIndex] > maxValue) {
				maxValue = paraData[i][paraIndex];
			}// of if
		}// of for i
		return maxValue;
	}// of for maxValueOfColum

	/**
	 ************************* 
	 * Add the data item from paraFromData to paraToData
	 * 
	 * @param paraFromData
	 *            The given data one
	 * @param paraIndex
	 *            The rows given data one
	 * @param paraToData
	 *            The given data two
	 * @param paraIndex
	 *            The rows given data two ***********************
	 */
	public static int[][] addDataItem(int[][] paraFromData, int paraIndex,
			int[][] paraToData, int newIndex) {
		for (int i = 0; i < paraFromData[paraIndex].length; i++) {
			paraToData[newIndex][i] = paraFromData[paraIndex][i];
		}// of for
		return paraToData;
	}// of addDataItem

	/**
	 ************************* 
	 * Print all data
	 * 
	 * @param paraData
	 *            The given data ***********************
	 */
	public static void printDataMatrix(int[][][] paraData) {
		if (paraData == null) {
			System.out.println("Null");
		} else {
			for (int i = 0; i < paraData.length; i++) {
				System.out.println("The " + i + " table is :\r\n");
				System.out.println("{");

				for (int j = 0; j < paraData[i].length; j++) {
					for (int k = 0; k < paraData[i][j].length; k++) {
						System.out.print(paraData[i][j][k] + "\t");
					}// of for k
					System.out.println();
				}// of for j
				System.out.println("}");
			}// of for i
		}// of if-else
	}// of printDataMatrix

	/******************************************
	 * The third homework ****************************************
	 */

	/**
	 ************************* 
	 * Split data to obtain not pure block
	 * 
	 * @param paraData
	 *            The given data
	 *************************
	 */
	public static int[][] splitData(int[][] paraData) {
		int decisionIndex = paraData[0].length - 1;
		int[][] returnResult;
		int[][][] paraThisData = new int[1][][];
		paraThisData[0] = paraData;
		int[][][] tempResult;
		int numRows = 0;
		tempResult = splitDataOnce(paraThisData);

		// If not split return null
		if (tempResult == null) {
			return null;
		}// Of if

		boolean ifPure = true;
		int[][] numBlocks = new int[tempResult.length][];
		for (int i = 0; i < tempResult.length; i++) {
			if (!checkIfPure(tempResult[i], decisionIndex)) {
				ifPure = false;
				numBlocks[numRows] = new int[tempResult[i].length];
				for (int j = 0; j < tempResult[i].length; j++) {
					numBlocks[numRows][j] = tempResult[i][j][decisionIndex];
				}// of for j
				numRows++;
			}// of for if
		}// of for i

		// If every data pure then return
		if (ifPure) {
			return null;
		}
		// Copy the result
		returnResult = new int[numRows][];
		for (int i = 0; i < numRows; i++) {
			returnResult[i] = numBlocks[i];
		}// of for i
		return returnResult;
	}// of splitData

	/**
	 ************************* 
	 * Print the data
	 * 
	 * @param paraData
	 *            The given data
	 *************************
	 */
	public static void printDataMatrix(int[][] paraData) {
		if (paraData == null) {
			System.out.println("Null");
		} else {
			for (int i = 0; i < paraData.length; i++) {
				System.out.println("The " + i + " not pure block is :\r\n");
				System.out.print("{");
				for (int j = 0; j < paraData[i].length; j++) {
					System.out.print(paraData[i][j] + "\t");
				}// of for j
				System.out.println("}");
			}// of for i
		}// of if-else
	}// of printDataMatrix

	public static void main(String[] args) {
		int[][][] test01 = {
				{ { 0, 1, 0 }, { 1, 1, 0 }, { 4, 1, 0 }, { 3, 4, 0 },
				  { 1, 4, 1 }, { 4, 4, 1 }, { 4, 4, 0 }, { 0, 4, 1 },
				  { 3, 1, 1 }, { 1, 1, 1 } },
				{ { 5, 4, 0 }, { 5, 1, 5 }, { 4, 0, 0 }, { 3, 0, 0 },
				  { 3, 2, 1 }, { 5, 4, 1 }, { 4, 4, 0 }, { 7, 4, 1 },
				  { 5, 4, 3 }, { 1, 1, 2 } },
				{ { 5, 4, 0 }, { 5, 1, 0 }, { 4, 0, 0 }, { 3, 0, 0 },
				  { 3, 2, 0 }, { 5, 4, 0 }, { 4, 4, 0 }, { 7, 4, 0 },
				  { 5, 4, 0 }, { 1, 1, 0 } } };

		int[][][] test02 = {
				{ { 0, 1, 0 }, { 1, 1, 0 }, { 4, 1, 0 }, { 3, 4, 0 },
						{ 1, 4, 0 }, { 4, 4, 0 }, { 4, 4, 0 }, { 0, 4, 0 },
						{ 3, 1, 0 }, { 1, 1, 0 } },
				{ { 5, 4, 0 }, { 5, 1, 0 }, { 4, 0, 0 }, { 3, 0, 0 },
						{ 3, 2, 0 }, { 5, 4, 0 }, { 4, 4, 0 }, { 7, 4, 0 },
						{ 5, 4, 0 }, { 1, 1, 0 } }, };

		int[][] test03 = { { 0, 1, 0 }, 
						   { 1, 1, 0 }, 
						   { 4, 1, 0 }, 
						   { 3, 4, 0 },
						   { 1, 4, 1 }, 
						   { 4, 4, 1 }, 
						   { 4, 4, 0 }, 
						   { 0, 4, 1 },
						   { 3, 1, 1 }, 
						   { 1, 1, 1 } };

		// The second homework test
		int result01[][][];
		result01 = splitDataOnce(test01);
		System.out.println("The result of not pure datas test is:");
		// printDataMatrix(result01);

		int result02[][][];
		System.out.println("The result of pure datas testPure is:");
		result02 = splitDataOnce(test02);
		// printDataMatrix(result02);

		// The third homework test
		int[][] result03 = splitData(test03);
		printDataMatrix(result03);
	}// of main
}// of SplitData
