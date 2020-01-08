package com.neturbo.set.utils;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import com.neturbo.set.xml.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.core.*;
import com.neturbo.set.exception.*;

public class FieldChkXMLFactory {

    private static HashMap instances =
            new HashMap(50);
    private static String xmlDirName = Config.getProperty("FieldChkXMLDir") +
                                       "/";
    private static String jsDirName = Config.getProperty("FieldChkJsDir") + "/";
    private static RBFactory fieldChkErrMsg = RBFactory.getInstance(
            "app.cib.resource.common.field_check");
    private static String defalutDatePattern;

    private XMLElement xml = null;
    private RBFactory rc = null;
    private String locale = "";//add by linrui for mul-language

    static {
        File jsDir = new File(jsDirName);
        if (!jsDir.exists()) {
            jsDir.mkdir();
        }
        defalutDatePattern = Config.getProperty("DefaultDatePattern");
        if (defalutDatePattern == null) {
            defalutDatePattern = "yyyy-MM-dd";
        }
    }

    /**
     *    私有的构造子保证外界无法直接将此类实例化
     */
    private FieldChkXMLFactory() {
    }

    //生成 check group 代码
    private void writeGroupJs(BufferedWriter writer,
                              XMLElement group) throws
            NTBSystemException {
    	//add by linrui 20180512
    	fieldChkErrMsg = RBFactory.getInstance(
        "app.cib.resource.common.field_check",locale);
        //获得 group 名称
        String groupName = group.getAttribute("name");
        try {
            //写函数头
            writeln(writer, "function validate_" + groupName +
                    "(form, ignoreErrorMsg, debugMode) {");
            writeln(writer, "errorIndex = 0;");

            //循环每个field
            List fieldList = group.getChildren();
            for (int i = 0; i < fieldList.size(); i++) {
                XMLElement field = (XMLElement) fieldList.get(i);
                writeFieldJs(writer, field);
            }

            //写函数尾
            writeln(writer, "errors.length = errorIndex;");
            writeln(writer, "if (errors.length > 0 ) {");
            writeln(writer, "if ( !ignoreErrorMsg ) {");
//          writeln(writer, "chkDisplayErrorMessageWithMarker(form.name);");
//          mod by liqun 20171124
            writeln(writer, "chkDisplayErrorMessageWithMarkerByLanguage(form.name,'"+locale+"');");
            writeln(writer, "window.location.hash = 'errMsg';");
            writeln(writer, "}");
            writeln(writer, "return false;");
            writeln(writer, "}");
            writeln(writer, "return true;");
            writeln(writer, "}");

            //换行格开每个 group 函数
            writer.newLine();
        } catch (Exception e) {
            Log.error("Error processing group: " + groupName, e);
        }
    }

    //生成 check field 代码
    private void writeFieldJs(BufferedWriter writer,
                              XMLElement field) throws
            NTBSystemException {

        //获得 field 名称
        String fieldName = field.getAttribute("name");
    	//add by linrui 20180512
    	fieldChkErrMsg = RBFactory.getInstance(
        "app.cib.resource.common.field_check",locale);
        try {
            String fieldLabel = field.getAttribute("label");
            fieldLabel = rc.getString(fieldLabel);
            String fieldRequired = field.getAttribute("required");
            if (fieldRequired == null) {
                fieldRequired = "NO";
            }

            //如果是 required field 则生产检查代码
            if (fieldRequired.toUpperCase().equals("YES") ||
                fieldRequired.toUpperCase().equals("TRUE")) {
                writeln(writer,
                        "if(debugMode==true)alert('" + fieldName +
                        ": required');");
                writeln(writer,
                        "if(!validateRequired(form.elements['" + fieldName +
                        "'])) {");
                String errMsg = fieldChkErrMsg.getString("REQUIRED");
                writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                writeErrorArray(writer, fieldName, fieldLabel);

            }

            //循环每个子节点
            List attrList = field.getChildren();
            for (int i = 0; i < attrList.size(); i++) {
                XMLElement attr = (XMLElement) attrList.get(i);
                //如果是格式定义节点则调用 writeFormatJs
                if (attr.getName().toLowerCase().equals("format")) {
                    writeFormatJs(writer, attr, fieldName, fieldLabel);
                }
                //如果是depend条件定义节点则调用 writeDependJs
                if (attr.getName().toLowerCase().equals("depend")) {
                    writeDependRejectJs(writer, attr, fieldName, fieldLabel);
                }
                //如果是Reject条件定义节点则调用 writeRejectJs
                if (attr.getName().toLowerCase().equals("reject")) {
                    writeDependRejectJs(writer, attr, fieldName, fieldLabel);
                }
            }

        }

        catch (Exception e) {
            Log.error("Error processing field: " + fieldName, e);
        }
    }

