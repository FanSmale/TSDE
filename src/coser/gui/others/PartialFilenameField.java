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
 * Progress: Done. Written time: 2008/08/04<br>
 * Last modify time: 2008/08/04<br>
 */

public class PartialFilenameField extends TextField implements TextListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4281060136943526294L;
	/**
	 * The content of this text field changes according to content of respective
	 * text field.
	 */
	TextField respectiveTextField;

	/**
	 *************************** 
	 * @param comeInWidth
	 *            Width of the TextField.
	 *************************** 
	 */
	public PartialFilenameField(int comeInWidth, TextField paramTextField) {
		super(comeInWidth);
		respectiveTextField = paramTextField;
	}// Of constructor

	/**
	 ********************************** 
	 * Text value of the first TextField changed
	 ********************************** 
	 */
	public void textValueChanged(TextEvent te) {
		String fullName = respectiveTextField.getText().trim();
		String partialName = fullName;
		if (partialName.lastIndexOf("/") > 0)
			partialName = partialName
					.substring(partialName.lastIndexOf("/") + 1);
		if (partialName.lastIndexOf(".") > 0)
			partialName = partialName
					.substring(0, partialName.lastIndexOf("."));
		setText(partialName);
	}// Of textValueChanged

}// Of class FileSelecter
