package com.lounger.db;

import com.lounger.web.LoungerException;

/**
 * 
 * <pre>项目名称：Lounger     
 * 类名称：LoungerDBException    
 * 类描述：数据库 sql等异常       
 * @version </pre>
 */
public class LoungerDBException extends Exception {

	private static final long serialVersionUID = 1L;
	public LoungerDBException() {}
	public LoungerDBException(String string) {
		super(string);  
	}
	 public void LoungerDBException(String string) throws LoungerDBException {
	        throw new LoungerDBException(string);
	} 

}
