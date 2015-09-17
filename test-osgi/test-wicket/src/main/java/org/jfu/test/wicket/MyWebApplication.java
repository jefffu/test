package org.jfu.test.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.cdi.CdiConfiguration;
import org.apache.wicket.protocol.http.WebApplication;

public class MyWebApplication extends WebApplication {

    @Override
    protected void init() {
        super.init();
        
        new CdiConfiguration().configure(this);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

}
