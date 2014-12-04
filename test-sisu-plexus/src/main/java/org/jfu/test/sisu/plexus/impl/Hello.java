package org.jfu.test.sisu.plexus.impl;

import org.codehaus.plexus.component.annotations.Component;
import org.jfu.test.sisu.plexus.Greeting;

@Component(role=Greeting.class, hint="hello")
public class Hello implements Greeting {

    @Override
    public void say(String name) {
        System.out.println("Hello, " + name+ "!");
    }

}
