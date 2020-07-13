package coser.common;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import weka.core.*;


/**
 * Summary: Simple nominal decision system.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <b> <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://scs.swpu.edu.cn/smale/>Lab of Machine Learning,
 * Southwest Petroleum University, Chengdu 610500, China.<br>
 * Project: The university data project/.
 * <p>
 * Progress: Equivalent class, partition, lower/upper approximations. <br>
 * Written time: April 27, 2015. <br>
 * Last modify time: April 29, 2015.
 */
public class SimpleNominalDecisionSystem extends Instances {

	public static final long serialVersionUID = 4836743165811824L;

	/**
	 * Statistics on the number of operations of algorithms.
	 */
	public static long operations = 0;

	boolean[] allAttributes;

	boolean[] conditions;

	boolean[] decision;

	/**
	 ************************* 
	 * Construct the decision system. Simply adopt everything of the super
	 * class.
	 * 
	 * @param paraReader
	 *            the input with the form of a Reader. We can generate a
	 *            FileReader given an .arff filename.
	 * 
	 ************************* 
	 **/
	public SimpleNominalDecisionSystem(Reader reader) throws IOException,
			Exception {
		super(reader);
		initialize();
	}// Of the first constructor

	/**
	 ************************* 
	 * Initialize
	 ************************* 
	 **/
	void initialize() {
		allAttributes = new boolean[numAttributes()];
		conditions = new boolean[numAttributes()];
		decision = new boolean[numAttributes()];
		for (int i = 0; i < numAttributes(); i++) {
			allAttributes[i] = true;
		}// Of for i

		for (int i = 0; i < numAttributes() - 1; i++) {
			conditions[i] = true;
		}// Of for i

		decision[numAttributes()-1] = true;
	}// Of initialize

	/**
	 ************************* 
	 * Construct the decision system from a file.
	 * 
	 * @param paraFilename
	 *            the filename
	 * 
	 ************************* 
	 * */
	public static SimpleNominalDecisionSystem constructFromFile(
			String paraFilename) {
		SimpleNominalDecisionSystem tempSystem = null;
		try {
			FileReader tempFileReader = new FileReader(paraFilename);
			tempSystem = new SimpleNominalDecisionSystem(tempFileReader);
			tempFileReader.close();
		} catch (Exception ee) {
			System.out
					.println("Error occurred while trying to read'"
							+ paraFilename
							+ "\' in SimpleNominalDecisionSystem.testIntMatrixObject().\r\n"
							+ ee);
		}// Of try
		return tempSystem;
	}// Of constructFromFile

	/**
	 ************************* 
	 * Partition the universe according to the given attribute.
	 * 
	 * @param paraAttributeIndex
	 *            the attribute index
	 ************************* 
	 * */

	public boolean[][] booleanPartitionFromAttribute(int paraAttributeIndex)
			throws Exception {
		if (paraAttributeIndex >= numAttributes()) {
			throw new Exception(
					"Error occurred in SimpleNominalDecisionSystem.booleanPartitionFromAttribute(int)"
							+ "\r\n The attribute index "
							+ paraAttributeIndex
							+ " is out of bound: "
							+ (numAttributes() - 1)
							+ ".");
		}// Of if

		int tempValues = attribute(paraAttributeIndex).numValues();

		boolean[][] resultMatrix = new boolean[tempValues][numInstances()];
		int tempBox = 0;
		for (int i = 0; i < numInstances(); i++) {
			// System.out.print("\t" + instance(i).value(paraAttributeIndex));
			tempBox = (int) (instance(i).value(paraAttributeIndex));
			resultMatrix[tempBox][i] = true;
		}// Of for i

		return resultMatrix;
	}// Of booleanPartitionFromAttribute

	/**
	 ************************* 
	 * Partition the universe according to the given attribute.
	 * 
	 * @param paraBox
	 *            the given box. Which instances are included.
	 * @return a boolean matrix indicating the blocks.
	 ************************* 
	 * */
	public int[][] intPartitionFromAttributeRemovePure(int paraAttributeIndex,
			int[] paraInstances) throws Exception {
		//Step 1. Generate block for each attribute value
		int numberOfValues = attribute(paraAttributeIndex).numValues();
		int[][] tempBlocks = new int[numberOfValues][paraInstances.length];
		int[] tempCounterForRows = new int[numberOfValues];
		int tempValue = 0;
		
		//System.out.println("Remove pure test 1, the source instances is:");
		//SimpleTool.printIntArray(paraInstances);

		for (int i = 0; i < paraInstances.length; i ++) {
			tempValue = (int)instance(paraInstances[i]).value(paraAttributeIndex);
			tempBlocks[tempValue][tempCounterForRows[tempValue]] = paraInstances[i];
			tempCounterForRows[tempValue] ++;
			operations ++;
		}//Of for i
		//System.out.println("Remove pure test 2, tempValues = " + tempValue);
		//System.out.println("tempCounterForRows[0] = " + tempCounterForRows[0]);
		//System.out.println("tempCounterForRows[1] = " + tempCounterForRows[1]);
		//System.out.println("tempCounterForRows[2] = " + tempCounterForRows[2]);
		//System.out.println("tempCounterForRows[3] = " + tempCounterForRows[3]);

		//Step 2. Remove empty blocks or blocks with only one instance.
		boolean[] tempUsefulRows = new boolean[numberOfValues];
		for (int i = 0; i < numberOfValues; i ++) {
			operations ++;
			if (tempCounterForRows[i] <= 1) {
				tempUsefulRows[i] = false;
			}//Of if
			tempUsefulRows[i] = true;
		}//Of if
		
		//System.out.println("Remove pure test 3");
		//Step 3. Remove pure blocks
		int tempClass;
		boolean tempPure = false;
		for (int i = 0; i < numberOfValues; i ++) {
			operations ++;
			if (!tempUsefulRows[i]){
				continue;
 			}//Of if
			
			tempPure = true;
			tempClass = (int)instance(tempBlocks[i][0]).value(numAttributes() - 1);
			//System.out.println("The length of current block is: " + tempCounterForRows[i]);
			for (int j = 1; j < tempCounterForRows[i]; j ++) {
				//System.out.println("Class " + (int)instance(tempBlocks[i][j]).value(numAttributes() - 1)
				//		+ " vs. " + tempClass);
				operations ++;
				if ((int)instance(tempBlocks[i][j]).value(numAttributes() - 1) != tempClass) {
					tempPure = false;
					break;
				}//Of if
			}//Of for j
			
			//System.out.println("Block " + i + " is pure? " + tempPure);
			tempUsefulRows[i] = !tempPure;
		}//Of for i
		
		//System.out.println("Remove pure test 4");
		//Step 4. Really remove
		int tempNumberOfUsefulRows = 0;
		for (int i = 0; i < numberOfValues; i ++) {
			operations ++;
			if (tempUsefulRows[i]) {
				tempNumberOfUsefulRows ++;
			}//Of if
		}//Of for i
		
		if (tempNumberOfUsefulRows == 0) {
			return null;
		}//Of if
		
		int[][] resultMatrix = new int[tempNumberOfUsefulRows][];
		int tempCurrentRow = 0;
		for (int i = 0; i < numberOfValues; i ++) {
			operations ++;
			if (!tempUsefulRows[i]) {
				continue;
			}//Of if
			resultMatrix[tempCurrentRow] = new int[tempCounterForRows[i]];
			for (int j = 0; j < tempCounterForRows[i]; j ++) {
				operations ++;
				resultMatrix[tempCurrentRow][j] = tempBlocks[i][j];
			}//Of for j
			tempCurrentRow ++;
		}//Of for i
		
		//System.out.println("Remove pure test 5, the result matrix is:");
		//SimpleTool.printIntMatrix(resultMatrix);
		
		return resultMatrix;
	}//Of intPartitionFromAttributeRemovePure
	
