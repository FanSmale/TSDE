/*
 * @(#)Coser.java
 */
package coser;

import java.awt.*;
import coser.gui.guicommon.*;
import coser.gui.menu.*;

/**
 * The main entrance to the whole system.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Project: The cost-sensitive rough sets project 2.1.2.
 * <p>
 * Progress: Done.<br>
 * Written time: March 09, 2011. <br>
 * Last modify time: October 8, 2013.
 */
public class Coser extends Object {
	/**
	 * The entrance method. Create a Frame, set its attributes, and add a
	 * DynamicPanel.
	 */
	public static void main(String args[]) {
		System.out.println("Hello, coser");
		// A simple frame to contain dialogs.
		Frame mainFrame = new Frame();
		mainFrame
				.setTitle("Cost-sensitive Rough Sets. http://grc.fjzs.edu.cn/~fmin/coser, minfanphd@163.com");
		try {
			GUICommon.setFrame(mainFrame);
		} catch (Exception ee) {
			System.out.println(ee.toString());
			System.exit(0);
		}// Of try
		mainFrame.setMenuBar(CoserMenuBar.coserMenuBar);

		// Basic settings of the frame
		mainFrame.setSize(800, 550);
		mainFrame.setLocation(50, 50);
		mainFrame.addWindowListener(ApplicationShutdown.applicationShutdown);
		mainFrame.setBackground(GUICommon.MY_COLOR);
		mainFrame.setVisible(true);
	}// Of main
}// Of class Coser