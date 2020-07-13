package coser.gui.dialog.bcnds;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import coser.common.*;
import coser.datamodel.decisionsystem.*;
import coser.project.*;
import coser.classifiers.trees.*;

import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: The cost-sensitive decision tree dialog. Weighted information gain
 * with different lamda values and the cost-gain algorithm are compared.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Done.<br>
 * Written time: November 5, 2011. <br>
 * Last modify time: Febuary 2, 2012.
 */
public class CostSensitiveDecisionTreePruneDialog extends Dialog implements
		ActionListener {

	private static final long serialVersionUID = 8474971044536818508L;

	public static CostSensitiveDecisionTreePruneDialog costSensitiveDecisionTreePruneDialog = new CostSensitiveDecisionTreePruneDialog();

	/**
	 * Testing strategy.
	 */
	private JComboBox<String> testStrategyComboBox;

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
	 * Omega step length. private DoubleField omegaStepLengthField;
	 */

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
	 * The text area to show results.
	 */
	private TextArea resultArea;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private CostSensitiveDecisionTreePruneDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "CC-DT prune comparison", true);

		int testStrategy = 1;
		String[] testStrategies = { "Use training set", "Split in two",
				"Cross validation" };
		testStrategyComboBox = new JComboBox<String>(testStrategies);
		testStrategyComboBox.setSelectedIndex(testStrategy);

		lambdaLowerBoundField = new DoubleField("-2");
		lambdaUpperBoundField = new DoubleField("0");
		lambdaStepLengthField = new DoubleField("0.5");
		// omegaStepLengthField = new DoubleField("0.2");

		trainingPercentageField = new DoubleField("0.6");
		cvFoldsField = new IntegerField("3");
		balanceCheckbox = new Checkbox("Balance for misclassification cost",
				false);
		experimentsField = new IntegerField("5");

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(8, 2));
		centerPanel.add(new Label("Test stragegy: "));
		centerPanel.add(testStrategyComboBox);
		centerPanel.add(new Label("Lambda upper bound: (valid for weighting)"));
		centerPanel.add(lambdaUpperBoundField);
		centerPanel.add(new Label("Lambda lower bound : "));
		centerPanel.add(lambdaLowerBoundField);
		centerPanel.add(new Label("Lambda step length: "));
		centerPanel.add(lambdaStepLengthField);
		// centerPanel.add(new Label("Omega step length: "));
		// centerPanel.add(omegaStepLengthField);
		centerPanel.add(new Label("Training set percentage: "));
		centerPanel.add(trainingPercentageField);
		centerPanel.add(new Label("Number of folds: "));
		centerPanel.add(cvFoldsField);
		centerPanel.add(new Label("Number of experiments"));
		centerPanel.add(experimentsField);
		centerPanel.add(balanceCheckbox);

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
						"coser/gui/dialog/bcnds/CostSensitiveDecisionTreePruneHelp.txt"));
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
		BothCostsNominalDecisionSystem currentDs = CoserProject.currentProject.currentBcNds;

		int testStrategy = testStrategyComboBox.getSelectedIndex();
		double lambdaUpperBound = lambdaUpperBoundField.getValue();
		double lambdaLowerBound = lambdaLowerBoundField.getValue();
		double lambdaStepLength = lambdaStepLengthField.getValue();
		// double omegaStepLength = omegaStepLengthField.getValue();
		double trainingPercentage = trainingPercentageField.getValue();
		boolean balance = balanceCheckbox.getState();

		int numberOfLambdas = 1 + (int) ((lambdaUpperBound - lambdaLowerBound) / lambdaStepLength);
		// Do not consider omega
		int numberOfOmegas = 0;// 1 + (int)(1 / omegaStepLength);
		int cvFolds = cvFoldsField.getValue();
		// 创建buildAndTestTree对象，分配空间。
		BuildAndTestTree buildAndTestTree = new BuildAndTestTree(currentDs,
				trainingPercentage, testStrategy, numberOfLambdas,
				numberOfOmegas, cvFolds, false, true);
		resultArea.setText("");
		int numberOfExperiments = experimentsField.getValue();

		try {
			// 以实验次数为循环变量循环
			for (int i = 0; i < numberOfExperiments; i++) {
				System.out.print("\t" + i);
				// 执行oneRunPruneCompare(..)方法。在一种测试代价设置下做一次实验。
				String tempResult = buildAndTestTree.oneRunPruneCompare(
						lambdaLowerBound, lambdaUpperBound, lambdaStepLength,
						balance);
				if (testStrategy == 1)
					// System.out.println();
					resultArea.append(" \t" + tempResult);
				buildAndTestTree.refreshTestCost();// 刷新测试代价
			}// Of for i

			resultArea.append("\r\n*******Summary************\r\n");
			String lambdaString = "lambda\t";
			for (int i = 0; i < 2; i++) {
				for (double tempLambda = lambdaLowerBound; tempLambda <= lambdaUpperBound; tempLambda += lambdaStepLength) {
					lambdaString += "" + tempLambda + "\t";
				}// Of for
					// lambdaString += "cost-gain\t";
			}// Of for i
			resultArea.append(lambdaString);

			resultArea.append("CC-ID3\r\n");
			resultArea.append("Win times\t");
			int[] algorithmWinsArray = buildAndTestTree.getAlgorithmWinsArray();
			for (int i = 0; i < algorithmWinsArray.length; i++) {
				resultArea.append("" + algorithmWinsArray[i] + "\t");
			}// Of for
			resultArea.append("\r\n");

			resultArea.append("Averagte cost\t");
			double[] misclassificationCostSumArray = buildAndTestTree
					.getCostSumArray();
			for (int i = 0; i < misclassificationCostSumArray.length; i++) {
				resultArea.append(""
						+ SimpleTool
								.shorterDouble(misclassificationCostSumArray[i]
										/ numberOfExperiments) + "\t");
			}// Of for
			resultArea.append("\r\n");
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
		}// Of try
			// ProgressDialog.progressDialog.setVisible(false);
	}// Of actionPerformed

}// Of class CostSensitiveDecisionTreePruneDialog
