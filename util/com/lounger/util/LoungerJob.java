package com.lounger.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.lounger.annotation.Immit;
import com.lounger.core.Parameter;
import com.lounger.web.LoungerException;



/**
 * 
 * <pre>项目名称：Lounger    
 * 类名称：LoungerJob    
 * 类描述：定时器的代理    
 * @version </pre>
 */
public abstract class LoungerJob implements Job{
	private static Logger log = Logger.getLogger(LoungerJob.class);
	/**
	 *二次调用定时器方法 以及为对象属性赋值
	 */
	public  void execute(JobExecutionContext arg0) throws JobExecutionException {
	Object	obj=this;
	Class<?> class1=obj.getClass();
	for (Field field : class1.getDeclaredFields()) {
		Immit immit = field.getAnnotation(Immit.class);
		field.setAccessible(true);
			Object fievalue;
			try {
				fievalue = field.get(obj);
				if(fievalue==null){
					if (field.getType().isInterface()) {
						field.getType().getGenericInterfaces();
						List<String> infslist = new ArrayList<String>();
						for (String service : Parameter.ImmitMap.keySet()) {
							for (Class<?> infsclass : Class.forName(service).getInterfaces()) {
								if (infsclass.getName().equals(field.getType().getName()))
									infslist.add(service);
							}
						}
						if (infslist.size() > 1) {
							int i = 0;
							for (String string : infslist) {
								if (immit.value().equals(
										Parameter.ImmitMap.get(string))) {
									i += 1;
									field.setAccessible(true);
									field.set(obj,Parameter.AnnotationClass.get(string));
								}
							}
							if (i == 0) {
								try {
									new LoungerException().LoungerException(class1 + ":"
											+ field.getName() + " (未找到" + field.getType()
											+ "的实现类) 该类的实现类较多希望给注解赋值");
								} catch (LoungerException e) {
									log.error(e.getMessage(), e);
									System.exit(0);
								}
							}
							if (i > 1) {
								try {
									new LoungerException().LoungerException(class1 + ":"
											+ field.getName() + " (未找到" + field.getType()
											+ "的实现类) 存在注解值相同的类");
								} catch (LoungerException e) {
									log.error(e.getMessage(), e);
									System.exit(0);
								}

							}
						} else if (infslist.size() == 1) {
							field.setAccessible(true);
							field.set(obj,Parameter.AnnotationClass.get(infslist.get(0)));
						} else {
							try {
								new LoungerException().LoungerException(class1 + ":"
										+ field.getName() + " (未找到" + field.getType()
										+ "的实现类)");
							} catch (LoungerException e) {
								log.error(e.getMessage(), e);
								System.exit(0);
							}
						}
						field.getType().getInterfaces();
					} else {
						field.setAccessible(true);
						field.set(obj, field
								.getType().newInstance());
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} 
			
	
	
		
		
	}
	Job(arg0);
	}
	public abstract  void Job(JobExecutionContext arg0) throws JobExecutionException ;
}
