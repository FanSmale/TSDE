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

public class BcoMinimalTimeCostReductionFirstDialog extends Dialog implements
		ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8552943019611145346L;

	public static BcoMinimalTimeCostReductionFirstDialog bcoMinimalTimeCostReductionFirstDialog = new BcoMinimalTimeCostReductionFirstDialog();

	/**
	 * The dialog box frame.
	 */
	JFrame dialogBox;

	/**
	 * The label "Bee counts"
	 */
	Label labelBeeCounts;
	/**
	 * The label "Neighbor counts"
	 */
	Label labelNeighborCounts;

	/**
	 * The label "Generations"
	 */
	Label labelGenerations;

	/**
	 * The label "RemoveRate"
	 */
	Label labelRemoveRate;

	/**
	 * The label "ExperimentCounts"
	 */
	// Label labelexperimentCounts;

	/**
	 * The text field of the bee counts.
	 */
	IntegerField textFieldBeeCounts;

	/**
	 * The text field of the neighbor counts.
	 */
	IntegerField textFieldNeighborCounts;

	/**
	 * The text field of the generations.
	 */
	IntegerField textFieldGenerations;
	/**
	 * The text field of the removing rate.
	 */
	DoubleField textFieldRemoveRate;

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
	public BcoMinimalTimeCostReductionFirstDialog() {

		super(GUICommon.mainFrame, "Minimal time cost attribute reduction BCO",
				true);

		dialogBox = new JFrame(
				"The Bee Colony to minimal test cost reduction problem");
		labelBeeCounts = new Label("Bee counts");
		labelGenerations = new Label("Generations");
		labelNeighborCounts = new Label("Neighbor counts");
		labelRemoveRate = new Label("Removing rate");
		// labelexperimentCounts = new Label("Experiment counts");
		textFieldBeeCounts = new IntegerField("20");
		textFieldNeighborCounts = new IntegerField("10");
		textFieldGenerations = new IntegerField("10");
		textFieldRemoveRate = new DoubleField("0.2");
		// textFieldExperimentCounts = new IntegerField("100");

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
						"coser/gui/dialog/tmds/BcoMinimalTimeCostReductionFirstDialogHelp.txt"));
		// helpButton.setSize(20, 10);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(5, 2));

		centerPanel.add(labelBeeCounts);
		centerPanel.add(textFieldBeeCounts);
		// centerPanel.add(labelexperimentCounts);
		// centerPanel.add(textFieldExperimentCounts);
		centerPanel.add(labelGenerations);
		centerPanel.add(textFieldGenerations); // the cycling generations
		centerPanel.add(labelNeighborCounts);
		centerPanel.add(textFieldNeighborCounts); // the times for searching
													// neighbor solutions of
													// each bee.
		centerPanel.add(labelRemoveRate);
		centerPanel.add(textFieldRemoveRate); // the rate of removing worse ones
												// from solutions found by bees.

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
			int beeCounts = Integer.parseInt(textFieldBeeCounts.getText()
					.trim());
			int neighborCounts = Integer.parseInt(textFieldNeighborCounts
					.getText().trim());
			int generations = Integer.parseInt(textFieldGenerations.getText()
					.trim());
			double removeRate = Double.parseDouble(textFieldRemoveRate
					.getText().trim());
			// int experimentCounts =
			// Integer.parseInt(textFieldExperimentCounts.getText().trim());
			BcoTimeNominalDecisionSystem currentDecisionSystem = CoserProject.currentProject.currentBcoTmNds;
			boolean[] currentFeatureSubset = currentDecisionSystem
					.beeColonyTimeReduction(beeCounts, generations,
							neighborCounts, removeRate);
			// boolean [] currentFeatureSubset =
			// currentDecisionSystem.beeColonyTimeRedutionSecond(50, 3, 20,
			// 0.4);
			// Output basic information about the scheme for reduct.
			System.out
					.println("Creating the minimal time cost reducion algorithm based on BCO");
			System.out.println("The number of bees = " + beeCounts
					+ "  The times for searching neighbor solutions = "
					+ neighborCounts);
			System.out.println("Generations = " + generations
					+ "  The rate of removing worse solutions = " + removeRate);
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
		BcoMinimalTimeCostReductionFirstDialog.bcoMinimalTimeCostReductionFirstDialog
				.setVisible(true);
	}// main

}// class BcoMinimalTimeCostReductionFirstDialog