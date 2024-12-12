package com.pia.commons.jpa.service.impl;

import com.pia.commons.jpa.converter.CarConverter;
import com.pia.commons.jpa.exception.CarNotFoundException;
import com.pia.commons.jpa.model.CarCreateDto;
import com.pia.commons.jpa.model.CarDto;
import com.pia.commons.jpa.model.CarUpdateDto;
import com.pia.commons.jpa.repository.CarRepository;
import com.pia.commons.jpa.repository.entity.Car;
import com.pia.commons.jpa.service.api.PersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

/**
 * @author Gokhan Demir
 */
@Service
@RequiredArgsConstructor
public class PersistenceServiceImpl implements PersistenceService {

  private final CarRepository carRepository;

  @Override
  public CarDto save(CarCreateDto carCreateDto) {
    return CarConverter.toDto(carRepository.save(CarConverter.toEntity(carCreateDto)));
  }

  @Override
  public CarDto update(String model, CarUpdateDto carUpdateDto) {
    Car car =
        carRepository
            .findById(model)
            .orElseThrow(() -> new CarNotFoundException(""));
    car.setColor(carUpdateDto.getColor());
    car.setBuiltYear(carUpdateDto.getBuiltYear());
    return CarConverter.toDto(carRepository.save(car));
  }
}
