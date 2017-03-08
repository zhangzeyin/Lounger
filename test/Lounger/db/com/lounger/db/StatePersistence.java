package com.lounger.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lounger.core.Parameter;

/**
 * 
 * <pre>项目名称：Lounger      
 * 类名称：StatePersistence    
 * 类描述：    获取数据库连接和sql处理对象      
 * @version </pre>
 */
public class StatePersistence {
	private static Logger log = Logger.getLogger(StatePersistence.class);
	
	public Connection GetConnection(){
		Connection con=null;
		long threadId = Thread.currentThread().getId(); 
		 Connect connent = Parameter.ThreadMap.get(threadId);
		 Map<String, Connection> Connections = connent.getConnections();
		 if(Connections==null){
			 Connections=new HashMap<String, Connection>();
		 }
		 con=Connections.get(connent.getDatabase());
		 if(con==null){
			 try {
				con=Parameter.Database.get(connent.getDatabase()).getConnection();
				Connections.put(connent.getDatabase(), con);
				connent.setConnections(Connections);
				
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		 }
			 try {
				 con.setAutoCommit(false); 
				 if(connent.getReadOnly()!=null){
					 con.setReadOnly(connent.getReadOnly());
				 }else{
					 con.setReadOnly(false);
				 }
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		return con;
	}
	
	public SqlQuery getSqlQuery(){
		SqlQuery sqlQuery = new SqlQuery();
		sqlQuery.setCon(GetConnection());
		return sqlQuery;
	}
}

