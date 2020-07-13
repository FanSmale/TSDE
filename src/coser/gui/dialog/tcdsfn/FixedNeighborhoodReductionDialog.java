package coser.gui.dialog.tcdsfn;

import java.awt.*;
import java.awt.event.*;
import coser.algorithm.*;

import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Specify reduction related parameters.
 * <p>
 * Author: <b>Hong Zhao</b> fhq_xa@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Done.<br>
 * Written time: March 12, 2011. <br>
 * Last modify time: April 21, 2012.
 */
public class FixedNeighborhoodReductionDialog extends Dialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1295845172505402675L;

	public static FixedNeighborhoodReductionDialog fixedNeighborhoodReductionDialog = new FixedNeighborhoodReductionDialog();

	/**
	 * The threshold delta of neighborhood.
	 */
	private DoubleField deltaField;

	/**
	 * The threshold efc_ctrl of neighborhood .
	 */
	private DoubleField efc_ctrlField;

	/**
	 * Ok Button.
	 */
	private Button okButton;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private FixedNeighborhoodReductionDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Neighborhood Reduction", true);
		deltaField = new DoubleField("0.005");
		efc_ctrlField = new DoubleField("0.001");
		Panel optionPanel = new Panel();
		optionPanel.setLayout(new GridLayout(3, 2));
		optionPanel.add(new Label("The delta"));
		optionPanel.add(deltaField);
		optionPanel.add(new Label("The efc_ctrl "));
		optionPanel.add(efc_ctrlField);

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
		setSize(230, 150);
		addWindowListener(dialogCloser);
		setVisible(false);
	}// Of constructor

	/**
	 *************************** 
	 * Read the arff file.
	 *************************** 
	 */
	public void actionPerformed(ActionEvent ae) {
		double delta = Double.parseDouble(deltaField.getText().trim());
		double efc_ctrl = Double.parseDouble(efc_ctrlField.getText().trim());
		if (ae.getSource() == okButton) {
			setVisible(false);
			try {
				// Reduction without cost
				FixedNeighborhoodReduction n = new FixedNeighborhoodReduction(
						delta, efc_ctrl, 0);
				String s = "Efficiency:" + n.getmessageEfficiency();
				s += "\r\n" + "Feature:" + n.getmessageFeature();
				ProgressDialog.progressDialog.setMessageAndShow(s);
			} catch (Exception ee) {
				ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
				ProgressDialog.progressDialog.setVisible(false);
				return;
			}// Of try
		}// if
	}// Of actionPerformed

}// Of class FixedNeighborhoodReductionDialog
