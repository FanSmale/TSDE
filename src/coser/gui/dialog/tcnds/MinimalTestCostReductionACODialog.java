package coser.gui.dialog.tcnds;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import coser.datamodel.decisionsystem.TestCostNominalDecisionSystemACO;
import coser.gui.dialog.common.DialogCloser;
import coser.gui.dialog.common.ErrorDialog;
import coser.gui.dialog.common.HelpDialog;
import coser.gui.dialog.common.ProgressDialog;
import coser.gui.guicommon.GUICommon;
import coser.gui.others.DoubleField;
import coser.gui.others.IntegerField;
import coser.project.CoserProject;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Specify reduction related parameters, and obtain minimal test cost
 * attribute reducts. The algorithm is implemented in
 * coser.datamodel.decisionsystem.TestCostNominalDecisionSystemACO.
 * <p>
 * Author: <b> Zilong Xu </b> xzl-wy163@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Done.<br>
 * Written time: March 12, 2011. <br>
 * Last modify time: April 27, 2011.
 */
public class MinimalTestCostReductionACODialog extends Dialog implements
		ActionListener {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8552943019611146346L;

	public static MinimalTestCostReductionACODialog minimalTestCostReductionACODialog = new MinimalTestCostReductionACODialog();

	/**
	 * The dialog box frame.
	 */
	JFrame dialogBox;

	/**
	 * The label "Ant counts"
	 */
	Label labelAntCounts;

	/**
	 * The label "Alpha"
	 */
	Label labelAlpha;

	/**
	 * The label "Beta"
	 */
	Label labelBeta;

	/**
	 * The textfield of the ant counts.
	 */
	IntegerField textFieldAntCounts;

	/**
	 * The textfield of the exponent alpha.
	 */
	DoubleField textFieldAlpha;

	/**
	 * The textfield of the exponent beta.
	 */
	DoubleField textFieldBeta;

	/**
	 * The test field of the experiment counts.
	 */
	IntegerField textFieldExperimentCounts;

	/**
	 * The OK button
	 */
	Button buttonOK;

	/**
	 * The Cancel button
	 */
	Button buttonCancel;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	public MinimalTestCostReductionACODialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Minimal test cost attribute reduction ACO",
				true);

		dialogBox = new JFrame(
				"The Ant Colony to minimal test cost reduct problem");
		labelAntCounts = new Label("Ant counts");
		labelAlpha = new Label("Alpha");
		labelBeta = new Label("Beta");
		// labelexperimentCounts = new Label("Experiment counts");
		textFieldAntCounts = new IntegerField("100");
		textFieldAlpha = new DoubleField("2");
		textFieldBeta = new DoubleField("2");
		textFieldExperimentCounts = new IntegerField("100");
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
						"coser/gui/dialog/tcnds/MinimalTestCostReductionACODialogHelp.txt"));
		helpButton.setSize(20, 10);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(3, 2));

		centerPanel.add(labelAntCounts);
		centerPanel.add(textFieldAntCounts);
		// centerPanel.add(labelexperimentCounts);
		// centerPanel.add(textFieldExperimentCounts);
		centerPanel.add(labelAlpha);
		centerPanel.add(textFieldAlpha);
		centerPanel.add(labelBeta);
		centerPanel.add(textFieldBeta);

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
	 *************************** 
	 * The performing action when events appear.
	 *************************** 
	 */
	public void actionPerformed(java.awt.event.ActionEvent ae) {
		setVisible(false);
		ProgressDialog.progressDialog
				.setMessageAndShow("Please wait for a while."
						+ "\r\nThe execution time depends on the dataset size and the number of experiments."
						+ "\r\nThe progress is shown in the console.\r\n");

		try {
			// Use another name to make statements shorter.
			// TestCostNominalDecisionSystem currentSystem =
			// CoserProject.currentProject.currentTcNds;

			String message = "";
			int antCounts = Integer.parseInt(textFieldAntCounts.getText()
					.trim());
			double alpha = Double.parseDouble(textFieldAlpha.getText().trim());
			double beta = Double.parseDouble(textFieldBeta.getText().trim());
			// int experimentCounts =
			// Integer.parseInt(textFieldExperimentCounts.getText().trim());
			TestCostNominalDecisionSystemACO currentDecisionSystem = new TestCostNominalDecisionSystemACO(
					CoserProject.currentProject.currentTcNds, antCounts, alpha,
					beta);
			currentDecisionSystem.minimalTestCostReductACO();
			System.out.println("The number of ants = " + antCounts
					+ "  The exopnent of the pheromone = " + alpha
					+ "  The exponent of the test cost = " + beta);

			message = currentDecisionSystem.getResult();
			message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com\r\n"
					+ message;
			ProgressDialog.progressDialog.setMessageAndShow(message);
			// System.out.println(tempDecisionSystem);
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
			ee.printStackTrace();
			return;
		}// Of try
	}// actionPerformed

	/**
	 *************************** 
	 * The main method used for the unit testing.
	 *************************** 
	 */
	public static void main(String[] args) {
		MinimalTestCostReductionACODialog.minimalTestCostReductionACODialog
				.setVisible(true);
	}// main

}// class MinimalTestCostReductionACODialog