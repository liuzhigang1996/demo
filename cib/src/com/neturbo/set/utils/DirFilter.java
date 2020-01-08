package com.neturbo.set.utils;

import java.io.File;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.io.*;
import java.util.*;
import java.text.DateFormat;

public class DirFilter
    implements FilenameFilter {
  String afn;

  public DirFilter(String afn) {
    this.afn = afn;
  }

  public boolean accept(File dir, String name) {
    // Strip path information:
    String f = new File(name).getName();
    return f.indexOf(afn) != -1;
  }

  public static boolean accept1(String name1, String afn1) {
    return name1.indexOf(afn1) != -1;
  }
}
