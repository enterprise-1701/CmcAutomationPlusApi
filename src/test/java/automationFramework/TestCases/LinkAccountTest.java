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
//Quality Center Test IDs: 72098, 72096, 72095, 72097, 72099, 72198, 77675
//#################################################################################

public class LinkAccountTest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static final String ACCTLINKED_ERROR = "Account Already Linked";
	private static final String LINK_FAILURE = "Link Account Failure";
	static WebDriver driver;
	static String browser;

	CoreTest coreTest = new CoreTest();
	AccountData acctData = new AccountData();

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
	public void linkAccountTest() throws Exception {

		// create balance via soap call
		SOAPClientSAAJ sClient = new SOAPClientSAAJ();
		CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
		String validCCNumber = ccGenerator.generate("4", 16);
		String accountID = sClient.travelHistorySOAPCall(validCCNumber);
		Log.info("cc number being used is " + validCCNumber);
		Log.info("account id being returned is " + accountID);
		Log.info("waiting for ABP to get updated");
		Utils.waitTime(180000);

		// create account and link it to cc
		coreTest.signIn(driver);
		coreTest.createCustomer(driver);
		BasePage bPage = new BasePage(driver);
		bPage.clickLinkAccount(driver);
		LinkAccountPage lPage = new LinkAccountPage(driver);

		// use cc number from soap call to link account
		lPage.enterBankAccount(driver, validCCNumber);
		lPage.selectExpMonth(driver);
		lPage.selectExpYear(driver, 2);
		lPage.clickSearchToken(driver);
		lPage.enterNickName(driver, "adam");
		lPage.clickLinkAccount(driver);

		bPage.clickLinkedAccount(driver);
		TokenSearchSubSystemPage ssPage = new TokenSearchSubSystemPage(driver);
		Log.info(ssPage.getAccountNumber(driver));
		Assert.assertEquals(ssPage.getAccountNumber(driver).substring(11, 23), accountID);

	}

	@Test(priority = 2, enabled = true)
	public void linkAccountAlreadyLinkedTest() throws Exception {

		// create balance via soap call
		SOAPClientSAAJ sClient = new SOAPClientSAAJ();
		CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
		String validCCNumber = ccGenerator.generate("4", 16);
		String accountID = sClient.travelHistorySOAPCall(validCCNumber);
		Log.info("cc number being used is " + validCCNumber);
		Log.info("account id being returned is " + accountID);
		Log.info("waiting for ABP to get updated");
		Utils.waitTime(180000);

		// create account and link it to cc
		coreTest.signIn(driver);
		coreTest.createCustomer(driver);
		BasePage bPage = new BasePage(driver);
		bPage.clickLinkAccount(driver);
		LinkAccountPage lPage = new LinkAccountPage(driver);

		// use cc number from soap call to link account
		lPage.enterBankAccount(driver, validCCNumber);
		lPage.selectExpMonth(driver);
		lPage.selectExpYear(driver, 2);
		lPage.clickSearchToken(driver);
		lPage.enterNickName(driver, "adam");
		lPage.clickLinkAccount(driver);

		// create a second customer
		bPage.clickHome(driver);
		bPage.clickNewCustomer(driver);
		coreTest.createCustomer(driver);

		// attempt to link the second customer to the saved CC number which
		// is already linked to the first customer
		bPage.clickLinkAccount(driver);
		lPage.enterBankAccount(driver, validCCNumber);
		lPage.selectExpMonth(driver);
		lPage.selectExpYear(driver, 2);
		lPage.clickSearchToken(driver);
		Assert.assertEquals(lPage.getAlreadyLinkedError(driver), ACCTLINKED_ERROR);
		Assert.assertTrue(lPage.isViewLinkDisplayed(driver));
		lPage.clickView(driver);
		driver.close();

	}

	@Test(priority = 3, enabled = true)
	public void linkAccountFailureTest() throws Exception {

		coreTest.signIn(driver);
		coreTest.createCustomer(driver);
		BasePage bPage = new BasePage(driver);
		bPage.clickLinkAccount(driver);
		LinkAccountPage lPage = new LinkAccountPage(driver);
		lPage.enterBankAccount(driver, Global.INVALID_CC);
		lPage.selectExpMonth(driver);
		lPage.selectExpYear(driver, 2);
		lPage.clickSearchToken(driver);
		Assert.assertEquals(lPage.getLinkFailureDisplayed(driver), LINK_FAILURE);
		Log.info("linkAccountFailurelTest Completed");
		driver.close();

	}

	@Test(priority = 4, enabled = true)
	public void linkAccountCancelTest() throws Exception {

		coreTest.signIn(driver);
		coreTest.createCustomer(driver);
		BasePage bPage = new BasePage(driver);
		bPage.clickLinkAccount(driver);
		LinkAccountPage lPage = new LinkAccountPage(driver);
		lPage.enterBankAccount(driver, Global.CC);
		lPage.selectExpMonth(driver);
		lPage.selectExpYear(driver, 2);
		lPage.clickCancel(driver);
		Assert.assertTrue(lPage.isLinkAccountDisplayed(driver));
		Log.info("linkAccountCancelTest Completed");
		driver.close();
	}

	@Test(priority = 5, enabled = true)
	public void linkAccountCardExpirationMissingTest() throws Exception {

		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		sPage.enterFirstname(driver, Global.FNAME);
		sPage.enterLastname(driver, Global.LNAME);
		sPage.clickSearch(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
		sPage.clickRecord(driver);
		sPage.clickSecurityBox(driver);
		sPage.clickContiune(driver);
		sPage.clickLinkAccount(driver);
		LinkAccountPage lPage = new LinkAccountPage(driver);
		lPage.enterBankAccount(driver, Global.CC);
		Assert.assertFalse(lPage.isSearchTokenEnabled(driver));
		driver.close();

	}

	@Test(priority = 6, enabled = true)
	public void linkAccountBankAccountMissingTest() throws Exception {

		coreTest.signIn(driver);
		SearchPage sPage = getSearchPage();
		sPage.clickCustomerType(driver, Global.CUSTOMERTYPE);
		sPage.enterFirstname(driver, Global.FNAME);
		sPage.enterLastname(driver, Global.LNAME);
		sPage.clickSearch(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -275)", "");
		sPage.clickRecord(driver);
		sPage.clickSecurityBox(driver);
		sPage.clickContiune(driver);
		sPage.clickLinkAccount(driver);
		LinkAccountPage lPage = new LinkAccountPage(driver);
		lPage.selectExpMonth(driver);
		lPage.selectExpYear(driver, 2);
		Assert.assertFalse(lPage.isSearchTokenEnabled(driver));
		driver.close();

	}

	// CCD-909 test case
	@Test(priority = 7, enabled = true)
	public void linkAccountMessageTest() throws Exception {

		// create balance via soap call
		SOAPClientSAAJ sClient = new SOAPClientSAAJ();
		CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
		String validCCNumber = ccGenerator.generate("4", 16);
		String accountID = sClient.travelHistorySOAPCall(validCCNumber);
		Log.info("cc number being used is " + validCCNumber);
		Log.info("account id being returned is " + accountID);
		Log.info("waiting for ABP to get updated");
		Utils.waitTime(180000);

		// create account and link it to cc
		coreTest.signIn(driver);
		coreTest.createCustomer(driver);
		BasePage bPage = new BasePage(driver);
		bPage.clickLinkAccount(driver);
		LinkAccountPage lPage = new LinkAccountPage(driver);

		// first attempt to link with invalid cc token
		String invalidCCNumber = ccGenerator.generate("4", 16);
		lPage.enterBankAccount(driver, invalidCCNumber);
		lPage.selectExpMonth(driver);
		lPage.selectExpYear(driver, 2);
		lPage.clickSearchToken(driver);
		Assert.assertEquals(lPage.getAccountNotFoundError(driver), "Account not found");

		// now use valid cc number from soap call to link account
		lPage.clearBankAccount(driver);
		lPage.enterBankAccount(driver, validCCNumber);
		lPage.selectExpMonth(driver);
		lPage.selectExpYear(driver, 2);
		lPage.clickSearchToken(driver);

		// make assertions that account not found is not being displayed
		Assert.assertTrue(lPage.isLinkAccountVerificationPanelDisplayed(driver));
		Assert.assertFalse(lPage.isAccountNotFoundErrorDisplayed(driver));
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