package coser.gui.others;

import java.awt.*;
import java.awt.event.*;
import coser.gui.dialog.common.*;

/**
 * Summary: Ensure the field contains a long value. <br>
 * Title: Upper Yangtze hydrology and reservoir systems experiment benchmark. <br>
 * Version: 1.0 <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Author: Fan Min minfan@uestc.edu.cn, minfanphd@163.com <br>
 * Organization: Upper Yangtze catchment reservoir systems research group. <br>
 * University of Electronics Science and Technology of China
 * (http://www.uestc.edu.cn), <br>
 * University of Vermont (http://www.uvm.edu). <br>
 * Sichuan University (http://www.scu.edu.cn). <br>
 * Progress: Version 1.0 done. <br>
 * Written time: Feb. 1, 2009. <br>
 * Last modify time: Feb. 1, 2009.
 */

public class LongField extends TextField implements FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3002550038977686874L;

	/**
	 *************************** 
	 * Specify the content and the width.
	 * 
	 * @param comeInWidth
	 *            The width of the .
	 *************************** 
	 */
	public LongField(String paraString, int paraWidth) {
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
	public LongField(String paraString) {
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
	public LongField(int paraWidth) {
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
			Long.parseLong(getText());
			// System.out.println(tempLong);
		} catch (Exception ee) {
			ErrorDialog.errorDialog
					.setMessageAndShow("Not a long value. Please check.");
			requestFocus();
		}
	}// Of focusLost

	/**
	 ********************************** 
	 * Get the long value.
	 * 
	 * @return the long value.
	 ********************************** 
	 */
	public long getValue() {
		long tempLong = 0;
		try {
			tempLong = Long.parseLong(getText());
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow("\"" + getText()
					+ "\" Not a long int. Please check.");
			requestFocus();
		}
		return tempLong;
	}// Of getValue
}// Of class LongField
