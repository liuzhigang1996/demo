package com.neturbo.set.exception;

public class NTBSystemException extends NTBException {
  public NTBSystemException() {
  }
  public NTBSystemException(int i)
  {
      super(i);
  }
  public NTBSystemException(String s)
  {
      super(s);
  }
  public NTBSystemException(String s, Object[] p)
  {
      super(s, p);
  }


}
