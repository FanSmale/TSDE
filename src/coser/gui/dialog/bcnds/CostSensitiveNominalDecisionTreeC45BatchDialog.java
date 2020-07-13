package coser.gui.dialog.bcnds;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import coser.classifiers.trees.BuildAndTestTreeCostC45;
import coser.classifiers.trees.CostJ48;
import coser.gui.dialog.common.DialogCloser;
import coser.gui.dialog.common.ProgressDialog;
import coser.gui.guicommon.GUICommon;
import coser.gui.others.DoubleField;
import coser.gui.others.IntegerField;
import coser.project.CoserProject;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: The cost-sensitive decision tree C4.5 dialog.
 * <p>
 * Author: <b>Zilon Xu</b> xzl-wy163@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Begining.<br>
 * Written time: September 25, 2012. <br>
 * Last modify time: September 7, 2012.
 */
public class CostSensitiveNominalDecisionTreeC45BatchDialog extends Dialog
		implements ActionListener {

	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = 4552885894898234041L;

	public static CostSensitiveNominalDecisionTreeC45BatchDialog costSensitiveNominalDecisionTreeC45BatchDialog = new CostSensitiveNominalDecisionTreeC45BatchDialog();

	/**
	 * The main panel
	 */
	private Panel mainPanel;

	/**
	 * The label of lambda lower bound
	 */
	private Label lambdaLowerBoundLabel;

	/**
	 * The double field of lambda lower bound
	 */
	private DoubleField lambdaLowerBoundField;

	/**
	 * The label of lambda upper bound
	 */
	private Label lambdaUpperBoundLabel;

	/**
	 * The double field of lambda upper bound
	 */
	private DoubleField lambdaUpperBoundField;

	/**
	 * The label of lambda step length
	 */
	private Label lambdaStepLengthLabel;

	/**
	 * The double field of lambda step length
	 */
	private DoubleField lambdaStepLengthField;

	/**
	 * The label of experiment counts
	 */
	private Label experimentCountsLabel;

	/**
	 * The integer field of experiment counts.
	 */
	private IntegerField experimentCountsField;

	/**
	 * The prune strategy label.
	 */
	private Label pruneStrategyLabel;

	/**
	 * The prune strategy combobox
	 */
	private JComboBox<String> pruneStrategyComboBox;

	/**
	 * The test strategy label.
	 */
	private Label testStrategyLabel;

	/**
	 * The test strategy combobox
	 */
	private JComboBox<String> testStrategyComboBox;

	/**
	 * The label of train set proportion
	 */
	private Label trainsetProportionLabel;

	/**
	 * The double field of train set proprotion
	 */
	private DoubleField trainsetProportionField;

	/**
	 * The button panel
	 */
	private Panel buttonPanel;

	/**
	 * The ok button
	 */
	private Button okButton;

	/**
	 * The cancel button
	 */
	private Button cancelButton;

	/**
	 * The help button
	 */
	private Button helpButton;

	/**
	 * The lower bound of lambda
	 */
	private double lambdaLowerBound;

	/**
	 * The upper bound of lambda
	 */
	private double lambdaUpperBound;

	/**
	 * The step length of lambda
	 */
	private double lambdaStepLength;

	/**
	 * Whether reveal details
	 */
	private Checkbox detailCheckBox;

	/**
	 * The experiment counts
	 */
	private int experimentCounts;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	public CostSensitiveNominalDecisionTreeC45BatchDialog() {
		super(GUICommon.mainFrame, "Cost-sensitive decistion tree C4.5 Batch",
				true);

		mainPanel = new Panel();
		buttonPanel = new Panel();

		lambdaLowerBoundLabel = new Label("The lower bound of lambda: ");
		lambdaLowerBoundField = new DoubleField("-2");
		lambdaUpperBoundLabel = new Label("The upper bound of lambda: ");
		lambdaUpperBoundField = new DoubleField("0");
		lambdaStepLengthLabel = new Label("The step length of lambda: ");
		lambdaStepLengthField = new DoubleField("0.5");
		experimentCountsLabel = new Label("The experiment counts: ");
		experimentCountsField = new IntegerField("100");
		pruneStrategyLabel = new Label("Prune strategy: ");
		String[] pruneStrategyString = { "Unprune", "Pre-prune", "Post-prune" };
		pruneStrategyComboBox = new JComboBox<String>(pruneStrategyString);
		testStrategyLabel = new Label("Test strategy");
		String[] testStrategyString = { "Use training set", "Split into two",
				"Cross validation" };
		testStrategyComboBox = new JComboBox<String>(testStrategyString);
		trainsetProportionLabel = new Label("Training set proportion:");
		trainsetProportionField = new DoubleField("0.6");
		detailCheckBox = new Checkbox("Reveal running details", false);

		okButton = new Button(" Build ");
		cancelButton = new Button(" Cancel ");
		helpButton = new Button(" Help ");

		mainPanel.setLayout(new GridLayout(8, 2));
		mainPanel.add(lambdaLowerBoundLabel);
		mainPanel.add(lambdaLowerBoundField);
		mainPanel.add(lambdaUpperBoundLabel);
		mainPanel.add(lambdaUpperBoundField);
		mainPanel.add(lambdaStepLengthLabel);
		mainPanel.add(lambdaStepLengthField);
		mainPanel.add(experimentCountsLabel);
		mainPanel.add(experimentCountsField);
		mainPanel.add(pruneStrategyLabel);
		mainPanel.add(pruneStrategyComboBox);
		mainPanel.add(testStrategyLabel);
		mainPanel.add(testStrategyComboBox);
		mainPanel.add(trainsetProportionLabel);
		mainPanel.add(trainsetProportionField);
		mainPanel.add(detailCheckBox);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(helpButton);

		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		cancelButton.addActionListener(dialogCloser);

		add(BorderLayout.CENTER, mainPanel);
		add(BorderLayout.SOUTH, buttonPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(200, 200);
		setSize(420, 250);
		addWindowListener(dialogCloser);
		setVisible(false);

	}// Of the constructor

	/**
	 *************************** 
	 * Build the tree
	 *************************** 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			lambdaLowerBound = Double.parseDouble(lambdaLowerBoundField
					.getText().trim());
			lambdaUpperBound = Double.parseDouble(lambdaUpperBoundField
					.getText().trim());
			lambdaStepLength = Double.parseDouble(lambdaStepLengthField
					.getText().trim());
			experimentCounts = Integer.parseInt(experimentCountsField.getText()
					.trim());
			int pruneStrategy;
			String pruneString = ((String) pruneStrategyComboBox
					.getSelectedItem()).trim();
			if (pruneString == "Unprune") {
				pruneStrategy = CostJ48.UN_PRUNE;
			} else if (pruneString == "Pre-prune") {
				pruneStrategy = CostJ48.PRE_PRUNE;
			} else if (pruneString == "Post-prune") {
				pruneStrategy = CostJ48.POST_PRUNE;
			} else {
				throw new Exception("Errors occur!");
			}// Of if - else if - else
			int testStrategy;
			String testStrategyString = ((String) testStrategyComboBox
					.getSelectedItem()).trim();
			if (testStrategyString == "Use training set") {
				testStrategy = 0;
			} else if (testStrategyString == "Split into two") {
				testStrategy = 1;
			} else if (testStrategyString == "Cross validation") {
				testStrategy = 2;
			} else {
				throw new Exception("Errors occur!");
			}// Of if - else if - else
			double trainsetProportion = Double
					.parseDouble(trainsetProportionField.getText().trim());
			boolean isDetial = detailCheckBox.getState();

			setVisible(false);
			String resultMessage = "";
			BuildAndTestTreeCostC45 buildAndTestTreeCostC45 = new BuildAndTestTreeCostC45(
					CoserProject.currentProject.currentBcNds, lambdaLowerBound,
					lambdaUpperBound, lambdaStepLength, pruneStrategy,
					testStrategy, trainsetProportion, isDetial);
			resultMessage = buildAndTestTreeCostC45.batchDecisionTreeC45(
					lambdaLowerBound, lambdaUpperBound, lambdaStepLength,
					experimentCounts);
			ProgressDialog.progressDialog.setMessageAndShow(resultMessage);
			ProgressDialog.progressDialog.setVisible(true);
			ProgressDialog.progressDialog.setSize(400, 400);
		} catch (Exception ee) {
			ee.printStackTrace();
		}// Of try-catch
	}// Of actionPerformed

	/**
	 *************************** 
	 * The main method using test this class.
	 *************************** 
	 */
	public static void main(String[] args) {
		CostSensitiveNominalDecisionTreeC45BatchDialog.costSensitiveNominalDecisionTreeC45BatchDialog
				.setVisible(true);
	}

}// Of class CostSensitiveDecisionTreeC45BatchDialog
