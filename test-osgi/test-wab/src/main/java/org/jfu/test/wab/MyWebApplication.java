package org.jfu.test.wab;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class MyWebApplication extends WebApplication {

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

}
