package com.lounger.core;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.lounger.util.UtilCore;
import com.lounger.web.LoungerException;
import com.lounger.web.LoungerInterceptors;
/**
 * <pre>
 * 项目名称：Lounger  
 * 类名称：RedConfig    
 * 类描述： 读取配置文件    
 * @version </pre>
 */
public class RedConfig {
	private static Logger log = Logger.getLogger(RedConfig.class);
	/**
	 * <pre>
	 * RedConfig(读取需要处理的xml)   
	 * 创建人：张泽寅
	 * 创建时间：2016-12-7 下午4:59:44    
	 * 修改人：张泽寅  
	 * 修改时间：2016-12-7 下午4:59:44    
	 * 修改备注： 
	 * @param config
	 * @throws Exception
	 * </pre>
	 */
	 public void Redconfig(String config) throws Exception {
		 System.out.println("===============================RED CONFIG XML BEGIN===============================");
		String[] configs = config.split(",");
		for (String conf : configs) {
			conf = conf.trim();
			String configpath = "";
			if (conf.indexOf("classpath:") != -1)
				configpath = Parameter.RootClassPath
						+ conf.replace("classpath:", "");
			else
				configpath = Parameter.RootPath +conf;

			String configpaths = configpath.substring(0,
					configpath.lastIndexOf("\\"));
			String filesname = configpath.substring(
					configpath.lastIndexOf("\\") + 1, configpath.length());
			if (conf.contains("*")) {
				File files = new File(configpaths);
				if (filesname.contains(".xml") || filesname.contains(".XML")) {
					filesname = filesname.replace(".xml", "")
							.replace("XML", "");
					if (files.isDirectory()) {
						for (String filename : files.list()) {
							if (isstipulate(filesname.split("\\*"), filename)) {
								if (filename.indexOf(".xml") > -1
										|| filename.indexOf(".XML") > -1) {
									RedXml(files.getPath() + "\\" + filename);
								}
							}
						}
					} else {
						try {
							new LoungerException().LoungerException("该文件("
									+ configpaths + ")路径不是文件夹");
						} catch (LoungerException e) {
							log.error(e.getMessage(), e);
							System.exit(0);
						}
					}
				} else {
					try {
						new LoungerException().LoungerException("请配置后缀为xml的文件");
						
					} catch (LoungerException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}
				}
			}else{
				if (filesname.contains(".xml") || filesname.contains(".XML")) {
					File file = new File(configpath);
					
					if(file.exists())    
					{ 
									RedXml(configpath);
					}else{
						
						try {
							new LoungerException().LoungerException("请检查是否存在（"+configpath+"）配置文件");
							
						} catch (LoungerException e) {
							log.error(e.getMessage(), e);
							System.exit(0);
						}
					}
					
				}else {
					try {
						new LoungerException().LoungerException("请配置后缀为xml的文件");
						
					} catch (LoungerException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}
				}
				
				
			}

		}

	}
	
	/**
	 * <pre>
	 * isstipulate(判断该文件名是否为规定的文件名格式)   
	 * 创建时间：2016-12-7 下午5:00:14    
	 * @param name 
	 * @param filename
	 * @return
	 * </pre>
	 */
	private  boolean isstipulate(String[] name, String filename) {
		int i = -1;
		for (String string : name) {
			if (filename.indexOf(string) > i) {
				i = filename.indexOf(string);
			} else {
				return false;
			}
		}

		return true;
	}

