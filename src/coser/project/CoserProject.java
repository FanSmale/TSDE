package coser.project;

import java.io.*;
import java.util.*;

import coser.datamodel.decisionsystem.*;
import coser.datamodel.informationsystem.*;
import coser.datamodel.mmer.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: A project containing .arff filename and other settings.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: The very beginning.<br>
 * Written time: March 11, 2011. <br>
 * Last modify time: March 11, 2011.
 */
public class CoserProject {

	/**
	 * Only one active project at a time.
	 */
	public static CoserProject currentProject = new CoserProject();

	/**
	 * Project filename.
	 */
	public String projectFilename;

	/**
	 * The decision system with any data.
	 */
	public RoughDecisionSystem currentRds;

	/**
	 * The information system with any data.
	 */
	public RoughInformationSystem currentRis;

	/**
	 * The cost sensitive decision system.
	 */
	public CostSensitiveDecisionSystem currentCsds;

	/**
	 * The decision system.
	 */
	public NominalDecisionSystem currentNds;

	/**
	 * The test-cost-sensitive nominal decision system.
	 */
	public TestCostNominalDecisionSystem currentTcNds;

	/**
	 * The time-sensitive decision system.
	 */
	public TimeNominalDecisionSystem currentTmNds;

	/**
	 * The time-constraint nominal decision system.
	 */
	public TimeConstraintNominalDecisionSystem currentTmConsNds;

	/**
	 * Lily's time nominal decision system.
	 */
	public LilyTimeNominalDecisionSystem currentLilyTmNds;

	/**
	 * Multi-objective time nominal decision system.
	 */
	public MultiObjectiveNominalDecisionSystem currentMoTmNds;

	/**
	 * Bco approaches for time nominal decision system.
	 */
	public BcoTimeNominalDecisionSystem currentBcoTmNds;

	/**
	 * Abc approaches for time nominal decision system.
	 */
	public SbcTimeNominalDecisionSystem currentAbcTmNds;

	/**
	 * Abc approaches for time nominal decision system.
	 */
	public AbcFormalTimeNominalDecisionSystem currentAbcFmTmNds;

	/**
	 * The numeric decision system.
	 */
	public NumericDecisionSystem currentNumericDs;

	/**
	 * The test-cost-sensitive decision system with fixed neighborhood
	 * (test-cost-sensitive neighborhood decision system).
	 */
	public TestCostDecisionSystemFixedNeighborhood currentTcDsFn;

	/**
	 * The test-cost-sensitive decision system with error range.
	 */
	public TestCostDecisionSystemErrorRange currentTcDsEr;

	/**
	 * The test-cost-sensitive decision system with normal distribution
	 * measurement error.
	 */
	public TestCostDecisionSystemNormalError currentTcDsNe;
	/**
	 * The both-cost-sensitive decision system.
	 */
	public BothCostsNominalDecisionSystem currentBcNds;

	/**
	 * The both-cost-sensitive numeric decision system.
	 */
	public BothCostsNumericDecisionSystem currentBcUds;

	/**
	 * The both-cost-sensitive normal distribution error decision system.
	 */
	public BothCostsDecisionSystemNormalError currentBcDsNe;

	/**
	 * The many-to-many entity-relation system.
	 */
	public ManyToManyEntityRelationSystem currentMmer;

	/**
	 ********************************** 
	 * Empty constructor.
	 ********************************** 
	 */
	private CoserProject() {
		currentRds = null;
		currentRis = null;
		currentCsds = null;
		currentNds = null;
		currentTcNds = null;
		currentNumericDs = null;
		currentTcDsFn = null;
		currentTcDsEr = null;
		currentTcDsNe = null;
		currentBcNds = null;
		currentBcUds = null;
		currentBcDsNe = null;
		currentMmer = null;
	}// Of empty constructor

	/**
	 ********************************** 
	 * Read a decision system with any data.
	 * 
	 * @param paraArffFilename
	 *            the arff filename.
	 * @throws Exception
	 *             if the file cannot be read.
	 ********************************** 
	 */
	public void readRoughDecisionSystem(String paraArffFilename)
			throws Exception {
		try {
			FileReader fileReader = new FileReader(paraArffFilename);
			currentRds = new RoughDecisionSystem(fileReader);
			fileReader.close();
		} catch (Exception ee) {
			throw new Exception("Error occurred while trying to read \'"
					+ paraArffFilename
					+ "\' in CoserProject.readDecisionSystem().\r\n" + ee);
		}// Of try

		currentRds.setArffFilename(paraArffFilename);
	}// Of readRoughDecisionSystem

