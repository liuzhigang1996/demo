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
    //����ӽڵ��б�
    List fields = formatXML.getChildren();

    //ѭ���ӽڵ��б�
    for (int i = 0; i < fields.size(); i++) {

      //����������Ŀ��һ
      formatFieldCount++;

      //����ӽڵ���
      XMLElement fieldXML = (XMLElement) fields.get(i);
      //������
      String type = fieldXML.getName();
      //������
      String name = Utils.null2Empty(fieldXML.getAttribute("name"));
      //�����ѭ��������԰�����¼��
      int count = Utils.nullEmpty2Zero(fieldXML.getAttribute("count"));

      //�������ͨ��
      if (type.equals("field")) {
        //ȱʡֵ
        String defaultValue = Utils.null2Empty(fieldXML.getAttribute(
            "default_value"));
        //����ĸ�ʽ
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

        //��transData�л�ø���ֵ���������ʹ��ȱʡֵ
        Object valueObj = formatData.get(name);
        String value = "";
        if (valueObj != null) {
          value = valueObj.toString();
        }
        else {
          value = defaultValue;
        }
        //formatǰskip
        formatSkip(skip);
        try {
          //���Ϊ�������޷ָ���
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
          //����ɷָ����ֿ�
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
        //����Ծ
        formatSkip(skip_after);
      }

      //�����ѭ����
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
