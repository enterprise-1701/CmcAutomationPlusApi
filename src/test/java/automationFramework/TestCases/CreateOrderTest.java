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
import automationFramework.RestApi.ApiCustomerPost;
import automationFramework.Utilities.*;
import org.openqa.selenium.JavascriptExecutor;

//#################################################################################
//Quality Center Test IDs: 77611, 77613, 77612
//#################################################################################

public class CreateOrderTest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static final String AMOUNT = "$1.00";
	private static final String BALANCE = "$1.00";
	private static final String EXPIRATION = "01/2021";
	private static final String CARDTYPE = "Active";
	private static CustomerData cData;
	private static String phoneNumber;
	private static String email;

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

	@Test(priority = 1, enabled = true)
	public void createOrderSubmit() throws Exception {

		createNewCustomer(driver);
		// create order using UI
		NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
		nPage3.clickFundingSource(driver);
		CreateFundingPage cPage = new CreateFundingPage(driver);
		cPage.selectPaymentType(driver, 1);
		cPage.enterName(driver, Global.CCNAME);
		cPage.enterCC(driver, Global.CC);
		cPage.selectMonth(driver);
		cPage.selectYear(driver);
		cPage.clickSubmit(driver);
		cPage.clickCreateOrder(driver);
		CreateOrderPage oPage = new CreateOrderPage(driver);
		oPage.selectOrderType(driver);
		oPage.selectPurseType(driver);
		oPage.selectOrderAmount(driver);
		oPage.clickSubmit(driver);
		oPage.clickBalanceHistoryExpand(driver);
		Assert.assertEquals(oPage.getPurse(driver), "Default Purse");
		Assert.assertEquals(oPage.getEntryType(driver), Global.ENTRY_TYPE);
		
		Utils.waitTime(10000);
		Assert.assertEquals(oPage.getTransactionAmount(driver), AMOUNT);
		Assert.assertEquals(oPage.getAvaiableBalance(driver), BALANCE);
		driver.close();

	}

	// test case paying with expired funding source. You have to go back and
	// change the expiration date via database
	@Test(priority = 2, enabled = true)
	public void createOrderExpiredCard() throws Exception {

		createNewCustomer(driver);
		NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
		nPage3.clickFundingSource(driver);
		CreateFundingPage cPage = new CreateFundingPage(driver);
		cPage.selectPaymentType(driver, 1);
		cPage.enterName(driver, Global.CCNAME);
		cPage.enterCC(driver, Global.CC);
		cPage.selectMonth(driver);
		cPage.selectYear(driver);
		cPage.clickSubmit(driver);
		cPage.clickCreateOrder(driver);
		CreateOrderPage oPage = new CreateOrderPage(driver);
		oPage.selectOrderType(driver);
		oPage.selectPurseType(driver);
		oPage.selectOrderAmount(driver);
		oPage.clickSubmit(driver);
		oPage.clickBalanceHistoryExpand(driver);

		Assert.assertEquals(oPage.getPurse(driver), "Default Purse");
		Assert.assertEquals(oPage.getEntryType(driver), Global.ENTRY_TYPE);
		Assert.assertEquals(oPage.getTransactionAmount(driver), AMOUNT);
		Assert.assertEquals(oPage.getAvaiableBalance(driver), BALANCE);
		driver.close();

	}

	@Test(priority = 3, enabled = true)
	public void createOrderCancel() throws Exception {

		createNewCustomer(driver);
		NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
		nPage3.clickFundingSource(driver);
		CreateFundingPage cPage = new CreateFundingPage(driver);
		cPage.selectPaymentType(driver, 1);
		cPage.enterName(driver, Global.CCNAME);
		cPage.enterCC(driver, Global.CC);
		cPage.selectMonth(driver);
		cPage.selectYear(driver);
		cPage.clickSubmit(driver);
		cPage.clickCreateOrder(driver);
		CreateOrderPage oPage = new CreateOrderPage(driver);
		oPage.selectOrderType(driver);
		oPage.selectPurseType(driver);
		oPage.selectOrderAmount(driver);
		oPage.clickCancel(driver);
		Assert.assertTrue(oPage.isCreateOrderDisplayed(driver), "create order link should be displayed!");
		driver.close();

	}

	@Test(priority = 4, enabled = true)
	public void createOrderNewFundingSource() throws Exception {

		createNewCustomer(driver);
		NewCustomerDisplayPage nPage3 = new NewCustomerDisplayPage(driver);
		nPage3.clickFundingSource(driver);
		CreateFundingPage cPage = new CreateFundingPage(driver);
		cPage.selectPaymentType(driver, 1);
		cPage.enterName(driver, Global.CCNAME);
		cPage.enterCC(driver, Global.CC);
		cPage.selectMonth(driver);
		cPage.selectYear(driver);
		cPage.clickSubmit(driver);
        Utils.waitTime(3000);
		
		cPage.clickCreateOrder(driver);
		CreateOrderPage oPage = new CreateOrderPage(driver);
		oPage.selectOrderType(driver);
		oPage.selectPurseType(driver);
		oPage.selectOrderAmount(driver);
		oPage.clickNewFundingSource(driver);

		cPage.selectPaymentType(driver, 1);
		cPage.enterName(driver, Global.CCNAME2);
		cPage.enterCC(driver, Global.CC2);
		cPage.selectMonth(driver);
		cPage.selectYear(driver);
		cPage.clickSubmit(driver);
		oPage.clickSubmit(driver);

		// Assertions on second card that got added
		Assert.assertEquals(cPage.getCardType2(driver), Global.CCTYPE);
		Log.info("Actual results " + cPage.getCardType2(driver) + " matches expected results " + Global.CCTYPE);
		Assert.assertEquals(cPage.getCardNumber2(driver), Global.CCMASKED2);
		Log.info("Actual results " + cPage.getCardNumber2(driver) + " matches expected results " + Global.CCMASKED2);
		Assert.assertEquals(cPage.getCardExpiration2(driver), EXPIRATION);
		Log.info("Actual results " + cPage.getCardExpiration2(driver) + " matches expected results " + EXPIRATION);
		Assert.assertEquals(cPage.getCardStatus2(driver), CARDTYPE);
		Log.info("Actual results " + cPage.getCardStatus2(driver) + " matches expected results " + CARDTYPE);
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