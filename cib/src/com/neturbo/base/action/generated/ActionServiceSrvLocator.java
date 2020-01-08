/**
 * ActionServiceSrvLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.neturbo.base.action.generated;

public class ActionServiceSrvLocator extends org.apache.axis.client.Service implements com.neturbo.base.action.generated.ActionServiceSrv {

    // Use to get a proxy class for ActionService
    private final java.lang.String ActionService_address = "http://localhost:8080/perbank/services/ActionService";

    public java.lang.String getActionServiceAddress() {
        return ActionService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ActionServiceWSDDServiceName = "ActionService";

    public java.lang.String getActionServiceWSDDServiceName() {
        return ActionServiceWSDDServiceName;
    }

    public void setActionServiceWSDDServiceName(java.lang.String name) {
        ActionServiceWSDDServiceName = name;
    }

    public com.neturbo.base.action.generated.ActionService getActionService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ActionService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getActionService(endpoint);
    }

    public com.neturbo.base.action.generated.ActionService getActionService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.neturbo.base.action.generated.ActionServiceSoapBindingStub _stub = new com.neturbo.base.action.generated.ActionServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getActionServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.neturbo.base.action.generated.ActionService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.neturbo.base.action.generated.ActionServiceSoapBindingStub _stub = new com.neturbo.base.action.generated.ActionServiceSoapBindingStub(new java.net.URL(ActionService_address), this);
                _stub.setPortName(getActionServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("ActionService".equals(inputPortName)) {
            return getActionService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://action.base.neturbo.com", "ActionServiceSrv");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("ActionService"));
        }
        return ports.iterator();
    }

}
