package com.lounger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * <pre>项目名称：Lounger 
 * 类名称：WebPath    
 * 类描述：说明该类或该方法的访问路径    
 * @version </pre>
 */
@Target(value={ElementType.METHOD,ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WebPath {

	String value();

}
