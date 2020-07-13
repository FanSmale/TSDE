/*
 *    Copyied from C45ModelSelection.java
 *
 */
package coser.classifiers.trees.csj48;

import coser.classifiers.trees.BuildAndTestTreeCostC45;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.RevisionUtils;
import weka.core.Utils;

//import java.util.Enumeration;

/**
 * 对于给定数据集，选择一个最佳C4.5型划分<br>
 * Class for selecting a C4.5-type split for a given dataset.
 * 
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @author Zilong Xu Repairing
 * @version $Revision: 1.11 $
 */
public class C45ModelSelection extends ModelSelection {

	/** for serialization */
	private static final long serialVersionUID = 3372204862440821989L;

	/**
	 * Minimum number of objects in interval.<br>
	 * 区间内对象的最小个数
	 */
	private int m_minNoObj;

	/**
	 * All the training data<br>
	 * 训练集数据
	 */
	private Instances m_allData;

	/**
	 ********************** 
	 * Initializes the split selection method with the given parameters.<br>
	 * 以给定参数初始化划分选择方法
	 * 
	 * @param minNoObj
	 *            minimum number of instances that have to occur in at least two
	 *            subsets induced by split
	 * @param allData
	 *            FULL training dataset (necessary for selection of split
	 *            points).
	 ********************** 
	 */
	public C45ModelSelection(int minNoObj, Instances allData) {
		m_minNoObj = minNoObj;
		m_allData = allData;
	}// Of the constructor

	/**
	 ********************** 
	 * Sets reference to training data to null.<br>
	 * 设置训练集引用为空
	 ********************** 
	 */
	public void cleanup() {

		m_allData = null;
	}// Of cleanup

