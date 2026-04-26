# auditor-aware-jpa

> **End of life.** This artifact is superseded by
> [`org.opentmf.util:auditor-aware:3.0.0`](https://github.com/opentmf/auditor-aware).
> The successor drops the JPA-specific mapped superclasses, becomes
> persistence-agnostic, and ships the `AuditorAware<String>` /
> `DateTimeProvider` beans on their own — usable from JPA, Mongo, JDBC,
> WebFlux, or any context where a Spring Security principal is readable.
>
> The `2.x` line continues to receive critical fixes for one LTS window per
> opentmf policy and will be archived alongside `opentmf-versions:4.0.0`.
> No new features will land here.
>
> **To migrate:** see [MIGRATING-FROM-2.x.md](https://github.com/opentmf/auditor-aware/blob/develop/MIGRATING-FROM-2.x.md)
> in the new repo. For most callers it is six mechanical steps:
>
> 1. Replace `<artifactId>auditor-aware-jpa</artifactId>` with `<artifactId>auditor-aware</artifactId>`.
> 2. Rename imports `org.opentmf.commons.jpa.config.*` → `org.opentmf.commons.audit.*`.
> 3. Rename auto-config class references (drop `Jpa`):
>    `ServletAuditorAwareJpaAutoConfiguration` → `ServletAuditorAwareAutoConfiguration`,
>    `ReactiveAuditorAwareJpaAutoConfiguration` → `ReactiveAuditorAwareAutoConfiguration`.
> 4. Rename bean qualifiers `servletAuditorAware` / `reactiveAuditorAware` → `auditorAware`,
>    and the date-time-provider qualifiers → `auditorAwareDateTimeProvider`.
> 5. Add `@EnableJpaAuditing(auditorAwareRef = "auditorAware",
>    dateTimeProviderRef = "auditorAwareDateTimeProvider")` to your
>    `@SpringBootApplication` (the new library no longer flips this switch
>    for you). Mongo users add `@EnableMongoAuditing` with the same refs.
> 6. If your entities extended `Insertable` / `Updatable` /
>    `AuditInsertable` / `AuditUpdatable`, copy the four classes from the
>    new README's "Cookbook" section into your own package and update each
>    entity's `extends` clause. The cookbook code is verbatim what `2.x`
>    shipped — tweak column names, lengths, or `@Version` type as you see fit.

---

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
