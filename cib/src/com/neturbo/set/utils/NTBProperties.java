package com.neturbo.set.utils;

import java.io.*;
import java.util.*;

/**
 *
 */
public class NTBProperties implements NTBResources{

    private static final String keyValueSeparators = "=: \t\r\n\f";

    private static final String specialSaveChars = "=: \t\r\n\f#!";

    private static final String strictKeyValueSeparators = "=:";

    private static final String whiteSpaceChars = " \t\r\n\f";

    private List keys;
    private List values;

    public NTBProperties(String name) throws Exception {
        this(name, Locale.getDefault());
    }

    public NTBProperties(String name, Locale locale) throws Exception {

        keys = new ArrayList();
        values = new ArrayList();

        if (name == null) {
            throw new Exception("Null name for resource loading");
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }

        name = Utils.replaceStr(name, ".", "/");
        String resourceName = "/" + name + "_" + locale + ".properties";

        InputStream is = this.getClass().getResourceAsStream(resourceName);

        if (is == null) {
            is = this.getClass().getResourceAsStream("/" + name + ".properties");
        }

        if (is == null) {
            throw new Exception("Resource " + name + " not found.");
        }

        load(is);

    }

    /*
     * Returns true if the given line is a line that must
     * be appended to the next line
     */
    private boolean continueLine(String line) {
        int slashCount = 0;
        int index = line.length() - 1;
        while ((index >= 0) && (line.charAt(index--) == '\\')) {
            slashCount++;
        }
        return (slashCount % 2 == 1);
    }

    /**
     */
    public List getKeys() {
        return keys;
    }

    /**
     *
     */
    public String getProperty(String key) {
        int index = keys.indexOf(key);

        if (index != -1) {
            return (String) values.get(index);
        } else {
            return null;
        }
    }

    /**
     */
    private void load(InputStream inStream) throws IOException {

        BufferedReader in =
                new BufferedReader(new InputStreamReader(inStream, "8859_1"));
        while (true) {
            // Get next line
            String line = in.readLine();
            if (line == null) {
                return;
            }

            if (line.length() > 0) {
                // Continue lines that end in slashes if they are not comments
                char firstChar = line.charAt(0);
                if ((firstChar != '#') && (firstChar != '!')) {
                    while (continueLine(line)) {
                        String nextLine = in.readLine();
                        if (nextLine == null) {
                            nextLine = "";
                        }
                        String loppedLine =
                                line.substring(0, line.length() - 1);
                        // Advance beyond whitespace on new line
                        int startIndex = 0;
                        for (startIndex = 0;
                                          startIndex < nextLine.length();
                                          startIndex++) {
                            if (whiteSpaceChars
                                .indexOf(nextLine.charAt(startIndex))
                                == -1) {
                                break;
                            }
                        }
                        nextLine =
                                nextLine.substring(startIndex, nextLine.length());
                        line = new String(loppedLine + nextLine);
                    }

                    // Find start of key
                    int len = line.length();
                    int keyStart;
                    for (keyStart = 0; keyStart < len; keyStart++) {
                        if (whiteSpaceChars.indexOf(line.charAt(keyStart))
                            == -1) {
                            break;
                        }
                    }

                    // Blank lines are ignored
                    if (keyStart == len) {
                        continue;
                    }

                    // Find separation between key and value
                    int separatorIndex;
                    for (separatorIndex = keyStart;
                                          separatorIndex < len;
                                          separatorIndex++) {
                        char currentChar = line.charAt(separatorIndex);
                        if (currentChar == '\\') {
                            separatorIndex++;
                        } else if (keyValueSeparators.indexOf(currentChar) !=
                                   -1) {
                            break;
                        }
                    }

                    // Skip over whitespace after key if any
                    int valueIndex;
                    for (valueIndex = separatorIndex;
                                      valueIndex < len;
                                      valueIndex++) {
                        if (whiteSpaceChars.indexOf(line.charAt(valueIndex))
                            == -1) {
                            break;
                        }
                    }

                    // Skip over one non whitespace key value separators if any
                    if (valueIndex < len) {
                        if (strictKeyValueSeparators
                            .indexOf(line.charAt(valueIndex))
                            != -1) {
                            valueIndex++;
                        }
                    }

                    // Skip over white space after other separators if any
                    while (valueIndex < len) {
                        if (whiteSpaceChars.indexOf(line.charAt(valueIndex))
                            == -1) {
                            break;
                        }
                        valueIndex++;
                    }
                    String key = line.substring(keyStart, separatorIndex);
                    String value =
                            (separatorIndex < len)
                            ? line.substring(valueIndex, len)
                            : "";

                    // Convert then store key and value
                    key = loadConvert(key);
                    value = loadConvert(value);

                    keys.add(key);
                    values.add(value);
                }
            }
        }
    }

    /*
     * Converts encoded &#92;uxxxx to unicode chars
     * and changes special saved chars to their original forms
     */
    private String loadConvert(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    public void populate(){
    }

    public void setArgs(Object ojb){
    }

    public static void main(String[] args) {
        try {
            NTBProperties aProperties = new NTBProperties("neturbo-config");
            List keys = aProperties.getKeys();
            for (int i = 0; i < keys.size(); i++) {
                System.out.println(
                        keys.get(i)
                        + "="
                        + aProperties.getProperty((String) keys.get(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
