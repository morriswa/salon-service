package org.morriswa.salon.annotations;

import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@WithUserDetails(userDetailsServiceBeanName = "testEmployeeAccount")
public @interface WithEmployeeAccount {
}
