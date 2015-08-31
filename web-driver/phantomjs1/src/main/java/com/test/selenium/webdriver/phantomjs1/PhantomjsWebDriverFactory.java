package com.test.selenium.webdriver.phantomjs1;


import com.test.selenium.webdriver.common.Settings;
import com.test.selenium.webdriver.common.WebDriver;
import com.test.selenium.webdriver.common.WebDriverFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

public class PhantomjsWebDriverFactory extends WebDriverFactory {

    @Override
    public String getName() {
        return "phantomjs-driver";
    }

    @Override
    public WebDriver createWebDriver() {
        PhantomJSDriverService service = new PhantomJSDriverService.Builder()
                .usingAnyFreePort()
                .usingPhantomJSExecutable(new File("/home/volodymyr/Programs/phantomjs/bin/phantomjs"))
                .withEnvironment(settings.getEnvironment())
                .build();
        Capabilities capabilities = new DesiredCapabilities();
        PhantomJSDriver phantomJSDriver = new PhantomJSDriver(service, capabilities);
        phantomJSDriver.manage().window().setSize(new Dimension(settings.getWindowSize().width, settings.getWindowSize().height));
        return new RemoteDriverWrapper(phantomJSDriver, service);
    }
}
