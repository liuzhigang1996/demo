package app.cib.util;

import java.text.NumberFormat;
import java.util.Locale;

import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;

public class TransAmountFormate {
	public static String re = "[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*";;//for pt amount
	 /**
	 * ����ʽ��������ҳ�����͹����������漰�������ݶ�������ô˷������и�ʽ����
	 * @param amount ���ĵĽ���ʽ"1.000,00", ��Ӣ�ĵĽ���ʽ"1,000.00"
	 * @param lang ��������
	 * @return
	 * @throws NTBException
	 */
	public static double formateAmount(String amount, Locale lang) throws NTBException{	
		//���: ����Ϊ��
		if(amount==null||"".equals(amount)){			
			return 0;			
		}
		double d = 0;		
		Locale myLocale = new Locale("en", "US"); // default locale �˴�����BOLд��
		
		if(Constants.PT.equals(lang.toString())){
			myLocale = new Locale("pt", "PT"); // �˴�����BOLд��			
			//���: ����ʽ���ܴ���ţ���ֻ����һ������
			if(amount.indexOf(".")>=0||amount.split(",").length>2){
				throw new NTBException("err.txn.InvalidAmount");
			}			
		}else{
			if(Constants.CN.equals(lang.toString())){
				myLocale = new Locale("zh", "CN"); // �˴�����BOLд��
		    }else if(Constants.US.equals(lang.toString())){
			    myLocale = new Locale("en", "US"); 
			}else if(Constants.HK.equals(lang.toString())){
				myLocale = new Locale("zh", "HK");
			}			
           //���: ������������ʽ���ܴ����ţ���ֻ����һ�����
			if(/*ע�ʹ˶δ���20150619���������amount.indexOf(",")>=0||*/amount.split("\\.").length>2){
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
