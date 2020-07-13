package coser.datamodel.decisionsystem;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;

import weka.classifiers.trees.*;
import weka.core.*;
import coser.common.*;

import java.util.List;
import java.io.FileNotFoundException;  
import java.io.PrintStream;  

/**
 * Summary:离散化算法：局部离散化+全局离散化<br>
 * 1) scaling the attribtue;<br>
 * 2) M-relative reduct;<br>
 * <p>
 * Author: <b>Liu-ying Wen</b> wenliuying1983@163.com
 * <p>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://scs.swpu.edu.cn/smale/ Lab of Machine
 * Learning</a>, Southwest Petroleum University, Chengdu 610500, China.<br>
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Progress: beginning.<br>
 * Written time: March 16, 2015. <br>
 * Last modify time: March 16, 2015.
 */

public class NewLocalDisPlusGlobalDisFastVersion2 extends RoughDecisionSystem {

	private static final long serialVersionUID = 30508534636805637L;
	static int[] numValuesOfAttribute;
	static boolean[][] reductResults;
	static boolean[] reductResultsOfGlobal;
	static int[][] candidateMergeIndex;
	// static int[][] beginIndexOfAttribute;
	// static int[][] endIndexOfAttribute;
	static int[][] beginAndEndIndexOfAttribute;
	static int totalNumValues;
	static double[][] valueRangeOfAttributes;
	static boolean[] statusOfAttribute;

	/**
	 ************************* 
	 * Construct the decision system. Simply adopt everything of the super
	 * class. This is the most often used constructor since it reads from a
	 * file.
	 * 
	 * @param paraReader
	 *            the given reader, often with the form of a file reader. We can
	 *            generate a FileReader given an .arff filename.
	 ************************* 
	 */
	public NewLocalDisPlusGlobalDisFastVersion2(Reader paraReader) throws IOException,
			Exception {
		super(paraReader);
	}// Of the first constructor

	
    
	/**
	 ************************* 
	 * Construct the decision system. Simply adopt everything of the super
	 * class.
	 ************************* 
	 */
	public NewLocalDisPlusGlobalDisFastVersion2(Instances paraDataset) throws Exception {
		super(paraDataset);
	}// Of the second constructor

	/**
	 ************************* 
	 * Construct the decision system. Simply adopt everything of the super
	 * class. Rarely used.
	 ************************* 
	 */
	public NewLocalDisPlusGlobalDisFastVersion2(Instances paraDataset, int paraCapacity)
			throws Exception {
		super(paraDataset, paraCapacity);
	}// Of the third constructor

	/**
	 ************************* 
	 * Construct the decision system. Simply adopt everything of the super
	 * class.
	 ************************* 
	 */
	public NewLocalDisPlusGlobalDisFastVersion2(Instances paraSource, int paraFirst,
			int paraToCopy) throws Exception {
		super(paraSource, paraFirst, paraToCopy);
	}// Of the fourth constructor

	/**
	 ************************* 
	 * Construct the decision system. Simply adopt everything of the super
	 * class.
	 ************************* 
	 */
	public NewLocalDisPlusGlobalDisFastVersion2(String paraName, FastVector paraAttInfo,
			int paraCapacity) throws Exception {
		super(paraName, paraAttInfo, paraCapacity);
	}// Of the fifth constructor

	/**
	 ************************* 
	 * Construct the decision system. Simply adopt everything of the super
	 * class.
	 ************************* 
	 */
	public NewLocalDisPlusGlobalDisFastVersion2(NewLocalDisPlusGlobalDisFastVersion2 paraDataset,
			boolean[] paraSelectedAttributes) throws Exception {
		super(paraDataset, paraSelectedAttributes);
	}// Of the sixth constructor

	/**
	 *******************
	 * Write a double-dimension array in a .arff file.  
	 * @param paraMatrixOfData
	 * 			  The given matrix data.
	 * @param paraOperateType
	 * 			  current operate,scaling or reduct. 
	 * @throws Exception
	 * 
	 * @return the decision system that restore 
	 * 
	 ******************* 
	 */
	public  NewLocalDisPlusGlobalDisFastVersion2 writeInFile(int[][] paraMatrixOfData, 
			String paraOperateType) throws Exception {
		StringBuffer sb = new StringBuffer();
		String filename = arffFilename.substring(0, arffFilename.length() - 5)
				+ "_" + paraOperateType + ".arff";
		// Relation name
		sb.append(ARFF_RELATION + " " + Utils.quote(relationName())
				+ "_" + paraOperateType + "\r\n");
		// Conditional attributes
		for (int i = 0; i < paraMatrixOfData[0].length - 1; i++) {
			sb.append("@ATTRIBUTE a" + i);
			sb.append("{0,1,2,3,4,5,6,7,8,9,10}\r\n");
		}// Of for i

		// The class attribute and all its values
		sb.append("@ATTRIBUTE class{");
		for (int i = 0; i < numClasses(); i++) {
			sb.append(i);
			if (i < numClasses() - 1) {
				sb.append(",");
			} else {
				sb.append("}\r\n");
			}
		}// Of for i
		// @DATA
		sb.append("\r\n" + ARFF_DATA + "\r\n");
		for(int i = 0; i < paraMatrixOfData.length; i++) {
			sb.append(paraMatrixOfData[i][0]);
			for(int j = 1; j < paraMatrixOfData[i].length; j++) {
				sb.append(", ").append(paraMatrixOfData[i][j]);
			}// Of for j
			sb.append("\r\n");
		}// Of for i
		
		// Write to an arff file.
		try(DataOutputStream dos = new DataOutputStream(
				new BufferedOutputStream(new FileOutputStream(filename)))) {
			dos.writeBytes(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}// Of try 
		NewLocalDisPlusGlobalDisFastVersion2 currentDs = null;
	
		// Read the arff file and return.
		try {
			FileReader fileReader = new FileReader(filename);
			currentDs = new NewLocalDisPlusGlobalDisFastVersion2(fileReader);
			currentDs.setClassIndex(paraMatrixOfData[0].length - 1);
			currentDs.setArffFilename(filename);
			fileReader.close();
			// System.out.println("The new data is " + currentDs);
		} catch (IOException e) {
				e.printStackTrace();
		}// Of try
		return currentDs;
	}// Of writeInFile
	
	
	/**
	 *******************
	 * Write a double-dimension array in a .arff file.  
	 * @param paraMatrixOfData
	 * 			  The given matrix data.
	 * @param paraOperateType
	 * 			  current operate,scaling or reduct. 
	 * @throws Exception
	 * 
	 * @return the decision system that restore 
	 * 
	 ******************* 
	 */
	public  NewLocalDisPlusGlobalDisFastVersion2 writeIntDSInFile(int[][] paraMatrixOfData, 
			String paraOperateType,int paraK) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		String filename = arffFilename.substring(0, arffFilename.length() - 5)
				+ "_" + paraK+ "_"+paraOperateType + ".arff";
		// Relation name
		sb.append(ARFF_RELATION + " " + Utils.quote(relationName())
				+ "_" + paraOperateType + "\r\n");
		// Conditional attributes
		for (int i = 0; i < paraMatrixOfData[0].length - 1; i++) {
			sb.append("@ATTRIBUTE a" + i);
			sb.append(" real\r\n");
		}// Of for i

		// The class attribute and all its values
		sb.append("@ATTRIBUTE class{");
		for (int i = 0; i < numClasses(); i++) {
			sb.append(i);
			if (i < numClasses() - 1) {
				sb.append(",");
			} else {
				sb.append("}\r\n");
			}
		}// Of for i
		// @DATA
		sb.append("\r\n" + ARFF_DATA + "\r\n");
		for(int i = 0; i < paraMatrixOfData.length; i++) {
			sb.append(paraMatrixOfData[i][0]);
			for(int j = 1; j < paraMatrixOfData[i].length; j++) {
				sb.append(", ").append(paraMatrixOfData[i][j]);
			}// Of for j
			sb.append("\r\n");
		}// Of for i
		
		// Write to an arff file.
		try(DataOutputStream dos = new DataOutputStream(
				new BufferedOutputStream(new FileOutputStream(filename)))) {
			dos.writeBytes(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}// Of try 
		NewLocalDisPlusGlobalDisFastVersion2 currentDs = null;
	
		// Read the arff file and return.
		try {
			FileReader fileReader = new FileReader(filename);
			currentDs = new NewLocalDisPlusGlobalDisFastVersion2(fileReader);
			currentDs.setClassIndex(paraMatrixOfData[0].length - 1);
			currentDs.setArffFilename(filename);
			fileReader.close();
			// System.out.println("The new data is " + currentDs);
		} catch (IOException e) {
				e.printStackTrace();
		}// Of try
		return currentDs;
	}// Of writeInFile
	
	
	/**
	 *******************
	 * Write a double-dimension array in a .arff file.  
	 * @param paraMatrixOfData
	 * 			  The given matrix data.
	 * @param paraOperateType
	 * 			  current operate,scaling or reduct. 
	 * @throws Exception
	 * 
	 * @return the decision system that restore 
	 * 
	 ******************* 
	 */
	
	public NewLocalDisPlusGlobalDisFastVersion2 writeAndReadArffFile(
			int[][] paraMatrixOfData, String paraOperateType) throws Exception {

		String filename = arffFilename.substring(0, arffFilename.length() - 5)
				+ "_" + paraOperateType + ".arff";
		String messageNormalized = "";
		NumberFormat numFormat = NumberFormat.getNumberInstance();
		numFormat.setMaximumFractionDigits(5);
		
		long startTime;
		long endTime;
		
		System.out.println("messageNormalized开始");
		startTime = new Date().getTime();
		for (int i = 0; i < numberOfInstances; i++) {
			for (int j = 0; j < paraMatrixOfData[i].length - 1; j++) {
				messageNormalized += numFormat.format(paraMatrixOfData[i][j]) + ", ";
				// messageNormalized += paraMatrixOfData[i][j] + ", ";
			}// Of for j
			messageNormalized += classAttribute().value(
					(int) instance(i).value(numberOfConditions));
			messageNormalized += "\r\n";// including class attribute
		}// Of for i
		endTime = new Date().getTime();
		System.out.println("messageNormalized结束");
		System.out.println("将二维数组信息连接起来存储到messageNormalized所用时间\r\n " + 
							(endTime - startTime) + "ms");
		
		StringBuffer text = new StringBuffer();
		
		startTime = new Date().getTime();
		// @RELATION The name of decision system
		text.append(ARFF_RELATION + " " + Utils.quote(relationName())
				+ "_scaling\r\n");
		// Conditional attributes
		for (int i = 0; i < paraMatrixOfData[0].length - 1; i++) {
			text.append("@ATTRIBUTE a" + i);
			text.append(" {0,1}\r\n");
		}// Of for i
			// The class attribute and all its values
		text.append("@ATTRIBUTE class{");
		for (int i = 0; i < numClasses(); i++) {
			text.append(classAttribute().value(i));
			if (i < numClasses() - 1) {
				text.append(",");
			} else {
				text.append("}\r\n");
			}
		}// Of for i
		
		// @DATA
		text.append("\r\n" + ARFF_DATA + "\r\n");
		// All instances
		text.append(messageNormalized);
		endTime = new Date().getTime();
		System.out.println("将所有信息append到buffer所需时间为\r\n " + 
							(endTime - startTime) + "ms");
		
		startTime = new Date().getTime();
		// Step 3. Write to an arff file.
		try {
			String s = text.toString();
			FileWriter fw = new FileWriter(filename);
			fw.write(s, 0, s.length());
			fw.flush();
			fw.close();
			//System.out.println("写结束");
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in DecisionSystem.nomalize() while writing to a file.\r\n"
							+ ee);
		}// Of try
		
		endTime = new Date().getTime();
		System.out.println("将信息存到arff文件所需时间为\r\n " + 
							(endTime - startTime) + "ms");
		
		startTime = new Date().getTime();
		// Step 4. Read the arff file and return.
		NewLocalDisPlusGlobalDisFastVersion2 currentDs;
		try {
			FileReader fileReader = new FileReader(filename);
			currentDs = new NewLocalDisPlusGlobalDisFastVersion2(fileReader);
			currentDs.setClassIndex(paraMatrixOfData[0].length - 1);
			currentDs.setArffFilename(filename);
			fileReader.close();
			// System.out.println("The new data is " + currentDs);
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred  while reading from a file.\r\n" + ee);
		}// Of try
		
		endTime = new Date().getTime();
		System.out.println("将信息读取到arff文件所需时间为\r\n " +
							(endTime - startTime) + "ms");
		return currentDs;

	}// Of writeAndReadArffFile

	/**
	 ************************* 
	 * 根据新的属性值列表，扩展原决策表. 
	 * @return 新的信息表
	 ************************* 
	 */
	public int[][] scalingDecisionSystem(RoughDecisionSystem paraRoughDecisionSystem) {
		int tempInstances = paraRoughDecisionSystem.numInstances();
		int[][] tempCurrentScalingData = new int[tempInstances][totalNumValues + 1];
		int tempIndexOfNewAttribute = 0;
		
		// Step 2. 根据新的属性列表，计算得到新的决策表S*
		for (int i = 0; i < tempInstances; i++) {
			for (int j = 0; j < paraRoughDecisionSystem.numAttributes() - 1; j++) {
				for (int k = 0; k < valueRangeOfAttributes[j].length; k++) {
					if (j == 0)
						tempIndexOfNewAttribute = k;
					else {
						int tempCount = 0;
						int tempI = j;
						while (tempI > 0) {
							tempCount += valueRangeOfAttributes[tempI - 1].length;
							tempI--;
						}// Of while
						tempIndexOfNewAttribute = k + tempCount;
					}// Of else
					
					if ((paraRoughDecisionSystem.instance(i).value(j)) >= valueRangeOfAttributes[j][k])
						tempCurrentScalingData[i][tempIndexOfNewAttribute] = 1;
					else
						tempCurrentScalingData[i][tempIndexOfNewAttribute] = 0;
				}// Of for k
			}// Of for j
			tempCurrentScalingData[i][totalNumValues] = (int) (paraRoughDecisionSystem.instance(i)
					.value(numAttributes() - 1));
		}// Of for i
		return tempCurrentScalingData;
	}
	
	/**
	 ************************* 
	 * 根据选择属性的属性值列表，扩展原决策表，每次只扩展一个属性.
	 * @param paraRoughDecisionSystem 
	 * 			原决策表S
	 * @param paraScaledAttribute
	 * 			所选的属性
	 * @return 扩展后的信息表S‘
	 ************************* 
	 */
	public int[][] scalingDecisionSystem(
			RoughDecisionSystem paraRoughDecisionSystem, int paraScaledAttribute) {
		int tempNumOfAttribtueValue = numValuesOfAttribute[paraScaledAttribute];
		int tempInstances = paraRoughDecisionSystem.numInstances();
		int[][] tempCurrentScalingData = new int[tempInstances][tempNumOfAttribtueValue + 1];
		int tempIndexOfNewAttribute = 0;
		// Step 1. 根据新的属性列表，计算得到新的决策表S’
		for (int i = 0; i < tempInstances; i++) {
			tempIndexOfNewAttribute = 0;
			for (int j = 0; j < valueRangeOfAttributes[paraScaledAttribute].length; j++) {
				if ((paraRoughDecisionSystem.instance(i)
						.value(paraScaledAttribute)) >= valueRangeOfAttributes[paraScaledAttribute][j])
					tempCurrentScalingData[i][tempIndexOfNewAttribute] = 1;
				else
					tempCurrentScalingData[i][tempIndexOfNewAttribute] = 0;
				tempIndexOfNewAttribute++;
			}// Of for j
			tempCurrentScalingData[i][tempNumOfAttribtueValue] = (int) (paraRoughDecisionSystem
					.instance(i).value(numAttributes() - 1));
		}// Of for i
		return tempCurrentScalingData;
	}
	
