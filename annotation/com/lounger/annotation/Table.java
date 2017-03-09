package com.lounger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <pre>项目名称：testone    
 * 类名称：Table    
 * 类描述： 表注解    
 * @version </pre>
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	String value() default "";

}