    private void writeFormatJs(BufferedWriter writer,
                               XMLElement format,
                               String fieldName,
                               String fieldLabel) throws
            NTBSystemException {
        String formatType = format.getAttribute("type");
    	//add by linrui 20180512
    	fieldChkErrMsg = RBFactory.getInstance(
        "app.cib.resource.common.field_check",locale);
        try {

            String pattern = Utils.null2Empty(format.getAttribute("pattern"));
            String fieldMaxlen = format.getAttribute("maxlen");
            String fieldMinlen = format.getAttribute("minlen");
            String fieldFixlen = format.getAttribute("fixlen");
            String prefix = format.getAttribute("prefix");
            String suffix = format.getAttribute("suffix");

            if (formatType != null) {
                if (formatType.equals("string")) {

                    writeln(writer,
                            "if(debugMode==true)alert('" + fieldName +
                            ": format_string');");
                    writeln(writer,
                            "if(!validateString(form.elements['" + fieldName +
                            "'], '" + pattern + "')) {");
                    String errMsg = fieldChkErrMsg.getString("FORMAT_STRING");
                    writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                    writeErrorArray(writer, fieldName, fieldLabel);
                }

                else if (formatType.equals("numonly")) {

                    writeln(writer,
                            "if(debugMode==true)alert('" + fieldName +
                            ": format_numonly');");
                    writeln(writer,
                            "if(!validateNUMONLY(form.elements['" + fieldName +
                            "'])) {");
                    String errMsg = fieldChkErrMsg.getString("FORMAT_NUMONLY");
                    writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                    writeErrorArray(writer, fieldName, fieldLabel);
                }

                else if (formatType.equals("engonly")) {

                    writeln(writer,
                            "if(debugMode==true)alert('" + fieldName +
                            ": format_engonly');");
                    writeln(writer,
                            "if(!validateENGONLY(form.elements['" + fieldName +
                            "'])) {");
                    String errMsg = fieldChkErrMsg.getString("FORMAT_ENGONLY");
                    writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                    writeErrorArray(writer, fieldName, fieldLabel);
                }

                else if (formatType.equals("engnum")) {

                    writeln(writer,
                            "if(debugMode==true)alert('" + fieldName +
                            ": format_engnum');");
                    writeln(writer,
                            "if(!validateENGNUM(form.elements['" + fieldName +
                            "'])) {");
                    String errMsg = fieldChkErrMsg.getString("FORMAT_ENGNUM");
                    writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                    writeErrorArray(writer, fieldName, fieldLabel);
                }

                else if (formatType.equals("date")) {
                    writeln(writer,
                            "if(debugMode==true)alert('" + fieldName +
                            ": format_date');");
                    writeln(writer,
                            "if(!validateDate(form.elements['" + fieldName +
                            "'], '" + pattern + "')) {");
                    String errMsg = fieldChkErrMsg.getString("FORMAT_DATE");
                    if (pattern.equals("")) {
                        pattern = " " + defalutDatePattern.toUpperCase();
                    }
                    errMsg = Utils.replaceStr(errMsg, "[PATTERN]", pattern);
                    writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                    writeErrorArray(writer, fieldName, fieldLabel);
                }

                else if (formatType.equals("time")) {
                    if (pattern == null) {
                        pattern = "HHmmss";
                    }
                    writeln(writer,
                            "if(debugMode==true)alert('" + fieldName +
                            ": format_time');");
                    writeln(writer,
                            "if(!validateTime(form.elements['" + fieldName +
                            "'], '" + pattern + "')) {");
                    String errMsg = fieldChkErrMsg.getString("FORMAT_TIME");
                    errMsg = Utils.replaceStr(errMsg, "[PATTERN]", pattern);
                    writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                    writeErrorArray(writer, fieldName, fieldLabel);
                }

                else if (formatType.equals("amount")) {
                    if (prefix == null) {
                        prefix = "13";
                    }
                    if (suffix == null) {
                        suffix = "2";
                    }
                    writeln(writer,
                            "if(debugMode==true)alert('" + fieldName +
                            ": format_amount');");
//                    writeln(writer,
//                            "if(!validateAmount(form.elements['" + fieldName +
//                            "'], '" + prefix + "', '" + suffix + "')) {");
                    //mod by linrui for mul-language
                    writeln(writer,
                            "if(!validateAmountByLang(form.elements['" + fieldName +
                            "'], '" + prefix + "', '" + suffix + "', '" + locale + "')) {");
                    String errMsg = fieldChkErrMsg.getString("FORMAT_AMOUNT");
                    errMsg = Utils.replaceStr(errMsg, "[PREFIX]", prefix);
                    errMsg = Utils.replaceStr(errMsg, "[SUFFIX]", suffix);
                    writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                    writeErrorArray(writer, fieldName, fieldLabel);
                }
                //add by linrui for change translation amount
                else if(formatType.equals("transferamount")){
                	if (prefix == null) {
                        prefix = "13";
                    }
                    if (suffix == null) {
                        suffix = "2";
                    }
                    writeln(writer,
                            "if(debugMode==true)alert('" + fieldName +
                            ": format_amount');");
                    writeln(writer,
                            "if(!validateAmountByLang(form.elements['" + fieldName +
                            "'], '" + prefix + "', '" + suffix + "', '" + locale + "')) {");
                 
                	String errMsg = fieldChkErrMsg.getString("FORMAT_AUTOPAY");
                    writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                    writeErrorArray(writer, fieldName, fieldLabel);
                }
                //end
                else if (formatType.equals("rate")) {
                    if (prefix == null) {
                        prefix = "2";
                    }
                    if (suffix == null) {
                        suffix = "6";
                    }
                    writeln(writer,
                            "if(debugMode==true)alert('" + fieldName +
                            ": format_rate');");
                    writeln(writer,
                            "if(!validateAmount(form.elements['" + fieldName +
                            "'], '" + prefix + "', '" + suffix + "')) {");
                    String errMsg = fieldChkErrMsg.getString("FORMAT_RATE");
                    errMsg = Utils.replaceStr(errMsg, "[PREFIX]", prefix);
                    errMsg = Utils.replaceStr(errMsg, "[SUFFIX]", suffix);
                    writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                    writeErrorArray(writer, fieldName, fieldLabel);
                }

                else if (formatType.equals("integer")) {

                    writeln(writer,
                            "if(debugMode==true)alert('" + fieldName +
                            ": format_number');");
                    writeln(writer,
                            "if(!validateInteger(form.elements['" + fieldName +
                            "'])) {");
                    String errMsg = fieldChkErrMsg.getString("FORMAT_NUMBER");
                    writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                    writeErrorArray(writer, fieldName, fieldLabel);
                }

                else if (formatType.equals("email")) {

                    writeln(writer,
                            "if(debugMode==true)alert('" + fieldName +
                            ": format_email');");
                    writeln(writer,
                            "if(!validateEmail(form.elements['" + fieldName +
                            "'], '" + pattern + "')) {");
                    String errMsg = fieldChkErrMsg.getString("FORMAT_EMAIL");
                    writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                    writeErrorArray(writer, fieldName, fieldLabel);
                }
                
                //add by xiao_h CR192 20160715
                else if (formatType.equals("autopay")) {

                    writeln(writer,
                            "if(debugMode==true)alert('" + fieldName +
                            ": format_numonly');");
                    writeln(writer,
                            "if(!validateNUMONLY(form.elements['" + fieldName +
                            "'])) {");
                    String errMsg = fieldChkErrMsg.getString("FORMAT_AUTOPAY");
                    writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                    writeErrorArray(writer, fieldName, fieldLabel);
                }
                //add by linrui 20190704 must conclude english
                else if (formatType.equals("engmust")) {

                    writeln(writer,
                            "if(debugMode==true)alert('" + fieldName +
                            ": format_engmust');");
                    writeln(writer,
                            "if(!validateENGMUST(form.elements['" + fieldName +
                            "'])) {");
                    String errMsg = fieldChkErrMsg.getString("FORMAT_ENGMUST");
                    writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                    writeErrorArray(writer, fieldName, fieldLabel);
                }
                
                //end
                
                else{ //add by wen_chy 20091209
                	writeln(writer,
                            "if(debugMode==true)alert('" + fieldName +
                            ": format_"+formatType+"');");
                    writeln(writer,
                            "if(!validate"+formatType.substring(0, 1).toUpperCase()+formatType.substring(1, formatType.length())+"(form.elements['" + fieldName +
                            "'], '" + pattern + "')) {");
                    String errMsg = fieldChkErrMsg.getString("FORMAT_"+formatType.toUpperCase());
                    writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                    writeErrorArray(writer, fieldName, fieldLabel);
                }
            }
            //判断 maxlen
            if (fieldMaxlen != null) {
                writeln(writer,
                        "if(debugMode==true)alert('" + fieldName +
                        ": maxlen');");
                writeln(writer,
                        "if(!validateMaxLength(form.elements['" + fieldName +
                        "'], " + fieldMaxlen + ")) {");
                String errMsg = fieldChkErrMsg.getString("MAXLEN");
                errMsg = Utils.replaceStr(errMsg, "[MAXLEN]", fieldMaxlen);
                writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                writeErrorArray(writer, fieldName, fieldLabel);
            }

            //判断 minlen
            if (fieldMinlen != null) {
                writeln(writer,
                        "if(debugMode==true)alert('" + fieldName +
                        ": minlen');");
                writeln(writer,
                        "if(!validateMinLength(form.elements['" + fieldName +
                        "'], " + fieldMinlen + ")) {");
                String errMsg = fieldChkErrMsg.getString("MINLEN");
                errMsg = Utils.replaceStr(errMsg, "[MINLEN]", fieldMinlen);
                writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                writeErrorArray(writer, fieldName, fieldLabel);
            }

            //判断 fixlen
            if (fieldFixlen != null) {
                writeln(writer,
                        "if(debugMode==true)alert('" + fieldName +
                        ": fixlen');");
                writeln(writer,
                        "if(!validateFixLength(form.elements['" + fieldName +
                        "'], " + fieldFixlen + ")) {");
                String errMsg = fieldChkErrMsg.getString("FIXLEN");
                errMsg = Utils.replaceStr(errMsg, "[FIXLEN]", fieldFixlen);
                writeln(writer, "errors[errorIndex] = '" + errMsg + "';");
                writeErrorArray(writer, fieldName, fieldLabel);
            }
        }

        catch (Exception e) {
            Log.error("Error processing field: " + fieldName, e);
        }
    }

