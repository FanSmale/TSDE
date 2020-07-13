package coser.algorithm;

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
public class SimpleCommonTestCostReduction {

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
	public static String exhaustiveSimpleCommonTestCostConstraintReduction(
			int paraAlgorithm, int paraMeasure, double paraBudgetFactor)
			throws Exception {
		String message = "";
		try {
			// Use another name to make statements shorter.

			SimpleCommonTestCostConstraintNominalDecisionSystem currentSystem = new SimpleCommonTestCostConstraintNominalDecisionSystem(
					CoserProject.currentProject.currentTcNds);
			currentSystem.setMeasure(paraMeasure);
			currentSystem.setGroupRandMode();
			currentSystem.refreshTestCost();
			currentSystem.refreshSimpleCommonTestCost();

			int optimalTestCost = currentSystem.computeOptimalTestCost();
			int testCostConstraint = (int) (optimalTestCost * paraBudgetFactor);
			if (paraAlgorithm == SimpleCommonTestCostConstraintNominalDecisionSystem.SCSESRA) {
				message += currentSystem
						.constraintReductionSCSESRA(testCostConstraint);
			} else if (paraAlgorithm == SimpleCommonTestCostConstraintNominalDecisionSystem.SCSESRAstar) {
				message += currentSystem
						.constraintReductionSCSESRAstar(testCostConstraint);
			} else if (paraAlgorithm == SimpleCommonTestCostConstraintNominalDecisionSystem.SCBASS) {

				message += currentSystem
						.constraintReductionSCBASS(testCostConstraint);

			} else {
				message += "\r\n====SESCSRA backtrack===="
						+ currentSystem
								.constraintReductionSCSESRA(testCostConstraint);
				message += "\r\n====SESCSRAstar backtrack===="
						+ currentSystem
								.constraintReductionSCSESRAstar(testCostConstraint);
				message += "\r\n\r\n====BASCSS===="
						+ currentSystem
								.constraintReductionSCBASS(testCostConstraint);
			}// Of if

			message += "\r\nMajority rate: "
					+ currentSystem.getOptimalSubreductMajority() + " / "
					+ currentSystem.numInstances() + " = "
					+ (currentSystem.getOptimalSubreductMajority() + 0.0)
					/ currentSystem.numInstances() + "\r\n";
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in SimpleCommonTestCostReduction.exhaustiveTestCostConstraintReduction(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of exhaustiveSimpleCommonTestCostConstraintReduction

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
	public static String batchExhaustiveSimpleCommonTestCostConstraintReduction(
			int paraAlgorithm, int paraMeasure, double paraBudgetFactor,
			int paraNumberOfExperiments) throws Exception {
		String message = "";
		int[] totalArray = new int[SimpleCommonTestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH];
		try {
			// Use another name to make statements shorter.
			SimpleCommonTestCostConstraintNominalDecisionSystem currentSystem = new SimpleCommonTestCostConstraintNominalDecisionSystem(
					CoserProject.currentProject.currentTcNds);
			currentSystem.setMeasure(paraMeasure);
			currentSystem.setGroupRandMode();

			message += "[0]: the number of test sets satisfying the constraint.\r\n";
			message += "[1]: the number of test sets with consistency checked.\r\n";
			message += "[2]: the number of maximal consistency test sets.\r\n";
			message += "[3]: the number of minimal cost test sets.\r\n";
			message += "[4]: the time for candidates building.\r\n";
			message += "[5]: the time for selecting optimal sub reduct.\r\n";
			message += "[6]: the time for the execution.\r\n";
			message += "[7]: the number of reducts. Only valid for SESRAstar.\r\n";
			message += "[8]: the basic computation for constraint checking.\r\n";

			// Only one algorithm
			if (paraAlgorithm <= SimpleCommonTestCostConstraintNominalDecisionSystem.SCBASS) {
				long[][] resultsArray = new long[paraNumberOfExperiments][SimpleCommonTestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH];
				for (int i = 0; i < paraNumberOfExperiments; i++) {

					currentSystem.refreshTestCost();
					currentSystem.refreshSimpleCommonTestCost();

					int optimalTestCost = currentSystem
							.computeOptimalTestCost();
					int testCostConstraint = (int) (optimalTestCost * paraBudgetFactor);

					// Algorithm choice.
					if (paraAlgorithm == SimpleCommonTestCostConstraintNominalDecisionSystem.SCSESRA) {
						currentSystem
								.constraintReductionSCSESRA(testCostConstraint);
					} else if (paraAlgorithm == SimpleCommonTestCostConstraintNominalDecisionSystem.SCSESRAstar) {
						currentSystem
								.constraintReductionSCSESRAstar(testCostConstraint);
					} else if (paraAlgorithm == SimpleCommonTestCostConstraintNominalDecisionSystem.SCBASS) {
						currentSystem
								.constraintReductionSCBASS(testCostConstraint);
					}// Of if

					long[] singularResultsArray = currentSystem
							.getExhaustiveSubreductResults();
					message += "\r\n";
					for (int j = 0; j < SimpleCommonTestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
						resultsArray[i][j] = singularResultsArray[j];
						totalArray[j] += resultsArray[i][j];
						message += "" + resultsArray[i][j] + "\t";
					}// Of for j
				}// Of for i

				message += "\r\nAverage values:\r\n";
				for (int j = 0; j < SimpleCommonTestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
					message += "" + (totalArray[j] + 0.0)
							/ paraNumberOfExperiments + "\t";
				}// Of for j

				return message;
			}// Of if

			// For all algorithms.
			long[] singularResultsArray;
			// Just to make the name shorter.
			int tempLength = SimpleCommonTestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH;
			long[][] resultsArrayForSCSESRA = new long[paraNumberOfExperiments][tempLength];
			long[][] resultsArrayForSCSESRAstar = new long[paraNumberOfExperiments][tempLength];
			long[][] resultsArrayForSCBASS = new long[paraNumberOfExperiments][tempLength];
			long[] totalArrayForSCSESRA = new long[tempLength];
			long[] totalArrayForSCSESRAstar = new long[tempLength];
			long[] totalArrayForSCBASS = new long[tempLength];

			float totalMajority = 0;
			int numerOfInstances = currentSystem.numInstances();

			for (int i = 0; i < paraNumberOfExperiments; i++) {
				System.out.print("#" + i + "\t");
				currentSystem.initializeForReduction();
				currentSystem.refreshTestCost();
				currentSystem.refreshSimpleCommonTestCost();
				int optimalTestCost = currentSystem.computeOptimalTestCost();
				int testCostConstraint = (int) (optimalTestCost * paraBudgetFactor);

				// ///////////////For SCSESRA
				currentSystem.constraintReductionSCSESRA(testCostConstraint);
				singularResultsArray = currentSystem
						.getExhaustiveSubreductResults();
				for (int j = 0; j < SimpleCommonTestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
					resultsArrayForSCSESRA[i][j] = singularResultsArray[j];
					totalArrayForSCSESRA[j] += resultsArrayForSCSESRA[i][j];
				}// Of for j

				// ///////////////For SCSESRAstar
				currentSystem
						.constraintReductionSCSESRAstar(testCostConstraint);
				singularResultsArray = currentSystem
						.getExhaustiveSubreductResults();
				for (int j = 0; j < SimpleCommonTestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
					resultsArrayForSCSESRAstar[i][j] = singularResultsArray[j];
					totalArrayForSCSESRAstar[j] += resultsArrayForSCSESRAstar[i][j];
				}// Of for j

				// ///////////////For SCBASS
				// String SCBASSResults =
				// currentSystem.constraintReductionSCBASS(testCostConstraint);
				singularResultsArray = currentSystem
						.getExhaustiveSubreductResults();
				for (int j = 0; j < SimpleCommonTestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
					resultsArrayForSCBASS[i][j] = singularResultsArray[j];
					totalArrayForSCBASS[j] += resultsArrayForSCBASS[i][j];
				}// Of for j
			}// Of for i

			message += "\r\nAverage values:\r\n";
			message += "\r\nSCSESRA:\r\n";
			for (int j = 0; j < SimpleCommonTestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
				message += "" + (totalArrayForSCSESRA[j] + 0.0)
						/ paraNumberOfExperiments + "\t";
			}// Of for j

			message += "\r\nSCSESRAstar:\r\n";
			for (int j = 0; j < SimpleCommonTestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
				message += "" + (totalArrayForSCSESRAstar[j] + 0.0)
						/ paraNumberOfExperiments + "\t";
			}// Of for j

			message += "\r\nSCBASS:\r\n";
			for (int j = 0; j < SimpleCommonTestCostConstraintNominalDecisionSystem.OPR_RESULT_LENGTH; j++) {
				message += "" + (totalArrayForSCBASS[j] + 0.0)
						/ paraNumberOfExperiments + "\t";
			}// Of for j

			float averageMajority = totalMajority / paraNumberOfExperiments
					/ numerOfInstances;
			message += "\r\nAverage majority:" + averageMajority + "\r\n";

		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in SimpleCommonTestCostReduction.batchExhaustiveSimpleCommonTestCostConstraintReduction: \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchExhaustiveSimpleCommonTestCostConstraintReduction

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
	 * @see Reduction#batchMinimalTestCostReduction(double, double, double, int,
	 *      boolean)
	 * @throws Exception
	 *             if something wrong happens.
	 *************************** 
	 */
	public static String batchSimpleCommonTestCostConstraintReduction(
			boolean paraWeighting, double paraLambdaUpperBound,
			double paraLambdaLowerBound, double paraLambdaStepLength,
			int paraGroupUpperBound, int paraGroupLowerBound,
			int paraGroupStepLength, double paraBudgetFactor,
			int paraExperiments, boolean paraShowDetail) throws Exception {
		String message = "";
		// Avoid implementing another method.
		if (!paraWeighting) {
			paraLambdaUpperBound = 0;
			paraLambdaLowerBound = -0.1;
			paraLambdaStepLength = 1;
		}// Of if

		if (paraGroupUpperBound > CoserProject.currentProject.currentTcNds
				.getNumberOfConditions()) {
			throw new Exception(
					"Error occurs in SimpleCommonTestCostReduction.batchSimpleCommonTestCostConstraintReduction()"
							+ "The group upper bound"
							+ paraGroupUpperBound
							+ "is greater than the number of conditions "
							+ CoserProject.currentProject.currentTcNds
									.getNumberOfConditions());
		}// Of if

		if (paraGroupLowerBound > paraGroupUpperBound) {
			throw new Exception(
					"Error occurs in SimpleCommonTestCostReduction.batchSimpleCommonTestCostConstraintReduction()"
							+ "The number of group lower bound is greater than that upper bound");
		}// Of if

		if (paraGroupLowerBound < 0 || paraGroupUpperBound < 0
				|| paraGroupStepLength < 0) {
			throw new Exception(
					"Error occurs in SimpleCommonTestCostReduction.batchSimpleCommonTestCostConstraintReduction()"
							+ "\r\nThe number of group lower bound, upper bound or the group step length is smaller than 0.");
		}// Of if

		if (paraGroupLowerBound != paraGroupUpperBound) {
			if (paraGroupStepLength == 0) {
				throw new Exception(
						"Error occurs in SimpleCommonTestCostReduction.batchSimpleCommonTestCostConstraintReduction()"
								+ "The group step length is zero, when group lower bound is not equal to that upper bound");
			}// Of if
			if (paraGroupLowerBound == 0 || paraGroupUpperBound == 0) {
				throw new Exception(
						"Error occurs in SimpleCommonTestCostReduction.batchSimpleCommonTestCostConstraintReduction()"
								+ "Either of the number of group lower bonud or upper bound is zero.");
			}// Of if
		} else {
			paraGroupStepLength = 1;
		}// of if

		try {
			// Use another name to make statements shorter.
			SimpleCommonTestCostConstraintNominalDecisionSystem currentSystem = new SimpleCommonTestCostConstraintNominalDecisionSystem(
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
						+ " is greater than the upper bound "
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

			int numberOfGroups = 1 + (int) (paraGroupUpperBound - paraGroupLowerBound
					/ paraGroupStepLength);
			int[] groupArray = new int[numberOfGroups];
			for (int i = 0; i < numberOfGroups; i++) {
				groupArray[i] = paraGroupLowerBound + i * paraGroupStepLength;
			}// of for i

			currentSystem.initializeForReduction();
			currentSystem.setWeighting(paraWeighting);
			currentSystem.readAllSubreducts();
			double tempOptimalEntropy = 0;
			int[] findingOptimalCount = new int[numberOfGroups];
			boolean findingOptimalFlag = false;
			// boolean[] subReduct = null;
			int testCostConstraint = 0;
			String subReductString = "";
			int[][] findingLocalBestCount = new int[numberOfGroups][numberOfLambdas];
			double[] tempEntropyArray = new double[numberOfLambdas];
			// boolean[] tempBestSubreduct = null;

			for (int k = 0; k < paraExperiments; k++) {
				currentSystem.refreshTestCost();
				currentSystem.setGroupRandMode();
				for (int i = 0; i < numberOfGroups; i++) {
					currentSystem.setNumberOfGroups(groupArray[i]);
					currentSystem.refreshSimpleCommonTestCost();

					// int optimalTestCost =
					// currentSystem.computeOptimalTestCost();
					testCostConstraint = (int) (currentSystem
							.getOptimalReductTestCost() * paraBudgetFactor);
					currentSystem.constraintReductionSCBASS(testCostConstraint);
					tempOptimalEntropy = currentSystem
							.getOptimalSubreductEntropy();
					double currenRoundBestEntropy = 100;
					for (int j = 0; j < numberOfLambdas; j++) {
						currentSystem
								.entropyBasedLambdaWeightedConstraintReduction(
										lambdaArray[j], testCostConstraint);
						tempEntropyArray[j] = currentSystem
								.getSubreductEntropy();
						if (tempEntropyArray[j] < currenRoundBestEntropy) {
							currenRoundBestEntropy = tempEntropyArray[j];
							// tempBestSubreduct = Arrays.copyOf(subReduct,
							// subReduct.length);
						}// Of if

						if (paraShowDetail) {
							subReductString = currentSystem
									.getSubreductString();
							message += "\r\nThe cost vector is: "
									+ SimpleTool.intArrayToString(currentSystem
											.getIndividualTestCostArray(), ',');
							message += "\r\nThe optimal conditional entropy is: "
									+ tempOptimalEntropy;
							message += "\r\nThe sub-reduct is: "
									+ subReductString;
							message += " with test-cost: "
									+ currentSystem.getSubreductTotalTestCost();
							message += " the entropy is: "
									+ currentSystem.getSubreductEntropy();
						}// Of if paraShowDetail
					}// Of for j

					for (int j = 0; j < numberOfLambdas; j++) {
						// if (Math.abs(tempEntropyArray[j] -
						// tempOptimalEntropy) < 1e-6) {
						if (tempEntropyArray[j] == tempOptimalEntropy) {
							findingLocalBestCount[i][j]++;
							findingOptimalFlag = true;
						}// Of if
					}// Of for j

					if (findingOptimalFlag) {
						findingOptimalCount[i]++;
						findingOptimalFlag = false;
					}// Of if

				}// of for i
					// Show the process in the console.
				if ((k + 1) % 10 == 0) {
					System.out.print("" + (k + 1) + "\t");
				}// Of if
			}// Of for k

			message += "\r\nlambda\t\tThe number of group\r\n";
			for (int i = 0; i < numberOfGroups; i++) {
				message += "\t" + groupArray[i];
			}// of for i
			for (int i = 0; i < numberOfLambdas; i++) {
				message += "\r\n" + lambdaArray[i];
				for (int j = 0; j < numberOfGroups; j++) {
					message += "\t" + (0.0 + findingLocalBestCount[j][i])
							/ paraExperiments;
				}// for j
			}// of for i

			message += "\r\nFinding optimal factor\r\n";
			for (int i = 0; i < numberOfGroups; i++) {
				message += "\t" + (0.0 + findingOptimalCount[i])
						/ paraExperiments;
			}// of for i
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in SimpleCommonTestCostReduction.batchTestCostConstraintReduction(): \r\n"
							+ ee);
		}// Of try

		return message;
	}// Of batchSimpleCommonTestCostConstraintReduction

}// Of class SimpleCommonTestCostReduction

