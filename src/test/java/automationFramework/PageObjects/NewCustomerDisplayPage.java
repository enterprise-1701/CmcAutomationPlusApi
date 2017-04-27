package automationFramework.PageObjects;

import java.awt.AWTException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class NewCustomerDisplayPage extends BasePage {

	// Element Locators
	private static final String FNAME = "//*[@id='customerContacts_list_tbl']/div/div[1]/table/tbody/tr/td[1]";
	private static final String LNAME = "//*[@id='customerContacts_list_tbl']/div/div[1]/table/tbody/tr/td[2]";
	private static final String EMAIL = "//*[@id='customerContacts_list_tbl']/div/div[1]/table/tbody/tr/td[3]/span";
	private static final String PHONE = "//*[@id='customerContacts_list_tbl']/div/div[1]/table/tbody/tr/td[4]/span/span";
	private static final String ADDRESS = "//*[@id='customerContacts_list_tbl']/div/div[1]/table/tbody/tr/td[5]/span/span";
	private static final String DOB = "//*[@id='customerContacts_list_tbl']/div/div[1]/table/tbody/tr/td[6]/span/span";
	private static final String CUSTOMERTYPE = "//*[@id='customerContacts_list_tbl']/div/div[1]/table/tbody/tr/td[7]/span";
	private static final String CONTACTTYPE = "//*[@id='customerContacts_list_tbl']/div/div[1]/table/tbody/tr/td[7]/span";
	private static final String ADDRESS1 = "//*[@id='addresses_list_tbl']/div/div[1]/table/tbody/tr/td[1]/span";
	private static final String CITY = "//*[@id='addresses_list_tbl']/div/div[1]/table/tbody/tr/td[3]/span";
	private static final String STATE = "//*[@id='addresses_list_tbl']/div/div[1]/table/tbody/tr/td[4]";
	private static final String POSTALCODE = "//*[@id='addresses_list_tbl']/div/div[1]/table/tbody/tr/td[5]/span";
	private static final String FUNDSOURCE = "//a[contains(text(),'Create Funding Source')]";
	private static final String NEW_ADDRESS = "//*[@id='create_new_address_link']";
	private static final String ADDRESS_LIST = "//*[@id='addresses_list_tbl']/div/div[1]/table/tbody/tr/td[1]";
	private static final String CONTACT = "//*[@id='customerContacts_list_tbl']/div/div[1]/table/tbody/tr/td[1]";
	private static final String DELETE_FUNDING_SOURCE = "//*[@id='fundingSources_delete_btn']/span";
	private static final String FUNDING_SOURCE_NO_RECORD_FOUND = "//*[@id='fundingSources_list_tbl']/div/div/table/tbody/tr/td";

	public NewCustomerDisplayPage(WebDriver driver) {
		super(driver);
	}

	public String getFname(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(FNAME)).getText();
	}

	public String getLname(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(LNAME)).getText();
	}

	public String getEmail(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(EMAIL)).getText();
	}

	public String getPhone(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(PHONE)).getText();
	}

	public String getAddress(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(ADDRESS)).getText();
	}

	public String getDob(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(DOB)).getText();
	}

	public String getCustomerType(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(CUSTOMERTYPE)).getText();
	}

	public String getContactType(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(CONTACTTYPE)).getText();
	}

	public String getAddress1(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(ADDRESS1)).getText();
	}

	public String getCity(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(CITY)).getText();
	}

	public String getState(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(STATE)).getText();
	}

	public String getPostalCode(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(POSTALCODE)).getText();
	}

	public String getNoRecordFoundFundingSource(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(FUNDING_SOURCE_NO_RECORD_FOUND)).getText();
	}

	public void clickFundingSource(WebDriver driver) throws InterruptedException, AWTException {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(FUNDSOURCE)));
		driver.findElement(By.xpath(FUNDSOURCE)).click();
	}

	public void clickContact(WebDriver driver) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(CONTACT)).click();
	}

	public void clickNewAddress(WebDriver driver) throws InterruptedException, AWTException {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(NEW_ADDRESS)));
		driver.findElement(By.xpath(NEW_ADDRESS)).click();
	}

	public void clickInsideAddress(WebDriver driver) throws InterruptedException, AWTException {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ADDRESS_LIST)));
		driver.findElement(By.xpath(ADDRESS_LIST)).click();
	}

	public void deleteFundingSource(WebDriver driver) throws InterruptedException, AWTException {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DELETE_FUNDING_SOURCE)));
		driver.findElement(By.xpath(DELETE_FUNDING_SOURCE)).click();
	}

}