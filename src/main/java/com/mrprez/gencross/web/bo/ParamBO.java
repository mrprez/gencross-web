package com.mrprez.gencross.web.bo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParamBO {
	public static final char DATE_TYPE = 'D';
	public static final char STRING_TYPE = 'S';
	public static final char INTEGER_TYPE = 'I';
	public static final char REAL_TYPE = 'R';
	public static final char BOOLEAN_TYPE = 'B';
	
	public static final String LAST_SEND_DATE_KEY = "last.send.date";
	public static final String ADMIN_ADRESS = "admin.adress";
	public static final String SAVE_ADRESS = "save.adress";
	public static final String TABLE_ADRESS = "table.adress";
	public static final String MIGRATION = "migration.active";
	
	private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss,SSS");
	
	private String key;
	private Object value;
	private Character type;
	private String stringValue;
	
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public char getType() {
		if(value instanceof String){
			type = STRING_TYPE;
		}else if(value instanceof Date){
			type = DATE_TYPE;
		}else if(value instanceof Double){
			type = REAL_TYPE;
		}else if(value instanceof Integer){
			type = INTEGER_TYPE;
		}else if(value instanceof Boolean){
			type = BOOLEAN_TYPE;
		}
		return type;
	}
	public void setType(char type) throws ParseException {
		this.type = type;
		if(stringValue!=null){
			switch(type){
			case DATE_TYPE:
				value = dateFormat.parseObject(stringValue);
				break;
			case STRING_TYPE:
				value = stringValue;
				break;
			case INTEGER_TYPE:
				value = Integer.parseInt(stringValue);
				break;
			case REAL_TYPE:
				value = Double.parseDouble(stringValue);
				break;
			case BOOLEAN_TYPE:
				value = Boolean.parseBoolean(stringValue);
				break;
			}
		}
	}
	protected String getStringValue() {
		if(value instanceof String){
			stringValue = (String)value;
		}else if(value instanceof Date){
			stringValue = dateFormat.format(value);
		}else if(value instanceof Double){
			stringValue = value.toString();
		}else if(value instanceof Integer){
			stringValue = value.toString();
		}else if(value instanceof Boolean){
			stringValue = value.toString();
		}
		return stringValue;
	}
	protected void setStringValue(String stringValue) throws ParseException {
		this.stringValue = stringValue;
		if(type!=null){
			switch(type){
			case DATE_TYPE:
				value = dateFormat.parseObject(stringValue);
				break;
			case STRING_TYPE:
				value = stringValue;
				break;
			case INTEGER_TYPE:
				value = Integer.parseInt(stringValue);
				break;
			case REAL_TYPE:
				value = Double.parseDouble(stringValue);
				break;
			case BOOLEAN_TYPE:
				value = Boolean.parseBoolean(stringValue);
				break;
			}
		}
	}
	
	
	
	
	
}
