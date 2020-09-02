package com.larbotech.batch.model;

import lombok.Getter;
import lombok.Setter;
import org.supercsv.cellprocessor.ift.CellProcessor;

@Getter
@Setter
public class ContextReader {
 private String[] headers;
 private CellProcessor[] processors;
}
