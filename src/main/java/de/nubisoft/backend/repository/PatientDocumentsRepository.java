package de.nubisoft.backend.repository;

import de.nubisoft.backend.domain.PatientDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class PatientDocumentsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    PatientDocumentsRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void saveAll(String patientId, List<PatientDocument> documents) {
        if (CollectionUtils.isEmpty(documents)) {
            return;
        }
        documents.forEach(document -> {
            try {
                var params = Map.of(
                        "CONTENT", document.content(),
                        "PATIENT_ID", patientId
                );
                jdbcTemplate.update("INSERT INTO PATIENT_DOCUMENTS(CONTENT, PATIENT_ID) VALUES(:CONTENT, :PATIENT_ID)", params);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<PatientDocument> findAllByPatientIdId(String patientId) {
        try {
            return jdbcTemplate.query("SELECT * FROM PATIENT_DOCUMENTS WHERE PATIENT_ID = :PATIENT_ID", Map.of("PATIENT_ID", patientId), new RowMapper<PatientDocument>() {
                @Override
                public PatientDocument mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new PatientDocument(rs.getString("CONTENT"));
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
