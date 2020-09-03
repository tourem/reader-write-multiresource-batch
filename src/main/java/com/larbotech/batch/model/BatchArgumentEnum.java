
package com.larbotech.batch.model;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BatchArgumentEnum {

    BATCH_NAME_ARG("batchName", "batch name argument");

    private final String argumentName;
    private final String description;

}