	/**
	 ************************* 
	 * 根据当前属性，对子表进行排序
	 * @param paraRoughDecisionSystem 
	 * 			原决策表S
	 * @param paraScaledAttribute
	 * 			所选的属性
	 * @return 排序后的信息子表S‘
	 ************************* 
	 */
	public double[][] sortDSAndComputeNewAttribtue(RoughDecisionSystem paraRoughDecisionSystem, int paraScaledAttribute){
		int tempInstances = paraRoughDecisionSystem.numInstances();
		
		double[] candidateValueSets = new double[tempInstances];
		double[][] sortedValueSets = new double[2][tempInstances];
		
		// System.out.println("属性的原始属性值列表");
		// Step 1. 扫描原决策表，获取每一个属性的属性值列表
 		for(int  i = 0; i < tempInstances; i++){
			candidateValueSets[i] = paraRoughDecisionSystem.instance(i).value(paraScaledAttribute);
 		}//end for i
 		// Step 1.2 对当前属性的属性值进行排序
 		sortedValueSets = SimpleTool.sortDoubleArray(candidateValueSets);
 	   // Step 1.3 计算得到排序后的新的子表
 		double[][] sortedDS = new double[tempInstances][2];
 		for(int i = 0; i < tempInstances; i++){
 			sortedDS[i][0] = sortedValueSets[0][i];
 			int j = (int) sortedValueSets[1][i];
 			
 			// sortedDS[i][0] = sortedValueSets[i][0];
 			// int j = (int) sortedValueSets[i][1];
 			
 			sortedDS[i][1] = paraRoughDecisionSystem.instance(j).value(paraRoughDecisionSystem.numAttributes()-1);
 		}//end for i
 		/*
 		System.out.println("排序后的子表：");
 		SimpleTool.printDoubleMatrix(sortedDS);
 		*/	
 		
 		
 	   // Step 1.4 对当前属性的属性值进行去重，并计算新属性值列表
 		double[] sortedValueSetCompress  = SimpleTool.compressDoubleArray(sortedValueSets[0]);
 		/*
 		System.out.println("去重后的属性值列表：");
 		SimpleTool.printDoubleArray(sortedValueSetCompress);
 		*/
 		// Step 1.3 计算当前属性新的属性值 		
 		if(sortedValueSetCompress.length == 1){
 			valueRangeOfAttributes[paraScaledAttribute] = new double[1];
 			valueRangeOfAttributes[paraScaledAttribute][0] = sortedValueSetCompress[0];
 		}//end if
 		else{
 			valueRangeOfAttributes[paraScaledAttribute] = new double[sortedValueSetCompress.length - 1];
	 		int indexOfAttrubiteValue = 0;
	 		int k=0;
	 		for(int j = 1; j < sortedValueSetCompress.length;j++){
	 				k = j-1;
	 				valueRangeOfAttributes[paraScaledAttribute][indexOfAttrubiteValue] = 
	 					(sortedValueSetCompress[k] + sortedValueSetCompress[j]) / 2;
	 				indexOfAttrubiteValue++;
	 		}//Of for j	
 		}//end else
 		
 		
 				
 		numValuesOfAttribute[paraScaledAttribute] = valueRangeOfAttributes[paraScaledAttribute].length;
 		totalNumValues += numValuesOfAttribute[paraScaledAttribute];
 				
 		
 		return sortedDS;
	}//endOfsortDS
	
	
	/**
	 ************************* 
	 * 根据当前属性，对子表进行排序
	 * @param paraRoughDecisionSystem 
	 * 			原决策表S
	 * @param paraScaledAttribute
	 * 			所选的属性
	 * @return 排序后的信息子表S‘
	 ************************* 
	 */
	public void computeNewAttribtueWithSort(RoughDecisionSystem paraRoughDecisionSystem, int paraScaledAttribute, int paraCutsNum){
		
		int tempInstances = paraRoughDecisionSystem.numInstances();
		
		double[] sortedValueSetCompress = new double[paraCutsNum+1];
		for(int i = 0; i <= paraCutsNum; i++ ){
			sortedValueSetCompress[i] = i;
		}//end for i
	
 		
 		// System.out.println("去重后的属性值列表：");
 		// SimpleTool.printDoubleArray(sortedValueSetCompress);
 			
 		// Step 1.3 计算当前属性新的属性值
 		valueRangeOfAttributes[paraScaledAttribute] = new double[sortedValueSetCompress.length - 1];
 		int indexOfAttrubiteValue = 0;
 		int k=0;
 		for(int j = 1; j < sortedValueSetCompress.length;j++){
 				k = j-1;
 				valueRangeOfAttributes[paraScaledAttribute][indexOfAttrubiteValue] = 
 					(sortedValueSetCompress[k] + sortedValueSetCompress[j]) / 2;
 				indexOfAttrubiteValue++;
 		}//Of for j	
 				
 		numValuesOfAttribute[paraScaledAttribute] = valueRangeOfAttributes[paraScaledAttribute].length;
 		totalNumValues += numValuesOfAttribute[paraScaledAttribute];
 			
	}//endOfsortDS
	
	
	/**
	 ************************* 
	 * compute new attribute. 
	 * 根据原属性值列表，计算得到新的属性值，并以此生成新的信息表.
	 * @return 新的信息表
	 ************************* 
	 */
	public void computeNewAttribute(RoughDecisionSystem paraRoughDecisionSystem ) throws Exception {
		
		int tempInstances = paraRoughDecisionSystem.numInstances();
		
		numValuesOfAttribute = new int[paraRoughDecisionSystem.numAttributes() - 1];
		totalNumValues = 0;
		valueRangeOfAttributes=new double[paraRoughDecisionSystem.numAttributes()-1][];
		
		double[] candidateValueSets = new double[tempInstances];
		double[] sortedValueSets = new double[tempInstances];
		// System.out.println("属性的原始属性值列表");
		// Step 1. 扫描原决策表，获取每一个属性的属性值列表，并计算得到断点集合，即新的属性集合
 		for(int  i = 0; i < paraRoughDecisionSystem.numAttributes() - 1; i++){
 			// Step 1.1 依次扫描每一个对象，获取当前属性的所有属性值
			for (int j = 0; j < tempInstances; j++) {
				candidateValueSets[j] = paraRoughDecisionSystem.instance(j).value(i);
			}//Of for j
			
			// Step 1.2 对当前属性的属性值进行排序，去重
			sortedValueSets = SimpleTool.compressAndSortDoubleArray
					(candidateValueSets, candidateValueSets.length);
			
			// SimpleTool.printDoubleArray(sortedValueSets);
			
			// Step 1.3 计算当前属性新的属性值
			valueRangeOfAttributes[i] = new double[sortedValueSets.length - 1];
			int indexOfAttrubiteValue = 0;
			int k=0;
			for(int j = 1; j < sortedValueSets.length;j++){
				k = j-1;
				valueRangeOfAttributes[i][indexOfAttrubiteValue] = 
					(sortedValueSets[k] + sortedValueSets[j]) / 2;
				indexOfAttrubiteValue++;
			}//Of for j	
			
			numValuesOfAttribute[i] = valueRangeOfAttributes[i].length;
			totalNumValues += numValuesOfAttribute[i];	
 		}//Of for i
 		// System.out.println("totalNumValues= " + totalNumValues);
 		// System.out.println("新属性值列表");
 		// SimpleTool.printDoubleMatrix(valueRangeOfAttributes);
		// tempCurrentScalingData二维数组用来存储扩展后的决策表信息
	
	}// Of computeNewAttribute

	
	/**
	 ************************* 
	 * compute new attribute. 
	 * 根据原属性值列表，计算得到新的属性值，并以此生成新的信息表.
	 * @return 新的信息表
	 ************************* 
	 */
	public void computeNewAttributeTopK(RoughDecisionSystem paraRoughDecisionSystem, 
			boolean[] paraStausOfAttribute ) throws Exception {
		
		int tempInstances = paraRoughDecisionSystem.numInstances();
		
		numValuesOfAttribute = new int[paraRoughDecisionSystem.numAttributes() - 1];
		totalNumValues = 0;
		valueRangeOfAttributes=new double[paraRoughDecisionSystem.numAttributes()-1][];
		
		double[] candidateValueSets = new double[tempInstances];
		double[] sortedValueSets = new double[tempInstances];
		// System.out.println("属性的原始属性值列表");
		// Step 1. 扫描原决策表，获取每一个属性的属性值列表，并计算得到断点集合，即新的属性集合
 		for(int  i = 0; i < paraRoughDecisionSystem.numAttributes() - 1; i++){
 			
 			if(paraStausOfAttribute[i] == false){
 				
 				valueRangeOfAttributes[i] = new double[1];
 				
 				valueRangeOfAttributes[i][0] = 0;
 			
 				
 				numValuesOfAttribute[i] = valueRangeOfAttributes[i].length;
 				totalNumValues += numValuesOfAttribute[i];	
 				continue;
 			}//end of if 
 				
 			// Step 1.1 依次扫描每一个对象，获取当前属性的所有属性值
			for (int j = 0; j < tempInstances; j++) {
				candidateValueSets[j] = paraRoughDecisionSystem.instance(j).value(i);
			}//Of for j
			
			// Step 1.2 对当前属性的属性值进行排序，去重
			sortedValueSets = SimpleTool.compressAndSortDoubleArray
					(candidateValueSets, candidateValueSets.length);
			
			// SimpleTool.printDoubleArray(sortedValueSets);
			
			// Step 1.3 计算当前属性新的属性值
			valueRangeOfAttributes[i] = new double[sortedValueSets.length - 1];
			int indexOfAttrubiteValue = 0;
			int k=0;
			for(int j = 1; j < sortedValueSets.length;j++){
				k = j-1;
				valueRangeOfAttributes[i][indexOfAttrubiteValue] = 
					(sortedValueSets[k] + sortedValueSets[j]) / 2;
				indexOfAttrubiteValue++;
			}//Of for j	
			
			numValuesOfAttribute[i] = valueRangeOfAttributes[i].length;
			totalNumValues += numValuesOfAttribute[i];	
 		}//Of for i
 		// System.out.println("totalNumValues= " + totalNumValues);
 		// System.out.println("新属性值列表");
 		// SimpleTool.printDoubleMatrix(valueRangeOfAttributes);
		// tempCurrentScalingData二维数组用来存储扩展后的决策表信息
	
	}// Of computeNewAttribute
	
	
	/**
	 ************************* 
	 * Scaling attribute. That is transform a many-valued attribute into a
	 * number of binary-valued attributes.
	 ************************* 
	 */
	public boolean[][] scalingAttributeToBinaryValueAttribute() throws Exception {
		int tempInstances = numInstances();
		numValuesOfAttribute = new int[numAttributes() - 1];
		totalNumValues = 0;
		// 统计每个属性的属性值个数，以及总的属性值个数和。
		for (int i = 0; i < numAttributes() - 1; i++) {
			numValuesOfAttribute[i] = attribute(i).numValues();
			totalNumValues += numValuesOfAttribute[i];
		}// Of for i
			// tempCurrentScalingData二维数组用来存储扩展后的决策表信息
		boolean[][] tempCurrentScalingData = new boolean[tempInstances][totalNumValues + 1];
		int tempIndexOfNewAttribute = 0;

		for (int i = 0; i < tempInstances; i++) {
			for (int j = 0; j < numAttributes() - 1; j++) {
				for (int k = 0; k < attribute(j).numValues(); k++) {
					if (j == 0)
						tempIndexOfNewAttribute = k;
					else {
						int tempCount = 0;
						int tempI = j;
						while (tempI > 0) {
							tempCount += attribute(tempI - 1).numValues();
							tempI--;
						}// Of while
						tempIndexOfNewAttribute = k + tempCount;
					}// Of else
					if ((int) (instance(i).value(j)) == k)
						tempCurrentScalingData[i][tempIndexOfNewAttribute] = true;
					else
						tempCurrentScalingData[i][tempIndexOfNewAttribute] = false;
				}// Of for k
			}// Of for j
			//tempCurrentScalingData[i][totalNumValues] = (int) (instance(i)
					//.value(numAttributes() - 1));
		}// Of for i
		return tempCurrentScalingData;
	}// Of scalingAttributeToBinaryValueAttribute
	
	
	/**
	 ********************************** 
	 * For toString(). See interface toString().
	 * 
	 * @return object status.
	 ********************************** 
	 */
	public String toString() {
		String reportString = "This is an AVT nominal decision system.\r\n";
		reportString += "Information is given by super classes.\r\n"
				+ super.toString();
		return reportString;
	}// Of toString

	/**
	 ********************************** 
	 * 计算每个原始属性在扩展决策表中的开始属性索引及结束属性索引。
	 * 
	 ********************************** 
	 */
	public void computeBeginIndexAndEndIndexOfAttribute() {
		
		for (int i = 0; i < numValuesOfAttribute.length; i++) {
			beginAndEndIndexOfAttribute[i][0] = 0;
			beginAndEndIndexOfAttribute[i][1] = 0;
			if (i == 0) {
				// tempMergeIndexCount = 0;
				beginAndEndIndexOfAttribute[i][0] = 0;
				beginAndEndIndexOfAttribute[i][1] = numValuesOfAttribute[0];
			}// Of if
			else {
				for (int j = 0; j < i; j++) {
					// System.out.println("numValuesOfAttribute["+ j + "]="
					// +numValuesOfAttribute[j] );
					beginAndEndIndexOfAttribute[i][0] += numValuesOfAttribute[j];
				}
				beginAndEndIndexOfAttribute[i][1] = beginAndEndIndexOfAttribute[i][0]
						+ numValuesOfAttribute[i];
			}// Of else
		}// Of for i
	}// Of computeBeginIndexAndEndIndexOfAttribute
	/**
	 ********************************** 
	 * 计算单个原始属性在扩展决策表中的开始属性索引及结束属性索引。
	 * 
	 ********************************** 
	 */
	public void computeBeginIndexAndEndIndexOfAttribute(int paraAttribute) {
		
		//for (int i = 0; i < numValuesOfAttribute.length; i++) {
		//System.out.println("test2");
			beginAndEndIndexOfAttribute[paraAttribute][0] = 0;
			beginAndEndIndexOfAttribute[paraAttribute][1] = 0;
			
			if (paraAttribute == 0) {
				// tempMergeIndexCount = 0;
				beginAndEndIndexOfAttribute[paraAttribute][0] = 0;
				beginAndEndIndexOfAttribute[paraAttribute][1] = numValuesOfAttribute[0];
				
			}// Of if
			else {
				for (int i = 0; i < paraAttribute; i++) {
					// System.out.println("numValuesOfAttribute["+ j + "]="
					// +numValuesOfAttribute[j] );
					beginAndEndIndexOfAttribute[paraAttribute][0] += numValuesOfAttribute[i];
				}
				beginAndEndIndexOfAttribute[paraAttribute][1] = beginAndEndIndexOfAttribute[paraAttribute][0]
						+ numValuesOfAttribute[paraAttribute];
			}// Of else
		//}// Of for i
	}// Of computeBeginIndexAndEndIndexOfAttribute
	/**
	 ********************************** 
	 * 根据约简集合，计算新的属性值个数。
	 * 
	 ********************************** 
	 */
	public void computeNumOfAttributeValue(boolean[] paraOptimalReduct){
		beginAndEndIndexOfAttribute = new int[numValuesOfAttribute.length][2];

		// Step 1. 计算每一个原始属性在扩展属性决策表中的起始索引下标
		computeBeginIndexAndEndIndexOfAttribute();
	
		int[] tempFalseNumberOfAttribute = new int[numValuesOfAttribute.length];
		
		//Step 2. 根据约简集合P，更新每一个原始属性的属性值个数以及总的属性值个数
		for (int i = 0; i < numValuesOfAttribute.length; i++) {
			tempFalseNumberOfAttribute[i] = 0;
			for (int j = beginAndEndIndexOfAttribute[i][0]; j < beginAndEndIndexOfAttribute[i][1]; j++) {
				if (paraOptimalReduct[j] == false) {
					tempFalseNumberOfAttribute[i]++;
				}// Of if
			}// Of for j
			numValuesOfAttribute[i] -= tempFalseNumberOfAttribute[i];
			totalNumValues -= tempFalseNumberOfAttribute[i];
		}// Of for i
		
		for (int i = 0; i < numValuesOfAttribute.length; i++) {
			if(numValuesOfAttribute[i] == 0)
				totalNumValues ++;
		}// Of for i
	}//Of computeNumOfAttributeValue
	
	/**
	 ********************************** 
	 * 根据约简集合，计算新的属性值个数，单个属性。
	 * 
	 ********************************** 
	 */
	public void computeNumOfAttributeValue(boolean[] paraOptimalReduct, int paraAttribute){
		
		//System.out.println("test1");
		// Step 1. 计算每一个原始属性在扩展属性决策表中的起始索引下标
		int tempNumOfAttributeValue = 0;
		computeBeginIndexAndEndIndexOfAttribute(paraAttribute);
	
		int  tempFalseNumberOfAttribute = 0;

		//Step 2. 根据约简集合P，更新每一个原始属性的属性值个数以及总的属性值个数
			for (int i = 0; i < paraOptimalReduct.length; i++) {
				if (paraOptimalReduct[i] == false) {
					tempFalseNumberOfAttribute++;
				}// Of if
			}// Of for j
			tempNumOfAttributeValue = numValuesOfAttribute[paraAttribute] - tempFalseNumberOfAttribute;
			numValuesOfAttribute[paraAttribute] = tempNumOfAttributeValue;
			totalNumValues -= tempNumOfAttributeValue;
			if(numValuesOfAttribute[paraAttribute] == 0)
				totalNumValues ++;
	}//Of computeNumOfAttributeValue
	
	/**
	 ********************************** 
	 * 根据约简集合，重新计算得到新的决策表信息。
	 * 
	 * @return 更新后的决策表信息.
	 ********************************** 
	 */
	public int[][] computeNewDataByOptimalReduct(int[][] pareMatrixData, boolean[] paraOptimalReduct) {
		
		
		// 存储更新后的信息
		int[][] tempNewDataAfterReduct;

		tempNewDataAfterReduct = new int[pareMatrixData.length][totalNumValues + 1];
		int tempIndexOfNewData = 0;
		
		// Step 4. 更新新的决策表S*，直接删除约简集合P中为false的属性及相关列
		for (int i = 0; i < numValuesOfAttribute.length; i++) {
			
			if(numValuesOfAttribute[i] == 0){
				for (int k = 0; k < pareMatrixData.length; k++) {
				tempNewDataAfterReduct[k][tempIndexOfNewData] = 0;
			}// Of for k
			tempIndexOfNewData++;
			continue;
			}//Of if
			
			for (int j = beginAndEndIndexOfAttribute[i][0]; j < beginAndEndIndexOfAttribute[i][1]; j++) {
				if (paraOptimalReduct[j] == true) {
					for (int k = 0; k < pareMatrixData.length; k++) {
						tempNewDataAfterReduct[k][tempIndexOfNewData] = pareMatrixData[k][j];
					}// Of for k
					tempIndexOfNewData++;
				}// Of if
				else {
					continue;
				}// Of else
			}// Of for j
		}// Of for i
		
		for (int i = 0; i < pareMatrixData.length; i++) {
			tempNewDataAfterReduct[i][tempIndexOfNewData] = 
					pareMatrixData[i][pareMatrixData[i].length - 1];
		}// Of for i

		// SimpleTool.printIntMatrix(tempNewDataAfterReduct);
		// Step 5. 返回更新后的决策表
		return tempNewDataAfterReduct;
	}// Of computeNewDataByOptimalReduct

