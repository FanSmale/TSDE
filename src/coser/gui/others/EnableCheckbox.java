package coser.gui.others;

import java.awt.*;
import java.awt.event.*;

/**
 * Title: Upper Yangtze hydrology and reservoir systems experiment benchmark.
 * Checkbox with the function of enable/disable a component.. Version: 1.0
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. Author: Fan Min minfan@uestc.edu.cn,
 * minfanphd@163.com Organization: Upper Yangtze catchment reservoir systems
 * research group. University of Electronics Science and Technology of China
 * (http://www.uestc.edu.cn), Sichuan University (www.scu.edu.cn). Progress: The
 * very beginning. Written time: Oct. 22, 2008. Last modify time: Dec. 13, 2008.
 */

public class EnableCheckbox extends Checkbox implements ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3098817833532161857L;

	/**
	 * The enabled/disabled component.
	 */
	private Component influencedComponent;

	/**
	 * Another enabled/disabled component. Now there is at most two components
	 * to enable/disable. If more are required, we can use a vector instead.
	 */
	private Component anotherInfluencedComponent = null;

	/**
	 *************************** 
	 * No special initialization..
	 *************************** 
	 */
	public EnableCheckbox() {
		super();
	}// Of constructor

	/**
	 *************************** 
	 * Constructor.
	 * 
	 * @param paraDisplay
	 *            What should be displayed for the checkbox.
	 * @param paraSelected
	 *            It is selected while initializing.
	 * @param paraComponent
	 *            Which component does the checkbox enable/disable.
	 *************************** 
	 */
	public EnableCheckbox(String paraDisplay, boolean paraSelected,
			Component paraComponent) {
		super(paraDisplay, paraSelected);
		influencedComponent = paraComponent;
		addItemListener(this);
	}// Of constructor

	/**
	 *************************** 
	 * Constructor.
	 * 
	 * @param paraDisplay
	 *            What should be displayed for the checkbox.
	 * @param paraSelected
	 *            It is selected while initializing.
	 * @param paraComponent
	 *            Which component does the checkbox enable/disable.
	 * @param paraAnotherComponent
	 *            Which component does the checkbox enable/disable.
	 *************************** 
	 */
	public EnableCheckbox(String paraDisplay, boolean paraSelected,
			Component paraComponent, Component paraAnotherComponent) {
		super(paraDisplay, paraSelected);
		influencedComponent = paraComponent;
		anotherInfluencedComponent = paraAnotherComponent;
		addItemListener(this);
	}// Of constructor

	/**
	 *************************** 
	 * Constructor.
	 * 
	 * @param paraDisplay
	 *            What should be displayed for the checkbox.
	 * @param paraGroup
	 *            Which group does it belongs to.
	 * @param paraSelected
	 *            It is selected while initializing.
	 * @param paraComponent
	 *            Which component does the checkbox enable/disable.
	 *************************** 
	 *            public EnableCheckbox(String paraDisplay, CheckboxGroup
	 *            paraGroup, boolean paraSelected, Component paraComponent){
	 *            super(paraDisplay, paraGroup, paraSelected);
	 *            influencedComponent = paraComponent; addItemListener(this);
	 *            }//Of constructor
	 */

	/**
	 ********************************** 
	 * Implement ActionListenter.
	 * 
	 * @param paraEvent
	 *            The event is unimportant.
	 ********************************** 
	 */
	public void itemStateChanged(ItemEvent paraEvent) {
		influencedComponent.setEnabled(getState());
		if (anotherInfluencedComponent != null)
			anotherInfluencedComponent.setEnabled(getState());
	}// Of actionPerformed

}// Of class FileSelecter
