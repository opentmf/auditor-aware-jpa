package com.pia.commons.jpa.service.api;

import com.pia.commons.jpa.model.CarCreateDto;
import com.pia.commons.jpa.model.CarDto;
import com.pia.commons.jpa.model.CarUpdateDto;

/**
 * @author Gokhan Demir
 */
public interface PersistenceService {

  CarDto save(CarCreateDto carDto);

  CarDto update(String model, CarUpdateDto carDto);
}
