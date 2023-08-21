package com.batching;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


public class MyBatchConfig {
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private PlatformTransactionManager platformTransactionManager;
	
	@Bean
	public Job job() {
		return new JobBuilder("Job-1", jobRepository).start(step1()).listener(myJobListener()).build();
	}
	
	@Bean
	public Step step1() {
		StepBuilder stepBuilder=new StepBuilder("Job-1",jobRepository);
		return stepBuilder.<String,String>chunk(1, platformTransactionManager).reader(reader()).processor(processor())
		.writer(writer()).build();
		
	}
	
	@Bean
	public Reader reader() {
		return new Reader();
	}
	
	@Bean
	public Writer writer() {
		return new Writer();
	}
	
	@Bean
	public Processor processor() {
		return new Processor();
	}
	
	@Bean
	public MyJobListener myJobListener() {
		return new MyJobListener();
	}
	
	
	

}
