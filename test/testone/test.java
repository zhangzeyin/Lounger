package testone;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Timer;


import org.apache.log4j.Logger;

import com.lounger.annotation.Immit;
import com.lounger.annotation.ReturnBody;
import com.lounger.annotation.WebPath;


@WebPath("/test")

public class test {
	@Immit("sa")
	private Service1 ser;
	
	
@WebPath("/test.do")
@ReturnBody
//@Method(MethodType.POST)
public  String magn(String[] jh){
	System.out.println(Thread.currentThread().getId());
	ser.test();
	ser.test1();
	new Timer();
	//System.out.println("ok");

	return "问问nihao";

			
}

public static void main(String[] args) throws Exception {
	
	System.out.println("12");
}



}
