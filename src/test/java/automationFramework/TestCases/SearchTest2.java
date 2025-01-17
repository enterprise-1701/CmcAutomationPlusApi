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

public class SearchTest2 {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
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

	@Test(priority = 8, enabled = true)
	public void searchCustomerCheckDuplicateTestFnameLname() throws Exception {

		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterFirstname(driver, DUPLICATE_FNAME);
		sPage.enterLastname(driver, DUPLICATE_LNAME);
		sPage.clickSearch(driver);
		Assert.assertEquals(sPage.getFirstName(driver), DUPLICATE_FNAME);
		Assert.assertEquals(sPage.getLastName(driver), DUPLICATE_LNAME);
		driver.close();

	}

	@Test(priority = 9, enabled = true)
	public void searchCustomerCheckDuplicateTestPhone() throws Exception {

		createNewCustomer();
		Log.info("phone being retreived is: " + coreTest.getPhone());
		SearchPage sPage = getSearchTab();
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterPhone(driver, coreTest.getPhone());
		sPage.clickSearch(driver);
		Assert.assertEquals(sPage.getPhone(driver), coreTest.getPhone());
		driver.close();

	}

	@Test(priority = 10, enabled = true)
	public void searchCustomerCheckDuplicateTestEmail() throws Exception {

		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterEmail(driver, coreTest.getEmail());
		sPage.clickSearch(driver);
		Assert.assertEquals(sPage.getEmail(driver), coreTest.getEmail());
		driver.close();

	}

	@Test(priority = 11, enabled = true)
	public void searchCustomerCheckDuplicateTestEmailPhone() throws Exception {

		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterEmail(driver, coreTest.getEmail());
		sPage.enterPhone(driver, coreTest.getPhone());
		sPage.clickSearch(driver);
		Assert.assertEquals(sPage.getPhone(driver), coreTest.getPhone());
		Assert.assertEquals(sPage.getEmail(driver), coreTest.getEmail());
		driver.close();

	}

	@Test(priority = 12, enabled = true)
	public void searchCustomerCheckDuplicateTestAll() throws Exception {

		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.clickCustomerType(driver, "Individual");
		sPage.enterFirstname(driver, DUPLICATE_FNAME);
		sPage.enterLastname(driver, DUPLICATE_LNAME);
		sPage.enterEmail(driver, coreTest.getEmail());
		sPage.enterPhone(driver, coreTest.getPhone());
		sPage.clickSearch(driver);
		Assert.assertEquals(sPage.getFirstName(driver), DUPLICATE_FNAME);
		Assert.assertEquals(sPage.getLastName(driver), DUPLICATE_LNAME);
		Assert.assertEquals(sPage.getPhone(driver), coreTest.getPhone());
		Assert.assertEquals(sPage.getEmail(driver), coreTest.getEmail());
		driver.close();

	}

	// Test new search button on upper right of screen
	@Test(priority = 13, enabled = true)
	public void searchNewSearchButton() throws Exception {

		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.clickCustomerType(driver, "Individual");
		sPage.clickSearch(driver);
		sPage.clickNewCustomer(driver);
		CreateCustomerPage cPage = new CreateCustomerPage(driver);
		Assert.assertEquals(cPage.getCustomerLegend(driver), "Customer: Create Customer");
		sPage.clickHeaderSearch(driver);
		Assert.assertEquals(sPage.getSearchLegend(driver), "Customer: Search");
		sPage.clickCustomerType(driver, "Individual");
		sPage.clickSearch(driver);
		driver.close();

	}