	/**
	 ************************* 
	 * Partition the universe according to the given attribute.
	 * 
	 * @param paraBox
	 *            the given box. Which instances are included.
	 * @return a boolean matrix indicating the blocks.
	 ************************* 
	 * */
	public boolean[][] booleanPartitionFromAttribute(int paraAttributeIndex,
			boolean[] paraBox) throws Exception {
		if (paraAttributeIndex >= numAttributes()) {
			throw new Exception(
					"Error occurred in SimpleNominalDecisionSystem.booleanPartitionFromAttribute(int)"
							+ "\r\n The attribute index "
							+ paraAttributeIndex
							+ " is out of bound: "
							+ (numAttributes() - 1)
							+ ".");
		}// Of if

		int tempValues = attribute(paraAttributeIndex).numValues();

		boolean[][] tempMatrix = new boolean[tempValues][numInstances()];
		boolean[] tempNotEmpty = new boolean[tempValues];
		int tempBox = 0;

		for (int i = 0; i < numInstances(); i++) {
			// Do not consider elements not in current box
			if (!paraBox[i]) {
				continue;
			}// Of if

			// System.out.print("\t" +
			// instance(i).value(paraAttributeIndex));
			tempBox = (int) (instance(i).value(paraAttributeIndex));
			tempMatrix[tempBox][i] = true;
			tempNotEmpty[tempBox] = true;
		}// Of for i

		int tempRows = 0;
		for (int i = 0; i < tempValues; i++) {
			if (tempNotEmpty[i]) {
				tempRows++;
			}// Of if
		}// Of for i

		// Remove empty boxes
		boolean[][] resultMatrix = new boolean[tempRows][numInstances()];
		int tempRowInCompressedmatrix = 0;

		for (int i = 0; i < tempValues; i++) {
			if (!tempNotEmpty[i]) {
				continue;
			}// Of if

			for (int j = 0; j < numInstances(); j++) {
				resultMatrix[tempRowInCompressedmatrix][j] = tempMatrix[i][j];
			}// Of for j
			tempRowInCompressedmatrix++;
		}// Of for i

		return resultMatrix;
	}// Of booleanPartitionFromAttribute

	/**
	 ************************* 
	 * Partition the universe according to the given attribute.
	 * 
	 * @param paraBox
	 *            the given box. Which instances are included.
	 ************************* 
	 * */
	public boolean[][] booleanPartitionFromAttribute(int paraAttributeIndex,
			boolean[][] paraMatrix) throws Exception {
		if (paraAttributeIndex >= numAttributes()) {
			throw new Exception(
					"Error occurred in SimpleNominalDecisionSystem.booleanPartitionFromAttribute(int)"
							+ "\r\n The attribute index "
							+ paraAttributeIndex
							+ " is out of bound: "
							+ (numAttributes() - 1)
							+ ".");
		}// Of if

		// Step 1. Generate the cubic
		boolean[][][] tempCubic = new boolean[paraMatrix.length][][];
		int tempRows = 0;
		for (int i = 0; i < paraMatrix.length; i++) {
			tempCubic[i] = booleanPartitionFromAttribute(paraAttributeIndex,
					paraMatrix[i]);
			tempRows += tempCubic[i].length;
		}// Of for i

		// Step 2. Remove empty boxes
		boolean[][] resultMatrix = new boolean[tempRows][numInstances()];
		int tempCurrentRow = 0;
		for (int i = 0; i < tempCubic.length; i++) {
			for (int j = 0; j < tempCubic[i].length; j++) {
				for (int k = 0; k < numInstances(); k++) {
					resultMatrix[tempCurrentRow][k] = tempCubic[i][j][k];
					operations++;
				}// Of for k
				tempCurrentRow++;
			}// Of for j
		}// Of for i
		return resultMatrix;
	}// Of booleanPartitionFromAttribute

