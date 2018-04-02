package com.dtlabs.runner;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchRunner {

	@Autowired
	private JobOperator jobOperator;
	
	@Scheduled(cron="0 * * * * MON-FRI")
	public void sampleJobRunner() throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException{
		JobParameters jobParameters = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis()).toJobParameters();
		jobOperator.start("sampleJob4", jobParameters.toString());
	}
}
