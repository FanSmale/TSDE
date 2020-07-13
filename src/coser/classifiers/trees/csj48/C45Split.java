/*
 * Copied from C45Split.java
 *
 */
package coser.classifiers.trees.csj48;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.RevisionUtils;
import weka.core.Utils;

import java.util.Enumeration;

import coser.classifiers.trees.BuildAndTestTreeCostC45;
import coser.classifiers.trees.CostJ48;
import coser.project.CoserProject;

/**
 * Class implementing a C4.5-type split on an attribute.
 * 
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @version $Revision: 1.13 $
 */
public class C45Split extends ClassifierSplitModel {

	/** for serialization */
	private static final long serialVersionUID = 3064079330067903161L;

	/**
	 * Desired number of branches.<br>
	 * 分支满意的数量
	 */
	private int m_complexityIndex;

	/**
	 * Attribute to split on.<br>
	 * 用于划分的属性
	 */
	public int m_attIndex;

	/**
	 * The test cost of the attribute used to split.<br>
	 * 用于划分的属性的测试代价
	 */
	private int attributeTestCost;

	/**
	 * Minimum number of objects in a split.<br>
	 * 在一个划分中的最小对象数
	 */
	private int m_minNoObj;

	/**
	 * Value of split point.<br>
	 * 划分点的值
	 */
	private double m_splitPoint;

	/**
	 * InfoGain of split.<br>
	 * 划分的信息增益
	 */
	private double m_infoGain;

	/**
	 * GainRatio of split.<br>
	 * 划分的信息增益
	 */
	private double m_gainRatio;

	/**
	 * The GainRatio of split with the test cost weighted<br>
	 * 测试代价加权的划分信息增益率
	 */
	private double m_testcostWeightedGainRatio;

	/**
	 * The sum of the weights of the instances.<br>
	 * 实例权重总和
	 */
	private double m_sumOfWeights;

	/**
	 * Number of split points.<br>
	 * 划分点的编号
	 */
	private int m_index;

	/**
	 * Static reference to splitting criterion.<br>
	 * 信息增益划分的静态对象
	 */
	private static InfoGainSplitCrit infoGainCrit = new InfoGainSplitCrit();

	/**
	 * Static reference to splitting criterion.<br>
	 * 信息增益率划分的静态对象
	 */
	private static GainRatioSplitCrit gainRatioCrit = new GainRatioSplitCrit();

	/**
	 ********************** 
	 * Initializes the split model.<br>
	 * 初始化划分模型
	 ********************** 
	 */
	public C45Split(int attIndex, int minNoObj, double sumOfWeights) {

		// Get index of attribute to split on.
		m_attIndex = attIndex;

		// Set minimum number of objects.
		m_minNoObj = minNoObj;

		// Set the sum of the weights
		m_sumOfWeights = sumOfWeights;

		// Set the test cost weighted gain ratio
		m_testcostWeightedGainRatio = 0;

		// Set the test cost of the attribute given to split
		try {
			if (CoserProject.currentProject.currentBcNds != null) {
				attributeTestCost = CoserProject.currentProject.currentBcNds
						.getIndividualTestCost(attIndex);
			} else if (CoserProject.currentProject.currentBcUds != null) {
				if (CostJ48.dataType == false) {
					attributeTestCost = CoserProject.currentProject.currentBcNds
							.getIndividualTestCost(attIndex);
				} else if (CostJ48.dataType == true) {
					attributeTestCost = CoserProject.currentProject.currentBcUds
							.getIndividualTestCost(attIndex);
				} else
					throw new Exception(
							"Erorrs occurs at C45Split(int attIndex, int minNoObj, double sumOfWeights)@C45Split.java");
			}// Of if-else if
		} catch (Exception e) {
			e.printStackTrace();
		}// Of try-catch

	}// Of C45Split

	/**
	 * Creates a C4.5-type split on the given data. Assumes that none of the
	 * class values is missing.
	 * 
	 * @exception Exception
	 *                if something goes wrong
	 */
	public void buildClassifier(Instances trainInstances) throws Exception {

		// System.out.println("C45Split.java: buildClassifier(..)");//
		// /////////////////////////

		// Initialize the remaining instance variables.
		m_numSubsets = 0;
		m_splitPoint = Double.MAX_VALUE;
		m_infoGain = 0;
		m_gainRatio = 0;

		// Different treatment for enumerated and numeric
		// attributes.
		if (trainInstances.attribute(m_attIndex).isNominal()) {
			m_complexityIndex = trainInstances.attribute(m_attIndex)
					.numValues();
			m_index = m_complexityIndex;
			handleEnumeratedAttribute(trainInstances);
		} else {
			m_complexityIndex = 2;
			m_index = 0;
			trainInstances.sort(trainInstances.attribute(m_attIndex));
			handleNumericAttribute(trainInstances);
		}
	}

