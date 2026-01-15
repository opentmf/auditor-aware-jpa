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
    update_count int not null,
    ...
);
```
The following mapped superclasses are provided:

| Mapped Superclass | Fields                                                         |
|-------------------|----------------------------------------------------------------|
| Insertable        | created_on, update_count                                       |
| Updatable         | created_on, modified_on, update_count                          |
| AuditInsertable   | created_on, created_by, update_count                           |
| AuditUpdatable    | created_on, created_by, modified_on, modified_by, update_count |

## Usage
**Important:** This project requires Spring Boot 3.4.0 or up if you intend to use auditor-aware JPA with reactive layer.

### Maven Dependency
```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.opentmf</groupId>
      <artifactId>opentmf-versions</artifactId>
      <type>pom</type>
      <scope>import</scope>
      <version>RELEASE</version>
    </dependency>
  </dependencies>
</dependencyManagement>
```
```xml
<dependency>
  <groupId>org.opentmf.util</groupId>
  <artifactId>auditor-aware-jpa</artifactId>
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
```java
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
### 1.0.2
- Changes version field name to update_count.
- Updates spring-boot to 3.4.1
- Updates testcontainers-keycloak to 3.6.0
- Updates pia-security to 1.0.9
- Updates pia-commons tp 1.0.2
### 1.0.3
- Updates spring-boot to 3.4.4
- First open source release
### 1.0.4
- Added updatable=false to created_on and created_by fields.
### 1.0.5
- **Bug Fix**: Fixed potential NullPointerException in `ServletAuditorAwareProvider` and `ReactiveAuditorAwareProvider` when `authentication.getName()` returns null. Both providers now safely fall back to "n/a" instead of throwing NPE.
- **Test Coverage**: Added comprehensive unit tests covering all branches in both auditor aware providers, ensuring 100% code coverage.
- **Test Coverage**: Added integration tests for `AuditInsertable` mapped superclass to verify `createdBy` field is properly populated.
- **Dependencies**: Updated `openid-rbac-security` to 1.1.1 (includes fallback user claims support)
- **Dependencies**: Updated `spring-boot` to 3.5.9
- **Build**: Updated Maven plugins (compiler, deploy, enforcer, javadoc, source, release, jacoco, sonar, central-publishing) to latest versions
