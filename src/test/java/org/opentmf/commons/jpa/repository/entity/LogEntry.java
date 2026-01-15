package org.opentmf.commons.jpa.repository.entity;

import org.opentmf.commons.jpa.entity.AuditInsertable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Test entity that extends AuditInsertable to verify it works correctly.
 * Log entries are only inserted, never updated, so they use AuditInsertable.
 * 
 * @author Gokhan Demir
 */
@Getter
@Setter
@Entity
@Table
public class LogEntry extends AuditInsertable {

  @Id
  @Column(length = 50, nullable = false)
  private String id;

  @Column(length = 500, nullable = false)
  private String message;

  @Column(length = 20, nullable = false)
  private String level;
}
