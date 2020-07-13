package coser.gui.dialog.bcuds;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import coser.classifiers.trees.BuildAndTestTreeCostC45;
import coser.datamodel.decisionsystem.BothCostsNumericDecisionSystem;
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
 * Summary: The cost-sensitive J48 decision tree dialog. Weighted information
 * gain with different lambda values and the cost-gain algorithm are compared.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: The very beginning.<br>
 * Written time: March 2, 2012. <br>
 * Last modify time: March 8, 2012.
 */
public class CostSensitiveJ48Dialog extends Dialog implements ActionListener {

	private static final long serialVersionUID = 8681604629130519117L;

	/**
	 * The only instance.
	 */
	public static CostSensitiveJ48Dialog costSensitiveJ48Dialog = new CostSensitiveJ48Dialog();

	/**
	 * Testing strategy.
	 */
	private JComboBox<String> testStrategyComboBox;

	/**
	 * Algorithm.
	 */
	// private JComboBox algorithmComboBox;

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
	 * Omega lower bound.
	 */
	// private DoubleField omegaLowerBoundField;

	/**
	 * Omega upper bound.
	 */
	// private DoubleField omegaUpperBoundField;

	/**
	 * Omega step length.
	 */
	// private DoubleField omegaStepLengthField;

	/**
	 * Prune strategy.
	 */
	private JComboBox<String> pruneStrategyComboBox;

	/**
	 * Training set percentage.
	 */
	private DoubleField trainingPercentageField;

	/**
	 * Number of folds.
	 */
	private IntegerField cvFoldsField;

	/**
	 * Balance.
	 */
	private Checkbox balanceCheckbox;

	/**
	 * Number of experiments.
	 */
	private IntegerField experimentsField;

	/**
	 * Whether reveal details
	 */
	private Checkbox detailCheckBox;

	/**
	 * The text area to show results.
	 */
	private TextArea resultArea;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private CostSensitiveJ48Dialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Cost sensitive J48", true);

		int testStrategy = 1;
		String[] testStrategies = { "Use training set", "Split in two",
				"Cross validation" };
		testStrategyComboBox = new JComboBox<String>(testStrategies);
		testStrategyComboBox.setSelectedIndex(testStrategy);
		// int algorithm = 2;
		// String[] algorithms = { "lambda-ID3", "EG2", "Both" };
		// algorithmComboBox = new JComboBox(algorithms);
		// algorithmComboBox.setSelectedIndex(algorithm);

		lambdaLowerBoundField = new DoubleField("-4");
		lambdaUpperBoundField = new DoubleField("0");
		lambdaStepLengthField = new DoubleField("0.25");
		// omegaLowerBoundField = new DoubleField("0");
		// omegaUpperBoundField = new DoubleField("4");
		// omegaStepLengthField = new DoubleField("0.25");

		String[] pruneStrategies = { "No", "Pre-prune", "Post-prune" };
		pruneStrategyComboBox = new JComboBox<String>(pruneStrategies);
		pruneStrategyComboBox.setSelectedIndex(2);