	/**
	 ********************************** 
	 * Read a decision system of Neighborhood.
	 * 
	 * @param paraArffFilename
	 *            the arff filename.
	 * @throws Exception
	 *             if the file cannot be read.
	 ********************************** 
	 */
	public void readTestCostDecisionSystemNeighborhood(String paraArffFilename)
			throws Exception {
		try {
			FileReader fileReader = new FileReader(paraArffFilename);
			currentTcDsFn = new TestCostDecisionSystemFixedNeighborhood(
					fileReader);
			fileReader.close();
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred while trying to read \'"
							+ paraArffFilename
							+ "\' in CoserProject.readTestCostDecisionSystemNeighborhood().\r\n"
							+ ee);
		}// Of try

		currentTcDsFn.setArffFilename(paraArffFilename);

		currentRds = currentTcDsFn;
		currentCsds = currentTcDsFn;
		currentNumericDs = currentTcDsFn;
	}// Of readTestCostDecisionSystemNeighborhood

	/**
	 ********************************** 
	 * Read a nominal decision system.
	 * 
	 * @param paraArffFilename
	 *            the arff filename.
	 * @throws Exception
	 *             if the file cannot be read, or the file contains non-nominal
	 *             data.
	 ********************************** 
	 */
	public void readNominalDecisionSystem(String paraArffFilename)
			throws Exception {
		try {
			FileReader fileReader = new FileReader(paraArffFilename);
			currentNds = new NominalDecisionSystem(fileReader);
			// Common.subReductsFileName =
			// currentRds.getAllSubreductsFilename();
			fileReader.close();
		} catch (Exception ee) {
			throw new Exception("Error occurred while trying to read \'"
					+ paraArffFilename
					+ "\' in CoserProject.readNominalDecisionSystem().\r\n"
					+ ee);
		}// Of try

		currentNds.setArffFilename(paraArffFilename);
		currentNds.setAllSubreductsFilename();

		currentRds = currentNds;
		currentCsds = currentNds;
	}// Of readNominalDecisionSystem

	/**
	 ********************************** 
	 * Read a decision system with any data.
	 * 
	 * @param paraArffFilename
	 *            the arff filename.
	 * @throws Exception
	 *             if the file cannot be read.
	 ********************************** 
	 */
	public void readTestCostNominalDecisionSystem(String paraArffFilename)
			throws Exception {
		try {
			FileReader fileReader = new FileReader(paraArffFilename);
			currentTcNds = new TestCostNominalDecisionSystem(fileReader);
			// currentTcNds.setClassIndex(currentTcNds.numAttributes() - 1);
			fileReader.close();
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred while trying to read \'"
							+ paraArffFilename
							+ "\' in CoserProject.readTestCostNominalDecisionSystem().\r\n"
							+ ee);
		}// Of try

		currentTcNds.setArffFilename(paraArffFilename);

		currentTcNds.setAllSubreductsFilename();

		// It can degenerate to a decision system
		currentRds = currentTcNds;
		currentCsds = currentTcNds;
		currentNds = currentTcNds;
	}// Of readTestCostNominalDecisionSystem

	/**
	 ********************************** 
	 * Read a decision system with any data.
	 * 
	 * @param paraArffFilename
	 *            the arff filename.
	 * @throws Exception
	 *             if the file cannot be read.
	 ********************************** 
	 */
	public void readTimeNominalDecisionSystem(String paraArffFilename)
			throws Exception {
		try {
			FileReader fileReader = new FileReader(paraArffFilename);
			currentTmNds = new TimeNominalDecisionSystem(fileReader);
			// currentTcNds.setClassIndex(currentTcNds.numAttributes() - 1);
			fileReader.close();
		} catch (Exception ee) {
			throw new Exception("Error occurred while trying to read \'"
					+ paraArffFilename
					+ "\' in CoserProject.readTimeNominalDecisionSystem().\r\n"
					+ ee);
		}// Of try

		currentTmNds.setArffFilename(paraArffFilename);

		currentTmNds.setAllSubreductsFilename();

		// It can degenerate to a decision system
		currentRds = currentTmNds;
		currentCsds = currentTmNds;
	}// Of readTimeNominalDecisionSystem

	/**
	 ********************************** 
	 * Read a numeric decision system.
	 * 
	 * @param paraArffFilename
	 *            the arff filename.
	 * @throws Exception
	 *             if the file cannot be read.
	 ********************************** 
	 */
	public void readNumericDecisionSystem(String paraArffFilename)
			throws Exception {
		try {
			FileReader fileReader = new FileReader(paraArffFilename);
			currentNumericDs = new NumericDecisionSystem(fileReader);
			currentNumericDs
					.setClassIndex(currentNumericDs.numAttributes() - 1);
			fileReader.close();
		} catch (Exception ee) {
			throw new Exception("Error occurred while trying to read \'"
					+ paraArffFilename
					+ "\' in CoserProject.readArffFile().\r\n" + ee);
		}// Of try

		// Only accept numeric attributes.
		if (currentNumericDs.isNominal()) {
			throw new Exception("Error occurred while trying to read \'"
					+ paraArffFilename
					+ "\' in CoserProject.readArffFile().\r\n"
					+ "The dataset is nominal. Please try other approaches.");
		}// Of if

		currentNumericDs.setArffFilename(paraArffFilename);

		currentRds = currentNumericDs;
		currentCsds = currentNumericDs;
	}// Of readNumericDecisionSystem

	/**
	 ********************************** 
	 * Read a decision system with numeric data and both costs.
	 * 
	 * @param paraArffFilename
	 *            the arff filename.
	 * @throws Exception
	 *             if the file cannot be read.
	 ********************************** 
	 */
	public void readTestCostDecisionSystemErrorRange(String paraArffFilename)
			throws Exception {
		try {
			FileReader fileReader = new FileReader(paraArffFilename);
			currentTcDsEr = new TestCostDecisionSystemErrorRange(fileReader);
			fileReader.close();
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred while trying to read \'"
							+ paraArffFilename
							+ "\' in CoserProject.readTestCostDecisionSystemErrorRange().\r\n"
							+ ee);
		}// Of try

		currentTcDsEr.setArffFilename(paraArffFilename);

		currentRds = currentTcDsEr;
		currentCsds = currentTcDsEr;
	}// Of readTestCostDecisionSystemErrorRange

	/**
	 ********************************** 
	 * Read a decision system with normal distribution error and test cost.
	 * 
	 * @param paraArffFilename
	 *            the arff filename.
	 * @throws Exception
	 *             if the file cannot be read.
	 ********************************** 
	 */
	public void readTestCostDecisionSystemNormalError(String paraArffFilename)
			throws Exception {
		try {
			FileReader fileReader = new FileReader(paraArffFilename);
			currentTcDsNe = new TestCostDecisionSystemNormalError(fileReader);
			fileReader.close();
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred while trying to read \'"
							+ paraArffFilename
							+ "\' in CoserProject.readTestCostDecisionSystemNormalError().\r\n"
							+ ee);
		}// Of try

		currentTcDsNe.setArffFilename(paraArffFilename);

		currentRds = currentTcDsNe;
		currentCsds = currentTcDsNe;
	}// Of readTestCostDecisionSystemNormalError

	/**
	 ********************************** 
	 * Read a decision system with nominal data and both costs.
	 * 
	 * @param paraArffFilename
	 *            the arff filename.
	 * @throws Exception
	 *             if the file cannot be read.
	 ********************************** 
	 */
	public void readBothCostsNominalDecisionSystem(String paraArffFilename)
			throws Exception {
		try {
			FileReader fileReader = new FileReader(paraArffFilename);
			currentBcNds = new BothCostsNominalDecisionSystem(fileReader);
			fileReader.close();
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred while trying to read \'"
							+ paraArffFilename
							+ "\' in CoserProject.readBothCostsNominalDecisionSystem().\r\n"
							+ ee);
		}// Of try

		currentBcNds.setArffFilename(paraArffFilename);
		currentBcNds.setAllSubreductsFilename();
		// setArffFilename(paraArffFilename);

		// It can degenerte to a decision system
		// currentTcNds = currentBcNds;
		currentRds = currentBcNds;
		currentCsds = currentBcNds;
		currentNds = currentBcNds;
	}// Of readBothCostsNominalDecisionSystem

	/**
	 ********************************** 
	 * Read a information system with numeric data and both costs.
	 * 
	 * @param paraArffFilename
	 *            the arff filename.
	 * @throws Exception
	 *             if the file cannot be read.
	 ********************************** 
	 */
	public void readRoughInformationSystem(String paraArffFilename)
			throws Exception {
		try {
			FileReader fileReader = new FileReader(paraArffFilename);
			currentRis = new RoughInformationSystem(fileReader);
			fileReader.close();
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred while trying to read \'"
							+ paraArffFilename
							+ "\' in CoserProject.readBothCostsNumericDecisionSystem().\r\n"
							+ ee);
		}// Of try

		currentRis.setArffFilename(paraArffFilename);
		currentRis.setAllSubreductsFilename();

		// setArffFilename(paraArffFilename);

		// It can degenerte to a decision system
	}// Of readRoughInformationSystem

	/**
	 ********************************** 
	 * Read a decision system with numeric data and both costs.
	 * 
	 * @param paraArffFilename
	 *            the arff filename.
	 * @throws Exception
	 *             if the file cannot be read.
	 ********************************** 
	 */
	public void readBothCostsNumericDecisionSystem(String paraArffFilename)
			throws Exception {
		try {
			FileReader fileReader = new FileReader(paraArffFilename);
			currentBcUds = new BothCostsNumericDecisionSystem(fileReader);
			fileReader.close();
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred while trying to read \'"
							+ paraArffFilename
							+ "\' in CoserProject.readBothCostsNumericDecisionSystem().\r\n"
							+ ee);
		}// Of try

		currentBcUds.setArffFilename(paraArffFilename);
		currentBcUds.setAllSubreductsFilename();

		currentRds = currentBcUds;
		currentCsds = currentBcUds;

		// setArffFilename(paraArffFilename);

		// It can degenerte to a decision system
	}// Of readBothCostsNumericDecisionSystem

	/**
	 ********************************** 
	 * Read a decision system with normal distribution error and both costs.
	 * 
	 * @param paraArffFilename
	 *            the arff filename.
	 * @throws Exception
	 *             if the file cannot be read.
	 ********************************** 
	 */
	public void readBothCostsDecisionSystemNormalError(String paraArffFilename)
			throws Exception {
		try {

			FileReader fileReader = new FileReader(paraArffFilename);
			currentBcDsNe = new BothCostsDecisionSystemNormalError(fileReader);
			fileReader.close();
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred while trying to read \'"
							+ paraArffFilename
							+ "\' in CoserProject.readBothCostsDecisionSystemNormalError().\r\n"
							+ ee);
		}// Of try

		currentBcDsNe.setArffFilename(paraArffFilename);
		currentBcDsNe.setAllSubreductsFilename();

		currentRds = currentBcDsNe;
		currentCsds = currentBcDsNe;

		// It can degenerte to a decision system
	}// Of readBothCostsDecisionSystemNormalError

	/**
	 ********************************** 
	 * Read a decision system with numeric data and both costs.
	 * 
	 * @param paraFirstSetFilename
	 *            the arff filename of the first set.
	 * @param paraSecondSetFilename
	 *            the arff filename of the second set.
	 * @param paraRelationFilename
	 *            the arff filename of the relation set.
	 * @param paraHaveID
	 *            the datasets have ID information.
	 * @throws Exception
	 *             if the file cannot be read.
	 ********************************** 
	 */
	public void readMmerSystem(String paraFirstSetFilename,
			String paraSecondSetFilename, String paraRelationFilename,
			boolean paraHaveID) throws Exception {
		try {
			currentMmer = new ManyToManyEntityRelationSystem(
					paraFirstSetFilename, paraSecondSetFilename,
					paraRelationFilename, paraHaveID);
		} catch (Exception ee) {
			throw new Exception("Error occurred while trying to read \'"
					+ paraFirstSetFilename + ", " + paraSecondSetFilename
					+ ", " + paraRelationFilename
					+ "\' in CoserProject.readMmerSystem().\r\n" + ee);
		}// Of try
	}// Of readBothCostsNumericDecisionSystem

	/**
	 ********************************** 
	 * Save settings. Often used for the save as operation. paraNewFilename the
	 * new filename.
	 ********************************** 
	 */
	public void saveSettings(String paraNewFilename) throws Exception {
		projectFilename = paraNewFilename;
		saveSettings();
	}// Of saveSettings

	/**
	 ********************************** 
	 * Save settings.
	 ********************************** 
	 */
	public void saveSettings() throws Exception {
		Properties property = new Properties();
		property.setProperty("Arff_Filename", currentRds.getArffFilename());

		try {
			property.store(new FileOutputStream(new File(projectFilename)),
					"Basic options of Coser. Fan Min, minfanphd@163.com");
		} catch (Exception ee) {
			throw new Exception(
					"Error occurred in common.Options.storeConfiguration()."
							+ "\n\t Invalid configuration filename: "
							+ projectFilename
							+ "\n\t The initial Exception is: " + ee);
		}// Of try
	}// Of saveSettings

}// Of CoserProject
