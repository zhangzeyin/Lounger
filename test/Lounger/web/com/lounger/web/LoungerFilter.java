package com.lounger.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.lounger.annotation.ReturnBody;
import com.lounger.core.Parameter;
import com.lounger.util.ValueUtil;

/**
 * <pre>项目名称：Lounger   
 * 类名称：LoungerFilter    
 * 类描述： Lounger总控制器  
 * @version </pre>
 */
public class LoungerFilter implements Filter {
	private static Logger log = Logger.getLogger(LoungerFilter.class);
	
	String encoding = "";

	@SuppressWarnings("unused")
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		response.setCharacterEncoding(encoding);
		res.getWriter();
		boolean noweb = true;
		String uri = request.getRequestURI().replace("//", "/"); // uri就是获取到的连接地址!
		if (Parameter.WebPostfix != null&&uri.lastIndexOf(".")>uri.lastIndexOf("/")) {
			String WebPost=uri.substring(uri.lastIndexOf("."),uri.length());
			WebPost=Parameter.WebPostfix.get(WebPost);
				if (WebPost!=null) {
					noweb = false;
					uri = uri.replace(request.getContextPath(), "")
							.replace(WebPost, "").replace("//", "/");
						//拦截器状态
						boolean state=true;
						//循环所有拦截器类
					for (String  Interceptorname: Parameter.Interceptors) {
						try {
							if(state){
							//根据类名获取类
								Class<?> clas=Class.forName(Interceptorname);
								//获取该类对象实例
								Object obj=clas.newInstance();
								//获取拦截器主方法
								Method Interceptor=clas.getMethod("Interceptor",HttpServletRequest.class,HttpServletResponse.class);
								//执行主方法
								Interceptor.invoke(obj, request,response);
								//获取拦截器运行后状态 执行invoke方法后状态为true 默认false
								Field stat=clas.getField("state");
								state=(boolean) stat.get(obj);
								if(state){
									//状态为true时获取拦截器处理过后的request
									Field fildreq=clas.getField("req");
									request=(HttpServletRequest) fildreq.get(obj);
									//状态为true时获取拦截器处理过后的response
									Field fildres=clas.getField("res");
									response=(HttpServletResponse) fildres.get(obj);
								}
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						} 
					}
					if(state){
						// 实现具体的分发结构
						Method method = Parameter.PathMap.get(uri);
						if (method != null) {
							String methodtype=Parameter.pathMethodMap.get(uri);
							
							if(!methodtype.equals("")&&!methodtype.equals(request.getMethod())){
								chain.doFilter(request, response);
							}
							
							method.getAnnotation(ReturnBody.class);
							try {
	
								List<String> MethodParamNames = Parameter.MethodParameterName
										.get(uri);
								Class<?>[] MethodParamTypes = method
										.getParameterTypes();
								Object[] object = new Object[MethodParamNames
										.size()];
								for (int i = 0; i < object.length; i++) {
									object[i] = ValueUtil.GetValue(request,
											response, MethodParamNames.get(i),
											MethodParamTypes[i].getName(), null,
											null);
								}
								Object result=null;
								try {
									 result = method.invoke(
											Parameter.AnnotationClass.get(method
													.getDeclaringClass().getName()),
													object);
									
								} catch (Exception e) {
									log.error(e.getMessage(),e);
								}
	
								if (method.getAnnotation(ReturnBody.class) != null) {
									String ResultClassName = result.getClass()
											.getName();
	
									PrintWriter out = response.getWriter();
									if (result == null) 
										out.print("is null");
									 else {
										if (result.getClass().isArray()) {
											JSONArray jsonArray = JSONArray
													.fromObject(result);
											out.print(jsonArray.toString());
										} else {
											if (ResultClassName == "int"
													|| ResultClassName == "java.lang.Integer"
													|| ResultClassName == "byte"
													|| ResultClassName == "java.lang.Byte"
													|| ResultClassName == "short"
													|| ResultClassName == "java.lang.Short"
													|| ResultClassName == "long"
													|| ResultClassName == "java.lang.Long"
													|| ResultClassName == "float"
													|| ResultClassName == "java.lang.Float"
													|| ResultClassName == "double"
													|| ResultClassName == "java.lang.Double"
													|| ResultClassName == "boolean"
													|| ResultClassName == "java.lang.Boolean") {
												result = result + "";
											}
											if (!(result instanceof String)) {
												JSONObject jsonobject = JSONObject
														.fromObject(result);
												out.print(jsonobject.toString());
											} else {
												out.print(result);
											}
										}
									}
								} else {
									if (result instanceof String) {
										request.getRequestDispatcher(
												Parameter.VideoPath + result)
												.forward(request, response);
									}
								}
							} catch (Exception e) {
								log.error(e.getMessage(), e);
							}

						} else {
							chain.doFilter(request, response);
						}
					
					}	
				}
			

			if (noweb) {
				chain.doFilter(request, response);
			}

		} else {
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		encoding = filterConfig.getInitParameter("encoding");
	}

	public void destroy() {

	}
}
