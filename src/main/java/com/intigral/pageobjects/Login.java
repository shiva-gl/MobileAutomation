package com.intigral.pageobjects;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import java.time.Duration;
import org.openqa.selenium.support.PageFactory;

import com.intigral.utils.CommonaPage;

import io.github.cdimascio.dotenv.Dotenv;

public class Login extends CommonaPage {
	private AndroidDriver<AndroidElement> driver;
	Dotenv dotenv;

	public Login(AndroidDriver<AndroidElement> driver) {
		super(driver);
		dotenv = Dotenv.load();
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	@AndroidFindBy(xpath = "//*[contains(@content-desc,'test-Username')]")
	private AndroidElement username;

	@AndroidFindBy(xpath = "//*[contains(@content-desc,'test-Password')]")
	private AndroidElement password;

	@AndroidFindBy(xpath = "//*[contains(@content-desc,'test-LOGIN')]")
	private AndroidElement loginButton;

	@AndroidFindBy(xpath = "//*[contains(@text,'PRODUCTS')]")
	private AndroidElement homepage;

	public void loginWithValidCred() {
		try {
			username.sendKeys(dotenv.get("app_user"));
			password.sendKeys(dotenv.get("app_password"));
			click(loginButton);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public MobileElement homepage() {
		return homepage;
	}
}