	/**
	 ********************** 
	 * Selects C4.5-type split for the given dataset.<br>
	 * 对于给定数据集选择C4.5型划分<br>
	 * 给定数据集，根据计算的启发式信息，选择合适的划分模型（模式，方法）作为C4.5型划分。
	 ********************** 
	 */
	public final ClassifierSplitModel selectModel(Instances data) {

		// System.out.println("4: C45ModelSelection.java: selectModel(..)");//
		// ////////////////////

		double minResult;
		// double currentResult;
		C45Split[] currentModel;// C4.5型划分模式（数组）
		C45Split bestModel = null;// 最佳C4.5型划分
		NoSplit noSplitModel = null;
		double averageInfoGain = 0;// 平均信息增益
		int validModels = 0;
		boolean multiVal = true;
		Distribution checkDistribution;
		Attribute attribute;
		double sumOfWeights;
		int i;

		try {

			// Check if all Instances belong to one class or if not enough
			// Instances to split.
			// 检查当前实例是否属于同一决策类（是否纯），或者是否有足够的实例来划分
			checkDistribution = new Distribution(data);
			noSplitModel = new NoSplit(checkDistribution);
			if (Utils.sm(checkDistribution.total(), 2 * m_minNoObj)
					|| Utils.eq(checkDistribution.total(), checkDistribution
							.perClass(checkDistribution.maxClass())))
				return noSplitModel;

			// Check if all attributes are nominal and have a
			// lot of values.
			// 检查所有属性是否为名词型，并且有很多值
			if (m_allData != null) {
				for (i = 0; i < data.numAttributes(); i++) {
					// Enumeration enu = data.enumerateAttributes();
					// while (enu.hasMoreElements()) {
					// attribute = (Attribute) enu.nextElement();
					attribute = data.attribute(i);
					if ((attribute.isNumeric())
							|| (Utils.sm((double) attribute.numValues(),
									(0.3 * (double) m_allData.numInstances())))) {
						multiVal = false;
						break;
					}// Of if
				}// Of for i
			}// Of if

			// 创建当前划分模型数组。数组长度等于数据集属性的个数，也就是可能产生划分的个数
			currentModel = new C45Split[data.numAttributes()];
			sumOfWeights = data.sumOfWeights();

			// For each attribute.
			// 对于每个属性而言
			for (i = 0; i < data.numAttributes(); i++) {

				// Apart from class attribute.
				// 去除决策属性
				if (i != (data).classIndex()) {

					// Get models for current attribute.
					// 获得当前属性的划分模型
					currentModel[i] = new C45Split(i, m_minNoObj, sumOfWeights);
					currentModel[i].buildClassifier(data);

					// Check if useful split for current attribute
					// exists and check for enumerated attributes with
					// a lot of values.
					// 检查当前属性是否存在有用划分，同时检查对于枚举型属性是否存在很多值
					if (currentModel[i].checkModel())
						if (m_allData != null) {
							if ((data.attribute(i).isNumeric())
									|| (multiVal || Utils.sm((double) data
											.attribute(i).numValues(),
											(0.3 * (double) m_allData
													.numInstances())))) {
								averageInfoGain = averageInfoGain
										+ currentModel[i].infoGain();
								validModels++;
							}
						} else {
							averageInfoGain = averageInfoGain
									+ currentModel[i].infoGain();
							validModels++;
						}
				} else
					currentModel[i] = null;
			}// Of for i

			// Check if any useful split was found.
			// 检查是否找到有用划分
			if (validModels == 0)
				return noSplitModel;
			averageInfoGain = averageInfoGain / (double) validModels;

			// Find "best" attribute to split on.
			// 找到最佳属性来划分
			minResult = 0;
			// 循环每个属性所产生划分的信息增益率
			for (i = 0; i < data.numAttributes(); i++) {
				// System.out.println("依次考虑每个属性的启发式信息");// /////////////////////
				if ((i != (data).classIndex())
						&& (currentModel[i].checkModel())) {

					// System.err.println("当前模型信息增益率 = "
					// + currentModel[i].gainRatio());// ///////////////
					// System.err.println("当前模型测试代价加权信息增益率 = "
					// + currentModel[i].testcostWeightedGainRatio());//
					// /////////////

					// Use 1E-3 here to get a closer approximation to the
					// original implementation.
					// 这里使用IE-3来获得一个原始实现的近似
					// 如果当前划分模型能够产生更高的信息增益率，就设定为最好模型
					// if ((currentModel[i].infoGain() >= (averageInfoGain -
					// 1E-3))
					// && Utils.gr(currentModel[i].gainRatio(), minResult)) {
					// bestModel = currentModel[i];
					// minResult = currentModel[i].gainRatio();
					// }// Of if-2

					// 根据加权信息增益率选择最佳划分模型
					if ((currentModel[i].infoGain() >= (averageInfoGain - 1E-3))
							&& Utils.gr(
									currentModel[i].testcostWeightedGainRatio(),
									minResult)) {
						bestModel = currentModel[i];
						minResult = currentModel[i].testcostWeightedGainRatio();
					}// Of if-2

				}// Of if-1

			}// Of for i

			// Check if useful split was found.
			// 检查是否已经找到有用划分
			if (Utils.eq(minResult, 0))
				return noSplitModel;

			// Add all Instances with unknown values for the corresponding
			// attribute to the distribution for the model, so that
			// the complete distribution is stored with the model.
			bestModel.distribution().addInstWithUnknown(data,
					bestModel.attIndex());
			BuildAndTestTreeCostC45.individualTestCostArray[bestModel
					.attIndex()] = 0;// 令使用过的属性的测试代价为零

			// System.out.println("C45ModelSelection.java: selectModel(..)");//
			// //////////////////////

			// 用于调试，输出测试代价数组
			// for (int j = 0; j <
			// BuildAndTestTreeCostC45.individualTestCostArray.length; j++) {
			// System.out
			// .print(BuildAndTestTreeCostC45.individualTestCostArray[j]
			// + " ");
			// }// Of for i
			// System.out.println();// ////////////////////

			// System.out.println("最佳属性：" + bestModel.attIndex());//
			// ////////////////////
			// System.out.println("最佳属性的测试代价：" +
			// bestModel.getAttributeTestCost());// ////////////

			// Set the split point analogue to C45 if attribute numeric.
			// 如果属性是数值型的，用类似于C4.5的方法设置划分点
			if (m_allData != null) {
				bestModel.setSplitPoint(m_allData);
			}// Of if
			return bestModel;
		} catch (Exception e) {
			e.printStackTrace();
		}// Of try-catch
		return null;
	}// Of selectModel

	/**
	 ********************** 
	 * Selects C4.5-type split for the given dataset.
	 ********************** 
	 */
	public final ClassifierSplitModel selectModel(Instances train,
			Instances test) {

		// System.out.println("C45ModelSelection.java: selectModel(.., ..)");//
		// ////////////////////

		return selectModel(train);
	}

	/**
	 ********************** 
	 * Returns the revision string.
	 * 
	 * @return the revision
	 ********************** 
	 */
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 1.11 $");
	}
}