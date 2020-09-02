package com.larbotech.batch.model;

import com.larbotech.batch.exception.TypeNotFoundException;
import java.util.Arrays;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.ift.CellProcessor;

public enum TypeEnum {
  STRING(new NotNull()),
  EMAIL(new StrRegEx("[a-z0-9\\._]+@[a-z0-9\\.]+")),
  INT(new NotNull(new ParseInt())),
  LONG(new NotNull(new ParseLong())),
  DATE(new NotNull(new ParseDate("yyyyMMdd"))),
  BOOLEAN(new NotNull(new ParseBool()));

  private CellProcessor cellProcessor;

  final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+";

  TypeEnum(CellProcessor cellProcessor) {
    this.cellProcessor = cellProcessor;
  }

  public static TypeEnum from(String value) {
    return Arrays.stream(TypeEnum.values()).filter(v -> v.name().equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(() -> new TypeNotFoundException("invalid type:" + value));
  }

  public CellProcessor getCellProcessor() {
    return cellProcessor;
  }
}
