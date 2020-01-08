package com.neturbo.set.exception;

public class NTBException extends Exception {

	private static final long serialVersionUID = -4902952678332385504L;
	private String errorCode;
	private NTBError error;
	private NTBErrorArray errorArray;

	private Object[] parameters;

	public NTBException() {
		errorCode = "";
	}

	public NTBException(int i) {
		this(Integer.toString(i));
	}

	public NTBException(String s) {
		super(s);
		errorCode = s;
	}

	public NTBException(String s, Object[] p) {
		super(s);
		errorCode = s;
		parameters = p;
	}

	public NTBException(NTBError a) {
		error = a;
	}

	public NTBException(NTBErrorArray a) {
		errorArray = a;
	}

	public NTBException(NTBErrorArray a, Object[] parameters) {
		errorArray = a;
		this.parameters = parameters;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public NTBError getError() {
		return error;
	}

	public NTBErrorArray getErrorArray() {
		return errorArray;
	}

	public Object[] getParameters() {
		return parameters;
	}
}
