package com.neturbo.set.core;

import java.util.*;

import javax.servlet.http.*;

import com.neturbo.set.exception.*;

public interface NTBUser{

  public abstract void setUserId(String userId);
  public abstract String getUserId();

  public abstract void setUserName(String userName);
  public abstract String getUserName();

    public abstract void setStatus(String Status);
  public abstract String getStatus();

  public abstract void setLoginStatus(String userId);
  public abstract String getLoginStatus();

  public abstract void setPrevLoginTime(Date prevLoginTime);
  public abstract Date getPrevLoginTime();

  public abstract void setRetryTimes(int retryTimes);
  public abstract int getRetryTimes();

  public abstract void setLanguage(Locale language);
  public abstract Locale getLanguage();

  // add by linrui at 20190729
  public abstract void setLangCode(String langCode);
  public abstract String getLangCode();

  public abstract void setSessionId(String sessionId);
  public abstract String getSessionId();

  public abstract void setFunctionList(List functionList);
  public abstract List getFunctionList();

}
