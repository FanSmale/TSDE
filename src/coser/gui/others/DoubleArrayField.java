package coser.gui.others;

import java.awt.*;
import java.awt.event.*;
import coser.common.*;

import coser.gui.dialog.common.*;

/**
 * Summary: Ensure the field contains a double array. <br>
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
 * Written time: Mar. 11, 2009. <br>
 * Last modify time: Mar. 11, 2009.
 */

public class DoubleArrayField extends TextField implements FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3210579928201981730L;

	double[] doubleArray;

	/**
	 *************************** 
	 * Only specify the content.
	 * 
	 * @param comeInWidth
	 *            The width of the .
	 *************************** 
	 */
	public DoubleArrayField() {
		this("5.0 .13");
	}// Of constructor

	/**
	 *************************** 
	 * Specify the content and the width.
	 * 
	 * @param comeInWidth
	 *            The width of the .
	 *************************** 
	 */
	public DoubleArrayField(String paraString, int paraWidth) {
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
	public DoubleArrayField(String paraString) {
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
	public DoubleArrayField(int paraWidth) {
		super(paraWidth);
		setText("5.13");
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
			doubleArray = SimpleTool.parseDoubleArray(getText());
			// System.out.println(tempInt);
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow("\"" + getText()
					+ "\" Not a double array. Please check.");
			requestFocus();
		}
	}// Of focusLost

}// Of class DoubleArrayField
