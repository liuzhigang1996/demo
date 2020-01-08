package app.cib.util.otp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

import sun.util.logging.resources.logging;

import com.neturbo.set.core.Log;
import com.neturbo.set.utils.NTBProperties;
import com.neturbo.set.utils.RBFactory;

/**
 * 
 * @author long_zg 2013-09-23
 *
 */
public class PropertyContent {

    private Properties propertyContent=null;
    
    private static HashMap instances = new HashMap();
    
    /**
     *    私有的构造子保证外界无法直接将此类实例化
     */
    private PropertyContent(String fileName, String lang) {

        try {
        	loadProperty(fileName, lang);
            instances.put(makeKey(fileName,lang), this);
        } catch (Exception e) {
            Log.error("Resource not exist (" + fileName + ")", e);
        }
    }
    
    synchronized public static PropertyContent getInstance(String path,String lang){
    	if (instances.containsKey(makeKey(path, lang))) {
    		Log.info("PropertyContent getInstance instances makeKey=" + makeKey(path, lang)) ;
            return (PropertyContent) instances.get(makeKey(path, lang));
        } else {
        	Log.info("PropertyContent getInstance new PropertyContent") ;
            return new PropertyContent(path, lang);
        }
   }

	// load
	private void loadProperty(String fileName,String lang){

//		File file;
//		InputStream is = null;
		String newFileName = fileName.replace(".", "/") ;
		
		newFileName = makeKey(newFileName,lang) ;
		
		newFileName = "/" + newFileName + ".properties" ;
		
		Log.info("PropertyContent loadProperty newFileName:" + newFileName) ;
		
		InputStream is = this.getClass().getResourceAsStream(newFileName);
		
		
		
		try {
			
			Log.info("PropertyContent loadProperty is:" + is) ;
			
//			file = new File(newFileName);
//			is = new FileInputStream(file);
			propertyContent = new Properties();
			propertyContent.load(is);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
   
   
	//read
	public String getProperty(String key){
		String valueString="";
		try {
			valueString = propertyContent.getProperty(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valueString;
	}

	//构造搜索关键字
    private static String makeKey(String fileName, String lang) {
    	
    	Log.info("PropertyContent makeKey fileName:" + fileName + " | lang=" + lang) ;
    	// 如果其他语言
    	String smsFileName = fileName ;
    	
		if("E".equals(lang)){
			 smsFileName = fileName + "_eng";
		} else if ("C".equals(lang)) {
			smsFileName +="_chi" ;
		} else if ("P".equals(lang)){
			smsFileName += "_put" ;
		}
		
		Log.info("PropertyContent makeKey return smsFileName:" + smsFileName) ;
		
        return smsFileName;
    }
}
