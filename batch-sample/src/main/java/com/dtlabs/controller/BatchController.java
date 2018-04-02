package com.dtlabs.controller;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchController {

	@Autowired
	private JobOperator jobOperator;
	
	@RequestMapping("/{jobName}")
	public String sampleJobController(@PathVariable("jobName") String jobName) throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException{
		JobParameters jobParameters = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis()).toJobParameters();
		Long executionId = jobOperator.start(jobName, jobParameters.toString());
		return "Batch Job is started. Execution ID : " + String.valueOf(executionId);
	}
}
