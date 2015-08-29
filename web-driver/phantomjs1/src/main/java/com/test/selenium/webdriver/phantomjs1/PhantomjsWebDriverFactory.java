package com.test.selenium.webdriver.phantomjs1;


import com.test.selenium.webdriver.common.Settings;
import com.test.selenium.webdriver.common.WebDriver;
import com.test.selenium.webdriver.common.WebDriverFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

public class PhantomjsWebDriverFactory extends WebDriverFactory {

    @Override
    public String getName() {
        return "chrome-driver";
    }

    @Override
    public WebDriver createWebDriver(Settings settings) {
        PhantomJSDriverService service = new PhantomJSDriverService.Builder()
                .usingAnyFreePort()
                .usingPhantomJSExecutable(new File("/home/volodymyr/Programs/phantomjs/bin/phantomjs"))
                .build();
        Capabilities capabilities = new DesiredCapabilities();
        PhantomJSDriver phantomJSDriver = new PhantomJSDriver(service, capabilities);
        return new RemoteDriverWrapper(phantomJSDriver);
    }
}