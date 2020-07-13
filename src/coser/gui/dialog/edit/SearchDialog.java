package coser.gui.dialog.edit;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Search a string in the java source file. This is because that
 * windows does not support searching in .java file.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: OK. Can be enhanced further.<br>
 * Written time: March 09, 2011. <br>
 * Last modify time: March 09, 2011.
 */

public class SearchDialog extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -93322187069347232L;

	public static SearchDialog searchDialog = new SearchDialog();

	/**
	 * The SoilType object.
	 */
	private FilenameField sourceFilenamesFilenameField;
	private TextField searchedStringField;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private SearchDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Search a string in the source file", false);

		sourceFilenamesFilenameField = new FilenameField("coserfiles.txt", 40);
		Button browseButton = new Button(" Browse ");
		browseButton.addActionListener(sourceFilenamesFilenameField);
		searchedStringField = new TextField(30);

		Panel centralPanel = new Panel();
		centralPanel
				.add(new Label("The file containing all source filenames: "));
		centralPanel.add(sourceFilenamesFilenameField);
		centralPanel.add(browseButton);
		centralPanel.add(new Label(
				"The string to be searched (the longer the better): "));
		centralPanel.add(searchedStringField);

		Button searchButton = new Button(" Search ");
		searchButton.addActionListener(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(new DialogCloser(this));
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog("Search Help",
				"coser/gui/dialog/edit/SearchDialogHelp.txt"));
		Panel southPanel = new Panel();
		southPanel.add(searchButton);
		southPanel.add(cancelButton);
		southPanel.add(helpButton);

		setLayout(new BorderLayout());
		add("Center", centralPanel);
		add("East", new Panel());
		add("West", new Panel());
		add("South", southPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(100, 150);
		setSize(500, 300);
		addWindowListener(new DialogCloser());
		setVisible(false);
	}// Of constructor

	public void actionPerformed(ActionEvent ae) {
		ProgressDialog.progressDialog
				.setMessageAndShow("The string appears in the following files:");
		try {
			String sourceFilenamesFilename = sourceFilenamesFilenameField
					.getText();
			RandomAccessFile filenamesFile = new RandomAccessFile(
					sourceFilenamesFilename, "r");
			String currentFilename = filenamesFile.readLine().trim();
			String searchedString = searchedStringField.getText();
			if (searchedString.trim().length() < 2) {
				ErrorDialog.errorDialog
						.setMessageAndShow("The searched string is too short!");
				filenamesFile.close();
				return;
			}

			RandomAccessFile currentFile;
			String currentLine;
			boolean found = false;

			while (!currentFilename.equals("")) {
				// System.out.println("Starting " + currentFilename);
				try {
					currentFile = new RandomAccessFile(currentFilename, "r");
				} catch (FileNotFoundException fnfe) {
					ProgressDialog.progressDialog.appendMessage("\r\n***"
							+ currentFilename + " not found, ignore.***");
					currentFilename = filenamesFile.readLine().trim();
					continue;
				}
				found = false;

				try {
					currentLine = currentFile.readLine();
					while (true) {
						if (currentLine.indexOf(searchedString) >= 0) {
							found = true;
							break;
						}
						currentLine = currentFile.readLine();
						if (currentLine == null)
							break;
					}// Of while
				} catch (EOFException eofe) {
					// Shall end now
				} catch (Exception ee) {
					ErrorDialog.errorDialog
							.setMessageAndShow("Exception occurred while trying to "
									+ "read a new line. " + ee.toString());
				}

				if (found) {
					ProgressDialog.progressDialog.appendMessage("\r\n"
							+ currentFilename);
				}

				try {
					currentFile.close();
					currentFilename = filenamesFile.readLine().trim();
				} catch (Exception ee) {
					ErrorDialog.errorDialog
							.setMessageAndShow("Cannot close "
									+ currentFilename
									+ "\r\nMaybe you can add a number of blank lines to the file list"
									+ " to amend this error.");
					break;
				}
			}// Of while
			filenamesFile.close();
			ProgressDialog.progressDialog
					.appendMessage("\r\n============The end============");
		} catch (Exception ee) {
			ErrorDialog.errorDialog
					.setMessageAndShow("Exception occurred while trying to search in SearchDialog:"
							+ "Maybe some filenames are incorrect. "
							+ ee.toString());
		}
	}

}// Of class SearchDialog