	/**
	 ************************* 
	 * Partition the universe according to the given attribute.
	 * 
	 * @param paraBox
	 *            the given box. Which instances are included.
	 ************************* 
	 * */
	public boolean[][] booleanPartitionFromAllAttributes() throws Exception {
		boolean[][] tempMatrix = new boolean[1][numInstances()];
		for (int i = 0; i < numInstances(); i++) {
			tempMatrix[0][i] = true;
		}// Of for i

		for (int i = 0; i < numAttributes(); i++) {
			tempMatrix = booleanPartitionFromAttribute(i, tempMatrix);
		}// Of for i

		return tempMatrix;
	}// Of booleanPartitionFromAllAttributes

	/**
	 ************************* 
	 * Partition the universe according to the given attributes.
	 * 
	 * @param paraAttributes
	 *            the given box. Which instances are included.
	 ************************* 
	 * */
	public boolean[][] booleanPartitionFromSomeAttributes(
			boolean[] paraAttributes) throws Exception {
		boolean[][] tempMatrix = new boolean[1][numInstances()];
		for (int i = 0; i < numInstances(); i++) {
			tempMatrix[0][i] = true;
		}// Of for i

		for (int i = 0; i < numAttributes(); i++) {
			if (!paraAttributes[i]) {
				continue;
			}// Of if
			tempMatrix = booleanPartitionFromAttribute(i, tempMatrix);
		}// Of for i

		return tempMatrix;
	}// Of booleanPartitionFromAllAttributes

	/**
	 ************************* 
	 * Partition the universe according to the given attributes.
	 * 
	 * @param paraAttributes
	 *            the given box. Which instances are included.
	 ************************* 
	 * */
	public int[][] intCubicToIntMatrix(int[][][] paraCubic) throws Exception {
		//Step 1. How many valid rows?
		int tempRows = 0;
		for (int i = 0; i < paraCubic.length; i ++) {
			operations++;
			if (paraCubic[i] == null) {
				continue;
			}//Of if
			
			tempRows += paraCubic[i].length;
		}//Of for i
		
		if (tempRows == 0) {
			return null;
		}//Of if
		
		//Step 2. Copy
		int tempCurrentRow = 0;
		int[][] resultMatrix = new int[tempRows][];
		for (int i = 0; i < paraCubic.length; i ++) {
			operations++;
			if (paraCubic[i] == null) {
				continue;
			}//Of if
			
			for (int j = 0; j < paraCubic[i].length; j ++) {
				operations++;
				if (paraCubic[i][j] == null) {
					System.out.println("Cubic " + i + ", " + j + " is empty.");
				}
				resultMatrix[tempCurrentRow] = paraCubic[i][j];
				tempCurrentRow ++;
			}//Of for j
		}//Of for i
		
		return resultMatrix;
	}//Of intCubicToIntMatrix
	
	
	/**
	 ************************* 
	 * Partition the universe according to the given attributes.
	 * 
	 * @param paraAttributes
	 *            the given box. Which instances are included.
	 ************************* 
	 * */
	public int[] positiveRegionTopDown() throws Exception {
		int[][] tempBlocks = new int[1][];
		
		int[] tempBlock = new int[numInstances()];
		//The block containing all objects.
		for (int i = 0; i < numInstances(); i++) {
			operations++;
			tempBlock[i] = i;
		}//Of for i
		tempBlocks[0] = tempBlock;
		System.out.println("At the beginning, entropy = " + conditionalEntropyFromIndices(tempBlocks));
		int tempBlocksLength = 1;
		//int tempPositiveCount = 0;
		
		int[][][] tempCubic;
		//SimpleTool.printIntMatrix(tempBlocks);

		for (int i = 0; i < numAttributes() - 1; i++) {
			//System.out.println("Processing attribute " + i);
			operations++;
			//Step 1.1 Partition the remaining objects according to a_i.
			// Generate the cubic
			tempCubic = new int[tempBlocksLength][][];
			//int tempRows = 0;
			for (int j = 0; j < tempBlocksLength; j++) {
				operations ++;
				tempCubic[j] = intPartitionFromAttributeRemovePure(i,
						tempBlocks[j]);
			}// Of for i
			//System.out.println("Processing attribute test 1");

			// Step 1.2. Merge the cubic to a matrix
			tempBlocks = intCubicToIntMatrix(tempCubic);
			System.out.println("After attribute " + i + ", entropy = " + conditionalEntropyFromIndices(tempBlocks));
			//System.out.println("Processing attribute test 2");
			//SimpleTool.printIntMatrix(tempBlocks);
			if (tempBlocks == null) {
				break;
			}//Of if
			//System.out.println("Processing attribute test 3");
			tempBlocksLength = tempBlocks.length;
		}// Of for i
		
		boolean[] tempBooleanPositiveRegion = new boolean[numInstances()];
		for (int i = 0; i < numInstances(); i ++) {
			operations ++;
			tempBooleanPositiveRegion[i] = true;
		}//Of for i
		
		//System.out.println("Processing attribute test 3");

		if (tempBlocks != null) {
			for (int i = 0; i < tempBlocks.length; i ++) {
				for (int j = 0; j < tempBlocks[i].length; j ++) {
					operations ++;
					tempBooleanPositiveRegion[tempBlocks[i][j]] = false;
				}//Of for j
			}//Of for i
		}//Of if

		return booleanArrayToIndexArray(tempBooleanPositiveRegion);
	}// Of positiveRegionTopDown
	
