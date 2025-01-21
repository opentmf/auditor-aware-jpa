package com.pia.commons.jpa.converter;

import com.pia.commons.jpa.model.CarCreateDto;
import com.pia.commons.jpa.model.CarDto;
import com.pia.commons.jpa.repository.entity.Car;
import lombok.experimental.UtilityClass;

/**
 * @author Gokhan Demir
 */
@UtilityClass
public class CarConverter {

  public static CarDto toDto(Car car) {
    CarDto carDto = new CarDto();
    carDto.setModel(car.getModel());
    carDto.setColor(car.getColor());
    carDto.setBuiltYear(car.getBuiltYear());
    carDto.setCreatedBy(car.getCreatedBy());
    carDto.setCreatedOn(car.getCreatedOn());
    carDto.setModifiedBy(car.getModifiedBy());
    carDto.setModifiedOn(car.getModifiedOn());
    carDto.setUpdateCount(car.getUpdateCount());
    return carDto;
  }

  public static Car toEntity(CarCreateDto carDto) {
    Car car = new Car();
    car.setModel(carDto.getModel());
    car.setColor(carDto.getColor());
    car.setBuiltYear(carDto.getBuiltYear());
    return car;
  }
}
