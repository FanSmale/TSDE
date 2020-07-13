package coser.gui.dialog.tcdsne;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import coser.algorithm.*;
import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Specify reduction related parameters, and obtain sub-minimal test
 * cost attribute reducts. The algorithm is implemented in
 * coser.datamodel.decisionsystem.TestCostSensitiveDecisionSystem.
 * <p>
 * Author: <b>Hong Zhao</b> minfanphd@163.com<br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Done.<br>
 * Written time: September 12, 2012. <br>
 * Last modify time: September 22, 2012.
 */
public class NormalErrorLambdaReductionDialog extends Dialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8203259221778161370L;

	public static NormalErrorLambdaReductionDialog normalErrorLambdaReductionDialog = new NormalErrorLambdaReductionDialog();

	/**
	 * Labmda lower bound.
	 */
	private DoubleField LambdaLowerBoundField;

	/**
	 * Labmda upper bound.
	 */
	private DoubleField LambdaUpperBoundField;

	/**
	 * Labmda step length.
	 */
	private DoubleField LambdaStepLengthField;

	/**
	 * Number of experiments with different test-cost settings.
	 */
	private IntegerField experimentTimesField;

	/**
	 * Show detailed results?
	 */
	private Checkbox showDetailCheckbox;

	/**
	 * /** Reduction start time, only useful in subclasses.
	 */
	protected long startTime;

	/**
	 * /** Reduction start time, only useful in subclasses.
	 */
	protected long endTime;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private NormalErrorLambdaReductionDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame,
				"Minimal test cost attribute reduction for TC-DS-NE", true);

		LambdaLowerBoundField = new DoubleField("-0.25");
		LambdaUpperBoundField = new DoubleField("-2");
		LambdaStepLengthField = new DoubleField("-0.25");
		experimentTimesField = new IntegerField("100");
		showDetailCheckbox = new Checkbox("Show running details", false);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(5, 2));
		centerPanel.add(new Label("Lambda upper bound: (valid for weighting)"));
		centerPanel.add(LambdaUpperBoundField);
		centerPanel.add(new Label("Lambda lower bound : "));
		centerPanel.add(LambdaLowerBoundField);
		centerPanel.add(new Label("Lambda step length: "));
		centerPanel.add(LambdaStepLengthField);
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
						"Minimal test cost attribute reduction for TCS-DS-NODE",
						"coser/gui/dialog/tcdsne/NormalErrorLambdaReductionDialogHelp.txt"));
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
			// TestCostDecisionSystemErrorRange currentSystem =
			// CoserProject.currentProject.currentTcNdsEr;

			// String message = "";

			// boolean weighting = (weightingComboBox.getSelectedIndex() == 1);
			int experiments = Integer.parseInt(experimentTimesField.getText()
					.trim());
			boolean showDetail = showDetailCheckbox.getState();

			String message = "";
			// if (!weighting) {
			// message =
			// Reduction.batchMinimalTestCostErrorRangeReduction(experiments,
			// showDetail);

			// } else {
			double LambdaUpperBound = Double.parseDouble(LambdaUpperBoundField
					.getText().trim());
			double LambdaLowerBound = Double.parseDouble(LambdaLowerBoundField
					.getText().trim());
			double LambdaStepLength = Double.parseDouble(LambdaStepLengthField
					.getText().trim());
			startTime = new Date().getTime();
			message = Reduction.batchMinimalTestCostLambdaNormalErrorReduction(
					LambdaUpperBound, LambdaLowerBound, LambdaStepLength,
					experiments, showDetail);
			// }//Of if
			endTime = new Date().getTime();
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

}// Of class NormalErrorLambdaReductionDialog

