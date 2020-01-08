package com.neturbo.set.utils;

import java.security.*;
import com.neturbo.set.core.*;

/**
 * 此处插入类型描述。
 * 创建日期：(2003-8-25 2:32:44)
 * @author：Administrator
 */
public class Encryption {
/**
 * Encryption 构造子注解。
 */
public Encryption() {
        super();
}
    public static final String digest(String inStr, String algorithm, String charset)
    {
        String randomStr = "some random value JKLMNOP_!#";
        String copyRightStr = "Copyright 2003 Nabai, Special thanks to my friends";
        String resultStr = null;
        try
        {
            MessageDigest messagedigest = MessageDigest.getInstance(algorithm);
            messagedigest.update(inStr.getBytes(charset));
            messagedigest.update(randomStr.getBytes(charset));
            messagedigest.update(copyRightStr.getBytes(charset));
            byte resultBytes[] = messagedigest.digest();
            resultStr = Utils.bytes2HexStr(resultBytes);
        }
        catch(NoSuchAlgorithmException e)
        {
            Log.error( "NoSuchAlgorithmException for " + algorithm, e);
            resultStr = null;
        }
        catch(Exception exception)
        {
            Log.error( "Exception in digest(String)", exception);
            resultStr = null;
        }
        return resultStr;
    }

    public static final String digest(String inStr, String algorithm){
        return digest(inStr, algorithm, "UTF-8");
    }

}
