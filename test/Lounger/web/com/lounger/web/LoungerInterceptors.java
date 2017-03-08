package com.lounger.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * <pre>项目名称：Lounger     
 * 类名称：LoungerInterceptors    
 * 类描述：    项目拦截器的处理类
 * @version </pre>
 */
public abstract class LoungerInterceptors {
	public boolean state=false;
	public HttpServletRequest req;
	public HttpServletResponse res;
	 public abstract void Interceptor(HttpServletRequest request, HttpServletResponse response) throws Exception;
	 
	 public  void invoke(HttpServletRequest request, HttpServletResponse response){
		 req=request;
		 res=response;
		 state=true;
	 }
}
