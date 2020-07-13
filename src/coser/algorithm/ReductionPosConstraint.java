package coser.algorithm;

import coser.common.*;
import coser.datamodel.decisionsystem.*;
import coser.project.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Reduction.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com and <b>Jiabin Liu</b> <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organizaion: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Half done.<br>
 * Written time: November 8, 2011. <br>
 * Last modify time: April 18, 2012.
 */
public class ReductionPosConstraint {

	/**
	 *************************** 
	 * Batch reduction to obtain positive constraint reducts. Reduction
	 * algorithms are implemented in decision systems, and this method invoke
	 * them many times with different test-cost settings to generate statistical
	 * results. .
	 * 
	 * @param paraBudgetFactor
	 *            control the budget insufficient to generate a reduct.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchPositiveConstraintReduction(
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
			PosConstraintNorminalDecisionSystem currentSystem = new PosConstraintNorminalDecisionSystem(
					CoserProject.currentProject.currentTcNds);

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
						"Error occurred in Reduction.batchMinimalTestCostReduction(): \r\n"
								+ message);
			}// Of if
			double[] lambdaArray = new double[numberOfLambdas];
			for (int i = 0; i < numberOfLambdas; i++) {
				lambdaArray[i] = paraLambdaUpperBound - i
						* paraLambdaStepLength;
			}// Of for i

			currentSystem.initializeForReduction();
			currentSystem.setWeighting(paraWeighting);

			currentSystem.readAllReducts();

			boolean[] partialReduct = null;

			int positiveConstraint = 0;
			String partialReductString = "";
			int[] findingLocalBestCount = new int[numberOfLambdas];
			double[] tempCostArray = new double[numberOfLambdas];
			int optimalPositive = 0;

			for (int i = 0; i < paraExperiments; i++) {
				currentSystem.refreshTestCost();

				// Compute the current optimal positive. It's finished in the
				// TestcostSensitiveDecisionSystem.java"
				optimalPositive = currentSystem.computeOptimalPositive();

				positiveConstraint = (int) (optimalPositive * paraBudgetFactor);

				double currenBestCost = Double.MAX_VALUE; // The best cost
				for (int j = 0; j < numberOfLambdas; j++) {
					partialReduct = currentSystem
							.positiveBasedLambdaWeightedConstraintReduction(
									lambdaArray[j], positiveConstraint);
					tempCostArray[j] = currentSystem
							.computeTestCost(partialReduct);
					if (tempCostArray[j] <= currenBestCost) {
						currenBestCost = tempCostArray[j];
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
								+ currentSystem.getSubreductTotalTestCost();

						message += " the entropy is: "
								+ currentSystem.getSubreductEntropy();
					}// Of if paraShowDetail
				}// Of for j

				for (int j = 0; j < numberOfLambdas; j++) {
					if (Math.abs(tempCostArray[j] - currenBestCost) < 1e-6) {
						findingLocalBestCount[j]++;
					}// Of if
				}// Of for

				// Show the process in the console.
				if ((i + 1) % 50 == 0) {
					System.out.print("" + (i + 1) + "\t");
				}// Of if
			}// Of for i
			message += "\r\nResults lambda\tFinding optimal factor\r\n";
			for (int i = 0; i < numberOfLambdas; i++) {
				message += "\t" + lambdaArray[i];
				message += "\t" + (0.0 + findingLocalBestCount[i])
						/ paraExperiments + "\r\n";
			}// Of for i
		} catch (Exception ee) {
			ee.printStackTrace();
			throw new Exception(
					"Error occurred in ReductionPosConstraint.batchPositiveConstraintReduction(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchPositiveConstraintReduction

}// Of class Reduction
