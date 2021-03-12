package ${packageName}.<#if (entity.moduleName != "")>${entity.moduleName}.</#if>web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.lmk.core.boot.support.annotation.BootBaseService;
import com.lmk.core.boot.web.BaseController;
import ${packageName}.<#if (entity.moduleName != "")>${entity.moduleName}.</#if>entity.${entityName};
import ${packageName}.<#if (entity.moduleName != "")>${entity.moduleName}.</#if>service.${entityName}Service;

/**
 * ${entity.entityComment}控制器
 * @author ${author}
 * @email ${email}
 */
@RestController
@RequestMapping(value = "/<#if (entity.moduleName != "")>${entity.moduleName}/</#if>${entityNameLower}")
public class ${entityName}Controller extends BaseController<${entityName}> {

	@BootBaseService
    @Autowired
	${entityName}Service ${entityNameLower}Service;

	

}