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
**Important:** Version 2.x requires Spring Boot 4.0.4 or later. For Spring Boot 3.x, use version 1.x.

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
### Use With QueryDSL
If your project uses QueryDSL, the annotation processor needs to know about the mapped superclasses from this library. Create a `package-info.java` in the package where your entity classes reside:
```java
@QueryEntities({
    Insertable.class,
    Updatable.class,
    AuditInsertable.class,
    AuditUpdatable.class})
package com.example.repository.entity;

import com.querydsl.core.annotations.QueryEntities;
import org.opentmf.commons.jpa.entity.AuditInsertable;
import org.opentmf.commons.jpa.entity.AuditUpdatable;
import org.opentmf.commons.jpa.entity.Insertable;
import org.opentmf.commons.jpa.entity.Updatable;
```
You only need to list the superclasses you actually extend. Without this, QueryDSL will not generate the `Q` types for the inherited fields.

## Changelog
See [CHANGELOG.md](CHANGELOG.md) for version history.
