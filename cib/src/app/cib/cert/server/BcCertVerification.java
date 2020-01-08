package app.cib.cert.server;

import java.io.*;
import java.math.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;

import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERInputStream;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERString;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERConstructedSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.x509.X509CertificateStructure;

import app.cib.cert.utils.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.utils.DateTime;

public class BcCertVerification {

  private CertSettingSingleton certSetting;
  private X509Certificate UserCert;
  private static boolean checkCertFlag = false;
  static {
      String checkCertStr = Utils. null2Empty(Config.getProperty(
              "checkCert")).toUpperCase();
      checkCertFlag = !checkCertStr.equals("NO");
  }

  public BcCertVerification() {
    UserCert = null;
  }

  public int verifyChain(X509Certificate userCert) {
    if (certSetting == null) {
        //获得证书发行者DN
      String issuerDN = userCert.getIssuerDN().toString();
      certSetting = CertSettingSingleton.getInstance(issuerDN);
    }
    if(certSetting == null){
      Log.error("Get CA or Root Failed, Not proper issuer setting!");
      return -1001;
    }
    return certSetting.verifyChain(userCert);
  }

  public int verifyDate(X509Certificate userCert) {
    if (certSetting == null) {
        //获得证书发行者DN
        String issuerDN = userCert.getIssuerDN().toString();
        certSetting = CertSettingSingleton.getInstance(issuerDN);
    }
    if(certSetting == null){
      Log.error(" get CA or Root Failed, Not proper issuer setting!");
      return -1001;
    }
    return certSetting.verifyDate(userCert);
  }

  public int verifyCRL(X509Certificate userCert) {
    if (certSetting == null) {
        //获得证书发行者DN
        String issuerDN = userCert.getIssuerDN().toString();
        certSetting = CertSettingSingleton.getInstance(issuerDN);
    }
    if(certSetting == null){
      Log.error(" get CA or Root Failed, Not proper issuer setting!");
      return -1001;
    }
    return certSetting.verifyCRL(userCert);
  }

  public int doVerifyCert(String s) {
    int i;
    if ( (i = decodeUserCert(s)) != 0) {
      return i;
    }
    if (certSetting == null) {
        //获得证书发行者DN
        String issuerDN = UserCert.getIssuerDN().toString();
        certSetting = CertSettingSingleton.getInstance(issuerDN);
    }
    
    //为了测试版本，注释 verifyDate的检查2012-6-6 Sam
    if ( (i = verifyDate(UserCert)) != 0) {
      return i;
    }
    //
    if ( (i = verifyChain(UserCert)) != 0) {
      return i;
    }
    if ( (i = verifyCRL(UserCert)) != 0) {
      return i;
    }
    else {
      return 0;
    }
  }

  public int doVerifyCert(X509Certificate x) {
	    int i;
	    UserCert = x;
	    if (certSetting == null) {
                //获得证书发行者DN
                String issuerDN = UserCert.getIssuerDN().toString();
                certSetting = CertSettingSingleton.getInstance(issuerDN);
	    }
	    if ( (i = verifyDate(UserCert)) != 0) {
	      return i;
	    }
	    if ( (i = verifyChain(UserCert)) != 0) {
	      return i;
	    }
	    if ( (i = verifyCRL(UserCert)) != 0) {
	      return i;
	    }
	    else {
	      return 0;
	    }
	  }

  private String getValueFromString(String allStr, String name) {
    int nameLen = name.length();
    String returnStr = "";
    int pos = 0;
    int startPos = 0;
    int count = 0;

    startPos = allStr.indexOf(name, pos);
    while (startPos != -1) {
      int endPos = allStr.indexOf(", ", startPos + 1);
      if (count > 0) {
        returnStr += ",";
      }
      if (endPos == -1) {
        returnStr += allStr.substring(startPos + nameLen);
        startPos = endPos;
      }
      else {
        returnStr += allStr.substring(startPos + nameLen, endPos);
        count++;
        pos = endPos;
        startPos = allStr.indexOf(name, pos);
      }
    }
    return returnStr;

  }

