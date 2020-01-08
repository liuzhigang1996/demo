package com.neturbo.set.transaction;

import java.text.*;
import java.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.xml.*;
import com.neturbo.set.utils.*;

public class PacketFormatBuffer {

  private int formatPos = 0;
  private int formatFieldCount = 0;
  private byte[] formatBytes;

  public PacketFormatBuffer(int bufSize) {
    formatBytes = new byte[bufSize];
  }

  public int getFormatPos() {
    return formatPos;
  }

  public void setFormatPos(int formatPos) {
    this.formatPos = formatPos;
  }

  public void formatForward(int forwardCount) {
    this.formatPos += forwardCount;
  }

  public void formatSkip(int skipCount) {
    this.formatPos += skipCount;
  }

  public byte[] getFormatBytes() {
    return formatBytes;
  }

  public int getFormatFieldCount() {
    return formatFieldCount;
  }

  public void setFormatFieldCount(int formatFieldCount) {
    this.formatFieldCount = formatFieldCount;
  }

  public void format(HashMap formatData,
                     XMLElement formatXML) {
    //获得子节点列表
    List fields = formatXML.getChildren();

    //循环子节点列表
    for (int i = 0; i < fields.size(); i++) {

      //解析的域数目加一
      formatFieldCount++;

      //获得子节点域
      XMLElement fieldXML = (XMLElement) fields.get(i);
      //域类型
      String type = fieldXML.getName();
      //域名称
      String name = Utils.null2Empty(fieldXML.getAttribute("name"));
      //如果是循环域，则可以包含记录数
      int count = Utils.nullEmpty2Zero(fieldXML.getAttribute("count"));

      //如果是普通域
      if (type.equals("field")) {
        //缺省值
        String defaultValue = Utils.null2Empty(fieldXML.getAttribute(
            "default_value"));
        //该域的格式
        XMLElement fieldFormatXML = fieldXML.getChildByName("format");
        int skip = Utils.nullEmpty2Zero(fieldFormatXML.getAttribute("skip"));
        int skip_after = Utils.nullEmpty2Zero(fieldFormatXML.getAttribute(
            "skip_after"));
        int length = Utils.nullEmpty2Zero(fieldFormatXML.getAttribute("length"));
        int decimal = Utils.nullEmpty2Zero(fieldFormatXML.getAttribute(
            "decimal"));
        char pic = Utils.null2Empty(fieldFormatXML.getAttribute("pic")).charAt(
            0);
        String delimiter = Utils.null2Empty(fieldFormatXML.getAttribute(
            "delimiter"));
        boolean signed = Utils.null2Empty(fieldFormatXML.getAttribute("signed")).
            equals("yes");
        boolean hidden_decimal = Utils.null2Empty(fieldFormatXML.getAttribute(
            "hidden_decimal")).equals("yes");

        //从transData中获得该域值，如果无则使用缺省值
        Object valueObj = formatData.get(name);
        String value = "";
        if (valueObj != null) {
          value = valueObj.toString();
        }
        else {
          value = defaultValue;
        }
        //format前skip
        formatSkip(skip);
        try {
          //如果为定长域，无分隔符
          if (delimiter.length() == 0) {
            formatForward(Packet_Field.formatFixlenField(
                formatBytes,
                pic,
                value,
                formatPos,
                length,
                signed,
                hidden_decimal,
                decimal));
          }
          //如果由分隔符分开
          else {
            formatForward(Packet_Field.formatDelimiterField(
                formatBytes,
                pic,
                value,
                formatPos,
                delimiter,
                signed,
                hidden_decimal,
                decimal));
          }
        }
        catch (Exception e) {
          Log.error(
                    "Format Error in format() of PacketFormatHeader", e);
        }
        //后跳跃
        formatSkip(skip_after);
      }

      //如果是循环域
      if (type.equals("loop")) {
        ArrayList loopRec = (ArrayList) formatData.get(name);
        if (count == 0) {
          count = loopRec.size();
        }
        for (i = 0; i < count; i++) {
          format( (HashMap) loopRec.get(i), fieldXML);
        }
      }
    }
  }

}
