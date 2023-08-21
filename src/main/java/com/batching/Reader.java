package com.batching;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class Reader implements ItemReader<String> {
	
	private String[] courseList= {"Core Java","Advance Java","Spring Framework","Angular 13"};
	private int count=0;

	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		System.out.println("Inside Reader Method.");
		if(count<courseList.length) {
			return courseList[count++];
		}
		return null;
	}

}
