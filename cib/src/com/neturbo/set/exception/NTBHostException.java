package com.neturbo.set.exception;

public class NTBHostException extends NTBException {
  public NTBHostException() {
  }

  public NTBHostException(int i)
  {
      super(i);
  }

  public NTBHostException(String s)
  {
      super(s);
  }

  public NTBHostException(String s, Object[] p)
  {
      super(s, p);
  }

  public NTBHostException(NTBError s)
  {
      super(s);
  }

  public NTBHostException(NTBErrorArray s)
  {
      super(s);
  }
}
