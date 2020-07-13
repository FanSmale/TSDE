package coser.gui.dialog.tcdsfn;

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
 * Summary: Load a Numeric decision system and specify the neighborhood
 * information.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Almost done.<br>
 * Written time: August 13, 2011. <br>
 * Last modify time: April 21, 2012.
 */
public class LoadTcDsFnDialog extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2713832747156386466L;

	public static LoadTcDsFnDialog loadTcDsFnDialog = new LoadTcDsFnDialog();

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
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private LoadTcDsFnDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Test-cost settings", true);

		arffFilenameField = new FilenameField(30);
		arffFilenameField.setText("data/irisNormalized.arff");
		Button browseButton = new Button(" Browse ");
		browseButton.addActionListener(arffFilenameField);

		Panel sourceFilePanel = new Panel();
		sourceFilePanel.add(new Label("The .arff file:"));
		sourceFilePanel.add(arffFilenameField);
		sourceFilePanel.add(browseButton);

		lowerBoundField = new IntegerField("1");
		upperBoundField = new IntegerField("100");
		String[] distributionModes = { "Uniform", "Normal", "Bounded Pareto" };
		distributionComboBox = new JComboBox<String>(distributionModes);
		distributionComboBox.setSelectedIndex(0);
		String[] relationshipModes = { "Independent", "Simple common",
				"Complex common" };
		testCostRelationshipComboBox = new JComboBox<String>(relationshipModes);
		testCostRelationshipComboBox.setSelectedIndex(0);
		String[] randomModes = { "Psuedo", "Real" };
		randomModeComboBox = new JComboBox<String>(randomModes);
		randomModeComboBox.setSelectedIndex(0);
		alphaField = new IntegerField("2");

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(6, 2));
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

		Button okButton = new Button(" OK ");
		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog("Test-cost distribution",
				"coser/gui/dialog/tcdsfn/LoadTcDsFnDialoghelp.txt"));
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
		setSize(420, 250);
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
						.readTestCostDecisionSystemNeighborhood(tempFilename);
			} catch (Exception ee) {
				ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
				ProgressDialog.progressDialog.setVisible(false);
				return;
			}// Of try

			// Just another name to make statements shorter.
			TestCostDecisionSystemFixedNeighborhood tempDecisionSystem = CoserProject.currentProject.currentTcDsFn;
			tempDecisionSystem.setArffFilename(tempFilename);
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

			// Finally refresh test-cost
			tempDecisionSystem.refreshTestCost();
			tempDecisionSystem.setNomalizedIndividualTestCostArray();
			// For decision systems
			CoserMenuBar.coserMenuBar.fixedNeighborhoodReductionMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.testCostFixedNeighborhoodReductionMenuItem
					.setEnabled(true);
			CoserMenuBar.coserMenuBar.fixedNeighborhoodAllReductsMenuItem
					.setEnabled(true);

		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
			return;
		}// Of try
		ProgressDialog.progressDialog.setVisible(false);
	}// Of actionPerformed

}// Of class LoadTcDsFnDialog