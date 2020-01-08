package com.neturbo.set.xml;

import com.neturbo.set.xml.xerces.*;

public class XMLFactory {
    private static XMLWriter defaultWriter = new XercesWriter();
    private static XMLParser defaultParser = new XercesParser();

    private XMLFactory() {
    }

    public static XMLParser getParser(){
        return (XMLParser)defaultParser.clone();
    }

    public static XMLWriter getWriter(){
        return (XMLWriter)defaultWriter.clone();
    }

    public static void setWriter(XMLWriter _defaultWriter) {
        defaultWriter = _defaultWriter;
    }

    public static void setParser(XMLParser _defaultParser) {
        defaultParser = _defaultParser;
    }

    public void setDefaultWriter(XMLWriter _defaultWriter) {
        defaultWriter = _defaultWriter;
    }

    public void setDefaultParser(XMLParser _defaultParser) {
        defaultParser = _defaultParser;
    }

    public XMLWriter getDefaultWriter() {
        return defaultWriter;
    }

    public XMLParser getDefaultParser() {
        return defaultParser;
    }
}
