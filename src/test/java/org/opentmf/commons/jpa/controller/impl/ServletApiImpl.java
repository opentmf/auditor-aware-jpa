package org.opentmf.commons.jpa.controller.impl;

import org.opentmf.commons.jpa.controller.api.ServletApi;
import org.opentmf.commons.jpa.model.CarCreateDto;
import org.opentmf.commons.jpa.model.CarDto;
import org.opentmf.commons.jpa.model.CarUpdateDto;
import org.opentmf.commons.jpa.service.api.PersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gokhan Demir
 */
@RestController
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = Type.SERVLET)
public class ServletApiImpl implements ServletApi {

  private final PersistenceService persistenceService;

  @Override
  public CarDto postCar(CarCreateDto car, String authorization) {
    return persistenceService.save(car);
  }

  @Override
  public CarDto patchCar(CarUpdateDto car, String model, String authorization) {
    return persistenceService.update(model, car);
  }
}
