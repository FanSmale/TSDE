package coser.algorithm;

import coser.common.*;
import coser.datamodel.decisionsystem.*;
import coser.project.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Reduction using genetic algorithms.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com and <b>Jiabin Liu</b> Copyright: The
 * source code and all documents are open and free. PLEASE keep this header
 * while revising the program. <br>
 * Organizaion: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Almost done.<br>
 * Written time: February 22, 2012. <br>
 * Last modify time: April 27, 2012.
 */

public class ReductionGAWithES {

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
	public static String batchGABasedMinimalTestCostReductionWithES(
			double paraLambdaUpperBound, double paraLambdaLowerBound,
			double paraLambdaStepLength, int paraNumberOfChromosomes,
			int paraNumberOfGeneration, int paraExperiments,
			boolean paraShowDetail) throws Exception {
		String message = "";
		try {
			// Use another name to make statements shorter.
			TestCostNominalDecisionSystemGAWithES currentSystem = new TestCostNominalDecisionSystemGAWithES(
					CoserProject.currentProject.currentTcNds);

			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			currentSystem.setDistributionString();

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + " Data distribution:"
					+ currentSystem.getDistributionString() + "\r\n";

			message += "There are " + paraExperiments
					+ " different test-cost settings.\r\n";

			int numberOfLambdas = 1 + (int) ((paraLambdaUpperBound - paraLambdaLowerBound) / paraLambdaStepLength);
			if (numberOfLambdas < 1) {
				message = "lambda lower bound: " + paraLambdaLowerBound
						+ " is greater than the upper bound"
						+ paraLambdaUpperBound;
				throw new Exception(
						"Error occurred in ReductionGAWithES.batchGABasedMinimalTestCostReductionWithES(): \r\n"
								+ message);
			}// Of if

			double[] lambdaArray = new double[numberOfLambdas];
			for (int i = 0; i < numberOfLambdas; i++) {
				lambdaArray[i] = paraLambdaUpperBound - i
						* paraLambdaStepLength;
			}// Of for i

			currentSystem.initializeForReduction();
			currentSystem.setWeighting(true);

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
					// reduct =
					// Invoking ljb's algorithm
					currentSystem
							.geneticAlgorithmBasedTCSAttributeReductionOfCHC(lambdaArray[j]);

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
				if ((i + 1) % 10 == 0) {
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
			ee.printStackTrace();
			throw new Exception(
					"Error occurred in ReductionGAWithES.batchGABasedMinimalTestCostReductionWithES(): \r\n"
							+ ee);

		}// Of try

		return message;
	}// Of batchGABasedMinimalTestCostReductionWithES

}// Of class ReductionGAWithES

