package org.jfu.test.sisu.app;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jfu.test.sisu.api.TestSisu;

@Named
@Singleton
public class Test {

    @Inject
    private TestSisu testSisu;

    public void test() {
        testSisu.sayHello();
    }
}
