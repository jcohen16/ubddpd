package com.ubddpd.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.net.MalformedURLException;
import java.net.URL;

public class AutoRefresher {

	private static Logger log = LogManager.getLogger("systemLogger");

	public static void main(String[] args) throws MalformedURLException, InterruptedException {

		WebDriver driver = null;

		try {
			driver = new RemoteWebDriver(new URL("http://localhost:7055/hub"), DesiredCapabilities.firefox());
		} catch (UnreachableBrowserException e) {
			log.fatal(
					"Driver failed!  Is (a) runSeleniumServer.bat (./res/bat/) running and (b) there's an existing Firefox with a Selenium session? ");
		}

		for (;;) {
			driver.navigate().refresh();
			log.info("FF session page refreshed");
			try {
				driver.switchTo().alert().accept();
			} catch (UnhandledAlertException uae) {
				log.info("No alert dialog box to dismiss.  Continuing.");
			} catch (NoAlertPresentException uae) {
				log.info("No alert dialog box to dismiss.  Continuing.");
			}
			Thread.sleep(300000);
		}

	}

}
