package com.scripted.web;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.scripted.dataload.PropertyDriver;

public class BrowserDriver {
	public static String BrowserDriver = null;
	public static WebDriver driver = null;
	public static ThreadLocal<RemoteWebDriver> thDriver = new ThreadLocal<RemoteWebDriver>();

	public static String strBrowserName = null;
	public static String strBrowserVersion = null;
	public static boolean strenableVideo;
	public static boolean strenableVNC;
	public static String strhostURL = null;
	public static String strBrowserNameAndVersion = null;
	// private static final Logger log = Logger.getLogger(BrowserDriver.class);
	public static Logger LOGGER = Logger.getLogger(BrowserDriver.class);

	public static WebDriver funcGetWebdriver() {
		try {
			PropertyDriver p = new PropertyDriver();
			if (driver == null) {
				p.setPropFilePath("src/main/resources/properties/browserconfig.properties");
				strBrowserName = p.readProp("browserName");
			}
			if (strBrowserName == null || strBrowserName.equals(" ")) {
				LOGGER.info("Browser name is null, please check the value of browserName in config.properties");
				System.exit(0);
			} else {
				LOGGER.info("Browser : " + strBrowserName);
				strBrowserName = strBrowserName.toLowerCase();

				switch (strBrowserName) {

				case "chrome":
					ChromeSettings chromeSettings = new ChromeSettings();
					driver = new ChromeDriver(chromeSettings.setBychromeOptions(p.getFilePath()));
					break;

				case "ie":
					IExplorerSettings iExplorerSettings = new IExplorerSettings();
					driver = new InternetExplorerDriver(iExplorerSettings.setByIExplorerOptions(p.getFilePath()));
					break;

				case "firefox":
					FireFoxSettings fireFoxSettings = new FireFoxSettings();
					driver = new FirefoxDriver(fireFoxSettings.setByFirefoxOptions(p.getFilePath()));
					break;

				case "phantom":

					PhatomJSSettings phatomJSSettings = new PhatomJSSettings();
					driver = new PhantomJSDriver(phatomJSSettings.setByPhatomJSSettings(p.getFilePath()));
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while configuring webdrivers" + "Exception :" + e);
			Assert.fail("Webdriver initialisation issues" + "Exception :" + e);
		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		return driver;
	}

	public static void launchWebURL(String strURL) {
		try {
			getDriver().get(strURL);
			pageWait();
			LOGGER.info("Application launched successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while launching Web URL" + "Exception :" + e);
			Assert.fail("Error occurred while launching Web URL" + "Exception :" + e);
		}
	}

	public static void closeBrowser() {
		try {
			getDriver().close();
			driver = null;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while closing browser" + "Exception :" + e);
			Assert.fail("Error occurred while closing browser" + "Exception :" + e);
		}
	}

	public static void quitBrowser() {
		try {
			getDriver().quit();
			driver = null;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while quit browser" + "Exception :" + e);
			Assert.fail("Error occurred while quit browser" + "Exception :" + e);
		}
	}

	public static WebDriver getDriver() {
		if (driver == null) {
			WebDriver rmDriver = thDriver.get();
			return rmDriver;
		} else {
			return driver;
		}
	}

	public static void pageWait() {
		getDriver().manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		getDriver().manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	public static WebDriver funcSelenoidWebdriver() {
		try {
			PropertyDriver p = new PropertyDriver();
			if (driver == null) {
				p.setPropFilePath("src/main/resources/properties/selenoidBrowserConfig.properties");
				strBrowserName = p.readProp("browserName");
				strBrowserVersion = p.readProp("browserVersion");
				strenableVideo = Boolean.valueOf(p.readProp("enableVideo"));
				strenableVNC = Boolean.valueOf(p.readProp("enableVNC"));
				strhostURL = p.readProp("hostURL");
			}
			if (strBrowserName == null || strBrowserName.equals(" ")) {
				LOGGER.info("Browser name is null, please check the value of browserName in config.properties");
				System.exit(0);
			} else {
				LOGGER.info("Browser : " + strBrowserName);
				strBrowserName = strBrowserName.toLowerCase();

				DesiredCapabilities dcap = DesiredCapabilities.chrome();
				dcap.setCapability("enableVideo", strenableVideo);
				dcap.setCapability("enableVNC", strenableVNC);
				dcap.setBrowserName(strBrowserName);
				dcap.setVersion(strBrowserVersion);
				URL url = new URL(strhostURL);

				switch (strBrowserName) {

				case "chrome":
					driver = new RemoteWebDriver(url, dcap);
					break;

				case "ie":
					driver = new RemoteWebDriver(url, dcap);
					break;

				case "firefox":
					driver = new RemoteWebDriver(url, dcap);
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while configuring webdrivers" + "Exception :" + e);
			Assert.fail("Webdriver initialisation issues" + "Exception :" + e);
		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		return driver;
	}

	public static WebDriver getSelenoidDriverParallel(String browser, String version) {
		try {
			URL url = null;
			System.out.println(browser);
			System.out.println(version);
			PropertyDriver p = new PropertyDriver();
			p.setPropFilePath("src/main/resources/properties/selenoidBrowserConfig.properties");
			Boolean strenableVideo = Boolean.valueOf(p.readProp("enableVideo"));
			Boolean strenableVNC = Boolean.valueOf(p.readProp("enableVNC"));
			String strhostURL = p.readProp("hostURL");

			switch (browser) {
			case "chrome":
				DesiredCapabilities cCaps = new DesiredCapabilities();
				ChromeOptions cOptions = new ChromeOptions();
				cOptions.setCapability("enableVideo", strenableVideo);
				cOptions.setCapability("enableVNC", strenableVNC);
				cCaps.setBrowserName(browser);
				cCaps.setVersion(version);
				cCaps.setCapability(ChromeOptions.CAPABILITY, cOptions);
				url = new URL(strhostURL);
				driver = new RemoteWebDriver(url, cCaps);
				break;

			case "ie":
				IExplorerSettings iExplorerSettings = new IExplorerSettings();
				driver = new InternetExplorerDriver(iExplorerSettings.setByIExplorerOptions(p.getFilePath()));
				break;

			case "firefox":
				DesiredCapabilities fCaps = new DesiredCapabilities();
				FirefoxOptions fOptions = new FirefoxOptions();
				fOptions.setCapability("enableVideo", strenableVideo);
				fOptions.setCapability("enableVNC", strenableVNC);
				fCaps.setBrowserName(browser);
				fCaps.setVersion(version);
				fCaps.setCapability(FirefoxOptions.FIREFOX_OPTIONS, fOptions);
				url = new URL(strhostURL);
				driver = new RemoteWebDriver(url, fCaps);
				break;

			case "opera":
				DesiredCapabilities oCaps = new DesiredCapabilities();
				// OperaOptions oOptions = new OperaOptions();
				oCaps.setCapability("enableVideo", strenableVideo);
				oCaps.setCapability("enableVNC", strenableVNC);
				oCaps.setBrowserName(browser);
				oCaps.setVersion(version);
				url = new URL(strhostURL);
				driver = new RemoteWebDriver(url, oCaps);
				break;
			}
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}

	public static WebDriver getSelenoidDriverSeq(String browser, String browserVersion) {
		try {
			URL url = null;
			PropertyDriver p = new PropertyDriver();
			if (driver == null) {
				p.setPropFilePath("src/main/resources/properties/selenoidBrowserConfig.properties");

				strBrowserNameAndVersion = p.readProp("browserNameAndVersion");
				strBrowserName = browser;
				strBrowserVersion = browserVersion;
				strenableVideo = Boolean.valueOf(p.readProp("enableVideo"));
				strenableVNC = Boolean.valueOf(p.readProp("enableVNC"));
				strhostURL = p.readProp("hostURL");
			}
			if (strBrowserName == null || strBrowserName.equals(" ")) {
				LOGGER.info("Browser name is null, please check the value of browserName in config.properties");
				System.exit(0);
			} else {
				LOGGER.info("Browser : " + strBrowserName);
				strBrowserName = strBrowserName.toLowerCase();

				switch (browser) {
				case "chrome":
					DesiredCapabilities cCaps = new DesiredCapabilities();
					ChromeOptions cOptions = new ChromeOptions();
					cOptions.setCapability("enableVideo", strenableVideo);
					cOptions.setCapability("enableVNC", strenableVNC);
					cCaps.setBrowserName(strBrowserName);
					cCaps.setVersion(strBrowserVersion);
					cCaps.setCapability(ChromeOptions.CAPABILITY, cOptions);
					url = new URL(strhostURL);
					driver = new RemoteWebDriver(url, cCaps);
					break;

				case "ie":
					IExplorerSettings iExplorerSettings = new IExplorerSettings();
					driver = new InternetExplorerDriver(iExplorerSettings.setByIExplorerOptions(p.getFilePath()));
					break;

				case "firefox":
					DesiredCapabilities fCaps = new DesiredCapabilities();
					FirefoxOptions fOptions = new FirefoxOptions();
					fOptions.setCapability("enableVideo", strenableVideo);
					fOptions.setCapability("enableVNC", strenableVNC);
					fCaps.setBrowserName(strBrowserName);
					fCaps.setVersion(strBrowserVersion);
					fCaps.setCapability(FirefoxOptions.FIREFOX_OPTIONS, fOptions);
					url = new URL(strhostURL);
					driver = new RemoteWebDriver(url, fCaps);
					break;

				case "opera":
					DesiredCapabilities oCaps = new DesiredCapabilities();
					OperaOptions oOptions = new OperaOptions();
					oCaps.setCapability("enableVideo", strenableVideo);
					oCaps.setCapability("enableVNC", strenableVNC);
					oCaps.setBrowserName(strBrowserName);
					oCaps.setVersion(strBrowserVersion);
					oCaps.setCapability(OperaOptions.CAPABILITY, oOptions);
					url = new URL(strhostURL);
					driver = new RemoteWebDriver(url, oCaps);
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while configuring webdrivers" + "Exception :" + e);
			Assert.fail("Webdriver initialisation issues" + "Exception :" + e);
		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		return driver;
	}

	public static void getSeleniunGridDriver(String browser) {
		try {
			URL url = null;
			LOGGER.info("Browser : " + browser);
			// LOGGER.info("Version : " + version);
			PropertyDriver p = new PropertyDriver();
			p.setPropFilePath("src/main/resources/properties/seleniumGridConfig.properties");
			String strhostURL = p.readProp("hostURL");

			switch (browser) {
			case "chrome":
				DesiredCapabilities cCaps = new DesiredCapabilities();
				ChromeOptions cOptions = new ChromeOptions();
				cOptions.addArguments("--start-maximized");
				cOptions.setExperimentalOption("useAutomationExtension", false);
				cCaps.setBrowserName(browser);
				cCaps.setCapability(ChromeOptions.CAPABILITY, cOptions);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, cCaps));
				break;

			case "internet explorer":
				DesiredCapabilities iCaps = new DesiredCapabilities();
				iCaps = DesiredCapabilities.internetExplorer();
				iCaps.setCapability(CapabilityType.BROWSER_NAME, browser);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, iCaps));
				break;

			case "firefox":
				DesiredCapabilities fCaps = new DesiredCapabilities();
				FirefoxOptions fOptions = new FirefoxOptions();
				fCaps.setBrowserName(browser);
				fCaps.setCapability(FirefoxOptions.FIREFOX_OPTIONS, fOptions);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, fCaps));
				break;

			case "opera":
				DesiredCapabilities oCaps = new DesiredCapabilities();
				OperaOptions oOptions = new OperaOptions();
				oCaps.setBrowserName(browser);
				oCaps.setCapability(OperaOptions.CAPABILITY, oOptions);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, oCaps));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
