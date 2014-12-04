package org.jfu.test.sisu.plexus;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.plexus.ContainerConfiguration;
import org.codehaus.plexus.DefaultContainerConfiguration;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

@Component(role=Main.class)
public class Main {

    @Inject
    @Named("hi")
//    @Requirement(role=Greeting.class, hint="hi")
    private Greeting greeting;

    @Requirement(hint="jefffu")
    private Person person;

    public void greeting() {
        greeting.say(person.getName());

        System.out.println(person);
    }

    public static void main(String[] args) throws PlexusContainerException, ComponentLookupException {
        ContainerConfiguration config = new DefaultContainerConfiguration();
        config.setClassPathScanning(PlexusConstants.SCANNING_ON);

        DefaultPlexusContainer container = new DefaultPlexusContainer(config);
        try {
            Main main = container.lookup(Main.class);
            main.greeting();
        } finally {
            container.dispose();
        }
    }
}
