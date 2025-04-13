package org.opentmf.commons.jpa.tests;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.opentmf.security.model.OpenTmfSecurityProperties;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Gokhan Demir
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
@TestInstance(Lifecycle.PER_CLASS)
abstract class AuditorAwareTestBase {

  @Autowired private OpenTmfSecurityProperties openTmfSecurityProperties;

  static final String MERCEDES = """
      {
        "model":"Mercedes",
        "color":"Green",
        "builtYear":2023
      }
      """;

  static final String PATCH_MERCEDES = """
      {
        "color":"Yellow",
        "builtYear":2023
      }
      """;

  static void startKeycloak(int publicPort) {
    @SuppressWarnings("resource")
    KeycloakContainer keycloakContainer = new KeycloakContainer().withRealmImportFile(
        "realm/rehearsal-realm.json");
    keycloakContainer.setPortBindings(List.of(publicPort + ":8080"));
    keycloakContainer.start();
  }

  URI getTokenUri() {
    try {
      var jwkSetUri = openTmfSecurityProperties.getJwkSetUri().getURL().toString();
      return URI.create(jwkSetUri.substring(0, jwkSetUri.lastIndexOf('/') + 1) + "token");
    } catch (IOException e) {
      throw new IllegalArgumentException("jwk-set-uri is not a valid URL");
    }
  }
}
