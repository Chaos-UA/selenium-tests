package com.test.selenium.webdriver.manager;


import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

public class JarUtil {


    public static List<URL> getJarsFromPath(Class clazz, String path) throws URISyntaxException, IOException {
        URL dirURL = clazz.getClassLoader().getResource(path);
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
        /* A file path: easy enough */
            File[] jars = new File(dirURL.toURI()).listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().toLowerCase().endsWith(".jar");
                }
            });
            List<URL> jarFiles = new ArrayList<>();
            for (File f : jars) {
                jarFiles.add(f.toURI().toURL());
            }
            return jarFiles;
        }

        if (dirURL == null) {
        /*
         * In case of a jar file, we can't actually find a directory.
         * Have to assume the same jar as clazz.
         */
            String me = clazz.getName().replace(".", "/")+".class";
            dirURL = clazz.getClassLoader().getResource(me);
        }
        if (dirURL.getProtocol().equals("jar")) {
        /* A JAR path */
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            List<URL> result = new ArrayList<>(); //avoid duplicates in case it is a subdirectory
            while(entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName();
                if (name.startsWith(path) && name.toLowerCase().endsWith(".jar")) { //filter according to the path\
                    String jarFile = name.replaceAll("\\.", "/");
                    jarFile = jarFile.substring(0, jarFile.length() - "/jar".length()) + ".jar";
                    jarFile = new File(jarFile).getName();
                    URL url = new URL(dirURL + "/" + jarFile);
                    JOptionPane.showConfirmDialog(null, url);
                    result.add(url);
                }
            }
            return result;
        }

        throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
    }
}
