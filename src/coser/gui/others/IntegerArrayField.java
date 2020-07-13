package coser.gui.others;

import java.awt.*;
import java.awt.event.*;
import coser.common.*;

import coser.gui.dialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Ensure the field contains an integer array.
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
public class IntegerArrayField extends TextField implements FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1486010947199189064L;

	/**
	 *************************** 
	 * Only specify the content.
	 * 
	 * @param comeInWidth
	 *            The width of the .
	 *************************** 
	 */
	public IntegerArrayField() {
		this("1 2 3");
	}// Of constructor

	/**
	 *************************** 
	 * Specify the content and the width.
	 * 
	 * @param comeInWidth
	 *            The width of the .
	 *************************** 
	 */
	public IntegerArrayField(String paraString, int paraWidth) {
		super(paraString, paraWidth);
		addFocusListener(this);
	}// Of constructor

	/**
	 *************************** 
	 * Only specify the content.
	 * 
	 * @param comeInWidth
	 *            The width of the .
	 *************************** 
	 */
	public IntegerArrayField(String paraString) {
		super(paraString);
		addFocusListener(this);
	}// Of constructor

	/**
	 *************************** 
	 * Only specify the width.
	 * 
	 * @param comeInWidth
	 *            The width of the .
	 *************************** 
	 */
	public IntegerArrayField(int paraWidth) {
		super(paraWidth);
		setText("513");
		addFocusListener(this);
	}// Of constructor

	/**
	 ********************************** 
	 * Implement FocusListenter.
	 * 
	 * @param paraEvent
	 *            The event is unimportant.
	 ********************************** 
	 */
	public void focusGained(FocusEvent paraEvent) {
	}// Of focusGained

	/**
	 ********************************** 
	 * Implement FocusListenter.
	 * 
	 * @param paraEvent
	 *            The event is unimportant.
	 ********************************** 
	 */
	public void focusLost(FocusEvent paraEvent) {
		try {
			SimpleTool.parseIntArray(getText());
			// System.out.println(tempInt);
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow("\"" + getText()
					+ "\"Not an integer array. Please check.");
			requestFocus();
		}
	}// Of focusLost

}// Of class IntegerArrayField
