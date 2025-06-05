package com.fiap.globalsolution.extremeevents.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_reports")
public class EventReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventType; // e.g., Flood, Earthquake, Wildfire

    @Column(nullable = false)
    private String location; // e.g., City, State, Coordinates

    @Column(nullable = false)
    private Integer severity; // e.g., Scale 1-5

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Default constructor (required by JPA)
    public EventReport() {
    }

    // Constructor with fields
    public EventReport(String eventType, String location, Integer severity, String description, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.location = location;
        this.severity = severity;
        this.description = description;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

