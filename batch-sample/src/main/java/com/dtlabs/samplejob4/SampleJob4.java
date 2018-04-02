package com.dtlabs.samplejob4;

import java.util.Random;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.dtlabs.tasklet.AddTasklet;

@Component
public class SampleJob4 {
	
	@Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;
    
    @Bean
    public Job job4(@Qualifier("step4") Step step4) {
		return jobs.get("sampleJob4").start(step4).build();
    }

    @Bean
    protected Step step4(@Qualifier("sampleJob4Tasklet") Tasklet tasklet) {
        return steps.get("step4")
            .tasklet(tasklet)
            .build();
    }

    @Bean
    @StepScope
    protected Tasklet sampleJob4Tasklet(){
    	AddTasklet tasklet = new AddTasklet();
    	tasklet.setNumber1(new Random().nextInt(1000));
    	tasklet.setNumber2(new Random().nextInt(1000));
    	return tasklet;
    }
	
}
