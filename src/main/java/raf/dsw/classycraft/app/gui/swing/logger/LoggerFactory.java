package raf.dsw.classycraft.app.gui.swing.logger;

import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.observer.ISubscriber;

public class LoggerFactory {
    public LoggerFactory() {
    }
    public static ISubscriber createLogger(String typeOfLogger){
        if(typeOfLogger.equalsIgnoreCase("CONSOLE_LOGGER")){
            return new ConsoleLogger();
        }
        if (typeOfLogger.equalsIgnoreCase("FILE_LOGGER")){
            return new FileLogger();
        }
        if (typeOfLogger.equalsIgnoreCase("MAINFRAME")){
            return MainFrame.getInstance();
        }
        return null;
    }
}
