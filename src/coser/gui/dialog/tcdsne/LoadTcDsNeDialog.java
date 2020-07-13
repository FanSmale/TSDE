package coser.gui.dialog.tcdsne;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import coser.datamodel.decisionsystem.*;
import coser.project.*;

import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;
import coser.gui.menu.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Load a test-cost-sensitive decision system with normal distribution
 * measurement errors.
 * <p>
 * Author: <b>Hong Zhao</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Almost done.<br>
 * Written time: August 12, 2012. <br>
 * Last modify time: September 22, 2012.
 */
public class LoadTcDsNeDialog extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7927371569940932790L;

	public static LoadTcDsNeDialog loadTcDsNeDialog = new LoadTcDsNeDialog();

	/**
	 * Select the arff file.
	 */
	private FilenameField arffFilenameField;

	/**
	 * Confidence Interval.
	 */
	private DoubleField confidenceIntervalField;

	/**
	 * The lower bound of additional tests for each test.
	 */
	private IntegerField additionalTestsLowerBoundField;

	/**
	 * The upper bound of additional tests for each test.
	 */
	private IntegerField additionalTestsUpperBoundField;

	/**
	 * Step length of error increase for each additional test.
	 */
	// private DoubleField errorLengthField;

	/**
	 * The test cost times of a finer test with respect to a coser one. For
	 * example, a test is $5, and a finer test has four times the cost, then the
	 * latter one has a cost of $20.
	 */
	private IntegerField finerTestCostTimesField;

	/**
	 * The lower bound of the test-cost.
	 */
	private IntegerField testCostLowerBoundField;

	/**
	 * The upper bound of the test-cost.
	 */
	private IntegerField testCostUpperBoundField;

	/**
	 * Distribution: UNIFORM, NORMAL, or PARETO.
	 */
	private JComboBox<String> distributionComboBox;

	/**
	 * Random mode.
	 */
	private JComboBox<String> randomModeComboBox;

	/**
	 * The alpha value. Not valid for the UNIFORM distribution.
	 */
	private IntegerField alphaField;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private LoadTcDsNeDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Additional tests and test-cost settings",
				true);

		arffFilenameField = new FilenameField(30);
		// arffFilenameField.setText("data/irisNormalized.arff");
		arffFilenameField.setText("data/wineNormalizedzh.arff");
		// arffFilenameField.setText("data/datasets/liverzh1.arff");
		Button browseButton = new Button(" Browse ");
		browseButton.addActionListener(arffFilenameField);

		Panel sourceFilePanel = new Panel();
		sourceFilePanel.add(new Label("The .arff file:"));
		sourceFilePanel.add(arffFilenameField);
		sourceFilePanel.add(browseButton);

		// For additional tests
		// confidenceIntervalField = new DoubleField("0.98");
		confidenceIntervalField = new DoubleField("1");
		additionalTestsLowerBoundField = new IntegerField("0");
		additionalTestsUpperBoundField = new IntegerField("0");
		// errorLengthField = new DoubleField("0.005");
		finerTestCostTimesField = new IntegerField("2");

		// For test costs
		testCostLowerBoundField = new IntegerField("1");
		testCostUpperBoundField = new IntegerField("100");
		String[] distributionModes = { "Uniform", "Normal", "Bounded Pareto" };
		distributionComboBox = new JComboBox<String>(distributionModes);
		distributionComboBox.setSelectedIndex(0);
		String[] randomModes = { "Psuedo", "Real" };
		randomModeComboBox = new JComboBox<String>(randomModes);
		randomModeComboBox.setSelectedIndex(0);
		alphaField = new IntegerField("2");

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(12, 2));
		centerPanel.add(new Label("Settings for additional tests"));
		centerPanel.add(new Label(""));
		centerPanel.add(new Label("Probability:[0,1]"));
		centerPanel.add(confidenceIntervalField);
		centerPanel.add(new Label("Minimal additional tests:"));
		centerPanel.add(additionalTestsLowerBoundField);
		centerPanel.add(new Label("Maximal additional tests:"));
		centerPanel.add(additionalTestsUpperBoundField);

		centerPanel.add(new Label("Settings for test costs"));
		centerPanel.add(new Label(""));
		centerPanel.add(new Label("Test-cost lower bound: "));
		centerPanel.add(testCostLowerBoundField);
		centerPanel.add(new Label("Test-cost upper bound: "));
		centerPanel.add(testCostUpperBoundField);
		centerPanel.add(new Label("Test-cost distribution: "));
		centerPanel.add(distributionComboBox);
		centerPanel.add(new Label("Random mode: "));
		centerPanel.add(randomModeComboBox);
		centerPanel.add(new Label("Alpha (invalid for Uniform): "));
		centerPanel.add(alphaField);
		centerPanel.add(new Label("The cost times of a finer test:"));
		centerPanel.add(finerTestCostTimesField);

		Button okButton = new Button(" OK ");
		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog("Test-cost distribution",
				"coser/gui/dialog/tcdsne/LoadTcDsNeHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(okButton);
		okPanel.add(cancelButton);
		okPanel.add(helpButton);

		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, sourceFilePanel);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, okPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(200, 200);
		setSize(480, 350);
		addWindowListener(dialogCloser);
		setVisible(false);
	}// Of constructor

	/**
	 *************************** 
	 * Read the arff file.
	 *************************** 
	 */
	public void actionPerformed(ActionEvent ae) {
		setVisible(false);
		ProgressDialog.progressDialog
				.setMessageAndShow("Please wait a few seconds. Setting ... \r\n");

		try {
			// Extend the decision system first to have more tests
			double confidenceInterval = confidenceIntervalField.getValue();
			// double errorLength = errorLengthField.getValue();
			int additionalTestsLowerBound = additionalTestsLowerBoundField
					.getValue();
			int additionalTestsUpperBound = additionalTestsUpperBoundField
					.getValue();
			int finerTestCostTimes = finerTestCostTimesField.getValue();
			// Random random = new Random();
			String tempFilename = arffFilenameField.getText().trim();
			try {
				// Read data from the source file.
				CoserProject.currentProject
						.readTestCostDecisionSystemNormalError(tempFilename);
			} catch (Exception ee) {
				ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
				ProgressDialog.progressDialog.setVisible(false);
				return;
			}// Of try

			// An instances to store objects.
			TestCostDecisionSystemNormalError currentDecisionSystem = CoserProject.currentProject.currentTcDsNe;
			// Read test cost options.
			int lowerBound = testCostLowerBoundField.getValue();
			int upperBound = testCostUpperBoundField.getValue();
			currentDecisionSystem.setIndividualCostBounds(
					CostSensitiveDecisionSystem.COST_TYPE_MONEY, lowerBound,
					upperBound);
			int distribution = distributionComboBox.getSelectedIndex();
			currentDecisionSystem.setIndividualCostDistribution(
					CostSensitiveDecisionSystem.COST_TYPE_MONEY, distribution);
			boolean randomMode = (randomModeComboBox.getSelectedIndex() == 1);
			currentDecisionSystem.setRandomMode(randomMode);
			int alpha = alphaField.getValue();
			currentDecisionSystem.setAlpha(alpha);
			finerTestCostTimes = finerTestCostTimesField.getValue();
			currentDecisionSystem.setFinerTestCostTimes(finerTestCostTimes);

			String newFilename = "";
			newFilename = currentDecisionSystem
					.extendNormalDistributionObservationalError(
							additionalTestsLowerBound,
							additionalTestsUpperBound, confidenceInterval);
			currentDecisionSystem.setArffFilename(newFilename);

			// Finally refresh test-cost
			currentDecisionSystem.refreshTestCost();

			// For test-cost-sensitive decision systems with error range
			CoserMenuBar.coserMenuBar.normalErrorAllReductsMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.normalErrorOptimalReductMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.normalErrorDeltaReductionMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.normalErrorLambdaReductionMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.normalErrorTimeComparisonMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.refreshTestCostMenuItem.setEnabled(true);

			// Let's have a look.
			ProgressDialog.progressDialog
					.setMessageAndShow(currentDecisionSystem.toString());
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
			return;
		}// Of try
	}// Of actionPerformed

}// Of class LoadTcDsNeDialog
