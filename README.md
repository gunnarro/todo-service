[![build and analyse](https://github.com/gunnarro/todo-service/actions/workflows/build.yml/badge.svg)](https://github.com/gunnarro/todo-service/actions/workflows/build.yml)
[![verify rest api](https://github.com/gunnarro/todo-service/actions/workflows/verify-service-rest-api.yml/badge.svg)](https://github.com/gunnarro/todo-service/actions/workflows/verify-service-rest-api.yml)

# Todo Rest Service
Service for create and handle todo lists.

| Name      | Firm     | Mobile              | Email           |
|-----------|----------|---------------------|-----------------|
| developer | Company  | myname@company.com  | +47 00 00 00 00 |

 * Check out from git:
   * git clone git@github.com:<user>/todoservice.git
   
 * mvn eclipse:clean
 * mvn eclipse:eclipse
 * open eclipse and import the project
 

Add following to the *Application.java* file:
```
static {
   System.setProperty("SERVER_IDENTITY_KEYSTORE_PATH", "config/server-identity.jks");
   System.setProperty("SERVER_IDENTITY_KEYSTORE_ALIAS", "gunnarro-microservice");
   System.setProperty("SERVER_IDENTITY_KEYSTORE_PASS", "test");
}
```

 * ```mvn clean install```
 * ```mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=sit,--spring.config.location=file:config/*```
 *  or 
 * ```java -jar -Dspring.profiles.active=sit -Dspring.config.location=file:config/ target/todoservice.jar```
 
   
[rest service api](https://localhost:xxxx/api-docs/swagger-ui.html)
[service documentation](https://github.com/gunnarro/microservice-archetype/wiki/documentation/todoservice)
	 



```
mvn clean verify -P verify-rest-api -U 
```

See result at
```
target/rest-service-api_validation_result.yaml
```

curl -v -H "Access-Control-Request-Method: GET" -H "Origin: http://localhost:3000" -X OPTIONS https://localhost:9999/todoservice/v1/todos/guro --insecure
curl -X DELETE https://localhost:9999/todoservice/v1/todos/11eec40c-c279-03fd-b266-cc2f713aeb66 -H accept: */* 

# Resources
- [TSID](https://vladmihalcea.com/tsid-identifier-jpa-hibernate/)
- [Auditing - hibernate envers](https://sunitc.dev/2020/01/21/spring-boot-how-to-add-jpa-hibernate-envers-auditing/)
- [spring boot - sunitc.dev](https://sunitc.dev/tag/spring-boot/)
- [docker compose](https://containers.dev/guide/dockerfile)

