package org.example.repo;

import org.example.models.Incident;
import org.example.models.IncidentStatus;
import org.example.models.Requests;

import java.util.*;

public class InMemoryIncidentRepository {
  private Map<String, Incident> incidents;
  private Map<IncidentStatus, List<Incident>> incidentStatusToIncidents;
  private Map<String, Incident> dedupKeyToIncidents;

  public InMemoryIncidentRepository() {
    this.incidents = new HashMap<>();
    this.incidentStatusToIncidents = new HashMap<>();
    this.dedupKeyToIncidents = new HashMap<>();
  }

  public List<Incident> getIncidents(IncidentStatus status) {
    return incidentStatusToIncidents.getOrDefault(status, List.of());
  }

  public void createIncident(Incident incident, String dedupKey) {
    incidents.put(incident.incidentId(), incident);
    dedupKeyToIncidents.put(dedupKey, incident);
    if (incidentStatusToIncidents.containsKey(incident.status())) {
      incidentStatusToIncidents.get(incident.status()).add(incident);
    } else {
      List<Incident> incidentList = new ArrayList<>();
      incidentList.add(incident);
      incidentStatusToIncidents.put(incident.status(), incidentList);
    }
  }

  public Incident getIncident(String incidentId) {
    return incidents.get(incidentId);
  }

  public Optional<Incident> getIncidentOnDedupKey(String dedupKey) {
    return Optional.ofNullable(dedupKeyToIncidents.get(dedupKey));
  }
}
