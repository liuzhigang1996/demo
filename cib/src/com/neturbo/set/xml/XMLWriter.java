package com.neturbo.set.xml;

import java.io.Writer;
import java.io.OutputStream;
import com.neturbo.set.exception.*;


/**
 * 此处插入类型描述。
 * 创建日期：(2003-8-31 20:15:59)
 * @author：Administrator
 */
public abstract class XMLWriter implements Cloneable {

    protected String realm;
    public abstract void setOutput(String fileName);
    public abstract void setOutput(Writer writer);
    public abstract void setOutput(OutputStream outputStream);
    public abstract void setRootElement(XMLElement newRootElement);
    public abstract void setCharset(String charset) throws NTBException;
    public abstract void Marshal() throws NTBException;

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