  public String getCertInfo(int i) {
    Principal principal1 = UserCert.getSubjectDN();
    String s1 = principal1.toString();
    Principal principal2 = UserCert.getIssuerDN();
    String s2 = principal2.toString();

    switch (i) {
      default:
        break;

      case 22: //
        BigInteger biginteger = UserCert.getSerialNumber();
        return biginteger.toString();

      case 0: //
        return getValueFromString(s1, "CN=");

      case 1: //
        return getValueFromString(s1, "C=");

      case 2: //
        return getValueFromString(s1, "O=");

      case 3: //
        return getValueFromString(s1, "OU=");

      case 4: //
        return getValueFromString(s1, "ST=");

      case 5: //
        return getValueFromString(s1, "L=");

      case 6: //
        String emailAddr = getValueFromString(s1, "EmailAddress=");
        if (emailAddr.equals("")) {
          emailAddr = getValueFromString(s1, "EMAILADDRESS=");
        }
        return emailAddr;

      case 9: //
        return getValueFromString(s2, "CN=");

      case 10: //
        return getValueFromString(s2, "C=");

      case 11: //'
        return getValueFromString(s2, "O=");

      case 12: //
        return getValueFromString(s2, "OU=");

      case 13: //
        return getValueFromString(s2, "ST=");

      case 14: //
        return getValueFromString(s2, "L=");

      case 15: //
        emailAddr = getValueFromString(s2, "EmailAddress=");
        if (emailAddr.equals("")) {
          emailAddr = getValueFromString(s2, "EMAILADDRESS=");
        }
        return emailAddr;

      case 19: //
        return DateTime.formatDate(UserCert.getNotBefore(), "yyyy-MM-dd");
      case 20: //
        return DateTime.formatDate(UserCert.getNotAfter(), "yyyy-MM-dd");
      case 21: //
        return String.valueOf(UserCert.getVersion());
    }
    return "";
  }

  public X509Certificate getUserCert() {
    return UserCert;
  }

  public String getCertSubjectDN() {
    Principal principal1 = UserCert.getSubjectDN();
    String s2 = principal1.toString();
    return s2;
  }

  public String getCertRefNo() {
    Principal principal1 = UserCert.getSubjectDN();
    String s2 = principal1.toString();
    return getValueFromString(s2, "SERIALNUMBER=");
  }

  /*
  private boolean verifySignature(
      X509Certificate x509certificate,
      String s,
      String s1) {
    try {
      byte abyte0[] = new byte[s1.length()];
      int i = CertUtils.B64Decode(s1, abyte0);
      byte abyte1[] = new byte[i];
      System.arraycopy(abyte0, 0, abyte1, 0, i);
      PublicKey publickey = x509certificate.getPublicKey();
      RSAPublicKey rsapublickey = (RSAPublicKey) publickey;
      BigInteger biginteger = rsapublickey.getModulus();
      byte abyte2[] = new byte[3];
      abyte2[0] = 1;
      abyte2[1] = 0;
      abyte2[2] = 1;
      BigInteger biginteger1 = new BigInteger(1, abyte2);
      BigInteger biginteger2 = new BigInteger(1, abyte1);
      BigInteger biginteger3 =
          biginteger2.modPow(biginteger1, biginteger);
      byte abyte3[] = biginteger3.toByteArray();
      byte abyte4[] = new byte[16];
      boolean flag = false;
      int j = 0;
      int k = 0;
      for (k = 0; k < abyte3.length; k++) {
        if (abyte3[k] == 0) {
          flag = true;
        }
        if (!flag) {
          continue;
        }
        abyte4[j] = abyte3[k];
        j++;
        if (abyte3[k] == 20) {
          break;
        }
      }

      byte abyte5[] = s.getBytes();
      byte abyte6[] = {
          0, 48, 33, 48, 9, 6, 5, 43, 14, 3, 2, 26, 5, 0, 4, 20};
      String s2 = new String(abyte4);
      String s3 = new String(abyte6);
      if (s2.equals(s3)) {
        byte abyte7[] = new byte[20];
        int l = 0;
        for (k++; k < abyte3.length; k++) {
          abyte7[l] = abyte3[k];
          l++;
        }

        MessageDigest messagedigest = MessageDigest.getInstance("SHA1");
        messagedigest.update(abyte5, 0, abyte5.length);
        byte abyte9[] = messagedigest.digest();
        if (MessageDigest.isEqual(abyte9, abyte7)) {
          return true;
        }
        else {
          Log.error(" digest not match!");
          return false;
        }
      }
      byte abyte8[] = new byte[16];
      boolean flag1 = false;
      for (int i1 = abyte3.length - 16; i1 < abyte3.length; i1++) {
        abyte8[i1] = abyte3[i1];

      }
      MessageDigest messagedigest1 = MessageDigest.getInstance("MD5");
      messagedigest1.update(abyte5, 0, abyte5.length);
      byte abyte10[] = messagedigest1.digest();
      if (MessageDigest.isEqual(abyte10, abyte8)) {
        return true;
      }
      else {
        Log.error("Digest not match!");
        return false;
      }
    }
    catch (Exception ex) {
      Log.error("Signature Verify Exception!", ex);
    }
    return false;
  }
*/