	/**
	 * <pre>
	 * RedXml(读取配置文件信息)   
	 * 创建时间：2016-12-7 下午5:01:36    
	 * @param configpath
	 * @throws Exception
	 * </pre>
	 */
	public  void RedXml(String configpath) throws Exception {
		log.info("RED XML:"+configpath);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(configpath));
		Element root = doc.getRootElement();
		if (root.getName().equals("Lounger")) {
			Element sqlprint = (Element) root
			.selectSingleNode("/Lounger/DatabaseTemplate");
			String sqlprintval=sqlprint.valueOf("@sqlprint");
			if(!sqlprintval.equals("")){
				Parameter.sqlprint=Boolean.parseBoolean(sqlprintval);
				
			}
			//处理数据库连接
			@SuppressWarnings("unchecked")
			List<Element> dc = root
					.selectNodes("/Lounger/DatabaseTemplate/Database");
			DisposeDatabase(dc,configpath);
			
			
			String WebPath = UtilCore.evalue(root, "WebPath");
			if (WebPath != null && !WebPath.trim().equals("")) {
				if (WebPath.indexOf(",") > -1) {
					String[] WebPaths = WebPath.trim().split(",");
					for (String path : WebPaths) {

						DisposeWebPath(path, configpath);

					}
				}

			} 
			String VideoPath = UtilCore.evalue(root, "VideoPath");
			if (VideoPath != null && !VideoPath.trim().equals("")) {
				Parameter.VideoPath = VideoPath;
			}
			String RootPath = UtilCore.evalue(root, "RootPath");
			if (RootPath != null && !RootPath.trim().equals("")) {

				if (Pattern.matches("[[a-zA-z0-9_]{1,}[.]{0,1}]{1,}", RootPath)) {
					Parameter.WebRootPath = RootPath;
				} else {
					try {
						new LoungerException()
								.LoungerException(configpath
										+ " 中定义<WebPath>参数错误（"
										+ RootPath
										+ "），请遵循正则表达式\"[[a-zA-z0-9_]{1,}[.]{0,1}]{1,}\"");
					} catch (LoungerException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		List<Element> Interceptors=root.selectNodes("/Lounger/Interceptors/Interceptor");
		if(Interceptors!=null&&Interceptors.size()>0){
			DisposeInterceptor(Interceptors,configpath);
		}
		Element FileItem = root.element("FileItemFactory");
		if (FileItem != null) {

			String Repository = UtilCore.evalue(FileItem, "Repository");
			if (Repository != null && !Repository.trim().equals("")) {
				Repository=Repository.trim();
				Parameter.Repository = Repository;
			} else {
				Parameter.Repository = System.getProperty("java.io.tmpdir");
			}
			String SizeThreshold = UtilCore.evalue(FileItem, "SizeThreshold")
					.trim();
			if (SizeThreshold != null && !SizeThreshold.equals("")) {
				if (UtilCore.isNumber(SizeThreshold)) {
					Parameter.SizeThreshold = Integer.valueOf(SizeThreshold);

				} else {
					try {
						new LoungerException()
								.LoungerException(configpath
										+ " 中定义<FileItemFactory> 定义<SizeThreshold>值必须为数字");
					} catch (LoungerException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}

				}

			} else {
				try {
					new LoungerException().LoungerException(configpath
							+ " 中定义<FileItemFactory> 必须定义<SizeThreshold>");
				} catch (LoungerException e) {
					log.error(e.getMessage(), e);
					System.exit(0);
				}

			}
			String SizeMax = UtilCore.evalue(FileItem, "SizeMax");
			if (SizeMax != null && !SizeMax.trim().equals("")) {
				SizeMax=SizeMax.trim();
				if (UtilCore.isNumber(SizeMax)) {
					Parameter.SizeMax = Long.valueOf(SizeMax);

				} else {
					try {
						new LoungerException()
								.LoungerException(configpath
										+ " 中定义<FileItemFactory> 定义<SizeMax>值必须为数字");
					} catch (LoungerException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}

				}

			} else {
				try {
					new LoungerException().LoungerException(configpath
							+ " 中定义<FileItemFactory> 必须定义<SizeMax>");
				} catch (LoungerException e) {
					log.error(e.getMessage(), e);
					System.exit(0);
				}

			}
		}

	}

/**
 * <pre>DisposeDatabase(处理数据库连接)   
 * 创建时间：2016-12-28 下午2:18:21    
 * @param dc
 * @param configpath</pre>
 */

	private void DisposeDatabase(List<Element> dc,String configpath) {
		for (Element element : dc) {
			Element db = element;
			
			
			BasicDataSource dataSource = new BasicDataSource();  
			 
			String Name = UtilCore.evalue(db, "Name");
			 if (Name != null && !Name.trim().equals("")) {
				 
				}else{
					try {
						new LoungerException().LoungerException(configpath+ " 中定义<Database>必须定义<Name>");
					} catch (LoungerException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}	
					
				}
			 String DriverClass = UtilCore.evalue(db, "DriverClass");
			 if (DriverClass != null && !DriverClass.trim().equals("")) {
				 DriverClass = DriverClass.trim();
				 dataSource.setDriverClassName(DriverClass);
				 
				 
				 if(DriverClass.indexOf("mysql")>0){
					 Parameter.DatabaseType.put(Name, "mysql");
				 }
				 if(DriverClass.indexOf("Oracle")>0){
					 Parameter.DatabaseType.put(Name, "oracle");
				 }
				 if(DriverClass.indexOf("sqlserver")>0){
					 Parameter.DatabaseType.put(Name, "sqlserver"); 
				 }
				 
				 
				 
				}else{
					try {
						new LoungerException().LoungerException(configpath+ " 中定义<Database>必须定义<DriverClass>");
					} catch (LoungerException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}	
					
				}
			 String Url = UtilCore.evalue(db, "Url");
			 if (Url != null && !Url.trim().equals("")) {
				 dataSource.setUrl(Url.trim());
				}else{
					try {
						new LoungerException().LoungerException(configpath+ " 中定义<Database>必须定义<Url>");
					} catch (LoungerException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}	
					
				}
			 String User = UtilCore.evalue(db, "User");
			 if (User != null && !User.trim().equals("")) {
				 dataSource.setUsername(User.trim());
				}else{
					try {
						new LoungerException().LoungerException(configpath+ " 中定义<Database>必须定义<User>");
					} catch (LoungerException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}	
					
				}
			 String Password = UtilCore.evalue(db, "Password");
			 if (Password != null && !Password.trim().equals("")) {
				 dataSource.setPassword(Password.trim());
				}else{
					try {
						new LoungerException().LoungerException(configpath+ " 中定义<Database>必须定义<Password>");
					} catch (LoungerException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}	
					
				}
			String defaultAutoCommit = UtilCore.evalue(db, "defaultAutoCommit");
			 if (defaultAutoCommit != null && !defaultAutoCommit.trim().equals("")) {
				 dataSource.setDefaultAutoCommit(Boolean.parseBoolean(defaultAutoCommit.trim()));
				}
			 
			 String defaultReadOnly = UtilCore.evalue(db, "defaultReadOnly");
			 if (defaultReadOnly != null && !defaultReadOnly.trim().equals("")) {
				 dataSource.setDefaultReadOnly(Boolean.parseBoolean(defaultReadOnly.trim()));
				}
			 String defaultTransactionIsolation = UtilCore.evalue(db, "defaultTransactionIsolation");
			 if (defaultTransactionIsolation != null && !defaultTransactionIsolation.trim().equals("")) {
				 dataSource.setDefaultTransactionIsolation(Integer.parseInt(defaultTransactionIsolation.trim()));
				}
			 
			 String defaultCatalog = UtilCore.evalue(db, "defaultCatalog");
			 if (defaultCatalog != null && !defaultCatalog.trim().equals("")) {
				 dataSource.setDefaultCatalog(defaultCatalog.trim());
				}
			 
			 String initialSize = UtilCore.evalue(db, "initialSize");
			 if (initialSize != null && !initialSize.trim().equals("")) {
				 dataSource.setInitialSize(Integer.parseInt(initialSize.trim()));
				}
			 String maxActive = UtilCore.evalue(db, "maxActive");
			 if (maxActive != null && !maxActive.trim().equals("")) {
				 dataSource.setMaxActive(Integer.parseInt(maxActive.trim()));
				}
			 String maxIdle = UtilCore.evalue(db, "maxIdle");
			 if (maxIdle != null && !maxIdle.trim().equals("")) {
				 dataSource.setMaxIdle(Integer.parseInt(maxIdle.trim()));
				}
			 String minIdle = UtilCore.evalue(db, "minIdle");
			 if (minIdle != null && !minIdle.trim().equals("")) {
				 dataSource.setMinIdle(Integer.parseInt(minIdle.trim()));
				}
			 String maxWait = UtilCore.evalue(db, "maxWait");
			 if (maxWait != null && !maxWait.trim().equals("")) {
				 dataSource.setMaxWait(Integer.parseInt(maxWait.trim()));
				}
			 String validationQuery = UtilCore.evalue(db, "validationQuery");
			 if (validationQuery != null && !validationQuery.trim().equals("")) {
				 dataSource.setValidationQuery(validationQuery.trim());
				}
			 String testOnBorrow = UtilCore.evalue(db, "testOnBorrow");
			 if (testOnBorrow != null && !testOnBorrow.trim().equals("")) {
				 dataSource.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow.trim()));
				}
			 String testOnReturn = UtilCore.evalue(db, "testOnReturn");
			 if (testOnReturn != null && !testOnReturn.trim().equals("")) {
				 dataSource.setTestOnReturn(Boolean.parseBoolean(testOnReturn.trim()));
				}
			 String testWhileIdle = UtilCore.evalue(db, "testWhileIdle");
			 if (testWhileIdle != null && !testWhileIdle.trim().equals("")) {
				 dataSource.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle.trim()));
				}
			 String timeBetweenEvictionRunsMillis = UtilCore.evalue(db, "timeBetweenEvictionRunsMillis");
			 if (timeBetweenEvictionRunsMillis != null && !timeBetweenEvictionRunsMillis.trim().equals("")) {
				 dataSource.setTimeBetweenEvictionRunsMillis(Integer.parseInt(timeBetweenEvictionRunsMillis.trim()));
				}
			 String numTestsPerEvictionRun = UtilCore.evalue(db, "numTestsPerEvictionRun");
			 if (numTestsPerEvictionRun != null && !numTestsPerEvictionRun.trim().equals("")) {
				 dataSource.setNumTestsPerEvictionRun(Integer.parseInt(numTestsPerEvictionRun.trim()));
				}
			 String minEvictableIdleTimeMillis = UtilCore.evalue(db, "minEvictableIdleTimeMillis");
			 if (minEvictableIdleTimeMillis != null && !minEvictableIdleTimeMillis.trim().equals("")) {
				 dataSource.setMinEvictableIdleTimeMillis(Integer.parseInt(minEvictableIdleTimeMillis.trim()));
				}
			 String poolPreparedStatements = UtilCore.evalue(db, "poolPreparedStatements");
			 if (poolPreparedStatements != null && !poolPreparedStatements.trim().equals("")) {
				 dataSource.setPoolPreparedStatements(Boolean.parseBoolean(poolPreparedStatements.trim()));
				}
			 String maxOpenPreparedStatements = UtilCore.evalue(db, "maxOpenPreparedStatements");
			 if (maxOpenPreparedStatements != null && !maxOpenPreparedStatements.trim().equals("")) {
				 dataSource.setMaxOpenPreparedStatements(Integer.parseInt(maxOpenPreparedStatements.trim()));
				}
			 String accessToUnderlyingConnectionAllowed = UtilCore.evalue(db, "accessToUnderlyingConnectionAllowed");
			 if (accessToUnderlyingConnectionAllowed != null && !accessToUnderlyingConnectionAllowed.trim().equals("")) {
				 dataSource.setAccessToUnderlyingConnectionAllowed(Boolean.parseBoolean(accessToUnderlyingConnectionAllowed.trim()));
				}
			 String removeAbandoned = UtilCore.evalue(db, "removeAbandoned");
			 if (removeAbandoned != null && !removeAbandoned.trim().equals("")) {
				 dataSource.setRemoveAbandoned(Boolean.parseBoolean(removeAbandoned.trim()));
				}
			 String removeAbandonedTimeout = UtilCore.evalue(db, "removeAbandonedTimeout");
			 if (removeAbandonedTimeout != null && !removeAbandonedTimeout.trim().equals("")) {
				 dataSource.setRemoveAbandonedTimeout(Integer.parseInt(removeAbandonedTimeout.trim()));
				}
			 String logAbandoned = UtilCore.evalue(db, "logAbandoned");
			 if (logAbandoned != null && !logAbandoned.trim().equals("")) {
				 dataSource.setLogAbandoned(Boolean.parseBoolean(logAbandoned.trim()));
				}
			 try {
				
				 TestDB(dataSource,configpath,UtilCore.evalue(db, "Name"));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
				Parameter.Database.put(UtilCore.evalue(db, "Name"),
						dataSource);
			
		}
		
	}