	/**
	 ************************* 
	 * Partition the universe according to the given attributes.
	 * 
	 * @param paraAttributes
	 *            the given box. Which instances are included.
	 ************************* 
	 * */
	public int[] reduction() throws Exception {
		int[][] tempBlocks = new int[1][];
		
		int[] tempBlock = new int[numInstances()];
		//The block containing all objects.
		for (int i = 0; i < numInstances(); i++) {
			tempBlock[i] = i;
		}//Of for i
		tempBlocks[0] = tempBlock;
		System.out.println("At the beginning, entropy = " + conditionalEntropyFromIndices(tempBlocks));
		int tempBlocksLength = 1;
		//int tempPositiveCount = 0;
		
		int[][][] tempCubic;
		//SimpleTool.printIntMatrix(tempBlocks);

		for (int i = 0; i < numAttributes() - 1; i++) {
			//System.out.println("Processing attribute " + i);
			//Step 1.1 Partition the remaining objects according to a_i.
			// Generate the cubic
			tempCubic = new int[tempBlocksLength][][];
			//int tempRows = 0;
			for (int j = 0; j < tempBlocksLength; j++) {
				operations ++;
				tempCubic[j] = intPartitionFromAttributeRemovePure(i,
						tempBlocks[j]);
			}// Of for i
			//System.out.println("Processing attribute test 1");

			// Step 1.2. Merge the cubic to a matrix
			tempBlocks = intCubicToIntMatrix(tempCubic);
			System.out.println("After attribute " + i + ", entropy = " + conditionalEntropyFromIndices(tempBlocks));
			//System.out.println("Processing attribute test 2");
			//SimpleTool.printIntMatrix(tempBlocks);
			if (tempBlocks == null) {
				break;
			}//Of if
			//System.out.println("Processing attribute test 3");
			tempBlocksLength = tempBlocks.length;
		}// Of for i
		
		boolean[] tempBooleanPositiveRegion = new boolean[numInstances()];
		for (int i = 0; i < numInstances(); i ++) {
			operations ++;
			tempBooleanPositiveRegion[i] = true;
		}//Of for i
		
		//System.out.println("Processing attribute test 3");

		if (tempBlocks != null) {
			for (int i = 0; i < tempBlocks.length; i ++) {
				for (int j = 0; j < tempBlocks[i].length; j ++) {
					operations ++;
					tempBooleanPositiveRegion[tempBlocks[i][j]] = false;
				}//Of for j
			}//Of for i
		}//Of if

		return booleanArrayToIndexArray(tempBooleanPositiveRegion);
	}// Of positiveRegionTopDown
		
	/**
	 ************************* 
	 * Compress the given matrix using the new length/rows.
	 * 
	 * @param paraAttributes
	 *            the given box. Which instances are included.
	 ************************* 
	 * */
	public int[][] compressMatrix(int[][] paraMatrix, int paraLength)
			throws Exception {
		if (paraMatrix.length == paraLength) {
			return paraMatrix;
		}// Of if

		if (paraMatrix.length < paraLength) {
			throw new Exception("The given matrix has length "
					+ paraMatrix.length
					+ " while you try to compress it to length " + paraLength);
		}// Of if

		int[][] resultMatrix = new int[paraLength][];
		for (int i = 0; i < paraLength; i++) {
			resultMatrix[i] = paraMatrix[i];
		}// Of for i

		return resultMatrix;
	}// Of compressMatrix

	/**
	 ************************* 
	 * Compress the given matrix using the new length/rows.
	 * 
	 * @param paraAttributes
	 *            the given box. Which instances are included.
	 ************************* 
	 * */
	public boolean[][] compressMatrix(boolean[][] paraMatrix, int paraLength)
			throws Exception {
		if (paraMatrix.length == paraLength) {
			return paraMatrix;
		}// Of if

		if (paraMatrix.length < paraLength) {
			throw new Exception("The given matrix has length "
					+ paraMatrix.length
					+ " while you try to compress it to length " + paraLength);
		}// Of if

		boolean[][] resultMatrix = new boolean[paraLength][];
		for (int i = 0; i < paraLength; i++) {
			resultMatrix[i] = paraMatrix[i];
		}// Of for i

		return resultMatrix;
	}// Of compressMatrix

	/**
	 ************************* 
	 * Partition the universe according to the given attributes.
	 * 
	 * @param paraAttributes
	 *            the given box. Which instances are included.
	 ************************* 
	 * */
	public boolean[][] booleanPartitionBottomUp(boolean[] paraAttributes)
			throws Exception {
		boolean[][] tempMatrix = new boolean[numInstances()][];
		int tempNumberOfBlocks = 0;
		boolean[] tempInstanceProcessed = new boolean[numInstances()];

		// boolean[] tempBlock = new boolean[numInstances()];
		boolean tempEqual = true;
		// Compute equivalent class and put into the matrix
		for (int i = 0; i < numInstances(); i++) {
			// This instance is included in a box already
			if (tempInstanceProcessed[i]) {
				continue;
			}// Of if

			// 降低存储空间非常重要的步骤，按需分配第二维空间！！！
			tempMatrix[tempNumberOfBlocks] = new boolean[numInstances()];
			// Construct the block for current instance
			for (int j = i; j < numInstances(); j++) {
				tempEqual = true;
				for (int k = 0; k < numAttributes(); k++) {
					if (!paraAttributes[k]) {
						continue;
					}// Of if
					if (instance(j).value(k) != instance(i).value(k)) {
						tempEqual = false;
						break;
					}// Of if
				}// Of for k

				if (tempEqual) {
					tempInstanceProcessed[j] = true;
					tempMatrix[tempNumberOfBlocks][j] = true;
				}// Of if
			}// Of for j

			tempNumberOfBlocks++;
		}// Of for i

		// Compress the matrix
		boolean[][] resultMatrix = compressMatrix(tempMatrix,
				tempNumberOfBlocks);

		return resultMatrix;
	}// Of booleanPartitionBottomUp

