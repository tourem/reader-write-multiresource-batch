package com.larbotech.batch.dao.impl;

import com.larbotech.batch.dao.BatchDao;
import com.larbotech.batch.exception.BatchNameNotFoundException;
import com.larbotech.batch.model.Batch;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class BatchDaoImpl extends JdbcDaoSupport implements BatchDao {

  private static final String query = "select * from Batch where name=?";


  @Autowired
  DataSource dataSource;

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  @Override
  public Batch findByName(String name) {
    Object[] inputs = new Object[]{name};
    Batch batch = getJdbcTemplate()
        .queryForObject(query, inputs, new BeanPropertyRowMapper<>(Batch.class));

    if (batch == null) {
      throw new BatchNameNotFoundException("batch name not found: " + name);
    }

    return batch;
  }
}
