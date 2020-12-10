package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource){
        this.jdbcTemplate=new JdbcTemplate(dataSource);
    }
    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement("insert into time_entries" +
                    "(project_id,user_id,date,hours)"+"values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1,timeEntry.getProjectId());
            statement.setLong(2,timeEntry.getUserId());
            statement.setDate(3,Date.valueOf(timeEntry.getDate()));
            statement.setInt(4,timeEntry.getHours());
            return statement;
        },generatedKeyHolder);
        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(Long id) {
        return jdbcTemplate.query("select id,project_id,user_id,date,hours from time_entries where id=?",new Object[]{id},extractor);
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("select id,project_id,user_id,date,hours from time_entries",mapper);
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
         jdbcTemplate.update("update time_entries "+"set project_id= ?,user_id=?,date=?," +
                "hours =? "+ "where id = ? ",timeEntry.getProjectId(),timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),timeEntry.getHours(),id );
         return find(id);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from time_entries where id  =?",id);
    }


    private RowMapper<TimeEntry> mapper = (rs,rowNum) ->new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private ResultSetExtractor<TimeEntry> extractor = (rs) -> rs.next()? mapper.mapRow(rs,1) : null;
}
