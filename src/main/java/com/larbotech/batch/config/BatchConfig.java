package com.larbotech.batch.config;

import com.larbotech.batch.model.Line;
import com.larbotech.batch.step.reader.CsvFileReader;
import com.larbotech.batch.step.writer.CsvFileWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Autowired
  private CsvFileReader csvFileReader;

  @Autowired
  private CsvFileWriter csvFileWriter;

  @Bean
  public Job job() {
    return jobBuilderFactory.get("job").incrementer(new RunIdIncrementer())
        .flow(stepReadValidateWriteFile()).end().build();
  }

  @Bean
  public Step stepReadValidateWriteFile() {
    return stepBuilderFactory.get("stepReadValidateWriteFile").<Line, Line>chunk(csvFileReader)
        .reader(csvFileReader).writer(csvFileWriter).build();
  }
}
