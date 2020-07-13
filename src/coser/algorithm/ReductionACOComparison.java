package coser.algorithm;

/**
 * 通过批量实验来比较有无竞争算法之间的差异
 * 
 * @author Administrator
 */
public class ReductionACOComparison {

	/**
	 * The ant counts lower bound
	 */
	private int antCountsLowerBound;

	/**
	 * The ant counts upper bound
	 */
	private int antCountsUpperBound;

	/**
	 * The ant counts increment
	 */
	private int antCountsIncrement;

	/**
	 * The alpha lower bound
	 */
	private int alphaLowerBound;

	/**
	 * The alpha upper bound
	 */
	private int alphaUpperBound;

	/**
	 * The alpha increment
	 */
	private int alphaIncrement;

	/**
	 * The appointed alpha
	 */
	private int appointedAlpha;

	/**
	 * The experiment counts
	 */
	private int experimentCounts;

	/**
	 * The Construct Method
	 */
	public ReductionACOComparison(int paraAntCountsLowerBound,
			int paraAntCountsUpperBound, int paraAntCountsIncrement,
			int paraAlphaLowerBound, int paraAlphaUpperBound,
			int paraAlphaIncrement, int paraAppointedAlpha,
			int paraExperimentCounts) {
		antCountsLowerBound = paraAntCountsLowerBound;
		antCountsUpperBound = paraAntCountsUpperBound;
		antCountsIncrement = paraAntCountsIncrement;
		alphaLowerBound = paraAlphaLowerBound;
		alphaUpperBound = paraAlphaUpperBound;
		alphaIncrement = paraAlphaIncrement;
		appointedAlpha = paraAppointedAlpha;
		experimentCounts = paraExperimentCounts;

		System.out.println("蚂蚁数量下界 = " + antCountsLowerBound);
		System.out.println("蚂蚁数量上界 = " + antCountsUpperBound);
		System.out.println("蚂蚁数量增量 = " + antCountsIncrement);
		System.out.println("阿尔法下界 = " + alphaLowerBound);
		System.out.println("阿尔法上界 = " + alphaUpperBound);
		System.out.println("阿尔法增量 = " + alphaIncrement);
		System.out.println("指定阿尔法值 = " + appointedAlpha);
		System.out.println("实验次数 = " + experimentCounts);

	}// Of Construct Method

	/**
	 * The batch comparison experiment
	 */
	public String batchComparisonExperiment() {
		int numberOfAntCounts = (int) ((antCountsUpperBound - antCountsLowerBound) / antCountsIncrement) + 1;
		int[] antCountsArray = new int[numberOfAntCounts];
		double[] competitionFOF = new double[numberOfAntCounts];
		double[] noncompetitionFOF = new double[numberOfAntCounts];

		antCountsArray[0] = antCountsLowerBound;
		for (int j = 1; j < numberOfAntCounts; j++) {
			antCountsArray[j] = antCountsArray[j - 1] + antCountsIncrement;
		}// Of for j

		// 输出蚂蚁数量数组
		// for (int k = 0; k < numberOfAntCounts; k++) {
		// System.out.println("antCountsArray[" + k + "] = "
		// + antCountsArray[k]);
		// }// Of for k

		// 调用算法
		for (int i = 0; i < numberOfAntCounts; i++) {
			System.out.println("antCounts = " + antCountsArray[i]);// ////////////////////
			try {
				// String results =
				ReductionACO.batchMinimalTestCostReductionACO(
						antCountsArray[i], alphaUpperBound, alphaLowerBound,
						alphaIncrement, experimentCounts);

				competitionFOF[i] = ReductionACO
						.getOverallFindingOptimalCount();

				// 取阿尔法值为2的无竞争算法的结果
				noncompetitionFOF[i] = ReductionACO.findingOptimalCount[2];
			} catch (Exception e) {
				e.printStackTrace();
			}// Of try-catch

			// 输出每一组实验的结果
			System.out.println("\n第" + i + "组\n " + "蚂蚁数 = "
					+ antCountsArray[i]);
			System.out.println("无竞争算法    Alpha = " + appointedAlpha);
			System.out.println("FOF = " + noncompetitionFOF[i]);
			System.out.println("\n有竞争算法");
			System.out.println("FOF = " + competitionFOF[i]);

		} // Of for

		// 集中输出总实验结果
		String resultMessage;
		resultMessage = "The comparison result between non-competition ACO and competition ACO\n"
				+ "Ant counts\t"
				+ "Finding optimal factor(Non-competition)\t"
				+ "Finding optimal factor(Competition)\n";

		for (int m = 0; m < numberOfAntCounts; m++) {
			resultMessage += antCountsArray[m] + "\t" + noncompetitionFOF[m]
					+ "\t" + competitionFOF[m] + "\n";
		}// Of for m

		System.out.println(resultMessage);

		return resultMessage;

	}// Of batchComparisonExperiment

}// Of class ReductionACOComparison