package automationFramework.TestCases;

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
//Quality Center Test IDs: 75818, 77463, 72100, 72196
//#################################################################################

public class TravelHistoryTest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());

	private static final String TOKEN_TYPE = "Bankcard";
	private static final String SUBSYSTEM = "ABP";
	private static final String LOCATION = "railstation";
	private static final String LOCATION_UC = "railstation";
	private static final String TRAVEL_MODE = "Transit";
	private static final String TOKEN = "Bankcard";
	private static final String FARE = "$2.50";
	private static final String UNPAID_FARE = "$0.00";
	private static final String DEVICE = "SAAJ-Auto";
	private static final String OPERATOR = "MTA/NYCT Bus";
	private static final String TRANSACTION_STATUS = "Success";
	private static final String SOURCE_UC = "NYCT";

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

	// STA-724 - view unregistred customer travel history
	@Test(enabled = true)
	public void viewTravelHistoryUnregistredCustomer() throws Exception {

		// create travel history via soap call
		SOAPClientSAAJ sClient = new SOAPClientSAAJ();
		CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
		String validCCNumber = ccGenerator.generate("4", 16);
		String accountID = sClient.travelHistorySOAPCall(validCCNumber);
		System.out.println("cc number being used is " + validCCNumber);
		System.out.println("account id being returned is " + accountID);

		// check cmc for travel history based on cc
		// takes around 6 minutes for travel history to show on cmc
		Utils.waitTime(360000);

		coreTest.signIn(driver);
		TokenSearchPage tPage = getTokenSearchPage();
		tPage.selectTokenType(driver, TOKEN_TYPE);
		tPage.selectSubsystem(driver, SUBSYSTEM);
		tPage.enterBankNumber(driver, validCCNumber);
		tPage.selectExpMonth(driver);
		tPage.selectExpYear(driver);
		tPage.clickSearchToken(driver);
		tPage.clickViewDetails(driver);
		tPage.clickTravelHistory(driver);

		TravelHistoryPage tvPage = new TravelHistoryPage(driver);
		Assert.assertEquals(tvPage.getLocation(driver), LOCATION_UC);
		Assert.assertEquals(tvPage.getTravelMode(driver), TRAVEL_MODE);
		Assert.assertEquals(tvPage.getToken(driver), TOKEN);
		Assert.assertEquals(tvPage.getFare(driver), FARE);
		Assert.assertEquals(tvPage.getUnpaidFare(driver), UNPAID_FARE);

		// check travel history details
		tvPage.clickTiming(driver);
		TravelHistoryDetailPage tvdPage = new TravelHistoryDetailPage(driver);
		Assert.assertEquals(tvdPage.getLocation(driver), LOCATION_UC);
		Assert.assertEquals(tvdPage.getDevice(driver), DEVICE);
		Assert.assertEquals(tvdPage.getOperator(driver), OPERATOR);
		Assert.assertEquals(tvdPage.getTransactionStatus(driver), TRANSACTION_STATUS);
		Assert.assertEquals(tvdPage.getFare(driver), FARE);
		Assert.assertEquals(tvdPage.getSource(driver), SOURCE_UC);
		tvdPage.clickClose(driver);

		Log.info("viewTravelHistoryUnregistredCustomer");
		driver.close();

	}

	// STA-686 - view regisetred customer travel history subaccount
	@Test(enabled = true)
	public void viewTravelHistoryRegistredCustomerSubAccount() throws Exception {

		// create travel history via soap call
		SOAPClientSAAJ sClient = new SOAPClientSAAJ();
		CreditCardNumberGenerator ccGenerator = new CreditCardNumberGenerator();
		String validCCNumber = ccGenerator.generate("4", 16);
		String accountID = sClient.travelHistorySOAPCall(validCCNumber);
		System.out.println("cc number being used is " + validCCNumber);
		System.out.println("account id being returned is " + accountID);

		// takes around 6 minutes for travel history to show on cmc
		Utils.waitTime(360000);

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

		// checking travel history under subaccount page
		bPage.clickLinkedAccount(driver);
		TokenSearchSubSystemPage ssPage = new TokenSearchSubSystemPage(driver);
		ssPage.clickTravelHistory(driver);
	
		Assert.assertEquals(ssPage.getLocation(driver), LOCATION);
		Assert.assertEquals(ssPage.getTravelMode(driver), TRAVEL_MODE);
		Assert.assertEquals(ssPage.getToken(driver), TOKEN);
		Assert.assertEquals(ssPage.getFare(driver), FARE);
		Assert.assertEquals(ssPage.getUnpaidFare(driver), UNPAID_FARE);

		ssPage.clickTravelHistoryRow(driver);
		TravelHistoryDetailPage tdPage = new TravelHistoryDetailPage(driver);

		Assert.assertEquals(tdPage.getLocation(driver), LOCATION);
		Assert.assertEquals(tdPage.getOperator(driver), OPERATOR);
		Assert.assertEquals(tdPage.getTransactionStatus(driver), TRANSACTION_STATUS);

		Log.info("viewTravelHistoryRegistredCustomerSubAccount Completed");
		driver.close();
	}

	// STA-686 - view regisetred customer travel history oneaccount
	@Test(enabled = false)
	public void viewTravelHistoryRegistredCustomerOneAccount() throws Exception {

		// create travel history via soap call
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
	
		// do another tap after linking the account
		// verify that travel history shows up on one account now
		String postTab = sClient.travelHistoryPostTab(validCCNumber);
		Log.info("second tab was " + postTab);
		
		// takes around 6 minutes for travel history to show on cmc
        Utils.waitTime(360000);

		bPage.clickTravelHistory(driver);
			// do assertions on oneaccount travel history

		Log.info("viewTravelHistoryRegistredCustomerOneAccount Completed");
		driver.close();
	}

	// private methods
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