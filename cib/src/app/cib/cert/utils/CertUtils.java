package app.cib.cert.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CertUtils {

  private static final char hexChar[] = {
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f'
  };

  public CertUtils() {
  }

  public static String URIRep(String s) {
    String s1 = "";
    int i = 0;
    int j = 0;
    while ( (i = s.indexOf(37, j)) != -1) {
      s1 = s1 + s.substring(j, i);
      String s2 = s.substring(i, i + 3);
      if (s2.indexOf("%2A") != -1) {
        s1 = s1 + "*";
        j = i + 3;
      }
      else if (s2.indexOf("%2F") != -1) {
        s1 = s1 + "/";
        j = i + 3;
      }
      else if (s2.indexOf("%2B") != -1) {
        s1 = s1 + "+";
        j = i + 3;
      }
      else if (s2.indexOf("%3D") != -1) {
        s1 = s1 + "=";
        j = i + 3;
      }
      else {
        s1 = s1 + s2;
        j = i + 1;
      }
    }
    s1 = s1 + s.substring(j, s.length());
    return s1;
  }

  public static int B64Decode(String s, byte abyte0[]) {
    int i = s.length();
    byte abyte1[] = new byte[i];
    for (int j = 0; j < i; j++) {
      abyte1[j] = (byte) s.charAt(j);

    }
    return B64Decode(abyte1, abyte0);
  }

  public static int B64Decode(byte abyte0[], byte abyte1[]) {
    int i = 0;
    int j = 0;
    int k = abyte0.length;
    int l = k / 4;
    for (int i1 = 0; i1 < l; i1++) {
      j = Decode4Char(abyte0, abyte1, j, i1);
      i += j;
    }

    k -= l * 4;
    if (k > 0) {
      byte abyte2[] = new byte[4];
      int j1;
      for (j1 = 0; j1 < k; j1++) {
        abyte2[j1] = abyte0[j1];

      }
      for (; j1 < 4; j1++) {
        abyte2[j1] = 61;

      }
      j = Decode4Char(abyte2, abyte1, j, 0);
      i += j;
    }
    return i;
  }

  private static int Decode4Char(byte abyte0[], byte abyte1[], int i, int j) {
    byte abyte2[] = new byte[4];
    int k = j * 4;
    int l = j * 3;
    for (int i1 = 0; i1 < 4; i1++) {
      abyte2[i1] = abyte0[k + i1];

    }
    i = map4char(abyte2);
    abyte1[l] = (byte) (abyte2[0] << 2 | abyte2[1] >> 4);
    abyte1[l + 1] = (byte) (abyte2[1] << 4 | abyte2[2] >> 2);
    abyte1[l + 2] = (byte) (abyte2[2] << 6 | abyte2[3]);
    for (int j1 = i; j1 < 3; j1++) {
      abyte1[l + j1] = 0;

    }
    return i;
  }

  private static int map4char(byte abyte0[]) {
    int i;
    for (i = 0; i < 4; i++) {
      if (abyte0[i] >= 97 && abyte0[i] <= 122) {
        abyte0[i] = (byte) ( (abyte0[i] - 97) + 26);
        continue;
      }
      if (abyte0[i] >= 65 && abyte0[i] <= 90) {
        abyte0[i] -= 65;
        continue;
      }
      if (abyte0[i] >= 48 && abyte0[i] <= 57) {
        abyte0[i] = (byte) ( (abyte0[i] - 48) + 52);
        continue;
      }
      if (abyte0[i] == 43) {
        abyte0[i] = 62;
        continue;
      }
      if (abyte0[i] == 47) {
        abyte0[i] = 63;
        continue;
      }
      if (abyte0[i] == 61) {
        break;
      }
    }

    if (i <= 1) {
      i = 1;
    }
    return i - 1;
  }

  public static byte[] splitCertificate(String s) {
    int i = s.length();
    byte abyte0[] = new byte[i];
    for (int j = 0; j < i; j++) {
      abyte0[j] = (byte) s.charAt(j);

    }
    return splitCertificate(abyte0);
  }

  public static byte[] splitCertificate(byte abyte0[]) {
    String s = new String("-----BEGIN CERTIFICATE-----\n");
    String s1 = new String("-----END CERTIFICATE-----\n");
    int i = abyte0.length;
    int j = i / 64;
    if (i % 64 != 0) {
      j++;
    }
    byte abyte1[] = new byte[i + j + s.length() + s1.length()];
    int j1 = 0;
    for (int k = 0; k < s.length(); ) {
      abyte1[j1] = (byte) s.charAt(k);
      k++;
      j1++;
    }

    int l;
    for (l = 0; l < j - 1; l++) {
      System.arraycopy(abyte0, l * 64, abyte1, j1, 64);
      j1 += 64;
      abyte1[j1] = 10;
      j1++;
    }

    boolean flag = false;
    for (l *= 64; l < i; l++) {
      byte byte0 = abyte0[l];
      if (!flag
          && (byte0 >= 97
              && byte0 <= 122
              || byte0 >= 65
              && byte0 <= 90
              || byte0 >= 48
              && byte0 <= 57
              || byte0 == 43
              || byte0 == 47)) {
        abyte1[j1] = byte0;
        j1++;
        continue;
      }
      if (byte0 != 61) {
        break;
      }
      flag = true;
      abyte1[j1] = byte0;
      j1++;
    }

    abyte1[j1] = 10;
    j1++;
    for (int i1 = 0; i1 < s1.length(); ) {
      abyte1[j1] = (byte) s1.charAt(i1);
      i1++;
      j1++;
    }

    byte abyte2[] = new byte[j1];
    System.arraycopy(abyte1, 0, abyte2, 0, j1);
    return abyte2;
  }

}
