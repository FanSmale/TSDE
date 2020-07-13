package coser.classifiers.trees;

import weka.classifiers.Sourcable;
import weka.classifiers.trees.Id3;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.NoSupportForMissingValuesException;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformationHandler;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;

import coser.datamodel.decisionsystem.BothCostsNominalDecisionSystem;

/**
 * ����һ���������о���������Id3.java�и��Ʋ��޸Ķ�����<br>
 * Summary: Build a decision tree for a cost sensitive decision system. Modified
 * from Id3.java
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * For more information of the data model please refer to our papers: <br>
 * \@ARTICLE{MinZhu2012Induction,<br>
 * author = {Fan Min and William Zhu},<br>
 * title = {Induction of cost-time-sensitive decision trees},<br>
 * journal = {submitted to RSKT 2012},<br>
 * year = {2012}<br>
 * }
 * <p>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program.
 * <p>
 * Organization: <a href=http://grc.fj zs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Progress: Half done. <br>
 * Written time: November 5, 2011. <br>
 * Last modify time: January 28, 2011.
 */
public class CostSensitiveDecisionTree extends Id3 implements
		TechnicalInformationHandler, Sourcable {

	/**
	 * For serialization
	 */
	public static final long serialVersionUID = -2693678645136322561L;

	/**
	 * The dataset. It is valid only for the current node.<br>
	 * ���ݼ������Ե�ǰģʽ��Ч��
	 */
	private BothCostsNominalDecisionSystem bcsds;

	/**
	 * The ID3 algorithm. public static final int ID3 = 0;<br>
	 * ID3�㷨��
	 */

	/**
	 * The cost gain algorithm.<br>
	 * ���������㷨
	 */
	public static final int COST_GAIN = 0;

	/**
	 * The weighted test cost algorithm. When lambda = 0, it coincides with the
	 * ID3 algorithm; When lambda = -0.5, it coincides with the CS-ID3
	 * algorithm;. When lambda = -1, it coincides with the IDX algorithm;.<br>
	 * ��Ȩ���Դ����㷨��<br>
	 * ��������=0ʱ����ͬ��ID3�㷨��<br>
	 * ��������=-0.5ʱ����ͬ��CS-ID3�㷨��<br>
	 * ��������=-1ʱ����ͬ��IDX�㷨��
	 */
	public static final int WEIGHTED_GAIN = 1;

	/**
	 * The power information gain algorithm. Also known as the EG2 algorithm.<br>
	 * ��Ϣ�����㷨�����߱���ΪEG2�㷨��
	 */
	public static final int EXPONENTIAL_GAIN = 2;

	/**
	 * The weighted test cost algorithm.<br>
	 * ��Ȩ���Դ����㷨
	 */
	public static final int ALL_ALGORITHMS = 3;

	/**
	 * No prune.<br>
	 * �޼�֦
	 */
	public static final int NO_PRUNE = 0;

	/**
	 * Preprune.<br>
	 * Ԥ��֦
	 */
	public static final int PRE_PRUNE = 1;

	/**
	 * Postprune.<br>
	 * ���֦
	 */
	public static final int POST_PRUNE = 2;

	/**
	 * The node's successors.<br>
	 * ���ĺ��
	 */
	private CostSensitiveDecisionTree[] successors;

	/**
	 * Attribute used for splitting.<br>
	 * ���ڻ��ֵ�����
	 */
	private Attribute splitAttribute;

	/**
	 * Class value if node is leaf.<br>
	 * Ҷ�ӽ��ľ�����ֵ
	 */
	private double leafClassValue;

	/**
	 * Misclassification cost if node the is leaf.<br>
	 * Ҷ�ӽ�����������
	 */
	private double leafMisclassificationCost;

	/**
	 * Class distribution if node is leaf. It is defined as a double array to
	 * facilitate normalization.<br>
	 * Ҷ�ӽ��ľ�����ֲ�����������Ϊһ��˫���ȸ����������Ա�����̬����
	 */
	private double[] leafDistribution;

	/**
	 * Class attribute of dataset. Only used in specifying the class in leaves.
	 * By default it should be the last attribute in the decision system.<br>
	 * ���ݼ��������ԡ�
	 */
	private Attribute classAttribute;

	/**
	 * The accuracy after classification.<br>
	 * �����ľ�ȷ��
	 */
	protected double accuracy;

	/**
	 * The maximal cost gain. Used for postprune<br>
	 * ���������档���ں��֦��
	 */
	protected double maximalCostGain;

	/**
	 * The average cost after classfication.<br>
	 * ������ƽ������
	 */
	protected double averageCost;

	/**
	 * Whether or not balance according to the misclassification matrix.<br>
	 * �����������۾����Ƿ�ƽ��
	 */
	protected boolean balance;

	/**
	 ************************* 
	 * Construct the object.<br>
	 * �������
	 * 
	 * @param paraBalance
	 *            wether or not balance �Ƿ�ƽ��
	 ************************* 
	 */
	public CostSensitiveDecisionTree(BothCostsNominalDecisionSystem paraDs,
			boolean paraBalance) throws Exception {
		super();// In fact do nothing.
		bcsds = new BothCostsNominalDecisionSystem(paraDs);

		// Initialize anyway.
		successors = null;
		splitAttribute = null;
		leafClassValue = 0;
		leafMisclassificationCost = 0;
		classAttribute = paraDs.classAttribute();
		balance = paraBalance;
	}// Of the constructor

	/**
	 ************************* 
	 * Get a string describing the classifier. Use the same style of ID3<br>
	 * ���һ���������������ַ�����ʹ����ID3��ͬ�����͡�
	 * 
	 * @return a description suitable for the GUI.
	 ************************* 
	 */
	public String globalInfo() {
		return "Class for constructing an unpruned decision tree based on the CS-ID3 "
				+ "algorithm. Can only deal with nominal attributes. No missing values "
				+ "allowed. Empty leaves may result in unclassified instances. For more "
				+ "information see: \n\n"
				+ getTechnicalInformation().toString();
	}// Of globalInfo

	/**
	 ************************* 
	 * Set the balance variable.
	 ************************* 
	 */
	public void setBalance(boolean paraBalance) {
		balance = paraBalance;
	}// Of setBalance

	/**
	 ************************* 
	 * Gets an instance of a TechnicalInformation object, containing detailed
	 * information about the technical background of this class, e.g., paper
	 * reference or book this class is based on. Use the same style of ID3<br>
	 * 
	 * @return the technical information about this class.
	 ************************* 
	 */
	public TechnicalInformation getTechnicalInformation() {
		TechnicalInformation result;

		result = new TechnicalInformation(Type.ARTICLE);
		result.setValue(Field.AUTHOR, "Fan Min and William Zhu");
		result.setValue(Field.YEAR, "2012");
		result.setValue(Field.TITLE,
				"Induction of cost-sensitive decision trees");
		result.setValue(Field.JOURNAL, "submitted to Machine Learning");

		return result;
	}// Of getTechnicalInformation

	/**
	 ************************* 
	 * Set the decision system.<br>
	 * ���þ���ϵͳ
	 ************************* 
	 */
	public void setDecisionSystem(BothCostsNominalDecisionSystem paraDs) {
		bcsds = paraDs;
	}// Of setDecisionSystem

	/**
	 ************************* 
	 * Get the classification accuracy.<br>
	 * ��÷��ྫȷ��
	 ************************* 
	 */
	public double getAccuracy() {
		return accuracy;
	}// Of getAccuracy

	/**
	 ************************* 
	 * Get the average cost.<br>
	 * ���ƽ������
	 ************************* 
	 */
	public double getAverageCost() {
		return averageCost;
	}// Of getAverageCost

	/**
	 ************************* 
	 * Build the cost-sensitive decision tree using the specified algorithm and
	 * prune strategy.<br>
	 * ʹ��ָ���㷨�ͼ�֦���Թ���������о�����
	 * 
	 * @param paraAlgorithm
	 *            0 for cost gain, 1 for weighted cost gain
	 * @param paraLabmda
	 *            only valid while the algorithm is weighted cost gain
	 * @param paraPrune
	 *            the prune strategy, 0 for no prune, 1 for pre-prune, 2 for
	 *            post-prune
	 * @throws Exception
	 *             if classifier can't be built successfully
	 * @see #makeTree(int, double, boolean)
	 ************************* 
	 */
	public void buildClassifier(int paraAlgorithm, double paraLabmda,
			int paraPrune) throws Exception {
		// Can classifier handle the data?
		getCapabilities().testWithFail(bcsds);
		boolean preprune = false;
		if (paraPrune == PRE_PRUNE) {
			preprune = true;
		}// Of if

		boolean tempPostprune = false;
		if (paraPrune == POST_PRUNE) {
			tempPostprune = true;
		}// Of if
		try {
			makeTree(paraAlgorithm, paraLabmda, preprune);
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in CostSensitiveDecisionTree.makeTree()\r\n"
							+ ee);
		}// Of try

		if (tempPostprune) {
			// System.out.print("postprune\t");
			postprune();
		} else {
			// System.out.print("no prune\t");
		}// of if
	}// Of buildClassifier

	/**
	 ************************* 
	 * Method for building a CS-ID3 tree.<br>
	 * �����������ID3������
	 * 
	 * @param paraAlgorithm
	 *            0 for cost gain, 1 for weighted cost gain
	 * @param paraLabmda
	 *            the lambda value for the weighted gain algorithm, also the
	 *            omega value for the exponential gain algorithm.
	 * @param paraPreprune
	 *            preprune or not
	 * @exception Exception
	 *                if decision tree can't be built successfully
	 ************************* 
	 */
	private void makeTree(int paraAlgorithm, double paraLambda,
			boolean paraPreprune) throws Exception {
		// Check if no instances have reached this node.
		// ����������Ƿ�û��ʵ��
		if (bcsds.numInstances() == 0) {
			splitAttribute = null;
			leafMisclassificationCost = 0;
			leafClassValue = Instance.missingValue();
			return;
		}// Of if

		// Already pure, make a leaf.
		// �Ѿ���֦������Ҷ�ӽ��
		if (bcsds.classEntropy() < 1e-6) {
			splitAttribute = null;
			leafClassValue = bcsds.instance(0).classValue();
			leafMisclassificationCost = 0;
			return;
		}// Of if

		// For postprune
		// ���֦
		leafMisclassificationCost = bcsds.averageMisclassificationCost(0);
		// Compute attribute with maximum cost gain.
		// �������Ե�����������
		double[] tempGains = new double[bcsds.numAttributes() - 1];
		double tempMaximalCostGain = Integer.MIN_VALUE;
		int bestAttributeIndex = -17;// Use a strange number to facilitate
										// debugging.

		for (int i = 0; i < bcsds.numAttributes() - 1; i++) {
			// Shared by all algorithms
			// �����㷨����
			double tempInfoGain = 0;
			if (balance) {
				try {
					tempInfoGain = bcsds.balancedInformationGain(i);
				} catch (Exception ee) {
					throw new Exception(
							"Error occurred in BothCostSensitiveDecisionTree."
									+ "balancedInformationGain()\r\n" + ee);
				}// Of try
			} else {
				tempInfoGain = bcsds.informationGain(i);
			}// Of if

			// Cannot help increasing classification
			// ���ܰ������ӷ���
			if (Math.abs(tempInfoGain) <= 1e-6) {
				tempGains[i] = Integer.MIN_VALUE;
				continue;
			}// Of if

			switch (paraAlgorithm) {
			case COST_GAIN:
				double tempCostGain = bcsds.costGain(i);
				if (tempCostGain == Integer.MIN_VALUE) {
					// Judge the information gain when the new attribute does
					// not help decrease
					// the misclassification cost
					// �������������ڼ�����������ʱ���ж���Ϣ����
					if (Math.abs(tempInfoGain) <= 1e-6) {
						tempGains[i] = Integer.MIN_VALUE;
					} else {
						// Penalty on the test cost
						// ���Դ��۵ĳͷ�
						tempGains[i] = tempInfoGain
								/ bcsds.getIndividualTestCostArray()[i];
					}// Of if
				} else {
					tempGains[i] = tempCostGain * bcsds.numInstances();
				}// Of if
				break;
			case WEIGHTED_GAIN:
				// System.out.print("\t" +
				// SimpleTool.shorterDouble(bcsds.balancedInformationGain(i))
				// + ":" + SimpleTool.shorterDouble(bcsds.informationGain(i)));
				// System.out.print(" " + SimpleTool.shorterDouble(tempGains[i])
				// + " ");
				tempGains[i] = tempInfoGain
						* Math.pow(bcsds.getIndividualTestCost(i), paraLambda);
				break;
			case EXPONENTIAL_GAIN:
				tempGains[i] = (Math.pow(2, tempInfoGain) - 1)
						/ Math.pow(bcsds.getIndividualTestCost(i) + 1,
								paraLambda);
				break;
			default:
				throw new Exception(
						"Error occurred in CostSensitiveDecisionTree.makeTree()\r\n"
								+ "The algorithm type " + paraAlgorithm
								+ " is not supported");
			}// Of switch

			// System.out.println("tempGains[" + i + "] = " + tempGains[i]);
			if (tempMaximalCostGain < tempGains[i]) {
				tempMaximalCostGain = tempGains[i];
				bestAttributeIndex = i;
			}// Of if
		}// Of for i

		if (tempMaximalCostGain <= Integer.MIN_VALUE) {
			System.out.println("Nothing to select.");
			System.out.println(bcsds);
			splitAttribute = null;
			leafClassValue = bcsds.bestClassValue();
			return;
		}// of if

		// For both preprune and postprune
		maximalCostGain = Integer.MIN_VALUE;
		double tempCostGain = 0;
		for (int i = 0; i < bcsds.numAttributes() - 1; i++) {
			tempCostGain = bcsds.costGain(i);
			if (maximalCostGain < tempCostGain) {
				maximalCostGain = tempCostGain;
			}// Of if
		}// Of for i
			// System.out.println("Maximal cost gain: " + maximalCostGain);

		// Pre-prune
		if (paraPreprune && (maximalCostGain < 0)) {
			splitAttribute = null;
			leafClassValue = bcsds.bestClassValue();
			return;
		}// Of if

		splitAttribute = bcsds.attribute(bestAttributeIndex);
		// Otherwise create successors.
		BothCostsNominalDecisionSystem[] splitData = splitData(bcsds,
				splitAttribute);
		successors = new CostSensitiveDecisionTree[splitAttribute.numValues()];
		for (int i = 0; i < splitAttribute.numValues(); i++) {
			// Do this recursively
			successors[i] = new CostSensitiveDecisionTree(splitData[i], balance);
			successors[i].makeTree(paraAlgorithm, paraLambda, paraPreprune);
		}// Of for i
	}// Of makeTree

	/**
	 ************************* 
	 * Postprune a CS-ID3 tree through postorder traversal.<br>
	 * �ݹ鷨����������֦��������ID3������
	 ************************* 
	 */
	private void postprune() throws Exception {
		// System.out.println("postprune ");
		// boolean successorsAreLeaves = true;
		for (int i = 0; i < successors.length; i++) {
			if (successors[i].splitAttribute == null) {
				continue;
			}// Of if

			successors[i].postprune();
			// Maybe it becomes a leaf after prune.
			if (successors[i].splitAttribute != null) {
				// successorsAreLeaves = false;
			}// Of if
		}// Of for i

		// Prune only if the tree does not decrease the total cost
		// ������û�м����ܴ���ʱ����֦
		double tempCost = classifyInstances(); // ���㵱ǰ�������ܴ���
		if (tempCost >= leafMisclassificationCost) {
			// System.out.print("postprune " + bcsds.numInstances() + "\t");
			splitAttribute = null;
			leafClassValue = bcsds.bestClassValue();
		}// Of if

		/*
		 * //Try to prune only if all successors are leaves if
		 * (successorsAreLeaves && (maximalCostGain < 0)) {
		 * System.out.println("postprune for " + bcsds.numInstances() +
		 * " instances."); splitAttribute = null; leafClassValue =
		 * bcsds.bestClassValue(); }//Of if
		 */
	}// Of postprune

	/**
	 ************************* 
	 * Classifies a given test instance using the decision tree.<br>
	 * ʹ�þ�������һ����������ʵ������
	 * 
	 * @param instance
	 *            the instance to be classified
	 * @return the classification
	 * @throws NoSupportForMissingValuesException
	 *             if instance has missing values
	 ************************* 
	 */
	public double classifyInstance(Instance instance)
			throws NoSupportForMissingValuesException {

		if (instance.hasMissingValue()) {
			throw new NoSupportForMissingValuesException(
					"Cs-Id3: no missing values, please.");
		}// Of if

		if (splitAttribute == null) {
			return leafClassValue;
		} else {
			return successors[(int) instance.value(splitAttribute)]
					.classifyInstance(instance);
		}// Of if
	}// Of classifyInstance

	/**
	 ************************* 
	 * Compute the instance's test cost while classification.<br>
	 * ������ʱ������ʵ���Ĳ��Դ���
	 * 
	 * @param instance
	 *            the instance to be classified
	 * @return the instance' test cost
	 * @throws NoSupportForMissingValuesException
	 *             if instance has missing values
	 ************************* 
	 */
	public double instanceTestCost(Instance instance, double paraTestCost)
			throws NoSupportForMissingValuesException, Exception {
		if (instance.hasMissingValue()) {
			throw new NoSupportForMissingValuesException(
					"Cs-Id3: no missing values, please.");
		}// Of if

		double currentTestCost = paraTestCost;
		boolean hasMatch = false;
		if (splitAttribute == null) {
			return currentTestCost;
		}// Of if

		for (int i = 0; i < bcsds.numAttributes(); i++) {
			if (bcsds.attribute(i) == splitAttribute) {
				currentTestCost += bcsds.getIndividualTestCostArray()[i];
				hasMatch = true;
				break;
			}// Of if
		}// Of for i
		if (!hasMatch) {
			throw new Exception(
					"Error occurred in CostSensitiveDecisionTree.instanceTestCost(). No attribute match.");
		}// Of if
		int childIndex = (int) instance.value(splitAttribute);
		return successors[childIndex].instanceTestCost(instance,
				currentTestCost);
	}// Of instanceTestCost

	/**
	 ************************* 
	 * Classifies using the training set.<br>
	 * ʹ��ѵ��������
	 * 
	 * @return average cost
	 * @throws Exception
	 *             if something wrong happens
	 ************************* 
	 */
	public double classifyInstances() throws Exception {
		return classifyInstances(bcsds);
	}// Of classifyInstances

	/**
	 ************************* 
	 * Classifies a given test set using the decision tree. Results are stored
	 * in object variables.<br>
	 * ʹ�þ�������һ�������Ĳ��Լ����ࡣ��������ڶ�������С�
	 * 
	 * @return average cost
	 * @param paraTestingSet
	 *            the testing set
	 * @throws Exception
	 *             if something wrong happens
	 ************************* 
	 */
	public double classifyInstances(
			BothCostsNominalDecisionSystem paraTestingSet) throws Exception {

		int correctClassified = 0;
		int totalInstances = paraTestingSet.numInstances();
		double totalTestCost = 0;
		double totalMisclassificationCost = 0;
		for (int i = 0; i < totalInstances; i++) {
			Instance currentInstance = paraTestingSet.instance(i);
			totalTestCost += instanceTestCost(currentInstance, 0);
			double classifiedValue = classifyInstance(currentInstance);
			int classfiedValueInt = (int) classifiedValue;
			int realVaueInt = (int) (currentInstance.classValue());
			if (classfiedValueInt == realVaueInt) {
				correctClassified++;
			} else {
				totalMisclassificationCost += bcsds.getClassificationCost(
						realVaueInt, classfiedValueInt);
			}// Of if
		}// Of for i

		accuracy = (0.0 + correctClassified) / totalInstances;
		averageCost = (0.0 + totalTestCost + totalMisclassificationCost)
				/ totalInstances;
		return averageCost;
	}// Of classifyInstances

	/**
	 ************************* 
	 * Splits a dataset according to the values of a nominal attribute.<br>
	 * ����һ�����������Ե�ֵ�������ݼ�
	 * 
	 * @param paraData
	 *            the data which is to be split
	 * @param paraAtt
	 *            the attribute to be used for splitting
	 * @return the sets of instances produced by the split
	 ************************* 
	 */
	private BothCostsNominalDecisionSystem[] splitData(
			BothCostsNominalDecisionSystem paraData, Attribute paraAtt)
			throws Exception {
		BothCostsNominalDecisionSystem[] splitData = new BothCostsNominalDecisionSystem[paraAtt
				.numValues()];
		for (int j = 0; j < paraAtt.numValues(); j++) {
			splitData[j] = new BothCostsNominalDecisionSystem(paraData);
			// Remove all instances so that some can be added later.
			splitData[j].delete();
		}// Of for j

		for (int i = 0; i < paraData.numInstances(); i++) {
			Instance inst = paraData.instance(i);
			splitData[(int) inst.value(paraAtt)].add(inst);
		}

		for (int i = 0; i < splitData.length; i++) {
			splitData[i].compactify();
		}// Of for i

		return splitData;
	}// Of splitData

	/**
	 ************************* 
	 * Prints the decision tree using the toString(int) method.<br>
	 * 
	 * @return a textual description of the classifier
	 * @see #toString(int)
	 ************************* 
	 */
	public String toString() {
		if ((leafDistribution == null) && (successors == null)) {
			return "CostSensitiveDecisionTree: No model built yet.";
		}// Of if
		return "CostSensitiveDecisionTree\n" + toString(0);
	}// Of toString

	/**
	 ************************* 
	 * Outputs a tree at a certain level.
	 * 
	 * @param level
	 *            the level at which the tree is to be printed
	 * @return the tree as string at the given level
	 ************************* 
	 */
	public String toString(int level) {

		StringBuffer text = new StringBuffer();

		if (splitAttribute == null) {
			if (Instance.isMissingValue(leafClassValue)) {
				text.append(": null");
			} else {
				text.append(": " + classAttribute.value((int) leafClassValue));
			}// Of Instance
		} else {
			for (int j = 0; j < splitAttribute.numValues(); j++) {
				text.append("\n");
				for (int i = 0; i < level; i++) {
					text.append("|  ");
				}// Of for i
				text.append(splitAttribute.name() + " = "
						+ splitAttribute.value(j));
				text.append(successors[j].toString(level + 1));
			}// Of for j
		}// Of if

		return text.toString();
	}// Of toString

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the options for the classifier
	 */
	public static void main(String[] args) {
		// runClassifier(new CostSensitiveDecisionTree(), args);
	}
}// Of class CostSensitiveDecisionTree