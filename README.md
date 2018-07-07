# API Document Generate Spring Boot Project

## 如何使用

### 1. 编译安装

```sh
mvn clean install
```

### 2.添加依赖至WEB项目

```xml
<dependency>
    <groupId>top.webdevelop.gull</groupId>
    <artifactId>spring-boot-starter-api-doc-db</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 3. 执行数据库初始化脚本

* 运行`db/init.sql`创建数据库、创建表


### 4. WEB项目添加配置

* 在`application.properties`添加，如下：

```properties
api.doc.auto=true
api.doc.project-name=spring-boot-api-doc-db-web
api.doc.web-root-package=top.webdevelop.gull.web
api.doc.datasource.url=jdbc:mysql://127.0.0.1:3306/api_doc?useUnicode=true&characterEncoding=UTF-8&useSSL=true&verifyServerCertificate=false
api.doc.datasource.username=root
api.doc.datasource.password=xxxx
api.doc.datasource.driver-class-name=com.mysql.jdbc.Driver
#api.doc.include-bean-names=
#api.doc.include-method-names=
#api.doc.exclude-bean-names=
#api.doc.exclude-method-names=
#api.doc.output-current-path=
```

### 4. 发布`spring-boot-api-doc-db-web`至服务器

* 修改`application.yml`数据库连接配置
 
