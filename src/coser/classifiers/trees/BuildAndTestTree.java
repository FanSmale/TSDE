package coser.classifiers.trees;

import coser.datamodel.decisionsystem.BothCostsNominalDecisionSystem;

/**
 * ���첢�Ҳ��Ծ�������ִ�в�ͬ���㷨��<br>
 * Summary: Build and test decision tree. Run different algorithms.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Progress: Half done.<br>
 * Written time: November 5, 2011. <br>
 * Last modify time: January 27, 2012.
 */
public class BuildAndTestTree {
	/**
	 * The decision system with both test cost and misclassification cost
	 * information<br>
	 * ���в��Դ��ۺ��������۵ľ���ϵͳ
	 */
	private BothCostsNominalDecisionSystem currentDecisionSystem;

	/**
	 * Training data.<br>
	 * ѵ����
	 */
	private BothCostsNominalDecisionSystem trainingDecisionSystem;

	/**
	 * Testing data.<br>
	 * ���Լ�
	 */
	private BothCostsNominalDecisionSystem testingDecisionSystem;

	/**
	 * The percentage of the training. Used in the split-in-two scenario.<br>
	 * ѵ�����İٷֱȡ��������ַ���
	 */
	double trainingPercentage;

	/**
	 * Test strategy, use training set, split-in-two, or cross-validation.<br>
	 * ���Բ��ԣ�ʹ��ѵ���������ַ���������֤��
	 */
	int testStrategy;

	/**
	 * Number of folds for cross-validation.<br>
	 * ������֤������
	 */
	int cvFolds;

	/**
	 * The average cost in training set. Only for the split-in-two scenario.<br>
	 * ѵ������ƽ�����ۡ������������ַ���ʱ��
	 */
	double costInTrainingSet;

	/**
	 * If all algorithms, these elements correspond with no prune, preprune,
	 * postprune. If only the weighted information gain algorithm, these
	 * elements correspond with different lambda values.<br>
	 * ��������㷨����ЩԪ�ض�Ӧ�޼�֦��Ԥ��֦�����֦����������Ǽ�Ȩ��Ϣ�����㷨����ЩԪ�ض�Ӧ�ڲ�ͬ��������ֵ��
	 */
	double[] misclassificationCostSumArray;

	/**
	 * All lambda settings, cost-gain, and best training approach.<br>
	 * ����������ֵ���ã��������������ѵ��������
	 */
	int[] algorithmsWinArray;

	/**
	 * Number of lambda.<br>
	 * ������ֵ�ĸ���
	 */
	int numberOfLambdas;

	/**
	 * Number of omega.<br>
	 * ŷ��٤ֵ�ĸ���
	 */
	int numberOfOmegas;

	/**
	 * Balance or not. When it is true, both appraoch will be tested.<br>
	 * ƽ����߲��ǡ�����Ϊ���ʱ�����ַ������������ԡ�
	 */
	boolean compareWithBalance;

	/**
	 * Compare different prune strategies or not.<br>
	 * �Ƚϲ�ͬ��֦���Ի��߲���
	 */
	boolean comparePruneStrategies;

	/**
	 * For different lambdas, cost-gain and competition approaches.<br>
	 * ���ڲ�ͬ��������ֵ����������;���������
	 */
	int numberOfSchemes;

