package coser.gui.dialog.tcnds;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import coser.algorithm.ReductionACOComparison;
import coser.gui.dialog.common.DialogCloser;
import coser.gui.dialog.common.ErrorDialog;
import coser.gui.dialog.common.HelpDialog;
import coser.gui.dialog.common.ProgressDialog;
import coser.gui.guicommon.GUICommon;
import coser.gui.others.DoubleField;
import coser.gui.others.IntegerField;

public class ComparisionACODialog extends Dialog implements ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7266634380607735130L;

	public static ComparisionACODialog comparisionACODialog = new ComparisionACODialog();

	/**
	 * The lower bound of ant counts label
	 */
	Label antCountsLowerBoundLabel;

	/**
	 * The text field of ant counts lower bound
	 */
	IntegerField antCountsLowerBoundText;

	/**
	 * The panel1
	 */
	Panel panel1;

	/**
	 * The upper bound of ant counts label
	 */
	Label antCountsUpperBoundLabel;

	/**
	 * The text field of ant counts upper bound
	 */
	IntegerField antCountsUpperBoundText;

	/**
	 * The panel2
	 */
	Panel panel2;

	/**
	 * The label of ant counts increment
	 */
	Label antCountsIncrementLabel;

	/**
	 * The text field of ant counts increment
	 */
	IntegerField antCountsIncrementText;

	/**
	 * The panel3
	 */
	Panel panel3;

	/**
	 * The label of alpha lower bound
	 */
	Label alphaLowerBoundLabel;

	/**
	 * The text field of alpha lower bound
	 */
	DoubleField alphaLowerBoundText;

	/**
	 * The panel4
	 */
	Panel panel4;

	/**
	 * The label of alpha upper bound
	 */
	Label alphaUpperBoundLabel;

	/**
	 * The text field of alpha upper bound
	 */
	DoubleField alphaUpperBoundText;

	/**
	 * The label of alpha step
	 */
	Label alphaIncrementLabel;

	/**
	 * The text field of alpha step
	 */
	DoubleField alphaIncrementText;

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
	Label appointedAlphaLabel;

	/**
	 * The appointed alpha text field
	 */
	DoubleField appointedAlphaText;

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
	 * The ant counts lower bound
	 */
	int antCountsLowerBound;

	/**
	 * The ant counts upper bound
	 */
	int antCountsUpperBound;

	/**
	 * Ant counts increment
	 */
	int antCountsIncrement;

	/**
	 * The lower bound of alpha value
	 */
	int alphaLowerBound;

	/**
	 * The upper bound of alpha value
	 */
	int alphaUpperBound;

	/**
	 * The alpha incremental step
	 */
	int alphaIncrement;

	/**
	 * The appointed alpha
	 */
	int appointedAlpha;

	/**
	 * The experiment counts
	 */
	int experimentCounts;

	/**
	 * The Construct Method
	 */
	public ComparisionACODialog() {

		super(GUICommon.mainFrame, "Minimal test cost attribute reduction ACO",
				true);

		antCountsLowerBoundLabel = new Label("Ant counts lower bound:");
		antCountsUpperBoundLabel = new Label("Ant counts upper bound:");
		antCountsIncrementLabel = new Label("Ant counts increment:");
		alphaLowerBoundLabel = new Label("Alpha lower bound:");
		alphaUpperBoundLabel = new Label("Alpha upper bound:");
		alphaIncrementLabel = new Label("Alpha increment:");
		appointedAlphaLabel = new Label("Appointed alpha:");
		experimentCountsLabel = new Label("Experiment counts:");

		antCountsLowerBoundText = new IntegerField("100");
		antCountsUpperBoundText = new IntegerField("400");
		antCountsIncrementText = new IntegerField("50");
		alphaLowerBoundText = new DoubleField("1");
		alphaUpperBoundText = new DoubleField("4");
		alphaIncrementText = new DoubleField("1");
		appointedAlphaText = new DoubleField("2");
		experimentCountsText = new IntegerField("100");
		// antCountsLowerBoundText.setColumns(10);
		// antCountsUpperBoundText.setColumns(10);
		// antCountsIncrementText.setColumns(10);
		// alphaLowerBoundText.setColumns(10);
		// alphaUpperBoundText.setColumns(10);
		// alphaIncrementText.setColumns(10);
		// appointedAlphaText.setColumns(10);
		// experimentCountsText.setColumns(10);

		oKButton = new Button("Compute");
		oKButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		cancelButton = new Button("Cancel");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog(
				"Minimal test cost attribute reduction",
				"coser/gui/dialog/tcnds/ComparisionACODialogHelp.txt"));
		helpButton.setSize(20, 10);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(8, 2));

		centerPanel.add(antCountsLowerBoundLabel);
		centerPanel.add(antCountsLowerBoundText);
		centerPanel.add(antCountsUpperBoundLabel);
		centerPanel.add(antCountsUpperBoundText);
		centerPanel.add(antCountsIncrementLabel);
		centerPanel.add(antCountsIncrementText);
		centerPanel.add(alphaLowerBoundLabel);
		centerPanel.add(alphaLowerBoundText);
		centerPanel.add(alphaUpperBoundLabel);
		centerPanel.add(alphaUpperBoundText);
		centerPanel.add(alphaIncrementLabel);
		centerPanel.add(alphaIncrementText);
		centerPanel.add(appointedAlphaLabel);
		centerPanel.add(appointedAlphaText);
		centerPanel.add(experimentCountsLabel);
		centerPanel.add(experimentCountsText);
		centerPanel.add(oKButton);
		centerPanel.add(cancelButton);

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
		// antCountsLowerBound = Integer.parseInt(antCountsLowerBoundText
		// .getText().trim());
		// antCountsUpperBound = Integer.parseInt(antCountsUpperBoundText
		// .getText().trim());
		// antCountsIncrement = Integer.parseInt(antCountsIncrementText
		// .getText().trim());
		// alphaLowerBound = Integer.parseInt(alphaLowerBoundText
		// .getText().trim());
		// alphaUpperBound = Integer.parseInt(alphaUpperBoundText
		// .getText().trim());
		// alphaIncrement = Integer.parseInt(alphaIncrementText.getText()
		// .trim());
		// experimentCounts = Integer.parseInt(experimentCountsText
		// .getText().trim());
		//
		// // 创建比较实验实例对象
		// ReductionACOComparison reductionACOComparison = new
		// ReductionACOComparison(
		// antCountsLowerBound, antCountsUpperBound,
		// antCountsIncrement, alphaLowerBound, alphaUpperBound,
		// alphaIncrement, appointedAlpha, experimentCounts);
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

			antCountsLowerBound = Integer.parseInt(antCountsLowerBoundText
					.getText().trim());
			antCountsUpperBound = Integer.parseInt(antCountsUpperBoundText
					.getText().trim());
			antCountsIncrement = Integer.parseInt(antCountsIncrementText
					.getText().trim());
			alphaLowerBound = Integer.parseInt(alphaLowerBoundText.getText()
					.trim());
			alphaUpperBound = Integer.parseInt(alphaUpperBoundText.getText()
					.trim());
			alphaIncrement = Integer.parseInt(alphaIncrementText.getText()
					.trim());
			experimentCounts = Integer.parseInt(experimentCountsText.getText()
					.trim());

			// 创建比较实验实例对象
			ReductionACOComparison reductionACOComparison = new ReductionACOComparison(
					antCountsLowerBound, antCountsUpperBound,
					antCountsIncrement, alphaLowerBound, alphaUpperBound,
					alphaIncrement, appointedAlpha, experimentCounts);

			// 调用批量比较实验方法
			message = reductionACOComparison.batchComparisonExperiment();

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
		ComparisionACODialog.comparisionACODialog.setVisible(true);
	}

}// Of class ComparisionACO