package com.fiap.globalsolution.extremeevents.controller;

import com.fiap.globalsolution.extremeevents.model.EventReport;
import com.fiap.globalsolution.extremeevents.repository.EventReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/events")
public class EventReportController {

    @Autowired
    private EventReportRepository eventReportRepository;

    // Create (POST)
    @PostMapping
    public ResponseEntity<EventReport> createEventReport(@RequestBody EventReport eventReport) {
        eventReport.setTimestamp(LocalDateTime.now()); // Set timestamp on creation
        EventReport savedReport = eventReportRepository.save(eventReport);
        return new ResponseEntity<>(savedReport, HttpStatus.CREATED);
    }

    // Read All (GET)
    @GetMapping
    public ResponseEntity<List<EventReport>> getAllEventReports() {
        List<EventReport> reports = eventReportRepository.findAll();
        return ResponseEntity.ok(reports);
    }

    // Read by ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<EventReport> getEventReportById(@PathVariable Long id) {
        Optional<EventReport> report = eventReportRepository.findById(id);
        return report.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<EventReport> updateEventReport(@PathVariable Long id, @RequestBody EventReport eventReportDetails) {
        Optional<EventReport> optionalReport = eventReportRepository.findById(id);

        if (optionalReport.isPresent()) {
            EventReport existingReport = optionalReport.get();
            existingReport.setEventType(eventReportDetails.getEventType());
            existingReport.setLocation(eventReportDetails.getLocation());
            existingReport.setSeverity(eventReportDetails.getSeverity());
            existingReport.setDescription(eventReportDetails.getDescription());
            // existingReport.setTimestamp(LocalDateTime.now());
            EventReport updatedReport = eventReportRepository.save(existingReport);
            return ResponseEntity.ok(updatedReport);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventReport(@PathVariable Long id) {
        if (eventReportRepository.existsById(id)) {
            eventReportRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

