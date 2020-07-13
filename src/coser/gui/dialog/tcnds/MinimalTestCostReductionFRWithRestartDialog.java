package coser.gui.dialog.tcnds;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import coser.algorithm.ReductionFRWithRestart;
import coser.gui.dialog.common.DialogCloser;
import coser.gui.dialog.common.HelpDialog;
import coser.gui.dialog.common.ProgressDialog;
import coser.gui.guicommon.GUICommon;
import coser.gui.others.DoubleField;
import coser.gui.others.IntegerField;

/**
 * Summary: The dialog involving minimal test cost reduction fast random
 * algorithm.
 * <p>
 * Author: <b>Jingkuan Li</b> kingljk@126.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Progress: Done.<br>
 * Written time: November 5, 2011. <br>
 * Last modify time: January 19, 2013.
 */
public class MinimalTestCostReductionFRWithRestartDialog extends Dialog
		implements ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -855294301961114612L;

	public static MinimalTestCostReductionFRWithRestartDialog minimalTestCostReductionFRWithRestartDialog = new MinimalTestCostReductionFRWithRestartDialog();

	/**
	 * The dialog box frame.
	 */
	public Panel centerPanel;

	/**
	 * The label "Alpha"
	 */
	Label labelAlpha;

	/**
	 * blank label
	 */
	Label blank;

	/**
	 * The label "Beta"
	 */
	Label labelBeta;

	/**
	 * The label "Delta"
	 */
	Label labelDelta;

	/**
	 * The label "Seed"
	 */
	Label labelSeed;

	/**
	 * The label "RestartTime"
	 */
	Label labelRestartTime;

	/**
	 * The label "ExperimentCounts"
	 */
	Label labelexperimentCounts;

	/**
	 * The textfield of the exponent alpha.
	 */
	IntegerField textFieldAlpha;

	/**
	 * The textfield of the exponent beta.
	 */
	IntegerField textFieldBeta;

	/**
	 * The textfield of the exponent delta.
	 */
	DoubleField textFieldDelta;

	/**
	 * The textfield of the exponent seed.
	 */
	IntegerField textFieldSeed;

	/**
	 * The textfield of the exponent RestartTime.
	 */
	IntegerField textFieldRestartTime;

	/**
	 * The test field of the experiment counts.
	 */
	JTextField textFieldExperimentCounts;

	/**
	 * The OK button
	 */
	Button buttonOK;

	/**
	 * The Cancel button
	 */
	Button buttonCancel;

	/**
	 **************************** 
	 * The constructor
	 **************************** 
	 */
	public MinimalTestCostReductionFRWithRestartDialog() {
		super(GUICommon.mainFrame,
				"Minimal test cost attribute reduction FR with re-start", true);

		centerPanel = new Panel();
		labelAlpha = new Label("Number of per batch");
		labelBeta = new Label("Number of population");
		labelDelta = new Label("Probability of attributes selected");
		labelSeed = new Label("Seed");
		labelRestartTime = new Label("Restart time");
		labelexperimentCounts = new Label("Experiment counts");

		textFieldAlpha = new IntegerField("2");
		textFieldBeta = new IntegerField("10");
		textFieldDelta = new DoubleField("0.8");
		textFieldSeed = new IntegerField("10");
		textFieldRestartTime = new IntegerField("15");
		textFieldExperimentCounts = new JTextField("100");
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
						"coser/gui/dialog/tcnds/MinimalTestCostReductionFRDialogHelp.txt"));
		helpButton.setSize(20, 10);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(6, 2));

		// the times of experiment
		centerPanel.add(labelexperimentCounts);
		centerPanel.add(textFieldExperimentCounts);

		// the parameter of randomizing
		centerPanel.add(labelAlpha);
		centerPanel.add(textFieldAlpha);

		// the selecting probability of attributes
		centerPanel.add(labelDelta);
		centerPanel.add(textFieldDelta);

		// the population of algorithm
		centerPanel.add(labelBeta);
		centerPanel.add(textFieldBeta);

		// the seed of random
		centerPanel.add(labelSeed);
		centerPanel.add(textFieldSeed);

		// the restart time of random
		centerPanel.add(labelRestartTime);
		centerPanel.add(textFieldRestartTime);

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

	}// Of constructor

	/**
	 ************************** 
	 * The performing action when events appear.
	 ************************** 
	 */
	public void actionPerformed(java.awt.event.ActionEvent ae) {
		setVisible(false);
		ProgressDialog.progressDialog
				.setMessageAndShow("Please wait for a while."
						+ "\r\nThe execution time depends on the dataset size and the number of experiments."
						+ "\r\nThe progress is shown in the console.\r\n");
		try {

			int alpha = Integer.parseInt(textFieldAlpha.getText().trim());
			int beta = Integer.parseInt(textFieldBeta.getText().trim());
			double delta = Double.parseDouble(textFieldDelta.getText().trim());
			int seed = Integer.parseInt(textFieldSeed.getText().trim());
			int restartTime = Integer.parseInt(textFieldRestartTime.getText()
					.trim());
			int experimentCounts = Integer.parseInt(textFieldExperimentCounts
					.getText().trim());

			ReductionFRWithRestart.batchMinimalTestCostReductionFRWithRestart(
					alpha, beta, delta, seed, restartTime, experimentCounts);
			System.out
					.println("  the number of selected attributes per batch = "
							+ alpha);
			System.out.println(" the selecting probability of attributes = "
					+ delta);
		} catch (Exception e2) {
			System.out.println("Please input correct information!");
			e2.printStackTrace();
		}// try-catch

	}// Of actionPerformed

	/**
	 ************************** 
	 * The main method
	 ************************** 
	 */
	public static void main(String[] args) {
		MinimalTestCostReductionFRWithRestartDialog.minimalTestCostReductionFRWithRestartDialog
				.setVisible(true);
	}// Of main

}// class MinimalTestCostReductionFRWithRestartDialog

