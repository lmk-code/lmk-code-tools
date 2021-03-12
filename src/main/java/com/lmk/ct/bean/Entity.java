package com.lmk.ct.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体类
 * @author LaoMake
 *
 */
public class Entity {
	private String serialVersionUID;
	private String tableName;
	private String moduleName;
	private String entityName;
	private String entityNameLower;
	private String entityComment;
	private DbField primaryKey;
	private Boolean hasDateType; 
	private List<DbField> fields;
	private List<ManyToMany> mtms = new ArrayList<ManyToMany>();
	private List<ManyToOne> manyToOnes = new ArrayList<ManyToOne>();
	private List<Entity> children = new ArrayList<Entity>();
	private Map<String, String> childMap = new HashMap<String, String>();
	
	public Entity() {
	}
	
	public Entity(String tableName, String tableComment, String moduleName, String entityName, String entityNameLower, Boolean hasDateType, List<DbField> fields, List<ManyToMany> mtmes, List<ManyToOne> manyToOnes, List<Entity> children) {
		super();
		this.tableName = tableName;
		this.entityComment = tableComment;
		this.moduleName = moduleName;
		this.entityName = entityName;
		this.entityNameLower = entityNameLower;
		this.hasDateType = hasDateType;
		this.fields = fields;
		this.mtms = mtmes;
		this.manyToOnes = manyToOnes;
		this.children = children;
	}
	
	public String getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setSerialVersionUID(String serialVersionUID) {
		this.serialVersionUID = serialVersionUID;
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
	
	public DbField getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(DbField primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Boolean getHasDateType() {
		return hasDateType;
	}

	public void setHasDateType(Boolean hasDateType) {
		this.hasDateType = hasDateType;
	}

	public List<DbField> getFields() {
		return fields;
	}

	public void setFields(List<DbField> fields) {
		this.fields = fields;
	}
	
	public List<ManyToMany> getMtms() {
		return mtms;
	}

	public void setMtms(List<ManyToMany> mtms) {
		this.mtms = mtms;
	}

	public List<ManyToOne> getManyToOnes() {
		return manyToOnes;
	}

	public void setManyToOnes(List<ManyToOne> manyToOnes) {
		this.manyToOnes = manyToOnes;
	}

	public List<Entity> getChildren() {
		return children;
	}

	public void setChildren(List<Entity> children) {
		this.children = children;
	}

	public Map<String, String> getChildMap() {
		return childMap;
	}

	public void setChildMap(Map<String, String> childMap) {
		this.childMap = childMap;
	}
}
