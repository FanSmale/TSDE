package coser.gui.others;

import java.awt.event.FocusEvent;

import coser.gui.dialog.common.ErrorDialog;

public class DecimalFractionField extends DoubleField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1856235790345L;

	/**
	 *************************** 
	 * Give it default values.
	 *************************** 
	 */
	public DecimalFractionField() {
		this("0.513", 10);
	}// Of the first constructor

	/**
	 *************************** 
	 * Only specify the content.
	 * 
	 * @param paraString
	 *            The content of the field.
	 *************************** 
	 */
	public DecimalFractionField(String paraString) {
		this(paraString, 10);
	}// Of the second constructor

	/**
	 *************************** 
	 * Only specify the width.
	 * 
	 * @param comeInWidth
	 *            The width of the field.
	 *************************** 
	 */
	public DecimalFractionField(int paraWidth) {
		this("0.513", paraWidth);
	}// Of the third constructor

	/**
	 *************************** 
	 * Specify the content and the width.
	 * 
	 * @param paraString
	 *            The content of the field.
	 * @param comeInWidth
	 *            The width of the field.
	 *************************** 
	 */
	public DecimalFractionField(String paraString, int paraWidth) {
		super(paraString, paraWidth);
	}// Of the fourth constructor

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
			doubleValue = Double.parseDouble(getText());
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow("\"" + getText()
					+ "\" Not a double. Please check.");
			requestFocus();
			return;
		}

		if ((doubleValue < 0) || (doubleValue > 1)) {
			ErrorDialog.errorDialog.setMessageAndShow("\"" + getText()
					+ "\" is not in [0, 1]. Please check.");
			requestFocus();
		}
	}// Of focusLost

}// Of DecimalFractionField