	/**
	 ********************************** 
	 * 根据约简集合，重新计算得到新的决策表信息。
	 * 
	 * @return 更新后的决策表信息.
	 ********************************** 
	 */
	public int[][] computeNewDataByReduct(int[][] pareMatrixData, boolean[] paraOptimalReduct) {
		
		
		// 存储更新后的信息
		int[][] tempNewDataAfterReduct = new int[pareMatrixData.length][paraOptimalReduct.length+1];
		tempNewDataAfterReduct = SimpleTool.copyIntMatrix(pareMatrixData);
		
		// Step 4. 更新新的决策表S*，直接删除约简集合P中为false的属性及相关列
		for (int i = 0; i < paraOptimalReduct.length; i++) {
			
			if(paraOptimalReduct[i] == true){
				continue;
			}//Of if
			
			for (int j = 0; j < pareMatrixData.length; j++) {
				tempNewDataAfterReduct[j][i] = 0;
			}// Of for j
		}// Of for i
		// SimpleTool.printIntMatrix(tempNewDataAfterReduct);
		// Step 5. 返回更新后的决策表
		return tempNewDataAfterReduct;
	}// Of computeNewDataByOptimalReduct
	
	/**
	 ********************************** 
	 * 根据约简集合，重新计算得到新的决策表信息（单个属性）。
	 * 
	 * @return 更新后的决策表信息.
	 ********************************** 
	 */
	public int[][] computeNewDataByOptimalReduct(int[][] pareMatrixData, boolean[] paraOptimalReduct, 
			int paraAttribute,int paraAttributeValueNum) {
		
		
		// 存储更新后的信息
		int[][] tempNewDataAfterReduct;

		tempNewDataAfterReduct = new int[pareMatrixData.length][paraAttributeValueNum + 1];
		int tempIndexOfNewData = 0;
		
		// Step 4. 更新新的决策表S*，直接删除约简集合P中为false的属性及相关列
		//for (int i = 0; i < numValuesOfAttribute.length; i++) {
			
			if(paraAttributeValueNum == 0){
				for (int k = 0; k < pareMatrixData.length; k++) {
					tempNewDataAfterReduct[k][tempIndexOfNewData] = 0;
				}// Of for k
				tempIndexOfNewData++;
			}//Of if
			
			for (int j = 0; j < pareMatrixData[paraAttribute].length-1; j++) {
				if (paraOptimalReduct[j] == true) {
					for (int k = 0; k < pareMatrixData.length; k++) {
						tempNewDataAfterReduct[k][tempIndexOfNewData] = pareMatrixData[k][j];
					}// Of for k
					tempIndexOfNewData++;
				}// Of if
				else {
					continue;
				}// Of else
			}// Of for j
		//}// Of for i
		
		for (int i = 0; i < pareMatrixData.length; i++) {
			tempNewDataAfterReduct[i][tempIndexOfNewData] = 
					pareMatrixData[i][pareMatrixData[i].length - 1];
		}// Of for i

		// SimpleTool.printIntMatrix(tempNewDataAfterReduct);
		// Step 5. 返回更新后的决策表
		return tempNewDataAfterReduct;
	}// Of computeNewDataByOptimalReduct
	
	/**
	 ********************************** 
	 * 根据约简集合，更新M相关数组信息。
	 * 
	 * @return 更新后的M相关约束数组.
	 ********************************** 
	 */
	public boolean[] updataDataInfor(NewLocalDisPlusGlobalDisFastVersion2 paraDecisionSystem,
			boolean[] paraMrelativeReduct) {

		// System.out.println("numAttributes() = " + numAttributes());
		boolean[] tempMrelativeReduct = new boolean[paraDecisionSystem
				.numAttributes() - 1];
		System.out.println("paraDecisionSystem.numAttributes() = "
				+ paraDecisionSystem.numAttributes());
		int indexOfNewM = 0;
		int countOfFalse = 0;

		int tempMergeIndexCount = 0;
		for (int i = 0; i < numValuesOfAttribute.length; i++) {
			int beginIndex = 0;
			int endIndex = 0;
			// System.out.println("对第" + i + "个属性进行更新！！");
			if (i == 0) {
				tempMergeIndexCount = 0;
				beginIndex = 0;
				endIndex = numValuesOfAttribute[0];
			}// Of if
			else {
				tempMergeIndexCount = 0;
				// int beginIndex = 0;
				for (int j = 0; j < i; j++) {
					System.out.println("numValuesOfAttribute[" + j + "]="
							+ numValuesOfAttribute[j]);
					beginIndex += numValuesOfAttribute[j];
				}
				endIndex = beginIndex + numValuesOfAttribute[i];
			}// Of else
			System.out.println("beginIndex = " + beginIndex);
			System.out.println("endIndex = " + endIndex);
			for (int j = beginIndex; j < endIndex; j++) {
				if (paraMrelativeReduct[j] == true) {
					// System.out.println("当前属性值不进行合并");
					continue;
				}// Of if
				if (tempMergeIndexCount > 1)
					continue;
				candidateMergeIndex[i][tempMergeIndexCount] = j;
				tempMergeIndexCount++;
			}// Of for j
			for (int j = 0; j < 2; j++) {
				System.out.println("candidateMergeIndex[" + i + "][" + j + "]="
						+ candidateMergeIndex[i][j]);
			}// Of for j

			for (int j = beginIndex; j < endIndex; j++) {
				// System.out.println("paraMrelativeReduct[" + i + "] = " +
				// paraMrelativeReduct[i] );
				if (paraMrelativeReduct[j] == true) {
					tempMrelativeReduct[indexOfNewM] = true;
					indexOfNewM++;
				}// Of if
				else {
					// countOfFalse++;
					if (j == candidateMergeIndex[i][0]) {
						tempMrelativeReduct[indexOfNewM] = false;
						indexOfNewM++;
					}// Of if
					else if (j == candidateMergeIndex[i][1]) {
						// continue;
						tempMrelativeReduct[indexOfNewM] = false;
						indexOfNewM++;
					}// Of else if
					else {
						tempMrelativeReduct[indexOfNewM] = true;
						indexOfNewM++;
					}// Of else
				}// Of else
			}// Of for j

		}// Of for i

		/*
		 * for(int i = 0; i <paraMrelativeReduct.length; i++){ //
		 * System.out.println("paraMrelativeReduct[" + i + "] = " +
		 * paraMrelativeReduct[i] ); if(paraMrelativeReduct[i]==true){
		 * tempMrelativeReduct[indexOfNewM] = true; indexOfNewM++; }//Of if
		 * else{ countOfFalse++; if(countOfFalse == 1){
		 * tempMrelativeReduct[indexOfNewM] = false; indexOfNewM++; }//Of if
		 * else if(countOfFalse == 2){ continue; } else{
		 * tempMrelativeReduct[indexOfNewM] = false; indexOfNewM++; }//Of else
		 * }//Of else }//Of for i
		 */
		System.out.println("更新M约束数组后：");
		for (int i = 0; i < tempMrelativeReduct.length; i++) {
			System.out.println("tempMrelativeReduct[" + i + "] = "
					+ tempMrelativeReduct[i]);
		}

		/*
		 * //统计每个属性的属性值个数，以及总的属性值个数和。 for(int i = 0; i<
		 * numValuesOfAttribute.length; i++){ if(candidateMergeIndex[i][0] <
		 * Integer.MAX_VALUE & candidateMergeIndex[i][1] < Integer.MAX_VALUE)
		 * numValuesOfAttribute[i]--; totalNumValues--; }
		 * 
		 * System.out.println("更新每个属性的属性值个数后："); for(int i = 0; i<
		 * numValuesOfAttribute.length; i++){
		 * System.out.println("numValuesOfAttribute[" + i + "] = " +
		 * numValuesOfAttribute[i]); }
		 */

		return tempMrelativeReduct;
	}// Of updataDataInfor

	/**
	 ********************************** 
	 * 布尔数组转换成long型数据。
	 * 
	 * @return long型数据.
	 ********************************** 
	 */
	public long booleanToLong(boolean[] paraBooleanArray) {
		long tempLongData = 0;
		for (int i = 0; i < paraBooleanArray.length; i++) {
			if (paraBooleanArray[i] == false)
				tempLongData += Math.pow(2, i);
		}// Of for i
		return tempLongData;
	}// Of booleanToLong

	/**
	 ********************************** 
	 * 根据最优约简方案，更新原始决策表信息。
	 * 
	 * @param paraMatrix
	 *            原决策表信息
	 * @param paraScaledDecisionSystem
	 *            更新后的扩展决策表S*
	 * @return 根据约简方案更新后的决策表信息.
	 ********************************** 
	 */
	public int[][] computeNewDecisionTable(int[][] paraMatrix,
											int[][] paraScaledDecisionSystem) {
		int tempInstances = paraMatrix.length;
		int[][] tempCurrentNewData = new int[tempInstances][numAttributes()];
		// System.out.println("numAttributes()="+numAttributes());
		// System.out.println("beginAndEndIndexOfAttribute.length="+beginAndEndIndexOfAttribute.length);
	
		// Step 1. 重新计算扩展属性的下标范围
		computeBeginIndexAndEndIndexOfAttribute();
		
		// Step 2. 根据更新后的扩展决策表，计算得到新的决策表信息
		for (int i = 0; i < tempInstances; i++) {
			// System.out.println("----------------");
			// System.out.println("i= " + i);
			for (int j = 0; j < numAttributes() - 1; j++) {
				// System.out.println("----");
				// System.out.println("j= " + j);
				if(valueRangeOfAttributes[j].length == 1 && valueRangeOfAttributes[j][0] == Double.MAX_VALUE){
					tempCurrentNewData[i][j] = 0;
					continue;
				}//Of if
				
				int countOneNumber = 0;
				// Step 2.1 对于更新后的扩展决策表，统计每一行中每一个扩展属性的属性值为1的属性个数
				for (int k = beginAndEndIndexOfAttribute[j][0]; 
						k < beginAndEndIndexOfAttribute[j][1]; k++) {
					// System.out.println("k= " + k);
					if(paraScaledDecisionSystem[i][k] == 0){
						continue;
					}//Of if
					countOneNumber++;
				}// Of for k
				// Step 2.2 属性值为1的属性个数，即为新决策表原始属性的属性值
				tempCurrentNewData[i][j] = countOneNumber;
			}// Of for j
			
			// Step 2.3 决策属性的属性值
			tempCurrentNewData[i][numAttributes()-1] = 
					(int) paraMatrix[i][numAttributes()-1];
		}// Of for i

		//Step 3. 返回更新后新的决策表
		return tempCurrentNewData;
	}// OfcomputeNewDecisionTable

	/**
	 ********************************** 
	 * 根据最优约简方案，更新原始决策表信息（单个属性）,返回整个决策表。
	 * 
	 * @param paraMatrix
	 *            原决策表信息
	 * @param paraScaledDecisionSystem
	 *            更新后的扩展决策表S*
	 * @param paraAttribute
	 * 			当前属性
	 * @param paraValueRangeOfAttribute
	 * 			当前属性的属性值列表
	 * @param paraNumOfDis
	 * 			当前已经离散化的属性的个数
	 * @return 根据约简方案更新后的决策表信息.
	 ********************************** 
	 */
	public int[][] computeNewDecisionTable(int[][] paraMatrix,
											int[][] paraScaledDecisionSystem, 
											int paraAttribute,
											double[] paraValueRangeOfAttribute) {
		int tempInstances = paraMatrix.length;
		
		System.out.println("tempInstances = " + tempInstances);
		int[][] tempCurrentNewData = paraMatrix;
		System.out.println("更新原决策表前的决策表信息：");
		SimpleTool.printIntMatrix(tempCurrentNewData);
		
		// Step 2. 根据更新后的扩展决策表，计算得到新的决策表信息
		for (int i = 0; i < tempInstances; i++) {
			for (int j = 0; j < numAttributes() - 1; j++) {
				if(j != paraAttribute){
					continue;
				}//end if
				if(paraValueRangeOfAttribute.length == 1 && paraValueRangeOfAttribute[0] == Double.MAX_VALUE){
					tempCurrentNewData[i][j] = 0;
					continue;
				}//Of if
				int countOneNumber = 0;
				// Step 2.1 对于更新后的扩展决策表，统计每一行中每一个扩展属性的属性值为1的属性个数
				for (int k = 0; 
						k < paraValueRangeOfAttribute.length; k++) {
					if(paraScaledDecisionSystem[i][k] == 0){
						continue;
					}//Of if
					countOneNumber++;
				}// Of for k
				// Step 2.2 属性值为1的属性个数，即为新决策表原始属性的属性值
				tempCurrentNewData[i][j] = countOneNumber;
			}// Of for j
			
		}// Of for i
		//Step 3. 返回更新后新的决策表
		return tempCurrentNewData;
	}// OfcomputeNewDecisionTable
	
	/**
	 ********************************** 
	 * 根据最优约简方案，更新原始决策表信息（单个属性）,返回当前属性对应的决策表。
	 * 
	 * @param paraMatrix
	 *            原决策表信息
	 * @param paraScaledDecisionSystem
	 *            更新后的扩展决策表S*
	 * @param paraAttribute
	 *            当前属性
	 * @param paraValueRangeOfAttribute
	 *            当前属性的属性值列表
	 * @return 根据约简方案更新后的决策表信息.
	 ********************************** 
	 */
	public int[][] computeNewDecisionTableOneAttribute(double[][] paraMatrix,
			int[][] paraScaledDecisionSystem, int paraAttribute,
			double[] paraValueRangeOfAttribute) {
		int tempInstances = paraMatrix.length;
		int[][] tempCurrentNewData = new int[tempInstances][2];
		// Step 2. 根据更新后的扩展决策表，计算得到新的决策表信息
		for (int i = 0; i < tempInstances; i++) {
			if (paraValueRangeOfAttribute.length == 1
					&& paraValueRangeOfAttribute[0] == Double.MAX_VALUE) {
				tempCurrentNewData[i][0] = 0;
				continue;
			}// Of if

			int countOneNumber = 0;
			// Step 2.1 对于更新后的扩展决策表，统计每一行中每一个扩展属性的属性值为1的属性个数
			for (int k = 0; k < paraValueRangeOfAttribute.length; k++) {
				// System.out.println("k= " + k);
				if (paraScaledDecisionSystem[i][k] == 0) {
					continue;
				}// Of if
				countOneNumber++;
			}// Of for k
				// Step 2.2 属性值为1的属性个数，即为新决策表原始属性的属性值
			tempCurrentNewData[i][0] = countOneNumber;
			// }// Of for j

			// Step 2.3 决策属性的属性值
			tempCurrentNewData[i][1] = (int) paraMatrix[i][numAttributes() - 1];
		}// Of for i
			// Step 3. 返回更新后新的决策表
		return tempCurrentNewData;
	}// OfcomputeNewDecisionTable
	
	/**
	 ********************************** 
	 * 根据二维矩阵存储成决策表。
	 * 
	 * @param paraMatrix
	 *            更新后的决策表信息
	 * @return 新的决策系统.
	 ********************************** 
	 */
	private NewLocalDisPlusGlobalDisFastVersion2 restore(int[][] paraMatrix) throws Exception {
		NewLocalDisPlusGlobalDisFastVersion2 tempSytem = new NewLocalDisPlusGlobalDisFastVersion2(this);
		// Step 1. Error check.
		if (tempSytem.numInstances() != paraMatrix.length) {
			throw new Exception(
					"Error occurred in AVTNominalDecisionSystem.restore()\r\n"
							+ "The number of instances is "
							+ tempSytem.numInstances()
							+ ", while the matrix rows are "
							+ paraMatrix.length);

		}// Of if
		if (tempSytem.numAttributes() != paraMatrix[0].length) {
			throw new Exception(
					"Error occurred in AVTNominalDecisionSystem.restore()\r\n"
							+ "The number of attributes is "
							+ tempSytem.numAttributes()
							+ ", while the matrix columns are "
							+ paraMatrix[0].length);

		}// Of if

		// Step 2. Restore.
		for (int i = 0; i < paraMatrix.length; i++) {
			for (int j = 0; j < paraMatrix[0].length; j++) {
				tempSytem.instance(i).setValue(j, paraMatrix[i][j]);
			}// Of for j
		}// Of for i
		return tempSytem;
	}// Of restore

	/**
	 ********************************** 
	 * 根据二维矩阵存储成决策表。
	 * 
	 * @param paraCurrentDS
	 *            最原始的决策表
	 * @param paraMatrix
	 *            更新后的决策表信息
	 * @return 新的决策系统.
	 ********************************** 
	 */
	public NewLocalDisPlusGlobalDisFastVersion2 restoreToArff(NewLocalDisPlusGlobalDisFastVersion2 paraCurrentDS,
			int[][] paraMatrixOfData, String paraTypeOfData,int paraK) throws Exception {
	
		StringBuffer sb = new StringBuffer();
		String filename = paraCurrentDS.arffFilename.substring(0,
				paraCurrentDS.arffFilename.length() - 5)
				+ "_" + paraK + "_"+ paraTypeOfData+ "finalDS.arff";
	
		sb.append(ARFF_RELATION + " " + Utils.quote(relationName())
				+ "_"+ paraTypeOfData +"\r\n");
		// Conditional attributes
		// sb.append(ARFF_RELATION + " " + Utils.quote(relationName()) + "\r\n");
		// Conditional attributes
		for (int i = 0; i < numberOfConditions; i++) {
			sb.append("@ATTRIBUTE " + attribute(i).name());
			sb.append(" real\r\n");
		}// Of for i

		// The class attribute and all its values

		sb.append("@ATTRIBUTE class{");
		for (int i = 0; i < paraCurrentDS.numClasses(); i++) {
			sb.append(i);
			if (i < paraCurrentDS.numClasses() - 1) {
				sb.append(", ");
			} else {
				sb.append("}\r\n");
			}
		}// Of for i
		
		
		// @DATA
		sb.append("\r\n" + ARFF_DATA + "\r\n");
		
		for(int i = 0; i < paraMatrixOfData.length; i++) {
			sb.append(paraMatrixOfData[i][0]);
			for(int j = 1; j < paraMatrixOfData[i].length; j++) {
				sb.append(", ").append(paraMatrixOfData[i][j]);
			}// Of for j
			sb.append("\r\n");
		}// Of for i
	
		try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
			dos.writeBytes(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}// Of try 
		
		NewLocalDisPlusGlobalDisFastVersion2 currentDs = null;
		
		// Read the arff file and return.
		try {
			FileReader fileReader = new FileReader(filename);
			currentDs = new NewLocalDisPlusGlobalDisFastVersion2(fileReader);
			currentDs.setClassIndex(paraMatrixOfData[0].length - 1);
			currentDs.setArffFilename(filename);
			fileReader.close();
			// System.out.println("The new data is " + currentDs);
		} catch (IOException e) {
				e.printStackTrace();
		}// Of try
		return currentDs;
		 
	}// Of normalize

