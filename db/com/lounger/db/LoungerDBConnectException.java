package com.lounger.db;


/**
 * <pre>项目名称：Lounger    
 * 类名称：LoungerDBConnectException    
 * 类描述：  数据库加载异常类  
 * @version </pre>
 */
public class LoungerDBConnectException extends Exception {

	private static final long serialVersionUID = 8541173464009004688L;
	public LoungerDBConnectException() {}
	public LoungerDBConnectException(String string) {
		super(string);  
	}
	 public void LoungerDBConnectException(String string) throws LoungerDBConnectException {
	        throw new LoungerDBConnectException(string);
	} 

}
