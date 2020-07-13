package coser.gui.dialog.tmds;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import coser.algorithm.ReductionABCForGen;
import coser.gui.dialog.common.DialogCloser;
import coser.gui.dialog.common.ErrorDialog;
import coser.gui.dialog.common.HelpDialog;
import coser.gui.dialog.common.ProgressDialog;
import coser.gui.guicommon.GUICommon;
import coser.gui.others.DoubleField;
import coser.gui.others.IntegerField;

public class MinTimeCostABCMutipleForIterDialog extends Dialog implements
		ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -512833090140317459L;

	public static MinTimeCostABCMutipleForIterDialog minTimeCostABCMutipleForIterDialog = new MinTimeCostABCMutipleForIterDialog();

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
	Label iterLowerBoundLabel;

	/**
	 * The text field of limit lower bound
	 */
	IntegerField iterLowerBoundText;

	/**
	 * The label of limit upper bound
	 */
	Label iterUpperBoundLabel;
	/**
	 * The text field of limit upper bound
	 */
	IntegerField iterUpperBoundText;
	/**
	 * The limit step label
	 */
	Label iterStepLabel;
	/**
	 * The limit step text
	 */
	IntegerField iterStepText;
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
	Label limitLabel;
	/**
	 * The maximal generations text field
	 */
	IntegerField limitText;

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
	int iterLowerBound;
	/**
	 * The limit upper bound
	 */
	int iterUpperBound;
	/**
	 * The limit step length
	 */
	int iterStepLength;
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
	int limit;
	/**
	 * The number of experiments
	 */
	int experimentCounts;

	/**
	 * The Construct Method
	 */
	public MinTimeCostABCMutipleForIterDialog() {

		super(
				GUICommon.mainFrame,
				"Minimal time cost attribute reduction ABC with multiple experiments on iteration",
				true);

		foodNumberLabel = new Label("Food number");
		foodNumberText = new IntegerField("20");
		numberOfPopulationLabel = new Label("Size of population");
		numberOfPopulationText = new IntegerField("40");
		iterLowerBoundLabel = new Label("Iteration Lower Bound:");
		iterLowerBoundText = new IntegerField("5");
		iterUpperBoundLabel = new Label("Iteration Upper Bound:");
		iterUpperBoundText = new IntegerField("50");
		iterStepLabel = new Label("Iteration Step Length:");
		iterStepText = new IntegerField("5");
		cPBestLabel = new Label("Probability for personal best:");
		cPBestText = new DoubleField("0.4");
		cGBestLabel = new Label("Probability for global best:");
		cGBestText = new DoubleField("0.9");
		limitLabel = new Label("Limited neighbors:");
		limitText = new IntegerField("110");
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
						"Minimal time cost attribute reduction ABC with multiple experiments on iteration",
						"coser/gui/dialog/tmds/MinTimeCostABCMutipleForIterDialogHelp.txt"));
		helpButton.setSize(20, 10);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(9, 2));

		centerPanel.add(numberOfPopulationLabel);
		centerPanel.add(numberOfPopulationText);
		centerPanel.add(foodNumberLabel);
		centerPanel.add(foodNumberText);
		centerPanel.add(iterLowerBoundLabel);
		centerPanel.add(iterLowerBoundText);
		centerPanel.add(iterUpperBoundLabel);
		centerPanel.add(iterUpperBoundText);
		centerPanel.add(iterStepLabel);
		centerPanel.add(iterStepText);
		centerPanel.add(cPBestLabel);
		centerPanel.add(cPBestText);
		centerPanel.add(cGBestLabel);
		centerPanel.add(cGBestText);
		centerPanel.add(limitLabel);
		centerPanel.add(limitText);
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
			iterLowerBound = Integer.parseInt(iterLowerBoundText.getText()
					.trim());
			iterUpperBound = Integer.parseInt(iterUpperBoundText.getText()
					.trim());
			iterStepLength = Integer.parseInt(iterStepText.getText().trim());
			cPBest = Double.parseDouble(cPBestText.getText().trim());
			cGBest = Double.parseDouble(cGBestText.getText().trim());
			limit = Integer.parseInt(limitText.getText().trim());
			experimentCounts = Integer.parseInt(experimentCountsText.getText()
					.trim());
			String results = ReductionABCForGen
					.batchMinimalTimeCostReductionABC(numberOfPopulation,
							foodNumber, iterUpperBound, iterLowerBound,
							iterStepLength, cPBest, cGBest, limit,
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
		MinTimeCostABCMutipleForIterDialog.minTimeCostABCMutipleForIterDialog
				.setVisible(true);
	}

}// MinTestCostACOMutiplen