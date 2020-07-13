package coser.gui.menu;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import coser.common.SimpleTool;
import coser.datamodel.decisionsystem.BothCostsDecisionSystemNormalError;
import coser.datamodel.decisionsystem.BothCostsNominalDecisionSystem;
import coser.datamodel.decisionsystem.CostSensitiveDecisionSystem;
import coser.datamodel.decisionsystem.NominalDecisionSystem;
import coser.datamodel.decisionsystem.TestCostDecisionSystemErrorRange;
import coser.datamodel.decisionsystem.TestCostDecisionSystemFixedNeighborhood;
import coser.datamodel.decisionsystem.TestCostDecisionSystemNormalError;
import coser.datamodel.decisionsystem.TestCostNominalDecisionSystem;
import coser.gui.dialog.bcdsgc.FeatureSelectionBasedonAdaptiveBCDialog;
import coser.gui.dialog.bcdsgc.FeatureSelectionBasedonGranularComputingDialog;
import coser.gui.dialog.bcdsgc.GranularComputingFeatureSelectionDialog;
import coser.gui.dialog.bcdsgc.LoadBcDsGcDialog;
import coser.gui.dialog.bcdsne.LoadBcDsNeDialog;
import coser.gui.dialog.bcdsne.MinimalCostNormalErrorFeatureSelectionDialog;
import coser.gui.dialog.bcdsne.NormalErrorFeatureSelectionTimeComparisonDialog;
import coser.gui.dialog.bcdsne.NormalErrorLambdaFeatureSelectionDialog;
import coser.gui.dialog.bcnds.CostSensitiveDecisionTreeDialog;
import coser.gui.dialog.bcnds.CostSensitiveDecisionTreePruneDialog;
import coser.gui.dialog.bcnds.CostSensitiveNominalDecisionTreeC45BatchDialog;
import coser.gui.dialog.bcnds.CostSensitiveNominalDecisionTreeC45OnceDialog;
import coser.gui.dialog.bcnds.LoadBcNdsDialog;
import coser.gui.dialog.bcnds.MinimalCostReductionDialog;
import coser.gui.dialog.bcuds.CostSensitiveJ48Dialog;
import coser.gui.dialog.bcuds.CostSensitiveNumericDecisionTreeC45OnceDialog;
import coser.gui.dialog.bcuds.LoadBcUdsDialog;
import coser.gui.dialog.common.ErrorDialog;
import coser.gui.dialog.common.HelpDialog;
import coser.gui.dialog.common.ProgressDialog;
import coser.gui.dialog.ds.LoadDsDialog;
import coser.gui.dialog.ds.NormalizationDialog;
import coser.gui.dialog.edit.EntropyDialog;
import coser.gui.dialog.edit.ReplaceDialog;
import coser.gui.dialog.edit.SearchDialog;
import coser.gui.dialog.nominalds.AllReductsDialog;
import coser.gui.dialog.nominalds.AllSubreductsDialog;
import coser.gui.dialog.nominalds.LoadNominalDsDialog;
import coser.gui.dialog.tcdser.ErrorRangeLambdaReductionDialog;
import coser.gui.dialog.tcdser.ErrorRangeTimeComparisonDialog;
import coser.gui.dialog.tcdser.LoadTcDsErDialog;
import coser.gui.dialog.tcdsfn.FNTimeComparisonDialog;
import coser.gui.dialog.tcdsfn.FixedNeighborhoodAllReductsDialog;
import coser.gui.dialog.tcdsfn.FixedNeighborhoodReductionDialog;
import coser.gui.dialog.tcdsfn.LoadTcDsFnDialog;
import coser.gui.dialog.tcdsfn.OptimalReductFNBkDialog;
import coser.gui.dialog.tcdsfn.TestCostDSFNGADialog;
import coser.gui.dialog.tcdsfn.TestCostDecisionSystemFixedNeighborhoodDialog;
import coser.gui.dialog.tcdsne.LoadTcDsNeDialog;
import coser.gui.dialog.tcdsne.NormalErrorDeltaReductionDialog;
import coser.gui.dialog.tcdsne.NormalErrorLambdaReductionDialog;
import coser.gui.dialog.tcdsne.NormalErrorTimeComparisonDialog;
import coser.gui.dialog.tcnds.ComparisionACODialog;
import coser.gui.dialog.tcnds.LoadTcNdsDialog;
import coser.gui.dialog.tcnds.MinTestCostACOMutipleDialog;
import coser.gui.dialog.tcnds.MinimalTestCostReductionACODialog;
import coser.gui.dialog.tcnds.MinimalTestCostReductionDialog;
import coser.gui.dialog.tcnds.MinimalTestCostReductionFRDialog;
import coser.gui.dialog.tcnds.MinimalTestCostReductionFRWithRestartDialog;
import coser.gui.dialog.tcnds.MinimalTestCostReductionGADialog;
import coser.gui.dialog.tcnds.MinimalTestCostReductionGAWithESDialog;
import coser.gui.dialog.tcnds.PositiveConstraintReductionDialog;
import coser.gui.dialog.tcnds.SimpleCommonTestCostConstraintReductionDialog;
import coser.gui.dialog.tcnds.SimpleCommonTestCostConstraintReductionExhaustiveDialog;
import coser.gui.dialog.tcnds.TestCostConstraintReductionCompareWithOptimalDialog;
import coser.gui.dialog.tcnds.TestCostConstraintReductionDialog;
import coser.gui.dialog.tcnds.TestCostConstraintReductionExhaustiveDialog;
import coser.gui.dialog.tcnds.TestCostConstraintReductionGADialog;
import coser.gui.dialog.tmds.AbcMinimalTimeCostReductionFirstDialog;
import coser.gui.dialog.tmds.BcoMinimalCostReductionDialog;
import coser.gui.dialog.tmds.BcoMinimalTimeCostReductionFirstDialog;
import coser.gui.dialog.tmds.ComparisionABCBasedOnCompetitiveParaDialog;
import coser.gui.dialog.tmds.ComparisionBCOBasedOnCompetitiveParaDialog;
import coser.gui.dialog.tmds.LoadTmDsDialog;
import coser.gui.dialog.tmds.MinTimeCostABCMutipleForIterDialog;
import coser.gui.dialog.tmds.MinTimeCostABCMutipleForLimitDialog;
import coser.gui.dialog.tmds.MinTimeCostBCOGNMutipleDialog;
import coser.gui.dialog.tmds.MinTimeCostBCOMutipleDialog;
import coser.gui.dialog.tmds.MinTimeCostBCONLMutipleDialog;
import coser.gui.dialog.tmds.SbcMinimalTimeCostReductionFirstDialog;
import coser.gui.dialog.tmds.TimeConstraintFeatureSelectionAcoDialog;
import coser.gui.dialog.tmds.TwoObjectiveReductionDialog;
import coser.gui.guicommon.ApplicationShutdown;
import coser.gui.guicommon.GUICommon;
import coser.project.CoserProject;

/**
 * The whole menu. Should cope with respective dialogs.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Project: The cost-sensitive rough sets project.
 * <p>
 * Progress: Done, may be revised at any time to include new ones.<br>
 * Written time: March 09, 2011. <br>
 * Last modify time: Feb. 18, 2013.
 */
public class CoserMenuBar extends MenuBar {

	/**
	 * For serialization.
	 */
	private static final long serialVersionUID = -8436059105842104698L;

	public static CoserMenuBar coserMenuBar = new CoserMenuBar();

	/************************* File menu ************************/
	/**
	 * The File menu
	 */
	private Menu fileMenu;

	/**
	 * The New menu item private MenuItem newMenuItem;
	 */

	/**
	 * The Exit menu item
	 */
	private MenuItem exitMenuItem;

	/********************* DS menu *********************/
	/**
	 * Decision system menu
	 */
	public Menu dsMenu;

	/**
	 * Load menu item
	 */
	public MenuItem loadDsMenuItem;

	/**
	 * The data normalization menu item
	 */
	public MenuItem normalizationMenuItem;

	/********************* Nominal DS menu *********************/
	/**
	 * Decision system menu
	 */
	public Menu nominalDsMenu;

	/**
	 * Load menu item
	 */
	public MenuItem loadNominalDsMenuItem;

	/**
	 * Positive region menu item
	 */
	public MenuItem positiveRegionMenuItem;

	/**
	 * The majority menu item
	 */
	public MenuItem majorityMenuItem;

	/**
	 * Minimal reduct menu item
	 */
	public MenuItem minimalReductMenuItem;

	/**
	 * The all reducts menu item
	 */
	public MenuItem allReductsMenuItem;

	/**
	 * The all sub-reducts menu item
	 */
	public MenuItem allSubreductsMenuItem;

	/********************* TC-NDS menu *********************/
	/**
	 * Test-cost-sensitive nominal decision system menu
	 */
	public Menu tcNdsMenu;

	/**
	 * Load Test-cost-sensitive decision system menu item
	 */
	public MenuItem loadTcNdsMenuItem;

	/**
	 * The minimal test cost reduction through backtrack menu item
	 */
	public MenuItem minimalTestCostReductionBacktrackMenuItem;

	/**
	 * The minimal test cost reduction menu item
	 */
	public MenuItem minimalTestCostReductionMenuItem;

	/**
	 * The test cost constraint attribute reduction exhaustive algorithm menu
	 * item
	 */
	public MenuItem testCostConstraintReductionExhaustiveMenuItem;

	/**
	 * The test cost constraint attribute reduction menu item
	 */
	public MenuItem testCostConstraintReductionMenuItem;

	/**
	 * The test cost constraint attribute reduction menu item # 2. Compared with
	 * optimal sub-reducts
	 */
	public MenuItem testCostConstraintReductionMenuItem2;

	/**
	 * The minimal test cost reduction menu item for GA.
	 */
	public MenuItem minimalTestCostReductionGAMenuItem;

	/**
	 * The minimal test cost reduction menu item for Ant Colony Optimization
	 */
	public Menu minimalTestCostReductionACOMenu; // ACO

	/**
	 * The first option of ACO: one experiment
	 */
	public MenuItem oneExperimentACOMenuItem;

	/**
	 * The second option of ACO: batch experiment
	 */
	public MenuItem mutipleExperimentACOMenuItem;

	/**
	 * The third option of ACO: comparison experiment
	 */
	public MenuItem comparisonExperimentACOMenuItem;

	/**
	 * The minimal test cost reduction menu item for Fast Randomized Algorithm
	 */
	public Menu minimalTestCostReductionFRMenu; // Fast Random

	/**
	 * The fast randomized algorithm with re-start.
	 */
	public MenuItem FRWithRestartMenuItem;

	/**
	 * The fast randomized algorithm without re-start.
	 */
	public MenuItem FRWithoutRestartMenuItem;

	/**
	 * The test cost reduction with constraint menu item for GA.
	 */
	public MenuItem testCostConstraintReductionGAMenuItem;

	/**
	 * The minimal test cost reduction menu item for GA with elitist select.
	 */
	public MenuItem minimalTestCostReductionGAWithESMenuItem;

	/**
	 * The simple common test cost constraint attribute reduction exhaustive
	 * algorithm menu item.
	 */
	public MenuItem simpleCommonTestCostConstraintReductionExhaustiveMenuItem;

	/**
	 * The simple common test cost constraint attribute reduction menu item.
	 */
	public MenuItem simpleCommonTestCostConstraintReductionMenuItem;

	/**
	 * The test cost reduction with positive constraint menu item.
	 */
	public MenuItem positiveConstraintReductionMenuItem;

	/********************* TM-DS menu *********************/
	/**
	 * Time-sensitive decision system menu
	 */
	public Menu tmDsMenu;

	/**
	 * Load Time-sensitive decision system menu item
	 */
	public MenuItem loadTmDsMenuItem;

	/**
	 * Random algorithm for time-sensitive attribute reduction menu item
	 */
	public MenuItem randomTmReductionMenuItem;

