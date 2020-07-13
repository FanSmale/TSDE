package coser.gui.dialog.tmds;

import java.awt.*;
import java.awt.event.*;

import coser.common.SimpleTool;
import coser.datamodel.decisionsystem.*;
import coser.gui.dialog.common.DialogCloser;
import coser.gui.dialog.common.ErrorDialog;
import coser.gui.dialog.common.HelpDialog;
import coser.gui.dialog.common.ProgressDialog;
import coser.gui.guicommon.GUICommon;
import coser.project.CoserProject;

public class TwoObjectiveReductionDialog extends Dialog implements
		ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8552943019611196346L;

	public static TwoObjectiveReductionDialog twoObjectiveReductionDialog = new TwoObjectiveReductionDialog();

	/**
	 * The text area to display results.
	 */
	TextArea resultArea;

	/**
	 * The OK button
	 */
	Button okButton;

	/**
	 * The Cancel button
	 */
	Button cancelButton;

	/**
	 * Construct Method
	 */
	public TwoObjectiveReductionDialog() {

		super(GUICommon.mainFrame, "Two objective reduction", true);

		resultArea = new TextArea(10, 20);
		okButton = new Button("Compute");
		okButton.addActionListener(this);

		DialogCloser dialogCloser = new DialogCloser(this);
		cancelButton = new Button("Cancel");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog(
				"Minimal test cost attribute reduction",
				"coser/gui/dialog/tmds/TwoObjectiveReductionDialogHelp.txt"));
		helpButton.setSize(20, 10);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(3, 2));

		Panel okPanel = new Panel();
		okPanel.add(okButton);
		okPanel.add(cancelButton);
		okPanel.add(helpButton);

		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, resultArea);
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
	public void actionPerformed(java.awt.event.ActionEvent ae) {
		// setVisible(false);
		ProgressDialog.progressDialog
				.setMessageAndShow("Please wait for a while."
						+ "\r\nThe execution time depends on the dataset size and the number of experiments."
						+ "\r\nThe progress is shown in the console.\r\n");

		try {
			MultiObjectiveNominalDecisionSystem currentSystem = CoserProject.currentProject.currentMoTmNds;

			String message = "";
			boolean[][] allOptimalReducts = currentSystem
					.computeTwoObjectiveOptimalReducts();
			message = SimpleTool.booleanMatrixToString(allOptimalReducts);
			resultArea.setText(message);
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
			ee.printStackTrace();
			return;
		}// Of try
	}// actionPerformed

	public static void main(String[] args) {
		TwoObjectiveReductionDialog.twoObjectiveReductionDialog
				.setVisible(true);
	}// main

}// class TwoObjectiveReductionDialog