package com.lounger.db;

/**
 * 
 * <pre>项目名称：Lounger    
 * 类名称：Column    
 * 类描述：表与实体类的对应      
 * @version </pre>
 */
public class Column {
	//列名称
	private String columnNmae;
	//属性名称
	private String fieldNmae;
	//属性类型名称
	private String className;
	//数据库类型
	private String dataType;
	//列序号
	private int serial;
	
	public int getSerial() {
		return serial;
	}
	public void setSerial(int serial) {
		this.serial = serial;
	}
	public String getColumnNmae() {
		return columnNmae;
	}
	public void setColumnNmae(String columnNmae) {
		this.columnNmae = columnNmae;
	}
	public String getFieldNmae() {
		return fieldNmae;
	}
	public void setFieldNmae(String fieldNmae) {
		this.fieldNmae = fieldNmae;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public Column(String columnNmae, String fieldNmae, String className,
			String dataType) {
		super();
		this.columnNmae = columnNmae;
		this.fieldNmae = fieldNmae;
		this.className = className;
		this.dataType = dataType;
	}
	public Column(String columnNmae, String fieldNmae, String className) {
		super();
		this.columnNmae = columnNmae;
		this.fieldNmae = fieldNmae;
		this.className = className;
	}
	public Column() {
		super();
	}
}
