/*
 *  Copied from NoSplit.java
 *
 */

package coser.classifiers.trees.csj48;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.RevisionUtils;

/**
 * Class implementing a "no-split"-split.
 * 
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @version $Revision: 1.9 $
 */
public final class NoSplit extends ClassifierSplitModel {

	/** for serialization */
	private static final long serialVersionUID = -1292620749331337546L;

	/**
	 * Creates "no-split"-split for given distribution.
	 */
	public NoSplit(Distribution distribution) {

		m_distribution = new Distribution(distribution);
		m_numSubsets = 1;
	}

	/**
	 * Creates a "no-split"-split for a given set of instances.
	 * 
	 * @exception Exception
	 *                if split can't be built successfully
	 */
	public final void buildClassifier(Instances instances) throws Exception {

		m_distribution = new Distribution(instances);
		m_numSubsets = 1;
	}

	/**
	 * Always returns 0 because only there is only one subset.
	 */
	public final int whichSubset(Instance instance) {

		return 0;
	}

	/**
	 * Always returns null because there is only one subset.
	 */
	public final double[] weights(Instance instance) {

		return null;
	}

	/**
	 * Does nothing because no condition has to be satisfied.
	 */
	public final String leftSide(Instances instances) {

		return "";
	}

	/**
	 * Does nothing because no condition has to be satisfied.
	 */
	public final String rightSide(int index, Instances instances) {

		return "";
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

		return "true"; // or should this be false??
	}

	/**
	 * Returns the revision string.
	 * 
	 * @return the revision
	 */
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 1.9 $");
	}

	/**
	 * Return the index of the attribute to split on
	 */
	@Override
	public int attIndex() {
		return m_attIndex;
	}// Of attIndex
}