	/**
	 ********************************** 
	 * 计算每个原始属性未进行约简的个数，并决定是否继续继续约简。
	 * 
	 * @param paraUnReductAttribute
	 *            扩展属性的约简状态
	 * @return 是否进行下一轮约简.
	 ********************************** 
	 */
	public boolean computewhetherConinueReduct(boolean[] paraUnReductAttribute) {

		for (int i = 0; i < numValuesOfAttribute.length; i++) {
			int counteOfUnReductAttribute = 0;
			int beginIndex = 0;
			int endIndex = 0;
			// System.out.println("对第" + i + "个属性进行更新！！");
			if (i == 0) {
				// tempMergeIndexCount = 0;
				beginIndex = 0;
				endIndex = numValuesOfAttribute[0];
			}// Of if
			else {
				// tempMergeIndexCount = 0;
				// int beginIndex = 0;
				for (int j = 0; j < i; j++) {
					System.out.println("numValuesOfAttribute[" + j + "]="
							+ numValuesOfAttribute[j]);
					beginIndex += numValuesOfAttribute[j];
				}
				endIndex = beginIndex + numValuesOfAttribute[i];
			}// Of else
			System.out.println("beginIndex = " + beginIndex);
			System.out.println("endIndex = " + endIndex);

			for (int j = beginIndex; j < endIndex; j++) {
				if (paraUnReductAttribute[j] == false) {
					// System.out.println("当前属性值不进行合并");
					counteOfUnReductAttribute++;
				}// Of if
			}// Of for j

			if (counteOfUnReductAttribute >= 2) {
				return true;
			}

		}// Of for i

		return false;
	}

	/**
	 ********************************** 
	

	/**
	 ********************************** 
	 * 将精度的计算结果存储到文件中.
	 * 
	 * @param Accuracy
	 *            要存储的精度值
	 * @param paraStorePath
	 *            存储的路径
	 * @author Liu-Ying Wen 2014/12/8
	 ********************************** 
	 */
	public void toStoreAccuracyInFile(double paraAccuracy, String paraStorePath) {
		byte[] buff = new byte[] {};
		try {
			String tempWriteString = " ";

			// 将double型的精度值转换成字符串类型
			tempWriteString = Double.toString(paraAccuracy);
			tempWriteString += "\r\n";
			// 将字符串型的精度值转换成字节
			buff = tempWriteString.getBytes();
			FileOutputStream out = new FileOutputStream(paraStorePath, true);
			out.write(buff, 0, buff.length);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}// Of try
	}// Of toStoreAccuracyInFile

	

	/**
	 ********************************** 
	 * 将精度的计算结果存储到文件中.
	 * 
	 * @param Accuracy
	 *            要存储的精度值
	 * @param paraStorePath
	 *            存储的路径
	 * @author Liu-Ying Wen 2014/12/8
	 ********************************** 
	 */
	public void toStoreDeviatioLine( String paraStorePath) {
		byte[] buff = new byte[] {};
		try {
			String tempWriteString = " ";
			tempWriteString += "\r\n---------------------------\r\n";
			// 将字符串型的精度值转换成字节
			buff = tempWriteString.getBytes();
			FileOutputStream out = new FileOutputStream(paraStorePath, true);
			out.write(buff, 0, buff.length);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}// Of try
	}// Of toStoreAccuracyInFile
	
	/**
	 ************************* 
	 * Find a (possibly) minimal reduct using the information entropy based
	 * approach.
	 * 
	 * @return the reduct with the form of boolean vector.
	 ************************* 
	 */
	public boolean[] entropyBasedReductionByMrelative(boolean[] paraSelectedAttribute ) throws Exception {
		initializeForReduction();
		boolean[] tempSelectedAttribute = paraSelectedAttribute; 
		/*
		System.out.println("已选择的属性情况");
		for(int i = 0; i < tempSelectedAttribute.length; i++){
			System.out.println(tempSelectedAttribute[i]);
		}
		System.out.println("-------------");
		*/
		double currentEntropy = 100;
		totalEntropy = conditionalEntropy();

		// Step 1. Compute the core
		computeCore();
		/*
		System.out.println("核属性情况");
		for(int i = 0; i < core.length; i++){
			System.out.println(core[i]);
		}
		System.out.println("-------------");
		*/
		// Step 2. Add attributes
		// Step 2.1 Copy core attributes
		int numberOfCoreAttributes = 0;
		for (int i = 0; i < numberOfConditions; i++) {
			currentSelectedAttributes[i] = core[i];
			if (currentSelectedAttributes[i]) {
				numberOfCoreAttributes++;
			}// Of if
		}// Of for i

		for (int i = 0; i < numberOfConditions; i++) {
			if (tempSelectedAttribute[i]) {
				currentSelectedAttributes[i] = true;
				numberOfCoreAttributes++;
			}// Of if
		}// Of for i
		/*
		System.out.println("合并核属性与已选择属性后的属性情况");
		for(int i = 0; i < currentSelectedAttributes.length; i++){
			System.out.println(currentSelectedAttributes[i]);
		}
		System.out.println("-------------");
		*/
		if (numberOfCoreAttributes == 0) {
			currentEntropy = 100;
		} else {
			currentEntropy = conditionalEntropy(currentSelectedAttributes);
		}
		int currentBestAttribute = -1;
		
		// Step 2.2 Add attributes one by one
		while (Math.abs(currentEntropy - totalEntropy) > 1e-6) {
			double currentBestEntropy = 10000;
			for (int i = 0; i < numberOfConditions; i++) {
				// Ignore selected attributes
				if (currentSelectedAttributes[i])
					continue;

				// Try this attribute
				currentSelectedAttributes[i] = true;
				currentEntropy = conditionalEntropy(currentSelectedAttributes);
				if (currentBestEntropy > currentEntropy) {
					currentBestEntropy = currentEntropy;
					currentBestAttribute = i;
				}
				// Set back
				currentSelectedAttributes[i] = false;
			}// Of for i
				// Really add it
			currentSelectedAttributes[currentBestAttribute] = true;
			currentEntropy = conditionalEntropy(currentSelectedAttributes);
		}// Of while
		
		// Step 3. Remove redundant attributes
		for (int i = 0; i < numberOfConditions; i++) {
			// Ignore core attributes and not selected attributes
			if ((core[i]) || (!currentSelectedAttributes[i] || tempSelectedAttribute[i])) {
				continue;
			}// Of if

			// Try to remove this attribute
			currentSelectedAttributes[i] = false;
			currentEntropy = conditionalEntropy(currentSelectedAttributes);
			if (Math.abs(currentEntropy - totalEntropy) > 1e-6) {
				// Set back
				currentSelectedAttributes[i] = true;
			} else {
				// This rarely happens, so output this message directly.
				System.out.println("attribute #" + i + " can be removed.");
			}// Of if
		}// Of for i
		currentReduct = currentSelectedAttributes;

		// int currentReductIndex = getCurrentReductIndex();
		// if (currentReductIndex == -1) {
			// String errorMessage = "Error occurred in NominalDecisionSystem.entropyBasedReduction(): "
					// + getReductString() + " is not a reduct";
			// throw new Exception(errorMessage);
		// }// Of if
		System.out.println("当前约简的结果");
		for(int i = 0; i < currentReduct.length; i++){
			System.out.println(currentReduct[i]);
		}
		return currentReduct;
		
	}// Of entropyBasedReduction
	
	
	/**
	 ************************* 
	 * Find a (possibly) minimal reduct using the information entropy based
	 * approach.
	 * 
	 * @return the reduct with the form of boolean vector.
	 ************************* 
	 */
	public boolean[] entropyBasedReduction(int[][] paraMatrix) throws Exception {
		initializeForReduction();
		
		double currentEntropy = 100;
		// totalEntropy = conditionalEntropy();
		// System.out.println("初始时的信息熵 = " + totalEntropy);

		totalEntropy = ConditionalEntropy.conditionalEntropy(paraMatrix);
		// System.out.println("用新的方法计算的信息熵为:" + totalEntropy);
		
		// Step 1. Compute the core
		computeCore();
		
		// Step 2. Add attributes
		// Step 2.1 Copy core attributes
		int numberOfCoreAttributes = 0;
		for (int i = 0; i < numberOfConditions; i++) {
			currentSelectedAttributes[i] = core[i];
			if (currentSelectedAttributes[i]) {
				numberOfCoreAttributes++;
			}// Of if
		}// Of for i
		
		int[][] tempReductMatrix  = SimpleTool.copyIntMatrix(paraMatrix);
		
		if (numberOfCoreAttributes == 0) {
			currentEntropy = 100;
		} else {
			// currentEntropy = conditionalEntropy(currentSelectedAttributes);
			currentEntropy = ConditionalEntropy.conditionalEntropy(tempReductMatrix);
		}
		int currentBestAttribute = -1;
		
		// Step 2.2 Add attributes one by one
		while (Math.abs(currentEntropy - totalEntropy) > 1e-6) {
			double currentBestEntropy = 10000;
			for (int i = 0; i < numberOfConditions; i++) {
				// Ignore selected attributes
				if (currentSelectedAttributes[i])
					continue;

				// Try this attribute
				currentSelectedAttributes[i] = true;
				int[][] tempMatrix  = computeNewDataByReduct(paraMatrix,currentSelectedAttributes);
				currentEntropy = ConditionalEntropy.conditionalEntropy(tempMatrix);
				
				// currentEntropy = conditionalEntropy(currentSelectedAttributes);
				//System.out.println("当前属性" + i + "的信息熵 = " + currentEntropy);
				if (currentBestEntropy > currentEntropy) {
					currentBestEntropy = currentEntropy;
					currentBestAttribute = i;
				}
				// Set back
				currentSelectedAttributes[i] = false;
			}// Of for i
			// System.out.println("当前选择的属性为" + currentBestAttribute + ",最佳信息熵 = " +currentBestEntropy );
				// Really add it
			currentSelectedAttributes[currentBestAttribute] = true;
			tempReductMatrix  = computeNewDataByReduct(paraMatrix,currentSelectedAttributes);
			currentEntropy =  ConditionalEntropy.conditionalEntropy(tempReductMatrix);
			//currentEntropy = conditionalEntropy(currentSelectedAttributes);
			// System.out.println("加了最佳属性后的信息熵 = " + currentEntropy);
		}// Of while
		
		// Step 3. Remove redundant attributes
		for (int i = 0; i < numberOfConditions; i++) {
			// Ignore core attributes and not selected attributes
			if ((core[i]) || (!currentSelectedAttributes[i])) {
				continue;
			}// Of if

			// Try to remove this attribute
			currentSelectedAttributes[i] = false;
			int[][] tempMatrix  = computeNewDataByReduct(tempReductMatrix,currentSelectedAttributes);
			currentEntropy =  ConditionalEntropy.conditionalEntropy(tempMatrix);
			
			//currentEntropy = conditionalEntropy(currentSelectedAttributes);
			if (Math.abs(currentEntropy - totalEntropy) > 1e-6) {
				// Set back
				currentSelectedAttributes[i] = true;
				tempReductMatrix  = computeNewDataByReduct(tempReductMatrix,currentSelectedAttributes);
			} else {
				// This rarely happens, so output this message directly.
				System.out.println("attribute #" + i + " can be removed.");
			}// Of if
		}// Of for i
		
		currentReduct = currentSelectedAttributes;
		
		return currentReduct;
		
	}// Of entropyBasedReduction
	
