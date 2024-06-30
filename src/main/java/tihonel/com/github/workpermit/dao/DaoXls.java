package tihonel.com.github.workpermit.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DaoXls {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DaoXls(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public String findFileNameById(int id){
        String fileName = null;
        try {
            SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("id", id);
            fileName = namedParameterJdbcTemplate.queryForObject("SELECT file_name FROM work_permit_xls WHERE work_permit_id = :id", parameterSource, String.class);
        } catch (DataAccessException e) {
            return null;
        }
        return fileName;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void saveFileName(String fileName, int id) {
        System.out.println("ID В ДАО " + id);
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("fileName", fileName)
                .addValue("id", id);
        namedParameterJdbcTemplate.update("INSERT INTO work_permit_xls(work_permit_id, file_name)" +
                "VALUES(:id, :fileName)", parameterSource);
    }

    @Transactional
    public void deleteNote(String fileName) {
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("fileName", fileName);
        namedParameterJdbcTemplate.update("DELETE FROM work_permit_xls WHERE file_name = :fileName", parameterSource);
    }

    public List<String> findAll(){
        return jdbcTemplate.queryForList("SELECT file_name FROM work_permit_xls", String.class);
    }
}
