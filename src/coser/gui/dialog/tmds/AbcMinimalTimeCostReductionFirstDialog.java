package coser.gui.dialog.tmds;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import coser.common.SimpleTool;
import coser.datamodel.decisionsystem.*;
import coser.gui.dialog.common.DialogCloser;
import coser.gui.dialog.common.ErrorDialog;
import coser.gui.dialog.common.HelpDialog;
import coser.gui.dialog.common.ProgressDialog;
import coser.gui.guicommon.GUICommon;
import coser.gui.others.DoubleField;
import coser.gui.others.IntegerField;
import coser.project.CoserProject;

public class AbcMinimalTimeCostReductionFirstDialog extends Dialog implements
		ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8552943019611145346L;

	public static AbcMinimalTimeCostReductionFirstDialog abcMinimalTimeCostReductionFirstDialog = new AbcMinimalTimeCostReductionFirstDialog();

	/**
	 * The dialog box frame.
	 */
	JFrame dialogBox;

	/**
	 * The label "the total number of bees"
	 */
	Label labelNP;
	/**
	 * The label "the number of food source"
	 */
	Label labelFoodNumber;

	/**
	 * The label "the limited times for neighbour solutions"
	 */
	Label labelLimit;

	/**
	 * The label "maximal running cycles"
	 */
	Label labelMaxCycles;
	/**
	 * The label "the selection probability of personal best solution"
	 */
	Label labelCPBest;
	/**
	 * The label "the selection probability of global best solution"
	 */
	Label labelCGBest;

	/**
	 * The label "ExperimentCounts"
	 */
	// Label labelexperimentCounts;

	/**
	 * The text field of the total number of bees".
	 */
	IntegerField textFieldNP;

	/**
	 * The text field of the number of inactive.
	 */
	IntegerField textFieldFoodNumber;

	/**
	 * The text field of the number of active.
	 */
	IntegerField textFieldLimit;
	/**
	 * The text field of the number of scout.
	 */
	IntegerField textFieldMaxCycles;
	/**
	 * The text field of the selection probability of personal best solution.
	 */
	DoubleField textFieldCPBest;
	/**
	 * The text field of the selection probability of global best solution.
	 */
	DoubleField textFieldCGBest;

	/**
	 * The test field of the experiment counts.
	 */
	// IntegerField textFieldExperimentCounts;

	/**
	 * The OK button
	 */
	Button buttonOK;

	/**
	 * The Cancel button
	 */
	Button buttonCancel;

	/**
	 * Construct Method
	 */
	public AbcMinimalTimeCostReductionFirstDialog() {

		super(GUICommon.mainFrame, "Minimal time cost attribute reduction ABC",
				true);

		dialogBox = new JFrame(
				"The artificial bee colony algorithm to minimal time cost reduction problem");
		labelNP = new Label("The total number of bees");
		labelFoodNumber = new Label("The number of employed bees");
		labelLimit = new Label("The limited neighbors");
		labelMaxCycles = new Label("The maximal cycles");
		labelCPBest = new Label("The probability for personal best");
		labelCGBest = new Label("The probability for global best");

		textFieldNP = new IntegerField("40");
		textFieldFoodNumber = new IntegerField("20");
		textFieldLimit = new IntegerField("110");
		textFieldMaxCycles = new IntegerField("10");
		textFieldCPBest = new DoubleField("0.3");
		textFieldCGBest = new DoubleField("0.9");

		buttonOK = new Button("Compute");
		buttonOK.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		buttonCancel = new Button("Cancel");
		buttonCancel.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton
				.addActionListener(new HelpDialog(
						"Minimal test cost attribute reduction",
						"coser/gui/dialog/tmds/AbcMinimalTimeCostReductionFirstDialogHelp.txt"));
		// helpButton.setSize(20, 10);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(6, 2));

		centerPanel.add(labelNP);
		centerPanel.add(textFieldNP);
		centerPanel.add(labelFoodNumber);
		centerPanel.add(textFieldFoodNumber);
		centerPanel.add(labelLimit);
		centerPanel.add(textFieldLimit);
		centerPanel.add(labelMaxCycles);
		centerPanel.add(textFieldMaxCycles);
		centerPanel.add(labelCPBest);
		centerPanel.add(textFieldCPBest);
		centerPanel.add(labelCGBest);
		centerPanel.add(textFieldCGBest);

		Panel okPanel = new Panel();
		okPanel.add(buttonOK);
		okPanel.add(buttonCancel);
		okPanel.add(helpButton);

		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, okPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(200, 200);
		setSize(530, 260);
		addWindowListener(dialogCloser);
		setVisible(false);

	}// Construct Method

	/**
	 * The performing action when events appear.
	 */
	public void actionPerformed(java.awt.event.ActionEvent ae) {
		// setVisible(false);
		ProgressDialog.progressDialog
				.setMessageAndShow("Please wait for a while."
						+ "\r\nThe execution time depends on the dataset size and the number of experiments."
						+ "\r\nThe progress is shown in the console.\r\n");

		try {
			// Use another name to make statements shorter.
			// TestCostNominalDecisionSystem currentSystem =
			// CoserProject.currentProject.currentTcNds;

			String message = "";
			int totalNumberBee = Integer.parseInt(textFieldNP.getText().trim());
			int numberOfFoods = Integer.parseInt(textFieldFoodNumber.getText()
					.trim());
			int limitedNeighbors = Integer.parseInt(textFieldLimit.getText()
					.trim());
			int maximalCycles = Integer.parseInt(textFieldMaxCycles.getText()
					.trim());
			double cPBest = Double
					.parseDouble(textFieldCPBest.getText().trim());
			double cGBest = Double
					.parseDouble(textFieldCGBest.getText().trim());
			// int experimentCounts =
			// Integer.parseInt(textFieldExperimentCounts.getText().trim());
			AbcFormalTimeNominalDecisionSystem currentDecisionSystem = CoserProject.currentProject.currentAbcFmTmNds;
			boolean[] currentFeatureSubset = currentDecisionSystem
					.AbcTestTimeReduction(totalNumberBee, numberOfFoods,
							limitedNeighbors, maximalCycles, cPBest, cGBest);
			// Output basic information about the scheme for reduct.
			// System.out
			// .println("Creating the minimal time cost reducion algorithm based on ABC");
			System.out.println("The total number of bees = " + totalNumberBee
					+ "  The number of foods = " + numberOfFoods);
			System.out.println("The limited neighbors = " + limitedNeighbors
					+ "  The maximal cycles = " + maximalCycles);
			System.out.println("The probability for the personal best = "
					+ cPBest + "  The probability for the global best = "
					+ cGBest);

			// For display
			message = SimpleTool
					.booleanArrayToAttributeSetString(currentFeatureSubset);
			// message = SimpleTool.booleanMatrixToString(currentFeatureSubset);
			message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com\r\n"
					+ message;
			ProgressDialog.progressDialog.setMessageAndShow(message);
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			// ProgressDialog.progressDialog.setVisible(false);
			ee.printStackTrace();
			return;
		}// Of try
	}// actionPerformed

	public static void main(String[] args) {
		AbcMinimalTimeCostReductionFirstDialog.abcMinimalTimeCostReductionFirstDialog
				.setVisible(true);
	}// main

}// class AbcMinimalTimeCostReductionFirstDialog