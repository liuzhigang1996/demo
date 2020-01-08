package com.neturbo.set.xml.xerces;

import java.io.*;

import java.sql.*;
import java.util.*;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.exception.*;
import com.neturbo.set.xml.*;

/**
 * �˴���������������
 * �������ڣ�(2003-8-31 20:15:59)
 * @author��Administrator
 */
public class XercesParser extends XMLParser {
    protected InputSource xmlInputSource;
    protected XMLElement RootElement;
    /**
     * XMLParser ������ע�⡣
     */
    public XercesParser() {
    }

    /**
     * XMLParser ������ע�⡣
     */
    public void setInput(String fileName) {
        xmlInputSource = new InputSource(fileName);
    }

    /**
     * XMLParser ������ע�⡣
     */
    public void setInput(Reader reader) {
        xmlInputSource = new InputSource(reader);
    }

    /**
     * XMLParser ������ע�⡣
     */
    public void setInput(InputStream inputStream) {
        xmlInputSource = new InputSource(inputStream);
    }

    /**
     * �˴����뷽��������
     * �������ڣ�(2003-8-31 20:39:52)
     * @return int
     */
    public XMLElement getRootElement() {
        return RootElement;
    }

    /**
     * �˴����뷽��������
     * �������ڣ�(2003-8-31 20:50:48)
     * @param element org.w3c.dom.Element
     */
    private void parseElement(Element element, XMLElement myElement) {

        if (element == null) {
            return;
        }

        Node child;
        NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            myElement.addAttribute(attr.getNodeName(), attr.getNodeValue());
        }

        Node next = (Node) element.getFirstChild();
        while ((child = next) != null) {
            next = child.getNextSibling();
            if (child.getNodeType() == Node.TEXT_NODE) {

                String trimmed = child.getNodeValue().trim();
                if (trimmed.length() == 0) {
                    element.removeChild(child);

                } else {
                    myElement.setText(trimmed);
                }
            } else
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                XMLElement childElement = new XMLElement(child.getNodeName());
                myElement.addChild(childElement);
                childElement.setParent(myElement);
                parseElement((Element) child, childElement);

            }
        }
    }

    /**
     * �˴����뷽��������
     * �������ڣ�(2003-8-31 20:34:42)
     */
    public void unMarshal() throws NTBException {
        DOMParser parser = new DOMParser();

        try {
            parser.parse(xmlInputSource);
        } catch (Exception e) {
            Log.error("Formating XML error in Marshal(XMLWriter)", e);
        }
        Document doc = parser.getDocument();
        Element element = doc.getDocumentElement();
        RootElement = new XMLElement(element.getNodeName());
        parseElement((Element) element, RootElement);

    }

}
