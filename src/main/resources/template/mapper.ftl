<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.<#if (entity.moduleName != "")>${entity.moduleName}.</#if>dao.${entity.entityName}Dao">

	<!-- 查询字段 -->
	<sql id="selectFields">${entity.primaryKey.tableFieldName}, <#list entity.manyToOnes as manyToOne>${manyToOne.columnName}, </#list><#list entity.fields as field>${field.tableFieldName}<#if (field_has_next)>, </#if></#list></sql>

	<!-- 新增 -->
	<insert id="insert" useGeneratedKeys="true" keyProperty="${entity.primaryKey.tableFieldName}">
		INSERT INTO
			${entity.tableName} (<#list entity.manyToOnes as manyToOne>${manyToOne.columnName}, </#list><#list entity.fields as field>${field.tableFieldName}<#if (field_has_next)>, </#if></#list>)
		VALUES
			(<#list entity.manyToOnes as manyToOne>${r"#{"}${manyToOne.entityNameLower}${r".id}"}, </#list><#list entity.fields as field>${r"#{"}${field.fieldName}${r"}"}<#if (field_has_next)>, </#if></#list>)
	</insert>
	<#if (entity.mtms?size>0)>
		<#list entity.mtms as manyToMany>
		
	<!-- 插入${manyToMany.entityComment}关联 -->
	<insert id="add${manyToMany.entityName}">
		INSERT INTO
			${manyToMany.joinTableName} (${manyToMany.sourceColumnName}, ${manyToMany.targetColumnName})
		VALUES
			(${r"#{param1}"}, ${r"#{param2}"})
	</insert>
		</#list>
	</#if>
	
	<!-- 按ID删除 -->
	<delete id="deleteById">
		DELETE FROM
			${entity.tableName}
		WHERE
			${entity.primaryKey.tableFieldName} = ${r"#{"}${entity.primaryKey.fieldName}${r"}"}
	</delete>
	
	<!-- 更新 -->
	<update id="update">
		UPDATE
			${entity.tableName}
		<set>
		<#list entity.manyToOnes as manyToOne>
			${r"<if"} test="${manyToOne.entityNameLower} != null"${r">"}
			${manyToOne.columnName} = ${r"#{"}${manyToOne.entityNameLower}${r".id}"},
			${r"</if>"}
		</#list>
		<#list entity.fields as field>
			${r"<if"} test="${field.fieldName} != null"${r">"}
			${field.tableFieldName} = ${r"#{"}${field.fieldName}${r"}"}<#if (field_has_next)>,</#if>
			${r"</if>"}
		</#list>
		</set>
		WHERE
			${entity.primaryKey.tableFieldName} = ${r"#{"}${entity.primaryKey.fieldName}${r"}"}
	</update>

	<!-- 更新状态 -->
	<update id="updateStatus">
		UPDATE
			${entity.tableName}
		SET
			status = ${r"#{"}status${r"}"}
		WHERE
			id = ${r"#{"}id${r"}"}
	</update>
	
	<!-- 按ID查询 -->
	<#if (entity.manyToOnes?size>0 || entity.children?size>0 || entity.mtms?size>0 )><select id="selectOne" resultMap="${entity.entityNameLower}Result"><#else><select id="selectOne" resultType="${packageName}.<#if (entity.moduleName != "")>${entity.moduleName}.</#if>entity.${entity.entityName}"></#if>
		SELECT
			${entity.primaryKey.tableFieldName},
		<#list entity.manyToOnes as manyToOne>
			${manyToOne.columnName},
		</#list>
		<#list entity.fields as field>
			${field.tableFieldName}<#if (field_has_next)>,</#if>
		</#list>
		FROM
			${entity.tableName}
		WHERE
			${entity.primaryKey.tableFieldName} = ${r"#{"}${entity.primaryKey.fieldName}${r"}"}
	</select>

	<!-- 分页查询 -->
	<#if (entity.manyToOnes?size>0 || entity.children?size>0 || entity.mtms?size>0 )><select id="pageList" resultMap="${entity.entityNameLower}Result"><#else><select id="pageList" resultType="${packageName}.<#if (entity.moduleName != "")>${entity.moduleName}.</#if>entity.${entity.entityName}"></#if>
		SELECT
			${entity.primaryKey.tableFieldName},
		<#list entity.manyToOnes as manyToOne>
			${manyToOne.columnName},
		</#list>
		<#list entity.fields as field>
			${field.tableFieldName}<#if (field_has_next)>,</#if>
		</#list>
		FROM
			${entity.tableName}
		<if test="search != null">
		WHERE
			${r"${search}"}
		</if>
		ORDER BY ${r"${sort}"}
		LIMIT ${r"#{offset}"}, ${r"#{size}"}
	</select>

	<!-- 列表查询 -->
	<#if (entity.manyToOnes?size>0 || entity.children?size>0 || entity.mtms?size>0 )><select id="loadList" resultMap="${entity.entityNameLower}Result"><#else><select id="loadList" resultType="${packageName}.<#if (entity.moduleName != "")>${entity.moduleName}.</#if>entity.${entity.entityName}"></#if>
		SELECT
			${entity.primaryKey.tableFieldName},
		<#list entity.manyToOnes as manyToOne>
			${manyToOne.columnName},
		</#list>
		<#list entity.fields as field>
			${field.tableFieldName}<#if (field_has_next)>,</#if>
		</#list>
		FROM
			${entity.tableName}
		<if test="search != null">
		WHERE
			${r"${search}"}
		</if>
		ORDER BY ${r"${sort}"}
	</select>

	<#if (entity.manyToOnes?size>0 || entity.children?size>0 || entity.mtms?size>0 )>
	<!-- 查询结果  -->
	<resultMap id="${entity.entityNameLower}Result" type="${packageName}.<#if (entity.moduleName != "")>${entity.moduleName}.</#if>entity.${entity.entityName}">
		<result property="id" column="${entity.primaryKey.tableFieldName}" />
	<#list entity.fields as field>
		<result property="${field.fieldName}" column="${field.tableFieldName}" />
	</#list>
		
		<#list entity.manyToOnes as manyToOne>
		<association property="${manyToOne.entityNameLower}" javaType="${packageName}.<#if (entity.moduleName != "")>${manyToOne.moduleName}.</#if>entity.${manyToOne.entityName}">
			<result property="id" column="${manyToOne.columnName}" />
		</association>
		
		</#list>
	</resultMap>
	</#if>
	
	<!-- 统计查询 -->
	<select id="count" resultType="int">
		SELECT
			COUNT(${entity.primaryKey.tableFieldName})
		FROM
			${entity.tableName}
		<if test="search != null">
		WHERE
			${r"${search}"}
		</if>
	</select>
	
</mapper>