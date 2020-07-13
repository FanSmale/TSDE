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
 * ���ڸ������ݼ���ѡ��һ�����C4.5�ͻ���<br>
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
	 * �����ڶ������С����
	 */
	private int m_minNoObj;

	/**
	 * All the training data<br>
	 * ѵ��������
	 */
	private Instances m_allData;

	/**
	 ********************** 
	 * Initializes the split selection method with the given parameters.<br>
	 * �Ը���������ʼ������ѡ�񷽷�
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
	 * ����ѵ��������Ϊ��
	 ********************** 
	 */
	public void cleanup() {

		m_allData = null;
	}// Of cleanup

	/**
	 ********************** 
	 * Selects C4.5-type split for the given dataset.<br>
	 * ���ڸ������ݼ�ѡ��C4.5�ͻ���<br>
	 * �������ݼ������ݼ��������ʽ��Ϣ��ѡ����ʵĻ���ģ�ͣ�ģʽ����������ΪC4.5�ͻ��֡�
	 ********************** 
	 */
	public final ClassifierSplitModel selectModel(Instances data) {

		// System.out.println("4: C45ModelSelection.java: selectModel(..)");//
		// ////////////////////

		double minResult;
		// double currentResult;
		C45Split[] currentModel;// C4.5�ͻ���ģʽ�����飩
		C45Split bestModel = null;// ���C4.5�ͻ���
		NoSplit noSplitModel = null;
		double averageInfoGain = 0;// ƽ����Ϣ����
		int validModels = 0;
		boolean multiVal = true;
		Distribution checkDistribution;
		Attribute attribute;
		double sumOfWeights;
		int i;

		try {

			// Check if all Instances belong to one class or if not enough
			// Instances to split.
			// ��鵱ǰʵ���Ƿ�����ͬһ�����ࣨ�Ƿ񴿣��������Ƿ����㹻��ʵ��������
			checkDistribution = new Distribution(data);
			noSplitModel = new NoSplit(checkDistribution);
			if (Utils.sm(checkDistribution.total(), 2 * m_minNoObj)
					|| Utils.eq(checkDistribution.total(), checkDistribution
							.perClass(checkDistribution.maxClass())))
				return noSplitModel;

			// Check if all attributes are nominal and have a
			// lot of values.
			// ������������Ƿ�Ϊ�����ͣ������кܶ�ֵ
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

			// ������ǰ����ģ�����顣���鳤�ȵ������ݼ����Եĸ�����Ҳ���ǿ��ܲ������ֵĸ���
			currentModel = new C45Split[data.numAttributes()];
			sumOfWeights = data.sumOfWeights();

			// For each attribute.
			// ����ÿ�����Զ���
			for (i = 0; i < data.numAttributes(); i++) {

				// Apart from class attribute.
				// ȥ����������
				if (i != (data).classIndex()) {

					// Get models for current attribute.
					// ��õ�ǰ���ԵĻ���ģ��
					currentModel[i] = new C45Split(i, m_minNoObj, sumOfWeights);
					currentModel[i].buildClassifier(data);

					// Check if useful split for current attribute
					// exists and check for enumerated attributes with
					// a lot of values.
					// ��鵱ǰ�����Ƿ�������û��֣�ͬʱ������ö���������Ƿ���ںܶ�ֵ
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
			// ����Ƿ��ҵ����û���
			if (validModels == 0)
				return noSplitModel;
			averageInfoGain = averageInfoGain / (double) validModels;

			// Find "best" attribute to split on.
			// �ҵ��������������
			minResult = 0;
			// ѭ��ÿ���������������ֵ���Ϣ������
			for (i = 0; i < data.numAttributes(); i++) {
				// System.out.println("���ο���ÿ�����Ե�����ʽ��Ϣ");// /////////////////////
				if ((i != (data).classIndex())
						&& (currentModel[i].checkModel())) {

					// System.err.println("��ǰģ����Ϣ������ = "
					// + currentModel[i].gainRatio());// ///////////////
					// System.err.println("��ǰģ�Ͳ��Դ��ۼ�Ȩ��Ϣ������ = "
					// + currentModel[i].testcostWeightedGainRatio());//
					// /////////////

					// Use 1E-3 here to get a closer approximation to the
					// original implementation.
					// ����ʹ��IE-3�����һ��ԭʼʵ�ֵĽ���
					// �����ǰ����ģ���ܹ��������ߵ���Ϣ�����ʣ����趨Ϊ���ģ��
					// if ((currentModel[i].infoGain() >= (averageInfoGain -
					// 1E-3))
					// && Utils.gr(currentModel[i].gainRatio(), minResult)) {
					// bestModel = currentModel[i];
					// minResult = currentModel[i].gainRatio();
					// }// Of if-2

					// ���ݼ�Ȩ��Ϣ������ѡ����ѻ���ģ��
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
			// ����Ƿ��Ѿ��ҵ����û���
			if (Utils.eq(minResult, 0))
				return noSplitModel;

			// Add all Instances with unknown values for the corresponding
			// attribute to the distribution for the model, so that
			// the complete distribution is stored with the model.
			bestModel.distribution().addInstWithUnknown(data,
					bestModel.attIndex());
			BuildAndTestTreeCostC45.individualTestCostArray[bestModel
					.attIndex()] = 0;// ��ʹ�ù������ԵĲ��Դ���Ϊ��

			// System.out.println("C45ModelSelection.java: selectModel(..)");//
			// //////////////////////

			// ���ڵ��ԣ�������Դ�������
			// for (int j = 0; j <
			// BuildAndTestTreeCostC45.individualTestCostArray.length; j++) {
			// System.out
			// .print(BuildAndTestTreeCostC45.individualTestCostArray[j]
			// + " ");
			// }// Of for i
			// System.out.println();// ////////////////////

			// System.out.println("������ԣ�" + bestModel.attIndex());//
			// ////////////////////
			// System.out.println("������ԵĲ��Դ��ۣ�" +
			// bestModel.getAttributeTestCost());// ////////////

			// Set the split point analogue to C45 if attribute numeric.
			// �����������ֵ�͵ģ���������C4.5�ķ������û��ֵ�
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