    private void writeDependRejectJs(BufferedWriter writer,
                                     XMLElement depend_Reject,
                                     String fieldName,
                                     String fieldLabel) throws
            NTBSystemException {
        String type = depend_Reject.getName().toLowerCase();
    	//add by linrui 20180512
    	fieldChkErrMsg = RBFactory.getInstance(
        "app.cib.resource.common.field_check",locale);
        try {
            String prompt = depend_Reject.getAttribute("prompt");
            prompt = rc.getString(prompt);
            String condition = depend_Reject.getText();
            condition = replaceField(condition);

            if (type.equals("reject")) {
                writeln(writer,
                        "if(debugMode==true)alert('" + fieldName +
                        ": reject');");
                writeln(writer,
                        "if(!isDisable(form.elements['" + fieldName + "'])){");
                writeln(writer, "if ( " + condition +
                        ") {");
                writeln(writer, "errors[errorIndex] = '" + prompt + "';");
                writeErrorArray(writer, fieldName, fieldLabel);
                writeln(writer, "}");
            }
            if (type.equals("depend")) {
                writeln(writer,
                        "if(debugMode==true)alert('" + fieldName +
                        ": reject');");
                writeln(writer,
                        "if(!isDisable(form.elements['" + fieldName + "'])){");
                writeln(writer, "if (!( " + condition +
                        ")) {");
                writeln(writer, "errors[errorIndex] = '" + prompt + "';");
                writeErrorArray(writer, fieldName, fieldLabel);
                writeln(writer, "}");
            }

            writer.newLine();
        } catch (Exception e) {
            Log.error("Error processing field: " + fieldName, e);
        }
    }

