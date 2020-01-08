package com.neturbo.set.exception;

import java.io.Serializable;
import java.util.*;

import javax.servlet.ServletRequest;

public class NTBErrorArray implements Serializable{
  ArrayList errorArray;
  public NTBErrorArray() {
    errorArray = new ArrayList();
  }

  public NTBError addError(String errorCode) {
    NTBError error = new NTBError(errorCode);
    errorArray.add(error);
    return error;
  }

  public NTBError addError(String errorCode, String errorName, String label) {
    NTBError error = new NTBError(errorCode, errorName, label);
    errorArray.add(error);
    return error;
  }

  public NTBError getError(int i) {
    return (NTBError) errorArray.get(i);
  }

  public NTBError getError(String errorCode, String field) {
    for (int i = 0; i < errorArray.size(); i++) {
      NTBError error = (NTBError) errorArray.get(i);
      if (error.getErrorCode().equals(errorCode) &&
          error.getField().equals(field)) {
        return error;
      }
    }
    return null;
  }

  public void removeError(String errorCode, String field) {
    for (int i = 0; i < errorArray.size(); i++) {
      NTBError error = (NTBError) errorArray.get(i);
      if (error.getErrorCode().equals(errorCode) &&
          error.getField().equals(field)) {
        errorArray.remove(i);
      }
    }
  }

  public void removeError(NTBError error) {
    errorArray.remove(error);
  }

  public int size() {
    return errorArray.size();
  }
}