	/**
	 * <pre>DisposeInterceptor(处理拦截器的配置)   
	 * 创建时间：2016-12-8 下午9:24:50    
	 * @param configpath 
	 * @param interceptors</pre>
	 * @throws Exception 
	 */
	private void DisposeInterceptor(List<Element> interceptors, String configpath) throws Exception {
		for (Element element : interceptors) {
			String	name=UtilCore.evalue(element, "Name");
			String	classname=UtilCore.evalue(element, "Class");
			if(name!=null&&!name.trim().equals("")&&classname!=null&&!classname.trim().equals("")){
				if(Pattern.matches("[a-zA-z]{2,}", name)){
					Class<?> clas=Class.forName(classname);
					if(clas!=null){
						if(clas.getSuperclass().equals(LoungerInterceptors.class)){
							Parameter.Interceptors.add(classname);
							
						}else{
							try {
								new LoungerException().LoungerException(classname+ " 该类未继承LoungerInterceptors类 ");
							} catch (LoungerException e) {
								log.error(e.getMessage(), e);
								System.exit(0);
							}
						} 
						
					}else{
						try {
							new LoungerException().LoungerException(classname+ " 该类不存在 ");
						} catch (LoungerException e) {
							log.error(e.getMessage(), e);
							System.exit(0);
						}
					}
				}else{
					try {
						new LoungerException().LoungerException(configpath
								+ " 中定义<Interceptor> 定义<Name>值必须符合 [a-zA-z]{2,}");
					} catch (LoungerException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}
				}	
			}else{
				try {
					new LoungerException().LoungerException(configpath
							+ " 中定义<Interceptor> 必须定义<Name>和<Class> 且不可为空 ");
				} catch (LoungerException e) {
					log.error(e.getMessage(), e);
					System.exit(0);
				}
			}
		}
		
	}

