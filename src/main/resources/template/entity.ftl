package ${packageName}.<#if (entity.moduleName != "")>${entity.moduleName}.</#if>entity;

<#if ((entity.mtms?size>0)||(entity.children?size>0))>
import java.util.List;
</#if>
<#if ((entity.mtms?size>0)||(entity.children?size>0))>
import java.util.ArrayList;
</#if>
<#if entity.hasDateType>
import java.util.Date;
</#if>
import com.lmk.core.boot.entity.IdEntity;

/**
 * ${entity.entityComment}实体
 * @author ${author}
 * @email ${email}
 */
public class ${entity.entityName} extends IdEntity {
	
	private static final long serialVersionUID = ${entity.serialVersionUID}L;
	
<#list entity.manyToOnes as manyToOne>
	/** ${manyToOne.entityComment} */
	private ${manyToOne.entityName} ${manyToOne.entityNameLower};

</#list>
<#list entity.fields as field>
	/** ${field.comment} */
	private ${field.fieldDataType} ${field.fieldName};

</#list>
<#list entity.children as child>
	/** ${child.entityComment} */
	private List<${child.entityName}> ${child.entityNameLower}s = new ArrayList<${child.entityName}>(0);

</#list>
<#list entity.mtms as manyToMany>
	/** ${manyToMany.entityComment} */
	private List<${manyToMany.entityName}> ${manyToMany.entityNameLower}s = new ArrayList<${manyToMany.entityName}>(0);
	
</#list>
	public ${entity.entityName}() {
	}

	public ${entity.entityName}(Integer id) {
		this.id = id;
	}
	
<#list entity.manyToOnes as manyToOne>
	public ${manyToOne.entityName} get${manyToOne.entityName}() {
		return this.${manyToOne.entityNameLower};
	}

	public void set${manyToOne.entityName}(${manyToOne.entityName} ${manyToOne.entityNameLower}) {
		this.${manyToOne.entityNameLower} = ${manyToOne.entityNameLower};
	}
	
</#list>
<#list entity.fields as field>
	public ${field.fieldDataType} get${field.fieldNameFirstUpper}() {
		return this.${field.fieldName};
	}

	public void set${field.fieldNameFirstUpper}(${field.fieldDataType} ${field.fieldName}) {
		this.${field.fieldName} = ${field.fieldName};
	}
	
</#list>
<#list entity.children as child>
	public List<${child.entityName}> get${child.entityName}s() {
		return ${child.entityNameLower}s;
	}

	public void set${child.entityName}s(List<${child.entityName}> ${child.entityNameLower}s) {
		this.${child.entityNameLower}s = ${child.entityNameLower}s;
	}
	
</#list>
<#list entity.mtms as manyToMany>
	public List<${manyToMany.entityName}> get${manyToMany.entityName}s() {
		return ${manyToMany.entityNameLower}s;
	}

	public void set${manyToMany.entityName}s(List<${manyToMany.entityName}> ${manyToMany.entityNameLower}s) {
		this.${manyToMany.entityNameLower}s = ${manyToMany.entityNameLower}s;
	}
	
</#list>
}