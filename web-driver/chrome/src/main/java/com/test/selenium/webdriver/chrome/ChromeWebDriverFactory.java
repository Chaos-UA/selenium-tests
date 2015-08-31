package com.test.selenium.webdriver.chrome;

import com.test.selenium.webdriver.common.WebDriver;
import com.test.selenium.webdriver.common.WebDriverFactory;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class ChromeWebDriverFactory extends WebDriverFactory {

    @Override
    public String getName() {
        return "chrome-driver";
    }

    @Override
    public WebDriver createWebDriver() {
        ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File("/opt/google/chrome/chromedriver"))
                .usingAnyFreePort()
                .withEnvironment(settings.getEnvironment())
                .build();
        ChromeOptions chromeOptions = new ChromeOptions();
        ChromeDriver chromeDriver = new ChromeDriver(chromeDriverService);
        chromeDriver.manage().window().setSize(new Dimension(settings.getWindowSize().width, settings.getWindowSize().height));
        return new RemoteDriverWrapper(chromeDriver, chromeDriverService, "Chrome");
    }
}
