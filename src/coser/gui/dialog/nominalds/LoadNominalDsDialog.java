package coser.gui.dialog.nominalds;

import java.awt.*;
import java.awt.event.*;
import coser.project.*;

import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;
import coser.gui.menu.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Load a nominal decision system.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Almost done.<br>
 * Written time: August 12, 2011. <br>
 * Last modify time: August 12, 2011.
 */
public class LoadNominalDsDialog extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3691233129157451884L;

	public static LoadNominalDsDialog loadNominalDsDialog = new LoadNominalDsDialog();

	/**
	 * Select the arff file.
	 */
	private FilenameField arffFilenameField;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private LoadNominalDsDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Load a nominal decision system", true);

		arffFilenameField = new FilenameField(40);
		arffFilenameField.setText("data/iris.arff");
		Button browseButton = new Button(" Browse ");
		browseButton.addActionListener(arffFilenameField);

		Panel sourceFilePanel = new Panel();
		sourceFilePanel.add(new Label("The .arff file:"));
		sourceFilePanel.add(arffFilenameField);
		sourceFilePanel.add(browseButton);

		Button okButton = new Button(" OK ");
		okButton.addActionListener(this);
		DialogCloser dialogCloser = new DialogCloser(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(dialogCloser);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog(
				"Load a nominal decision system",
				"coser/gui/dialog/nominalds/LoadNominalDsHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(okButton);
		okPanel.add(cancelButton);
		okPanel.add(helpButton);

		setLayout(new GridLayout(2, 1));
		add(sourceFilePanel);
		// add(optionPanel);
		add(okPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(160, 160);
		setSize(500, 260);
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
		ProgressDialog.progressDialog
				.setMessageAndShow("Please wait a few seconds. Loading ... \r\n");

		String tempFilename = arffFilenameField.getText().trim();
		try {
			// Read data from the source file.
			CoserProject.currentProject.readNominalDecisionSystem(tempFilename);
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow(ee.toString());
			ProgressDialog.progressDialog.setVisible(false);
			return;
		}// Of try

		CoserMenuBar.coserMenuBar.normalizationMenuItem.setEnabled(true);

		CoserMenuBar.coserMenuBar.positiveRegionMenuItem.setEnabled(true);
		CoserMenuBar.coserMenuBar.majorityMenuItem.setEnabled(true);
		CoserMenuBar.coserMenuBar.minimalReductMenuItem.setEnabled(true);
		CoserMenuBar.coserMenuBar.allReductsMenuItem.setEnabled(true);
		CoserMenuBar.coserMenuBar.allSubreductsMenuItem.setEnabled(true);

		ProgressDialog.progressDialog.setVisible(false);
	}// Of actionPerformed

}// Of class LoadNominalDsDialog
