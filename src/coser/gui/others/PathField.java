package coser.gui.others;

import java.awt.*;
import java.awt.event.*;
import coser.gui.guicommon.*;

/**
 * Summary: A field responds to the selection of a path. <br>
 * Title: Upper Yangtze hydrology and reservoir systems experiment benchmark.
 * <p>
 * Author: <b>Fan Min</b> <a
 * href=mailto:minfan@uestc.edu.cn>minfan@uestc.edu.cn</a>, <a
 * href=mailto:minfanphd@163.com>minfanphd@163.com</a><br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program.<br>
 * Organization: Upper Yangtze catchment reservoir systems research group.<br>
 * <a href=http://www.uestc.edu.cn>University of Electronics Science and
 * Technology of China</a>, <a href=www.scu.edu.cn>Sichuan University</a>, <a
 * href=www.umv.edu>University of Vermont</a>.
 * <p>
 * Version: 1.0<br>
 * Progress: Half done.<br>
 * Written time: Feb. 03, 2009.<br>
 * Last modify time: Feb. 03, 2009.
 */

public class PathField extends TextField implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8004989278968449802L;

	/**
	 *************************** 
	 * No special initialization..
	 *************************** 
	 */
	public PathField() {
		super();
	}// Of constructor

	/**
	 *************************** 
	 * No special initialization.
	 * 
	 * @param comeInWidth
	 *            The width of the .
	 *************************** 
	 */
	public PathField(int paraWidth) {
		super(paraWidth);
	}// Of constructor

	/**
	 ********************************** 
	 * Implement ActionListenter.
	 * 
	 * @param paraEvent
	 *            The event is unimportant.
	 ********************************** 
	 */
	public void actionPerformed(ActionEvent paraEvent) {
		FileDialog fileDialog = new FileDialog(GUICommon.mainFrame,
				"Select a directory");
		// fileDialog.setSize(300, 300);
		fileDialog.setVisible(true);
		if (fileDialog.getDirectory() == null) {
			setText("");
			return;
		}
		setText(fileDialog.getDirectory());
	}// Of actionPerformed

}// Of class PathField
