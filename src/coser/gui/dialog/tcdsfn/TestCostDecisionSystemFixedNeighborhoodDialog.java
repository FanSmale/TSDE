package coser.gui.dialog.tcdsfn;

import java.awt.*;
import java.awt.event.*;
import coser.datamodel.decisionsystem.*;
import coser.algorithm.*;
import coser.project.*;

import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;
import java.text.*;

/**
 * Project: The cost-sensitive neighborhood rough sets project.
 * <p>
 * Summary: Specify the related parameters.
 * <p>
 * Author: <b>Hong Zhao</b> fhq_xa@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Half done. <br>
 * Written time: March 12, 2011. <br>
 * Last modify time: April 21, 2012.
 */

public class TestCostDecisionSystemFixedNeighborhoodDialog extends Dialog
		implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3975624089095956542L;

	public static TestCostDecisionSystemFixedNeighborhoodDialog testCostDecisionSystemFixedNeighborhoodDialog = new TestCostDecisionSystemFixedNeighborhoodDialog();

	/**
	 * The lower bound of delta.
	 */
	private DoubleField deltalowerBoundField;

	/**
	 * The upper bound of delta.
	 */
	private DoubleField deltaupperBoundField;

	/**
	 * The step length of delta.
	 */
	private DoubleField deltaStepLengthField;

	/**
	 * The efc_ctrl.
	 */
	private DoubleField efc_ctrlField;

	/**
	 * The number of tests.
	 */
	private IntegerField numofTestField;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private TestCostDecisionSystemFixedNeighborhoodDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Test-cost settings", true);
		deltalowerBoundField = new DoubleField("0.005");
		deltaupperBoundField = new DoubleField("0.03");
		deltaStepLengthField = new DoubleField("0.003");
		efc_ctrlField = new DoubleField("0.01");
		numofTestField = new IntegerField("10");
		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(6, 2));
		centerPanel.add(new Label("The number of test: "));
		centerPanel.add(numofTestField);
		centerPanel.add(new Label("deltalowerBoundField: "));
		centerPanel.add(deltalowerBoundField);
		centerPanel.add(new Label("deltaupperBoundField: "));
		centerPanel.add(deltaupperBoundField);
		centerPanel.add(new Label("deltaStepLengthField: "));
		centerPanel.add(deltaStepLengthField);
		centerPanel.add(new Label("efc_ctrl:(efc_ctrl=0 without testcost) "));
		centerPanel.add(efc_ctrlField);

		Button okButton = new Button(" OK ");
		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton
				.addActionListener(new HelpDialog("Test-cost distribution",
						"coser/gui/dialog/tcdsfn/TestCostDecisionSystemFixedNeighborhoodDialogHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(okButton);
		okPanel.add(cancelButton);
		okPanel.add(helpButton);

		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, okPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(200, 200);
		setSize(300, 260);
		addWindowListener(dialogCloser);
		setVisible(false);
	}// Of constructor

	/**
	 *************************** 
	 * Test cost decision system neighborhood dialog.
	 *************************** 
	 */
	public void actionPerformed(ActionEvent ae) {
		setVisible(false);
		ProgressDialog.progressDialog
				.setMessageAndShow("Please wait a few seconds. Setting ... \r\n");

		try {
			// Use another name to make statements shorter.
			TestCostDecisionSystemFixedNeighborhood currentSystem = CoserProject.currentProject.currentTcDsFn;
			NumberFormat numFormat = NumberFormat.getNumberInstance();
			numFormat.setMaximumFractionDigits(5);

			double deltalowerBound = Double.parseDouble(deltalowerBoundField
					.getText().trim());
			double deltaupperBound = Double.parseDouble(deltaupperBoundField
					.getText().trim());
			double deltaStepLength = Double.parseDouble(deltaStepLengthField
					.getText().trim());
			double efc_ctrl = Double
					.parseDouble(efc_ctrlField.getText().trim());
			int numofTest = Integer.parseInt(numofTestField.getText().trim());
			// double []efficiency = null;
			boolean[] feature = null;
			double right = 0;
			double maxExceed = 0;
			double aveExceed = 0;
			double sumOptimalTestCost = 0;
			double sumCurrentTestCost = 0;
			String messageInformation = "";
			String messageDetail = "";
			FixedNeighborhoodReduction nr;
			double delta = deltalowerBound;
			messageInformation += "Source file:"
					+ currentSystem.getArffFilename();
			messageDetail = "delta\t Finding optimal factor\t Maximal exceeding factor\t Average exceeding factor\r\n";
			efc_ctrl = Double.parseDouble(efc_ctrlField.getText().trim());
			double temp;
			while (delta <= deltaupperBound) {
				messageDetail += numFormat.format(delta) + "\t\t";
				right = 0;
				maxExceed = 0;
				aveExceed = 0;
				for (int i = 0; i < numofTest; i++) {
					// Reduction with cost
					nr = new FixedNeighborhoodReduction(delta, efc_ctrl, 1);
					feature = nr.getFeature();
					// efficiency = nr.getEfficiency();
					sumCurrentTestCost = currentSystem.computeTestCost(feature);
					sumOptimalTestCost = currentSystem
							.computeOptimalReductTestCostFNBacktrack();
					temp = (sumCurrentTestCost - sumOptimalTestCost)
							/ sumOptimalTestCost;
					aveExceed += temp;
					if (maxExceed < temp)
						maxExceed = temp;
					if (Math.abs(sumOptimalTestCost - sumCurrentTestCost) < 1e-6)
						right++;
				}// for

				messageDetail += numFormat.format(right / numofTest * 100)
						+ "%\t\t\t";
				messageDetail += numFormat.format(maxExceed) + "\t\t\t\t";
				messageDetail += numFormat.format(aveExceed / numofTest)
						+ "\r\n";
				if (deltaStepLength == 0)
					break;
				delta += deltaStepLength;
			}// while
			messageInformation += "\r\nThe number of test is " + numofTest
					+ "\r\n\r\n";
			ProgressDialog.progressDialog.setMessageAndShow(messageInformation
					+ messageDetail);
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			return;
		}// Of try
	}// Of actionPerformed

}// Of class TestCostDecisionSystemFixedNeighborhoodDialog
