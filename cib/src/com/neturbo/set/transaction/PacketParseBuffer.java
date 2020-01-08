package com.neturbo.set.transaction;

import java.text.*;
import java.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.xml.*;
import com.neturbo.set.utils.*;

public class PacketParseBuffer {

  private int parsePos=0;
  private byte[] parseBytes;
  private int packetLength;

  public PacketParseBuffer(byte[] parseBytes) {
    this.parseBytes = parseBytes;
    packetLength = parseBytes.length;
  }

  public int getParsePos() {
    return parsePos;
  }

  public void setParsePos(int parsePos) {
    this.parsePos = parsePos;
  }

  public void parseForward(int forwardCount) {
    this.parsePos += forwardCount;
  }

  public void parseSkip(int skipCount) {
    this.parsePos += skipCount;
  }

  public byte[] getParseBytes() {
    return parseBytes;
  }

  public int getPacketLength() {
    return packetLength;
  }

  public void setPacketLength(int packetLength) {
    this.packetLength = packetLength;
  }

  public boolean isParseFinish(){
    return parsePos >= packetLength;
  }

  public void parse(HashMap parseData, XMLElement parseXML) {

    //获得子节点列表
    List fields = parseXML.getChildren();

    //循环子节点列表
    for (int i = 0; i < fields.size(); i++) {
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
        XMLElement formatXML = fieldXML.getChildByName("format");
        int skip = Utils.nullEmpty2Zero(formatXML.getAttribute("skip"));
        int skip_after = Utils.nullEmpty2Zero(formatXML.getAttribute(
            "skip_after"));
        int length = Utils.nullEmpty2Zero(formatXML.getAttribute("length"));
        int decimal = Utils.nullEmpty2Zero(formatXML.getAttribute("decimal"));
        char pic = Utils.null2Empty(formatXML.getAttribute("pic")).charAt(0);
        String delimiter = Utils.null2Empty(formatXML.getAttribute("delimiter"));
        boolean signed = Utils.null2Empty(formatXML.getAttribute("signed")).
            equals("yes");
        boolean hidden_decimal = Utils.null2Empty(formatXML.getAttribute(
            "hidden_decimal")).equals("yes");

        //format前skip
        parseSkip(skip);
        if(isParseFinish()){
          return;
        }
        //解析目前的域
        try {
          //如果为定长域，无分隔符
          if (delimiter.length() == 0) {
            parseSkip(Packet_Field.parseFixlenField(
                parseBytes,
                parseData,
                name,
                pic,
                parsePos,
                length,
                signed,
                hidden_decimal,
                decimal));


          }
          //如果由分隔符分开
          else {
            parseForward(Packet_Field.parseDelimiterField(
                parseBytes,
                parseData,
               name,
               pic,
                parsePos,
                delimiter,
                signed,
                hidden_decimal,
                decimal));
          }
        }
        catch (Exception e) {
          Log.error( "Format Error in format() of PacketFormatHeader",e);
        }
        if(isParseFinish()){
          return;
        }
        //后跳跃
        parseSkip(skip_after);
        if(isParseFinish()){
          return;
        }
      }

      //如果是循环域
      if (type.equals("loop")) {
        ArrayList loopRec = new ArrayList();
        for (i = 0; i < count; i++) {
          HashMap rec = new HashMap();
          parse(rec, fieldXML);
          loopRec.add(rec);
        }
        parseData.put(name, loopRec);
      }
    }
  }

}
