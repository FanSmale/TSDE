package coser.gui.dialog.ds;

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
 * Summary: Data Normalization.
 * <p>
 * Author: <b>Hong Zhao</b> fhq_xa@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Done.<br>
 * Written time: June 23, 2011. <br>
 * Last modify time: August 24, 2011.
 */

public class NormalizationDialog extends Dialog implements ActionListener {
	/**
	 * Serail UID.
	 */
	private static final long serialVersionUID = 4424791096804832585L;

	public static NormalizationDialog normalizationDialog = new NormalizationDialog();

	/**
	 * The type of file: arff.
	 */
	private JComboBox<String> outputFileTypeComboBox;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private NormalizationDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Nomalization data", true);

		String[] typeFile = { "arff" };
		outputFileTypeComboBox = new JComboBox<String>(typeFile);
		outputFileTypeComboBox.setSelectedIndex(0);

		Panel optionPanel = new Panel();
		optionPanel.add(new Label("The type of output"));
		optionPanel.add(outputFileTypeComboBox);

		Button okButton = new Button("   OK  ");
		okButton.setSize(20, 10);
		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button("  Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog("New project",
				"coser/gui/dialog/ds/NormalizationHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(okButton);
		okPanel.add(cancelButton);
		okPanel.add(helpButton);

		setLayout(new GridLayout(2, 1));
		add(optionPanel);
		add(okPanel);

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
		setVisible(false);
		String messageDetail = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com";
		messageDetail += "\r\nCoded by Hong Zhao";
		messageDetail += "\r\nThe source filename: ";
		ProgressDialog.progressDialog
				.setMessageAndShow("Please wait a few seconds. Loading ... \r\n");
		String arffFilename = "unspecified";
		try {
			arffFilename = CoserProject.currentProject.currentRds
					.getArffFilename();
			messageDetail += arffFilename;
			FileReader fileReader = new FileReader(arffFilename);
			RoughDecisionSystem currentRds = new RoughDecisionSystem(fileReader);
			currentRds.setClassIndex(currentRds.numAttributes() - 1);
			currentRds.setArffFilename(arffFilename);
			fileReader.close();

			RoughDecisionSystem ds = currentRds.normalize();
			messageDetail += "\r\nThe number of condition attributes: "
					+ (ds.numAttributes() - 1);
			messageDetail += "\r\nThe number of distinct value of each condition attribute:\r\n";
			for (int i = 0; i < currentRds.numAttributes() - 1; i++) {
				messageDetail += ds.numDistinctValues(i) + " ";
			}
			arffFilename = arffFilename.substring(0, arffFilename.length() - 5);// delete
																				// ".arff"
			arffFilename += "_norm.arff";
			messageDetail += "\r\nThe number of decision attribute value: "
					+ ds.numClasses();
			messageDetail += "\r\nThe number of Instances: "
					+ ds.numInstances();
			messageDetail += "\r\nThe output file is ";
			messageDetail += arffFilename + ".\r\n";
			ProgressDialog.progressDialog.setMessageAndShow(messageDetail);
		} catch (Exception ee) {
			ErrorDialog.errorDialog
					.setMessageAndShow("Error occurred while trying to read \'"
							+ arffFilename
							+ "\' in NormalizationDialog.actionPerformed().\r\n"
							+ ee);
		}// Of try
	}// Of actionPerformed
}// Of class NormalizationDialog

