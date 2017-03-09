package com.lounger.db;

import java.sql.Connection;
import java.util.Map;

/**
 * 
 * <pre>项目名称：Lounger    
 * 类名称：Connect    
 * 类描述： seevice 事务控制以及数据库连接 
 */
public class Connect {
	//事务控制
	private Boolean readOnly;
	//数据源
	private String  database;
	//数据库连接
	private Map<String, Connection> Connections;
	
	
	public Boolean getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public Map<String, Connection> getConnections() {
		return Connections;
	}
	public void setConnections(Map<String, Connection> connections) {
		Connections = connections;
	}
	

}
