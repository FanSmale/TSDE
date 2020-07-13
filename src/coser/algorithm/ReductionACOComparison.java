package coser.algorithm;

/**
 * ͨ������ʵ�����Ƚ����޾����㷨֮��Ĳ���
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

		System.out.println("���������½� = " + antCountsLowerBound);
		System.out.println("���������Ͻ� = " + antCountsUpperBound);
		System.out.println("������������ = " + antCountsIncrement);
		System.out.println("�������½� = " + alphaLowerBound);
		System.out.println("�������Ͻ� = " + alphaUpperBound);
		System.out.println("���������� = " + alphaIncrement);
		System.out.println("ָ��������ֵ = " + appointedAlpha);
		System.out.println("ʵ����� = " + experimentCounts);

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

		// ���������������
		// for (int k = 0; k < numberOfAntCounts; k++) {
		// System.out.println("antCountsArray[" + k + "] = "
		// + antCountsArray[k]);
		// }// Of for k

		// �����㷨
		for (int i = 0; i < numberOfAntCounts; i++) {
			System.out.println("antCounts = " + antCountsArray[i]);// ////////////////////
			try {
				// String results =
				ReductionACO.batchMinimalTestCostReductionACO(
						antCountsArray[i], alphaUpperBound, alphaLowerBound,
						alphaIncrement, experimentCounts);

				competitionFOF[i] = ReductionACO
						.getOverallFindingOptimalCount();

				// ȡ������ֵΪ2���޾����㷨�Ľ��
				noncompetitionFOF[i] = ReductionACO.findingOptimalCount[2];
			} catch (Exception e) {
				e.printStackTrace();
			}// Of try-catch

			// ���ÿһ��ʵ��Ľ��
			System.out.println("\n��" + i + "��\n " + "������ = "
					+ antCountsArray[i]);
			System.out.println("�޾����㷨    Alpha = " + appointedAlpha);
			System.out.println("FOF = " + noncompetitionFOF[i]);
			System.out.println("\n�о����㷨");
			System.out.println("FOF = " + competitionFOF[i]);

		} // Of for

		// ���������ʵ����
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