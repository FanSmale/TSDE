package coser.gui.dialog.bcdsne;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

import javax.swing.*;

import coser.common.*;
import coser.datamodel.decisionsystem.*;
import coser.project.*;

import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;
import coser.gui.menu.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Load a normalized both cost sensitive decision system.
 * <p>
 * Author: <b>Fan Min, Hong Zhao</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Almost done.<br>
 * Written time: February 18, 2013. <br>
 * Last modify time: February 18, 2013.
 */
public class LoadBcDsNeDialog extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5910799961661768013L;

	public static LoadBcDsNeDialog loadBcDsNeDialog = new LoadBcDsNeDialog();

	/**
	 * Select the arff file.
	 */
	private FilenameField arffFilenameField;

	/**
	 * The lower bound of the test-cost.
	 */
	private IntegerField lowerBoundField;

	/**
	 * The upper bound of the test-cost.
	 */
	private IntegerField upperBoundField;

	/**
	 * Distribution: UNIFORM, NORMAL, or PARETO.
	 */
	private JComboBox<String> distributionComboBox;

	/**
	 * Test-cost relationship.
	 */
	private JComboBox<String> testCostRelationshipComboBox;

	/**
	 * Random mode.
	 */
	private JComboBox<String> randomModeComboBox;

	/**
	 * The alpha value. Not valid for the UNIFORM distribution.
	 */
	private IntegerField alphaField;
	/**
	 * The delta value. Adjustment factor for the error range.
	 */
	private DoubleField deltaField;

	/**
	 * How to specify the misclassification cost.
	 */
	// private JComboBox misCostComboBox;

	/**
	 * The text area to specify misclassification costs matrix.
	 */
	private TextArea misclassificationCostMatrixArea;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private LoadBcDsNeDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Both costs settings", true);

		arffFilenameField = new FilenameField(30);
		// arffFilenameField.setText("data/votingNormalized.arff");
		// arffFilenameField.setText("data/datasets/wdbc_norm.arff");
		arffFilenameField.setText("data/datasets/liver_norm.arff");
		Button browseButton = new Button(" Browse ");
		browseButton.addActionListener(arffFilenameField);

		Panel sourceFilePanel = new Panel();
		sourceFilePanel.add(new Label("The .arff file:"));
		sourceFilePanel.add(arffFilenameField);
		sourceFilePanel.add(browseButton);

		lowerBoundField = new IntegerField("1");
		upperBoundField = new IntegerField("10");
		String[] distributionModes = { "Uniform", "Normal", "Bounded Pareto" };
		distributionComboBox = new JComboBox<String>(distributionModes);
		distributionComboBox.setSelectedIndex(0);
		String[] relationshipModes = { "Independent", "Simple common",
				"Complex common" };
		testCostRelationshipComboBox = new JComboBox<String>(relationshipModes);
		testCostRelationshipComboBox.setSelectedIndex(0);
		String[] randomModes = { "Pseudo", "Real" };
		randomModeComboBox = new JComboBox<String>(randomModes);
		randomModeComboBox.setSelectedIndex(0);
		alphaField = new IntegerField("2");
		deltaField = new DoubleField("0.98");
		// String [] misCostModes = {"Provided below", "Provided later"};
		// misCostComboBox = new JComboBox(misCostModes);
		// misCostComboBox.setSelectedIndex(0);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(8, 2));
		centerPanel.add(new Label("Test-cost lower bound: "));
		centerPanel.add(lowerBoundField);
		centerPanel.add(new Label("Test-cost upper bound: "));
		centerPanel.add(upperBoundField);
		centerPanel.add(new Label("Test-cost distribution: "));
		centerPanel.add(distributionComboBox);
		centerPanel.add(new Label("Test-cost relationship: "));
		centerPanel.add(testCostRelationshipComboBox);
		centerPanel.add(new Label("Random mode: "));
		centerPanel.add(randomModeComboBox);
		centerPanel.add(new Label("Alpha (invalid for Uniform): "));
		centerPanel.add(alphaField);
		centerPanel.add(new Label(
				"Delta (Adjustment factor for the error range): "));
		centerPanel.add(deltaField);
		centerPanel.add(new Label("Misclassification cost matrix: "));
		// centerPanel.add(misCostComboBox);

		misclassificationCostMatrixArea = new TextArea(3, 3);
		misclassificationCostMatrixArea.setText("0,500,\r\n500,0,end.");

		Panel mainPanel = new Panel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(BorderLayout.NORTH, sourceFilePanel);
		mainPanel.add(BorderLayout.CENTER, centerPanel);
		mainPanel.add(BorderLayout.SOUTH, misclassificationCostMatrixArea);

		Button okButton = new Button(" OK ");
		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog("Test-cost distribution",
				"coser/gui/dialog/bcdsne/LoadBcDsNeHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(okButton);
		okPanel.add(cancelButton);
		okPanel.add(helpButton);

		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, mainPanel);
		add(BorderLayout.SOUTH, okPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(200, 200);
		setSize(420, 300);
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
			String tempFilename = arffFilenameField.getText().trim();
			NumberFormat numFormat = NumberFormat.getNumberInstance();
			numFormat.setMaximumFractionDigits(3);
			try {
				// Read data from the source file.
				CoserProject.currentProject
						.readBothCostsDecisionSystemNormalError(tempFilename);
			} catch (Exception ee) {
				ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
				ProgressDialog.progressDialog.setVisible(false);
				return;
			}// Of try
				// Just another name to make statements shorter.
			BothCostsDecisionSystemNormalError tempDecisionSystem = CoserProject.currentProject.currentBcDsNe;

			int lowerBound = Integer.parseInt(lowerBoundField.getText());
			int upperBound = Integer.parseInt(upperBoundField.getText());
			tempDecisionSystem.setIndividualCostBounds(
					CostSensitiveDecisionSystem.COST_TYPE_MONEY, lowerBound,
					upperBound);
			int distribution = distributionComboBox.getSelectedIndex();
			tempDecisionSystem.setIndividualCostDistribution(
					CostSensitiveDecisionSystem.COST_TYPE_MONEY, distribution);
			int relationship = testCostRelationshipComboBox.getSelectedIndex();
			tempDecisionSystem.setTestCostsRelationShip(relationship);
			boolean randomMode = (randomModeComboBox.getSelectedIndex() == 1);
			tempDecisionSystem.setCostRandomMode(randomMode);
			int alpha = Integer.parseInt(alphaField.getText().trim());
			tempDecisionSystem.setAlpha(alpha);
			double delta = Double.parseDouble(deltaField.getText().trim());
			tempDecisionSystem.setNormalErrorSizeArray(delta);

			tempDecisionSystem
					.setMisclassificationCostMatrix(misclassificationCostMatrixArea
							.getText());

			// Finally refresh test-cost
			tempDecisionSystem.refreshTestCost();

			// For decision systems
			CoserMenuBar.coserMenuBar.positiveRegionMenuItem.setEnabled(true);
			CoserMenuBar.coserMenuBar.majorityMenuItem.setEnabled(true);
			CoserMenuBar.coserMenuBar.normalizationMenuItem.setEnabled(true);
			CoserMenuBar.coserMenuBar.allReductsMenuItem.setEnabled(true);
			CoserMenuBar.coserMenuBar.allSubreductsMenuItem.setEnabled(true);

			// For test-cost-sensitive decision systems
			CoserMenuBar.coserMenuBar.minimalTestCostReductionMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.testCostConstraintReductionExhaustiveMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.testCostConstraintReductionMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.minimalTestCostReductionGAMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.testCostConstraintReductionGAMenuItem
					.setEnabled(true);
			// For both-cost-sensitive numeric decision systems
			CoserMenuBar.coserMenuBar.refreshTestCostMenuItem.setEnabled(true);
			CoserMenuBar.coserMenuBar.bcDsNeOptimalFeatureSubsetMenuItem
					.setEnabled(true);
			// CoserMenuBar.coserMenuBar.bcDsNeOptimalFeatureSubsetsMenuItem.setEnabled(true);
			CoserMenuBar.coserMenuBar.normalErrorLambdaFeatureSelectionMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.normalErrorFeatureSelectionTimeComparisonMenuItem
					.setEnabled(true);
			// Let's have a look.

			String tempString = "Decision system and cost information loaded."
					+ "\r\nThe test cost vector is: "
					+ SimpleTool.intArrayToString(
							tempDecisionSystem.getIndividualTestCostArray(),
							',')
					+ "\r\nSome sample computations are given below."
					+ "\r\nAverage misclassification cost by selecting the first two attributes: "
					+ numFormat.format(tempDecisionSystem
							.averageMisclassificationCost(0))
					+ "\r\nAverage misclassification cost by selecting the first three attributes: "
					+ numFormat.format(tempDecisionSystem
							.averageMisclassificationCost(7))
					+ "\r\nAverage total cost by selecting the first two, three, four attributes: "
					+ numFormat.format(tempDecisionSystem.totalCost(3)) + ", "
					+ numFormat.format(tempDecisionSystem.totalCost(7)) + ", "
					+ numFormat.format(tempDecisionSystem.totalCost(15))
					+ "\r\nThe end.";
			ProgressDialog.progressDialog.setMessageAndShow(tempString);

		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
			return;
		}// Of try
			// ProgressDialog.progressDialog.setVisible(false);
	}// Of actionPerformed

}// Of class LoadBcUdsDialog
