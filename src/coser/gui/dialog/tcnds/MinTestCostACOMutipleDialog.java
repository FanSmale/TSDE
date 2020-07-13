package coser.gui.dialog.tcnds;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import coser.algorithm.ReductionACO;
import coser.gui.dialog.common.DialogCloser;
import coser.gui.dialog.common.ErrorDialog;
import coser.gui.dialog.common.HelpDialog;
import coser.gui.dialog.common.ProgressDialog;
import coser.gui.guicommon.GUICommon;
import coser.gui.others.DoubleField;
import coser.gui.others.IntegerField;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Specify reduction related parameters, and obtain minimal test cost
 * attribute reducts. The algorithm is implemented in
 * coser.datamodel.decisionsystem.TestCostNominalDecisionSystemACO.
 * <p>
 * Author: <b> Zilong Xu </b> xzl-wy163@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Done.<br>
 * Written time: March 12, 2011. <br>
 * Last modify time: April 27, 2011.
 */
public class MinTestCostACOMutipleDialog extends Dialog implements
		ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -512833090130417459L;

	public static MinTestCostACOMutipleDialog minTestCostACOMutipleDialog = new MinTestCostACOMutipleDialog();

	/**
	 * The frame
	 */
	// JFrame frame;

	/**
	 * The label of ant counts
	 */
	Label antCountsLabel;

	/**
	 * The text field of ant counts
	 */
	IntegerField antCountsText;

	/**
	 * The label of alpha
	 */
	Label alphaLowerBoundLabel;

	/**
	 * The text field of alpha lower bound
	 */
	DoubleField alphaLowerBoundText;

	/**
	 * The label of alpha
	 */
	Label alphaUpperBoundLabel;

	/**
	 * The text field of alpha upper bound
	 */
	DoubleField alphaUpperBoundText;

	/**
	 * The alpha step label
	 */
	Label alphaStepLabel;

	/**
	 * The alpha step text
	 */
	DoubleField alphaStepText;

	/**
	 * The beta upper bound label
	 */
	Label betaUpperBoundLabel;

	/**
	 * The beta upper bound text field
	 */
	DoubleField betaUpperBoundText;

	/**
	 * The experiment counts label
	 */
	Label experimentCountsLabel;

	/**
	 * The experiment counts text field
	 */
	IntegerField experimentCountsText;

	/**
	 * The OK button
	 */
	Button OkButton;

	/**
	 * The Cancel button
	 */
	Button CancelButton;

	/**
	 * The help button
	 */
	Button helpButton;

	/**
	 * The number of ants.
	 */
	int antCounts;

	/**
	 * The alpha lower bound
	 */
	double alphaLowerBound;

	/**
	 * The alpha upper bound
	 */
	double alphaUpperBound;

	/**
	 * The alpha step length
	 */
	double alphaStepLength;

	/**
	 * The number of experiments
	 */
	int experimentsCounts;

	/**
	 * The Construct Method
	 */
	public MinTestCostACOMutipleDialog() {

		super(GUICommon.mainFrame, "Minimal test cost attribute reduction ACO",
				true);

		antCountsLabel = new Label("Ant counts");
		antCountsText = new IntegerField("100");
		alphaLowerBoundLabel = new Label("Alpha Lower Bound:");
		alphaLowerBoundText = new DoubleField("1");
		alphaUpperBoundLabel = new Label("Alpha Upper Bound:");
		alphaUpperBoundText = new DoubleField("4");
		// betaUpperBoundLabel = new JLabel("Beta Upper Bound:");
		// betaUpperBoundText = new JTextField("1");
		alphaStepLabel = new Label("Alpha Step Length:");
		alphaStepText = new DoubleField("1");
		experimentCountsLabel = new Label("Number of experiments:");
		experimentCountsText = new IntegerField("100");
		OkButton = new Button("Compute");
		OkButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		CancelButton = new Button("Cancel");
		CancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog(
				"Minimal test cost attribute reduction",
				"coser/gui/dialog/tcnds/MinTestCostACOMutipleDialogHelp.txt"));
		helpButton.setSize(20, 10);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(5, 2));

		centerPanel.add(antCountsLabel);
		centerPanel.add(antCountsText);
		centerPanel.add(alphaLowerBoundLabel);
		centerPanel.add(alphaLowerBoundText);
		centerPanel.add(alphaUpperBoundLabel);
		centerPanel.add(alphaUpperBoundText);
		centerPanel.add(alphaStepLabel);
		centerPanel.add(alphaStepText);
		centerPanel.add(experimentCountsLabel);
		centerPanel.add(experimentCountsText);

		Panel okPanel = new Panel();
		okPanel.add(OkButton);
		okPanel.add(CancelButton);
		okPanel.add(helpButton);

		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, okPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(200, 200);
		setSize(530, 260);
		addWindowListener(dialogCloser);
		setVisible(false);

	}// Construct Method

	/**
	 * The performing action when events appear.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		setVisible(false);
		ProgressDialog.progressDialog
				.setMessageAndShow("Please wait for a while."
						+ "\r\nThe execution time depends on the dataset size and the number of experiments."
						+ "\r\nThe progress is shown in the console.\r\n");

		try {
			// Use another name to make statements shorter.
			// TestCostNominalDecisionSystem currentSystem =
			// CoserProject.currentProject.currentTcNds;

			// String message = "";

			String message = "";
			int antCounts = Integer.parseInt(antCountsText.getText().trim());
			alphaLowerBound = Double.parseDouble(alphaLowerBoundText.getText()
					.trim());
			alphaUpperBound = Double.parseDouble(alphaUpperBoundText.getText()
					.trim());
			alphaStepLength = Double
					.parseDouble(alphaStepText.getText().trim());
			experimentsCounts = Integer.parseInt(experimentCountsText.getText()
					.trim());
			String results = ReductionACO.batchMinimalTestCostReductionACO(
					antCounts, alphaUpperBound, alphaLowerBound,
					alphaStepLength, experimentsCounts);

			System.out.println(results);// 输出结果

			message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com\r\n"
					+ results;
			ProgressDialog.progressDialog.setMessageAndShow(message);
			// System.out.println(tempDecisionSystem);
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
			ee.printStackTrace();
			return;
		}// Of try
	}// Of actionPerformed

	/**
	 * 主方法，可以用于单元测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MinTestCostACOMutipleDialog.minTestCostACOMutipleDialog
				.setVisible(true);
	}

}// MinTestCostACOMutiplen