	/**
	 * Returns index of attribute for which split was generated.
	 */
	@Override
	public final int attIndex() {

		// System.out.println("获得当前划分模型所使用的属性编号。");// ///////////////////////

		return m_attIndex;
	}// Of attIndex

	/**
	 * 
	 * Gets class probability for instance.
	 * 
	 * @exception Exception
	 *                if something goes wrong
	 */
	public final double classProb(int classIndex, Instance instance,
			int theSubset) throws Exception {

		if (theSubset <= -1) {
			double[] weights = weights(instance);
			if (weights == null) {
				return m_distribution.prob(classIndex);
			} else {
				double prob = 0;
				for (int i = 0; i < weights.length; i++) {
					prob += weights[i] * m_distribution.prob(classIndex, i);
				}
				return prob;
			}
		} else {
			if (Utils.gr(m_distribution.perBag(theSubset), 0)) {
				return m_distribution.prob(classIndex, theSubset);
			} else {
				return m_distribution.prob(classIndex);
			}
		}
	}

	/**
	 * Returns coding cost for split (used in rule learner).
	 */
	public final double codingCost() {

		return Utils.log2(m_index);
	}

	/**
	 * Returns (C4.5-type) gain ratio for the generated split.
	 */
	public final double gainRatio() {
		return m_gainRatio;
	}

	/**
	 * Creates split on enumerated attribute.
	 * 
	 * @exception Exception
	 *                if something goes wrong
	 */
	private void handleEnumeratedAttribute(Instances trainInstances)
			throws Exception {

		Instance instance;

		m_distribution = new Distribution(m_complexityIndex,
				trainInstances.numClasses());

		// Only Instances with known values are relevant.
		Enumeration<?> enu = trainInstances.enumerateInstances();
		while (enu.hasMoreElements()) {
			instance = (Instance) enu.nextElement();
			if (!instance.isMissing(m_attIndex))
				m_distribution.add((int) instance.value(m_attIndex), instance);
		}

		// Check if minimum number of Instances in at least two
		// subsets.
		if (m_distribution.check(m_minNoObj)) {
			m_numSubsets = m_complexityIndex;
			m_infoGain = infoGainCrit.splitCritValue(m_distribution,
					m_sumOfWeights);
			m_gainRatio = gainRatioCrit.splitCritValue(m_distribution,
					m_sumOfWeights, m_infoGain);
			computeTestcostWeightedGainRatio();// 计算测试代价加权信息增益率
		}
	}

	/**
	 * Creates split on numeric attribute.
	 * 
	 * @exception Exception
	 *                if something goes wrong
	 */
	private void handleNumericAttribute(Instances trainInstances)
			throws Exception {

		int firstMiss;
		int next = 1;
		int last = 0;
		int splitIndex = -1;
		double currentInfoGain;
		double defaultEnt;
		double minSplit;
		Instance instance;
		int i;

		// Current attribute is a numeric attribute.
		m_distribution = new Distribution(2, trainInstances.numClasses());

		// Only Instances with known values are relevant.
		Enumeration<?> enu = trainInstances.enumerateInstances();
		i = 0;
		while (enu.hasMoreElements()) {
			instance = (Instance) enu.nextElement();
			if (instance.isMissing(m_attIndex))
				break;
			m_distribution.add(1, instance);
			i++;
		}
		firstMiss = i;

		// Compute minimum number of Instances required in each
		// subset.
		minSplit = 0.1 * (m_distribution.total())
				/ ((double) trainInstances.numClasses());
		if (Utils.smOrEq(minSplit, m_minNoObj))
			minSplit = m_minNoObj;
		else if (Utils.gr(minSplit, 25))
			minSplit = 25;

		// Enough Instances with known values?
		if (Utils.sm((double) firstMiss, 2 * minSplit))
			return;

		// Compute values of criteria for all possible split
		// indices.
		defaultEnt = infoGainCrit.oldEnt(m_distribution);
		while (next < firstMiss) {

			if (trainInstances.instance(next - 1).value(m_attIndex) + 1e-5 < trainInstances
					.instance(next).value(m_attIndex)) {

				// Move class values for all Instances up to next
				// possible split point.
				m_distribution.shiftRange(1, 0, trainInstances, last, next);

				// Check if enough Instances in each subset and compute
				// values for criteria.
				if (Utils.grOrEq(m_distribution.perBag(0), minSplit)
						&& Utils.grOrEq(m_distribution.perBag(1), minSplit)) {
					currentInfoGain = infoGainCrit.splitCritValue(
							m_distribution, m_sumOfWeights, defaultEnt);
					if (Utils.gr(currentInfoGain, m_infoGain)) {
						m_infoGain = currentInfoGain;
						splitIndex = next - 1;
					}
					m_index++;
				}
				last = next;
			}
			next++;
		}

		// Was there any useful split?
		if (m_index == 0)
			return;

		// Compute modified information gain for best split.
		m_infoGain = m_infoGain - (Utils.log2(m_index) / m_sumOfWeights);
		if (Utils.smOrEq(m_infoGain, 0))
			return;

		// Set instance variables' values to values for
		// best split.
		m_numSubsets = 2;
		m_splitPoint = (trainInstances.instance(splitIndex + 1).value(
				m_attIndex) + trainInstances.instance(splitIndex).value(
				m_attIndex)) / 2;

		// In case we have a numerical precision problem we need to choose the
		// smaller value
		if (m_splitPoint == trainInstances.instance(splitIndex + 1).value(
				m_attIndex)) {
			m_splitPoint = trainInstances.instance(splitIndex)
					.value(m_attIndex);
		}

		// Restore distributioN for best split.
		m_distribution = new Distribution(2, trainInstances.numClasses());
		m_distribution.addRange(0, trainInstances, 0, splitIndex + 1);
		m_distribution.addRange(1, trainInstances, splitIndex + 1, firstMiss);

		// Compute modified gain ratio for best split.
		m_gainRatio = gainRatioCrit.splitCritValue(m_distribution,
				m_sumOfWeights, m_infoGain);

		computeTestcostWeightedGainRatio();// 计算测试代价加权的信息增益率
	}

