package com.lmk.ct.bean;

public class ManyToOne {
	private String tableName;
	private String columnName;
	private String moduleName;
	private String entityName;
	private String entityComment;
	private String entityNameLower;
	
	public ManyToOne() {
		super();
	}

	public ManyToOne(String tableName, String columnName, String moduleName, String entityName, String entityNameLower) {
		super();
		this.tableName = tableName;
		this.columnName = columnName;
		this.moduleName = moduleName;
		this.entityName = entityName;
		this.entityNameLower = entityNameLower;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	public String getEntityComment() {
		return entityComment;
	}

	public void setEntityComment(String entityComment) {
		this.entityComment = entityComment;
	}

	public String getEntityNameLower() {
		return entityNameLower;
	}

	public void setEntityNameLower(String entityNameLower) {
		this.entityNameLower = entityNameLower;
	}

}
