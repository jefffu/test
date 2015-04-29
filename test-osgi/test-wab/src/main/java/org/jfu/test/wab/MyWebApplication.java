package org.jfu.test.wab;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

public class MyWebApplication extends WebApplication {

    private LogService logService;

    @Override
    protected void init() {
        super.init();
        BundleContext bundleContext = (BundleContext)getServletContext().getAttribute("osgi-bundlecontext");

        ServiceReference<LogService> serviceReferenceLogService = bundleContext.getServiceReference(LogService.class);

        logService = bundleContext.getService(serviceReferenceLogService);

    }

    @Override
    public Class<? extends Page> getHomePage() {
        logService.log(LogService.LOG_INFO, ">>>>>>>> getHomePage()");
        return HomePage.class;
    }

}
