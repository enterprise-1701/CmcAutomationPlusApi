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
//Quality Center Test IDs: 73569, 73571
//#################################################################################

public class UpdateAddressTest {

	private static String phoneNumber;
	private static String email;
	private static CustomerData cData;
	private static Logger Log = Logger.getLogger(Logger.class.getName());
	static WebDriver driver;
	static String browser;
	CoreTest coreTest = new CoreTest();

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

	// Create new customer, search for that customer and enter new address
	@Test(priority = 1, enabled = true)
	public void addNewAddress() throws Exception {

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
		Assert.assertEquals(sPage.getAddress(driver).substring(0, 12), Global.ADDRESS);
		Log.info("Actual results " + sPage.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);

		sPage.clickNewAddress(driver);
		AddressDetailsPage addPage = new AddressDetailsPage(driver);
		addPage.selectCountry(driver);
		addPage.enterAddress(driver, Global.NEWADDRESS);
		addPage.enterCity(driver, Global.NEWCITY);
		addPage.selectState(driver);
		addPage.enterPostalCode(driver, Global.NEWPOSTAL);
		addPage.clickSubmit(driver);

		Assert.assertEquals(sPage.getNewAddress(driver), Global.NEWADDRESS);
		Assert.assertEquals(sPage.getNewCity(driver), Global.NEWCITY);
		Assert.assertEquals(sPage.getNewPostalCode(driver), Global.NEWPOSTAL);
		driver.close();

	}

	// test after canceling new address is not created
	@Test(priority = 2, enabled = true)
	public void addNewAddressCancel() throws Exception {

		createNewCustomer(driver);
		SearchPage sPage = new SearchPage(driver);

		sPage.clickNewAddress(driver);
		AddressDetailsPage addPage = new AddressDetailsPage(driver);
		addPage.selectCountry(driver);
		addPage.enterAddress(driver, Global.NEWADDRESS);
		addPage.enterCity(driver, Global.NEWCITY);
		addPage.selectState(driver);
		addPage.enterPostalCode(driver, Global.NEWPOSTAL);
		addPage.clickCancel(driver);

		// checking for null here since there should be no new address inserted
		Assert.assertEquals(sPage.getNewAddress(driver), null);
		Assert.assertEquals(sPage.getNewCity(driver), null);
		Assert.assertEquals(sPage.getNewPostalCode(driver), null);
		driver.close();

	}

	// Create new customer, search for that customer and update address
	@Test(priority = 3, enabled = true)
	public void updateAddress() throws Exception {

		createNewCustomer(driver);
		SearchPage sPage = new SearchPage(driver);

		Assert.assertEquals(sPage.getFirstName(driver), Global.FNAME);
		Log.info("Actual results " + sPage.getFirstName(driver) + " matches " + Global.FNAME);
		Assert.assertEquals(sPage.getLastName(driver), Global.LNAME);
		Log.info("Actual results " + sPage.getLastName(driver) + " matches " + Global.LNAME);
		Assert.assertEquals(sPage.getEmail(driver), coreTest.getEmail());
		Log.info("Actual results " + sPage.getEmail(driver) + " matches " + coreTest.getEmail());
		Assert.assertEquals(sPage.getPhone(driver), coreTest.getPhone());
		Log.info("Actual results " + sPage.getPhone(driver) + " matches " + coreTest.getPhone());
		Assert.assertEquals(sPage.getAddress(driver).substring(0, 12), Global.ADDRESS);
		Log.info("Actual results " + sPage.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);

		sPage.clickInsideAddress(driver);
		AddressDetailsPage addPage = new AddressDetailsPage(driver);
		addPage.selectCountry(driver);
		addPage.enterAddress(driver, Global.NEWADDRESS);
		addPage.enterCity(driver, Global.NEWCITY);
		addPage.selectState(driver);
		addPage.enterPostalCode(driver, Global.NEWPOSTAL);
		addPage.clickSubmit(driver);
		Utils.waitTime(5000);
		Assert.assertEquals(sPage.getUpdatedAddress(driver), Global.NEWADDRESS);
		Assert.assertEquals(sPage.getUpdatedCity(driver), Global.NEWCITY);
		Assert.assertEquals(sPage.getUpdatedPostalCode(driver), Global.NEWPOSTAL);
		driver.close();

	}

	// Cancel update address and test that address stays the same
	@Test(priority = 4, enabled = true)
	public void updateAddressCancel() throws Exception {

		createNewCustomer(driver);
		SearchPage sPage = new SearchPage(driver);

		sPage.clickInsideAddress(driver);
		AddressDetailsPage addPage = new AddressDetailsPage(driver);
		addPage.selectCountry(driver);
		addPage.enterAddress(driver, Global.NEWADDRESS);
		addPage.enterCity(driver, Global.NEWCITY);
		addPage.selectState(driver);
		addPage.enterPostalCode(driver, Global.NEWPOSTAL);
		addPage.clickCancel(driver);

		Assert.assertEquals(sPage.getUpdatedAddress(driver), Global.ADDRESS);
		Assert.assertEquals(sPage.getUpdatedCity(driver), Global.CITY);
		Assert.assertEquals(sPage.getUpdatedPostalCode(driver), Global.POSTAL);
		driver.close();

	}

	// Negative test cases
	@Test(priority = 5, enabled = true)
	public void addNewAddressCheckRequiredFields() throws Exception {

		createNewCustomer(driver);
		SearchPage sPage = new SearchPage(driver);
		sPage.clickNewAddress(driver);
		AddressDetailsPage addPage = new AddressDetailsPage(driver);

		// country not selected
		addPage.selectCountry(driver);
		addPage.selectNoCountry(driver);

		Assert.assertEquals(addPage.getRequiredFieldError(driver), Global.REQUIREDFIELD_ERROR);
		addPage.selectCountry(driver);

		// Input a value for address and delete it
		addPage.enterAddress(driver, "x");
		addPage.deleteAddress(driver);
		Assert.assertEquals(addPage.getRequiredFieldError(driver), Global.REQUIREDFIELD_ERROR);
		addPage.enterAddress(driver, Global.NEWADDRESS);
		driver.close();

	}

	@Test(priority = 6, enabled = true)
	public void addNewAddressInvalidData() throws Exception {

		createNewCustomer(driver);
		SearchPage sPage = new SearchPage(driver);

		sPage.clickNewAddress(driver);
		AddressDetailsPage addPage = new AddressDetailsPage(driver);
		addPage.selectCountry(driver);
		addPage.enterAddress(driver, Global.NEWADDRESS);
		addPage.enterCity(driver, Global.NEWCITY);
		addPage.selectState(driver);
		addPage.enterPostalCode(driver, "1");
		addPage.clickSubmit(driver);
		Assert.assertEquals(addPage.getPostalError(driver), "Zip Code is invalid.");
		driver.close();

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