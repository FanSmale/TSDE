package coser.gui.dialog.tcnds;

import java.awt.*;
import java.awt.event.*;
import coser.algorithm.*;

import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Specify reduction related parameters, and obtain test cost
 * constraint reducts. The algorithm is implemented in
 * coser.datamodel.decisionsystem.TestCostConstraintGADecisionSystem.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com and <b>Jiabin Liu</b><br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organizaion: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: The very beginning.<br>
 * Written time: September 1, 2011. <br>
 * Last modify time: March 1, 2012.
 */
public class TestCostConstraintReductionGADialog extends Dialog implements
		ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 24298535612351L;

	public static TestCostConstraintReductionGADialog testCostConstraintReductionGADialog = new TestCostConstraintReductionGADialog();

	/**
	 * Algorithms: weighted, not weighted.
	 */
	// private JComboBox weightingComboBox;

	/**
	 * /** Labmda lower bound.
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
	 * Budget factor. Control the given test-cost less than the value to
	 * construct a reduct.
	 */
	private DoubleField budgetFactorField;

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
	 * Show detailed results?
	 */
	private Checkbox showDetailCheckbox;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private TestCostConstraintReductionGADialog() {
		// This dialog is not module
		super(GUICommon.mainFrame,
				"Test cost constraint attribute reduction based on GA", true);

		// String[] weightingModes = { "Unweighting", "Weighting" };
		// weightingComboBox = new JComboBox(weightingModes);
		// weightingComboBox.setSelectedIndex(1);

		lambdaLowerBoundField = new DoubleField("-2");
		lambdaUpperBoundField = new DoubleField("0");
		lambdaStepLengthField = new DoubleField("0.25");
		budgetFactorField = new DoubleField("0.8");
		numberOfChromosomeField = new IntegerField("32");
		numberOfGenerationField = new IntegerField("100");
		experimentTimesField = new IntegerField("100");
		showDetailCheckbox = new Checkbox("Show running details", false);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(8, 2));
		// centerPanel.add(new Label("Weighting mode: "));
		// centerPanel.add(weightingComboBox);
		centerPanel.add(new Label("Lambda upper bound: "));
		centerPanel.add(lambdaUpperBoundField);
		centerPanel.add(new Label("Lambda lower bound : "));
		centerPanel.add(lambdaLowerBoundField);
		centerPanel.add(new Label("Lambda step length: "));
		centerPanel.add(lambdaStepLengthField);
		centerPanel.add(new Label("Budget factor: "));
		centerPanel.add(budgetFactorField);
		centerPanel.add(new Label("The number of chromosome: "));
		centerPanel.add(numberOfChromosomeField);
		centerPanel.add(new Label("The max genetic generation: "));
		centerPanel.add(numberOfGenerationField);
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
						"Minimal test cost attribute reduction based on GA",
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
	 * Show the results.
	 *************************** 
	 */
	public void actionPerformed(ActionEvent ae) {
		setVisible(false);
		ProgressDialog.progressDialog
				.setMessageAndShow("Please wait for a while."
						+ "\r\nThe execution time depends on the dataset size and the number of experiments."
						+ "\r\nThe progress is shown in the console.\r\n");

		try {
			String message = "";

			// boolean weighting = (weightingComboBox.getSelectedIndex() == 1);
			double lambdaUpperBound = Double.parseDouble(lambdaUpperBoundField
					.getText().trim());
			double lambdaLowerBound = Double.parseDouble(lambdaLowerBoundField
					.getText().trim());
			double lambdaStepLength = Double.parseDouble(lambdaStepLengthField
					.getText().trim());
			double budgetFactor = Double.parseDouble(budgetFactorField
					.getText().trim());
			int experiments = Integer.parseInt(experimentTimesField.getText()
					.trim());
			boolean showDetail = showDetailCheckbox.getState();
			int numberOfChromosome = Integer.parseInt(numberOfChromosomeField
					.getText().trim());
			int numberOfGeneration = Integer.parseInt(numberOfGenerationField
					.getText().trim());

			// reduce from GA1
			message = TestCostConstraintReductionGA
					.batchTestCostConstraintReductionGAComparison(
							lambdaUpperBound, lambdaLowerBound,
							lambdaStepLength, budgetFactor, numberOfChromosome,
							numberOfGeneration, experiments, showDetail);

			/*
			 * // combine the two algorithm message =
			 * ConstraintReductionGA.batchConstraintReductionGA1(
			 * lambdaUpperBound, lambdaLowerBound, lambdaStepLength,
			 * budgetFactor, numberOfChromosome, numberOfGeneration,
			 * experiments, showDetail);
			 */

			message = "Generated by Coser http://grc.fjzs.edu.cn, minfanphd@163.com\r\n"
					+ message;
			ProgressDialog.progressDialog.setMessageAndShow(message);

		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
			return;
		}// Of try
	}// Of actionPerformed

}// Of class ReductionGeneticDialog

