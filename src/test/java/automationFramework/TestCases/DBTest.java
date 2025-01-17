package automationFramework.TestCases;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import automationFramework.DAO.DBAutomation;
import automationFramework.Utilities.*;

//#################################################################################
//Generic test to check DB connections
//#################################################################################

public class DBTest {

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

	}

	@Test(enabled = true)
	public void checkDB() throws Exception {

		DBAutomation dbAuto = new DBAutomation();
		dbAuto.dbCmsConnect();
		dbAuto.dbFindCustomer("WR7jIPKSkqYN@gmail.com");
	

	}

	@AfterMethod
	public void tearDown() {
		Log.info("TearDown Complete");
		Reporter.log("TearDown Complete");
	

	}
}