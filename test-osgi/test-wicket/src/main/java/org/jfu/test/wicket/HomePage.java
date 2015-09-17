package org.jfu.test.wicket;

import javax.inject.Inject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.ops4j.pax.cdi.api.OsgiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahamojo.dam.repo.api.AssetService;

public class HomePage extends WebPage {

    private static final long serialVersionUID = 7233573700913648653L;
    
    private static Logger logger = LoggerFactory.getLogger(HomePage.class);

    @Inject
    @OsgiService
    private AssetService assetService;

    public HomePage() {
        super();
        logger.debug("======== assetService: {}", assetService);
        add(new Label("test", "Hello world!"));
    }

}
