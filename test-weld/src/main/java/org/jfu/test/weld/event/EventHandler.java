package org.jfu.test.weld.event;

import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventHandler {
    
    private static Logger logger = LoggerFactory.getLogger(EventHandler.class);

    public void hanldeLoggedInEvent(@Observes @Role("user") LoggedInEvent loggedInEvent) {
        logger.debug("======== {} logged in as role {}.", loggedInEvent.getUserName(), loggedInEvent.getRole());
    }
}
