package de.nubisoft.backend.service;


import de.nubisoft.backend.domain.PatientDocument;
import de.nubisoft.backend.domain.PatientV1;
import de.nubisoft.backend.domain.PatientV2;
import de.nubisoft.backend.exception.NotFoundException;
import de.nubisoft.backend.exception.SavePatientException;
import de.nubisoft.backend.repository.PatientDocumentsRepository;
import de.nubisoft.backend.repository.PatientsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@Slf4j
public class PatientsService {

    private final PatientsRepository patientsRepository;
    private final PatientDocumentsRepository patientDocumentsRepository;

    @Autowired
    public PatientsService(PatientsRepository patientsRepository, PatientDocumentsRepository patientDocumentsRepository) {
        this.patientsRepository = patientsRepository;
        this.patientDocumentsRepository = patientDocumentsRepository;
    }

    @Transactional
    public PatientV1 savePatientV1(PatientV1 patient) {
        log.info("Saving new patient: {}", patient);
        var id = patientsRepository.save(patient);
        return getPatientV1ById(id);
    }

    @Transactional
    public PatientV1 savePatientV1OrThrowIfError(PatientV1 patient) throws SavePatientException {
        log.info("Saving new patient: {}", patient);
        var id = patientsRepository.saveOrThrowIfError(patient);
        return getPatientV1ById(id);
    }

    public PatientV1 getPatientV1ById(String id) {
        return patientsRepository.getById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public PatientV2 savePatientV2(PatientV2 patient) {
        log.info("Saving new patient: {}", patient);
        var id = patientsRepository.save(patient.patient());
        patientDocumentsRepository.saveAll(id, patient.documents());
        return getPatientV2ById(id);
    }

    public PatientV2 getPatientV2ById(String id) {
        return new PatientV2(getPatientV1ById(id), patientDocumentsRepository.findAllByPatientIdId(id));
    }

    public List<PatientDocument> getPatientExternalResults(String patientId, Integer count) {
        // POBIERZ POŁĄCZENIE
        var patient = getPatientV1ById(patientId);
        return fetchPatientResultsFromVerySlowExternalService(patient.externalId(), count);
        // ZWRÓC POŁĄCZENIE
    }

    private List<PatientDocument> fetchPatientResultsFromVerySlowExternalService(String patientExternalId, Integer count) {

        try {
            log.info("Fetching patient documents from external service. Patient id: {}", patientExternalId);
            String documentContent = new String(new ClassPathResource("document.txt").getInputStream().readAllBytes());
            List<PatientDocument> documents = new ArrayList<>();
            IntStream.range(0, count).forEach(id ->
                    Mono.just(id)
                            .delayElement(Duration.ofMillis(1000))
                            .doOnNext(it -> documents.add(new PatientDocument(patientExternalId + documentContent + UUID.randomUUID())))
                            .block()
            );
            return documents;
        } catch (Exception unexpected) {
            throw new RuntimeException(unexpected);
        }
    }
}