	/**
	 ************************* 
	 * 不完全约简，只选择前三个strong cuts.
	 * @param paraK
	 * 			控制strong cuts的个数
	 * @return the reduct with the form of boolean vector.
	 ************************* 
	 */
	public boolean[] entropyBasedReduction(int[][] paraMatrix,int paraK) throws Exception {
		initializeForReduction();
		long startTime;
		long endTime;
		String runTimeInformation;
		
		double currentEntropy = 100;
		//totalEntropy = conditionalEntropy();
		totalEntropy = ConditionalEntropy.conditionalEntropyForBoolean(paraMatrix);
		//System.out.println("用新的方法计算的信息熵为:" + totalEntropy);
	
		// Step 1. Compute the core
		computeCore();
	
		// Step 2. Add attributes
		// Step 2.1 Copy core attributes
		int numberOfCoreAttributes = 0;
	
		for (int i = 0; i < numberOfConditions; i++) {
			currentSelectedAttributes[i] = core[i];
			if (currentSelectedAttributes[i]) {
				numberOfCoreAttributes++;
			}// Of if
		}// Of for i
		/*
		SimpleTool.printBooleanArray(currentSelectedAttributes);
		System.out.println("原决策表");
		SimpleTool.printIntMatrix(paraMatrix);
		*/
		int[][] tempReductMatrix  = SimpleTool.copyIntMatrix(paraMatrix);
		/*
		System.out.println("根据属性选择后的决策表");
		SimpleTool.printIntMatrix(tempReductMatrix);
		*/
		if (numberOfCoreAttributes == 0) {
			currentEntropy = 100;
		} else {
			// currentEntropy = conditionalEntropy(currentSelectedAttributes);
			// System.out.println("旧方法信息熵:" + currentEntropy);
			currentEntropy = ConditionalEntropy.conditionalEntropy(tempReductMatrix);
			// System.out.println("新方法信息熵:" + currentEntropy);
		}
		int currentBestAttribute = -1;
		
		// Step 2.2 Add attributes one by one
		for(int i = 0; i < paraK; i++) {
			startTime = new Date().getTime();
			double currentBestEntropy = 10000;
			// System.out.println("numberOfConditions = " + numberOfConditions);
			for (int j = 0; j < numberOfConditions; j++) {
				// Ignore selected attributes
				if (currentSelectedAttributes[j])
					continue;

				// Try this attribute
				currentSelectedAttributes[j] = true;
				/*
				SimpleTool.printBooleanArray(currentSelectedAttributes);
				System.out.println("原决策表");
				SimpleTool.printIntMatrix(paraMatrix);
				*/
				int[][] tempMatrix  = computeNewDataByReduct(paraMatrix,currentSelectedAttributes);
				// System.out.println("根据属性选择后的决策表");
				// SimpleTool.printIntMatrix(tempMatrix);
				
				// currentEntropy = conditionalEntropy(currentSelectedAttributes);
				// System.out.println("旧方法信息熵:" + currentEntropy);
				currentEntropy = ConditionalEntropy.conditionalEntropy(tempMatrix);
				// System.out.println("新方法信息熵:" + currentEntropy);
				
				if (currentBestEntropy > currentEntropy) {
					currentBestEntropy = currentEntropy;
					currentBestAttribute = j;
				}
				// Set back
				currentSelectedAttributes[j] = false;
			}// Of for j
			// Really add it
			currentSelectedAttributes[currentBestAttribute] = true;
			/*
			SimpleTool.printBooleanArray(currentSelectedAttributes);
			System.out.println("真正开始更新@@@@@@");
			System.out.println("原决策表");
			SimpleTool.printIntMatrix(paraMatrix);
			
			currentEntropy = conditionalEntropy(currentSelectedAttributes);
			System.out.println("旧方法信息熵:" + currentEntropy);
			*/
			// currentEntropy = conditionalEntropy(currentSelectedAttributes);
			tempReductMatrix  = computeNewDataByReduct(paraMatrix,currentSelectedAttributes);
			currentEntropy =  ConditionalEntropy.conditionalEntropy(tempReductMatrix);
			// System.out.println("新方法信息熵:" + currentEntropy);
			
			// System.out.println("新决策表");
			// SimpleTool.printIntMatrix(tempReductMatrix);
			
			//若当前信息表的信息熵=0，则提前结束约简
			if(Math.abs(currentEntropy - totalEntropy) < 1e-6){
				break;
			}
			endTime = new Date().getTime();
			runTimeInformation = "计算所有属性的信息熵花费的时间: "
					+ (endTime - startTime) + " ms.";
			System.out.println("约简所用时间\r\n " + runTimeInformation);
		}// end for i
		
		
		// Step 3. Remove redundant attributes
		for (int i = 0; i < numberOfConditions; i++) {
			// Ignore core attributes and not selected attributes
			if ((core[i]) || (!currentSelectedAttributes[i])) {
				continue;
			}// Of if

			// Try to remove this attribute
			currentSelectedAttributes[i] = false;
			// currentEntropy = conditionalEntropy(currentSelectedAttributes);
			int[][] tempMatrix  = computeNewDataByReduct(tempReductMatrix,currentSelectedAttributes);
			currentEntropy =  ConditionalEntropy.conditionalEntropy(tempMatrix);
			if (Math.abs(currentEntropy - totalEntropy) > 1e-6) {
				// Set back
				currentSelectedAttributes[i] = true;
				tempReductMatrix  = computeNewDataByReduct(tempReductMatrix,currentSelectedAttributes);
			} else {
				// This rarely happens, so output this message directly.
				System.out.println("attribute #" + i + " can be removed.");
			}// Of if
		}// Of for i
		
		currentReduct = currentSelectedAttributes;
		return currentReduct;
	}// Of entropyBasedReduction
	
	
	/**
	 ************************* 
	 * 不完全约简，只选择前k个strong attributes.
	 * @param paraK
	 * 			控制strong attributes的个数
	 * @return the reduct with the form of boolean vector.
	 ************************* 
	 */
	public boolean[] findTopKAtrributes(int[][] paraMatrix,int paraK) throws Exception {
		initializeForReduction();

		
		double[] currentEntropy = new double[numberOfConditions];
		// totalEntropy = ConditionalEntropy.conditionalEntropyForBoolean(paraMatrix);
		// totalEntropy = ConditionalEntropy.conditionalEntropy(paraMatrix);
		
		// Step 1. Compute the core
		computeCore();
	
		// Step 2. Add attributes
		// Step 2.1 Copy core attributes
		int numberOfCoreAttributes = 0;
	
		for (int i = 0; i < numberOfConditions; i++) {
			currentSelectedAttributes[i] = core[i];
			if (currentSelectedAttributes[i]) {
				numberOfCoreAttributes++;
			}// Of if
		}// Of for i
		
		int[][] tempReductMatrix  = SimpleTool.copyIntMatrix(paraMatrix);
		
		// Step 2.2 Add attributes one by one
		// startTime = new Date().getTime();
		double currentBestEntropy = 10000;
		for (int i = 0; i < numberOfConditions; i++) {
			// Ignore selected attributes
			if (currentSelectedAttributes[i])
				continue;
			// Try this attribute
			currentSelectedAttributes[i] = true;
			int[][] tempMatrix  = computeNewDataByReduct(paraMatrix,currentSelectedAttributes);
			currentEntropy[i] = ConditionalEntropy.conditionalEntropy(tempMatrix);
			// Set back
			currentSelectedAttributes[i] = false;
		}// Of for j

		//对所有属性的信息熵进行排序，并记录原始属性下标i
		double[][] sortConditionalEntropy = SimpleTool.sortDoubleArray(currentEntropy);
		
		//对前k个最优信息熵对应的属性i进行设置
		for(int i = 0; i < paraK; i++){
			if(i < sortConditionalEntropy[0].length)
				currentSelectedAttributes[(int)sortConditionalEntropy[1][i]] = true;
		}//end for i
			
		// endTime = new Date().getTime();
		// runTimeInformation = "计算所有扩展属性的信息熵花费的时间: "+ (endTime - startTime) + " ms.";
		// System.out.println(runTimeInformation);
	
		
		/*
		// Step 3. Remove redundant attributes
		for (int i = 0; i < numberOfConditions; i++) {
			// Ignore core attributes and not selected attributes
			if ((core[i]) || (!currentSelectedAttributes[i])) {
				continue;
			}// Of if

			// Try to remove this attribute
			currentSelectedAttributes[i] = false;
			// currentEntropy = conditionalEntropy(currentSelectedAttributes);
			int[][] tempMatrix  = computeNewDataByReduct(tempReductMatrix,currentSelectedAttributes);
			double tempCurrentEntropy =  ConditionalEntropy.conditionalEntropy(tempMatrix);
			if (Math.abs(tempCurrentEntropy - totalEntropy) > 1e-6) {
				// Set back
				currentSelectedAttributes[i] = true;
				tempReductMatrix  = computeNewDataByReduct(tempReductMatrix,currentSelectedAttributes);
			} else {
				// This rarely happens, so output this message directly.
				System.out.println("attribute #" + i + " can be removed.");
			}// Of if
		}// Of for i
		*/
		
		
		currentReduct = currentSelectedAttributes;
		/*
		for(int i = 0; i < currentReduct.length; i++){
			System.out.println(currentReduct[i]);
		}
		*/
		return currentReduct;
	}// Of findTopKAtrributes
	
	
	
	
	
	
	/**
	 ************************* 
	 * 不完全约简，只选择前三个strong cuts.
	 * @param paraK
	 * 			控制strong cuts的个数
	 * @return the reduct with the form of boolean vector.
	 ************************* 
	 */
	public boolean[] entropyBasedReductionFast(int[][] paraMatrix,int paraK) throws Exception {
		initializeForReduction();
		long startTime;
		long endTime;
		String runTimeInformation;
		
		double[] currentEntropy = new double[numberOfConditions];
		// totalEntropy = ConditionalEntropy.conditionalEntropyForBoolean(paraMatrix);
		totalEntropy = ConditionalEntropy.conditionalEntropy(paraMatrix);
		
		// Step 1. Compute the core
		computeCore();
	
		// Step 2. Add attributes
		// Step 2.1 Copy core attributes
		int numberOfCoreAttributes = 0;
	
		for (int i = 0; i < numberOfConditions; i++) {
			currentSelectedAttributes[i] = core[i];
			if (currentSelectedAttributes[i]) {
				numberOfCoreAttributes++;
			}// Of if
		}// Of for i
		
		int[][] tempReductMatrix  = SimpleTool.copyIntMatrix(paraMatrix);
		
		// Step 2.2 Add attributes one by one
		startTime = new Date().getTime();
		double currentBestEntropy = 10000;
		for (int i = 0; i < numberOfConditions; i++) {
			// Ignore selected attributes
			if (currentSelectedAttributes[i])
				continue;
			// Try this attribute
			currentSelectedAttributes[i] = true;
			int[][] tempMatrix  = computeNewDataByReduct(paraMatrix,currentSelectedAttributes);
			currentEntropy[i] = ConditionalEntropy.conditionalEntropy(tempMatrix);
			// Set back
			currentSelectedAttributes[i] = false;
		}// Of for j

		//对所有属性的信息熵进行排序，并记录原始属性下标i
		double[][] sortConditionalEntropy = SimpleTool.sortDoubleArray(currentEntropy);
		
		//对前k个最优信息熵对应的属性i进行设置
		for(int i = 0; i < paraK; i++){
			if(i < sortConditionalEntropy[0].length)
				currentSelectedAttributes[(int)sortConditionalEntropy[1][i]] = true;
		}//end for i
			
		endTime = new Date().getTime();
		runTimeInformation = "计算所有扩展属性的信息熵花费的时间: "+ (endTime - startTime) + " ms.";
		System.out.println(runTimeInformation);
	
		
		
		// Step 3. Remove redundant attributes
		for (int i = 0; i < numberOfConditions; i++) {
			// Ignore core attributes and not selected attributes
			if ((core[i]) || (!currentSelectedAttributes[i])) {
				continue;
			}// Of if

			// Try to remove this attribute
			currentSelectedAttributes[i] = false;
			// currentEntropy = conditionalEntropy(currentSelectedAttributes);
			int[][] tempMatrix  = computeNewDataByReduct(tempReductMatrix,currentSelectedAttributes);
			double tempCurrentEntropy =  ConditionalEntropy.conditionalEntropy(tempMatrix);
			if (Math.abs(tempCurrentEntropy - totalEntropy) > 1e-6) {
				// Set back
				currentSelectedAttributes[i] = true;
				tempReductMatrix  = computeNewDataByReduct(tempReductMatrix,currentSelectedAttributes);
			} else {
				// This rarely happens, so output this message directly.
				System.out.println("attribute #" + i + " can be removed.");
			}// Of if
		}// Of for i
		
		
		
		currentReduct = currentSelectedAttributes;
		
		for(int i = 0; i < currentReduct.length; i++){
			System.out.println(currentReduct[i]);
		}
		
		return currentReduct;
	}// Of entropyBasedReductionFast
	
	
	/**
	 ************************* 
	 * 不完全约简，只选择前三个strong cuts.
	 * @param paraK
	 * 			控制strong cuts的个数
	 * @return the reduct with the form of boolean vector.
	 ************************* 
	 */
	public boolean[] entropyBasedReductionFast() throws Exception {
		initializeForReduction();
		double currentEntropy = 100;
		totalEntropy = conditionalEntropy();

		// Step 1. Compute the core
		computeCore();
		/*
		System.out.println("核属性为：");
		for(int i = 0; i < core.length; i++){
			System.out.println(core[i]);
		}
		System.out.println("-------------");
		*/
		// Step 2. Add attributes
		// Step 2.1 Copy core attributes
		int numberOfCoreAttributes = 0;
		for (int i = 0; i < numberOfConditions; i++) {
			currentSelectedAttributes[i] = core[i];
			if (currentSelectedAttributes[i]) {
				numberOfCoreAttributes++;
			}// Of if
		}// Of for i

		
		if (numberOfCoreAttributes == 0) {
			currentEntropy = 100;
		} else {
			currentEntropy = conditionalEntropy(currentSelectedAttributes);
		}
		int currentBestAttribute = -1;
		
		// Step 2.2 Add attributes one by one
		while (Math.abs(currentEntropy - totalEntropy) > 1e-6) {
			double currentBestEntropy = 10000;
			/*
			for (int i = 0; i < numberOfConditions; i++) {
				// Ignore selected attributes
				if (currentSelectedAttributes[i])
					continue;

				// Try this attribute
				currentSelectedAttributes[i] = true;
				currentEntropy = conditionalEntropy(currentSelectedAttributes);
				if (currentBestEntropy > currentEntropy) {
					currentBestEntropy = currentEntropy;
					currentBestAttribute = i;
				}
				// Set back
				currentSelectedAttributes[i] = false;
			}// Of for i
			*/
			int countIndex = 0;
			for(int i = 0; i < valueRangeOfAttributes.length; i++){
				if(statusOfAttribute[i] == false){
					countIndex++;
					continue;
				}//end of if
				for(int j = 0; j < valueRangeOfAttributes[i].length; j++){
					if (currentSelectedAttributes[countIndex]){
						countIndex++;
						continue;
					}//end of if
					
					// Try this attribute
					currentSelectedAttributes[countIndex] = true;
					currentEntropy = conditionalEntropy(currentSelectedAttributes);
					if (currentBestEntropy > currentEntropy) {
						currentBestEntropy = currentEntropy;
						currentBestAttribute = countIndex;
					}
					// Set back
					currentSelectedAttributes[countIndex] = false;
					countIndex++;
				}//end of for j
			}//end of for i
			
			
				// Really add it
			currentSelectedAttributes[currentBestAttribute] = true;
			currentEntropy = conditionalEntropy(currentSelectedAttributes);
		}// Of while
		/*
		// Step 3. Remove redundant attributes
		for (int i = 0; i < numberOfConditions; i++) {
			// Ignore core attributes and not selected attributes
			if ((core[i]) || (!currentSelectedAttributes[i])) {
				continue;
			}// Of if

			// Try to remove this attribute
			currentSelectedAttributes[i] = false;
			currentEntropy = conditionalEntropy(currentSelectedAttributes);
			if (Math.abs(currentEntropy - totalEntropy) > 1e-6) {
				// Set back
				currentSelectedAttributes[i] = true;
			} else {
				// This rarely happens, so output this message directly.
				//System.out.println("attribute #" + i + " can be removed.");
			}// Of if
		}// Of for i
	*/
		currentReduct = currentSelectedAttributes;

		// int currentReductIndex = getCurrentReductIndex();
		// if (currentReductIndex == -1) {
			// String errorMessage = "Error occurred in NominalDecisionSystem.entropyBasedReduction(): "
					// + getReductString() + " is not a reduct";
			// throw new Exception(errorMessage);
		// }// Of if
		// System.out.println("约简结果");
		
		for(int i = 0; i < currentReduct.length; i++){
			System.out.println(currentReduct[i]);
		}
		
		return currentReduct;
	}// Of entropyBasedReductionFast
	
	/**
	 ********************************** 
	 * 
	 * @return The classification accuracy.
	 ********************************** 
	 */
	public void computeNewValueRangeOfAttributes(boolean[] paraReductResult){
		double[][] newValueRangeOfAttributes = new double[numberOfConditions][];
		for(int i = 0; i < numberOfConditions;i++){
			// System.out.println("i = " + i);
			// System.out.println("numValuesOfAttribute[" + i + "] = " + numValuesOfAttribute[i]);
			if(numValuesOfAttribute[i] == 0){
				newValueRangeOfAttributes[i] = new double[1];
				newValueRangeOfAttributes[i][0] = Double.MAX_VALUE;
				numValuesOfAttribute[i] = 1;
				continue;
			}//Of if
			newValueRangeOfAttributes[i] = new double[numValuesOfAttribute[i]];
			int indexOfAttributeValue = 0;
			int indexOfOldAttribtueValue = 0;
			int countNumberOfFlase = 0;
			for(int j = beginAndEndIndexOfAttribute[i][0]; 
					j < beginAndEndIndexOfAttribute[i][1]; j++){
				// System.out.println("j = " + j);
				if(paraReductResult[j]==false){
					countNumberOfFlase++;
					indexOfOldAttribtueValue++;
					continue;
				}//Of if
				newValueRangeOfAttributes[i][indexOfAttributeValue] = valueRangeOfAttributes[i][indexOfOldAttribtueValue];
				indexOfAttributeValue++;
				indexOfOldAttribtueValue++;
			}//Of for j
		}//Of for i
		
		valueRangeOfAttributes = newValueRangeOfAttributes;
		
	}
	

	/**
	 ********************************** 
	 * 
	 * @return The classification accuracy.
	 ********************************** 
	 */
	public void computeNewValueRangeOfAttributes(boolean[] paraReductResult,
			int paraAttribute, int paraNumOfAttribtueValue){
		double[] newValueRangeOfAttributes;
		//for(int i = 0; i < numberOfConditions;i++){
			//System.out.println("i = " + i);
			//System.out.println("numValuesOfAttribute[" + i + "] = " + numValuesOfAttribute[i]);
			if(numValuesOfAttribute[paraAttribute] == 0){
				newValueRangeOfAttributes  = new double[1];
				newValueRangeOfAttributes[0] = Double.MAX_VALUE;
				numValuesOfAttribute[paraAttribute] = 1;
				// System.out.println("属性的属性值个数维0");
			}//Of if
			//System.out.println("属性的属性值个数为" + numValuesOfAttribute[paraAttribute]);
			newValueRangeOfAttributes  = new double[paraNumOfAttribtueValue];
			int indexOfAttributeValue = 0;
			int indexOfOldAttribtueValue = 0;
			int countNumberOfFlase = 0;
			for(int j = 0; j < paraReductResult.length; j++){
				//System.out.println("j = " + j);
				if(paraReductResult[j]==false){
					countNumberOfFlase++;
					indexOfOldAttribtueValue++;
					continue;
				}//Of if
				newValueRangeOfAttributes[indexOfAttributeValue] = valueRangeOfAttributes[paraAttribute][indexOfOldAttribtueValue];
				indexOfAttributeValue++;
				indexOfOldAttribtueValue++;
			}//Of for j
		//}//Of for i
		valueRangeOfAttributes[paraAttribute] = newValueRangeOfAttributes;
		//valueRangeOfAttributes = newValueRangeOfAttributes;
		
	}
	
	
	/**
	 ********************************** 
	 * 
	 * @return The classification accuracy.
	 ********************************** 
	 */
	public double classifyTreeTest(RoughDecisionSystem paraTrainSystem, 
			RoughDecisionSystem paraTestSystem) throws Exception {
		double tempAccuracy = 0;

		// RoughDecisionSystem[] tempSystems = divideInTwo(divideRatio);
		
		// Train
		// Id3 tempTree = new Id3();
		J48 tempTree= new J48();
		// C5 tempTree= new C5(); 
		// RandomForest tempTree =new RandomForest();
		// RandomTree tempTree = new RandomTree();
		// NBTree tempTree =new NBTree();
		
		tempTree.buildClassifier(paraTrainSystem);
		

		// test
		double tempClassLabel = 0;
		int tempCorrect = 0;
		
		for (int i = 0; i < paraTestSystem.numInstances(); i++) {
			tempClassLabel = tempTree.classifyInstance(paraTestSystem.instance(i));
			if (Math.abs(tempClassLabel
					- paraTestSystem.instance(i).classValue()) < 1e-6) {
				tempCorrect++;
			}// Of if
		}// Of for i

		// System.out.println("tempCorrect=" + tempCorrect);
		tempAccuracy = (0.0 + tempCorrect) / paraTestSystem.numInstances();

		// tempTree.
		return tempAccuracy;
	}// Of classifyTreeTest
	
	/**
	 ********************************** 
	 * 将信息熵最大的属性ai离散化后的列加入到S*中。
	 * 
	 * @param paraDecisionSystem
	 *            原决策表信息
	 * @param paraScaledDS
	 *            属性ai的扩展决策表（更新后的）
	 * @param paraAttribute
	 *            当前属性
	 * @param paraBestCut
	 *            当前属性ai的最佳CUT集
	 * @param paraDiscretizationDS
	 * 			  离散化后的总表S*
	 * @param paraNumOfDis
	 * 			   已离散化的属性个数
	 ********************************** 
	 */
	public void addAttributeToDiscretizationDS(double[][] paraDecisionSystem,
			int[][] paraScaledDS, int paraAttribute, double[] paraBestCut,
			int[][] paraDiscretizationDS){
		int tempInstances = paraDecisionSystem.length;
		// Step 1. 根据更新后的扩展决策表，计算得到新的决策表信息
		for (int i = 0; i < tempInstances; i++) {
			for (int j = 0; j < numAttributes() - 1; j++) {
				if(j != paraAttribute){
					continue;
				}//end if
				if(paraBestCut.length == 1 && paraBestCut[0] == Double.MAX_VALUE){
					paraDiscretizationDS[i][paraAttribute] = 0;
					continue;
				}//Of if
				
				int countOneNumber = 0;
				// Step 2.1 对于更新后的扩展决策表，统计每一行中每一个扩展属性的属性值为1的属性个数
				for (int k = 0; k < paraBestCut.length; k++) {
					if(paraScaledDS[i][k] == 0){
						continue;
					}//Of if
					countOneNumber++;
				}// Of for k
				// Step 2.2 属性值为1的属性个数，即为新决策表原始属性的属性值
				paraDiscretizationDS[i][paraAttribute] = countOneNumber;
			}// Of for j
			// Step 2.3 决策属性的属性值
			paraDiscretizationDS[i][numAttributes()-1] = 
					(int) paraDecisionSystem[i][numAttributes()-1];
		}// Of for i	
	}//end of addAttributeToDiscretizationDS
	
