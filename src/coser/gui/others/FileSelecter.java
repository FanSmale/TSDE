package coser.gui.others;

import java.awt.*;
import java.awt.event.*;

import coser.gui.guicommon.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Select a file and put the filename into a text field.
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

public class FileSelecter extends Object implements ActionListener {

	/**
	 * Who wants the file name?
	 */
	private TextField textField;

	/**
	 * LOAD or SAVE. Defined in FileDialog
	 */
	private int mode;

	/**
	 *************************** 
	 * Construct a file selector. Attention that it is not an GUI object.
	 * 
	 * @param paramField
	 *            where to put the filename
	 *************************** 
	 */
	public FileSelecter(TextField paramField, int paramMode) {
		textField = paramField;
		mode = paramMode;
	}// Of constructor

	/**
	 ********************************** 
	 * What to do when "browse" is clicked.
	 ********************************** 
	 */
	public void actionPerformed(ActionEvent paramEvent) {
		FileDialog fileDialog = new FileDialog(GUICommon.mainFrame,
				"Select a file", mode);
		// fileDialog.setSize(300, 300);
		fileDialog.setVisible(true);
		if (fileDialog.getDirectory() == null) {
			textField.setText("");
			return;
		}
		textField.setText(fileDialog.getDirectory() + fileDialog.getFile());
	}// Of actionPerformed

}// Of class FileSelecter
