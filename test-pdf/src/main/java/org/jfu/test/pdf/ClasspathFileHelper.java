package org.jfu.test.pdf;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClasspathFileHelper {
    private static Logger logger = LoggerFactory.getLogger(ClasspathFileHelper.class);

    public static Path getPath(ClassLoader classLoader, String classPathFile) {
        Path path = Paths.get(classLoader.getResource(classPathFile).getPath());
        if (logger.isDebugEnabled()) {
            logger.debug("Path: " + path.toString());
        }
        return path;
    }
}
