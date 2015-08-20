package org.jfu.test.logback;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDebug {

    private Logger logger = LoggerFactory.getLogger(TestDebug.class);

    @Test
    public void test() {
        logger.debug("Test...", new RuntimeException("Test Runtime Exception."));
    }
}
