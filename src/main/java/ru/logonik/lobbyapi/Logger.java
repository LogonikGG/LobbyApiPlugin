package ru.logonik.lobbyapi;

import java.util.logging.Level;

public class Logger {

    private static java.util.logging.Logger logger;

    public static void error(String s, Throwable ex) {
        logger.log(Level.SEVERE, s, ex);
    }

    public static void warn(String s) {
        logger.warning(s);
    }

    public static void info(String s) {
        logger.info(s);
    }

    protected static void setLogger(java.util.logging.Logger logger) {
        Logger.logger = logger;
    }

}
