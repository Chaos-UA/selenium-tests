package com.test.selenium.webdriver.chrome;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.test.selenium.webdriver.common.WebDriver;
import com.test.selenium.webdriver.common.WebDriverFactory;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

public class ChromeWebDriverFactory extends WebDriverFactory {

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
        ChromeDriverService chromeDriverService = new BuilderProxyFix()
                .usingDriverExecutable(new File("/opt/google/chrome/chromedriver"))
                .usingAnyFreePort()
                .withEnvironment(settings.getEnvironment())
                .build();
        ChromeOptions chromeOptions = new ChromeOptions();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, SELENIUM_PROXY);

        ChromeDriver chromeDriver = new ChromeDriver(chromeDriverService, capabilities);
        chromeDriver.manage().window().setSize(new Dimension(settings.getWindowSize().width, settings.getWindowSize().height));
        return new RemoteDriverWrapper(chromeDriver, chromeDriverService, "Chrome");
    }

    public static class BuilderProxyFix extends ChromeDriverService.Builder {
        @Override
        protected ImmutableList<String> createArgs() {
            List<String> def =  super.createArgs();
            ImmutableList.Builder<String> argsBuilder = ImmutableList.builder();
            argsBuilder.addAll(def);
           // argsBuilder.add("--proxy-server=" + SELENIUM_PROXY.getHttpProxy());
            return argsBuilder.build();
        }
    }
}
