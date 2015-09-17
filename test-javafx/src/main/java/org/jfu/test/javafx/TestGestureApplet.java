package org.jfu.test.javafx;

import java.applet.Applet;

public class TestGestureApplet extends Applet {
    
    private static final long serialVersionUID = 571912156676601556L;

    @Override
    public void start() {
        TestGesture.launch();
    }

}
