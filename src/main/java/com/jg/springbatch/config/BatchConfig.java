package com.jg.springbatch.config;

import com.jg.springbatch.batch.Writer;
import com.jg.springbatch.model.User;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job job(final JobBuilderFactory jobBuilderFactory,
                   @Qualifier("step") final Step step,
                   @Qualifier("finalStep") final Step finalStep) {
        return jobBuilderFactory.get("job-name")
                .incrementer(new RunIdIncrementer())
                // Initial step.
                .start(step)
                // Condition - In this case, when job exits.
                .on(ExitStatus.COMPLETED.getExitCode())
                // Result when condition is met (Step).
                .to(finalStep)
                // End flow.
                .end()
                .build();
    }

    @Bean
    public Step step(final StepBuilderFactory stepBuilderFactory,
                     final ItemReader<User> reader,
                     final ItemProcessor<User, User> processor,
                     final Writer writer) {
        return stepBuilderFactory.get("step-name")
                // Process in chunks of 2.
                .<User, User>chunk(2)
                // Reader.
                .reader(reader)
                // Processor - executed once per object.
                .processor(processor)
                // Writer - executed once per chunk.
                .writer(writer)
                .build();
    }

    @Bean
    public Step finalStep(final StepBuilderFactory stepBuilderFactory,
                          final Tasklet finalTasklet) {
        return stepBuilderFactory.get("final-step")
                // Tasklet to execute at Step.
                .tasklet(finalTasklet)
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
