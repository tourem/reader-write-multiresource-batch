package com.larbotech.batch.dao;

import com.larbotech.batch.model.Batch;

public interface BatchDao {
    Batch findByName(String name);
}
