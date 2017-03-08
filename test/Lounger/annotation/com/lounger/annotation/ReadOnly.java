package com.lounger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * <pre>项目名称：Lounger    
 * 类名称：ReadOnly    
 * 类描述：    事务制度注解      
 * @version </pre>
 */
@Target(value={ElementType.METHOD,ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadOnly {

	boolean value();
	
}