	/**
	 *************************** 
	 * The only constructor. ���췽��<br>
	 * 
	 * @param paraSystem
	 *            The BothCostsNominalDecisionSystem ���Դ��ۺ��������۾���ϵͳ
	 * @param paraTrainingPercentage
	 *            The percentage of the training set. ѵ�����ٷֱ�
	 * @param paraTestStrategy
	 *            The strategy of the test. ���Բ���
	 * @param paraNumberOfLambdas
	 *            The number of lambda values. ������ֵ�ĸ���
	 * @param paraNumberOfOmegas
	 *            The number of omega values. ŷ��٤ֵ�ĸ���
	 * @param paraCvFolds
	 *            The folds of cross validation. ������֤������
	 * @param paraCompareWithBalance
	 *            ƽ����߲��ǡ�����Ϊ���ʱ�����ַ������������ԡ�
	 *************************** 
	 */
	public BuildAndTestTree(BothCostsNominalDecisionSystem paraSystem,
			double paraTrainingPercentage, int paraTestStrategy,
			int paraNumberOfLambdas, int paraNumberOfOmegas, int paraCvFolds,
			boolean paraCompareWithBalance, boolean paraComparePruneStrategies) {
		currentDecisionSystem = paraSystem;
		trainingPercentage = paraTrainingPercentage;
		testStrategy = paraTestStrategy;
		numberOfLambdas = paraNumberOfLambdas;
		numberOfOmegas = paraNumberOfOmegas;
		algorithmsWinArray = null;
		misclassificationCostSumArray = null;
		numberOfSchemes = numberOfLambdas + numberOfOmegas + 3;

		// These parameters cannot be true at the same time.
		if (paraCompareWithBalance) {
			numberOfSchemes = numberOfLambdas * 2 + numberOfOmegas + 1;
		}
		if (paraComparePruneStrategies) {
			numberOfSchemes = numberOfLambdas * 2 + 1;
		}
		System.out.println("numberOfSchemes = " + numberOfSchemes);
		algorithmsWinArray = new int[numberOfSchemes];
		misclassificationCostSumArray = new double[numberOfSchemes];
		cvFolds = paraCvFolds;
		compareWithBalance = paraCompareWithBalance;
		comparePruneStrategies = paraComparePruneStrategies;
	}// Of constructor

	/**
	 *************************** 
	 * Refresh test cost.
	 *************************** 
	 */
	public void refreshTestCost() throws Exception {
		currentDecisionSystem.refreshTestCost();
	}// of refreshTestCost

	/**
	 *************************** 
	 * Get the cost sum array.<br>
	 * ��ô����ܺ�����
	 *************************** 
	 */
	public double[] getCostSumArray() {
		return misclassificationCostSumArray;
	}// of getCostSumArray

	/**
	 *************************** 
	 * Get the algorithm wins array.<br>
	 * ����㷨ȡʤ��������
	 *************************** 
	 */
	public int[] getAlgorithmWinsArray() {
		return algorithmsWinArray;
	}// of getAlgorithmWinsArray

