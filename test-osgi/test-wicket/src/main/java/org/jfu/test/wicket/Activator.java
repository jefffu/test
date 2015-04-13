package org.jfu.test.wicket;

import java.util.Hashtable;

import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.apache.wicket.protocol.http.WicketFilter;
import org.ops4j.pax.web.extender.whiteboard.ExtenderConstants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

    private ServiceRegistration<Servlet> servletReg;
    private ServiceRegistration<Filter> filterReg;

    @Override
    public void start(BundleContext context) throws Exception {
        Hashtable<String, String> props = new Hashtable<String, String>();

        props.put("alias", "/wicket");
        servletReg = context.registerService(Servlet.class, new EmptyServlet(), props);


        WicketFilter filter = new WicketFilter(new MyWebApplication());
        filter.setFilterPath("/wicket");
        props = new Hashtable<String, String>();
        props.put("filter-name", "WicketFilter");
        props.put(ExtenderConstants.PROPERTY_URL_PATTERNS, "/wicket/*");
        filterReg = context.registerService(Filter.class, filter, props);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (servletReg != null) {
            servletReg.unregister();
            servletReg = null;
        }
        if (filterReg != null) {
            filterReg.unregister();
            filterReg = null;
        }
    }

}
