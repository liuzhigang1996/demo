/**
 * ActionServiceSrv.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.neturbo.base.action.generated;

public interface ActionServiceSrv extends javax.xml.rpc.Service {
    public java.lang.String getActionServiceAddress();

    public com.neturbo.base.action.generated.ActionService getActionService() throws javax.xml.rpc.ServiceException;

    public com.neturbo.base.action.generated.ActionService getActionService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
