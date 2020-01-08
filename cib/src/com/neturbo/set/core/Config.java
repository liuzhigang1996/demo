package com.neturbo.set.core;

import java.io.*;
import java.util.*;

import com.neturbo.base.action.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.xml.*;
import org.apache.log4j.*;
import org.springframework.context.*;
import org.springframework.context.support.*;
import app.cib.util.CachedDBRCFactory;

public class Config {
    private static HashMap properties = new HashMap();

    private static boolean initialized = false;

    private static String userDir;

    private static String appRoot;

    private static String log4jConfigPath = null;

    private static String appContextPaths = null;

    private static ActionMappings actionMappings = null;
    private static ApplicationContext appContext = null;
    private static ActionForwards globalForwards = null;


    private static XMLElement menuXML = null;

    static {
        init();
    }

    private Config() {
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void init() {
        if (initialized) {
            return;
        }
        try {
            System.out.println("Neturbo : Start initializing neturbo");
            RBFactory rbList = RBFactory.getInstance("config");
            HashMap allString = rbList.getAllString();
            System.out.println("Neturbo : initializing neturbo basic configurations.");
            if (!initConfig(allString)) {
                return;
            }
            System.out.println("Neturbo : initializing neturbo DB resource configurations.");
            if (!initDbResources()) {
                return;
            }
            System.out.println("Neturbo : initializing neturbo menu configurations.");
            if (!initMenu()) {
                return;
            }
            System.out.println("Neturbo : initializing neturbo log configurations.");
            if (!initLog()) {
                return;
            }
            System.out.println("Neturbo : initialized neturbo configurations");
            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String replaceSystemString(String str) {
        if (userDir != null) {
            str = Utils.replaceStr(str, "${user.dir}", userDir);
        }
        if (appRoot == null){
            appRoot = ActionServlet.getAppRoot();
        }
        if (appRoot != null) {
            str = Utils.replaceStr(str, "${app.root}", appRoot);
        }
        return str;
    }

    private static String getValue(HashMap rbStrings, String key,
                                   String defaultValue) {
        String tempStr = (String) rbStrings.get(key);
        if (tempStr == null) {
            tempStr = defaultValue;
        }
        return tempStr;
    }

    private static String getValue(HashMap rbStrings, String key) {
        return (String) rbStrings.get(key);
    }

    private static boolean initConfig(HashMap rbStrings) {
        // ������������
        properties.putAll(rbStrings);
        log4jConfigPath = getProperty("log4jConfig");
        appContextPaths = getProperty("AppContext");
        userDir = System.getProperty("user.dir");
        return true;
    }

    private static boolean initLog() {
        InputStream inputStream;
        String propsFileName = log4jConfigPath;
        try {
            if (propsFileName.indexOf("${app.classpath}") > -1) {
                propsFileName = Utils.replaceStr(propsFileName,
                                                 "${app.classpath}", "");
                inputStream = Config.class.getResourceAsStream(propsFileName);
            } else {
                propsFileName = replaceSystemString(propsFileName);
                inputStream = new FileInputStream(propsFileName);
            }
            Properties props = new Properties();
            props.load(inputStream);
            PropertyConfigurator.configure(props);
        } catch (Exception e) {
            System.err.println("Error Log4j configuration");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean initMenu() {
        menuXML = getConfigXML("MenuConfig");
        return true;
    }

    private static boolean initDbResources() {
        String className = getValue(properties, "DbResourcesClass");
        try {
            Class clazz = Class.forName(className);
            CachedDBRCFactory factory = (CachedDBRCFactory) clazz.newInstance();
            factory.init();
        } catch (Exception e) {
            System.err.println("Error initializing DB resources of (" + className + ")");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getProperty(String name) {
        return replaceSystemString(getValue(properties, name));
    }

    public static void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    public static XMLElement getConfigXML(String name) {
        String key = "#{(xml)" + name + "}";
        XMLElement rootElement = (XMLElement) properties.get(key);
        if (rootElement != null) {
            return rootElement;
        }
        String configXMLFileName = getValue(properties, name);
        if (configXMLFileName == null) {
            return null;
        }
        try {
            XMLParser xmlParser = XMLFactory.getParser();
            if (configXMLFileName.indexOf("${app.classpath}") > -1) {
                configXMLFileName = Utils.replaceStr(configXMLFileName,
                        "${app.classpath}", "");
                xmlParser.setInput(Config.class.getResourceAsStream(
                        configXMLFileName));
            } else {
                configXMLFileName = replaceSystemString(configXMLFileName);
                File tempFile = new File(configXMLFileName);
                if (!tempFile.exists()) {
                    return null;
                }
                xmlParser.setInput(configXMLFileName);
            }
            xmlParser.unMarshal();
            rootElement = xmlParser.getRootElement();
            properties.put(key, rootElement);
        } catch (Exception e) {
            Log.error("Parse Config XML(" + configXMLFileName + ") Error", e);
        }
        return rootElement;
    }

    public static ApplicationContext getAppContext() {
        if (appContext != null) {
            return appContext;
        }
        String appContextPaths = getValue(properties, "ServiceContext");
        if (appContextPaths == null) {
            return null;
        }
        if (appContextPaths.indexOf("${app.classpath}") > -1) {
            appContextPaths = Utils.replaceStr(appContextPaths,
                                               "${app.classpath}", "");
            String[] appContextArray = Utils.splitStr(appContextPaths, ";");
            appContext = new ClassPathXmlApplicationContext(appContextArray);
            return appContext;
        } else {
            appContextPaths = replaceSystemString(appContextPaths);
            String[] appContextArray = Utils.splitStr(appContextPaths, ";");
            appContext = new FileSystemXmlApplicationContext(appContextArray);
            return appContext;
        }

    }

    public static void setActionMappings(ActionMappings actionMappings1) {
        actionMappings = actionMappings1;
    }

    public static void setGlobalForwards(ActionForwards globalForwards1) {
        globalForwards = globalForwards1;
    }

    public static void setAppRoot(String appRoot1) {
        appRoot = appRoot1;
    }

    public static String getAppRoot() {
        return appRoot;
    }

    public static ActionMappings getActionMappings() {
        return actionMappings;
    }

    public static ActionForwards getGlobalForwards() {
        return globalForwards;
    }

    public static Locale getDefaultLocale() {
        String localStr = getValue(properties, "DefaultLocale", "zh_CN");
        return new Locale(
                localStr.substring(0, 2),
                localStr.substring(3, 5));

    }

    public static XMLElement getMenuXML() {
        if (menuXML == null) {
            initMenu();
        }
        return menuXML;
    }
    
    public static InputStream getConfigStream(String configFileName) {
        try {
            if (configFileName.indexOf("${app.classpath}") > -1) {
            	configFileName = Utils.replaceStr(configFileName,
                        "${app.classpath}", "");
                return Config.class.getResourceAsStream(
                		configFileName);
            } else {
            	configFileName = replaceSystemString(configFileName);
                File tempFile = new File(configFileName);
                if (!tempFile.exists()) {
                    return null;
                }
                return new FileInputStream(tempFile);
            }

        } catch (Exception e) {
            Log.error("Error getting stream from (" + configFileName + ")", e);
        }
        return null;
    }


}