	/**
	 ************************* 
	 * Partition the universe according to the given attributes.
	 * 
	 * @param paraAttributes
	 *            the given box. Which instances are included.
	 ************************* 
	 * */
	public int[][] booleanPartitionIndicesBottomUp(boolean[] paraAttributes)
			throws Exception {
		// tempMatrix[][]为n行、列数不定的二维数组,第二维动态分配存储空间
		int[][] tempMatrix = new int[numInstances()][];
		// tempNumberOfBlocks用来统计块数，即等价类的个数
		int tempNumberOfBlocks = 0;
		// tempInstancesInBlocks用来统计每一个等价类的对象数
		int[] tempInstancesInBlocks = new int[numInstances()];
		// tempInstanceProcessed[i]用来记录对象是否已加入某一个类中
		boolean[] tempInstanceProcessed = new boolean[numInstances()];
		int[] tempCurrentBlock = new int[numInstances()];
		// int tempCurrentRow = 0;
		
		boolean tempEqual = true;
		
		// Compute equivalent class and put into the matrix
		for (int i = 0; i < numInstances(); i++) {
			// This instance is included in a box already
			if (tempInstanceProcessed[i]) {
				continue;
			}// Of if
			System.out.println("-------------------------" );
			System.out.println("对象" + i + "等价类的计算" );
			// Construct the block for current instance
			for (int j = i; j < numInstances(); j++) {
				tempEqual = true;
				for (int k = 0; k < numAttributes(); k++) {
					if (!paraAttributes[k]) {
						continue;
					}// Of if
					if (instance(j).value(k) != instance(i).value(k)) {
						tempEqual = false;
						break;
					}// Of if
				}// Of for k
				// 若两个对象一致，则往当前块中加入对象j的下标
				if (tempEqual) {
					System.out.println("即将往第" + i + "个对象的等价类加入对象" + j);
					tempInstanceProcessed[j] = true;
					tempCurrentBlock[tempInstancesInBlocks[tempNumberOfBlocks]] = j;
					System.out.println("tempInstancesInBlocks[" + tempNumberOfBlocks + "] = "
							+ tempInstancesInBlocks[tempNumberOfBlocks]);
					System.out.println("tempCurrentBlock[" + tempInstancesInBlocks[tempNumberOfBlocks] + "] = "
							+ j);
					tempInstancesInBlocks[tempNumberOfBlocks]++;
					
				}// Of if
			}// Of for j

			// 将当前等价类压缩存放至tempCompressedBlock中，并将压缩后的数组加到tempMatrix中。
			int[] tempCompressedBlock = new int[tempInstancesInBlocks[tempNumberOfBlocks]];
			for (int j = 0; j < tempInstancesInBlocks[tempNumberOfBlocks]; j++) {
				tempCompressedBlock[j] = tempCurrentBlock[j];
			}// Of for j
			tempMatrix[tempNumberOfBlocks] = tempCompressedBlock;

			tempNumberOfBlocks++;
		}// Of for i

		// Compress the matrix
		int[][] resultMatrix = compressMatrix(tempMatrix, tempNumberOfBlocks);

		return resultMatrix;
	}// Of booleanPartitionIndicesBottomUp

	/**
	 ************************* 
	 * Partition the universe according to all attributes.
	 ************************* 
	 * */
	public boolean[][] booleanPartitionBottomUp() throws Exception {
		return booleanPartitionBottomUp(allAttributes);
	}// Of booleanPartitionBottomUp

	/**
	 ************************* 
	 * Partition the universe according to all attributes.
	 ************************* 
	 * */
	public int[][] booleanPartitionIndicesBottomUp() throws Exception {
		return booleanPartitionIndicesBottomUp(allAttributes);
	}// Of booleanPartitionIndicesBottomUp

	/**
	 ************************* 
	 * Compute the equivalent class of the given object.
	 * 
	 * @param paraX
	 *            the index of the given instance.
	 * @param paraAttributes
	 *            the given attribute set.
	 ************************* 
	 * */
	public boolean[] equivalentClass(int paraX, boolean[] paraAttributes)
			throws Exception {
		boolean[] resultArray = new boolean[numInstances()];
		for (int i = 0; i < numInstances(); i++) {
			boolean tempEqual = true;
			for (int j = 0; j < numAttributes(); j++) {
				if (!paraAttributes[j]) {
					continue;
				}// Of if

				if (instance(i).value(j) != instance(paraX).value(j)) {
					tempEqual = false;
					break;
				}// Of if
			}// Of for j

			resultArray[i] = tempEqual;
		}// Of for i

		return resultArray;
	}// Of equivalentClass

	/**
	 ************************* 
	 * Compute the equivalent class of the given object.
	 * 
	 * @param paraX
	 *            the index of the given instance.
	 * @param paraAttributes
	 *            the given attribute set.
	 ************************* 
	 * */
	public int[] equivalentClassIndices(int paraX, boolean[] paraAttributes)
			throws Exception {
		int[] tempArray = new int[numInstances()];
		int tempIndex = 0;
		for (int i = 0; i < numInstances(); i++) {
			boolean tempEqual = true;
			for (int j = 0; j < numAttributes(); j++) {
				if (!paraAttributes[j]) {
					continue;
				}// Of if

				if (instance(i).value(j) != instance(paraX).value(j)) {
					tempEqual = false;
					break;
				}// Of if
			}// Of for j

			if (tempEqual) {
				tempArray[tempIndex] = i;
				tempIndex++;
			}// Of if
		}// Of for i

		// Compress
		int[] resultArray = new int[tempIndex];
		for (int i = 0; i < tempIndex; i++) {
			resultArray[i] = tempArray[i];
		}// Of for i

		return resultArray;
	}// Of equivalentClassIndices

	/**
	 ************************* 
	 * Compute the equivalent class of the given object.
	 * 
	 * @param paraX
	 *            the index of the given instance.
	 ************************* 
	 * */
	public int[] equivalentClassIndices(int paraX) throws Exception {
		boolean[] tempAllAttributes = new boolean[numAttributes()];
		for (int i = 0; i < numAttributes(); i++) {
			tempAllAttributes[i] = true;
		}// Of for i

		return equivalentClassIndices(paraX, tempAllAttributes);
	}// Of equivalentClassIndices

	/**
	 ************************* 
	 * Compute the equivalent class of the given object.
	 * 
	 * @param paraX
	 *            the index of the given instance.
	 ************************* 
	 * */
	public boolean[] equivalentClass(int paraX) throws Exception {
		boolean[] tempAllAttributes = new boolean[numAttributes()];
		for (int i = 0; i < numAttributes(); i++) {
			tempAllAttributes[i] = true;
		}// Of for i

		return equivalentClass(paraX, tempAllAttributes);
	}// Of equivalentClass

