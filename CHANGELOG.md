# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.1/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.0.0] - unreleased

### Changed
- Upgraded to Spring Boot 4.0.4 (Spring Framework 7, Spring Security 7, Hibernate 7.1, Jakarta Persistence 3.2, Jackson 3).
- Requires `openid-rbac-security` 2.0.0 and `opentmf-commons` 2.1.0.
- Renamed starter dependency from `spring-boot-starter-web` to `spring-boot-starter-webmvc`.
- Migrated nullability annotations from `org.springframework.lang.NonNull` to `org.jspecify.annotations.NonNull`.
- Adapted to Spring Boot 4 modularization — updated auto-configuration class references (`DataJpaRepositoriesAutoConfiguration`, `OrderedWebFilter` package).
- Upgraded Testcontainers to 2.0 (managed by Spring Boot BOM).

## [1.0.5]

### Fixed
- Fixed potential NullPointerException in `ServletAuditorAwareProvider` and `ReactiveAuditorAwareProvider` when `authentication.getName()` returns null. Both providers now safely fall back to "n/a" instead of throwing NPE.

### Added
- Comprehensive unit tests covering all branches in both auditor aware providers, ensuring 100% code coverage.
- Integration tests for `AuditInsertable` mapped superclass to verify `createdBy` field is properly populated.

### Changed
- Updated `openid-rbac-security` to 1.1.1 (includes fallback user claims support).
- Updated `spring-boot` to 3.5.9.
- Updated Maven plugins (compiler, deploy, enforcer, javadoc, source, release, jacoco, sonar, central-publishing) to latest versions.

## [1.0.4]

### Changed
- Added `updatable=false` to `created_on` and `created_by` fields.

## [1.0.3]

### Changed
- Updated `spring-boot` to 3.4.4.
- First open source release.

## [1.0.2]

### Changed
- Renamed version field to `update_count`.
- Updated `spring-boot` to 3.4.1.
- Updated `testcontainers-keycloak` to 3.6.0.
- Updated `pia-security` to 1.0.9.
- Updated `pia-commons` to 1.0.2.

## [1.0.1]

### Changed
- Updated `pia-security` to 1.0.7.

## [1.0.0]

Initial release.
