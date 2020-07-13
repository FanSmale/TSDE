package coser.classifiers.trees;

import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.classifiers.Sourcable;
import weka.core.AdditionalMeasureProducer;
import weka.core.Capabilities;
import weka.core.Drawable;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Matchable;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.RevisionUtils;
import weka.core.Summarizable;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.core.TechnicalInformationHandler;
import weka.core.Utils;
import weka.core.WeightedInstancesHandler;
import coser.classifiers.trees.csj48.BinC45ModelSelection;
import coser.classifiers.trees.csj48.C45ModelSelection;
import coser.classifiers.trees.csj48.C45PruneableClassifierTree;
import coser.classifiers.trees.csj48.ClassifierTree;
import coser.classifiers.trees.csj48.ModelSelection;
import coser.classifiers.trees.csj48.PruneableClassifierTree;
import coser.datamodel.decisionsystem.CostSensitiveDecisionSystem;

/**
 * Summary: Cost-sensitive decision tree, revised from J48.java. For more
 * information please refer to our papers: <br>
 * \@INPROCEEDINGS{MinZhu11MinimalCost,<br>
 * author = {Fan Min and William Zhu},<br>
 * title = {},<br>
 * booktitle = {Proceedings of International Conference on Database Theory and
 * Application},<br>
 * series = {FGIT-DTA/BSBT, CCIS},<br>
 * volume = {258},<br>
 * year = {2011},<br>
 * pages = {100--107}<br>
 * }
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program.
 * <p>
 * Organizaion: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Progress: Copied from J48.java. <br>
 * Written time: March 1, 2011. <br>
 * Last modify time: March 20, 2012.
 */
