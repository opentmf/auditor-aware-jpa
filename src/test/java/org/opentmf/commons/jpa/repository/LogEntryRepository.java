package org.opentmf.commons.jpa.repository;

import org.opentmf.commons.jpa.repository.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Gokhan Demir
 */
@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, String> {}
