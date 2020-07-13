package coser.algorithm;

import java.util.Date;

//import coser.datamodel.decisionsystem.TimeNominalDecisionSystem;
import coser.datamodel.decisionsystem.BcoTimeNominalDecisionSystem;
import coser.datamodel.decisionsystem.AbcFormalTimeNominalDecisionSystem;

import coser.project.CoserProject;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: The Time Cost Sensitive Decision System with artificial bee colony
 * algorithm Batch Experiments <br>
 * Author: <b> JinLing Cai </b> E-main: jinling_cai@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. Organizaion: <a href =
 * http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>, HoHai University,
 * JiangSu 232000, China.
 * <p>
 * Progress: Time-cost-sensitive attribute reduction based on Bee Colony
 * Optimization implemented. <br>
 * Written time: April, 2013 <br>
 * Last modify time: April 17, 2013
 */
public class ReductionABCForLimit {

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
	 * The overall finding optimal factor
	 */
	static int overallFindingOptimalCount;

	/**
	 * The minimal maximal exceeding factor
	 */
	static double minimalMaximalExceedingFactor;

	/**
	 * The average exceeding factor of every batch experiment
	 */
	static double averageExceedingFactorPer;

	/**
	 * The overallExceedingFactorSum
	 */
	static double overallExceedingFactorSum;

	/**
	 *************************** 
	 * Batch reduction to obtain minimal time-cost reducts. Reduction algorithms
	 * are implemented in decision systems, and this method invoke them many
	 * times with different time cost settings to generate statistical results.
	 * 
	 * @param paraLimitUpperBound
	 *            the upper bound of limit counts for neighbors.
	 * @param paraLimitLowerBound
	 *            the lower bound of limit counts for neighbors.
	 * @param paraLimitStepLength
	 *            the step length of limit. For example, if the upper and lower
	 *            bounds are 20 and 120 respectively, and the step length is 10,
	 *            then the limit will have values 20,30,...,120.
	 * 
	 * @param paraExperiments
	 *            the number of experiments to undertake. Each time the data
	 *            sets have different time cost settings.
	 * @return the message showing the result.
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchMinimalTimeCostReductionABC(
			int paraNumberOfPopulation, int paraFoodNumber,
			int paraLimitUpperBound, int paraLimitLowerBound,
			int paraLimitStepLength, double paraCPBest, double paraCGBest,
			int paraMaxCycles, int paraExperiments) throws Exception {

		String message = "";
		Date date1 = new Date();
		long startTime = date1.getTime();

		try {
			// Create the time cost nominal decision system ABC
			AbcFormalTimeNominalDecisionSystem currentSystem = new AbcFormalTimeNominalDecisionSystem(
					CoserProject.currentProject.currentTmNds,
					paraNumberOfPopulation, paraFoodNumber, 100, paraMaxCycles,
					paraCPBest, paraCGBest);

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "There are " + paraExperiments
					+ " different time-cost settings.";

			int numberOfLimit = 1 + (int) ((paraLimitUpperBound - paraLimitLowerBound) / paraLimitStepLength);
			if (numberOfLimit < 1) {
				message = "limit lower bound: " + paraLimitLowerBound
						+ " is greater than the upper bound"
						+ paraLimitUpperBound;
				throw new Exception(
						"Error occurred in ReductionABCForLimit.batchMinimalTimeCostReductionABC(): \r\n"
								+ message);
			}// Of if
			int[] limitArray = new int[numberOfLimit];
			limitArray[0] = paraLimitLowerBound;
			for (int i = 1; i < numberOfLimit; i++) {
				limitArray[i] = limitArray[i - 1] + paraLimitStepLength;
			}// Of for i

			currentSystem.getAllReductsFilename();
			currentSystem.readAllReducts();// Move to NewProjectDialog
			// SimpleTool.pr

			// boolean[] reduct = null;

			// The best results obtained from different limit settings.
			// int overallFindingOptimalCount = 0;
			overallFindingOptimalCount = 0;
			// The minimal one among all limit settings,
			// while the maximal one among all time-cost settings.
			// double minimalMaximalExceedingFactor = 0;
			minimalMaximalExceedingFactor = 0;
			// double overallExceedingFactorSum = 0;
			overallExceedingFactorSum = 0;

			findingOptimalCount = new int[numberOfLimit];
			double exceedingFactor = 0;
			maximalExceedingFactor = new double[numberOfLimit];
			exceedingFactorSum = new double[numberOfLimit];

			for (int i = 0; i < paraExperiments; i++) {

				System.out.println("\nExperiment number: " + (i + 1));

				boolean currentTimeCostSettingFindingOptimal = false;
				// the minimal exceeding factor of set time cost in current
				// round.
				double currentRoundMinimalExceedingFactor = 10000;

				currentSystem.refreshTestCost();
				currentSystem.computeOptimalTimeCost();

				for (int j = 0; j < numberOfLimit; j++) {
					System.out.println("limitArray[ " + (j + 1) + " ]="
							+ limitArray[j]);

					currentSystem.initialize();
					// currentSystem.setMaxCycleValue(30);

					// Let's have a look.
					currentSystem.AbcTestTimeReduction(paraNumberOfPopulation,
							paraFoodNumber, limitArray[j], paraMaxCycles,
							paraCPBest, paraCGBest);
					exceedingFactor = currentSystem.exceedingFactor();

					if (maximalExceedingFactor[j] < exceedingFactor) {
						maximalExceedingFactor[j] = exceedingFactor;
					}// Of if
					if (currentRoundMinimalExceedingFactor > exceedingFactor) {
						currentRoundMinimalExceedingFactor = exceedingFactor;
					}// Of if

					if (currentSystem.optimalTimeCost()) {
						findingOptimalCount[j]++;
						currentTimeCostSettingFindingOptimal = true;
					}// Of if
					exceedingFactorSum[j] += exceedingFactor;

				}// Of for j

				if (currentTimeCostSettingFindingOptimal) {
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
			message += "\r\nResults/limit\tFinding optimal factor\t"
					+ "Maximal exceeding factor\tAverage exceeding factor\r\n";
			for (int i = 0; i < numberOfLimit; i++) {
				message += "" + limitArray[i];
				message += "\t" + (0.0 + findingOptimalCount[i])
						/ paraExperiments;
				message += "\t" + maximalExceedingFactor[i];
				message += "\t" + (exceedingFactorSum[i] / paraExperiments)
						+ "\r\n";
			}// Of for i
			averageExceedingFactorPer = overallExceedingFactorSum
					/ paraExperiments;
			message += "\r\nDisplay the statistical results of " + ""
					+ paraExperiments + "experiments\n";
			message += "\t Average overall finding optimal count\t"
					+ "Minimal maximal exceeding factor\t Average ovrall exceeding factor\n"
					+ "\t" + (0.0 + overallFindingOptimalCount)
					/ paraExperiments + "\t" + minimalMaximalExceedingFactor
					+ "\t" + (overallExceedingFactorSum / paraExperiments)
					+ "\r\n";
		} catch (Exception ee) {
			ee.printStackTrace();
			// throw new Exception(
			// "Error occurred in Reduction.batchMinimalTestCostReductionBCO(): \r\n"
			// + ee);
		}// Of try

		Date date2 = new Date();
		long endTime = date2.getTime();

		long runTime = endTime - startTime;

		message += "Running time£º" + runTime + "ms";

		return message;

	}// Of batchMinimalTestCostReductionABC

	/**
	 *************************** 
	 * Get the overall finding optimal factor
	 * 
	 * @return The finding optimal count
	 *************************** 
	 */
	public static int getOverallFindingOptimalCount() {
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