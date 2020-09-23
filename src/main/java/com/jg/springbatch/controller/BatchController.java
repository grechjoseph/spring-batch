package com.jg.springbatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job job;

    @GetMapping
    public BatchStatus getJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        final Map<String, JobParameter> params = new HashMap<>();
        final JobParameters jobParameters = new JobParameters(params);
        final JobExecution execution = jobLauncher.run(job, jobParameters);

        while(execution.isRunning()) {

        }

        return execution.getStatus();
    }

}
