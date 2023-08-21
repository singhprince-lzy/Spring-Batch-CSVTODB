package com.batching.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.batching.model.Product;

@Configuration
public class BatchConfig {
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private PlatformTransactionManager platformTransactionManager;
	
	@Bean
	public Job job() {
		return new JobBuilder("Job-1", jobRepository).flow(step1()).end().build();
	}
	
	@Bean
	public Step step1() {
		StepBuilder stepBuilder=new StepBuilder("Job-1",jobRepository);
		return stepBuilder.<Product,Product>chunk(1, platformTransactionManager).reader(reader()).processor(processor())
		.writer(writer()).build();
		
	}

	@Bean
	public ItemReader<Product> reader() {

		FlatFileItemReader<Product> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("products.csv"));

		DefaultLineMapper<Product> mapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames("id", "name", "description", "price");

		BeanWrapperFieldSetMapper<Product> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Product.class);

		mapper.setLineTokenizer(lineTokenizer);
		mapper.setFieldSetMapper(fieldSetMapper);

		reader.setLineMapper(mapper);

		return reader;
	}

	@Bean
	public ItemProcessor<Product, Product> processor() {

		return (p) -> {
			p.setPrice(p.getPrice() - (p.getPrice() * 10 / 100));
			return p;
		};

	}

	@Bean
	public ItemWriter<Product> writer() {
		JdbcBatchItemWriter<Product> writer = new JdbcBatchItemWriter<Product>();
		writer.setDataSource(dataSource);
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Product>());
		writer.setSql("Insert into product(id,name,description,price) values (:id,:name,:description,:price);");

		return writer;

	}
}
