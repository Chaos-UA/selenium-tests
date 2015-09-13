package com.test.selenium.webdriver.common;


import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;

import java.awt.*;

public abstract class WebDriverFactory {

    private static final BrowserMobProxy BROWSER_MOB_PROXY = new BrowserMobProxyServer();
    protected static final Settings settings = new Settings();

    static {
        settings.setWindowSize(new Dimension(1336, 768));
        BROWSER_MOB_PROXY.addHeaders(settings.getHeaders());
        BROWSER_MOB_PROXY.start(0);
    }

    public static BrowserMobProxy getBrowserMobProxy() {
        return BROWSER_MOB_PROXY;
    }

    public WebDriverFactory() {
        try (WebDriver webDriver = createWebDriver()) {
            webDriver.get("http://www.xhaus.com/headers");
            java.util.List<WebElement> webElements = webDriver.findElementsByXpath("/html/body/div/table/tbody/tr");
            for (WebElement webElement : webElements) {
                System.out.println(webElement.getText());
            }
            if (!webElements.stream().filter(v->v.getText().equals("Cache-Control no-cache")).findFirst().isPresent()) {
                throw new RuntimeException("Header is not set: 'Cache-Control: no-cache'");
            }
        }
    }

    public abstract String getName();

    public abstract WebDriver createWebDriver();
}
