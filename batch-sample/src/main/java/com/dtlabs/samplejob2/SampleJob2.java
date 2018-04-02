package com.dtlabs.samplejob2;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.dtlabs.person.Person;

@Component
public class SampleJob2 {

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;
	
	@Autowired
	private DataSource dataSource;

	@Bean
	public Job job2(@Qualifier("step2") Step step2) {
		return jobs.get("sampleJob2").start(step2).build();
	}

	@Bean
    protected Step step2(@Qualifier("sampleJob2ItemReader") ItemReader<Person> reader, @Qualifier("sampleJob2ItemWriter") ItemWriter<Person> writer) {
		return steps.get("step2").<Person, Person>chunk(10).reader(reader).writer(writer).build();
	}

	@Bean
	@StepScope
	protected FlatFileItemReader<Person> sampleJob2ItemReader() {
		// Tokenizer
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
		tokenizer.setNames(new String[] {"id", "name", "age"});

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
	protected JdbcBatchItemWriter<Person> sampleJob2ItemWriter() {
		JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setSql("INSERT INTO batch_test (id, name, age) VALUES (:id, :name, :age)");
		itemWriter.setDataSource(dataSource);
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
		return itemWriter;
	}
}
