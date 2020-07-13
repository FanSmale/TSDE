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
 * Summary:��ɢ���㷨���ֲ���ɢ��+ȫ����ɢ��<br>
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
		
		System.out.println("messageNormalized��ʼ");
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
		System.out.println("messageNormalized����");
		System.out.println("����ά������Ϣ���������洢��messageNormalized����ʱ��\r\n " + 
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
		System.out.println("��������Ϣappend��buffer����ʱ��Ϊ\r\n " + 
							(endTime - startTime) + "ms");
		
		startTime = new Date().getTime();
		// Step 3. Write to an arff file.
		try {
			String s = text.toString();
			FileWriter fw = new FileWriter(filename);
			fw.write(s, 0, s.length());
			fw.flush();
			fw.close();
			//System.out.println("д����");
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in DecisionSystem.nomalize() while writing to a file.\r\n"
							+ ee);
		}// Of try
		
		endTime = new Date().getTime();
		System.out.println("����Ϣ�浽arff�ļ�����ʱ��Ϊ\r\n " + 
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
		System.out.println("����Ϣ��ȡ��arff�ļ�����ʱ��Ϊ\r\n " +
							(endTime - startTime) + "ms");
		return currentDs;

	}// Of writeAndReadArffFile

	/**
	 ************************* 
	 * �����µ�����ֵ�б���չԭ���߱�. 
	 * @return �µ���Ϣ��
	 ************************* 
	 */
	public int[][] scalingDecisionSystem(RoughDecisionSystem paraRoughDecisionSystem) {
		int tempInstances = paraRoughDecisionSystem.numInstances();
		int[][] tempCurrentScalingData = new int[tempInstances][totalNumValues + 1];
		int tempIndexOfNewAttribute = 0;
		
		// Step 2. �����µ������б�����õ��µľ��߱�S*
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
	 * ����ѡ�����Ե�����ֵ�б���չԭ���߱�ÿ��ֻ��չһ������.
	 * @param paraRoughDecisionSystem 
	 * 			ԭ���߱�S
	 * @param paraScaledAttribute
	 * 			��ѡ������
	 * @return ��չ�����Ϣ��S��
	 ************************* 
	 */
	public int[][] scalingDecisionSystem(
			RoughDecisionSystem paraRoughDecisionSystem, int paraScaledAttribute) {
		int tempNumOfAttribtueValue = numValuesOfAttribute[paraScaledAttribute];
		int tempInstances = paraRoughDecisionSystem.numInstances();
		int[][] tempCurrentScalingData = new int[tempInstances][tempNumOfAttribtueValue + 1];
		int tempIndexOfNewAttribute = 0;
		// Step 1. �����µ������б�����õ��µľ��߱�S��
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
	 * ���ݵ�ǰ���ԣ����ӱ��������
	 * @param paraRoughDecisionSystem 
	 * 			ԭ���߱�S
	 * @param paraScaledAttribute
	 * 			��ѡ������
	 * @return ��������Ϣ�ӱ�S��
	 ************************* 
	 */
	public double[][] sortDSAndComputeNewAttribtue(RoughDecisionSystem paraRoughDecisionSystem, int paraScaledAttribute){
		int tempInstances = paraRoughDecisionSystem.numInstances();
		
		double[] candidateValueSets = new double[tempInstances];
		double[][] sortedValueSets = new double[2][tempInstances];
		
		// System.out.println("���Ե�ԭʼ����ֵ�б�");
		// Step 1. ɨ��ԭ���߱���ȡÿһ�����Ե�����ֵ�б�
 		for(int  i = 0; i < tempInstances; i++){
			candidateValueSets[i] = paraRoughDecisionSystem.instance(i).value(paraScaledAttribute);
 		}//end for i
 		// Step 1.2 �Ե�ǰ���Ե�����ֵ��������
 		sortedValueSets = SimpleTool.sortDoubleArray(candidateValueSets);
 	   // Step 1.3 ����õ��������µ��ӱ�
 		double[][] sortedDS = new double[tempInstances][2];
 		for(int i = 0; i < tempInstances; i++){
 			sortedDS[i][0] = sortedValueSets[0][i];
 			int j = (int) sortedValueSets[1][i];
 			
 			// sortedDS[i][0] = sortedValueSets[i][0];
 			// int j = (int) sortedValueSets[i][1];
 			
 			sortedDS[i][1] = paraRoughDecisionSystem.instance(j).value(paraRoughDecisionSystem.numAttributes()-1);
 		}//end for i
 		/*
 		System.out.println("�������ӱ�");
 		SimpleTool.printDoubleMatrix(sortedDS);
 		*/	
 		
 		
 	   // Step 1.4 �Ե�ǰ���Ե�����ֵ����ȥ�أ�������������ֵ�б�
 		double[] sortedValueSetCompress  = SimpleTool.compressDoubleArray(sortedValueSets[0]);
 		/*
 		System.out.println("ȥ�غ������ֵ�б�");
 		SimpleTool.printDoubleArray(sortedValueSetCompress);
 		*/
 		// Step 1.3 ���㵱ǰ�����µ�����ֵ 		
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
	 * ���ݵ�ǰ���ԣ����ӱ��������
	 * @param paraRoughDecisionSystem 
	 * 			ԭ���߱�S
	 * @param paraScaledAttribute
	 * 			��ѡ������
	 * @return ��������Ϣ�ӱ�S��
	 ************************* 
	 */
	public void computeNewAttribtueWithSort(RoughDecisionSystem paraRoughDecisionSystem, int paraScaledAttribute, int paraCutsNum){
		
		int tempInstances = paraRoughDecisionSystem.numInstances();
		
		double[] sortedValueSetCompress = new double[paraCutsNum+1];
		for(int i = 0; i <= paraCutsNum; i++ ){
			sortedValueSetCompress[i] = i;
		}//end for i
	
 		
 		// System.out.println("ȥ�غ������ֵ�б�");
 		// SimpleTool.printDoubleArray(sortedValueSetCompress);
 			
 		// Step 1.3 ���㵱ǰ�����µ�����ֵ
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
	 * ����ԭ����ֵ�б�����õ��µ�����ֵ�����Դ������µ���Ϣ��.
	 * @return �µ���Ϣ��
	 ************************* 
	 */
	public void computeNewAttribute(RoughDecisionSystem paraRoughDecisionSystem ) throws Exception {
		
		int tempInstances = paraRoughDecisionSystem.numInstances();
		
		numValuesOfAttribute = new int[paraRoughDecisionSystem.numAttributes() - 1];
		totalNumValues = 0;
		valueRangeOfAttributes=new double[paraRoughDecisionSystem.numAttributes()-1][];
		
		double[] candidateValueSets = new double[tempInstances];
		double[] sortedValueSets = new double[tempInstances];
		// System.out.println("���Ե�ԭʼ����ֵ�б�");
		// Step 1. ɨ��ԭ���߱���ȡÿһ�����Ե�����ֵ�б�������õ��ϵ㼯�ϣ����µ����Լ���
 		for(int  i = 0; i < paraRoughDecisionSystem.numAttributes() - 1; i++){
 			// Step 1.1 ����ɨ��ÿһ�����󣬻�ȡ��ǰ���Ե���������ֵ
			for (int j = 0; j < tempInstances; j++) {
				candidateValueSets[j] = paraRoughDecisionSystem.instance(j).value(i);
			}//Of for j
			
			// Step 1.2 �Ե�ǰ���Ե�����ֵ��������ȥ��
			sortedValueSets = SimpleTool.compressAndSortDoubleArray
					(candidateValueSets, candidateValueSets.length);
			
			// SimpleTool.printDoubleArray(sortedValueSets);
			
			// Step 1.3 ���㵱ǰ�����µ�����ֵ
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
 		// System.out.println("������ֵ�б�");
 		// SimpleTool.printDoubleMatrix(valueRangeOfAttributes);
		// tempCurrentScalingData��ά���������洢��չ��ľ��߱���Ϣ
	
	}// Of computeNewAttribute

	
	/**
	 ************************* 
	 * compute new attribute. 
	 * ����ԭ����ֵ�б�����õ��µ�����ֵ�����Դ������µ���Ϣ��.
	 * @return �µ���Ϣ��
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
		// System.out.println("���Ե�ԭʼ����ֵ�б�");
		// Step 1. ɨ��ԭ���߱���ȡÿһ�����Ե�����ֵ�б�������õ��ϵ㼯�ϣ����µ����Լ���
 		for(int  i = 0; i < paraRoughDecisionSystem.numAttributes() - 1; i++){
 			
 			if(paraStausOfAttribute[i] == false){
 				
 				valueRangeOfAttributes[i] = new double[1];
 				
 				valueRangeOfAttributes[i][0] = 0;
 			
 				
 				numValuesOfAttribute[i] = valueRangeOfAttributes[i].length;
 				totalNumValues += numValuesOfAttribute[i];	
 				continue;
 			}//end of if 
 				
 			// Step 1.1 ����ɨ��ÿһ�����󣬻�ȡ��ǰ���Ե���������ֵ
			for (int j = 0; j < tempInstances; j++) {
				candidateValueSets[j] = paraRoughDecisionSystem.instance(j).value(i);
			}//Of for j
			
			// Step 1.2 �Ե�ǰ���Ե�����ֵ��������ȥ��
			sortedValueSets = SimpleTool.compressAndSortDoubleArray
					(candidateValueSets, candidateValueSets.length);
			
			// SimpleTool.printDoubleArray(sortedValueSets);
			
			// Step 1.3 ���㵱ǰ�����µ�����ֵ
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
 		// System.out.println("������ֵ�б�");
 		// SimpleTool.printDoubleMatrix(valueRangeOfAttributes);
		// tempCurrentScalingData��ά���������洢��չ��ľ��߱���Ϣ
	
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
		// ͳ��ÿ�����Ե�����ֵ�������Լ��ܵ�����ֵ�����͡�
		for (int i = 0; i < numAttributes() - 1; i++) {
			numValuesOfAttribute[i] = attribute(i).numValues();
			totalNumValues += numValuesOfAttribute[i];
		}// Of for i
			// tempCurrentScalingData��ά���������洢��չ��ľ��߱���Ϣ
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
	 * ����ÿ��ԭʼ��������չ���߱��еĿ�ʼ������������������������
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
	 * ���㵥��ԭʼ��������չ���߱��еĿ�ʼ������������������������
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
	 * ����Լ�򼯺ϣ������µ�����ֵ������
	 * 
	 ********************************** 
	 */
	public void computeNumOfAttributeValue(boolean[] paraOptimalReduct){
		beginAndEndIndexOfAttribute = new int[numValuesOfAttribute.length][2];

		// Step 1. ����ÿһ��ԭʼ��������չ���Ծ��߱��е���ʼ�����±�
		computeBeginIndexAndEndIndexOfAttribute();
	
		int[] tempFalseNumberOfAttribute = new int[numValuesOfAttribute.length];
		
		//Step 2. ����Լ�򼯺�P������ÿһ��ԭʼ���Ե�����ֵ�����Լ��ܵ�����ֵ����
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
	 * ����Լ�򼯺ϣ������µ�����ֵ�������������ԡ�
	 * 
	 ********************************** 
	 */
	public void computeNumOfAttributeValue(boolean[] paraOptimalReduct, int paraAttribute){
		
		//System.out.println("test1");
		// Step 1. ����ÿһ��ԭʼ��������չ���Ծ��߱��е���ʼ�����±�
		int tempNumOfAttributeValue = 0;
		computeBeginIndexAndEndIndexOfAttribute(paraAttribute);
	
		int  tempFalseNumberOfAttribute = 0;

		//Step 2. ����Լ�򼯺�P������ÿһ��ԭʼ���Ե�����ֵ�����Լ��ܵ�����ֵ����
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
	 * ����Լ�򼯺ϣ����¼���õ��µľ��߱���Ϣ��
	 * 
	 * @return ���º�ľ��߱���Ϣ.
	 ********************************** 
	 */
	public int[][] computeNewDataByOptimalReduct(int[][] pareMatrixData, boolean[] paraOptimalReduct) {
		
		
		// �洢���º����Ϣ
		int[][] tempNewDataAfterReduct;

		tempNewDataAfterReduct = new int[pareMatrixData.length][totalNumValues + 1];
		int tempIndexOfNewData = 0;
		
		// Step 4. �����µľ��߱�S*��ֱ��ɾ��Լ�򼯺�P��Ϊfalse�����Լ������
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
		// Step 5. ���ظ��º�ľ��߱�
		return tempNewDataAfterReduct;
	}// Of computeNewDataByOptimalReduct

	/**
	 ********************************** 
	 * ����Լ�򼯺ϣ����¼���õ��µľ��߱���Ϣ��
	 * 
	 * @return ���º�ľ��߱���Ϣ.
	 ********************************** 
	 */
	public int[][] computeNewDataByReduct(int[][] pareMatrixData, boolean[] paraOptimalReduct) {
		
		
		// �洢���º����Ϣ
		int[][] tempNewDataAfterReduct = new int[pareMatrixData.length][paraOptimalReduct.length+1];
		tempNewDataAfterReduct = SimpleTool.copyIntMatrix(pareMatrixData);
		
		// Step 4. �����µľ��߱�S*��ֱ��ɾ��Լ�򼯺�P��Ϊfalse�����Լ������
		for (int i = 0; i < paraOptimalReduct.length; i++) {
			
			if(paraOptimalReduct[i] == true){
				continue;
			}//Of if
			
			for (int j = 0; j < pareMatrixData.length; j++) {
				tempNewDataAfterReduct[j][i] = 0;
			}// Of for j
		}// Of for i
		// SimpleTool.printIntMatrix(tempNewDataAfterReduct);
		// Step 5. ���ظ��º�ľ��߱�
		return tempNewDataAfterReduct;
	}// Of computeNewDataByOptimalReduct
	
	/**
	 ********************************** 
	 * ����Լ�򼯺ϣ����¼���õ��µľ��߱���Ϣ���������ԣ���
	 * 
	 * @return ���º�ľ��߱���Ϣ.
	 ********************************** 
	 */
	public int[][] computeNewDataByOptimalReduct(int[][] pareMatrixData, boolean[] paraOptimalReduct, 
			int paraAttribute,int paraAttributeValueNum) {
		
		
		// �洢���º����Ϣ
		int[][] tempNewDataAfterReduct;

		tempNewDataAfterReduct = new int[pareMatrixData.length][paraAttributeValueNum + 1];
		int tempIndexOfNewData = 0;
		
		// Step 4. �����µľ��߱�S*��ֱ��ɾ��Լ�򼯺�P��Ϊfalse�����Լ������
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
		// Step 5. ���ظ��º�ľ��߱�
		return tempNewDataAfterReduct;
	}// Of computeNewDataByOptimalReduct
	
	/**
	 ********************************** 
	 * ����Լ�򼯺ϣ�����M���������Ϣ��
	 * 
	 * @return ���º��M���Լ������.
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
			// System.out.println("�Ե�" + i + "�����Խ��и��£���");
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
					// System.out.println("��ǰ����ֵ�����кϲ�");
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
		System.out.println("����MԼ�������");
		for (int i = 0; i < tempMrelativeReduct.length; i++) {
			System.out.println("tempMrelativeReduct[" + i + "] = "
					+ tempMrelativeReduct[i]);
		}

		/*
		 * //ͳ��ÿ�����Ե�����ֵ�������Լ��ܵ�����ֵ�����͡� for(int i = 0; i<
		 * numValuesOfAttribute.length; i++){ if(candidateMergeIndex[i][0] <
		 * Integer.MAX_VALUE & candidateMergeIndex[i][1] < Integer.MAX_VALUE)
		 * numValuesOfAttribute[i]--; totalNumValues--; }
		 * 
		 * System.out.println("����ÿ�����Ե�����ֵ������"); for(int i = 0; i<
		 * numValuesOfAttribute.length; i++){
		 * System.out.println("numValuesOfAttribute[" + i + "] = " +
		 * numValuesOfAttribute[i]); }
		 */

		return tempMrelativeReduct;
	}// Of updataDataInfor

	/**
	 ********************************** 
	 * ��������ת����long�����ݡ�
	 * 
	 * @return long������.
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
	 * ��������Լ�򷽰�������ԭʼ���߱���Ϣ��
	 * 
	 * @param paraMatrix
	 *            ԭ���߱���Ϣ
	 * @param paraScaledDecisionSystem
	 *            ���º����չ���߱�S*
	 * @return ����Լ�򷽰����º�ľ��߱���Ϣ.
	 ********************************** 
	 */
	public int[][] computeNewDecisionTable(int[][] paraMatrix,
											int[][] paraScaledDecisionSystem) {
		int tempInstances = paraMatrix.length;
		int[][] tempCurrentNewData = new int[tempInstances][numAttributes()];
		// System.out.println("numAttributes()="+numAttributes());
		// System.out.println("beginAndEndIndexOfAttribute.length="+beginAndEndIndexOfAttribute.length);
	
		// Step 1. ���¼�����չ���Ե��±귶Χ
		computeBeginIndexAndEndIndexOfAttribute();
		
		// Step 2. ���ݸ��º����չ���߱�����õ��µľ��߱���Ϣ
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
				// Step 2.1 ���ڸ��º����չ���߱�ͳ��ÿһ����ÿһ����չ���Ե�����ֵΪ1�����Ը���
				for (int k = beginAndEndIndexOfAttribute[j][0]; 
						k < beginAndEndIndexOfAttribute[j][1]; k++) {
					// System.out.println("k= " + k);
					if(paraScaledDecisionSystem[i][k] == 0){
						continue;
					}//Of if
					countOneNumber++;
				}// Of for k
				// Step 2.2 ����ֵΪ1�����Ը�������Ϊ�¾��߱�ԭʼ���Ե�����ֵ
				tempCurrentNewData[i][j] = countOneNumber;
			}// Of for j
			
			// Step 2.3 �������Ե�����ֵ
			tempCurrentNewData[i][numAttributes()-1] = 
					(int) paraMatrix[i][numAttributes()-1];
		}// Of for i

		//Step 3. ���ظ��º��µľ��߱�
		return tempCurrentNewData;
	}// OfcomputeNewDecisionTable

	/**
	 ********************************** 
	 * ��������Լ�򷽰�������ԭʼ���߱���Ϣ���������ԣ�,�����������߱�
	 * 
	 * @param paraMatrix
	 *            ԭ���߱���Ϣ
	 * @param paraScaledDecisionSystem
	 *            ���º����չ���߱�S*
	 * @param paraAttribute
	 * 			��ǰ����
	 * @param paraValueRangeOfAttribute
	 * 			��ǰ���Ե�����ֵ�б�
	 * @param paraNumOfDis
	 * 			��ǰ�Ѿ���ɢ�������Եĸ���
	 * @return ����Լ�򷽰����º�ľ��߱���Ϣ.
	 ********************************** 
	 */
	public int[][] computeNewDecisionTable(int[][] paraMatrix,
											int[][] paraScaledDecisionSystem, 
											int paraAttribute,
											double[] paraValueRangeOfAttribute) {
		int tempInstances = paraMatrix.length;
		
		System.out.println("tempInstances = " + tempInstances);
		int[][] tempCurrentNewData = paraMatrix;
		System.out.println("����ԭ���߱�ǰ�ľ��߱���Ϣ��");
		SimpleTool.printIntMatrix(tempCurrentNewData);
		
		// Step 2. ���ݸ��º����չ���߱�����õ��µľ��߱���Ϣ
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
				// Step 2.1 ���ڸ��º����չ���߱�ͳ��ÿһ����ÿһ����չ���Ե�����ֵΪ1�����Ը���
				for (int k = 0; 
						k < paraValueRangeOfAttribute.length; k++) {
					if(paraScaledDecisionSystem[i][k] == 0){
						continue;
					}//Of if
					countOneNumber++;
				}// Of for k
				// Step 2.2 ����ֵΪ1�����Ը�������Ϊ�¾��߱�ԭʼ���Ե�����ֵ
				tempCurrentNewData[i][j] = countOneNumber;
			}// Of for j
			
		}// Of for i
		//Step 3. ���ظ��º��µľ��߱�
		return tempCurrentNewData;
	}// OfcomputeNewDecisionTable
	
	/**
	 ********************************** 
	 * ��������Լ�򷽰�������ԭʼ���߱���Ϣ���������ԣ�,���ص�ǰ���Զ�Ӧ�ľ��߱�
	 * 
	 * @param paraMatrix
	 *            ԭ���߱���Ϣ
	 * @param paraScaledDecisionSystem
	 *            ���º����չ���߱�S*
	 * @param paraAttribute
	 *            ��ǰ����
	 * @param paraValueRangeOfAttribute
	 *            ��ǰ���Ե�����ֵ�б�
	 * @return ����Լ�򷽰����º�ľ��߱���Ϣ.
	 ********************************** 
	 */
	public int[][] computeNewDecisionTableOneAttribute(double[][] paraMatrix,
			int[][] paraScaledDecisionSystem, int paraAttribute,
			double[] paraValueRangeOfAttribute) {
		int tempInstances = paraMatrix.length;
		int[][] tempCurrentNewData = new int[tempInstances][2];
		// Step 2. ���ݸ��º����չ���߱�����õ��µľ��߱���Ϣ
		for (int i = 0; i < tempInstances; i++) {
			if (paraValueRangeOfAttribute.length == 1
					&& paraValueRangeOfAttribute[0] == Double.MAX_VALUE) {
				tempCurrentNewData[i][0] = 0;
				continue;
			}// Of if

			int countOneNumber = 0;
			// Step 2.1 ���ڸ��º����չ���߱�ͳ��ÿһ����ÿһ����չ���Ե�����ֵΪ1�����Ը���
			for (int k = 0; k < paraValueRangeOfAttribute.length; k++) {
				// System.out.println("k= " + k);
				if (paraScaledDecisionSystem[i][k] == 0) {
					continue;
				}// Of if
				countOneNumber++;
			}// Of for k
				// Step 2.2 ����ֵΪ1�����Ը�������Ϊ�¾��߱�ԭʼ���Ե�����ֵ
			tempCurrentNewData[i][0] = countOneNumber;
			// }// Of for j

			// Step 2.3 �������Ե�����ֵ
			tempCurrentNewData[i][1] = (int) paraMatrix[i][numAttributes() - 1];
		}// Of for i
			// Step 3. ���ظ��º��µľ��߱�
		return tempCurrentNewData;
	}// OfcomputeNewDecisionTable
	
	/**
	 ********************************** 
	 * ���ݶ�ά����洢�ɾ��߱�
	 * 
	 * @param paraMatrix
	 *            ���º�ľ��߱���Ϣ
	 * @return �µľ���ϵͳ.
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
	 * ���ݶ�ά����洢�ɾ��߱�
	 * 
	 * @param paraCurrentDS
	 *            ��ԭʼ�ľ��߱�
	 * @param paraMatrix
	 *            ���º�ľ��߱���Ϣ
	 * @return �µľ���ϵͳ.
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
	 * ����ÿ��ԭʼ����δ����Լ��ĸ������������Ƿ��������Լ��
	 * 
	 * @param paraUnReductAttribute
	 *            ��չ���Ե�Լ��״̬
	 * @return �Ƿ������һ��Լ��.
	 ********************************** 
	 */
	public boolean computewhetherConinueReduct(boolean[] paraUnReductAttribute) {

		for (int i = 0; i < numValuesOfAttribute.length; i++) {
			int counteOfUnReductAttribute = 0;
			int beginIndex = 0;
			int endIndex = 0;
			// System.out.println("�Ե�" + i + "�����Խ��и��£���");
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
					// System.out.println("��ǰ����ֵ�����кϲ�");
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
	 * �����ȵļ������洢���ļ���.
	 * 
	 * @param Accuracy
	 *            Ҫ�洢�ľ���ֵ
	 * @param paraStorePath
	 *            �洢��·��
	 * @author Liu-Ying Wen 2014/12/8
	 ********************************** 
	 */
	public void toStoreAccuracyInFile(double paraAccuracy, String paraStorePath) {
		byte[] buff = new byte[] {};
		try {
			String tempWriteString = " ";

			// ��double�͵ľ���ֵת�����ַ�������
			tempWriteString = Double.toString(paraAccuracy);
			tempWriteString += "\r\n";
			// ���ַ����͵ľ���ֵת�����ֽ�
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
	 * �����ȵļ������洢���ļ���.
	 * 
	 * @param Accuracy
	 *            Ҫ�洢�ľ���ֵ
	 * @param paraStorePath
	 *            �洢��·��
	 * @author Liu-Ying Wen 2014/12/8
	 ********************************** 
	 */
	public void toStoreDeviatioLine( String paraStorePath) {
		byte[] buff = new byte[] {};
		try {
			String tempWriteString = " ";
			tempWriteString += "\r\n---------------------------\r\n";
			// ���ַ����͵ľ���ֵת�����ֽ�
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
		System.out.println("��ѡ����������");
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
		System.out.println("���������");
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
		System.out.println("�ϲ�����������ѡ�����Ժ���������");
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
		System.out.println("��ǰԼ��Ľ��");
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
		// System.out.println("��ʼʱ����Ϣ�� = " + totalEntropy);

		totalEntropy = ConditionalEntropy.conditionalEntropy(paraMatrix);
		// System.out.println("���µķ����������Ϣ��Ϊ:" + totalEntropy);
		
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
				//System.out.println("��ǰ����" + i + "����Ϣ�� = " + currentEntropy);
				if (currentBestEntropy > currentEntropy) {
					currentBestEntropy = currentEntropy;
					currentBestAttribute = i;
				}
				// Set back
				currentSelectedAttributes[i] = false;
			}// Of for i
			// System.out.println("��ǰѡ�������Ϊ" + currentBestAttribute + ",�����Ϣ�� = " +currentBestEntropy );
				// Really add it
			currentSelectedAttributes[currentBestAttribute] = true;
			tempReductMatrix  = computeNewDataByReduct(paraMatrix,currentSelectedAttributes);
			currentEntropy =  ConditionalEntropy.conditionalEntropy(tempReductMatrix);
			//currentEntropy = conditionalEntropy(currentSelectedAttributes);
			// System.out.println("����������Ժ����Ϣ�� = " + currentEntropy);
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
	 * ����ȫԼ��ֻѡ��ǰ����strong cuts.
	 * @param paraK
	 * 			����strong cuts�ĸ���
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
		//System.out.println("���µķ����������Ϣ��Ϊ:" + totalEntropy);
	
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
		System.out.println("ԭ���߱�");
		SimpleTool.printIntMatrix(paraMatrix);
		*/
		int[][] tempReductMatrix  = SimpleTool.copyIntMatrix(paraMatrix);
		/*
		System.out.println("��������ѡ���ľ��߱�");
		SimpleTool.printIntMatrix(tempReductMatrix);
		*/
		if (numberOfCoreAttributes == 0) {
			currentEntropy = 100;
		} else {
			// currentEntropy = conditionalEntropy(currentSelectedAttributes);
			// System.out.println("�ɷ�����Ϣ��:" + currentEntropy);
			currentEntropy = ConditionalEntropy.conditionalEntropy(tempReductMatrix);
			// System.out.println("�·�����Ϣ��:" + currentEntropy);
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
				System.out.println("ԭ���߱�");
				SimpleTool.printIntMatrix(paraMatrix);
				*/
				int[][] tempMatrix  = computeNewDataByReduct(paraMatrix,currentSelectedAttributes);
				// System.out.println("��������ѡ���ľ��߱�");
				// SimpleTool.printIntMatrix(tempMatrix);
				
				// currentEntropy = conditionalEntropy(currentSelectedAttributes);
				// System.out.println("�ɷ�����Ϣ��:" + currentEntropy);
				currentEntropy = ConditionalEntropy.conditionalEntropy(tempMatrix);
				// System.out.println("�·�����Ϣ��:" + currentEntropy);
				
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
			System.out.println("������ʼ����@@@@@@");
			System.out.println("ԭ���߱�");
			SimpleTool.printIntMatrix(paraMatrix);
			
			currentEntropy = conditionalEntropy(currentSelectedAttributes);
			System.out.println("�ɷ�����Ϣ��:" + currentEntropy);
			*/
			// currentEntropy = conditionalEntropy(currentSelectedAttributes);
			tempReductMatrix  = computeNewDataByReduct(paraMatrix,currentSelectedAttributes);
			currentEntropy =  ConditionalEntropy.conditionalEntropy(tempReductMatrix);
			// System.out.println("�·�����Ϣ��:" + currentEntropy);
			
			// System.out.println("�¾��߱�");
			// SimpleTool.printIntMatrix(tempReductMatrix);
			
			//����ǰ��Ϣ�����Ϣ��=0������ǰ����Լ��
			if(Math.abs(currentEntropy - totalEntropy) < 1e-6){
				break;
			}
			endTime = new Date().getTime();
			runTimeInformation = "�����������Ե���Ϣ�ػ��ѵ�ʱ��: "
					+ (endTime - startTime) + " ms.";
			System.out.println("Լ������ʱ��\r\n " + runTimeInformation);
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
	 * ����ȫԼ��ֻѡ��ǰk��strong attributes.
	 * @param paraK
	 * 			����strong attributes�ĸ���
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

		//���������Ե���Ϣ�ؽ������򣬲���¼ԭʼ�����±�i
		double[][] sortConditionalEntropy = SimpleTool.sortDoubleArray(currentEntropy);
		
		//��ǰk��������Ϣ�ض�Ӧ������i��������
		for(int i = 0; i < paraK; i++){
			if(i < sortConditionalEntropy[0].length)
				currentSelectedAttributes[(int)sortConditionalEntropy[1][i]] = true;
		}//end for i
			
		// endTime = new Date().getTime();
		// runTimeInformation = "����������չ���Ե���Ϣ�ػ��ѵ�ʱ��: "+ (endTime - startTime) + " ms.";
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
	 * ����ȫԼ��ֻѡ��ǰ����strong cuts.
	 * @param paraK
	 * 			����strong cuts�ĸ���
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

		//���������Ե���Ϣ�ؽ������򣬲���¼ԭʼ�����±�i
		double[][] sortConditionalEntropy = SimpleTool.sortDoubleArray(currentEntropy);
		
		//��ǰk��������Ϣ�ض�Ӧ������i��������
		for(int i = 0; i < paraK; i++){
			if(i < sortConditionalEntropy[0].length)
				currentSelectedAttributes[(int)sortConditionalEntropy[1][i]] = true;
		}//end for i
			
		endTime = new Date().getTime();
		runTimeInformation = "����������չ���Ե���Ϣ�ػ��ѵ�ʱ��: "+ (endTime - startTime) + " ms.";
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
	 * ����ȫԼ��ֻѡ��ǰ����strong cuts.
	 * @param paraK
	 * 			����strong cuts�ĸ���
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
		System.out.println("������Ϊ��");
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
		// System.out.println("Լ����");
		
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
				// System.out.println("���Ե�����ֵ����ά0");
			}//Of if
			//System.out.println("���Ե�����ֵ����Ϊ" + numValuesOfAttribute[paraAttribute]);
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
	 * ����Ϣ����������ai��ɢ������м��뵽S*�С�
	 * 
	 * @param paraDecisionSystem
	 *            ԭ���߱���Ϣ
	 * @param paraScaledDS
	 *            ����ai����չ���߱����º�ģ�
	 * @param paraAttribute
	 *            ��ǰ����
	 * @param paraBestCut
	 *            ��ǰ����ai�����CUT��
	 * @param paraDiscretizationDS
	 * 			  ��ɢ������ܱ�S*
	 * @param paraNumOfDis
	 * 			   ����ɢ�������Ը���
	 ********************************** 
	 */
	public void addAttributeToDiscretizationDS(double[][] paraDecisionSystem,
			int[][] paraScaledDS, int paraAttribute, double[] paraBestCut,
			int[][] paraDiscretizationDS){
		int tempInstances = paraDecisionSystem.length;
		// Step 1. ���ݸ��º����չ���߱�����õ��µľ��߱���Ϣ
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
				// Step 2.1 ���ڸ��º����չ���߱�ͳ��ÿһ����ÿһ����չ���Ե�����ֵΪ1�����Ը���
				for (int k = 0; k < paraBestCut.length; k++) {
					if(paraScaledDS[i][k] == 0){
						continue;
					}//Of if
					countOneNumber++;
				}// Of for k
				// Step 2.2 ����ֵΪ1�����Ը�������Ϊ�¾��߱�ԭʼ���Ե�����ֵ
				paraDiscretizationDS[i][paraAttribute] = countOneNumber;
			}// Of for j
			// Step 2.3 �������Ե�����ֵ
			paraDiscretizationDS[i][numAttributes()-1] = 
					(int) paraDecisionSystem[i][numAttributes()-1];
		}// Of for i	
	}//end of addAttributeToDiscretizationDS
	
	/**
	 ********************************** 
	 * �Ծֲ���ɢ�������Ϣ�����ȫ����ɢ���С�
	 * 
	 * @param paraLocalDisDS
	 *            �ֲ���ɢ����ľ��߱�
	 * @param paraDSType
	 *            ���ݼ����ͣ�ѵ����OR���Լ���
	 * @return ȫ����ɢ�������Ϣ��
	 * @throws Exception
	 ********************************** 
	 */
	public int[][] globalDiscretization(
			NewLocalDisPlusGlobalDisFastVersion2 paraLocalDisDS,
			String paraDSType,int paraTopKOfAttribute) throws Exception {

		
		int[][] tempGlobalDisDS = new int[paraLocalDisDS.numInstances()][paraLocalDisDS
				.numAttributes()];

		// Step 1. �����߱���Ϣ�洢��һ����ά�����У����ڴ���
		int[][] tempReductedDecisionSystem = new int[paraLocalDisDS
				.numInstances()][paraLocalDisDS.numAttributes()];
		for (int i = 0; i < paraLocalDisDS.numInstances(); i++) {
			for (int j = 0; j < paraLocalDisDS.numAttributes(); j++) {
				tempReductedDecisionSystem[i][j] = (int)paraLocalDisDS.instance(i)
						.value(j);
			}// Of for j
		}// Of for i
			// Step 2. ����ѵ�����������ÿ�������µ�����ֵ
		if (paraDSType.equals("train")) {
			statusOfAttribute = new boolean[paraLocalDisDS.numberOfConditions];
			statusOfAttribute = paraLocalDisDS.findTopKAtrributes(tempReductedDecisionSystem,paraTopKOfAttribute);
			// System.out.println("----------���Ե�Top20ѡ��״̬----------");
			// SimpleTool.printBooleanArray(statusOfAttribute);
			
			//paraLocalDisDS.computeNewAttribute(paraLocalDisDS);
			paraLocalDisDS.computeNewAttributeTopK(paraLocalDisDS,statusOfAttribute);
			
		}// end if
		// System.out.println("------���Ե�ѡ��״̬-------");
		// SimpleTool.printBooleanArray(statusOfAttribute);
		
		// System.out.println("------���ԵĶϵ���Ϣ-------");
		// SimpleTool.printDoubleMatrix(valueRangeOfAttributes);
		
		
		
		// Step 3. ȫ��Scaling��Ϣ�����洢
		NewLocalDisPlusGlobalDisFastVersion2 scalingDecisionSystem = null;
		int[][] tempScaledDecisionSystem = paraLocalDisDS
				.scalingDecisionSystem(paraLocalDisDS);
		// System.out.println("�����洢scaling��Ķ�ά�������ݵ�һ�ž��߱��У�");
		scalingDecisionSystem = paraLocalDisDS.writeInFile(
				tempScaledDecisionSystem, "global_scaling");

		
		
		
		// Step 4. ����ѵ����������Ҫ������Լ�õ�cut��P
		if (paraDSType.equals("train")) {
			
			boolean[] tempSelectedAttributeByBoolean = new boolean[totalNumValues];
			reductResultsOfGlobal = new boolean[totalNumValues];
			for (int i = 0; i < totalNumValues; i++)
				tempSelectedAttributeByBoolean[i] = false;

			// Step 4.1 ��scaling��ľ��߱����Լ��
			// System.out.println("-----��ȫ��scaling��ľ��߱����Լ��------");
			reductResultsOfGlobal = scalingDecisionSystem
					 .entropyBasedReduction();
			// reductResultsOfGlobal = scalingDecisionSystem.entropyBasedReductionFast();
			// System.out.println("-----��ȫ��scaling��ľ��߱����Լ�����-----");
			
			// Step 4.2 ����Լ���������¼���õ��µ�����ֵ�б�
			paraLocalDisDS.computeNumOfAttributeValue(reductResultsOfGlobal);
			// Step 4.3 ����Լ����¾��߱���ڵĶ�ά����tempScaledDecisionSystem
			// System.out.println("------����Լ�������չ����¾��߱�ʼ-----");
			tempScaledDecisionSystem = paraLocalDisDS
					.computeNewDataByOptimalReduct(tempScaledDecisionSystem,reductResultsOfGlobal);
			// System.out.println("------����Լ�������չ����¾��߱����-----");

			// Step 4.3 ���������µ�����ֵ�б�����б��൱����P
			paraLocalDisDS.computeNewValueRangeOfAttributes(reductResultsOfGlobal);
		}// end if

		// Step 5. ��Լ���Ķ�ά����洢��һ���µľ��߱�reductedDecisionSystem��
		// System.out.println("��Լ���Ķ�ά����洢��һ���µľ��߱��У�");
		NewLocalDisPlusGlobalDisFastVersion2 reductedDecisionSystem = scalingDecisionSystem
				.writeInFile(tempScaledDecisionSystem, "train");

		// System.out.println("����ԭʼ���߱�ʼ");
		// Step 6.
		// ��������Լ�򷽰�tempMrelativeReduct������ԭʼ���߱�Ķ�ά����tempReductedDecisionSystem��Ϣ
		tempGlobalDisDS = paraLocalDisDS.computeNewDecisionTable(
				tempReductedDecisionSystem, tempScaledDecisionSystem);
		// System.out.println("����ԭʼ���߱����");
/*
		// Step 7. �洢���ľ��߱���Ϣ
		NewLocalDisPlusGlobalDisFast trainSystem = paraLocalDisDS
				.restoreToArff(paraLocalDisDS, tempGlobalDisDS, "globalDS");
		*/
		return tempGlobalDisDS;
	}// end of globalDiscretization
	
	/**
	 ********************************** 
	 * �Ծֲ���ɢ�������Ϣ�����ȫ����ɢ���С�
	 * 
	 * @param paraLocalDisDS
	 *            �ֲ���ɢ����ľ��߱�
	 * @param paraDSType
	 *            ���ݼ����ͣ�ѵ����OR���Լ���
	 * @return ȫ����ɢ�������Ϣ��
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
			
			// Step 1. �����߱���Ϣ�洢��һ����ά�����У����ڴ���
			int[][] tempReductedDecisionSystem = SimpleTool.copyIntMatrix(paraDSByLocalDiscretization);
			
	
			// System.out.println("��Ϣ��");
			// SimpleTool.printIntMatrix(tempReductedDecisionSystem);
			
			
			// Step 2. �����ѵ����������Ҫ���������µ�����ֵ�б�
			if (paraTypeOfDS.equals("train")) {
				// ����ѵ������ȡÿ�������µ�����ֵ���������µ�����ֵ������һ���µ���Ϣ���洢��һ����ά����tempScaledDecisionSystem��
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
			// Step 3. ����ѵ��������ѵ�����е�ÿ�����ԣ������ʼ�Ŀ���Ϣ
			if (paraTypeOfDS.equals("train")){
				int cutBlockIndex = 0;
				for (int i = 0; i < paraOriginalDS.numAttributes() - 1; i++) {
					System.out.println("-----------------------------");
					System.out.println("������" + i + "���п���Ϣ�ļ���");
					//Step 3.1. �����µ�����ֵ
					
					paraOriginalDS.computeNewAttribtueWithSort(paraOriginalDS, i, paraCutsNum);
					//blockInfor[i]  = new CutBlock[valueRangeOfAttributes[i].length][][] ;
					reductResults[i] = new boolean[valueRangeOfAttributes[i].length];
				
					//Step 3.2. ��������Ŀ���Ϣ
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
						
						//Step 3.3. �����������Ϣ����Ϣ�أ�ѡ����Ϣ����С�Ŀ���뵽P
						
						currentEntropy = blockEntropyForCutBlock(blockInfor[cutBlockIndex],paraOriginalDS.numInstances());
						// System.out.println("����" + i + "�ĵ�" + j + "���ϵ����Ϣ�� = " + currentEntropy);
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
				//����Ϣ����С�Ŀ���Ϣ���뵽P
				cutsBlockInfor =  blockInfor[bestBlockIndex];
				reductResults[bestCutAttribute][bestCut] = true;
				//selectedCutIndex[bestCutAttribute][0] = bestCut;
					
				// SimpleTool.printBooleanArray(reductResults[bestCutAttribute]);
				System.out.println("��1��ѡ��Ķϵ���"+"����"+bestCutAttribute+"�ĵ�"+bestCut+"���ϵ�");
				CutBlock.printCutBlockInfor(cutsBlockInfor);
				//System.out.println("bestBlockIndex = " + bestBlockIndex);
				
				// Step 4. ����Ѱ�����cut��ֱ��Cut���Ŀ���Ϣ��=0	
				for(int roundTime = 2; roundTime < 18 ; roundTime++){
					miniEntropy = Double.MAX_VALUE;
					bestCut = 0; 
					bestCutAttribute = 0;
					CutBlock[][] bestBlockInfor = new CutBlock[2*cutsBlockInfor.length][paraOriginalDS.numClasses()];
					
					if(blockEntropyForCutBlock(cutsBlockInfor,paraOriginalDS.numInstances()) < 1e-6 )
						break;
					
					//Step 4.2 ����ÿһ�����Ϣ�أ����ҵ���С��
					for(int i = 0; i < blockInfor.length; i++){
						//for(int j = 0; j < valueRangeOfAttributes[i].length; j++ ){
						
							// System.out.println("��������Ŀ���Ϣ");
							// System.out.println("����" + blockInfor[i][0][0].attributeNum + "�ĵ�" + blockInfor[i][0][0].cutIndex+"���ϵ�");
							// CutBlock.printCutBlockInfor(blockInfor[i]);
							
							if(reductResults[blockInfor[i][0][0].attributeNum][blockInfor[i][0][0].cutIndex] == true)
								continue;
							//����õ��µĿ���Ϣ��
							CutBlock[][] tempBlockInfor = 
									computeNewBlockInfor(blockInfor[i], cutsBlockInfor);
							
							// System.out.println("����"+ i+ "��������Ŀ���ϢΪ");
							// CutBlock.printCutBlockInfor(tempBlockInfor);
							
							//����tempBlockInfor�Ŀ���Ϣ��
							currentEntropy = blockEntropyForCutBlock(tempBlockInfor,paraOriginalDS.numInstances());
							
							// System.out.println("���뵱ǰ������Ϣ�� = " + currentEntropy);
							
							if(currentEntropy < miniEntropy){
								miniEntropy = currentEntropy;
								bestCut = blockInfor[i][0][0].cutIndex;
								bestCutAttribute = blockInfor[i][0][0].attributeNum;
								bestBlockInfor = tempBlockInfor;
							}//end if
						//}//end for j
					}//end for i
					
					
					//ѹ��block��Ϣ��ȥ��ȫΪ0���У�������
					
					CutBlock[][] bestBlockInforAfterCompress = compressBlockInfor(bestBlockInfor);
					
					
					//Step 4.3 ����ÿһ�����Ϣ�أ����ҵ���С��
					cutsBlockInfor = bestBlockInforAfterCompress;
					// cutsBlockInfor = bestBlockInfor;
					reductResults[bestCutAttribute][bestCut] = true;
					System.out.println("��"+ roundTime+ "��ѡ��Ķϵ���"+"����"+bestCutAttribute+"�ĵ�"+bestCut+"���ϵ�");
					CutBlock.printCutBlockInfor(cutsBlockInfor);
					
					// System.out.println("��ǰ����Ϣ�� = " + blockEntropyForCutBlock(cutsBlockInfor,paraOriginalDS.numInstances()));
					// SimpleTool.printBooleanMatrix(reductResults);
				}//end for roundTime
			}//end if
			
			// SimpleTool.printBooleanMatrix(reductResults);
			
			//Step 5.����P�е�������ֵ�����¾��߱�
			for (int i = 0; i < paraOriginalDS.numAttributes() - 1; i++){
				int tempCustNum = 0;
				for(int j = 0; j< valueRangeOfAttributes[i].length; j++){
					if(reductResults[i][j] == true)
						tempCustNum++;
				}//end for j
				
				paraOriginalDS.updateDS(
						tempReductedDecisionSystem, reductResults[i], tempCustNum, i , discretizationDecisionSystem);
			}//end for i
			
			System.out.println("ȫ����ɢ����ľ��߱�");
			SimpleTool.printIntMatrix(discretizationDecisionSystem);
				
			// Step 6. ���ؾֲ���ɢ�������Ϣ��
			return discretizationDecisionSystem;
	}// end of globalDiscretization
	
	
	
	/**
	 ********************************** 
	 * ѹ��CutBlock���顣
	 * 
	 * @param paraCurrentBlockInfor
	 *            ѹ��ǰ������
	 * @return ѹ���������
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
			//ѹ����
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
	 * �ֲ���ɢ����Ϣ��
	 * 
	 * @param paraLocalDisDS
	 *            �ֲ���ɢ����ľ��߱�
	 * @param paraTypeOfDS
	 *            ���ݼ����ͣ�ѵ����OR���Լ���
	 * @return �ֲ���ɢ�������Ϣ��
	 * @throws Exception
	 ********************************** 
	 */
	public CutBlock[][] computeNewBlockInfor(
			CutBlock[][] paraCurrentBlock, 
			CutBlock[][] paraCurrentSelectedBlock){
		
		
/*
		System.out.println("����");
		CutBlock.printCutBlockInfor(paraCurrentBlock);
		System.out.println("�Ϳ�");
		CutBlock.printCutBlockInfor(paraCurrentSelectedBlock);
		System.out.println("���кϲ�");
		
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
	 * ��������������ͬ�Ķ�����Ŀ��
	 * 
	 * @param paraCurrentBlock
	 *            ��������Ŀ�
	 * @param paraCurrentSelectedBlock
	 *            �Ѿ�ѡ��Ŀ�
	 * @return ��ͬ�Ķ�����Ŀ
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
	 * �ֲ���ɢ����Ϣ��
	 * 
	 * @param paraLocalDisDS
	 *            �ֲ���ɢ����ľ��߱�
	 * @param paraTypeOfDS
	 *            ���ݼ����ͣ�ѵ����OR���Լ���
	 * @return �ֲ���ɢ�������Ϣ��
	 * @throws Exception
	 ********************************** 
	 */
	public int[][] localDiscretization(
			NewLocalDisPlusGlobalDisFastVersion2 paraOriginalDS,
			String paraTypeOfDS, int paraCutsNum) throws Exception {

		int[][] discretizationDecisionSystem = new int[paraOriginalDS
		                               				.numInstances()][paraOriginalDS.numAttributes()];
		
		// Step 1. �����߱���Ϣ�洢��һ����ά�����У����ڴ���
		double[][] tempReductedDecisionSystem = new double[paraOriginalDS.numInstances()][paraOriginalDS.numAttributes()];
		for (int i = 0; i < paraOriginalDS.numInstances(); i++) {
			for (int j = 0; j < paraOriginalDS.numAttributes(); j++) {
				tempReductedDecisionSystem[i][j] = paraOriginalDS.instance(i).value(j);
			}// Of for j
		}// Of for i
/*
		System.out.println("����ǰ����Ϣ��");
		SimpleTool.printDoubleMatrix(tempReductedDecisionSystem);
		*/
		
		// Step 2. �����ѵ����������Ҫ���������µ�����ֵ�б�
		if (paraTypeOfDS.equals("train")) {
			// ����ѵ������ȡÿ�������µ�����ֵ���������µ�����ֵ������һ���µ���Ϣ���洢��һ����ά����tempScaledDecisionSystem��
			numValuesOfAttribute = new int[paraOriginalDS.numAttributes() - 1];
			totalNumValues = 0;
			valueRangeOfAttributes=new double[paraOriginalDS.numAttributes()-1][];
			//beginAndEndIndexOfAttribute = new int[numValuesOfAttribute.length][2];
			reductResults = new boolean[numValuesOfAttribute.length][];	
		}// end if

		// Step 3. ��ÿ�����ԣ����оֲ���ɢ��
		for (int i = 0; i < paraOriginalDS.numAttributes() - 1; i++) {
			if (paraTypeOfDS.equals("train")){
				int[] selectedCutIndex = new int[paraCutsNum];
				// System.out.println("-----------------------------");
				// System.out.println("������" + i + "���оֲ���ɢ��");
				
				// Step 3.1. ���ݵ�ǰ���ԣ����ӱ�������򣬼����µ�����ֵ�б�
				double[][] tempSortedDS = paraOriginalDS.sortDSAndComputeNewAttribtue(paraOriginalDS, i);
				
				reductResults[i] = new boolean[valueRangeOfAttributes[i].length];
				int[][] cutsBlockInfor = new int[ paraCutsNum + 1 ][paraOriginalDS.numClasses() ];
				int[][][] blockInfor  = new int[valueRangeOfAttributes[i].length][][] ;
				/*
				System.out.println("�������ӱ���Ϣ��");
				SimpleTool.printDoubleMatrix(tempSortedDS);
				System.out.println("����" + i + "��������ֵ�б�");
				SimpleTool.printDoubleArray(valueRangeOfAttributes[i]);
				*/
				//Step 3.2. ��������Ŀ���Ϣ
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
				System.out.println("����Ϣ");
				for(int j = 0; j< valueRangeOfAttributes[i].length; j++ ){
					SimpleTool.printIntMatrix(blockInfor[j]);
				}//end for j
				 */
				//Step 3.3. �����������Ϣ����Ϣ�أ�ѡ����Ϣ����С�Ŀ���뵽P
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
				//����Ϣ����С�Ŀ���Ϣ���뵽P
				cutsBlockInfor =  blockInfor[bestCut];
				reductResults[i][bestCut] = true;
				selectedCutIndex[0] = bestCut;
				
				// SimpleTool.printBooleanArray(reductResults[i]);
				// System.out.println("��һ��ѡ��Ķϵ���"+bestCut+",ѡ���Ŀ���ϢΪ");
				// SimpleTool.printIntMatrix(cutsBlockInfor);
				
				//Step 3.4. ������P�м���ϵ㣬ֱ��|P| > paraCutsNum
				for(int j = 1; j < paraCutsNum; j++){
					//System.out.println("��" + (j+1) + "��ѡ��ϵ�");
					
					miniEntropy = Double.MAX_VALUE;
					bestCut = 0;
					int[][] bestBlockInfor = new int[j+2][paraOriginalDS.numClasses()];
					
					for(int k = 0; k< valueRangeOfAttributes[i].length; k++ ){
						if(reductResults[i][k] == true){
							// System.out.println("��" + k + "���ϵ��Ѿ���ѡ����");
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
							//Ѱ�Һ��ʵ�λ��
							for(int l = 0; l < j; l++){
								if(valueRangeOfAttributes[i][k] < valueRangeOfAttributes[i][l])
									break;
								positionOfShift++;
							}//end for l
						}//end else
						tempBlockInfor = cutShift(blockInfor[k], cutsBlockInfor, positionOfShift);
						/*
						System.out.println("����" + valueRangeOfAttributes[i][k] + "�ϵ��Ŀ���Ϣ");
						SimpleTool.printIntMatrix(tempBlockInfor);
						*/
						//����tempBlockInfor�Ŀ���Ϣ��
						double currentEntropy = blockEntropy(tempBlockInfor,paraOriginalDS.numInstances());
						// System.out.println("��" + k + "���ϵ��������Ϣ�� = " + currentEntropy);
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
					// System.out.println("��"+(j+1)+"��ѡ��Ķϵ���"+bestCut+",ѡ���Ŀ���ϢΪ");
					// SimpleTool.printIntMatrix(cutsBlockInfor);
					
				}//end for j
			}//end if
			
			//Step 3.5.����P�е�������ֵ�����¾��߱�
			paraOriginalDS.updateDS(
					tempReductedDecisionSystem, reductResults[i], paraCutsNum, i , discretizationDecisionSystem);
			/*
			System.out.println("��ɢ������" + i + "�����Ϣ��Ϊ");
			SimpleTool.printIntMatrix(discretizationDecisionSystem);
			*/
		}// end for i
		
		// Step 4. ���ؾֲ���ɢ�������Ϣ��
		return discretizationDecisionSystem;
	}// end of localDiscretization
	
	/**
	 ********************************** 
	 * ����cut�����¾��߱�
	 * @param paraDecisionSystem
	 * 			ԭ����ϵͳ
	 * @param paraSelectedCut
	 * 			�ϵ㼯��ѡ��״̬
	 * @param paraNewDecisionSystem
	 * 			�µ���ɢ�������Ϣϵͳ
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
			// Step 2.3 �������Ե�����ֵ
			paraNewDecisionSystem[i][numAttributes()-1] = 
								(int) paraDecisionSystem[i][numAttributes()-1];
		}//end for i
	}//end of updateDS
	
	
	
	/**
	 ********************************** 
	 * ����cut�����¾��߱�
	 * @param paraDecisionSystem
	 * 			ԭ����ϵͳ
	 * @param paraSelectedCut
	 * 			�ϵ㼯��ѡ��״̬
	 * @param paraNewDecisionSystem
	 * 			�µ���ɢ�������Ϣϵͳ
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
			// Step 2.3 �������Ե�����ֵ
			paraNewDecisionSystem[i][numAttributes()-1] = 
								 paraDecisionSystem[i][numAttributes()-1];
		}//end for i
	}//end of updateDS
	
	/**
	 ********************************** 
	 * �������Ϣ����Ϣ�ء�
	 * 
	 * @return ��Ϣ��.
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
			// System.out.println("��ǰ�����Ϣ�� = " + currentEntropy);
			return currentEntropy;
	}
	

	/**
	 ********************************** 
	 * �������Ϣ����Ϣ�ء�
	 * 
	 * @return ��Ϣ��.
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
			// System.out.println("��ǰ�����Ϣ�� = " + currentEntropy);
			return currentEntropy;
	}
	
	
	/**
	 ********************************** 
	 * ���ݸ����Ŀ���Ϣ���ƶ���λ�ã����¼����µĿ���Ϣ��
	 * 
	 * @return ���º�ľ��߱���Ϣ.
	 ********************************** 
	 */	
	public int[][] cutShift(int[][] paraBlockInfor, int[][] paraCutsBlockInfor, int paraPositionOfShift){
		/*
		System.out.println("����");
		SimpleTool.printIntMatrix(paraBlockInfor);
		System.out.println("���뵽��");
		SimpleTool.printIntMatrix(paraCutsBlockInfor);
		System.out.println("�ĵ�" + paraPositionOfShift + "��λ��");
	
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
	 * ����Լ�򼯺ϣ����¼���õ��µľ��߱���Ϣ��
	 * 
	 * @return ���º�ľ��߱���Ϣ.
	 ********************************** 
	 */
	public int[][] updateDSByOptimalReduct(int[][] pareMatrixData, boolean[] paraOptimalReduct) {
		// �洢���º����Ϣ
		int[][] tempNewDataAfterReduct;
		tempNewDataAfterReduct = SimpleTool.copyIntMatrix(pareMatrixData) ;
		
		// Step 4. �����µľ��߱�S*��ֱ��ɾ��Լ�򼯺�P��Ϊfalse�����Լ������
		for (int i = 0; i < paraOptimalReduct.length; i++) {
			
			if(paraOptimalReduct[i] == true){
				continue;
			}//Of if
			
			for(int j = 0; j < pareMatrixData.length; j++){
				tempNewDataAfterReduct[j][i] = 0;
			}//end for j
		}// Of for i
		// Step 5. ���ظ��º�ľ��߱�
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
		
		int topKOfAttribute = 10;//ȫ����ɢ��ʱѡ���������Ը���
		int tempCutsNum = 4;//�ֲ���ɢ��ѡ��Ķϵ���Ŀ
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
					
						
						//��������ļ�λ��
					File test = new File("./" + "NewResult/"+ tempCutsNum + "/" + decisionSystem.m_RelationName + "/" + decisionSystem.m_RelationName + "Folder" + i + "Output.txt");
					PrintStream out = new PrintStream(new FileOutputStream(test));
					System.setOut(out);
						
						
						// Step 1. �����ݼ��ָ�Ϊ10�ݣ�9����Ϊѵ����1����Ϊ���ԡ�
						NewLocalDisPlusGlobalDisFastVersion2[] subsets = decisionSystem.divideInTwo(0.9);

						NewLocalDisPlusGlobalDisFastVersion2 decisionSystemOfTrain = subsets[0];
						NewLocalDisPlusGlobalDisFastVersion2 decisionSystemOfTest = subsets[1];

						// tempInitialAccuracy = decisionSystem
						// .classifyTreeTest(decisionSystemOfTrain,
						// decisionSystemOfTest);

						// System.out.println("����ʼ");

						// Step 2.�ֲ���ɢ��
						startTime = new Date().getTime();
						// System.out.println("ѵ�������оֲ���ɢ����ʼ");

						// Step 2.1 ѵ�������оֲ���ɢ��
						int[][] tempDisDSOfTrain = decisionSystemOfTrain.localDiscretization(decisionSystemOfTrain,
								"train", tempCutsNum);
						// System.out.println("ѵ�����ֲ���ɢ�������Ϣ��");
						// SimpleTool.printIntMatrix(tempDisDSOfTrain);
						// System.out.println("ѵ�������оֲ���ɢ������");
						// System.out.println("------------------------");

						// Step 2.2 ���Լ����оֲ���ɢ��
						// System.out.println("���Լ����оֲ���ɢ����ʼ");
						int[][] tempDisDSOfTest = decisionSystemOfTest.localDiscretization(decisionSystemOfTest, "test",
								tempCutsNum);
						// System.out.println("���Լ��ֲ���ɢ�������Ϣ��");
						// SimpleTool.printIntMatrix(tempDisDSOfTest);
						// System.out.println("���Լ����оֲ���ɢ������");

						endTime = new Date().getTime();
						runTimeInformation = "Reduction cost time: " + (endTime - startTime) + " ms.";

						// System.out.println("�ֲ���ɢ��������ʱ��\r\n " +
						// runTimeInformation);

						long localRunningTime = endTime - startTime;
						decisionSystem.toStoreAccuracyInFile(localRunningTime,
								"./" + "NewResult/" + tempCutsNum + "/" + decisionSystem.m_RelationName
										+ "/localRunningTimeOf" + decisionSystem.m_RelationName + ".txt");

						/*
						 * System.out.println("�ֲ���ɢ�����ÿ�����Ե�cut��״̬��");
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

						// Step 3.ȫ����ɢ��
						startTime1 = new Date().getTime();
						// System.out.println("��ѵ��������ȫ����ɢ��");

						// Step 3.1 ѵ����ȫ����ɢ��
						int[][] globalDisDSOfTrain = localDisDSOfTrain.globalDiscretization(localDisDSOfTrain, "train",
								topKOfAttribute);
						// int[][] globalDisDSOfTrain =
						// localDisDSOfTrain.cutShiftGlobalDiscretization(localDisDSOfTrain,tempDisDSOfTrain,"train",tempCutsNum);

						// System.out.println("============================");
						// System.out.println("�Բ��Լ�����ȫ����ɢ��");
						// Step 3.2 ���Լ�ȫ����ɢ��
						int[][] globalDisDSOfTest = localDisDSOfTest.globalDiscretization(localDisDSOfTest, "test",
								topKOfAttribute);
						// int[][] globalDisDSOfTest =
						// localDisDSOfTest.cutShiftGlobalDiscretization(localDisDSOfTest,tempDisDSOfTest,"test",
						// tempCutsNum);

						// System.out.println("ȫ����ɢ�����ÿ�����Ե�cut��״̬��");
						// SimpleTool.printBooleanMatrix(reductResults);

						endTime = new Date().getTime();
						runTimeInformation = "Reduction cost time: " + (endTime - startTime1) + " ms.";

						// System.out.println("ȫ����ɢ��������ʱ��\r\n " +
						// runTimeInformation);

						long globalRunningTime = endTime - startTime1;
						decisionSystem.toStoreAccuracyInFile(globalRunningTime,
								"./" + "NewResult/" + tempCutsNum + "/" + decisionSystem.m_RelationName
										+ "/globalRunningTimeOf" + decisionSystem.m_RelationName + ".txt");

						// Step 4. �洢���ľ��߱���Ϣ
						NewLocalDisPlusGlobalDisFastVersion2 trainSystem = decisionSystem.restoreToArff(decisionSystem,
								globalDisDSOfTrain, "trainFolder" + i, tempCutsNum);
						NewLocalDisPlusGlobalDisFastVersion2 testSystem = decisionSystem.restoreToArff(decisionSystem,
								globalDisDSOfTest, "testFolder" + i, tempCutsNum);

						endTime = new Date().getTime();
						long totalRunningTime = endTime - startTime;
						decisionSystem.toStoreAccuracyInFile(totalRunningTime,
								"./" + "NewResult/" + tempCutsNum + "/" + decisionSystem.m_RelationName
										+ "/totalRunningTimeOf" + decisionSystem.m_RelationName + ".txt");

						// Step 5. ���Է��ྫ��

						double tempInitialAccuracy = 0;
						double tempFinalAccuracy = 0;
						double localDisAccuracy = 0;

						// ���߱�δ�����㷨����ķ��ྫ�ȼ��㼰�洢 tempInitialAccuracy =
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
