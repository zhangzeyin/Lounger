package com.lounger.core;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;


import com.lounger.annotation.Database;
import com.lounger.annotation.Persistence;
import com.lounger.annotation.ReadOnly;
import com.lounger.annotation.Service;
import com.lounger.db.Connect;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;



/**
 * 
 * <pre>
 * 项目名称：Lounger     
 * 类名称：MethodLIstener    
 * 类描述：   server dao 的拦截器
 * @version </pre>
 */
public class MethodLIstener implements MethodInterceptor{
	private static Logger log = Logger.getLogger(MethodLIstener.class);
	

	private Enhancer enhancer = new Enhancer();  
	 public Object getProxy(Class<?> clazz){  
	  enhancer.setSuperclass(clazz);  
	  enhancer.setCallback(this);  
	  return enhancer.create();  
	 }  
	 //实现MethodInterceptor接口方法  
	 public Object intercept(Object obj, Method method, Object[] args,  
	   MethodProxy proxy) throws Throwable { 
		 long threadId = Thread.currentThread().getId(); 
		 Connect connent = Parameter.ThreadMap.get(threadId);

		Class<?> cla= method.getDeclaringClass();
		Service service = cla.getAnnotation(Service.class);
		Persistence persistence = cla.getAnnotation(Persistence.class);
		if(service!=null){
			ReadOnly readOnly = method.getAnnotation(ReadOnly.class);
			if(readOnly==null)
				readOnly = cla.getAnnotation(ReadOnly.class);
				
			if(connent==null){
				connent=new Connect();
			}
			
			connent.setReadOnly(readOnly.value());
			Parameter.ThreadMap.put(threadId,connent);
			
		}else if(persistence!=null){
			Database database = method.getAnnotation(Database.class);
			if(database==null)
				database = cla.getAnnotation(Database.class);
			if(connent==null){
				connent=new Connect();
			}
			connent.setDatabase(database.value());
			Parameter.ThreadMap.put(threadId,connent);
		}
		
		 
	  //通过代理类调用父类中的方法  
	  Object result = proxy.invokeSuper(obj, args);  
	  
	  if(service!=null){
		  clos(connent);
		  Parameter.ThreadMap.remove(threadId);
		}else if(persistence!=null){
			if(connent.getReadOnly()==null){
				clos(connent);
				Parameter.ThreadMap.remove(threadId);
			}
		}
	  return result;  
	 }  

	private void clos(Connect connent) {
		if (connent.getConnections() != null) {
			for (String name : connent.getConnections().keySet()) {
				Connection Connection = connent.getConnections().get(name);
				if (Connection != null) {

					try {
						if (!Connection.isReadOnly()) {
							Connection.commit();
						}
						Connection.close();
					} catch (SQLException e) {
						log.error(e.getMessage(), e);
					}
				}
			}

		}
	}

}
