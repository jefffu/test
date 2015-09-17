package org.jfu.test.weld.extension;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AfterTypeDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessBeanAttributes;
import javax.enterprise.inject.spi.ProcessInjectionPoint;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import javax.enterprise.inject.spi.ProcessProducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyExtension implements Extension {

    private static Logger logger = LoggerFactory.getLogger(MyExtension.class);
    
    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd, BeanManager beanManager) {
        logger.debug(">>>>>>>> BeforeBeanDiscovery: {}, beanManager: {}", bbd, beanManager );
    }
    
    public <X> void processAnnotatedType(@Observes ProcessAnnotatedType<X> pat) {
        logger.debug("-------- ProcessAnnotatedType: {}", pat.getAnnotatedType() );
    }
    
    public void afterTypeDiscovery(@Observes AfterTypeDiscovery atd) {
        logger.debug(">>>>>>>> AfterTypeDiscovery: {}", atd );
    }
    
    public <T, X> void processInjectionPoint(@Observes ProcessInjectionPoint<T, X> pip) {
        logger.debug("-------- ProcessInjectionPoint: {}", pip.getInjectionPoint());
    }
    
    public <T, X> void processInjectionTarget(@Observes ProcessInjectionPoint<T, X> pit) {
        logger.debug("-------- ProcessInjectionTarget: {}", pit.getInjectionPoint());
    }
    
    public <T> void processBeanAttributes(@Observes ProcessBeanAttributes<T> pba) {
        logger.debug("-------- ProcessBeanAttributes: {}", pba.getBeanAttributes());
    }
    
    public <X> void processBean(@Observes ProcessBean<X> pb) {
        logger.debug("-------- ProcessBean: {}", pb.getBean());
    }
    
    public <T, X> void processProducer(@Observes ProcessProducer<T, X> pp) {
        logger.debug("-------- ProcessProducer: {}", pp.getProducer());
    }
    
    public <T, X> void processObserverMethod(@Observes ProcessObserverMethod<T, X> pom) {
        logger.debug("-------- ProcessObserverMethod: {}", pom.getObserverMethod());
    }
    
    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd) {
        logger.debug(">>>>>>>> AfterBeanDiscovery: {}", abd );
    }
    
    public void afterDeploymentValidation(@Observes AfterDeploymentValidation adv, BeanManager beanManager) {
        logger.debug(">>>>>>>> AfterDeploymentValidation: {}, beanManager: {}", adv, beanManager);
    }

    public void beforeShutdown(@Observes BeforeShutdown bs) {
        logger.debug(">>>>>>>> BeforeShutdown: {}", bs );
    }
}
