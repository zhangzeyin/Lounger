package com.lounger.db;

/**
 * 
 * <pre>项目名称：Lounger    
 * 类名称：Id    
 * 类描述：数据库id字段的对应相关信息
 * @version </pre>
 */
public class Id {
	//id字段数据库名称
	private String idName;
	//id类型
	private String idClass;
	//id生成策略
	private IdEmum tactics;
	//如果是oracle的话需要sequence序列
	private String sequence;
	public String getIdName() {
		return idName;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}
	public String getIdClass() {
		return idClass;
	}
	public void setIdClass(String idClass) {
		this.idClass = idClass;
	}
	
	public IdEmum getTactics() {
		return tactics;
	}
	public void setTactics(IdEmum tactics) {
		this.tactics = tactics;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public Id(String idName, String idClass, IdEmum tactics,String sequence) {
		super();
		this.idName = idName;
		this.idClass = idClass;
		this.tactics = tactics;
		this.sequence = sequence;
	}
	public Id() {
		super();
	}

}
