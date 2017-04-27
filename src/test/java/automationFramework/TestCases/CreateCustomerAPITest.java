package automationFramework.TestCases;

import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import automationFramework.PageObjects.*;
import automationFramework.RestApi.ApiCustomerPost;
import automationFramework.Utilities.*;

//#################################################################################
// Quality Center Test IDs: 71940
//#################################################################################

public class CreateCustomerAPITest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static String phoneNumber;
	private static String email;
	private static CustomerData cData;

	static WebDriver driver;
	static String browser;
	CoreTest coreTest = new CoreTest();
	boolean saveEmail = true;

	@Parameters("browser")
	@BeforeMethod
	public void setUp(String browser) throws InterruptedException {

		Logging.setLogConsole();
		Logging.setLogFile();
		Log.info("Setup Started");
		Log.info("Current OS: " + WindowsUtils.readStringRegistryValue(Global.OS));
		Log.info("Current Browser: " + browser);
		driver = Utils.openBrowser(browser);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		Log.info("Setup Completed");
	}

	@Test(priority = 1, enabled = true)
	public void createNewCustomerApi() throws Exception {

		// Create customer test data via api rest call
		cData = ApiCustomerPost.apiPostSuccess();
		email = cData.getEmail();
		phoneNumber = cData.getPhone();
		Log.info("Email and phone number from API:  " + email + " " + phoneNumber);

		// return to selenium testing 
		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterEmail(driver, email);
		sPage.clickSearch(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
		sPage.clickRecord(driver);
		sPage.clickSecurityBox(driver);
		sPage.clickContiune(driver);

		// Verify customer data on cmc UI
		NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
		Assert.assertEquals(nPage3.getFname(driver), cData.getFname());
		Assert.assertEquals(nPage3.getLname(driver), cData.getLname());
		Assert.assertEquals(nPage3.getEmail(driver), email);
		Assert.assertEquals(nPage3.getPhone(driver), phoneNumber);
		Assert.assertEquals(nPage3.getAddress1(driver), cData.getAddress());
		Assert.assertEquals(nPage3.getAddress(driver).substring(0, 12), cData.getAddress());
		Assert.assertEquals(nPage3.getContactType(driver), cData.getContactType());
		Assert.assertEquals(nPage3.getCity(driver), cData.getCity());
		Assert.assertEquals(nPage3.getPostalCode(driver), cData.getPostal());
		driver.close();
	}
	
	private SearchPage getSearchPage() throws Exception {
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		SearchPage sPage = new SearchPage(driver);
		return sPage;
	}

	@AfterMethod
	public void tearDown() {
		Log.info("TearDown Complete");
		Reporter.log("TearDown Complete");
		driver.quit();

	}
}