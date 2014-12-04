package hello.impl.blueprint;

import hello.service.Hello;

public class HelloImpl implements Hello {

    @Override
    public String sayHello(String name) {
        return "[Blueprint] Hello, " + name + ".";
    }

}
