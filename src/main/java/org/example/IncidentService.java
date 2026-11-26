package org.example;

import org.example.models.*;
import org.example.repo.InMemoryIncidentRepository;

import javax.print.attribute.standard.Severity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class IncidentService {

  private InMemoryIncidentRepository incidentRepository;

  public IncidentService() {
    this.incidentRepository = new InMemoryIncidentRepository();
  }

  public Incident createIncident(Requests.IncidentCreationRequest creationRequest) {
    // Validate request
    Optional<Incident> existingIncident =
        incidentRepository.getIncidentOnDedupKey(creationRequest.dedupKey());
    Notification notification = null;
    if (existingIncident.isEmpty()) {
      UUID incidentId = UUID.randomUUID();

      try {
        if (IncidentSeverity.valueOf(creationRequest.severity()) == IncidentSeverity.CRITICAL) {
          // MockServer call
          ExotelMockResponse mockResponse = new ExotelMockResponse("CA-7781", "queued");
          notification =
              new Notification(
                  NotificationChannel.PHONE, NotificationStatus.SENT, mockResponse.callSid(), null);
        } else if (IncidentSeverity.valueOf(creationRequest.severity())
            == IncidentSeverity.WARNING) {
          notification = new Notification(null, NotificationStatus.SKIPPED, null, null);
        }
      } catch (Exception e) {
        // handle
      }

      Incident incident =
          new Incident(
              incidentId.toString(),
              creationRequest.serviceId(),
              IncidentSeverity.valueOf(creationRequest.severity()),
              creationRequest.title(),
              creationRequest.description(),
              IncidentStatus.TRIGGERED,
              creationRequest.timestamp(),
              Optional.ofNullable(notification));

      incidentRepository.createIncident(incident, creationRequest.dedupKey());
      return incident;
    }
    return existingIncident.get();
  }
}
