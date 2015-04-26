package org.jfu.test.wab;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class HomePage extends WebPage {

    private static final long serialVersionUID = 7233573700913648653L;

    public HomePage() {
        super();
        add(new Label("test", "Hello world!"));
    }

}
