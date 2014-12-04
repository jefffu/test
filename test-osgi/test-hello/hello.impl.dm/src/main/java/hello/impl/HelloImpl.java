package hello.impl;

import hello.service.Hello;

public class HelloImpl implements Hello {

    @Override
    public String sayHello(String name) {
        return "[DM] Hello, " + name + ".";
    }

}
