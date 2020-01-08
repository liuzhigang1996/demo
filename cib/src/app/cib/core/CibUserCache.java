package app.cib.core;

import java.util.*;
import javax.naming.InitialContext;

import com.ibm.websphere.cache.DistributedMap;

import com.neturbo.set.core.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CibUserCache {
    public CibUserCache() {
    }

    private static Map corpUserCache = new HashMap();
    private static Map bankUserCache = new HashMap();

    static {
        String dynamicCacheForWASStr1 = Config.getProperty(
                "dynamicCacheForWAS_CorpUser");
        try {
            InitialContext ic = new InitialContext();
            DistributedMap dynamicCacheForWAS = (DistributedMap) ic.lookup(
                    dynamicCacheForWASStr1);
            if (dynamicCacheForWAS != null) {
//                dynamicCacheForWAS.setTimeToLive(3600);
                corpUserCache = dynamicCacheForWAS;
                
                // Jet added 2008-08-29
                corpUserCache.clear();

                Log.info("Use dynamic cache for IBM WAS cluster [" +
                         dynamicCacheForWASStr1 + "]");
            }
        } catch (Exception ex) {
            Log.error("Error ing dynamic cache for IBM WAS cluster [" +
                      dynamicCacheForWASStr1 + "]", ex);
        }

        String dynamicCacheForWASStr2 = Config.getProperty("dynamicCacheForWAS_BankUser");
        try {
            InitialContext ic = new InitialContext();
            DistributedMap dynamicCacheForWAS = (DistributedMap) ic.lookup(
                    dynamicCacheForWASStr2);
            if (dynamicCacheForWAS != null) {
                bankUserCache = dynamicCacheForWAS;
                
                // Jet added 2008-08-29
                bankUserCache.clear();

                Log.info("Use dynamic cache for IBM WAS cluster [" +
                         dynamicCacheForWASStr2 + "]");
            }
        } catch (Exception ex) {
            Log.error("Error loading dynamic cache for IBM WAS cluster [" +
                      dynamicCacheForWASStr2 + "]", ex);
        }
    }

    public static Map getCorpUserCache(){
        return corpUserCache;
    }

    public static Map getBankUserCache(){
        return bankUserCache;
    }
}
