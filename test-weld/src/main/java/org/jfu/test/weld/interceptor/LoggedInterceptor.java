package org.jfu.test.weld.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Logged
@Interceptor
public class LoggedInterceptor {
    
    private static Logger logger = LoggerFactory.getLogger(LoggedInterceptor.class);

    @AroundInvoke
    public Object logMethodEntry(InvocationContext invocationContext) throws Exception {
        
        long cost = System.nanoTime();
        Object object = invocationContext.proceed();
        cost = System.nanoTime() - cost;
        
        logger.debug("------- Call method {}.{} cost {} ns.", invocationContext.getMethod().getDeclaringClass().getName(), invocationContext.getMethod().getName(), cost);
        
        return object;
        
    }
}
