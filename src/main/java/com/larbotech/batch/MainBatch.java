package com.larbotech.batch;



import com.larbotech.batch.exception.BadParameterizedStepJobException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.ExitCodeMapper;
import org.springframework.batch.core.launch.support.SimpleJvmExitCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Date;

import static com.larbotech.batch.model.BatchArgumentEnum.BATCH_NAME_ARG;

@Slf4j
@SpringBootApplication
public class MainBatch implements CommandLineRunner {

    public static final String TIMESTAMP_PARAM_NAME = "date";

    @Autowired
    private ApplicationArguments applicationArguments;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    public static void main(String[] args) {
        SpringApplication.run(MainBatch.class, args);
    }

    @Override
    public void run(String... args) {

        log.info("Application started with command-line arguments: {}", Arrays.toString(applicationArguments.getSourceArgs()));
        log.info("NonOptionArgs: {}", applicationArguments.getNonOptionArgs());
        log.info("OptionNames: {}", applicationArguments.getOptionNames());

        if (applicationArguments.containsOption(BATCH_NAME_ARG.getArgumentName())) {

            String batchName = applicationArguments.getOptionValues(BATCH_NAME_ARG.getArgumentName()).get(0);

            JobParameters param = new JobParametersBuilder()
                    .addDate(TIMESTAMP_PARAM_NAME, new Date())
                    .addString(BATCH_NAME_ARG.getArgumentName(), batchName)
                    .toJobParameters();

            int exitStatus = 0;
            try {
                ExitCodeMapper exitCodeMapper = new SimpleJvmExitCodeMapper();
                JobExecution jobExecution = jobLauncher.run(job, param);
                exitStatus = exitCodeMapper.intValue(jobExecution.getExitStatus().getExitCode());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            System.exit(exitStatus);
            return;
        }
        throw new BadParameterizedStepJobException("Missing --" + BATCH_NAME_ARG.getArgumentName() + "=argument");
    }
}
