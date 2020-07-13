package coser.common;

import java.io.FileReader;

import coser.datamodel.decisionsystem.NewLocalDisPlusGlobalDis;

/********************************
 * Summary: Compute the conditional entropy. Given an integer matrix. Author:
 * XiangjuLi Written time: October 20, 2014. ******************************
 */
public class ConditionalEntropy {

	/**
	 ************************* 
	 * Compute the max values of every column
	 * 
	 * @param paraMatrix
	 *            The given integer matrix ***********************
	 */
	public static int[] maxValues(int[][] paraMatrix) {
		int returnMaxValues[] = new int[paraMatrix[0].length];

		// System.out.println("paraMatrix[0].length = " + paraMatrix[0].length);
		// Initialise
		for (int i = 0; i < paraMatrix[0].length; i++) {
			returnMaxValues[i] = paraMatrix[0][i];
		}// of for i

		// Find the max value of every cloum
		for (int i = 0; i < paraMatrix[0].length; i++) {
			for (int j = 1; j < paraMatrix.length; j++) {
				if (returnMaxValues[i] < paraMatrix[j][i]) {
					returnMaxValues[i] = paraMatrix[j][i];
				}// of if
			}// of for j row
		}// of for i column
		return returnMaxValues;
	}// of maxValues

	/**
	 ********************************
	 * Compute the conditional entropy of the given integer matrix
	 * 
	 * @param paraMatrix
	 *            The given integer matrix
	 ********************************
	 */
	public static double conditionalEntropy(int[][] paraMatrix) {
		double returnconEntropy = 0;
		// Find the max value of every cloum
		int maxVlauesArray[] = new int[paraMatrix[0].length];
		maxVlauesArray = maxValues(paraMatrix);
		
		// Compute the conditional entropy
		returnconEntropy = conditionalEntropy(paraMatrix, maxVlauesArray);
		return returnconEntropy;
	}
	
	/**
	 ********************************
	 * Compute the conditional entropy of the given integer matrix
	 * 
	 * @param paraMatrix
	 *            The given integer matrix
	 ********************************
	 */
	public static double conditionalEntropyForBoolean(int[][] paraMatrix) {
		double returnconEntropy = 0;
		// Find the max value of every cloum
		int maxVlauesArray[] = new int[paraMatrix[0].length];
		for(int i = 0; i < maxVlauesArray.length; i++)
			maxVlauesArray[i] = 1;
		// Compute the conditional entropy
		returnconEntropy = conditionalEntropy(paraMatrix, maxVlauesArray);
		return returnconEntropy;
	}
	
	/**
	 ************************* 
	 * Compute the conditional entropy of the given interger matrix
	 * 
	 * @param paraMatrix
	 *            The given integer matrix
	 * @param paraMaxVlaues
	 *            The max value of every cloum of the given integer matrix
	 *************************
	 */
	public static double conditionalEntropy(int[][] paraMatrix,
			int[] paraMaxVlaues) {
		double returnconEntropy = 0;

		// Step1:Compute partition of condition attributes and decision
		// attributes
		boolean[][] paraX = new boolean[1][paraMatrix.length];
		boolean[][] paraY = new boolean[1][paraMatrix.length];
		for (int i = 0; i < paraMatrix.length; i++) {
			// Initialize the matrix
			paraX[0][i] = true;
			paraY[0][i] = true;
		}// of for i
		//SimpleTool.printBooleanMatrix(paraX);
		
		
			// Compute partition of condition attributes
		for (int i = 0; i < paraMatrix[0].length - 1; i++) {
			paraX = computePartition(paraMatrix, paraX, i, paraMaxVlaues[i]);
			//SimpleTool.printBooleanMatrix(paraX);
		}// of for i
		//System.out.println("条件属性划分后的结果");
		//SimpleTool.printBooleanMatrix(paraX);
		
		// Compute partition of decision attribute
		paraY = computePartition(paraMatrix, paraY[0],
				paraMatrix[0].length - 1,
				paraMaxVlaues[paraMatrix[0].length - 1]);
		// System.out.println("决策属性划分后的结果");
		// SimpleTool.printBooleanMatrix(paraY);
		// Step2:compute the conditional information entropy according to
		// partition
		returnconEntropy = conditionalEntropy(paraX, paraY);
		returnconEntropy /= Math.log(2);
		returnconEntropy /= paraMatrix.length;
		return returnconEntropy;
	}// of for conditionalEntropy

