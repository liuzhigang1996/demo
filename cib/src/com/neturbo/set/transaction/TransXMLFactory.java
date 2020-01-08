package com.neturbo.set.transaction;

import java.util.*;
import javax.servlet.http.*;
import com.neturbo.set.xml.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.core.*;
import com.neturbo.set.exception.*;

public class TransXMLFactory {

  private static HashMap instances =
      new HashMap(20);
  private static String XMLDirName = Config.getProperty("TransXMLDir")+ "/";
  /**
   *    ˽�еĹ����ӱ�֤����޷�ֱ�ӽ�����ʵ����
   */
  private TransXMLFactory() {
  }

  public static void setTransXMLDir(String newTransXMLDir){
    XMLDirName = newTransXMLDir + "/";
  }

  public static String getTransXMLDir(){
    return XMLDirName;
  }

  /**
   *    ��������������һ������ָ�����ڲ�״̬��ʵ��
   *    serviceName -- ��ͬ�ı��ĸ�ʽΪ��ͬ��Service�����罻�еĶ�˽��Թ�ϵͳ
   *    transCode -- ������
   */
  public synchronized static XMLElement
      getService(String serviceName) throws NTBSystemException {
    String XMLFileName = XMLDirName + serviceName + ".xml";
    if (instances.containsKey(serviceName)) {
      return (XMLElement) instances.get(serviceName);
    }
    else {
      try {
        XMLParser xmlParser = XMLFactory.getParser();
       xmlParser.setInput(XMLFileName);

        xmlParser.unMarshal();
        XMLElement docElement = xmlParser.getRootElement();
        instances.put(serviceName, docElement);
        return docElement;
      }
      catch (Exception e) {
        Log.error(
                  "XML file " + XMLFileName + " initialzation error",
                  e);
        throw new NTBSystemException(560);
      }
    }
  }

  public synchronized static XMLElement
      getTransation(String serviceName, String transCode) throws NTBSystemException {
    transCode = serviceName + "_" + transCode;
    String XMLFileName  = XMLDirName + transCode + ".xml";
    if (instances.containsKey(transCode)) {
      return (XMLElement) instances.get(transCode);
    }
    else {
      try {
        XMLParser xmlParser = XMLFactory.getParser();
        xmlParser.setInput(XMLFileName);
        xmlParser.unMarshal();
        XMLElement docElement = xmlParser.getRootElement();
        instances.put(transCode, docElement);
        return docElement;
      }
      catch (Exception e) {
        Log.error(
            "XML file " + XMLFileName + " initialzation error",
            e);
        throw new NTBSystemException(560);
      }
    }
  }
}
