package org.opentmf.commons.jpa.controller.impl;

import org.opentmf.commons.jpa.controller.api.ReactiveApi;
import org.opentmf.commons.jpa.model.CarCreateDto;
import org.opentmf.commons.jpa.model.CarDto;
import org.opentmf.commons.jpa.model.CarUpdateDto;
import org.opentmf.commons.jpa.service.api.PersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Gokhan Demir
 */
@RestController
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = Type.REACTIVE)
public class ReactiveApiImpl implements ReactiveApi {

  private final PersistenceService persistenceService;

  @Override
  public Mono<CarDto> postCar(CarCreateDto car, String authorization) {
    return Mono.just(persistenceService.save(car));
  }

  @Override
  public Mono<CarDto> patchCar(CarUpdateDto car, String model, String authorization) {
    return Mono.just(persistenceService.update(model, car));
  }
}
