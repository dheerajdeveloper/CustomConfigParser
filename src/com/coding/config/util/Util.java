package com.coding.config.util;

import com.coding.config.constant.Constants;

/**
 * Util class to hold utility method used while loading the config file.
 *
 */
public class Util {

	/**
	 * static method to find out if we need to tokenize the string while
	 * loading the config file.
	 * if the file starts and end with double quote then we dont need to
	 * tokenize the string
	 * otherwise if the file contains comma, then we need to tokenize the 
	 * string.
	 * @param conten
	 * @return
	 */
	public static boolean shouldTokenize(String conten) {
		if (conten.startsWith("“") && conten.endsWith("”")) {
			return false;
		} else if (conten.contains(Constants.COMMA)) {
			return true;
		}
		return false;
	}

}
