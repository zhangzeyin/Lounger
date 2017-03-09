package com.lounger.core;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


import com.lounger.web.GetClass;
import com.lounger.web.LoungerException;

/**
 * <pre>
 * 项目名称：Lounger  
 * 类名称：LoungerListener 
 * 类描述： 项目加载时加载配置
 * @version </pre>
 */
public class LoungerListener extends HttpServlet implements
		ServletContextListener {
	private static Logger log = Logger.getLogger(LoungerListener.class);
	private static final long serialVersionUID = 7590212268674269838L;

	public void contextDestroyed(ServletContextEvent arg0) {

	}

	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext servletContext = arg0.getServletContext();
        String log4jLocation = servletContext.getInitParameter("log4j-properties");  
        Parameter.RootClassPath = this.getClass().getClassLoader()
        		.getResource("").getPath();
        Parameter.RootPath = arg0.getServletContext().getRealPath("/");


        if (log4jLocation == null) {  
            BasicConfigurator.configure();  
        } else {  
            String webAppPath = servletContext.getRealPath("/");  
            if (log4jLocation.indexOf("classpath:") != -1){
            	
            	log4jLocation=Parameter.RootClassPath
				+ log4jLocation.replace("classpath:", "");
            }else{
            	log4jLocation=Parameter.RootPath +log4jLocation;
            }

            String log4jProp = webAppPath + log4jLocation;  
            File yoMamaYesThisSaysYoMama = new File(log4jProp);  
            if (yoMamaYesThisSaysYoMama.exists()) {  
                PropertyConfigurator.configure(log4jProp);  
            } else {  
                BasicConfigurator.configure();  
            }  
        }  
				
		System.out.println("-----------------------------LOUNGER--------------------------");
		
		
		try {
			String loungerconfi = servletContext
					.getInitParameter("loungerconfig");

			if (loungerconfi != null && loungerconfi.trim() != "") {
				ReadConfig redConfig=new ReadConfig();
				redConfig.Redconfig(loungerconfi);
			}
			RedWebPath();
			if (Parameter.SizeMax != null && Parameter.SizeThreshold != null) {
				DiskFileItemFactory diskFactory = new DiskFileItemFactory();
				diskFactory.setSizeThreshold(Parameter.SizeThreshold);
				diskFactory.setRepository(new File(Parameter.Repository));
				ServletFileUpload upload = new ServletFileUpload(diskFactory);
				upload.setSizeMax(Parameter.SizeMax);
				Parameter.FileUpload = upload;
			}
			Verification.VerificationTable();
			Redjob();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void Redjob() throws SchedulerException {
		System.out.println("===============================RELEASE THE TIMER BEGIN===============================");
		 SchedulerFactory sf = null; 
	        Scheduler sched = null;
	for (Class<?> Jobclass : Parameter.JobMap.keySet()) {
		if(sf==null){
			sf = new StdSchedulerFactory(); 
			 sched = sf.getScheduler();
		}
		
		JobDetail job = JobBuilder.newJob((Class<? extends org.quartz.Job>) Jobclass).withIdentity(Jobclass.getName()+"(job)", "group").build(); 
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(Jobclass.getName()+"(trigger)", "group").withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build(); 
		sched.scheduleJob(job, trigger); 
		log.info("RELEASE THE TIMER:"+Jobclass.getName());
		
	}
 	if(sched!=null){	 
    sched.start();
 	}
	}

	/**
	 * <pre>
	 * RedWebPath(记载控制层访问路径)   
	 * 创建时间：2016-12-7 下午4:58:03    
	 * 修改备注： 
	 * @throws Exception
	 * </pre>
	 */
	public void RedWebPath() throws Exception {
		if (!Parameter.WebRootPath.equals("")) {

			Parameter.ClassAll = GetClass.getClasses(Parameter.WebRootPath);
			DisposeAnnotation.load();

		} else {
			try {
				new LoungerException()
						.LoungerException("加载失败：请在xml中定义<RootPath>");
			} catch (LoungerException e) {
				log.error(e.getMessage(), e);
				System.exit(0);
			}
		}

	}

	
	
}
