package com.neturbo.set.utils;

import java.util.List;

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
public interface NTBResources {
    public List getKeys();
    public String getProperty(String key);
    public void setArgs(Object argObj);
    public void populate();

}
