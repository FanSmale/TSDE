package coser.algorithm;

import java.util.Date;

import coser.datamodel.decisionsystem.TestCostNominalDecisionSystemACO;
import coser.project.CoserProject;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: The Test Cost Sensitive Decision System with Ant Colony Optimization
 * Batch Experiments <br>
 * Author: <b> Zilong Xu </b> E-main: xzl-wy163@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. Organizaion: <a
 * href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>, Zhangzhou Normal
 * University, Fujian 363000, China.
 * <p>
 * Progress: Test-cost-sensitive attribute reduction based on Ant Colony
 * Optimization implemented. <br>
 * Written time: February, 2012 <br>
 * Last modify time: December 27, 2012
 */
public class ReductionACO {

	/**
	 * The array of finding optimal counts
	 */
	static int[] findingOptimalCount;

	/**
	 * The array of maximal exceeding factor
	 */
	static double[] maximalExceedingFactor;

	/**
	 * The array of exceeding factor sum
	 */
	static double[] exceedingFactorSum;

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
	 * The overallExceedingFactorSum
	 */
	static double overallExceedingFactorSum;

	/**
	 *************************** 
	 * Batch reduction to obtain minimal test-cost reducts. Reduction algorithms
	 * are implemented in decision systems, and this method invoke them many
	 * times with different test-cost settings to generate statistical results.
	 * 
	 * @param paraAlphaUpperBound
	 *            the upper bound of alpha, non-positive.
	 * @param paraAlphaLowerBound
	 *            the lower bound of alpha.
	 * @param paraAlphaStepLength
	 *            the step length of alpha. For example, if the upper and lower
	 *            bounds are -0.5 and -2 respectively, and the step length is
	 *            0.5, then the lambda will have values -0.5, -1.0, -1.5, and
	 *            -2.0. Fan suggests to set the lower bound to -2.1 since the
	 *            computation of double values are not accurate.
	 * @param paraExperiments
	 *            the number of experiments to undertake. Each time the dataset
	 *            have different test-cost settings.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchMinimalTestCostReductionACO(int paraAntsCounts,
			double paraAlphaUpperBound, double paraAlphaLowerBound,
			double paraAlphaStepLength, int paraExperiments) throws Exception {

		String message = "";
		Date date1 = new Date();
		long startTime = date1.getTime();

		try {
			// 创建测试代价蚁群决策系统
			// Create the test cost nominal decision system ACO
			TestCostNominalDecisionSystemACO currentSystem = new TestCostNominalDecisionSystemACO(
					CoserProject.currentProject.currentTcNds, paraAntsCounts,
					2, 2);

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.";

			int numberOfAlpha = 1 + (int) ((paraAlphaUpperBound - paraAlphaLowerBound) / paraAlphaStepLength);
			if (numberOfAlpha < 1) {
				message = "alpha lower bound: " + paraAlphaLowerBound
						+ " is greater than the upper bound"
						+ paraAlphaUpperBound;
				throw new Exception(
						"Error occurred in Reduction.batchMinimalTestCostReductionACO(): \r\n"
								+ message);
			}// Of if
			double[] alphaArray = new double[numberOfAlpha];
			for (int i = 0; i < numberOfAlpha; i++) {
				alphaArray[i] = paraAlphaUpperBound - i * paraAlphaStepLength;
			}// Of for i

			currentSystem.getAllReductsFilename();
			currentSystem.readAllReducts();// Move to NewProjectDialog

			// boolean[] reduct = null;

			// The best results obtained from different lambda settings.
			// int overallFindingOptimalCount = 0;
			overallFindingOptimalCount = 0;
			// The minimal one among all lambda settings,
			// while the maximal one among all test-cost settings.
			// double minimalMaximalExceedingFactor = 0;
			minimalMaximalExceedingFactor = 0;
			// double overallExceedingFactorSum = 0;
			overallExceedingFactorSum = 0;

			findingOptimalCount = new int[numberOfAlpha];
			double exceedingFactor = 0;
			maximalExceedingFactor = new double[numberOfAlpha];
			exceedingFactorSum = new double[numberOfAlpha];

			for (int i = 0; i < paraExperiments; i++) {

				System.out.println("\nExperiment number: " + (i + 1));

				boolean currentTestCostSettingFindingOptimal = false;
				double currentRoundMinimalExceedingFactor = 10000;

				currentSystem.refreshTestCost();
				currentSystem.computeOptimalTestCost();

				for (int j = 0; j < numberOfAlpha; j++) {

					currentSystem.initialize();
					currentSystem.setAlphaValue(alphaArray[j]);
					currentSystem.setBetaValue(2);
					// currentSystem.alpha = alphaArray[j];
					// currentSystem.beta = 2;

					// Let's have a look.
					currentSystem.minimalTestCostReductACO();
					exceedingFactor = currentSystem.exceedingFactor();

					if (maximalExceedingFactor[j] < exceedingFactor) {
						maximalExceedingFactor[j] = exceedingFactor;
					}// Of if
					if (currentRoundMinimalExceedingFactor > exceedingFactor) {
						currentRoundMinimalExceedingFactor = exceedingFactor;
					}// Of if

					if (currentSystem.optimalTestCost()) {
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
			for (int i = 0; i < numberOfAlpha; i++) {
				message += "" + alphaArray[i];
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
			ee.printStackTrace();
			// throw new Exception(
			// "Error occurred in Reduction.batchMinimalTestCostReductionACO(): \r\n"
			// + ee);
		}// Of try

		Date date2 = new Date();
		long endTime = date2.getTime();

		long runTime = endTime - startTime;

		message += "Running time：" + runTime + "ms";

		return message;

	}// Of batchMinimalTestCostReductionACO

	/**
	 *************************** 
	 * Get the overall finding optimal factor
	 * 
	 * @return The finding optimal count
	 *************************** 
	 */
	public static double getOverallFindingOptimalCount() {
		return overallFindingOptimalCount;
	}// Of getOverallFindingOptimalCount

	/**
	 *************************** 
	 * Get the minimal maximal exceeding factor
	 * 
	 * @return The minimal maximal exceeding factor
	 *************************** 
	 */
	public static double getMinimalMaximalExceedingFactor() {
		return minimalMaximalExceedingFactor;
	}// Of getMinimalMaximalExceedingFactor

	/**
	 *************************** 
	 * Get the average exceeding factor
	 * 
	 * @return The average exceeding factor
	 *************************** 
	 */
	public static double getAverageExceedingFactorPer() {
		return averageExceedingFactorPer;
	}// Of getAverageExceedingFactorPer

}// Of class ReductionACO