package ru.kpfu.itis.gr201.ponomarev;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;

public class Main {

    private static final int PORT = port();
    private static final String APP_BASE = ".";
    private static final String CONTEXT_PATH = "";

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.getConnector();
        tomcat.setBaseDir(tempDirectory());
        tomcat.setPort(PORT);
        tomcat.getHost().setAppBase(APP_BASE);
        tomcat.addWebapp(CONTEXT_PATH, APP_BASE);
        tomcat.start();
        tomcat.getServer().await();
    }

    private static int port() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 8080;
    }

    private static String tempDirectory() {
        try {
            File tmpDir = File.createTempFile("tomcat", "." + PORT);
            tmpDir.delete();
            tmpDir.mkdir();
            tmpDir.deleteOnExit();
            return tmpDir.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