	/**
	 * Ant colony constraint algorithm for time-sensitive attribute reduction
	 * menu item
	 */
	public MenuItem acoTmConstraintReductionMenuItem;

	/**
	 * Bee colony optimization algorithm for time-sensitive attribute reduction
	 * menu
	 */
	public Menu bcoMinimalCostReductionMenu; // BCO

	/**
	 * Simulated bee colony algorithm for time-sensitive attribute reduction
	 * menu
	 */
	public MenuItem sbcMinimalCostReductionMenuItem; // SBC

	/**
	 * Artificial bee colony algorithm for time-sensitive attribute reduction
	 * menu
	 */
	public Menu abcMinimalCostReductionMenu; // ABC

	// The sub-menu items of BCO
	/**
	 * The first option of BCO: one experiment
	 */
	public MenuItem oneExperimentBCOMenuItem;

	/**
	 * The second option of BCO: batch experiment for removing rate
	 */
	public MenuItem mutipleExperimentBCOMenuItem;

	/**
	 * The third option of BCO: batch experiment for limited neighbors
	 */
	public MenuItem mutipleExperimentBCONLMenuItem;

	/**
	 * The fourth option of BCO: batch experiment for limited neighbors
	 */
	public MenuItem mutipleExperimentBCOIterMenuItem;

	/**
	 * The fifth option of BCO: competition experiment
	 */
	public MenuItem comparisonExperimentBCOMenuItem;

	// The sub-menu items of ABC

	/**
	 * The first option of ABC: one experiment
	 */
	public MenuItem oneExperimentABCMenuItem;

	/**
	 * The second option of ABC: batch experiment for removing rate
	 */
	public MenuItem mutipleExperimentABCForLimitMenuItem;

	/**
	 * The third option of ABC: competition experiment
	 */
	public MenuItem comparisonExperimentABCMenuItem;

	/**
	 * The fourth option of ABC: batch experiment for removing rate
	 */
	public MenuItem mutipleExperimentABCForIterMenuItem;

	/**
	 * Two Objective reduction menu item
	 */
	public MenuItem twoObjectiveReductionMenuItem;

	/********************* TC-DS-FN *********************/
	/**
	 * Test-cost-sensitive decision system with neighborhood menu
	 */
	public Menu tcDsFnMenu;

	/**
	 * Load Test-cost-sensitive decision system with neighborhood menu item
	 */
	public MenuItem loadTcDsFnMenuItem;

	/**
	 * The neighborhood reduction of Qing-hua Hu
	 */
	public MenuItem fixedNeighborhoodReductionMenuItem;
	/**
	 * Test-cost-sensitive decision system based on neighborhood
	 */
	public MenuItem testCostFixedNeighborhoodReductionMenuItem;
	/**
	 * The all neighborhood reduction based on backtrack
	 */
	public MenuItem fixedNeighborhoodAllReductsMenuItem;

	/**
	 * Backtrack one optimal reduct based on neighborhood rough set(FN) menu
	 * item
	 */
	private MenuItem optimalReductFnBkMenuItem;

	/**
	 * Refresh test costs FN menu item
	 */
	private MenuItem refreshTestCostFNMenuItem;

	/**
	 * Test cost sensitive FN-GA menu item
	 */
	private MenuItem testCostFNGAMenuItem;

	/**
	 * Time comparision of backtack and genetic algorithms of FN
	 */
	public MenuItem FNTimeComparisonMenuItem;

	/**
	 * The delta.
	 */
	// private DoubleField deltaField;

	/********************* TC-DS-ER *********************/
	/**
	 * Test-cost-sensitive decision system with error ranges menu
	 */
	public Menu tcDsErMenu;

	/**
	 * Load Test-cost-sensitive decision system with error range menu item
	 */
	public MenuItem loadTcDsErMenuItem;

	/**
	 * Backtrack all reducts menu item
	 */
	public MenuItem errorRangeAllReductsMenuItem;

	/**
	 * Backtrack one optimal reduct menu item
	 */
	public MenuItem errorRangeOptimalReductMenuItem;

	/**
	 * Heuristic reduction menu item
	 */
	public MenuItem errorRangeLambdaReductionMenuItem;

	/**
	 * Time comparision
	 */
	public MenuItem errorRangeTimeComparisonMenuItem;
	/********************* TC-DS-NE *********************/
	/**
	 * Test-cost-sensitive decision system with normal distribution measurement
	 * error menu
	 */
	public Menu tcDsNeMenu;

	/**
	 * Load Test-cost-sensitive decision system with error range menu item
	 */
	public MenuItem loadTcDsNeMenuItem;

	/**
	 * Backtrack all reducts menu item
	 */
	public MenuItem normalErrorAllReductsMenuItem;

	/**
	 * Backtrack one optimal reduct menu item
	 */
	public MenuItem normalErrorOptimalReductMenuItem;

	/**
	 * Heuristic reduction menu item
	 */
	public MenuItem normalErrorDeltaReductionMenuItem;
	/**
	 * Heuristic reduction menu item
	 */
	public MenuItem normalErrorLambdaReductionMenuItem;

	/**
	 * Time comparision
	 */
	public MenuItem normalErrorTimeComparisonMenuItem;

	/********************* BC-NDS menu *********************/
	/**
	 * Both-cost-sensitive nominal decision system menu
	 */
	public Menu bcNdsMenu;

	/**
	 * Load both-cost-sensitive Numeric decision system menu item
	 */
	public MenuItem loadBcNdsMenuItem;

	/**
	 * Compute both-cost-sensitive optimal reduct menu item. Only for one run.
	 */
	public MenuItem bcNdsOptimalReductMenuItem;

	/**
	 * Compute both-cost-sensitive optimal reduct menu item
	 */
	public MenuItem bcNdsOptimalReductsMenuItem;

	/**
	 * Cost-sensitive decision tree lambda comparison menu item
	 */
	public MenuItem bcDtLambdaMenuItem;

	/**
	 * Cost-sensitive decision tree with class balance menu item public MenuItem
	 * bcDtClassBalanceMenuItem;
	 */

	/**
	 * Cost-sensitive decision tree menu item, for prune strategies
	 */
	public MenuItem costSensitiveDecisionTreePruneMenuItem;

	/**
	 * Cost-sensitive decision tree menu
	 */
	public Menu costSensitiveNominalDecisionTreeC45;// C4.5

	/**
	 * Cost-sensitive decision tree once menu item
	 */
	public MenuItem costSensitiveNominalDecisionTreeC45Once;// C4.5

	/**
	 * Cost-sensitive decision tree batch experiments menu item
	 */
	public MenuItem costSensitiveNominalDecisionTreeC45Batch;// C4.5

	/********************* BC-UDS menu *********************/
	/**
	 * Both-cost-sensitive numeric decision system menu
	 */
	public Menu bcUdsMenu;

	/**
	 * Load both-cost-sensitive numeric decision system menu item
	 */
	public MenuItem loadBcUdsMenuItem;

	/**
	 * Pocess the cost-sensitive decistion tree through the algorithm modified
	 * from C4.5
	 */
	public Menu costSensitiveNumericDecisionTreeC45;// C4.5

	/**
	 * Create a cost-sensitive numberic decistion tree.
	 */
	public MenuItem costSensitiveNumbericDecisionTreeC45Once;// C4.5

	/**
	 * The experiment of cost-sensitive numberic decision tree.
	 */
	public MenuItem costSensitiveNumbericDecisionTreeC45Experiment;// C4.5

	/********************* BC-DS-NE menu *********************/
	/**
	 * Both-cost-sensitive normal distribution measurement error decision system
	 * menu
	 */
	public Menu bcDsNeMenu;

	/**
	 * Load both-cost-sensitive numeric decision system menu item
	 */
	public MenuItem loadBcDsNeMenuItem;

	/**
	 * Compute both-cost-sensitive optimal feature selection menu item. Only for
	 * one run.
	 */
	public MenuItem bcDsNeOptimalFeatureSubsetMenuItem;

	/**
	 * Compute both-cost-sensitive optimal feature selection menu item
	 */
	// public MenuItem bcDsNeOptimalFeatureSubsetsMenuItem;

	/**
	 * Compute both-cost-sensitive optimal feature selection menu item
	 */
	public MenuItem normalErrorLambdaFeatureSelectionMenuItem;

	/**
	 * Time comparision
	 */
	public MenuItem normalErrorFeatureSelectionTimeComparisonMenuItem;
	/********************* BC-DS-GC menu *********************/
	/**
	 * Both-cost-sensitive granular computing menu
	 */
	public Menu bcDsGcMenu;

	/**
	 * Load both-cost-sensitive numeric decision system menu item
	 */
	public MenuItem loadBcDsGcMenuItem;

	/**
	 * Compute both-cost-sensitive optimal feature selection menu item. Only for
	 * one run.
	 */
	public MenuItem bcDsGcOptimalFeatureSubsetMenuItem;

	/**
	 * Compute both-cost-sensitive optimal feature selection menu item based on
	 * adaptive misclassification cost. Only for one run.
	 */
	public MenuItem adaptiveBcDsGcOptimalFeatureSubsetMenuItem;

	/**
	 * Compute both-cost-sensitive optimal feature selection menu item
	 */
	public MenuItem granularComputingFeatureSelectionMenuItem;

	/************************* Refresh menu ************************/
	/**
	 * The refresh menu
	 */
	private Menu refreshMenu;

	/**
	 * The refresh test cost menu item
	 */
	public MenuItem refreshTestCostMenuItem;

	/************************* Edit menu ************************/
	/**
	 * The Edit menu
	 */
	private Menu editMenu;

	/**
	 * The Search menu item
	 */
	private MenuItem searchMenuItem;

	/**
	 * The Replace menu item
	 */
	private MenuItem replaceMenuItem;

	/**
	 * The Entorpy menu item
	 */
	private MenuItem entropyMenuItem;

	/************************* Help menu ************************/
	/**
	 * The Help menu
	 */
	private Menu helpMenu;

	/**
	 * The Help menu item
	 */
	private MenuItem helpMenuItem;

	/**
	 * The About menu item
	 */
	private MenuItem aboutMenuItem;