	/*
	 * // This test case has to be modifed to be dynamic // Test cases for
	 * section 2.3.1.1
	 * 
	 * @Test(priority = 14, enabled = true) public void
	 * searchCustomerAndCriteriaLastName() throws Exception {
	 * 
	 * coreTest.signIn(driver); SearchPage sPage = getSearchPage();
	 * sPage.clickCustomerType(driver, "Individual");
	 * sPage.enterLastname(driver, "doe"); sPage.enterFirstname(driver, "jane");
	 * sPage.clickSearch(driver);
	 * 
	 * Assert.assertEquals(sPage.getFirstName(driver), "jane"); Log.info(
	 * "Actual results " + sPage.getFirstName(driver) +
	 * " matches expected results " + "doe");
	 * Assert.assertEquals(sPage.getLastName(driver), "doe"); Log.info(
	 * "Actual results " + sPage.getLastName(driver) +
	 * " matches expected results " + "doe");
	 * 
	 * Assert.assertEquals(sPage.getFirstNameSecondRow(driver), "jane");
	 * Log.info("Actual results " + sPage.getFirstNameSecondRow(driver) +
	 * " matches expected results " + "jane");
	 * Assert.assertEquals(sPage.getLastNameSecondRow(driver), "doe"); Log.info(
	 * "Actual results " + sPage.getLastNameSecondRow(driver) +
	 * " matches expected results " + "doe");
	 * 
	 * Assert.assertFalse(sPage.isFirstNameThirdRowDisplayed(driver),
	 * "third row for this AND search should not be displayed");
	 * Assert.assertFalse(sPage.isLastNameThirdRowDisplayed(driver),
	 * "third row for this AND search should not be displayed");
	 * 
	 * driver.close();
	 * 
	 * }
	 * 
	 * // This test case has to be modifed to be dynamic // Test cases for
	 * section 2.3.1.1
	 * 
	 * @Test(priority = 15, enabled = true) public void
	 * searchCustomerAndCriteriaLastNameEmail() throws Exception {
	 * 
	 * coreTest.signIn(driver); SearchPage sPage = getSearchPage();
	 * sPage.clickCustomerType(driver, "Individual");
	 * sPage.enterLastname(driver, "doe"); sPage.enterFirstname(driver, "jane");
	 * sPage.enterEmail(driver, "jane1234@gmail.com");
	 * sPage.clickSearch(driver);
	 * 
	 * Assert.assertEquals(sPage.getFirstName(driver), "jane"); Log.info(
	 * "Actual results " + sPage.getFirstName(driver) +
	 * " matches expected results " + "doe");
	 * Assert.assertEquals(sPage.getLastName(driver), "doe"); Log.info(
	 * "Actual results " + sPage.getLastName(driver) +
	 * " matches expected results " + "doe");
	 * Assert.assertEquals(sPage.getEmail(driver), "jane1234@gmail.com");
	 * Log.info("Actual results " + sPage.getEmail(driver) +
	 * " matches expected results " + "jane1234@gmail.com");
	 * 
	 * Assert.assertFalse(sPage.isFirstNameSecondRowDisplayed(driver),
	 * "second row for this AND search should not be displayed");
	 * Assert.assertFalse(sPage.isLastNameSecondRowDisplayed(driver),
	 * "second row for this AND search should not be displayed");
	 * Assert.assertFalse(sPage.isFirstNameThirdRowDisplayed(driver),
	 * "third row for this AND search should not be displayed");
	 * Assert.assertFalse(sPage.isLastNameThirdRowDisplayed(driver),
	 * "third row for this AND search should not be displayed");
	 * 
	 * driver.close();
	 * 
	 * }
	 * 
	 */

	// Dynamic Search
	@Test(priority = 16, enabled = true)
	public void searchCustomerVerifiedThreeInfoTest() throws Exception {

		createNewCustomer(driver);
		SearchPage sPage = new SearchPage(driver);

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
		sPage.clickNameBox(driver);
		sPage.clickAddressBox(driver);
		sPage.clickDobBox(driver);
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

	// negative test case for searchCustomerVerifiedThreeInfoTest
	@Test(priority = 17, enabled = true)
	public void searchCustomerNotVerifiedThreeInfoTest() throws Exception {

		createNewCustomer(driver);
		SearchPage sPage = new SearchPage(driver);

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
		sPage.clickNameBox(driver);
		sPage.clickAddressBox(driver);
		Assert.assertFalse(sPage.isContinueEnabled(driver));
		driver.close();

	}

	// clicking not verified button should still take the CSR to the customer
	// information page
	@Test(priority = 18, enabled = true)
	public void searchCustomerClickNotVerified() throws Exception {

		createNewCustomer(driver);
		SearchPage sPage = new SearchPage(driver);

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
		sPage.clickNotVerified(driver);

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

	// private methods
	private void createNewCustomer() throws Exception {
		coreTest.signIn(driver);
		coreTest.createCustomerClickHome(driver);
	}

	private SearchPage getSearchPage() throws Exception {
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		SearchPage sPage = new SearchPage(driver);
		return sPage;
	}

	private SearchPage getSearchTab() throws Exception {
		SearchPage sPage = new SearchPage(driver);
		return sPage;
	}

	private WebDriver createNewCustomer(WebDriver driver) throws Exception {

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
		return driver;
	}

	@AfterMethod
	public void tearDown() {
		Log.info("TearDown Complete");
		Reporter.log("TearDown Complete");
		driver.quit();

	}
}