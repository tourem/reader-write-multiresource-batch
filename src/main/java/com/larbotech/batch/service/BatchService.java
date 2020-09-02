package com.larbotech.batch.service;

import com.larbotech.batch.model.Batch;
import com.larbotech.batch.model.ContextReader;

public interface BatchService {
  ContextReader getContextReader(Batch batch);
}
