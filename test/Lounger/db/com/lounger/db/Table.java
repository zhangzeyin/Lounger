package com.lounger.db;

import java.util.Map;
/**
 * 
 * <pre>项目名称：Lounger      
 * 类名称：Table    
 * 类描述：表对应实体    
 * @version </pre>
 */
public class Table {
	//表名称
	private String tableName;
	//表的实体类名称
	private String className;
	//表列的集合
	private Map<String,Column> columns;
	//表的id对象
	private Id id;
	//表的数据源名称
	private String database;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String, Column> getColumns() {
		return columns;
	}
	public void setColumns(Map<String, Column> columns) {
		this.columns = columns;
	}
	public Id getId() {
		return id;
	}
	public void setId(Id id) {
		this.id = id;
	}
	
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public Table(String tableName, String className, Map<String,Column> columns, Id id,String database) {
		super();
		this.tableName = tableName;
		this.className = className;
		this.columns = columns;
		this.id = id;
		this.database=database;
	}
	public Table() {
		super();
	}

}
