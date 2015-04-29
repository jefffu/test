package org.jfu.test.wab;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.protocol.http.WebApplication;
import org.jfu.test.jooq.api.AuthorDao;
import org.jfu.test.jooq.api.tables.records.AuthorRecord;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class HomePage extends WebPage {

    private static final long serialVersionUID = 7233573700913648653L;

    public HomePage() {
        super();

        WebApplication webApplication = WebApplication.get();
        BundleContext bundleContext = (BundleContext) webApplication
                .getServletContext().getAttribute("osgi-bundlecontext");

        ServiceReference<AuthorDao> sfAuthorDao = bundleContext.getServiceReference(AuthorDao.class);
        AuthorDao authorDao = bundleContext.getService(sfAuthorDao);

        AuthorRecord ar = authorDao.getAuthor(1);
        String greeting = "";
        if (ar != null) {
            greeting = "Hello world, " + ar.getFirstName() + " " + ar.getLastName() + "!";
        } else {
            greeting = "Hello world!";
        }

        add(new Label("test", greeting));
    }

}