    private String replaceField(String condiction) {
        if (condiction.indexOf("[") == -1 || condiction.indexOf("]") == -1
        	/*add by linrui 20191008*/|| condiction.indexOf("document") != -1/*add by linrui end*/) {
            return condiction;
        }

        condiction = " " + condiction + " ";
        StringTokenizer token = new StringTokenizer(condiction, "[");
        StringBuffer returnStr = new StringBuffer();
        returnStr.append(token.nextToken());
        while (token.hasMoreTokens()) {
            String divStr = token.nextToken();
            String[] divStrArray = Utils.splitStr(divStr, "]");
            returnStr.append("getFieldValue(form.elements['" + divStrArray[0] +
                             "'])");
            if (divStrArray.length > 1) {
                returnStr.append(divStrArray[1]);
            }
        }
        return returnStr.toString();
    }

    private void writeErrorArray(BufferedWriter writer,
                                 String fieldName, String fieldLabel) throws
            Exception {
    	//add by linrui 20180512
    	fieldChkErrMsg = RBFactory.getInstance(
        "app.cib.resource.common.field_check",locale);
        writeln(writer, "errorNameList[errorIndex] = '" + fieldName + "';");
        writeln(writer, "errorLabelList[errorIndex] = '" + fieldLabel + "';");
        writeln(writer, "errorLayerList[errorIndex] = '-1';");
        writeln(writer, "errorArrayIndex[errorIndex] = 0;");
        writeln(writer, "errorIndex++;");
        writeln(writer, "}");
    }

