package coser.gui.dialog.tcnds;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import coser.algorithm.*;
import coser.datamodel.decisionsystem.TestCostNominalDecisionSystem;

import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;
import coser.project.CoserProject;

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
public class TestCostConstraintReductionExhaustiveDialog extends Dialog
		implements ActionListener, ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7117952781990876204L;

	public static TestCostConstraintReductionExhaustiveDialog testCostConstraintReductionExhaustiveDialog = new TestCostConstraintReductionExhaustiveDialog();

	/**
	 * Algorithms: SESRA, SESRAstar, AROP_S.
	 */
	private JComboBox<String> algorithmComboBox;

	/**
	 * Consistency tempMeasure.
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
	private TestCostConstraintReductionExhaustiveDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame,
				"Test cost constraint reduction (exhaustive)", true);

		String[] algorithms = { "SESRA", "SESRAstar", "BASS", "Backtrack",
				"ALL" };
		algorithmComboBox = new JComboBox<String>(algorithms);
		algorithmComboBox.setSelectedIndex(0);

		String[] measures = { "POS", "ENTROY", "MAJORITY" };
		consistencyMetricComboBox = new JComboBox<String>(measures);
		consistencyMetricComboBox.setSelectedIndex(0);

		budgetFactorField = new DoubleField("0.8");

		numberOfExperimentsField = new IntegerField("1");

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(7, 2));
		centerPanel.add(new Label("Algorithm: "));
		centerPanel.add(algorithmComboBox);
		// algorithmComboBox.addItemListener(this);
		centerPanel.add(new Label("Consistency tempMeasure: "));
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
						"Test cost constraint reduction (exhaustive)",
						"coser/gui/dialog/tcnds/TestCostConstraintReductionExhaustiveDialogHelp.txt"));
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
			TestCostNominalDecisionSystem currentSystem = CoserProject.currentProject.currentTcNds;

			String message = "";

			int algorithm = algorithmComboBox.getSelectedIndex();
			int tempMeasure = consistencyMetricComboBox.getSelectedIndex();
			double budgetFactor = Double.parseDouble(budgetFactorField
					.getText().trim());
			int numberOfExperiments = Integer.parseInt(numberOfExperimentsField
					.getText().trim());

			currentSystem.setMeasure(tempMeasure);
			// if (tempMeasure != currentSystem.getMeasure()) {
			// throw new Exception("The tempMeasure is " + tempMeasure +
			// " while the tempMeasure of the current system is " +
			// currentSystem.getMeasure());
			// }
			if (numberOfExperiments < 2) {
				message = Reduction.exhaustiveTestCostConstraintReduction(
						algorithm, tempMeasure, budgetFactor);
			} else {
				message = Reduction.batchExhaustiveTestCostConstraintReduction(
						algorithm, tempMeasure, budgetFactor,
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

	/**
	 *************************** 
	 * Control the combobox.
	 *************************** 
	 */
	public void itemStateChanged(ItemEvent e) {
		int currentIndex = algorithmComboBox.getSelectedIndex();
		TestCostNominalDecisionSystem currentSystem = CoserProject.currentProject.currentTcNds;
		if ((currentIndex == 2) || (currentIndex == 4)) {
			consistencyMetricComboBox.setSelectedIndex(currentSystem
					.getMeasure());
			consistencyMetricComboBox.setEnabled(false);
		} else {
			consistencyMetricComboBox.setEnabled(true);
		}// Of if
	}// Of itemStateChanged

}// Of class TestCostConstraintReductionExhaustiveDialog