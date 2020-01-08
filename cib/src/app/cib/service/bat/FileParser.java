package app.cib.service.bat;

import java.text.*;
import java.util.*;
import java.math.*;
import java.io.*;


import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.xml.*;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
public class FileParser {
    //��ǰλ��
    private int parsePos;
    //Buffer�ֽ�����
    //private byte[] parseBuffer;
    private XMLElement rootFormatXML;

    private BufferedReader parseBuffer;
    private InputStream parseStream;
    private GenericJdbcDao genericJdbcDao;
    //add by hjs
    private int lineLength = 0;
    //add by linrui 20180319
    private Map headerData;

    public FileParser(XMLElement rootFormatXML, InputStream parseFileStream) {
        try {
            parseBuffer = new BufferedReader(new InputStreamReader(
                    parseFileStream));
            this.rootFormatXML = rootFormatXML;
            //add by hjs
            String length = rootFormatXML.getAttribute("line_length");
            this.lineLength = Integer.parseInt(length!=null ? length : "0");
        } catch (Exception ex) {
            Log.error("Initialzie download file parser error", ex);
        }
    }

    public void parseForward(int forwardCount) {
        parsePos += forwardCount;
    }

    public boolean isParseFinish() {
        try {
            return (parseStream.available() == 0);
        } catch (Exception Ex) {
            Log.error("Parse finish flag error", Ex);
        }
        return false;
    }

