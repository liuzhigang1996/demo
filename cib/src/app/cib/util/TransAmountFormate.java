package app.cib.util;

import java.text.NumberFormat;
import java.util.Locale;

import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;

public class TransAmountFormate {
	public static String re = "[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*";;//for pt amount
	 /**
	 * 金额格式化方法：页面上送过来的所有涉及金额的数据都必须调用此方法进行格式化。
	 * @param amount 葡文的金额格式"1.000,00", 中英文的金额格式"1,000.00"
	 * @param lang 语言种类
	 * @return
	 * @throws NTBException
	 */
	public static double formateAmount(String amount, Locale lang) throws NTBException{	
		//检查: 金额不能为空
		if(amount==null||"".equals(amount)){			
			return 0;			
		}
		double d = 0;		
		Locale myLocale = new Locale("en", "US"); // default locale 此处沿用BOL写法
		
		if(Constants.PT.equals(lang.toString())){
			myLocale = new Locale("pt", "PT"); // 此处沿用BOL写法			
			//检查: 金额格式不能带点号，且只能有一个逗号
			if(amount.indexOf(".")>=0||amount.split(",").length>2){
				throw new NTBException("err.txn.InvalidAmount");
			}			
		}else{
			if(Constants.CN.equals(lang.toString())){
				myLocale = new Locale("zh", "CN"); // 此处沿用BOL写法
		    }else if(Constants.US.equals(lang.toString())){
			    myLocale = new Locale("en", "US"); 
			}else if(Constants.HK.equals(lang.toString())){
				myLocale = new Locale("zh", "HK");
			}			
           //检查: 非葡萄牙金额格式不能带逗号，且只能有一个点号
			if(/*注释此段代码20150619允许带逗号amount.indexOf(",")>=0||*/amount.split("\\.").length>2){
				throw new NTBException("err.txn.InvalidAmount");
			}			
		}
		try {
			NumberFormat nf = NumberFormat.getInstance(myLocale);
			d = nf.parse(amount).doubleValue();			
		} catch (Exception e) {
			Log.error(e) ;
			throw new NTBException("err.txn.InvalidAmount");
		}
		return d;
	}
}