	/**
	 *************************** 
	 * Construct a menu bar and all menus, menu items.
	 *************************** 
	 */
	public CoserMenuBar() {
		super();

		/******************** File menu *********************/

		/*
		 * newMenuItem = new MenuItem("New project", new
		 * MenuShortcut(KeyEvent.VK_N)); newMenuItem.addActionListener(new
		 * NewProjectShower()); newMenuItem.setEnabled(true);
		 * 
		 * openMenuItem = new MenuItem("Open project", new
		 * MenuShortcut(KeyEvent.VK_O)); openMenuItem.addActionListener(new
		 * FileSelecter(null, FileDialog.LOAD)); openMenuItem.setEnabled(false);
		 * 
		 * saveMenuItem = new MenuItem("Save project", new
		 * MenuShortcut(KeyEvent.VK_S)); // saveMenuItem.addActionListener();
		 * saveMenuItem.setEnabled(false);
		 * 
		 * closeMenuItem = new MenuItem("Close project", new
		 * MenuShortcut(KeyEvent.VK_P)); closeMenuItem.addActionListener(new
		 * ProjectCloser()); closeMenuItem.setEnabled(false);
		 */

		exitMenuItem = new MenuItem("Exit Coser", new MenuShortcut(
				KeyEvent.VK_X));
		exitMenuItem.addActionListener(ApplicationShutdown.applicationShutdown);
		fileMenu = new Menu("File");

		// fileMenu.add(newMenuItem);
		// fileMenu.add(openMenuItem);
		// fileMenu.add(saveMenuItem);
		// fileMenu.add(closeMenuItem);
		// fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		add(fileMenu);

		/********************* DS Menu *********************/
		loadDsMenuItem = new MenuItem("Load DS");
		loadDsMenuItem.addActionListener(new LoadDsShower());
		loadDsMenuItem.setEnabled(true);

		normalizationMenuItem = new MenuItem("Normalization");
		normalizationMenuItem.addActionListener(new NormalizationShower());
		normalizationMenuItem.setEnabled(false);

		dsMenu = new Menu("DS");
		dsMenu.add(loadDsMenuItem);
		dsMenu.add(normalizationMenuItem);
		dsMenu.setEnabled(true);
		add(dsMenu);

		/********************* Nominal DS menu *********************/
		loadNominalDsMenuItem = new MenuItem("Load Nominal DS");
		loadNominalDsMenuItem.addActionListener(new LoadNominalDsShower());
		loadNominalDsMenuItem.setEnabled(true);

		positiveRegionMenuItem = new MenuItem("Positive region");
		positiveRegionMenuItem.addActionListener(new PositiveRegionShower());
		positiveRegionMenuItem.setEnabled(false);

		majorityMenuItem = new MenuItem("Majority instances");
		majorityMenuItem.addActionListener(new MajorityShower());
		majorityMenuItem.setEnabled(false);

		minimalReductMenuItem = new MenuItem("Minimal reduct (backtrack)");
		minimalReductMenuItem.addActionListener(new MinimalReductShower());
		minimalReductMenuItem.setEnabled(false);

		allReductsMenuItem = new MenuItem("All reducts");
		allReductsMenuItem.addActionListener(new AllReductsShower());
		allReductsMenuItem.setEnabled(false);

		allSubreductsMenuItem = new MenuItem("All sub-reducts");
		allSubreductsMenuItem.addActionListener(new AllSubreductsShower());
		allSubreductsMenuItem.setEnabled(false);

		nominalDsMenu = new Menu("Nominal DS");
		nominalDsMenu.add(loadNominalDsMenuItem);
		nominalDsMenu.add(positiveRegionMenuItem);
		nominalDsMenu.add(majorityMenuItem);
		nominalDsMenu.add(minimalReductMenuItem);
		nominalDsMenu.add(allReductsMenuItem);
		nominalDsMenu.add(allSubreductsMenuItem);
		nominalDsMenu.setEnabled(true);
		add(nominalDsMenu);

		/********************* TC-NDS menu *********************/
		loadTcNdsMenuItem = new MenuItem("Load TC-NDS");
		loadTcNdsMenuItem.addActionListener(new LoadTcNdsShower());
		loadTcNdsMenuItem.setEnabled(true);

		minimalTestCostReductionBacktrackMenuItem = new MenuItem(
				"Minimal test cost reduction (backtrack)");
		minimalTestCostReductionBacktrackMenuItem
				.addActionListener(new MinimalTestCostReductBacktrackShower());
		minimalTestCostReductionBacktrackMenuItem.setEnabled(false);

		minimalTestCostReductionMenuItem = new MenuItem(
				"Minimal test cost reduction (lambda-weighting)");
		minimalTestCostReductionMenuItem
				.addActionListener(new MinimalTestCostReductionShower());
		minimalTestCostReductionMenuItem.setEnabled(false);

		testCostConstraintReductionExhaustiveMenuItem = new MenuItem(
				"Test cost constraint reduction (exhaustive)");
		testCostConstraintReductionExhaustiveMenuItem
				.addActionListener(new TestCostConstraintReductionExhaustiveShower());
		testCostConstraintReductionExhaustiveMenuItem.setEnabled(false);

		testCostConstraintReductionMenuItem = new MenuItem(
				"Test cost constraint reduction");
		testCostConstraintReductionMenuItem
				.addActionListener(new TestCostConstraintReductionShower());
		testCostConstraintReductionMenuItem.setEnabled(false);

		testCostConstraintReductionMenuItem2 = new MenuItem(
				"Test cost constraint reduction (compared with optimal)");
		testCostConstraintReductionMenuItem2
				.addActionListener(new TestCostConstraintReductionShower2());
		testCostConstraintReductionMenuItem2.setEnabled(false);
		// reductionSettingsMenuItem = new MenuItem("Reduction Settings");
		// reductionSettingsMenuItem.addActionListener(new
		// ReductionSettingsShower());
		// reductionSettingsMenuItem.setEnabled(true);

		minimalTestCostReductionGAMenuItem = new MenuItem(
				"Minimal test cost reduction based on GA");
		minimalTestCostReductionGAMenuItem
				.addActionListener(new MinimalTestCostReductionGAShower());
		minimalTestCostReductionGAMenuItem.setEnabled(false);

		minimalTestCostReductionGAWithESMenuItem = new MenuItem(
				"Minimal test cost reduction based on GA with elitist select");
		minimalTestCostReductionGAWithESMenuItem
				.addActionListener(new MinimalTestCostReductionGAWithESShower());
		minimalTestCostReductionGAWithESMenuItem.setEnabled(false);

		testCostConstraintReductionGAMenuItem = new MenuItem(
				"Test cost constraint reduction based on GA");
		testCostConstraintReductionGAMenuItem
				.addActionListener(new TestCostConstraintReductionGAShower());
		testCostConstraintReductionGAMenuItem.setEnabled(false);

		// ACO
		minimalTestCostReductionACOMenu = new Menu(
				"Minimal test cost reduction based on ACO");
		minimalTestCostReductionACOMenu.setEnabled(false);
		oneExperimentACOMenuItem = new MenuItem("One experiment");
		mutipleExperimentACOMenuItem = new MenuItem("Mutiple experiments");
		comparisonExperimentACOMenuItem = new MenuItem("Comparison experiments");
		oneExperimentACOMenuItem
				.addActionListener(new oneExperimentACOShower());
		mutipleExperimentACOMenuItem
				.addActionListener(new mutipleExperimentACOShower());
		comparisonExperimentACOMenuItem
				.addActionListener(new comparisonExperimentACOShower());

		// Fast random
		minimalTestCostReductionFRMenu = new Menu(
				"Minimal test cost reduction with fast random");
		minimalTestCostReductionFRMenu.setEnabled(false);
		FRWithoutRestartMenuItem = new MenuItem("Without re-start");
		FRWithRestartMenuItem = new MenuItem("With re-start");
		FRWithoutRestartMenuItem
				.addActionListener(new FRWithoutRestartShower());
		FRWithRestartMenuItem.addActionListener(new FRWithRestartShower());

		simpleCommonTestCostConstraintReductionExhaustiveMenuItem = new MenuItem(
				"Simple common test cost constraint reduction (exhaustive)");
		simpleCommonTestCostConstraintReductionExhaustiveMenuItem
				.addActionListener(new SimpleCommonTestCostConstraintReductionExhaustiveShower());
		simpleCommonTestCostConstraintReductionExhaustiveMenuItem
				.setEnabled(false);

		simpleCommonTestCostConstraintReductionMenuItem = new MenuItem(
				"Simple common test cost constraint reduction");
		simpleCommonTestCostConstraintReductionMenuItem
				.addActionListener(new SimpleCommonTestCostConstraintReductionShower());
		simpleCommonTestCostConstraintReductionMenuItem.setEnabled(false); // reductionMenu.add(reductionSettingsMenuItem);

		positiveConstraintReductionMenuItem = new MenuItem(
				"Positive constraint reduction");
		positiveConstraintReductionMenuItem
				.addActionListener(new PositiveConstraintReductionShower());
		positiveConstraintReductionMenuItem.setEnabled(false);

		tcNdsMenu = new Menu("TC-NDS");
		tcNdsMenu.add(loadTcNdsMenuItem);
		tcNdsMenu.add(minimalTestCostReductionBacktrackMenuItem);
		tcNdsMenu.add(minimalTestCostReductionMenuItem);
		tcNdsMenu.add(testCostConstraintReductionExhaustiveMenuItem);
		tcNdsMenu.add(testCostConstraintReductionMenuItem);
		tcNdsMenu.add(testCostConstraintReductionMenuItem2);
		tcNdsMenu.add(minimalTestCostReductionGAMenuItem);
		tcNdsMenu.add(minimalTestCostReductionGAWithESMenuItem);
		tcNdsMenu.add(testCostConstraintReductionGAMenuItem);
		minimalTestCostReductionACOMenu.add(oneExperimentACOMenuItem);
		minimalTestCostReductionACOMenu.add(mutipleExperimentACOMenuItem);
		minimalTestCostReductionACOMenu.add(comparisonExperimentACOMenuItem);
		tcNdsMenu.add(minimalTestCostReductionACOMenu); // ACO

		minimalTestCostReductionFRMenu.add(FRWithoutRestartMenuItem);
		minimalTestCostReductionFRMenu.add(FRWithRestartMenuItem);
		tcNdsMenu.add(minimalTestCostReductionFRMenu); // Fast Random

		tcNdsMenu
				.add(simpleCommonTestCostConstraintReductionExhaustiveMenuItem);
		tcNdsMenu.add(simpleCommonTestCostConstraintReductionMenuItem);
		tcNdsMenu.add(positiveConstraintReductionMenuItem);

		tcNdsMenu.setEnabled(true);
		add(tcNdsMenu);

		/********************* TM-DS *********************/
		loadTmDsMenuItem = new MenuItem("Load TM-DS");
		loadTmDsMenuItem.addActionListener(new LoadTmDsShower());
		loadTmDsMenuItem.setEnabled(true);
		randomTmReductionMenuItem = new MenuItem("Random algorithm");
		randomTmReductionMenuItem.setEnabled(false);
		acoTmConstraintReductionMenuItem = new MenuItem(
				"Time constraint feature selection based on ACO");
		acoTmConstraintReductionMenuItem
				.addActionListener(new AcoTmConstraintReductionShower());
		acoTmConstraintReductionMenuItem.setEnabled(false);

		// Bee colony optimization to MTCR
		bcoMinimalCostReductionMenu = new Menu(
				"Minimal cost reduction based on BCO");
		bcoMinimalCostReductionMenu.setEnabled(false);
		oneExperimentBCOMenuItem = new MenuItem("One experiment");
		mutipleExperimentBCOMenuItem = new MenuItem(
				"Mutiple experiments for removing rate");
		mutipleExperimentBCONLMenuItem = new MenuItem(
				"Mutiple experiments for neighbors");
		mutipleExperimentBCOIterMenuItem = new MenuItem(
				"Mutiple experiments for iteration");
		comparisonExperimentBCOMenuItem = new MenuItem("Comparison experiments");
		oneExperimentBCOMenuItem
				.addActionListener(new BcoMinimalReductionOneExperimentShower());
		mutipleExperimentBCOMenuItem
				.addActionListener(new mutipleExperimentBCOShower());
		mutipleExperimentBCONLMenuItem
				.addActionListener(new mutipleExperimentBCONLShower());
		mutipleExperimentBCOIterMenuItem
				.addActionListener(new mutipleExperimentBCOIterShower());
		comparisonExperimentBCOMenuItem
				.addActionListener(new comparisonExperimentBCOShower());

		// artificial bee colony algorithm for numerical function
		abcMinimalCostReductionMenu = new Menu(
				"Minimal cost reduction based on ABC");
		abcMinimalCostReductionMenu.setEnabled(false);
		oneExperimentABCMenuItem = new MenuItem("One experiment");
		mutipleExperimentABCForLimitMenuItem = new MenuItem(
				"Mutiple experiments for limit");
		comparisonExperimentABCMenuItem = new MenuItem("Comparison experiments");
		mutipleExperimentABCForIterMenuItem = new MenuItem(
				"Mutiple experiments for iteration");
		oneExperimentABCMenuItem
				.addActionListener(new abcMinimalReductionOneExperimentShower());
		mutipleExperimentABCForLimitMenuItem
				.addActionListener(new mutipleExperimentABCForLimitShower());
		comparisonExperimentABCMenuItem
				.addActionListener(new comparisonExperimentABCShower());
		mutipleExperimentABCForIterMenuItem
				.addActionListener(new mutipleExperimentABCForIterShower());

		// Simulated bee colony algorithm to MTCR
		sbcMinimalCostReductionMenuItem = new MenuItem(
				"Minimal time cost reduction based on SBC");
		sbcMinimalCostReductionMenuItem
				.addActionListener(new SbcMinimalTimeCostReductionOneExperimentShower());
		sbcMinimalCostReductionMenuItem.setEnabled(false);

		twoObjectiveReductionMenuItem = new MenuItem("Two objective reduction");
		twoObjectiveReductionMenuItem
				.addActionListener(new TwoObjectiveReductionShower());
		twoObjectiveReductionMenuItem.setEnabled(false);

		tmDsMenu = new Menu("TM-DS");
		tmDsMenu.add(loadTmDsMenuItem);
		tmDsMenu.add(randomTmReductionMenuItem);
		tmDsMenu.add(acoTmConstraintReductionMenuItem);
		bcoMinimalCostReductionMenu.add(oneExperimentBCOMenuItem);
		bcoMinimalCostReductionMenu.add(mutipleExperimentBCOMenuItem);
		bcoMinimalCostReductionMenu.add(mutipleExperimentBCONLMenuItem);
		bcoMinimalCostReductionMenu.add(mutipleExperimentBCOIterMenuItem);
		bcoMinimalCostReductionMenu.add(comparisonExperimentBCOMenuItem);
		tmDsMenu.add(bcoMinimalCostReductionMenu); // BCO
		abcMinimalCostReductionMenu.add(oneExperimentABCMenuItem);
		abcMinimalCostReductionMenu.add(mutipleExperimentABCForLimitMenuItem);
		abcMinimalCostReductionMenu.add(comparisonExperimentABCMenuItem);
		abcMinimalCostReductionMenu.add(mutipleExperimentABCForIterMenuItem);
		tmDsMenu.add(abcMinimalCostReductionMenu);// basic ABC
		tmDsMenu.add(sbcMinimalCostReductionMenuItem);// SBC

		tmDsMenu.add(twoObjectiveReductionMenuItem);

		add(tmDsMenu);

		/********************* TC-DS-FN *********************/
		loadTcDsFnMenuItem = new MenuItem("Load TC-DS-FN");
		loadTcDsFnMenuItem.addActionListener(new LoadTcDsFnShower());
		loadTcDsFnMenuItem.setEnabled(true);

		fixedNeighborhoodReductionMenuItem = new MenuItem(
				"Fixed Neighborhood Reduction");
		fixedNeighborhoodReductionMenuItem
				.addActionListener(new FixedNeighborhoodReductionShower());
		fixedNeighborhoodReductionMenuItem.setEnabled(false);

		fixedNeighborhoodAllReductsMenuItem = new MenuItem(
				"All neighborhood reducts (backtrack)");
		fixedNeighborhoodAllReductsMenuItem
				.addActionListener(new FixedNeighborhoodAllReducsShower());
		fixedNeighborhoodAllReductsMenuItem.setEnabled(false);

		testCostFixedNeighborhoodReductionMenuItem = new MenuItem(
				"Test Cost Neighborhood Reduction");
		testCostFixedNeighborhoodReductionMenuItem
				.addActionListener(new TestCostFixedNeighborhoodReductionShower());
		testCostFixedNeighborhoodReductionMenuItem.setEnabled(false);

		optimalReductFnBkMenuItem = new MenuItem("optimal Reduct FN-BK");
		optimalReductFnBkMenuItem
				.addActionListener(new OptimalReductFnBkShower());
		optimalReductFnBkMenuItem.setEnabled(false);

		refreshTestCostFNMenuItem = new MenuItem("refresh FN-test costs");
		refreshTestCostFNMenuItem
				.addActionListener(new RefreshTestCostFNShower());
		refreshTestCostFNMenuItem.setEnabled(false);

		FNTimeComparisonMenuItem = new MenuItem("FN Time Comparison");
		FNTimeComparisonMenuItem
				.addActionListener(new FNTimeComparisonShower());
		FNTimeComparisonMenuItem.setEnabled(false);

		testCostFNGAMenuItem = new MenuItem("Test Cost DS-FN-GA");
		testCostFNGAMenuItem.addActionListener(new TestCostDSFNGAShower());
		testCostFNGAMenuItem.setEnabled(false);

		tcDsFnMenu = new Menu("TC-DS-FN");
		tcDsFnMenu.add(loadTcDsFnMenuItem);
		tcDsFnMenu.add(fixedNeighborhoodReductionMenuItem);
		tcDsFnMenu.add(fixedNeighborhoodAllReductsMenuItem);
		tcDsFnMenu.add(testCostFixedNeighborhoodReductionMenuItem);
		tcDsFnMenu.add(optimalReductFnBkMenuItem);
		tcDsFnMenu.add(refreshTestCostFNMenuItem);
		tcDsFnMenu.add(FNTimeComparisonMenuItem);
		tcDsFnMenu.add(testCostFNGAMenuItem);

		add(tcDsFnMenu);

		/********************* TC-DS-ER *********************/
		loadTcDsErMenuItem = new MenuItem("Load TC-DS-ER");
		loadTcDsErMenuItem.addActionListener(new LoadTcDsErShower());
		loadTcDsErMenuItem.setEnabled(true);

		errorRangeAllReductsMenuItem = new MenuItem("All reducts (backtrack)");
		errorRangeAllReductsMenuItem
				.addActionListener(new TcDsErAllReductsExhaustiveShower());
		errorRangeAllReductsMenuItem.setEnabled(false);

		errorRangeOptimalReductMenuItem = new MenuItem(
				"Optimal reduct (backtrack)");
		errorRangeOptimalReductMenuItem
				.addActionListener(new TcDsErOptimalReductBacktrackShower());
		errorRangeOptimalReductMenuItem.setEnabled(false);

		errorRangeLambdaReductionMenuItem = new MenuItem(
				"Lambda weighted reduction");
		errorRangeLambdaReductionMenuItem
				.addActionListener(new ErrorRangeLambdaReductionShower());
		errorRangeLambdaReductionMenuItem.setEnabled(false);

		errorRangeTimeComparisonMenuItem = new MenuItem("Time comparison");
		errorRangeTimeComparisonMenuItem
				.addActionListener(new ErrorRangeTimeComparisonShower());
		errorRangeTimeComparisonMenuItem.setEnabled(false);

		refreshTestCostMenuItem = new MenuItem("Refresh test costs");
		refreshTestCostMenuItem.addActionListener(new RefreshTestCostShower());
		refreshTestCostMenuItem.setEnabled(false);

		tcDsErMenu = new Menu("TC-DS-ER");
		tcDsErMenu.add(loadTcDsErMenuItem);
		tcDsErMenu.add(errorRangeAllReductsMenuItem);
		tcDsErMenu.add(errorRangeOptimalReductMenuItem);
		tcDsErMenu.add(errorRangeLambdaReductionMenuItem);
		tcDsErMenu.add(errorRangeTimeComparisonMenuItem);
		tcDsErMenu.add(refreshTestCostMenuItem);
		tcDsErMenu.setEnabled(true);

		add(tcDsErMenu);
		/********************* TC-DS-NE *********************/
		loadTcDsNeMenuItem = new MenuItem("Load TC-DS-NE");
		loadTcDsNeMenuItem.addActionListener(new LoadTcDsNeShower());
		loadTcDsNeMenuItem.setEnabled(true);

		normalErrorAllReductsMenuItem = new MenuItem("All reducts (backtrack)");
		normalErrorAllReductsMenuItem
				.addActionListener(new TcDsNeAllReductsExhaustiveShower());
		normalErrorAllReductsMenuItem.setEnabled(false);

		normalErrorOptimalReductMenuItem = new MenuItem(
				"Optimal reduct (backtrack)");
		normalErrorOptimalReductMenuItem
				.addActionListener(new TcDsNeOptimalReductBacktrackShower());
		normalErrorOptimalReductMenuItem.setEnabled(false);

		normalErrorDeltaReductionMenuItem = new MenuItem(
				"Delta weighted reduction");
		normalErrorDeltaReductionMenuItem
				.addActionListener(new NormalErrorDeltaReductionShower());
		normalErrorDeltaReductionMenuItem.setEnabled(false);

		normalErrorLambdaReductionMenuItem = new MenuItem(
				"Lamda weighted reduction");
		normalErrorLambdaReductionMenuItem
				.addActionListener(new NormalErrorLambdaReductionShower());
		normalErrorLambdaReductionMenuItem.setEnabled(false);

		normalErrorTimeComparisonMenuItem = new MenuItem("Time comparison");
		normalErrorTimeComparisonMenuItem
				.addActionListener(new NormalErrorTimeComparisonShower());
		normalErrorTimeComparisonMenuItem.setEnabled(false);

		tcDsNeMenu = new Menu("TC-DS-NE");
		tcDsNeMenu.add(loadTcDsNeMenuItem);
		tcDsNeMenu.add(normalErrorAllReductsMenuItem);
		tcDsNeMenu.add(normalErrorOptimalReductMenuItem);
		tcDsNeMenu.add(normalErrorDeltaReductionMenuItem);
		tcDsNeMenu.add(normalErrorLambdaReductionMenuItem);
		tcDsNeMenu.add(normalErrorTimeComparisonMenuItem);
		tcDsNeMenu.setEnabled(true);

		add(tcDsNeMenu);

		/********************* BC-NDS *********************/
		loadBcNdsMenuItem = new MenuItem("Load BC-NDS");
		loadBcNdsMenuItem.addActionListener(new LoadBcNdsShower());
		loadBcNdsMenuItem.setEnabled(true);

		bcNdsOptimalReductMenuItem = new MenuItem("Optimal reduct (one run)");
		bcNdsOptimalReductMenuItem
				.addActionListener(new BcNdsOptimalReductBacktrackShower());
		bcNdsOptimalReductMenuItem.setEnabled(false);

		bcNdsOptimalReductsMenuItem = new MenuItem(
				"Optimal reducts (backtrack)");
		bcNdsOptimalReductsMenuItem
				.addActionListener(new BcNdsOptimalReductsBacktrackShower());
		bcNdsOptimalReductsMenuItem.setEnabled(false);

		bcDtLambdaMenuItem = new MenuItem("CC-DT parameter comparison");
		bcDtLambdaMenuItem
				.addActionListener(new CostSensitiveDecisionTreeShower());
		bcDtLambdaMenuItem.setEnabled(false);

		// bcDtClassBalanceMenuItem = new MenuItem("CS-DT class balance");
		// bcDtClassBalanceMenuItem.addActionListener(new
		// CostSensitiveDecisionTreeBalanceShower());
		// bcDtClassBalanceMenuItem.setEnabled(false);

		costSensitiveDecisionTreePruneMenuItem = new MenuItem(
				"CC-DT prune comparison");
		costSensitiveDecisionTreePruneMenuItem
				.addActionListener(new CostSensitiveDecisionTreePruneShower());
		costSensitiveDecisionTreePruneMenuItem.setEnabled(false);
		costSensitiveNominalDecisionTreeC45 = new Menu("Cost Tree C4.5");
		costSensitiveNominalDecisionTreeC45Once = new MenuItem(
				"Generate Cost C4.5 Tree");
		costSensitiveNominalDecisionTreeC45Once
				.addActionListener(new CostSensitiveDecisionTreeC45OnceShower());
		costSensitiveNominalDecisionTreeC45.setEnabled(false);
		costSensitiveNominalDecisionTreeC45Batch = new MenuItem(
				"Generate batch Cost C4.5 Trees with different parameters");
		costSensitiveNominalDecisionTreeC45Batch
				.addActionListener(new CostSensitiveDecisionTreeC45BatchShower());

		bcNdsMenu = new Menu("BC-NDS");
		bcNdsMenu.add(loadBcNdsMenuItem);
		bcNdsMenu.add(bcNdsOptimalReductMenuItem);
		bcNdsMenu.add(bcNdsOptimalReductsMenuItem);
		bcNdsMenu.add(bcDtLambdaMenuItem);
		bcNdsMenu.add(costSensitiveDecisionTreePruneMenuItem);
		bcNdsMenu.add(costSensitiveNominalDecisionTreeC45);
		costSensitiveNominalDecisionTreeC45
				.add(costSensitiveNominalDecisionTreeC45Once);
		costSensitiveNominalDecisionTreeC45
				.add(costSensitiveNominalDecisionTreeC45Batch);

		add(bcNdsMenu);

		/********************* BC-UDS *********************/
		loadBcUdsMenuItem = new MenuItem("Load BC-UDS");
		loadBcUdsMenuItem.addActionListener(new LoadBcUdsShower());
		loadBcUdsMenuItem.setEnabled(true);

		bcUdsMenu = new Menu("BC-UDS");
		bcUdsMenu.add(loadBcUdsMenuItem);

		// The sub menu of the cost-sensitive decision tree C4.5.
		costSensitiveNumericDecisionTreeC45 = new Menu(
				"Cost-sensitive numberic decision tree");// C4.5
		bcUdsMenu.add(costSensitiveNumericDecisionTreeC45);
		costSensitiveNumericDecisionTreeC45.setEnabled(false);

		// The menu items of the cost-sensitive decision tree C4.5.
		costSensitiveNumbericDecisionTreeC45Once = new MenuItem(
				"Creat a numeric decision tree");
		costSensitiveNumbericDecisionTreeC45Once
				.addActionListener(new CostSensitiveNumericDecisionTreeC45OnceShower());
		costSensitiveNumbericDecisionTreeC45Experiment = new MenuItem(
				"Cost-sensitive numberic decision tree experiment");// C4.5
		costSensitiveNumbericDecisionTreeC45Experiment
				.addActionListener(new CostSensitiveNumericDecisionTreeC45Shower());

		costSensitiveNumericDecisionTreeC45
				.add(costSensitiveNumbericDecisionTreeC45Once);
		costSensitiveNumericDecisionTreeC45
				.add(costSensitiveNumbericDecisionTreeC45Experiment);// C4.5

		add(bcUdsMenu);

		/********************* BC-DS-NE *********************/
		loadBcDsNeMenuItem = new MenuItem("Load BC-DS-NE");
		loadBcDsNeMenuItem.addActionListener(new LoadBcDsNeShower());
		loadBcDsNeMenuItem.setEnabled(true);

		bcDsNeOptimalFeatureSubsetMenuItem = new MenuItem(
				"Optimal reduct (one run)");
		bcDsNeOptimalFeatureSubsetMenuItem
				.addActionListener(new BcDsNeOptimalReductBacktrackShower());
		bcDsNeOptimalFeatureSubsetMenuItem.setEnabled(false);

		bcDsNeOptimalFeatureSubsetMenuItem = new MenuItem(
				"Optimal reducts (backtrack)");
		bcDsNeOptimalFeatureSubsetMenuItem
				.addActionListener(new BcDsNeOptimalReductsBacktrackShower());
		bcDsNeOptimalFeatureSubsetMenuItem.setEnabled(false);

		normalErrorLambdaFeatureSelectionMenuItem = new MenuItem(
				"Lambda Weighted Feature Selection");
		normalErrorLambdaFeatureSelectionMenuItem
				.addActionListener(new NormalErrorLambdaFeatureSelectionShower());
		normalErrorLambdaFeatureSelectionMenuItem.setEnabled(false);

		normalErrorFeatureSelectionTimeComparisonMenuItem = new MenuItem(
				"Time comparison");
		normalErrorFeatureSelectionTimeComparisonMenuItem
				.addActionListener(new NormalErrorFeatureSelectionTimeComparisonShower());
		normalErrorFeatureSelectionTimeComparisonMenuItem.setEnabled(false);

		bcDsNeMenu = new Menu("BC-DS-NE");
		bcDsNeMenu.add(loadBcDsNeMenuItem);
		bcDsNeMenu.add(bcDsNeOptimalFeatureSubsetMenuItem);
		bcDsNeMenu.add(bcDsNeOptimalFeatureSubsetMenuItem);
		bcDsNeMenu.add(normalErrorLambdaFeatureSelectionMenuItem);
		bcDsNeMenu.add(normalErrorFeatureSelectionTimeComparisonMenuItem);
		add(bcDsNeMenu);
		/********************* BC-DS-GC *********************/
		loadBcDsGcMenuItem = new MenuItem("Load BC-DS-GC");
		loadBcDsGcMenuItem.addActionListener(new LoadBcDsGcShower());
		loadBcDsGcMenuItem.setEnabled(true);

		bcDsGcOptimalFeatureSubsetMenuItem = new MenuItem(
				"Feature selection based on granular computing");
		bcDsGcOptimalFeatureSubsetMenuItem
				.addActionListener(new BcDsGcOptimalReductsBacktrackShower());
		bcDsGcOptimalFeatureSubsetMenuItem.setEnabled(false);

		adaptiveBcDsGcOptimalFeatureSubsetMenuItem = new MenuItem(
				"Feature selection based on adaptive MC");
		adaptiveBcDsGcOptimalFeatureSubsetMenuItem
				.addActionListener(new AdaptiveBcDsGcOptimalReductsBacktrackShower());
		adaptiveBcDsGcOptimalFeatureSubsetMenuItem.setEnabled(false);

		granularComputingFeatureSelectionMenuItem = new MenuItem(
				"Minimal cost feature selection based on GrC");
		granularComputingFeatureSelectionMenuItem
				.addActionListener(new GranularComputingFeatureSelectionShower());
		granularComputingFeatureSelectionMenuItem.setEnabled(false);

		// normalErrorFeatureSelectionTimeComparisonMenuItem = new
		// MenuItem("Time comparison");
		// normalErrorFeatureSelectionTimeComparisonMenuItem
		// .addActionListener(new
		// NormalErrorFeatureSelectionTimeComparisonShower());
		// normalErrorFeatureSelectionTimeComparisonMenuItem.setEnabled(false);

		bcDsGcMenu = new Menu("BC-DS-GC");
		bcDsGcMenu.add(loadBcDsGcMenuItem);
		bcDsGcMenu.add(bcDsGcOptimalFeatureSubsetMenuItem);
		bcDsGcMenu.add(adaptiveBcDsGcOptimalFeatureSubsetMenuItem);
		bcDsGcMenu.add(granularComputingFeatureSelectionMenuItem);
		// bcDsGcMenu.add(normalErrorFeatureSelectionTimeComparisonMenuItem);
		add(bcDsGcMenu);

		/********************* Refresh *********************/
		refreshTestCostMenuItem = new MenuItem("Refresh test cost");
		// refreshTestCostMenuItem.addActionListener(new
		// RefreshTestCostShower());
		refreshTestCostMenuItem.setEnabled(false);

		refreshMenu = new Menu("Refresh");
		refreshMenu.add(refreshTestCostMenuItem);

		add(refreshMenu);

		/********************* Edit menu *********************/
		searchMenuItem = new MenuItem("Search");
		searchMenuItem.addActionListener(new SearchShower());

		replaceMenuItem = new MenuItem("Replace");
		replaceMenuItem.addActionListener(new ReplaceShower());

		entropyMenuItem = new MenuItem("Entropy");
		entropyMenuItem.addActionListener(new EntropyShower());

		editMenu = new Menu("Edit");
		editMenu.add(searchMenuItem);
		editMenu.add(replaceMenuItem);
		editMenu.add(entropyMenuItem);

		add(editMenu);

		/********************* Help menu *********************/
		helpMenuItem = new MenuItem("Help");
		helpMenuItem.addActionListener(HelpDialog.helpDialog);

		aboutMenuItem = new MenuItem("About");
		aboutMenuItem.addActionListener(HelpDialog.aboutDialog);
		helpMenu = new Menu("Help");
		helpMenu.add(helpMenuItem);
		helpMenu.add(aboutMenuItem);

		setHelpMenu(helpMenu);
		setFont(GUICommon.MY_FONT);
	}// Of the constructor

