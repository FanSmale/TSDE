package coser.gui.dialog.tcdsfn;

import java.awt.*;
import java.awt.event.*;
import coser.datamodel.decisionsystem.*;
import coser.project.*;

import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Specify reduction related parameters.
 * <p>
 * Author: <b>Hong Zhao</b> hongzhaocn@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organizaion: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Almost Done.<br>
 * Written time: August 26, 2011. <br>
 * Last modify time: April 21, 2012
 */
public class FixedNeighborhoodAllReductsDialog extends Dialog implements
		ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1595292667549207174L;

	public static FixedNeighborhoodAllReductsDialog fixedNeighborhoodAllReductsDialog = new FixedNeighborhoodAllReductsDialog();

	/**
	 * The threshold granule range of neighborhood.
	 */
	private DoubleField granuleField;

	/**
	 * The threshold steplength of granule range .
	 */
	private DoubleField steplengthField;

	/**
	 * The number of tests with different granule ranges.
	 */
	private IntegerField numoftestsField;

	/**
	 * Ok Button.
	 */
	private Button okButton;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */

	private FixedNeighborhoodAllReductsDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Neighborhood All Reducts", true);
		granuleField = new DoubleField("0.005");
		steplengthField = new DoubleField("0.003");
		numoftestsField = new IntegerField("1");
		Panel optionPanel = new Panel();
		optionPanel.setLayout(new GridLayout(3, 2));
		optionPanel.add(new Label("The granule range"));
		optionPanel.add(granuleField);
		optionPanel.add(new Label("The step length "));
		optionPanel.add(steplengthField);
		optionPanel.add(new Label("number of tests "));
		optionPanel.add(numoftestsField);

		okButton = new Button("   OK  ");
		okButton.setSize(20, 10);
		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button("  Help ");
		helpButton.setSize(20, 10);
		helpButton
				.addActionListener(new HelpDialog("New project",
						"coser/gui/dialog/tcdsfn/FixedNeighborhoodReductionDialogHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(okButton);
		okPanel.add(cancelButton);
		okPanel.add(helpButton);

		setLayout(new BorderLayout());
		add("Center", optionPanel);
		add("South", okPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(160, 160);
		setSize(280, 180);
		addWindowListener(dialogCloser);
		setVisible(false);
	}// Of constructor

	/**
	 *************************** 
	 * Read the arff file.
	 *************************** 
	 */
	public void actionPerformed(ActionEvent ae) {

		double granule = Double.parseDouble(granuleField.getText().trim());
		double steplength = Double
				.parseDouble(steplengthField.getText().trim());
		int numoftests = Integer.parseInt(numoftestsField.getText().trim());
		String message = "";
		if (ae.getSource() == okButton) {
			setVisible(false);
			try {
				TestCostDecisionSystemFixedNeighborhood currentSystem = CoserProject.currentProject.currentTcDsFn;
				// double granules[] = new double[currentSystem.numAttributes()
				// - 1];
				for (int k = 0; k < numoftests; k++) {
					message += "\r\nCase: " + (k + 1) + "\r\nGranule range is "
							+ granule;
					message += "\r\nReduction \r\n";
					currentSystem.setGranuleRangeArray(granule);
					currentSystem.computeAllReductsBacktrack();
					message += currentSystem.allReductsToString();
					granule += steplength;
				}// of for
				ProgressDialog.progressDialog.setMessageAndShow(message);
			} catch (Exception ee) {
				ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
				ProgressDialog.progressDialog.setVisible(false);
				return;
			}// Of try
		}// if
	}// Of actionPerformed

}// Of class FixedNeighborhoodAllReductsDialog
