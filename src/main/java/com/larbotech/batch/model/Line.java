package com.larbotech.batch.model;

import lombok.Getter;

@Getter
public class Line {
  String[] headers;
  String value;
  boolean valid;
  String originFileName;

  public Line(String value, boolean valid, String originFileName, String[] headers) {
    this.value = value;
    this.valid = valid;
    this.originFileName = originFileName;
    this.headers = headers;
  }

  public Line(String value, String originFileName, String[] headers) {
    this.value = value;
    this.originFileName = originFileName;
    this.headers = headers;
  }

  public String getHeader(){
    return String.join(",", headers);
  }
}
