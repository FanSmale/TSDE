package coser.algorithm;

import java.text.*;
import coser.project.*;
import coser.datamodel.decisionsystem.*;

/**
 * Project: Test-Cost-Sensitive Neighborhood Reduction.
 * <p>
 * Summary: Neighborhood Reduction.
 * <p>
 * Author: <b>Hong Zhao</b> fhq_xa@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href = http://grc.fjzs.edu.cn/>Lab of Granular
 * Computing</a>, Zhangzhou Normal University, Fujian 363000, China.<br>
 * Progress: Almost done.<br>
 * Written time: June 23, 2011. <br>
 * Last modify time: April 21, 2012.
 */
public class FixedNeighborhoodReduction {
	TestCostDecisionSystemFixedNeighborhood currentSystem = CoserProject.currentProject.currentTcDsFn;

	/**
	 * the significance of the selected attributes.
	 */
	double[] efficiency;

	/**
	 * the selected attributes.
	 */
	boolean[] feature;

	/**
	 * the dependency of the attribute.
	 */
	double dependency;

	/**
	 * the threshold to stop the search.
	 */
	int num_end;

	/**
	 * the string of the significance.
	 */
	String messageEfficiency;

	/**
	 * the string of selected attributes.
	 */
	String messageFeature;

	/**
	 * the optimal test cost.
	 */
	double sumOptimalTestCost;

	/**
	 *************************** 
	 * The first constructor.
	 *************************** 
	 */

	public FixedNeighborhoodReduction() {
	}

	/**
	 *************************** 
	 * The second constructor. Reduction without cost when flag equals zero.
	 * Reduction with cost when flag equals one.
	 *************************** 
	 */
	public FixedNeighborhoodReduction(double delta, double efc_ctrl, int flag)
			throws Exception {
		double data_array[][] = new double[currentSystem.numInstances()][currentSystem
				.numAttributes()];
		double temp[] = new double[currentSystem.numInstances()];
		for (int i = 0; i < currentSystem.numAttributes(); i++) {
			temp = currentSystem.attributeToDoubleArray(i);
			for (int j = 0; j < currentSystem.numInstances(); j++)
				data_array[j][i] = temp[j];
		}// for i
		if (flag == 1) {
			currentSystem.refreshTestCost();
			// sumOptimalTestCost =
			// currentSystem.computeOptimalReductTestCostNHBacktrack();
		}
		double[] cost = currentSystem.getNomalizedIndividualTestCostArray();
		NumberFormat numFormat = NumberFormat.getNumberInstance();
		numFormat.setMaximumFractionDigits(5);
		try {
			messageFeature = "";
			messageEfficiency = "";
			int m = data_array.length;
			int n = data_array[0].length;
			// int min = 0;
			int[] feature_slct = new int[n];
			efficiency = new double[n - 1];
			feature = new boolean[n - 1];
			boolean[] feature_lft = new boolean[n - 1];
			int[] sample_lft = new int[m];
			int[] sample_all = new int[m];
			// int []smp_csst_all = new int[m];
			double[][] array_cur = new double[m][n];
			int n_array_cur = -1;
			double[][] array_tmp = new double[m][n];
			int n_array_tmp = 0;
			int num_cur = -1; // the number of selected feature
			double[] dpd_tmp = new double[n];// the dependency of the feature
			double[] dpd_tmp_cost = new double[n];
			int[][] smp_csst_tmp = new int[n][m];
			for (int i = 0; i < n - 1; i++)
				feature_lft[i] = true;
			assignment(sample_lft);
			assignment(sample_all);
			// String message = "";
			// String testzh = "";
			num_end = 1;
			double temp_dpd = 0;
			while (num_cur < n - 2) {
				if (num_cur != -1) {
					for (int i = 0; i < m; i++) {
						array_cur[i][num_cur] = data_array[i][feature_slct[num_cur]];
					}// for i
					n_array_cur++;
				}// if
				for (int i = 0; i < n - 1; i++) {// ///////////////////////////////////////////////////////////
					array_tmp = copyArray(array_cur);// ///////////////////////////////////////////////////
					if (feature_lft[i] == false)
						continue;
					for (int j = 0; j < m; j++) {
						array_tmp[j][num_cur + 1] = data_array[j][i];
						array_tmp[j][num_cur + 2] = data_array[j][n - 1];
					}// for j
					n_array_tmp = n_array_cur + 3;
					int[] e = attributeDependency(array_tmp, n_array_tmp,
							delta, sample_lft);
					double w = dependency;
					dpd_tmp[i] = w;
					dpd_tmp_cost[i] = w + (1 - cost[i]) * efc_ctrl;
					for (int i1 = 0; i1 < smp_csst_tmp.length; i1++)
						for (int j1 = 0; j1 < smp_csst_tmp[0].length; j1++)
							smp_csst_tmp[i1][j1] = -1;
					if (e.length != 0)
						for (int j = 0; j < e.length; j++) {
							smp_csst_tmp[i][j] = e[j];
						}// for j
				}// for int i
					// message += "\n";
				int max_sequence = maxid(dpd_tmp_cost);
				double max_dpd = dpd_tmp[max_sequence];
				if (num_cur > 0 && Math.abs(max_dpd - temp_dpd) < 0.000001) {// efficiency[num_cur]>0.001)//{
					num_end = num_cur - 1;
					num_cur = n - 1;
				} else if ((max_dpd > 0 || num_cur > 0) && temp_dpd != max_dpd) {
					temp_dpd = max_dpd;
					efficiency[max_sequence] = max_dpd;
					feature_slct[num_cur + 1] = max_sequence;// feature_lft[max_sequence];
					feature[max_sequence] = true;
					messageFeature += (feature_slct[num_cur + 1]) + " ";
					messageEfficiency += numFormat
							.format(efficiency[max_sequence]) + " ";
					feature_lft[max_sequence] = false;
					num_cur = num_cur + 1;
				}// if (max_dpd>0 || num_cur>0)
				else {
					num_end = num_cur - 1;
					num_cur = n - 1;
				}
			}// while
		} catch (Exception ee) {
			throw new Exception("Error occurred in featureselect_FW_fast: \r\n"
					+ ee);
		}// try
	}// featureselect_FW_fast

