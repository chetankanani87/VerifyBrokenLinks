package verifyBrokenLinks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import lib.BrowserDriverUtility;
import lib.ExtentReportUtility;

public class VerifyBrokenLinks {

	static WebDriver dr = BrowserDriverUtility.InvokeBrowser("webdriver.chrome.driver",
			"C:\\Chetan\\Softs\\SeleniumSuite\\WebDrivers\\chromedriver.exe", "https://www.google.ca");
	static ExtentReports report = ExtentReportUtility.InvokeExtentReport();;
	static ExtentTest logger = report.createTest("Verification of Broken Links");

	public static void main(String args[]) throws IOException {
		//System.setProperty("http.proxyHost", "192.168.0.1");
		//System.setProperty("http.proxyPort", "8080");

		List<WebElement> links = dr.findElements(By.tagName("a"));
		logger.info("Total Links are: " + links.size());
		
		System.out.println("Total Links are: " + links.size());

		for (int i = 0; i < links.size(); i++) {
			logger.info("Link: " +links.get(i).getAttribute("href"));
			WebElement ele = links.get(i);
			String url = ele.getAttribute("href");
			if ((url != null) && !url.startsWith("javascript")) {
				verifyLinkActive(url);
			}else {
				logger.pass("This anchor contains javascript href.");
				System.out.println("This anchor contains javascript href.");
			}
			report.flush();
		}
		dr.close();
	}

	public static void verifyLinkActive(String linkUrl) {

		try {
			URL url = new URL(linkUrl);
			HttpURLConnection httpURLConnect = (HttpURLConnection) url.openConnection();

			httpURLConnect.setConnectTimeout(3000);
			httpURLConnect.connect();

			if (httpURLConnect.getResponseCode() == 200) {
				System.out.println(linkUrl + " - " + httpURLConnect.getResponseMessage());
				logger.pass(linkUrl + " - " + httpURLConnect.getResponseMessage());
			} else if (httpURLConnect.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
				System.out.println(
						linkUrl + " - " + httpURLConnect.getResponseMessage() + " - " + HttpURLConnection.HTTP_NOT_FOUND);
				logger.fail(
						linkUrl + " - " + httpURLConnect.getResponseMessage() + " - " + HttpURLConnection.HTTP_NOT_FOUND);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
