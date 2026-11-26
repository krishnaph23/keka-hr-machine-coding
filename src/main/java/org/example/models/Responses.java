package org.example.models;

import java.time.LocalDateTime;

public class Responses {
  public record IncidentCreationResponse(String incidentId) {}

  public record ListIncidentsResponse(
      String incidentId,
      String serviceId,
      IncidentSeverity severity,
      String title,
      String description,
      IncidentStatus status,
      LocalDateTime createdAt,
      Notification notification) {}
}
