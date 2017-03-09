package com.lounger.web;

/**
 * 
 * <pre>项目名称：Lounger    
 * 类名称：LoungerWebException    
 * 类描述：    web异常
 * @version </pre>
 */
public class LoungerWebException extends Exception {

	private static final long serialVersionUID = 8541173464009004688L;
	public LoungerWebException() {}
	public LoungerWebException(String string) {   
		super(string);  
	}
	
	 public void LoungerWebException(final String string) throws LoungerWebException {
	        throw new LoungerWebException(string);
	} 
}
