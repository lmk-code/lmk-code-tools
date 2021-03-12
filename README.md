# LaoMake 代码生成器
这是一款快捷的代码生成器，与 `Mybatis Generator` 类似，但更加方便、易用 。

**主要的工作原理是：**

1. 解析数据库表：表名、字段名、字段的数据类型，并以此封装实体类相关信息；
2. 解析表与表的外键：封装一对多、多对一、多对多等关系；
3. 根据 `Freemarker` 模板文件输出代码。

`Freemarker` 模板文件文件位于 `/resources/template` 文件夹。项目默认提供了6个模板文件，用于演示输出基本的Java Web项目代码。**但这些模板文件并不是通用的**，使用这些模板文件生成的代码，需要配合 [**lmk-core**](https://github.com/lmk-code/lmk-core) 模块使用。**您可以自行增加、修改模板文件，以输出特定格式的代码。**

**注意：** 建议将配置文件中 `code.tablePrefix` 设置为 `true`，以开启自动解析模块名的功能。为了能正确解析模块名，表的命名应该符合 **`模块名_表名`** 格式，即将模块名作为表名的前缀。如: `admin_user` 表示 `admin` 模块下的 `user` 表、`shop_user` 表示 `shop` 模块下的 `user` 表。

如果表结构相对比较简单，

**配置：** 项目的全局配置文件是：`/resources/app.properties`，**请您重点关注：数据库相关配置、代码生成规则配置。** 其内容如下：

```properties
############ 数据库相关配置-开始（请根据实际情况修改） ############
db.host=127.0.0.1
db.port=3306
db.user=root
db.password=PASSWORD
db.database=DATABASE
############ 数据库相关配置-结束 ############

############ 代码生成规则配置-开始（请根据实际情况修改） ############
## 重要 ## Java代码注释中：作者名称
code.author=LaoMake

## 重要 ## Java代码注释中：作者邮箱
code.email=laomake@hotmail.com

## 重要 ## Java代码注释中：版本号
code.version=1.0

## 重要 ## Java代码的基础包名
code.packageName=com.lmk

## 重要 ## 是否将表名的前缀解析为包名
code.tablePrefix=false

## 是否解析、生成多对一关系
code.manyToOne=true

## 是否解析、生成多对多关系
code.manyToMany=true

## 是否解析、生成一对多关系
code.oneToMany=false
############ 代码生成规则配置-结束 ############
```









