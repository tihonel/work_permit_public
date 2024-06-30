package tihonel.com.github.workpermit.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DaoInstruction {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public DaoInstruction(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Integer> getIdsInstructionByWorkPermitId(int id){
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("id", id);
        return namedParameterJdbcTemplate.queryForList("SELECT id FROM instruction WHERE work_permit_id = :id", parameterSource, Integer.class);
    }

}