    public HashMap parseSection(XMLElement parseNode) throws NTBException {
        int i;
        HashMap parseData = new HashMap();
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
                    if(retData != null){
                        retData = ((String)retData).trim();
                    }
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
                
                // Jet modified 2008-11-19
//                if (retData != null) {
                    parseData.put(name, retData);
//                }
            }
        } catch (Exception Ex) {
            Log.error("Parse section \"" + parsingName + "\" error",
                      Ex);
            if (Ex instanceof NTBException) {
            	throw (NTBException)Ex;
            } else {
                throw new NTBException("err.bat.ParseSectionError");
            }
        }
        return parseData;
    }

    public HashMap parseSectionif(XMLElement parseNode, HashMap parentData) throws NTBException {
        int i;
        HashMap parseData = null;
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
            if (keytype == null) {
                keytype = "string";
            }
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
            if (Ex instanceof NTBException) {
            	throw (NTBException)Ex;
            } else {
                throw new NTBException("err.bat.ParseSectionifError");
            }
        }
        return parseData;
    }

    public ArrayList parseSectionloop(XMLElement parseNode, HashMap parentData) throws NTBException{
        //����ӽڵ��б�;
        int i;
        ArrayList parseData = new ArrayList();
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
            } else if (countField != null && parentData.get(countField) != null) {
                countStr = parentData.get(countField).toString();
                count = Integer.parseInt(countStr);
            } else {
                count = Integer.MAX_VALUE;
            }
            //ѭ���ӽڵ��б�;
            for (i = 0; i < count; i++) {
                HashMap loopData;
                loopData = parseSection(parseNode);
                parseData.add(loopData);
                if (isParseFinish()) {
                    break;
                }
            }
        } catch (Exception Ex) {
            Log.error("Parse sectionloop \"" + parsingName + "\" error", Ex);
            if (Ex instanceof NTBException) {
            	throw (NTBException)Ex;
            } else {
                throw new NTBException("err.bat.ParseSectionloopError");
            }
        }
        return parseData;
    }

    public void parseRecord(FileRecordProcessor recordProc) throws Exception {
        //����ӽڵ��б�;
        XMLElement parseNode = rootFormatXML;
        ArrayList recordList = new ArrayList();
        //ѭ���ӽڵ��б�;
        int count = Integer.MAX_VALUE;
        for (int i = 0; i < count; i++) {
        	String line = parseBuffer.readLine();
            if (line == null) {
                break;
            }
            if (line.trim().equals("")) {
                continue;
            }
            // add by hjs, check line length
            if(this.lineLength!=0 && this.lineLength!=line.length()) {
            	throw new NTBException("err.bat.LineLengthError", new Object[]{new Integer(i+1)});
            }
            parseStream = new ByteArrayInputStream(line.getBytes());
            HashMap loopData = null;
            try {
                // add by hjs, check parse error
            	loopData = parseSection(parseNode);
            } catch (NTBException e) {
            	throw new NTBException(e.getErrorCode(), new Object[]{new Integer(i+1)});
            }
            int retVal = recordProc.processRecord(loopData);
//            Log.info(String.valueOf(retVal));
            if (retVal == FileRecordProcessor.FILE_RECORD_STOP) {
                break;
            }
            if (retVal == FileRecordProcessor.FILE_RECORD_BATCH) {
                recordList.add(loopData);
            }
            //����д��ݿ�
            if (recordList.size() >= 200) {
                Log.info("Write start(" + recordList.size() + " records)=" +
                         System.currentTimeMillis());
                recordProc.processRecords(recordList);
                Log.info("Write end=" + System.currentTimeMillis());
                recordList.clear();
            }
        }

        //�������д��ݿ�
        Log.info("Write start(" + recordList.size() + " records)=" +
                 System.currentTimeMillis());
        recordProc.processRecords(recordList);
        Log.info("Write end=" + System.currentTimeMillis());
    }

    public String readLength(int length, int encodingSymbol) throws NTBException {
        String parseStr = null;
        try {
            byte[] parseBytes = new byte[length];
            int readlength = parseStream.read(parseBytes, 0, length);
            parseStr = Encoding.decode(parseBytes, length, encodingSymbol);
            parseForward(readlength);
        } catch (Exception Ex) {
            Log.error("Read fixed length field error", Ex);
            throw new NTBException("err.bat.ReadLengthError");
        }
        return parseStr;
    }

    public String readDelimiter(String delimiter, int encodingSymbol) throws NTBException {
        String parseStr = null;
        try {
            int delimiterLen;
            delimiterLen = delimiter.length();
            byte[] delimiterbuffer = new byte[delimiterLen];
            byte[] parseBytes = new byte[1024];
            byte readByte;
            int readLen;
            parseStream.read(parseBytes, 0, delimiterLen);
            //System.arraycopy(parseBuffer, parsePos, parseBytes, 0, delimiterLen);
            parseForward(delimiterLen);
            readLen = delimiterLen;
            while (new String(parseBytes, readLen - delimiterLen, delimiterLen).
                   equals(delimiter)) {
                parseStream.read(parseBytes, parsePos, delimiterLen);
                //parseBytes[readLen] = parseBuffer[parsePos];
                readLen = readLen + 1;
                parseForward(1);
            }
            parseStr = Encoding.decode(parseBytes, readLen - delimiterLen,
                                       encodingSymbol);
            parseStr = parseStr.substring(0, readLen - 1);
        } catch (Exception Ex) {
            Log.error("Read field with delimiter error", Ex);
            throw new NTBException("err.bat.ReadDelimiterError");
        }
        return parseStr;
    }

    public Object parseString(XMLElement parseNode) throws NTBException {

        String lengthStr;
        int length = 0;
        String comp3lengthStr;
        int comp3length = 0;
        int decimalLen = 0;
        String delimiter;
        String encodingStr;
        int encoding;
        String parseStr = null;
        String parsingName = "";
        String defaultValue = null;
        try {
            parsingName = parseNode.getAttribute("name");
            defaultValue = parseNode.getAttribute("default");
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
                    String decimalLenStr = lengthStr.substring(combaPos + 1);
                    decimalLen = Integer.parseInt(decimalLenStr);
                    comp3lengthStr = comp3lengthStr.substring(0, combaPos);
                }
                comp3length = Integer.parseInt(comp3lengthStr);
                length = (comp3length + 1) / 2;
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
                } else {
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
                parseStr = parseStr.substring(parseStr.length() - comp3length,
                                              comp3length);
            }
            String decimalPoint = parseNode.getAttribute("decimalpoint");
            if (decimalPoint == null || decimalPoint.toUpperCase().equals("NO")) {
                if (decimalLen > 0) {
                    parseStr = parseStr.substring(0,
                                                  parseStr.length() -
                                                  decimalLen) +
                               "." +
                               parseStr.substring(parseStr.length() -
                                                  decimalLen);
                }
            }
        } catch (Exception Ex) {
            Log.error("Parse field \"" + parsingName + "\" with value \"" +
                      parseStr +
                      "\" error", Ex);
            throw new NTBException("err.bat.ParseStringError");
        }
        if (parseStr == null) {
            parseStr = defaultValue;
        }
        return parseStr;
    }

    public Object parseNumber(XMLElement parseNode) throws NTBException {
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
            throw new NTBException("err.bat.ParseNumberError");
        }
        return retData;
    }

    public Object parseDecimal(XMLElement parseNode) throws NTBException {
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
            throw new NTBException("err.bat.ParseDecimalError");
        }
        return retData;
    }

    public Object parseDouble(XMLElement parseNode) throws NTBException {
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
            throw new NTBException("err.bat.ParseDoubleError");
        }
        return retData;
    }

    public Object parseDate(XMLElement parseNode) throws NTBException {
        String parseStr = null;
        Object retData = null;
        String parsingName = "";
        try {
            parsingName = parseNode.getAttribute("name");
            parseStr = ((String) parseString(parseNode)).trim();
            if (parseStr != null && parseStr.trim().length() > 0) {
                String format = parseNode.getAttribute("format");
                if (format == null) {
                    format = "yyyyMMdd";
                }
                SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
                java.util.Date retdate = simpledateformat.parse(parseStr,
                        new ParsePosition(0));
                retData = retdate;
            }
        } catch (Exception Ex) {
            Log.warn("Parse date field \"" + parsingName + "\" with value \"" +
                     parseStr + "\" error",
                     Ex);
            throw new NTBException("err.bat.ParseDateError");
        }
        return retData;
    }

    public Object parseTime(XMLElement parseNode) throws NTBException {
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
            throw new NTBException("err.bat.ParseTimeError");
        }
        return retData;
    }

    public void parseSkip(XMLElement parseNode) throws NTBException {

        String parsingName = null;
        try {
            parsingName = parseNode.getAttribute("name");
            String lengthStr;
            int length;
            lengthStr = parseNode.getAttribute("length");
            length = Integer.parseInt(lengthStr);
            readLength(length, Encoding.EncodingASCII);
            parseForward(length);
        } catch (Exception Ex) {
            Log.error("Parse skip \"" + parsingName + "\" error", Ex);
            throw new NTBException("err.bat.ParseSkipError");
        }
    }

    public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
        this.genericJdbcDao = genericJdbcDao;
    }
    //add by linrui 20180319
	public List parseRecRecord(FileRecordProcessor recordProc) throws Exception {
		boolean checkUniqueFlag = false;
		// GET NODE LIST;
		XMLElement parseNode = rootFormatXML;
		ArrayList recordList = new ArrayList();
		// FOR NODE LIST;
		int count = Integer.MAX_VALUE;
		for (int i = 0; i < count; i++) {
			String line = parseBuffer.readLine();
			if (line == null) {
				break;
			}
			if (line.trim().equals("")) {
				continue;
			}
			parseStream = new ByteArrayInputStream(line.getBytes("UTF-8"));
			HashMap loopData = parseSection(parseNode);           
			recordList.add(loopData);			
			if (recordList.size() >= 200) {
				Log.info("Write start(" + recordList.size() + " records)="
						+ System.currentTimeMillis());
				recordProc.processRecords(recordList);
				Log.info("Write end=" + System.currentTimeMillis());
				recordList.clear();
			}
		}
		return recordList;
	}
}
