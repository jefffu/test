package org.jfu.test.cdi.impl;

import org.jfu.test.cdi.api.HelloService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

@OsgiServiceProvider
public class HelloServiceImpl implements HelloService {

    @Override
    public void say() {

        System.out.println("Hello, world!");
    }

}