	/**
	 ********************************** 
	 * 对局部离散化后的信息表进行全局离散化中。
	 * 
	 * @param paraLocalDisDS
	 *            局部离散化后的决策表
	 * @param paraDSType
	 *            数据集类型（训练集OR测试集）
	 * @return 全局离散化后的信息表
	 * @throws Exception
	 ********************************** 
	 */
	public int[][] globalDiscretization(
			NewLocalDisPlusGlobalDisFastVersion2 paraLocalDisDS,
			String paraDSType,int paraTopKOfAttribute) throws Exception {

		
		int[][] tempGlobalDisDS = new int[paraLocalDisDS.numInstances()][paraLocalDisDS
				.numAttributes()];

		// Step 1. 将决策表信息存储到一个二维数组中，便于处理
		int[][] tempReductedDecisionSystem = new int[paraLocalDisDS
				.numInstances()][paraLocalDisDS.numAttributes()];
		for (int i = 0; i < paraLocalDisDS.numInstances(); i++) {
			for (int j = 0; j < paraLocalDisDS.numAttributes(); j++) {
				tempReductedDecisionSystem[i][j] = (int)paraLocalDisDS.instance(i)
						.value(j);
			}// Of for j
		}// Of for i
			// Step 2. 若是训练集，则计算每个属性新的属性值
		if (paraDSType.equals("train")) {
			statusOfAttribute = new boolean[paraLocalDisDS.numberOfConditions];
			statusOfAttribute = paraLocalDisDS.findTopKAtrributes(tempReductedDecisionSystem,paraTopKOfAttribute);
			// System.out.println("----------属性的Top20选择状态----------");
			// SimpleTool.printBooleanArray(statusOfAttribute);
			
			//paraLocalDisDS.computeNewAttribute(paraLocalDisDS);
			paraLocalDisDS.computeNewAttributeTopK(paraLocalDisDS,statusOfAttribute);
			
		}// end if
		// System.out.println("------属性的选择状态-------");
		// SimpleTool.printBooleanArray(statusOfAttribute);
		
		// System.out.println("------属性的断点信息-------");
		// SimpleTool.printDoubleMatrix(valueRangeOfAttributes);
		
		
		
		// Step 3. 全局Scaling信息表，并存储
		NewLocalDisPlusGlobalDisFastVersion2 scalingDecisionSystem = null;
		int[][] tempScaledDecisionSystem = paraLocalDisDS
				.scalingDecisionSystem(paraLocalDisDS);
		// System.out.println("即将存储scaling后的二维数组数据到一张决策表中：");
		scalingDecisionSystem = paraLocalDisDS.writeInFile(
				tempScaledDecisionSystem, "global_scaling");

		
		
		
		// Step 4. 若是训练集，则需要进行求约得到cut集P
		if (paraDSType.equals("train")) {
			
			boolean[] tempSelectedAttributeByBoolean = new boolean[totalNumValues];
			reductResultsOfGlobal = new boolean[totalNumValues];
			for (int i = 0; i < totalNumValues; i++)
				tempSelectedAttributeByBoolean[i] = false;

			// Step 4.1 对scaling后的决策表进行约简
			// System.out.println("-----对全局scaling后的决策表进行约简------");
			reductResultsOfGlobal = scalingDecisionSystem
					 .entropyBasedReduction();
			// reductResultsOfGlobal = scalingDecisionSystem.entropyBasedReductionFast();
			// System.out.println("-----对全局scaling后的决策表进行约简结束-----");
			
			// Step 4.2 根据约简结果，重新计算得到新的属性值列表
			paraLocalDisDS.computeNumOfAttributeValue(reductResultsOfGlobal);
			// Step 4.3 根据约简更新决策表对于的二维数组tempScaledDecisionSystem
			// System.out.println("------根据约简更新扩展后的新决策表开始-----");
			tempScaledDecisionSystem = paraLocalDisDS
					.computeNewDataByOptimalReduct(tempScaledDecisionSystem,reductResultsOfGlobal);
			// System.out.println("------根据约简更新扩展后的新决策表结束-----");

			// Step 4.3 计算属性新的属性值列表，这个列表相当于是P
			paraLocalDisDS.computeNewValueRangeOfAttributes(reductResultsOfGlobal);
		}// end if

		// Step 5. 将约简后的二维数组存储到一张新的决策表reductedDecisionSystem中
		// System.out.println("将约简后的二维数组存储到一张新的决策表中：");
		NewLocalDisPlusGlobalDisFastVersion2 reductedDecisionSystem = scalingDecisionSystem
				.writeInFile(tempScaledDecisionSystem, "train");

		// System.out.println("更行原始决策表开始");
		// Step 6.
		// 根据最优约简方案tempMrelativeReduct，更新原始决策表的二维数组tempReductedDecisionSystem信息
		tempGlobalDisDS = paraLocalDisDS.computeNewDecisionTable(
				tempReductedDecisionSystem, tempScaledDecisionSystem);
		// System.out.println("更行原始决策表结束");
/*
		// Step 7. 存储最后的决策表信息
		NewLocalDisPlusGlobalDisFast trainSystem = paraLocalDisDS
				.restoreToArff(paraLocalDisDS, tempGlobalDisDS, "globalDS");
		*/
		return tempGlobalDisDS;
	}// end of globalDiscretization
	
	/**
	 ********************************** 
	 * 对局部离散化后的信息表进行全局离散化中。
	 * 
	 * @param paraLocalDisDS
	 *            局部离散化后的决策表
	 * @param paraDSType
	 *            数据集类型（训练集OR测试集）
	 * @return 全局离散化后的信息表
	 * @throws Exception
	 ********************************** 
	 */
	public int[][] cutShiftGlobalDiscretization(
			NewLocalDisPlusGlobalDisFastVersion2 paraOriginalDS,
			int[][] paraDSByLocalDiscretization,
			String paraTypeOfDS,
			int paraCutsNum) throws Exception {

		int[][] discretizationDecisionSystem = new int[paraOriginalDS
			                               				.numInstances()][paraOriginalDS.numAttributes()];
			
			// Step 1. 将决策表信息存储到一个二维数组中，便于处理
			int[][] tempReductedDecisionSystem = SimpleTool.copyIntMatrix(paraDSByLocalDiscretization);
			
	
			// System.out.println("信息表：");
			// SimpleTool.printIntMatrix(tempReductedDecisionSystem);
			
			
			// Step 2. 如果是训练集，则需要计算属性新的属性值列表
			if (paraTypeOfDS.equals("train")) {
				// 根据训练集获取每个属性新的属性值，并按照新的属性值，生成一个新的信息表，存储在一个二维数组tempScaledDecisionSystem中
				numValuesOfAttribute = new int[paraOriginalDS.numAttributes() - 1];
				totalNumValues = 0;
				valueRangeOfAttributes=new double[paraOriginalDS.numAttributes()-1][];
				
				//beginAndEndIndexOfAttribute = new int[numValuesOfAttribute.length][2];
				reductResults = new boolean[numValuesOfAttribute.length][];
				
				
			}// end if

			
			CutBlock[][] cutsBlockInfor = new CutBlock[ 50 ][paraOriginalDS.numClasses() ];
			CutBlock[][][] blockInfor  = new CutBlock[(paraOriginalDS.numAttributes() - 1) * paraCutsNum][2][paraOriginalDS.numClasses()];
			
			CutBlock.initialize(cutsBlockInfor);
			CutBlock.initialize(blockInfor);
			
			//int[][] selectedCutIndex = new int[paraOriginalDS.numAttributes() - 1][];
			double miniEntropy = Double.MAX_VALUE;
			double currentEntropy = 0;
			int bestCut = 0;
			int bestCutAttribute = 0;
			int bestBlockIndex = 0;
			// Step 3. 若是训练集，对训练集中的每个属性，计算初始的块信息
			if (paraTypeOfDS.equals("train")){
				int cutBlockIndex = 0;
				for (int i = 0; i < paraOriginalDS.numAttributes() - 1; i++) {
					System.out.println("-----------------------------");
					System.out.println("对属性" + i + "进行块信息的计算");
					//Step 3.1. 计算新的属性值
					
					paraOriginalDS.computeNewAttribtueWithSort(paraOriginalDS, i, paraCutsNum);
					//blockInfor[i]  = new CutBlock[valueRangeOfAttributes[i].length][][] ;
					reductResults[i] = new boolean[valueRangeOfAttributes[i].length];
				
					//Step 3.2. 计算基础的块信息
					for(int j = 0; j< valueRangeOfAttributes[i].length; j++){
						
						for(int k = 0; k <  paraOriginalDS.numInstances(); k++){
							if(tempReductedDecisionSystem[k][i] < valueRangeOfAttributes[i][j]){
								
								blockInfor[cutBlockIndex][0][tempReductedDecisionSystem[k][paraOriginalDS.numAttributes()-1]].blockNum++;
								blockInfor[cutBlockIndex][0][tempReductedDecisionSystem[k][paraOriginalDS.numAttributes()-1]].blockElementIndex.add(k);
								// blockInfor[cutBlockIndex][0][tempReductedDecisionSystem[k][paraOriginalDS.numAttributes()-1]].attributeNum = i;
								// blockInfor[cutBlockIndex][0][tempReductedDecisionSystem[k][paraOriginalDS.numAttributes()-1]].cutIndex = j;
								
							}//end if
							else{
								blockInfor[cutBlockIndex][1][tempReductedDecisionSystem[k][paraOriginalDS.numAttributes()-1]].blockNum++;
								blockInfor[cutBlockIndex][1][tempReductedDecisionSystem[k][paraOriginalDS.numAttributes()-1]].blockElementIndex.add(k);
								// blockInfor[cutBlockIndex][1][tempReductedDecisionSystem[k][paraOriginalDS.numAttributes()-1]].attributeNum = i;
								// blockInfor[cutBlockIndex][1][tempReductedDecisionSystem[k][paraOriginalDS.numAttributes()-1]].cutIndex = j;
								
							}//end else
						}//end for k
						
						for(int k = 0; k < blockInfor[cutBlockIndex].length; k++){
							for(int l = 0; l <  blockInfor[cutBlockIndex][k].length; l++){
								blockInfor[cutBlockIndex][k][l].attributeNum = i;
								blockInfor[cutBlockIndex][k][l].cutIndex = j;
							}//end for l
						}//end for k
					
						// CutBlock.printCutBlockInfor(blockInfor[cutBlockIndex]);
						
						//Step 3.3. 计算基础块信息的信息熵，选择信息熵最小的块加入到P
						
						currentEntropy = blockEntropyForCutBlock(blockInfor[cutBlockIndex],paraOriginalDS.numInstances());
						// System.out.println("属性" + i + "的第" + j + "个断点的信息熵 = " + currentEntropy);
						if(currentEntropy < miniEntropy){
								miniEntropy = currentEntropy;
								bestCut = j;
								bestCutAttribute = i;
								bestBlockIndex = cutBlockIndex;
						}//end if
						cutBlockIndex++;
						
					}//end for j
					//SimpleTool.printIntMatrix(cutsBlockInfor);
				}//end for i
				//将信息熵最小的块信息加入到P
				cutsBlockInfor =  blockInfor[bestBlockIndex];
				reductResults[bestCutAttribute][bestCut] = true;
				//selectedCutIndex[bestCutAttribute][0] = bestCut;
					
				// SimpleTool.printBooleanArray(reductResults[bestCutAttribute]);
				System.out.println("第1次选择的断点是"+"属性"+bestCutAttribute+"的第"+bestCut+"个断点");
				CutBlock.printCutBlockInfor(cutsBlockInfor);
				//System.out.println("bestBlockIndex = " + bestBlockIndex);
				
				// Step 4. 继续寻找最佳cut，直到Cut集的块信息熵=0	
				for(int roundTime = 2; roundTime < 18 ; roundTime++){
					miniEntropy = Double.MAX_VALUE;
					bestCut = 0; 
					bestCutAttribute = 0;
					CutBlock[][] bestBlockInfor = new CutBlock[2*cutsBlockInfor.length][paraOriginalDS.numClasses()];
					
					if(blockEntropyForCutBlock(cutsBlockInfor,paraOriginalDS.numInstances()) < 1e-6 )
						break;
					
					//Step 4.2 计算每一块的信息熵，并找到最小的
					for(int i = 0; i < blockInfor.length; i++){
						//for(int j = 0; j < valueRangeOfAttributes[i].length; j++ ){
						
							// System.out.println("即将插入的块信息");
							// System.out.println("属性" + blockInfor[i][0][0].attributeNum + "的第" + blockInfor[i][0][0].cutIndex+"个断点");
							// CutBlock.printCutBlockInfor(blockInfor[i]);
							
							if(reductResults[blockInfor[i][0][0].attributeNum][blockInfor[i][0][0].cutIndex] == true)
								continue;
							//计算得到新的块信息、
							CutBlock[][] tempBlockInfor = 
									computeNewBlockInfor(blockInfor[i], cutsBlockInfor);
							
							// System.out.println("将第"+ i+ "个块插入后的块信息为");
							// CutBlock.printCutBlockInfor(tempBlockInfor);
							
							//计算tempBlockInfor的块信息熵
							currentEntropy = blockEntropyForCutBlock(tempBlockInfor,paraOriginalDS.numInstances());
							
							// System.out.println("插入当前块后的信息熵 = " + currentEntropy);
							
							if(currentEntropy < miniEntropy){
								miniEntropy = currentEntropy;
								bestCut = blockInfor[i][0][0].cutIndex;
								bestCutAttribute = blockInfor[i][0][0].attributeNum;
								bestBlockInfor = tempBlockInfor;
							}//end if
						//}//end for j
					}//end for i
					
					
					//压缩block信息，去除全为0的行！！！！
					
					CutBlock[][] bestBlockInforAfterCompress = compressBlockInfor(bestBlockInfor);
					
					
					//Step 4.3 计算每一块的信息熵，并找到最小的
					cutsBlockInfor = bestBlockInforAfterCompress;
					// cutsBlockInfor = bestBlockInfor;
					reductResults[bestCutAttribute][bestCut] = true;
					System.out.println("第"+ roundTime+ "次选择的断点是"+"属性"+bestCutAttribute+"的第"+bestCut+"个断点");
					CutBlock.printCutBlockInfor(cutsBlockInfor);
					
					// System.out.println("当前的信息熵 = " + blockEntropyForCutBlock(cutsBlockInfor,paraOriginalDS.numInstances()));
					// SimpleTool.printBooleanMatrix(reductResults);
				}//end for roundTime
			}//end if
			
			// SimpleTool.printBooleanMatrix(reductResults);
			
			//Step 5.根据P中的新属性值，更新决策表
			for (int i = 0; i < paraOriginalDS.numAttributes() - 1; i++){
				int tempCustNum = 0;
				for(int j = 0; j< valueRangeOfAttributes[i].length; j++){
					if(reductResults[i][j] == true)
						tempCustNum++;
				}//end for j
				
				paraOriginalDS.updateDS(
						tempReductedDecisionSystem, reductResults[i], tempCustNum, i , discretizationDecisionSystem);
			}//end for i
			
			System.out.println("全局离散化后的决策表：");
			SimpleTool.printIntMatrix(discretizationDecisionSystem);
				
			// Step 6. 返回局部离散化后的信息表
			return discretizationDecisionSystem;
	}// end of globalDiscretization
	
	
	
	/**
	 ********************************** 
	 * 压缩CutBlock数组。
	 * 
	 * @param paraCurrentBlockInfor
	 *            压缩前的数组
	 * @return 压缩后的数组
	 ********************************** 
	 */
	public CutBlock[][] compressBlockInfor(CutBlock[][] paraCurrentBlockInfor){
		CutBlock[][] tempBlockInfor = new CutBlock[paraCurrentBlockInfor.length][paraCurrentBlockInfor[0].length];
	
		int index  = 0;
		for(int i = 0; i < paraCurrentBlockInfor.length; i++){
			int countZeroNum = 0;
			for(int j = 0; j < paraCurrentBlockInfor[0].length; j++){
				if(paraCurrentBlockInfor[i][j].blockNum != 0)
					break;
				else{
					countZeroNum++;
					continue;
				}
			}//end for j
			//压缩！
			if(countZeroNum == paraCurrentBlockInfor[0].length){
				continue;
			}//end of if
			else{
				tempBlockInfor[index] = paraCurrentBlockInfor[i];
				index++;
			}
		}//end of i
		CutBlock[][] resultBlockInfor = new CutBlock[index][paraCurrentBlockInfor[0].length];
		for(int i = 0; i < index; i++){
			resultBlockInfor[i] = tempBlockInfor[i];
		}//end for i
		return resultBlockInfor;
	}//end of compressBlockInfor
	
