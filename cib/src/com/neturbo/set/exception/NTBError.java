package com.neturbo.set.exception;

import java.io.Serializable;



public class NTBError implements Serializable{

    private String field;
    private String errorCode;
    private String label;
    private Object parameters;

    public NTBError() {
    }

    public NTBError(String errorCode) {
        this.errorCode = errorCode;
    }

    public NTBError(String errorCode, String field, String label) {
        this.field = field;
        this.errorCode = errorCode;
        this.label = label;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getField() {
        return field;
    }

    public String getLabel() {
        return label;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
