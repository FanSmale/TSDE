package coser.algorithm;

/**
 * Compare the competitive algorithm with non-competitive one through batch
 * experiments
 * 
 * @author Administrator
 */
public class ReductionBCOComparison {

	/**
	 * The bee counts lower bound
	 */
	private int beeCountsLowerBound;
	/**
	 * The bee counts upper bound
	 */
	private int beeCountsUpperBound;
	/**
	 * The bee counts increment
	 */
	private int beeCountsIncrement;
	/**
	 * The running generations
	 */
	private int generations;
	/**
	 * The limited number of searching neighbor solutions
	 */
	private int neighborLimits;

	/**
	 * The remove rate lower bound
	 */
	private double removeRateLowerBound;

	/**
	 * The remove rate upper bound
	 */
	private double removeRateUpperBound;

	/**
	 * The remove rate increment
	 */
	private double removeRateIncrement;

	/**
	 * The appointed remove rate
	 */
	private double appointedRemoveRate;

	/**
	 * The experiment counts
	 */
	private int experimentCounts;

	/**
	 * The Construct Method
	 */
	public ReductionBCOComparison(int paraBeeCountsLowerBound,
			int paraBeeCountsUpperBound, int paraBeeCountsIncrement,
			int paraGenerations, int paraNeighborLimits,
			double paraRemoveRateLowerBound, double paraRemoveRateUpperBound,
			double paraRemoveRateIncrement, double paraAppointedRemoveRate,
			int paraExperimentCounts) {
		beeCountsLowerBound = paraBeeCountsLowerBound;
		beeCountsUpperBound = paraBeeCountsUpperBound;
		beeCountsIncrement = paraBeeCountsIncrement;
		generations = paraGenerations;
		neighborLimits = paraNeighborLimits;
		removeRateLowerBound = paraRemoveRateLowerBound;
		removeRateUpperBound = paraRemoveRateUpperBound;
		removeRateIncrement = paraRemoveRateIncrement;
		appointedRemoveRate = paraAppointedRemoveRate;
		experimentCounts = paraExperimentCounts;

		System.out.println("The lower bound of bees = " + beeCountsLowerBound);
		System.out.println("The upper bound of bees = " + beeCountsUpperBound);
		System.out.println("The increment of bees = " + beeCountsIncrement);
		System.out.println("The running generations of BCO = " + generations);
		System.out
				.println("The limited neighbor solutions = " + neighborLimits);
		System.out.println("The upper bound of removing rate = "
				+ removeRateUpperBound);
		System.out.println("The lower bound of removing rate = "
				+ removeRateLowerBound);
		System.out.println("The increment of removing rate = "
				+ removeRateIncrement);
		System.out.println("The appointed value of removing rate = "
				+ appointedRemoveRate);
		System.out.println("Experiments = " + experimentCounts);

	}// Of Construct Method

	/**
	 * The batch comparison experiment
	 */
	public String batchComparisonExperiment() {
		int numberOfChangedBeeCounts = (int) ((beeCountsUpperBound - beeCountsLowerBound) / beeCountsIncrement) + 1;
		int[] beeCountsArray = new int[numberOfChangedBeeCounts];
		double[] competitionFOF = new double[numberOfChangedBeeCounts];
		double[] noncompetitionFOF = new double[numberOfChangedBeeCounts];

		beeCountsArray[0] = beeCountsLowerBound;
		for (int j = 1; j < numberOfChangedBeeCounts; j++) {
			beeCountsArray[j] = beeCountsArray[j - 1] + beeCountsIncrement;
		}// Of for j

		// Output the number of bees every experiment.
		// for (int k = 0; k < numberOfChangedBeeCounts; k++) {
		// System.out.println("beeCountsArray[" + k + "] = "
		// + beeCountsArray[k]);
		// }// Of for k

		// Invoke the relevant algorithm.
		for (int i = 0; i < numberOfChangedBeeCounts; i++) {
			System.out.println("beeCounts = " + beeCountsArray[i]);
			try {
				ReductionBCO.batchMinimalTimeCostReductionBCO(
						beeCountsArray[i], generations, neighborLimits,
						removeRateUpperBound, removeRateLowerBound,
						removeRateIncrement, experimentCounts);

				competitionFOF[i] = ReductionBCO
						.getOverallFindingOptimalCount();

				// The result of non-competitive algorithm with the value of
				// remove rate set as 0.2.
				noncompetitionFOF[i] = ReductionBCO.findingOptimalCount[5];
			} catch (Exception e) {
				e.printStackTrace();
			}// Of try-catch

			// Output the result of experiments respectively.
			System.out.println("\nThe " + (i + 1)
					+ " th experiments with changed number of bees counts.\n "
					+ "bee counts = " + beeCountsArray[i]);
			System.out
					.println("As to non-competitive algorithm, removing rate = "
							+ appointedRemoveRate);
			System.out.println("FOF = " + noncompetitionFOF[i]);
			System.out.println("\nCompetitive algorithm");
			System.out.println("FOF = " + competitionFOF[i]);
		} // Of for

		// Output the overall result of experiments.
		String resultMessage;
		resultMessage = "The final comparison result between non-competitive BCO and competitive BCO\n"
				+ "Bee counts\t"
				+ "Finding optimal factor(Non-competitive)\t"
				+ "Finding optimal factor(Competitive)\n";

		for (int m = 0; m < numberOfChangedBeeCounts; m++) {
			resultMessage += beeCountsArray[m] + "\t" + noncompetitionFOF[m]
					+ "\t" + competitionFOF[m] + "\n";
		}// Of for m

		System.out.println(resultMessage);

		return resultMessage;

	}// Of batchComparisonExperiment

}// Of class ReductionBCOComparison