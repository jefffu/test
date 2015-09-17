package org.jfu.test.weld.service.impl;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.jfu.test.weld.event.LoggedInEvent;
import org.jfu.test.weld.event.RoleQualifier;
import org.jfu.test.weld.producer.RandomRole;
import org.jfu.test.weld.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginServiceImpl implements LoginService{
    
    private static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Inject
    @Any
    private Event<LoggedInEvent> loggedInEvent;
    
    @Inject
    @RandomRole
    private String role;

    @SuppressWarnings("serial")
    @Override
    public void login(String userName) {
        logger.debug("======== {} logged in as role {}, fire an event", userName, role);
        
        LoggedInEvent payload = new LoggedInEvent();
        payload.setUserName(userName);
        payload.setRole(role);

        loggedInEvent.select(new RoleQualifier() {
            
            @Override
            public String value() {
                return role;
            }
        }).fire(payload);
    }
    
    
}
