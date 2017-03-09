package testone;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;



public class cd {
	 public void go() throws Exception { 
	      SchedulerFactory sf = new StdSchedulerFactory(); 
	        Scheduler sched = sf.getScheduler(); 
	        
	        JobDetail job = JobBuilder.newJob(SayHello.class).withIdentity("job1", "group1").build(); 
	        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build(); 
	        sched.scheduleJob(job, trigger); 
	       	 
	       sched.start(); 
	      
	} 
	 

		public static void main(String[] args) throws Exception { 
	 
	        cd test = new cd(); 
	        test.go(); 
	    } 


}
