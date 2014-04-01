package com.cintel.frame.auth.context;

import org.springframework.util.Assert;


/**
 * 
 * @author WangShuDa
 *
 * @see org.acegisecurity.context.ThreadLocalSecurityContextHolderStrategy
 */
public class SecurityContextHolder {

    private static ThreadLocal<Object> contextHolder = new ThreadLocal<Object>();

    public static void clearContext() {
        contextHolder.set(null);
    }

    public static SecurityContext getContext() {
        if (contextHolder.get() == null) {
            contextHolder.set(new SecurityContextImpl());
        }

        return (SecurityContext) contextHolder.get();
    }

    public static void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        contextHolder.set(context);
    }
}
