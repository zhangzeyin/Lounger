package com.lounger.web;


/**
 * 
 * <pre>项目名称：Lounger      
 * 类名称：LoungerException    
 * 类描述： Lounger 异常
 * @version </pre>
 */
public class LoungerException extends Exception {

	private static final long serialVersionUID = 8541173464009004688L;
	public LoungerException() {}
	public LoungerException(String string) {
		super(string);  
	}
	
	 public void LoungerException(String string) throws LoungerException {
	        throw new LoungerException(string);
	} 
}
