import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FireFoxDriver {

	public static String strBrowser = "Chrome"; // "HTMLUnit" or "Mozilla" or
												// "Chrome"
	public static WebDriver driver;
	public static List<WebElement> links, links1;
	public static Properties prop;

	public static void init() {

		log("init function start");

		// init the prop file
		if (prop == null) {
			prop = new Properties();
			try {
				FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + "/Webconfig.properties");
				prop.load(fs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log("init function end");
	}

	public static void waitForElement(int timeOutInSeconds, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	
	/*public static void waitForElement(int timeOutInSeconds, WebElement element) {
		new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS)
		.pollingEvery(10, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
	}*/

	
	public static void main(String[] args) throws InterruptedException {

		log("main function start");

		Hashtable<String, String> Input = new Hashtable<String, String>();
		init();

		Input.put("startdate1", "01/01/2013");
		Input.put("enddate1", "01/30/2017");
		Input.put("startdate2", "01/01/2013");
		Input.put("enddate2", "01/30/2017");
		Input.put("min_contract", "100");
		Input.put("max_contract", "100000000");
		Input.put("naics_code", "541511");

		Input.put("contract_agency", "FEDERAL ACQUISITION SERVICE");
		Input.put("performance_city", "DULUTH");
		Input.put("description", "IT AND TELECOM- FACILITY OPERATION AND MAINTENANCE");
		Input.put("zip_code", "300977155");
		Input.put("service_code", "D301");
		Input.put("state_code", "GA");
		Input.put("type_of_contract", "FIRM FIXED PRICE");

		String strSetAside[] = { "NO SET ASIDE USED." };

		String strContracting[] = { "SMALL BUSINESS" };

		String result = Process(Input, strSetAside, strContracting);
		log("main function end and status = " + result);

	}

	public static void log(String strMsg) {
		System.out.println(strMsg);
	}

	public static String Process(Hashtable<String, String> Input, String strSetAside[], String strContracting[])
			throws InterruptedException {

		log("Process function start");
		String result = "success";

		try {
			// Initializing Chrome Driver
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver");

			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			// chromePrefs.put("directory_upgrade", "True");
			System.out.println(prop.getProperty("download_location"));
			chromePrefs.put("download.default_directory", prop.getProperty("download_location"));

			ChromeOptions options = new ChromeOptions();
			options.addArguments("--kiosk");
			// options.addArguments("--headless");
			options.setExperimentalOption("prefs", chromePrefs);
			options.addArguments("--test-type");
			options.addArguments("--disable-extensions");

			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability(ChromeOptions.CAPABILITY, options);
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

			// setting all preferences and capabilities
			driver = new ChromeDriver(options);
			log("Process function - Headless Chrome driver initialized");

			// Open web page
			// driver.manage().window().setPosition(new Point(-3000, 0));
			driver.get(prop.getProperty("url"));
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			log("Process function - webpage opened");

			// Enter User name
			JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
			myExecutor.executeScript(
					"document.getElementsByName('USERNAME')[0].value='" + prop.getProperty("username") + "'");

			// Enter Password
			myExecutor.executeScript(
					"document.getElementsByName('PASSWORD')[0].value='" + prop.getProperty("password") + "'");

			// Find the Sign up button and click
			waitForElement(1000, driver.findElement(By.name(prop.getProperty("name_login"))));
			WebElement Signup_button = driver.findElement(By.name(prop.getProperty("name_login")));
			Signup_button.click();

			// Find the Adhoc Reports link and click
			waitForElement(1000, driver.findElement(By.cssSelector(prop.getProperty("css_AdhocReport"))));
			WebElement linkforAdHoc = driver.findElement(By.cssSelector(prop.getProperty("css_AdhocReport")));
			log("Process function - Click Adhoc link");
			linkforAdHoc.click();

			// Switch the frame
			WebElement myFrame = driver.findElement(By.tagName("iframe"));
			waitForElement(1000, myFrame);
			driver.switchTo().frame(myFrame.getAttribute("name"));
			log("Process function - IFrame switched");

			// Click the "Personal Folder" button
			waitForElement(1000, driver.findElement(By.cssSelector(prop.getProperty("css_PersonalFolder"))));
			driver.findElement(By.cssSelector(prop.getProperty("css_PersonalFolder"))).click();

			// Click on Contracts
			waitForElement(1000, driver.findElement(By.xpath(prop.getProperty("xpath_Contracts"))));
			Actions action = new Actions(driver);
			WebElement contracts = driver.findElement(By.xpath(prop.getProperty("xpath_Contracts")));
			action.doubleClick(contracts).perform();
			log("Process function - Contracts clicked");

			// Click the Execute button
			waitForElement(1000, driver.findElement(By.id(prop.getProperty("id_ExecuteButton"))));
			driver.findElement(By.id(prop.getProperty("id_ExecuteButton"))).click();
			log("Process function - Execute Clicked");

			// Enter start dates , end dates and NAICS Code
			waitForElement(1000, driver.findElement(By.name(prop.getProperty("name_startdate1"))));
			log("Process function - Enter values");
			String strName = prop.getProperty("name_startdate1");
			String value = Input.get("startdate1");
			myExecutor.executeScript("document.getElementsByName('" + strName + "')[0].value='" + value + "'");

			waitForElement(1000, driver.findElement(By.name(prop.getProperty("name_enddate1"))));
			strName = prop.getProperty("name_enddate1");
			value = Input.get("enddate1");
			myExecutor.executeScript("document.getElementsByName('" + strName + "')[0].value='" + value + "'");

			strName = prop.getProperty("name_startdate2");
			value = Input.get("startdate2");
			myExecutor.executeScript("document.getElementsByName('" + strName + "')[0].value='" + value + "'");
			strName = prop.getProperty("name_enddate2");
			value = Input.get("enddate2");

			myExecutor.executeScript("document.getElementsByName('" + strName + "')[0].value='" + value + "'");
			strName = prop.getProperty("name_code");
			value = Input.get("naics_code");
			myExecutor.executeScript("document.getElementsByName('" + strName + "')[0].value='" + value + "'");

			strName = prop.getProperty("name_contract_agency");
			value = Input.get("contract_agency");
			myExecutor.executeScript("document.getElementsByName('" + strName + "')[0].value='" + value + "'");

			strName = prop.getProperty("name_performance_city");
			value = Input.get("performance_city");
			myExecutor.executeScript("document.getElementsByName('" + strName + "')[0].value='" + value + "'");

			strName = prop.getProperty("name_service_code");
			value = Input.get("service_code");
			myExecutor.executeScript("document.getElementsByName('" + strName + "')[0].value='" + value + "'");

			strName = prop.getProperty("name_description");
			value = Input.get("description");
			myExecutor.executeScript("document.getElementsByName('" + strName + "')[0].value='" + value + "'");

			strName = prop.getProperty("name_zip_code");
			value = Input.get("zip_code");
			myExecutor.executeScript("document.getElementsByName('" + strName + "')[0].value='" + value + "'");

			strName = prop.getProperty("name_state_code");
			value = Input.get("state_code");
			myExecutor.executeScript("document.getElementsByName('" + strName + "')[0].value='" + value + "'");

			strName = prop.getProperty("name_type_of_contract");
			value = Input.get("type_of_contract");
			myExecutor.executeScript("document.getElementsByName('" + strName + "')[0].value='" + value + "'");

			strName = prop.getProperty("name_min");
			value = Input.get("min_contract");
			myExecutor.executeScript("document.getElementsByName('" + strName + "')[0].value='" + value + "'");

			strName = prop.getProperty("name_max");
			value = Input.get("max_contract");
			myExecutor.executeScript("document.getElementsByName('" + strName + "')[0].value='" + value + "'");

			// Click Display Report button
			waitForElement(1000, driver.findElement(By.id(prop.getProperty("id_DisplayButton"))));
			log("Process function - Click Display report button");
			driver.findElement(By.id(prop.getProperty("id_DisplayButton"))).click();

			// Click export
			waitForElement(1000, driver.findElement(By.id(prop.getProperty("id_exporttab"))));
			myExecutor = ((JavascriptExecutor) driver);
			WebElement ExportButton = driver.findElement(By.id(prop.getProperty("id_exporttab")));
			myExecutor.executeScript("arguments[0].click();", ExportButton);

			// Counting maximum number of pages

			String maxPage = " ";
			if (!driver.getPageSource().contains("No report results to be displayed.")) {
				maxPage = driver.findElement(By.xpath(prop.getProperty("xpath_max_page"))).getText();

				log("maxPage = " + maxPage);

				Double total_waiting_time = 0.0;

				if (!maxPage.isEmpty()) {
					total_waiting_time = Double.valueOf(maxPage) * Double.valueOf(prop.getProperty("wait_factor"));
				}

				// Switching to frame1
				waitForElement(1000, driver.findElement(By.name(prop.getProperty("frame1"))));
				driver.switchTo().frame(prop.getProperty("frame1"));

				// Click on csv format
				waitForElement(1000, driver.findElement(By.xpath(prop.getProperty("xpath_csv"))));
				myExecutor = ((JavascriptExecutor) driver);
				WebElement csvRadioButton = driver.findElement(By.xpath(prop.getProperty("xpath_csv")));
				myExecutor.executeScript("arguments[0].click();", csvRadioButton);

				// Click final export
				waitForElement(1000, driver.findElement(By.id(prop.getProperty("id_exportbutton"))));
				ExportButton = driver.findElement(By.id(prop.getProperty("id_exportbutton")));
				myExecutor.executeScript("arguments[0].click();", ExportButton);

				// waiting for file to download
				log("Process function - total_waiting_time " + total_waiting_time + " seconds or "
						+ Math.round(((total_waiting_time % 86400) % 3600)) / 60 + " minutes");
				Thread.sleep(total_waiting_time.intValue() * 1000);
			} else {
				result = "No report results to be displayed.";
			}
			driver.switchTo().defaultContent();
			log("Process function - Report downloaded in CSV format");
			waitForElement(5000, driver.findElement(By.id(prop.getProperty("id_logOff"))));
			driver.findElement(By.id(prop.getProperty("id_logOff"))).click();
			log("Process function - logging off");

			log("Process function end");
			return result;

		} catch (Exception e) {
			result = e.getMessage();
			e.printStackTrace();
			return result;
		} finally {
			driver.quit();
		}
	}

	private static String trim(String maxPage) {
		// TODO Auto-generated method stub
		return null;
	}
}