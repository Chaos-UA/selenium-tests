package com.test.selenium.webdriver.manager;


import com.test.selenium.webdriver.common.LogUtil;
import com.test.selenium.webdriver.common.WebDriverFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class WebDriverManager {
    private static final WebDriverFactory[] WEB_DRIVER_FACTORIES;

    static {
        // load web driver factories from jar files.
        // Need to separate them because of phantomjs driver, which has lower version than
        // chrome and firefox drivers and has to be separated to work all together (common classes is not compatible).
        try {
            // TODO: HARDCODED
            File[] jars = new File("/home/volodymyr/projects/unkur-research/selenium/web-driver/web-driver-manager/target/web-drivers").listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().toLowerCase().endsWith(".jar");
                }
            });
            List<URL> jarUrls = new ArrayList<>();
            for (File f : jars) {
                jarUrls.add(f.toURI().toURL());
            }

            List<WebDriverFactory> webDriverFactories = new ArrayList<>();
            for (URL url : jarUrls) {
                URLClassLoader urlClassLoader = new URLClassLoader(
                        new URL[]{url},
                        null
                );
                Manifest manifest = new Manifest(urlClassLoader.getResourceAsStream("META-INF/MANIFEST.MF"));
                urlClassLoader.close();
                String[] factoryClasses = manifest.getMainAttributes().getValue("webDriverFactoryClasses").split(",", -1);
                LogUtil.getLogger().info("Initializing web driver factories: " + Arrays.asList(factoryClasses));
                urlClassLoader = new URLClassLoader(
                        new URL[]{url},
                        WebDriverManager.class.getClassLoader()
                );
                for (String factoryClass : factoryClasses) {
                    LogUtil.getLogger().info("Initializing web driver factory: " + factoryClass);
                    Class<WebDriverFactory> factoryClazz = (Class<WebDriverFactory>) urlClassLoader.loadClass(factoryClass);
                    webDriverFactories.add(factoryClazz.newInstance());
                }
            }
            LogUtil.getLogger().info("Web driver factories has initialized");
            //Collections.shuffle(webDriverFactories);
            WEB_DRIVER_FACTORIES = webDriverFactories.toArray(new WebDriverFactory[0]);
        }
        catch (Throwable t) {
            LogUtil.getLogger().debug("Initialization error", t);
            throw new RuntimeException("Initialization error", t);
        }
    }

    public static WebDriverFactory[] getWebDriverFactories() {
        return WEB_DRIVER_FACTORIES;
    }
}
