package coser.gui.dialog.bcdsgc;

import java.awt.*;
import java.awt.event.*;
import coser.algorithm.*;

import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Specify reduction related parameters, and obtain minimal cost
 * feature sets based on adaptive misclassification costs and test costs. The
 * algorithm is implemented in
 * coser.datamodel.decisionsystem.BothCostsNumericDecisionSystem.
 * <p>
 * Author: <b>Hong Zhao</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Done.<br>
 * Written time: April 9, 2013. <br>
 * Last modify time: April 9, 2013.
 */
public class FeatureSelectionBasedonAdaptiveBCDialog extends Dialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3860622028132561717L;

	public static FeatureSelectionBasedonAdaptiveBCDialog featureSelectionBasedonAdaptiveBCDialog = new FeatureSelectionBasedonAdaptiveBCDialog();

	/**
	 * The lower bound of confidence level.
	 */
	private DoubleField confidenceLevelLowerBoundField;

	/**
	 * The upper bound of confidence level.
	 */
	private DoubleField confidenceLevelUpperBoundField;

	/**
	 * The step length of confidence level.
	 */
	private DoubleField confidenceLevelStepLengthField;

	/**
	 * Misclassification cost with small value.
	 */

	private IntegerField misCostField;

	/**
	 * Ratio of two misclassification costs.
	 */
	private IntegerField ratioTwoMisCostsField;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private FeatureSelectionBasedonAdaptiveBCDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame,
				"Feature selection based on adaptive both costs", true);

		// confidenceLevelLowerBoundField = new DoubleField("0.1");
		// confidenceLevelUpperBoundField = new DoubleField("1.0");
		// confidenceLevelStepLengthField = new DoubleField("0.1");
		confidenceLevelLowerBoundField = new DoubleField("0.6");
		confidenceLevelUpperBoundField = new DoubleField("1");
		confidenceLevelStepLengthField = new DoubleField("0.02");
		misCostField = new IntegerField("50");
		ratioTwoMisCostsField = new IntegerField("10");

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(5, 2));
		centerPanel.add(new Label("Confidence level lower bound: "));
		centerPanel.add(confidenceLevelLowerBoundField);
		centerPanel.add(new Label("Confidence level upper bound: "));
		centerPanel.add(confidenceLevelUpperBoundField);
		centerPanel.add(new Label("Confidence level step length: "));
		centerPanel.add(confidenceLevelStepLengthField);
		centerPanel.add(new Label("Misclassification cost: "));
		centerPanel.add(misCostField);
		centerPanel.add(new Label(
				"Ratio of two mis costs: (1 for unified mis cost)"));
		centerPanel.add(ratioTwoMisCostsField);
		Button okButton = new Button(" Compute ");
		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog(
				"Both cost sensitive attribute reduction (exhaustive)",
				"coser/gui/dialog/bcdsne/MinimalCostReductionDialogHelp.txt"));
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
			// BothCostsNominalDecisionSystem currentSystem =
			// CoserProject.currentProject.currentBcNds;

			// String message = "";

			String message = "";
			double confidenceLevelLowerBound = confidenceLevelLowerBoundField
					.getValue();
			double confidenceLevelUpperBound = confidenceLevelUpperBoundField
					.getValue();
			double confidenceLevelStepLength = confidenceLevelStepLengthField
					.getValue();
			int misCost = misCostField.getValue();
			int ratioTwoMisCost = ratioTwoMisCostsField.getValue();
			message = Reduction.batchOptimalGranularComputingBasedonAdaptiveBC(
					confidenceLevelLowerBound, confidenceLevelUpperBound,
					confidenceLevelStepLength, misCost, ratioTwoMisCost);

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

}// Of class MinimalCostReductionDialog

