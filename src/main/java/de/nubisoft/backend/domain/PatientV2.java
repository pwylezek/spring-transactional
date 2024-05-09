package de.nubisoft.backend.domain;

import java.util.List;

public record PatientV2(PatientV1 patient, List<PatientDocument> documents) {
}
