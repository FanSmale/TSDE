package coser.algorithm;

import coser.common.*;
import coser.datamodel.decisionsystem.*;
import coser.project.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Reduction using genetic algorithms.
 * <p>
 * Author: <b>Guiying Pan</b> <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Almost done.<br>
 * Written time: August 11, 2011. <br>
 * Last modify time: September 1, 2011.
 */

public class ReductionGA {

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
	 * @param paraNumberOfChromosomes
	 *            the number of chromosome.
	 * @param paraNumberOfGeneration
	 *            the max generation.
	 * @param paraAlgorithm
	 *            Algorithms choice.
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
	public static String batchGABasedMinimalTestCostReduction(
			double paraLambdaUpperBound, double paraLambdaLowerBound,
			double paraLambdaStepLength, int paraNumberOfChromosomes,
			int paraNumberOfGeneration, boolean paraAlgorithm,
			int paraExperiments, boolean paraShowDetail) throws Exception {
		String message = "";
		try {
			// Use another name to make statements shorter.
			TestCostNominalDecisionSystemGA currentSystem = new TestCostNominalDecisionSystemGA(
					CoserProject.currentProject.currentTcNds);

			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.\r\n";
			if (!paraAlgorithm) {
				message += "This is the fisrt algorithm.";
			} else {
				message += "This is the second algorithm.";
			}// Of if

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

			// boolean[] reduct = null;
			if (paraNumberOfChromosomes % 2 == 1) {
				paraNumberOfChromosomes += 1;
			}// Of if

			currentSystem.setNumberOfChromosomes(paraNumberOfChromosomes);
			currentSystem.setGeneration(paraNumberOfGeneration);
			currentSystem.computeCore();

			// The best results obtained from different lambda settings.
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
				double currentRoundMinimalExceedingFactor = 10000;
				for (int j = 0; j < numberOfLambdas; j++) {
					currentSystem.geneticAlgorithmBasedTCSAttributeReduction(
							lambdaArray[j], paraAlgorithm);
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

			currentSystem.setDistributionString();
			currentSystem.setGAFilename();
			String resultFilename = currentSystem.getGAFilename();
			SimpleTool_GuiyingPan.writeFile(resultFilename, message);
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in ReductionGA.batchGABasedMinimalTestCostReduction(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchGABasedMinimalTestCostReduction

	/**
	 *************************** 
	 * Batch reduction to obtain minimal reducts. Reduction algorithms are
	 * implemented in decision systems, and this method invoke them many times
	 * with different test-cost settings to generate statistical results.
	 * 
	 * @param paraNumberOfChromosomes
	 *            the number of chromosomes.
	 * @param paraGeneration
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
	public static String batchGABasedMinimalReduct(int paraNumberOfChromosomes,
			int paraGeneration, int paraExperiments, boolean paraShowDetail)
			throws Exception {
		String message = "";
		try {
			// Use another name to make statements shorter.
			TestCostNominalDecisionSystemGA currentSystem = new TestCostNominalDecisionSystemGA(
					CoserProject.currentProject.currentTcNds);

			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "There are " + paraExperiments + " different "
					+ currentSystem.getDistributionString()
					+ "-test-cost settings.";

			if (paraNumberOfChromosomes % 2 == 1) {
				paraNumberOfChromosomes += 1;
			}// Of if

			currentSystem.initializeForReduction();
			currentSystem.setWeighting(false);
			currentSystem.setNumberOfChromosomes(paraNumberOfChromosomes);
			currentSystem.setGeneration(paraGeneration);

			long[] longReducts = null;
			int findingOptimalCount = 0;
			double exceedingFactor = 0;
			double maximalExceedingFactor = 0;
			double exceedingFactorSum = 0;

			for (int i = 0; i < paraExperiments; i++) {
				currentSystem.refreshTestCost();
				currentSystem.computeOptimalTestCost();

				longReducts = currentSystem.GABasedMinimalAttributeReduction();
				int bestTestCost = Integer.MAX_VALUE;
				int indexOfBestTestCost = 0;
				for (int j = 0; j < longReducts.length; j++) {
					int tempTestCost = currentSystem
							.computeTestCost(longReducts[j]);
					if (tempTestCost < bestTestCost) {
						bestTestCost = tempTestCost;
						indexOfBestTestCost = j;
					}// Of if
				}// Of for

				boolean[] reduct = SimpleTool.longToBooleanArray(
						longReducts[indexOfBestTestCost],
						currentSystem.getNumberOfConditions());
				currentSystem.setCurrentReduct(reduct);
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

			String resultFilename = currentSystem.getArffFilename();
			currentSystem.setDistributionString();
			resultFilename = resultFilename.substring(0,
					resultFilename.length() - 5)
					+ "_"
					+ currentSystem.getDistributionString()
					+ "_Unweight"
					+ ".gred";
			SimpleTool_GuiyingPan.writeFile(resultFilename, message);
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in ReductionGA.batchGABasedMinimalReduct(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchGABasedMinimalReduct

}// Of class ReductionGA

