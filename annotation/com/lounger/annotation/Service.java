package com.lounger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>项目名称：Lounger  
 * 类名称：Service    
 * 类描述：说明该类为业务处理成    
 * @version </pre>
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

	String value() default "";

}
