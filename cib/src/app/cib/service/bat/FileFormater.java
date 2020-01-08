package app.cib.service.bat;

import java.text.*;
import java.util.*;
import java.math.*;
import com.neturbo.set.core.*;
import com.neturbo.set.xml.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.exception.*;
import java.io.*;
import java.sql.Timestamp;

public class FileFormater {

    //��ǰλ��;
    private int formatPos;
    //Buffer;
    private BufferedWriter formatBuffer;


    //�����嶨��xmlԪ��;
    private XMLElement rootFormatXML;

    public FileFormater(XMLElement rootFormatXML,
                         OutputStream formatFileStream) {

        try {
            //��ñ���ͷ�ͱ�����;
            //Modify by Huang GengHan 2006-6-15
            formatBuffer = new BufferedWriter(new OutputStreamWriter(
                    formatFileStream));
            this.rootFormatXML = rootFormatXML;
        } catch (Exception Ex) {
            Log.error("Initialize upload file formater error", Ex);
        }

    }

    public void formatForward(int forwardCount) {
        formatPos += forwardCount;
    }

    public int formatRecord(Map dataMap) {
        formatSection(rootFormatXML, dataMap);
        try{
            formatBuffer.newLine();
            this.formatForward(1);
            formatBuffer.flush();
        }catch(Exception e){
            Log.error("Error formating record", e);
        }
        return formatPos;
    }

    public void formatSection(XMLElement formatNode, Map data) {

        int i;
        List fields;
        String formatName = "";

        try {
            formatName = formatNode.getAttribute("name");
            //����ӽڵ��б�;
            fields = formatNode.getChildren();

            //ѭ���ӽڵ��б�;
            for (i = 0; i < fields.size(); i++) {
                XMLElement fieldXML;
                fieldXML = (XMLElement) fields.get(i);
                //������;
                String type;
                String name;
                Object subData = null;

                type = fieldXML.getName();
                name = fieldXML.getAttribute("name");
                if (name != null && data != null) {
                    subData = data.get(name);
                    if (subData == null) {
                        subData = fieldXML.getAttribute("default");
                    }
                }
                if (type.equals("section")) {
                    formatSection(fieldXML, (HashMap) subData);
                } else if (type.equals("if")) {
                    formatSectionif(fieldXML, (HashMap) subData);
                } else if (type.equals("sectionloop")) {
                    formatSectionloop(fieldXML, (ArrayList) subData, data);
                } else if (type.equals("number")) {
                    formatNumber(fieldXML, subData);
                } else if (type.equals("string")) {
                    formatString(fieldXML, subData);
                } else if (type.equals("decimal")) {
                    formatDecimal(fieldXML, subData);
                } else if (type.equals("double")) {
                    formatDouble(fieldXML, subData);
                } else if (type.equals("date")) {
                    formatDate(fieldXML, subData);
                } else if (type.equals("time")) {
                    formatTime(fieldXML, subData);
                } else if (type.equals("skip")) {
                    formatSkip(fieldXML);
                }

            }
        } catch (Exception Ex) {
            Log.error("Format section \"" + formatName + "\" error", Ex);
        }

    }

    public void formatSectionif(XMLElement formatNode, HashMap data) {

        int i;
        HashMap formatData;
        String key;
        Object keyvalue;
        String keytype;
        String isvalueStr;
        String notvalueStr;
        String isvalues[];
        String notvalues[];
        String formatName = "";

        try {
            formatName = formatNode.getAttribute("name");
            //����ӽڵ��б�;
            key = formatNode.getAttribute("key");
            keyvalue = data.get(key);

            keytype = formatNode.getAttribute("keytype");
            isvalueStr = formatNode.getAttribute("isvalue");
            if (isvalueStr != null) {
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
                        formatSection(formatNode, data);
                        return;
                    }
                }
            }