	/**
	 *************************** 
	 * One run, compare all lambda settings. Also compare different algorthms.<br>
	 * һ�����У��Ƚ��������������á�ͬʱҲ�Ƚϲ�ͬ�㷨��
	 * 
	 * @param paraLambdaLowerBound
	 *            The lower bound of lambda values ������ֵ���½�
	 * @param paraLambdaStepLength
	 *            The step length of lambda values ������ֵ�Ĳ���
	 * @param paraOmegaLowerBound
	 *            The lower bound of omega values ŷ��٤ֵ���½�
	 * @param paraOmegaLength
	 *            The step length of omega values ŷ��٤ֵ�Ĳ���
	 * @param paraPruneStrategy
	 *            The strategy of pruning tree ��������֦����
	 *************************** 
	 */
	public String oneRunAllAlgorithms(double paraLambdaLowerBound,
	// double paraLambdaUpperBound,
			double paraLambdaStepLength, double paraOmegaLowerBound,
			// double paraOmegaUpperBound,
			double paraOmegaLength, int paraPruneStrategy) throws Exception {
		double[] tempCost = new double[numberOfSchemes];

		if (testStrategy == 1) {
			refreshTrainingTestingSets();
		}// Of if

		// int bestStrategy = -1;
		int bestTreeIndexInTraining = 0;
		double bestCostInTesting = Integer.MAX_VALUE;
		double bestCostInTraining = Integer.MAX_VALUE;

		int bestTreeIndexInTrainingID3 = 0;
		double bestCostInTrainingID3 = Integer.MAX_VALUE;
		int bestTreeIndexInTrainingEG2 = 0;
		double bestCostInTrainingEG2 = Integer.MAX_VALUE;
		int i;

		// For weighted gain algorithm
		// ���ڼ�Ȩ��Ϣ�����㷨
		for (i = 0; i < numberOfLambdas; i++) {
			double tempLambda = paraLambdaLowerBound + i * paraLambdaStepLength;
			if (testStrategy == 0) {
				tempCost[i] = oneRunTrainingSet(
						CostSensitiveDecisionTree.WEIGHTED_GAIN, tempLambda,
						paraPruneStrategy, false);
			} else if (testStrategy == 1) {
				tempCost[i] = oneRunSplitInTwo(
						CostSensitiveDecisionTree.WEIGHTED_GAIN, tempLambda,
						paraPruneStrategy, false);
				// Only for lambda-ID3
				if (costInTrainingSet < bestCostInTrainingID3) {
					bestCostInTrainingID3 = costInTrainingSet;
					bestTreeIndexInTrainingID3 = i;
				}// Of if

				if (costInTrainingSet < bestCostInTraining) {
					bestCostInTraining = costInTrainingSet;
					bestTreeIndexInTraining = i;
					bestTreeIndexInTrainingID3 = i;
				}// Of if
			} else {
				tempCost[i] = oneRunCV(CostSensitiveDecisionTree.WEIGHTED_GAIN,
						tempLambda, paraPruneStrategy, false);
			}// Of if
			misclassificationCostSumArray[i] += tempCost[i];

			if (tempCost[i] < bestCostInTesting) {
				bestCostInTesting = tempCost[i];
			}// Of if
		}// Of for i

		// For weighted gain-balance algorithm.
		// ���ڼ�Ȩ��Ϣ����ƽ���㷨
		if (compareWithBalance) {
			for (; i < numberOfLambdas * 2; i++) {
				double tempLambda = paraLambdaLowerBound
						+ (i - numberOfLambdas) * paraLambdaStepLength;
				if (testStrategy == 0) {
					tempCost[i] = oneRunTrainingSet(
							CostSensitiveDecisionTree.WEIGHTED_GAIN,
							tempLambda, paraPruneStrategy, true);
				} else if (testStrategy == 1) {
					tempCost[i] = oneRunSplitInTwo(
							CostSensitiveDecisionTree.WEIGHTED_GAIN,
							tempLambda, paraPruneStrategy, true);
					if (costInTrainingSet < bestCostInTraining) {
						bestCostInTraining = costInTrainingSet;
						bestTreeIndexInTraining = i;
					}// Of if
				} else {
					tempCost[i] = oneRunCV(
							CostSensitiveDecisionTree.WEIGHTED_GAIN,
							tempLambda, paraPruneStrategy, true);
				}// Of if
				misclassificationCostSumArray[i] += tempCost[i];

				if (tempCost[i] < bestCostInTesting) {
					bestCostInTesting = tempCost[i];
				}// Of if
			}// Of for i
		}// Of if compareWithBalance

		int position = i;
		for (; i < position + numberOfOmegas; i++) {
			double tempOmega = paraOmegaLowerBound + (i - position)
					* paraOmegaLength;
			if (testStrategy == 0) {
				tempCost[i] = oneRunTrainingSet(
						CostSensitiveDecisionTree.EXPONENTIAL_GAIN, tempOmega,
						paraPruneStrategy, true);
			} else if (testStrategy == 1) {
				tempCost[i] = oneRunSplitInTwo(
						CostSensitiveDecisionTree.EXPONENTIAL_GAIN, tempOmega,
						paraPruneStrategy, true);
				// Only for EG2
				if (costInTrainingSet < bestCostInTrainingEG2) {
					bestCostInTrainingEG2 = costInTrainingSet;
					bestTreeIndexInTrainingEG2 = i;
				}// Of if

				if (costInTrainingSet < bestCostInTraining) {
					bestCostInTraining = costInTrainingSet;
					bestTreeIndexInTraining = i;
				}// Of if
			} else {
				tempCost[i] = oneRunCV(
						CostSensitiveDecisionTree.EXPONENTIAL_GAIN, tempOmega,
						paraPruneStrategy, true);
			}// Of if
			misclassificationCostSumArray[i] += tempCost[i];

			if (tempCost[i] < bestCostInTesting) {
				bestCostInTesting = tempCost[i];
			}// Of if
		}// Of for i

		/*
		 * //For the cost gain algorithm if (testStrategy == 0) { tempCost[i] =
		 * oneRunTrainingSet(CostSensitiveDecisionTree.COST_GAIN, 0,
		 * paraPruneStrategy, false); } else if (testStrategy == 1) {
		 * tempCost[i] = oneRunSplitInTwo(CostSensitiveDecisionTree.COST_GAIN,
		 * 0, paraPruneStrategy, false); if (costInTrainingSet <
		 * bestCostInTrainingSet) { bestTreeIndexInTraining = i; }//Of if } else
		 * { tempCost[i] = oneRunCV(CostSensitiveDecisionTree.COST_GAIN, 0,
		 * paraPruneStrategy, false); }//Of if misclassificationCostSumArray[i]
		 * += tempCost[i];
		 * 
		 * if (tempCost[i] < bestCost) { bestCost = tempCost[i]; }//Of if //i
		 * ++;
		 */

		// For the best training approach
		if (testStrategy == 1) {
			tempCost[i] = tempCost[bestTreeIndexInTrainingID3];
			misclassificationCostSumArray[i] += tempCost[i];

			i++;
			tempCost[i] = tempCost[bestTreeIndexInTrainingEG2];
			misclassificationCostSumArray[i] += tempCost[i];

			i++;
			tempCost[i] = tempCost[bestTreeIndexInTraining];
			misclassificationCostSumArray[i] += tempCost[i];
		}// Of if

		String tempMessage = "";
		for (i = 0; i < numberOfSchemes; i++) {
			if (Math.abs(tempCost[i] - bestCostInTesting) < 1e-6) {
				algorithmsWinArray[i]++;
				tempMessage += "1\t";
			} else {
				tempMessage += "0\t";
			}// Of if
		}// Of for i

		if (testStrategy == 1) {
			tempMessage += "" + bestTreeIndexInTraining;
		}// Of if
		tempMessage += "\r\n";
		return tempMessage;
	}// Of oneRunAllAlgorithms

