package com.lmk.ct.bean;

public class ForeignKey {
	private String columnName;
	private String sourceTableName;
	private String targetTableName;
	
	public ForeignKey() {
		super();
	}
	
	public ForeignKey(String columnName, String sourceTableName,
			String targetTableName) {
		super();
		this.columnName = columnName;
		this.sourceTableName = sourceTableName;
		this.targetTableName = targetTableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getSourceTableName() {
		return sourceTableName;
	}

	public void setSourceTableName(String sourceTableName) {
		this.sourceTableName = sourceTableName;
	}

	public String getTargetTableName() {
		return targetTableName;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}
	
}
