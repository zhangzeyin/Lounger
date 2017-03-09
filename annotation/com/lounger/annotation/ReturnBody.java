package com.lounger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * <pre>项目名称：Lounger    
 * 类名称：ReturnBody    
 * 类描述： 说明该方法是返回数据而非页面   
 * @version </pre>
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ReturnBody {

}
