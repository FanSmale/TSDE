package coser.gui.dialog.tmds;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import coser.algorithm.ReductionBCOGN;
import coser.gui.dialog.common.DialogCloser;
import coser.gui.dialog.common.ErrorDialog;
import coser.gui.dialog.common.HelpDialog;
import coser.gui.dialog.common.ProgressDialog;
import coser.gui.guicommon.GUICommon;
import coser.gui.others.DoubleField;
import coser.gui.others.IntegerField;

public class MinTimeCostBCOGNMutipleDialog extends Dialog implements
		ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -512833090140317459L;

	public static MinTimeCostBCOGNMutipleDialog minTimeCostBCOGNMutipleDialog = new MinTimeCostBCOGNMutipleDialog();

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
	// Label generationsLabel;
	/**
	 * The text field of generations
	 */
	// IntegerField generationsText;
	/**
	 * The limited number of searching neighbor solutions label
	 */
	// Label neighborLimitsLabel;

	/**
	 * The limited number of searching neighbor solutions text field
	 */
	// IntegerField neighborLimitsText;

	/**
	 * The label of neighbors' lower bound
	 */
	Label generationsLowerBoundLabel;

	/**
	 * The text field of neighbors' lower bound
	 */
	IntegerField generationsLowerBoundText;

	/**
	 * The label of neighbors' upper bound
	 */
	Label generationsUpperBoundLabel;

	/**
	 * The text field of neighbors' upper bound
	 */
	IntegerField generationsUpperBoundText;

	/**
	 * The neighbors' step label
	 */
	Label generationsStepLabel;

	/**
	 * The neighbors' step text
	 */
	IntegerField generationsStepText;

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
	int neighbors;
	/**
	 * The rate of removing worse solutions.
	 */
	double removeRate;
	/**
	 * The limited neighbors' lower bound
	 */
	int generationsLowerBound;

	/**
	 * The limited neighbors' upper bound
	 */
	int generationsUpperBound;

	/**
	 * The neighbors' step length
	 */
	int generationsStepLength;

	/**
	 * The number of experiments
	 */
	int experimentsCounts;

	/**
	 * The Construct Method
	 */
	public MinTimeCostBCOGNMutipleDialog() {

		super(
				GUICommon.mainFrame,
				"Minimal time cost attribute reduction BCO as to variable neighbors with multiple experiments",
				true);

		beeCountsLabel = new Label("Bee counts");
		beeCountsText = new IntegerField("30");
		// generationsLabel = new Label("Generations");
		// generationsText = new IntegerField("15");
		// neighborLimitsLabel = new Label("Neighbor counts:");
		// neighborLimitsText = new IntegerField("30");
		generationsLowerBoundLabel = new Label("Generations Lower Bound:");
		generationsLowerBoundText = new IntegerField("15");
		generationsUpperBoundLabel = new Label("Generations Upper Bound:");
		generationsUpperBoundText = new IntegerField("30");
		generationsStepLabel = new Label("Generations Step Length:");
		generationsStepText = new IntegerField("5");
		experimentCountsLabel = new Label("Number of experiments:");
		experimentCountsText = new IntegerField("10");
		OkButton = new Button("Compute");
		OkButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		CancelButton = new Button("Cancel");
		CancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton
				.addActionListener(new HelpDialog(
						"Minimal time cost attribute reduction BCO as to variable neighbors with multiple experiments",
						"coser/gui/dialog/tmds/MinTimeCostBCOMutipleDialogHelp.txt"));
		helpButton.setSize(20, 10);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(5, 2));

		centerPanel.add(beeCountsLabel);
		centerPanel.add(beeCountsText);
		// centerPanel.add(generationsLabel);
		// centerPanel.add(generationsText);
		// centerPanel.add(neighborLimitsLabel);
		// centerPanel.add(neighborLimitsText);
		centerPanel.add(generationsLowerBoundLabel);
		centerPanel.add(generationsLowerBoundText);
		centerPanel.add(generationsUpperBoundLabel);
		centerPanel.add(generationsUpperBoundText);
		centerPanel.add(generationsStepLabel);
		centerPanel.add(generationsStepText);
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
			// generations = Integer.parseInt(generationsText.getText().trim());
			// neighborLimits = Integer.parseInt(neighborLimitsText.getText()
			// .trim());
			generationsLowerBound = Integer.parseInt(generationsLowerBoundText
					.getText().trim());
			generationsUpperBound = Integer.parseInt(generationsUpperBoundText
					.getText().trim());
			generationsStepLength = Integer.parseInt(generationsStepText
					.getText().trim());
			experimentsCounts = Integer.parseInt(experimentCountsText.getText()
					.trim());
			String results = ReductionBCOGN
					.batchMinimalTimeCostReductionBCOIter(beeCounts,
							generationsUpperBound, generationsLowerBound,
							generationsStepLength, experimentsCounts);

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
		MinTimeCostBCOGNMutipleDialog.minTimeCostBCOGNMutipleDialog
				.setVisible(true);
	}

}// MinTestCostACOMutiplen