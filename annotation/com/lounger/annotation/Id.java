package com.lounger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lounger.db.IdEmum;

/**
 * 
 * <pre>项目名称：Lounger    
 * 类名称：Id    
 * 类描述： id注解    
 * @version </pre>
 */
@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
	
	IdEmum value() ;
	
	String sequence() default "";
	
}
