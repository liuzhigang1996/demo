package com.neturbo.set.transaction;

import java.math.*;
import java.text.*;
import java.util.*;

import com.neturbo.set.core.*;
import com.neturbo.set.exception.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.xml.*;

public class PacketParser {
    //��ǰλ��
    private int parsePos;
    //Buffer�ĳ���
    private int parseBufferSize;
    //Buffer
    private byte[] parseBuffer;

    //�����Ŀ
    private int fieldCount;

    //��ʽ�ļ���
    private String formatFile;

    //����ͷ����Ԫ��
    private String serviceName;
    private XMLElement serviceXML;
    private int headerLength;
    //����ͷ��Section
    private XMLElement headerPartXML;

    //���״���
    private String transIDField;
    private String transID;

    //
    private String transLengthField;
    private int transLength;

    //�����嶨��xmlԪ��
    private XMLElement formatXML;
    //�������Section
    private XMLElement bodyPartXML;

    private Map publicData=new HashMap();
    private PacketHandler packetHandler1;
    private boolean succeed = false;

    private int ServerOrClient;
    public static final int SERVER_PARSE = 0;
    public static final int CLIENT_PARSE = 1;

    public PacketParser(String newServiceName, byte[] newParseData,
                        int dataLen, PacketHandler handler,
                        int newServerOrClient) {
        try {
            ServerOrClient = newServerOrClient;
            parseBuffer = new byte[dataLen];
            System.arraycopy(newParseData, 0, parseBuffer, 0, dataLen);
            parseBufferSize = dataLen;
            //��ñ���ͷ�ͱ������XML;
            serviceName = newServiceName;
            serviceXML = TransXMLFactory.getService(serviceName);
            packetHandler1 = handler;

            //��ñ���ͷ�ͱ�����;
            if (ServerOrClient == SERVER_PARSE) {
                headerPartXML = serviceXML.findNodeByName("header-to-host");
            } else {
                headerPartXML = serviceXML.findNodeByName("header-from-host");
            }
            String headerLengthStr = headerPartXML.getAttribute("length");
            if (headerLengthStr != null) {
                headerLength = Integer.parseInt(headerLengthStr);
            }
            transIDField = headerPartXML.getAttribute("idfield");
            transLengthField = headerPartXML.getAttribute("lengthfield");
        } catch (Exception ex) {
            Log.error("Initialzie packet parser error");
        }
    }

    public PacketParser(String newServiceName, String transId,
                        byte[] newParseData,
                        int dataLen, PacketHandler handler,
                        int newServerOrClient) {
        this(newServiceName, newParseData, dataLen, handler, newServerOrClient);
        this.transID = transId;
    }

    public void parseForward(int forwardCount) {
        parsePos += forwardCount;
    }

