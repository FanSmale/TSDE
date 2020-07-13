package coser.gui.others;

import java.awt.event.*;

import coser.gui.guicommon.*;
import coser.gui.dialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Close project.
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

public class ProjectCloser extends Object implements ActionListener {

	/**
	 *************************** 
	 * Close currently selected project
	 *************************** 
	 */
	public void actionPerformed(ActionEvent ae) {
		int currentSelectedIndex = GUICommon.mainPane.getSelectedIndex();
		YesNoCancelDialog.yesNoCancelDialog.setTitleAndMessageAndShow(
				"Close project",
				"Do you want to save "
						+ GUICommon.mainPane.getTitleAt(currentSelectedIndex)
						+ " before closing it?");

		int choice = YesNoCancelDialog.yesNoCancelDialog.getChoice();
		if (choice == YesNoCancelDialog.YES) {
			// Save the project
		} else if (choice == YesNoCancelDialog.NO) {
			// Do nothing.
		} else {
			// Canceled. Hence not removed.
			return;
		}// Of if

		GUICommon.mainPane.remove(currentSelectedIndex);
	}// Of actionPerformed
}// Of class ProjectCloser