	/**
	 * <pre>
	 * DisposeWebPath(处理xm文件中webpath)   
	 * 创建时间：2016-12-7 下午5:02:17    
	 * @param path
	 * @param configpath
	 * </pre>
	 */
	private  void DisposeWebPath(String path, String configpath) {
		boolean bla = Pattern.matches("[.]{1}[a-zA-z]{2,}", path);
		if (!bla) {
			try {
				new LoungerException().LoungerException(configpath
						+ " 中<WebPath>错误（" + path
						+ "），请遵循正则表达式\"[.]{1}[a-zA-z]{2,}\"");
			} catch (LoungerException e) {
				log.error(e.getMessage(), e);
				System.exit(0);
			}
		} else {
			Parameter.WebPostfix.put(path, path);
		}
	}

	/**
	 * <pre>
	 * TestDB(测试数据库连接是否正常)   
	 * 创建人：张泽寅
	 * 创建时间：2016-12-7 下午5:03:39    
	 * 修改人：张泽寅  
	 * 修改时间：2016-12-7 下午5:03:39    
	 * 修改备注： 
	 * @param configpath 
	 * @param dbparser
	 * @param configpath
	 * @param string 
	 * @throws Exception 
	 * </pre>
	 */
	private void TestDB(BasicDataSource dataSource, String configpath, String dbparsername) throws Exception {
		Connection con = null;
		Statement stmt = null;
		List<String> tabs=new ArrayList<String>();
		try {
			con = dataSource.getConnection();
			String findtabsql="";
			String databaseType=Parameter.DatabaseType.get(dbparsername);
			if(databaseType.equals("mysql")){
				findtabsql="show tables";
			}
			if(databaseType.equals("oracle")){
				findtabsql="SELECT TABLE_NAME FROM USER_TABLES";
				
			}
			if(databaseType.equals("sqlserver")){
				findtabsql="select name from sys.tables";
				
			}
			if(!findtabsql.equals("")){
			 stmt = con.createStatement();
			ResultSet rs=stmt.executeQuery(findtabsql);
				while (rs.next()) {
					tabs.add(rs.getString(1));
				}
				;
				Parameter.DataTableMap.put(dbparsername,tabs);
			}else{
				
				new LoungerException().LoungerException("在 ["
					+ configpath.substring(configpath.lastIndexOf("\\") + 1,
							configpath.length()) + "] 中名为"
					+ dbparsername + "的Database暂不支持");
				
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			System.exit(1);
		} finally {
			if (con != null)
				con.close();
			if(stmt!=null)
				stmt.close();
		}
		
	}
}
