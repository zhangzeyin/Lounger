package com.lounger.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.lounger.db.Connect;
import com.lounger.db.Table;
/**
 * <pre>
 * 项目名称：Lounger  
 * 类名称：Parameter    
 * 类描述：框架的核心参数
 * @version </pre>
 */
public class Parameter {
	/**
	 * 项目路径 
	 */
	public static String RootPath="";
	/**
	 * 是否打印sql
	 */
	public static boolean sqlprint=false; 
	/**
	 * 要在项目初始化时扫描的包
	 */
	public static String WebRootPath=""; 
	/**
	 * 返回页面的前缀
	 */
	public static String VideoPath="";
	/**
	 * 项目的class文件根路径
	 */
	public static String RootClassPath="";	
	/**
	 * 所有的数据库连接信息
	 *  key Databasename
	 *  value 连接池
	 */
	public static Map<String,BasicDataSource> Database=new HashMap<String,BasicDataSource>();
	/**
	 * 所有的数据库连接类型
	 * key Databasename
	 * vaalue type
	 */
	public static Map<String,String> DatabaseType=new HashMap<String,String>();
	/**
	 * 请求后台的后缀名 根据这些后缀进行拦截
	 */
	public static Map<String,String> WebPostfix=new HashMap<String,String>();
	/**
	 * 拦截器类集合名称
	 */
	public static List<String> Interceptors=new ArrayList<String>();
	/**
	 * 所有在RootPath中的类
	 */
	public static List<Class<?>> ClassAll=null;
	/**
	 * 所有可访问的路径所对应的方法
	 */
	public static Map<String, Method> PathMap=new HashMap<String, Method>();
	/**
	 * 所有可访问的路径所对应的方法
	 */
	public static Map<String, String> pathMethodMap=new HashMap<String, String>();
	/**
	 * 所有可访问的路径所对应的方法的参数名称
	 */
	public static Map<String, List<String>> MethodParameterName=new HashMap<String, List<String>>();
	/**
	 * 所有在RootPath下类名上存在注解的类 <类名,该类对象> 
	 */
	public static Map<String, Object> AnnotationClass=new HashMap<String, Object>();
	/**
	 * 带有Service Persistence的注解<类名,注解value>
	 */
	public static Map<String,String> ImmitMap=new HashMap<String,String>();
	/**
	 * 文件上传工厂
	 */
	public static ServletFileUpload FileUpload=null;
	/**
	 * 临时文件存储路径
	 */
	public static String Repository=null;
	/**
	 * 文件大小超过该值则创建临时文件
	 */
	public static Integer SizeThreshold =null;
	/**
	 * 上传文件的大小限制
	 */
	public static Long SizeMax=null;
	/**
	 * 数据库ban集合
	 */
	public static Map<String,Table> BanTableMap=new HashMap<String,Table>();
	/**
	 * 数据库连接所对应的所有表
	 */
	public static Map<String,List<String>> DataTableMap=new HashMap<String,List<String>>();
	/**
	 * 线程中的数据库连接 是否只读 数据连接池名称
	 * key 线程id
	 * value 线程相关信息  
	 */
	public static Map<Long,Connect> ThreadMap=new HashMap<Long,Connect>();
	/**
	 * 定时器类集合
	 * key 类
	 * value 时间
	 */
	public static Map<Class<?>,String> JobMap=new HashMap<Class<?>,String>();
	
}
