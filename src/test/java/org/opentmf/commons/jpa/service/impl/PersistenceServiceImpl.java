package org.opentmf.commons.jpa.service.impl;

import org.opentmf.commons.jpa.converter.CarConverter;
import org.opentmf.commons.jpa.exception.CarNotFoundException;
import org.opentmf.commons.jpa.model.CarCreateDto;
import org.opentmf.commons.jpa.model.CarDto;
import org.opentmf.commons.jpa.model.CarUpdateDto;
import org.opentmf.commons.jpa.repository.CarRepository;
import org.opentmf.commons.jpa.repository.entity.Car;
import org.opentmf.commons.jpa.service.api.PersistenceService;
import lombok.RequiredArgsConstructor;
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
