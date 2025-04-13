package org.opentmf.commons.jpa.controller.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.opentmf.commons.jpa.model.CarCreateDto;
import org.opentmf.commons.jpa.model.CarDto;
import org.opentmf.commons.jpa.model.CarUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

/**
 * @author Gokhan Demir
 */
public interface ReactiveApi {

  @PostMapping(
      path = "/car",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody Mono<CarDto> postCar(
      @RequestBody CarCreateDto car,
      @RequestHeader(value = "Authorization") String authorization);

  @PatchMapping(
      path = "/car/{model}",
      consumes = "application/merge-patch+json",
      produces = APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody Mono<CarDto> patchCar(
      @RequestBody CarUpdateDto car,
      @PathVariable("model") String model,
      @RequestHeader(value = "Authorization") String authorization);
}
