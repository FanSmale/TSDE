package coser.classifiers.trees.csj48;

import coser.datamodel.decisionsystem.BothCostsNominalDecisionSystem;

/**
 * ���첢�Ҳ��Ծ�������ִ�в�ͬ���㷨��<br>
 * Summary: Build and test decision tree. Run different algorithms.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Progress: Half done.<br>
 * Written time: November 5, 2011. <br>
 * Last modify time: January 27, 2012.
 */
public class CostSensitiveClassifierTree extends ClassifierTree {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -672019236106890815L;

	/**
	 * The test cost of the attribute tested in the node
	 */
	protected int nodeTestcost;

	/**
	 * The misclassification cost of the data set in the node
	 */
	protected int nodeMisclassificationCost;

	/**
	 * The total cost of the node.
	 */
	protected int nodeTotalCost;

	/**
	 * The test cost of the subtree whose root is the node.
	 */
	protected int subtreeTestcost;

	/**
	 * The misclassification cost of the subtree whose root is the node.
	 */
	protected int subtreeMisclassificationCost;

	/**
	 * The total cost of the subtree whose root is the node.
	 */
	protected int subtreeTotalCost;

	/**
	 ********************* 
	 * Constructor.
	 ********************* 
	 */
	public CostSensitiveClassifierTree(ModelSelection toSelectLocModel) {
		super(toSelectLocModel);
	}// Of the construcotr

	/**
	 ********************* 
	 * Method for building a classifier tree.
	 * 
	 * @param data
	 *            the data to build the tree from
	 * @throws Exception
	 *             if something goes wrong
	 ********************* 
	 */
	public void buildClassifier(BothCostsNominalDecisionSystem data)
			throws Exception {

		// Can classifier tree handle the data?
		// �������ܹ������������
		getCapabilities().testWithFail(data);

		// remove instances with missing class
		// ɾ������ȱʧ�������ʵ��
		data = new BothCostsNominalDecisionSystem(data);
		data.deleteWithMissingClass();

		// �������ṹ
	}// Of buildClassifier

}// Of class CostSensitiveClassifierTree