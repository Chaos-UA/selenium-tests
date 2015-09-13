package com.test.selenium.webdriver.phantomjs1;


import com.test.selenium.webdriver.common.Settings;
import com.test.selenium.webdriver.common.WebDriver;
import com.test.selenium.webdriver.common.WebDriverFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class PhantomjsWebDriverFactory extends WebDriverFactory {

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
        return "phantomjs-driver";
    }

    @Override
    public WebDriver createWebDriver() {
        PhantomJSDriverService service = new PhantomJSDriverService.Builder()
                .usingAnyFreePort()
                .usingCommandLineArguments(new String[]{
                        //String.format("--proxy=%s", SELENIUM_PROXY.getHttpProxy()),
                        //"--proxy-type=HTTP"
                })
                .usingPhantomJSExecutable(new File("/home/volodymyr/Programs/phantomjs/bin/phantomjs"))
                .withEnvironment(settings.getEnvironment())
                .build();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        for (String header : settings.getHeaders().keySet()) {
            capabilities.setCapability("phantomjs.page.customHeaders." + header, settings.getHeaders().get(header));
        }

        //capabilities.setCapability(CapabilityType.PROXY, SELENIUM_PROXY);

        PhantomJSDriver phantomJSDriver = new PhantomJSDriver(service, capabilities);
        phantomJSDriver.manage().window().setSize(new Dimension(settings.getWindowSize().width, settings.getWindowSize().height));
        return new RemoteDriverWrapper(phantomJSDriver, service);
    }
}