	/**
	 ********************************** 
	 * Compute a partition of the matrix according to a given attribute.
	 * 
	 * @param paraMatrix
	 *            The given original integer matrix
	 * @param paraPartition
	 *            The given integer matrix need partition
	 * @param paraIndex
	 *            the current attribute index
	 * @param paraMaxVlaues
	 *            The max value of the current attribute
	 ********************************** 
	 */
	public static boolean[][] computePartition(int[][] paraMatrix,
			boolean[][] paraPartition, int paraIndex, int paraMaxValue) {
		
		// System.out.println("computePartition");
		// SimpleTool.printBooleanMatrix(paraPartition);
		boolean[][] returnMatrix;
		// The partition result of every row of given matrix
		boolean[][] tempMatrix;
		// The max rows could reach
		int totalRows = (paraMaxValue + 1) * paraPartition.length;
		
		boolean[][] totalPartition = new boolean[totalRows][paraMatrix.length];
		// Step1:compute the partion
		int tempTotalRows = 0;
		for (int i = 0; i < paraPartition.length; i++) {
			tempMatrix = computePartition(paraMatrix, paraPartition[i],
					paraIndex, paraMaxValue);
			// SimpleTool.printBooleanMatrix(tempMatrix);
			for (int j = 0; j < tempMatrix.length; j++) {
				for (int k = 0; k < tempMatrix[j].length; k++) {
					totalPartition[tempTotalRows][k] = tempMatrix[j][k];
				}// of for k
				tempTotalRows++;
			}// of for j
		}// Of for i

		// Step2: copy the result
		returnMatrix = new boolean[tempTotalRows][paraMatrix.length];
		for (int i = 0; i < tempTotalRows; i++) {
			for (int j = 0; j < paraMatrix.length; j++) {
				returnMatrix[i][j] = totalPartition[i][j];
			}// Of for j
		}// Of for i

		return returnMatrix;
	}// Of computePartition

	/**
	 ********************************** 
	 * Compute a partition of an arry according to a given attribute.
	 * 
	 * @param paraMatrix
	 *            The given original integer matrix
	 * @param paraPartition
	 *            The given integer array need partition
	 * @param paraIndex
	 *            the current attribute index
	 * @param paraMaxVlaues
	 *            The max value of the current attribute
	 ********************************** 
	 */
	public static boolean[][] computePartition(int[][] paraMatrix,
			boolean[] paraPartition, int paraIndex, int paraMaxValue) {
		// How many valid attribute values
		int tempTotalValues = paraMaxValue + 1;
		int tempValue = 0;
		boolean[] tempValueExists = new boolean[tempTotalValues];
		int tempValidValues = 0;
		// Compute the number of effective values
		for (int i = 0; i < paraPartition.length; i++) {
			if (paraPartition[i]) {
				tempValue = paraMatrix[i][paraIndex];
				if (!tempValueExists[tempValue]) {
					tempValueExists[tempValue] = true;
					tempValidValues++;
				}// of if-2
			}// Of if-1
		}// Of for i

		// Compress to an integer array
		int[] tempIndexArray = new int[tempValidValues];
		int tempIndex = 0;
		for (int i = 0; i < tempValueExists.length; i++) {
			if (tempValueExists[i]) {
				tempIndexArray[tempIndex] = i;
				tempIndex++;
			}// Of if
		}// Of for i

		// Copy the result
		boolean[][] resultMatrix = new boolean[tempValidValues][paraMatrix.length];
		for (int i = 0; i < paraMatrix.length; i++) {
			if (paraPartition[i]) {
				tempValue = paraMatrix[i][paraIndex];
				for (int j = 0; j < tempIndexArray.length; j++) {
					if (tempIndexArray[j] == tempValue) {
						resultMatrix[j][i] = true;
						break;
					}// Of if
				}// Of for j
			}// Of if
		}// Of if
		/*System.out.println("划分结果");
		SimpleTool.printBooleanMatrix(resultMatrix);*/
		return resultMatrix;
	}// Of computePartition

