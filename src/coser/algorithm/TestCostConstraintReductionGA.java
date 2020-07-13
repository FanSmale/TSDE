package coser.algorithm;

import coser.common.*;
import coser.datamodel.decisionsystem.*;
import coser.project.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Reduction using genetic algorithms.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com and <b>Jiabin Liu</b>
 * <p>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Almost done.<br>
 * Written time: September 11, 2011. <br>
 * Last modify time: April 26, 2012.
 */

public class TestCostConstraintReductionGA {

	/**
	 *************************** 
	 * Run the genetic algorithm paraExperiments times. Currently ignore this
	 * one because it is essentially contained in
	 * batchTestCostConstraintReductionGAComparison().
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
	 * @param paraNumberOfChromosomes
	 *            the number of chromosome.
	 * @param paraNumberOfGeneration
	 *            the max generation.
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
	public static String batchTestCostConstraintReductionGA(
			// combine the two algorithm
			double paraLambdaUpperBound, double paraLambdaLowerBound,
			double paraLambdaStepLength, double paraBudgetFacotr,
			int paraNumberOfChromosomes, int paraNumberOfGeneration,
			int paraExperiments, boolean paraShowDetail) throws Exception {

		String message = "";

		try {
			// Use another name to make statements shorter.
			TestCostConstraintNominalDecisionSystemGA currentSystem = new TestCostConstraintNominalDecisionSystemGA(
					CoserProject.currentProject.currentTcNds);
			currentSystem.initializeForReduction(true, paraNumberOfChromosomes,
					paraNumberOfGeneration);

			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			message += "Statistical results of "
					+ currentSystem.getArffFilename()
					+ "\t Data distribution: "
					+ currentSystem
							.getDistributionString(CostSensitiveDecisionSystem.COST_TYPE_MONEY)
					+ "\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.\r\n";

			int numberOfLambdas = 1 + (int) ((paraLambdaUpperBound - paraLambdaLowerBound) / paraLambdaStepLength);
			if (numberOfLambdas < 1) {
				message = "lambda lower bound: " + paraLambdaLowerBound
						+ " is greater than the upper bound"
						+ paraLambdaUpperBound;
				throw new Exception(
						"Error occurred in TestCostConstraintReductionGA.batchTestCostConstraintReductionGA(): \r\n"
								+ message);
			}// Of if

			double[] lambdaArray = new double[numberOfLambdas];
			for (int i = 0; i < numberOfLambdas; i++) {
				lambdaArray[i] = paraLambdaUpperBound - i
						* paraLambdaStepLength;
			}// Of for i

			boolean[] gASubreduct;
			int testCostConstraint = 0;

			// Finding optimal (the same result as the optimal subreduct)
			int[] findingOptimalCount = new int[numberOfLambdas];
			// Let the optimal consistency be 1000 and the consistency of the
			// current subreduct be 900.
			// The consistency percentage will be 0.9.
			double[] minimalConsistencyPercentage = new double[numberOfLambdas];

			for (int j = 0; j < numberOfLambdas; j++) {
				minimalConsistencyPercentage[j] = 1;
			}// Of for j

			double[] averageConsistencyPercentage = new double[numberOfLambdas];
			double tempPercentage = 0.0;

			int competitionFindingOptimalCount = 0;
			double competitionCurrentFraction = 0;
			double competitionMinimalFraction = 1;
			double competitionFractionSum = 0.0;
			boolean currentRoundAlreadyOptimal = false;

			for (int i = 0; i < paraExperiments; i++) {
				currentSystem.refreshTestCost();
				testCostConstraint = (int) (currentSystem
						.computeOptimalTestCost() * paraBudgetFacotr);

				// First, compute an optimal sub-reduct using the exhaustive
				// approach.
				currentSystem.constraintReductionBacktrack(testCostConstraint);
				int optimalConsistency = currentSystem
						.getOptimalSubreductConsistency();

				currentRoundAlreadyOptimal = false;
				competitionCurrentFraction = 0;

				for (int j = 0; j < numberOfLambdas; j++) {

					// Third, compute a sub-reduct using the genetic approach
					gASubreduct = currentSystem.testCostConstraintReductionGA(
							lambdaArray[j], testCostConstraint);
					int gAConsistency = currentSystem
							.computeConsistency(gASubreduct);

					if (gAConsistency > optimalConsistency) {
						throw new Exception(
								"Internal error! Error occurred in TestCostConstraintReductionGA.batchTestCostConstraintReductionGA\r\n"
										+ "The consistency of the genetic algorithm "
										+ gAConsistency
										+ " is greater than that of the exhaustive algorithm "
										+ optimalConsistency);
					}// Of if

					if ((!currentRoundAlreadyOptimal)
							&& (gAConsistency == optimalConsistency)) {
						currentRoundAlreadyOptimal = true;
						competitionFindingOptimalCount++;
					}// Of if

					if (gAConsistency == optimalConsistency) {
						findingOptimalCount[j]++;
					}// Of if

					tempPercentage = (0.0 + gAConsistency) / optimalConsistency;
					averageConsistencyPercentage[j] += tempPercentage;
					if (tempPercentage < minimalConsistencyPercentage[j]) {
						minimalConsistencyPercentage[j] = tempPercentage;
					}// Of if

					// Max
					if (competitionCurrentFraction < tempPercentage) {
						competitionCurrentFraction = tempPercentage;
					}// Of if

					if (paraShowDetail) {
						message += "\r\nThe cost vector is: "
								+ SimpleTool.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
						message += "\r\nThe constraint is: "
								+ testCostConstraint;
						message += "\r\nThe optimal consistency is: "
								+ optimalConsistency;
						message += "\r\nThe consistency of the genetic algorithm is: "
								+ gAConsistency;
					}// Of if paraShowDetail

				}// Of for j

				competitionFractionSum += competitionCurrentFraction;

				// Min for genetic
				if (competitionMinimalFraction > competitionCurrentFraction) {
					competitionMinimalFraction = competitionCurrentFraction;
				}// Of if

				// Show the process in the console.
				if ((i + 1) % 10 == 0) {
					System.out.print("" + (i + 1) + "\t");
				}// Of if
			}// Of for i (the ith experiment)

			message += "\r\nResults for genetic algorithm \r\n \tlambda\tFinding optimal factor\t"
					+ "Minimal consistency percentage\tAverage consistency percentage\r\n";
			for (int i = 0; i < numberOfLambdas; i++) {
				message += "" + lambdaArray[i];
				message += "\t" + (0.0 + findingOptimalCount[i])
						/ paraExperiments;
				message += "\t" + minimalConsistencyPercentage[i];
				message += "\t"
						+ (averageConsistencyPercentage[i] / paraExperiments)
						+ "\r\n";
			}// Of for i
			message += "\r\nUsing the competition approach";
			message += "\t" + (0.0 + competitionFindingOptimalCount)
					/ paraExperiments + "\t" + competitionMinimalFraction
					+ "\t" + (0.0 + competitionFractionSum) / paraExperiments
					+ "\r\n";
			message += "The end of genetic algorithm\r\n";

		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in TestCostConstraintReductionGA.batchTestCostConstraintReductionGA(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchTestCostConstraintReductionGA

	/**
	 *************************** 
	 * Two algorithms, namely the greedy algorithm using the lambda weighted
	 * approach, and the genetic algorithm, are compared.
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
	 * @param paraNumberOfChromosomes
	 *            the number of chromosome.
	 * @param paraNumberOfGeneration
	 *            the max generation.
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
	public static String batchTestCostConstraintReductionGAComparison(
			// combine the two algorithm
			double paraLambdaUpperBound, double paraLambdaLowerBound,
			double paraLambdaStepLength, double paraBudgetFacotr,
			int paraNumberOfChromosomes, int paraNumberOfGeneration,
			int paraExperiments, boolean paraShowDetail) throws Exception {
		String message = "";
		try {
			// Step 1. Initialize for both algorithms
			// Use another name to make statements shorter.
			TestCostConstraintNominalDecisionSystemGA currentSystem = new TestCostConstraintNominalDecisionSystemGA(
					CoserProject.currentProject.currentTcNds);
			currentSystem.initializeForReduction(true, paraNumberOfChromosomes,
					paraNumberOfGeneration);

			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			message += "Statistical results of "
					+ currentSystem.getArffFilename()
					+ " Data distribution:"
					+ currentSystem
							.getDistributionString(CostSensitiveDecisionSystem.COST_TYPE_MONEY)
					+ "\r\n";

			message += "There are " + paraExperiments
					+ " different test-cost settings.\r\n";

			int numberOfLambdas = 1 + (int) ((paraLambdaUpperBound - paraLambdaLowerBound) / paraLambdaStepLength);
			if (numberOfLambdas < 1) {
				message = "lambda lower bound: " + paraLambdaLowerBound
						+ " is greater than the upper bound"
						+ paraLambdaUpperBound;
				throw new Exception(
						"Error occurred in TestCostConstraintReductionGA.batchTestCostConstraintReductionGAComparison(): \r\n"
								+ message);
			}// Of if

			double[] lambdaArray = new double[numberOfLambdas];
			for (int i = 0; i < numberOfLambdas; i++) {
				lambdaArray[i] = paraLambdaUpperBound - i
						* paraLambdaStepLength;
			}// Of for i

			// Finding optimal (the same result as the optimal subreduct)
			int[][] findingOptimalCount = new int[2][numberOfLambdas];

			// Let the optimal consistency be 1000 and the consistency of the
			// current subreduct be 900.
			// The consistency percentage will be 0.9.
			double[][] minimalConsistencyPercentage = new double[2][numberOfLambdas];

			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < numberOfLambdas; j++) {
					minimalConsistencyPercentage[i][j] = 1;
				}// Of for j
			}// Of for i

			double[][] averageConsistencyPercentage = new double[2][numberOfLambdas];
			double[] tempPercentage = { 0.0, 0.0 };

			// Initialize for competition
			// double [] consistencyFraction = new double[2];
			int[] competitionFindingOptimalCount = new int[2];
			double[] competitionCurrentFraction = new double[2];
			// Attention: this variable is never computed!
			double[] competitionMinimalFraction = new double[2];
			double[] competitionFractionSum = new double[2];
			boolean[] currentRoundAlreadyOptimal = new boolean[2];// false;
			for (int i = 0; i < 2; i++) {
				competitionFindingOptimalCount[i] = 0;
				competitionCurrentFraction[i] = 0;
				competitionMinimalFraction[i] = 1;
				competitionFractionSum[i] = 0;
				currentRoundAlreadyOptimal[i] = false;
			}

			// Step 2. Run the two algorithms many times and compare their
			// result
			for (int i = 0; i < paraExperiments; i++) {
				// Step 2.1. Initialize the test cost and the constraint
				currentSystem.refreshTestCost();
				int tempOptimalTestCost = currentSystem
						.computeOptimalTestCost();
				int tempConstraint = (int) (tempOptimalTestCost * paraBudgetFacotr);

				// Step 2.2. Initialize two arrays, and do not change the other
				// three
				for (int k = 0; k < 2; k++) {
					competitionCurrentFraction[k] = 0;
					currentRoundAlreadyOptimal[k] = false;
				}

				// Step 2.3. Compute an optimal sub-reduct using the backtrack
				// approach.
				// !Important, the SESRA, SESRA* and BASS algorithms may not be
				// consistent
				currentSystem.constraintReductionSESRA(tempConstraint);
				int optimalConsistency = currentSystem
						.getOptimalSubreductConsistency();
				// System.out.println("optimalConsistency = " +
				// optimalConsistency);

				// Step 2.4. Compute using two algorithms
				for (int j = 0; j < numberOfLambdas; j++) {
					// Step 2.4.1. The lambda-weighting approach of Fan
					boolean[] greedySubreduct = currentSystem
							.entropyBasedLambdaWeightedConstraintReduction(
									lambdaArray[j], tempConstraint);
					int lambdaWeightedConsistency = currentSystem
							.computeConsistency(greedySubreduct);

					if (lambdaWeightedConsistency > optimalConsistency) {
						throw new Exception(
								"Internal error! Error occurred in TestCostConstraintReductionGA.batchTestCostConstraintReductionGAComparison\r\n"
										+ "The consistency of the heuristic algorithm "
										+ lambdaWeightedConsistency
										+ " is greater than that of the exhaustive algorithm "
										+ optimalConsistency);
					}// Of if

					if ((!currentRoundAlreadyOptimal[0])
							&& (lambdaWeightedConsistency == optimalConsistency)) {
						currentRoundAlreadyOptimal[0] = true;
						competitionFindingOptimalCount[0]++;
					}// Of if

					if (lambdaWeightedConsistency == optimalConsistency) {
						// System.out.println("batchTestCostConstraintReductionGAComparison, current is optimal");
						findingOptimalCount[0][j]++;
					}// Of if

					tempPercentage[0] = (0.0 + lambdaWeightedConsistency)
							/ optimalConsistency;
					if (tempPercentage[0] < minimalConsistencyPercentage[0][j]) {
						minimalConsistencyPercentage[0][j] = tempPercentage[0];
					}// Of if
					averageConsistencyPercentage[0][j] += tempPercentage[0];

					// Max
					if (competitionCurrentFraction[0] < tempPercentage[0]) {
						competitionCurrentFraction[0] = tempPercentage[0];
					}// Of if

					// Step 2.4.2. Jiabin's approach
					boolean[] gASubreduct = currentSystem
							.testCostConstraintReductionGA(lambdaArray[j],
									tempConstraint);
					int gAConsistency = currentSystem
							.computeConsistency(gASubreduct);
					// System.out.println("gAConsistency = " + gAConsistency);

					if (gAConsistency > optimalConsistency) {
						throw new Exception(
								"Internal error! Error occurred in TestCostConstraintReductionGA.batchTestCostConstraintReductionGAComparison()\r\n"
										+ "The consistency of the genetic algorithm "
										+ gAConsistency
										+ " is greater than that of the exhaustive algorithm "
										+ optimalConsistency);
					}// Of if

					if ((!currentRoundAlreadyOptimal[1])
							&& (gAConsistency == optimalConsistency)) {
						currentRoundAlreadyOptimal[1] = true;
						competitionFindingOptimalCount[1]++;
					}// Of if

					if (gAConsistency == optimalConsistency) {
						findingOptimalCount[1][j]++;
					}// Of if

					tempPercentage[1] = (0.0 + gAConsistency)
							/ optimalConsistency;
					if (tempPercentage[1] < minimalConsistencyPercentage[1][j]) {
						minimalConsistencyPercentage[1][j] = tempPercentage[1];
					}// Of if
					averageConsistencyPercentage[1][j] += tempPercentage[1];

					// Max
					if (competitionCurrentFraction[1] < tempPercentage[1]) {
						competitionCurrentFraction[1] = tempPercentage[1];
					}// Of if

					if (paraShowDetail) {
						message += "\r\nThe cost vector is: "
								+ SimpleTool.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
						message += "\r\nThe constraint is: " + tempConstraint;
						message += "\r\nThe optimal consistency is: "
								+ optimalConsistency;
						message += "\r\nThe consistency of the greedy approach is: "
								+ lambdaWeightedConsistency;
						message += "\r\nThe consistency of the genetic algorithm is: "
								+ gAConsistency;
					}// Of if paraShowDetail
				}// Of for j

				competitionFractionSum[0] += competitionCurrentFraction[0];
				competitionFractionSum[1] += competitionCurrentFraction[1];

				// Min for greedy
				if (competitionMinimalFraction[0] > competitionCurrentFraction[0]) {
					competitionMinimalFraction[0] = competitionCurrentFraction[0];
				}// Of if

				// Min for genetic
				if (competitionMinimalFraction[1] > competitionCurrentFraction[1]) {
					competitionMinimalFraction[1] = competitionCurrentFraction[1];
				}// Of if

				// Show the process in the console.
				if ((i + 1) % 10 == 0) {
					System.out.print("" + (i + 1) + "\t");
				}// Of if
			}// Of for i (the ith experiment)

			message += "\r\nResults for greedy\r\n \tlambda\tFinding optimal factor\t"
					+ "Minimal consistency percentage\tAverage consistency percentage\r\n";
			for (int i = 0; i < numberOfLambdas; i++) {
				message += "" + lambdaArray[i];
				message += "\t" + (0.0 + findingOptimalCount[0][i])
						/ paraExperiments;
				message += "\t" + minimalConsistencyPercentage[0][i];
				message += "\t"
						+ (averageConsistencyPercentage[0][i] / paraExperiments)
						+ "\r\n";
			}// Of for i
			message += "\r\nUsing the competition approach";
			message += "\t" + (0.0 + competitionFindingOptimalCount[0])
					/ paraExperiments + "\t" + competitionMinimalFraction[0]
					+ "\t" + (0.0 + competitionFractionSum[0])
					/ paraExperiments + "\r\n";
			message += "The end of greedy algorithm\r\n";

			message += "\r\nResults for genetic\r\n \tlambda\tFinding optimal factor\t"
					+ "Minimal consistency percentage\tAverage consistency percentage\r\n";
			for (int i = 0; i < numberOfLambdas; i++) {
				message += "" + lambdaArray[i];
				message += "\t" + (0.0 + findingOptimalCount[1][i])
						/ paraExperiments;
				message += "\t" + minimalConsistencyPercentage[1][i];
				message += "\t"
						+ (averageConsistencyPercentage[1][i] / paraExperiments)
						+ "\r\n";
			}// Of for i
			message += "\r\nUsing the competition approach";
			message += "\t" + (0.0 + competitionFindingOptimalCount[1])
					/ paraExperiments + "\t" + competitionMinimalFraction[1]
					+ "\t" + (0.0 + competitionFractionSum[1])
					/ paraExperiments + "\r\n";
			message += "The end of genetic algorithm\r\n";

		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in TestCostConstraintReductionGA.batchTestCostConstraintReductionGAComparison(): \r\n"
							+ ee);
		}// Of try
		return message;
	}// Of batchTestCostConstraintReductionGAComparison
}// Of class ConstraintReductionGA

