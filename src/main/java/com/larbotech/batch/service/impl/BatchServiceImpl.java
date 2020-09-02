package com.larbotech.batch.service.impl;

import com.larbotech.batch.model.Batch;
import com.larbotech.batch.model.ContextReader;
import com.larbotech.batch.model.TypeEnum;
import com.larbotech.batch.service.BatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.ift.CellProcessor;

@Service
public class BatchServiceImpl implements BatchService {

  public String[] getHeaders(Map<String, Object> jsonMapSchema) {
    return jsonMapSchema.keySet().toArray(new String[jsonMapSchema.size()]);
  }

  private CellProcessor[] getProcessors(Map<String, Object> jsonMapSchema) {
    List<CellProcessor> processors = new ArrayList<>();
    for (Map.Entry<String, Object> entry : jsonMapSchema.entrySet()) {
      processors.add(TypeEnum.from(entry.getValue().toString()).getCellProcessor());
    }
    return processors.toArray(new CellProcessor[jsonMapSchema.size()]);
  }

  @Override
  public ContextReader getContextReader(Batch batch) {
    ContextReader contextReader = new ContextReader();
    JsonParser springParser = JsonParserFactory.getJsonParser();
    Map<String, Object> jsonMapSchema = springParser.parseMap(batch.getSchema());
    contextReader.setHeaders(getHeaders(jsonMapSchema));
    contextReader.setProcessors(getProcessors(jsonMapSchema));
    return contextReader;
  }
}
