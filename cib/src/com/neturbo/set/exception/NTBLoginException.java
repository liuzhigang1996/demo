package com.neturbo.set.exception;

public class NTBLoginException extends NTBException {
  public NTBLoginException() {
  }
  public NTBLoginException(int i)
  {
      super(i);
  }
  public NTBLoginException(String s)
  {
      super(s);
  }
  public NTBLoginException(String s, Object[] p)
  {
      super(s, p);
  }


}
