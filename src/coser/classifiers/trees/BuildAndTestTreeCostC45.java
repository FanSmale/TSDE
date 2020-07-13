package coser.classifiers.trees;

import weka.core.Instances;
import coser.classifiers.trees.csj48.ClassifierTree;
import coser.datamodel.decisionsystem.BothCostsNominalDecisionSystem;
import coser.datamodel.decisionsystem.BothCostsNumericDecisionSystem;
import coser.datamodel.decisionsystem.CostSensitiveDecisionSystem;
import coser.datamodel.decisionsystem.RoughDecisionSystem;

/**
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
public class BuildAndTestTreeCostC45 {

	/**
	 * The current cost sensitive decision system.
	 */
	private CostSensitiveDecisionSystem currentCostSensitiveDecisionSystem;

	/**
	 * Training data.
	 */
	private CostSensitiveDecisionSystem trainingDecisionSystem;

	/**
	 * Testing data.
	 */
	private CostSensitiveDecisionSystem testingDecisionSystem;

	/**
	 * The prune strategy including no pruning, pre-prune, and post-prune.
	 */
	private int pruneStrategy;

	/**
	 * The test strategy
	 */
	private int testStrategy;

	/**
	 * The train set proportion
	 */
	private double trainsetProportion;

	/**
	 * Whether reveal running details<br>
	 * True - Reveal running details<br>
	 * False - Do not reveal running details
	 */
	private boolean isDetail;

	/**
	 * The test cost of the decision system.
	 */
	public static int[] individualTestCostArray;

	/**
	 * The misclassification cost matrix
	 */
	public int[][] costMatrix;

	/**
	 * The probability of the corrected classification
	 */
	private double correctedClassifyProb;

	/**
	 * The attributes which are tested.
	 */
	public static boolean[] attributeTested;

	/**
	 * The tested counts of each attribute
	 */
	public static int[] attributeTestedCounts;

	/**
	 ************************* 
	 * The first constructor
	 * 
	 * @param paraBothCostsNominalDecisionSystem
	 *            The given nominal decision system
	 * @param paraLambda
	 *            The parameter lambda
	 * @param paraPruneStrategy
	 *            The pruning stragy
	 * @param paraTestStrategy
	 *            The testing strategy
	 * @param paraTrainsetProportion
	 *            The proportion of training data set
	 * @param paraDetail
	 *            If reveal the running details
	 ************************* 
	 */
	public BuildAndTestTreeCostC45(
			BothCostsNominalDecisionSystem paraBothCostsNominalDecisionSystem,
			double paraLambda, int paraPruneStrategy, int paraTestStrategy,
			double paraTrainsetProportion, boolean paraDetail) {
		currentCostSensitiveDecisionSystem = paraBothCostsNominalDecisionSystem;
		CostJ48.dataType = false;
		CostJ48.lambda = paraLambda;
		pruneStrategy = paraPruneStrategy;
		testStrategy = paraTestStrategy;
		trainsetProportion = paraTrainsetProportion;
		isDetail = paraDetail;
		initialize();
	}// Of the first constructor

	/**
	 ************************* 
	 * The second constructor
	 * 
	 * @param paraBothCostsNumericDecisionSystem
	 *            The given both costs numeric decision system
	 * @param paraLambda
	 *            The parameter lambda
	 * @param paraPruneStrategy
	 *            The pruning stragy
	 * @param paraTestStrategy
	 *            The testing strategy
	 * @param paraTrainsetProportion
	 *            The proportion of training data set
	 * @param paraDetail
	 *            If reveal the running details
	 ************************* 
	 */
	public BuildAndTestTreeCostC45(
			BothCostsNumericDecisionSystem paraBothCostsNumericDecisionSystem,
			double paraLambda, int paraPruneStrategy, int paraTestStrategy,
			double paraTrainsetProportion, boolean paraDetail) {
		currentCostSensitiveDecisionSystem = paraBothCostsNumericDecisionSystem;
		CostJ48.dataType = true;
		CostJ48.lambda = paraLambda;
		pruneStrategy = paraPruneStrategy;
		testStrategy = paraTestStrategy;
		trainsetProportion = paraTrainsetProportion;
		isDetail = paraDetail;
		initialize();
	}// Of the second constructor

	/**
	 ************************* 
	 * The third constructor
	 * 
	 * @param paraNominalDecisionSystem
	 *            The given both costs nominal decision system
	 * @param paraLambdaLowerBound
	 *            The lower bound of parameter lambda
	 * @param paraLambdaUpperBound
	 *            The upper bound of parameter lambda
	 * @param paraLambdaStepLength
	 *            The step length of parameter lambda
	 * @param paraPruneStrategy
	 *            The pruning strategy
	 * @param paraTestStrategy
	 *            The testing strategy
	 * @param paraTrainsetProportion
	 *            The proportion of training data set
	 * @param paraDetail
	 *            If reveal running details
	 ************************* 
	 */
	public BuildAndTestTreeCostC45(
			BothCostsNominalDecisionSystem paraNominalDecisionSystem,
			double paraLambdaLowerBound, double paraLambdaUpperBound,
			double paraLambdaStepLength, int paraPruneStrategy,
			int paraTestStrategy, double paraTrainsetProportion,
			boolean paraDetail) {
		super();
		currentCostSensitiveDecisionSystem = paraNominalDecisionSystem;
		pruneStrategy = paraPruneStrategy;
		testStrategy = paraTestStrategy;
		trainsetProportion = paraTrainsetProportion;
		CostJ48.dataType = false;
		isDetail = paraDetail;
		initialize();
	}// Of the third constructor

	/**
	 ************************* 
	 * The fourth constructor
	 * 
	 * @param paraNumericDecisionSystem
	 *            The given both costs nominal decision system
	 * @param paraLambdaLowerBound
	 *            The lower bound of parameter lambda
	 * @param paraLambdaUpperBound
	 *            The upper bound of parameter lambda
	 * @param paraLambdaStepLength
	 *            The step length of parameter lambda
	 * @param paraPruneStrategy
	 *            The pruning strategy
	 * @param paraTestStrategy
	 *            The testing strategy
	 * @param paraTrainsetProportion
	 *            The proportion of training data set
	 * @param paraDetail
	 *            If reveal running details
	 ************************* 
	 */
	public BuildAndTestTreeCostC45(
			BothCostsNumericDecisionSystem paraNumericDecisionSystem,
			double paraLambdaLowerBound, double paraLambdaUpperBound,
			double paraLambdaStepLength, int paraPruneStrategy,
			int paraTestStrategy, double paraTrainsetProportion,
			boolean paraDetail) {
		super();
		currentCostSensitiveDecisionSystem = paraNumericDecisionSystem;
		// lambdaLowerBound = paraLambdaLowerBound;
		// lambdaUpperBound = paraLambdaUpperBound;
		// lambdaStepLength = paraLambdaStepLength;
		pruneStrategy = paraPruneStrategy;
		testStrategy = paraTestStrategy;
		trainsetProportion = paraTrainsetProportion;
		CostJ48.dataType = true;
		isDetail = paraDetail;
		initialize();
	}// Of the fourth constructor

	/**
	 ************************* 
	 * Initialization
	 ************************* 
	 */
	private void initialize() {
		copyArray(currentCostSensitiveDecisionSystem
				.getIndividualTestCostArray());
		costMatrix = currentCostSensitiveDecisionSystem
				.getMisclassificationCostMatrix();
		correctedClassifyProb = 0;
		BuildAndTestTreeCostC45.attributeTested = new boolean[currentCostSensitiveDecisionSystem
				.getNumberOfConditions()];
		BuildAndTestTreeCostC45.attributeTestedCounts = new int[currentCostSensitiveDecisionSystem
				.getNumberOfConditions()];
		if (testStrategy == 0) {
			trainingDecisionSystem = currentCostSensitiveDecisionSystem;
			testingDecisionSystem = currentCostSensitiveDecisionSystem;
		} else if (testStrategy == 1) {
			try {
				RoughDecisionSystem[] subsets = currentCostSensitiveDecisionSystem
						.divideInTwo(trainsetProportion);
				trainingDecisionSystem = new CostSensitiveDecisionSystem(
						subsets[0]);
				testingDecisionSystem = new CostSensitiveDecisionSystem(
						subsets[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}// Of try-catch
		}// Of if-else

	}// Of initialize

	/**
	 ************************* 
	 * Build the decision tree.
	 * 
	 * @return The CostJ48 object
	 ************************* 
	 */
	public CostJ48 buildDecisionTree() {
		ClassifierTree classifierTree = null;
		CostJ48 costJ48 = new CostJ48(classifierTree, CostJ48.lambda, true, 0,
				2, false, false, 0, false, false, true, pruneStrategy, 0);
		// Set the test cost array.
		costJ48.setTestcostArray(individualTestCostArray);
		// Set the misclassification matrix.
		costJ48.setMisclassificationCostMatrix(costMatrix);

		try {
			// costJ48.buildClassifier(currentCostSensitiveDecisionSystem);
			costJ48.buildClassifier(trainingDecisionSystem);
		} catch (Exception ee) {
			ee.printStackTrace();
		}// Of try-catch

		// Print the created decision tree.
		try {
			// String treeGraph = costJ48.graph();
			// System.out.println(treeGraph);
		} catch (Exception e) {
			e.printStackTrace();
		}// Of try-catch

		if (pruneStrategy == 2) {

			if (isDetail) {
				System.out.println("Average cost(before pruning) = "
						+ evaluateCost(costJ48, trainingDecisionSystem));
				System.out.println(costJ48);
			}// Of if
			postPrune(costJ48.root);
			if (isDetail) {
				System.out.println("Average cost(after pruning) = "
						+ evaluateCost(costJ48, trainingDecisionSystem));
				// System.out.println(costJ48);
			}// Of if
		}// Of if

		return costJ48;

	}// Of buildDecisionTree

	/**
	 ************************* 
	 * Batch experiments
	 * 
	 * @param paraLambdaLowerBound
	 *            The lower bound of lambda
	 * @param paraLambdaUpperBound
	 *            The upper bound of lambda
	 * @param paraLambdaStepLength
	 *            The step length of lambda
	 * @param paraExperiments
	 *            The experiment counts
	 * @return The message representing results.
	 * @throws Exception
	 *             Exceptions
	 ************************* 
	 */
	public String batchDecisionTreeC45(double paraLambdaLowerBound,
			double paraLambdaUpperBound, double paraLambdaStepLength,
			int paraExperiments) throws Exception {

		String message = "";

		int numberOfLambdas = 1 + (int) ((paraLambdaUpperBound - paraLambdaLowerBound) / paraLambdaStepLength);
		if (numberOfLambdas < 1) {
			message = "lambda lower bound: " + paraLambdaLowerBound
					+ " is greater than the upper bound" + paraLambdaUpperBound;
			throw new Exception(
					"Error occurred in Reduction.batchMinimalTestCostReduction(): \r\n"
							+ message);
		}// Of if

		// Initialize the lambda array.
		double[] lambdaArray = new double[numberOfLambdas];
		for (int i = 0; i < numberOfLambdas; i++) {
			lambdaArray[i] = paraLambdaUpperBound - i * paraLambdaStepLength;
		}// Of for i

		// The classifier tree array saving each classifier tree which
		// corresponding to each lambda value in one experiment.
		CostJ48[] costJ48Array = new CostJ48[numberOfLambdas + 1];

		// The current average cost.
		double currentAverageCost = 0;

		// The minimal average cost among the current round experiment.
		double currentRoundMinimalAverageCost;

		// The best classifier tree index of the current round experiment.
		int bestClassifierTreeIndex;

		// The win times array.
		int[] winTimesArray = new int[numberOfLambdas + 1];

		// The average cost array.
		double[] averageCost = new double[numberOfLambdas + 1];

		// Start timing.
		long time1 = System.currentTimeMillis();

		// Batch experiments
		for (int j = 0; j < paraExperiments; j++) {

			System.out.println("Experiment round: " + (j + 1));

			currentCostSensitiveDecisionSystem.refreshTestCost();
			// copyArray(currentCostSensitiveDecisionSystem
			// .getIndividualTestCostArray());

			currentAverageCost = 0;// Current average cost

			// The smallest average cost in current round.
			currentRoundMinimalAverageCost = Integer.MAX_VALUE;

			// The order of the best tree in current round.
			bestClassifierTreeIndex = 0;

			// Generate cost-sensitive decision trees thorugh different
			// parameters.
			for (int i = 0; i < lambdaArray.length; i++) {

				// initialize();
				copyArray(currentCostSensitiveDecisionSystem
						.getIndividualTestCostArray());
				BuildAndTestTreeCostC45.attributeTested = new boolean[currentCostSensitiveDecisionSystem
						.getNumberOfConditions()];

				// Set the current parameter value
				CostJ48.lambda = lambdaArray[i];

				// Generate cost-sensitive decision trees.
				CostJ48 currentCostJ48 = buildDecisionTree();

				// Post prune.
				if (pruneStrategy == 2) {
					postPrune(currentCostJ48.root);
				}// Of if

				costJ48Array[i] = currentCostJ48;

				// Evaluate the average cost of decision trees.
				currentAverageCost = evaluateCost(currentCostJ48,
						trainingDecisionSystem);

				// Select the best classifier tree with minimal average in the
				// current round experiment.
				if (currentAverageCost <= currentRoundMinimalAverageCost) {
					currentRoundMinimalAverageCost = currentAverageCost;
					bestClassifierTreeIndex = i;
				}// if

			}// for i

			// Obtain the best decision tree in using competition strategy in
			// current round.
			costJ48Array[numberOfLambdas] = costJ48Array[bestClassifierTreeIndex];

			// Initialize the variables before testing trees.
			currentAverageCost = 0;
			currentRoundMinimalAverageCost = Integer.MAX_VALUE;
			bestClassifierTreeIndex = 0;

			// Test each decision tree corresponding to each lambda value and
			// the competition tree on test data set.
			double[] currentRoundAverageCost = new double[numberOfLambdas + 1];
			for (int k = 0; k <= numberOfLambdas; k++) {
				// Evaluate the average cost of the decision tree.
				currentAverageCost = evaluateCost(costJ48Array[k],
						testingDecisionSystem);
				currentRoundAverageCost[k] = currentAverageCost;
				// Compute the sum of the average cost corresponding to each
				// lambda value among all experiments.
				averageCost[k] += currentAverageCost;
				if (currentAverageCost <= currentRoundMinimalAverageCost) {
					currentRoundMinimalAverageCost = currentAverageCost;
					// bestClassifierTreeIndex = k;
				}// if
			}// for k

			// Statistic win times of each decision tree corresponding to each
			// lambda value.
			for (int l = 0; l <= numberOfLambdas; l++) {
				if (Math.abs(currentRoundAverageCost[l]
						- currentRoundMinimalAverageCost) < 1e-6) {
					winTimesArray[l]++;
				}// Of if
			}// Of for l

		}// for j

		// Stop timing.
		long time2 = System.currentTimeMillis();

		// Compute the execution time.
		long runTime = time2 - time1;

		// Compute the average cost corresponding to each lambda value per
		// experiment and print the statistical results.
		System.out.println("\n*******Summary*******");
		String resultMessage = "";
		resultMessage += "Lambda lower bound: " + paraLambdaLowerBound + "\n";
		resultMessage += "Lambda upper bound: " + paraLambdaUpperBound + "\n";
		resultMessage += "Lambda step length: " + paraLambdaStepLength + "\n";
		resultMessage += "Lambda value counts: " + numberOfLambdas + "\n";
		resultMessage += "Experiment counts: " + paraExperiments + "\n\n";
		resultMessage += "lambda" + "\t" + "Win times" + "\t"
				+ "average cost\n";
		for (int i = 0; i < numberOfLambdas; i++) {
			averageCost[i] = averageCost[i] / paraExperiments;
			resultMessage += lambdaArray[i] + "\t" + winTimesArray[i] + "\t"
					+ averageCost[i] + "\n";
		}// for i
		averageCost[numberOfLambdas] = averageCost[numberOfLambdas]
				/ paraExperiments;
		resultMessage += "CC-Tree" + "\t" + winTimesArray[numberOfLambdas]
				+ "\t" + averageCost[numberOfLambdas] + "\n\n";
		resultMessage += "Algorithm running time: " + runTime + " ms";

		System.out.println(resultMessage);

		System.out.println("Run-time£º" + runTime + "ms");

		return resultMessage;
	}// batchDecisionTreeC45

	/**
	 ************************* 
	 * Batch decision tree experiments with competition ranking.
	 * 
	 * @param paraLambdaLowerBound
	 *            The lower bound of lambda
	 * @param paraLambdaUpperBound
	 *            The upper bound of lambda
	 * @param paraLambdaStepLength
	 *            The step length of lambda
	 * @param paraExperiments
	 *            The experiment counts
	 * 
	 * @return The probability of the correct classification on the test data
	 *         set.
	 * @throws Exception
	 *             Exceptions
	 ************************* 
	 */
	public String batchDecisionTreeC45Rank(double paraLambdaLowerBound,
			double paraLambdaUpperBound, double paraLambdaStepLength,
			int paraExperiments) throws Exception {

		String message = "";

		int numberOfLambdas = 1 + (int) ((paraLambdaUpperBound - paraLambdaLowerBound) / paraLambdaStepLength);
		if (numberOfLambdas < 1) {
			message = "lambda lower bound: " + paraLambdaLowerBound
					+ " is greater than the upper bound" + paraLambdaUpperBound;
			throw new Exception(
					"Error occurred in Reduction.batchMinimalTestCostReduction(): \r\n"
							+ message);
		}// Of if

		// Initialize the lambda array.
		double[] lambdaArray = new double[numberOfLambdas];
		for (int i = 0; i < numberOfLambdas; i++) {
			lambdaArray[i] = paraLambdaUpperBound - i * paraLambdaStepLength;
		}// Of for i

		// The classifier tree array saving each classifier tree which
		// corresponding to each lambda value in one experiment.
		CostJ48[] costJ48Array = new CostJ48[numberOfLambdas];

		// The ranking array reserving cost J48.
		// ÓÐ´ýÐÞ¸Ä
		CostJ48[] costJ48RankingArray = new CostJ48[numberOfLambdas];

		// The current average cost.
		double currentAverageCost = 0;

		// The current average cost array
		double[] currentAverageCostArray = new double[numberOfLambdas];

		// The minimal average cost among the current round experiment.
		double currentRoundMinimalAverageCost;

		// The best classifier tree index of the current round experiment.
		int bestClassifierTreeIndex;

		// The win times array
		int[] winTimesArray = new int[numberOfLambdas * 2];

		// The average cost array
		double[] averageCost = new double[numberOfLambdas * 2];

		// The assistant sorting array
		boolean[] sortAssistArray = new boolean[numberOfLambdas];

		// Start timing.
		long time1 = System.currentTimeMillis();

		// Batch experiments.
		for (int j = 0; j < paraExperiments; j++) {

			System.out.println("Experiment round: " + (j + 1));

			currentCostSensitiveDecisionSystem.refreshTestCost();
			// copyArray(currentCostSensitiveDecisionSystem
			// .getIndividualTestCostArray());

			// Current average cost.
			currentAverageCost = 0;

			// The smallest average cost in current round.
			currentRoundMinimalAverageCost = Integer.MAX_VALUE;

			// The order of best tree in current round.
			bestClassifierTreeIndex = 0;

			// Generate decision tree using different parameter values.
			for (int i = 0; i < lambdaArray.length; i++) {

				// initialize();
				copyArray(currentCostSensitiveDecisionSystem
						.getIndividualTestCostArray());
				BuildAndTestTreeCostC45.attributeTested = new boolean[currentCostSensitiveDecisionSystem
						.getNumberOfConditions()];

				// Set current value.
				CostJ48.lambda = lambdaArray[i];

				// Build the cost-sensitive decision tree.
				CostJ48 currentCostJ48 = buildDecisionTree();

				// Post prune.
				if (pruneStrategy == 2) {
					postPrune(currentCostJ48.root);
				}// Of if

				costJ48Array[i] = currentCostJ48;

				// Evaluate the average cost of decision trees.
				currentAverageCost = evaluateCost(currentCostJ48,
						trainingDecisionSystem);

				currentAverageCostArray[i] = currentAverageCost;

			}// for i

			// Sort the generated cost-sensitive decision trees according to
			// average costs and save their references in a new array.
			int cur = 0;
			for (int n = 0; n < numberOfLambdas; n++) {
				currentRoundMinimalAverageCost = Integer.MAX_VALUE;
				for (int k = 0; k < numberOfLambdas; k++) {
					// Select the best classifier tree with minimal average
					// cost.
					if (sortAssistArray[k] == false
							&& currentAverageCostArray[k] <= currentRoundMinimalAverageCost) {
						currentRoundMinimalAverageCost = currentAverageCostArray[k];
						bestClassifierTreeIndex = k;
					}// if
				}// Of for k
				sortAssistArray[bestClassifierTreeIndex] = true;
				costJ48RankingArray[cur] = costJ48Array[bestClassifierTreeIndex];
				cur++;
			}// Of for n

			// Print the averge cost after sorting.
			for (int i = 0; i < numberOfLambdas; i++) {
				System.out.println("Ranking Tree"
						+ (i + 1)
						+ ": "
						+ evaluateCost(costJ48RankingArray[i],
								trainingDecisionSystem));
			}// for i

			// Initialize the variables before testing trees.
			currentAverageCost = 0;
			currentRoundMinimalAverageCost = Integer.MAX_VALUE;
			bestClassifierTreeIndex = 0;

			// Classify instances from testing data sets by each decision tree.
			double[] currentRoundAverageCost = new double[numberOfLambdas * 2];
			for (int k = 0; k < numberOfLambdas; k++) {
				// Evaluate the average cost of the decision tree.
				currentAverageCost = evaluateCost(costJ48Array[k],
						testingDecisionSystem);
				currentRoundAverageCost[k] = currentAverageCost;
				// Compute the sum of the average cost corresponding to each
				// lambda value among all experiments.
				averageCost[k] += currentAverageCost;
				if (currentAverageCost <= currentRoundMinimalAverageCost) {
					currentRoundMinimalAverageCost = currentAverageCost;
				}// if
			}// for k
			for (int k = 0; k < numberOfLambdas; k++) {
				// Evaluate the average cost of the ranking decision tree.
				currentAverageCost = evaluateCost(costJ48RankingArray[k],
						testingDecisionSystem);
				currentRoundAverageCost[numberOfLambdas + k] = currentAverageCost;
				// Compute the sum of the average cost corresponding to each
				// lambda value among all experiments.
				averageCost[numberOfLambdas + k] += currentAverageCost;
				if (currentAverageCost <= currentRoundMinimalAverageCost) {
					currentRoundMinimalAverageCost = currentAverageCost;
				}// if
			}// for k

			// Statistic win times of each decision tree corresponding to each
			// lambda value.
			for (int l = 0; l < numberOfLambdas; l++) {
				if (Math.abs(currentRoundAverageCost[l]
						- currentRoundMinimalAverageCost) < 1e-6) {
					winTimesArray[l]++;
				}// Of if
			}// Of for l
			for (int l = 0; l < numberOfLambdas; l++) {
				if (Math.abs(currentRoundAverageCost[numberOfLambdas + l]
						- currentRoundMinimalAverageCost) < 1e-6) {
					winTimesArray[numberOfLambdas + l]++;
				}// Of if
			}// Of for l

		}// for j

		// End timing.
		long time2 = System.currentTimeMillis();

		// Compute the run-time.
		long runTime = time2 - time1;

		// Compute the average cost corresponding to each lambda value per
		// experiment and print the statistical results.
		System.out.println("\n********Summary********");
		String resultMessage = "";
		resultMessage += "Lambda lower bound: " + paraLambdaLowerBound + "\n";
		resultMessage += "Lambda upper bound: " + paraLambdaUpperBound + "\n";
		resultMessage += "Lambda step length: " + paraLambdaStepLength + "\n";
		resultMessage += "Lambda value counts: " + numberOfLambdas + "\n";
		resultMessage += "Experiment counts: " + paraExperiments + "\n\n";
		resultMessage += "lambda" + "\t" + "Win times" + "\t"
				+ "average cost\n";
		for (int i = 0; i < numberOfLambdas; i++) {
			averageCost[i] = averageCost[i] / paraExperiments;
			resultMessage += lambdaArray[i] + "\t" + winTimesArray[i] + "\t"
					+ averageCost[i] + "\n";
		}// for i
		for (int i = numberOfLambdas; i < numberOfLambdas * 2; i++) {
			averageCost[i] = averageCost[i] / paraExperiments;
			resultMessage += "Rk-Tree" + (i - numberOfLambdas + 1) + "\t"
					+ winTimesArray[i] + "\t" + averageCost[i] + "\n";
		}// Of for i
		averageCost[numberOfLambdas] = averageCost[numberOfLambdas]
				/ paraExperiments;

		resultMessage += "Algorithm running time: " + runTime + " ms";

		System.out.println(resultMessage);

		System.out.println("Run-time£º" + runTime + "ms");

		return resultMessage;
	}// Of batchDecisionTreeC45Rank

	/**
	 ************************* 
	 * Batch decision tree experiments considering the variety of the
	 * misclassification matrix.
	 * 
	 * @param paraLambdaLowerBound
	 *            The lower bound of lambda value
	 * @param paraLambdaUpperBound
	 *            The upper bound of lambda value
	 * @param paraLambdaStepLength
	 *            The lambda step length
	 * @param paraExperiments
	 *            The number of experiments
	 * 
	 * @return The probability of the correct classification on the test data
	 *         set.
	 ************************* 
	 */
	public String batchDecisionTreeMisCostVariety(double paraLambdaLowerBound,
			double paraLambdaUpperBound, double paraLambdaStepLength,
			int paraExperiments) throws Exception {

		String message = "";

		String resultMessage = "";

		int numberOfLambdas = 1 + (int) ((paraLambdaUpperBound - paraLambdaLowerBound) / paraLambdaStepLength);
		if (numberOfLambdas < 1) {
			message = "lambda lower bound: " + paraLambdaLowerBound
					+ " is greater than the upper bound" + paraLambdaUpperBound;
			throw new Exception(
					"Error occurred in Reduction.batchMinimalTestCostReduction(): \r\n"
							+ message);
		}// Of if

		// Initialize the lambda array.
		double[] lambdaArray = new double[numberOfLambdas];
		for (int i = 0; i < numberOfLambdas; i++) {
			lambdaArray[i] = paraLambdaUpperBound - i * paraLambdaStepLength;
		}// Of for i

		// The classifier tree array saving each classifier tree which
		// corresponding to each lambda value in one experiment.
		CostJ48[] costJ48Array = new CostJ48[numberOfLambdas + 1];

		// The current average cost.
		double currentAverageCost = 0;

		// The minimal average cost among the current round experiment.
		double currentRoundMinimalAverageCost;

		// The best classifier tree index of the current round
		// experiment.
		int bestClassifierTreeIndex;

		// The win times array.
		int[] winTimesArray = new int[numberOfLambdas + 1];

		// The average cost array.
		double[] averageCost = new double[numberOfLambdas + 1];

		// Start timing.
		long time1 = System.currentTimeMillis();

		// Misclassification matrices vary.
		for (int m = 0; m < 10; m++) {

			costMatrix[0][1] = 50 + 50 * m;
			costMatrix[1][0] = 50 + 50 * m;

			// Batch experiments
			for (int j = 0; j < paraExperiments; j++) {

				System.out.println("Experiment round: " + (j + 1));

				currentCostSensitiveDecisionSystem.refreshTestCost();
				// copyArray(currentCostSensitiveDecisionSystem
				// .getIndividualTestCostArray());

				currentAverageCost = 0;
				currentRoundMinimalAverageCost = Integer.MAX_VALUE;
				bestClassifierTreeIndex = 0;
				winTimesArray = new int[numberOfLambdas + 1];
				averageCost = new double[numberOfLambdas + 1];

				// Generate decision trees with different parameter values.
				for (int i = 0; i < lambdaArray.length; i++) {

					// initialize();
					copyArray(currentCostSensitiveDecisionSystem
							.getIndividualTestCostArray());
					BuildAndTestTreeCostC45.attributeTested = new boolean[currentCostSensitiveDecisionSystem
							.getNumberOfConditions()];

					// Set current paramter value.
					CostJ48.lambda = lambdaArray[i];

					// Buile the cost-sensitive decision tree.
					CostJ48 currentCostJ48 = buildDecisionTree();

					// Post prune.
					if (pruneStrategy == 2) {
						postPrune(currentCostJ48.root);
					}// Of if

					costJ48Array[i] = currentCostJ48;

					// Print the decision tree.
					// System.out.println("costJ48Array[" + i + "]:\n"
					// + costJ48Array[i]);

					// Evaluate the average cost of obtained decision trees.
					currentAverageCost = evaluateCost(currentCostJ48,
							trainingDecisionSystem);

					// Select the best classifier tree with minimal average in
					// the
					// current round experiment.
					if (currentAverageCost <= currentRoundMinimalAverageCost) {
						currentRoundMinimalAverageCost = currentAverageCost;
						bestClassifierTreeIndex = i;
					}// if

				}// for i

				// Obain the best decision tree with competition strategy.
				costJ48Array[numberOfLambdas] = costJ48Array[bestClassifierTreeIndex];

				// Initialize the variables before testing trees.
				currentAverageCost = 0;
				currentRoundMinimalAverageCost = Integer.MAX_VALUE;
				bestClassifierTreeIndex = 0;

				// Test each decision tree corresponding to each lambda value
				// and
				// the competition tree on test data set.
				double[] currentRoundAverageCost = new double[numberOfLambdas + 1];
				for (int k = 0; k <= numberOfLambdas; k++) {
					// Evaluate the average cost of the decision tree.
					currentAverageCost = evaluateCost(costJ48Array[k],
							testingDecisionSystem);
					currentRoundAverageCost[k] = currentAverageCost;
					// Compute the sum of the average cost corresponding to each
					// lambda value among all experiments.
					averageCost[k] += currentAverageCost;
					if (currentAverageCost <= currentRoundMinimalAverageCost) {
						currentRoundMinimalAverageCost = currentAverageCost;
						// bestClassifierTreeIndex = k;
					}// if
				}// for k

				// Statistic win times of each decision tree corresponding to
				// each
				// lambda value.
				for (int l = 0; l <= numberOfLambdas; l++) {
					if (Math.abs(currentRoundAverageCost[l]
							- currentRoundMinimalAverageCost) < 1e-6) {
						winTimesArray[l]++;
					}// Of if
				}// Of for l

			}// for j

			// Compute the average cost corresponding to each lambda value
			// per
			// experiment and print the statistical results.
			System.out.println("\n*********Summary********");

			resultMessage += "Misclassification(balance) = " + costMatrix[0][1]
					+ "\n";
			resultMessage += "Lambda lower bound: " + paraLambdaLowerBound
					+ "\n";
			resultMessage += "Lambda upper bound: " + paraLambdaUpperBound
					+ "\n";
			resultMessage += "Lambda step length: " + paraLambdaStepLength
					+ "\n";
			resultMessage += "Lambda value counts: " + numberOfLambdas + "\n";
			resultMessage += "Experiment counts: " + paraExperiments + "\n\n";
			resultMessage += "lambda" + "\t" + "Win times" + "\t"
					+ "average cost\n";
			for (int i = 0; i < numberOfLambdas; i++) {
				averageCost[i] = averageCost[i] / paraExperiments;
				resultMessage += lambdaArray[i] + "\t" + winTimesArray[i]
						+ "\t" + averageCost[i] + "\n";
			}// for i
			averageCost[numberOfLambdas] = averageCost[numberOfLambdas]
					/ paraExperiments;
			resultMessage += "CC-Tree" + "\t" + winTimesArray[numberOfLambdas]
					+ "\t" + averageCost[numberOfLambdas] + "\n\n";
			// resultMessage += "Algorithm running time: " + runTime +
			// " ms";

			System.out.println(resultMessage);

		}// Of for m

		// Record the ending time.
		long time2 = System.currentTimeMillis();

		// Compute the execution time.
		long runTime = time2 - time1;

		System.out.println("Run-time£º" + runTime + "ms");

		return resultMessage;
	}// Of batchDecisionTreeMisCostInfluence

	/**
	 ************************* 
	 * Evaluate the decistion tree on the test data set.
	 * 
	 * @param paraCostJ48
	 *            The cost J48 to be evaluted.
	 * @param paraTest
	 *            The testing data set.
	 * @return The probability of the correct classification on the test data
	 *         set.
	 ************************* 
	 */
	public double evaluateDecisionTree(CostJ48 paraCostJ48, Instances paraTest) {
		int predictClassValue = 0;
		int actualClassValue = 0;
		double totalMisCost = 0;
		int classIndex = paraTest.classIndex();
		correctedClassifyProb = 0;

		// Classify objects in testing data set using generated decision trees.
		// Record the number of objects classified correctly and compute the
		// classification accuracy.
		for (int i = 0; i < paraTest.numInstances(); i++) {
			// System.out.println("The instance: " + paraTest.instance(i));//
			actualClassValue = (int) paraTest.instance(i).value(classIndex);
			try {
				predictClassValue = (int) paraCostJ48.classifyInstance(paraTest
						.instance(i));
			} catch (Exception e) {
				e.printStackTrace();
			}// Of try-catch
				// System.out.println("Actual class£º" + actualClassValue);
				// System.out.println("Predict class£º" + predictClassValue);
			if (actualClassValue == predictClassValue) {
				correctedClassifyProb++;
			} else {
				totalMisCost += costMatrix[(int) paraTest.instance(i).value(
						classIndex)][(int) predictClassValue];
			}// Of if - else
		}// Of for i

		// Compute the classification accuracy.
		correctedClassifyProb = correctedClassifyProb / paraTest.numInstances();
		System.out
				.println("Classification accuracy = " + correctedClassifyProb);//

		return totalMisCost;
	}// Of testDecisionTree

	/**
	 ************************* 
	 * Evaluate the cost of the generated decision tree.
	 * 
	 * @param paraCostJ48
	 *            The cost J48 to be evaluted.
	 * @param paraTest
	 *            The testing data set.
	 * @return The average cost including the test cost and the
	 *         misclassification cost.
	 ************************* 
	 */
	public double evaluateCost(CostJ48 paraCostJ48, Instances paraTest) {
		int actualClassValue = 0;
		int predictClassValue = 0;
		int totalTestCost = 0;
		int totalMisCost = 0;
		int totalCost = 0;
		int classIndex = paraTest.classIndex();

		// Initialize the selected attribute array.
		for (int k = 0; k < attributeTested.length; k++) {
			attributeTested[k] = false;
		}// Of for k

		for (int i = 0; i < paraTest.numInstances(); i++) {
			actualClassValue = (int) paraTest.instance(i).value(classIndex);
			try {
				predictClassValue = (int) paraCostJ48.classifyInstance(paraTest
						.instance(i));
			} catch (Exception e) {
				e.printStackTrace();
			}// Of try-catch

			// Compute the total test cost.
			totalTestCost += computeTestCost(attributeTested);

			// Initialize the selected array.
			for (int k = 0; k < attributeTested.length; k++) {
				attributeTested[k] = false;
			}// Of for k

			// Compute the total misclassifiation cost
			totalMisCost += costMatrix[actualClassValue][predictClassValue];
		}// Of for i

		// Compute the total cost including test costs and misclassfication
		// costs.
		totalCost = totalTestCost + totalMisCost;

		return (double) totalCost / paraTest.numInstances();

	}// Of evaluateCost

	/**
	 ************************* 
	 * Copy the test cost array
	 * 
	 * @param paraArray
	 *            The source array to be copied.
	 ************************* 
	 */
	private void copyArray(int[] paraArray) {
		individualTestCostArray = new int[paraArray.length];
		for (int i = 0; i < paraArray.length; i++) {
			individualTestCostArray[i] = paraArray[i];
		}// Of for i
	}// Of copyArray

	/**
	 ************************* 
	 * Compute the total test cost according to the attribute array
	 * 
	 * @param paraAttribute
	 *            The boolean array representing selected attributes.
	 * @return The test cost.
	 ************************* 
	 */
	private int computeTestCost(boolean[] paraAttribute) {
		int testcost = 0;
		for (int i = 0; i < paraAttribute.length; i++) {
			if (paraAttribute[i]) {
				testcost += currentCostSensitiveDecisionSystem
						.getIndividualTestCost(i);
			}// Of if
		}// Of for i
		return testcost;
	}// Of computeTestCost

	/**
	 ********************** 
	 * Post prune
	 * 
	 * @param paraRoot
	 *            The classifier tree root.
	 ********************** 
	 */
	public void postPrune(ClassifierTree paraRoot) {
		if (CostJ48.dataType == false) {
			postPruneSymbolic(paraRoot);
		} else if (CostJ48.dataType == true) {
			for (int i = 0; i < attributeTestedCounts.length; i++) {
				attributeTestedCounts[i] = 0;
			}// Of for i
			postPruneNumeric(paraRoot);
		} else {
			return;
		}// Of if-else
	}// Of postPrune

	/**
	 ********************** 
	 * Post prune the symbolic cost-sensitive decision tree.
	 * 
	 * @param paraRoot
	 *            The classifier tree root.
	 ********************** 
	 */
	public void postPruneSymbolic(ClassifierTree paraRoot) {
		if (paraRoot.isLeaf()) {
			return;
		} else {
			// Otherwise, it is not a leaf node. Post prune in post order.
			for (int i = 0; i < paraRoot.m_sons.length; i++) {
				postPruneSymbolic(paraRoot.m_sons[i]);
			}// Of for i
				// Compare the average cost between testing and no testing.
			CostJ48 tempCostJ48 = new CostJ48(paraRoot, CostJ48.lambda, true,
					0, 2, false, false, 0, false, false, true, 0, 0);
			double unPruneAverageCost = evaluateCost(tempCostJ48,
					paraRoot.m_train);// Compute the average cost of obtained
										// decision tree.
			paraRoot.setLeaf(true);// Try to prune.
			// Compute the average cost after pruning.
			double prunedAverageCost = evaluateCost(tempCostJ48,
					paraRoot.m_train);
			// Cancel pruning if the average cost is small before pruning.
			if (unPruneAverageCost < prunedAverageCost) {
				paraRoot.setLeaf(false);
			}// Of if
		}// Of if else
	}// Of postPruneSymbolic

	/**
	 ********************** 
	 * Post prune the numeric cost-sensitive decision tree.<br>
	 * 
	 * @param paraRoot
	 *            The classifier tree root.
	 ********************** 
	 */
	public void postPruneNumeric(ClassifierTree paraRoot) {
		if (paraRoot.isLeaf()) {
			return;
		} else {// Otherwise, it is not a leaf node. Post prune in post order.
			attributeTestedCounts[paraRoot.m_localModel.attIndex()]++;
			for (int i = 0; i < paraRoot.m_sons.length; i++) {
				postPruneNumeric(paraRoot.m_sons[i]);
			}// Of for i
				// When the current attribute is tested before.
			if (attributeTestedCounts[paraRoot.m_localModel.attIndex()] >= 2) {
				attributeTestedCounts[paraRoot.m_localModel.attIndex()]--;
				CostJ48 tempCostJ48 = new CostJ48(paraRoot, CostJ48.lambda,
						true, 0, 2, false, false, 0, false, false, true, 0, 0);
				// Compute the average cost of decision tree before pruning.
				double unPruneAverageCost = evaluateCost(tempCostJ48,
						paraRoot.m_train);
				// Try to prune.
				paraRoot.setLeaf(true);
				// Compute the average cost after pruning.
				double prunedAverageCost = evaluateCost(tempCostJ48,
						paraRoot.m_train)
						+ currentCostSensitiveDecisionSystem
								.getIndividualTestCost(paraRoot.m_localModel
										.attIndex());
				// Cancel pruning if the average cost is small before pruning.
				if (unPruneAverageCost < prunedAverageCost) {
					paraRoot.setLeaf(false);
				}// Of if
				return;
			}// Of if
				// When the current attribute is not tested before.
			CostJ48 tempCostJ48 = new CostJ48(paraRoot, CostJ48.lambda, true,
					0, 2, false, false, 0, false, false, true, 0, 0);
			// Compute the average cost before pruning.
			double unPruneAverageCost = evaluateCost(tempCostJ48,
					paraRoot.m_train);
			// Try to prune.
			paraRoot.setLeaf(true);
			// Compute the average cost after pruning.
			double prunedAverageCost = evaluateCost(tempCostJ48,
					paraRoot.m_train);
			// Cancel pruning if the average cost is small before pruning.
			if (unPruneAverageCost < prunedAverageCost) {
				paraRoot.setLeaf(false);
			}// Of if
			attributeTestedCounts[paraRoot.m_localModel.attIndex()]--;
		}// Of if else

	}// Of postPruneNumeric

	/**
	 ********************** 
	 * Post prune the numeric cost-sensitive decision tree.<br>
	 * Half post-pruning
	 * 
	 * @return A boolean representing if the tree is pruned.
	 ********************** 
	 */
	public boolean postPruneNumericHalf() {
		boolean isPrune = false;
		if (isPrune) {
			isPrune = true;
		}// Of if
		return isPrune;
	}// Of postPruneNumericHalf

	/**
	 ************************* 
	 * Set the train set proportion
	 * 
	 * @param paraTrainsetProportion
	 *            The proportion of the training set.
	 ************************* 
	 */
	public void setTrainsetProportion(double paraTrainsetProportion) {
		trainsetProportion = paraTrainsetProportion;
	}// Of setTrainsetProportion
}// Of class BuildAndTestTreeCostC45