package automationFramework.PageObjects;

import java.awt.AWTException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import automationFramework.Utilities.Utils;
import org.openqa.selenium.Keys;

public class ContactDetailsPage extends BasePage {

	// Element Locators
	private static final String FNAME = "//*[@id='customerContact_firstname_txt']";
	private static final String LNAME = "//*[@id='customerContact_lastname_txt']";
	private static final String CONTACT_TYPE = "///*[@id='customerContact_contactType_sel']";
	private static final String ADDRESS = "//*[@id='customerContact_address_sel']";
	private static final String NEWADDRESS = "//*[@id='customerContact_newAddress_btn']";
	private static final String PHONE = "//*[@id='customerContact_phone1_txt']";
	private static final String PHONETYPE = "//*[@id='customerContact_primryPhoneType_sel']";
	private static final String PHONE2 = "//*[@id='customerContact_phone2_txt']";
	private static final String PHONETYPE2 = "//*[@id='customerContact_secondaryPhoneType_sel']";
	private static final String PHONE3 = "//*[@id='customerContact_phone3_txt']";
	private static final String PHONETYPE3 = "//*[@id='customerContact_tertiaryPhoneType_sel']";
	private static final String EMAIL = "//*[@id='customerContact_email_txt']";
	private static final String USERNAME = ".//*[@id='createCustomer_username_tst']";
	private static final String PIN = "//*[@id='customerContact_pin_txt']";
	private static final String SECURITYQ = "//*[@id='customerContact_sequrityQ_sel']";
	private static final String SECURITYA = "//*[@id='customerContact_sequrityA_txt']";
	private static final String DOB = "//*[@id='customerContact_dob_txt']/span/input";
	private static final String SUBMIT = "//*[@id='customerContact_submit_btn']";
	private static final String CANCEL = "//*[@id='customerContact_cancel_btn']";
	private static final String REQUIREDF_ERROR = "//*[@id='v-required-alert']";
	private static final String REQUIREDF_MUTUAL_ERROR = "#customerContact > div:nth-child(3) > div:nth-child(2) > div:nth-child(3)";
	private static final String COUNTRY = "//*[@id='customerContact_country_sel']";
	private static final String ADDRESSLINE1 = "//*[@id='customerContact_address1_txt']";
	private static final String ADDRESSLINE2 = "//*[@id='customerContact_address2_txt']";
	private static final String CITY = "//*[@id='customerContact_city_txt']";
	private static final String STATE = "//*[@id='customerContact_state_sel']";
	private static final String POSTALCODE = "//*[@id='customerContact_postalCode_txt']";
	private static final String INVALID_EMAIL_ERROR = "v-email-alert";
	private static final String INVALID_FORMAT_ERROR = "//*[@id='v-format-alert']";
	private static final String MIN_LENGTH_ERROR = "//*[@id='v-minlength-alert']";
	private static final String NEW_DOB = "//*[@id='customerContact_dob_txt']/span/div/table/tbody/tr[3]/td[4]/a";
	private static final String DOB_YEAR = ".//*[@id='customerContact_dob_txt']/span/div/div/div/select[2]";
	private static final String RESET_PASSWORD = "//*[@id='customerContact_pwReset_btn']";

	public ContactDetailsPage(WebDriver driver) {
		super(driver);
	}

	public void selectContactType(WebDriver driver) throws InterruptedException, AWTException {
		Select mySelect = new Select(driver.findElement(By.xpath(CONTACT_TYPE)));
		mySelect.selectByIndex(1);
	}

	public void selectAddress(WebDriver driver) throws InterruptedException, AWTException {
		Select mySelect = new Select(driver.findElement(By.xpath(ADDRESS)));
		mySelect.selectByIndex(1);
	}

