package com.dtlabs.samplejob1;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.dtlabs.person.Person;

@Component
public class SampleJob1 {
	
	@Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;
    
    @Bean
    public Job job1(@Qualifier("step1") Step step1) {
		return jobs.get("sampleJob1").start(step1).build();
    }

    @Bean
    protected Step step1(@Qualifier("sampleJob1ItemReader") ItemReader<Person> reader, @Qualifier("sampleJob1ItemWriter") ItemWriter<Person> writer) {
        return steps.get("step1")
            .<Person, Person> chunk(10)
            .reader(reader)
            .writer(writer)
            .build();
    }

    @Bean
    @StepScope
    protected FlatFileItemReader<Person> sampleJob1ItemReader(){
    	// Tokenizer
    	DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
    	tokenizer.setNames(new String[]{"id", "name", "age"});
    	
    	// FieldSetMapper
    	BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    	fieldSetMapper.setTargetType(Person.class);
    	
    	// LineMapper
    	DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
    	lineMapper.setFieldSetMapper(fieldSetMapper);
    	lineMapper.setLineTokenizer(tokenizer);

    	// ItemReader
    	FlatFileItemReader<Person> itemReader = new FlatFileItemReader<>();
    	itemReader.setResource(new ClassPathResource("/META-INF/csv/person.csv"));
		itemReader.setLineMapper(lineMapper);
		return itemReader;
    }
    
    @Bean
    @StepScope
    protected FlatFileItemWriter<Person> sampleJob1ItemWriter(){
    	// Field Extractor
    	BeanWrapperFieldExtractor<Person> fieldExtractor = new BeanWrapperFieldExtractor<>();
    	fieldExtractor.setNames(new String[]{"id", "name", "age"});
    	
    	// Line Aggregator
    	DelimitedLineAggregator<Person> lineAggregator = new DelimitedLineAggregator<>();
    	lineAggregator.setDelimiter(",");
    	lineAggregator.setFieldExtractor(fieldExtractor);
    	
    	// ItemWriter
    	FlatFileItemWriter<Person> itemWriter = new FlatFileItemWriter<>();
    	itemWriter.setResource(new FileSystemResource("./csv/person_new.csv"));
    	itemWriter.setLineAggregator(lineAggregator);
    	return itemWriter;
    }
}