		trainingPercentageField = new DoubleField("0.6");
		cvFoldsField = new IntegerField("3");
		balanceCheckbox = new Checkbox("Balance for misclassification cost",
				false);
		experimentsField = new IntegerField("5");
		detailCheckBox = new Checkbox("Reveal running details", false);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(10, 2));
		centerPanel.add(new Label("Test stragegy: "));
		centerPanel.add(testStrategyComboBox);
		// centerPanel.add(new Label("Benchmark algorithm: "));
		// centerPanel.add(algorithmComboBox);
		centerPanel.add(new Label("Lambda upper bound: (valid for weighting)"));
		centerPanel.add(lambdaUpperBoundField);
		centerPanel.add(new Label("Lambda lower bound : "));
		centerPanel.add(lambdaLowerBoundField);
		centerPanel.add(new Label("Lambda step length: "));
		centerPanel.add(lambdaStepLengthField);
		// centerPanel.add(new
		// Label("Omega upper bound: (valid for weighting)"));
		// centerPanel.add(omegaUpperBoundField);
		// centerPanel.add(new Label("Omega lower bound : "));
		// centerPanel.add(omegaLowerBoundField);
		// centerPanel.add(new Label("Omega step length: "));
		// centerPanel.add(omegaStepLengthField);
		centerPanel.add(new Label("Prune strategy: "));
		centerPanel.add(pruneStrategyComboBox);
		centerPanel.add(new Label("Training set percentage: "));
		centerPanel.add(trainingPercentageField);
		centerPanel.add(new Label("Number of folds: "));
		centerPanel.add(cvFoldsField);
		centerPanel.add(new Label("Number of experiments"));
		centerPanel.add(experimentsField);
		centerPanel.add(balanceCheckbox);
		centerPanel.add(detailCheckBox);

		resultArea = new TextArea(10, 10);
		resultArea.setText("");

		Panel mainPanel = new Panel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(BorderLayout.CENTER, centerPanel);
		mainPanel.add(BorderLayout.SOUTH, resultArea);

		Button okButton = new Button(" Build ");
		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton
				.addActionListener(new HelpDialog(
						"Cost-sensitive decision tree",
						"src/coser/gui/dialog/bcsds/CostSensitiveDecisionTreeHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(okButton);
		okPanel.add(cancelButton);
		okPanel.add(helpButton);

		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, mainPanel);
		add(BorderLayout.SOUTH, okPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(200, 200);
		setSize(420, 470);
		addWindowListener(dialogCloser);
		setVisible(false);
	}// Of constructor

	/**
	 *************************** 
	 * Build the tree.
	 *************************** 
	 */
	public void actionPerformed(ActionEvent ae) {
		// setVisible(false);
		// ProgressDialog.progressDialog.setMessageAndShow("Please wait a few seconds. Setting ... \r\n");
		BothCostsNumericDecisionSystem currentRds = CoserProject.currentProject.currentBcUds;

		int testStrategy = testStrategyComboBox.getSelectedIndex();
		// int algorithm = algorithmComboBox.getSelectedIndex();
		double lambdaUpperBound = lambdaUpperBoundField.getValue();
		double lambdaLowerBound = lambdaLowerBoundField.getValue();
		double lambdaStepLength = lambdaStepLengthField.getValue();
		// double omegaUpperBound = omegaUpperBoundField.getValue();
		// double omegaLowerBound = omegaLowerBoundField.getValue();
		// double omegaStepLength = omegaStepLengthField.getValue();
		int pruneStrategy = pruneStrategyComboBox.getSelectedIndex();
		double trainingPercentage = trainingPercentageField.getValue();
		boolean balance = balanceCheckbox.getState();

		resultArea.setText("");
		int experimentCounts = experimentsField.getValue();
		boolean isDetails = detailCheckBox.getState();

		try {
			resultArea.append("\r\n*******Summary************\r\n");
			// resultArea.append("lambda/omega\t");
			// String tempTitle = "";
			String resultMessage = "";

			BuildAndTestTreeCostC45 buildAndTestTreeCostC45 = new BuildAndTestTreeCostC45(
					currentRds, lambdaLowerBound, lambdaUpperBound,
					lambdaStepLength, pruneStrategy, testStrategy,
					trainingPercentage, isDetails);
			resultMessage = buildAndTestTreeCostC45.batchDecisionTreeC45(
					lambdaLowerBound, lambdaUpperBound, lambdaStepLength,
					experimentCounts);
			// 排序树——竞争树的一般化
			// resultMessage = buildAndTestTreeCostC45.batchDecisionTreeC45Rank(
			// lambdaLowerBound, lambdaUpperBound, lambdaStepLength,
			// experimentCounts);
			// resultMessage = buildAndTestTreeCostC45
			// .batchDecisionTreeMisCostVariety(lambdaLowerBound,
			// lambdaUpperBound, lambdaStepLength,
			// experimentCounts);

			resultArea.append(resultMessage);

			// if (algorithm != 1) {
			// for (double tempLambda = lambdaLowerBound; tempLambda <=
			// lambdaUpperBound; tempLambda += lambdaStepLength) {
			// tempTitle += "l=" + tempLambda + "\t";
			// }// Of for
			// }
			// if (algorithm != 0) {
			// for (double tempOmega = omegaLowerBound; tempOmega <=
			// omegaUpperBound; tempOmega += omegaStepLength) {
			// tempTitle += "w=" + tempOmega + "\t";
			// }// Of for
			// }

			if (balance) {
				// tempTitle += tempTitle;
			}// Of if

			// resultArea.append(tempTitle + "l-ID3-CC\tEG2-CC\tboth-CCr\n");

		} catch (Exception ee) {
			ee.printStackTrace();
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
		}// Of try
			// ProgressDialog.progressDialog.setVisible(false);
	}// Of actionPerformed

	/**
	 *************************** 
	 * Implements ItemListener
	 *************************** 
	 * public void itemStateChanged(ActionEvent ae){
	 */

	/**
	 *************************** 
	 * The main method using to test the class.
	 *************************** 
	 */
	public static void main(String[] args) {
		CostSensitiveJ48Dialog.costSensitiveJ48Dialog.setVisible(true);
	}// Of main

}// Of class CostSensitiveJ48Dialog