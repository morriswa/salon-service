package org.morriswa.eecs447.annotations;

import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@WithUserDetails(userDetailsServiceBeanName = "testClientAccount")
public @interface WithClientAccount {
}
