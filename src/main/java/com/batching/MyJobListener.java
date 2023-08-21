package com.batching;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MyJobListener implements JobExecutionListener {
	
	@Override
	public  void beforeJob(JobExecution jobExecution) {
		System.out.println("Inside Before Job Execution: "+jobExecution.getStartTime());
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("Inside After Job Execution: "+jobExecution.getStatus().toString());
	}

}