	/**
	 ********************************** 
	 * Compute the conditional entropy according to the given partition.
	 * 
	 * @param paraXsize
	 *            The partition of conditional attribute
	 * @param paraYsize
	 *            The partition of decision attribute
	 ********************************** 
	 */
	public static double conditionalEntropy(boolean[][] paraXsize,
			boolean[][] paraYsize) {
		double returnconEntropy = 0;
		// The number elements intersection of X and Y
		int[][] blocksXYsize = new int[paraXsize.length][paraYsize.length];
		int[] blocksXsize = new int[paraXsize.length];

		// Step1: compute the number of every blocks
		for (int i = 0; i < paraXsize.length; i++) {
			for (int j = 0; j < paraXsize[i].length; j++) {
				if (paraXsize[i][j]) {
					blocksXsize[i]++;
					for (int k = 0; k < paraYsize.length; k++) {
						if (paraYsize[k][j]) {
							blocksXYsize[i][k]++;
						}// of IF-2
					}// of for k
				}// of if-1
			}// of for j
		}// of for i
		
	/*	SimpleTool.printIntMatrix(blocksXYsize);
		System.out.println("--------------------");
		SimpleTool.printIntArray(blocksXsize);*/

		// Step2:compute the conditional entropy
		returnconEntropy = conditionalEn(blocksXYsize, blocksXsize);
		return returnconEntropy;
	}// of conditionalEntropy

	/**
	 ********************************** 
	 * Compute the conditional entropy according to the given integer matrix.
	 * 
	 * @param paraBlocksXYsize
	 *            The number of elements of the intersection of X and Y
	 * @param paraBlocksXsize
	 *            The number of elements of X
	 ********************************** 
	 */
	public static double conditionalEn(int[][] paraBlocksXYsize,
			int[] paraBlocksXsize) {
		double returnResult = 0;

		for (int i = 0; i < paraBlocksXYsize.length; i++) {
			for (int j = 0; j < paraBlocksXYsize[i].length; j++) {
				if (paraBlocksXYsize[i][j] != 0) {
					returnResult += paraBlocksXYsize[i][j]
							* Math.log(paraBlocksXYsize[i][j]);
				}// of if
			}// of for j
			returnResult -= paraBlocksXsize[i] * Math.log(paraBlocksXsize[i]);
		}// of for i

		return -returnResult;
	}// of conditionalEn

	/**
	 ********************************** 
	 * Print the given matrix
	 * 
	 * @param paraMatrix
	 *            The given matrix
	 ********************************** 
	 */
	public static void printMatrix(int[][] paraMatrix) {
		System.out.print("{\r\n");
		for (int i = 0; i < paraMatrix.length; i++) {
			for (int j = 0; j < paraMatrix[i].length; j++) {
				System.out.print(paraMatrix[i][j] + "\t");
			}// of for j
			System.out.println();
		}// of for i
		System.out.print("}");
	}// of println

	public static void main(String[] args) {
		int[][] testMatrix = { { 0, 1, 0 }, { 0, 1, 0 }, { 1, 1, 0 }, { 4, 1, 0 },
				{ 3, 4, 0 }, { 1, 4, 1 }, { 4, 4, 1 },
				{ 0, 4, 1 }, { 3, 1, 1 }};
		
		String arffFilename = "data/java/waveform.arff";
	
	}// of main
}// of ConditionalEntropy

