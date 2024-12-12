package com.pia.commons.jpa.tests;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pia.commons.jpa.model.CarDto;
import com.pia.commons.jpa.service.api.TokenService;
import com.pia.commons.util.JacksonUtil;
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
}
