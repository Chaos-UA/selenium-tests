package com.test.selenium.webdriver.chrome;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.test.selenium.webdriver.common.Settings;
import com.test.selenium.webdriver.common.WebDriver;
import com.test.selenium.webdriver.common.WebDriverFactory;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class ChromiumWebDriverFactory extends WebDriverFactory{
    private static final Proxy SELENIUM_PROXY = new Proxy();
    static {
        try {
            InetSocketAddress connectableAddressAndPort = new InetSocketAddress(InetAddress.getLocalHost(), getBrowserMobProxy().getPort());
            String proxyStr = String.format("%s:%d", connectableAddressAndPort.getHostString(), connectableAddressAndPort.getPort());
            SELENIUM_PROXY.setProxyType(Proxy.ProxyType.MANUAL);
            SELENIUM_PROXY.setHttpProxy(proxyStr);
            SELENIUM_PROXY.setSslProxy(proxyStr);
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public String getName() {
        return "chrome-driver";
    }

    @Override
    public WebDriver createWebDriver() {
        try {
            ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
                    .usingDriverExecutable(new File("/usr/lib/chromium-browser/chromedriver"))
                    .usingAnyFreePort()
                    .withEnvironment(settings.getEnvironment())
                    .build();
            ChromeOptions chromeOptions = new ChromeOptions();

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(CapabilityType.PROXY, SELENIUM_PROXY);

            ChromeDriver chromeDriver = new ChromeDriver(chromeDriverService, capabilities);
            chromeDriver.manage().window().setSize(new Dimension(settings.getWindowSize().width, settings.getWindowSize().height));

            return new RemoteDriverWrapper(chromeDriver, chromeDriverService, "Chromium");
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