            notvalueStr = formatNode.getAttribute("notvalue");
            if (notvalueStr != null) {
                notvalues = Utils.splitStr(notvalueStr, ",");
                int count = 0;
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
                    formatSection(formatNode, data);
                    return;
                }
            }
        } catch (Exception Ex) {
            Log.error("Format sectionif \"" + formatName + "\" error", Ex);
        }

    }

    public void formatSectionloop(XMLElement formatNode, ArrayList data,
                                  Map parentData) {

        //����ӽڵ��б�;
        int i;
        String formatName = "";

        try {
            formatName = formatNode.getAttribute("name");
            String countStr;
            String countField;
            int count;
            countStr = formatNode.getAttribute("count");
            countField = formatNode.getAttribute("countfield");
            if (countStr != null) {
                count = Integer.parseInt(countStr);
            } else if (countField != null) {
                countStr = (String) parentData.get(countField);
                if (countStr != null) {
                    count = Integer.parseInt(countStr);
                } else {
                    count = data.size();
                }
            } else {
                count = data.size();
            }
            //ѭ���ӽڵ��б�;
            for (i = 0; i < count; i++) {
                HashMap loopData;
                if (data != null) {
                    if (i < data.size()) {
                        loopData = (HashMap) data.get(i);
                    } else {
                        loopData = null;
                    }
                } else {
                    loopData = null;
                }
                formatSection(formatNode, loopData);
            }
        } catch (Exception Ex) {
            Log.error("Format sectionloop \"" + formatName + "\" error", Ex);
        }

    }

    public void writeLength(String formatStr, int length, int encodingSymbol) {
        try {
            byte[] formatBytes = Encoding.encode(formatStr, encodingSymbol);
            formatBuffer.write(new String(formatBytes, 0, length));
            formatForward(length);
        } catch (Exception Ex) {
            Log.error("Writing fixed length field error", Ex);
        }
    }

    public void writeDelimiter(String formatStr, String delimiter,
                               int encodingSymbol) {
        try {
            formatStr = formatStr + delimiter;
            byte[] formatBytes = Encoding.encode(formatStr, encodingSymbol);
            System.arraycopy(formatBytes, 0, formatBuffer, formatPos,
                             formatBytes.length);
            formatForward(formatBytes.length);
        } catch (Exception Ex) {
            Log.error("Writing field with delimiter error", Ex);
        }
    }

    public String prefixChar(String source, int length, String prefix) {
        if (source == null) {
            source = "";
        }
        String stringbuffer = "";
        if (length < source.length()) {
            source = source.substring(0, length);
        }
        for (int j = 0; j < length - source.length(); j++) {
            stringbuffer = stringbuffer + prefix;
        }
        stringbuffer = stringbuffer + source;
        return stringbuffer;
    }

    public String suffixChar(String source, int length, String suffix) {
        if (source == null) {
            source = "";
        }
        String stringbuffer = "";
        if (length < source.length()) {
            source = source.substring(0, length);
        }
        for (int j = 0; j < length - source.length(); j++) {
            stringbuffer = stringbuffer + suffix;
        }
        stringbuffer = source + stringbuffer;
        return stringbuffer;
    }

    public String alignString(String source, int length, String alignStr) {
        if (source == null) {
            source = "";
        }
        String stringbuffer = "";
        for (int j = 0; j < length - source.length(); j++) {
            stringbuffer = stringbuffer + " ";
        }
        if (alignStr.equals("LEFT")) {
            stringbuffer = source + stringbuffer;
        } else if (alignStr.equals("RIGHT")) {
            stringbuffer = stringbuffer + source;
        }
        return stringbuffer;
    }

    public void formatString(XMLElement formatNode, Object data) {

        String lengthStr;
        int length = 0;
        String delimiter;
        String encodingStr;
        int encoding;
        String align;
        String prefixZero;
        boolean comp3Flag = false;

        String formatStr = (String) data;
        try {
            lengthStr = formatNode.getAttribute("length");
            if (lengthStr == null) {
                lengthStr = formatNode.getAttribute("comp3length");
                comp3Flag = true;
            }
            if (lengthStr != null) {
                int combaPos = lengthStr.indexOf(",");
                if (combaPos > 0) {
                    lengthStr = lengthStr.substring(0, combaPos);
                }
                try {
                    length = Integer.parseInt(lengthStr);
                    if (comp3Flag) {
                        length = (int) (length / 2) + 1;
                    }
                } catch (Exception Ex) {
                    length = 0;
                }
            }

            encodingStr = formatNode.getAttribute("encoding");
            encoding = Encoding.EncodingASCII;
            if (encodingStr != null) {
                if (encodingStr.toUpperCase().equals("EBCDIC")) {
                    encoding = Encoding.EncodingEBCDIC;
                } else if (encodingStr.toUpperCase().equals("ASCII")) {
                    encoding = Encoding.EncodingASCII;
                } else if (encodingStr.toUpperCase().equals("COMP3")) {
                    encoding = Encoding.EncodingCOMP3;
                } else {
                   /* Log.warn("Undefined format \"" + encodingStr + "\" for \"" +
                             formatNode.getAttribute("name") + "\" error");*/
                }
            }

            prefixZero = formatNode.getAttribute("prefixzero");
            if (prefixZero != null) {
                formatStr = prefixChar(formatStr, length, "0");
            }

            align = formatNode.getAttribute("align");
            if (align != null) {
                formatStr = alignString(formatStr, length, align);
            } else if (length > 0) {
                formatStr = alignString(formatStr, length, "LEFT");
            }

            delimiter = formatNode.getAttribute("delimiter");
            if (delimiter != null) {
                writeDelimiter(formatStr, delimiter, encoding);
            } else if (length > 0) {
                writeLength(formatStr, length, encoding);
            }
        } catch (Exception Ex) {
            Log.error("Format field \"" + formatNode.getAttribute("name") +
                      "\" with value \"" +
                      data.toString() +
                      "\" error", Ex);
        }

    }

    public void formatNumber(XMLElement formatNode, Object data) {

        String valueStr = "";
        try {
            String lengthStr = formatNode.getAttribute("length");
            if (lengthStr == null) {
                lengthStr = formatNode.getAttribute("comp3length");
            }
            int length = Integer.parseInt(lengthStr);

            if (data == null) {
                data = "0";
            }
            Long value = new Long(data.toString().trim());
            valueStr = value.toString();
            valueStr = prefixChar(valueStr, length, "0");
        } catch (Exception Ex) {
            Log.warn("Format number field \"" + formatNode.getAttribute("name") +
                     "\" with value \"" +
                     data.toString() +
                     "\" error", Ex);
        }
        formatString(formatNode, valueStr);

    }

    public void formatDecimal(XMLElement formatNode, Object data) {

        String valueStr = "";
        try {
            if (data == null) {
                data = "0";
            }
            BigDecimal value = new BigDecimal(data.toString());
            valueStr = value.toString();
            String lengthStr = formatNode.getAttribute("length");
            if (lengthStr == null) {
                lengthStr = formatNode.getAttribute("comp3length");
            }
            String hasdecimalpoint = formatNode.getAttribute("decimalpoint");
            String decimalPoint = "";
            if (hasdecimalpoint != null) {
                if (hasdecimalpoint.toUpperCase() == "YES") {
                    decimalPoint = ".";
                }
            }
            if (lengthStr != null) {
                int combaPos = lengthStr.indexOf(",");
                if (combaPos > 0) {
                    int decimalLen = Integer.parseInt(lengthStr.substring(
                            combaPos + 1));
                    int intLen = Integer.parseInt(lengthStr.substring(0,
                            combaPos)) - decimalLen;
                    if (valueStr.indexOf(".") < 0) {
                        valueStr = valueStr + ".0";
                    }

                    String[] valueStrAry = Utils.splitStr(valueStr, ".");
                    valueStrAry[0] = prefixChar(valueStrAry[0], intLen, "0");
                    valueStrAry[1] = suffixChar(valueStrAry[1], decimalLen,
                                                "0");
                    valueStr = valueStrAry[0] + decimalPoint +
                               valueStrAry[1];

                }
            }
        } catch (Exception Ex) {
            Log.warn("Format decimal field \"" +
                     formatNode.getAttribute("name") + "\" with value \"" +
                     data.toString() +
                     "\" error", Ex);
        }
        formatString(formatNode, valueStr);

    }

    public void formatDouble(XMLElement formatNode, Object data) {

        String valueStr = "";
        try {
            if (data != null) {
                Double value = new Double(data.toString().trim());
                valueStr = value.toString();
            }
        } catch (Exception Ex) {
            Log.warn("Format double field \"" + formatNode.getAttribute("name") +
                     "\" with value \"" + data.toString() +
                     "\" error", Ex);
        }
        formatString(formatNode, valueStr);
    }

    public void formatDate(XMLElement formatNode, Object data) {
        String format = formatNode.getAttribute("format");
        if (format == null) {
            format = "yyyyMMdd";
        }
        String valueStr = "";
        try {
            if (data != null) {
                if(data instanceof Date){
                    valueStr = DateTime.formatDate((Date)data, format);
                }else if(data instanceof Timestamp){
                    Date d = new Date(((Timestamp)data).getTime());
                    valueStr = DateTime.formatDate(d, format);
                }else if(data instanceof String){
                    valueStr = (String)data;
                }
                try {
                    valueStr = com.neturbo.set.utils.Format.formatDateTime(
                            valueStr,
                            "yyyyMMdd", format);
                } catch (Exception e0) {
                    try {
                        valueStr = com.neturbo.set.utils.Format.formatDateTime(
                                valueStr,
                                "yyyy-MM-dd", format);
                    } catch (Exception e1) {
                        try {
                            valueStr = com.neturbo.set.utils.Format.
                                       formatDateTime(valueStr,
                                    "yyyy-MM-dd HH:mm:ss", format);
                        } catch (Exception e2) {
                            valueStr = valueStr;
                        }
                    }
                }
            }
        } catch (Exception Ex) {
            Log.warn("Format date field \"" + formatNode.getAttribute("name") +
                     "\" with value \"" + data.toString() +
                     "\" error", Ex);
        }
        formatString(formatNode, valueStr);
    }

    public void formatTime(XMLElement formatNode, Object data) {
        String format = formatNode.getAttribute("format");
        if (format == null) {
            format = "HHmmss";
        }
        String valueStr = "";
        try {
            if (data != null) {
                if(data instanceof Date){
                    valueStr = DateTime.formatDate((Date)data, format);
                }else if(data instanceof Timestamp){
                    Date d = new Date(((Timestamp)data).getTime());
                    valueStr = DateTime.formatDate(d, format);
                }else if(data instanceof String){
                    valueStr = (String)data;
                }
                try {
                    valueStr = com.neturbo.set.utils.Format.formatDateTime(
                            valueStr,
                            "HHmmss", format);
                } catch (Exception e0) {
                    try {
                        valueStr = com.neturbo.set.utils.Format.formatDateTime(
                                valueStr,
                                "HH:mm:ss", format);
                    } catch (Exception e1) {
                        try {
                            valueStr = com.neturbo.set.utils.Format.
                                       formatDateTime(valueStr,
                                    "yyyy-MM-dd HH:mm:ss", format);
                        } catch (Exception e2) {
                            valueStr = valueStr;
                        }
                    }
                }
            }
        } catch (Exception Ex) {
            Log.warn("Format time field \"" + formatNode.getAttribute("name") +
                     "\" with value \"" + data.toString() +
                     "\" error", Ex);
        }
        formatString(formatNode, valueStr);
    }

    public void formatSkip(XMLElement formatNode) {

        try {
            String lengthStr;
            int length;
            lengthStr = formatNode.getAttribute("length");
            length = Integer.parseInt(lengthStr);
            formatForward(length);
        } catch (Exception Ex) {
            Log.error("Format skip \"" + formatNode.getAttribute("name") +
                      "\" error", Ex);
        }
    }

}