	/**
	 ************************* 
	 * Is paraX a subset of paraY. We assume that paraX and paraY are sorted
	 * from small to big.
	 * 
	 * @param paraX
	 *            对象集合X.
	 * @param paraY
	 * 			    对象集合Y.
	 ************************* 
	 * */
	public boolean isSubset(int[] paraX, int[] paraY) {
		// i,j分别用于控制两个集合下标的移动，类似于指针
		int i = 0;
		int j = 0;
		//循环判断，由于这里两个数组都是有序的，所以可以逐个移动查找判断
		while ((i < paraX.length) && (j < paraY.length)) {
			if (paraX[i] == paraY[j]) {
				i++;
				j++;
			} else if (paraX[i] > paraY[j]) {
				j++;
			} else {
				// 若集合Y的值已经大于X的值，意味着当前集合X的值不可能存在于Y中，返回false
				return false;
			}// Of if
		}// Of while
		//循环完后，如果集合x已经判断到了最后一个，意味着x是y的一个子集
		if (i == paraX.length) {
			return true;
		}// Of if

		return false;
	}// Of isSubset

	/**
	 ************************* 
	 * Is the intersection of paraX and paraY empty?
	 * 
	 * @param paraX
	 *            the index of the given instance.
	 ************************* 
	 * */
	public boolean isIntersectionEmpty(int[] paraX, int[] paraY) {
		System.out.println("Two sets");

		int i = 0;
		int j = 0;
		while ((i < paraX.length) && (j < paraY.length)) {
			if (paraX[i] == paraY[j]) {
				return false;
			} else if (paraX[i] > paraY[j]) {
				j++;
			} else {
				i++;
			}// Of if
		}// Of while

		return true;
	}// Of isIntersectionEmpty

	/**
	 ************************* 
	 * Convert a boolean array to an index array corresponding to true elements.
	 * 
	 * @param paraBooleanArray
	 *            the given boolean array
	 * @return the index array
	 ************************* 
	 * */
	public static int[] booleanArrayToIndexArray(boolean[] paraBooleanArray) {
		int tempSize = 0;
		// How many elements.
		for (int i = 0; i < paraBooleanArray.length; i++) {
			if (paraBooleanArray[i]) {
				tempSize++;
			}// Of if
		}// Of for i

		if (tempSize == 0) {
			return null;
		}// Of if

		// Determine the values.
		int[] resultArray = new int[tempSize];
		int tempIndex = 0;
		for (int i = 0; i < paraBooleanArray.length; i++) {
			if (paraBooleanArray[i]) {
				resultArray[tempIndex] = i;
				tempIndex++;
			}// Of if
		}// Of for i

		return resultArray;
	}// Of booleanArrayToIndexArray

	/**
	 ************************* 
	 * Compute the equivalent class of the given object.
	 * 
	 * @param paraX
	 *            the index of the given instance.
	 ************************* 
	 * */
	public int[] computeLowerApproximation(int[] paraX, boolean[] paraAttributes)
			throws Exception {
		boolean[] tempLowerApproximation = computeBooleanLowerApproximation(
				paraX, paraAttributes);

		// Compress
		int[] resultLowerApproximation = booleanArrayToIndexArray(tempLowerApproximation);

		return resultLowerApproximation;
	}// Of computeLowerApproximation

	/**
	 ************************* 
	 * Compute the equivalent class of the given object.
	 * 
	 * @param paraX
	 *            the index of the given instance.
	 ************************* 
	 * */
	public boolean[] computeBooleanLowerApproximation(int[] paraX,
			boolean[] paraAttributes) throws Exception {
		int[][] tempMatrix = booleanPartitionIndicesBottomUp(paraAttributes);
		boolean[] tempLowerApproximation = new boolean[numInstances()];

		for (int i = 0; i < tempMatrix.length; i++) {
			boolean tempIsSubset = isSubset(tempMatrix[i], paraX);
			if (tempIsSubset) {
				for (int j = 0; j < tempMatrix[i].length; j++) {
					tempLowerApproximation[tempMatrix[i][j]] = true;
					// tempSize++;
				}// Of for j
			}// Of if
		}// Of for i

		return tempLowerApproximation;
	}// Of computeBooleanLowerApproximation

	/**
	 ************************* 
	 * Compute the equivalent class of the given object.
	 * 
	 * @param paraX
	 *            the index of the given instance.
	 ************************* 
	 * */
	public int[] computeLowerApproximation(int[] paraX) throws Exception {
		boolean[] tempAllAttributes = new boolean[numAttributes()];
		for (int i = 0; i < numAttributes(); i++) {
			tempAllAttributes[i] = true;
		}// Of for i
		return computeLowerApproximation(paraX, tempAllAttributes);
	}// Of computeLowerApproximation

	/**
	 ************************* 
	 * Compute the equivalent class of the given object.
	 * 
	 * @param paraX
	 *            the index of the given instance.
	 ************************* 
	 * */
	public int[] computeUpperApproximation(int[] paraX) throws Exception {
		int[][] tempMatrix = booleanPartitionIndicesBottomUp();
		boolean[] tempLowerApproximation = new boolean[numInstances()];
		// int tempSize = 0;

		for (int i = 0; i < tempMatrix.length; i++) {
			//SimpleTool.printIntArray(tempMatrix[i]);
			boolean tempIsIntersectionEmpty = isIntersectionEmpty(
					tempMatrix[i], paraX);
			if (!tempIsIntersectionEmpty) {
				for (int j = 0; j < tempMatrix[i].length; j++) {
					tempLowerApproximation[tempMatrix[i][j]] = true;
					// tempSize++;
				}// Of for j
			}// Of if
		}// Of for i

		// Compress
		int[] resultLowerApproximation = booleanArrayToIndexArray(tempLowerApproximation);

		return resultLowerApproximation;
	}// Of computeLowerApproximation

