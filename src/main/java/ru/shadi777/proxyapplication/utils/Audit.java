package ru.shadi777.proxyapplication.utils;

import ru.shadi777.proxyapplication.enums.AuditType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {
    AuditType type() default AuditType.ALL;
}
