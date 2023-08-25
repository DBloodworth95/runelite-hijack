package com.runelitehijack.reflection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AllPermission;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.util.HashMap;

public class CustomClassLoader extends ClassLoader {
    public final HashMap<String, InputStream> resources = new HashMap<>();

    public CustomClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }

    public Class<?> defineClass(String name, byte[] bytes) {
        Permissions perms = new Permissions();
        perms.add(new AllPermission());
        ProtectionDomain protDomain = new ProtectionDomain(
                getClass().getProtectionDomain().getCodeSource(), perms,
                this,
                getClass().getProtectionDomain().getPrincipals()
        );

        try {
            return defineClass(name, bytes, 0, bytes.length, protDomain);
        } catch (LinkageError ex) {
            return null;
        }
    }

    public static byte[] readAllBytes(InputStream is) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096]; // Common buffer size
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            return os.toByteArray();
        }
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (resources.containsKey(name)) {
            return resources.get(name);
        }
        return super.getResourceAsStream(name);
    }
}
