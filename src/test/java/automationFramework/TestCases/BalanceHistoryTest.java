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
//Quality Center Test IDs: 75817, 72101
//#################################################################################

public class BalanceHistoryTest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static final String TRANSACTION_AMOUNT = "$1.88";
	private static final String ENDING_BALANCE = "$1.88";
	private static final String ENTRY_TYPE = "Load Value";
	private static final String PURSE_RESTRICTION = "Unrestricted";
	private static final String TOKEN_TYPE = "Bankcard";
	private static final String SUBSYSTEM = "ABP";
	private static final String PURSE_RC = "Default Purse";
	private static final String CHARGE = "Charge";
	private static final String LOAD = "LoadValue";
	private static final String NO_RECORD_FOUND = "No records found";

	static WebDriver driver;
	static String browser;
	private static String phoneNumber;
	private static String email;
	private static CustomerData cData;
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

	// STA-721 - view unregistered customer balance history dynamic
	@Test(enabled = true)
	public void viewBalanceHistoryUnregisteredDynamic() throws Exception {

		// create travel balance via soap call
		SOAPClientSAAJ sClient = new SOAPClientSAAJ();
		CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
		String validCCNumber = ccGenerator.generate("4", 16);
		Log.info("New CC Number is:  " + validCCNumber);
		String accountID = sClient.travelHistorySOAPCall(validCCNumber);
		Log.info("cc number being used is " + validCCNumber);
		Log.info("account id being returned is " + accountID);

		// wait time for balnce history to display on cmc
		Utils.waitTime(120000);

		coreTest.signIn(driver);
		TokenSearchPage tPage = getTokenSearchPage();
		tPage.selectTokenType(driver, TOKEN_TYPE);
		tPage.selectSubsystem(driver, SUBSYSTEM);
		tPage.enterBankNumber(driver, validCCNumber);
		tPage.selectExpMonth(driver);
		tPage.selectExpYear(driver);
		tPage.clickSearchToken(driver);
		tPage.clickViewDetails(driver);
		tPage.clickBalanceHistorySubAccount(driver);
		BalanceHistoryPage bdPage = new BalanceHistoryPage(driver);
		Assert.assertEquals(bdPage.getPurseSubAccount(driver), Global.PURSE);
		Assert.assertEquals(bdPage.getPurseRestriction(driver), PURSE_RESTRICTION);
		Assert.assertEquals(bdPage.getEntryTypeSubAccount(driver), ENTRY_TYPE);
		Assert.assertEquals(bdPage.getTransactionAmountSubAccount(driver), TRANSACTION_AMOUNT);
		Assert.assertEquals(bdPage.getEndingBalanceSubAccount(driver), ENDING_BALANCE);
		driver.close();

	}

	// view registered customer balance history dynamic
	@Test(enabled = true)
	public void viewBalanceHistoryRegisteredDynamic() throws Exception {

		registerCustomerAndCreateBalance(driver);
		BalanceHistoryPage bdPage = new BalanceHistoryPage(driver);
		Assert.assertEquals(bdPage.getPurse(driver), PURSE_RC);
		Assert.assertEquals(bdPage.getEntryType(driver), ENTRY_TYPE);
		Assert.assertEquals(bdPage.getTransactionAmount(driver), TRANSACTION_AMOUNT);
		Assert.assertEquals(bdPage.getEndingBalance(driver), ENDING_BALANCE);
		driver.close();

	}

	// view registered customer balance history details
	@Test(enabled = true)
	public void viewBalanceHistoryDetailsRegisteredDynamic() throws Exception {

		registerCustomerAndCreateBalance(driver);
		BalanceHistoryPage bhPage = new BalanceHistoryPage(driver);
		bhPage.clickRow(driver);
		BalanceHistoryDetailPage bdPage = new BalanceHistoryDetailPage(driver);
		Assert.assertEquals(bdPage.getPurse(driver), PURSE_RC);
		Assert.assertEquals(bdPage.getEntryType(driver), ENTRY_TYPE);
		Assert.assertEquals(bdPage.getTransactionAmount(driver), TRANSACTION_AMOUNT);
		Assert.assertEquals(bdPage.getEndingBalance(driver), ENDING_BALANCE);
		driver.close();

	}

	// CCD-851 - balance history filter
	@Test(enabled = true)
	public void viewBalanceHistoryFilterTest() throws Exception {

		registerCustomerAndCreateBalance(driver);
		BalanceHistoryPage bdPage = new BalanceHistoryPage(driver);
		bdPage.selectEntryType(driver, CHARGE);
		bdPage.clickBalanceHistoryFilter(driver);
		Assert.assertEquals(bdPage.getNoRecordFound(driver), NO_RECORD_FOUND);
		bdPage.selectEntryType(driver, LOAD);
		bdPage.clickBalanceHistoryFilter(driver);

		Assert.assertEquals(bdPage.getPurse(driver), PURSE_RC);
		Assert.assertEquals(bdPage.getEntryType(driver), ENTRY_TYPE);
		Assert.assertEquals(bdPage.getTransactionAmount(driver), TRANSACTION_AMOUNT);
		Assert.assertEquals(bdPage.getEndingBalance(driver), ENDING_BALANCE);
		driver.close();

	}

	// private methods
	private WebDriver registerCustomerAndCreateBalance(WebDriver driver) throws Exception {

		// create balance via soap api
		SOAPClientSAAJ sClient = new SOAPClientSAAJ();
		CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
		String validCCNumber = ccGenerator.generate("4", 16);
		String accountID = sClient.travelHistorySOAPCall(validCCNumber);
		Log.info("cc number being used is " + validCCNumber);
		Log.info("account id being returned is " + accountID);

		// Create customer test data via rest api
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

		BasePage bPage = new BasePage(driver);
		bPage.clickLinkAccount(driver);
		LinkAccountPage lPage = new LinkAccountPage(driver);

		// use cc number from soap call to link account
		lPage.enterBankAccount(driver, validCCNumber);
		lPage.selectExpMonth(driver);
		lPage.selectExpYear(driver, 2);
		lPage.clickSearchToken(driver);
		lPage.enterNickName(driver, Global.NICKNAME);
		lPage.clickLinkAccount(driver);

		// check balance history
		bPage.clickBalanceHistory(driver);
		return driver;
	}

	private TokenSearchPage getTokenSearchPage() throws Exception {
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		TokenSearchPage tPage = new TokenSearchPage(driver);
		return tPage;
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