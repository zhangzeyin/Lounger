package com.lounger.db;
/**
 * 
 * <pre>项目名称：Lounger    
 * 类名称：IdEmum    
 * 类描述：    id的主键策略枚举    
 * @version </pre>
 */
public enum IdEmum {
	//uuid唯一标示 ，查询最大的id+1，当前时间毫秒值，数据库自增
	UUID,MAXID,TIMID,INCREMENT
}
