package coser.gui.dialog.numericis;

import java.awt.event.FocusEvent;

import coser.gui.dialog.common.ErrorDialog;
import coser.gui.others.IntegerField;

public class ValueAreaOfk extends IntegerField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1856235790345L;

	/**
	 *************************** 
	 * Give it default values.
	 *************************** 
	 */
	public ValueAreaOfk() {
		this("4", 10);
	}// Of the first constructor

	protected int IntValue;

	/**
	 *************************** 
	 * Only specify the content.
	 * 
	 * @param paraString
	 *            The content of the field.
	 *************************** 
	 */
	public ValueAreaOfk(String paraString) {
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
	public ValueAreaOfk(int paraWidth) {
		this("4", paraWidth);
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
	public ValueAreaOfk(String paraString, int paraWidth) {
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
			IntValue = Integer.parseInt(getText());
		} catch (Exception ee) {
			ErrorDialog.errorDialog.setMessageAndShow("\"" + getText()
					+ "\" Not an int. Please check.");
			requestFocus();
			return;
		}

		if ((IntValue < 2)) {
			ErrorDialog.errorDialog.setMessageAndShow("\"" + getText()
					+ "\" is not in [2, 943]. Please check.");
			requestFocus();
		}
	}// Of focusLost

}// Of IntegerFractionField