	public void enterFname(WebDriver driver, String fname) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(FNAME)).clear();
		driver.findElement(By.xpath(FNAME)).sendKeys(fname);
	}

	public void deleteFname(WebDriver driver) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(FNAME)).click();
		driver.findElement(By.xpath(FNAME)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(FNAME)).sendKeys(Keys.ENTER);
	}

	public void enterLname(WebDriver driver, String lname) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(LNAME)).clear();
		driver.findElement(By.xpath(LNAME)).sendKeys(lname);
	}

	public void deleteLname(WebDriver driver) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(LNAME)).clear();
		driver.findElement(By.xpath(FNAME)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(FNAME)).sendKeys(Keys.ENTER);

	}

	public void enterPhone(WebDriver driver, String phone) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(PHONE)).clear();
		driver.findElement(By.xpath(PHONE)).sendKeys(phone);
	}

	public void selectPhoneType(WebDriver driver) throws InterruptedException, AWTException {
		Select mySelect = new Select(driver.findElement(By.xpath(PHONETYPE)));
		mySelect.selectByIndex(2);
	}

	public void enterPhone2(WebDriver driver, String phone2) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(PHONE2)).clear();
		driver.findElement(By.xpath(PHONE2)).sendKeys(phone2);
	}

	public void selectPhoneType2(WebDriver driver) throws InterruptedException, AWTException {
		Select mySelect = new Select(driver.findElement(By.xpath(PHONETYPE2)));
		mySelect.selectByIndex(2);
	}

	public void enterPhone3(WebDriver driver, String phone3) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(PHONE3)).clear();
		driver.findElement(By.xpath(PHONE3)).sendKeys(phone3);
	}

	public void selectPhoneType3(WebDriver driver) throws InterruptedException, AWTException {
		Select mySelect = new Select(driver.findElement(By.xpath(PHONETYPE3)));
		mySelect.selectByIndex(2);
	}

	public void enterEmail(WebDriver driver, String email) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(EMAIL)).clear();
		driver.findElement(By.xpath(EMAIL)).sendKeys(email);
	}

	public void enterUsername(WebDriver driver, String username) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(USERNAME)).clear();
		driver.findElement(By.xpath(USERNAME)).sendKeys(username);
	}

	public void enterPin(WebDriver driver, String pin) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(PIN)).click();
		driver.findElement(By.xpath(PIN)).clear();
		driver.findElement(By.xpath(PIN)).sendKeys(pin);
	}

	public void selectSequirtyQuestion(WebDriver driver) throws InterruptedException, AWTException {
		Select mySelect = new Select(driver.findElement(By.xpath(SECURITYQ)));
		mySelect.selectByIndex(2);
	}

	public void enterSecurityAnswer(WebDriver driver, String answer) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(SECURITYA)).clear();
		driver.findElement(By.xpath(SECURITYA)).sendKeys(answer);
	}

	public void enterDob(WebDriver driver, String dob) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(DOB)).click();
		driver.findElement(By.xpath(DOB)).clear();
		driver.findElement(By.xpath(DOB)).sendKeys(dob);
	}

	public void updateDob(WebDriver driver) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(DOB)).click();
		driver.findElement(By.xpath(DOB)).click();
		Select mySelect = new Select(driver.findElement(By.xpath(DOB_YEAR)));
		mySelect.selectByIndex(1);
		driver.findElement(By.xpath(NEW_DOB)).click();
		Utils.waitTime(3000);
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ENTER);
	}

	public void enterAddress(WebDriver driver, String address) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(ADDRESSLINE1)).clear();
		driver.findElement(By.xpath(ADDRESSLINE1)).sendKeys(address);
	}

	public void enterAddress2(WebDriver driver, String address2) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(ADDRESSLINE2)).clear();
		driver.findElement(By.xpath(ADDRESSLINE2)).sendKeys(address2);
	}

	public void enterCity(WebDriver driver, String city) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(CITY)).clear();
		driver.findElement(By.xpath(CITY)).sendKeys(city);
	}

	public void enterPostalCode(WebDriver driver, String postalCode) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(POSTALCODE)).clear();
		driver.findElement(By.xpath(POSTALCODE)).sendKeys(postalCode);
	}

	public void clickNewAddress(WebDriver driver) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(NEWADDRESS)).click();
	}

	public void clickSubmit(WebDriver driver) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(SUBMIT)).click();
	}

	public void clickCancel(WebDriver driver) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(CANCEL)).click();
	}

	public void clickResetPassword(WebDriver driver) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(RESET_PASSWORD)).click();
	}

	public String getRequiredFieldError(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.id(REQUIREDF_ERROR)).getText();
	}

	public String getRequiredFieldMutualError(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.cssSelector(REQUIREDF_MUTUAL_ERROR)).getText();
	}

	public String getInvalidFormatError(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(INVALID_FORMAT_ERROR)).getText();
	}

	public String getMinLengthError(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(MIN_LENGTH_ERROR)).getText();
	}

	public String getInvalidEmailError(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.id(INVALID_EMAIL_ERROR)).getText();
	}

	public void selectCountry(WebDriver driver) throws InterruptedException, AWTException {
		Select mySelect = new Select(driver.findElement(By.xpath(COUNTRY)));
		mySelect.selectByIndex(7);
	}

	public void selectState(WebDriver driver) throws InterruptedException, AWTException {
		Select mySelect = new Select(driver.findElement(By.xpath(STATE)));
		mySelect.selectByIndex(6);
	}

	public boolean isSubmitEnabled(WebDriver driver) throws InterruptedException, AWTException {
		return driver.findElement(By.xpath(SUBMIT)).isEnabled();
	}

}