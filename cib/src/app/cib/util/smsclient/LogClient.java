//package app.cib.util.smsclient;
//
//import app.cib.util.smsclient.log.LogRequest;
//import app.cib.util.smsclient.log.LogResponse;
//import app.cib.util.smsclient.log.LogSmsRequest;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.neturbo.set.core.Config;
//
//public class LogClient
//{
//  private static LogClient myInstatnce;
//  private static List serverUrls;
//  private static int robinCounter;
//  private static String smsWsdlUrl1 = Config.getProperty("app.sms.log.server.url_1");
//  private static String smsWsdlUrl2 = Config.getProperty("app.sms.log.server.url_2");
//
//  private LogClient(String serverUrl) {
//    if (serverUrls == null)
//      serverUrls = new ArrayList();
//    serverUrls.add(serverUrl);
//  }
//
//  public static LogClient getInstance() {
//    if (myInstatnce == null) {
//      myInstatnce = new LogClient(smsWsdlUrl1);
//
//      if ((smsWsdlUrl2 != null) && (!"".equals(smsWsdlUrl2))) {
//        setSecondaryServer(smsWsdlUrl2);
//      }
//    }
//
//    return myInstatnce;
//  }
//
//  public static void setSecondaryServer(String serverUrl)
//  {
//    if (serverUrls == null)
//      serverUrls = new ArrayList();
//    serverUrls.add(serverUrl);
//  }
//
//  private synchronized String getServer()
//  {
//    robinCounter += 1;
//    robinCounter %= serverUrls.size();
//    String curServer = (String)serverUrls.get(robinCounter);
//    return curServer;
//  }
//
//  public LogResponse logSms(LogSmsRequest logSmsRequest)
//  {
//    WsLogClient wsLogClient = getWsLogclient();
//
//    LogResponse logResponse = wsLogClient.logSms(logSmsRequest);
//
//    if (logResponse == null) {
//      wsLogClient = getWsLogclient();
//      logResponse = wsLogClient.logSms(logSmsRequest);
//    }
//
//    return logResponse;
//  }
//
//  public LogResponse logInfo(LogRequest logRequest)
//  {
//    WsLogClient wsLogClient = getWsLogclient();
//
//    LogResponse logResponse = wsLogClient.logInfo(logRequest);
//
//    if (logResponse == null) {
//      wsLogClient = getWsLogclient();
//      logResponse = wsLogClient.logInfo(logRequest);
//    }
//
//    return logResponse;
//  }
//
//  public WsLogClient getWsLogclient()
//  {
//    String curServer = getServer();
//    WsLogClient wsLogclient = null;
//
//    if (smsWsdlUrl1.equals(curServer))
//      wsLogclient = WsLogClientImpl1.getInstance();
//    else if (smsWsdlUrl2.equals(curServer)) {
//      wsLogclient = WsLogClientImpl2.getInstance();
//    }
//
//    return wsLogclient;
//  }
//}