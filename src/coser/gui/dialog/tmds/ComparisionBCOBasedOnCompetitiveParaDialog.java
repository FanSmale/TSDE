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

import coser.algorithm.ReductionBCOComparison;
import coser.gui.dialog.common.DialogCloser;
import coser.gui.dialog.common.ErrorDialog;
import coser.gui.dialog.common.HelpDialog;
import coser.gui.dialog.common.ProgressDialog;
import coser.gui.guicommon.GUICommon;
import coser.gui.others.DoubleField;
import coser.gui.others.IntegerField;

public class ComparisionBCOBasedOnCompetitiveParaDialog extends Dialog
		implements ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7266634380607735130L;

	public static ComparisionBCOBasedOnCompetitiveParaDialog comparisionBCOBasedOnCompetitiveParaDialog = new ComparisionBCOBasedOnCompetitiveParaDialog();

	/**
	 * The lower bound of bee counts label
	 */
	Label beeCountsLowerBoundLabel;

	/**
	 * The text field of bee counts lower bound
	 */
	IntegerField beeCountsLowerBoundText;

	/**
	 * The panel1
	 */
	Panel panel1;

	/**
	 * The upper bound of bee counts label
	 */
	Label beeCountsUpperBoundLabel;

	/**
	 * The text field of bee counts upper bound
	 */
	IntegerField beeCountsUpperBoundText;

	/**
	 * The panel2
	 */
	Panel panel2;

	/**
	 * The label of bee counts increment
	 */
	Label beeCountsIncrementLabel;

	/**
	 * The text field of bee counts increment
	 */
	IntegerField beeCountsIncrementText;
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
	 * The panel3
	 */
	Panel panel3;

	/**
	 * The label of remove rate lower bound
	 */
	Label removeRateLowerBoundLabel;

	/**
	 * The text field of remove rate lower bound
	 */
	DoubleField removeRateLowerBoundText;

	/**
	 * The panel4
	 */
	Panel panel4;

	/**
	 * The label of remove rate upper bound
	 */
	Label removeRateUpperBoundLabel;

	/**
	 * The text field of remove rate upper bound
	 */
	DoubleField removeRateUpperBoundText;

	/**
	 * The label of remove rate step
	 */
	Label removeRateIncrementLabel;

	/**
	 * The text field of remove rate step
	 */
	DoubleField removeRateIncrementText;

	/**
	 * The label of experiment counts
	 */
	Label experimentCountsLabel;

	/**
	 * The text field of experiment counts
	 */
	IntegerField experimentCountsText;

	/**
	 * The appointed alpha label
	 */
	Label appointedRemoveRateLabel;

	/**
	 * The appointed remove rate text field
	 */
	DoubleField appointedRemoveRateText;

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
	 * The bee counts lower bound
	 */
	int beeCountsLowerBound;

	/**
	 * The bee counts upper bound
	 */
	int beeCountsUpperBound;

	/**
	 * Bee counts increment
	 */
	int beeCountsIncrement;
	/**
	 * The number of running generations.
	 */
	int generations;
	/**
	 * The limited number of searching neighbor solutions.
	 */
	int neighborLimits;

	/**
	 * The lower bound of remove rate value
	 */
	double removeRateLowerBound;

	/**
	 * The upper bound of remove rate value
	 */
	double removeRateUpperBound;

	/**
	 * The remove rate incremental step
	 */
	double removeRateIncrement;

	/**
	 * The appointed remove rate
	 */
	double appointedRemoveRate;

	/**
	 * The experiment counts
	 */
	int experimentCounts;

	/**
	 * The Construct Method
	 */
	public ComparisionBCOBasedOnCompetitiveParaDialog() {

		super(GUICommon.mainFrame, "Minimal time cost attribute reduction BCO",
				true);

		beeCountsLowerBoundLabel = new Label("Bee counts lower bound:");
		beeCountsUpperBoundLabel = new Label("Bee counts upper bound:");
		beeCountsIncrementLabel = new Label("Bee counts increment:");
		generationsLabel = new Label("Running generations:");
		neighborLimitsLabel = new Label("Limits of neighbor solutions:");
		removeRateLowerBoundLabel = new Label("Remove rate lower bound:");
		removeRateUpperBoundLabel = new Label("Remove rate upper bound:");
		removeRateIncrementLabel = new Label("Remove rate increment:");
		appointedRemoveRateLabel = new Label("Appointed Remove rate:");
		experimentCountsLabel = new Label("Experiment counts:");

		beeCountsLowerBoundText = new IntegerField("30");
		beeCountsUpperBoundText = new IntegerField("100");
		beeCountsIncrementText = new IntegerField("10");
		generationsText = new IntegerField("10");
		neighborLimitsText = new IntegerField("10");
		removeRateLowerBoundText = new DoubleField("0.1");
		removeRateUpperBoundText = new DoubleField("0.5");
		removeRateIncrementText = new DoubleField("0.1");
		appointedRemoveRateText = new DoubleField("0.2");
		experimentCountsText = new IntegerField("10");
		// beeCountsLowerBoundText.setColumns(10);
		// beeCountsUpperBoundLabel.setColumns(10);
		// beeCountsIncrementText.setColumns(10);
		// removeRateLowerBoundText.setColumns(10);
		// removeRateUpperBoundText.setColumns(10);
		// removeRateIncrementText.setColumns(10);
		// appointedRemoveRateText.setColumns(10);
		// experimentCountsText.setColumns(10);

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
						"coser/gui/dialog/tmds/ComparisionBCOBasedOnCompetitiveParaDialogHelp.txt"));
		helpButton.setSize(20, 10);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(10, 2));

		centerPanel.add(beeCountsLowerBoundLabel);
		centerPanel.add(beeCountsLowerBoundText);
		centerPanel.add(beeCountsUpperBoundLabel);
		centerPanel.add(beeCountsUpperBoundText);
		centerPanel.add(beeCountsIncrementLabel);
		centerPanel.add(beeCountsIncrementText);
		centerPanel.add(generationsLabel);
		centerPanel.add(generationsText);
		centerPanel.add(neighborLimitsLabel);
		centerPanel.add(neighborLimitsText);
		centerPanel.add(removeRateLowerBoundLabel);
		centerPanel.add(removeRateLowerBoundText);
		centerPanel.add(removeRateUpperBoundLabel);
		centerPanel.add(removeRateUpperBoundText);
		centerPanel.add(removeRateIncrementLabel);
		centerPanel.add(removeRateIncrementText);
		centerPanel.add(appointedRemoveRateLabel);
		centerPanel.add(appointedRemoveRateText);
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

		// The action of OK Button
		// oKButton.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// beeCountsLowerBound = Integer.parseInt(beeCountsLowerBoundText
		// .getText().trim());
		// beeCountsUpperBound = Integer.parseInt(beeCountsUpperBoundLabel
		// .getText().trim());
		// beeCountsIncrement = Integer.parseInt(beeCountsIncrementText
		// .getText().trim());
		// removeRateLowerBound = Integer.parseInt(removeRateLowerBoundText
		// .getText().trim());
		// removeRateUpperBound = Integer.parseInt(removeRateUpperBoundText
		// .getText().trim());
		// removeRateIncrement =
		// Integer.parseInt(removeRateIncrementText.getText()
		// .trim());
		// experimentCounts = Integer.parseInt(experimentCountsText
		// .getText().trim());
		//
		// // 创建比较实验实例对象
		// ReductionACOComparison reductionACOComparison = new
		// ReductionACOComparison(
		// beeCountsLowerBound, beeCountsUpperBound,
		// beeCountsIncrement, removeRateLowerBound, removeRateUpperBound,
		// removeRateIncrement, appointedRemoveRate, experimentCounts);
		//
		// // 调用批量比较实验方法
		// reductionACOComparison.batchComparisonExperiment();
		//
		// }
		// });

		// The action of Cancel Button
		// cancelButton.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// hide();
		// }
		// });

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

			beeCountsLowerBound = Integer.parseInt(beeCountsLowerBoundText
					.getText().trim());
			beeCountsUpperBound = Integer.parseInt(beeCountsUpperBoundText
					.getText().trim());
			beeCountsIncrement = Integer.parseInt(beeCountsIncrementText
					.getText().trim());
			generations = Integer.parseInt(generationsText.getText().trim());
			neighborLimits = Integer.parseInt(neighborLimitsText.getText()
					.trim());
			removeRateLowerBound = Double.parseDouble(removeRateLowerBoundText
					.getText().trim());
			removeRateUpperBound = Double.parseDouble(removeRateUpperBoundText
					.getText().trim());
			removeRateIncrement = Double.parseDouble(removeRateIncrementText
					.getText().trim());
			experimentCounts = Integer.parseInt(experimentCountsText.getText()
					.trim());
			appointedRemoveRate = 0.6;
			// 创建比较实验实例对象
			ReductionBCOComparison reductionBCOComparison = new ReductionBCOComparison(
					beeCountsLowerBound, beeCountsUpperBound,
					beeCountsIncrement, generations, neighborLimits,
					removeRateLowerBound, removeRateUpperBound,
					removeRateIncrement, appointedRemoveRate, experimentCounts);

			// 调用批量比较实验方法
			message = reductionBCOComparison.batchComparisonExperiment();

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
		ComparisionBCOBasedOnCompetitiveParaDialog.comparisionBCOBasedOnCompetitiveParaDialog
				.setVisible(true);
	}

}// Of class ComparisionACO