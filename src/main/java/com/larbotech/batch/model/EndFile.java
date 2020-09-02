package com.larbotech.batch.model;

public class EndFile extends Line {

  public EndFile(String value, String path, String[] headers) {
    super(value, path, headers);
  }

  public EndFile(String path, String[] headers) {
    super(null, path, headers);
  }
}
