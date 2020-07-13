package coser.gui.dialog.tmds;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import coser.algorithm.ReductionABCForLimit;
import coser.gui.dialog.common.DialogCloser;
import coser.gui.dialog.common.ErrorDialog;
import coser.gui.dialog.common.HelpDialog;
import coser.gui.dialog.common.ProgressDialog;
import coser.gui.guicommon.GUICommon;
import coser.gui.others.DoubleField;
import coser.gui.others.IntegerField;

public class MinTimeCostABCMutipleForLimitDialog extends Dialog implements
		ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -512833090140317459L;

	public static MinTimeCostABCMutipleForLimitDialog minTimeCostABCMutipleForLimitDialog = new MinTimeCostABCMutipleForLimitDialog();

	/**
	 * The frame
	 */
	// JFrame frame;

	/**
	 * The label of the number of population
	 */
	Label numberOfPopulationLabel;
	/**
	 * The text field of the number of population
	 */
	IntegerField numberOfPopulationText;
	/**
	 * The label of food number
	 */
	Label foodNumberLabel;
	/**
	 * The text field of food number
	 */
	IntegerField foodNumberText;
	/**
	 * The label of limit lower bound
	 */
	Label limitLowerBoundLabel;

	/**
	 * The text field of limit lower bound
	 */
	IntegerField limitLowerBoundText;

	/**
	 * The label of limit upper bound
	 */
	Label limitUpperBoundLabel;
	/**
	 * The text field of limit upper bound
	 */
	IntegerField limitUpperBoundText;
	/**
	 * The limit step label
	 */
	Label limitStepLabel;
	/**
	 * The limit step text
	 */
	IntegerField limitStepText;
	/**
	 * The probability for the personal best label
	 */
	Label cPBestLabel;

	/**
	 * The probability for the personal best text field
	 */
	DoubleField cPBestText;

	/**
	 * The probability for the global best label
	 */
	Label cGBestLabel;

	/**
	 * The probability for the global best text field
	 */
	DoubleField cGBestText;

	/**
	 * The maximal generations label
	 */
	Label maxCyclesLabel;
	/**
	 * The maximal generations text field
	 */
	IntegerField maxCyclesText;

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
	 * The number of population.
	 */
	int numberOfPopulation;
	/**
	 * The number of food.
	 */
	int foodNumber;
	/**
	 * The limit lower bound
	 */
	int limitLowerBound;
	/**
	 * The limit upper bound
	 */
	int limitUpperBound;
	/**
	 * The limit step length
	 */
	int limitStepLength;
	/**
	 * The probability for the personal best
	 */
	double cPBest;
	/**
	 * The probability for the global best
	 */
	double cGBest;
	/**
	 * The maximal cycles
	 */
	int maxCycles;
	/**
	 * The number of experiments
	 */
	int experimentCounts;

	/**
	 * The Construct Method
	 */
	public MinTimeCostABCMutipleForLimitDialog() {

		super(
				GUICommon.mainFrame,
				"Minimal time cost attribute reduction ABC with multiple experiments on limit",
				true);

		foodNumberLabel = new Label("Food number");
		foodNumberText = new IntegerField("20");
		numberOfPopulationLabel = new Label("Size of population");
		numberOfPopulationText = new IntegerField("40");
		limitLowerBoundLabel = new Label("Limit Lower Bound:");
		limitLowerBoundText = new IntegerField("20");
		limitUpperBoundLabel = new Label("Limit Upper Bound:");
		limitUpperBoundText = new IntegerField("120");
		limitStepLabel = new Label("Limit Step Length:");
		limitStepText = new IntegerField("10");
		cPBestLabel = new Label("Probability for personal best:");
		cPBestText = new DoubleField("0.3");
		cGBestLabel = new Label("Probability for global best:");
		cGBestText = new DoubleField("0.9");
		maxCyclesLabel = new Label("maximal generations:");
		maxCyclesText = new IntegerField("300");
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
						"Minimal time cost attribute reduction ABC with multiple experiments",
						"coser/gui/dialog/tmds/MinTimeCostABCMutipleForLimitDialogHelp.txt"));
		helpButton.setSize(20, 10);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(9, 2));

		centerPanel.add(numberOfPopulationLabel);
		centerPanel.add(numberOfPopulationText);
		centerPanel.add(foodNumberLabel);
		centerPanel.add(foodNumberText);
		centerPanel.add(limitLowerBoundLabel);
		centerPanel.add(limitLowerBoundText);
		centerPanel.add(limitUpperBoundLabel);
		centerPanel.add(limitUpperBoundText);
		centerPanel.add(limitStepLabel);
		centerPanel.add(limitStepText);
		centerPanel.add(cPBestLabel);
		centerPanel.add(cPBestText);
		centerPanel.add(cGBestLabel);
		centerPanel.add(cGBestText);
		centerPanel.add(maxCyclesLabel);
		centerPanel.add(maxCyclesText);
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
			numberOfPopulation = Integer.parseInt(numberOfPopulationText
					.getText().trim());
			foodNumber = Integer.parseInt(foodNumberText.getText().trim());
			limitLowerBound = Integer.parseInt(limitLowerBoundText.getText()
					.trim());
			limitUpperBound = Integer.parseInt(limitUpperBoundText.getText()
					.trim());
			limitStepLength = Integer.parseInt(limitStepText.getText().trim());
			cPBest = Double.parseDouble(cPBestText.getText().trim());
			cGBest = Double.parseDouble(cGBestText.getText().trim());
			maxCycles = Integer.parseInt(maxCyclesText.getText().trim());
			experimentCounts = Integer.parseInt(experimentCountsText.getText()
					.trim());
			String results = ReductionABCForLimit
					.batchMinimalTimeCostReductionABC(numberOfPopulation,
							foodNumber, limitUpperBound, limitLowerBound,
							limitStepLength, cPBest, cGBest, maxCycles,
							experimentCounts);

			System.out.println(results);// output

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
		MinTimeCostABCMutipleForLimitDialog.minTimeCostABCMutipleForLimitDialog
				.setVisible(true);
	}

}// MinTestCostACOMutiplen