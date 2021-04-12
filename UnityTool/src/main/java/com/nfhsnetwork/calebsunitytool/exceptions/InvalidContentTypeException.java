package com.nfhsnetwork.calebsunitytool.exceptions;

public class InvalidContentTypeException extends UnityException {
	
	private final String contentTypeGiven;
	private final String contentTypeExpected;
	private static final String DEFAULT_MESSAGE = "Invalid Content Type: %s given, %s expected.";
	
	public InvalidContentTypeException() {
		super(String.format(DEFAULT_MESSAGE, "", ""));
		this.contentTypeGiven = "";
		this.contentTypeExpected = "";
	}

	public InvalidContentTypeException(String message) {
		super(message);
		this.contentTypeGiven = "";
		this.contentTypeExpected = "";
	}
	
	public InvalidContentTypeException(String given, String expected)
	{
		super(String.format(DEFAULT_MESSAGE, given, expected));
		this.contentTypeGiven = given;
		this.contentTypeExpected = expected;
	}

	public InvalidContentTypeException(Throwable cause) {
		super(String.format(DEFAULT_MESSAGE, "", ""), cause);
		this.contentTypeGiven = "";
		this.contentTypeExpected = "";
	}
	
	public InvalidContentTypeException(String given, String expected, Throwable cause)
	{
		super(String.format(DEFAULT_MESSAGE, given, expected), cause);
		this.contentTypeGiven = given;
		this.contentTypeExpected = expected;
	}

	public InvalidContentTypeException(String message, Throwable cause) {
		super(message, cause);
		this.contentTypeGiven = "";
		this.contentTypeExpected = "";
	}

	public InvalidContentTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.contentTypeGiven = "";
		this.contentTypeExpected = "";
	}
	
	public String typeGiven()
	{
		return this.contentTypeGiven;
	}
	
	public String typeExpected()
	{
		return this.contentTypeExpected;
	}

}
