package org.jfu.test.sisu.impl;

import org.jfu.test.sisu.api.TestSisu;
import org.osgi.service.log.LogService;

public class TestSisuImpl implements TestSisu {

    private LogService logService;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    @Override
    public void sayHello() {
        logService.log(LogService.LOG_INFO, "======== Hello World!");
    }

}
