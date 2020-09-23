package com.jg.springbatch.batch;

import com.jg.springbatch.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class Writer implements ItemWriter<User> {

    @Override
    public void write(final List<? extends User> list) throws Exception {
        log.info("Writing Users: {}", list.toString());
    }

}
