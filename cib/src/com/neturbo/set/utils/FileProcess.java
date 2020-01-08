package com.neturbo.set.utils;

import java.io.*;
import java.util.*;
import java.text.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class FileProcess {

  public static ArrayList processDir(String processDir1, String filterName,
                                     FileProcessInterface processBean,
                                     boolean recursive) throws
      Exception {
    ArrayList fileList = new ArrayList();
    File processDir = new File(processDir1);
    boolean rslt = true; //保存中间结果
    File processFile1[] = processDir.listFiles(new DirFilter(filterName));
    for (int i = 0; i < processFile1.length; i++) {
      if (processFile1[i].isFile()) {
        processBean.process(processFile1[i]);
        fileList.add(processFile1[i]);
      }
    }

    //如果允许递归子目录
    if (recursive) {
      File subs[] = processDir.listFiles();
      for (int i = 0; i < subs.length; i++) {
        if (subs[i].isDirectory()) {
          processDir(String.valueOf(subs[i]), filterName, processBean,
                     recursive); //递归删除子文件夹内容
          processBean.process(subs[i]);
        }
      }
    }
    return fileList;
  }

  public static void delDir(String deleteDir11, String filterName) throws
      Exception {
    File deleteDir = new File(deleteDir11);
    boolean rslt = true; //保存中间结果
    if (deleteDir.exists()) {
      File delFile1[] = deleteDir.listFiles(new DirFilter(filterName));
      for (int j = 0; j < delFile1.length; j++) {
        if (delFile1[j].isFile() &&
            ! (delFile1[j].getName().indexOf("date.etc") > -1)) {
          delFile1[j].delete();
        }
      }

      File subs[] = deleteDir.listFiles();
      for (int i = 0; i < subs.length; i++) {
        if (subs[i].isDirectory()) {
          delDir(String.valueOf(subs[i]), filterName); //递归删除子文件夹内容
          rslt = subs[i].delete();
        }
      }
    }
  }

  public static void copyDir(String fromDir, String toDir, String filterName) throws
      Exception {
    File copyDir = new File(fromDir);
    File copyToDir = new File(toDir);
    if (copyDir.exists()) {
      if (!copyToDir.exists()) {
        copyToDir.mkdir();
      }
      boolean rslt = true; //保存中间结果

      File copyFile1[] = copyDir.listFiles(new DirFilter(filterName));
      for (int i = 0; i < copyFile1.length; i++) {
        if (copyFile1[i].isFile()) {
          String newFileName = copyFile1[i].getName();
          copyFile(String.valueOf(copyFile1[i]), (toDir + "/" + newFileName));
        }
      }

      File subs[] = copyDir.listFiles();
      for (int i = 0; i < subs.length; i++) {
        if (subs[i].isDirectory()) {
          String newFileName = subs[i].getName();
          toDir = toDir + "/" + newFileName;
          File FiletoDir = new File(toDir);
          copyDir(String.valueOf(subs[i]), toDir, filterName); //递归复制子文件夹内容
        }
      }
    }
  }

  public static void copyFile(String fromFile, String toFile) throws Exception {
    if (! (fromFile.indexOf("date.etc") > -1)) {
      File in = new File(fromFile);
      File out = new File(toFile);
      if (in.isFile()) {
        byte[] buf = new byte[1024];
        FileInputStream fis = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);
        int readlen = 0;
        while ( (readlen = fis.read(buf)) != -1) {
          fos.write(buf, 0, readlen);
        }
        fis.close();
        fos.close();
      }
    }
  }

  public static void delFile(String deleteFile) throws Exception {
    File deleteObj = new File(deleteFile);
    if (deleteObj.isFile()) {
      deleteObj.delete();
    }
  }

  public static void delDir(String deleteDir) throws Exception {
    File deleteDirObj = new File(deleteDir);
    if (deleteDirObj.isFile()) {
      deleteDirObj.delete();
    }
  }

  public static void moveDir(String fromDir, String toDir, String filterName) throws
      Exception {
    copyDir(fromDir, toDir, filterName);
    delDir(fromDir, filterName);
  }

  public static void moveFile(String fromDir, String toDir) throws Exception {
    copyFile(fromDir, toDir);
    delFile(fromDir);
  }

  public static void fileRename(String workDir, String oldName, String newName) throws
      Exception {
    oldName = workDir + "/" + oldName;
    newName = workDir + "/" + newName;
    File oldFile = new File(oldName);
    File newFile = new File(newName);
    oldFile.renameTo(newFile);
  }

  public static void fileRename(String oldName, String newName) throws
      Exception {
    File oldFile = new File(oldName);
    File newFile = new File(newName);
    oldFile.renameTo(newFile);
  }

  public static void combineFiles(ArrayList fromFiles, String toFile) throws
      Exception {
    File out = new File(toFile);
    if(out.exists()){
      out.delete();
    }
    for (int i = 0; i < fromFiles.size(); i++) {
      File in = new File(fromFiles.get(i).toString());
      if (in.isFile()) {
        byte[] buf = new byte[1024];
        FileInputStream fis = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out, true);
        int readlen = 0;
        while ( (readlen = fis.read(buf)) != -1) {
          fos.write(buf, 0, readlen);
        }
        fis.close();
        fos.close();
      }
    }
  }
}