	/**
	 ********************************** 
	 * 局部离散化信息表。
	 * 
	 * @param paraLocalDisDS
	 *            局部离散化后的决策表
	 * @param paraTypeOfDS
	 *            数据集类型（训练集OR测试集）
	 * @return 局部离散化后的信息表
	 * @throws Exception
	 ********************************** 
	 */
	public CutBlock[][] computeNewBlockInfor(
			CutBlock[][] paraCurrentBlock, 
			CutBlock[][] paraCurrentSelectedBlock){
		
		
/*
		System.out.println("将块");
		CutBlock.printCutBlockInfor(paraCurrentBlock);
		System.out.println("和块");
		CutBlock.printCutBlockInfor(paraCurrentSelectedBlock);
		System.out.println("进行合并");
		
	*/	
		CutBlock[][] resultBlockInfor = new CutBlock[paraCurrentBlock.length * paraCurrentSelectedBlock.length][paraCurrentBlock[0].length];
		CutBlock.initialize(resultBlockInfor);
		
		List<Integer> currentBlockElementIndex;
		int rowIndexOfNewBlock = 0;
		for(int i = 0; i < paraCurrentBlock.length; i++){
			for(int j = 0; j < paraCurrentBlock[i].length; j++){
				for(int k = 0; k < paraCurrentSelectedBlock.length; k++){
					for(int l = 0; l < paraCurrentSelectedBlock[k].length; l++){
						if(l != j)
							continue;
						//rowIndexOfNewBlock = 2*i+k;
						rowIndexOfNewBlock = i * paraCurrentSelectedBlock.length + k;
						// int blockNum = 0;
						if(paraCurrentBlock[i][j].blockNum == 0 || paraCurrentSelectedBlock[k][l].blockNum == 0){
							resultBlockInfor[rowIndexOfNewBlock][l].blockNum = 0;
							resultBlockInfor[rowIndexOfNewBlock][l].attributeNum = paraCurrentBlock[i][j].attributeNum;
							resultBlockInfor[rowIndexOfNewBlock][l].cutIndex = paraCurrentBlock[i][j].cutIndex;
						}//end of if
						else{
							currentBlockElementIndex = countNumOfSameInstance(paraCurrentBlock[i][j], paraCurrentSelectedBlock[k][l]);;
							resultBlockInfor[rowIndexOfNewBlock][l].blockElementIndex = currentBlockElementIndex;	
							resultBlockInfor[rowIndexOfNewBlock][l].blockNum = currentBlockElementIndex.size();
							resultBlockInfor[rowIndexOfNewBlock][l].attributeNum = paraCurrentBlock[i][j].attributeNum;
							resultBlockInfor[rowIndexOfNewBlock][l].cutIndex = paraCurrentBlock[i][j].cutIndex;
						}
						
					}//end for k
				}//end for k
			}//end for j
		}//end for i
		
		
		return resultBlockInfor;
		
	}//end of computeNewBlockInfor
	
	

	/**
	 ********************************** 
	 * 计算两个块中相同的对象数目。
	 * 
	 * @param paraCurrentBlock
	 *            即将插入的块
	 * @param paraCurrentSelectedBlock
	 *            已经选择的块
	 * @return 相同的对象数目
	 ********************************** 
	 */
	public List<Integer> countNumOfSameInstance(CutBlock paraCurrentBlock, CutBlock paraCurrentSelectedBlock){
		//int countResult =0;
		List<Integer> blockElementIndex = new LinkedList();
		
		int i = 0;
		int j = 0;
		while ((i < paraCurrentBlock.blockElementIndex.size()) && (j < paraCurrentSelectedBlock.blockElementIndex.size())) {
			if ((int)paraCurrentBlock.blockElementIndex.get(i) == (int)paraCurrentSelectedBlock.blockElementIndex.get(j)) {
				blockElementIndex.add(paraCurrentBlock.blockElementIndex.get(i));
				i++;
				j++;
			} else if ((int)paraCurrentBlock.blockElementIndex.get(i) > (int)paraCurrentSelectedBlock.blockElementIndex.get(j)) {
				j++;
			} else {
				i++;
			}// Of if
		}// Of while
	/*
		for(int i = 0; i < paraCurrentBlock.blockElementIndex.size(); i++){
			for(int j = 0; j < paraCurrentSelectedBlock.blockElementIndex.size(); j++){
				if(paraCurrentBlock.blockElementIndex.get(i) == paraCurrentSelectedBlock.blockElementIndex.get(j)){
					countResult++;
					blockElementIndex.add(paraCurrentBlock.blockElementIndex.get(i));
				}	
			}//end for j
		}//end for j
		*/
		return blockElementIndex;
	}//end of countNumOfSameInstance
	
	
	/**
	 ********************************** 
	 * 局部离散化信息表。
	 * 
	 * @param paraLocalDisDS
	 *            局部离散化后的决策表
	 * @param paraTypeOfDS
	 *            数据集类型（训练集OR测试集）
	 * @return 局部离散化后的信息表
	 * @throws Exception
	 ********************************** 
	 */
	public int[][] localDiscretization(
			NewLocalDisPlusGlobalDisFastVersion2 paraOriginalDS,
			String paraTypeOfDS, int paraCutsNum) throws Exception {

		int[][] discretizationDecisionSystem = new int[paraOriginalDS
		                               				.numInstances()][paraOriginalDS.numAttributes()];
		
		// Step 1. 将决策表信息存储到一个二维数组中，便于处理
		double[][] tempReductedDecisionSystem = new double[paraOriginalDS.numInstances()][paraOriginalDS.numAttributes()];
		for (int i = 0; i < paraOriginalDS.numInstances(); i++) {
			for (int j = 0; j < paraOriginalDS.numAttributes(); j++) {
				tempReductedDecisionSystem[i][j] = paraOriginalDS.instance(i).value(j);
			}// Of for j
		}// Of for i
/*
		System.out.println("排序前的信息表：");
		SimpleTool.printDoubleMatrix(tempReductedDecisionSystem);
		*/
		
		// Step 2. 如果是训练集，则需要计算属性新的属性值列表
		if (paraTypeOfDS.equals("train")) {
			// 根据训练集获取每个属性新的属性值，并按照新的属性值，生成一个新的信息表，存储在一个二维数组tempScaledDecisionSystem中
			numValuesOfAttribute = new int[paraOriginalDS.numAttributes() - 1];
			totalNumValues = 0;
			valueRangeOfAttributes=new double[paraOriginalDS.numAttributes()-1][];
			//beginAndEndIndexOfAttribute = new int[numValuesOfAttribute.length][2];
			reductResults = new boolean[numValuesOfAttribute.length][];	
		}// end if

		// Step 3. 对每个属性，进行局部离散化
		for (int i = 0; i < paraOriginalDS.numAttributes() - 1; i++) {
			if (paraTypeOfDS.equals("train")){
				int[] selectedCutIndex = new int[paraCutsNum];
				// System.out.println("-----------------------------");
				// System.out.println("对属性" + i + "进行局部离散化");
				
				// Step 3.1. 根据当前属性，对子表进行排序，计算新的属性值列表
				double[][] tempSortedDS = paraOriginalDS.sortDSAndComputeNewAttribtue(paraOriginalDS, i);
				
				reductResults[i] = new boolean[valueRangeOfAttributes[i].length];
				int[][] cutsBlockInfor = new int[ paraCutsNum + 1 ][paraOriginalDS.numClasses() ];
				int[][][] blockInfor  = new int[valueRangeOfAttributes[i].length][][] ;
				/*
				System.out.println("排序后的子表信息：");
				SimpleTool.printDoubleMatrix(tempSortedDS);
				System.out.println("属性" + i + "的新属性值列表：");
				SimpleTool.printDoubleArray(valueRangeOfAttributes[i]);
				*/
				//Step 3.2. 计算基础的块信息
				for(int j = 0; j< valueRangeOfAttributes[i].length; j++){
					blockInfor[j] = new int[2][paraOriginalDS.numClasses()];
					for(int k = 0; k <  paraOriginalDS.numInstances(); k++){	
						if(tempSortedDS[k][0] < valueRangeOfAttributes[i][j]){
							blockInfor[j][0][(int)tempSortedDS[k][1]]++;
						}//end if
						else{
							blockInfor[j][1][(int)tempSortedDS[k][1]]++;
						}//end else
					}//end for k
				}//end for j
				/*
				System.out.println("块信息");
				for(int j = 0; j< valueRangeOfAttributes[i].length; j++ ){
					SimpleTool.printIntMatrix(blockInfor[j]);
				}//end for j
				 */
				//Step 3.3. 计算基础块信息的信息熵，选择信息熵最小的块加入到P
				double miniEntropy = Double.MAX_VALUE;
				int bestCut = 0;
				for(int j = 0; j< valueRangeOfAttributes[i].length; j++ ){
					double currentEntropy = 0;
					currentEntropy = blockEntropy(blockInfor[j],paraOriginalDS.numInstances());
					if(currentEntropy < miniEntropy){
						miniEntropy = currentEntropy;
						bestCut = j;
					}//end if
				}//end for j
				//将信息熵最小的块信息加入到P
				cutsBlockInfor =  blockInfor[bestCut];
				reductResults[i][bestCut] = true;
				selectedCutIndex[0] = bestCut;
				
				// SimpleTool.printBooleanArray(reductResults[i]);
				// System.out.println("第一次选择的断点是"+bestCut+",选择后的块信息为");
				// SimpleTool.printIntMatrix(cutsBlockInfor);
				
				//Step 3.4. 依次往P中加入断点，直到|P| > paraCutsNum
				for(int j = 1; j < paraCutsNum; j++){
					//System.out.println("第" + (j+1) + "次选择断点");
					
					miniEntropy = Double.MAX_VALUE;
					bestCut = 0;
					int[][] bestBlockInfor = new int[j+2][paraOriginalDS.numClasses()];
					
					for(int k = 0; k< valueRangeOfAttributes[i].length; k++ ){
						if(reductResults[i][k] == true){
							// System.out.println("第" + k + "个断点已经被选！！");
							continue;
						}
						//double currentEntropy = 0;
						//double tempEntropy = 0;
						int[][] tempBlockInfor = new int[j+2][paraOriginalDS.numClasses()];
						int positionOfShift = 0;
						
						if(valueRangeOfAttributes[i][k] < valueRangeOfAttributes[i][selectedCutIndex[0]])
							positionOfShift = 0;
						else if(valueRangeOfAttributes[i][k] > valueRangeOfAttributes[i][selectedCutIndex[j-1]])
							positionOfShift =  j;
						else{
							//寻找合适的位置
							for(int l = 0; l < j; l++){
								if(valueRangeOfAttributes[i][k] < valueRangeOfAttributes[i][l])
									break;
								positionOfShift++;
							}//end for l
						}//end else
						tempBlockInfor = cutShift(blockInfor[k], cutsBlockInfor, positionOfShift);
						/*
						System.out.println("加入" + valueRangeOfAttributes[i][k] + "断点后的块信息");
						SimpleTool.printIntMatrix(tempBlockInfor);
						*/
						//计算tempBlockInfor的块信息熵
						double currentEntropy = blockEntropy(tempBlockInfor,paraOriginalDS.numInstances());
						// System.out.println("第" + k + "个断点加入后的信息熵 = " + currentEntropy);
						if(currentEntropy < miniEntropy){
							miniEntropy = currentEntropy;
							bestCut = k;
							bestBlockInfor = tempBlockInfor;
						}//end if
					}//end for k	
						
					cutsBlockInfor =  bestBlockInfor;
					reductResults[i][bestCut] = true;
					selectedCutIndex[j] = bestCut;
					// SimpleTool.printBooleanArray(reductResults[i]);
					// System.out.println("第"+(j+1)+"次选择的断点是"+bestCut+",选择后的块信息为");
					// SimpleTool.printIntMatrix(cutsBlockInfor);
					
				}//end for j
			}//end if
			
			//Step 3.5.根据P中的新属性值，更新决策表
			paraOriginalDS.updateDS(
					tempReductedDecisionSystem, reductResults[i], paraCutsNum, i , discretizationDecisionSystem);
			/*
			System.out.println("离散化属性" + i + "后的信息表为");
			SimpleTool.printIntMatrix(discretizationDecisionSystem);
			*/
		}// end for i
		
		// Step 4. 返回局部离散化后的信息表
		return discretizationDecisionSystem;
	}// end of localDiscretization
	
	/**
	 ********************************** 
	 * 根据cut集更新决策表。
	 * @param paraDecisionSystem
	 * 			原决策系统
	 * @param paraSelectedCut
	 * 			断点集的选择状态
	 * @param paraNewDecisionSystem
	 * 			新的离散化后的信息系统
	 ********************************** 
	 */	
	
	public void updateDS(double[][] paraDecisionSystem, 
			boolean[] paraSelectedCut, 
			int paraCutsNum, int paraAttribute, 
			int[][] paraNewDecisionSystem){
		int[] selectedCuts = new int[paraCutsNum];
		int count = 0;
		for(int i = 0; i < paraSelectedCut.length; i++){
			if(paraSelectedCut[i] == true){
				selectedCuts[count] = i;
				count++;
			}//end if
		}//end for i
		//SimpleTool.printIntArray(selectedCuts);
		
		for(int i = 0; i < paraDecisionSystem.length; i++){
			count = 0;
			for(int j = 0; j < paraCutsNum; j++){
				if(paraDecisionSystem[i][paraAttribute] < 
						valueRangeOfAttributes[paraAttribute][selectedCuts[j]])
					break;
				count++;
			}//end for j
			paraNewDecisionSystem[i][paraAttribute] = count;
			// Step 2.3 决策属性的属性值
			paraNewDecisionSystem[i][numAttributes()-1] = 
								(int) paraDecisionSystem[i][numAttributes()-1];
		}//end for i
	}//end of updateDS
	
	
	
	/**
	 ********************************** 
	 * 根据cut集更新决策表。
	 * @param paraDecisionSystem
	 * 			原决策系统
	 * @param paraSelectedCut
	 * 			断点集的选择状态
	 * @param paraNewDecisionSystem
	 * 			新的离散化后的信息系统
	 ********************************** 
	 */	
	
	public void updateDS(int[][] paraDecisionSystem, 
			boolean[] paraSelectedCut, 
			int paraCutsNum, int paraAttribute, 
			int[][] paraNewDecisionSystem){
		int[] selectedCuts = new int[paraCutsNum];
		int count = 0;
		for(int i = 0; i < paraSelectedCut.length; i++){
			if(paraSelectedCut[i] == true){
				selectedCuts[count] = i;
				count++;
			}//end if
		}//end for i
		//SimpleTool.printIntArray(selectedCuts);
		
		for(int i = 0; i < paraDecisionSystem.length; i++){
			count = 0;
			for(int j = 0; j < paraCutsNum; j++){
				if(paraDecisionSystem[i][paraAttribute] < 
						valueRangeOfAttributes[paraAttribute][selectedCuts[j]])
					break;
				count++;
			}//end for j
			paraNewDecisionSystem[i][paraAttribute] = count;
			// Step 2.3 决策属性的属性值
			paraNewDecisionSystem[i][numAttributes()-1] = 
								 paraDecisionSystem[i][numAttributes()-1];
		}//end for i
	}//end of updateDS
	
	/**
	 ********************************** 
	 * 计算块信息的信息熵。
	 * 
	 * @return 信息熵.
	 ********************************** 
	 */	
	public double blockEntropy(int[][] paraBlockInfor, int paraTotalBlockSize){
		
			double tempEntropy = 0;
			double currentEntropy = 0;
			
			for(int i = 0; i < paraBlockInfor.length; i++){
				int blockSize = 0;
				for(int j = 0; j < paraBlockInfor[0].length; j++){
					blockSize += paraBlockInfor[i][j];
					if(paraBlockInfor[i][j] > 0)
						tempEntropy -= paraBlockInfor[i][j]* Utils.log2(paraBlockInfor[i][j]);
				}//end for l
				if(blockSize > 0)
					tempEntropy +=  blockSize * Utils.log2(blockSize);
			}//end for k
			currentEntropy = tempEntropy/paraTotalBlockSize;
			// System.out.println("当前块的信息熵 = " + currentEntropy);
			return currentEntropy;
	}
	