	/**
	 * Returns (C4.5-type) information gain for the generated split.
	 */
	public final double infoGain() {

		return m_infoGain;
	}

	/**
	 * Prints left side of condition..
	 * 
	 * @param data
	 *            training set.
	 */
	public final String leftSide(Instances data) {

		// System.out.println("data = " + data);//
		// ///////////////////////////////

		return data.attribute(m_attIndex).name();
	}

	/**
	 * Prints the condition satisfied by instances in a subset.
	 * 
	 * @param index
	 *            of subset
	 * @param data
	 *            training set.
	 */
	public final String rightSide(int index, Instances data) {

		StringBuffer text;

		text = new StringBuffer();
		if (data.attribute(m_attIndex).isNominal())
			text.append(" = " + data.attribute(m_attIndex).value(index));
		else if (index == 0)
			text.append(" <= " + Utils.doubleToString(m_splitPoint, 6));
		else
			text.append(" > " + Utils.doubleToString(m_splitPoint, 6));
		return text.toString();
	}

	/**
	 * Returns a string containing java source code equivalent to the test made
	 * at this node. The instance being tested is called "i".
	 * 
	 * @param index
	 *            index of the nominal value tested
	 * @param data
	 *            the data containing instance structure info
	 * @return a value of type 'String'
	 */
	public final String sourceExpression(int index, Instances data) {

		StringBuffer expr = null;
		if (index < 0) {
			return "i[" + m_attIndex + "] == null";
		}
		if (data.attribute(m_attIndex).isNominal()) {
			expr = new StringBuffer("i[");
			expr.append(m_attIndex).append("]");
			expr.append(".equals(\"")
					.append(data.attribute(m_attIndex).value(index))
					.append("\")");
		} else {
			expr = new StringBuffer("((Double) i[");
			expr.append(m_attIndex).append("])");
			if (index == 0) {
				expr.append(".doubleValue() <= ").append(m_splitPoint);
			} else {
				expr.append(".doubleValue() > ").append(m_splitPoint);
			}
		}
		return expr.toString();
	}

	/**
	 * Sets split point to greatest value in given data smaller or equal to old
	 * split point. (C4.5 does this for some strange reason).
	 */
	public final void setSplitPoint(Instances allInstances) {

		double newSplitPoint = -Double.MAX_VALUE;
		double tempValue;
		Instance instance;

		if ((allInstances.attribute(m_attIndex).isNumeric())
				&& (m_numSubsets > 1)) {
			Enumeration<?> enu = allInstances.enumerateInstances();
			while (enu.hasMoreElements()) {
				instance = (Instance) enu.nextElement();
				if (!instance.isMissing(m_attIndex)) {
					tempValue = instance.value(m_attIndex);
					if (Utils.gr(tempValue, newSplitPoint)
							&& Utils.smOrEq(tempValue, m_splitPoint))
						newSplitPoint = tempValue;
				}
			}
			m_splitPoint = newSplitPoint;
		}
	}

