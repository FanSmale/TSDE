package coser.gui.dialog.edit;

import java.awt.*;
import java.awt.event.*;
import weka.core.*;

import coser.common.*;

import coser.gui.guicommon.*;
import coser.gui.others.*;
import coser.gui.dialog.common.*;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Compute an entropy.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organizaion: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: OK.<br>
 * Written time: Febuary 2,2012. <br>
 * Last modify time: Febuary 2,2012.
 */

public class EntropyDialog extends Dialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4514159686553609148L;

	public static EntropyDialog entropyDialog = new EntropyDialog();

	/**
	 * Two numbers.
	 */
	private IntegerField firstField, secondField;

	private DoubleField resultField;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	private EntropyDialog() {
		// This dialog is not module
		super(GUICommon.mainFrame, "Compute entropy", false);

		firstField = new IntegerField("1");
		secondField = new IntegerField("1");
		resultField = new DoubleField("0");

		Panel centralPanel = new Panel();
		centralPanel.setLayout(new GridLayout(3, 2));
		centralPanel.add(new Label("The first number: "));
		centralPanel.add(firstField);
		centralPanel.add(new Label("The second number: "));
		centralPanel.add(secondField);
		centralPanel.add(new Label("Result: "));
		centralPanel.add(resultField);

		Button computeButton = new Button(" Compute ");
		computeButton.addActionListener(this);
		Button cancelButton = new Button(" Cancel ");
		cancelButton.addActionListener(new DialogCloser(this));
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog("Search Help",
				"src/coser/gui/dialog/edit/EntropyDialogHelp.txt"));
		Panel southPanel = new Panel();
		southPanel.add(computeButton);
		southPanel.add(cancelButton);
		southPanel.add(helpButton);

		setLayout(new BorderLayout());
		add("Center", centralPanel);
		add("East", new Panel());
		add("West", new Panel());
		add("South", southPanel);

		setBackground(GUICommon.MY_COLOR);
		setLocation(100, 150);
		setSize(300, 200);
		addWindowListener(new DialogCloser());
		setVisible(false);
	}// Of constructor

	public void actionPerformed(ActionEvent ae) {
		double firstNumber = firstField.getValue();
		double secondNumber = secondField.getValue();
		double total = firstNumber + secondNumber;
		double firstPart = firstNumber / total;
		double secondPart = secondNumber / total;

		double entropy = -firstPart * Utils.log2(firstPart) - secondPart
				* Utils.log2(secondPart);
		resultField.setText("" + SimpleTool.shorterDouble(entropy));
	}
}// Of class EntropyDialog
