package com.pia.commons.jpa.tests;

import com.pia.commons.jpa.model.CarDto;
import com.pia.commons.jpa.service.api.TokenService;
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

@ActiveProfiles("reactive")
class AuditorAwareReactiveIT extends AuditorAwareTestBase {

  static {
    startKeycloak(8091);
  }

  @Autowired private TokenService reactiveTokenService;
  @Autowired ApplicationContext applicationContext;

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
    return webTestClient.post()
        .uri("/car/Mercedes")
        .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(PATCH_MERCEDES)
        .exchange();
  }
}
