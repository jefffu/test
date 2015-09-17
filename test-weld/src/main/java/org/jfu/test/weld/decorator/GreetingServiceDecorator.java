package org.jfu.test.weld.decorator;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.jfu.test.weld.service.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Decorator
public class GreetingServiceDecorator implements GreetingService {
    
    private static Logger logger = LoggerFactory.getLogger(GreetingServiceDecorator.class);
    
    @Inject
    @Delegate
    @Any
    private GreetingService greetingService;

    @Override
    public String hi(String name) {
        logger.debug("======== decorator say hi!");
        
        return String.format("[%s]", greetingService.hi(name));
    }

}
