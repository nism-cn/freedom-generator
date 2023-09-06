package org.nism.fg.base.utils;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderUtil {
    public static ClassLoader getClassLoader(String url) {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            URLClassLoader classLoader = new URLClassLoader(new URL[]{}, ClassLoader.getSystemClassLoader());
            method.invoke(classLoader, new URL(url));
            return classLoader;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