	/**
	 *************************** 
	 * One run, compare prune strategies.<br>
	 * һ�����У��Ƚϼ�֦����
	 *************************** 
	 */
	public String oneRunPruneCompare(double paraLambdaLowerBound,
			double paraLambdaUpperBound, double paraLambdaStepLength,
			boolean paraBalance) throws Exception {
		double[] tempCost = new double[numberOfSchemes];

		if (testStrategy == 1) {
			refreshTrainingTestingSets();
		}// Of if
		double tempBestCost = Integer.MAX_VALUE;
		// int bestStrategy = -1;
		int bestTreeIndexInTraining = 0;
		double bestCostInTraining = Integer.MAX_VALUE;
		int j = 0;
		for (int tempPrune = CostSensitiveDecisionTree.NO_PRUNE; tempPrune <= CostSensitiveDecisionTree.POST_PRUNE; tempPrune += 2) {
			for (int i = 0; i < numberOfLambdas; i++, j++) {
				double tempLambda = paraLambdaLowerBound + i
						* paraLambdaStepLength;
				if (testStrategy == 0) {
					tempCost[j] = oneRunTrainingSet(
							CostSensitiveDecisionTree.WEIGHTED_GAIN,
							tempLambda, tempPrune, paraBalance);
				} else if (testStrategy == 1) {
					tempCost[j] = oneRunSplitInTwo(
							CostSensitiveDecisionTree.WEIGHTED_GAIN,
							tempLambda, tempPrune, paraBalance);
					if (costInTrainingSet < bestCostInTraining) {
						bestCostInTraining = costInTrainingSet;
						bestTreeIndexInTraining = j;
					}// Of if
				} else {
					tempCost[j] = oneRunCV(
							CostSensitiveDecisionTree.WEIGHTED_GAIN,
							tempLambda, tempPrune, paraBalance);
				}// Of if
				misclassificationCostSumArray[j] += tempCost[j];

				if (tempCost[j] < tempBestCost) {
					tempBestCost = tempCost[j];
				}// Of if
			}// Of for i

			/*
			 * //For the cost gain algorithm if (testStrategy == 0) {
			 * tempCost[j] =
			 * oneRunTrainingSet(CostSensitiveDecisionTree.COST_GAIN, 0,
			 * tempPrune, false); } else if (testStrategy == 1) { tempCost[j] =
			 * oneRunSplitInTwo(CostSensitiveDecisionTree.COST_GAIN, 0,
			 * tempPrune, false); if (costInTrainingSet < bestCostInTraining) {
			 * bestTreeIndexInTraining = j; }//Of if } else { tempCost[j] =
			 * oneRunCV(CostSensitiveDecisionTree.COST_GAIN, 0, tempPrune,
			 * false); }//Of if misclassificationCostSumArray[j] += tempCost[j];
			 * 
			 * if (tempCost[j] < tempBestCost) { tempBestCost = tempCost[j];
			 * }//Of if j ++;
			 */
		}// Of for tempPrune

		// For the best training approach
		if (testStrategy == 1) {
			tempCost[j] = tempCost[bestTreeIndexInTraining];
			misclassificationCostSumArray[j] += tempCost[j];
		}// Of if

		String tempMessage = "";
		for (j = 0; j < numberOfSchemes; j++) {
			if (Math.abs(tempCost[j] - tempBestCost) < 1e-6) {
				algorithmsWinArray[j]++;
				tempMessage += "1\t";
			} else {
				tempMessage += "0\t";
			}// Of if
		}// Of for i

		if (testStrategy == 1) {
			tempMessage += "" + bestTreeIndexInTraining;
		}// Of if
		tempMessage += "\r\n";
		return tempMessage;
	}// Of oneRunPruneCompare

