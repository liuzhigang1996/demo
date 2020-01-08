package com.neturbo.set.utils;

import java.io.*;
import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public abstract class FileProcessInterface {
  public FileProcessInterface() {
  }
  public abstract void process(File afile) throws Exception;
}
