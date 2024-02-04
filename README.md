# todoservice
Service description

| Name      | Firm     | Mobile              | Email           
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

curl -v -H "Access-Control-Request-Method: GET" -H "Origin: http://localhost:3000" -X OPTIONS https://localhost:9999/todoservice/v1/todos/ping --insecure

