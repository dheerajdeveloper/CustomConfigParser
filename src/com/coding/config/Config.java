package com.coding.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import com.coding.config.constant.Constants;

/**
 * Config class represents the config file loaded while booting.
 *
 */
public class Config {

	Logger LOG = Logger.getLogger(Config.class.getName());

	private static Config config;

	/*
	 * Hashset to store the values of overrides specified during boot time.
	 */
	private static Set<String> overrideProperties;

	/*
	 * hashmap of string, hashmap to store settings on a group level basis. We
	 * are using a linked hashmap to maintain the order of insertion. since the
	 * last inserted setting(in case of overrides) will be given priority.
	 */
	private static HashMap<String, LinkedHashMap<String, Object>> configHashMap;

	/*
	 * handle to store the status of config loading.
	 */
	private static ConfigStatus status;

	/*
	 * keeping the constructor private so that only one instance of config class
	 * can be instantiated.
	 */
	private Config() {

	}

	/**
	 * public method to load the config file while starting the program
	 * 
	 * @param filePath
	 * @param overrides
	 * @throws FileNotFoundException
	 */
	public static void load(String filePath, String[] overrides)
			throws FileNotFoundException {
		config = getConfigInstance();
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException(
					"Config file does not exist at location: " + filePath);
		}
		ConfigBuilder builder = new ConfigBuilder(file, overrides);
		new Thread(builder).start();
	}

	/**
	 * GetConfigInstance method with default level access because ConfigBuilder
	 * class in the same package uses this method to get instance of the Config
	 * class.
	 * 
	 * @return
	 */
	static Config getConfigInstance() {

		if (config == null) {
			config = new Config();
			overrideProperties = new HashSet<String>();
			configHashMap = new HashMap<String, LinkedHashMap<String, Object>>();

			// Initializing the config status as not started while
			// initializing config class.
			status = ConfigStatus.NOT_STARTED;
		}

		return config;

	}

	/**
	 * public method to retrieve a value from the config file. there can be two
	 * possible ways to retrieve a value: 1. on group name In this call all the
	 * properties of this group will be returned. this value will be returned by
	 * converting hashmap into string.
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {

		/*
		 * if key does not contains "." then it means that the request is for
		 * the values of a group. so we will simply query configHashMap for that
		 * key and will return the hashmap in the form of string.
		 */
		if (!key.contains(".")) {

			if (configHashMap.containsKey(key)) {
				LinkedHashMap<String, Object> groupHashMap = new LinkedHashMap<String, Object>();

				for (Entry<String, Object> keyValPair : configHashMap.get(key).entrySet()) {
					String currentKey = keyValPair.getKey();
					if (currentKey.contains(Constants.HYPHEN
							+ Constants.OVERRIDE_ATTRIBUTE)) {
						groupHashMap.put(currentKey.split(Constants.HYPHEN)[0],
								keyValPair.getValue());
					} else if(!groupHashMap.containsKey(currentKey)){
						groupHashMap.put(currentKey, keyValPair.getValue());

					}

				}
				return groupHashMap.toString();
			}

		} else {
			/*
			 * if key contains a "." it means that request is for a speicific
			 * setting of a group. so we will parse the group and the setting
			 * and will then query the hashmap inside the hash map and will
			 * retrieve the requested key(if it exists).
			 */
			String[] tokens = key.split("\\.");
			String groupName = tokens[0];
			String settingName = tokens[1];

			if (configHashMap.containsKey(groupName)) {

				if (configHashMap.get(groupName).containsKey(
						settingName + Constants.HYPHEN
								+ Constants.OVERRIDE_ATTRIBUTE)) {

					return configHashMap
							.get(groupName)
							.get(settingName + Constants.HYPHEN
									+ Constants.OVERRIDE_ATTRIBUTE).toString();
				} else if (configHashMap.get(groupName)
						.containsKey(settingName)) {

					return configHashMap.get(groupName).get(settingName)
							.toString();
				}

			}

		}
		return null;

	}

	void updateConfigStatus(ConfigStatus configStatus) {
		status = configStatus;
	}

	public static ConfigStatus getConfigStatus() {
		return status;
	}

	void addOverrideProperties(List<String> overrideProperties) {
		this.overrideProperties.addAll(overrideProperties);
	}

	Set<String> getOverrideProperties() {
		return overrideProperties;
	}

	HashMap<String, LinkedHashMap<String, Object>> getConfigHashMap() {
		return configHashMap;
	}

	void addKeyValueInConfigHashMap(String hashMapKey,
			LinkedHashMap<String, Object> value) {
		configHashMap.put(hashMapKey, value);

	}

}
