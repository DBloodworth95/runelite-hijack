package com.runelitehijack;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import javax.swing.UIManager;

import com.runelitehijack.client.ClientHijack;

public class ClientHijackLauncher {

    public ClientHijackLauncher() {
        startHijackingThread();
    }

    private void startHijackingThread() {
        new Thread(() -> {
            ClassLoader classLoader = waitForRuneliteClassLoader();
            performHijack(classLoader);
        }).start();
    }

    private ClassLoader waitForRuneliteClassLoader() {
        while (true) {
            ClassLoader classLoader = (ClassLoader) UIManager.get("ClassLoader");
            if (classLoader != null && containsRunelitePackage(classLoader)) {
                return classLoader;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                ex.printStackTrace();
            }
        }
    }

    private boolean containsRunelitePackage(ClassLoader classLoader) {
        for (Package pack : classLoader.getDefinedPackages()) {
            if ("net.runelite.client.rs".equals(pack.getName())) {
                return true;
            }
        }
        return false;
    }

    private void performHijack(ClassLoader classLoader) {
        try {
            URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
            Method addUrl = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addUrl.setAccessible(true);

            URI uri = getHijackJarURI();
            addUrl.invoke(urlClassLoader, uri.toURL());

            Class<?> clazz = urlClassLoader.loadClass(ClientHijack.class.getName());
            clazz.getConstructor().newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private URI getHijackJarURI() throws Exception {
        URI uri = ClientHijackLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        if (uri.getPath().endsWith("classes/")) {
            uri = uri.resolve("..");
        }
        if (!uri.getPath().endsWith(".jar")) {
            uri = uri.resolve("DBloodworthHijacker.jar");
        }
        return uri;
    }

    public static void main(String[] args) {
        System.setProperty("runelite.launcher.reflect", "true");
        new ClientHijackLauncher();
        try {
            Class<?> clazz = Class.forName("net.runelite.launcher.Launcher");
            Method mainMethod = clazz.getMethod("main", String[].class);
            mainMethod.invoke(null, new Object[]{args});
        } catch (Exception ignored) {
        }
    }
}