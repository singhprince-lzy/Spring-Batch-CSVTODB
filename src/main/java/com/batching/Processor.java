package com.batching;

import org.springframework.batch.item.ItemProcessor;

public class Processor implements ItemProcessor<String, String> {

	@Override
	public String process(String item) throws Exception {
		System.out.println("Inside item Processor unit.");
		System.out.println("Processes item: "+item);
		return item;
	}

}
