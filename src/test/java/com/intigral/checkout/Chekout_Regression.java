package com.intigral.checkout;

import com.intigral.pageobjects.Checkout;
import com.intigral.pageobjects.Login;
import com.intigral.utils.DriverInit;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;

public class Chekout_Regression extends DriverInit {
	public AndroidDriver<AndroidElement> driver;
	public Checkout chekoutPage;
	public Login loginpage;

	@BeforeMethod(alwaysRun = true)
	public void setUp() throws Exception {
		driver = driverinvoke();
		loginpage = new Login(driver);
		chekoutPage = new Checkout(driver);
		loginpage.loginWithValidCred();

		Assert.assertTrue(loginpage.homepage().isDisplayed());
	}

	@Test(enabled = true)
	public void checkout_product_via_item_select() throws InterruptedException {
		String[] product_detail = chekoutPage.selectProductByName();
		for (String detail : product_detail) {
			try {
				chekoutPage.scroll(detail);
				Assert.assertTrue(chekoutPage.isElementDisplayed(detail));
			} catch (Exception e) {
				chekoutPage.fail(e.getMessage());
			}

		}

		chekoutPage.scroll("ADD TO CART");

		chekoutPage.addToKart();
	}

	@Test(enabled = true)
	public void checkout_item_via_home() throws InterruptedException {
		Assert.assertTrue(loginpage.homepage().isDisplayed());
		String[] product_detail = chekoutPage.addTocartHome();
		Assert.assertTrue(chekoutPage.isElementDisplayed("REMOVE"));
		chekoutPage.tapOnCartSymbol();
		for (String detail : product_detail) {
			Thread.sleep(5000);
			chekoutPage.scroll(detail);
			Assert.assertTrue(chekoutPage.isElementDisplayed(detail));
		}
		Assert.assertTrue(chekoutPage.isElementDisplayed("REMOVE"));
		chekoutPage.scroll("CHECKOUT");
		chekoutPage.tapOnCheckout();
		Assert.assertTrue(chekoutPage.isElementDisplayed("CHECKOUT: INFORMATION"));
		chekoutPage.fillAddress();
		chekoutPage.completeTheOrder();
		Assert.assertTrue(chekoutPage.isElementDisplayed("CHECKOUT: COMPLETE"));

	}

	@Test(enabled = true)
	public void remove_item_from_home_which_is_added_to_cart() throws InterruptedException {
		try {
			Assert.assertTrue(loginpage.homepage().isDisplayed());
			String[] product_detail = chekoutPage.addTocartHome();
			Assert.assertTrue(chekoutPage.isElementDisplayed("REMOVE"));
			chekoutPage.tapOnRemove();
			if (chekoutPage.isElementDisplayed("REMOVE")) {
				throw new Exception();
			}
			Assert.assertTrue(chekoutPage.isElementPresent("1").size() == 0);
		} catch (Exception e) {
			chekoutPage.fail("Remove option still present after clicked on remove");
		}

	}

	@Test(enabled = true)
	public void remove_item_from_cart() throws InterruptedException {
		try {
			chekoutPage.addTocartHome();
			chekoutPage.tapOnCartSymbol();
			chekoutPage.scroll("REMOVE");
			chekoutPage.tapOnRemove();
			Thread.sleep(5000);
			if (chekoutPage.isElementPresent("1").size() != 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			chekoutPage.fail("Remove option still present after clicked on remove");
		}

	}

	@Test(enabled = true)
	public void checkout_after_removing_item_from_cart() throws InterruptedException {
		try {
			chekoutPage.addTocartHome();
			chekoutPage.tapOnCartSymbol();
			chekoutPage.scroll("REMOVE");
			chekoutPage.tapOnRemove();
			chekoutPage.tapOnCheckout();
			if (chekoutPage.isElementDisplayed("CHECKOUT: INFORMATION")) {
				throw new Exception();
			}
		} catch (Exception e) {
			chekoutPage.fail("Checkout info coming even if item not in cart");
		}

	}

	@Test(enabled = true)
	public void add_multiple_itmes_to_cart() throws InterruptedException {
		try {
			chekoutPage.addItemsToCart();
			if (chekoutPage.isElementDisplayed("ADD TO CART")) {
				throw new Exception();
			}
		} catch (Exception e) {
			chekoutPage.fail("Unable to add multiple items to cart");
		}

	}

	@Test(enabled = true)
	public void add_random_item_to_cart() throws InterruptedException {
		try {
			
			chekoutPage.scroll(10);
			chekoutPage.addTocartHome();

			if (!chekoutPage.isElementDisplayed("REMOVE")) {
				throw new Exception();
			}
		} catch (Exception e) {
			chekoutPage.fail("Unable to add items to cart");
		}

	}
	
	@Test(enabled = true)
	public void adding_item_to_cart_via_price_select() throws InterruptedException {
		try {
			
			chekoutPage.slectProductByPrice();
			
			Thread.sleep(5000);
			
			chekoutPage.scroll("ADD TO CART");

			chekoutPage.addToKart();

			if (!chekoutPage.isElementDisplayed("REMOVE")) {
				throw new Exception();
			}
		} catch (Exception e) {
			chekoutPage.fail("Unable to add items to cart from product page");
		}

	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() throws Exception {
		driver.quit();
	}
}
