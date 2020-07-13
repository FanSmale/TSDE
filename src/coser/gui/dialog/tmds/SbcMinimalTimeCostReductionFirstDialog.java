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

public class SbcMinimalTimeCostReductionFirstDialog extends Dialog implements
		ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8552943019611145346L;

	public static SbcMinimalTimeCostReductionFirstDialog sbcMinimalTimeCostReductionFirstDialog = new SbcMinimalTimeCostReductionFirstDialog();

	/**
	 * The dialog box frame.
	 */
	JFrame dialogBox;

	/**
	 * The label "the total number of bees"
	 */
	Label labelTotalNumberBees;
	/**
	 * The label "the number of inactive"
	 */
	Label labelNumberInactive;

	/**
	 * The label "the number of active"
	 */
	Label labelNumberActive;

	/**
	 * The label "the number of scout"
	 */
	Label labelNumberScout;
	/**
	 * The label "the maximal number of visits"
	 */
	Label labelMaxNumberVisits;
	/**
	 * The label "the maximal number of cycles"
	 */
	Label labelMaxNumberCycles;
	/**
	 * The label "the probability of persuasion"
	 */
	Label labelProPersuasion;

	/**
	 * The label "ExperimentCounts"
	 */
	// Label labelexperimentCounts;

	/**
	 * The text field of the total number of bees".
	 */
	IntegerField textFieldTotalNumberBees;

	/**
	 * The text field of the number of inactive.
	 */
	IntegerField textFieldNumberInactive;

	/**
	 * The text field of the number of active.
	 */
	IntegerField textFieldNumberActive;
	/**
	 * The text field of the number of scout.
	 */
	IntegerField textFieldNumberScout;
	/**
	 * The text field of the maximal number of visits.
	 */
	IntegerField textFieldMaxNumberVisits;
	/**
	 * The text field of the maximal number of cycles.
	 */
	IntegerField textFieldMaxNumberCycles;
	/**
	 * The text field of the probability of persuasion.
	 */
	DoubleField textFieldProPersuasion;

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
	public SbcMinimalTimeCostReductionFirstDialog() {

		super(GUICommon.mainFrame, "Minimal time cost attribute reduction BCO",
				true);

		dialogBox = new JFrame(
				"The Bee Colony to minimal test cost reduction problem");
		labelTotalNumberBees = new Label("The total number of bees");
		labelNumberInactive = new Label("The number of onlookers");
		labelNumberActive = new Label("The number of employed bees");
		labelNumberScout = new Label("The number of scouts");
		labelMaxNumberVisits = new Label("The maximal number of visits");
		labelMaxNumberCycles = new Label("The maximal number of cycles");
		labelProPersuasion = new Label("The probability of persuasion");

		textFieldTotalNumberBees = new IntegerField("100");
		textFieldNumberInactive = new IntegerField("20");
		textFieldNumberActive = new IntegerField("50");
		textFieldNumberScout = new IntegerField("30");
		textFieldMaxNumberVisits = new IntegerField("100");
		textFieldMaxNumberCycles = new IntegerField("3460");
		textFieldProPersuasion = new DoubleField("0.9");

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
						"coser/gui/dialog/tmds/BcoMinimalCostReductionFirstDialogHelp.txt"));
		// helpButton.setSize(20, 10);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(7, 2));

		centerPanel.add(labelTotalNumberBees);
		centerPanel.add(textFieldTotalNumberBees);
		centerPanel.add(labelNumberInactive);
		centerPanel.add(textFieldNumberInactive);
		centerPanel.add(labelNumberActive);
		centerPanel.add(textFieldNumberActive);
		centerPanel.add(labelNumberScout);
		centerPanel.add(textFieldNumberScout);
		centerPanel.add(labelMaxNumberVisits);
		centerPanel.add(textFieldMaxNumberVisits);
		centerPanel.add(labelMaxNumberCycles);
		centerPanel.add(textFieldMaxNumberCycles);
		centerPanel.add(labelProPersuasion);
		centerPanel.add(textFieldProPersuasion);

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
			int totalNumberBee = Integer.parseInt(textFieldTotalNumberBees
					.getText().trim());
			int numberOfInactive = Integer.parseInt(textFieldNumberInactive
					.getText().trim());
			int numberActive = Integer.parseInt(textFieldNumberActive.getText()
					.trim());
			int numberScout = Integer.parseInt(textFieldNumberScout.getText()
					.trim());
			int maxNumberVisits = Integer.parseInt(textFieldMaxNumberVisits
					.getText().trim());
			int maxNumberCycles = Integer.parseInt(textFieldMaxNumberCycles
					.getText().trim());
			double proPersuasion = Double.parseDouble(textFieldMaxNumberCycles
					.getText().trim());
			// int experimentCounts =
			// Integer.parseInt(textFieldExperimentCounts.getText().trim());
			SbcTimeNominalDecisionSystem currentDecisionSystem = CoserProject.currentProject.currentAbcTmNds;
			boolean[] currentFeatureSubset = currentDecisionSystem
					.beeColonyTimeReductionThird(totalNumberBee,
							numberOfInactive, numberActive, numberScout,
							maxNumberVisits, maxNumberCycles, proPersuasion);
			// Output basic information about the scheme for reduct.
			System.out
					.println("Creating the minimal time cost reducion algorithm based on BCO");
			System.out.println("The total number of bees = " + totalNumberBee
					+ "  The number of onlookers = " + numberOfInactive);
			System.out.println("The number of employed bees = " + numberActive
					+ "  The number of scouts = " + numberScout);
			System.out.println("The maximal number of visits = "
					+ maxNumberVisits + "  The maximal number of cycles = "
					+ maxNumberCycles);
			System.out.println("The probability of persuasion = "
					+ proPersuasion);

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
		SbcMinimalTimeCostReductionFirstDialog.sbcMinimalTimeCostReductionFirstDialog
				.setVisible(true);
	}// main

}// class SbcMinimalTimeCostReductionFirstDialog