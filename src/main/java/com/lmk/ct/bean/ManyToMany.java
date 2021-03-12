package com.lmk.ct.bean;

public class ManyToMany {
	private String databaseName;
	private String joinTableName;
	private String tableName;
	private String moduleName;
	private String entityName;
	private String entityNameLower;
	private String entityComment;
	private String sourceColumnName;
	private String targetColumnName;
	
	public ManyToMany() {
		super();
	}

	public ManyToMany(String databaseName, String joinTableName, String tableName, String moduleName, String entityName, String entityNameLower, String entityComment, String sourceColumnName, String targetColumnName) {
		super();
		this.databaseName = databaseName;
		this.joinTableName = joinTableName;
		this.tableName = tableName;
		this.moduleName = moduleName;
		this.entityName = entityName;
		this.entityNameLower = entityNameLower;
		this.entityComment = entityComment;
		this.sourceColumnName = sourceColumnName;
		this.targetColumnName = targetColumnName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getJoinTableName() {
		return joinTableName;
	}

	public void setJoinTableName(String joinTableName) {
		this.joinTableName = joinTableName;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
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

	public String getEntityNameLower() {
		return entityNameLower;
	}

	public void setEntityNameLower(String entityNameLower) {
		this.entityNameLower = entityNameLower;
	}

	public String getEntityComment() {
		return entityComment;
	}

	public void setEntityComment(String entityComment) {
		this.entityComment = entityComment;
	}

	public String getSourceColumnName() {
		return sourceColumnName;
	}

	public void setSourceColumnName(String sourceColumnName) {
		this.sourceColumnName = sourceColumnName;
	}

	public String getTargetColumnName() {
		return targetColumnName;
	}

	public void setTargetColumnName(String targetColumnName) {
		this.targetColumnName = targetColumnName;
	}
	
}
