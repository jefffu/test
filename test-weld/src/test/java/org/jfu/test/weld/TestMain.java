package org.jfu.test.weld;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jfu.test.weld.service.GreetingService;
import org.jfu.test.weld.service.LoginService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMain {
    
    private static Logger logger = LoggerFactory.getLogger(TestMain.class);

    @Test
    public void test() {
        Weld weld = new Weld();
        WeldContainer weldContainer = weld.initialize();

        LoginService loginService = weldContainer.instance().select(LoginService.class).get();
        
        loginService.login("jeff");
        
        logger.debug("======== select greetingService from weldContainer...");
        GreetingService greetingService = weldContainer.instance().select(GreetingService.class).get();
        
        logger.debug("======== call greetingService.hi...");
        String greeting = greetingService.hi("Jeff");
        
        logger.debug("======== Greeting: {}", greeting);
        
        greetingService.hi("Mike");
        
        weld.shutdown();
    }
}
