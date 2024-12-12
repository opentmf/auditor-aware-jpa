package com.pia.commons.jpa.repository.entity;

import com.pia.commons.jpa.entity.AuditUpdatable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Gokhan Demir
 */
@Getter
@Setter
@Entity
@Table
public class Car extends AuditUpdatable {

  @Id
  @Column(length = 20, nullable = false)
  private String model;

  @Column(length = 50, nullable = false)
  private String color;

  @Column(nullable = false)
  private int builtYear;
}
