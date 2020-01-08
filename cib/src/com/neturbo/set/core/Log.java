package com.neturbo.set.core;

import java.sql.*;
import java.util.*;
import com.neturbo.set.utils.*;
import org.apache.log4j.*;
import org.apache.log4j.net.*;

public class Log {

    private List outputList;
    private String level;
    private static Logger logger = Logger.getRootLogger();

    public static void setLogger(Class loggerClass){
        logger = Logger.getLogger(loggerClass);
    }

    public static void fatal(Object Msg) {
        logger.fatal(Msg);
    }

    public static void error(Object Msg) {
        logger.error(Msg);
    }

    public static void warn(Object Msg) {
        logger.warn(Msg);
    }

    public static void info(Object Msg) {
        logger.info(Msg);
    }

    public static void debug(Object Msg) {
        logger.debug(Msg);
    }

    public static void fatal(Object Msg, Throwable t) {
        logger.fatal(Msg, t);
    }

    public static void error(Object Msg, Throwable t) {
        logger.error(Msg, t);
    }

    public static void warn(Object Msg, Throwable t) {
        logger.warn(Msg, t);
    }

    public static void info(Object Msg, Throwable t) {
        logger.info(Msg, t);
    }

    public static void debug(Object Msg, Throwable t) {
        logger.debug(Msg, t);
    }
}
