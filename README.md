# auditor-aware-jpa
Auditor aware mapped superclasses for JPA.

If your project:
- uses JPA for persistence layer
- exposes API endpoints using either reactive or servlet web application
- and Spring Security is enabled

Then this library autoconfigures auditor aware provider and supports the use of `@CreatedBy` and `@LastModifiedBy` annotations.

The following is a table creation snippet for the 5 fields, which are provided via different mapped superclasses of developer's choice:

```sql92
-- SQL92 sample
create table FOO (
    ...
    created_on timestamp not null,
    created_by varchar(100) not null,
    modified_on timestamp,
    modified_by varchar(100),
    version int not null,
    ...
);
```
The following mapped superclasses are provided:

| Mapped Superclass | Fields                                                    |
|-------------------|-----------------------------------------------------------|
| Insertable        | created_on, version                                       |
| Updatable         | created_on, modified_on, version                          |
| AuditInsertable   | created_on, created_by, version                           |
| AuditUpdatable    | created_on, created_by, modified_on, modified_by, version |

## Usage
**Important:** This project requires Spring Boot 3.4.0 or up if you intend to use auditor-aware JPA with reactive layer.

### Maven Dependency
```xml
<dependency>
  <groupId>com.pia.commons</groupId>
  <artifactId>pia-commons-jpa</artifactId>
</dependency>
```
### application.yaml (Optional)
In this library, there are two different auto-configurations depending on the web application type. Normally, Spring Boot will detect your web application type dynamically depending on your classpath and exposed API endpoints. However, if you want no surprises, you can explicitly specify the web application type in application configuration. Example:
```yaml
spring:
  main:
    web-application-type: servlet
```
Or
```yaml
spring:
  main:
    web-application-type: reactive
```
### Inherit from a Mapped Superclass
```sql92
@Entity
@Table
public class SomeEntity extends AuditUpdatable {
  ...
}
```
### Use With `DataJpaTest`
Depending on your web-application-type, import correct autoconfiguration class. Either:
```java
@DataJpaTest
@Import(ServletAuditorAwareJpaAutoConfiguration.class)
...
```
Or
```java
@DataJpaTest
@Import(ReactiveAuditorAwareJpaAutoConfiguration.class)
...
```
## Version History
### 1.0.0
- Initial Version
### 1.0.1
- Updates pia-security to 1.0.7
