package org.jfu.test.sisu.plexus.impl;

import org.codehaus.plexus.component.annotations.Component;
import org.jfu.test.sisu.plexus.Greeting;

@Component(role=Greeting.class, hint="hi")
public class Hi implements Greeting {

    @Override
    public void say(String name) {
        System.out.println("Hi, "+name+"!");
    }

}
