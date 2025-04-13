package org.opentmf.commons.jpa.service.api;

import org.opentmf.commons.jpa.model.CarCreateDto;
import org.opentmf.commons.jpa.model.CarDto;
import org.opentmf.commons.jpa.model.CarUpdateDto;

/**
 * @author Gokhan Demir
 */
public interface PersistenceService {

  CarDto save(CarCreateDto carDto);

  CarDto update(String model, CarUpdateDto carDto);
}
