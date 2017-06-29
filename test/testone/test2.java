package testone;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lounger.db.IdEmum;
import com.lounger.util.BasicFormatterImpl;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;



public class test2 {
	
	public static void main(String args[]) {
		
		String sql="select uid u,uname from ban b";
		
		
		try {
			Select select = (Select) CCJSqlParserUtil.parse(sql);
			PlainSelect plainselect = (PlainSelect) select.getSelectBody();
			net.sf.jsqlparser.schema.Table table = (net.sf.jsqlparser.schema.Table)plainselect.getFromItem();  
		  
		  List<SelectItem> c=  plainselect.getSelectItems();
		  
		  for (SelectItem selectItem : c) {
			  System.out.println( selectItem.toString());
			
		}
		    //processSelectBody(selectBody);  
		  //  System.out.println(select.toString());
		   // return select.toString();  
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(sql.matches("select * from A left join B on A_user_id=B_id left join C on A_device_id=C_id"));	
		String sqlA=sql.toLowerCase();
		//Connection con = GetConnection();
		PreparedStatement st = null; 
		if(sqlA.contains("select")){
			int selectnum=(","+sqlA+",").split("select").length-1;
			if(selectnum==12){
				String tabname = sql.substring(sqlA.indexOf("from")+4,sql.length()).trim();
				
				if(sqlA.indexOf("where")>-1){
					tabname=tabname.substring(0,tabname.indexOf("where")).trim();
				}else if(sqlA.indexOf("order")>-1){
					tabname=tabname.substring(0,tabname.indexOf("order")).trim();	
				}else if(sqlA.indexOf("group")>-1){
					tabname=tabname.substring(0,tabname.indexOf("group")).trim();	
				}
				if(tabname.toLowerCase().indexOf("jion")>-1){
				
				}
				
				for (String string : tabname.split("(J|j)(O|o)(I|i)(N|n)")) {
					 //string=string.replaceAll("( )((N|n)(A|a)(T|t)(U|u)(R|r)(A|a)(L|l)|(O|o)(U|u)(T|t)(E|e)(R|r)|(C|c)(R|r)(O|o)(S|s)(S|s)|(L|l)(E|e)(F|f)(T|t)|(R|r)(E|e)(I|i)(G|g)(H|h)(T|t)|(I|i)(N|n)(N|n)(E|e)(R|r)|(F|f)(U|u)(L|l)(L|l)|(S|s)(E|e)(L|l)(F|f))( )", " ");
					string=string.replaceAll(" (L|l)(E|e)(F|f)(T|t) ", " ");
					string=string.replaceAll(" (I|i)(N|n)(N|n)(E|e)(R|r) ", " ");
					string=string.replaceAll(" (R|r)(E|e)(I|i)(G|g)(H|h)(T|t) ", " ");
					string=string.replaceAll(" (C|c)(R|r)(O|o)(S|s)(S|s) ", " ");
					string=string.replaceAll(" (O|o)(U|u)(T|t)(E|e)(R|r) ", " ");
					string=string.replaceAll(" (N|n)(A|a)(T|t)(U|u)(R|r)(A|a)(L|l) ", " ");
				
					if(string.toLowerCase().indexOf("on")>-1){
						
						System.out.println(string.substring(0,string.toLowerCase().indexOf("on")));
					}else{
						System.out.println(string);
					}
				}
				
			}
			
			
		}
		
		
		}

}