    private synchronized void generateJsFile(String jsFileName,
                                             XMLElement docElement) throws
            NTBSystemException {
        try {
            StringWriter strWriter = new StringWriter();
            BufferedWriter writer = new BufferedWriter(strWriter);

            List groupList = docElement.getChildren();
            for (int i = 0; i < groupList.size(); i++) {
                XMLElement group = (XMLElement) groupList.get(i);
                writeGroupJs(writer, group);
            }
            writer.flush();
            writer.close();
            String jsContent = strWriter.toString();

            FileOutputStream fos = new FileOutputStream(jsFileName);
            DataOutputStream dos = new DataOutputStream(fos);

            dos.write(jsContent.getBytes("UTF-8"));
            dos.flush();
            dos.close();
            fos.close();

        } catch (Exception e) {
            Log.error("Error generating Javascript File : " + jsFileName, e);
        }
    }

    public synchronized static void generate(String locale,String xmlName, RBFactory rc) throws
            NTBSystemException {
        /*String xmlFileName = xmlDirName + xmlName + ".xml";
        String jsFileName = jsDirName + xmlName + ".js";
        if (!instances.containsKey(xmlFileName)) {*/
    	String xmlFileName = xmlDirName + xmlName + ".xml";//mod by linrui for mul-language 20180519
        String jsFileName = jsDirName + xmlName + "_" + locale + ".js";
        if (!instances.containsKey(xmlFileName+ "_" + locale)) {
            try {
                File jsFile = new File(jsFileName);
                if (jsFile.exists()) {
                    jsFile.delete();
                }
                XMLParser xmlParser = XMLFactory.getParser();
                xmlParser.setInput(xmlFileName);
                xmlParser.unMarshal();
                XMLElement docElement = xmlParser.getRootElement();
                FieldChkXMLFactory newInstance = new FieldChkXMLFactory();
                newInstance.xml = docElement;
                newInstance.rc = rc;
                newInstance.locale = locale;//add by linrui for mul-language
                newInstance.generateJsFile(jsFileName, docElement);
//                instances.put(xmlFileName, newInstance);
                instances.put(xmlFileName+ "_" + locale, newInstance);//add by linrui for mul-language
            } catch (Exception e) {
                Log.error(
                        "XML file " + xmlFileName + " initialzation error",
                        e);
                throw new NTBSystemException(560);
            }
        }
    }
    
    private void writeln(BufferedWriter writer, String line) throws
            Exception {
        String newline = line + "\n";
        writer.write(newline);
    }

    /**
     * 此处插入方法描述。
     * 创建日期：(2003-9-1 1:11:09)
     * @param args java.lang.String[]
     */
    public static void main(String[] args) {
        try {
            FieldChkXMLFactory.generate("zh_HK","login", RBFactory.getInstance("temp"));//mod by linrui for test
        } catch (NTBException e) {
            System.out.println("Parse Error");
        }
    }

}
