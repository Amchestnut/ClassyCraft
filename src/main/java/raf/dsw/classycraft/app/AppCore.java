package raf.dsw.classycraft.app;

import raf.dsw.classycraft.app.core.ApplicationFramework;

public class AppCore {
    public static void main(String[] args) {
        ApplicationFramework appCore = ApplicationFramework.getInstance();
        appCore.initialize();
    }
}