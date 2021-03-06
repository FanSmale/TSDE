/*
 *    Copied from GainRatioSplitCrit.java
 *    Copyright (C) 1999 University of Waikato, Hamilton, New Zealand
 *
 */

package coser.classifiers.trees.csj48;

import weka.core.RevisionUtils;
import weka.core.Utils;

/**
 * Class for computing the gain ratio for a given distribution.
 * 
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @version $Revision: 1.8 $
 */
public final class GainRatioSplitCrit extends EntropyBasedSplitCrit {

	/** for serialization */
	private static final long serialVersionUID = -433336694718670930L;

	/**
	 * This method is a straightforward implementation of the gain ratio
	 * criterion for the given distribution.
	 */
	public final double splitCritValue(Distribution bags) {

		System.out.println("GainRatioSplitCrit.java: splitCritValue(..)");//
		// ////////////////

		double numerator;
		double denumerator;

		numerator = oldEnt(bags) - newEnt(bags);

		// Splits with no gain are useless.
		if (Utils.eq(numerator, 0))
			return Double.MAX_VALUE;
		denumerator = splitEnt(bags);

		// Test if split is trivial.
		if (Utils.eq(denumerator, 0))
			return Double.MAX_VALUE;

		// We take the reciprocal value because we want to minimize the
		// splitting criterion's value.
		return denumerator / numerator;
	}

	/**
	 * This method computes the gain ratio in the same way C4.5 does.
	 * 
	 * @param bags
	 *            the distribution 分布
	 * @param totalnoInst
	 *            the weight of ALL instances 所有实例的总权重
	 * @param numerator
	 *            the info gain 信息增益
	 */
	public final double splitCritValue(Distribution bags, double totalnoInst,
			double numerator) {

		// System.out.println("GainRatioSplitCrit.java: splitCritValue(.., ..)");//
		// // ////////////

		double denumerator;

		// Compute split info.
		denumerator = splitEnt(bags, totalnoInst);

		// Test if split is trivial.
		if (Utils.eq(denumerator, 0))
			return 0;
		denumerator = denumerator / totalnoInst;

		return numerator / denumerator;
	}

	/**
	 * Help method for computing the split entropy.
	 */
	private final double splitEnt(Distribution bags, double totalnoInst) {

		double returnValue = 0;
		double noUnknown;
		int i;

		noUnknown = totalnoInst - bags.total();
		if (Utils.gr(bags.total(), 0)) {
			for (i = 0; i < bags.numBags(); i++)
				returnValue = returnValue - logFunc(bags.perBag(i));
			returnValue = returnValue - logFunc(noUnknown);
			returnValue = returnValue + logFunc(totalnoInst);
		}
		return returnValue;
	}

	/**
	 * Returns the revision string.
	 * 
	 * @return the revision
	 */
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 1.8 $");
	}
}
