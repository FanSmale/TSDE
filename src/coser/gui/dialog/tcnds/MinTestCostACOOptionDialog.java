package coser.gui.dialog.tcnds;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Summary: Specify reduction related parameters, and obtain minimal test cost
 * attribute reducts. The algorithm is implemented in
 * coser.datamodel.decisionsystem.TestCostNominalDecisionSystemACO.
 * <p>
 * Author: <b> Zilong Xu </b> xzl-wy163@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Done.<br>
 * Written time: March 12, 2011. <br>
 * Last modify time: April 27, 2011.
 */
public class MinTestCostACOOptionDialog {

	public static MinTestCostACOOptionDialog minTestCostACOOptionDialog = new MinTestCostACOOptionDialog();

	/**
	 * The frame of the dialog box
	 */
	JFrame frame;

	/**
	 * The combobox of experiment option
	 */
	JComboBox<String> experimentOption;

	/**
	 * The OK button
	 */
	JButton OKButton;

	/**
	 * The cancel button
	 */
	JButton CancelButton;

	/**
	 * The panel of two buttons
	 */
	JPanel buttonPanel;

	/**
	 * Construct Method
	 */
	public MinTestCostACOOptionDialog() {
		frame = new JFrame("Please select experiment mode");
		String[] experimentOptions = { "One Experiment", "Multiple Experiment",
				"Comparision Experiment" };
		experimentOption = new JComboBox<String>(experimentOptions);
		buttonPanel = new JPanel();
		OKButton = new JButton("OK");
		CancelButton = new JButton("Cancel");
		buttonPanel.add(OKButton);
		buttonPanel.add(CancelButton);
		frame.setSize(300, 100);
		frame.setLocation(300, 200);
		frame.setLayout(new GridLayout(2, 1));

		frame.add(experimentOption);
		frame.add(buttonPanel);

		frame.setVisible(true);

		// 确定按钮的动作
		OKButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String option = (String) experimentOption.getSelectedItem();
				if (option == "One Experiment") {
					frame.setVisible(false);
					MinimalTestCostReductionACODialog.minimalTestCostReductionACODialog
							.setVisible(true);
				} else if (option == "Multiple Experiment") {
					frame.setVisible(false);
					MinTestCostACOMutipleDialog.minTestCostACOMutipleDialog
							.setVisible(true);
				} else if (option == "Comparision Experiment") {
					frame.setVisible(false);
					ComparisionACODialog.comparisionACODialog.setVisible(true);
				} else
					System.err.println("Errors occur!");
			}// Of actionPerformed
		});

		// 取消按钮的动作
		CancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
			}
		});

	}// Of constructor

	// Show the dialog
	public void show() {
		frame.setVisible(true);
	}// Of show

}// Of class MinTestCostOptiionACO