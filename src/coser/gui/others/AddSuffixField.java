package coser.gui.others;

import java.awt.*;
import java.awt.event.*;

/**
 * Title: Granular hydroelectric system. Only filename, without directory or
 * suffix.<br>
 * Version: 1.0<br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this head while revising the program. <br>
 * Author: <A href="mailto:minfan@uestc.edu.cn">Fan Min</A><br>
 * Company: Rough Sets Group, University of Electronics Science and Technology
 * of China. <br>
 * Progress: Done. Copied from another application.<br>
 * Written time: 2008/08/04<br>
 * Last modify time: 2008/08/04<br>
 */

public class AddSuffixField extends TextField implements TextListener {

	/**
	 * Serail UID.
	 */
	private static final long serialVersionUID = 2623293053067190813L;

	/**
	 * The content of this text field changes according to content of respective
	 * text field.
	 */
	TextField respectiveTextField;

	/**
	 * The suffix.
	 */
	String suffix;

	/**
	 *************************** 
	 * @param paramWidth
	 *            Width of the TextField.
	 *************************** 
	 */
	public AddSuffixField(int paramWidth, TextField paramTextField,
			String paramSuffix) {
		super(paramWidth);
		respectiveTextField = paramTextField;
		suffix = paramSuffix;
	}// Of constructor

	/**
	 ********************************** 
	 * Text value of the first TextField changed
	 ********************************** 
	 */
	public void textValueChanged(TextEvent te) {
		String sourceName = respectiveTextField.getText().trim();
		setText(sourceName + suffix);
	}// Of textValueChanged

}// Of class AddSuffixField
