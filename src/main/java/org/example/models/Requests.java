package org.example.models;

import java.time.LocalDateTime;

public class Requests {

  public record IncidentCreationRequest(
      String serviceId,
      String severity,
      String title,
      String description,
      String dedupKey,
      LocalDateTime timestamp) {}
}
