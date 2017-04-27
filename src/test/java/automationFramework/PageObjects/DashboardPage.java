package automationFramework.PageObjects;

import java.awt.AWTException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {

	// Element Locators
	private static final String CUSTOMER = "//*[@id='header_customer_btn']";

	public DashboardPage(WebDriver driver) {
		super(driver);
	}

	public void clickCustomerTab(WebDriver driver) throws InterruptedException, AWTException {
		driver.findElement(By.xpath(CUSTOMER)).click();
	}

}