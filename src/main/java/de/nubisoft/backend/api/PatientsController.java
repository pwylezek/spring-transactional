package de.nubisoft.backend.api;


import de.nubisoft.backend.domain.PatientDocument;
import de.nubisoft.backend.domain.PatientV1;
import de.nubisoft.backend.domain.PatientV2;
import de.nubisoft.backend.service.PatientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientsController {

    private final PatientsService patientsService;

    @Autowired
    public PatientsController(PatientsService patientsService) {
        this.patientsService = patientsService;
    }

    @PostMapping("v1/patients")
    PatientV1 savePatientV1(@RequestBody PatientV1 patient) {
        return this.patientsService.savePatientV1(patient);
    }

    @GetMapping("v1/patients/{id}")
    PatientV1 getPatientV1(@PathVariable String id) {
        return this.patientsService.getPatientV1ById(id);
    }

    @GetMapping("v1/patients/{id}/external-results")
    List<PatientDocument> getPatientExternalDocuments(@PathVariable String id, @RequestParam Integer count) {
        return this.patientsService.getPatientExternalResults(id, count);
    }

    @PostMapping("v2/patients")
    PatientV2 savePatientV2(@RequestBody PatientV2 patient) {
        return this.patientsService.savePatientV2(patient);
    }

    @GetMapping("v2/patients/{id}")
    PatientV2 getPatientV2(@PathVariable String id) {
        return this.patientsService.getPatientV2ById(id);
    }
}
