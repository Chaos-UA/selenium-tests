package com.test.selenium.webdriver.firefox;

import com.test.selenium.webdriver.common.Settings;
import com.test.selenium.webdriver.common.WebDriver;
import com.test.selenium.webdriver.common.WebDriverFactory;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;


public class FireFoxWebDriverFactory extends WebDriverFactory {

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
        return "firefox-driver";
    }

    @Override
    public WebDriver createWebDriver() {
        try {
            FirefoxProfile firefoxProfile = new FirefoxProfile();
            FirefoxBinary firefoxBinary = new FirefoxBinary(new File("/usr/bin/firefox"));
            for (String key : settings.getEnvironment().keySet()) {
                firefoxBinary.setEnvironmentProperty(key, settings.getEnvironment().get(key));
            }

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(CapabilityType.PROXY, SELENIUM_PROXY);

            FirefoxDriver firefoxDriver = new FirefoxDriver(firefoxBinary, firefoxProfile, capabilities);
            firefoxDriver.manage().window().setSize(new Dimension(settings.getWindowSize().width, settings.getWindowSize().height));

            return new RemoteDriverWrapper(firefoxDriver, firefoxBinary);
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
