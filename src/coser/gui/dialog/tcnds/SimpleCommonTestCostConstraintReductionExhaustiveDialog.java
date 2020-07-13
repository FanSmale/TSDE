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
 * Author: <b>Guiying Pan</b> kentwingpan@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Process: Half done.<br>
 * Written time: August 18, 2011. <br>
 * Last modify time: August 18, 2011.
 */
public class SimpleCommonTestCostConstraintReductionExhaustiveDialog extends
		Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3955577330310604734L;

	public static SimpleCommonTestCostConstraintReductionExhaustiveDialog simpleCommonTestCostConstraintReductionExhaustiveDialog = new SimpleCommonTestCostConstraintReductionExhaustiveDialog();

	/**
	 * Algorithms: SESRA, SESRAstar, AROP_S.
	 */
	private JComboBox<String> algorithmComboBox;

	/**
	 * Consistency measure.
	 */
	private JComboBox<String> consistencyMetricComboBox;

	/**
	 * Budget factor. Control the given test cost less than the value to
	 * construct a reduct.
	 */
	private DoubleField budgetFactorField;

	/**
	 * Number of experiments. For statistical purpose.
	 */
	private IntegerField numberOfExperimentsField;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private SimpleCommonTestCostConstraintReductionExhaustiveDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame,
				"Simple common test cost constraint reduction (exhaustive)",
				true);

		String[] algorithms = { "SCTCSESRA", "SCTCSESRAstar", "SCTCBASS", "ALL" };
		algorithmComboBox = new JComboBox<String>(algorithms);
		algorithmComboBox.setSelectedIndex(0);

		String[] measures = { "POS", "ENTROY", "MAJORITY" };
		consistencyMetricComboBox = new JComboBox<String>(measures);
		consistencyMetricComboBox.setSelectedIndex(1);

		budgetFactorField = new DoubleField("0.8");

		numberOfExperimentsField = new IntegerField("1");

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(6, 2));
		centerPanel.add(new Label("Algorithm: "));
		centerPanel.add(algorithmComboBox);
		centerPanel.add(new Label("Consistency measure: "));
		centerPanel.add(consistencyMetricComboBox);
		centerPanel.add(new Label("Budget factor: "));
		centerPanel.add(budgetFactorField);
		centerPanel.add(new Label("Number of experiments: "));
		centerPanel.add(numberOfExperimentsField);

		Button okButton = new Button(" Compute ");
		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton
				.addActionListener(new HelpDialog(
						"Simple common test cost constraint reduction (exhaustive)",
						"coser/gui/dialog/tcnds/SimpleCommonTestCostConstraintReductionExhaustiveDialogHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(okButton);
		okPanel.add(cancelButton);
		okPanel.add(helpButton);

		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, okPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(200, 200);
		setSize(530, 280);
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

			String message = "";

			int algorithm = algorithmComboBox.getSelectedIndex();
			int measure = consistencyMetricComboBox.getSelectedIndex();
			double budgetFactor = Double.parseDouble(budgetFactorField
					.getText().trim());
			int numberOfExperiments = Integer.parseInt(numberOfExperimentsField
					.getText().trim());

			if (numberOfExperiments < 2) {
				message = SimpleCommonTestCostReduction
						.exhaustiveSimpleCommonTestCostConstraintReduction(
								algorithm, measure, budgetFactor);
			} else {
				message = SimpleCommonTestCostReduction
						.batchExhaustiveSimpleCommonTestCostConstraintReduction(
								algorithm, measure, budgetFactor,
								numberOfExperiments);
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

}// Of class TestCostConstraintReductionExhaustiveDialog
