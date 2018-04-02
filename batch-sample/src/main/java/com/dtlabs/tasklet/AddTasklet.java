package com.dtlabs.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class AddTasklet implements Tasklet {

	private static final Logger logger = LoggerFactory.getLogger(AddTasklet.class);
	
	private int number1, number2;
	
	public void setNumber1(int number1) {
		this.number1 = number1;
	}

	public void setNumber2(int number2) {
		this.number2 = number2;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		int result = this.number1 + this.number2;
		logger.info("Result : {} + {} = {}", this.number1, this.number2, result);
		return RepeatStatus.FINISHED;
	}

}
