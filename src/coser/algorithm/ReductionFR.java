package coser.algorithm;

import java.util.Date;

import coser.datamodel.decisionsystem.TestCostNominalDecisionSystemFastRandom;
import coser.gui.dialog.common.ProgressDialog;
import coser.project.CoserProject;

public class ReductionFR {

	/**
	 * The array of finding optimal counts
	 */
	static int[] findingOptimalCount;

	/**
	 * The finding optimal factor
	 */
	static double overallFindingOptimalCount;

	/**
	 * The minimal maximal exceeding factor
	 */
	static double minimalMaximalExceedingFactor;

	/**
	 * The average exceeding factor
	 */
	static double averageExceedingFactorPer;

	/**
	 *************************** 
	 * Batch reduction to obtain minimal test-cost reducts. Reduction algorithms
	 * are implemented in decision systems, and this method invoke them many
	 * times with different test-cost settings to generate statistical results.
	 * 
	 * @param paraAlpha
	 *            the number of randomizing per batch.
	 * @param paraBeta
	 *            the population of one experiment
	 * @param paraDelta
	 *            the selecting probability of attributes
	 * @param Seed
	 *            the seed of randomizing
	 * @param paraExperiments
	 *            the number of experiments to undertake. Each time the dataset
	 *            have different test-cost settings.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchMinimalTestCostReductionFR(int paraAlpha,
			int paraBeta, double paraDelta, int paraseed, int paraExperiments)
			throws Exception {
		String message = "";
		Date date1 = new Date();
		long startTime = date1.getTime();
		try {
			// Use another name to make statements shorter.
			TestCostNominalDecisionSystemFastRandom currentSystem = new TestCostNominalDecisionSystemFastRandom(
					CoserProject.currentProject.currentTcNds, paraAlpha,
					paraDelta, paraseed);
			System.out.print("Fast randomized algorithm without re-start \n");
			System.out.println("batchMinimalTestCostReductionFR: 测试代价数组："
					+ currentSystem.getIndividualTestCostArray());

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings." + "\r\n";
			int numberOfBeta = paraBeta;
			currentSystem.initializeForReduction();

			double exceedingFactor = 0;
			double maximalExceedingFactor = 0;
			double exceedingFactorSum = 0;
			double counter = 0;
			double counter1 = 0;
			// Random xx = new Random(10);
			for (int i = 0; i < paraExperiments; i++) {

				System.out.println("number:" + i);

				currentSystem.refreshTestCost();// Refresh test cost array

				int optimalTestCost = currentSystem.computeOptimalTestCost();

				currentSystem.initialize();

				double minTestCost = 10000;
				String reduct = null;

				for (int j = 0; j < numberOfBeta; j++) {

					currentSystem.initialize();
					double tempTestCost = currentSystem
							.minimalTestCostReductFR();
					String reductString = currentSystem.reductStringFR();

					if (tempTestCost < minTestCost) {
						minTestCost = tempTestCost;
						reduct = reductString;
					}// Of if
				}// of for-j

				System.out.print("\n the reduct: " + reduct);

				System.out.println("\n the minimal test cost reduct:"
						+ minTestCost);

				System.out.println("\n the optimal test cost reduct:"
						+ optimalTestCost);

				// Let's have a look.
				exceedingFactor = (minTestCost - optimalTestCost)
						/ optimalTestCost;
				// the exceeding factor
				System.out.println("\n the present of exceeding factors= "
						+ exceedingFactor);

				if (exceedingFactor <= 0.1 & exceedingFactor != 0) {
					counter1++;
				}// of if
				if (exceedingFactor == 0) {
					counter++;
				}// of if
				if (maximalExceedingFactor < exceedingFactor) {
					maximalExceedingFactor = exceedingFactor;
				}// Of if
				exceedingFactorSum += exceedingFactor;

				message += "number:" + i + "\r\n";
				message += " the exceeding factor: " + exceedingFactor + "\r\n";
				message += "reduct:" + reduct + "\r\n";

				if ((i + 1) % 50 == 0) {
					System.out.print("" + (i + 1) + "\t");
				}// Of if
			}// of for i

			message += "The maximal exceeding factor:" + maximalExceedingFactor
					+ "\r\n";
			message += "The average exceeding factor:"
					+ (exceedingFactorSum / paraExperiments) + "\r\n";
			message += "The times of optimal results appearing：" + (counter)
					+ "\r\n";
			message += "The times of better results appearing：" + (counter1)
					+ "\r\n";
			message += "The propability of better results appearing："
					+ (counter1 / paraExperiments) + "\r\n";
			message += "The populaition of experment：" + numberOfBeta + "\r\n";
		} catch (Exception ee) {
			ee.printStackTrace();
			throw new Exception(
					"Error occurred in Reduction.batchMinimalTestCostReduction(): \r\n"
							+ ee);
		}// Of try

		Date date2 = new Date();
		long endTime = date2.getTime();

		long runTime = endTime - startTime;
		message += "the run time：" + runTime + "ms\r\n";
		message += "the seed of randoming：" + paraseed + "\r\n";
		System.out.print("Fast randomized algorithm without re-start \n");
		message += "the selecting probability of attributes:" + paraDelta
				+ "\r\n";
		ProgressDialog.progressDialog.setMessageAndShow(message);
		return message;
	}// Of batchMinimalTestCostReductionFR

	/**
	 *********************************** 
	 * Get the overall finding optimal factor
	 * 
	 * @return the times of finding optimal reduct.
	 *********************************** 
	 */
	public static double getOverallFindingOptimalCount() {
		return overallFindingOptimalCount;
	}// Of getOverallFindingOptimalCount

	/**
	 ****************************** 
	 * Get the minimal maximal exceeding factor
	 * 
	 * @return the minimal maximal exceeding factor
	 ****************************** 
	 */
	public static double getMinimalMaximalExceedingFactor() {
		return minimalMaximalExceedingFactor;
	}// Of getMinimalMaximalExceedingFactor

	/**
	 ********************************* 
	 * Get the average exceeding factor
	 * 
	 * @return the average exceeding factor
	 ********************************* 
	 */
	public static double getAverageExceedingFactorPer() {
		return averageExceedingFactorPer;
	}// Of getAverageExceedingFactorPer
}// of ReductionFR
