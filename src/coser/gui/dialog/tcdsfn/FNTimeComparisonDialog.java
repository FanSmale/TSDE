package coser.gui.dialog.tcdsfn;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import coser.datamodel.decisionsystem.*;
import coser.project.*;
import coser.algorithm.*;
import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Compare the performances of the backtrack algorithm and heuristic
 * algorithm. The algorithm is implemented in
 * coser.datamodel.decisionsystem.TestCostSensitiveDecisionSystem.
 * <p>
 * Author: <b>Shujiao Liao</b> sjliao2011@163.com<br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organizaion: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Done.<br>
 * Written time: August 23, 2011. <br>
 * Last modify time: August 23, 2011.
 */
public class FNTimeComparisonDialog extends Dialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8018133171659603261L;

	public static FNTimeComparisonDialog fNTimeComparisonDialog = new FNTimeComparisonDialog();

	/**
	 * The delta.
	 */
	private DoubleField deltaField;

	/**
	 * Labmda.
	 */
	private DoubleField lambdaField;

	/**
	 * The efc_ctrl.
	 */
	private DoubleField efc_ctrlField;

	/**
	 * The number of chromosome.
	 */
	private IntegerField numberOfChromosomeField;

	/**
	 * The max genetic generation.
	 */
	private IntegerField numberOfGenerationField;

	/**
	 * Number of experiments with different test-cost settings.
	 */
	private IntegerField experimentTimesField;

	/**
	 * Algorithms: first or second.
	 */
	private JComboBox<String> algorithmComboBox;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private FNTimeComparisonDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Time comparison", true);

		lambdaField = new DoubleField("-1", 10);
		experimentTimesField = new IntegerField("100", 10);
		numberOfChromosomeField = new IntegerField("32");
		numberOfGenerationField = new IntegerField("50");
		deltaField = new DoubleField("0.005", 10);
		efc_ctrlField = new DoubleField("0.01");

		String[] algorithmModes = { "Fisrt", "Second" };
		algorithmComboBox = new JComboBox<String>(algorithmModes);
		algorithmComboBox.setSelectedIndex(0);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(7, 2));
		centerPanel.add(new Label("algorithm mode: "));
		centerPanel.add(algorithmComboBox);
		centerPanel.add(new Label("Delta: "));
		centerPanel.add(deltaField);
		centerPanel.add(new Label("efc_ctrl: "));
		centerPanel.add(efc_ctrlField);
		centerPanel.add(new Label("Lambda: "));
		centerPanel.add(lambdaField);
		centerPanel.add(new Label("The number of chromosome: "));
		centerPanel.add(numberOfChromosomeField);
		centerPanel.add(new Label("The max genetic generation: "));
		centerPanel.add(numberOfGenerationField);
		centerPanel.add(new Label("Number of experiments: "));
		centerPanel.add(experimentTimesField);

		Button okButton = new Button(" Compute ");
		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog(
				"Time comparison for backtrack and GA algorithms",
				"coser/gui/dialog/tcdsfn/FNTimeComparisonDialogHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(okButton);
		okPanel.add(cancelButton);
		okPanel.add(helpButton);

		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, okPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(200, 150);
		setSize(380, 300);
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
			TestCostDecisionSystemFixedNeighborhood currentSystem = CoserProject.currentProject.currentTcDsFn;

			boolean algorithm = (algorithmComboBox.getSelectedIndex() == 1);
			double delta = deltaField.getValue();
			currentSystem.setDelta(delta);
			double efc_ctrl = Double
					.parseDouble(efc_ctrlField.getText().trim());
			double lambda = lambdaField.getValue();
			int experiments = experimentTimesField.getValue();
			int numberOfChromosome = Integer.parseInt(numberOfChromosomeField
					.getText().trim());
			int numberOfGeneration = Integer.parseInt(numberOfGenerationField
					.getText().trim());
			String message = "";
			ReductionFNGA tc = new ReductionFNGA();

			message = tc.batchComparisonFN(delta, efc_ctrl, lambda,
					numberOfChromosome, numberOfGeneration, algorithm,
					experiments);

			message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, sjliao2011@163.com\r\n"
					+ message;
			ProgressDialog.progressDialog.setMessageAndShow(message);
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
			return;
		}// Of try
	}// Of actionPerformed

}// Of class FNTimeComparisonDialog

