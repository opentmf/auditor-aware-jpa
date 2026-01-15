package org.opentmf.commons.jpa.tests;

import org.opentmf.commons.jpa.model.CarDto;
import org.opentmf.commons.jpa.repository.LogEntryRepository;
import org.opentmf.commons.jpa.repository.entity.LogEntry;
import org.opentmf.commons.jpa.service.api.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("reactive")
class AuditorAwareReactiveIT extends AuditorAwareTestBase {

  static {
    startKeycloak(8091);
  }

  @Autowired private TokenService reactiveTokenService;
  @Autowired ApplicationContext applicationContext;
  @Autowired private LogEntryRepository logEntryRepository;

  private WebTestClient webTestClient;

  @BeforeAll
  void beforeAll() {
    webTestClient = WebTestClient
        .bindToApplicationContext(applicationContext)
        .apply(SecurityMockServerConfigurers.springSecurity())
        .configureClient()
        .build();
  }

  @Test
  void testPostAndPatch_withValidToken_returnsValidResult() {
    var token = reactiveTokenService.getToken(getTokenUri(), "write");
    Assertions.assertNotNull(token);
    CarDto car = postMercedes(token)
        .expectStatus().isCreated()
        .expectBody(CarDto.class).returnResult().getResponseBody();
    Assertions.assertNotNull(car);
    Assertions.assertEquals("writer@pia-team.com", car.getCreatedBy());
    Assertions.assertNull(car.getModifiedBy());
    CarDto car2 = patchMercedes(token)
        .expectStatus().isOk()
        .expectBody(CarDto.class).returnResult().getResponseBody();
    Assertions.assertNotNull(car2);
    Assertions.assertEquals("writer@pia-team.com", car2.getCreatedBy());
    Assertions.assertEquals("writer@pia-team.com", car2.getModifiedBy());
  }

  private ResponseSpec postMercedes(String accessToken) {
    return webTestClient.post()
        .uri("/car")
        .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(MERCEDES)
        .exchange();
  }

  private ResponseSpec patchMercedes(String accessToken) {
    return webTestClient.patch()
        .uri("/car/Mercedes")
        .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
        .contentType(MediaType.valueOf("application/merge-patch+json"))
        .bodyValue(PATCH_MERCEDES)
        .exchange();
  }

  @Test
  @Transactional
  void testAuditInsertable_createsEntityWithCreatedBy() {
    // Given: a LogEntry entity that extends AuditInsertable
    LogEntry logEntry = new LogEntry();
    logEntry.setId("log-002");
    logEntry.setMessage("Test log message for reactive");
    logEntry.setLevel("WARN");

    // When: saving the entity
    LogEntry saved = logEntryRepository.save(logEntry);

    // Then: createdBy should be populated by the auditor aware provider
    Assertions.assertNotNull(saved.getCreatedBy());
    Assertions.assertNotNull(saved.getCreatedOn());
    Assertions.assertEquals(0, saved.getUpdateCount());
    // Verify it doesn't have modifiedBy (since it extends AuditInsertable, not AuditUpdatable)
    // Note: We can't directly check for the absence of modifiedBy field, but we can verify
    // that the entity was created successfully with createdBy populated
    Assertions.assertEquals("log-002", saved.getId());
    Assertions.assertEquals("Test log message for reactive", saved.getMessage());
    Assertions.assertEquals("WARN", saved.getLevel());
  }
}