	/**
	 ************************* 
	 * Compute the positive region of the decision system. The last attribute is
	 * the class.
	 * 
	 ************************* 
	 * */
	public int[] positiveRegion() throws Exception {
		boolean[] resultBooleanApproximation = new boolean[numInstances()];

		// Step 1. Divide the universe into a number of concepts/subsets
		// according to the class.
		int[][] tempConcepts = booleanPartitionIndicesBottomUp(decision);
		boolean[] tempApproximation;

		for (int i = 0; i < tempConcepts.length; i++) {
			// Step 2.1 Compute the lower approximation of each concept.
			tempApproximation = computeBooleanLowerApproximation(
					tempConcepts[i], conditions);
			//System.out.println("The lower approximation of ");
			//SimpleTool.printIntArray(tempConcepts[i]);
			//System.out.println("It is");
			//SimpleTool.printIntArray(booleanArrayToIndexArray(tempApproximation));
			//System.out.println("Lower approximation ends");

			// Step 2.2 Merge the lower approximations to form the positive
			// region.
			for (int j = 0; j < numInstances(); j++) {
				operations ++;
				if (tempApproximation[j]) {
					resultBooleanApproximation[j] = true;
				}// of if
			}// Of for j
		}// Of for i

		// Compress
		int[] resultPositiveRegion = booleanArrayToIndexArray(resultBooleanApproximation);

		return resultPositiveRegion;
	}// Of positiveRegion

	/**
	 ************************* 
	 * Computes the entropy of an array.
	 * Elements in the array should not be smaller than 0.
	 * 
	 * @param paraIntArray
	 ************************* 
	 */
	public static double conditionalEntropy(int[][] paraMatrix) {
		double resultEntropy = 0;
		int tempSubsetSize = 0;
		int tempTotalSize = 0;
		for (int i = 0; i < paraMatrix.length; i ++) {
			tempSubsetSize = 0;
			if (paraMatrix[i] == null) {
				continue;
			}//Of if
			
			for (int j = 0; j < paraMatrix[i].length; j ++) {
				tempSubsetSize += paraMatrix[i][j];
			}//Of for j
			tempTotalSize += tempSubsetSize;
			
			if (tempSubsetSize == 0) {
				continue;
			}//Of if
			
			resultEntropy += tempSubsetSize * entropy(paraMatrix[i]);
		}//Of for i
		
		resultEntropy /= tempTotalSize;
		return resultEntropy;
	}//Of conditionalEntropy

	/**
	 ************************* 
	 * Computes the entropy of an array.
	 * Elements in the array should not be smaller than 0.
	 * 
	 * @param paraIntArray
	 ************************* 
	 */
	public  static double conditionalEntropyForDataset(int[][] paraMatrix) {
		double resultEntropy = 0;
		int tempSubsetSize = 0;
		for (int i = 0; i < paraMatrix.length; i ++) {
			tempSubsetSize = 0;
			if (paraMatrix[i] == null) {
				continue;
			}//Of if
			
			for (int j = 0; j < paraMatrix[i].length; j ++) {
				tempSubsetSize += paraMatrix[i][j];
			}//Of for j
			
			if (tempSubsetSize == 0) {
				continue;
			}//Of if
			
			resultEntropy += tempSubsetSize * entropy(paraMatrix[i]);
		}//Of for i
	
		resultEntropy /= paraMatrix.length;
		//System.out.println(paraMatrix.length);
		return resultEntropy;
	}//Of conditionalEntropyForDataset

