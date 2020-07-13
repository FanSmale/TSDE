package coser.gui.dialog.nominalds;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import coser.datamodel.decisionsystem.*;
import coser.project.*;
import coser.gui.guicommon.*;
import coser.gui.dialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Compute all sub-reduct. The algorithm is implemented in
 * coser.datamodel.decisionsystem.RoughDecisionSystem.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Done.<br>
 * Written time: July 10, 2011. <br>
 * Last modify time: June 13, 2012.
 */
public class AllSubreductsDialog extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -280536868212516770L;

	public static AllSubreductsDialog allSubreductsDialog = new AllSubreductsDialog();

	/**
	 * Algorithms.
	 */
	private JComboBox<String> algorithmComboBox;

	/**
	 * Metrics.
	 */
	private JComboBox<String> measureComboBox;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private AllSubreductsDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Construct all subreducts", true);

		String[] algorithms = { "Backtrack" };
		algorithmComboBox = new JComboBox<String>(algorithms);
		algorithmComboBox.setSelectedIndex(0);

		String[] measures = { "Positive region", "Conditional entropy",
				"Majority" };
		measureComboBox = new JComboBox<String>(measures);
		measureComboBox.setSelectedIndex(0);

		Panel centerPanel = new Panel();
		centerPanel.setLayout(new GridLayout(2, 2));
		centerPanel.add(new Label("Algorithm: "));
		centerPanel.add(algorithmComboBox);
		centerPanel.add(new Label("Measure: "));
		centerPanel.add(measureComboBox);

		Button okButton = new Button(" Compute ");
		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog(
				"Attribute reduction for all reducts",
				"coser/gui/dialog/nominalds/allSubreductsDialogHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(okButton);
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
	}// Of constructor

	/**
	 *************************** 
	 * Compute all subreducts and write to an arff file. The entropy of each
	 * subreduct is written, therefore it is inappropriate to employ positive
	 * region based approaches while reading the file.
	 *************************** 
	 */
	public void actionPerformed(ActionEvent ae) {
		setVisible(false);
		ProgressDialog.progressDialog
				.setMessageAndShow("Please wait for a while."
						+ "\r\nThe execution time depends on the dataset size and the number of experiments."
						+ "\r\nThe progress is shown in the console.\r\n");

		try {
			// Use another name to make statements shorter.
			NominalDecisionSystem currentSystem = CoserProject.currentProject.currentNds;
			int tempMeasure = measureComboBox.getSelectedIndex();
			currentSystem.setMeasure(tempMeasure);
			String message = "";
			boolean[][] allSubreducts = currentSystem.computeAllSubreducts();
			int[] allSubreductsConsistency = currentSystem
					.getCandidateSubreductConsistencyArray();
			// String separator=File.separator;
			String subReductsFilename = currentSystem
					.getAllSubreductsFilename();
			PrintWriter writer = null;

			File subReductsFile = new File(subReductsFilename);
			if (subReductsFile.exists()) {
				subReductsFile.delete();
			}
			// System.out.println(" Succeed! ");
			subReductsFile.createNewFile();

			int length = currentSystem.getNumberOfConditions();

			message += "Number of conditions is " + length;
			message += "\r\nResults are written to " + subReductsFilename;
			// message += "\r\nsub-reducts: infomation entropy";
			// boolean [] availableAttribute = new boolean[length];

			writer = new PrintWriter(new FileOutputStream(subReductsFile));
			// The first line serves for the measure
			writer.println(tempMeasure);
			// The second line servers for the number of subreducts
			writer.println(allSubreducts.length);

			String tempMessage = "";
			for (int i = 0; i < allSubreducts.length; i++) {
				tempMessage = "";
				for (int j = 0; j < allSubreducts[0].length; j++) {
					if (allSubreducts[i][j]) {
						tempMessage += j + ",";
					}// Of if
				}// Of for j
				tempMessage = tempMessage
						.substring(0, tempMessage.length() - 1);
				tempMessage += ": " + allSubreductsConsistency[i];
				writer.println(tempMessage);
			}// Of for i

			writer.flush();
			writer.close();

			message += "\r\nFinish!";

			message = "Generated by Coser http://grc.fjzs.edu.cn, minfanphd@163.com\r\n"
					+ message;
			ProgressDialog.progressDialog.setMessageAndShow(message);
			// System.out.println(tempDecisionSystem);
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
			return;
		}// Of try
	}// Of actionPerformed

}// Of class AllSubreductsDialog

