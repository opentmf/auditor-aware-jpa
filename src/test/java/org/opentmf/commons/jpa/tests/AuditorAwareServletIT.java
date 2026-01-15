package org.opentmf.commons.jpa.tests;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.opentmf.commons.jpa.model.CarDto;
import org.opentmf.commons.jpa.repository.LogEntryRepository;
import org.opentmf.commons.jpa.repository.entity.LogEntry;
import org.opentmf.commons.jpa.service.api.TokenService;
import org.opentmf.commons.util.JacksonUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("servlet")
@AutoConfigureMockMvc
class AuditorAwareServletIT extends AuditorAwareTestBase {

  static {
    startKeycloak(8092);
  }

  @Autowired private TokenService servletTokenService;
  @Autowired WebApplicationContext webApplicationContext;
  @Autowired private MockMvc mockMvc;
  @Autowired private LogEntryRepository logEntryRepository;

  @BeforeAll
  void beforeAll() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(springSecurity())
        .build();
  }

  @Test
  void testPostAndPatch_withValidToken_returnsValidResult() throws Exception {
    String token = servletTokenService.getToken(getTokenUri(), "write");
    Assertions.assertNotNull(token);
    mockMvc.perform(postMercedesBuilder(token))
        .andExpect(status().isCreated())
        .andExpect(result -> {
          var json = result.getResponse().getContentAsString();
          var car = JacksonUtil.jsonToObject(json, CarDto.class);
          Assertions.assertEquals("writer@pia-team.com", car.getCreatedBy());
          Assertions.assertNull(car.getModifiedBy());
        });
    mockMvc.perform(patchMercedesBuilder(token))
        .andExpect(status().isOk())
        .andExpect(result -> {
          var json = result.getResponse().getContentAsString();
          var car = JacksonUtil.jsonToObject(json, CarDto.class);
          Assertions.assertEquals("writer@pia-team.com", car.getCreatedBy());
          Assertions.assertEquals("writer@pia-team.com", car.getModifiedBy());
        });
  }

  private static @NotNull MockHttpServletRequestBuilder postMercedesBuilder(String token) {
    return MockMvcRequestBuilders.post("/car")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(MERCEDES);
  }

  private static @NotNull MockHttpServletRequestBuilder patchMercedesBuilder(String token) {
    return MockMvcRequestBuilders.patch("/car/Mercedes")
        .header("Authorization", "Bearer " + token)
        .contentType("application/merge-patch+json")
        .content(PATCH_MERCEDES);
  }

  @Test
  @Transactional
  void testAuditInsertable_createsEntityWithCreatedBy() {
    // Given: a LogEntry entity that extends AuditInsertable
    LogEntry logEntry = new LogEntry();
    logEntry.setId("log-001");
    logEntry.setMessage("Test log message");
    logEntry.setLevel("INFO");

    // When: saving the entity
    LogEntry saved = logEntryRepository.save(logEntry);

    // Then: createdBy should be populated by the auditor aware provider
    Assertions.assertNotNull(saved.getCreatedBy());
    Assertions.assertNotNull(saved.getCreatedOn());
    Assertions.assertEquals(0, saved.getUpdateCount());
    // Verify it doesn't have modifiedBy (since it extends AuditInsertable, not AuditUpdatable)
    // Note: We can't directly check for the absence of modifiedBy field, but we can verify
    // that the entity was created successfully with createdBy populated
    Assertions.assertEquals("log-001", saved.getId());
    Assertions.assertEquals("Test log message", saved.getMessage());
    Assertions.assertEquals("INFO", saved.getLevel());
  }
}
