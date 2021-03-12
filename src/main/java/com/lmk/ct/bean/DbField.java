package com.lmk.ct.bean;

/**
 * 表的字段信息
 * @author LaoMake
 *
 */
public class DbField {

	private String fieldName;
	private String fieldNameFirstUpper;
	private String tableFieldName;
	private KeyType keyType = KeyType.NotKey;
	private Integer length = 0;
	private FieldDataType fieldDataType = FieldDataType.String;
	private Boolean notNull = false;
	private String defaultValue = "";
	private String comment = "";
	
	public DbField() {
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getFieldNameFirstUpper() {
		return fieldNameFirstUpper;
	}

	public void setFieldNameFirstUpper(String fieldNameFirstUpper) {
		this.fieldNameFirstUpper = fieldNameFirstUpper;
	}

	
	public String getTableFieldName() {
		return tableFieldName;
	}

	public void setTableFieldName(String tableFieldName) {
		this.tableFieldName = tableFieldName;
	}

	public KeyType getKeyType() {
		return keyType;
	}

	public void setKeyType(KeyType keyType) {
		this.keyType = keyType;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public FieldDataType getFieldDataType() {
		return fieldDataType;
	}

	public void setFieldDataType(FieldDataType fieldDataType) {
		this.fieldDataType = fieldDataType;
	}

	public Boolean getNotNull() {
		return notNull;
	}

	public void setNotNull(Boolean notNull) {
		this.notNull = notNull;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