public class CostJ48 extends Classifier implements OptionHandler, Drawable,
		Matchable, Sourcable, WeightedInstancesHandler, Summarizable,
		AdditionalMeasureProducer, TechnicalInformationHandler {

	/** for serialization */
	static final long serialVersionUID = -217733168393644444L;

	/**
	 * The data type of the decision system.<br>
	 * false represents the nominal decision system.<br>
	 * true represents the numberic decision system.
	 */
	public static boolean dataType = false;

	/**
	 * Do not prune the initial tree.
	 */
	public static final int UN_PRUNE = 0;

	/**
	 * Pre-prune the initial tree.
	 */
	public static final int PRE_PRUNE = 1;

	/**
	 * Post-prune the initial tree.
	 */
	public static final int POST_PRUNE = 2;

	/**
	 * The decision tree 决策树
	 */
	public ClassifierTree root;

	/**
	 * The exponention of the test cost.
	 */
	public static double lambda;

	/**
	 * Unpruned tree? 是否不剪枝
	 */
	private boolean unpruned = false;

	/**
	 * Confidence level 一致性水平
	 */
	private float confidenceFactor = 0.25f;

	/**
	 * Minimum number of instances 实例最小数
	 */
	private int minNumObj = 2;

	/**
	 * Determines whether probabilities are smoothed using Laplace correction
	 * when predictions are generated<br>
	 * 决定当预言生成时，是否使用拉普拉斯矫正来平滑概率
	 */
	private boolean useLaplace = false;

	/**
	 * Use reduced error pruning? 是否使用误差减少剪枝
	 */
	private boolean reducedErrorPruning = false;

	/**
	 * Number of folds for reduced error pruning. 误差减少剪枝的折数
	 */
	private int numFolds = 3;

	/**
	 * Binary splits on nominal attributes? 对名词型属性实行二元划分
	 */
	private boolean binarySplits = false;

	/**
	 * Subtree raising to be performed? 子树提升吗？
	 */
	private boolean subtreeRaising = true;

	/**
	 * Cleanup after the tree has been built. 树建立之后，是否清理？
	 */
	private boolean noCleanup = false;

	/**
	 * The pruning strategy including no prune, pre-purne, and post-prune.
	 */
	// private int pruneStrategy;

	/**
	 * Random number seed for reduced-error pruning. 误差减少型剪枝的随机数种子
	 */
	private int seed = 1;

	/**
	 * Tha array saving tested attributes
	 */
	public static boolean[] attributeTested;

	/**
	 * The test cost array
	 */
	public int[] testcostArray;

	/**
	 * The misclassification cost matrix
	 */
	public int[][] costMatrix;

	/**
	 ************************ 
	 * The constructor 1
	 ************************ 
	 */
	public CostJ48() {
		super();
	}// Of the constructor 1

	/**
	 ************************ 
	 * The constructor 2
	 ************************ 
	 */
	public CostJ48(ClassifierTree paraRoot, double paraLambda,
			boolean paraUnpruned, float paraConfidenceFactor,
			int paraMinNumObj, boolean paraUseLaplace,
			boolean paraReducedErrorPruning, int paraNumFolds,
			boolean paraBinarySplits, boolean paraSubtreeRaising,
			boolean paraNoCleanup, int paraPruneStrategy, int paraSeed) {
		super();
		this.root = paraRoot;
		this.unpruned = paraUnpruned;
		this.confidenceFactor = paraConfidenceFactor;
		this.minNumObj = paraMinNumObj;
		this.useLaplace = paraUseLaplace;
		this.reducedErrorPruning = paraReducedErrorPruning;
		this.numFolds = paraNumFolds;
		this.binarySplits = paraBinarySplits;
		this.subtreeRaising = paraSubtreeRaising;
		this.noCleanup = paraNoCleanup;
		// this.pruneStrategy = paraPruneStrategy;
		this.seed = paraSeed;
		this.testcostArray = null;
		this.costMatrix = null;
		CostJ48.lambda = paraLambda;

	}// Of the constructor 2

	/**
	 ************************ 
	 * Returns a string describing classifier
	 * 
	 * @return a description suitable for displaying in the
	 *         explorer/experimenter gui
	 ************************ 
	 */
	public String globalInfo() {

		return "Class for generating a pruned or unpruned cost-sensitive C4.5 decision tree. For more "
				+ "information, see\n\n" + getTechnicalInformation().toString();
	}// Of globalInfo

	/**
	 ************************ 
	 * Returns an instance of a TechnicalInformation object, containing detailed
	 * information about the technical background of this class, e.g., paper
	 * reference or book this class is based on.
	 * 
	 * @return the technical information about this class
	 ************************ 
	 */
	public TechnicalInformation getTechnicalInformation() {
		TechnicalInformation result;

		result = new TechnicalInformation(Type.BOOK);
		result.setValue(Field.AUTHOR, "Ross Quinlan");
		result.setValue(Field.YEAR, "1993");
		result.setValue(Field.TITLE, "C4.5: Programs for Machine Learning");
		result.setValue(Field.PUBLISHER, "Morgan Kaufmann Publishers");
		result.setValue(Field.ADDRESS, "San Mateo, CA");

		return result;
	}// Of getTechnicalInformation

	/**
	 ************************ 
	 * Returns default capabilities of the classifier.
	 * 
	 * @return the capabilities of this classifier
	 ************************ 
	 */
	public Capabilities getCapabilities() {
		Capabilities result;

		try {
			if (!reducedErrorPruning)
				result = new C45PruneableClassifierTree(null, !unpruned,
						confidenceFactor, subtreeRaising, !noCleanup)
						.getCapabilities();
			else
				result = new PruneableClassifierTree(null, !unpruned, numFolds,
						!noCleanup, seed).getCapabilities();
		} catch (Exception e) {
			result = new Capabilities(this);
		}

		result.setOwner(this);

		return result;
	}// Of getCapabilities

	/**
	 ************************ 
	 * Generates the classifier.
	 * 
	 * @param instances
	 *            the data to train the classifier with
	 * @throws Exception
	 *             if classifier can't be built successfully
	 ************************ 
	 */
	public void buildClassifier(Instances instances) throws Exception {

		// System.out.println("CostJ48.java: buildClassifier(..)");//
		// //////////////////////
		// System.out.println("data = " + instances);//
		// ////////////////////////////

		ModelSelection modSelection;

		binarySplits = false;

		// 根据参数（是否二元划分）建立相应的选择模型
		if (binarySplits)
			modSelection = new BinC45ModelSelection(minNumObj, instances);
		else
			modSelection = new C45ModelSelection(minNumObj, instances);

		// 根据参数建立决策树root
		if (!reducedErrorPruning) {
			root = new C45PruneableClassifierTree(modSelection, !unpruned,
					confidenceFactor, subtreeRaising, !noCleanup);
		} else {
			root = new PruneableClassifierTree(modSelection, !unpruned,
					numFolds, !noCleanup, seed);
		}

		// 建立分类器
		root.buildClassifier(instances);

		/*
		 * if (binarySplits) { ((BinC45ModelSelection) modSelection).cleanup();
		 * } else { ((C45ModelSelection) modSelection).cleanup(); }
		 */

	}// Of buildClassifier

	/**
	 ********************* 
	 * Prune the generated C4.5 decision tree.<br>
	 * The pruning object is to minimize the total cost, including the test cost
	 * and the misclassification cost.
	 ********************* 
	 */
	public void prune(int pruneStrategy) {

		switch (pruneStrategy) {
		case UN_PRUNE:
			return;
		case PRE_PRUNE:
			prePrune();
		case POST_PRUNE:
			postPrune(root);
		default:
			break;
		}// Of switch
	}// Of prune

	/**
	 ********************** 
	 * Pre prune the created decision tree.
	 ********************** 
	 */
	public void prePrune() {

	}// Of prePrune

	/**
	 ************************* 
	 * Post prune the decision tree according to the average cost. <br>
	 * Post-order recursive
	 ************************* 
	 */
	public void postPrune(CostJ48 paraCostJ48) {
		if (paraCostJ48.root.isLeaf()) {
			return;
		} else {
			for (int i = 0; i < paraCostJ48.root.m_sons.length; i++) {
				// CostJ48 tempCostJ48 = new
				// CostJ48(paraCostJ48.root.m_sons[i],CostJ48.lambda,true,0.2,2,false,false,2,false,false,false,0,2);
			}// Of for i
		}// Of if - else
	}// Of postPrune

	/**
	 ********************** 
	 * Post prune the created decision tree.<br>
	 * 后序遍历剪枝<br>
	 * 递归法<br>
	 * 所有非叶子结点都含有属性测试信息和划分信息。（对于名词型决策系统而言）<br>
	 * 对于任意一个以某测试为根结点的代价敏感决策树。比较测试该属性的代价（测试代价+误分类代价）和不测试该属性的代价（误分类代价）。<br>
	 * 如果是前者大，就剪枝即不测试当前结点所代表的属性；如果是后者大，就测试该属性。
	 ********************** 
	 */
	public void postPrune(ClassifierTree paraRoot) {

		if (paraRoot.isLeaf()) {
			// 桩：计算误分类代价
			return;
		} else {// 否则，不是叶子结点，就递归后序遍历剪枝。
			for (int i = 0; i < paraRoot.m_sons.length; i++) {
				postPrune(paraRoot.m_sons[i]);// 递归
			}// Of for i
				// 桩：比较测试（测试代价+误分类代价）与不测试（误分类代价）的总代价
				// 具体地说是当前结点的测试代价加上以当前结点为根的子树的误分类代价和当前结点的误分类代价相比较
			// CostJ48 tempCostJ48 = new CostJ48(paraRoot, CostJ48.lambda, true,
			// 0, 2, false, false, 0, false, false, true, 0, 0);

			// 计算误分类代价
		}// Of if else

	}// Of postPrune

	/**
	 ********************** 
	 * Classifies an instance.
	 * 
	 * @param instance
	 *            the instance to classify
	 * @return the classification for the instance
	 * @throws Exception
	 *             if instance can't be classified successfully
	 ********************** 
	 */
	public double classifyInstance(Instance instance) throws Exception {

		return root.classifyInstance(instance);
	}

	/**
	 ************************ 
	 * Returns class probabilities for an instance.
	 * 
	 * @param instance
	 *            the instance to calculate the class probabilities for
	 * @return the class probabilities
	 * @throws Exception
	 *             if distribution can't be computed successfully
	 ************************ 
	 */
	public final double[] distributionForInstance(Instance instance)
			throws Exception {

		return root.distributionForInstance(instance, useLaplace);
	}// Of distributionForInstance

	/**
	 ************************ 
	 * Returns the type of graph this classifier represents.
	 * 
	 * @return Drawable.TREE
	 ************************ 
	 */
	public int graphType() {
		return Drawable.TREE;
	}// Of graphType

	/**
	 ************************ 
	 * Returns graph describing the tree.
	 * 
	 * @return the graph describing the tree
	 * @throws Exception
	 *             if graph can't be computed
	 ************************ 
	 */
	public String graph() throws Exception {

		return root.graph();
	}

	/**
	 ************************ 
	 * Returns tree in prefix order.
	 * 
	 * @return the tree in prefix order
	 * @throws Exception
	 *             if something goes wrong
	 ************************ 
	 */
	public String prefix() throws Exception {

		return root.prefix();
	}

	/**
	 * 
	 * Returns tree as an if-then statement.
	 * 
	 * @param className
	 *            the name of the Java class
	 * @return the tree as a Java if-then type statement
	 * @throws Exception
	 *             if something goes wrong
	 */
	public String toSource(String className) throws Exception {

		StringBuffer[] source = root.toSource(className);
		return "class " + className + " {\n\n"
				+ "  public static double classify(Object[] i)\n"
				+ "    throws Exception {\n\n" + "    double p = Double.NaN;\n"
				+ source[0] // Assignment code
				+ "    return p;\n" + "  }\n" + source[1] // Support code
				+ "}\n";
	}

	/**
	 * Returns an enumeration describing the available options.
	 * 
	 * Valid options are:
	 * <p>
	 * 
	 * -U <br>
	 * Use unpruned tree.
	 * <p>
	 * 
	 * -C confidence <br>
	 * Set confidence threshold for pruning. (Default: 0.25)
	 * <p>
	 * 
	 * -M number <br>
	 * Set minimum number of instances per leaf. (Default: 2)
	 * <p>
	 * 
	 * -R <br>
	 * Use reduced error pruning. No subtree raising is performed.
	 * <p>
	 * 
	 * -N number <br>
	 * Set number of folds for reduced error pruning. One fold is used as the
	 * pruning set. (Default: 3)
	 * <p>
	 * 
	 * -B <br>
	 * Use binary splits for nominal attributes.
	 * <p>
	 * 
	 * -S <br>
	 * Don't perform subtree raising.
	 * <p>
	 * 
	 * -L <br>
	 * Do not clean up after the tree has been built.
	 * 
	 * -A <br>
	 * If set, Laplace smoothing is used for predicted probabilites.
	 * <p>
	 * 
	 * -Q <br>
	 * The seed for reduced-error pruning.
	 * <p>
	 * 
	 * @return an enumeration of all the available options. commented by Fan Min
	 */
	public Enumeration<Option> listOptions() {

		Vector<Option> newVector = new Vector<Option>(9);

		newVector.addElement(new Option("\tUse unpruned tree.", "U", 0, "-U"));
		newVector
				.addElement(new Option(
						"\tSet confidence threshold for pruning.\n"
								+ "\t(default 0.25)", "C", 1,
						"-C <pruning confidence>"));
		newVector.addElement(new Option(
				"\tSet minimum number of instances per leaf.\n"
						+ "\t(default 2)", "M", 1,
				"-M <minimum number of instances>"));
		newVector.addElement(new Option("\tUse reduced error pruning.", "R", 0,
				"-R"));
		newVector.addElement(new Option(
				"\tSet number of folds for reduced error\n"
						+ "\tpruning. One fold is used as pruning set.\n"
						+ "\t(default 3)", "N", 1, "-N <number of folds>"));
		newVector.addElement(new Option("\tUse binary splits only.", "B", 0,
				"-B"));
		newVector.addElement(new Option("\tDon't perform subtree raising.",
				"S", 0, "-S"));
		newVector.addElement(new Option(
				"\tDo not clean up after the tree has been built.", "L", 0,
				"-L"));
		newVector.addElement(new Option(
				"\tLaplace smoothing for predicted probabilities.", "A", 0,
				"-A"));
		newVector.addElement(new Option(
				"\tSeed for random data shuffling (default 1).", "Q", 1,
				"-Q <seed>"));

		return newVector.elements();
	}

	/**
	 * Parses a given list of options.
	 * 
	 * <!-- options-start --> Valid options are:
	 * <p/>
	 * 
	 * <pre>
	 * -U
	 *  Use unpruned tree.
	 * </pre>
	 * 
	 * <pre>
	 * -C &lt;pruning confidence&gt;
	 *  Set confidence threshold for pruning.
	 *  (default 0.25)
	 * </pre>
	 * 
	 * <pre>
	 * -M &lt;minimum number of instances&gt;
	 *  Set minimum number of instances per leaf.
	 *  (default 2)
	 * </pre>
	 * 
	 * <pre>
	 * -R
	 *  Use reduced error pruning.
	 * </pre>
	 * 
	 * <pre>
	 * -N &lt;number of folds&gt;
	 *  Set number of folds for reduced error
	 *  pruning. One fold is used as pruning set.
	 *  (default 3)
	 * </pre>
	 * 
	 * <pre>
	 * -B
	 *  Use binary splits only.
	 * </pre>
	 * 
	 * <pre>
	 * -S
	 *  Don't perform subtree raising.
	 * </pre>
	 * 
	 * <pre>
	 * -L
	 *  Do not clean up after the tree has been built.
	 * </pre>
	 * 
	 * <pre>
	 * -A
	 *  Laplace smoothing for predicted probabilities.
	 * </pre>
	 * 
	 * <pre>
	 * -Q &lt;seed&gt;
	 *  Seed for random data shuffling (default 1).
	 * </pre>
	 * 
	 * <!-- options-end -->
	 * 
	 * @param options
	 *            the list of options as an array of strings
	 * @throws Exception
	 *             if an option is not supported
	 */
	public void setOptions(String[] options) throws Exception {

		// Other options
		String minNumString = Utils.getOption('M', options);
		if (minNumString.length() != 0) {
			minNumObj = Integer.parseInt(minNumString);
		} else {
			minNumObj = 2;
		}
		binarySplits = Utils.getFlag('B', options);
		useLaplace = Utils.getFlag('A', options);

		// Pruning options
		unpruned = Utils.getFlag('U', options);
		subtreeRaising = !Utils.getFlag('S', options);
		noCleanup = Utils.getFlag('L', options);
		if ((unpruned) && (!subtreeRaising)) {
			throw new Exception(
					"Subtree raising doesn't need to be unset for unpruned tree!");
		}
		reducedErrorPruning = Utils.getFlag('R', options);
		if ((unpruned) && (reducedErrorPruning)) {
			throw new Exception(
					"Unpruned tree and reduced error pruning can't be selected "
							+ "simultaneously!");
		}
		String confidenceString = Utils.getOption('C', options);
		if (confidenceString.length() != 0) {
			if (reducedErrorPruning) {
				throw new Exception(
						"Setting the confidence doesn't make sense "
								+ "for reduced error pruning.");
			} else if (unpruned) {
				throw new Exception(
						"Doesn't make sense to change confidence for unpruned "
								+ "tree!");
			} else {
				confidenceFactor = (new Float(confidenceString)).floatValue();
				if ((confidenceFactor <= 0) || (confidenceFactor >= 1)) {
					throw new Exception(
							"Confidence has to be greater than zero and smaller "
									+ "than one!");
				}
			}
		} else {
			confidenceFactor = 0.25f;
		}
		String numFoldsString = Utils.getOption('N', options);
		if (numFoldsString.length() != 0) {
			if (!reducedErrorPruning) {
				throw new Exception("Setting the number of folds"
						+ " doesn't make sense if"
						+ " reduced error pruning is not selected.");
			} else {
				numFolds = Integer.parseInt(numFoldsString);
			}
		} else {
			numFolds = 3;
		}
		String seedString = Utils.getOption('Q', options);
		if (seedString.length() != 0) {
			seed = Integer.parseInt(seedString);
		} else {
			seed = 1;
		}
	}

	/**
	 * Gets the current settings of the Classifier.
	 * 
	 * @return an array of strings suitable for passing to setOptions
	 */
	public String[] getOptions() {

		String[] options = new String[14];
		int current = 0;

		if (noCleanup) {
			options[current++] = "-L";
		}
		if (unpruned) {
			options[current++] = "-U";
		} else {
			if (!subtreeRaising) {
				options[current++] = "-S";
			}
			if (reducedErrorPruning) {
				options[current++] = "-R";
				options[current++] = "-N";
				options[current++] = "" + numFolds;
				options[current++] = "-Q";
				options[current++] = "" + seed;
			} else {
				options[current++] = "-C";
				options[current++] = "" + confidenceFactor;
			}
		}
		if (binarySplits) {
			options[current++] = "-B";
		}
		options[current++] = "-M";
		options[current++] = "" + minNumObj;
		if (useLaplace) {
			options[current++] = "-A";
		}

		while (current < options.length) {
			options[current++] = "";
		}
		return options;
	}

	/**
	 * Returns the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String seedTipText() {
		return "The seed used for randomizing the data "
				+ "when reduced-error pruning is used.";
	}

	/**
	 * Get the value of Seed.
	 * 
	 * @return Value of Seed.
	 */
	public int getSeed() {

		return seed;
	}

	/**
	 * Set the value of Seed.
	 * 
	 * @param newSeed
	 *            Value to assign to Seed.
	 */
	public void setSeed(int newSeed) {

		seed = newSeed;
	}

	/**
	 * Returns the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String useLaplaceTipText() {
		return "Whether counts at leaves are smoothed based on Laplace.";
	}

	/**
	 * Get the value of useLaplace.
	 * 
	 * @return Value of useLaplace.
	 */
	public boolean getUseLaplace() {

		return useLaplace;
	}

	/**
	 * Set the value of useLaplace.
	 * 
	 * @param paraUseLaplace
	 *            Value to assign to useLaplace.
	 */
	public void setUseLaplace(boolean paraUseLaplace) {

		useLaplace = paraUseLaplace;
	}

	/**
	 * Returns a description of the classifier.
	 * 
	 * @return a description of the classifier
	 */
	public String toString() {

		if (root == null) {
			return "No classifier built";
		}
		if (unpruned)
			return "CostJ48 unpruned tree\n------------------\n"
					+ root.toString();
		else
			return "CostJ48 pruned tree\n------------------\n"
					+ root.toString();
	}

	/**
	 * Returns a superconcise version of the model
	 * 
	 * @return a summary of the model
	 */
	public String toSummaryString() {

		return "Number of leaves: " + root.numLeaves() + "\n"
				+ "Size of the tree: " + root.numNodes() + "\n";
	}

	/**
	 * Returns the size of the tree
	 * 
	 * @return the size of the tree
	 */
	public double measureTreeSize() {
		return root.numNodes();
	}

	/**
	 * Returns the number of leaves
	 * 
	 * @return the number of leaves
	 */
	public double measureNumLeaves() {
		return root.numLeaves();
	}

	/**
	 * Returns the number of rules (same as number of leaves)
	 * 
	 * @return the number of rules
	 */
	public double measureNumRules() {
		return root.numLeaves();
	}

	/**
	 * Returns an enumeration of the additional measure names
	 * 
	 * @return an enumeration of the measure names
	 */
	public Enumeration<String> enumerateMeasures() {
		Vector<String> newVector = new Vector<String>(3);
		newVector.addElement("measureTreeSize");
		newVector.addElement("measureNumLeaves");
		newVector.addElement("measureNumRules");
		return newVector.elements();
	}

	/**
	 * Returns the value of the named measure
	 * 
	 * @param additionalMeasureName
	 *            the name of the measure to query for its value
	 * @return the value of the named measure
	 * @throws IllegalArgumentException
	 *             if the named measure is not supported
	 */
	public double getMeasure(String additionalMeasureName) {
		if (additionalMeasureName.compareToIgnoreCase("measureNumRules") == 0) {
			return measureNumRules();
		} else if (additionalMeasureName.compareToIgnoreCase("measureTreeSize") == 0) {
			return measureTreeSize();
		} else if (additionalMeasureName
				.compareToIgnoreCase("measureNumLeaves") == 0) {
			return measureNumLeaves();
		} else {
			throw new IllegalArgumentException(additionalMeasureName
					+ " not supported (j48)");
		}
	}

	/**
	 * Returns the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String unprunedTipText() {
		return "Whether pruning is performed.";
	}

	/**
	 * Get the value of unpruned.
	 * 
	 * @return Value of unpruned.
	 */
	public boolean getUnpruned() {

		return unpruned;
	}

	/**
	 * Set the value of unpruned. Turns reduced-error pruning off if set.
	 * 
	 * @param v
	 *            Value to assign to unpruned.
	 */
	public void setUnpruned(boolean v) {

		if (v) {
			reducedErrorPruning = false;
		}
		unpruned = v;
	}

	/**
	 * Returns the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String confidenceFactorTipText() {
		return "The confidence factor used for pruning (smaller values incur "
				+ "more pruning).";
	}

	/**
	 * Get the value of CF.
	 * 
	 * @return Value of CF.
	 */
	public float getConfidenceFactor() {

		return confidenceFactor;
	}

	/**
	 * Set the value of CF.
	 * 
	 * @param v
	 *            Value to assign to CF.
	 */
	public void setConfidenceFactor(float v) {

		confidenceFactor = v;
	}

	/**
	 * Returns the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String minNumObjTipText() {
		return "The minimum number of instances per leaf.";
	}

	/**
	 * Get the value of minNumObj.
	 * 
	 * @return Value of minNumObj.
	 */
	public int getMinNumObj() {

		return minNumObj;
	}

	/**
	 * Set the value of minNumObj.
	 * 
	 * @param v
	 *            Value to assign to minNumObj.
	 */
	public void setMinNumObj(int v) {

		minNumObj = v;
	}

	/**
	 * Returns the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String reducedErrorPruningTipText() {
		return "Whether reduced-error pruning is used instead of C.4.5 pruning.";
	}

	/**
	 * Get the value of reducedErrorPruning.
	 * 
	 * @return Value of reducedErrorPruning.
	 */
	public boolean getReducedErrorPruning() {

		return reducedErrorPruning;
	}

	/**
	 * Set the value of reducedErrorPruning. Turns unpruned trees off if set.
	 * 
	 * @param v
	 *            Value to assign to reducedErrorPruning.
	 */
	public void setReducedErrorPruning(boolean v) {

		if (v) {
			unpruned = false;
		}
		reducedErrorPruning = v;
	}

	/**
	 * Returns the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String numFoldsTipText() {
		return "Determines the amount of data used for reduced-error pruning. "
				+ " One fold is used for pruning, the rest for growing the tree.";
	}

	/**
	 * Get the value of numFolds.
	 * 
	 * @return Value of numFolds.
	 */
	public int getNumFolds() {

		return numFolds;
	}

	/**
	 * Set the value of numFolds.
	 * 
	 * @param v
	 *            Value to assign to numFolds.
	 */
	public void setNumFolds(int v) {

		numFolds = v;
	}

	/**
	 * Returns the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String binarySplitsTipText() {
		return "Whether to use binary splits on nominal attributes when "
				+ "building the trees.";
	}

	/**
	 * Get the value of binarySplits.
	 * 
	 * @return Value of binarySplits.
	 */
	public boolean getBinarySplits() {

		return binarySplits;
	}

	/**
	 * Set the value of binarySplits.
	 * 
	 * @param v
	 *            Value to assign to binarySplits.
	 */
	public void setBinarySplits(boolean v) {

		binarySplits = v;
	}

	/**
	 * Returns the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String subtreeRaisingTipText() {
		return "Whether to consider the subtree raising operation when pruning.";
	}

	/**
	 * Get the value of subtreeRaising.
	 * 
	 * @return Value of subtreeRaising.
	 */
	public boolean getSubtreeRaising() {

		return subtreeRaising;
	}

	/**
	 * Set the value of subtreeRaising.
	 * 
	 * @param v
	 *            Value to assign to subtreeRaising.
	 */
	public void setSubtreeRaising(boolean v) {

		subtreeRaising = v;
	}

	/**
	 * Returns the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String saveInstanceDataTipText() {
		return "Whether to save the training data for visualization.";
	}

	/**
	 * Check whether instance data is to be saved.
	 * 
	 * @return true if instance data is saved
	 */
	public boolean getSaveInstanceData() {

		return noCleanup;
	}

	/**
	 * Set whether instance data is to be saved.
	 * 
	 * @param v
	 *            true if instance data is to be saved
	 */
	public void setSaveInstanceData(boolean v) {

		noCleanup = v;
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
	 ************************ 
	 * Evaluate the generated decision tree.<br>
	 * Compute the average cost including the test cost and the
	 * misclassification cost of the decision tree.
	 ************************ 
	 */
	public void evaluate() {
		double totalCost = 0;
		double averageCost = 0;
		attributeTested = new boolean[root.m_train.numAttributes()];
		for (int i = 0; i < attributeTested.length; i++) {
			attributeTested[i] = false;
		}// Of for i
		totalCost = computeCost(root);
		averageCost = totalCost / root.m_train.numInstances();
		System.out.println("该决策树的总代价 = " + totalCost);
		System.out.println("平均代价 = " + averageCost);
	}// Of evaluate

	/**
	 ************************ 
	 * Compute the cost of the decision tree.<br>
	 * Recursive method
	 * 
	 * @return The cost including test cost and misclassification cost of the
	 *         subtree.
	 ************************ 
	 */
	public double computeCost(ClassifierTree paraTree) {
		double cost = 0;
		if (paraTree.isLeaf() == false) {// 如果不是叶子结点
			attributeTested[paraTree.m_localModel.attIndex()] = true;// 把该结点所测试的属性加入属性测试数组
			for (int i = 0; i < paraTree.m_sons.length; i++) {
				cost += computeCost(paraTree.m_sons[i]);// 递归
			}// Of for i
			attributeTested[paraTree.m_localModel.attIndex()] = false;// 该结点子树已经计算完成，将返回上层，取消该属性的测试记录
			return cost;
		} else {// 否则，是叶子结点
			CostSensitiveDecisionSystem subset = null;
			double miscostSubset = 0;
			// 循环以计算每个实例的测试代价和误分类代价
			for (int i = 0; i < paraTree.m_train.numInstances(); i++) {
				try {
					// 创建临时代价敏感决策系统
					subset = new CostSensitiveDecisionSystem(paraTree.m_train);
					subset.setMisclassificationCostMatrix(costMatrix);// 设置误分类代价矩阵
				} catch (Exception e) {
					e.printStackTrace();
				}// Of try-catch
				try {
					miscostSubset = subset.averageMisclassificationCost()
							* subset.numInstances();
				} catch (Exception e) {
					e.printStackTrace();
				}// Of try-catch
			}// Of for i
			double testcostSubset = 0;
			// 计算每个实例的测试代价
			for (int j = 0; j < testcostArray.length; j++) {
				if (attributeTested[j]) {
					testcostSubset += testcostArray[j];
				}// Of if
			}// Of for j
			testcostSubset *= subset.numInstances();
			return testcostSubset + miscostSubset;
		}// Of if - else
	}// Of computeCost

	/**
	 ************************ 
	 * Compute the test cost
	 ************************ 
	 */
	public int computeTestcost(boolean[] paraTest) {
		int tempTestcost = 0;
		for (int i = 0; i < paraTest.length; i++) {
			if (paraTest[i]) {
				tempTestcost += testcostArray[i];
			}// Of if
		}// Of for i
		return tempTestcost;
	}// Of computeTestcost

	/**
	 ************************ 
	 * Set the test cost array
	 ************************ 
	 */
	public void setTestcostArray(int[] paraTestcostArray) {
		testcostArray = paraTestcostArray;
	}// Of setTestcostArray

	/**
	 ************************ 
	 * Set the misclassification cost matrix
	 ************************ 
	 */
	public void setMisclassificationCostMatrix(int[][] paraCostMatrix) {
		costMatrix = paraCostMatrix;
	}// Of setMisclassificationCostMatrix

	/**
	 * Main method for testing this class
	 * 
	 * @param argv
	 *            the commandline options
	 */
	public static void main(String[] argv) {
		runClassifier(new CostJ48(), argv);
	}
}// Of class CostJ48