	/**
	 *************************** 
	 * Reset menus to enable only these initial ones.
	 *************************** 
	 */
	public void resetMenus() {
		normalizationMenuItem.setEnabled(false);
		positiveRegionMenuItem.setEnabled(false);
		majorityMenuItem.setEnabled(false);
		minimalReductMenuItem.setEnabled(false);
		allReductsMenuItem.setEnabled(false);
		allSubreductsMenuItem.setEnabled(false);
		minimalTestCostReductionBacktrackMenuItem.setEnabled(false);
		minimalTestCostReductionMenuItem.setEnabled(false);
		testCostConstraintReductionExhaustiveMenuItem.setEnabled(false);
		testCostConstraintReductionMenuItem.setEnabled(false);
		testCostConstraintReductionMenuItem2.setEnabled(false);
		// reductionSettingsMenuItem.setEnabled(false);
		minimalTestCostReductionGAMenuItem.setEnabled(false);
		testCostConstraintReductionGAMenuItem.setEnabled(false);
		simpleCommonTestCostConstraintReductionExhaustiveMenuItem
				.setEnabled(false);
		simpleCommonTestCostConstraintReductionMenuItem.setEnabled(false);
		fixedNeighborhoodReductionMenuItem.setEnabled(false);
		testCostFixedNeighborhoodReductionMenuItem.setEnabled(false);
		errorRangeAllReductsMenuItem.setEnabled(false);
		errorRangeOptimalReductMenuItem.setEnabled(false);
		errorRangeLambdaReductionMenuItem.setEnabled(false);
		errorRangeTimeComparisonMenuItem.setEnabled(false);
		bcNdsOptimalReductMenuItem.setEnabled(false);
		bcNdsOptimalReductsMenuItem.setEnabled(false);
		bcDtLambdaMenuItem.setEnabled(false);
		costSensitiveDecisionTreePruneMenuItem.setEnabled(false);
		// loadBcUdsMenuItem.setEnabled(false);
		// refreshTestCostMenuItem.setEnabled(false);
	}// Of resetMenus

