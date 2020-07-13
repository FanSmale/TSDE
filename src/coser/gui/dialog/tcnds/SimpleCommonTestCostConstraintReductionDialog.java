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
 * Summary: Specify reduction related parameters.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Half done.<br>
 * Written time: April 07, 2011. <br>
 * Last modify time: April 07, 2011.
 */
public class SimpleCommonTestCostConstraintReductionDialog extends Dialog
		implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2595617090447634681L;

	public static SimpleCommonTestCostConstraintReductionDialog simpleCommonTestCostConstraintReductionDialog = new SimpleCommonTestCostConstraintReductionDialog();

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
	 * The number of group lower bound.
	 */
	private IntegerField groupLowerBoundField;

	/**
	 * The number of group upper bound.
	 */
	private IntegerField groupUpperBoundField;

	/**
	 * The number of group upper bound.
	 */
	private IntegerField groupStepLengthField;

	/**
	 * Budget factor. Control the given test-cost less than the value to
	 * construct a reduct.
	 */
	private DoubleField budgetFactorField;

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
	private SimpleCommonTestCostConstraintReductionDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame,
				"Test cost constraint reduction (heuristic)", true);

		String[] weightingModes = { "Unweighting", "Weighting" };
		weightingComboBox = new JComboBox<String>(weightingModes);
		weightingComboBox.setSelectedIndex(1);

		lambdaLowerBoundField = new DoubleField("-2");
		lambdaUpperBoundField = new DoubleField("-0.25");
		lambdaStepLengthField = new DoubleField("0.25");
		groupLowerBoundField = new IntegerField("1");
		groupUpperBoundField = new IntegerField("1");
		groupStepLengthField = new IntegerField("1");
		budgetFactorField = new DoubleField("0.8");
		experimentTimesField = new IntegerField("100");
		showDetailCheckbox = new Checkbox("Show running details", false);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(10, 2));
		centerPanel.add(new Label("Weighting mode: "));
		centerPanel.add(weightingComboBox);
		centerPanel.add(new Label("Lambda upper bound: "));
		centerPanel.add(lambdaUpperBoundField);
		centerPanel.add(new Label("Lambda lower bound: "));
		centerPanel.add(lambdaLowerBoundField);
		centerPanel.add(new Label("Lambda step length: "));
		centerPanel.add(lambdaStepLengthField);
		centerPanel.add(new Label("The number of group upper bound: "));
		centerPanel.add(groupUpperBoundField);
		centerPanel.add(new Label("The number of group lower bound: "));
		centerPanel.add(groupLowerBoundField);
		centerPanel.add(new Label("Group step length: "));
		centerPanel.add(groupStepLengthField);
		centerPanel.add(new Label("Budget factor: "));
		centerPanel.add(budgetFactorField);
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
						"Test cost constraint reduction (heuristic)",
						"coser/gui/dialog/tcnds/SimpleCommonTestCostConstraintReductionDialogHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(okButton);
		okPanel.add(cancelButton);
		okPanel.add(helpButton);

		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, okPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(200, 200);
		setSize(410, 280);
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
			double lambdaUpperBound = Double.parseDouble(lambdaUpperBoundField
					.getText().trim());
			double lambdaLowerBound = Double.parseDouble(lambdaLowerBoundField
					.getText().trim());
			double lambdaStepLength = Double.parseDouble(lambdaStepLengthField
					.getText().trim());
			int groupUpperBound = Integer.parseInt(groupUpperBoundField
					.getText().trim());
			int groupLowerBound = Integer.parseInt(groupLowerBoundField
					.getText().trim());
			int groupStepLength = Integer.parseInt(groupStepLengthField
					.getText().trim());

			double budgetFactor = Double.parseDouble(budgetFactorField
					.getText().trim());

			int experiments = Integer.parseInt(experimentTimesField.getText()
					.trim());
			boolean showDetail = showDetailCheckbox.getState();

			String message = SimpleCommonTestCostReduction
					.batchSimpleCommonTestCostConstraintReduction(weighting,
							lambdaUpperBound, lambdaLowerBound,
							lambdaStepLength, groupUpperBound, groupLowerBound,
							groupStepLength, budgetFactor, experiments,
							showDetail);

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

}// Of class SimpleCommonTestCostConstraintReductionDialog

