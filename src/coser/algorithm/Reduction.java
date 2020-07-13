package coser.algorithm;

import java.text.NumberFormat;

import coser.common.*;
import coser.datamodel.decisionsystem.*;
import coser.project.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Reduction.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Half done.<br>
 * Written time: March 17, 2011. <br>
 * Last modify time: May 4, 2011.
 */
public class Reduction {
	/**
	 *************************** 
	 * Batch reduction to obtain minimal reducts. Test costs are generated
	 * however not considered in reduction.
	 * 
	 * @param paraExperiments
	 *            the number of experiments to undertake. Each time the dataset
	 *            have different test-cost settings.
	 * @param paraShowDetail
	 *            show the detail of the program running. Only valid if
	 *            paraExperiments is not greater than 50.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchMinimalReduct(int paraExperiments,
			boolean paraShowDetail) throws Exception {
		String message = "";
		try {
			// Use another name to make statements shorter.
			TestCostNominalDecisionSystem currentSystem = CoserProject.currentProject.currentTcNds;

			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.";

			currentSystem.initializeForReduction();
			currentSystem.setWeighting(false);
			// currentSystem.readAllReducts();//Move to NewProjectDialog

			// boolean[] reduct = null;

			int findingOptimalCount = 0;
			double exceedingFactor = 0;
			double maximalExceedingFactor = 0;
			double exceedingFactorSum = 0;

			for (int i = 0; i < paraExperiments; i++) {
				currentSystem.refreshTestCost();
				currentSystem.computeOptimalTestCost();

				currentSystem.entropyBasedReduction();
				String reductString = currentSystem.getReductString();

				// Let's have a look.
				exceedingFactor = currentSystem.computeExceedingFactor();
				if (maximalExceedingFactor < exceedingFactor) {
					maximalExceedingFactor = exceedingFactor;
				}// Of if
				if (paraShowDetail) {
					message += "\r\nThe cost vector is: "
							+ SimpleTool.intArrayToString(
									currentSystem.getIndividualTestCostArray(),
									',');
					message += "\r\nThe reduct is: " + reductString;
					message += " with test-cost: "
							+ currentSystem.getReductTotalTestCost();
					message += "\r\nThe optimal cost is: "
							+ currentSystem.getOptimalReductTestCost();
					message += " with exceeding factor: "
							+ currentSystem.computeExceedingFactor();
				}// Of if paraShowDetail
				if (currentSystem.isCurrentReductOptimal()) {
					findingOptimalCount++;
				}// Of if
				exceedingFactorSum += exceedingFactor;

				// Show the process in the console.
				if ((i + 1) % 50 == 0) {
					System.out.print("" + (i + 1) + "\t");
				}// Of if
			}// Of for i
			message += "\r\nResults/Finding optimal factor\t"
					+ "Maximal exceeding factor\tAverage exceeding factor\r\n";
			message += "\t" + (0.0 + findingOptimalCount) / paraExperiments;
			message += "\t" + maximalExceedingFactor;
			message += "\t" + (exceedingFactorSum / paraExperiments) + "\r\n";
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchMinimalReduct(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchMinimalReduct

	/**
	 *************************** 
	 * Batch reduction to obtain minimal test-cost reducts. Reduction algorithms
	 * are implemented in decision systems, and this method invoke them many
	 * times with different test-cost settings to generate statistical results.
	 * 
	 * @param paraLambdaUpperBound
	 *            the upper bound of lambda, non-positive.
	 * @param paraLambdaLowerBound
	 *            the lower bound of lambda.
	 * @param paraLambdaStepLength
	 *            the step length of labda. For example, if the upper and lower
	 *            bounds are -0.5 and -2 respectively, and the step length is
	 *            0.5, then the lambda will have values -0.5, -1.0, -1.5, and
	 *            -2.0. Fan suggests to set the lower bound to -2.1 since the
	 *            computation of double values are not accurate.
	 * @param paraExperiments
	 *            the number of experiments to undertake. Each time the dataset
	 *            have different test-cost settings.
	 * @param paraShowDetail
	 *            show the detail of the program running. Only valid if
	 *            paraExperiments is not greater than 50.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchMinimalTestCostReduction(
			double paraLambdaUpperBound, double paraLambdaLowerBound,
			double paraLambdaStepLength, int paraExperiments,
			boolean paraShowDetail) throws Exception {
		String message = "";
		try {
			// Use another name to make statements shorter.
			TestCostNominalDecisionSystem currentSystem = CoserProject.currentProject.currentTcNds;

			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.";

			int numberOfLambdas = 1 + (int) ((paraLambdaUpperBound - paraLambdaLowerBound) / paraLambdaStepLength);
			if (numberOfLambdas < 1) {
				message = "lambda lower bound: " + paraLambdaLowerBound
						+ " is greater than the upper bound"
						+ paraLambdaUpperBound;
				throw new Exception(
						"Error occurred in Reduction.batchMinimalTestCostReduction(): \r\n"
								+ message);
			}// Of if
			double[] lambdaArray = new double[numberOfLambdas];
			for (int i = 0; i < numberOfLambdas; i++) {
				lambdaArray[i] = paraLambdaUpperBound - i
						* paraLambdaStepLength;
			}// Of for i

			currentSystem.initializeForReduction();
			currentSystem.setWeighting(true);
			int overallFindingOptimalCount = 0;
			// The minimal one among all labda settings,
			// while the maximal one among all test-cost settings.
			double minimalMaximalExceedingFactor = 0;
			double overallExceedingFactorSum = 0;

			int[] findingOptimalCount = new int[numberOfLambdas];
			double exceedingFactor = 0;
			double[] maximalExceedingFactor = new double[numberOfLambdas];
			double[] exceedingFactorSum = new double[numberOfLambdas];

			for (int i = 0; i < paraExperiments; i++) {
				currentSystem.refreshTestCost();
				currentSystem.computeOptimalTestCost();

				boolean currentTestCostSettingFindingOptimal = false;
				double currentRoundMinimalExceedingFactor = Double.MAX_VALUE;
				for (int j = 0; j < numberOfLambdas; j++) {
					currentSystem.entropyBasedLambdaWeightedReduction(
							lambdaArray[j], false);
					String reductString = currentSystem.getReductString();

					// Let's have a look.
					exceedingFactor = currentSystem.computeExceedingFactor();
					if (maximalExceedingFactor[j] < exceedingFactor) {
						maximalExceedingFactor[j] = exceedingFactor;
					}// Of if
					if (currentRoundMinimalExceedingFactor > exceedingFactor) {
						currentRoundMinimalExceedingFactor = exceedingFactor;
					}// Of if

					if (paraShowDetail) {
						message += "\r\nThe cost vector is: "
								+ SimpleTool.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
						message += "\r\nThe reduct is: " + reductString;
						message += " with test-cost: "
								+ currentSystem.getReductTotalTestCost();
						message += "\r\nThe optimal cost is: "
								+ currentSystem.getOptimalReductTestCost();
						message += " with exceeding factor: "
								+ currentSystem.computeExceedingFactor();
					}// Of if paraShowDetail
					if (currentSystem.isCurrentReductOptimal()) {
						findingOptimalCount[j]++;
						currentTestCostSettingFindingOptimal = true;
					}// Of if
					exceedingFactorSum[j] += exceedingFactor;
				}// Of for j

				if (currentTestCostSettingFindingOptimal) {
					overallFindingOptimalCount++;
				}// of if
				if (currentRoundMinimalExceedingFactor > minimalMaximalExceedingFactor) {
					minimalMaximalExceedingFactor = currentRoundMinimalExceedingFactor;
				}// Of if
				overallExceedingFactorSum += currentRoundMinimalExceedingFactor;

				// Show the process in the console.
				if ((i + 1) % 50 == 0) {
					System.out.print("" + (i + 1) + "\t");
				}// Of if
			}// Of for i
			message += "\r\nResults/lambda\tFinding optimal factor\t"
					+ "Maximal exceeding factor\tAverage exceeding factor\r\n";
			for (int i = 0; i < numberOfLambdas; i++) {
				message += "" + lambdaArray[i];
				message += "\t" + (0.0 + findingOptimalCount[i])
						/ paraExperiments;
				message += "\t" + maximalExceedingFactor[i];
				message += "\t" + (exceedingFactorSum[i] / paraExperiments)
						+ "\r\n";
			}// Of for i
			message += "\r\nUse the minimal one among all lambda settings:\r\n "
					+ "\t"
					+ (0.0 + overallFindingOptimalCount)
					/ paraExperiments
					+ "\t"
					+ minimalMaximalExceedingFactor
					+ "\t"
					+ (overallExceedingFactorSum / paraExperiments)
					+ "\r\n";
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchMinimalTestCostReduction(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchMinimalTestCostReduction

	/**
	 *************************** 
	 * Batch reduction to obtain test-cost constraint reducts. Reduction
	 * algorithms are implemented in decision systems, and this method invoke
	 * them many times with different test-cost settings to generate statistical
	 * results. Only one parameter is different from that of
	 * batchMinimalTestCostReduction().
	 * 
	 * @param paraBudgetFactor
	 *            control the budget insufficient to generate a reduct.
	 * @return the message showing the result.
	 * @see #batchMinimalTestCostReduction(double, double, double, int, boolean)
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchTestCostConstraintReduction(
			boolean paraWeighting, double paraLambdaUpperBound,
			double paraLambdaLowerBound, double paraLambdaStepLength,
			double paraBudgetFactor, int paraExperiments, boolean paraShowDetail)
			throws Exception {
		String message = "";
		// Avoid implementing another method.
		if (!paraWeighting) {
			paraLambdaUpperBound = 0;
			paraLambdaLowerBound = -0.1;
			paraLambdaStepLength = 1;
		}// Of if

		try {
			// Use another name to make statements shorter.
			TestCostConstraintNominalDecisionSystem currentSystem = new TestCostConstraintNominalDecisionSystem(
					CoserProject.currentProject.currentTcNds);

			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.";

			int numberOfLambdas = 1 + (int) ((paraLambdaUpperBound - paraLambdaLowerBound) / paraLambdaStepLength);
			if (numberOfLambdas < 1) {
				message = "lambda lower bound: " + paraLambdaLowerBound
						+ " is greater than the upper bound"
						+ paraLambdaUpperBound;
				throw new Exception(
						"Error occurred in Reduction.batchTestCostConstraintReduction(): \r\n"
								+ message);
			}// Of if
			double[] lambdaArray = new double[numberOfLambdas];
			for (int i = 0; i < numberOfLambdas; i++) {
				lambdaArray[i] = paraLambdaUpperBound - i
						* paraLambdaStepLength;
			}// Of for i

			currentSystem.initializeForReduction();
			currentSystem.setWeighting(paraWeighting);
			int testCostConstraint = 0;
			String partialReductString = "";
			int[] findingLocalBestCount = new int[numberOfLambdas];
			double[] tempEntropyArray = new double[numberOfLambdas];

			for (int i = 0; i < paraExperiments; i++) {
				currentSystem.refreshTestCost();

				currentSystem.computeOptimalTestCost();
				testCostConstraint = (int) (currentSystem
						.getOptimalReductTestCost() * paraBudgetFactor);

				double currenRoundBestEntropy = 100;
				for (int j = 0; j < numberOfLambdas; j++) {
					currentSystem
							.entropyBasedLambdaWeightedConstraintReduction(
									lambdaArray[j], testCostConstraint);
					tempEntropyArray[j] = currentSystem.getSubreductEntropy();
					if (tempEntropyArray[j] < currenRoundBestEntropy) {
						currenRoundBestEntropy = tempEntropyArray[j];
					}// Of if

					if (paraShowDetail) {
						partialReductString = currentSystem
								.getSubreductString();
						message += "\r\nThe cost vector is: "
								+ SimpleTool.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
						message += "\r\nThe sub-reduct is: "
								+ partialReductString;
						message += " with test-cost: "
								+ currentSystem.getSubreductTestCost();
						message += " the entropy is: "
								+ currentSystem.getSubreductEntropy();
					}// Of if paraShowDetail
				}// Of for j

				for (int j = 0; j < numberOfLambdas; j++) {
					if (Math.abs(tempEntropyArray[j] - currenRoundBestEntropy) < 1e-6) {
						findingLocalBestCount[j]++;
					}// Of if
				}// Of for

				// Show the process in the console.
				if ((i + 1) % 50 == 0) {
					System.out.print("" + (i + 1) + "\t");
				}// Of if
			}// Of for i
			message += "\r\nResults/lambda\tFinding optimal factor\r\n";
			for (int i = 0; i < numberOfLambdas; i++) {
				message += "" + lambdaArray[i];
				message += "\t" + (0.0 + findingLocalBestCount[i])
						/ paraExperiments + "\r\n";
			}// Of for i
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchTestCostConstraintReduction(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchTestCostConstraintReduction

	/**
	 *************************** 
	 * Singular reduction to obtain an optimal sub-reduct.
	 * 
	 * @param paraAlgorithm
	 *            the exhaustive algorithm.
	 * @param paraMeasure
	 *            the measure to evaluate the decision system consistency.
	 * @param paraBudgetFactor
	 *            control the budget insufficient to generate a reduct.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String exhaustiveTestCostConstraintReduction(
			int paraAlgorithm, int paraMeasure, double paraBudgetFactor)
			throws Exception {
		String message = "";
		try {
			// Use another name to make statements shorter.
			TestCostConstraintNominalDecisionSystem currentSystem = new TestCostConstraintNominalDecisionSystem(
					CoserProject.currentProject.currentTcNds);

			currentSystem.initializeForReduction();
			currentSystem.refreshTestCost();
			currentSystem.setMeasure(paraMeasure);

			int optimalTestCost = currentSystem.computeOptimalTestCost();
			int testCostConstraint = (int) (optimalTestCost * paraBudgetFactor);
			if (testCostConstraint == 0) {
				throw new Exception(
						"Error occurred in Reduction.exhaustiveTestCostConstraintReduction()\r\n"
								+ "The optimal test cost is "
								+ optimalTestCost
								+ " and the constraint is 0.\r\n"
								+ "The test costs are "
								+ SimpleTool.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ','));
			}// Of if
			if (paraAlgorithm == TestCostConstraintNominalDecisionSystem.SESRA) {
				message += currentSystem
						.constraintReductionSESRA(testCostConstraint);
			} else if (paraAlgorithm == TestCostConstraintNominalDecisionSystem.SESRAstar) {
				message += currentSystem
						.constraintReductionSESRAstar(testCostConstraint);
			} else if (paraAlgorithm == TestCostConstraintNominalDecisionSystem.BASS) {
				message += currentSystem
						.constraintReductionBASS(testCostConstraint);
			} else if (paraAlgorithm == TestCostConstraintNominalDecisionSystem.PURE_BACKTRACK) {
				message += currentSystem
						.constraintReductionBacktrack(testCostConstraint);
			} else {
				message += "\r\n====SESRA backtrack===="
						+ currentSystem
								.constraintReductionSESRA(testCostConstraint);
				int sesraCost = currentSystem.getOptimalSubreductCost();
				int sesraConsistency = currentSystem
						.getOptimalSubreductConsistency();
				message += "\r\n====SESRAstar backtrack===="
						+ currentSystem
								.constraintReductionSESRAstar(testCostConstraint);
				int sesraStarCost = currentSystem.getOptimalSubreductCost();
				int sesraStarConsistency = currentSystem
						.getOptimalSubreductConsistency();
				message += "\r\n====BASS===="
						+ currentSystem
								.constraintReductionBASS(testCostConstraint);
				int bassCost = currentSystem.getOptimalSubreductCost();
				int bassConsistency = currentSystem
						.getOptimalSubreductConsistency();
				message += "\r\n====PURE_BACKTRACK===="
						+ currentSystem
								.constraintReductionBacktrack(testCostConstraint);
				int backtrackCost = currentSystem.getOptimalSubreductCost();
				int backtrackConsistency = currentSystem
						.getOptimalSubreductConsistency();
				if ((sesraStarCost != sesraCost) || (bassCost != sesraCost)
						|| (backtrackCost != sesraCost)) {
					throw new Exception(
							"The test cost of SESRA, SESRA Star, BASS, and BACKTRACK are "
									+ sesraCost + ", " + sesraStarCost + ", "
									+ bassCost + ", " + backtrackCost + ".");
				}// Of if
				if ((sesraStarConsistency != sesraConsistency)
						|| (bassConsistency != sesraConsistency)
						|| (backtrackConsistency != sesraConsistency)) {
					throw new Exception(
							"The consistency of SESRA, SESRA Star, BASS, and BACKTRACK are "
									+ sesraConsistency + ", "
									+ sesraStarConsistency + ", "
									+ bassConsistency + ", "
									+ backtrackConsistency + ".");
				}// Of if
			}// Of if
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.exhaustiveTestCostConstraintReduction(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of exhaustiveTestCostConstraintReduction

	/**
	 *************************** 
	 * Exhausitve reduction to obtain optimal sub-reduct for different settings.
	 * 
	 * @param paraAlgorithm
	 *            the exhaustive algorithm.
	 * @param paraMeasure
	 *            the measure to evaluate the decision system consistency.
	 * @param paraBudgetFactor
	 *            control the budget insufficient to generate a reduct.
	 * @param paraNumberOfExperiments
	 *            how many times should the algorithms run.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchExhaustiveTestCostConstraintReduction(
			int paraAlgorithm, int paraMeasure, double paraBudgetFactor,
			int paraNumberOfExperiments) throws Exception {
		String message = "";
		int[] totalArray = new int[TestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH];
		try {
			// Use another name to make statements shorter.
			TestCostConstraintNominalDecisionSystem currentSystem = new TestCostConstraintNominalDecisionSystem(
					CoserProject.currentProject.currentTcNds);
			currentSystem.setMeasure(paraMeasure);

			message += "[0]: the number of test sets satisfying the constraint.\r\n";
			message += "[1]: the number of test sets with consistency checked.\r\n";
			message += "[2]: the number of maximal consistency test sets.\r\n";
			message += "[3]: the number of minimal cost test sets.\r\n";
			message += "[4]: the time for candidates building.\r\n";
			message += "[5]: the time for computing consistency.\r\n";
			message += "[6]: the time for the execution.\r\n";
			message += "[7]: the number of reducts. Only valid for SESRAstar.\r\n";
			message += "[8]: backtrack steps.\r\n";
			message += "[9]: the length of the sub-reduct.\r\n";

			// Only one algorithm
			if (paraAlgorithm <= TestCostConstraintNominalDecisionSystem.PURE_BACKTRACK) {
				long[][] resultsArray = new long[paraNumberOfExperiments][TestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH];
				for (int i = 0; i < paraNumberOfExperiments; i++) {
					System.out.print("#" + i + "\t");

					currentSystem.initializeForReduction();
					currentSystem.refreshTestCost();

					int optimalTestCost = currentSystem
							.computeOptimalTestCost();
					int testCostConstraint = (int) (optimalTestCost * paraBudgetFactor);

					if (testCostConstraint == 0) {
						throw new Exception(
								"Error occurred in Reduction.batchExhaustiveTestCostConstraintReduction()\r\n"
										+ "The optimal test cost is "
										+ optimalTestCost
										+ " and the constraint is 0.\r\n"
										+ "The test costs are "
										+ SimpleTool.intArrayToString(
												currentSystem
														.getIndividualTestCostArray(),
												','));
					}// Of if

					// Algorithm choice.
					if (paraAlgorithm == TestCostConstraintNominalDecisionSystem.SESRA) {
						currentSystem
								.constraintReductionSESRA(testCostConstraint);
					} else if (paraAlgorithm == TestCostConstraintNominalDecisionSystem.SESRAstar) {
						currentSystem
								.constraintReductionSESRAstar(testCostConstraint);
					} else if (paraAlgorithm == TestCostConstraintNominalDecisionSystem.BASS) {
						currentSystem
								.constraintReductionBASS(testCostConstraint);
					} else if (paraAlgorithm == TestCostConstraintNominalDecisionSystem.PURE_BACKTRACK) {
						currentSystem
								.constraintReductionBacktrack(testCostConstraint);
					}// Of if

					long[] singularResultsArray = currentSystem
							.getExhaustiveSubreductResults();
					message += "\r\n";
					for (int j = 0; j < TestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
						resultsArray[i][j] = singularResultsArray[j];
						totalArray[j] += resultsArray[i][j];
						message += "" + resultsArray[i][j] + "\t";
					}// Of for j
				}// Of for i

				message += "\r\nAverage values:\r\n";
				for (int j = 0; j < TestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
					message += "" + (totalArray[j] + 0.0)
							/ paraNumberOfExperiments + "\t";
				}// Of for j

				return message;
			}// Of if

			// For all algorithms.
			long[] singularResultsArray;
			// Just to make the name shorter.
			int tempLength = TestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH;
			long[][] resultsArrayForSESRA = new long[paraNumberOfExperiments][tempLength];
			long[][] resultsArrayForSESRAstar = new long[paraNumberOfExperiments][tempLength];
			long[][] resultsArrayForBASS = new long[paraNumberOfExperiments][tempLength];
			long[][] resultsArrayForBacktrack = new long[paraNumberOfExperiments][tempLength];

			long[] totalArrayForSESRA = new long[tempLength];
			long[] totalArrayForSESRAstar = new long[tempLength];
			long[] totalArrayForBASS = new long[tempLength];
			long[] totalArrayForBacktrack = new long[tempLength];

			// int numberOfInstances = currentSystem.numInstances();

			for (int i = 0; i < paraNumberOfExperiments; i++) {
				System.out.print("#" + i + "\t");
				currentSystem.initializeForReduction();
				currentSystem.refreshTestCost();

				int optimalTestCost = currentSystem.computeOptimalTestCost();
				int testCostConstraint = (int) (optimalTestCost * paraBudgetFactor);

				// ///////////////For SESRA
				currentSystem.constraintReductionSESRA(testCostConstraint);
				singularResultsArray = currentSystem
						.getExhaustiveSubreductResults();
				for (int j = 0; j < TestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
					resultsArrayForSESRA[i][j] = singularResultsArray[j];
					totalArrayForSESRA[j] += resultsArrayForSESRA[i][j];
				}// Of for j

				// ///////////////For SESRAstar
				currentSystem.constraintReductionSESRAstar(testCostConstraint);
				singularResultsArray = currentSystem
						.getExhaustiveSubreductResults();
				for (int j = 0; j < TestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
					resultsArrayForSESRAstar[i][j] = singularResultsArray[j];
					totalArrayForSESRAstar[j] += resultsArrayForSESRAstar[i][j];
				}// Of for j

				// ///////////////For BASS
				currentSystem.constraintReductionBASS(testCostConstraint);
				singularResultsArray = currentSystem
						.getExhaustiveSubreductResults();
				for (int j = 0; j < TestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
					resultsArrayForBASS[i][j] = singularResultsArray[j];
					totalArrayForBASS[j] += resultsArrayForBASS[i][j];
				}// Of for j

				// ///////////////For Backtrack
				currentSystem.constraintReductionBacktrack(testCostConstraint);
				singularResultsArray = currentSystem
						.getExhaustiveSubreductResults();
				for (int j = 0; j < TestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
					resultsArrayForBacktrack[i][j] = singularResultsArray[j];
					totalArrayForBacktrack[j] += resultsArrayForBacktrack[i][j];
				}// Of for j
			}// Of for i

			message += "\r\nAverage values:\r\n";
			message += "\r\nSESRA:\r\n";
			for (int j = 0; j < TestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
				message += "" + (totalArrayForSESRA[j] + 0.0)
						/ paraNumberOfExperiments + "\t";
			}// Of for j
			message += "\r\nSESRAstar:\r\n";
			for (int j = 0; j < TestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
				message += "" + (totalArrayForSESRAstar[j] + 0.0)
						/ paraNumberOfExperiments + "\t";
			}// Of for j
			message += "\r\nBASS:\r\n";
			for (int j = 0; j < TestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
				message += "" + (totalArrayForBASS[j] + 0.0)
						/ paraNumberOfExperiments + "\t";
			}// Of for j
			message += "\r\nBacktrack:\r\n";
			for (int j = 0; j < TestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
				message += "" + (totalArrayForBacktrack[j] + 0.0)
						/ paraNumberOfExperiments + "\t";
			}// Of for j
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchExhaustiveTestCostConstraintReduction(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchExhaustiveTestCostConstraintReduction

	/**
	 *************************** 
	 * Batch reduction to obtain minimal test-cost reducts. Reduction algorithms
	 * are implemented in decision systems, and this method invoke them many
	 * times with different test-cost settings to generate statistical results.
	 * 
	 * @param paraLambdaUpperBound
	 *            the upper bound of lambda, non-positive.
	 * @param paraLambdaLowerBound
	 *            the lower bound of lambda.
	 * @param paraLambdaStepLength
	 *            the step length of labda. For example, if the upper and lower
	 *            bounds are -0.5 and -2 respectively, and the step length is
	 *            0.5, then the lambda will have values -0.5, -1.0, -1.5, and
	 *            -2.0. Fan suggests to set the lower bound to -2.1 since the
	 *            computation of double values are not accurate.
	 * @param paraExperiments
	 *            the number of experiments to undertake. Each time the dataset
	 *            have different test-cost settings.
	 * @param paraShowDetail
	 *            show the detail of the program running. Only valid if
	 *            paraExperiments is not greater than 50.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchMinimalTestCostErrorRangeReduction(
			double paraLambdaUpperBound, double paraLambdaLowerBound,
			double paraLambdaStepLength, int paraExperiments,
			boolean paraShowDetail) throws Exception {
		String message = "";
		try {
			// Use another name to make statements shorter.
			TestCostDecisionSystemErrorRange currentSystem = CoserProject.currentProject.currentTcDsEr;

			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "The data set has " + currentSystem.numInstances()
					+ " instances and " + (currentSystem.numAttributes() - 1)
					+ " tests.\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.";

			int numberOfLambdas = 1 + (int) ((paraLambdaUpperBound - paraLambdaLowerBound) / paraLambdaStepLength);
			if (numberOfLambdas < 1) {
				message = "lambda lower bound: " + paraLambdaLowerBound
						+ " is greater than the upper bound"
						+ paraLambdaUpperBound;
				throw new Exception(
						"Error occurred in Reduction.batchMinimalTestCostErrorRangeReduction(): \r\n"
								+ message);
			}// Of if
			double[] lambdaArray = new double[numberOfLambdas];
			for (int i = 0; i < numberOfLambdas; i++) {
				lambdaArray[i] = paraLambdaUpperBound - i
						* paraLambdaStepLength;
			}// Of for i

			currentSystem.initializeForReduction();
			int overallFindingOptimalCount = 0;
			// The minimal one among all labda settings,
			// while the maximal one among all test-cost settings.
			double minimalMaximalExceedingFactor = 0;
			double overallExceedingFactorSum = 0;

			int[] findingOptimalCount = new int[numberOfLambdas];
			double exceedingFactor = 0;
			double[] maximalExceedingFactor = new double[numberOfLambdas];
			double[] exceedingFactorSum = new double[numberOfLambdas];

			for (int i = 0; i < paraExperiments; i++) {
				System.out.print("" + i + "\t");
				currentSystem.refreshTestCost();
				currentSystem.computeOptimalReductTestCostBacktrack();
				if (paraShowDetail) {
					String tempString = SimpleTool
							.booleanArrayToAttributeSetString(currentSystem
									.getOptimalReduct());
					message += "\r\n\r\nThe optimal reduct is: " + tempString;
				}// Of if

				boolean currentTestCostSettingFindingOptimal = false;
				double currentRoundMinimalExceedingFactor = Double.MAX_VALUE;
				for (int j = 0; j < numberOfLambdas; j++) {
					currentSystem
							.inconsistencyBasedLambdaReduction(lambdaArray[j]);
					String reductString = currentSystem.getReductString();

					// Let's have a look.
					exceedingFactor = currentSystem.computeExceedingFactor();
					if (exceedingFactor >= 9999.9) {
						message = "Exceeding factor is too big.\r\n"
								+ "The current reductString is: "
								+ reductString
								+ "\r\n"
								+ "The current test cost setting is: "
								+ SimpleTool.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',')
								+ "\r\n";
						throw new Exception(
								"Error occurred in Reduction.batchMinimalTestCostReduction(): \r\n"
										+ message);
					}// Of if

					if (maximalExceedingFactor[j] < exceedingFactor) {
						maximalExceedingFactor[j] = exceedingFactor;
					}// Of if
					if (currentRoundMinimalExceedingFactor > exceedingFactor) {
						currentRoundMinimalExceedingFactor = exceedingFactor;
					}// Of if

					if (paraShowDetail) {
						message += "\r\nThe cost vector is: "
								+ SimpleTool.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
						message += "\r\nThe reduct is: " + reductString;
						message += " with test-cost: "
								+ currentSystem.getReductTotalTestCost();
						message += "\r\nThe optimal cost is: "
								+ currentSystem.getOptimalReductTestCost();
						message += " with exceeding factor: "
								+ currentSystem.computeExceedingFactor();
					}// Of if paraShowDetail
					if (currentSystem.isCurrentReductOptimal()) {
						findingOptimalCount[j]++;
						currentTestCostSettingFindingOptimal = true;
					}// Of if
					exceedingFactorSum[j] += exceedingFactor;
				}// Of for j

				if (currentTestCostSettingFindingOptimal) {
					overallFindingOptimalCount++;
				}// of if
				if (currentRoundMinimalExceedingFactor > minimalMaximalExceedingFactor) {
					minimalMaximalExceedingFactor = currentRoundMinimalExceedingFactor;
				}// Of if
				overallExceedingFactorSum += currentRoundMinimalExceedingFactor;

				// Show the process in the console.
				if ((i + 1) % 50 == 0) {
					System.out.print("" + (i + 1) + "\t");
				}// Of if
			}// Of for i
			message += "\r\nResults/lambda\tFinding optimal factor\t"
					+ "Maximal exceeding factor\tAverage exceeding factor\r\n";
			for (int i = 0; i < numberOfLambdas; i++) {
				message += "" + lambdaArray[i];
				message += "\t" + (0.0 + findingOptimalCount[i])
						/ paraExperiments;
				message += "\t" + maximalExceedingFactor[i];
				message += "\t" + (exceedingFactorSum[i] / paraExperiments)
						+ "\r\n";
			}// Of for i
			message += "\r\nUse the minimal one among all lambda settings:\r\n "
					+ "\t"
					+ (0.0 + overallFindingOptimalCount)
					/ paraExperiments
					+ "\t"
					+ minimalMaximalExceedingFactor
					+ "\t"
					+ (overallExceedingFactorSum / paraExperiments)
					+ "\r\n";

		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchMinimalTestCostErrorRangeReduction(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchMinimalTestCostErrorRangeReduction

	/**
	 *************************** 
	 * Batch reduction to obtain minimal test-cost reducts. Reduction algorithms
	 * are implemented in decision systems, and this method invoke them many
	 * times with different test-cost settings to generate statistical results.
	 * 
	 * @param paraDeltaUpperBound
	 *            the upper bound of delta, non-positive.
	 * @param paraDeltaLowerBound
	 *            the lower bound of delta.
	 * @param paraDeltaStepLength
	 *            the step length of delta. For example, if the upper and lower
	 *            bounds are 2 and 0 respectively, and the step length is 0.25.
	 * @param paraExperiments
	 *            the number of experiments to undertake. Each time the dataset
	 *            have different test-cost settings.
	 * @param paraShowDetail
	 *            show the detail of the program running. Only valid if
	 *            paraExperiments is not greater than 50.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchMinimalTestCostNormalErrorReduction(
			double paraDeltaUpperBound, double paraDeltaLowerBound,
			double paraDeltaStepLength, int paraExperiments,
			boolean paraShowDetail) throws Exception {
		String message = "";
		try {
			// Use another name to make statements shorter.
			TestCostDecisionSystemNormalError currentSystem = CoserProject.currentProject.currentTcDsNe;

			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "The data set has " + currentSystem.numInstances()
					+ " instances and " + (currentSystem.numAttributes() - 1)
					+ " tests.\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.";

			int numberOfLambdas = 1 + (int) ((paraDeltaUpperBound - paraDeltaLowerBound) / paraDeltaStepLength);
			if (numberOfLambdas < 1) {
				message = "delta lower bound: " + paraDeltaLowerBound
						+ " is greater than the upper bound"
						+ paraDeltaUpperBound;
				throw new Exception(
						"Error occurred in Reduction.batchMinimalTestCostNormalErrorReduction(): \r\n"
								+ message);
			}// Of if
			double[] lambdaArray = new double[numberOfLambdas];
			for (int i = 0; i < numberOfLambdas; i++) {
				lambdaArray[i] = paraDeltaLowerBound + i * paraDeltaStepLength;
			}// Of for i

			currentSystem.initializeForReduction();

			int overallFindingOptimalCount = 0;
			double minimalMaximalExceedingFactor = 0;
			double overallExceedingFactorSum = 0;

			int[] findingOptimalCount = new int[numberOfLambdas];
			double exceedingFactor = 0;
			double[] maximalExceedingFactor = new double[numberOfLambdas];
			double[] exceedingFactorSum = new double[numberOfLambdas];

			for (int i = 0; i < paraExperiments; i++) {
				System.out.print("" + (paraExperiments - i) + "\t");
				currentSystem.refreshTestCost();
				currentSystem.computeOptimalReductTestCostBacktrack();
				if (paraShowDetail) {
					String tempString = SimpleTool
							.booleanArrayToAttributeSetString(currentSystem
									.getOptimalReduct());
					message += "\r\n\r\nThe optimal reduct is: " + tempString;
				}// Of if

				boolean currentTestCostSettingFindingOptimal = false;
				double currentRoundMinimalExceedingFactor = 10000;
				for (int j = 0; j < numberOfLambdas; j++) {
					currentSystem
							.inconsistencyBasedDeltaReduction(lambdaArray[j]);
					String reductString = currentSystem.getReductString();

					// Let's have a look.
					exceedingFactor = currentSystem.computeExceedingFactor();
					if (exceedingFactor >= 9999.9) {
						message = "Exceeding factor is too big.\r\n"
								+ "The current reductString is: "
								+ reductString
								+ "\r\n"
								+ "The current test cost setting is: "
								+ SimpleTool.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',')
								+ "\r\n";
						throw new Exception(
								"Error occurred in Reduction.batchMinimalTestCostReduction(): \r\n"
										+ message);
					}// Of if

					if (maximalExceedingFactor[j] < exceedingFactor) {
						maximalExceedingFactor[j] = exceedingFactor;
					}// Of if
					if (currentRoundMinimalExceedingFactor > exceedingFactor) {
						currentRoundMinimalExceedingFactor = exceedingFactor;
					}// Of if

					if (paraShowDetail) {
						message += "\r\nThe cost vector is: "
								+ SimpleTool.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
						message += "\r\nThe reduct is: " + reductString;
						message += " with test-cost: "
								+ currentSystem.getReductTotalTestCost();
						message += "\r\nThe optimal cost is: "
								+ currentSystem.getOptimalReductTestCost();
						message += " with exceeding factor: "
								+ currentSystem.computeExceedingFactor();
					}// Of if paraShowDetail
					if (currentSystem.isCurrentReductOptimal()) {
						findingOptimalCount[j]++;
						currentTestCostSettingFindingOptimal = true;
					}// Of if
					exceedingFactorSum[j] += exceedingFactor;
				}// Of for j

				if (currentTestCostSettingFindingOptimal) {
					overallFindingOptimalCount++;
				}// of if
				if (currentRoundMinimalExceedingFactor > minimalMaximalExceedingFactor) {
					minimalMaximalExceedingFactor = currentRoundMinimalExceedingFactor;
				}// Of if
				overallExceedingFactorSum += currentRoundMinimalExceedingFactor;

			}// Of for i
			message += "\r\nResults/lambda\tFinding optimal factor\t"
					+ "Maximal exceeding factor\tAverage exceeding factor\r\n";
			for (int i = 0; i < numberOfLambdas; i++) {
				message += "" + lambdaArray[i];
				message += "\t" + (0.0 + findingOptimalCount[i])
						/ paraExperiments;
				message += "\t" + maximalExceedingFactor[i];
				message += "\t" + (exceedingFactorSum[i] / paraExperiments)
						+ "\r\n";
			}// Of for i
			message += "\r\nUse the minimal one among all lambda settings:\r\n "
					+ "\t"
					+ (0.0 + overallFindingOptimalCount)
					/ paraExperiments
					+ "\t"
					+ minimalMaximalExceedingFactor
					+ "\t"
					+ (overallExceedingFactorSum / paraExperiments)
					+ "\r\n";

		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchMinimalTestCostNormalErrorReduction(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchMinimalTestCostNormalErrorReduction

	/**
	 *************************** 
	 * Comparison of backtrack and heuristic approaches to TCS-DS-ER.
	 * 
	 * @param paraLambda
	 *            the value of lambda.
	 * @param paraExperiments
	 *            the number of experiments to undertake. Each time the dataset
	 *            have different test-cost settings.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchComparisonErrorRange(double paraLambda,
			int paraExperiments) throws Exception {
		long maxBacktrackSteps = Long.MIN_VALUE;
		long minBacktrackSteps = Long.MAX_VALUE;
		double averageBacktrackSteps = 0;
		long currentBacktrackSteps = 0;

		long maxBacktrackTime = Long.MIN_VALUE;
		long minBacktrackTime = Long.MAX_VALUE;
		double averageBacktrackTime = 0;
		long currentBacktrackTime = 0;

		long maxHeuristicTime = Long.MIN_VALUE;
		long minHeuristicTime = Long.MAX_VALUE;
		double averageHeuristicTime = 0;
		long currentHeuristicTime = 0;

		String message = "";
		try {
			// Use another name to make statements shorter.
			TestCostDecisionSystemErrorRange currentSystem = CoserProject.currentProject.currentTcDsEr;

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "The data set has " + currentSystem.numInstances()
					+ " instances and " + (currentSystem.numAttributes() - 1)
					+ " tests.\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.";
			message += "lambda = " + paraLambda;

			currentSystem.initializeForReduction();
			// currentSystem.setWeighting(true);

			for (int i = 0; i < paraExperiments; i++) {
				System.out.print("" + i + "\t");
				currentSystem.refreshTestCost();
				currentSystem.computeOptimalReductTestCostBacktrack();

				currentBacktrackSteps = currentSystem.getBacktrackSteps();
				averageBacktrackSteps += currentBacktrackSteps;
				if (maxBacktrackSteps < currentBacktrackSteps) {
					maxBacktrackSteps = currentBacktrackSteps;
				}
				if (minBacktrackSteps > currentBacktrackSteps) {
					minBacktrackSteps = currentBacktrackSteps;
				}

				currentBacktrackTime = currentSystem.getReductionTime();
				averageBacktrackTime += currentBacktrackTime;
				if (maxBacktrackTime < currentBacktrackTime) {
					maxBacktrackTime = currentBacktrackTime;
				}
				if (minBacktrackTime > currentBacktrackTime) {
					minBacktrackTime = currentBacktrackTime;
				}

				currentSystem.inconsistencyBasedLambdaReduction(paraLambda);
				currentHeuristicTime = currentSystem.getReductionTime();
				averageHeuristicTime += currentHeuristicTime;
				if (maxHeuristicTime < currentHeuristicTime) {
					maxHeuristicTime = currentHeuristicTime;
				}
				if (minHeuristicTime > currentHeuristicTime) {
					minHeuristicTime = currentHeuristicTime;
				}
			}// Of for i

			message += "\r\n\tmin\tmax\taverage" + "\r\nbacktrack steps\t"
					+ minBacktrackSteps + "\t" + maxBacktrackSteps + "\t"
					+ (averageBacktrackSteps / paraExperiments)
					+ "\r\nbacktrack time\t" + minBacktrackTime + "\t"
					+ maxBacktrackTime + "\t"
					+ (averageBacktrackTime / paraExperiments)
					+ "\r\nheuristic time\t" + minHeuristicTime + "\t"
					+ maxHeuristicTime + "\t"
					+ (averageHeuristicTime / paraExperiments);

		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchComparisonErrorRange(): \r\n"
							+ ee);
		}// Of try

		return message;

	}// Of batchComparisonErrorRange

	/**
	 *************************** 
	 * Comparison of backtrack and two heuristic approaches to TCS-DS-NE.
	 * 
	 * @param paraLambda
	 *            the value of lambda.
	 * @param paraDelta
	 *            the value of delta.
	 * @param paraExperiments
	 *            the number of experiments to undertake. Each time the dataset
	 *            have different test-cost settings.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchComparisonNormalError(double paraLambda,
			double paraDelta, int paraExperiments) throws Exception {

		long maxHeuristicLambdaTime = Long.MIN_VALUE;
		long minHeuristicLambdaTime = Long.MAX_VALUE;
		double averageHeuristicLambdaTime = 0;
		long currentHeuristicLambdaTime = 0;

		long maxHeuristicDeltaTime = Long.MIN_VALUE;
		long minHeuristicDeltaTime = Long.MAX_VALUE;
		double averageHeuristicDeltaTime = 0;
		long currentHeuristicDeltaTime = 0;

		String message = "";
		try {
			// Use another name to make statements shorter.
			TestCostDecisionSystemNormalError currentSystem = CoserProject.currentProject.currentTcDsNe;

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "The data set has " + currentSystem.numInstances()
					+ " instances and " + (currentSystem.numAttributes() - 1)
					+ " tests.\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.";
			message += "lambda = " + paraLambda + ", delta = " + paraDelta;

			currentSystem.initializeForReduction();

			for (int i = 0; i < paraExperiments; i++) {
				System.out.print("" + i + "\t");
				currentSystem.refreshTestCost();
				currentSystem.inconsistencyBasedDeltaReduction(paraDelta);
				currentHeuristicDeltaTime = currentSystem.getReductionTime();
				averageHeuristicDeltaTime += currentHeuristicDeltaTime;
				if (maxHeuristicDeltaTime < currentHeuristicDeltaTime) {
					maxHeuristicDeltaTime = currentHeuristicDeltaTime;
				}
				if (minHeuristicDeltaTime > currentHeuristicDeltaTime) {
					minHeuristicDeltaTime = currentHeuristicDeltaTime;
				}
			}// Of for i
			for (int i = 0; i < paraExperiments; i++) {
				System.out.print("" + i + "\t");
				currentSystem.refreshTestCost();
				currentSystem.inconsistencyBasedLambdaReduction(paraLambda);
				currentHeuristicLambdaTime = currentSystem.getReductionTime();
				averageHeuristicLambdaTime += currentHeuristicLambdaTime;
				if (maxHeuristicLambdaTime < currentHeuristicLambdaTime) {
					maxHeuristicLambdaTime = currentHeuristicLambdaTime;
				}
				if (minHeuristicLambdaTime > currentHeuristicLambdaTime) {
					minHeuristicLambdaTime = currentHeuristicLambdaTime;
				}

			}// Of for i

			message += "\r\n\t\t\tmin\tmax\taverage"
					+ "\r\nLambdaheuristic time\t" + minHeuristicLambdaTime
					+ "\t" + maxHeuristicLambdaTime + "\t"
					+ (averageHeuristicLambdaTime / paraExperiments)
					+ "\r\nDeltaheuristic time\t" + minHeuristicDeltaTime
					+ "\t" + maxHeuristicDeltaTime + "\t"
					+ (averageHeuristicDeltaTime / paraExperiments);

		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchComparisonNormalError(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchComparisonNormalError

	/**
	 *************************** 
	 * Batch reduction to obtain minimal cost reducts. Reduction algorithms are
	 * implemented in decision systems, and this method invoke them many times
	 * with different misclassification-cost settings to generate more results.
	 * 
	 * @param paraMisCostLowerBound
	 *            The lower bound of the misclassfication cost.
	 * @param paraMisCostUpperBound
	 *            The upper bound of the misclassfication cost.
	 * @param paraCostStepLength
	 *            The step length of the misclassification cost.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchMinimalCostReduction(int paraMisCostLowerBound,
			int paraMisCostUpperBound, int paraCostStepLength) throws Exception {
		String message = "";
		try {
			// Use another name to make statements shorter.
			BothCostsNominalDecisionSystem currentSystem = CoserProject.currentProject.currentBcNds;

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "Misclassification cost setting\tTest cost\tTotal cost"
					+ "\tBacktrack steps\tNon-fast backtrack steps\tRun time\tNon-fast run time\tReduct size\tReduct\r\n";

			int testCost = 0;
			double averageCost = 0;
			long backtrackSteps = 0;
			long backtrackRunTime = 0;
			boolean[] tempOptimalReduct = null;
			long nonFastBacktrackSteps = 0;
			long nonFastBacktrackTime = 0;

			for (int misCost = paraMisCostLowerBound; misCost <= paraMisCostUpperBound; misCost += paraCostStepLength) {
				System.out.print("\t" + misCost);
				message += "" + misCost + "\t";
				currentSystem.setUnifiedMisclassificationCost(misCost);
				averageCost = currentSystem.optimalCostReductBacktrack(true);
				tempOptimalReduct = currentSystem.getOptimalReduct();
				testCost = currentSystem.computeTestCost(tempOptimalReduct);
				backtrackSteps = currentSystem.getBacktrackSteps();
				backtrackRunTime = currentSystem.getReductionTime();

				// Compare the fast vs. no-fast versions
				currentSystem.optimalCostReductBacktrack(false);
				nonFastBacktrackSteps = currentSystem.getBacktrackSteps();
				nonFastBacktrackTime = currentSystem.getReductionTime();

				message += ""
						+ testCost
						+ "\t"
						+ averageCost
						+ "\t"
						+ backtrackSteps
						+ "\t"
						+ nonFastBacktrackSteps
						+ "\t"
						+ backtrackRunTime
						+ "\t"
						+ nonFastBacktrackTime
						+ "\t"
						+ SimpleTool.getSubsetSize(tempOptimalReduct)
						+ "\t"
						+ SimpleTool
								.booleanArrayToAttributeSetString(tempOptimalReduct)
						+ "\r\n";
			}// Of for i
			message += "\r\nThe end.\r\n";
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchMinimalCostReduction(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchMinimalCostReduction

	/**
	 *************************** 
	 * Batch feature selection to obtain minimal cost subset. Feature selection
	 * algorithms are implemented in decision systems, and this method invoke
	 * them many times with different misclassification-cost settings to
	 * generate more results.
	 * 
	 * @param paraMisCostLowerBound
	 *            The lower bound of the misclassfication cost.
	 * @param paraMisCostUpperBound
	 *            The upper bound of the misclassfication cost.
	 * @param paraCostStepLength
	 *            The step length of the misclassification cost.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchMinimalCostNormalErrorFeatureSelection(
			int paraMisCostLowerBound, int paraMisCostUpperBound,
			int paraCostStepLength, int paraRatio) throws Exception {
		String message = "";
		try {
			// Use another name to make statements shorter.
			BothCostsDecisionSystemNormalError currentSystem = CoserProject.currentProject.currentBcDsNe;

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "Miscost1 setting\tMiscost2 setting\tTest cost\tTotal cost"
					+ "\tBacktrack steps\tNon-fast backtrack steps\tRun time\tNon-fast run time\tReduct size\tReduct\r\n";
			int testCost = 0;
			double averageCost = 0;
			long backtrackSteps = 0;
			long backtrackRunTime = 0;
			boolean[] tempOptimalReduct = null;
			long nonFastBacktrackSteps = 0;
			long nonFastBacktrackTime = 0;

			for (int misCost = paraMisCostLowerBound; misCost <= paraMisCostUpperBound; misCost += paraCostStepLength) {
				System.out.print("\t\t" + misCost);
				message += "" + misCost + "\t" + misCost * paraRatio + "\t";
				currentSystem
						.setMisclassificationCostMatrix(misCost, paraRatio);

				averageCost = currentSystem.optimalCostReductBacktrack(true);
				// System.out.println(averageCost);
				tempOptimalReduct = currentSystem.getOptimalReduct();
				// System.out.println(tempOptimalReduct);
				// if (tempOptimalReduct != null) {
				testCost = currentSystem.computeTestCost(tempOptimalReduct);

				backtrackSteps = currentSystem.getBacktrackSteps();

				backtrackRunTime = currentSystem.getReductionTime();

				// Compare the fast vs. no-fast versions

				currentSystem.optimalCostReductBacktrack(false);

				nonFastBacktrackSteps = currentSystem.getBacktrackSteps();

				nonFastBacktrackTime = currentSystem.getReductionTime();

				message += "" + testCost + "\t" + averageCost + "\t"
						+ backtrackSteps + "\t" + nonFastBacktrackSteps + "\t"
						+ backtrackRunTime + "\t" + nonFastBacktrackTime + "\t";
				if (tempOptimalReduct != null) {
					message += SimpleTool.getSubsetSize(tempOptimalReduct)
							+ "\t"
							+ SimpleTool
									.booleanArrayToAttributeSetString(tempOptimalReduct);
				}
				message += "\r\n";
			}
			// }// Of for i
			message += "\r\nThe end.\r\n";
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchMinimalCostNormalErrorFeatureSelection(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchMinimalCostNormalErrorFeatureSelection

	/**
	 *************************** 
	 * Batch feature selection to obtain minimal cost subset. Feature selection
	 * algorithms are implemented in decision systems, and this method invoke
	 * them many times with different misclassification-cost settings to
	 * generate more results.
	 * 
	 * @param paraConfidenceLevelLowerBound
	 *            The lower bound of the confidence level.
	 * @param paraConfidenceLevelUpperBound
	 *            The upper bound of the confidence level.
	 * @param paraConfidenceLevelStepLength
	 *            The step length of the confidence level.
	 * @param paraMisCost
	 *            One of the misclassification costs.
	 * @param paraRatio
	 *            The ratio of two misclassification costs.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchOptimalGranularComputingFeatureSelection(
			double paraConfidenceLevelLowerBound,
			double paraConfidenceLevelUpperBound,
			double paraConfidenceLevelStepLength, int paraMisCost, int paraRatio)
			throws Exception {
		String message = "";
		NumberFormat numFormat = NumberFormat.getNumberInstance();
		numFormat.setMaximumFractionDigits(4);
		try {
			// Use another name to make statements shorter.
			BothCostsDecisionSystemNormalError currentSystem = CoserProject.currentProject.currentBcDsNe;
			currentSystem.refreshTestCost();
			message += "Misclassification costs are computed based on average test cost";
			message += "\r\nStatistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "Misclassification costs are " + "" + paraMisCost + "\t"
					+ paraMisCost * paraRatio + "\t\r\n";
			message += "Confidence Level\tError Range\tTest cost\tTotal cost"
					+ "\tBacktrack steps\tNon-fast backtrack steps\tRun time\tNon-fast run time\tReduct size\tReduct\r\n";
			int testCost = 0;
			double averageCost = 0;
			boolean[] tempOptimalReduct = null;
			int bothCostsRatio = 100;
			double tempCostSum = 0;

			currentSystem.setMisclassificationCostMatrix(paraMisCost
					* bothCostsRatio, paraRatio);
			int[] testCostArray = new int[currentSystem.numAttributes() - 1];

			int[] tempTestCostArray = new int[currentSystem.numAttributes() - 1];

			int numberOfConfidenceLevels = 1 + (int) ((paraConfidenceLevelUpperBound - paraConfidenceLevelLowerBound) / paraConfidenceLevelStepLength);
			double[] confidenceLevelArray = new double[numberOfConfidenceLevels];

			for (int i = 0; i < numberOfConfidenceLevels; i++) {
				confidenceLevelArray[i] = paraConfidenceLevelLowerBound + i
						* paraConfidenceLevelStepLength;
			}// Of for i

			for (int k = 0; k < currentSystem.numAttributes() - 1; k++) {
				testCostArray[k] = currentSystem.getIndividualTestCost(k);
			}// Of for k
			for (int j = 0; j < numberOfConfidenceLevels; j++) {
				currentSystem.setNormalErrorSizeArray(confidenceLevelArray[j]);
				tempCostSum = 0;
				for (int k = 0; k < currentSystem.numAttributes() - 1; k++) {
					tempTestCostArray[k] = testCostArray[k]
							+ (int) (5 * testCostArray[k] / (0.01 + confidenceLevelArray[j]));
					tempCostSum += tempTestCostArray[k];
				}

				currentSystem.setIndividualTestCostArray(tempTestCostArray);
				currentSystem.setMisclassificationCostMatrix((int) (tempCostSum
						/ (currentSystem.numAttributes() - 1) * 20), paraRatio);
				averageCost = currentSystem.optimalCostReductBacktrack(true);
				tempOptimalReduct = currentSystem.getOptimalReduct();
				testCost = currentSystem.computeTestCost(tempOptimalReduct);
				message += numFormat.format(confidenceLevelArray[j])
						+ "\t"
						+ numFormat.format(currentSystem
								.getAverageValueNormalErrorSize()) + '\t'
						+ numFormat.format(testCost) + "\t"
						+ numFormat.format(averageCost) + "\t";
				if (tempOptimalReduct != null) {
					message += SimpleTool.getSubsetSize(tempOptimalReduct)
							+ "\t"
							+ SimpleTool
									.booleanArrayToAttributeSetString(tempOptimalReduct);
				}
				message += "\r\n";
			}
			// }// Of for i
			message += "\r\nThe end.\r\n";
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchOptimalGranularComputingFeatureSelection(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchOptimalGranularComputingFeatureSelection

	/**
	 *************************** 
	 * Batch feature selection to obtain minimal cost subset. Feature selection
	 * algorithms are implemented in decision systems, and this method invoke
	 * them many times with different misclassification-cost settings to
	 * generate more results.
	 * 
	 * @param paraConfidenceLevelLowerBound
	 *            The lower bound of the confidence level.
	 * @param paraConfidenceLevelUpperBound
	 *            The upper bound of the confidence level.
	 * @param paraConfidenceLevelStepLength
	 *            The step length of the confidence level.
	 * @param paraMisCost
	 *            One of the misclassification costs.
	 * @param paraRatio
	 *            The ratio of two misclassification costs.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchOptimalGranularComputingBasedonAdaptiveBC(
			double paraConfidenceLevelLowerBound,
			double paraConfidenceLevelUpperBound,
			double paraConfidenceLevelStepLength, int paraMisCost, int paraRatio)
			throws Exception {
		String message = "";
		NumberFormat numFormat = NumberFormat.getNumberInstance();
		numFormat.setMaximumFractionDigits(4);
		try {
			// Use another name to make statements shorter.
			BothCostsDecisionSystemNormalError currentSystem = CoserProject.currentProject.currentBcDsNe;
			currentSystem.refreshTestCost();
			message += "Misclassification costs are computed based on current test cost";
			message += "\r\nStatistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "Misclassification costs are " + "" + paraMisCost + "\t"
					+ paraMisCost * paraRatio + "\t\r\n";
			message += "Confidence Level\tError Range\tTest cost\tTotal cost"
			// +
			// "\tBacktrack steps\tNon-fast backtrack steps\tRun time\tNon-fast run time"
					+ "\tReduct size\tReduct\r\n";
			int testCost = 0;
			double averageCost = 0;
			// long backtrackSteps = 0;
			// long backtrackRunTime = 0;
			boolean[] tempOptimalReduct = null;
			// long nonFastBacktrackSteps = 0;
			// long nonFastBacktrackTime = 0;
			int bothCostsRatio = 100;
			// double tempCostSum =0;

			currentSystem.setMisclassificationCostMatrix(paraMisCost
					* bothCostsRatio, paraRatio);
			int[] testCostArray = new int[currentSystem.numAttributes() - 1];

			int[] tempTestCostArray = new int[currentSystem.numAttributes() - 1];

			int numberOfConfidenceLevels = 1 + (int) ((paraConfidenceLevelUpperBound - paraConfidenceLevelLowerBound) / paraConfidenceLevelStepLength);
			double[] confidenceLevelArray = new double[numberOfConfidenceLevels];

			for (int i = 0; i < numberOfConfidenceLevels; i++) {
				confidenceLevelArray[i] = paraConfidenceLevelLowerBound + i
						* paraConfidenceLevelStepLength;
			}// Of for i

			for (int k = 0; k < currentSystem.numAttributes() - 1; k++) {
				testCostArray[k] = currentSystem.getIndividualTestCost(k);
			}// Of for k
				// System.out.println(SimpleTool.intArrayToString(testCostArray,','));
			for (int j = 0; j < numberOfConfidenceLevels; j++) {
				currentSystem.setNormalErrorSizeArray(confidenceLevelArray[j]);
				// tempCostSum =0;
				for (int k = 0; k < currentSystem.numAttributes() - 1; k++) {
					// tempTestCostArray[k] = (int) (testCostArray[k] *
					// (bothCostsRatio - bothCostsRatio *
					// confidenceLevelArray[j]));
					tempTestCostArray[k] = testCostArray[k]
							+ (int) (5 * testCostArray[k] / (0.01 + confidenceLevelArray[j]));
					// tempCostSum +=tempTestCostArray[k];
				}

				// System.out.println(SimpleTool.intArrayToString(tempTestCostArray,','));
				currentSystem.setIndividualTestCostArray(tempTestCostArray);
				// System.out.println("misclassification cost "+
				// tempCostSum/(currentSystem.numAttributes()-1)*50);
				// currentSystem
				// .setMisclassificationCostMatrix((int)(tempCostSum/(currentSystem.numAttributes()-1)*50),
				// paraRatio);
				averageCost = currentSystem
						.optimalCostReductBacktrackAdaptiveBC(true);
				// System.out.println(averageCost);
				tempOptimalReduct = currentSystem.getOptimalReduct();
				// System.out.println(tempOptimalReduct);
				// if (tempOptimalReduct != null) {
				testCost = currentSystem.computeTestCost(tempOptimalReduct);

				// backtrackSteps = currentSystem.getBacktrackSteps();

				// backtrackRunTime = currentSystem.getReductionTime();

				// Compare the fast vs. no-fast versions

				// currentSystem.optimalCostReductBacktrack(false);

				// nonFastBacktrackSteps = currentSystem.getBacktrackSteps();

				// nonFastBacktrackTime = currentSystem.getReductionTime();

				message += numFormat.format(confidenceLevelArray[j])
						+ "\t"
						+ numFormat.format(currentSystem
								.getAverageValueNormalErrorSize()) + '\t'
						+ numFormat.format(testCost) + "\t"
						+ numFormat.format(averageCost) + "\t";
				// + backtrackSteps
				// + "\t" + nonFastBacktrackSteps + "\t"
				// + backtrackRunTime + "\t" + nonFastBacktrackTime + "\t";
				if (tempOptimalReduct != null) {
					message += SimpleTool.getSubsetSize(tempOptimalReduct)
							+ "\t"
							+ SimpleTool
									.booleanArrayToAttributeSetString(tempOptimalReduct);
				}
				message += "\r\n";
			}
			// }// Of for i
			message += "\r\nThe end.\r\n";
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchOptimalGranularComputingBasedonAdaptiveBC(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchOptimalGranularComputingBasedonAdaptiveBC

	/**
	 *************************** 
	 * Batch reduction to obtain minimal test-cost reducts. Reduction algorithms
	 * are implemented in decision systems, and this method invoke them many
	 * times with different test-cost settings to generate statistical results.
	 * 
	 * @param paraConfidenceLevelLowerBound
	 *            The lower bound of the confidence level.
	 * @param paraConfidenceLevelUpperBound
	 *            The upper bound of the confidence level.
	 * @param paraConfidenceLevelStepLength
	 *            The step length of the confidence level.
	 * @param paraExperiments
	 *            the number of experiments to undertake. Each time the dataset
	 *            have different test-cost settings.
	 * @param paraShowDetail
	 *            show the detail of the program running. Only valid if
	 *            paraExperiments is not greater than 50.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchGranularComputingFeatureSelection(
			double paraConfidenceLevelUpperBound,
			double paraConfidenceLevelLowerBound,
			double paraConfidenceLevelStepLength, int paraExperiments,
			boolean paraShowDetail) throws Exception {
		String message = "";
		NumberFormat numFormat = NumberFormat.getNumberInstance();
		numFormat.setMaximumFractionDigits(2);
		try {
			// Use another name to make statements shorter.
			BothCostsDecisionSystemNormalError currentSystem = CoserProject.currentProject.currentBcDsNe;
			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "The data set has " + currentSystem.numInstances()
					+ " instances and " + (currentSystem.numAttributes() - 1)
					+ " tests.\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.";

			int numberOfConfidenceLevels = 1 + (int) ((paraConfidenceLevelUpperBound - paraConfidenceLevelLowerBound) / paraConfidenceLevelStepLength);
			if (numberOfConfidenceLevels < 1) {
				message = "Confidence level lower bound: "
						+ paraConfidenceLevelLowerBound
						+ " is greater than the upper bound"
						+ paraConfidenceLevelUpperBound;
			}// Of if
			double[] confidenceLevelArray = new double[numberOfConfidenceLevels];
			for (int i = 0; i < numberOfConfidenceLevels; i++) {
				confidenceLevelArray[i] = paraConfidenceLevelLowerBound + i
						* paraConfidenceLevelStepLength;
			}// Of for i

			currentSystem.initializeForReduction();

			int[] findingOptimalCount = new int[numberOfConfidenceLevels];
			double[] averageTotalCost = new double[numberOfConfidenceLevels];
			double[] averageMisclassfictionCost = new double[numberOfConfidenceLevels];
			double misclassficationCost;
			int[] testCostArray = new int[currentSystem.numAttributes() - 1];

			int[] tempTestCostArray = new int[currentSystem.numAttributes() - 1];

			for (int i = 0; i < paraExperiments; i++) {
				System.out.print("" + (paraExperiments - i) + "\t");
				currentSystem.refreshTestCost();
				for (int k = 0; k < currentSystem.numAttributes() - 1; k++) {
					testCostArray[k] = currentSystem.getIndividualTestCost(k);
				}// Of for k
				for (int j = 0; j < numberOfConfidenceLevels; j++) {
					currentSystem
							.setNormalErrorSizeArray(confidenceLevelArray[j]);
					for (int k = 0; k < currentSystem.numAttributes() - 1; k++)
						tempTestCostArray[k] = (int) (testCostArray[k] * (10 - 10 * confidenceLevelArray[j]));

					currentSystem.setIndividualTestCostArray(tempTestCostArray);
					// System.out.print("delta:"+deltaArray[j]+" cost array: ");
					// System.out.println(SimpleTool.intArrayToString(tempTestCostArray,','));
					currentSystem.optimalCostReductBacktrack(true);
					misclassficationCost = currentSystem
							.averageMisclassificationCost(currentSystem
									.getOptimalFeatureSubset());
					if (paraShowDetail) {
						String tempString = SimpleTool
								.booleanArrayToAttributeSetString(currentSystem
										.getOptimalReduct());
						message += "\r\n\r\nThe optimal feature subset is: "
								+ tempString;
						message += "\r\nThe cost vector is: "
								+ SimpleTool.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
						message += "\r\nThe optimal cost is: "
								+ numFormat.format(currentSystem
										.getOptimalAverageCost());
						message += "\r\nThe error range is: "
								+ numFormat.format(confidenceLevelArray[j]);
						message += "\r\nThe average misclassification cost is: "
								+ numFormat.format(misclassficationCost);
						// message +=
						// "\r\nThe result of heuristic algorithm is as follows.\r\n\r\n";
					}// Of if

					// Let's have a look.
					averageTotalCost[j] = averageTotalCost[j]
							+ currentSystem.getOptimalAverageCost();
					averageMisclassfictionCost[j] = averageMisclassfictionCost[j]
							+ currentSystem
									.averageMisclassificationCost(currentSystem
											.getOptimalFeatureSubset());

					if (misclassficationCost == 0) {
						findingOptimalCount[j]++;
					}// Of if

				}// Of for j
			}// Of for i

			message += "\r\nResults/Confidence level\tFinding optimal factor\t"
					+ "Average Total Cost\tAverage Misclassfiction Cost\r\n";
			for (int i = 0; i < numberOfConfidenceLevels; i++) {
				message += "" + numFormat.format(confidenceLevelArray[i]);
				message += "\t\t\t"
						+ numFormat.format((0.0 + findingOptimalCount[i])
								/ paraExperiments);
				message += "\t\t\t"
						+ numFormat.format(averageTotalCost[i]
								/ paraExperiments);
				message += "\t\t\t"
						+ numFormat
								.format((averageMisclassfictionCost[i] / paraExperiments))
						+ "\r\n";
			}// Of for i

		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchGranularComputingFeatureSelection(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchGranularComputingFeatureSelection

	/**
	 *************************** 
	 * Comparison of backtrack and heuristic approaches to TCS-DS-ER.
	 * 
	 * @param paraLambda
	 *            the value of lambda.
	 * @param paraExperiments
	 *            the number of experiments to undertake. Each time the dataset
	 *            have different test-cost settings.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchComparisonNormalError(double paraLambda,
			int paraExperiments) throws Exception {
		long maxBacktrackSteps = 0;
		long minBacktrackSteps = Long.MAX_VALUE;
		double averageBacktrackSteps = 0;
		long currentBacktrackSteps = 0;

		long maxBacktrackTime = 0;
		long minBacktrackTime = Long.MAX_VALUE;
		double averageBacktrackTime = 0;
		long currentBacktrackTime = 0;

		long maxHeuristicTime = 0;
		long minHeuristicTime = Long.MAX_VALUE;
		double averageHeuristicTime = 0;
		long currentHeuristicTime = 0;

		String message = "";
		try {
			// Use another name to make statements shorter.
			BothCostsDecisionSystemNormalError currentSystem = CoserProject.currentProject.currentBcDsNe;

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "The data set has " + currentSystem.numInstances()
					+ " instances and " + (currentSystem.numAttributes() - 1)
					+ " tests.\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.";
			message += "lambda = " + paraLambda;

			currentSystem.initializeForReduction();

			for (int i = 0; i < paraExperiments; i++) {
				System.out.print("" + i + "\t");
				currentSystem.refreshTestCost();
				currentSystem.optimalCostReductBacktrack(true);

				currentBacktrackSteps = currentSystem.getBacktrackSteps();
				averageBacktrackSteps += currentBacktrackSteps;
				if (maxBacktrackSteps < currentBacktrackSteps) {
					maxBacktrackSteps = currentBacktrackSteps;
				}
				if (minBacktrackSteps > currentBacktrackSteps) {
					minBacktrackSteps = currentBacktrackSteps;
				}

				currentBacktrackTime = currentSystem.getReductionTime();
				averageBacktrackTime += currentBacktrackTime;
				if (maxBacktrackTime < currentBacktrackTime) {
					maxBacktrackTime = currentBacktrackTime;
				}
				if (minBacktrackTime > currentBacktrackTime) {
					minBacktrackTime = currentBacktrackTime;
				}

				currentSystem.inconsistencyBasedLambdaReduction(paraLambda);
				currentHeuristicTime = currentSystem.getReductionTime();
				averageHeuristicTime += currentHeuristicTime;
				if (maxHeuristicTime < currentHeuristicTime) {
					maxHeuristicTime = currentHeuristicTime;
				}
				if (minHeuristicTime > currentHeuristicTime) {
					minHeuristicTime = currentHeuristicTime;
				}
			}// Of for i

			message += "\r\n\t\t\tmin\tmax\taverage" + "\r\nbacktrack steps\t"
					+ minBacktrackSteps + "\t" + maxBacktrackSteps + "\t"
					+ (averageBacktrackSteps / paraExperiments)
					+ "\r\nbacktrack time\t" + minBacktrackTime + "\t"
					+ maxBacktrackTime + "\t"
					+ (averageBacktrackTime / paraExperiments)
					+ "\r\nheuristic time\t" + minHeuristicTime + "\t"
					+ maxHeuristicTime + "\t"
					+ (averageHeuristicTime / paraExperiments);

		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchComparisonNormalError(): \r\n"
							+ ee);
		}// Of try

		return message;

	}// Of batchComparisonNormalError

	/**
	 *************************** 
	 * Batch reduction to obtain test-cost constraint reducts. Reduction
	 * algorithms are implemented in decision systems, and this method invoke
	 * them many times with different test-cost settings to generate statistical
	 * results. Only one parameter is different from that of
	 * batchMinimalTestCostReduction().
	 * 
	 * @param paraBudgetFactor
	 *            control the budget insufficient to generate a reduct.
	 * @return the message showing the result.
	 * @see #batchMinimalTestCostReduction(double, double, double, int, boolean)
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchTestCostConstraintReductionCompareWithOptimal(
			boolean paraWeighting, boolean paraHeuristics,
			double paraLambdaUpperBound, double paraLambdaLowerBound,
			double paraLambdaStepLength, double paraBudgetFactor,
			int paraExperiments, boolean paraShowDetail) throws Exception {
		String message = "";
		// Avoid implementing another method.
		if (!paraWeighting) {
			paraLambdaUpperBound = 0;
			paraLambdaLowerBound = -0.1;
			paraLambdaStepLength = 1;
		}// Of if

		long tempRunTime = 0;

		try {
			// Use another name to make statements shorter.
			TestCostConstraintNominalDecisionSystem currentSystem = new TestCostConstraintNominalDecisionSystem(
					CoserProject.currentProject.currentTcNds);

			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.";

			int numberOfLambdas = 1 + (int) ((paraLambdaUpperBound - paraLambdaLowerBound) / paraLambdaStepLength);
			if (numberOfLambdas < 1) {
				message = "lambda lower bound: " + paraLambdaLowerBound
						+ " is greater than the upper bound"
						+ paraLambdaUpperBound;
				throw new Exception(
						"Error occurred in Reduction.batchTestCostConstraintReductionCompareWithOptimal(): \r\n"
								+ message);
			}// Of if
			double[] lambdaArray = new double[numberOfLambdas];
			for (int i = 0; i < numberOfLambdas; i++) {
				lambdaArray[i] = paraLambdaUpperBound - i
						* paraLambdaStepLength;
			}// Of for i

			currentSystem.initializeForReduction();
			currentSystem.setWeighting(paraWeighting);

			boolean[] partialReduct = null;

			int[] findingOptimalCount = new int[numberOfLambdas];
			double consistencyFraction = 0;
			double[] minimalConsistencyFraction = new double[numberOfLambdas];
			double[] consistencyFractionSum = new double[numberOfLambdas];
			int testCostConstraint = 0;
			int tempConsistency = 0;

			int competitionFindingOptimalCount = 0;
			double competitionCurrentFraction = 0;
			double competitionMinimalFraction = 1;
			double competitionFractionSum = 0;
			boolean currentRoundAlreadyOptimal = false;

			for (int i = 0; i < numberOfLambdas; i++) {
				minimalConsistencyFraction[i] = 1;
			}// Of for i

			for (int i = 0; i < paraExperiments; i++) {
				currentSystem.refreshTestCost();

				currentSystem.computeOptimalTestCost();
				testCostConstraint = (int) (currentSystem
						.getOptimalReductTestCost() * paraBudgetFactor);

				currentSystem.constraintReductionBacktrack(testCostConstraint);
				int optimalConsistency = currentSystem
						.getOptimalSubreductConsistency();
				currentRoundAlreadyOptimal = false;

				competitionCurrentFraction = 0;
				for (int j = 0; j < numberOfLambdas; j++) {
					if (paraHeuristics) {
						partialReduct = currentSystem
								.entropyBasedLambdaWeightedConstraintReduction(
										lambdaArray[j], testCostConstraint);
					} else {
						partialReduct = currentSystem
								.posBasedLambdaWeightedConstraintReduction(
										lambdaArray[j], testCostConstraint);
					}
					tempRunTime += currentSystem.getReductionTime();
					tempConsistency = currentSystem
							.computeConsistency(partialReduct);
					if ((!currentRoundAlreadyOptimal)
							&& (tempConsistency == optimalConsistency)) {
						currentRoundAlreadyOptimal = true;
						competitionFindingOptimalCount++;
					}// Of if

					if (tempConsistency == optimalConsistency) {
						findingOptimalCount[j]++;
					}// Of if

					consistencyFraction = (0.0 + tempConsistency)
							/ optimalConsistency;
					consistencyFractionSum[j] += consistencyFraction;
					if (consistencyFraction < minimalConsistencyFraction[j]) {
						minimalConsistencyFraction[j] = consistencyFraction;
					}// Of if

					// Max
					if (competitionCurrentFraction < consistencyFraction) {
						competitionCurrentFraction = consistencyFraction;
					}// Of if
				}// Of for j
				competitionFractionSum += competitionCurrentFraction;

				// Min
				if (competitionMinimalFraction > competitionCurrentFraction) {
					competitionMinimalFraction = competitionCurrentFraction;
				}// Of if

				// Show the process in the console.
				if ((i + 1) % 5 == 0) {
					System.out.print("" + (i + 1) + "\t");
				}// Of if
			}// Of for i

			tempRunTime /= paraExperiments * numberOfLambdas;

			message += "\r\nResults/lambda\tFinding optimal factor\tMinimal consistency fraction\t"
					+ "Average consistency fraction\r\n";
			for (int i = 0; i < numberOfLambdas; i++) {
				message += "" + lambdaArray[i];
				message += "\t" + (0.0 + findingOptimalCount[i])
						/ paraExperiments + "\t"
						+ minimalConsistencyFraction[i] + "\t"
						+ (0.0 + consistencyFractionSum[i]) / paraExperiments
						+ "\r\n";
			}// Of for i
			message += "\r\nUsing the competition approach";
			message += "\t" + (0.0 + competitionFindingOptimalCount)
					/ paraExperiments + "\t" + competitionMinimalFraction
					+ "\t" + (0.0 + competitionFractionSum) / paraExperiments
					+ "\r\n";
			message += "Average run time: " + tempRunTime + "\r\n";
			message += "The end\r\n";
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchTestCostConstraintReduction(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchTestCostConstraintReductionCompareWithOptimal

	/**
	 *************************** 
	 * Batch reduction to obtain minimal test-cost reducts. Reduction algorithms
	 * are implemented in decision systems, and this method invoke them many
	 * times with different test-cost settings to generate statistical results.
	 * 
	 * @param paraLambdaUpperBound
	 *            the upper bound of lambda, non-positive.
	 * @param paraLambdaLowerBound
	 *            the lower bound of lambda.
	 * @param paraLambdaStepLength
	 *            the step length of labda. For example, if the upper and lower
	 *            bounds are -0.5 and -2 respectively, and the step length is
	 *            0.5, then the lambda will have values -0.5, -1.0, -1.5, and
	 *            -2.0. Fan suggests to set the lower bound to -2.1 since the
	 *            computation of double values are not accurate.
	 * @param paraExperiments
	 *            the number of experiments to undertake. Each time the dataset
	 *            have different test-cost settings.
	 * @param paraShowDetail
	 *            show the detail of the program running. Only valid if
	 *            paraExperiments is not greater than 50.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchMinimalTotalCostNormalErrorFeatureSelection(
			double paraLambdaUpperBound, double paraLambdaLowerBound,
			double paraLambdaStepLength, int paraExperiments,
			boolean paraShowDetail) throws Exception {
		String message = "";
		NumberFormat numFormat = NumberFormat.getNumberInstance();
		numFormat.setMaximumFractionDigits(3);
		try {
			// Use another name to make statements shorter.
			BothCostsDecisionSystemNormalError currentSystem = CoserProject.currentProject.currentBcDsNe;
			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "The data set has " + currentSystem.numInstances()
					+ " instances and " + (currentSystem.numAttributes() - 1)
					+ " tests.\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.";

			int numberOfLambdas = 1 + (int) ((paraLambdaUpperBound - paraLambdaLowerBound) / paraLambdaStepLength);
			if (numberOfLambdas < 1) {
				message = "delta lower bound: " + paraLambdaLowerBound
						+ " is greater than the upper bound"
						+ paraLambdaUpperBound;
				throw new Exception(
						"Error occurred in Reduction.batchMinimalTotalCostNormalErrorFeatureSelection(): \r\n"
								+ message);
			}// Of if
			double[] lambdaArray = new double[numberOfLambdas];
			for (int i = 0; i < numberOfLambdas; i++) {
				lambdaArray[i] = paraLambdaLowerBound + i
						* paraLambdaStepLength;
			}// Of for i

			currentSystem.initializeForReduction();

			int overallFindingOptimalCount = 0;
			double minimalMaximalExceedingFactor = 0;
			double overallExceedingFactorSum = 0;

			int[] findingOptimalCount = new int[numberOfLambdas];
			double exceedingFactor = 0;
			double[] maximalExceedingFactor = new double[numberOfLambdas];
			double[] exceedingFactorSum = new double[numberOfLambdas];
			boolean[] currentReduct = null;
			for (int i = 0; i < paraExperiments; i++) {
				System.out.print("" + (paraExperiments - i) + "\t");
				currentSystem.refreshTestCost();
				currentSystem.optimalCostReductBacktrack(true);
				if (paraShowDetail) {
					String tempString = SimpleTool
							.booleanArrayToAttributeSetString(currentSystem
									.getOptimalReduct());
					message += "\r\n\r\nThe optimal feature subset is: "
							+ tempString;
					message += "\r\nThe cost vector is: "
							+ SimpleTool.intArrayToString(
									currentSystem.getIndividualTestCostArray(),
									',');
					message += "\r\nThe optimal cost is: "
							+ numFormat.format(currentSystem
									.getOptimalAverageCost());
					message += "\r\nThe average misclassification cost is: "
							+ numFormat.format(currentSystem
									.averageMisclassificationCost(currentSystem
											.getOptimalFeatureSubset()));
					message += "\r\nThe result of heuristic algorithm is as follows.\r\n\r\n";

				}// Of if

				boolean currentTestCostSettingFindingOptimal = false;
				double currentRoundMinimalExceedingFactor = Double.MAX_VALUE;

				for (int j = 0; j < numberOfLambdas; j++) {
					currentReduct = currentSystem
							.inconsistencyBasedLambdaReduction(lambdaArray[j]);
					String reductString = currentSystem.getReductString();
					// Let's have a look.
					exceedingFactor = currentSystem
							.computeExceedingFactor(currentReduct);
					if (maximalExceedingFactor[j] < exceedingFactor) {
						maximalExceedingFactor[j] = exceedingFactor;
					}// Of if
					if (currentRoundMinimalExceedingFactor > exceedingFactor) {
						currentRoundMinimalExceedingFactor = exceedingFactor;
					}// Of if

					if (paraShowDetail) {
						message += "\r\nThe feature subset is: " + reductString;
						message += " with total cost: "
								+ numFormat.format(currentSystem
										.totalCost(currentReduct));
						message += "\r\naverageMisclassificationCost: "
								+ numFormat
										.format(currentSystem
												.averageMisclassificationCost(currentReduct));
						message += " with exceeding factor: "
								+ numFormat.format(currentSystem
										.computeExceedingFactor(currentReduct));
					}// Of if paraShowDetail
					if (exceedingFactor == 0) {
						findingOptimalCount[j]++;
						currentTestCostSettingFindingOptimal = true;
					}// Of if
					exceedingFactorSum[j] += exceedingFactor;
				}// Of for j

				if (currentTestCostSettingFindingOptimal) {
					overallFindingOptimalCount++;
				}// of if
				if (currentRoundMinimalExceedingFactor > minimalMaximalExceedingFactor) {
					minimalMaximalExceedingFactor = currentRoundMinimalExceedingFactor;
				}// Of if
				overallExceedingFactorSum += currentRoundMinimalExceedingFactor;

			}// Of for i
			message += "\r\nResults/lambda\tFinding optimal factor\t"
					+ "Maximal exceeding factor\tAverage exceeding factor\r\n";
			for (int i = 0; i < numberOfLambdas; i++) {
				message += "" + lambdaArray[i];
				message += "\t"
						+ numFormat.format((0.0 + findingOptimalCount[i])
								/ paraExperiments);
				message += "\t" + numFormat.format(maximalExceedingFactor[i]);
				message += "\t"
						+ numFormat
								.format((exceedingFactorSum[i] / paraExperiments))
						+ "\r\n";
			}// Of for i
			message += "\r\nUse the minimal one among all lambda settings:\r\n "
					+ "\t"
					+ numFormat.format((0.0 + overallFindingOptimalCount)
							/ paraExperiments)
					+ "\t"
					+ numFormat.format(minimalMaximalExceedingFactor)
					+ "\t"
					+ numFormat
							.format((overallExceedingFactorSum / paraExperiments))
					+ "\r\n";

		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchMinimalTotalCostNormalErrorFeatureSelection(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchMinimalTotalCostNormalErrorFeatureSelection

	/**
	 *************************** 
	 * Batch reduction to obtain minimal test-cost reducts. Reduction algorithms
	 * are implemented in decision systems, and this method invoke them many
	 * times with different test-cost settings to generate statistical results.
	 * 
	 * @param paraLambdaUpperBound
	 *            the upper bound of lambda, non-positive.
	 * @param paraLambdaLowerBound
	 *            the lower bound of lambda.
	 * @param paraLambdaStepLength
	 *            the step length of labda. For example, if the upper and lower
	 *            bounds are -0.5 and -2 respectively, and the step length is
	 *            0.5, then the lambda will have values -0.5, -1.0, -1.5, and
	 *            -2.0. Fan suggests to set the lower bound to -2.1 since the
	 *            computation of double values are not accurate.
	 * @param paraExperiments
	 *            the number of experiments to undertake. Each time the dataset
	 *            have different test-cost settings.
	 * @param paraShowDetail
	 *            show the detail of the program running. Only valid if
	 *            paraExperiments is not greater than 50.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchMinimalTestCostLambdaNormalErrorReduction(
			double paraLambdaUpperBound, double paraLambdaLowerBound,
			double paraLambdaStepLength, int paraExperiments,
			boolean paraShowDetail) throws Exception {
		String message = "";
		try {
			// Use another name to make statements shorter.
			TestCostDecisionSystemNormalError currentSystem = CoserProject.currentProject.currentTcDsNe;

			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "The data set has " + currentSystem.numInstances()
					+ " instances and " + (currentSystem.numAttributes() - 1)
					+ " tests.\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.";

			int numberOfLambdas = 1 + (int) ((paraLambdaUpperBound - paraLambdaLowerBound) / paraLambdaStepLength);
			if (numberOfLambdas < 1) {
				message = "lambda lower bound: " + paraLambdaLowerBound
						+ " is greater than the upper bound"
						+ paraLambdaUpperBound;
				throw new Exception(
						"Error occurred in Reduction.batchMinimalTestCostErrorRangeReduction(): \r\n"
								+ message);
			}// Of if
			double[] lambdaArray = new double[numberOfLambdas];
			for (int i = 0; i < numberOfLambdas; i++) {
				lambdaArray[i] = paraLambdaUpperBound - i
						* paraLambdaStepLength;
			}// Of for i

			currentSystem.initializeForReduction();
			int overallFindingOptimalCount = 0;
			// The minimal one among all labda settings,
			// while the maximal one among all test-cost settings.
			double minimalMaximalExceedingFactor = 0;
			double overallExceedingFactorSum = 0;

			int[] findingOptimalCount = new int[numberOfLambdas];
			double exceedingFactor = 0;
			double[] maximalExceedingFactor = new double[numberOfLambdas];
			double[] exceedingFactorSum = new double[numberOfLambdas];

			for (int i = 0; i < paraExperiments; i++) {
				System.out.print("" + i + "\t");
				currentSystem.refreshTestCost();
				currentSystem.computeOptimalReductTestCostBacktrack();
				if (paraShowDetail) {
					String tempString = SimpleTool
							.booleanArrayToAttributeSetString(currentSystem
									.getOptimalReduct());
					message += "\r\n\r\nThe optimal reduct is: " + tempString;
				}// Of if

				boolean currentTestCostSettingFindingOptimal = false;
				double currentRoundMinimalExceedingFactor = Double.MAX_VALUE;
				for (int j = 0; j < numberOfLambdas; j++) {
					currentSystem
							.inconsistencyBasedLambdaReduction(lambdaArray[j]);
					String reductString = currentSystem.getReductString();

					// Let's have a look.
					exceedingFactor = currentSystem.computeExceedingFactor();
					if (exceedingFactor >= 9999.9) {
						message = "Exceeding factor is too big.\r\n"
								+ "The current reductString is: "
								+ reductString
								+ "\r\n"
								+ "The current test cost setting is: "
								+ SimpleTool.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',')
								+ "\r\n";
						throw new Exception(
								"Error occurred in Reduction.batchMinimalTestCostReduction(): \r\n"
										+ message);
					}// Of if

					if (maximalExceedingFactor[j] < exceedingFactor) {
						maximalExceedingFactor[j] = exceedingFactor;
					}// Of if
					if (currentRoundMinimalExceedingFactor > exceedingFactor) {
						currentRoundMinimalExceedingFactor = exceedingFactor;
					}// Of if

					if (paraShowDetail) {
						message += "\r\nThe cost vector is: "
								+ SimpleTool.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
						message += "\r\nThe reduct is: " + reductString;
						message += " with test-cost: "
								+ currentSystem.getReductTotalTestCost();
						message += "\r\nThe optimal cost is: "
								+ currentSystem.getOptimalReductTestCost();
						message += " with exceeding factor: "
								+ currentSystem.computeExceedingFactor();
					}// Of if paraShowDetail
					if (currentSystem.isCurrentReductOptimal()) {
						findingOptimalCount[j]++;
						currentTestCostSettingFindingOptimal = true;
					}// Of if
					exceedingFactorSum[j] += exceedingFactor;
				}// Of for j

				if (currentTestCostSettingFindingOptimal) {
					overallFindingOptimalCount++;
				}// of if
				if (currentRoundMinimalExceedingFactor > minimalMaximalExceedingFactor) {
					minimalMaximalExceedingFactor = currentRoundMinimalExceedingFactor;
				}// Of if
				overallExceedingFactorSum += currentRoundMinimalExceedingFactor;

				// Show the process in the console.
				if ((i + 1) % 50 == 0) {
					System.out.print("" + (i + 1) + "\t");
				}// Of if
			}// Of for i
			message += "\r\nResults/lambda\tFinding optimal factor\t"
					+ "Maximal exceeding factor\tAverage exceeding factor\r\n";
			for (int i = 0; i < numberOfLambdas; i++) {
				message += "" + lambdaArray[i];
				message += "\t" + (0.0 + findingOptimalCount[i])
						/ paraExperiments;
				message += "\t" + maximalExceedingFactor[i];
				message += "\t" + (exceedingFactorSum[i] / paraExperiments)
						+ "\r\n";
			}// Of for i
			message += "\r\nUse the minimal one among all lambda settings:\r\n "
					+ "\t"
					+ (0.0 + overallFindingOptimalCount)
					/ paraExperiments
					+ "\t"
					+ minimalMaximalExceedingFactor
					+ "\t"
					+ (overallExceedingFactorSum / paraExperiments)
					+ "\r\n";

		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in Reduction.batchMinimalTestCostLambdaNormalErrorReduction(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchMinimalTestCostLambdaNormalErrorReduction
}// Of class Reduction
