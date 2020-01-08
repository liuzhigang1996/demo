/**
 * 
 */
package app.cib.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * Jan 4, 2009
 */
public class IpValidator {
	
	/**
	 * 根据样式集（patterns）验证IP的合法性
	 * @author hjs
	 * @param ip：待验证的IP地址
	 * @param patterns：e.g [192.168.2.*（通配）, 192.168.1.1-192.168.1.255（范围）, ...]
	 * @return
	 */
	public static boolean validateByPatterns(String ip, String[] patterns) {
		for (int i = 0; i < patterns.length; i++) {
			String pattern = patterns[i].trim();
			if("".equals(pattern)){
				continue;
			}
			
			if(validateByPattern(ip, pattern)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean validateByPattern(String ip, String pattern) {
		boolean matched = false;
		if(pattern.indexOf("-") > -1){ // 根据范围匹配
			String[] s = pattern.split("\\-");
			matched = validateByScope(ip, s[0].trim(), s[1].trim());
		} else {
			matched = validateByRegulation(ip, pattern.trim());
		}
		
		return matched;
	}
	
	private static boolean validateByRegulation(String ip, String pattern){
		String regEx = pattern;
		
		regEx = Utils.replaceStr(regEx, ".", "\\.");
		regEx = Utils.replaceStr(regEx, "*", "\\d{1,3}");
//		Log.debug("regEx=" + regEx);
		
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(ip);
		return m.find();
	}
	
	private static boolean validateByScope(String ip, String ip1, String ip2){
		long ipVal = calculateIpValue(ip);
		long ip1Val = calculateIpValue(ip1);
		long ip2Val = calculateIpValue(ip2);
		
		return ipVal >= ip1Val && ipVal <= ip2Val;
	}

	public static long calculateIpValue(String ip){
		long val = 0;
		
		String[] s = ip.trim().split("\\.");
		for(int i=0; i<s.length; i++){
			val += Double.parseDouble(s[i]) * Math.pow(255, s.length-i-1);
		}
		
		return val;
	}

}
