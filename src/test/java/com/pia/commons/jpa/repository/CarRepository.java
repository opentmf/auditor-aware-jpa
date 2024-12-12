package com.pia.commons.jpa.repository;

import com.pia.commons.jpa.repository.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Gokhan Demir
 */
@Repository
public interface CarRepository extends JpaRepository<Car, String> {}
