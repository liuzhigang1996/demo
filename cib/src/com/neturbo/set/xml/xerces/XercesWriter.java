package com.neturbo.set.xml.xerces;

import java.io.*;

import java.sql.*;
import java.util.*;
import javax.xml.parsers.*;
import org.apache.xerces.dom.*;
import org.apache.xerces.jaxp.*;
import org.w3c.dom.*;
import com.neturbo.set.core.*;
import com.neturbo.set.exception.*;
import com.neturbo.set.xml.*;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.OutputFormat;

/**
 * 此处插入类型描述。
 * 创建日期：(2003-8-31 20:15:59)
 * @author：Administrator
 */
public class XercesWriter extends XMLWriter {
    protected XMLSerializer serializer;
    protected XMLElement rootElement;
    protected String charset = "UTF-8";
    protected OutputFormat outputFormat = new OutputFormat("XML", charset, true);
    /**
     * XMLParser 构造子注解。
     */
    public XercesWriter() {
    }

    public void setOutput(String fileName) {
        try {
            Writer writer = new FileWriter(fileName);
            serializer = new XMLSerializer(writer, outputFormat);
        } catch (Exception e) {

        }
    }

    public void setOutput(Writer writer) {
        serializer = new XMLSerializer(writer, outputFormat);
    }

    public void setOutput(OutputStream outputStream) {
        serializer = new XMLSerializer(outputStream, outputFormat);
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * 此处插入方法描述。
     * 创建日期：(2003-8-31 20:50:48)
     * @param element org.w3c.dom.Element
     */
    public void writeElement(XMLElement myElement, Document doc,
                             Element element) {

        if (myElement == null) {
            return;
        }

        Map attrs = myElement.getAttributes();
        Object[] keys = attrs.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            String value = (String) attrs.get(key);
            element.setAttribute(key, value);
        }

        String text = myElement.getText();
        if (text != null) {
            element.appendChild(doc.createTextNode(text));
        }

        List childs = myElement.getChildren();
        for (int i = 0; i < childs.size(); i++) {
            XMLElement child = (XMLElement) childs.get(i);
            if (child == null) {
                continue;
            }
            Element childElement = doc.createElement(child.getName());
            writeElement(child, doc, childElement);
            element.appendChild(childElement);
        }
    }

    /**
     * 此处插入方法描述。
     * 创建日期：(2003-8-31 20:39:52)
     * @param newRootElement int
     */
    public void setRootElement(XMLElement newRootElement) {
        rootElement = newRootElement;
    }

    public void Marshal() throws NTBException {
        try {
            //获得dom的实现
            org.apache.xerces.dom.DOMImplementationImpl domImpl = new
                org.apache.xerces.dom.DOMImplementationImpl();
            //获得docType(dtd)
            DocumentType docType = domImpl.createDocumentType(rootElement.getName(), null, null);
            //获得新的doc
            Document doc = domImpl.createDocument(null, rootElement.getName(),
                                                  docType);
            Element root = doc.getDocumentElement();
            writeElement(rootElement, doc, root);

            serializer.serialize(doc);

        }

        catch (Exception e) {
            Log.error("Formating XML error in Marshal(XMLWriter)", e);
        }
    }

}
