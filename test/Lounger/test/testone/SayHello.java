package testone;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.lounger.annotation.Timer;
import com.lounger.util.LoungerJob;

@Timer("0/5 * * * * ?")
public class SayHello extends LoungerJob {
	//@Immit("sa")
	private Service1 ser;
	public int a=1;

	@Override
	public void Job(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("ok----------------------");
		//ser.test();
		
	}
	
	
}
