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
import automationFramework.DAO.DBAutomation;
import automationFramework.PageObjects.CreateFundingPage;
import automationFramework.PageObjects.CreateOrderPage;
import automationFramework.PageObjects.NewCustomerDisplayPage;
import automationFramework.Utilities.*;

//#################################################################################
//Quality Center Test IDs: 77611, 77612
//#################################################################################

public class CreateOrderDBTest {

	private static Logger Log = Logger.getLogger(Logger.class.getName());
	private static String email;
	private static boolean orderRecordFound;
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
	public void createOrderDBcheck() throws Exception {

		coreTest.signIn(driver);
		coreTest.createCustomer(driver);
		email = coreTest.getEmail();
		Log.info("Email being passed to DAO: " + email);
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

		Log.info("checking the database now");
		DBAutomation dbAuto = new DBAutomation();
		dbAuto.dbCmsConnect();
		Log.info("Cms connected");
		int customerID = dbAuto.dbFindCustomerId(email);
		Log.info("customer id found in db: " + customerID);
		dbAuto.dbDisconnect();

		DBAutomation dbAuto2 = new DBAutomation();
		dbAuto2.dbOamConnect();
		Log.info("Oam connected");
		orderRecordFound = dbAuto2.dbFindJournalEntry(customerID);
		dbAuto2.dbDisconnectOAM();

		Assert.assertTrue(orderRecordFound, "error - order record was not found in the database");
		driver.close();
		Log.info("createOrderDBcheck Completed");

	}

	@Test(priority = 2, enabled = true)
	public void createOrderCancelDBcheck() throws Exception {
		
		coreTest.signIn(driver);
		coreTest.createCustomer(driver);
		email = coreTest.getEmail();
		Log.info("Email being passed to DAO: " + email);
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

		Log.info("checking the database now");
		DBAutomation dbAuto = new DBAutomation();
		dbAuto.dbCmsConnect();
		Log.info("Cms connected");
		int customerID = dbAuto.dbFindCustomerId(email);
		Log.info("customer id found in db: " + customerID);
		dbAuto.dbDisconnect();

		DBAutomation dbAuto2 = new DBAutomation();
		dbAuto2.dbOamConnect();
		Log.info("Oam connected");
		orderRecordFound = dbAuto2.dbFindJournalEntry(customerID);
		dbAuto2.dbDisconnectOAM();

		Assert.assertFalse(orderRecordFound, "error - order record was found for a canceled order");
		driver.close();
		Log.info("createOrderDBcheck Completed");

	}

	@AfterMethod
	public void tearDown() {
		Log.info("TearDown Completed");
		Reporter.log("TearDown Completed");
		driver.quit();

	}
}