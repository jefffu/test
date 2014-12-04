package org.test.aether;

import java.io.File;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.deployment.DeployRequest;
import org.eclipse.aether.deployment.DeploymentException;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.installation.InstallationException;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.transport.wagon.WagonTransporterFactory;
import org.eclipse.aether.util.artifact.SubArtifact;
import org.eclipse.aether.util.repository.AuthenticationBuilder;

public class App {

    private static RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils
                .newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class,
                BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class,
                FileTransporterFactory.class);
        locator.addService(TransporterFactory.class,
                HttpTransporterFactory.class);

        locator.setErrorHandler(new DefaultServiceLocator.ErrorHandler() {
            @Override
            public void serviceCreationFailed(Class<?> type, Class<?> impl,
                    Throwable exception) {
                exception.printStackTrace();
            }
        });

        return locator.getService(RepositorySystem.class);
    }

    private static DefaultRepositorySystemSession newRepositorySystemSession(
            RepositorySystem system) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils
                .newSession();

        LocalRepository localRepo = new LocalRepository("target/local-repo");
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(
                session, localRepo));

        session.setTransferListener(new ConsoleTransferListener());
        session.setRepositoryListener(new ConsoleRepositoryListener());

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null );

        return session;
    }

    public static void main(String[] args)
            throws DependencyCollectionException,
            DependencyResolutionException, DeploymentException,
            InstallationException {

        RepositorySystem repoSystem = newRepositorySystem();
        DefaultRepositorySystemSession session = newRepositorySystemSession(repoSystem);

        File jarFile = new File(
                "/home/jefffu/.m2/repository/org/eclipse/aether/aether-api/1.0.0.v20140518/aether-api-1.0.0.v20140518.jar");

        Artifact jarArtifact = new DefaultArtifact("org.eclipse.aether",
                "aether-api", "", "jar", "1.0.0.v20140518", null, jarFile);
        System.out.println(jarFile);
        jarArtifact.setFile(jarFile);

        System.out.println(jarArtifact.getFile());

        Artifact pomArtifact = new SubArtifact(
                jarArtifact,
                "",
                "pom",
                new File(
                        "/home/jefffu/.m2/repository/org/eclipse/aether/aether-api/1.0.0.v20140518/aether-api-1.0.0.v20140518.pom"));

        RemoteRepository testHttpRepo = new RemoteRepository.Builder(
                "testRepo", "default",
                "http://localhost:8081/nexus/content/repositories/testrepo/")
                .setAuthentication(
                        new AuthenticationBuilder().addUsername("jefffu")
                                .addPassword("123456").build()).build();

//        RemoteRepository testSshRepo = new RemoteRepository.Builder(
//                "testSshRepo", "sshDefault",
//                "scp://repo.tianji.com:/tmp/maven/repo").setAuthentication(
//                new AuthenticationBuilder().addUsername("jefffu")
//                        .addPrivateKey("/home/jefffu/.ssh/id_rsa", "123456")
//                        .build()).build();

        DeployRequest request = new DeployRequest();
        request.setRepository(testHttpRepo);
        request.addArtifact(jarArtifact).addArtifact(pomArtifact);

        repoSystem.deploy(session, request);
    }
}
