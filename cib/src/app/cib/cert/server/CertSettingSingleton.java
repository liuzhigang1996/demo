package app.cib.cert.server;

import java.util.*;
import java.io.*;
import java.math.*;
import java.security.*;
import java.security.cert.*;

import app.cib.cert.utils.CertUtils;
import com.neturbo.set.xml.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 *
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CertSettingSingleton {

    private static HashMap instances;

    private static XMLElement settingXMLRoot = Config
                                               .getConfigXML("certConfig");

    private static String appRoot;

    private static int errorCode = 0;

    private X509Certificate certChain[] = null;

    private X509CRL crl = null;

    private CertSettingSingleton() {
    }

    // ����WebApp��ʵĿ¼������Ѱ��֤��洢Ŀ¼
    public static void setAppRoot(String _appRoot) {
        appRoot = _appRoot;
    }

    // ��һ��settingXML��ʼ��֤��У������
    private static int initCertSetting(XMLElement settingXML) {
        try {
            // ������Ӧ���Ƶķ�֤��������
            if (settingXML != null) {
                // ��ʼ��
                CertSettingSingleton certSettingInstance = new
                        CertSettingSingleton();

                // ����֤�������ļ���
                XMLElement issuerXML = settingXML.findNodeByName("issuer");
                ArrayList certChainList = new ArrayList();
                while (issuerXML != null) {
                    String certFileName = issuerXML.getAttribute("file");
                    certFileName = Utils.replaceStr(certFileName,
                            "${app.root}", appRoot);
                    Log.info("certFileName = " + certFileName);
                    // ����֤���ļ����������б�
                    if (certFileName != null) {
                        X509Certificate certObj = getCertFromFile(certFileName);
                        // У��֤������ʱ��
                        int retVal;
                        if ((retVal = verifyDate(certObj)) != 0) {
                            return retVal;
                        }
                        certChainList.add(certObj);
                    } else {
                        Log.error("Certification file name can not be null!");
                        return -1002;
                    }
                    issuerXML = issuerXML.getChildByName("issuer");
                }

                // ת���б�Ϊ����
                X509Certificate[] certChainNow = new X509Certificate[
                                                 certChainList
                                                 .size()];
                for (int ic = 0; ic < certChainList.size(); ic++) {
                    certChainNow[ic] = (X509Certificate) certChainList.get(ic);
                }
                certSettingInstance.certChain = certChainNow;
                Log.info("certChain = " + certChainNow);

                // ����֤�������ļ���
                XMLElement crlXML = settingXML.findNodeByName("crl");
                String crlFileName = crlXML.getAttribute("file");
                crlFileName = Utils.replaceStr(crlFileName, "${app.root}",
                                               appRoot);

                Log.info("crlFileName = " + crlFileName);

                // ����֤���ļ����������б�
                if (crlFileName != null) {
                    try {
                        X509CRL crlNow = getCRLFromFile(crlFileName);
                        certSettingInstance.crl = crlNow;
                    } catch (Exception e) {
                        Log.error("Error reading CRL file : " + crlFileName, e);
                    }
                }

                String key = certChainNow[0].getSubjectDN().toString();
                instances.put(key, certSettingInstance);
            } else {
                Log.error(" Certification file name can not be null!");
                return -1002;
            }
        } catch (FileNotFoundException _ex) {
            Log.error(" CertFile not found!", _ex);
            return -1003;
        } catch (IOException _ex) {
            Log.error("Read certfile  I/O Exception!", _ex);
            return -1004;
        } catch (CertificateException _ex) {
            Log.error("Read Cert Failed!", _ex);
            return -1005;
        } catch (CRLException _ex) {
            Log.error("CRL Exception!", _ex);
            return -1006;
        } catch (Exception _ex) {
            Log.error("Get CA or Root Failed!", _ex);
            return -1001;
        }
        return 0;
    }

    // ���������ļ��������е�֤��У������
    private static int init() {

        // ���δ���������ļ� cert_settings.xml �����xml�ļ�����
        try {
            if (settingXMLRoot == null) {
                settingXMLRoot = Config.getConfigXML("certConfig");
            }
        } catch (Exception _ex) {
            Log.error("Parse setting XML failed!", _ex);
            return -1001;
        }

        // �����Ŀ�ľ���·��
        appRoot = Config.getAppRoot();
        // ѭ��ÿ�� settingXML������ initCertSetting ����֤����CRL�ļ�
        List settingXMLs = settingXMLRoot.getChildren();
        instances = new HashMap(settingXMLs.size());
        for (int is = 0; is < settingXMLs.size(); is++) {
            XMLElement settingXML = (XMLElement) settingXMLs.get(is);
            int retVal = initCertSetting(settingXML);
            if (retVal != 0) {
                return retVal;
            }
        }

        // ��������
        return 0;
    }

    // ���ļ��ж�ȡCRL
    private static X509CRL getCRLFromFile(String s) throws Exception {
        if (!s.equals("")) {
            FileInputStream fileinputstream = new FileInputStream(s);
            CertificateFactory certificatefactory = CertificateFactory
                    .getInstance("X.509");
            X509CRL x509crl = (X509CRL) certificatefactory
                              .generateCRL(fileinputstream);
            fileinputstream.close();
            return x509crl;
        } else {
            return null;
        }
    }

    // ���ļ��ж�ȡ֤��
    private static X509Certificate getCertFromFile(String s) throws Exception {
        FileInputStream fileinputstream = new FileInputStream(s);
        CertificateFactory certificatefactory = CertificateFactory
                                                .getInstance("X.509");
        X509Certificate x509certificate = (X509Certificate) certificatefactory
                                          .generateCertificate(fileinputstream);
        fileinputstream.close();
        return x509certificate;
    }

    // ���µ���CRL
    public int reLoadCRL(String s) {
        X509CRL x509crl = null;
        try {
            x509crl = getCRLFromFile(s);
        } catch (FileNotFoundException _ex) {
            Log.error("CertFile not found!", _ex);
            return -1003;
        } catch (CRLException _ex) {
            Log.error("CRL Exception!", _ex);
            return -1006;
        } catch (Exception _ex) {
        }

        if (x509crl != null) {
            crl = x509crl;
            return 0;
        } else {
            return -1001;
        }
    }

    // У��֤����
    public int verifyChain(X509Certificate userCert) {
        if (errorCode != 0) {
            return errorCode;
        }
        try {
            X509Certificate certNow = userCert;
            PublicKey pkIssuer = null;
            for (int i = 0; i < certChain.length; i++) {
                X509Certificate certIssuer = certChain[i];
                pkIssuer = certIssuer.getPublicKey();
                if (pkIssuer != null) {
                    if (!certNow.getIssuerDN()
                        .equals(certIssuer.getSubjectDN())) {
                        Log.error("*** certUser.getIssuerDN() is "
                                  + certNow.getIssuerDN());
                        Log.error("*** certCA.getSubjectDN()) is "
                                  + certIssuer.getSubjectDN());
                        Log.error("Verify CA chain failed!");
                        return -1020;
                    }
                    try {
                        certNow.verify(pkIssuer, "SunJSSE");
                    } catch (Exception e) {
                        certNow.verify(pkIssuer, "IBMJCE");
                    }
                    certNow = certChain[i];
                }
            }
        } catch (NoSuchAlgorithmException _ex) {
            Log.error(" VerifyChain() No such Algorithm!", _ex);
            return -1017;
        } catch (InvalidKeyException _ex) {
            Log.error("VerifyChain() PublicKey Invaild!", _ex);
            return -1018;
        } catch (NoSuchProviderException _ex) {
            Log.error("VerifyChain() No such Provider!", _ex);
            return -1019;
        } catch (SignatureException _ex) {
            Log.error("VerifyChain() Sign not Match!", _ex);
            return -1020;
        } catch (Exception _ex) {
            Log.error("VerifyChain() Cert Verify Exception!", _ex);
            return -1020;
        }

        return 0;
    }

    // У��ʱ��
    public static int verifyDate(X509Certificate x509certificate) {
        if (x509certificate == null) {
            Log.error("User Certificate is null!");
            return -1010;
        }
        try {
            x509certificate.checkValidity();
        } catch (CertificateExpiredException _ex) {
            Log.error("Certificate Expired!");
            return -1011;
        } catch (CertificateNotYetValidException _ex) {
            Log.error("Certificate Not YetValid!");
            return -1012;
        }
        return 0;
    }

    // У��CRL
    public int verifyCRL(X509Certificate x509certificate) {
        if (errorCode != 0) {
            return errorCode;
        }
        if (crl == null) {
            Log.info("VerifyCRL() No CRL!");
            return 0;
        }
        if (crl.isRevoked(x509certificate)) {
            Log.error("VerifyCRL() This Cert is Revoked! ");
            return -1014;
        } else {
            return 0;
        }
    }

    // ��������������һ������ָ�����ڲ�״̬��ʵ��
    public synchronized static CertSettingSingleton getInstance(String issuerDN) {
        if (instances == null) {
            errorCode = init();
        }
        //Log for debug
        Log.debug("*** check cert *** = " + issuerDN);
        if (instances.containsKey(issuerDN)) {
            //Log for debug
            Set keySet = instances.keySet();
            for (Iterator it = keySet.iterator(); it.hasNext(); ) {
                String keyName = (String) it.next();
                Log.debug("*** cert chain key *** = " + keyName);
            }
            return (CertSettingSingleton) instances.get(issuerDN);
        } else {
            errorCode = init();
            return (CertSettingSingleton) instances.get(issuerDN);
        }

    }

}
