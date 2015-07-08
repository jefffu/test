package org.jfu.test.cdi.app;


import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.jfu.test.cdi.api.HelloService;

@Command(scope = "cdi", name = "test")
@Service
public class CdiTestCommand implements Action {

    @Reference
    private HelloService helloService;

    @Override
    public Object execute() throws Exception {
        // TODO Auto-generated method stub
        System.out.println(helloService);
        if (helloService != null) {
            helloService.say();
        }
        return null;
    }

}
