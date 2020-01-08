package com.neturbo.set.utils;

import java.util.*;
import com.neturbo.set.core.*;

public class SubmitToken {

  //gen ResourceBundle
  public static HashMap tokens = new HashMap(20);
  private SubmitToken() {
  }

  public synchronized static String getToken() {
	//clear the Token
	 clearToken();
	//generate single token
    String timeStampStr = String.valueOf(System.currentTimeMillis());
    Random rand = new Random();
    String rndValue1 = String.valueOf(rand.nextInt(999999));
    String rndValue2 = String.valueOf(rand.nextInt(999999));
    String rndValue3 = String.valueOf(rand.nextInt(999999));
    String token = timeStampStr + "-" + rndValue1 + "-" + rndValue2 + "-" +  rndValue3;
    tokens.put(token,DateTime.formatDate(new Date(),"yyMMddhhmm"));
    return token;
  }

  //checker and def duplicate data
  public synchronized static boolean checkToken(String token) {
    String retVal = (String)tokens.remove(token);
    if(retVal!=null/* && retVal.equals("1")*/){
      return true;
    }
    return false;
  }

  public static void clearToken() {
		long nowTime = Long.parseLong(DateTime.formatDate(new Date(), "yyMMddhhmm"));
		Iterator<Map.Entry<String, String>> it = tokens.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if (nowTime - Long.parseLong(entry.getValue()) > 100) {
				it.remove();
			}
		}
	}

}
