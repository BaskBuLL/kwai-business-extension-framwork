package com.kuaishou.business.extension.spring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Autowired;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface KExtPointInvoke {

    String defaultImpl() default "";

}
