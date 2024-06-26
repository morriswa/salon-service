package org.morriswa.salon.annotations;

import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@WithUserDetails(userDetailsServiceBeanName = "testAdminAccount")
public @interface WithAdminAccount {
}
