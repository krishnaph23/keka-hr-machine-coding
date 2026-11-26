package org.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.example.models.Incident;
import org.example.models.Requests;
import org.example.models.Responses;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class IncidentResource {
  private final IncidentService service;

  @POST
  @Path("/alert")
  public Responses.IncidentCreationResponse createIncident(
      Requests.IncidentCreationRequest request) {
    try {
      Incident incident = service.createIncident(request);
      return new Responses.IncidentCreationResponse(incident.incidentId());
    } catch (IllegalArgumentException e) {
      throw new BadRequestException(e.getMessage());
    }
  }

//  @GET
//  @Path("/incidents")
//  public Responses.IncidentCreationResponse listIncidents() {}
}
