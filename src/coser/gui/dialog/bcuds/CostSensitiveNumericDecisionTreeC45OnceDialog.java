package coser.gui.dialog.bcuds;

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
 * Written time: September 7, 2012. <br>
 * Last modify time: September 7, 2012.
 */
public class CostSensitiveNumericDecisionTreeC45OnceDialog extends Dialog
		implements ActionListener {

	public static CostSensitiveNumericDecisionTreeC45OnceDialog costSensitiveNumericDecisionTreeC45OnceDialog = new CostSensitiveNumericDecisionTreeC45OnceDialog();

	private static final long serialVersionUID = 1378150864641480365L;

	/**
	 * The main panel
	 */
	private Panel mainPanel;

	/**
	 * The label of lambda value
	 */
	private Label lambdaValueLabel;

	/**
	 * The double field of lambda value setup.
	 */
	private DoubleField lambdaValueField;

	/**
	 * The label of prune
	 */
	private Label pruneLabel;

	/**
	 * The pruning strategy
	 */
	private JComboBox<String> pruneComboBox;

	/**
	 * The testing strategy label
	 */
	private Label testStrategyLabel;

	/**
	 * The testing strategy combobox.
	 */
	private JComboBox<String> testStrategyComboBox;

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
	 * The prune strategy
	 */
	private int pruneStrategy;

	/**
	 * The test strategy
	 */
	private int testStrategy;

	/**
	 * The label of training set proportion
	 */
	private Label trainProportionLabel;

	/**
	 * The double field of training set proportion
	 */
	private DoubleField trainProportionField;

	/**
	 * Whether reveal details
	 */
	private Checkbox detailCheckBox;

	/**
	 * The lambda value
	 */
	private double lambda;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	public CostSensitiveNumericDecisionTreeC45OnceDialog() {
		super(GUICommon.mainFrame, "Cost-sensitive decistion tree C4.5", true);

		mainPanel = new Panel();
		buttonPanel = new Panel();

		lambdaValueLabel = new Label("The lambda value");
		lambdaValueField = new DoubleField("0");
		pruneLabel = new Label("The pruning strategy");
		String[] pruneStrategyString = { "Unprune", "Pre-prune", "Post-prune" };
		pruneComboBox = new JComboBox<String>(pruneStrategyString);
		testStrategyLabel = new Label("Test strategy");
		String[] testStrategyString = { "Use training set", "Split into two",
				"Cross validation" };
		testStrategyComboBox = new JComboBox<String>(testStrategyString);
		trainProportionLabel = new Label("Training set proportion:");
		trainProportionField = new DoubleField("0.6");
		detailCheckBox = new Checkbox("Reveal running details", false);

		mainPanel.setLayout(new GridLayout(5, 2));
		mainPanel.add(lambdaValueLabel);
		mainPanel.add(lambdaValueField);
		mainPanel.add(pruneLabel);
		mainPanel.add(pruneComboBox);
		mainPanel.add(testStrategyLabel);
		mainPanel.add(testStrategyComboBox);
		mainPanel.add(trainProportionLabel);
		mainPanel.add(trainProportionField);
		mainPanel.add(detailCheckBox);

		okButton = new Button(" Build ");
		cancelButton = new Button(" Cancel ");
		helpButton = new Button(" Help ");
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
		setSize(420, 200);
		addWindowListener(dialogCloser);
		setVisible(false);

		pruneStrategy = CostJ48.UN_PRUNE;
		lambda = 0;

	}// Of the constructor

	/**
	 *************************** 
	 * Build the tree
	 *************************** 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			lambda = lambdaValueField.getValue();
			String pruneString = ((String) pruneComboBox.getSelectedItem())
					.trim();
			if (pruneString == "Unprune") {
				pruneStrategy = CostJ48.UN_PRUNE;
			} else if (pruneString == "Pre-prune") {
				pruneStrategy = CostJ48.PRE_PRUNE;
			} else if (pruneString == "Post-prune") {
				pruneStrategy = CostJ48.POST_PRUNE;
			} else {
				throw new Exception("Errors occur!");
			}// Of if - else if - else
			double trainProportion = 0.6;
			testStrategy = testStrategyComboBox.getSelectedIndex();
			if (testStrategy == 1) {
				trainProportion = Double.parseDouble(trainProportionField
						.getText().trim());
			}// Of if
			boolean isDetial = detailCheckBox.getState();

			BuildAndTestTreeCostC45 buildAndTestTreeCostC45 = new BuildAndTestTreeCostC45(
					CoserProject.currentProject.currentBcUds, lambda,
					pruneStrategy, testStrategy, trainProportion, isDetial);
			CostJ48 currentCostJ48 = buildAndTestTreeCostC45
					.buildDecisionTree();
			System.out.println(currentCostJ48);
			ProgressDialog.progressDialog.setMessageAndShow(currentCostJ48
					.toString());
			// toBack();
			// setFocusable(false);
			setVisible(false);
			ProgressDialog.progressDialog.setSize(400, 400);
			ProgressDialog.progressDialog.setLocation(250, 200);
			// ProgressDialog.progressDialog.toFront();
			// ProgressDialog.progressDialog.setAlwaysOnTop(true);
			// ProgressDialog.progressDialog.setFocusable(true);
		} catch (Exception ee) {
			ee.printStackTrace();
		}// Of try-catch

	}// Of actionPerformed

	/**
	 *************************** 
	 * The main method using test the class
	 *************************** 
	 */
	public static void main(String[] args) {
		CostSensitiveNumericDecisionTreeC45OnceDialog.costSensitiveNumericDecisionTreeC45OnceDialog
				.setVisible(true);
	}// Of main

}// Of class CostSensitiveNumericDecisionTreeC45OnceDialog