	/**
	 *************************** 
	 * One run, test on the training set.<br>
	 * һ�����У���ѵ�����ϲ���
	 * 
	 * @return the average cost.
	 *************************** 
	 */
	public double oneRunTrainingSet(int paraAlgorithm, double paraLambda,
			int paraPruneStrategy, boolean paraBalance) throws Exception {
		// Just another name to make statements shorter.
		// double tempAccuracy;
		double tempCost;

		CostSensitiveDecisionTree costSensitiveDecisionTree = new CostSensitiveDecisionTree(
				currentDecisionSystem, paraBalance);
		try {
			costSensitiveDecisionTree.buildClassifier(paraAlgorithm,
					paraLambda, paraPruneStrategy);
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in costSensitiveDecisionTree.buildClassifier(): \r\n"
							+ ee);
		}// Of try

		try {
			costSensitiveDecisionTree.classifyInstances();
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in costSensitiveDecisionTree.classifyInstances(): \r\n"
							+ ee);
		}// Of try
			// tempAccuracy = costSensitiveDecisionTree.getAccuracy();
		tempCost = costSensitiveDecisionTree.getAverageCost();

		return tempCost;
	}// Of oneRunTrainingSet

	/**
	 *************************** 
	 * Refresh the training test set. Only for split in two scenario.<br>
	 * ˢ��ѵ�����Լ��������������ַ���
	 *************************** 
	 */
	public void refreshTrainingTestingSets() throws Exception {
		BothCostsNominalDecisionSystem[] tempDSArray = currentDecisionSystem
				.divideInTwo(trainingPercentage);
		trainingDecisionSystem = tempDSArray[0];
		testingDecisionSystem = tempDSArray[1];
	}// Of refreshTrainingTestingSets

	/**
	 *************************** 
	 * One run, split in two.<br>
	 * һ�����У����ֳ�������
	 * 
	 * @return the average cost.
	 *************************** 
	 */
	public double oneRunSplitInTwo(int paraAlgorithm, double paraLambda,
			int paraPruneStrategy, boolean paraBalance) throws Exception {
		// Just another name to make statements shorter.
		// tempAccuracy,
		double tempCost;

		CostSensitiveDecisionTree costSensitiveDecisionTree = new CostSensitiveDecisionTree(
				trainingDecisionSystem, paraBalance);
		try {
			costSensitiveDecisionTree.buildClassifier(paraAlgorithm,
					paraLambda, paraPruneStrategy);
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in costSensitiveDecisionTree.buildClassifier(): \r\n"
							+ ee);
		}// Of try

		try {
			costSensitiveDecisionTree.classifyInstances(testingDecisionSystem);
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in costSensitiveDecisionTree.classifyInstances(): \r\n"
							+ ee);
		}// Of try
			// tempAccuracy = costSensitiveDecisionTree.getAccuracy();
		tempCost = costSensitiveDecisionTree.getAverageCost();

		// Cost in training and testing sets are different.
		try {
			costSensitiveDecisionTree.classifyInstances(trainingDecisionSystem);
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in costSensitiveDecisionTree.classifyInstances(): \r\n"
							+ ee);
		}// Of try
		costInTrainingSet = costSensitiveDecisionTree.getAverageCost();

		return tempCost;
	}// Of oneRunSplitInTwo

	/**
	 *************************** 
	 * One run, cross-validation.<br>
	 * һ�����У�������֤
	 * 
	 * @return the average cost.
	 *************************** 
	 */
	public double oneRunCV(int paraAlgorithm, double paraLambda,
			int paraPruneStrategy, boolean paraBalance) throws Exception {
		// Just another name to make statements shorter.
		// tempAccuracy,
		double tempCost;
		int numberOfInstances = currentDecisionSystem.numInstances();
		boolean[] trainInBooleanArray = new boolean[currentDecisionSystem
				.numInstances()];
		boolean[] testInBooleanArray = new boolean[currentDecisionSystem
				.numInstances()];
		BothCostsNominalDecisionSystem currentTrain, currentTest;
		CostSensitiveDecisionTree costSensitiveDecisionTree = null;
		// resultArea.append("Cross validation with " + cvFolds + " folds");

		// double totalAccuracy = 0;
		double totalCost = 0;
		for (int i = 0; i < cvFolds; i++) {
			for (int j = 0; j < numberOfInstances; j++) {
				if (j % cvFolds == i) {
					trainInBooleanArray[j] = false;
					testInBooleanArray[j] = true;
				} else {
					trainInBooleanArray[j] = true;
					testInBooleanArray[j] = false;
				}// Of if
			}// Of for j

			currentTrain = new BothCostsNominalDecisionSystem(
					currentDecisionSystem);
			currentTrain.delete(trainInBooleanArray);
			currentTest = new BothCostsNominalDecisionSystem(
					currentDecisionSystem);
			currentTest.delete(testInBooleanArray);

			costSensitiveDecisionTree = new CostSensitiveDecisionTree(
					currentTrain, paraBalance);
			costSensitiveDecisionTree.buildClassifier(paraAlgorithm,
					paraLambda, paraPruneStrategy);

			costSensitiveDecisionTree.classifyInstances(currentTest);
			// double tTempAccuracy = costSensitiveDecisionTree.getAccuracy();
			// totalAccuracy += tTempAccuracy;
			double tTempCost = costSensitiveDecisionTree.getAverageCost();
			totalCost += tTempCost;
		}// Of for i

		// tempAccuracy = totalAccuracy / cvFolds;
		tempCost = totalCost / cvFolds;

		return tempCost;
	}// Of oneRunCV

}// Of class BuildAndTestTree
