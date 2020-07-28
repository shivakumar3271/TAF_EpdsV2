package com.scripted.configurations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;

import com.scripted.dataload.PropertyDriver;
import com.scripted.generic.FileUtils;
import com.scripted.reporting.AllureReport;

public class SkriptmateConfigurations {

	static String log4j_Path = "src\\main\\resources\\logConf\\log4j.properties";
	static String extentProFile_Path = "src\\main\\resources\\extent.properties";
	static String skrimateConfig_Path = "SkriptmateConfigurations\\SkriptmateConfig.Properties";
	static String logPath = "SkriptmateLogs\\Skriptmate.log";
	private static String cdir = System.getProperty("user.dir");

	public static void beforeSuite() {
		try {
			setlog4jConfig(log4j_Path);
			Map<String, String> configMap = readConf();
			configMap.forEach((k, v) -> {
				if (k.equalsIgnoreCase("Skriptmate.allure.clear") && v.equalsIgnoreCase("true")) {
					File dirPath = new File(FileUtils.getCurrentDir() + "\\" + "allure-results");
					FileUtils.deleteDirectory(dirPath);
					File ocrDirPath = new File(cdir + "\\OCROutput" + "\\temp");
					FileUtils.deleteDirectory(ocrDirPath);
				}
				if (k.equalsIgnoreCase("Skriptmate.extent") && v.equalsIgnoreCase("true")) {
					try {
						// ExtentUtils.setExtentPropValue(extentProFile_Path);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void afterSuite() {
		Map<String, String> configMap = readConf();
		try {
			configMap.forEach((k, v) -> {
				if (k.equalsIgnoreCase("Skriptmate.allure") && v.equalsIgnoreCase("true"))
					try {
						AllureReport.customizeReport();
					} catch (Exception e) {
						e.printStackTrace();
					}

			});
			// Will kill all the open driver after the suite execution if any
			killDrivers();
		} catch (Exception e) {

		}
	}

	public static Map<String, String> readConf() {
		PropertyDriver propertyDriver = new PropertyDriver();
		propertyDriver.setPropFilePath(skrimateConfig_Path);
		return propertyDriver.readProp();
	}

	public static void setLog4jPropValue(String log4jPropFilePath) throws Exception {
		PropertiesConfiguration conf = new PropertiesConfiguration(log4jPropFilePath);
		conf.setProperty("log4j.appender.file.File", cdir + "\\" + logPath);
		conf.save();
	}

	public static void setlog4jConfig(String log4jPropFileLoc) throws Exception {
		String log4jPropFilePath = cdir + "\\" + log4jPropFileLoc;
		setLog4jPropValue(log4jPropFilePath);
		InputStream log4j = new FileInputStream(log4jPropFilePath);
		PropertyConfigurator.configure(log4j);
	}

	public static void killDrivers() {
		Process process;
		try {
			String line;
			String pidInfo = "";
			process = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = input.readLine()) != null) {
				pidInfo += line;
			}
			input.close();
			System.out.println(pidInfo);
			if (pidInfo.contains("ChromeDriver.exe")) {
				Runtime.getRuntime().exec("taskkill /f /im ChromeDriver.exe");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
