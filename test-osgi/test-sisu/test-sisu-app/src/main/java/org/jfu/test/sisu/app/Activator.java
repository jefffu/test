package org.jfu.test.sisu.app;

import org.eclipse.sisu.inject.DefaultBeanLocator;
import org.eclipse.sisu.inject.MutableBeanLocator;
import org.eclipse.sisu.launch.SisuExtender;
import org.eclipse.sisu.osgi.ServiceBindings;
import org.eclipse.sisu.space.BundleClassSpace;
import org.eclipse.sisu.space.SpaceModule;
import org.eclipse.sisu.wire.WireModule;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Activator extends SisuExtender {

    private void doStart(BundleContext context) {
        super.start(context);
    }

    private void doStop(BundleContext context) {
        super.stop(context);
    }

    @Override
    public void start(BundleContext context) {
        // TODO Auto-generated method stub
        ServiceReference<LogService> logServiceRef = context.getServiceReference(LogService.class);
        LogService logService = context.getService(logServiceRef);
        logService.log(LogService.LOG_INFO, "======== start: ");

        AbstractModule myModule = new AbstractModule() {

            @Override
            protected void configure() {
                final MutableBeanLocator locator = new DefaultBeanLocator();
                locator.add(new ServiceBindings(context, "*", "", Integer.MIN_VALUE));
                bind(MutableBeanLocator.class).toInstance(locator);

            }
        };

        Injector injector = Guice.createInjector(new WireModule(myModule, new SpaceModule(new BundleClassSpace(context.getBundle()))));
        logService.log(LogService.LOG_INFO, "======== injector: " + injector);

        doStart(context);

        Binding<Test> binding = injector.getBinding(Test.class);
        logService.log(LogService.LOG_INFO, "======== binding: " + binding);

        Test test = binding.getProvider().get();
        logService.log(LogService.LOG_INFO, "======== test: " + test);

        test.test();
    }

    @Override
    public void stop(BundleContext context) {
        doStop(context);
    }

}
