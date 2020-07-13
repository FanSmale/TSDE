package coser.gui.dialog.tmds;

//import java.awt.BorderLayout;
//import java.awt.Button;
//import java.awt.Dialog;
//import java.awt.GridLayout;
//import java.awt.Label;
//import java.awt.Panel;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import coser.algorithm.ReductionABCComparison;
import coser.gui.dialog.common.DialogCloser;
import coser.gui.dialog.common.ErrorDialog;
import coser.gui.dialog.common.HelpDialog;
import coser.gui.dialog.common.ProgressDialog;
import coser.gui.guicommon.GUICommon;
import coser.gui.others.DoubleField;
import coser.gui.others.IntegerField;

public class ComparisionABCBasedOnCompetitiveParaDialog extends Dialog
		implements ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7266634380607735130L;

	public static ComparisionABCBasedOnCompetitiveParaDialog comparisionABCBasedOnCompetitiveParaDialog = new ComparisionABCBasedOnCompetitiveParaDialog();

	/**
	 * The lower bound of food number label
	 */
	Label foodNumberLowerBoundLabel;
	/**
	 * The text field of food number lower bound
	 */
	IntegerField foodNumberLowerBoundText;

	/**
	 * The panel1
	 */
	Panel panel1;

	/**
	 * The upper bound of food number label
	 */
	Label foodNumberUpperBoundLabel;
	/**
	 * The text field of food number upper bound
	 */
	IntegerField foodNumberUpperBoundText;

	/**
	 * The panel2
	 */
	Panel panel2;

	/**
	 * The label of food number increment
	 */
	Label foodNumberIncrementLabel;
	/**
	 * The text field of food number increment
	 */
	IntegerField foodNumberIncrementText;

	/**
	 * The panel3
	 */
	Panel panel3;

	/**
	 * The label of limit lower bound
	 */
	Label limitLowerBoundLabel;
	/**
	 * The text field of limit lower bound
	 */
	IntegerField limitLowerBoundText;

	/**
	 * The panel4
	 */
	Panel panel4;

	/**
	 * The label of limit upper bound
	 */
	Label limitUpperBoundLabel;
	/**
	 * The text field of limit upper bound
	 */
	IntegerField limitUpperBoundText;

	/**
	 * The label of limit step
	 */
	Label limitIncrementLabel;
	/**
	 * The text field of limit step
	 */
	IntegerField limitIncrementText;
	/**
	 * The appointed limit label
	 */
	Label appointedLimitLabel;
	/**
	 * The appointed limit text field
	 */
	IntegerField appointedLimitText;
	/**
	 * The label of the probability of selecting the local best solution
	 */
	Label cPBestLabel;
	/**
	 * The text field of the probability of selecting the local best solution
	 */
	DoubleField cPBestText;
	/**
	 * The label of the probability of selecting the global best solution
	 */
	Label cGBestLabel;
	/**
	 * The text field of the probability of selecting the global best solution
	 */
	DoubleField cGBestText;
	/**
	 * The label of the maximal generation
	 */
	Label maxCyclesLabel;
	/**
	 * The text field of the maximal generation
	 */
	IntegerField maxCyclesText;
	/**
	 * The label of experiment counts
	 */
	Label experimentCountsLabel;
	/**
	 * The text field of experiment counts
	 */
	IntegerField experimentCountsText;

	/**
	 * OK button
	 */
	Button oKButton;
	/**
	 * Cancel button
	 */
	Button cancelButton;
	/**
	 * The help button
	 */
	Button helpButton;

	/**
	 * The food number lower bound
	 */
	int foodNumberLowerBound;

	/**
	 * The food number upper bound
	 */
	int foodNumberUpperBound;

	/**
	 * The food number increment
	 */
	int foodNumberIncrement;
	/**
	 * The lower bound of limit value
	 */
	int limitLowerBound;

	/**
	 * The upper bound of limit value
	 */
	int limitUpperBound;

	/**
	 * The limit incremental step
	 */
	int limitIncrement;

	/**
	 * The appointed limit
	 */
	int appointedLimit;
	/**
	 * The probability of selecting the local best solution.
	 */
	double cPBest;
	/**
	 * The probability of selecting the global best solution.
	 */
	double cGBest;
	/**
	 * The maximal generation.
	 */
	int maxCycles;
	/**
	 * The experiment counts
	 */
	int experimentCounts;

	/**
	 * The Construct Method
	 */
	public ComparisionABCBasedOnCompetitiveParaDialog() {

		super(GUICommon.mainFrame, "Minimal time cost attribute reduction ABC",
				true);
		foodNumberLowerBoundLabel = new Label("Food number lower bound:");
		foodNumberUpperBoundLabel = new Label("Food number upper bound:");
		foodNumberIncrementLabel = new Label("Food number increment:");
		limitLowerBoundLabel = new Label("Limit lower bound:");
		limitUpperBoundLabel = new Label("Limit upper bound:");
		limitIncrementLabel = new Label("Limit increment:");
		appointedLimitLabel = new Label("Appointed limit:");
		cPBestLabel = new Label("The probability for selecting local optima:");
		cGBestLabel = new Label("The probability for selecting global optima:");
		maxCyclesLabel = new Label("The maximal generation:");
		experimentCountsLabel = new Label("Experiment counts:");

		foodNumberLowerBoundText = new IntegerField("20");
		foodNumberUpperBoundText = new IntegerField("80");
		foodNumberIncrementText = new IntegerField("10");
		limitLowerBoundText = new IntegerField("20");
		limitUpperBoundText = new IntegerField("120");
		limitIncrementText = new IntegerField("10");
		appointedLimitText = new IntegerField("100");
		cPBestText = new DoubleField("0.4");
		cGBestText = new DoubleField("0.9");
		maxCyclesText = new IntegerField("30");
		experimentCountsText = new IntegerField("100");

		oKButton = new Button("Compute");
		oKButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		cancelButton = new Button("Cancel");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton
				.addActionListener(new HelpDialog(
						"Minimal time cost attribute reduction based on competitive approach",
						"coser/gui/dialog/tmds/ComparisionABCBasedOnCompetitiveParaDialogHelp.txt"));
		helpButton.setSize(20, 10);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(11, 2));

		centerPanel.add(foodNumberLowerBoundLabel);
		centerPanel.add(foodNumberLowerBoundText);
		centerPanel.add(foodNumberUpperBoundLabel);
		centerPanel.add(foodNumberUpperBoundText);
		centerPanel.add(foodNumberIncrementLabel);
		centerPanel.add(foodNumberIncrementText);
		centerPanel.add(limitLowerBoundLabel);
		centerPanel.add(limitLowerBoundText);
		centerPanel.add(limitUpperBoundLabel);
		centerPanel.add(limitUpperBoundText);
		centerPanel.add(limitIncrementLabel);
		centerPanel.add(limitIncrementText);
		centerPanel.add(appointedLimitLabel);
		centerPanel.add(appointedLimitText);
		centerPanel.add(cPBestLabel);
		centerPanel.add(cPBestText);
		centerPanel.add(cGBestLabel);
		centerPanel.add(cGBestText);
		centerPanel.add(maxCyclesLabel);
		centerPanel.add(maxCyclesText);
		centerPanel.add(experimentCountsLabel);
		centerPanel.add(experimentCountsText);

		Panel okPanel = new Panel();
		okPanel.add(oKButton);
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
	}// Of Construct Method

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

		String message = "";

		try {
			foodNumberLowerBound = Integer.parseInt(foodNumberLowerBoundText
					.getText().trim());
			foodNumberUpperBound = Integer.parseInt(foodNumberUpperBoundText
					.getText().trim());
			foodNumberIncrement = Integer.parseInt(foodNumberIncrementText
					.getText().trim());
			limitLowerBound = Integer.parseInt(limitLowerBoundText.getText()
					.trim());
			limitUpperBound = Integer.parseInt(limitUpperBoundText.getText()
					.trim());
			limitIncrement = Integer.parseInt(limitIncrementText.getText()
					.trim());
			appointedLimit = 120;
			cPBest = Double.parseDouble(cPBestText.getText().trim());
			cGBest = Double.parseDouble(cGBestText.getText().trim());
			maxCycles = Integer.parseInt(maxCyclesText.getText().trim());
			experimentCounts = Integer.parseInt(experimentCountsText.getText()
					.trim());
			// 创建比较实验实例对象
			ReductionABCComparison reductionABCComparison = new ReductionABCComparison(
					foodNumberLowerBound, foodNumberUpperBound,
					foodNumberIncrement, limitLowerBound, limitUpperBound,
					limitIncrement, appointedLimit, cPBest, cGBest, maxCycles,
					experimentCounts);

			// 调用批量比较实验方法
			message = reductionABCComparison.batchComparisonExperiment();

			message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com\r\n"
					+ message;
			ProgressDialog.progressDialog.setMessageAndShow(message);
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
			ee.printStackTrace();
			return;
		}

	}// actionPerformed

	/**
	 * 主方法，用于单元测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ComparisionABCBasedOnCompetitiveParaDialog.comparisionABCBasedOnCompetitiveParaDialog
				.setVisible(true);
	}

}// Of class ComparisionACO