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
### Maven Dependency
```xml
<dependency>
  <groupId>com.pia.commons</groupId>
  <artifactId>pia-commons-jpa</artifactId>
</dependency>
```

### Inherit from a Mapped Superclass
```sql92
@Entity
@Table
public class SomeEntity extends AuditUpdatable {
  ...
}
```
## Version History
### 1.0.0
- Initial Version