package com.lounger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <pre>项目名称：Lounger    
 * 类名称：Column    
 * 类描述：  列注解
 * @version </pre>
 */
@Target(value={ElementType.FIELD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	boolean value() default true;
	String name() default "";
}
