package tn.esprit.ms3locationzone.incident.mapper;

import tn.esprit.ms3locationzone.incident.dto.IncidentLocationResponse;
import tn.esprit.ms3locationzone.incident.entity.IncidentLocation;
import org.springframework.stereotype.Component;

@Component
public class IncidentLocationMapper {

    public IncidentLocationResponse toResponse(IncidentLocation i) {
        return new IncidentLocationResponse(i.getIncidentId(), i.getLatitude(), i.getLongitude(),
                i.getPriorite(), i.getZoneId(), i.getDateSynchro());
    }
}