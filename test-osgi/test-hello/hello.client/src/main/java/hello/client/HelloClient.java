package hello.client;

import hello.service.Hello;

public class HelloClient implements Runnable {

    private static final int DELAY = 10000;

    private Hello[] m_hello;

    private String m_name;

    private boolean m_end;

    @Override
    public void run() {
         while (!m_end) {
             try {
                 invokeHelloServices();
                 Thread.sleep(DELAY);
             } catch (InterruptedException ie) {
                 /* will recheck end */
             }
         }
    }

    public void invokeHelloServices() {
        for (int i = 0; i < m_hello.length; i++) {
            // Update with your name.
            System.out.println(m_hello[i].sayHello(m_name));
        }
    }

    public void starting() {
        System.out.println("======== starting...");
        Thread thread = new Thread(this);
        m_end = false;
        thread.start();
    }

    public void stopping() {
        System.out.println("======== stopping...");
        m_end = true;
    }

}
