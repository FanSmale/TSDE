package coser.gui.dialog.tcnds;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import coser.algorithm.*;

import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Specify reduction related parameters, and obtain sub-minimal test
 * cost attribute reducts. The algorithm is implemented in
 * coser.datamodel.decisionsystem.TestCostNominalDecisionSystem.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Done.<br>
 * Written time: March 12, 2011. <br>
 * Last modify time: April 27, 2011.
 */
public class MinimalTestCostReductionDialog extends Dialog implements
		ActionListener {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8078644877529042296L;

	public static MinimalTestCostReductionDialog minimalTestCostReductionDialog = new MinimalTestCostReductionDialog();

	/**
	 * Algorithms: weighted, not weighted.
	 */
	private JComboBox<String> weightingComboBox;

	/**
	 * Labmda lower bound.
	 */
	private DoubleField lambdaLowerBoundField;

	/**
	 * Labmda upper bound.
	 */
	private DoubleField lambdaUpperBoundField;

	/**
	 * Labmda step length.
	 */
	private DoubleField lambdaStepLengthField;

	/**
	 * Number of experiments with different test-cost settings.
	 */
	private IntegerField experimentTimesField;

	/**
	 * Show detailed results?
	 */
	private Checkbox showDetailCheckbox;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private MinimalTestCostReductionDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Minimal test cost attribute reduction",
				true);

		String[] weightingModes = { "Unweighting", "Weighting" };
		weightingComboBox = new JComboBox<String>(weightingModes);
		weightingComboBox.setSelectedIndex(1);

		lambdaLowerBoundField = new DoubleField("-2");
		lambdaUpperBoundField = new DoubleField("-0.25");
		lambdaStepLengthField = new DoubleField("0.25");
		experimentTimesField = new IntegerField("100");
		showDetailCheckbox = new Checkbox("Show running details", false);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(6, 2));
		centerPanel.add(new Label("Weighting mode: "));
		centerPanel.add(weightingComboBox);
		centerPanel.add(new Label("Lambda upper bound: (valid for weighting)"));
		centerPanel.add(lambdaUpperBoundField);
		centerPanel.add(new Label("Lambda lower bound : "));
		centerPanel.add(lambdaLowerBoundField);
		centerPanel.add(new Label("Lambda step length: "));
		centerPanel.add(lambdaStepLengthField);
		centerPanel.add(new Label("Number of experiments: "));
		centerPanel.add(experimentTimesField);
		centerPanel.add(showDetailCheckbox);

		Button okButton = new Button(" Compute ");
		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton
				.addActionListener(new HelpDialog(
						"Minimal test cost attribute reduction",
						"coser/gui/dialog/tcnds/MinimalTestCostReductionDialogHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(okButton);
		okPanel.add(cancelButton);
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
	 * Read the arff file.
	 *************************** 
	 */
	public void actionPerformed(ActionEvent ae) {
		setVisible(false);
		ProgressDialog.progressDialog
				.setMessageAndShow("Please wait for a while."
						+ "\r\nThe execution time depends on the dataset size and the number of experiments."
						+ "\r\nThe progress is shown in the console.\r\n");

		try {
			// Use another name to make statements shorter.
			// TestCostNominalDecisionSystem currentSystem =
			// CoserProject.currentProject.currentTcNds;

			// String message = "";

			boolean weighting = (weightingComboBox.getSelectedIndex() == 1);
			int experiments = Integer.parseInt(experimentTimesField.getText()
					.trim());
			boolean showDetail = showDetailCheckbox.getState();

			String message = "";
			if (!weighting) {
				message = Reduction.batchMinimalReduct(experiments, showDetail);
			} else {
				double lambdaUpperBound = Double
						.parseDouble(lambdaUpperBoundField.getText().trim());
				double lambdaLowerBound = Double
						.parseDouble(lambdaLowerBoundField.getText().trim());
				double lambdaStepLength = Double
						.parseDouble(lambdaStepLengthField.getText().trim());
				message = Reduction.batchMinimalTestCostReduction(
						lambdaUpperBound, lambdaLowerBound, lambdaStepLength,
						experiments, showDetail);
			}// Of if
			message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com\r\n"
					+ message;
			ProgressDialog.progressDialog.setMessageAndShow(message);
			// System.out.println(tempDecisionSystem);
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
			return;
		}// Of try
			// ProgressDialog.progressDialog.setVisible(false);
	}// Of actionPerformed

}// Of class MinimalTestCostReductionDialog

