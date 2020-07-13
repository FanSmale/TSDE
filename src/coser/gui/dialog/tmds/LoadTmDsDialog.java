package coser.gui.dialog.tmds;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import coser.datamodel.decisionsystem.*;
import coser.project.*;

import coser.gui.guicommon.*;
import coser.gui.menu.CoserMenuBar;
import coser.gui.others.*;
import coser.gui.dialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Load a normalized test cost sensitive decision system with error
 * range.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Almost done.<br>
 * Written time: August 12, 2011. <br>
 * Last modify time: August 12, 2011.
 */
public class LoadTmDsDialog extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1826087094625089819L;

	public static LoadTmDsDialog loadTmDsDialog = new LoadTmDsDialog();

	/**
	 * Select the arff file.
	 */
	private FilenameField arffFilenameField;

	/**
	 * Select the sequence file.
	 */
	private FilenameField sequenceFilenameField;

	/**
	 * The lower bound of the test cost.
	 */
	private IntegerField testCostLowerBoundField;

	/**
	 * The upper bound of the test cost.
	 */
	private IntegerField testCostUpperBoundField;

	/**
	 * Distribution: UNIFORM, NORMAL, or PARETO.
	 */
	private JComboBox<String> testCostDistributionComboBox;

	/**
	 * Test-cost relationship.
	 */
	private JComboBox<String> testCostRelationshipComboBox;

	/**
	 * The lower bound of the testing time.
	 */
	private IntegerField testingTimeLowerBoundField;

	/**
	 * The upper bound of the testing time.
	 */
	private IntegerField testingTimeUpperBoundField;

	/**
	 * The lower bound of the waiting time.
	 */
	private IntegerField waitingTimeLowerBoundField;

	/**
	 * The upper bound of the waiting time.
	 */
	private IntegerField waitingTimeUpperBoundField;

	/**
	 * Distribution: UNIFORM, NORMAL, or PARETO.
	 */
	private JComboBox<String> testingTimeDistributionComboBox;

	/**
	 * Distribution: UNIFORM, NORMAL, or PARETO.
	 */
	private JComboBox<String> waitingTimeDistributionComboBox;

	/**
	 * Time parts.
	 */
	private JComboBox<String> timePartsComboBox;

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
	private LoadTmDsDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Test-cost settings", true);

		arffFilenameField = new FilenameField(30);
		arffFilenameField.setText("data/iris.arff");
		Button browseButton = new Button(" Browse ");
		browseButton.addActionListener(arffFilenameField);

		sequenceFilenameField = new FilenameField(30);
		sequenceFilenameField.setText("data/iris.ord");
		Button sequenceBrowseButton = new Button(" Browse ");
		sequenceBrowseButton.addActionListener(sequenceFilenameField);

		Panel sourceFilePanel = new Panel();
		sourceFilePanel.add(new Label("The .arff file:"));
		sourceFilePanel.add(arffFilenameField);
		sourceFilePanel.add(browseButton);
		Panel sequenceFilePanel = new Panel();
		sequenceFilePanel.add(new Label("The sequence file:"));
		sequenceFilePanel.add(sequenceFilenameField);
		sequenceFilePanel.add(sequenceBrowseButton);

		Panel filesPanel = new Panel();
		filesPanel.setLayout(new GridLayout(2, 1));
		filesPanel.add(sourceFilePanel);
		filesPanel.add(sequenceFilePanel);

		// test cost
		testCostLowerBoundField = new IntegerField("1");
		testCostUpperBoundField = new IntegerField("100");
		String[] testCostDistributionModes = { "Uniform", "Normal",
				"Bounded Pareto" };
		testCostDistributionComboBox = new JComboBox<String>(
				testCostDistributionModes);
		testCostDistributionComboBox.setSelectedIndex(0);
		String[] relationshipModes = { "Independent", "Simple common",
				"Complex common" };
		testCostRelationshipComboBox = new JComboBox<String>(relationshipModes);
		testCostRelationshipComboBox.setSelectedIndex(0);

		// testing time and waiting time
		testingTimeLowerBoundField = new IntegerField("1");
		testingTimeUpperBoundField = new IntegerField("100");
		waitingTimeLowerBoundField = new IntegerField("1");
		waitingTimeUpperBoundField = new IntegerField("500");
		String[] distributionModes = { "Uniform", "Normal", "Bounded Pareto" };
		testingTimeDistributionComboBox = new JComboBox<String>(
				distributionModes);
		testingTimeDistributionComboBox.setSelectedIndex(0);
		waitingTimeDistributionComboBox = new JComboBox<String>(
				distributionModes);
		waitingTimeDistributionComboBox.setSelectedIndex(0);

		String[] timePartsString = { "Only testing", "Testing and waiting" };
		timePartsComboBox = new JComboBox<String>(timePartsString);
		timePartsComboBox.setSelectedIndex(1);
		String[] randomModes = { "Pseudo", "Real" };
		randomModeComboBox = new JComboBox<String>(randomModes);
		randomModeComboBox.setSelectedIndex(0);
		alphaField = new IntegerField("2");

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(13, 2));

		centerPanel.add(new Label("Test cost lower bound: "));
		centerPanel.add(testCostLowerBoundField);
		centerPanel.add(new Label("Test cost upper bound: "));
		centerPanel.add(testCostUpperBoundField);
		centerPanel.add(new Label("Test cost distribution: "));
		centerPanel.add(testCostDistributionComboBox);
		centerPanel.add(new Label("Test-cost relationship: "));
		centerPanel.add(testCostRelationshipComboBox);

		centerPanel.add(new Label("Testing-time lower bound: "));
		centerPanel.add(testingTimeLowerBoundField);
		centerPanel.add(new Label("Testing-time upper bound: "));
		centerPanel.add(testingTimeUpperBoundField);
		centerPanel.add(new Label("Testing-time distribution: "));
		centerPanel.add(testingTimeDistributionComboBox);
		centerPanel.add(new Label("Waiting-time lower bound: "));
		centerPanel.add(waitingTimeLowerBoundField);
		centerPanel.add(new Label("Waiting-time upper bound: "));
		centerPanel.add(waitingTimeUpperBoundField);
		centerPanel.add(new Label("Waiting-time distribution: "));
		centerPanel.add(waitingTimeDistributionComboBox);

		centerPanel.add(new Label("Time relationship: "));
		centerPanel.add(timePartsComboBox);
		centerPanel.add(new Label("Random mode: "));
		centerPanel.add(randomModeComboBox);
		centerPanel.add(new Label("Alpha (invalid for Uniform): "));
		centerPanel.add(alphaField);

		Button okButton = new Button(" OK ");
		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog(
				"Time sensitive data loading",
				"coser/gui/dialog/tmds/LoadTmDsHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(okButton);
		okPanel.add(cancelButton);
		okPanel.add(helpButton);

		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, filesPanel);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, okPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(200, 200);
		setSize(420, 400);
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
			try {
				// Read data from the source file.
				CoserProject.currentProject
						.readTimeNominalDecisionSystem(tempFilename);
			} catch (Exception ee) {
				ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
				ProgressDialog.progressDialog.setVisible(false);
				return;
			}// Of try

			// Just another name to make statements shorter.
			TimeNominalDecisionSystem tempDecisionSystem = CoserProject.currentProject.currentTmNds;

			// Money
			int lowerBound = Integer
					.parseInt(testCostLowerBoundField.getText());
			int upperBound = Integer
					.parseInt(testCostUpperBoundField.getText());
			tempDecisionSystem.setIndividualCostBounds(
					CostSensitiveDecisionSystem.COST_TYPE_MONEY, lowerBound,
					upperBound);
			int distribution = testCostDistributionComboBox.getSelectedIndex();
			tempDecisionSystem.setIndividualCostDistribution(
					CostSensitiveDecisionSystem.COST_TYPE_MONEY, distribution);

			// Testing time
			lowerBound = Integer.parseInt(testingTimeLowerBoundField.getText());
			upperBound = Integer.parseInt(testingTimeUpperBoundField.getText());
			tempDecisionSystem.setIndividualCostBounds(
					CostSensitiveDecisionSystem.COST_TYPE_TESTING_TIME,
					lowerBound, upperBound);
			distribution = testingTimeDistributionComboBox.getSelectedIndex();
			tempDecisionSystem.setIndividualCostDistribution(
					CostSensitiveDecisionSystem.COST_TYPE_TESTING_TIME,
					distribution);
			int timeParts = timePartsComboBox.getSelectedIndex() + 1;

			// Waiting time
			if (timeParts == 2) {
				// Also set for the waiting time
				lowerBound = Integer.parseInt(waitingTimeLowerBoundField
						.getText());
				upperBound = Integer.parseInt(waitingTimeUpperBoundField
						.getText());
				tempDecisionSystem.setIndividualCostBounds(
						CostSensitiveDecisionSystem.COST_TYPE_WAITING_TIME,
						lowerBound, upperBound);
				distribution = waitingTimeDistributionComboBox
						.getSelectedIndex();
				tempDecisionSystem.setIndividualCostDistribution(
						CostSensitiveDecisionSystem.COST_TYPE_WAITING_TIME,
						distribution);
			}// Of if

			boolean randomMode = (randomModeComboBox.getSelectedIndex() == 1);
			tempDecisionSystem.setCostRandomMode(randomMode);
			int alpha = Integer.parseInt(alphaField.getText().trim());
			tempDecisionSystem.setAlpha(alpha);

			// Finally refresh test-cost
			tempDecisionSystem
					.generateIndividualCosts(CostSensitiveDecisionSystem.COST_TYPE_MONEY);
			tempDecisionSystem
					.generateIndividualCosts(CostSensitiveDecisionSystem.COST_TYPE_TESTING_TIME);
			if (timeParts == 2) {
				tempDecisionSystem
						.generateIndividualCosts(CostSensitiveDecisionSystem.COST_TYPE_WAITING_TIME);
			}

			// Clone for four subclasses
			CoserProject.currentProject.currentLilyTmNds = new LilyTimeNominalDecisionSystem(
					tempDecisionSystem);
			CoserProject.currentProject.currentBcoTmNds = new BcoTimeNominalDecisionSystem(
					tempDecisionSystem);
			CoserProject.currentProject.currentMoTmNds = new MultiObjectiveNominalDecisionSystem(
					tempDecisionSystem);
			CoserProject.currentProject.currentTmConsNds = new TimeConstraintNominalDecisionSystem(
					tempDecisionSystem);

			ProgressDialog.progressDialog
					.appendMessage("\r\n********00000000000000000***********\r\n");
			ProgressDialog.progressDialog
					.appendMessage("" + tempDecisionSystem);
			ProgressDialog.progressDialog
					.appendMessage("\r\n********11111111111111111***********\r\n");
			ProgressDialog.progressDialog.appendMessage(""
					+ CoserProject.currentProject.currentLilyTmNds);
			ProgressDialog.progressDialog
					.appendMessage("\r\n********22222222222222222***********\r\n");
			ProgressDialog.progressDialog.appendMessage(""
					+ CoserProject.currentProject.currentBcoTmNds);
			ProgressDialog.progressDialog
					.appendMessage("\r\n********33333333333333333***********\r\n");
			ProgressDialog.progressDialog.appendMessage(""
					+ CoserProject.currentProject.currentMoTmNds);
			ProgressDialog.progressDialog
					.appendMessage("\r\n********44444444444444444***********\r\n");
			ProgressDialog.progressDialog.appendMessage(""
					+ CoserProject.currentProject.currentTmConsNds);

			CoserMenuBar.coserMenuBar.randomTmReductionMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.acoTmConstraintReductionMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.bcoMinimalCostReductionMenu
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.sbcMinimalCostReductionMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.abcMinimalCostReductionMenu
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.twoObjectiveReductionMenuItem
					.setEnabled(true);

			// Let's have a look.
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
			return;
		}// Of try
			// ProgressDialog.progressDialog.setVisible(false);
	}// Of actionPerformed

}// Of class LoadTmDsDialog