	/**
	 ********************** 
	 * Compute the heuristic information<br>
	 * Heuristic information = Information Gain Ratio * (1 + test cost) ^ lambda
	 ********************** 
	 */
	public void computeTestcostWeightedGainRatio() {

		// System.out.println("C45Split.java: computeTestcostWeightedGainRatio()");//
		// ////////////////////////
		// System.out.println("lambda = " + CostJ48.lambda);//
		// //////////////////////////////

		// 在数值型决策系统中，属性一次测量，可以多次用于划分。为避免再次使用时，测试代价为零造成的问题，就使用如下表达式来计算启发式信息。
		m_testcostWeightedGainRatio = m_gainRatio
				* Math.pow(
						BuildAndTestTreeCostC45.individualTestCostArray[m_attIndex],
						CostJ48.lambda);

		// System.out.println("信息增益率 = " + m_gainRatio);//
		// ///////////////////////
		// System.out.println("测试代价 = "
		// + BuildAndTestTreeCostC45.individualTestCostArray[m_attIndex]);
		// System.out.println("权值 = " + CostJ48.lambda);//
		// ////////////////////////////
		// System.out.println("启发式信息 = 信息增益率 ×（1 + 测试代价）^ 权值");//
		// ////////////////
		// System.out.println("测试代价加权信息增益率  = " +
		// m_testcostWeightedGainRatio);// ///////////////
	}// Of computeTestcostWeightedGainRatio

	/**
	 ********************** 
	 * Return the heuristic informaiton: test cost weighted gain ratio.
	 ********************** 
	 */
	public double testcostWeightedGainRatio() {
		return m_testcostWeightedGainRatio;
	}// Of testcostWeightedGainRatio

	/**
	 * Returns the minsAndMaxs of the index.th subset.
	 */
	public final double[][] minsAndMaxs(Instances data, double[][] minsAndMaxs,
			int index) {

		double[][] newMinsAndMaxs = new double[data.numAttributes()][2];

		for (int i = 0; i < data.numAttributes(); i++) {
			newMinsAndMaxs[i][0] = minsAndMaxs[i][0];
			newMinsAndMaxs[i][1] = minsAndMaxs[i][1];
			if (i == m_attIndex)
				if (data.attribute(m_attIndex).isNominal())
					newMinsAndMaxs[m_attIndex][1] = 1;
				else
					newMinsAndMaxs[m_attIndex][1 - index] = m_splitPoint;
		}

		return newMinsAndMaxs;
	}

	/**
	 * Sets distribution associated with model.
	 */
	public void resetDistribution(Instances data) throws Exception {

		Instances insts = new Instances(data, data.numInstances());
		for (int i = 0; i < data.numInstances(); i++) {
			if (whichSubset(data.instance(i)) > -1) {
				insts.add(data.instance(i));
			}
		}
		Distribution newD = new Distribution(insts, this);
		newD.addInstWithUnknown(data, m_attIndex);
		m_distribution = newD;
	}

	/**
	 * Returns weights if instance is assigned to more than one subset. Returns
	 * null if instance is only assigned to one subset.
	 */
	public final double[] weights(Instance instance) {

		double[] weights;
		int i;

		if (instance.isMissing(m_attIndex)) {
			weights = new double[m_numSubsets];
			for (i = 0; i < m_numSubsets; i++)
				weights[i] = m_distribution.perBag(i) / m_distribution.total();
			return weights;
		} else {
			return null;
		}
	}

	/**
	 * Returns index of subset instance is assigned to. Returns -1 if instance
	 * is assigned to more than one subset.
	 * 
	 * @exception Exception
	 *                if something goes wrong
	 */
	public final int whichSubset(Instance instance) throws Exception {

		if (instance.isMissing(m_attIndex))
			return -1;
		else {
			if (instance.attribute(m_attIndex).isNominal())
				return (int) instance.value(m_attIndex);
			else if (Utils.smOrEq(instance.value(m_attIndex), m_splitPoint))
				return 0;
			else
				return 1;
		}
	}

	/**
	 ********************** 
	 * Get the test cost of the attribute given to split.
	 * 
	 * @return The test cost of the attribute given to split
	 ********************** 
	 */
	public int getAttributeTestCost() {
		return attributeTestCost;
	}// Of getAttributeTestCost

	/**
	 ********************** 
	 * @param attributeTestCost
	 *            The test cost to be set
	 ********************** 
	 */
	public void setAttributeTestCost(int paraAttributeTestCost) {
		attributeTestCost = paraAttributeTestCost;
	}// Of setAttributeTestCost

	/**
	 * Returns the revision string.
	 * 
	 * @return the revision
	 */
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 1.13 $");
	}

}