  public int decodeUserCert(String s) {
    if (s.length() == 0) {
      Log.error("UserCert is null!");
      return -1010;
    }
    try {
      UserCert = getCertFromString(s);
    }
    catch (Exception _ex) {
      Log.error("Read UserCert Failed!", _ex);
    }
    if (UserCert == null) {
      Log.error("get UserCert Failed!");
      return -1023;
    }
    return 0;
  }

  public int doVerifySignedData(String s, String s1, String s2) {
    s2 = CertUtils.URIRep(s2);
    int i;
    if ( (i = doVerifyCert(s)) != 0) {
      return i;
    }
    if(verifyData(UserCert, s1, s2) == 0){
        return 0;
    }else{
        return -1021;
    }
  }

  public int doVerifySignedData(X509Certificate c, String s1, String s2) {
    UserCert = c;
    if(verifyData(UserCert, s1, s2) == 0){
        return 0;
    }else{
        return -1021;
    }
  }

  private X509Certificate getCertFromString(String certStr) throws Exception {
    byte certByteArray[] = new byte[2048];
    if (!certStr.startsWith("-----BEGIN CERTIFICATE-----")) {
      certByteArray = Utils.hexStr2Bytes(certStr);
    }
    else {
      String s1 = CertUtils.URIRep(certStr);
      certByteArray = s1.getBytes();
    }
    return getCertFromByteArray(certByteArray);
  }

  private X509Certificate getCertFromByteArray(byte[] certByteArray) throws
      Exception {
    ByteArrayInputStream bytearrayinputstream =
        new ByteArrayInputStream(certByteArray);
    CertificateFactory certificatefactory =
        CertificateFactory.getInstance("X.509");
    X509Certificate x509certificate =
        (X509Certificate) certificatefactory.generateCertificate(
            bytearrayinputstream);
    bytearrayinputstream.close();
    return x509certificate;
  }

  //从文件中读取证书
  private X509Certificate getCertFromFile(String s) throws Exception {
    FileInputStream fileinputstream = new FileInputStream(s);
    CertificateFactory certificatefactory =
        CertificateFactory.getInstance("X.509");
    X509Certificate x509certificate =
        (X509Certificate) certificatefactory.generateCertificate(
            fileinputstream);
    fileinputstream.close();
    return x509certificate;
  }

  /** ----------------------------------------------------
   *               get Subject Alternative Names
   *               Code for jdk 1.3
   * -----------------------------------------------------
     /**
    * Extracts the TBS certificate from the given certificate.
    *
    * @param cert the X.509 certificate to extract the TBS certificate from.
    * @return the TBS certificate
    * @exception IOException if extraction fails.
    * @exception CertificateEncodingException if extraction fails.
    */
   private static TBSCertificateStructure getTBSCertificateStructure(
       X509Certificate cert) throws CertificateEncodingException, IOException {
     DERObject obj = toDERObject(cert.getTBSCertificate());
     return TBSCertificateStructure.getInstance(obj);
   }

  /**
   * Converts the DER-encoded byte array into a
   * <code>DERObject</code>.
   *
   * @param data the DER-encoded byte array to convert.
   * @return the DERObject.
   * @exception IOException if conversion fails
   */
  private static DERObject toDERObject(byte[] data) throws IOException {
    ByteArrayInputStream inStream = new ByteArrayInputStream(data);
    DERInputStream derInputStream = new DERInputStream(inStream);
    return derInputStream.readObject();
  }

  /**
   * Extracts the value of a certificate extension.
   *
   * @param ext the certificate extension to extract the value from.
   * @exception IOException if extraction fails.
   */
  private static DERObject getExtensionObject(org.bouncycastle.asn1.x509.
                                              X509Extension ext) throws
      IOException {
    return toDERObject(ext.getValue().getOctets());
  }

