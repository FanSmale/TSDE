package coser.algorithm;

import java.util.*;
import java.text.*;

import coser.common.*;
import coser.datamodel.decisionsystem.*;
import coser.project.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Reduction using genetic algorithms.
 * <p>
 * Author: <b>Shujiao Liao</b> sjliao2011@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organizaion: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Almost done.<br>
 * Written time: August 11, 2011. <br>
 * Last modify time: September 1, 2011.
 */

public class ReductionFNGA extends FixedNeighborhoodReduction {

	long reductionStartTime;

	long reductionEndTime;

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
	public static String batchFNGABasedMinimalTestCostReduction(double delta,
			double paraLambdaUpperBound, double paraLambdaLowerBound,
			double paraLambdaStepLength, int paraNumberOfChromosomes,
			int paraNumberOfGeneration, boolean paraAlgorithm,
			int paraExperiments, boolean paraShowDetail) throws Exception {
		int currentReductTotalTestCost = 0;
		int optimalReductTestCost = 0;
		String message = "";
		try {
			// Use another name to make statements shorter.

			TestCostDecisionSystemFixedNeighborhoodGA currentSystem = new TestCostDecisionSystemFixedNeighborhoodGA(
					CoserProject.currentProject.currentTcDsFn);

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
			currentSystem.numericCore();

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
				optimalReductTestCost = currentSystem
						.computeOptimalReductTestCostFNBacktrack();

				boolean currentTestCostSettingFindingOptimal = false;
				double currentRoundMinimalExceedingFactor = 10000;
				for (int j = 0; j < numberOfLambdas; j++) {
					currentSystem.geneticAlgorithmBasedTCSAttributeReduction(
							lambdaArray[j], paraAlgorithm);
					String reductString = currentSystem.getReductString();
					currentReductTotalTestCost = currentSystem
							.computeReductTestCost();

					// Let's have a look.
					exceedingFactor = (0.0 + currentReductTotalTestCost)
							/ (0.0 + optimalReductTestCost) - 1;
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
								+ currentReductTotalTestCost;
						message += "\r\nThe optimal cost is: "
								+ optimalReductTestCost;
						message += " with exceeding factor: " + exceedingFactor;
					}// Of if paraShowDetail

					if (Math.abs(currentReductTotalTestCost
							- optimalReductTestCost) < 1e-6) {
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

			message += "\r\n\r\nWhen delta =" + delta
					+ ", the results are shown as below:";
			message += "\r\nlambda\tFinding optimal factor\t"
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

			currentSystem.setGAFilename();
			String resultFilename = currentSystem.getGAFilename();
			SimpleTool_GuiyingPan.writeFile(resultFilename, message);

		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in ReductionFNGA.batchFNGABasedMinimalTestCostReduction(): \r\n"
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
	public static String batchFNGABasedMinimalReduct(double delta,
			int paraNumberOfChromosomes, int paraGeneration,
			int paraExperiments, boolean paraShowDetail) throws Exception {
		int currentReductTotalTestCost = 0;
		int optimalReductTestCost = 0;
		String message = "";
		try {
			// Use another name to make statements shorter.
			TestCostDecisionSystemFixedNeighborhoodGA currentSystem = new TestCostDecisionSystemFixedNeighborhoodGA(
					CoserProject.currentProject.currentTcDsFn);

			if (paraShowDetail && (paraExperiments > 50)) {
				message += "Too many detail information to show. You can see them only if "
						+ " the number of paraExperiments is not greater than 50.\r\n";
				paraShowDetail = false;
			}// Of if

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "There are "
					+ paraExperiments
					+ " different "
					+ currentSystem
							.getDistributionString(CostSensitiveDecisionSystem.COST_TYPE_MONEY)
					+ "-test-cost settings.";

			if (paraNumberOfChromosomes % 2 == 1) {
				paraNumberOfChromosomes += 1;
			}// Of if

			currentSystem.initializeForReduction();
			currentSystem.setWeighting(false);
			currentSystem.setNumberOfChromosomes(paraNumberOfChromosomes);
			currentSystem.setGeneration(paraGeneration);
			currentSystem.numericCore();

			long[] longReducts = null;
			int findingOptimalCount = 0;
			double exceedingFactor = 0;
			double maximalExceedingFactor = 0;
			double exceedingFactorSum = 0;

			for (int i = 0; i < paraExperiments; i++) {
				currentSystem.refreshTestCost();
				optimalReductTestCost = currentSystem
						.computeOptimalReductTestCostFNBacktrack();

				longReducts = currentSystem.GABasedMinimalAttributeReduction();
				double bestTestCost = Integer.MAX_VALUE;
				int indexOfBestTestCost = 0;
				for (int j = 0; j < longReducts.length; j++) {
					double tempTestCost = currentSystem
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
					currentReductTotalTestCost = currentSystem
							.getReductTotalTestCost();
					message += " with test-cost: " + currentReductTotalTestCost;
					message += "\r\nThe optimal cost is: "
							+ optimalReductTestCost;
					message += " with exceeding factor: "
							+ currentSystem.computeExceedingFactor();
				}// Of if paraShowDetail

				if (Math.abs(currentReductTotalTestCost - optimalReductTestCost) < 1e-6) {
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
			resultFilename = resultFilename.substring(0,
					resultFilename.length() - 5)
					+ "_"
					+ currentSystem
							.getDistributionString(CostSensitiveDecisionSystem.COST_TYPE_MONEY)
					+ "_Unweight" + ".gred";
			SimpleTool_GuiyingPan.writeFile(resultFilename, message);
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in ReductionFNGA.batchFNGABasedMinimalReduct(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchGABasedMinimalReduct

	/**
	 *************************** 
	 * Comparison of backtrack and heuristic approaches to TCS-DS-FN.
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
	public String batchComparisonFN(double delta, double efc_ctrl,
			double paraLambda, int paraNumberOfChromosomes,
			int paraNumberOfGeneration, boolean paraAlgorithm,
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

		long maxGATime = 0;
		long minGATime = Long.MAX_VALUE;
		double averageGATime = 0;
		long currentGATime = 0;

		String message = "";
		try {
			TestCostDecisionSystemFixedNeighborhoodGA currentSystem = new TestCostDecisionSystemFixedNeighborhoodGA(
					CoserProject.currentProject.currentTcDsFn);

			double data_array[][] = currentSystem.toDoubleMatrix();

			message += "Statistical results of "
					+ currentSystem.getArffFilename() + "\r\n";
			message += "The data set has " + currentSystem.numInstances()
					+ " instances and " + (currentSystem.numAttributes() - 1)
					+ " conditonal attributes.\r\n";
			message += "There are " + paraExperiments
					+ " different test-cost settings.";
			message += "lambda = " + paraLambda;

			currentSystem.initializeForReduction();
			currentSystem.setWeighting(true);

			long GAprepareStartTime = new Date().getTime();
			if (!paraAlgorithm) {
				message += "\r\nThis is the fisrt algorithm.";
			} else {
				message += "\r\nThis is the second algorithm.";
			}// Of if
			if (paraNumberOfChromosomes % 2 == 1) {
				paraNumberOfChromosomes += 1;
			}// Of if

			currentSystem.setNumberOfChromosomes(paraNumberOfChromosomes);
			currentSystem.setGeneration(paraNumberOfGeneration);
			currentSystem.numericCore();
			long GAprepareEndTime = new Date().getTime();
			long GAprepareTime = GAprepareEndTime - GAprepareStartTime;

			for (int i = 0; i < paraExperiments; i++) {
				currentSystem.refreshTestCost1();

				currentSystem.computeOptimalReductTestCostFNBacktrack();
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

				currentSystem.geneticAlgorithmBasedTCSAttributeReduction(
						paraLambda, paraAlgorithm);
				currentGATime = currentSystem.getReductionTime();
				averageGATime += currentGATime;
				if (maxGATime < currentGATime) {
					maxGATime = currentGATime;
				}
				if (minGATime > currentGATime) {
					minGATime = currentGATime;
				}

				neighborhoodReduction(data_array, delta, efc_ctrl);
				currentHeuristicTime = getFNReductionRunTime();
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
					+ (averageHeuristicTime / paraExperiments)
					+ "\r\ngenetic time\t" + (GAprepareTime + minGATime) + "\t"
					+ (GAprepareTime + maxGATime) + "\t"
					+ ((GAprepareTime + averageGATime) / paraExperiments);

		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in ReductionFNGA.batchComparisonFN(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchComparisonFN

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	public void neighborhoodReduction(double[][] data_array, double delta,
			double efc_ctrl) throws Exception {
		reductionStartTime = new Date().getTime();
		currentSystem.setNomalizedIndividualTestCostArray();
		double[] cost = currentSystem.getNomalizedIndividualTestCostArray();
		NumberFormat numFormat = NumberFormat.getNumberInstance();
		numFormat.setMaximumFractionDigits(5);
		try {
			messageFeature = "";
			messageEfficiency = "";
			int m = data_array.length;
			int n = data_array[0].length;
			int[] feature_slct = new int[n];// 选中的属性
			efficiency = new double[n - 1];
			feature = new boolean[n - 1];
			boolean[] feature_lft = new boolean[n - 1];// 剩下的属性
			int[] sample_lft = new int[m];
			double[][] array_cur = new double[m][n];
			int n_array_cur = -1;
			double[][] array_tmp = new double[m][n];
			int n_array_tmp = 0;
			int num_cur = -1; // the number of selected feature
			double[] dpd_tmp = new double[n];// the dependency of the feature
			double[] dpd_tmp_cost = new double[n];
			int[][] smp_csst_tmp = new int[n][m];
			for (int i = 0; i < n - 1; i++)
				feature_lft[i] = true;
			assignment(sample_lft);
			// String message = "";
			num_end = 1;
			double temp_dpd = 0;
			while (num_cur < n - 2) {
				if (num_cur != -1) {
					for (int i = 0; i < m; i++) {
						array_cur[i][num_cur] = data_array[i][feature_slct[num_cur]];// 将新选中的属性对应数据列加到原有表中
					}// for i
					n_array_cur++;
				}// if
				for (int i = 0; i < n - 1; i++) {// ///////////////////////////////////////////////////////////
					array_tmp = copyArray(array_cur);// ///////////////////////////////////////////////////
					if (feature_lft[i] == false)
						continue;
					for (int j = 0; j < m; j++) {
						array_tmp[j][num_cur + 1] = data_array[j][i];
						array_tmp[j][num_cur + 2] = data_array[j][n - 1];// 在当前的基本上加上新的属性及决策属性
					}// for j
					n_array_tmp = n_array_cur + 3;
					int[] e = attributeDependency(array_tmp, n_array_tmp,
							delta, sample_lft);
					double w = dependency;
					dpd_tmp[i] = w;
					dpd_tmp_cost[i] = w + (1 - cost[i]) * efc_ctrl;
					for (int i1 = 0; i1 < smp_csst_tmp.length; i1++)
						for (int j1 = 0; j1 < smp_csst_tmp[0].length; j1++)
							smp_csst_tmp[i1][j1] = -1;
					if (e.length != 0)
						for (int j = 0; j < e.length; j++) {
							smp_csst_tmp[i][j] = e[j];
						}// for j
				}// for int i
					// message += "\n";
				int max_sequence = maxid(dpd_tmp_cost);
				double max_dpd = dpd_tmp[max_sequence];
				if (num_cur > 0 && Math.abs(max_dpd - temp_dpd) < 0.000001) {// efficiency[num_cur]>0.001)//{
					num_end = num_cur - 1;
					num_cur = n - 1;
				} else if ((max_dpd > 0 || num_cur > 0) && temp_dpd != max_dpd) {
					temp_dpd = max_dpd;
					efficiency[max_sequence] = max_dpd;
					feature_slct[num_cur + 1] = max_sequence;// feature_lft[max_sequence];
					feature[max_sequence] = true;
					messageFeature += (feature_slct[num_cur + 1]) + " ";
					messageEfficiency += numFormat
							.format(efficiency[max_sequence]) + " ";
					feature_lft[max_sequence] = false;
					num_cur = num_cur + 1;
				}// if (max_dpd>0 || num_cur>0)
				else {
					num_end = num_cur - 1;
					num_cur = n - 1;
				}
			}// while
		}// try
		catch (Exception ee) {
			throw new Exception("Error occurred in featureselect_FW_fast: \r\n"
					+ ee);
		}

		reductionEndTime = new Date().getTime();
	}// neighborhoodReduction

	/**
	 ************************* 
	 * Get the reduction run time.
	 * 
	 * @return the reduction run time.
	 ************************* 
	 */
	public long getFNReductionRunTime() {
		return (reductionEndTime - reductionStartTime);
	}// Of getReductionRunTime

}// Of class ReductionGA

