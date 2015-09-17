package org.jfu.test.weld.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.jfu.test.weld.interceptor.Logged;
import org.jfu.test.weld.service.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreetingServiceImpl implements GreetingService {
    
    private static Logger logger = LoggerFactory.getLogger(GreetingServiceImpl.class);
    
    @Override
    @Logged
    public String hi(String name) {
        logger.debug("======== say hi to {}!", name);
        return "Hi, " + name;
    }

    @PostConstruct
    public void init() {
        logger.debug("======== PostConstruct...");
    }
    
    @PreDestroy
    public void destroy() {
        logger.debug("======== PreDestroy...");
    }
}
