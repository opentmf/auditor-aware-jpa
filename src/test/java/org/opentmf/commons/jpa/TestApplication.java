package org.opentmf.commons.jpa;

import org.opentmf.commons.jpa.model.TokenProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(TokenProperties.class)
@ComponentScan(basePackages = {"org.opentmf.security", "org.opentmf.commons.jpa"})
public class TestApplication {

  public static void main(String[] args) {
    SpringApplication.run(TestApplication.class, args);
  }
}
