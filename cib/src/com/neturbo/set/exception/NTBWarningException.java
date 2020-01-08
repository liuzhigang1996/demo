package com.neturbo.set.exception;

public class NTBWarningException extends NTBException {
  public NTBWarningException() {
  }

  public NTBWarningException(int i)
  {
      super(i);
  }

  public NTBWarningException(String s)
  {
      super(s);
  }

  public NTBWarningException(String s, Object[] p)
  {
      super(s, p);
  }

  public NTBWarningException(NTBError s)
  {
      super(s);
  }

  public NTBWarningException(NTBErrorArray s)
  {
      super(s);
  }
}
