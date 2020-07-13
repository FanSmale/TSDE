package coser.algorithm;

/**
 * Compare the competitive algorithm with non-competitive one through batch
 * experiments
 * 
 * @author Administrator
 */
public class ReductionABCComparison {

	/**
	 * The number of population
	 */
	// private int numberOfPopulation;
	/**
	 * The food number lower bound
	 */
	private int foodNumberLowerBound;
	/**
	 * The food number upper bound
	 */
	private int foodNumberUpperBound;
	/**
	 * The food number increment
	 */
	private int foodNumberIncrement;
	/**
	 * The running generations
	 */
	// private int generations;
	/**
	 * The limited number of searching neighbor solutions
	 */
	// private int neighborLimits;

	/**
	 * The limit lower bound
	 */
	private int limitLowerBound;

	/**
	 * The limit upper bound
	 */
	private int limitUpperBound;

	/**
	 * The limit increment
	 */
	private int limitIncrement;

	/**
	 * The appointed limit
	 */
	private int appointedLimit;
	/**
	 * The probability of selecting local best solution
	 */
	private double cPBest;
	/**
	 * The probability of selecting global best solution
	 */
	private double cGBest;

	/**
	 * The maximal number of generations
	 */
	private int maxCycles;
	/**
	 * The experiment counts
	 */
	private int experimentCounts;

	/**
	 * The Construct Method
	 */
	public ReductionABCComparison(int paraFoodNumberLowerBound,
			int paraFoodNumberUpperBound, int paraFoodNumberIncrement,
			int paraLimitLowerBound, int paraLimitUpperBound,
			int paraLimitIncrement, int paraAppointedLimit, double paraCPBest,
			double paraCGBest, int paraMaxCycels, int paraExperimentCounts) {
		// numberOfPopulation = paraNumberOfPopulation;
		foodNumberLowerBound = paraFoodNumberLowerBound;
		foodNumberUpperBound = paraFoodNumberUpperBound;
		foodNumberIncrement = paraFoodNumberIncrement;
		limitLowerBound = paraLimitLowerBound;
		limitUpperBound = paraLimitUpperBound;
		limitIncrement = paraLimitIncrement;
		appointedLimit = paraAppointedLimit;
		maxCycles = paraMaxCycels;
		cPBest = paraCPBest;
		cGBest = paraCGBest;
		experimentCounts = paraExperimentCounts;

		System.out.println("The lower bound of food number = "
				+ foodNumberLowerBound);
		System.out.println("The upper bound of food number = "
				+ foodNumberUpperBound);
		System.out.println("The increment of food number = "
				+ foodNumberIncrement);
		// System.out.println("The running generations of BCO = " +
		// generations);
		// System.out.println("The limited neighbor solutions = " +
		// neighborLimits);
		System.out.println("The upper bound of limit = " + limitUpperBound);
		System.out.println("The lower bound of limit = " + limitLowerBound);
		System.out.println("The increment of limit = " + limitIncrement);
		System.out.println("The appointed value of limit = " + appointedLimit);
		System.out
				.println("The probability of selecting local best solution = "
						+ cPBest);
		System.out
				.println("The probability of selecting global best solution = "
						+ cGBest);
		System.out.println("The maximal generation = " + maxCycles);
		System.out.println("Experiments = " + experimentCounts);

	}// Of Construct Method

	/**
	 * The batch comparison experiment
	 */
	public String batchComparisonExperiment() {
		int numberOfChangedFoodNumber = (int) ((foodNumberUpperBound - foodNumberLowerBound) / foodNumberIncrement) + 1;
		int[] foodNumberArray = new int[numberOfChangedFoodNumber];
		int[] competitionFOF = new int[numberOfChangedFoodNumber];
		int[] nonCompetitionFOF = new int[numberOfChangedFoodNumber];

		foodNumberArray[0] = foodNumberLowerBound;
		for (int j = 1; j < numberOfChangedFoodNumber; j++) {
			foodNumberArray[j] = foodNumberArray[j - 1] + foodNumberIncrement;
		}// Of for j

		// Output the number of bees every experiment.
		// for (int k = 0; k < numberOfChangedBeeCounts; k++) {
		// System.out.println("beeCountsArray[" + k + "] = "
		// + beeCountsArray[k]);
		// }// Of for k

		// Invoke the relevant algorithm.
		for (int i = 0; i < numberOfChangedFoodNumber; i++) {
			System.out.println("\nThe " + (i + 1)
					+ " th comparison with changed number of food number.\n "
					+ "food number = " + foodNumberArray[i]);
			try {
				ReductionABCForLimit.batchMinimalTimeCostReductionABC(
						foodNumberArray[i] * 2, foodNumberArray[i],
						limitUpperBound, limitLowerBound, limitIncrement,
						cPBest, cGBest, maxCycles, experimentCounts);

				competitionFOF[i] = ReductionABCForLimit
						.getOverallFindingOptimalCount();

				// The result of non-competitive algorithm with the value of
				// remove rate set as 0.2.
				nonCompetitionFOF[i] = ReductionABCForLimit.findingOptimalCount[8];
			} catch (Exception e) {
				e.printStackTrace();
			}// Of try-catch

			// Output the result of experiments respectively.
			System.out.println("As to non-competitive algorithm, limit = "
					+ appointedLimit);
			System.out.println("FOF = " + nonCompetitionFOF[i]);
			System.out.println("\nCompetitive algorithm");
			System.out.println("FOF = " + competitionFOF[i]);
		} // Of for

		// Output the overall result of experiments.
		String resultMessage;
		resultMessage = "The final comparison result between non-competitive ABC and competitive ABC\n"
				+ "Food number\t"
				+ "Finding optimal factor(Non-competitive)\t"
				+ "Finding optimal factor(Competitive)\n";

		for (int m = 0; m < numberOfChangedFoodNumber; m++) {
			resultMessage += foodNumberArray[m] + "\t" + nonCompetitionFOF[m]
					+ "\t" + competitionFOF[m] + "\n";
		}// Of for m

		System.out.println(resultMessage);

		return resultMessage;

	}// Of batchComparisonExperiment

}// Of class ReductionABCComparison