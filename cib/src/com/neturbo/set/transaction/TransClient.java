package com.neturbo.set.transaction;

import java.io.*;
import java.net.*;
import java.util.*;

import com.neturbo.set.core.*;
import com.neturbo.set.exception.*;
import com.neturbo.set.xml.*;

public class TransClient {
    public TransClient() {
    }

    private int receiveBufferSize = 8196;
    private int sendBufferSize = 8196;
    private String serviceIP = "127.0.0.1";
    private int servicePort = 5001;
    private int readLength;
    private boolean succeed = false;

    // 是否結束標誌
    private boolean isContinue = true;
    private int headerLength = 0;


    protected String serviceName;
    protected String transCode;

    // 根据主机不同初始化
    public TransClient(String newServiceName, String newTransCode) throws
            NTBException {
        serviceName = newServiceName;
        transCode = newTransCode;
        XMLElement xmlRoot = TransXMLFactory.getService(serviceName);

        // 从文件中读出 IP 和 Port
        try {
            XMLElement serviceNode = xmlRoot.findNodeByName("service");
            serviceIP = serviceNode.getAttribute("host");
            servicePort = Integer.parseInt(serviceNode.getAttribute("port"));
            sendBufferSize = Integer.parseInt(serviceNode
                                              .getAttribute("sendbuffer"));
            receiveBufferSize = Integer.parseInt(serviceNode
                                                 .getAttribute("receivebuffer"));
            XMLElement headerFromHost = serviceNode.findNodeByName(
                    "header-from-host");
            headerLength = Integer.parseInt(headerFromHost
                                            .getAttribute("length"));
        } catch (Exception ex) {
            Log.error("Error parsing XML Definitions for transation service",
                      ex);
            throw new NTBException("err.host.XmlError");
        }

    }

    public TransClient(String newTransCode) throws NTBException {
        this(Config.getProperty("DefaultTransService"), newTransCode);
    }

    public Map doTransaction(Map formatData, PacketHandler handler) throws
            NTBException {
        PacketFormater formater = new PacketFormater(serviceName, transCode,
                sendBufferSize, PacketFormater.CLIENT_FORMAT);
        byte sendBytes[] = formater.format(formatData);
//        Log.debug(new String(Encoding.decode(sendBytes, sendBytes.length,
//                                             Encoding.EncodingEBCDIC)));
        byte receiveBytes[] = sendAndReceive(sendBytes);
        Log.info("hostReturn, receiveBytes.length(): " + (receiveBytes==null?"0":receiveBytes.length));
//        String receiveData = Encoding.decode(receiveBytes, receiveBytes.length, Encoding.EncodingEBCDIC);
//        Log.debug("ReceiveData(" + receiveBytes.length + "): " + receiveData);
//        Log.debug(Utils.bytes2HexStr(receiveData.getBytes()));

        PacketParser parser = new PacketParser(serviceName, transCode,
                                               receiveBytes,
                                               receiveBytes.length, handler,
                                               PacketParser.CLIENT_PARSE);
        Map retData = parser.parse();
        succeed = parser.isSucceed();
        return retData;
    }

    // 发送与接受报文
    public byte[] sendAndReceive(byte formatBytes[]) throws NTBException {

        //如果发包过大,则报错
        if (formatBytes.length > sendBufferSize) {
            Log.error("Send bytes length larger then format buffer size");
            throw new NTBException("err.host.NoEnoughBuffer");
        }

        //初始化接口和收包缓冲长度
        byte parseBytes[] = new byte[receiveBufferSize];
        byte returnBytes[];
        Socket socket = null;
        try {
            try {
                socket = new Socket(serviceIP, servicePort);
                //设置超时为 120 秒
                socket.setSoTimeout(120000);
            } catch (Exception ex) {
                Log.error(" Socket construction failed for IP[" + serviceIP +
                          "] port[" + servicePort + "]", ex);
                throw new NTBException("err.host.connectionError");
            }

            try {
                BufferedInputStream bufferedinputstream = new
                        BufferedInputStream(
                                socket.getInputStream(), receiveBufferSize);
                //发包
                BufferedOutputStream bufferedoutputstream = new
                        BufferedOutputStream(
                                socket.getOutputStream(), sendBufferSize);
                bufferedoutputstream.write(formatBytes);
                bufferedoutputstream.flush();
                //收包
                //读第一个包
                int readLength = bufferedinputstream.read(parseBytes);
                if (readLength <= 0) {
                    throw new NTBException("err.host.ErrorReadingSocket");
                }
                //如果没有达到总长度则再读
                while (readLength < headerLength) {
                    readLength +=
                            bufferedinputstream.read(parseBytes, readLength,
                            receiveBufferSize - readLength);
                }

                //继续读到完成为止
                int tempReadLength = readLength;
                while (tempReadLength > 0) {
                    tempReadLength =
                            bufferedinputstream.read(parseBytes, readLength,
                            receiveBufferSize - readLength);
                    // Jet added, return -1 when the stream reach end
                    if(tempReadLength >= 0){
                    	readLength += tempReadLength;
                    }
                }

                returnBytes = new byte[readLength];
                System.arraycopy(parseBytes, 0, returnBytes, 0, readLength);
            } catch (Exception exception) {
                Log.error("Exception in socketRead()", exception);
                throw new NTBException("err.host.ErrorReadingSocket");
            }
        } catch (NTBException ex) {
            throw ex;
        } finally { //关闭接口
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception _ex) {}
        }

        return returnBytes;
    }

    public boolean isSucceed() {
        return succeed;
    }

    //add by hjs 2006-11-16
    public void setIsSucceed(boolean succeed) {
        this.succeed = succeed;
    }
}
