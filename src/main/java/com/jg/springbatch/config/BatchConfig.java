package com.jg.springbatch.config;

import com.jg.springbatch.batch.Writer;
import com.jg.springbatch.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job job(final JobBuilderFactory jobBuilderFactory,
                   final Step step) {
        return jobBuilderFactory.get("job-name")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step step(final StepBuilderFactory stepBuilderFactory,
                     final ItemReader<User> reader,
                     final ItemProcessor<User, User> processor,
                     final Writer writer) {
        return stepBuilderFactory.get("step-name")
                .<User, User>chunk(2)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<User> flatFileItemReader(final LineMapper<User> lineMapper) {
        final FlatFileItemReader<User> userFlatFileItemReader = new FlatFileItemReader<>();
        userFlatFileItemReader.setResource(new ClassPathResource("users.csv"));
        userFlatFileItemReader.setName("reader");
        userFlatFileItemReader.setLinesToSkip(1);
        userFlatFileItemReader.setLineMapper(lineMapper);
        return userFlatFileItemReader;
    }

    @Bean
    public LineMapper<User> lineMapper() {
        final DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();

        final DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("id", "name", "department", "salary");
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

        final BeanWrapperFieldSetMapper<User> fieldMapper = new BeanWrapperFieldSetMapper<>();
        fieldMapper.setTargetType(User.class);
        defaultLineMapper.setFieldSetMapper(fieldMapper);

        return defaultLineMapper;
    }

}
