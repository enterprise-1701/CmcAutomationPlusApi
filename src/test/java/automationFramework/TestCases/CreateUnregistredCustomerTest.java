package automationFramework.TestCases;

import java.awt.AWTException;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
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
//Quality Center Test IDs: 75698, 74591, 75699, 77628
//#################################################################################

public class CreateUnregistredCustomerTest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());

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

	// STA-695 - register anonymous customer - no record found
	// Search token no records found and register new customer
	@Test(priority = 1, enabled = true)
	public void registerNewCustomerNoRecordFound() throws Exception {

		coreTest.signIn(driver);
		TokenSearchPage tPage = getTokenSearchPage();
		tPage.selectTokenType(driver, "Bankcard");
		tPage.selectSubsystem(driver, "ABP");

		// generate a new CC number that is not in the subsystem
		CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
		tPage.enterBankNumber(driver, ccGenerator.generate("4", 16));
		tPage.selectExpMonth(driver);
		tPage.selectExpYear(driver);
		tPage.clickSearchToken(driver);
		tPage.enterTokenVerificationNickName(driver, Global.NICKNAME);
		tPage.clickTokenVerificationRegisterCustomer(driver);
		coreTest.createCustomerTokenSearch(driver);
		verifyNewCustomerCreation();
		driver.close();

	}

	// STA-695 - register anonymous customer - transit token found
	// Search token transit token found and register new customer
	@Test(priority = 2, enabled = true)
	public void registerNewCustomerTranistTokenFound() throws Exception {

		// First generate transit account by using no records found
		coreTest.signIn(driver);
		TokenSearchPage tPage = getTokenSearchPage();
		tPage.selectTokenType(driver, "Bankcard");
		tPage.selectSubsystem(driver, "ABP");

		// generate a new CC number that is not in the system
		CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
		String validCCNumber = ccGenerator.generate("4", 16);
		Log.info("CC number being used:  " + validCCNumber);
		tPage.enterBankNumber(driver, validCCNumber);
		tPage.selectExpMonth(driver);
		tPage.selectExpYear(driver);
		tPage.clickSearchToken(driver);
		tPage.enterTokenVerificationNickName(driver, Global.NICKNAME);
		tPage.clickRegisterCustomer(driver);
		Assert.assertTrue(tPage.isLinkAccountConfirmationDisplayed(driver));
		tPage.clickHome(driver);
		
		// search for the token again with same cc number 
		tPage.enterBankNumber(driver, validCCNumber);
		tPage.selectExpMonth(driver);
		tPage.selectExpYear(driver);
		tPage.clickSearchToken(driver);
		TokenSearchCustomerVerifiPage vPage = new TokenSearchCustomerVerifiPage(driver);
		Assert.assertTrue(vPage.isAccountNumberDisplayed(driver));
		Assert.assertTrue(vPage.isTokenInfoDisplayed(driver));
		vPage.enterTokenVerificationNickName(driver, Global.NICKNAME);
		vPage.clickRegisterCustomer(driver);

		coreTest.createCustomerTokenSearch(driver);
		verifyNewCustomerCreation();
		driver.close();

	}

	// STA-695 - register anonymous customer - invalid token
	// Search token invalid token and register new customer
	@Test(priority = 3, enabled = true)
	public void registerNewCustomerInvalidToken() throws Exception {

		coreTest.signIn(driver);
		TokenSearchPage tPage = getTokenSearchPage();
		tPage.selectTokenType(driver, "Bankcard");
		tPage.selectSubsystem(driver, "ABP");
		tPage.enterBankNumber(driver, Global.INVALID_CC);
		tPage.selectExpMonth(driver);
		tPage.selectExpYear(driver);
		tPage.clickSearchToken(driver);
		Assert.assertEquals(tPage.getNoRecordFoundError(driver), "No Records Found");
		tPage.enterNickName(driver, Global.NICKNAME);
		tPage.clickRegisterCustomer(driver);
		Assert.assertEquals(tPage.getInavlidTokenError(driver), "Invalid Token");
		tPage.clickInvalidTokenRegister(driver);

		// register through normal create customer functionality
		coreTest.createCustomerTokenSearchInvalidToken(driver);
		verifyNewCustomerCreation();
		driver.close();

	}

	// STA-930 - register customer from link on subsystem page
	@Test(priority = 4, enabled = true)
	public void registerNewCustomerSubysystemPage() throws Exception {

		// create token travel history via soap call
		SOAPClientSAAJ sClient = new SOAPClientSAAJ();
		CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
		String validCCNumber = ccGenerator.generate("4", 16);
		String accountID = sClient.travelHistorySOAPCall(validCCNumber);
		System.out.println("cc number being used is " + validCCNumber);
		System.out.println("account id being returned is " + accountID);

		// Search for token using same cc number
		coreTest.signIn(driver);
		TokenSearchPage tPage = getTokenSearchPage();
		tPage.selectTokenType(driver, "Bankcard");
		tPage.selectSubsystem(driver, "ABP");
		tPage.enterBankNumber(driver, validCCNumber);
		tPage.selectExpMonth(driver);
		tPage.selectExpYear(driver);
		tPage.clickSearchToken(driver);
		tPage.enterTokenWithBalanceVerificationNickName(driver, Global.NICKNAME);
		tPage.clickViewDetails(driver);

		TokenSearchSubSystemPage sPage = new TokenSearchSubSystemPage(driver);
		sPage.clickRegisterCustomer(driver);
		RegisterSubaccountPage rPage = new RegisterSubaccountPage(driver);

		// Verify register customer pop-up, register customer and verify registration 
		Assert.assertEquals(rPage.getTitle(driver), "Register Customer");
		rPage.enterNickname(driver, Global.NICKNAME);
		rPage.clickRegister(driver);
		coreTest.createCustomerTokenSearch(driver);
		verifyNewCustomerCreation();
		driver.close();

	}

	// Do all the customer registration assertions
	private void verifyNewCustomerCreation() throws InterruptedException, AWTException {

		String email = coreTest.getEmail();
		String phoneNumber = coreTest.getPhone();
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
		Assert.assertEquals(nPage3.getPostalCode(driver), Global.POSTAL);
		Log.info("Actual results " + nPage3.getPostalCode(driver) + " matches expected results " + Global.POSTAL);
		Assert.assertTrue(nPage3.isLinkAccountDisplayed(driver));
		Log.info("Linked Accounts Displayed:  " + nPage3.isLinkAccountDisplayed(driver));
	}

	private TokenSearchPage getTokenSearchPage() throws Exception {
		DashboardPage dashPage = new DashboardPage(driver);
		dashPage.clickCustomerTab(driver);
		dashPage.switchToFrame(driver);
		TokenSearchPage tPage = new TokenSearchPage(driver);
		return tPage;
	}

	@AfterMethod
	public void tearDown() {
		Log.info("TearDown Complete");
		Reporter.log("TearDown Complete");
		driver.quit();

	}
}