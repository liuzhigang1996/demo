package app.cib.util;

import java.util.*;

public class StatementToken {

	private static HashMap tokens = new HashMap(20);
	//add by hjs 20070131
	private static Random rand = new Random();

	private StatementToken() {
	}

	public synchronized static String getToken(String path) {
		long sysTime = System.currentTimeMillis();
		String timeStampStr = String.valueOf(sysTime);
		String rndValue1 = String.valueOf(rand.nextInt(999999));
		String rndValue2 = String.valueOf(rand.nextInt(999999));
		String rndValue3 = String.valueOf(rand.nextInt(999999));
		String token = timeStampStr + "-" + rndValue1 + "-" + rndValue2 + "-"
				+ rndValue3;
		if (tokens.size() > 500) {
			Iterator iterator = tokens.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				long timeStamp = Long.parseLong(key.split("-")[0]);
				if (sysTime - timeStamp > 1800000) {
					iterator.remove();
				}
			}
		}
		tokens.put(token, path);
		return token;
	}

	public synchronized static String checkToken(String token) {
//		 String retVal = (String)tokens.remove(token); 
		String retVal = (String) tokens.get(token);
		if (retVal != null) {
			return retVal;
		}
		return null;
	}

}
