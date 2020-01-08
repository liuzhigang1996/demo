//package app.cib.util.smsclient;
//
//import app.cib.util.smsclient.sms.SmsRequest;
//import app.cib.util.smsclient.sms.SmsResponse;
//import com.neturbo.set.core.Log;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//public class SMSClient
//  implements Serializable
//{
//  private static SMSClient myInstatnce;
//  private static List serverUrls;
//  private static int robinCounter;
//
//  private SMSClient()
//  {
//    if (serverUrls == null)
//      serverUrls = new ArrayList();
//  }
//
//  public static SMSClient getInstance()
//  {
//    if (myInstatnce == null) {
//      myInstatnce = new SMSClient();
//    }
//
//    return myInstatnce;
//  }
//
//  public static void setMainServer(String serverUrl)
//  {
//    Log.info("SMSClient setMainServer serverUrl : " + serverUrl);
//    if (serverUrls == null)
//      serverUrls = new ArrayList();
//    if (serverUrls.size() > 0)
//      serverUrls.set(0, serverUrl);
//    else
//      serverUrls.add(serverUrl);
//  }
//
//  public static void setSecondaryServer(String serverUrl)
//  {
//    Log.info("SMSClient setSecondaryServer serverUrl : " + serverUrl);
//
//    if (serverUrls == null)
//      serverUrls = new ArrayList();
//    if (2 == serverUrls.size()) {
//      serverUrls.remove(1);
//    }
//    serverUrls.add(1, serverUrl);
//  }
//
//  private synchronized String getServer()
//  {
//    robinCounter += 1;
//    robinCounter %= serverUrls.size();
//
//    Log.info("SMSClient getServer robinCounter = " + robinCounter);
//    String curServer = (String)serverUrls.get(robinCounter);
//    return curServer;
//  }
//
//  public  SmsResponse sendSms(SmsRequest req)
//    throws Exception
//  {
//    WsClient wsClient = getWsclient();
//
//    SmsResponse smsResponse = null;
//    try {
//      Log.info("SMSClient sendSms first service");
//      smsResponse = wsClient.sendSms(req);
//    } catch (Exception e) {
//      Log.info(e.getMessage());
//
//      wsClient = getWsclient();
//      try {
//        Log.info("SMSClient sendSms second service");
//        smsResponse = wsClient.sendSms(req);
//      } catch (Exception e1) {
//        Log.info(e1.getMessage());
//        throw e1;
//      }
//
//    }
//
//    return smsResponse;
//  }
//
//  public WsClient getWsclient()
//    throws Exception
//  {
//    String curServer = getServer();
//    WsClient wsClient = null;
//
//    Log.info("SMSClient getWsclient robinCounter = " + robinCounter);
//
//    if (robinCounter == 0) {
//      wsClient = WsClientImpl1.getInstance((String)serverUrls.get(0));
//      Log.info("SMSClient getWsclient WsClientImpl1.getInstance((String)serverUrls.get(0))");
//    } else if (robinCounter == 1) {
//      wsClient = WsClientImpl2.getInstance((String)serverUrls.get(1));
//      Log.info("SMSClient getWsclient WsClientImpl2.getInstance((String)serverUrls.get(1))");
//    }
//    return wsClient;
//  }
//}