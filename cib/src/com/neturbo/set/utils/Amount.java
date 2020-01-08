/**
 * @author hjs
 * 2007-4-16
 */
package com.neturbo.set.utils;

import com.neturbo.set.core.Log;

/**
 * @author hjs
 * 2007-4-16
 */
public class Amount {
	
	public static boolean check(String amount, int length, int scale) {
		if(length<=0 || scale<0 || length<scale){
			return false;
		}
		
		amount = Utils.replaceStr(amount, ",", "");
		int intLength = length - scale;
		
		try{
			new Double(amount);
		} catch(Exception e) {
			Log.error("amount format error", e);
			return false;
		}
		
		int dotIndex = amount.indexOf(".");
		String integerPart = "";
		String floatPart = "";
		if(dotIndex == -1){
			integerPart = amount;
		} else {
			integerPart = amount.substring(0, dotIndex);
			floatPart = amount.substring(dotIndex+1);
		}

		if(floatPart.length() > scale){
			return false;
		}
		if(integerPart.length() > intLength){
			return false;
		}
		
		return true;
		
	}

}
