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
 * Author: <b>Fan Min</b> minfanphd@163.com and <b>Jiabin Liu</b> Copyright: The
 * source code and all documents are open and free. PLEASE keep this header
 * while revising the program. <br>
 * Organizaion: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Done.<br>
 * Written time: February 22, 2012. <br>
 * Last modify time: April 27, 2012.
 */
public class MinimalTestCostReductionGAWithESDialog extends Dialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1366538496937681775L;

	public static MinimalTestCostReductionGAWithESDialog minimalTestCostReductionGAWithESDialog = new MinimalTestCostReductionGAWithESDialog();

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
	private MinimalTestCostReductionGAWithESDialog() {
		// This dialog is not module
		super(
				GUICommon.mainFrame,
				"Minimal test cost attribute reduction based on GA with elitist select",
				true);

		String[] weightingModes = { "Unweighting", "Weighting" };
		weightingComboBox = new JComboBox<String>(weightingModes);
		weightingComboBox.setSelectedIndex(1);

		lambdaLowerBoundField = new DoubleField("-2");
		lambdaUpperBoundField = new DoubleField("0");
		lambdaStepLengthField = new DoubleField("0.25");
		numberOfChromosomeField = new IntegerField("32");
		numberOfGenerationField = new IntegerField("50");
		experimentTimesField = new IntegerField("100");
		showDetailCheckbox = new Checkbox("Show running details", false);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(9, 2));
		// centerPanel.add(new Label("algorithm mode: "));
		centerPanel.add(new Label("Weighting mode: "));
		centerPanel.add(weightingComboBox);
		centerPanel.add(new Label("Lambda upper bound: (valid for weighting)"));
		centerPanel.add(lambdaUpperBoundField);
		centerPanel.add(new Label("Lambda lower bound : "));
		centerPanel.add(lambdaLowerBoundField);
		centerPanel.add(new Label("Lambda step length: "));
		centerPanel.add(lambdaStepLengthField);
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
						"Minimal test cost attribute reduction based on GA with elitist select",
						"src/coser/gui/dialog/tcnds/MinimalTestCostReductionGAWithESDialogHelp.txt"));
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

			boolean weighting = (weightingComboBox.getSelectedIndex() == 1);
			int numberOfChromosome = Integer.parseInt(numberOfChromosomeField
					.getText().trim());
			int numberOfGeneration = Integer.parseInt(numberOfGenerationField
					.getText().trim());
			int experiments = Integer.parseInt(experimentTimesField.getText()
					.trim());
			boolean showDetail = showDetailCheckbox.getState();
			double lambdaStepLength = Double.parseDouble(lambdaStepLengthField
					.getText().trim());

			if (!weighting) {
				message = ReductionGA.batchGABasedMinimalReduct(
						numberOfChromosome, numberOfGeneration, experiments,
						showDetail);
			} else {

				double lambdaUpperBound = Double
						.parseDouble(lambdaUpperBoundField.getText().trim());
				double lambdaLowerBound = Double
						.parseDouble(lambdaLowerBoundField.getText().trim());

				message = ReductionGAWithES
						.batchGABasedMinimalTestCostReductionWithES(
								lambdaUpperBound, lambdaLowerBound,
								lambdaStepLength, numberOfChromosome,
								numberOfGeneration, experiments, showDetail);
			}// Of if

			message = "Generated by Coser http://grc.fjzs.edu.cn, minfanphd@163.com\r\n"
					+ message;
			ProgressDialog.progressDialog.setMessageAndShow(message);
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
			return;
		}// Of try
			// ProgressDialog.progressDialog.setVisible(false);
	}// Of actionPerformed

}// Of class MinimalTestCostReductionGAWithESDialog

