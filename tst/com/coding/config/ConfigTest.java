package com.coding.config;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigTest {

	@BeforeClass
	public static void  setUp() throws FileNotFoundException, InterruptedException {
		Config.load("tst-data/config", new String[] { "ubuntu", "production" });
		while (!Config.getConfigStatus().equals(ConfigStatus.COMPLETE)) {
			Thread.currentThread().sleep(1000);
			if (Config.getConfigStatus().equals(ConfigStatus.FAILED)) {
				throw new RuntimeException(
						"Exception while loading the config file.");
			}
		}
	}

	@Test
	public void HappyCase() {
		Assert.assertEquals(Config.get("ftp.name"),
				"“hello there, ftp uploading”");
	}

	@Test
	public void HappyCase1() {
		Assert.assertEquals(Config.get("common.paid_users_size_limit"),
				"2147483648");
	}

	@Test
	public void HappyCase2() {
		Assert.assertEquals(Config.get("http.params"), "[array, of, values]");
	}

	@Test
	public void HappyCase3() {
		Assert.assertNull(Config.get("ftp.lastname"));
	}

	@Test
	public void HappyCase4() {
		Assert.assertEquals(Config.get("ftp.enabled"), "false");
	}

	@Test
	public void HappyCase5() {
		Assert.assertEquals(Config.get("ftp.path"), "/etc/var/uploads");
	}

	@Test
	public void HappyCase6() {
		LinkedHashMap<String, Object> temp = new LinkedHashMap<String, Object>();
		temp.put("name", "“hello there, ftp uploading”");
		temp.put("path", "/etc/var/uploads");
		temp.put("enabled", false);
		Assert.assertEquals(Config.get("ftp"), temp.toString());

	}

}
