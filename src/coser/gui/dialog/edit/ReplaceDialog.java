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
 * Summary: Replace a string in the java source file. It is very useful for
 * source code management. However it is also a dangerous operation. Therefore
 * backup of source files is strongly recommended while many files are
 * processed.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: OK. Copied from Hydrosimu.<br>
 * Written time: March 09, 2011. <br>
 * Last modify time: March 09, 2011.
 */
public class ReplaceDialog extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5439403804654121315L;

	public static ReplaceDialog replaceDialog = new ReplaceDialog();

	/**
	 * The SoilType object.
	 */
	private FilenameField sourceFilenamesFilenameField;
	private TextField oldStringField;
	private TextField newStringField;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private ReplaceDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Replace a string in the source file", false);

		sourceFilenamesFilenameField = new FilenameField("coserfiles.txt", 40);
		Button browseButton = new Button(" Browse ");
		browseButton.addActionListener(sourceFilenamesFilenameField);
		oldStringField = new TextField(30);
		newStringField = new TextField(30);

		Panel centralPanel = new Panel();
		centralPanel.add(new Label(
				"WARNING: Before replacing please BACKUP files!"));
		centralPanel
				.add(new Label("The file containing all source filenames: "));
		centralPanel.add(sourceFilenamesFilenameField);
		centralPanel.add(browseButton);
		centralPanel.add(new Label("Search: "));
		centralPanel.add(oldStringField);
		centralPanel.add(new Label("Replace with: "));
		centralPanel.add(newStringField);

		Button replaceButton = new Button(" Replace ");
		replaceButton.addActionListener(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(new DialogCloser(this));
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog("Replace Help",
				"coser/gui/dialog/edit/ReplaceDialogHelp.txt"));
		Panel southPanel = new Panel();
		southPanel.add(replaceButton);
		southPanel.add(cancelButton);
		southPanel.add(helpButton);

		setLayout(new BorderLayout());
		add("Center", centralPanel);
		add("East", new Panel());
		add("West", new Panel());
		add("South", southPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(150, 150);
		setSize(420, 300);
		addWindowListener(new DialogCloser());
		setVisible(false);
	}// Of constructor

	public void actionPerformed(ActionEvent ae) {
		ProgressDialog.progressDialog
				.setMessageAndShow("The string appears in the following files:");
		int totalReplacement = 0;
		try {
			String sourceFilenamesFilename = sourceFilenamesFilenameField
					.getText();
			RandomAccessFile filenamesFile = new RandomAccessFile(
					sourceFilenamesFilename, "r");
			String currentFilename = filenamesFile.readLine().trim();
			String oldString = oldStringField.getText();
			String newString = newStringField.getText();
			int oldStringLength = oldString.length();
			// if (oldStringLength < 3) {
			// ErrorDialog.errorDialog.setMessageAndShow("The replaced string is too short!");
			// return;
			// }

			RandomAccessFile currentRandomAccessFile, tempRandomAccessFile;
			File currentFile, tempFile;
			String currentLine;
			String tempPrefix, tempSuffix;

			boolean found = false;
			int currentIndexInLine;

			while (!currentFilename.equals("")) {
				// System.out.println("Starting " + currentFilename);
				try {
					currentRandomAccessFile = new RandomAccessFile(
							currentFilename, "r");
				} catch (FileNotFoundException fnfe) {
					ProgressDialog.progressDialog.appendMessage("\r\n***"
							+ currentFilename + " not found, ignore.***");
					currentFilename = filenamesFile.readLine().trim();
					continue;
				}
				// System.out.println(currentFilename + " is ok.");
				tempRandomAccessFile = new RandomAccessFile(currentFilename
						+ ".tmp", "rw");
				found = false;

				try {
					currentLine = currentRandomAccessFile.readLine();
					while (true) {
						currentIndexInLine = currentLine.indexOf(oldString);
						if (currentIndexInLine >= 0) {
							found = true;
							tempPrefix = currentLine.substring(0,
									currentIndexInLine);
							tempSuffix = currentLine
									.substring(currentIndexInLine
											+ oldStringLength);
							currentLine = tempPrefix + newString + tempSuffix;
							totalReplacement++;
						}

						tempRandomAccessFile.writeBytes(currentLine + "\r\n");
						currentLine = currentRandomAccessFile.readLine();
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

				try {
					currentRandomAccessFile.close();
					tempRandomAccessFile.close();
				} catch (Exception ee) {
					ErrorDialog.errorDialog.setMessageAndShow("Cannot close "
							+ currentFilename);
				}

				try {
					tempFile = new File(currentFilename + ".tmp");
					if (found) {
						ProgressDialog.progressDialog.appendMessage("\r\n"
								+ currentFilename);
						currentFile = new File(currentFilename);
						currentFile.delete();
						// System.out.println(currentFilename + ".tmp renameTo "
						// + currentFilename);
						tempFile.renameTo(currentFile);
					} else {
						tempFile.delete();
					}
				} catch (Exception ee) {
					ErrorDialog.errorDialog
							.setMessageAndShow("Exception occurred on file operation:"
									+ ee.toString()
									+ "\r\nCancel remaining operations");
					break;
				}
				currentFilename = filenamesFile.readLine().trim();
			}// Of while

			filenamesFile.close();
			ProgressDialog.progressDialog
					.appendMessage("\r\nTotal replacement: " + totalReplacement);
			ProgressDialog.progressDialog
					.appendMessage("\r\n============The end============");
		} catch (Exception ee) {
			ErrorDialog.errorDialog
					.setMessageAndShow("Exception occurred while trying to replace in ReplaceDialog:"
							+ "Maybe some filenames are incorrect. "
							+ ee.toString());
		}
	}

}// Of class ReplaceDialog
