package de.nubisoft.backend.repository;

import de.nubisoft.backend.domain.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
class AddressesRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    AddressesRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    String save(Address address) {
        try {
            var id = UUID.randomUUID().toString();
            jdbcTemplate.update("INSERT INTO ADDRESSES(ID, ADDRESS) VALUES(:ID, :ADDRESS)", Map.of("ID", id, "ADDRESS", address.address()));
            return id;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Address> getById(String id) {
        try {
            var address = jdbcTemplate.queryForObject("SELECT * FROM ADDRESSES WHERE ID = :ID", Map.of("ID", id), new RowMapper<Address>() {
                @Override
                public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Address(
                            rs.getString("ID"),
                            rs.getString("ADDRESS")
                    );
                }
            });
            return Optional.of(address);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
