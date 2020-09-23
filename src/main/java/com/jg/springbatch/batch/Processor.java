package com.jg.springbatch.batch;

import com.jg.springbatch.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Processor implements ItemProcessor<User, User> {

    @Override
    public User process(final User user) throws Exception {
        log.info("Processing User: {}", user.toString());
        user.setName(user.getName().toUpperCase());
        log.info("Updating User: {}", user.toString());
        return user;
    }

}
