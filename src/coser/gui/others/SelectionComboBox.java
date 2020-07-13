package coser.gui.others;

import java.awt.event.*;

import javax.swing.*;

/**
 * Summary: Combobox with some choices. Choices are given by a string array,
 * which the return values are a number starting from 0. So the sequence is very
 * important. <br>
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

public class SelectionComboBox extends JComboBox<String> {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2891611536670404093L;

	/**
	 *************************** 
	 * No special initialization..
	 *************************** 
	 */
	public SelectionComboBox(String[] paraChoices) {
		super();
		for (int i = 0; i < paraChoices.length; i++) {

		}// Of for i
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
	}// Of actionPerformed

}// Of class SelectionComboBox
