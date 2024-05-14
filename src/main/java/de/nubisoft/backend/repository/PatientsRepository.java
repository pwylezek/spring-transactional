package de.nubisoft.backend.repository;

import de.nubisoft.backend.domain.PatientV1;
import de.nubisoft.backend.exception.SavePatientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public class PatientsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AddressesRepository addressesRepository;

    @Autowired
    PatientsRepository(DataSource dataSource, AddressesRepository addressesRepository) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.addressesRepository = addressesRepository;
    }

    public String save(PatientV1 patient) {
        var id = UUID.randomUUID().toString();
        var addressId = addressesRepository.save(patient.address());
        var params = Map.of(
                "ID", id,
                "EXTERNAL_ID", patient.externalId(),
                "NAME", patient.name(),
                "ADDRESS_ID", addressId
        );
        jdbcTemplate.update("INSERT INTO PATIENTS(ID, EXTERNAL_ID, NAME, ADDRESS_ID) VALUES(:ID, :EXTERNAL_ID, :NAME, :ADDRESS_ID)", params);
        return id;
    }

    public String saveOrThrowIfError(PatientV1 patient) throws SavePatientException {
        try {
            return save(patient);
        } catch (Exception e) {
            throw new SavePatientException(e);
        }
    }

    public Optional<PatientV1> getById(String id) {
        try {
            var patient = jdbcTemplate.queryForObject("SELECT * FROM PATIENTS WHERE ID = :ID", Map.of("ID", id), new RowMapper<PatientV1>() {
                @Override
                public PatientV1 mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new PatientV1(
                            rs.getString("ID"),
                            rs.getString("EXTERNAL_ID"),
                            rs.getString("NAME"),
                            addressesRepository.getById(rs.getString("ADDRESS_ID")).orElseThrow()
                    );
                }
            });
            return Optional.of(patient);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
