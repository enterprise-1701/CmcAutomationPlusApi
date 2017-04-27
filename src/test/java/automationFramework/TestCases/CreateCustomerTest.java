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
import automationFramework.Utilities.*;

//#################################################################################
// Quality Center Test IDs: 71940, 72023, 72017, 72027, 72011, 71948, 72009, 71949
//#################################################################################

public class CreateCustomerTest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static final String STATE = "California";
	private static String phoneNumber;
	private static String email;

	static WebDriver driver;
	static String browser;
	CoreTest coreTest = new CoreTest();
	UserData userData = new UserData();
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
	public void createNewCustomer() throws Exception {

		coreTest.signIn(driver);
		coreTest.createCustomer(driver);
		email = coreTest.getEmail();
		phoneNumber = coreTest.getPhone();
		NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
		Assert.assertEquals(nPage3.getFname(driver), Global.FNAME);
		Log.info("Actual results " + nPage3.getFname(driver) + " matches expected results " + Global.FNAME);
		Assert.assertEquals(nPage3.getLname(driver), Global.LNAME);
		Log.info("Actual results " + nPage3.getLname(driver) + " matches expected results " + Global.LNAME);
		Assert.assertEquals(nPage3.getEmail(driver), email);
		Log.info("Actual results " + nPage3.getEmail(driver) + " matches expected results " + email);
		Assert.assertEquals(nPage3.getPhone(driver), phoneNumber);
		Log.info("Actual results " + nPage3.getPhone(driver) + " matches expected results " + phoneNumber);
		Assert.assertEquals(nPage3.getAddress1(driver), Global.ADDRESS);
		Log.info("Actual results " + nPage3.getAddress1(driver) + " matches expected results " + Global.ADDRESS);
		Assert.assertEquals(nPage3.getAddress(driver).substring(0, 12), Global.ADDRESS);
		Log.info("Actual results " + nPage3.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);
		Assert.assertEquals(nPage3.getContactType(driver), Global.CONTACTTYPE);
		Log.info("Actual results " + nPage3.getContactType(driver) + " matches expected results " + Global.CONTACTTYPE);
		Assert.assertEquals(nPage3.getCity(driver), Global.CITY);
		Log.info("Actual results " + nPage3.getCity(driver) + " matches expected results " + Global.CITY);
		Assert.assertEquals(nPage3.getState(driver), STATE);
		Log.info("Actual results " + nPage3.getState(driver) + " matches expected results " + STATE);
		Assert.assertEquals(nPage3.getPostalCode(driver), Global.POSTAL);
		Log.info("Actual results " + nPage3.getPostalCode(driver) + " matches expected results " + Global.POSTAL);
		driver.close();
	}

	@Test(priority = 2, enabled = true)
	public void createCustomerCancel() throws Exception {

		coreTest.signIn(driver);
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.getLandingPage(Global.URL1);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickNewCustomer(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, Global.FNAME);
		nPage.clickCancel(driver);
		nPage.clickNewCustomer(driver);
		Assert.assertEquals(nPage.getFirstname(driver), "");
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, Global.FNAME);
		nPage.enterLastname(driver, Global.LNAME);
		email = Utils.randomEmailString();
		nPage.enterEmail(driver, email);
		phoneNumber = Utils.randomPhoneNumber();
		nPage.enterPhone(driver, phoneNumber);
		nPage.clickContinue(driver);
		NewCustomerPage nPaget = new NewCustomerPage(driver);
		nPaget.clickCancel(driver);
		Assert.assertEquals(nPage.getFirstname(driver), "");
		driver.close();
	}

	@Test(priority = 3, enabled = true)
	public void createCustomerContactTypeNotSelected() throws Exception {

		coreTest.signIn(driver);
		NewCustomerPage nPage = createCustomerFirstPage(true);
		nPage.selectCountry(driver);
		nPage.enterAddress(driver, Global.ADDRESS);
		nPage.enterCity(driver, Global.CITY);
		nPage.selectState(driver);
		nPage.enterPostalCode(driver, Global.POSTAL);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPage.selectPhoneType(driver, Global.PHONETYPE);
		nPage.selectSecurityQ(driver);
		nPage.enterSecuirtyA(driver, Global.SECURITYA);
		nPage.enterUserName(driver, Utils.randomUsernameString());
		nPage.enterPin(driver, Global.PIN);
		nPage.enterDob(driver, Global.DOB);
		Assert.assertFalse(nPage.isSubmitEnabled(driver), "Submit button should not be enabled!");
		driver.close();

	}

	@Test(priority = 4, enabled = true)
	public void createCustomerInvalidEmail() throws Exception {

		coreTest.signIn(driver);
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.getLandingPage(Global.URL1);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickNewCustomer(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, Global.FNAME);
		nPage.enterLastname(driver, Global.LNAME);
		nPage.enterEmail(driver, Utils.randomUsernameString());
		Assert.assertFalse(nPage.isContinueEnabled(driver), "Continue button should not be enabled!");
		Assert.assertEquals(nPage.getEmailError(driver), Global.INVALID_EMAIL);
		nPage.enterEmail(driver, Utils.randomEmailString());
		nPage.enterPhone(driver, Utils.randomPhoneNumber());
		nPage.clickContinue(driver);
		NewCustomerPage nPaget = new NewCustomerPage(driver);
		nPaget.selectContactType(driver, Global.CONTACTTYPE);
		nPaget.selectCountry(driver);
		nPaget.enterAddress(driver, Global.ADDRESS);
		nPaget.enterCity(driver, Global.CITY);
		nPaget.enterPostalCode(driver, Global.POSTAL);
		nPaget.enterEmail(driver, Utils.randomUsernameString());
		Assert.assertFalse(nPaget.isSubmitEnabled(driver), "Submit button should not be enabled!");
		Assert.assertEquals(nPaget.getEmailError(driver), Global.INVALID_EMAIL);
		driver.close();

	}

	@Test(priority = 5, enabled = true)
	public void createCustomerInvalidPin() throws Exception {

		coreTest.signIn(driver);
		NewCustomerPage nPage = createCustomerFirstPage(true);
		nPage.selectCountry(driver);
		nPage.enterAddress(driver, Global.ADDRESS);
		nPage.enterCity(driver, Global.CITY);
		nPage.enterPostalCode(driver, Global.POSTAL);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPage.selectPhoneType(driver, Global.PHONETYPE);
		nPage.selectSecurityQ(driver);
		nPage.enterSecuirtyA(driver, Global.SECURITYA);
		nPage.enterUserName(driver, Utils.randomUsernameString());
		nPage.enterPin(driver, Global.BAD_PIN);
		Assert.assertEquals(nPage.getPinError(driver), Global.PIN_ERROR);
		driver.close();
	}

	@Test(priority = 6, enabled = true)
	public void createCustomerInvalidUserName() throws Exception {

		coreTest.signIn(driver);
		NewCustomerPage nPage = createCustomerFirstPage(true);
		nPage.selectCountry(driver);
		nPage.enterAddress(driver, Global.ADDRESS);
		nPage.enterCity(driver, Global.CITY);
		nPage.enterPostalCode(driver, Global.POSTAL);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPage.selectPhoneType(driver, Global.PHONETYPE);
		nPage.selectSecurityQ(driver);
		nPage.enterSecuirtyA(driver, Global.SECURITYA);
		nPage.enterUserName(driver, Global.USER);

		// Check to see why error message has changed on the app
		Assert.assertEquals(nPage.getEmailError(driver), "Invalid email address.");
		driver.close();
	}

	@Test(priority = 7, enabled = true)
	public void createCustomerDuplicateUserName() throws Exception {

		coreTest.signIn(driver);
		coreTest.createCustomerClickHome(driver);

		// Sign back in and attempt to create a second customer with duplicate
		// email
		saveEmail = false;
		NewCustomerPage nPage = createCustomerFirstPage(saveEmail);

		nPage.selectContactType(driver, Global.CONTACTTYPE);
		nPage.selectCountry(driver);
		nPage.enterAddress(driver, Global.ADDRESS);
		nPage.enterCity(driver, Global.CITY);
		nPage.selectState(driver);
		nPage.enterPostalCode(driver, Global.POSTAL);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPage.selectPhoneType(driver, Global.PHONETYPE);
		nPage.selectSecurityQ(driver);
		nPage.enterSecuirtyA(driver, Global.SECURITYA);

		coreTest.getUserData();
		// using email address for username based on new nextwave api
		Log.info("email being retreived is: " + userData.getEmail());
		coreTest.getUserData();
		nPage.enterUserName(driver, userData.getEmail());
		nPage.enterPin(driver, Global.PIN);
		nPage.enterDob(driver, Global.DOB);
		nPage.clickSubmit(driver);
		Assert.assertEquals(nPage.getDuplicateError(driver), Global.DUPLICATE_ERROR);
		driver.close();
	}

	@Test(priority = 8, enabled = true)
	public void createCustomerInvalidPhone() throws Exception {

		coreTest.signIn(driver);
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.getLandingPage(Global.URL1);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickNewCustomer(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, Global.FNAME);
		nPage.enterLastname(driver, Global.LNAME);
		email = Utils.randomEmailString();
		nPage.enterEmail(driver, email);
		nPage.enterPhone(driver, Global.INVALID_PHONE);
		// Assert.assertEquals(nPage.g(driver), Global.INVALID_PHONE );
		driver.close();

	}

	@Test(priority = 9, enabled = true)
	public void createCustomerNameMissing() throws Exception {

		coreTest.signIn(driver);
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.getLandingPage(Global.URL1);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickNewCustomer(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		email = Utils.randomEmailString();
		nPage.enterEmail(driver, email);
		Assert.assertFalse(nPage.isContinueEnabled(driver), "Continue button should not be enabled!");
		driver.close();

	}

	@Test(priority = 10, enabled = true)
	public void createCustomerEmailMissing() throws Exception {

		coreTest.signIn(driver);
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.getLandingPage(Global.URL1);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickNewCustomer(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, Global.FNAME);
		nPage.enterLastname(driver, Global.LNAME);
		Assert.assertFalse(nPage.isContinueEnabled(driver), "Continue button should not be enabled!");
		driver.close();

	}

	// pin should not contain user name
	@Test(priority = 11, enabled = true)
	public void createCustomerInvalidPin2() throws Exception {

		coreTest.signIn(driver);
		NewCustomerPage nPage = createCustomerFirstPage(true);
		nPage.selectContactType(driver, Global.CONTACTTYPE);
		nPage.selectCountry(driver);
		nPage.enterAddress(driver, Global.ADDRESS);
		nPage.enterCity(driver, Global.CITY);
		nPage.selectState(driver);
		nPage.enterPostalCode(driver, Global.POSTAL);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPage.selectPhoneType(driver, Global.PHONETYPE);
		nPage.selectSecurityQ(driver);
		nPage.enterSecuirtyA(driver, Global.SECURITYA);
		nPage.enterUserName(driver, "test1234@yahoo.com");
		nPage.enterPin(driver, "1234");
		nPage.enterDob(driver, Global.DOB);
		nPage.clickSubmit(driver);
		Assert.assertEquals(nPage.getPinError2(driver), Global.PIN_ERROR2);
		driver.close();

	}

	@Test(priority = 12, enabled = true)
	public void createCustomerPostalCodeMissing() throws Exception {

		coreTest.signIn(driver);
		NewCustomerPage nPage = createCustomerFirstPage(true);
		nPage.selectContactType(driver, Global.CONTACTTYPE);
		nPage.selectCountry(driver);
		nPage.enterAddress(driver, Global.ADDRESS);
		nPage.enterCity(driver, Global.CITY);
		nPage.selectState(driver);
		// postal code missing
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
		nPage.selectPhoneType(driver, Global.PHONETYPE);
		nPage.selectSecurityQ(driver);
		nPage.enterSecuirtyA(driver, Global.SECURITYA);
		nPage.enterUserName(driver, "adam@yahoo.com");
		nPage.enterPin(driver, Global.PIN);
		nPage.enterDob(driver, Global.DOB);
		Assert.assertFalse(nPage.isSubmitEnabled(driver), "Submit button should be disabled");
		driver.close();

	}

	// CCD-584 - Using + in gmail account
	@Test(priority = 13, enabled = true)
	public void createNewCustomerGmailPlus() throws Exception {

		coreTest.signIn(driver);
		email = Utils.randomEmailStringGmailPlus();
		coreTest.createCustomer(driver, email);

		Log.info("gmail plus generated is: " + email);
		phoneNumber = coreTest.getPhone();
		NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
		Assert.assertEquals(nPage3.getFname(driver), Global.FNAME);
		Log.info("Actual results " + nPage3.getFname(driver) + " matches expected results " + Global.FNAME);
		Assert.assertEquals(nPage3.getLname(driver), Global.LNAME);
		Log.info("Actual results " + nPage3.getLname(driver) + " matches expected results " + Global.LNAME);
		Assert.assertEquals(nPage3.getEmail(driver), email);
		Log.info("Actual results " + nPage3.getEmail(driver) + " matches expected results " + email);
		Assert.assertEquals(nPage3.getPhone(driver), phoneNumber);
		Log.info("Actual results " + nPage3.getPhone(driver) + " matches expected results " + phoneNumber);
		Assert.assertEquals(nPage3.getAddress1(driver), Global.ADDRESS);
		Log.info("Actual results " + nPage3.getAddress1(driver) + " matches expected results " + Global.ADDRESS);
		Assert.assertEquals(nPage3.getAddress(driver).substring(0, 12), Global.ADDRESS);
		Log.info("Actual results " + nPage3.getAddress(driver).substring(0, 12) + " matches " + Global.ADDRESS);
		Assert.assertEquals(nPage3.getContactType(driver), Global.CONTACTTYPE);
		Log.info("Actual results " + nPage3.getContactType(driver) + " matches expected results " + Global.CONTACTTYPE);
		Assert.assertEquals(nPage3.getCity(driver), Global.CITY);
		Log.info("Actual results " + nPage3.getCity(driver) + " matches expected results " + Global.CITY);
		Assert.assertEquals(nPage3.getState(driver), STATE);
		Log.info("Actual results " + nPage3.getState(driver) + " matches expected results " + STATE);
		Assert.assertEquals(nPage3.getPostalCode(driver), Global.POSTAL);
		Log.info("Actual results " + nPage3.getPostalCode(driver) + " matches expected results " + Global.POSTAL);
		driver.close();
	}

	// private methods
	private NewCustomerPage createCustomerFirstPage(boolean saveEmail) throws Exception {

		if (saveEmail == true) {
			DashboardPage dashPage = new DashboardPage(driver);
			dashPage.getLandingPage(Global.URL1);
			dashPage.clickCustomerTab(driver);
			dashPage.switchToFrame(driver);
		}

		CreateCustomerPage nPage = new CreateCustomerPage(driver);
		nPage.clickNewCustomer(driver);
		nPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		nPage.enterFirstname(driver, Global.FNAME);
		nPage.enterLastname(driver, Global.LNAME);
		email = Utils.randomEmailString();
		nPage.enterEmail(driver, email);

		if (saveEmail == true) {
			System.out.println("inside the if statement " + saveEmail);
			userData.setEmail(email);
		}

		phoneNumber = Utils.randomPhoneNumber();
		nPage.enterPhone(driver, phoneNumber);
		nPage.clickContinue(driver);
		NewCustomerPage nPaget = new NewCustomerPage(driver);
		return nPaget;

	}

	@AfterMethod
	public void tearDown() {
		Log.info("TearDown Complete");
		Reporter.log("TearDown Complete");
		driver.quit();

	}
}