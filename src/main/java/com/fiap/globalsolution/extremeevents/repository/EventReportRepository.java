package com.fiap.globalsolution.extremeevents.repository;

import com.fiap.globalsolution.extremeevents.model.EventReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventReportRepository extends JpaRepository<EventReport, Long> {
}

