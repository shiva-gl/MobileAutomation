package com.intigral.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.intigral.utils.CommonaPage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class Checkout extends CommonaPage {
	private AndroidDriver<AndroidElement> driver;

	public Checkout(AndroidDriver<AndroidElement> driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	@AndroidFindBy(xpath = "//*[contains(@content-desc,'test-Item title')]")
	private List<AndroidElement> products;

	@AndroidFindBy(xpath = "//*[contains(@content-desc,'test-Price')]")
	private List<AndroidElement> productsPrice;

	@AndroidFindBy(xpath = "//*[contains(@text,'ADD TO CART')]")
	private AndroidElement addToKart;

	@AndroidFindBy(xpath = "//*[contains(@text,'ADD TO CART')]")
	private List<AndroidElement> addToKarts;

	@AndroidFindBy(className = "android.widget.ImageView")
	private List<AndroidElement> cartSymbol;

	@AndroidFindBy(xpath = "//*[contains(@text,'CHECKOUT')]")
	private AndroidElement chekout;

	@AndroidFindBy(xpath = "//*[contains(@text,'REMOVE')]")
	private AndroidElement remove;
	
	@AndroidFindBy(xpath = "//*[contains(@content-desc,'test-First Name')]")
	private AndroidElement fName;
	
	@AndroidFindBy(xpath = "//*[contains(@content-desc,'test-Last Name')]")
	private AndroidElement lName;
	
	@AndroidFindBy(xpath = "//*[contains(@content-desc,'test-Zip/Postal Code')]")
	private AndroidElement pCode;
	
	@AndroidFindBy(xpath = "//*[contains(@content-desc,'test-CONTINUE')]")
	private AndroidElement contNue;
	
	@AndroidFindBy(xpath = "//*[contains(@text,'FINISH')]")
	private AndroidElement finish;
	
	@AndroidFindBy(xpath = "//*[contains(@content-desc,'test-REMOVE')]")
	private AndroidElement testRemove;
	
	

	public String[] selectProductByName() {
		String prouct_name = products.get(0).getText();
		String product_price = productsPrice.get(0).getText();
		click(products.get(0));
		return new String[] { prouct_name, product_price };
	}
	
	public void slectProductByPrice() {
		click(productsPrice.get(0));
	}

	public String[] addTocartHome() {

		String prouct_name = products.get(0).getText();
		String product_price = productsPrice.get(0).getText();
		AndroidElement cart = (AndroidElement) new WebDriverWait(driver, 30)
				.until(ExpectedConditions.elementToBeClickable(addToKarts.get(0)));
		click(cart);
		return new String[] { prouct_name, product_price };
	}
	
	public void addItemsToCart() {
		
		for(AndroidElement ele: addToKarts) {
			click(ele);
		}

	}

	public boolean isElementDisplayed(String text) {
		AndroidElement element = (AndroidElement) new WebDriverWait(driver, 30).until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text," + "'" + text + "'" + ")]")));
		return element.isDisplayed();
	}

	public List<AndroidElement> isElementPresent(String text) {
		return driver.findElementsByXPath("//*[contains(@text," + "'" + text + "'" + ")]");
	}

	public void tapOnCartSymbol() {
		click(cartSymbol.get(3));
	}

	public void addToKart() {
		click(addToKart);
	}

	public void tapOnCheckout() {
		click(chekout);
	}

	public void tapOnRemove() {
		click(remove);
	}
	
	public void fillAddress() {
		try {
			fName.sendKeys("Test");
			lName.sendKeys("Test");
			pCode.sendKeys("12345");
		}
		catch(Exception e) {
			fail(e.getMessage());
		}
	}
	
	public void completeTheOrder() {
		scroll("CONTINUE");
		click(contNue);
		scroll("FINISH");
		click(finish);
		
	}
	
	public AndroidElement testRemove() {
		return testRemove;
	}
	

}