	/**
	 ************************* 
	 * Computes the entropy of an array.
	 * Elements in the array should not be smaller than 0.
	 * 
	 * @param paraIntArray
	 ************************* 
	 */
	public double conditionalEntropyFromIndices(int[][] paraIndicesMatrix) {
		if (paraIndicesMatrix == null) {
			return 0;
		}//Of if
		
		//Step 1. Convert the indices to distribution matrix.
		int tempClassLabels = attribute(numAttributes() - 1).numValues();
		int[][] tempMatrix = new int[paraIndicesMatrix.length][tempClassLabels];
		int tempColumn = 0;
		for (int i = 0; i < paraIndicesMatrix.length; i ++) {
			for (int j = 0; j < paraIndicesMatrix[i].length; j ++) {
				tempColumn = (int)instance(paraIndicesMatrix[i][j]).value(numAttributes() - 1);
				tempMatrix[i][tempColumn]++; 
			}//Of for j
		}//Of for i

		//Step 2. Compute the conditional entropy
		return conditionalEntropyForDataset(tempMatrix);
	}//Of conditionalEntropyFromIndices

		
	/**
	 ************************* 
	 * Computes the entropy of an array.
	 * Elements in the array should not be smaller than 0.
	 * 
	 * @param paraIntArray
	 ************************* 
	 */
	public static double entropy(int[] paraArray) {
		double resultEntropy = 0;
		int tempTotal = 0;
		for (int i = 0; i < paraArray.length; i++) {
			tempTotal += paraArray[i];
			if (paraArray[i] > 0) {
				resultEntropy -= paraArray[i] * Utils.log2(paraArray[i]);
			}//Of if
		}// Of for j
		resultEntropy /= tempTotal;
		resultEntropy += Utils.log2(tempTotal);

		return resultEntropy;
	}// Of classEntropy

	
	/**
	 ********************************** 
	 * Test the booleanPartitionFromAttribute method.
	 ********************************** 
	 */
	public void testParition() {
		boolean[][] tempMatrix = null;
		try {
			
			System.out.println("Test partition using one attribute.");
			tempMatrix = booleanPartitionFromAttribute(0);
			// SimpleTool.printBooleanMatrix(tempMatrix);

			System.out.println("Test partition using the second attributes.");
			boolean[][] tempMatrix2 = booleanPartitionFromAttribute(1,
					tempMatrix[0]);
			SimpleTool.printBooleanMatrix(tempMatrix2);

			System.out.println("Test partition using some attributes.");
			boolean[][] tempMatrix3 = booleanPartitionFromAttribute(1,
					tempMatrix);
			SimpleTool.printBooleanMatrix(tempMatrix3);

			operations = 0;
			System.out.println("Test partition using all attributes.");
			boolean[][] tempMatrix4 = booleanPartitionFromAllAttributes();
			SimpleTool.printBooleanMatrix(tempMatrix4);
			System.out.println("There are  " + operations + " operations.");

			boolean[] tempAttributes = new boolean[numAttributes()];
			tempAttributes[1] = true;
			// tempAttributes[3] = true;
			// System.out.println("Test partition using specified attributes.");
			// boolean[][] tempMatrix5 =
			// booleanPartitionFromSomeAttributes(tempAttributes);
			// SimpleTool.printBooleanMatrix(tempMatrix5);

			System.out.println("Test equivalent class.");
			boolean[] tempEquivalentClass = equivalentClass(0);
			SimpleTool.printBooleanArray(tempEquivalentClass);

			System.out.println("Test equivalent class indices.");
			int[] tempEquivalentClassIndices = equivalentClassIndices(0);
			SimpleTool.printIntArray(tempEquivalentClassIndices);

			operations = 0;
			System.out.println("Test partition using the bottom-up approach.");
			// boolean[][] tempMatrix6 = booleanPartitionBottomUp();
			// SimpleTool.printBooleanMatrix(tempMatrix6);
			System.out.println("There are  " + operations + " operations.");

			operations = 0;
			System.out
					.println("Test partition using the bottom-up approach with indices.");
			boolean[] tempBooleanArray = new boolean[numAttributes()];
			
			// tempBooleanArray[0] = true;
			// tempBooleanArray[1] = true;
			// tempBooleanArray[2] = true;
			int[][] tempMatrix7 = booleanPartitionIndicesBottomUp();
			System.out.println("There are " + tempMatrix7.length + " blocks.");
			for (int i = 0; i < tempMatrix7.length; i++) {
				SimpleTool.printIntArray(tempMatrix7[i]);
			}// Of for i
			System.out.println("There are  " + operations + " operations.");
		
			/*
			 * int[] tempX = { 0, 1, 2, 3, 4, 5 };
			 * System.out.println("Before computing lower approximation.");
			 * int[] tempLowerApproximation = computeLowerApproximation(tempX);
			 * System.out.println("Lower approximation of ");
			 * SimpleTool.printIntArray(tempX); System.out.println("it is:");
			 * SimpleTool.printIntArray(tempLowerApproximation);
			 * 
			 * System.out.println("Before computing upper approximation.");
			 * int[] tempUpperApproximation = computeUpperApproximation(tempX);
			 * System.out.println("Upper approximation of ");
			 * SimpleTool.printIntArray(tempX); System.out.println("it is:");
			 * SimpleTool.printIntArray(tempUpperApproximation);
			 */

			
			operations = 0;
			int[] tempPositiveRegion = positiveRegion();
			System.out.println("Positive region: ");
			SimpleTool.printIntArray(tempPositiveRegion);
			
			System.out.println("There are  " + operations + " operations.");
			operations = 0;
			SimpleTool.printIntArray(positiveRegionTopDown());
			System.out.println("There are  " + operations + " operations.");
			
		} catch (Exception ee) {
			System.out.println(ee);
		}// Of try

	}// Of testParition

	/**
	 ********************************** 
	 * Test the booleanPartitionFromAttribute method.
	 ********************************** 
	 */
	public void testEntropy() {
		int[][] tempMatrixForEntropy = {{0, 0, 0, 0, 0, 0}, {0, 0, 0, 0,0,  0}, {1,0, 0, 0,0,   1},{0, 0, 0, 0, 0, 0}, {0, 0, 0, 0,0,  0}, {1,0, 0, 0,0,   1}};
		double tempEntropy = conditionalEntropyForDataset(tempMatrixForEntropy);
		System.out.println("The entropy is: " + tempEntropy);
	}//Of testEntropy
	
	/**
	 ********************************** 
	 * Test the class.
	 ********************************** 
	 */
	public static void main(String[] args) {
		int[][] tempMatrix = { 
				 { 0, 1, 1}, 
				 { 0, 0, 1}, 
				 { 1, 1, 0}, 
				 { 1, 0, 1},
				 { 1, 1, 0}, 
				 { 1, 1, 0}, 
				 { 1, 0, 1}};
		 
			
		 int[][] tempMatrix1 = { 
				 {0, 1, 1}, 
				 {0, 0, 1}, 
				 {0, 1, 0}, 
				 {0, 0, 1},
				 {0, 1, 0}, 
				 {1, 1, 0}, 
				 {0, 0, 1}};
		 int[][] tempMatrix2 = { 
				 {0, 1, 1}, 
				 {0, 0, 1}, 
				 {1, 1, 0}, 
				 {0, 0, 1},
				 {0, 1, 0}, 
				 {1, 1, 0}, 
				 {0, 0, 1}};
		
		String arffFilename = "data/java/test.arff";
		
		// String arffFilename = "data/mushroom.arff";
		SimpleNominalDecisionSystem tempSystem = SimpleNominalDecisionSystem
				.constructFromFile(arffFilename);

		// System.out.println(tempSystem.toString());
		
		// tempSystem.testParition();
		
		int[][] tempReductedDecisionSystem = new int[tempSystem.numInstances()][tempSystem.numAttributes()];
		for (int i = 0; i < tempSystem.numInstances(); i++) {
			for (int j = 0; j < tempSystem.numAttributes(); j++) {
				tempReductedDecisionSystem[i][j] = (int)tempSystem.instance(i).value(j);
			}// Of for j
		}// Of for i

		// SimpleTool.printIntMatrix(tempReductedDecisionSystem);
		
		// double tempEntropy = tempSystem.conditionalEntropy(testMatrix);
	// 	System.out.println("The entropy is: " + tempEntropy);
		
	//	tempEntropy = ConditionalEntropy
	//	.conditionalEntropy(tempReductedDecisionSystem);
	//	System.out.println("The entropy is: " + tempEntropy);
		
		//double tempEntropy = conditionalEntropyForDataset(tempReductedDecisionSystem);
		double tempEntropy = conditionalEntropyForDataset(tempMatrix);
		System.out.println("The entropy is: " + tempEntropy);
		
	}// Of main

}// Of class SimpleNominalDecisionSystem