    public String getTransID() {
        return transID;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public boolean isParseFinish() {
        return (parsePos >= parseBufferSize);
    }

    public Map parseHeader(XMLElement parseXML) {
        return parseSection(parseXML);
    }

    public Map parseBody(XMLElement parseXML) {
        return parseSection(parseXML);
    }

    public Map parse() throws NTBHostException{

        Map headerTable = new HashMap();
        Map bodyTable = new HashMap();
        try {
            headerTable = parseHeader(headerPartXML);
            if(transID == null){
                transID = (String) headerTable.get(transIDField);
            }
        } catch (Exception ex) {
            Log.error("Parse packet header error", ex);
        }

        succeed = packetHandler1.processPacket(transID, headerTable);
        if(!succeed){
            return headerTable;
        }

        try {
            formatXML = TransXMLFactory.getTransation(serviceName, transID);
            if (ServerOrClient == SERVER_PARSE) {
                bodyPartXML = formatXML.findNodeByName("packet-to-host");
            } else {
                bodyPartXML = formatXML.findNodeByName("packet-from-host");
            }
            parsePos = headerLength;
            publicData.putAll(headerTable);
            bodyTable = parseBody(bodyPartXML);
        } catch (Exception ex) {
            Log.error("Parse packet body error", ex);
        }
        headerTable.putAll(bodyTable);

        return headerTable;
    }

    public Map parseSection(XMLElement parseNode) {
        int i;
        Map parseData = new HashMap();
        List fields;
        String parsingName = "";
        try {
            parsingName = parseNode.getAttribute("name");
            //����ӽڵ��б�;
            fields = parseNode.getChildren();
            //ѭ���ӽڵ��б�;
            for (i = 0; i < fields.size(); i++) {
                XMLElement fieldXML;
                fieldXML = (XMLElement) fields.get(i);
                //������;
                String type;
                String name;
                Object retData;
                type = fieldXML.getName();
                name = fieldXML.getAttribute("name");
                retData = null;
                if (type.equals("section")) {
                    retData = parseSection(fieldXML);
                } else if (type.equals("sectionif")) {
                    retData = parseSectionif(fieldXML, parseData);
                } else if (type.equals("sectionloop")) {
                    retData = parseSectionloop(fieldXML, parseData);
                } else if (type.equals("number")) {
                    retData = parseNumber(fieldXML);
                } else if (type.equals("string")) {
                    retData = parseString(fieldXML);
                } else if (type.equals("decimal")) {
                    retData = parseDecimal(fieldXML);
                } else if (type.equals("double")) {
                    retData = parseDouble(fieldXML);
                } else if (type.equals("date")) {
                    retData = parseDate(fieldXML);
                } else if (type.equals("time")) {
                    retData = parseTime(fieldXML);
                } else if (type.equals("skip")) {
                    parseSkip(fieldXML);
                }
                if (retData != null) {
                    parseData.put(name, retData);
                }
            }
        } catch (Exception Ex) {
            Log.error("Parse section \"" + parsingName + "\" error",
                      Ex);
        }
        Log.info("parseData: " + parseData);
        return parseData;
    }

    public Map parseSectionif(XMLElement parseNode, Map parentData) {
        int i;
        Map parseData = null;
        String key;
        Object keyvalue;
        String keytype;
        String isvalueStr;
        String notvalueStr;
        String[] isvalues;
        Object isvalue;
        String[] notvalues;
        Object notvalue;
        String parsingName = "";
        try {
            parsingName = parseNode.getAttribute("name");
            //����ӽڵ��б�;
            key = parseNode.getAttribute("key");
            keyvalue = parentData.get(key);
            keytype = parseNode.getAttribute("keytype");
            isvalueStr = parseNode.getAttribute("isvalue");
            if (isvalueStr != null && keyvalue != null) {
                isvalues = Utils.splitStr(isvalueStr, ",");
                boolean equalsFlag = false;
                for (i = 0; i < isvalues.length; i++) {
                    if (keytype.toLowerCase().equals("number")) {
                        int isvalueInt = Integer.parseInt(isvalues[i].trim());
                        int keyvalueInt = Integer.parseInt(keyvalue.toString().
                                trim());
                        equalsFlag = isvalueInt == keyvalueInt;
                    } else if (keytype.toLowerCase().equals("string")) {
                        equalsFlag = isvalues[i].equals(keyvalue);
                    } else if (keytype.toLowerCase().equals("decimal") ||
                               keytype.toLowerCase().equals("double")) {
                        double isvalueDbl = Double.parseDouble(isvalues[i].trim());
                        double keyvalueDbl = Double.parseDouble(keyvalue.
                                toString().trim());
                        equalsFlag = isvalueDbl == keyvalueDbl;
                    }
                    if (equalsFlag) {
                        return parseSection(parseNode);
                    }
                }
            }
            notvalueStr = parseNode.getAttribute("notvalue");
            if (notvalueStr != null && keyvalue != null) {
                notvalues = Utils.splitStr(notvalueStr, ",");
                int count;
                count = 0;
                boolean equalsFlag = false;
                for (i = 0; i < notvalues.length; i++) {
                    if (keytype.toLowerCase().equals("number")) {
                        int notvalueInt = Integer.parseInt(notvalues[i].trim());
                        int keyvalueInt = Integer.parseInt(keyvalue.toString().
                                trim());
                        equalsFlag = notvalueInt == keyvalueInt;
                    } else if (keytype.toLowerCase().equals("string")) {
                        equalsFlag = notvalues[i].equals(keyvalue);
                    } else if (keytype.toLowerCase().equals("decimal") ||
                               keytype.toLowerCase().equals("double")) {
                        double notvalueDbl = Double.parseDouble(notvalues[i].
                                trim());
                        double keyvalueDbl = Double.parseDouble(keyvalue.
                                toString().trim());
                        equalsFlag = notvalueDbl == keyvalueDbl;
                    }
                    if (!equalsFlag) {
                        count = count + 1;
                    }
                }
                if (count == notvalues.length) {
                    parseData = parseSection(parseNode);
                }
            }
        } catch (Exception Ex) {
            Log.error("Parse sectionif \"" + parsingName + "\" error", Ex);
        }
        return parseData;
    }

    public List parseSectionloop(XMLElement parseNode, Map parentData) {
        //����ӽڵ��б�;
        int i;
        List parseData = new ArrayList();
        String parsingName = "";
        try {
            parsingName = parseNode.getAttribute("name");
            String countStr;
            int count;
            String countField;
            countStr = parseNode.getAttribute("count");
            countField = parseNode.getAttribute("countfield");
            if (countStr != null) {
                count = Integer.parseInt(countStr);
            } else {
                Object countObj = parentData.get(countField);
                if(countObj == null){
                    countObj = publicData.get(countField);
                }
                if(countObj != null ){
                    countStr = countObj.toString().trim();
                    count = Integer.parseInt(countStr);
                }else{
                    count = Integer.MAX_VALUE;
                }
            }
            //ѭ���ӽڵ��б�;
            for (i = 0; i < count; i++) {
                Map loopData;
                loopData = parseSection(parseNode);
                parseData.add(loopData);
                if (isParseFinish()) {
                    break;
                }
            }
        } catch (Exception Ex) {
            Log.error("Parse sectionloop \"" + parsingName + "\" error", Ex);
        }
        return parseData;
    }

    public String readLength(int length, int encodingSymbol) {
        String parseStr = null;
        try {
            byte[] parseBytes = new byte[length];
            System.arraycopy(parseBuffer, parsePos, parseBytes, 0, length);
            parseStr = Encoding.decode(parseBytes, length, encodingSymbol);
            parseForward(length);
        } catch (Exception Ex) {
            Log.error("Read fixed length field error", Ex);
        }
        return parseStr;
    }

    public String readDelimiter(String delimiter, int encodingSymbol) {
        String parseStr = null;
        try {
            int delimiterLen;
            delimiterLen = delimiter.length();
            byte[] delimiterbuffer = new byte[delimiterLen];
            byte[] parseBytes = new byte[1024];
            byte readByte;
            int readLen;
            System.arraycopy(parseBuffer, parsePos, parseBytes, 0, delimiterLen);
            parseForward(delimiterLen);
            readLen = delimiterLen;
            while (new String(parseBytes, readLen - delimiterLen, delimiterLen).
                   equals(delimiter)) {
                parseBytes[readLen] = parseBuffer[parsePos];
                readLen = readLen + 1;
                parseForward(1);
            }
            parseStr = Encoding.decode(parseBytes, readLen - delimiterLen,
                                       encodingSymbol);
            parseStr = parseStr.substring(0, readLen - 1);
        } catch (Exception Ex) {
            Log.error("Read field with delimiter error", Ex);
        }
        return parseStr;
    }

    public Object parseString(XMLElement parseNode) {

        String lengthStr;
        int length = 0;
        String comp3lengthStr;
        int comp3length = 0;
        int decimalLen = 0;
        String sign = "";
        String delimiter;
        String encodingStr;
        int encoding;
        String parseStr = null;
        String parsingName = "";
        
        try {
            parsingName = parseNode.getAttribute("name");
            lengthStr = parseNode.getAttribute("length");
            comp3lengthStr = parseNode.getAttribute("comp3length");
            if (lengthStr != null) {
                int combaPos = lengthStr.indexOf(",");
                if (combaPos > 0) {
                    String decimalLenStr = lengthStr.substring(combaPos + 1);
                    decimalLen = Integer.parseInt(decimalLenStr);
                    lengthStr = lengthStr.substring(0, combaPos);
                }
                length = Integer.parseInt(lengthStr);
            }
            if (comp3lengthStr != null) {
                int combaPos = comp3lengthStr.indexOf(",");
                if (combaPos > 0) {
                    String decimalLenStr = comp3lengthStr.substring(combaPos + 1);
                    decimalLen = Integer.parseInt(decimalLenStr);
                    comp3lengthStr = comp3lengthStr.substring(0, combaPos);
                }
                comp3length = Integer.parseInt(comp3lengthStr);
                length = (int) (comp3length / 2) + 1;
            }
            delimiter = parseNode.getAttribute("delimiter");
            encodingStr = parseNode.getAttribute("encoding");
            encoding = Encoding.EncodingASCII;
            if (encodingStr != null) {
                if (encodingStr.toUpperCase().equals("EBCDIC")) {
                    encoding = Encoding.EncodingEBCDIC;
                } else if (encodingStr.toUpperCase().equals("COMP3")) {
                    encoding = Encoding.EncodingCOMP3;
                } else if (encodingStr.toUpperCase().equals("EBCDIC_SIGNED")) {
                    encoding = Encoding.EncodingEBCDIC_SIGNED;
                } else if (encodingStr.toUpperCase().equals("COMP")) {
                    encoding = Encoding.EncodingCOMP;
                } else if (encodingStr.toUpperCase().equals("ASCII")) {
                    encoding = Encoding.EncodingASCII;
                }else if(encodingStr.toUpperCase().equals("UTF8") || encodingStr.toUpperCase().equals("UTF-8")){
                	encoding = Encoding.EncodingUTF8; 
                }else {
                    /*Log.warn("Undefined format \"" + encodingStr + "\" for \"" +
                             parsingName + "\" error");*/
                }
            }
            if (delimiter != null) {
                parseStr = readDelimiter(delimiter, encoding);
            } else if (length > 0) {
                parseStr = readLength(length, encoding);
            }
            if (comp3lengthStr != null) {
                if(parseStr.indexOf("-")>=0){
                    sign = "-";
                }
                parseStr = parseStr.substring(parseStr.length() - comp3length);
            }
            String decimalPoint = parseNode.getAttribute("decimalpoint");
            if ((decimalPoint == null || decimalPoint.toUpperCase().equals("NO")) && decimalLen > 0) {
                    parseStr = parseStr.substring(0,
                                                  parseStr.length() - decimalLen) +
                               "." +
                               parseStr.substring(parseStr.length() - decimalLen);
            }
        } catch (Exception Ex) {
            Log.error("Parse field \"" + parsingName + "\" with value \"" +
                      parseStr +
                      "\" error", Ex);
            return null;
        }
        return sign + parseStr;
    }

    public Object parseNumber(XMLElement parseNode) {
        String parseStr = null;
        Object retData = null;
        String parsingName = "";
        try {
            parsingName = parseNode.getAttribute("name");
            parseStr = ((String) parseString(parseNode)).trim();
            retData = new Long(parseStr);
        } catch (Exception Ex) {
            Log.warn("Parse number field \"" + parsingName + "\" with value \"" +
                     parseStr + "\" error",
                     Ex);
            return null;
        }
        return retData;
    }

    public Object parseDecimal(XMLElement parseNode) {
        String parseStr = null;
        Object retData = null;
        String parsingName = "";
        try {
            parsingName = parseNode.getAttribute("name");
            parseStr = ((String) parseString(parseNode)).trim();
            retData = new BigDecimal(parseStr);
        } catch (Exception Ex) {
            Log.warn("Parse decimal field \"" + parsingName +
                     "\" with value \"" +
                     parseStr + "\" error",
                     Ex);
            return null;
        }
        return retData;
    }

    public Object parseDouble(XMLElement parseNode) {
        String parseStr = null;
        Object retData = null;
        String parsingName = "";
        try {
            parsingName = parseNode.getAttribute("name");
            parseStr = ((String) parseString(parseNode)).trim();
            retData = new Double(parseStr);
        } catch (Exception Ex) {
            Log.warn("Parse double field \"" + parsingName + "\" with value \"" +
                     parseStr + "\" error",
                     Ex);
            return null;
        }
        return retData;
    }

    public Object parseDate(XMLElement parseNode) {
        String parseStr = null;
        Object retData = null;
        String parsingName = "";
        try {
            parsingName = parseNode.getAttribute("name");
            parseStr = ((String) parseString(parseNode)).trim();
            if (parseStr != null && parseStr.trim().length() > 0) {
                String format = parseNode.getAttribute("format");
                SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
                java.util.Date retdate = simpledateformat.parse(parseStr,
                        new ParsePosition(0));
                retData = retdate;
            }
        } catch (Exception Ex) {
            Log.warn("Parse date field \"" + parsingName + "\" with value \"" +
                     parseStr + "\" error",
                     Ex);
            return null;
        }
        return retData;
    }

    public Object parseTime(XMLElement parseNode) {
        String parseStr = null;
        Date retData = null;
        String parsingName = "";
        try {
            parsingName = parseNode.getAttribute("name");
            parseStr = ((String) parseString(parseNode)).trim();
            if (parseStr != null && parseStr.trim().length() > 0) {
                String format = parseNode.getAttribute("format");
                SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
                java.util.Date retdate = simpledateformat.parse(parseStr,
                        new ParsePosition(0));
                retData = retdate;
            }
        } catch (Exception Ex) {
            Log.warn("Parse time field \"" + parsingName + "\" with value \"" +
                     parseStr + "\" error",
                     Ex);
            return null;
        }
        return retData;
    }

    public void parseSkip(XMLElement parseNode) {

        String parsingName = null;
        try {
            parsingName = parseNode.getAttribute("name");
            String lengthStr;
            int length;
            lengthStr = parseNode.getAttribute("length");
            length = Integer.parseInt(lengthStr);
            parseForward(length);
        } catch (Exception Ex) {
            Log.error("Parse skip \"" + parsingName + "\" error", Ex);
        }
    }
}
