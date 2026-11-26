package org.example.models;

import java.time.LocalDateTime;
import java.util.Optional;

public record Incident(
    String incidentId,
    String serviceId,
    IncidentSeverity severity,
    String title,
    String description,
    IncidentStatus status,
    LocalDateTime createdAt,
    Optional<Notification> notification) {}
