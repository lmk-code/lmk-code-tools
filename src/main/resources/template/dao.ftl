package ${packageName}.<#if (entity.moduleName != "")>${entity.moduleName}.</#if>dao;

import org.springframework.stereotype.Repository;
import com.lmk.core.boot.dao.BaseDao;
import ${packageName}.<#if (entity.moduleName != "")>${entity.moduleName}.</#if>entity.${entityName};

/**
 * ${entity.entityComment}数据访问层
 * @author ${author}
 * @email ${email}
 */
@Repository
public interface ${entityName}Dao extends BaseDao<${entityName}> {
	
}