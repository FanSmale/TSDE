package coser.gui.dialog.tmds;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import coser.algorithm.ReductionBCO;
import coser.gui.dialog.common.DialogCloser;
import coser.gui.dialog.common.ErrorDialog;
import coser.gui.dialog.common.HelpDialog;
import coser.gui.dialog.common.ProgressDialog;
import coser.gui.guicommon.GUICommon;
import coser.gui.others.DoubleField;
import coser.gui.others.IntegerField;

public class MinTimeCostBCOMutipleDialog extends Dialog implements
		ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -512833090140317459L;

	public static MinTimeCostBCOMutipleDialog minTimeCostBCOMutipleDialog = new MinTimeCostBCOMutipleDialog();

	/**
	 * The frame
	 */
	// JFrame frame;

	/**
	 * The label of bee counts
	 */
	Label beeCountsLabel;

	/**
	 * The text field of bee counts
	 */
	IntegerField beeCountsText;
	/**
	 * The label of generations
	 */
	Label generationsLabel;
	/**
	 * The text field of generations
	 */
	IntegerField generationsText;
	/**
	 * The limited number of searching neighbor solutions label
	 */
	Label neighborLimitsLabel;

	/**
	 * The limited number of searching neighbor solutions text field
	 */
	IntegerField neighborLimitsText;

	/**
	 * The label of removing rate lower bound
	 */
	Label removeRateLowerBoundLabel;

	/**
	 * The text field of removing rate lower bound
	 */
	DoubleField removeRateLowerBoundText;

	/**
	 * The label of removing rate upper bound
	 */
	Label removeRateUpperBoundLabel;

	/**
	 * The text field of removing rate upper bound
	 */
	DoubleField removeRateUpperBoundText;

	/**
	 * The removing rate step label
	 */
	Label removeRateStepLabel;

	/**
	 * The removing rate step text
	 */
	DoubleField removeRateStepText;

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
	 * The number of bees.
	 */
	int beeCounts;
	/**
	 * The number of running generations.
	 */
	int generations;
	/**
	 * The limited number of searching neighbor solutions.
	 */
	int neighborLimits;
	/**
	 * The removeRate lower bound
	 */
	double removeRateLowerBound;

	/**
	 * The removeRate upper bound
	 */
	double remvoRateUpperBound;

	/**
	 * The removeRate step length
	 */
	double removeRateStepLength;

	/**
	 * The number of experiments
	 */
	int experimentsCounts;

	/**
	 * The Construct Method
	 */
	public MinTimeCostBCOMutipleDialog() {

		super(
				GUICommon.mainFrame,
				"Minimal time cost attribute reduction BCO with multiple experiments",
				true);

		beeCountsLabel = new Label("Bee counts");
		beeCountsText = new IntegerField("20");
		generationsLabel = new Label("Generations");
		generationsText = new IntegerField("10");
		neighborLimitsLabel = new Label("Neighbor counts:");
		neighborLimitsText = new IntegerField("10");
		removeRateLowerBoundLabel = new Label("RemoveRate Lower Bound:");
		removeRateLowerBoundText = new DoubleField("0.0");
		removeRateUpperBoundLabel = new Label("RemoveRate Upper Bound:");
		removeRateUpperBoundText = new DoubleField("0.9");
		removeRateStepLabel = new Label("RemoveRate Step Length:");
		removeRateStepText = new DoubleField("0.1");
		experimentCountsLabel = new Label("Number of experiments:");
		experimentCountsText = new IntegerField("100");
		OkButton = new Button("Compute");
		OkButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		CancelButton = new Button("Cancel");
		CancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton
				.addActionListener(new HelpDialog(
						"Minimal time cost attribute reduction BCO with multiple experiments",
						"coser/gui/dialog/tmds/MinTimeCostBCOMutipleDialogHelp.txt"));
		helpButton.setSize(20, 10);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(7, 2));

		centerPanel.add(beeCountsLabel);
		centerPanel.add(beeCountsText);
		centerPanel.add(generationsLabel);
		centerPanel.add(generationsText);
		centerPanel.add(neighborLimitsLabel);
		centerPanel.add(neighborLimitsText);
		centerPanel.add(removeRateLowerBoundLabel);
		centerPanel.add(removeRateLowerBoundText);
		centerPanel.add(removeRateUpperBoundLabel);
		centerPanel.add(removeRateUpperBoundText);
		centerPanel.add(removeRateStepLabel);
		centerPanel.add(removeRateStepText);
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
			String message = "";
			beeCounts = Integer.parseInt(beeCountsText.getText().trim());
			generations = Integer.parseInt(generationsText.getText().trim());
			neighborLimits = Integer.parseInt(neighborLimitsText.getText()
					.trim());
			removeRateLowerBound = Double.parseDouble(removeRateLowerBoundText
					.getText().trim());
			remvoRateUpperBound = Double.parseDouble(removeRateUpperBoundText
					.getText().trim());
			removeRateStepLength = Double.parseDouble(removeRateStepText
					.getText().trim());
			experimentsCounts = Integer.parseInt(experimentCountsText.getText()
					.trim());
			String results = ReductionBCO.batchMinimalTimeCostReductionBCO(
					beeCounts, generations, neighborLimits,
					remvoRateUpperBound, removeRateLowerBound,
					removeRateStepLength, experimentsCounts);

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
		MinTimeCostBCOMutipleDialog.minTimeCostBCOMutipleDialog
				.setVisible(true);
	}

}// MinTestCostACOMutiplen