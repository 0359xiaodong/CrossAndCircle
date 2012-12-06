package com.example.crossandcircle;

public enum FieldType {
	EMPTY (0),
	CIRCLE (1),
	CROSS (-1);
	
	private int value;

	private FieldType(int i)
	{
		value = i;
	}
	
	public int getValue() {
		return value;
	}
}
