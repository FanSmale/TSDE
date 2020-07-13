package coser.gui.guicommon;

import java.awt.*;
import javax.swing.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Variables commonly used by GUI components.
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
public class GUICommon extends Object {
	public static Frame mainFrame = null;

	public static JTabbedPane mainPane = null;

	/**
	 * For default project number.
	 */
	public static int currentProjectNumber = 0;

	/**
	 * Defaut font.
	 */
	public static final Font MY_FONT = new Font("Times New Romans", Font.PLAIN,
			12);

	/**
	 * Default color
	 */
	public static final Color MY_COLOR = Color.lightGray;

	/**
	 *************************** 
	 * Set the main frame. This can be done only once at the initialzing stage.
	 * 
	 * @param paramFrame
	 *            the main frame of the GUI.
	 *************************** 
	 */
	public static void setFrame(Frame paramFrame) throws Exception {
		if (mainFrame == null) {
			mainFrame = paramFrame;
		} else {
			throw new Exception("The main frame can be set only ONCE!");
		}// Of if
	}// Of setFrame

	/**
	 *************************** 
	 * Set the main pane. This can be done only once at the initialzing stage.
	 * 
	 * @param paramPane
	 *            the main pane of the GUI.
	 *************************** 
	 */
	public static void setPane(JTabbedPane paramPane) throws Exception {
		if (mainPane == null) {
			mainPane = paramPane;
		} else {
			throw new Exception("The main pane can be set only ONCE!");
		}// Of if
	}// Of setPAne

}// Of class GUICommon
