package pl.jacadev.jce.agent.utils;

import pl.jacadev.jce.agent.Agent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

/**
 * Created by oem on 15.08.14.
 */
public class SystemClassLoader {

    public static final URLClassLoader SYSTEM_CLASS_LOADER = (URLClassLoader) ClassLoader.getSystemClassLoader();
    public static final Class<URLClassLoader> SYSTEM_CL_CLASS = URLClassLoader.class;

    public static void addUrl(URL url){
        try {
            Method method = SYSTEM_CL_CLASS.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(SYSTEM_CLASS_LOADER, url);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Agent.showError(e.toString());
        }
    }

    public static void defineClass(String name, byte[] code){
        try {
            Method method = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            method.setAccessible(true);
            method.invoke(SYSTEM_CLASS_LOADER, code, 0, code.length - 1);
        } catch (ReflectiveOperationException e) {
            Agent.showError(e.toString());
        }
    }
}
