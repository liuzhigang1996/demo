package com.neturbo.set.xml;

import java.io.*;
import java.util.*;
import com.neturbo.set.exception.*;

/**
 * 此处插入类型描述。
 * 创建日期：(2003-8-31 20:15:59)
 * @author：Administrator
 */
public abstract class XMLParser implements Cloneable {

    protected String realm;
    public abstract void setInput(String fileName);
    public abstract void setInput(Reader reader);
    public abstract void setInput(InputStream inputStream);
    public abstract XMLElement getRootElement();
    public abstract void unMarshal() throws NTBException;

    public Object clone() {
        Object object = null;
        try {
            object = super.clone();
        } catch (CloneNotSupportedException exception) {
            System.err.println("AbstractSpoon is not Cloneable");
        }
        return object;
    }
}
