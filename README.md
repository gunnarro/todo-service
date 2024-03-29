[![build and analyse](https://github.com/gunnarro/todo-service/actions/workflows/build.yml/badge.svg)](https://github.com/gunnarro/todo-service/actions/workflows/build.yml)
[![verify rest api](https://github.com/gunnarro/todo-service/actions/workflows/verify-service-rest-api.yml/badge.svg)](https://github.com/gunnarro/todo-service/actions/workflows/verify-service-rest-api.yml)
[![build and deploy image to docker.hub](https://github.com/gunnarro/todo-service/actions/workflows/deploy-docker-hub.yml/badge.svg)](https://github.com/gunnarro/todo-service/actions/workflows/deploy-docker-hub.yml)

# Todo Rest Service
Service for create and handle todo lists.

| Name      | Firm     | Mobile          | Email               |
|-----------|----------|-----------------|---------------------|
| developer | Company  | +47 00 00 00 00 | myname@company.com  |

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

## API definition
```
curl -v -u "my-service-name:change-me" --insecure https://localhost:9999/v3/api-docs | json_pp >todo-service-api.yaml
```

## Service call
```
curl -v -H "Access-Control-Request-Method: GET" -H "Origin: http://localhost:3000" -X OPTIONS https://localhost:9999/todoservice/v1/todos/guro --insecure
curl -X DELETE https://localhost:9999/todoservice/v1/todos/11eec40c-c279-03fd-b266-cc2f713aeb66 -H accept: */* 
```

## Docker

### Build with docker
Create docker image:
```
docker run -d -p 9999:9999 --name todo-service todo-service:latest
```
where 'name' is the container name, if omitted, docker wil generate a container name.

Push to docker hub:
```
docker push gunnarro/todo-service:latest
```

Run docker image:
```
docker run -d -p 9999:9999 --name todo-service todo-service:latest
```
or get docker image from docker hub (deployed by the project github action).
By default, docker pull pulls images from Docker Hub.
```
docker pull gunnarro/todo-service:latest
or 
docker image pull docker.io/gunnarro/todo-service:latest
then run
docker run ....
```

### Build with jib
NB! Must add DOCKER_HUB_USER and DOCKER_HUB_PWD to environment or add to the .bash_profile ( on ubuntu .profile ) file
```
mvn compile jib:build -P build-docker -Djib.to.auth.username=$DOCKER_HUB_USER -Djib.to.auth.password=$DOCKER_HUB_PWD
```

## Test
### Fuzz testing
- [Snyk fuzzing tools](https://snyk.io/blog/fuzzing-tools-in-your-security-toolkit/)
- [RESTler fuzzing tool](https://github.com/microsoft/restler-fuzzer)
- [rest-api-fuzz-testing](https://github.com/microsoft/rest-api-fuzz-testing)

```
sudo ./restler-quick-start.py --api_spec_path /home/gunnarro/code/github/todo-service/docs/openapi/todo-service-api.yaml --restler_drop_dir /home/gunnarro/code/github/restler-fuzzer/test/todo_service
```

# Resources
- [TSID](https://vladmihalcea.com/tsid-identifier-jpa-hibernate/)
- [hibernate envers doc](https://docs.jboss.org/envers/docs/)
- [Auditing - hibernate envers](https://sunitc.dev/2020/01/21/spring-boot-how-to-add-jpa-hibernate-envers-auditing/)
- [Auditing - javers](https://javers.org/documentation/spring-boot-integration/)
- [Auditing - javers vs envers](https://javers.org/blog/2017/12/javers-vs-envers-comparision.html)
- [spring boot - sunitc.dev](https://sunitc.dev/tag/spring-boot/)
- [docker compose](https://containers.dev/guide/dockerfile)
- [react bootstrap form validation](https://www.abstractapi.com/guides/react-bootstrap-form-validation)
- [reactstrap](https://www.npmjs.com/package/reactstrap)
- [openapi-generator](https://github.com/OpenAPITools/openapi-generator)
- [Bucket4j rate limiting](https://www.baeldung.com/spring-bucket4j)
  - [bucket4j 8.9.0 doc](https://bucket4j.com/8.9.0/toc.html)


## Database
[liquibase json format](https://docs.liquibase.com/concepts/changelogs/json-format.html)


### Meny
https://meny.no/varer/ost/brunost/gudbrandsdalsost-7038010029530
```
{
  "@context": "http://www.schema.org",
  "@type": "product",
  "name": "Gudbrandsdalsost",
  "logo": "",
  "sku": "7038010029530",
  "mpn": null,
  "brand": {
    "@type": "Thing",
    "name": "Tine"
  },
  "manufacturer": {
    "@type": "Organization",
    "name": "Tine sa"
  },
  "image": [
    "https://bilder.ngdata.no/7038010029530/meny/large.jpg"
  ],
  "description": "G35 skivet 130g Tine",
  "gtin13": "7038010029530",
  "review": null,
  "aggregateRating": null,
  "video": null,
  "offers": {
    "@type": "Offer",
    "url": null,
    "availability": "http://schema.org/InStock",
    "priceCurrency": "NOK",
    "itemCondition": null,
    "price": "23.5",
    "selller": null
  },
  "category": "Ost",
  "url": "https://meny.no/varer/ost/brunost/gudbrandsdalsost-7038010029530"
}
```
### Oda
https://oda.com/no/products/438-tine-skivet-gudbrandsdalsost/
```
{
  "@context": "https://schema.org",
  "@type": "Product",
  "name": "Skivet Gudbrandsdalsost",
  "image": [
    "https://bilder.kolonial.no/local_products/ba33e946-f1f7-46bf-8ba1-fcf19e330549.jpg?auto=format&amp;fit=max&amp;w=752&amp;s=7e9251de247d971d4417179fcbea1f04"
  ],
  "brand": "Tine",
  "offers": {
    "@type": "Offer",
    "url": "https://oda.com/no/products/438-tine-skivet-gudbrandsdalsost/",
    "itemCondition": "https://schema.org/NewCondition",
    "priceCurrency": "NOK",
    "price": "21.20",
    "availability": "https://schema.org/InStock"
  }
}
```

### Spar
https://spar.no/nettbutikk/varer/drikke/brus/coca-cola-5000112636864
```
{
  "@context": "http://www.schema.org",
  "@type": "product",
  "name": "Coca-Cola",
  "logo": "",
  "sku": "5000112636864",
  "mpn": null,
  "brand": {
    "@type": "Thing",
    "name": "Coca-cola"
  },
  "manufacturer": {
    "@type": "Organization",
    "name": "Coca-cola europacific partners norge as"
  },
  "image": [
    "https://bilder.ngdata.no/5000112636864/kmh/large.jpg?fallback=404&foretrekk=bedark"
  ],
  "description": "1,5l flaske",
  "gtin13": "5000112636864",
  "review": null,
  "aggregateRating": null,
  "video": null,
  "offers": {
    "@type": "Offer",
    "url": null,
    "availability": "http://schema.org/InStock",
    "priceCurrency": "NOK",
    "itemCondition": null,
    "price": "38.9",
    "selller": null
  },
  "category": "Drikke",
  "url": "https://spar.no/nettbutikk/varer/drikke/brus/coca-cola-5000112636864"
}
```
