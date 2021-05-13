package com.nfhsnetwork.unitytool.common;

public class StdPropertyChangeEvent {
	
	public static enum PropertyType {
		PARSE_COMPLETE,
		DONE,
		PROGRESS,
		FAILED,
		CANCELLED,
		VAR1,
		VAR2,
		VAR3,
		VAR4,
		VAR5
	}
	
	private final Object source;
	private final PropertyType type;
	private final Object oldValue;
	private final Object newValue;

	public StdPropertyChangeEvent(Object source, PropertyType type, Object oldvalue, Object newvalue)
	{
		this.source = source;
		this.type = type;
		this.oldValue = oldvalue;
		this.newValue = newvalue;
	}
	
	public Object getSource()
	{
		return this.source;
	}
	
	public PropertyType getPropertyName()
	{
		return this.type;
	}
	
	public Object getOldValue()
	{
		return this.oldValue;
	}
	
	public Object getNewValue()
	{
		return this.newValue;
	}
}
