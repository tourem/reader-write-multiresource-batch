package com.larbotech.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.ExitCodeMapper;
import org.springframework.batch.core.launch.support.SimpleJvmExitCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MainBatch implements CommandLineRunner {

  @Autowired
  JobLauncher jobLauncher;

  @Autowired
  Job job;

  public static void main(String[] args) {
    SpringApplication.run(MainBatch.class, args);
  }

  @Override
  public void run(String... args) {
    int exitStatus = 0;
    try {
      ExitCodeMapper exitCodeMapper = new SimpleJvmExitCodeMapper();
      JobParameters jobParameters = new JobParametersBuilder()
          .addLong("time", System.currentTimeMillis())
          .toJobParameters();
      JobExecution jobExecution = jobLauncher.run(job, jobParameters);
      exitStatus = exitCodeMapper.intValue(jobExecution.getExitStatus().getExitCode());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    System.exit(exitStatus);
  }
}