	/**
	 ********************************** 
	 * 计算块信息的信息熵。
	 * 
	 * @return 信息熵.
	 ********************************** 
	 */	
	public double blockEntropyForCutBlock(CutBlock[][] paraBlockInfor, int paraTotalBlockSize){
		
			double tempEntropy = 0;
			double currentEntropy = 0;
			
			for(int i = 0; i < paraBlockInfor.length; i++){
				int blockSize = 0;
				for(int j = 0; j < paraBlockInfor[i].length; j++){
					blockSize += paraBlockInfor[i][j].blockNum;
					if(paraBlockInfor[i][j].blockNum > 0)
						tempEntropy -= paraBlockInfor[i][j].blockNum * Utils.log2(paraBlockInfor[i][j].blockNum);
				}//end for j
				if(blockSize > 0)
					tempEntropy +=  blockSize * Utils.log2(blockSize);
			}//end for i
			currentEntropy = tempEntropy/paraTotalBlockSize;
			// System.out.println("当前块的信息熵 = " + currentEntropy);
			return currentEntropy;
	}
	
	
	/**
	 ********************************** 
	 * 根据给定的块信息及移动的位置，重新计算新的块信息。
	 * 
	 * @return 更新后的决策表信息.
	 ********************************** 
	 */	
	public int[][] cutShift(int[][] paraBlockInfor, int[][] paraCutsBlockInfor, int paraPositionOfShift){
		/*
		System.out.println("将块");
		SimpleTool.printIntMatrix(paraBlockInfor);
		System.out.println("插入到块");
		SimpleTool.printIntMatrix(paraCutsBlockInfor);
		System.out.println("的第" + paraPositionOfShift + "个位置");
	
		System.out.println("paraCutsBlockInfor.length -1 " + (paraCutsBlockInfor.length-1));
	*/	
		
		int[][] resultBlockInfor = new int[paraCutsBlockInfor.length + 1][paraCutsBlockInfor[0].length];
		if(paraPositionOfShift == 0){
			for(int i = 0; i < resultBlockInfor.length; i++){
				if( i == 0){
					resultBlockInfor[i] = paraBlockInfor[0];
				}
				else if( i == 1){
					for(int j = 0; j < resultBlockInfor[0].length; j++){
						resultBlockInfor[i][j] = paraCutsBlockInfor[0][j] - resultBlockInfor[0][j];
					}//end for j
				}
				else{
					resultBlockInfor[i] = paraCutsBlockInfor[i-1];
				}//end else
			}//end for i 
		}//end if
		else if(paraPositionOfShift == paraCutsBlockInfor.length-1){
			for(int i = resultBlockInfor.length-1; i >= 0 ; i--){
				if( i == resultBlockInfor.length - 1){
					resultBlockInfor[i] = paraBlockInfor[1];
				}
				else if( i == resultBlockInfor.length - 2){
					for(int j = 0; j < paraCutsBlockInfor[0].length; j++){
						resultBlockInfor[i][j] = 
								paraCutsBlockInfor[paraCutsBlockInfor.length-1][j] - resultBlockInfor[resultBlockInfor.length-1][j];
					}//end for j
				}
				else{
					resultBlockInfor[i] = paraCutsBlockInfor[i];
				}//end else
			}//end for i 
		}//end if
		else{
			for(int i = 0; i < resultBlockInfor.length ; i++){
				if( i == paraPositionOfShift){
					for(int j = 0; j < paraCutsBlockInfor[0].length; j++){
						resultBlockInfor[i][j] = 
								paraBlockInfor[0][j] - paraCutsBlockInfor[paraPositionOfShift-1][j];
					}//end for j
				}//end if
				else if( i == paraPositionOfShift+1){
					for(int j = 0; j < paraCutsBlockInfor[0].length; j++){
						resultBlockInfor[i][j] = 
								paraBlockInfor[1][j] - paraCutsBlockInfor[paraPositionOfShift+1][j];
					}//end for j
				}//end else if
			}//end for i
		}
		return resultBlockInfor;
	}//end of cutShift
	
	/**
	 ********************************** 
	 * 根据约简集合，重新计算得到新的决策表信息。
	 * 
	 * @return 更新后的决策表信息.
	 ********************************** 
	 */
	public int[][] updateDSByOptimalReduct(int[][] pareMatrixData, boolean[] paraOptimalReduct) {
		// 存储更新后的信息
		int[][] tempNewDataAfterReduct;
		tempNewDataAfterReduct = SimpleTool.copyIntMatrix(pareMatrixData) ;
		
		// Step 4. 更新新的决策表S*，直接删除约简集合P中为false的属性及相关列
		for (int i = 0; i < paraOptimalReduct.length; i++) {
			
			if(paraOptimalReduct[i] == true){
				continue;
			}//Of if
			
			for(int j = 0; j < pareMatrixData.length; j++){
				tempNewDataAfterReduct[j][i] = 0;
			}//end for j
		}// Of for i
		// Step 5. 返回更新后的决策表
		return tempNewDataAfterReduct;
	}// Of computeNewDataByOptimalReduct
	
	
	
	/**
	 ************************* 
	 * Divide the decision system in two according to the given percentage.
	 * Obtain two subsets for training and testing classifiers.
	 * 
	 * @param paraPercentage
	 *            The percentage of instances in the first subset.
	 * @return Two decision systems in an array.
	 ************************* 
	 */
	public NewLocalDisPlusGlobalDisFastVersion2[] divideInTwo(double paraPercentage)
			throws Exception {
		//System.out.println("\r\n numberOfInstances=" + numberOfInstances);
		boolean[] firstInclusionArray = SimpleTool
				.generateBooleanArrayForDivision(numberOfInstances,
						paraPercentage);
		//System.out.print("firstInclusionArray=[");
		//for (int i = 0; i < firstInclusionArray.length; i++) {
			//System.out.print(firstInclusionArray[i] + ",");
		//}
		//System.out.print("]\r\n");

		NewLocalDisPlusGlobalDisFastVersion2 firstDecisionSystem = new NewLocalDisPlusGlobalDisFastVersion2(this);
		firstDecisionSystem.delete(firstInclusionArray);

		boolean[] secondInclusionArray = SimpleTool
				.revertBooleanArray(firstInclusionArray);
		NewLocalDisPlusGlobalDisFastVersion2 secondDecisionSystem = new NewLocalDisPlusGlobalDisFastVersion2(this);
		secondDecisionSystem.delete(secondInclusionArray);

		NewLocalDisPlusGlobalDisFastVersion2[] subsets = new NewLocalDisPlusGlobalDisFastVersion2[2];
		subsets[0] = firstDecisionSystem;
		subsets[1] = secondDecisionSystem;
		

		return subsets;
	}// Of divideInTwo
	
	/**
	 ************************* 
	 * The main function.
	 ************************* 
	 */
	public static void main(String args[]) {
		
		int topKOfAttribute = 10;//全局离散化时选择的最佳属性个数
		int tempCutsNum = 4;//局部离散化选择的断点数目
		while(tempCutsNum <= 6){
			int dataNum = 1;
			while(dataNum <= 39){
			
			long startTime,startTime1,startTime2;
			long endTime;
			String runTimeInformation;	
			String arffFilename = null;
		
			if(dataNum == 1){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/iris/iris.arff";
			}
			else if(dataNum == 2){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/wpbc/wpbc.arff";
			}
			else if(dataNum == 3){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/wdbc/wdbc.arff";
			}
			else if(dataNum == 4){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/breast/breast.arff";
			}
			else if(dataNum == 5){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/pid/pid.arff";
			}
			else if(dataNum == 6){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/wave/waveform.arff";
			}
			else if(dataNum == 7){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/optdigit/optdigit.arff";
			}
			else if(dataNum == 8){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/page-blocks/page-blocks.arff";
			}
			else if(dataNum == 9){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/sat/sat.arff";
			}
			else if(dataNum == 10){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/pendigit/pendigit.arff";
			}
			else if(dataNum == 11){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/eeg/eeg.arff";
			}
			else if(dataNum == 12){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/magic/magic.arff";
			}
			else if(dataNum == 13){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/madelon/madelon.arff";
			}
			else if(dataNum == 14){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/musk1/musk1.arff";
			}
			else if(dataNum == 15){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/libras/libras.arff";
			}
			else if(dataNum == 16){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/waveform2/waveform2.arff";
			}
			else if(dataNum == 17){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/DLA/DLA.arff";
			}
			else if(dataNum == 18){
				if(tempCutsNum == 4){
					dataNum++;
					continue;
				}
				arffFilename = "data/DisLocalGlobal/dccc/DCCC.arff";
			}
			else if(dataNum == 19){
				arffFilename = "data/DisLocalGlobal/abalone/abalone.arff";
			}
			else if(dataNum == 20){
				arffFilename = "data/DisLocalGlobal/coil2000/coil2000.arff";
			}
			else if(dataNum == 21){
				arffFilename = "data/DisLocalGlobal/gsad/gsad.arff";
			}
			else if(dataNum == 22){
				arffFilename = "data/DisLocalGlobal/ionosphere/ionosphere.arff";
			}
			else if(dataNum == 23){
				arffFilename = "data/DisLocalGlobal/jm1/jm1.arff";
			}
			else if(dataNum == 24){
				arffFilename = "data/DisLocalGlobal/mc1/mc1.arff";
			}
			else if(dataNum == 25){
				
				arffFilename = "data/DisLocalGlobal/mfeat-factors/mfeat-factors.arff";
			}
			else if(dataNum == 26){
				
				arffFilename = "data/DisLocalGlobal/mfeat-fourier/mfeat-fourier.arff";
			}
			else if(dataNum == 27){
				
				arffFilename = "data/DisLocalGlobal/mfeat-karhunen/mfeat-karhunen.arff";
			}
			else if(dataNum == 28){
				
				arffFilename = "data/DisLocalGlobal/mfeat-zernike/mfeat-zernike.arff";
			}
			else if(dataNum == 29){
				
				arffFilename = "data/DisLocalGlobal/pc2/pc2.arff";
			}
			else if(dataNum == 30){
				
				arffFilename = "data/DisLocalGlobal/penbased/penbased.arff";
			}
			else if(dataNum == 31){
				
				arffFilename = "data/DisLocalGlobal/satimage/satimage.arff";
			}
			else if(dataNum == 32){
				
				arffFilename = "data/DisLocalGlobal/segment/segment.arff";
			}
			else if(dataNum == 33){
				
				arffFilename = "data/DisLocalGlobal/sonar/sonar.arff";
			}
			else if(dataNum == 34){
				
				arffFilename = "data/DisLocalGlobal/spambase/spambase.arff";
			}
			else if(dataNum == 35){
				arffFilename = "data/DisLocalGlobal/texture/texture.arff";
			}
			else if(dataNum == 36){
				
				arffFilename = "data/DisLocalGlobal/usps/usps.arff";
			}
			else if(dataNum == 37){
				arffFilename = "data/DisLocalGlobal/winequality-white/winequality-white.arff";
			}
			else if(dataNum == 38){
				arffFilename = "data/DisLocalGlobal/wle/wle.arff";
			}
			else if(dataNum == 39){
				arffFilename = "data/DisLocalGlobal/shuttle/shuttle.arff";
			}
			
			try {
				
					FileReader fileReader = new FileReader(arffFilename);
					NewLocalDisPlusGlobalDisFastVersion2 decisionSystem = new NewLocalDisPlusGlobalDisFastVersion2(fileReader);
					decisionSystem.setClassIndex(decisionSystem.numberOfConditions);
					decisionSystem.setArffFilename(arffFilename);
					fileReader.close();
					
					
					
					//10-folders crossing validation
					for(int i = 0; i < 10; i++){
					
						
						//设置输出文件位置
					File test = new File("./" + "NewResult/"+ tempCutsNum + "/" + decisionSystem.m_RelationName + "/" + decisionSystem.m_RelationName + "Folder" + i + "Output.txt");
					PrintStream out = new PrintStream(new FileOutputStream(test));
					System.setOut(out);
						
						
						// Step 1. 将数据集分割为10份，9份作为训练，1份作为测试。
						NewLocalDisPlusGlobalDisFastVersion2[] subsets = decisionSystem.divideInTwo(0.9);

						NewLocalDisPlusGlobalDisFastVersion2 decisionSystemOfTrain = subsets[0];
						NewLocalDisPlusGlobalDisFastVersion2 decisionSystemOfTest = subsets[1];

						// tempInitialAccuracy = decisionSystem
						// .classifyTreeTest(decisionSystemOfTrain,
						// decisionSystemOfTest);

						// System.out.println("程序开始");

						// Step 2.局部离散化
						startTime = new Date().getTime();
						// System.out.println("训练集进行局部离散化开始");

						// Step 2.1 训练集进行局部离散化
						int[][] tempDisDSOfTrain = decisionSystemOfTrain.localDiscretization(decisionSystemOfTrain,
								"train", tempCutsNum);
						// System.out.println("训练集局部离散化后的信息表");
						// SimpleTool.printIntMatrix(tempDisDSOfTrain);
						// System.out.println("训练集进行局部离散化结束");
						// System.out.println("------------------------");

						// Step 2.2 测试集进行局部离散化
						// System.out.println("测试集进行局部离散化开始");
						int[][] tempDisDSOfTest = decisionSystemOfTest.localDiscretization(decisionSystemOfTest, "test",
								tempCutsNum);
						// System.out.println("测试集局部离散化后的信息表");
						// SimpleTool.printIntMatrix(tempDisDSOfTest);
						// System.out.println("测试集进行局部离散化结束");

						endTime = new Date().getTime();
						runTimeInformation = "Reduction cost time: " + (endTime - startTime) + " ms.";

						// System.out.println("局部离散化所用总时间\r\n " +
						// runTimeInformation);

						long localRunningTime = endTime - startTime;
						decisionSystem.toStoreAccuracyInFile(localRunningTime,
								"./" + "NewResult/" + tempCutsNum + "/" + decisionSystem.m_RelationName
										+ "/localRunningTimeOf" + decisionSystem.m_RelationName + ".txt");

						/*
						 * System.out.println("局部离散化后的每个属性的cut集状态：");
						 * SimpleTool.printBooleanMatrix(reductResults);
						 */
						NewLocalDisPlusGlobalDisFastVersion2 localDisDSOfTrain = null;
						localDisDSOfTrain = decisionSystem.writeIntDSInFile(tempDisDSOfTrain,
								"localDSOfTrainFolder" + i, tempCutsNum);
						NewLocalDisPlusGlobalDisFastVersion2 localDisDSOfTest = null;
						localDisDSOfTest = decisionSystem.writeIntDSInFile(tempDisDSOfTest, "localDSOfTestFolder" + i,
								tempCutsNum);

						// localDisAccuracy = decisionSystem
						// .classifyTreeTest(localDisDSOfTrain,
						// localDisDSOfTest);

						// Step 3.全局离散化
						startTime1 = new Date().getTime();
						// System.out.println("对训练集进行全局离散化");

						// Step 3.1 训练集全局离散化
						int[][] globalDisDSOfTrain = localDisDSOfTrain.globalDiscretization(localDisDSOfTrain, "train",
								topKOfAttribute);
						// int[][] globalDisDSOfTrain =
						// localDisDSOfTrain.cutShiftGlobalDiscretization(localDisDSOfTrain,tempDisDSOfTrain,"train",tempCutsNum);

						// System.out.println("============================");
						// System.out.println("对测试集进行全局离散化");
						// Step 3.2 测试集全局离散化
						int[][] globalDisDSOfTest = localDisDSOfTest.globalDiscretization(localDisDSOfTest, "test",
								topKOfAttribute);
						// int[][] globalDisDSOfTest =
						// localDisDSOfTest.cutShiftGlobalDiscretization(localDisDSOfTest,tempDisDSOfTest,"test",
						// tempCutsNum);

						// System.out.println("全局离散化后的每个属性的cut集状态：");
						// SimpleTool.printBooleanMatrix(reductResults);

						endTime = new Date().getTime();
						runTimeInformation = "Reduction cost time: " + (endTime - startTime1) + " ms.";

						// System.out.println("全局离散化所用总时间\r\n " +
						// runTimeInformation);

						long globalRunningTime = endTime - startTime1;
						decisionSystem.toStoreAccuracyInFile(globalRunningTime,
								"./" + "NewResult/" + tempCutsNum + "/" + decisionSystem.m_RelationName
										+ "/globalRunningTimeOf" + decisionSystem.m_RelationName + ".txt");

						// Step 4. 存储最后的决策表信息
						NewLocalDisPlusGlobalDisFastVersion2 trainSystem = decisionSystem.restoreToArff(decisionSystem,
								globalDisDSOfTrain, "trainFolder" + i, tempCutsNum);
						NewLocalDisPlusGlobalDisFastVersion2 testSystem = decisionSystem.restoreToArff(decisionSystem,
								globalDisDSOfTest, "testFolder" + i, tempCutsNum);

						endTime = new Date().getTime();
						long totalRunningTime = endTime - startTime;
						decisionSystem.toStoreAccuracyInFile(totalRunningTime,
								"./" + "NewResult/" + tempCutsNum + "/" + decisionSystem.m_RelationName
										+ "/totalRunningTimeOf" + decisionSystem.m_RelationName + ".txt");

						// Step 5. 测试分类精度

						double tempInitialAccuracy = 0;
						double tempFinalAccuracy = 0;
						double localDisAccuracy = 0;

						// 决策表未经过算法处理的分类精度计算及存储 tempInitialAccuracy =
						tempInitialAccuracy = decisionSystem.classifyTreeTest(decisionSystemOfTrain,
								decisionSystemOfTest);
						localDisAccuracy = decisionSystem.classifyTreeTest(localDisDSOfTrain, localDisDSOfTest);
						tempFinalAccuracy = decisionSystem.classifyTreeTest(trainSystem, testSystem);

						decisionSystem.toStoreAccuracyInFile(tempInitialAccuracy,
								"./" + "NewResult/" + tempCutsNum + "/" + decisionSystem.m_RelationName + "/"
										+ decisionSystem.m_RelationName + "InitialAccuracy.txt");
						decisionSystem.toStoreAccuracyInFile(localDisAccuracy,
								"./" + "NewResult/" + tempCutsNum + "/" + decisionSystem.m_RelationName + "/"
										+ decisionSystem.m_RelationName + "LocalDisAccuracy.txt");
						decisionSystem.toStoreAccuracyInFile(tempFinalAccuracy,
								"./" + "NewResult/" + tempCutsNum + "/" + decisionSystem.m_RelationName + "/"
										+ decisionSystem.m_RelationName + "FinalAccuracy.txt");
					} // end for i

					decisionSystem
							.toStoreDeviatioLine("./" + "NewResult/" + tempCutsNum + "/" + decisionSystem.m_RelationName
									+ "/" + decisionSystem.m_RelationName + "InitialAccuracy.txt");
					decisionSystem
							.toStoreDeviatioLine("./" + "NewResult/" + tempCutsNum + "/" + decisionSystem.m_RelationName
									+ "/" + decisionSystem.m_RelationName + "LocalDisAccuracy.txt");
					decisionSystem
							.toStoreDeviatioLine("./" + "NewResult/" + tempCutsNum + "/" + decisionSystem.m_RelationName
									+ "/" + decisionSystem.m_RelationName + "FinalAccuracy.txt");

				} catch (Exception ee) {
					System.out.println("Error occurred while trying to read \'" + arffFilename
							+ "\' in CoserProject.readArffFile().\r\n" + ee);

				} // Of try
			
				dataNum++;	
			}//end while dataNum
			//System.out.println("----------------------------");
			tempCutsNum++;
		}//end of while tempCutsNum	
	}// Of main
}
