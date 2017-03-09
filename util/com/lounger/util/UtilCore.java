package com.lounger.util;

import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.lounger.core.Parameter;
import com.lounger.web.LoungerException;

/**
 * <pre>项目名称：Lounger   
 * 类名称：UtilCore 
 * 类描述：工具方法 
 * @version 
 * </pre>
 */
public class UtilCore {
	private static Logger log = Logger.getLogger(UtilCore.class);
	
	/**
	 * <pre>
	 * evalue(获取节点中 某节点的值)   
	 * 创建时间：2016-12-7 下午4:39:11    
	 * @param root 节点
	 * @param xPath 某节点
	 * @return
	 * </pre>
	 */
	public static String evalue(Element root, String xPath) {
		Element e = (Element) root.selectSingleNode(xPath);
		if (e == null || "".equals(e.getText()))
			return null;
		else
			return e.getText();
	}

	/**
	 * <pre>
	 * isNumber(判断该字符串是否为数字)   
	 * 创建时间：2016-12-7 下午4:38:04    
	 * @param str
	 * @return
	 * </pre>
	 */
	public static boolean isNumber(String str) {
		try {
			Long.valueOf(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * <pre>isDatabase(判断是否存在数组中的数据库连接)   
	 * 创建人：张泽寅
	 * 创建时间：2017-1-24 上午10:04:26    
	 * 修改人：张泽寅  
	 * 修改时间：2017-1-24 上午10:04:26    
	 * 修改备注： 
	 * @param Dbnames 
	 * @return</pre>
	 */
	public static boolean isDatabase(String[] Dbnames) {
		Set<String> dbs=Parameter.DataTableMap.keySet();
		for(String dbn:Dbnames){
			boolean isnull=false;
			for (String db : dbs) {
				if(db.equals(dbn)){
					isnull=true;
					
				}
			}
			if(!isnull){
				try {
					new LoungerException()
							.LoungerException("不存在名为“"+dbn+"”的数据库连接");
				} catch (LoungerException e) {
					log.error(e.getMessage(), e);
				}
				return false;
			}
		}
		return true;
	}
}
