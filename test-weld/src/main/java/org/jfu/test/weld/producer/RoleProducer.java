package org.jfu.test.weld.producer;

import java.util.Random;

import javax.enterprise.inject.Produces;

public class RoleProducer {
    
    private final static String[] ROLES = {"admin", "user", "manager"};

    @Produces
    @RandomRole
    public String randomRole() {
        Random random = new Random(System.nanoTime());
        return ROLES[random.nextInt(3)];
    }
}