	/**
	 *************************** 
	 * The attribute dependency.
	 *************************** 
	 */
	public int[] attributeDependency(double[][] data_array, int n_array_tmp,
			double delta, int[] smp_chk) throws Exception {
		int m = data_array.length;
		int n = n_array_tmp;
		// String s = "";
		try {
			int num_rightclassified = 0;
			int[] smp_csst = new int[m];
			int t = 0;
			dependency = 0;
			boolean sign, in;
			int j, k;
			double sqare_distance;
			double d;
			for (int i = 0; i < smp_chk.length; i++) {
				sign = true;
				for (j = 0; j < m; j++) {
					sqare_distance = 0;
					in = true;
					if (j != i) {
						for (k = 0; k < n - 1; k++) {
							d = Math.pow(data_array[smp_chk[i]][k]
									- data_array[j][k], 2);
							if (d > 1)
								sqare_distance = sqare_distance + 1;
							else
								sqare_distance = sqare_distance + d;
							if (sqare_distance > Math.pow(delta, 2)) {
								in = false;
								k = n - 1;// break;
							}// if
						}// For k
						if (in == true)
							if (data_array[j][n - 1] != data_array[smp_chk[i]][n - 1]) {
								j = m;
								sign = false;
							}
					}// IF ( j!= i)
				}// For j
				if (sign == true) {
					num_rightclassified = num_rightclassified + 1;
					smp_csst[t] = smp_chk[i];
					t = t + 1;
				}
			}// For i
			dependency = (0.0 + num_rightclassified) / m;
			return smp_csst;
		} catch (Exception ee) {
			throw new Exception("Error occurred in attributeDependency: \r\n"
					+ ee);
		}// Of try
	}// Dependency attributeDependency

	/**
	 ************************* 
	 * Get the string of the significance.
	 ************************* 
	 */
	public String getmessageEfficiency() {
		return messageEfficiency;
	}

	/**
	 ************************* 
	 * Get the string of selected attributes.
	 ************************* 
	 */
	public String getmessageFeature() {
		return messageFeature;
	}

	/**
	 ************************* 
	 * Assignment 0 1 2 3 4 5 6...
	 ************************* 
	 */
	public void assignment(int[] data_array) {
		for (int i = 0; i < data_array.length; i++) {
			data_array[i] = i;
		}
	}

	/**
	 ************************* 
	 * Array copy
	 ************************* 
	 */
	public double[][] copyArray(double[][] data_array) {
		double[][] temp = new double[data_array.length][data_array[0].length];
		for (int i = 0; i < data_array.length; i++) {
			for (int j = 0; j < data_array[0].length; j++)
				temp[i][j] = data_array[i][j];
		}
		return temp;
	}

	/**
	 ************************* 
	 * Return the id of the max
	 ************************* 
	 */
	public int maxid(double[] dpd_tmp) {
		int maxi = 0;
		for (int i = 1; i < dpd_tmp.length; i++) {
			if (dpd_tmp[i] > dpd_tmp[maxi])
				maxi = i;
		}
		return maxi;
	}

	/**
	 ************************* 
	 * Get efficiency
	 ************************* 
	 */
	public double[] getEfficiency() {
		return efficiency;
	}

	/**
	 ************************* 
	 * Get feature
	 ************************* 
	 */
	public boolean[] getFeature() {
		return feature;
	}

	/**
	 ************************* 
	 * Get the threshold to stop the search.
	 ************************* 
	 */
	public int getNum_Select() {
		return num_end;
	}

}// NeighborhoodReduction

