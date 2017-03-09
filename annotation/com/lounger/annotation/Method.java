package com.lounger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lounger.web.MethodType;

/**
 * 
 * <pre>项目名称：Lounger    
 * 类名称：Method    
 * 类描述：请求方式注解   
 * @version </pre>
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Method {
	
	MethodType value() ;

}
