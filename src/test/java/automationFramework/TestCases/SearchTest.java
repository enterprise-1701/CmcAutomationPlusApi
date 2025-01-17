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
//Quality Center Test IDs: 71939, 72031, 71944, 72030
//#################################################################################

public class SearchTest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static final String NORECORD = "No records found.";
	private static final String CRTYPE = "Individual";
	private static final String CTYPE = "Primary";
	private static final String DUPLICATE_FNAME = "robert";
	private static final String DUPLICATE_LNAME = "downton";
	private static String phoneNumber;
	private static String email;
	private static CustomerData cData;

	static WebDriver driver;
	static String browser;
	CoreTest coreTest = new CoreTest();
	UserData userData = new UserData();

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

	// Dynamic Search
	@Test(priority = 1, enabled = true)
	public void searchCustomerVerifiedTest() throws Exception {

		// Create customer test data via api rest call
		cData = ApiCustomerPost.apiPostSuccess();
		email = cData.getEmail();
		phoneNumber = cData.getPhone();
		Log.info("Email and phone number from API:  " + email + " " + phoneNumber);

		// returning to selenium testing
		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterEmail(driver, email);
		sPage.clickSearch(driver);

		Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME);
		Log.info("Actual results " + sPage.getFirstName(driver) + " matches " + Global.FNAME);
		Assert.assertEquals(sPage.getLastName(driver), Global.LNAME);
		Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME);
		Assert.assertEquals(sPage.getEmail(driver), email);
		Log.info("Actual results " + sPage.getEmail(driver) + " matches " + email);
		Assert.assertEquals(sPage.getPhone(driver), phoneNumber);
		Log.info("Actual results " + sPage.getPhone(driver) + " matches " + phoneNumber);
		Assert.assertEquals(sPage.getCustomerType(driver), CRTYPE);
		Log.info("Actual results " + sPage.getCustomerType(driver) + " matches " + CRTYPE);
		Assert.assertEquals(sPage.getContactType(driver), CTYPE);
		Log.info("Actual results " + sPage.getContactType(driver) + " matches " + CTYPE);
		Assert.assertEquals(sPage.getAddress(driver).substring(0, 12), Global.ADDRESS);
		Log.info("Actual results " + sPage.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);

		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
		sPage.clickRecord(driver);
		sPage.clickSecurityBox(driver);
		sPage.clickContiune(driver);

		Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME);
		Log.info("Actual results " + sPage.getFirstName(driver) + " matches " + Global.FNAME);
		Assert.assertEquals(sPage.getLastName(driver), Global.LNAME);
		Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME);
		Assert.assertEquals(sPage.getEmail(driver), email);
		Log.info("Actual results " + sPage.getEmail(driver) + " matches " + email);
		Assert.assertEquals(sPage.getPhone(driver), phoneNumber);
		Log.info("Actual results " + sPage.getPhone(driver) + " matches " + phoneNumber);
		Assert.assertEquals(sPage.getContactTypeTableTwo(driver), CTYPE);
		Log.info("Actual results " + sPage.getContactTypeTableTwo(driver) + " matches " + CTYPE);
		Assert.assertEquals(sPage.getAddress(driver).substring(0, 12), Global.ADDRESS);
		Log.info("Actual results " + sPage.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);
		driver.close();

	}

	@Test(priority = 2, enabled = true)
	public void searchCustomerNotVerifiedTest() throws Exception {

		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterFirstname(driver, Global.FNAME);
		sPage.enterLastname(driver, Global.LNAME);
		sPage.clickSearch(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
		sPage.clickRecord(driver);
		sPage.clickNotVerified(driver);
		Assert.assertTrue(sPage.isRecordPresent(driver), "customer records are not being displayed!");
		driver.close();

	}

	@Test(priority = 3, enabled = true)
	public void searchCustomerNoResultsTest() throws Exception {

		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterFirstname(driver, Global.FNAME2);
		sPage.enterLastname(driver, Global.LNAME2);
		sPage.enterEmail(driver, Global.EMAIL);
		sPage.enterPhone(driver, Global.PHONE2);
		sPage.clickSearch(driver);
		Assert.assertEquals(sPage.getNoRecordFound(driver), NORECORD);
		Log.info("Actual results " + sPage.getNoRecordFound(driver) + " matches expected results " + NORECORD);
		driver.close();

	}

	@Test(priority = 4, enabled = true)
	public void searchCustomerTypeNotSelected() throws Exception {

		coreTest.signIn(driver);
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		SearchPage sPage = new SearchPage(driver);
		Assert.assertFalse(sPage.isSearchEnabled(driver), "Search button should not be enabled!");
		driver.close();

	}

	@Test(priority = 5, enabled = true)
	public void searchCustomerInvalidEmail() throws Exception {

		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterFirstname(driver, Global.FNAME);
		sPage.enterLastname(driver, Global.LNAME);
		sPage.enterEmail(driver, Global.FNAME);
		Assert.assertEquals(sPage.getEmailError(driver), Global.INVALID_EMAIL);
		Assert.assertFalse(sPage.isSearchEnabled(driver), "Search button should not be enabled!");
		driver.close();

	}

	@Test(priority = 6, enabled = true)
	public void searchCustomerCheckDuplicateTestFname() throws Exception {

		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterFirstname(driver, DUPLICATE_FNAME);
		sPage.clickSearch(driver);
		Assert.assertEquals(sPage.getFirstName(driver), DUPLICATE_FNAME);
		driver.close();

	}

	@Test(priority = 7, enabled = true)
	public void searchCustomerCheckDuplicateTestLname() throws Exception {

		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterLastname(driver, DUPLICATE_LNAME);
		sPage.clickSearch(driver);
		Assert.assertEquals(sPage.getLastName(driver), DUPLICATE_LNAME);
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