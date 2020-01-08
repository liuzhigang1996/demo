package com.neturbo.set.utils;

import java.io.*;
import java.util.*;
import com.neturbo.set.core.*;

public class TemplateFactory {

	private String fileName = "";
        private String fileContent;
	//����template
	private static HashMap instances = new HashMap(20);
	/**
	 *    ˽�еĹ����ӱ�֤����޷�ֱ�ӽ�����ʵ����
	 */
	private TemplateFactory(String fileName) {

		try {
                  String tempFileName = Config.getProperty("TemplateDir") + "/" + fileName;
                  File tempFile = new File(tempFileName);
                  FileInputStream tempFileStream = new FileInputStream(tempFile);
                  BufferedInputStream bin = new BufferedInputStream(tempFileStream);

                  byte[] buffer = new byte[BUFFER_SIZE];

                  int readCount;
                  while ((readCount = bin.read(buffer)) > 0) {
                          fileContent = new String(buffer, 0, readCount);
                          Log.info("Template " +fileName +" read, " + readCount + "bytes read");
                  }

                  buffer = null;
                  instances.put(fileName, this);
		} catch (Exception e) {
			Log.error("Template not exist (" + fileName + ")", e);
		}
	}

	/**
	 *    ˽�еĹ����ӱ�֤����޷�ֱ�ӽ�����ʵ����
	 */
	private TemplateFactory() {
	}

	/**
	 *    ��������������һ������ָ�����ڲ�״̬��ʵ��
	 */
	public synchronized static TemplateFactory getInstance(String fileName) {
		fileName = fileName.trim();
		if (instances.containsKey(fileName)) {
			return (TemplateFactory) instances.get(fileName);
		} else {
			return new TemplateFactory(fileName);
		}
	}

	//���ָ��ֵ
	public String getContent() {
		return fileContent;
	}

        public String getContent(Map parametersMap){
            String content = replaceWithParameters(fileContent, parametersMap);
            return content;
        }

        private String replaceWithParameters(String content, Map parametersMap){
            Set keySet = parametersMap.keySet();

            for (Iterator it = keySet.iterator(); it.hasNext(); ) {
                String fieldName = (String) it.next();
                Object value = parametersMap.get(fieldName);
                content = Utils.replaceStr(content, "<%" +fieldName+ "%>", Utils.null2Empty(value));
            }
            return content;
        }

        //���ָ��ֵ
        public String[] getMailContent() {
          String[] mailContent = null;
          try{
            mailContent = Utils.splitStr(fileContent,"</title>");
            String title[] = Utils.splitStr(mailContent[0],"<title>");
            mailContent[0] = title[0];
          }catch(Exception e){

          }
          return mailContent;
        }

        //���ָ��ֵ
        public String[] getMailContent(Map parametersMap) {
          String[] mailContent = null;
          try{
            mailContent = Utils.splitStr(fileContent,"</title>");
            String title[] = Utils.splitStr(mailContent[0],"<title>");
            mailContent[0] = title[0];
            mailContent[0] = replaceWithParameters(mailContent[0] , parametersMap);;
            mailContent[1] = replaceWithParameters(mailContent[1] , parametersMap);;
          }catch(Exception e){

          }
          return mailContent;
        }
        /**
         * IO��������С
         */
        private static final int BUFFER_SIZE = 36 * 1024;

}