  /**
   * Creates a <code>BasicConstraints</code> object from given
   * extension.
   *
   * @param ext the extension.
   * @return the <code>BasicConstraints</code> object.
   * @exception IOException if something fails.
   */
  private static GeneralNames getGeneralNames(org.bouncycastle.asn1.x509.
                                              X509Extension ext) throws
      IOException {
    DERObject obj = getExtensionObject(ext);
    return GeneralNames.getInstance(obj);
  }

  /**
   * Creates a <code>ArrayList</code> object from given
   * extension.
   *
   * @return the <code>ArrayList</code> object.
   * @exception return empty arrayList if something fails.
   */
  public ArrayList getSubjectAlteNamesList() {
    ArrayList retVal = new ArrayList();
    try {
      TBSCertificateStructure tbsCert = getTBSCertificateStructure(
          UserCert);
      X509Extensions extentsions = tbsCert.getExtensions();
      org.bouncycastle.asn1.x509.X509Extension extAltNames = extentsions.
          getExtension(
              X509Extensions.SubjectAlternativeName);
      GeneralName[] altNames = getGeneralNames(extAltNames).getNames();
      for (int i = 0; i < altNames.length; i++) {
        GeneralName altName = altNames[i];
        int tagNo = altName.getTagNo();
        Object name = altName.getName();
        switch (tagNo) {
          case 1:
            DERIA5String nameValue1 = (DERIA5String) name;
            String altNameStr1 = "rfc822Name=" + nameValue1.getString();
            retVal.add(altNameStr1);
            break;
          case 2:
            DERIA5String nameValue2 = (DERIA5String) name;
            String altNameStr2 = "dNSName=" + nameValue2.getString();
            retVal.add(altNameStr2);
            break;
          case 6:
            DERIA5String nameValue6 = (DERIA5String) name;
            String altNameStr6 = "uniformResourceIdentifier=" +
                nameValue6.getString();
            retVal.add(altNameStr6);
            break;
          case 0:
            DERConstructedSequence nameValue0 = (DERConstructedSequence) name;

            //DERObjectIdentifier.class
            Object nameValue0_0 = nameValue0.getObjectAt(0); //User Principal Name
            DERTaggedObject nameValue0_1 = (DERTaggedObject) nameValue0.
                getObjectAt(1);
            String altNameStr0 = "OtherName=User Principal Name=" +
                ( (DERUTF8String) nameValue0_1.getObject()).getString();
            retVal.add(altNameStr0);

            break;
        }
      }
    }
    catch (Exception e) {
      System.out.println("[Error]: Error getting subject alternative names");
      e.printStackTrace();
    }
    return retVal;
  }

  /**
   * Creates a <code>String</code> object from given
   * extension.
   *
   * @return the <code>String</code> object.
   * @exception return null if something fails.
   */
  public String getSubjectAlteNameHexCode() {
    ArrayList nameList = getSubjectAlteNamesList();
    for (int i = 0; i < nameList.size(); i++) {
      String name = (String) nameList.get(i);
      if (name.indexOf("OtherName=User Principal Name=") >= 0) {
        return name.substring("OtherName=User Principal Name=".length());
      }
    }
    return null;
  }

  //选择证书
  public int verifyData(X509Certificate certificate, String data,
                        String signature) {

      Signature verifyEngine;
      String algorithmName = "SHA1WithRSA";
      try {
          verifyEngine = Signature.getInstance(algorithmName);
          verifyEngine.initVerify(certificate.getPublicKey());
          verifyEngine.update(data.getBytes());
          if(verifyEngine.verify(Utils.hexStr2Bytes(signature))){
              return 0;
          }else{
              return 1;
          }
      } catch (Exception ex) {
          ex.printStackTrace();
      }
      return 2;
    }

  // main function for test
  public static void main(String[] args) {
    BcCertVerification testObj = new BcCertVerification();
    try {
      testObj.UserCert = testObj.getCertFromFile("D:/Sample Certificate/test_cert/personal.cer");
      System.out.println(testObj.getSubjectAlteNameHexCode());
      System.out.println(testObj.getSubjectAlteNamesList());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    //CertSettingSingleton.setRealPath("E:/cert/iaik/WebContent/");
    //testObj.doVerifyCert(certStr);
  }

    public boolean getCheckCertFlag() {
        return checkCertFlag;
    }

}