	/**
	 *************************** 
	 * Show the new project dialog.
	 *************************** 
	 * private class NewProjectShower implements ActionListener{ public void
	 * actionPerformed(ActionEvent ae){
	 * NewProjectDialog.newProjectDialog.setVisible(true); }//Of actionPerformed
	 * }//Of NewProjectShower
	 */

	/**
	 *************************** 
	 * Show the load decision system dialog.
	 *************************** 
	 */
	private class LoadNominalDsShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			LoadNominalDsDialog.loadNominalDsDialog.setVisible(true);
		}// Of actionPerformed
	}// Of LoadNominalDsShower

	/**
	 *************************** 
	 * Show the positive region dialog.
	 *************************** 
	 */
	private class PositiveRegionShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			NominalDecisionSystem currentSystem = CoserProject.currentProject.currentNds;
			String message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com";
			message += "\r\nThere are ";
			int total = currentSystem.numInstances();
			message += total + " instances in the decision system, \r\n";
			int pos = 0;

			try {
				pos = currentSystem.positiveRegion();
				message += pos + " of which are in the positive region.";
			} catch (Exception ee) {
				message = ee.toString();
			}

			ProgressDialog.progressDialog.setMessageAndShow(message);

		}// Of actionPerformed
	}// Of PositiveRegionShower

	/**
	 *************************** 
	 * Show the majority dialog.
	 *************************** 
	 */
	private class MajorityShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			NominalDecisionSystem currentSystem = CoserProject.currentProject.currentNds;
			String message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com";
			message += "\r\nThere are ";
			int total = currentSystem.numInstances();
			message += total + " instances in the decision system, \r\n";

			int majority = 0;
			try {
				majority = currentSystem.majority();
				message += majority + " of which can be classified correctly.";
			} catch (Exception ee) {
				message = ee.toString();
			}

			ProgressDialog.progressDialog.setMessageAndShow(message);

		}// Of actionPerformed
	}// Of MajorityShower

	/**
	 *************************** 
	 * Show the minimal reduct dialog.
	 *************************** 
	 */
	private class MinimalReductShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			NominalDecisionSystem currentSystem = CoserProject.currentProject.currentNds;
			String message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com";
			message += "\r\nThere are ";
			int total = currentSystem.numAttributes() - 1;
			message += total + " tests in the decision system, \r\n";
			// int pos = 0;

			ProgressDialog.progressDialog.setMessageAndShow(message);
			try {
				ProgressDialog.progressDialog
						.appendMessage("\r\nComputing...\r\n");
				currentSystem.computeOptimalReductBacktrack();
				boolean[] minimalReduct = currentSystem.getOptimalReduct();
				ProgressDialog.progressDialog.appendMessage(currentSystem
						.getRunTimeInformation());
				ProgressDialog.progressDialog
						.appendMessage("\r\n"
								+ SimpleTool
										.booleanArrayToAttributeSetString(minimalReduct));
			} catch (Exception ee) {
				message = ee.toString();
				ProgressDialog.progressDialog.setMessageAndShow(message);
			}// Of try

		}// Of actionPerformed
	}// Of MinimalReductShower

	/**
	 *************************** 
	 * Show the all reducts dialog.
	 *************************** 
	 */
	private class AllReductsShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			AllReductsDialog.allReductsDialog.setVisible(true);
		}// Of actionPerformed
	}// Of AllReductsShower

	/**
	 *************************** 
	 * Show the all sub-reducts dialog.
	 *************************** 
	 */
	private class AllSubreductsShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			AllSubreductsDialog.allSubreductsDialog.setVisible(true);
		}// Of actionPerformed
	}// Of AllSubreductsShower

	/**
	 *************************** 
	 * Show the load decision system with any data dialog.
	 *************************** 
	 */
	private class LoadDsShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			LoadDsDialog.loadDsDialog.setVisible(true);
		}// Of actionPerformed
	}// Of LoadDsShower

	/**
	 *************************** 
	 * Show the normalization dialog.
	 *************************** 
	 */
	private class NormalizationShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			NormalizationDialog.normalizationDialog.setVisible(true);
		}// Of actionPerformed
	}// Of NormalizationShower

	/**
	 *************************** 
	 * Show the load test-cost-sensitive decision system dialog.
	 *************************** 
	 */
	private class LoadTcNdsShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			LoadTcNdsDialog.loadTcNdsDialog.setVisible(true);
		}// Of actionPerformed
	}// Of LoadTcNdsShower

	/**
	 *************************** 
	 * Show the dialog. Unfininshed.
	 *************************** 
	 * private class MoreTestsShower implements ActionListener{ public void
	 * actionPerformed(ActionEvent ae){
	 * NormalizationDialog.normalizationDialog.setVisible(true); }//Of
	 * actionPerformed }//Of MoreTestsShower
	 */

	/**
	 *************************** 
	 * Show the minimal reduct dialog.
	 *************************** 
	 */
	private class MinimalTestCostReductBacktrackShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			TestCostNominalDecisionSystem currentSystem = CoserProject.currentProject.currentTcNds;
			String message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com";
			try {
				message += "\r\nThe test cost array is: "
						+ SimpleTool
								.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
			} catch (Exception ee) {
				message = ee.toString();
			}
			ProgressDialog.progressDialog.setMessageAndShow(message);
			try {
				ProgressDialog.progressDialog
						.appendMessage("\r\nComputing...\r\n");
				currentSystem.computeOptimalReductBacktrack();
				boolean[] minimalTestCostReduct = currentSystem
						.getOptimalReduct();
				ProgressDialog.progressDialog
						.appendMessage("\r\nThe optimal reduct is: "
								+ SimpleTool
										.booleanArrayToAttributeSetString(minimalTestCostReduct));
				ProgressDialog.progressDialog
						.appendMessage("\r\nThe test cost is: "
								+ currentSystem.getOptimalReductTestCost());
				ProgressDialog.progressDialog.appendMessage("\r\n"
						+ currentSystem.getRunTimeInformation());
			} catch (Exception ee) {
				message = ee.toString();
				ProgressDialog.progressDialog.setMessageAndShow(message);
			}// Of try

		}// Of actionPerformed
	}// Of MinimalTestCostReductBacktrackShower

	/**
	 *************************** 
	 * Show the minimal test cost reduction dialog.
	 *************************** 
	 */
	private class MinimalTestCostReductionShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			MinimalTestCostReductionDialog.minimalTestCostReductionDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of MinimalTestCostReductionShower

	/**
	 *************************** 
	 * Show the test cost constraint reduction dialog.
	 *************************** 
	 */
	private class TestCostConstraintReductionShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			TestCostConstraintReductionDialog.testCostConstraintReductionDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of TestCostConstraintReductionShower

	/**
	 *************************** 
	 * Show the test cost constraint reduction dialog.
	 *************************** 
	 */
	private class TestCostConstraintReductionShower2 implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			TestCostConstraintReductionCompareWithOptimalDialog.testCostConstraintReductionDialog2
					.setVisible(true);
		}// Of actionPerformed
	}// Of TestCostConstraintReductionShower2

	/**
	 *************************** 
	 * Show the test cost constraint reduction dialog.
	 *************************** 
	 */
	private class TestCostConstraintReductionExhaustiveShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			TestCostConstraintReductionExhaustiveDialog.testCostConstraintReductionExhaustiveDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of MinimalTestCostReductionShower

	/**
	 *************************** 
	 * Show the minimal test cost reduction based on GA dialog.
	 *************************** 
	 */
	private class MinimalTestCostReductionGAShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			MinimalTestCostReductionGADialog.minimalTestCostReductionGADialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of MinimalTestCostReductionBasedGAShower/**

	/**
	 *************************** 
	 * Show the minimal test cost reduction based on GA with elitist selcct
	 * dialog.
	 *************************** 
	 */
	private class MinimalTestCostReductionGAWithESShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			MinimalTestCostReductionGAWithESDialog.minimalTestCostReductionGAWithESDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of MinimalTestCostReductionGAShowerWithES/**

	/**
	 *************************** 
	 * Show the one experiment of the minimal test cost reduction based on ACO
	 * dialog.
	 *************************** 
	 */
	private class oneExperimentACOShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			MinimalTestCostReductionACODialog.minimalTestCostReductionACODialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of MinimalTestCostReductionBasedACOShower/** ACO

	/**
	 *************************** 
	 * Show the mutiple experiment of the minimal test cost reduction based on
	 * ACO dialog.
	 *************************** 
	 */
	private class mutipleExperimentACOShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			MinTestCostACOMutipleDialog.minTestCostACOMutipleDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of MinimalTestCostReductionBasedACOShower/** ACO

	/**
	 *************************** 
	 * Show comparison experiments of the minimal test cost reduction based on
	 * ACO dialog.
	 *************************** 
	 */
	private class comparisonExperimentACOShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			ComparisionACODialog.comparisionACODialog.setVisible(true);
		}// Of actionPerformed
	}// Of MinimalTestCostReductionBasedACOShower/** ACO

	/**
	 *************************** 
	 * Show the without re-start of the minimal test cost reduction based on FR
	 * dialog.
	 *************************** 
	 */
	private class FRWithoutRestartShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			MinimalTestCostReductionFRDialog.minimalTestCostReductionFRDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of MinimalTestCostReductionBasedFRShower/** FR

	/**
	 *************************** 
	 * Show the with re-start of the minimal test cost reduction based on FR
	 * dialog.
	 *************************** 
	 */
	private class FRWithRestartShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			MinimalTestCostReductionFRWithRestartDialog.minimalTestCostReductionFRWithRestartDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of MinimalTestCostReductionBasedFRShower/** FR

	/**
	 *************************** 
	 * Show the simple common test cost constraint reduction exhaustive
	 * algorithm dialog.
	 *************************** 
	 */
	private class SimpleCommonTestCostConstraintReductionExhaustiveShower
			implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			SimpleCommonTestCostConstraintReductionExhaustiveDialog.simpleCommonTestCostConstraintReductionExhaustiveDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of SimpleCommonTestCostConstraintReductionExhaustiveShower

	/**
	 *************************** 
	 * Show the simple common test cost constraint reduction exhaustive
	 * algorithm dialog.
	 *************************** 
	 */
	private class SimpleCommonTestCostConstraintReductionShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			SimpleCommonTestCostConstraintReductionDialog.simpleCommonTestCostConstraintReductionDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of SimpleCommonTestCostConstraintReductionShower

	/**
	 *************************** 
	 * Show the test cost constraint reduction based on GA dialog.
	 *************************** 
	 */
	private class TestCostConstraintReductionGAShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			TestCostConstraintReductionGADialog.testCostConstraintReductionGADialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of TestCostConstraintReductionGAShower/**

	/**
	 *************************** 
	 * Show the positive constraint reduction dialog.
	 *************************** 
	 */
	private class PositiveConstraintReductionShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			PositiveConstraintReductionDialog.positiveConstraintReductionDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of PositiveConstraintReductionShower

	/**
	 *************************** 
	 * Show the load test-cost-sensitive decision system dialog.
	 *************************** 
	 */
	private class LoadTmDsShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			LoadTmDsDialog.loadTmDsDialog.setVisible(true);
		}// Of actionPerformed
	}// Of LoadTmDsShower

	/**
	 *************************** 
	 * Show time constraint feature selection based on ACO dialog.
	 *************************** 
	 */
	private class AcoTmConstraintReductionShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			TimeConstraintFeatureSelectionAcoDialog.timeConstraintFeatureSelectionAcoDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of MinimalTestCostReductionBasedACOShower/** ACO

	/**
	 *************************** 
	 * Show minimal cost reduction based on BCO dialog.
	 *************************** 
	 */
	private class BcoMinimalReductionShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			BcoMinimalCostReductionDialog.bcoMinimalCostReductionDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of BcoMinimalReductionShower/** BCO

	/**
	 *************************** 
	 * Show minimal cost reduction based on BCO dialog.
	 *************************** 
	 */
	private class BcoMinimalReductionOneExperimentShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			BcoMinimalTimeCostReductionFirstDialog.bcoMinimalTimeCostReductionFirstDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of BcoMinimalReductionOneExperimentShower/** BCO

	/**
	 *************************** 
	 * Show the multiple experiments of the minimal time cost reduction based on
	 * BCO dialog.
	 *************************** 
	 */
	private class mutipleExperimentBCOShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			MinTimeCostBCOMutipleDialog.minTimeCostBCOMutipleDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of mutipleExperimentBCOShower/** BCO

	/**
	 *************************** 
	 * Show the multiple experiments of the minimal time cost reduction based on
	 * BCO dialog.
	 *************************** 
	 */
	private class mutipleExperimentBCONLShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			MinTimeCostBCONLMutipleDialog.minTimeCostBCONLMutipleDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of mutipleExperimentBCONLShower/** BCO

	/**
	 *************************** 
	 * Show the multiple experiments of the minimal time cost reduction based on
	 * BCO dialog.
	 *************************** 
	 */
	private class mutipleExperimentBCOIterShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			MinTimeCostBCOGNMutipleDialog.minTimeCostBCOGNMutipleDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of mutipleExperimentBCOIterShower/** BCO

	/**
	 *************************** 
	 * Show comparison experiments of the minimal test cost reduction based on
	 * ACO dialog.
	 *************************** 
	 */
	private class comparisonExperimentBCOShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			ComparisionBCOBasedOnCompetitiveParaDialog.comparisionBCOBasedOnCompetitiveParaDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of comparisonExperimentBCOShower/** BCO

	/**
	 *************************** 
	 * Show minimal cost reduction based on ABC dialog.
	 *************************** 
	 */
	private class abcMinimalReductionOneExperimentShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			AbcMinimalTimeCostReductionFirstDialog.abcMinimalTimeCostReductionFirstDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of abcMinimalReductionOneExperimentShower/** ABC

	/**
	 *************************** 
	 * Show the multiple experiments of the minimal time cost reduction based on
	 * BCO dialog.
	 *************************** 
	 */
	private class mutipleExperimentABCForLimitShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			MinTimeCostABCMutipleForLimitDialog.minTimeCostABCMutipleForLimitDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of mutipleExperimentABCShower/** ABC

	/**
	 *************************** 
	 * Show comparison experiments of the minimal test cost reduction based on
	 * ACO dialog.
	 *************************** 
	 */
	private class comparisonExperimentABCShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			ComparisionABCBasedOnCompetitiveParaDialog.comparisionABCBasedOnCompetitiveParaDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of comparisonExperimentABCShower/** ABC

	/**
	 *************************** 
	 * Show the multiple experiments of the minimal time cost reduction based on
	 * BCO dialog.
	 *************************** 
	 */
	private class mutipleExperimentABCForIterShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			MinTimeCostABCMutipleForIterDialog.minTimeCostABCMutipleForIterDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of mutipleExperimentABCForIterShower/** ABC

	/**
	 *************************** 
	 * Show Minimal time cost reduction based on SBC dialog.
	 *************************** 
	 */
	private class SbcMinimalTimeCostReductionOneExperimentShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			SbcMinimalTimeCostReductionFirstDialog.sbcMinimalTimeCostReductionFirstDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of SbcMinimalTimeCostReductionOneExperimentShower/** SBC

	/**
	 *************************** 
	 * Show Minimal time cost reduction with basic ABC dialog.
	 *************************** 
	 */
	private class AbcFormalMinimalTimeCostReductionOneExperimentShower
			implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			AbcMinimalTimeCostReductionFirstDialog.abcMinimalTimeCostReductionFirstDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of AbcFormalMinimalTimeCostReductionOneExperimentShower/** ABC

	/**
	 *************************** 
	 * Show the multiple objective reduction based on GA. dialog.
	 *************************** 
	 */
	private class TwoObjectiveReductionShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			TwoObjectiveReductionDialog.twoObjectiveReductionDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of TwoObjectiveReductionShower

	/**
	 *************************** 
	 * Show the load test-cost-sensitive decision system dialog.
	 *************************** 
	 */
	private class LoadTcDsFnShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			LoadTcDsFnDialog.loadTcDsFnDialog.setVisible(true);
			optimalReductFnBkMenuItem.setEnabled(true);
			refreshTestCostFNMenuItem.setEnabled(true);
			FNTimeComparisonMenuItem.setEnabled(true);
			testCostFNGAMenuItem.setEnabled(true);
		}// Of actionPerformed
	}// Of LoadTcDsFnShower

	/**
	 *************************** 
	 * Show the neighborhood reduction dialog.
	 *************************** 
	 */
	private class FixedNeighborhoodReductionShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			FixedNeighborhoodReductionDialog.fixedNeighborhoodReductionDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of FixedNeighborhoodReductionShower

	/**
	 *************************** 
	 * Show the test cost distribution dialog for neighborhood systems.
	 *************************** 
	 */
	private class TestCostFixedNeighborhoodReductionShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			TestCostDecisionSystemFixedNeighborhoodDialog.testCostDecisionSystemFixedNeighborhoodDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of TestCostFixedNeighborhoodReductionShower

	/**
	 *************************** 
	 * Show all the neighborhood reduction dialog.
	 *************************** 
	 */
	private class FixedNeighborhoodAllReducsShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			FixedNeighborhoodAllReductsDialog.fixedNeighborhoodAllReductsDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of FixedNeighborhoodReductionShower

	/**
	 *************************** 
	 * Compute the FN-optimal reduct by backtracking directly.
	 *************************** 
	 */
	private class OptimalReductFnBkShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			OptimalReductFNBkDialog.optimalReductFNBkDialog.setVisible(true);
		}// Of actionPerformed
	}// Of OptimalReductFnBkShower

	/**
	 *************************** 
	 * Refresh test cost.
	 *************************** 
	 */
	private class RefreshTestCostFNShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			TestCostDecisionSystemFixedNeighborhood currentSystem = CoserProject.currentProject.currentTcDsFn;
			String message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, sjliao2011@163.com";
			try {
				message += "\r\nThe test cost array was: "
						+ SimpleTool
								.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
				currentSystem.refreshTestCost1();
				message += "\r\nNow it is: "
						+ SimpleTool
								.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',')
						+ "\r\nThe end.\r\n";
			} catch (Exception ee) {
				message = ee.toString();
			}
			ProgressDialog.progressDialog.setMessageAndShow(message);
		}// Of actionPerformed
	}// Of RefreshTestCostFNShower

	/**
	 *************************** 
	 * Show the FN time comparison.
	 *************************** 
	 */
	private class FNTimeComparisonShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			FNTimeComparisonDialog.fNTimeComparisonDialog.setVisible(true);
		}// Of actionPerformed
	}// Of FNTimeComparisonShower

	/**
	 *************************** 
	 * Show the TestCostSensitiveDSFNGA dialog.
	 *************************** 
	 */
	private class TestCostDSFNGAShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			TestCostDSFNGADialog.testCostDSFNGADialog.setVisible(true);
		}// Of actionPerformed
	}// Of TestCostDSFNGAShower

	/**
	 *************************** 
	 * Show the test cost distribution dialog for neighborhood systems.
	 *************************** 
	 */
	private class LoadTcDsErShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			LoadTcDsErDialog.loadTcDsErDialog.setVisible(true);
		}// Of actionPerformed
	}// Of LoadTcDsErShower

	/**
	 *************************** 
	 * Show the Tc-Ds-Er all reducts dialog.
	 *************************** 
	 */
	private class TcDsErAllReductsExhaustiveShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			TestCostDecisionSystemErrorRange currentSystem = CoserProject.currentProject.currentTcDsEr;
			String message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com";
			try {
				message += "\r\nThe test cost array is: "
						+ SimpleTool
								.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
			} catch (Exception ee) {
				message = ee.toString();
			}
			message += "\r\nReducing...";
			ProgressDialog.progressDialog.setMessageAndShow(message);
			try {
				currentSystem.computeAllReductsBacktrack();
			} catch (Exception ee) {
				message = ee.toString();
			}

			ProgressDialog.progressDialog.appendMessage("\r\nDone.\r\n");
			String tempFilename = "";
			try {
				tempFilename = currentSystem.getAllReductsFilename();
				ProgressDialog.progressDialog
						.appendMessage("Please refer to file: " + tempFilename
								+ " for all reduct");
			} catch (Exception ee) {
				ProgressDialog.progressDialog.appendMessage(ee.toString());
			}// Of try
		}// Of actionPerformed
	}// Of TcDsErAllReductsExhaustiveShower

	/**
	 *************************** 
	 * Compute the optimal reduct by backtracking directly.
	 *************************** 
	 */
	private class TcDsErOptimalReductBacktrackShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			TestCostDecisionSystemErrorRange currentSystem = CoserProject.currentProject.currentTcDsEr;
			String message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com";
			try {
				message += "\r\nThe error range array is: "
						+ SimpleTool.doubleArrayToString(
								currentSystem.getErrorRangeArray(), ',');
				message += "\r\nThe test cost array is: "
						+ SimpleTool
								.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
			} catch (Exception ee) {
				message = ee.toString();
			}
			message += "\r\nReducing...";
			ProgressDialog.progressDialog.setMessageAndShow(message);
			int optimalTestCost = 0;
			boolean[] optimalReduct = null;
			try {
				optimalTestCost = currentSystem
						.computeOptimalReductTestCostBacktrack();
				optimalReduct = currentSystem.getOptimalReduct();
			} catch (Exception ee) {
				message = ee.toString();
			}

			ProgressDialog.progressDialog.appendMessage("\r\n"
					+ SimpleTool
							.booleanArrayToAttributeSetString(optimalReduct)
					+ " is the optimal reduct with cost " + optimalTestCost
					+ ".\r\nDone.\r\n");
		}// Of actionPerformed
	}// Of TcDsErOptimalReductBacktrackShower

	/**
	 *************************** 
	 * Show the error range lambda reduction dialog.
	 *************************** 
	 */
	private class ErrorRangeLambdaReductionShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			ErrorRangeLambdaReductionDialog.errorRangeLambdaReductionDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of ErrorRangeLambdaReductionShower

	/**
	 *************************** 
	 * Show the error range time comparison.
	 *************************** 
	 */
	private class ErrorRangeTimeComparisonShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			ErrorRangeTimeComparisonDialog.errorRangeTimeComparisonDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of ErrorRangeTimeComparisonShower

	/**
	 *************************** 
	 * Show the test cost distribution dialog for neighborhood systems.
	 *************************** 
	 */
	private class LoadTcDsNeShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			LoadTcDsNeDialog.loadTcDsNeDialog.setVisible(true);
		}// Of actionPerformed
	}// Of LoadTcDsErShower

	/**
	 *************************** 
	 * Show the TC-DS-NE all reducts dialog.
	 *************************** 
	 */
	private class TcDsNeAllReductsExhaustiveShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			TestCostDecisionSystemNormalError currentSystem = CoserProject.currentProject.currentTcDsNe;
			String message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com";
			try {
				message += "\r\nThe test cost array is: "
						+ SimpleTool
								.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
			} catch (Exception ee) {
				message = ee.toString();
			}
			message += "\r\nReducing...";
			ProgressDialog.progressDialog.setMessageAndShow(message);
			try {
				currentSystem.computeAllReductsBacktrack();
			} catch (Exception ee) {
				message = ee.toString();
			}

			ProgressDialog.progressDialog.appendMessage("\r\nDone.\r\n");
			String tempFilename = "";
			try {
				tempFilename = currentSystem.getAllReductsFilename();
				ProgressDialog.progressDialog
						.appendMessage("Please refer to file: " + tempFilename
								+ " for all reduct");
			} catch (Exception ee) {
				ProgressDialog.progressDialog.appendMessage(ee.toString());
			}// Of try
		}// Of actionPerformed
	}// Of TcDsNeAllReductsExhaustiveShower

	/**
	 *************************** 
	 * Compute the optimal reduct by backtracking directly.
	 *************************** 
	 */
	private class TcDsNeOptimalReductBacktrackShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			TestCostDecisionSystemNormalError currentSystem = CoserProject.currentProject.currentTcDsNe;
			String message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com";
			try {
				message += "\r\nThe error range array is: "
						+ SimpleTool.doubleArrayToString(
								currentSystem.getNormalErrorSizeArray(), ',');
				message += "\r\nThe test cost array is: "
						+ SimpleTool
								.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
			} catch (Exception ee) {
				message = ee.toString();
			}
			message += "\r\nReducing...";
			ProgressDialog.progressDialog.setMessageAndShow(message);
			int optimalTestCost = 0;
			boolean[] optimalReduct = null;
			try {
				optimalTestCost = currentSystem
						.computeOptimalReductTestCostBacktrack();
				optimalReduct = currentSystem.getOptimalReduct();
			} catch (Exception ee) {
				message = ee.toString();
			}

			ProgressDialog.progressDialog.appendMessage("\r\n"
					+ SimpleTool
							.booleanArrayToAttributeSetString(optimalReduct)
					+ " is the optimal reduct with cost " + optimalTestCost
					+ ".\r\nDone.\r\n");
		}// Of actionPerformed
	}// Of TcDsNeOptimalReductBacktrackShower

	/**
	 *************************** 
	 * Show the normal distribution measurement error delta reduction dialog.
	 *************************** 
	 */
	private class NormalErrorDeltaReductionShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			NormalErrorDeltaReductionDialog.normalErrorDeltaReductionDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of NormalErrorDeltaReductionShower

	/**
	 *************************** 
	 * Show the normal distribution measurement error lambda reduction dialog.
	 *************************** 
	 */
	private class NormalErrorLambdaReductionShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			NormalErrorLambdaReductionDialog.normalErrorLambdaReductionDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of NormalErrorLambdaReductionShower

	/**
	 *************************** 
	 * Show the normal error time comparison.
	 *************************** 
	 */
	private class NormalErrorTimeComparisonShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			NormalErrorTimeComparisonDialog.normalErrorTimeComparisonDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of NormalErrorTimeComparisonShower

	/**
	 *************************** 
	 * Show the load both cost decision system dialog.
	 *************************** 
	 */
	private class LoadBcNdsShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			LoadBcNdsDialog.loadBcNdsDialog.setVisible(true);
		}// Of actionPerformed
	}// Of LoadBcNdsShower

	/**
	 *************************** 
	 * Compute the optimal reduct by backtracking directly.
	 *************************** 
	 */
	private class BcNdsOptimalReductBacktrackShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			BothCostsNominalDecisionSystem currentSystem = CoserProject.currentProject.currentBcNds;
			String message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com";
			message += "\r\nReducing...";
			ProgressDialog.progressDialog.setMessageAndShow(message);
			double optimalCost = 0;
			boolean[] optimalReduct = null;
			int testCost = 0;
			try {
				optimalCost = currentSystem.optimalCostReductBacktrack(true);
				optimalReduct = currentSystem.getOptimalReduct();
				testCost = currentSystem.computeTestCost(optimalReduct);

				ProgressDialog.progressDialog
						.appendMessage("\r\nTest set "
								+ SimpleTool
										.booleanArrayToAttributeSetString(optimalReduct)
								// + " (" +
								// SimpleTool.booleanArrayToLong(optimalReduct)
								// + ")"
								+ " is the optimal reduct with average cost "
								+ optimalCost + ",\r\n"
								+ "where the test cost is " + testCost
								+ ".\r\n" + "Run step summary: A total of "
								+ currentSystem.getBacktrackSteps()
								+ " steps.\r\nDone.\r\n");
			} catch (Exception ee) {
				message = ee.toString();
				ErrorDialog.errorDialog.setMessageAndShow(message);
			}
		}// Of actionPerformed
	}// Of BcNdsOptimalReductBacktrackShower

	/**
	 *************************** 
	 * Compute the optimal reduct by backtracking directly.
	 *************************** 
	 */
	private class BcNdsOptimalReductsBacktrackShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			MinimalCostReductionDialog.minimalCostReductionDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of BcNdsOptimalReductsBacktrackShower

	/**
	 *************************** 
	 * Show the CS-ID3 with lambda comparison dialog.
	 *************************** 
	 */
	private class CostSensitiveDecisionTreeShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			CostSensitiveDecisionTreeDialog.costSensitiveDecisionTreeDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of CostSensitiveDecisionTreeShower

	/********************* BC-DS-NE *********************/
	/**
	 *************************** 
	 * Show the load both cost decision system Normal Error dialog.
	 *************************** 
	 */
	private class LoadBcDsNeShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			LoadBcDsNeDialog.loadBcDsNeDialog.setVisible(true);
		}// Of actionPerformed
	}// Of LoadBcDsNeShower

	/**
	 *************************** 
	 * Compute the optimal reduct by backtracking directly.
	 *************************** 
	 */
	private class BcDsNeOptimalReductBacktrackShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			BothCostsDecisionSystemNormalError currentSystem = CoserProject.currentProject.currentBcDsNe;
			String message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com";
			message += "\r\nReducing...";
			ProgressDialog.progressDialog.setMessageAndShow(message);
			double optimalCost = 0;
			boolean[] optimalReduct = null;
			int testCost = 0;
			try {
				optimalCost = currentSystem.optimalCostReductBacktrack(true);
				optimalReduct = currentSystem.getOptimalReduct();
				if (optimalReduct == null) {
					ProgressDialog.progressDialog
							.appendMessage("\r\nHave no optimal Reduct!\r\nPlease set larger misclassification cost!");
				} else {
					testCost = currentSystem.computeTestCost(optimalReduct);

					ProgressDialog.progressDialog
							.appendMessage("\r\nTest set "
									+ SimpleTool
											.booleanArrayToAttributeSetString(optimalReduct)
									// + " (" +
									// SimpleTool.booleanArrayToLong(optimalReduct)
									// + ")"
									+ " is the optimal reduct with average cost "
									+ optimalCost + ",\r\n"
									+ "where the test cost is " + testCost
									+ ".\r\n" + "Run step summary: A total of "
									+ currentSystem.getBacktrackSteps()
									+ " steps.\r\nDone.\r\n");
				}
			} catch (Exception ee) {
				message = ee.toString();
				ErrorDialog.errorDialog.setMessageAndShow(message);
			}
		}// Of actionPerformed
	}// Of BcDsNeOptimalReductBacktrackShower

	/**
	 *************************** 
	 * Compute the optimal feature subset by backtracking directly.
	 *************************** 
	 */
	private class BcDsNeOptimalReductsBacktrackShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			MinimalCostNormalErrorFeatureSelectionDialog.minimalCostNormalDistributionErrorFeatureSelectionDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of BcDsNeOptimalReductsBacktrackShower

	/**
	 *************************** 
	 * Show the normal error lambda reduction dialog.
	 *************************** 
	 */
	private class NormalErrorLambdaFeatureSelectionShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			NormalErrorLambdaFeatureSelectionDialog.normalErrorLambdaFeatureSelectionDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of NormalErrorLambdaFeatureSelectionShower

	/**
	 *************************** 
	 * Show the normal error time comparison.
	 *************************** 
	 */
	private class NormalErrorFeatureSelectionTimeComparisonShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			NormalErrorFeatureSelectionTimeComparisonDialog.normalErrorFeatureSelectionTimeComparisonDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of NormalErrorFeatureSelectionTimeComparisonShower

	/********************* BC-DS-GC *********************/
	/**
	 *************************** 
	 * Show the load both cost decision system Normal Error dialog.
	 *************************** 
	 */
	private class LoadBcDsGcShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			LoadBcDsGcDialog.loadBcDsGcDialog.setVisible(true);
		}// Of actionPerformed
	}// Of LoadBcDsNeShower

	/**
	 *************************** 
	 * Compute the optimal feature subset by backtracking directly.
	 *************************** 
	 */
	private class BcDsGcOptimalReductsBacktrackShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			FeatureSelectionBasedonGranularComputingDialog.featureSelectionBasedonGranularComputingDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of BcDsGcOptimalReductsBacktrackShower

	/**
	 *************************** 
	 * Compute the optimal feature subset with adaptive BC by backtracking
	 * directly.
	 *************************** 
	 */
	private class AdaptiveBcDsGcOptimalReductsBacktrackShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			FeatureSelectionBasedonAdaptiveBCDialog.featureSelectionBasedonAdaptiveBCDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of AdaptiveBcDsGcOptimalReductsBacktrackShower

	/**
	 *************************** 
	 * Show the normal error lambda reduction dialog.
	 *************************** 
	 */
	private class GranularComputingFeatureSelectionShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			GranularComputingFeatureSelectionDialog.granularComputingFeatureSelectionDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of NormalErrorLambdaFeatureSelectionShower

	/**
	 *************************** 
	 * Show the CS-ID3 with class balance dialog.
	 *************************** 
	 * private class CostSensitiveDecisionTreeBalanceShower implements
	 * ActionListener{ public void actionPerformed(ActionEvent ae){
	 * CostSensitiveDecisionTreeBalanceDialog
	 * .costSensitiveDecisionTreeBalanceDialog.setVisible(true); }//Of
	 * actionPerformed }//Of CostSensitiveDecisionTreeBalanceShower
	 */

	/**
	 *************************** 
	 * Show the CS-ID3 prune dialog.
	 *************************** 
	 */
	private class CostSensitiveDecisionTreePruneShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			CostSensitiveDecisionTreePruneDialog.costSensitiveDecisionTreePruneDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of CostSensitiveDecisionTreePruneShower

	/**
	 *************************** 
	 * Show the cost sensitive C4.5 decision tree dialog.
	 *************************** 
	 */
	private class CostSensitiveDecisionTreeC45OnceShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			CostSensitiveNominalDecisionTreeC45OnceDialog.costSensitiveNominalDecisionTreeC45Dialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of CostSensitiveDecisionTreeC45OnceShower

	/**
	 *************************** 
	 * Show the cost sensitive C4.5 decision tree dialog.
	 *************************** 
	 */
	private class CostSensitiveDecisionTreeC45BatchShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			CostSensitiveNominalDecisionTreeC45BatchDialog.costSensitiveNominalDecisionTreeC45BatchDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of CostSensitiveDecisionTreeC45BatchShower

	/**
	 *************************** 
	 * Show the cost sensitive numeric C4.5 decision tree once dialog.
	 *************************** 
	 */
	private class CostSensitiveNumericDecisionTreeC45OnceShower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			CostSensitiveNumericDecisionTreeC45OnceDialog.costSensitiveNumericDecisionTreeC45OnceDialog
					.setVisible(true);
		}// Of actionPerformed
	}// Of CostSensitiveNumericDecisionTreeC45OnceShower

	/**
	 *************************** 
	 * Show the cost sensitive numeric C4.5 decision tree dialog.
	 *************************** 
	 */
	private class CostSensitiveNumericDecisionTreeC45Shower implements
			ActionListener {
		public void actionPerformed(ActionEvent ae) {
			CostSensitiveJ48Dialog.costSensitiveJ48Dialog.setVisible(true);
		}// Of actionPerformed
	}// Of CostSensitiveNumericDecisionTreeC45Shower

	/**
	 *************************** 
	 * Show the load both cost numeric decision system dialog.
	 *************************** 
	 */
	private class LoadBcUdsShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			LoadBcUdsDialog.loadBcUdsDialog.setVisible(true);
		}// Of actionPerformed
	}// Of LoadBcUdsShower

	/**
	 *************************** 
	 * Refresh test cost.
	 *************************** 
	 */
	private class RefreshTestCostShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			CostSensitiveDecisionSystem currentSystem = CoserProject.currentProject.currentCsds;
			String message = "Generated by Coser http://grc.fjzs.edu.cn/~fmin, minfanphd@163.com";
			try {
				message += "\r\nThe test cost array was: "
						+ SimpleTool
								.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',');
				currentSystem.refreshTestCost();
				message += "\r\nNow it is: "
						+ SimpleTool
								.intArrayToString(currentSystem
										.getIndividualTestCostArray(), ',')
						+ "\r\nThe end.\r\n";
			} catch (Exception ee) {
				message = ee.toString();
			}
			ProgressDialog.progressDialog.setMessageAndShow(message);
		}// Of actionPerformed
	}// Of RefreshTestCostShower

	/**
	 *************************** 
	 * Show the dialog.
	 *************************** 
	 */
	private class SearchShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			SearchDialog.searchDialog.setVisible(true);
		}// Of actionPerformed
	}// Of SearchShower

	/**
	 *************************** 
	 * Show the dialog.
	 *************************** 
	 */
	private class ReplaceShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			ReplaceDialog.replaceDialog.setVisible(true);
		}// Of actionPerformed
	}// Of ReplaceShower

	/**
	 *************************** 
	 * Show the dialog.
	 *************************** 
	 */
	private class EntropyShower implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			EntropyDialog.entropyDialog.setVisible(true);
		}// Of actionPerformed
	}// Of ReplaceShower

}// Of class CoserMenuBar