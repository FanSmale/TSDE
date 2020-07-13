package coser.common;

import java.util.*;

/**
 * Settings, important options.
 * <p>
 * Author: <b>Fan Min</b> minfanphd@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://grc.fjzs.edu.cn/>Lab of Granular Computing</a>,
 * Zhangzhou Normal University, Fujian 363000, China.<br>
 * Project: The cost-sensitive rough sets project
 * <p>
 * Progress: The very beginning.<br>
 * Written time: March 11, 2011. <br>
 * Last modify time: March 11, 2011.
 */
public class Options {
	/**
	 * Use real random.
	 */
	public static boolean realRandom = false;

	/**
	 * Avoid too many static variables/functions. I (Fan) think the system needs
	 * only one Options object. So in most cases we do not need to pass it as a
	 * parameter. I will check that and simplify the program if it is true.
	 */
	public static Options options = new Options();

	/**
	 * The configuration filename.
	 */
	// private String optionsFilename;

	/**
	 ********************************** 
	 * Ensure the whole system has only one Options object and at the same time
	 * not to use too many static variables.
	 ********************************** 
	 */
	private Options() {
	}// Of the empty constructor.

	/**
	 ********************************** 
	 * Load the configuration from the given properties. Set to default values
	 * when the property value is invlid or does not exist.
	 * 
	 * @param paraProperty
	 *            The property contains all settings
	 * @return Which properties are set to default.
	 ********************************** 
	 */
	public String loadFromProperty(Properties paraProperty) {
		String setToDefaultProperties = "";
		try {
			realRandom = paraProperty.getProperty("Real_Random").toUpperCase()
					.equals("TRUE");
		} catch (Exception ee) {
			realRandom = false;
			setToDefaultProperties += "Real_Random\t";
		}
		return setToDefaultProperties;
	}// Of loadFromProperty

	/**
	 ********************************** 
	 * Save to the property object.
	 * 
	 * @param paraProperty
	 *            The property contains all settings
	 * @throws Exception
	 *             May be incurred by writing configuration file.
	 ********************************** 
	 */
	public void saveToProperty(Properties paraProperty) throws Exception {
		String tempString = "" + realRandom;
		paraProperty.setProperty("Real_Random", tempString);
	}// Of saveToProperty

}// Of Options
