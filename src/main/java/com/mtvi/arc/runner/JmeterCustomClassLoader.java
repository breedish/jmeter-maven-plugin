package com.mtvi.arc.runner;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * JmeterCustomClassLoader.
 */
public class JmeterCustomClassLoader extends URLClassLoader {

    public JmeterCustomClassLoader(URL[] urls) {
        super(urls);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    /**
     *
     * @param urls - list of URLs to add to the thread's classloader
     */
    public static void updateLoader(URL [] urls) {
        JmeterCustomClassLoader loader = (JmeterCustomClassLoader) Thread.currentThread().getContextClassLoader();
        for(URL url : urls) {
            loader.addURL(url);
        }
    }

}
