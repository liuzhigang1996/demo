package com.neturbo.set.utils;

import java.io.UnsupportedEncodingException;

import com.neturbo.set.core.*;
import com.neturbo.set.exception.*;

public class Encoding {

  public static final int EncodingASCII = 0;
  public static final int EncodingEBCDIC = 1;
  public static final int EncodingCOMP3 = 2;
  public static final int EncodingEBCDIC_SIGNED = 3;
  public static final int EncodingCOMP = 4;
  public static final int EncodingUTF8 = 8;

  private static byte a2e[] = {
      0, (byte) 1, (byte) 2, (byte) 3, (byte) 55, (byte) 45, (byte) 46,
      (byte) 47, (byte) 22, (byte) 5, (byte) 37, (byte) 11, (byte) 12,
      (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18,
      (byte) 19, (byte) 60, (byte) 61, (byte) 50,
      (byte) 38, (byte) 24, (byte) 25, (byte) 63, (byte) 39, (byte) 28,
      (byte) 29, (byte) 30, (byte) 31, (byte)
      64, (byte) 79, (byte) 127, (byte) 123, (byte) 91, (byte) 108, (byte) 80,
      (byte) 125, (byte) 77, (byte) 93, (byte) 92, (byte) 78, (byte) 107,
      (byte) 96, (byte) 75, (byte) 97, (byte) 240, (byte) 241, (byte) 242,
      (byte) 243, (byte) 244, (byte) 245,
      (byte) 246, (byte) 247, (byte) 248, (byte) 249, (byte) 122, (byte) 94,
      (byte) 76, (byte) 126, (byte) 110, (byte) 111, (byte)
      124, (byte) 193, (byte) 194, (byte) 195, (byte) 196, (byte) 197,
      (byte) 198, (byte) 199, (byte) 200, (byte) 201, (byte) 209, (byte) 210,
      (byte) 211, (byte) 212, (byte) 213, (byte) 214, (byte)
      215, (byte) 216, (byte) 217, (byte) 226, (byte) 227, (byte) 228,
      (byte) 229, (byte) 230, (byte) 231, (byte) 232, (byte) 233, (byte) 74,
      (byte) 224, (byte) 90, (byte) 95, (byte) 109, (byte)
      121, (byte) 129, (byte) 130, (byte) 131, (byte) 132, (byte) 133,
      (byte) 134, (byte) 135, (byte) 136, (byte) 137, (byte) 145, (byte) 146,
      (byte) 147, (byte) 148, (byte) 149, (byte) 150, (byte)
      151, (byte) 152, (byte) 153, (byte) 162, (byte) 163, (byte) 164,
      (byte) 165, (byte) 166, (byte) 167, (byte) 168, (byte) 169, (byte) 192,
      (byte) 106, (byte) 208, (byte) 161, (byte) 7, (byte)
      32, (byte) 33, (byte) 34, (byte) 35, (byte) 36, (byte) 21, (byte) 6,
      (byte) 23, (byte) 40, (byte) 41, (byte) 42, (byte) 43, (byte) 44,
      (byte) 9, (byte) 10, (byte) 27, (byte) 48, (byte) 49, (byte) 26,
      (byte) 51, (byte) 52, (byte) 53, (byte) 54,
      (byte) 8, (byte) 56, (byte) 57, (byte) 58, (byte) 59, (byte) 4, (byte) 20,
      (byte) 62, (byte) 225, (byte) 65, (byte) 66, (byte) 67, (byte) 68,
      (byte) 69, (byte) 70, (byte) 71,
      (byte) 72, (byte) 73, (byte) 81, (byte) 82, (byte) 83, (byte) 84,
      (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 98,
      (byte) 99, (byte) 100, (byte) 101, (byte) 102,
      (byte) 103, (byte) 104, (byte) 105, (byte) 112, (byte) 113, (byte) 114,
      (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120,
      (byte) 128, (byte) 138, (byte) 139,
      (byte) 140, (byte) 141, (byte) 142, (byte) 143, (byte) 144, (byte) 154,
      (byte) 155, (byte) 156, (byte) 157, (byte) 158, (byte)
      159, (byte) 160, (byte) 170, (byte) 171, (byte) 172, (byte) 173,
      (byte) 174, (byte) 175, (byte) 176, (byte) 177, (byte) 178, (byte) 179,
      (byte) 180, (byte) 181, (byte) 182, (byte) 183, (byte)
      184, (byte) 185, (byte) 186, (byte) 187, (byte) 188, (byte) 189,
      (byte) 190, (byte) 191, (byte) 202, (byte) 203, (byte) 204, (byte) 205,
      (byte) 206, (byte) 207, (byte) 218, (byte) 219, (byte)
      220, (byte) 221, (byte) 222, (byte) 223, (byte) 234, (byte) 235,
      (byte) 236, (byte) 237, (byte) 238, (byte) 239, (byte) 250, (byte) 251,
      (byte) 252, (byte) 253, (byte) 254, (byte) 255
  };

  private static byte e2a[] = {
      0, (byte) 1, (byte) 2, (byte) 3, (byte) 156, (byte) 9, (byte) 134,
      (byte) 127, (byte) 151, (byte) 141, (byte) 142, (byte) 11, (byte) 12,
      (byte) 13, (byte) 14, (byte) 15, (byte)
      16, (byte) 17, (byte) 18, (byte) 19, (byte) 157, (byte) 133, (byte) 8,
      (byte) 135, (byte) 24, (byte) 25, (byte) 146, (byte) 143, (byte) 28,
      (byte) 29, (byte) 30, (byte) 31, (byte)
      128, (byte) 129, (byte) 130, (byte) 131, (byte) 132, (byte) 10, (byte) 23,
      (byte) 27, (byte) 136, (byte) 137, (byte) 138, (byte) 139, (byte) 140,
      (byte) 5, (byte) 6, (byte) 7, (byte)
      144, (byte) 145, (byte) 22, (byte) 147, (byte) 148, (byte) 149,
      (byte) 150, (byte) 4, (byte) 152, (byte) 153, (byte) 154, (byte) 155,
      (byte) 20, (byte) 21, (byte) 158, (byte) 26, (byte)
      32, (byte) 160, (byte) 161, (byte) 162, (byte) 163, (byte) 164,
      (byte) 165, (byte) 166, (byte) 167, (byte) 168, (byte) 91, (byte) 46,
      (byte) 60, (byte) 40, (byte) 43, (byte) 33, (byte)
      38, (byte) 169, (byte) 170, (byte) 171, (byte) 172, (byte) 173,
      (byte) 174, (byte) 175, (byte) 176, (byte) 177, (byte) 93, (byte) 36,
      (byte) 42, (byte) 41, (byte) 59, (byte) 94, (byte)
      45, (byte) 47, (byte) 178, (byte) 179, (byte) 180, (byte) 181, (byte) 182,
      (byte) 183, (byte) 184, (byte) 185, (byte) 124, (byte) 44, (byte) 37,
      (byte) 95, (byte) 62, (byte) 63, (byte)
      186, (byte) 187, (byte) 188, (byte) 189, (byte) 190, (byte) 191,
      (byte) 192, (byte) 193, (byte) 194, (byte) 96, (byte) 58, (byte) 35,
      (byte) 64, (byte) 39, (byte) 61, (byte) 34, (byte)
      195, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102,
      (byte) 103, (byte) 104, (byte) 105, (byte) 196, (byte) 197, (byte) 198,
      (byte) 199, (byte) 200, (byte)
      201, (byte) 202, (byte) 106, (byte) 107, (byte) 108, (byte) 109,
      (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 203,
      (byte) 204, (byte) 205, (byte) 206, (byte) 207, (byte)
      208, (byte) 209, (byte) 126, (byte) 115, (byte) 116, (byte) 117,
      (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 210,
      (byte) 211, (byte) 212, (byte) 213, (byte) 214, (byte)
      215, (byte) 216, (byte) 217, (byte) 218, (byte) 219, (byte) 220,
      (byte) 221, (byte) 222, (byte) 223, (byte) 224, (byte) 225, (byte) 226,
      (byte) 227, (byte) 228, (byte) 229, (byte) 230, (byte)
      231, (byte) 123, (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69,
      (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 232, (byte) 233,
      (byte) 234, (byte) 235, (byte) 236, (byte) 237, (byte)
      125, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79,
      (byte) 80, (byte) 81, (byte) 82, (byte) 238, (byte) 239, (byte) 240,
      (byte) 241, (byte) 242, (byte) 243, (byte)
      92, (byte) 159, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87,
      (byte) 88, (byte) 89, (byte) 90, (byte) 244, (byte) 245, (byte) 246,
      (byte) 247, (byte) 248, (byte) 249, (byte)
      48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54,
      (byte) 55, (byte) 56, (byte) 57, (byte) 250, (byte) 251, (byte) 252,
      (byte) 253, (byte) 254, (byte) 255

  };

  public static byte ASCII2EBCDIC(byte c) {
    int i;
    if (c >= 0) {
      i = c;
    }
    else {
      i = 256 + c;
    }
    return a2e[i];
  }

  public static byte EBCDIC2ASCII(byte c) {
    int i;
    if (c >= 0) {
      i = c;
    }
    else {
      i = 256 + c;
    }
    return e2a[i];
  }

  public static byte[] encode(String formatString, int encodingSymbol) throws
      NTBSystemException {
    int length = formatString.length();
    byte[] formatbytes = new byte[length];
    byte[] sourcebytes = Utils.convHexOrBin(formatString);
    switch (encodingSymbol) {
      case EncodingEBCDIC: 
        for (int i = 0; i < length; i++) {
          formatbytes[i] = ASCII2EBCDIC(sourcebytes[i]);
        }
        break;
    case EncodingCOMP3: 
        String resultStr = "D";
        if(Double.parseDouble(formatString) >= 0) {
            resultStr = "F";
        }
        resultStr = formatString + resultStr;
        formatbytes= Utils.hexStr2Bytes(resultStr);
        break;
    case EncodingUTF8:
		try {
			formatbytes = formatString.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NTBSystemException("err.utl.UnsupportedEncoding");
		}
		break;
      default:
        return sourcebytes;
    }
    return formatbytes;
  }

  public static String decode(byte[] parsebytes, int length, int encodingSymbol) throws
      NTBSystemException {
    String parseStr;
    switch (encodingSymbol) {
      case EncodingASCII:
        for (int i = 0; i < length; i++) {
          if (parsebytes[i] == 0) {
            parsebytes[i] = 32;
          }
        }
        parseStr = new String(parsebytes);
        break;
      case EncodingEBCDIC: 
        try{
          parseStr = new String(parsebytes, "Cp937");
        }catch(Exception e){
          for (int i = 0; i < length; i++) {
            parsebytes[i] = EBCDIC2ASCII(parsebytes[i]);
            if (parsebytes[i] == 0) {
              parsebytes[i] = 32;
            }
          }
          parseStr = new String(parsebytes);
        }
        break;

      case EncodingCOMP3: 
        parseStr = Utils.bytes2HexStr(parsebytes);
        if (parseStr.substring(parseStr.length() - 1).toUpperCase().equals("D")) {
          parseStr = "-" + parseStr.substring(0, parseStr.length() - 1);
        }
        else {
          parseStr = parseStr.substring(0, parseStr.length() - 1);
        }
        break;

      case EncodingEBCDIC_SIGNED: 
        parseStr = "";
        String byteStr = "F0";
        byte[] parsebyte = new byte[1];
        for (int i = 0; i < length; i++) {
          parsebyte[0] = parsebytes[i];
          byteStr = Utils.bytes2HexStr(parsebyte);
          parseStr = parseStr + byteStr.substring(1);
        }
        if (byteStr.substring(0, 1).equals("D")) {
          parseStr = "-" + parseStr;
        }
        break;

      case EncodingCOMP: 
        parseStr = Utils.bytes2HexStr(parsebytes);
        break;
      case EncodingUTF8:
		try {
			parseStr = new String(parsebytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			parseStr = new String(parsebytes);
		}
    	 break;
      default:
        throw new NTBSystemException(565);
    }
    return parseStr;
  }
}
