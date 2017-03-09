package com.lounger.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.lounger.db.Column;
import com.lounger.db.LoungerDBException;
import com.lounger.db.Table;
import com.lounger.util.UtilCore;
/**
 * <pre>
 * 项目名称：Lounger     
 * 类名称：Verification    
 * 类描述：表的验证  
 * @version </pre>
 */
public class Verification {
	private static Logger log = Logger.getLogger(Verification.class);
	/**
	 * <pre>VerificationTable(验证表)   
	 * @throws Exception</pre>
	 */
	public static void	VerificationTable() throws Exception{
		Collection<Table> tableNames=Parameter.BanTableMap.values();
		
				System.out.println("===============================LOAD VERIFICATION TABLE BEGIN===============================");
		for (Table table : tableNames) {
			String	Dbname=	table.getDatabase();
			String	Tbname=	table.getTableName();
			log.info("VERIFICATION TABLE(class:"+table.getClassName()+"):"+Tbname);
			String[]  Dbnames = null;
			if(Dbname!=null){
			 Dbnames = Dbname.split(",");
			}
			Object[] dbs= Parameter.DataTableMap.keySet().toArray();
			int dbsize = Parameter.Database.keySet().size();

			if (dbsize > 1) {
				if(!UtilCore.isDatabase(Dbnames)){
					try {
						new LoungerDBException()
								.LoungerDBException("名为"+Tbname+"的表数据库连接设置异常");
					} catch (LoungerDBException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}
				}
				
				for (Object db : dbs) {
					for (String dbn : Dbnames) {
						if(dbn.equals(db)){
							VerificationTab(Tbname, db.toString());
						}
					}
					if(Dbname==null){
						VerificationTab(Tbname, db.toString());
					}
				}
			}else if(dbsize==1){
				VerificationTab(Tbname, dbs[0].toString());
			}
			

			
		}
		
	}
	/**
	 * <pre>VerificationTab(验证表是否存在)   
	 * @param Tbname 表名称
	 * @param db 数据源名称
	 * </pre>
	 */
	public static void VerificationTab(String Tbname,String db) {
		
		boolean istab=false;
		List<String> tabs=Parameter.DataTableMap.get(db);
		for (String tab : tabs) {
			if(tab.toUpperCase().equals(Tbname.toUpperCase())){
				istab=true;	
				VerificationColumn(Tbname,db);
			}
		}
		if(!istab){

			try {
				new LoungerDBException()
						.LoungerDBException("数据库连接"+db+"中不存在名为"+Tbname+"的表");
			} catch (LoungerDBException e) {
				log.error(e.getMessage(), e);
				System.exit(0);
			}
		}
	}
	/**
	 * <pre>VerificationColumn(验证列)   
	 * @param tbname 表名称
	 * @param db 数据源
	 * </pre>
	 */
	private static void VerificationColumn(String tbname, String db)  {
		Table table=Parameter.BanTableMap.get(tbname);
		BasicDataSource dataSource=Parameter.Database.get(db);
		String findColumnSql="";
		String databaseType=Parameter.DatabaseType.get(db);
		if(databaseType.equals("sqlserver")){
			findColumnSql=" SELECT  a.name,a.colorder,b.name   FROM syscolumns a left join  systypes b on a.xusertype=b.xusertype inner join sysobjects d on  a.id=d.id  and d.xtype='U' and  d.name<>'dtproperties' where d.name='"+tbname+"' order by a.id,a.colorder";
		}
		if(databaseType.equals("oracle")){
			String majuscule=tbname.toUpperCase();
			findColumnSql="select COLUMN_NAME,COLUMN_ID,DATA_TYPE from user_tab_columns where Table_Name='"+majuscule+"' order by column_name";
		}
		if(databaseType.equals("mysql")){
			findColumnSql="select COLUMN_NAME,ORDINAL_POSITION,DATA_TYPE from information_schema.COLUMNS where table_name = '"+tbname+"' and table_schema = (select database())";
			
		}
		
		Connection con = null;
		Statement stmt = null;
		try {
			con = dataSource.getConnection();
			stmt = con.createStatement();
			ResultSet rs=stmt.executeQuery(findColumnSql);
			Map<String,Map<String,String>> bdcolumns =new HashMap<String,Map<String,String>>();
			while (rs.next()) {
				Map<String,String> bdcolumn=new HashMap<String, String>();
				bdcolumn.put("name", rs.getString(1));
				bdcolumn.put("serial", rs.getString(2));
				bdcolumn.put("type", rs.getString(3));
				bdcolumns.put(rs.getString(1), bdcolumn);
			}
		Map<String, Column> clums=	table.getColumns();
		
		for (String clumn : clums.keySet()) {
			Map<String,String> 	bdcolumn=bdcolumns.get(clumn);
			if(bdcolumn!=null){
				Column column=clums.get(clumn);
				column.setSerial(Integer.parseInt(bdcolumn.get("serial")));
				clums.put(clumn, column);
				table.setColumns(clums);
				VerificationColumnType(column.getClassName(),bdcolumn.get("type"),databaseType,db,tbname,clumn);
				
			}else{
				try {
					new LoungerDBException()
							.LoungerDBException("数据库连接"+db+"中名为"+tbname+"的表不存在"+clumn+"字段");
				} catch (LoungerDBException e) {
					log.error(e.getMessage(), e);
					System.exit(0);
				}		}
			
		
		}
		
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			System.exit(0);
		}finally{
				try {
					if (con != null)
					con.close();
					if(stmt!=null)
						stmt.close();
				} catch (SQLException e) {
					log.error(e.getMessage(), e);
				}
		}

	}
	/**
	 * <pre>VerificationColumnType(验证列的数据类型)   
	 * @param classname
	 * @param dbtype
	 * @param databaseType
	 * @param db
	 * @param tbname
	 * @param clumn</pre>
	 */
	private static void VerificationColumnType(String classname, String dbtype, String databaseType, String db, String tbname, String clumn){
		dbtype = dbtype.toUpperCase();
		boolean istype=false;
		if(databaseType.equals("mysql")){
			if(dbtype.equals("SMALLINT")||dbtype.equals("MEDIUMINT")||dbtype.equals("INT")||dbtype.equals("INTEGER")){
				if(classname.equals("int")||classname.equals("java.lang.Integer")||
						classname.equals("short")||classname.equals("java.lang.Short")||
						classname.equals("long")||classname.equals("Ljava.lang.Long")){
					istype=true;
				}
			}
			if(dbtype.equals("BIGINT")){
				if(classname.equals("long")||classname.equals("Ljava.lang.Long")){
					istype=true;
				}
			}
			if(dbtype.equals("TINYINT")){		
				if(classname.equals("byte")||classname.equals("java.lang.Byte")){
					istype=true;
				}
			}
			if(dbtype.equals("FLOAT")||dbtype.equals("REAL")){
				if(classname.equals("float")||classname.equals("java.lang.Float")){
					istype=true;
				}
			}
			if(dbtype.equals("DOUBLE")||dbtype.equals("FLOAT")){
				if(classname.equals("double")||classname.equals("java.lang.Double")){
					istype=true;
				}
			}
			if(dbtype.equals("DECIMAL")){
				 if(classname.equals("java.math.BigDecimal")){
					 istype=true;		
					}
			}
			if(dbtype.equals("DATE")||dbtype.equals("YEAR")){
				 if(classname.equals("java.util.Date")||classname.equals("java.sql.Date")){
					 istype=true;						
					}
			}
			
			if(dbtype.equals("TIME")){
				if(classname.equals("java.sql.TIME")){
					istype=true;
				}
			}
			if(dbtype.equals("TIMESTAMP")||dbtype.equals("DATETIME")){
				if(classname.equals("java.sql.Timestamp")){
					istype=true;
				}
			}
			if (dbtype.equals("CHAR") || dbtype.equals("VARCHAR")
					|| dbtype.equals("TINYTEXT") || dbtype.equals("TEXT")
					|| dbtype.equals("MEDIUMTEXT")|| dbtype.equals("LONGTEXT")
					|| dbtype.equals("ENUM")|| dbtype.equals("SET")
					|| dbtype.equals("JSON")|| dbtype.equals("NVARCHAR")) {
				if(classname.equals("java.lang.String")){
					istype=true;
				}
			}
			if (dbtype.equals("TINYBLOB") || dbtype.equals("BLOB")
					|| dbtype.equals("MEDIUMBLOB") || dbtype.equals("LONGBLOB")
					|| dbtype.equals("BIT")|| dbtype.equals("BINARY")
					|| dbtype.equals("VARBINARY")) {
				 if(classname.equals("[B")||classname.equals("[Ljava.lang.Byte;")||classname.equals("java.sql.Blob")){
					 istype=true;
					}
			}
		}
		if(databaseType.equals("oracle")){
			//BINARY_DOUBLE  BINARY_FLOAT BLOB CLOB CHAR DATE (INTERVAL DAY(2) TO SECOND(6))  (INTERVAL YEAR(2) TO MONTH)  LONG (LONG RAW) NCLOB NUMBER NVARCHAR2 RAW TIMESTAMP
			//(TIMESTAMP(6) WITH LOCAL TIME ZONE) (TIMESTAMP(6) WITH TIME ZONE)  VARCHAR2(12)
			
			//NUMBER
			if(dbtype.equals("NUMBER")){
				
				if (classname.equals("int")
						|| classname.equals("float")
						|| classname.equals("double")
						|| classname.equals("short")
						|| classname.equals("long")
						|| classname.equals("java.lang.Float")
						|| classname.equals("java.lang.Long")
						|| classname.equals("java.lang.Short")
						|| classname.equals("java.lang.Double")
						|| classname.equals("java.lang.Integer")) {
					istype=true;
				}
				
				
			}
			
			//BINARY_DOUBLE BINARY_FLOAT
			if(dbtype.equals("BINARY_DOUBLE")){
				if (classname.equals("double")|| classname.equals("java.lang.Double")) {
					istype=true;
				}
			}
			
			if(dbtype.equals("BINARY_FLOAT")){
				if (classname.equals("float")|| classname.equals("java.lang.Float")) {
					istype=true;
				}
			}
			
			//BLOB RAW
			if(dbtype.equals("BLOB")||dbtype.equals("RAW")){
				if(classname.equals("[B")||classname.equals("[Ljava.lang.Byte;")||classname.equals("java.sql.Blob")){
					istype=true;
				}
			}
			
			
			
			//CHAR LONG  NVARCHAR2  VARCHAR2
			if(dbtype.equals("CLOB")||dbtype.equals("CHAR")||dbtype.equals("LONG")||dbtype.equals("NVARCHAR2")||dbtype.equals("VARCHAR2")){
				if(classname.equals("java.lang.String")){
					istype=true;
				}
			}
			//LONG RAW
			if(dbtype.equals("LONG RAW")){
				if(classname.equals("[B")||classname.equals("[Ljava.lang.Byte;")){
					istype=true;
				}
			}
			//DATE  
			if(dbtype.equals("DATE")){
				if(classname.equals("java.util.Date")||classname.equals("java.sql.Date")){
					istype=true;
				}
			}
			//NCLOB CLOB
			if(dbtype.equals("NCLOB")||dbtype.equals("CLOB")){
				if(classname.equals("java.sql.Clob")){
					istype=true;
				}
			}
			
			//INTERVAL DAY(2) TO SECOND(6)  INTERVAL YEAR(2) TO MONTH
			
			//TIMESTAMP  (TIMESTAMP(6) WITH LOCAL TIME ZONE) (TIMESTAMP(6) WITH TIME ZONE)
			if(dbtype.contains("TIMESTAMP")){
				if(classname.equals("java.sql.Timestamp")){
					istype=true;
				}
			}
		}
		if(databaseType.equals("sqlserver")){
			//image int money nchar ntext numeric nvarchar real smalldatetime smallint smallmoney sql_variant text time timestamp tinyint uniqueidentifier varbinary varchar xml
		
			
			//xml
			if(dbtype.equals("XML")){
				if(classname.equals("java.sql.SQLXML")||classname.equals("java.lang.String")){
					istype=true;
				}
			}
			
			//smallint tinyint
			if(dbtype.equals("SMALLINT")||dbtype.equals("TINYINT")){
				if(classname.equals("short")||classname.equals("java.lang.Short")){
					istype=true;
				}
			}
		
		//sql_variant
			
		//real	float
			if(dbtype.equals("REAL")||dbtype.equals("FLOAT")){
				if(classname.equals("float")||classname.equals("java.lang.Float")){
					istype=true;
				}
			}
			
		//nchar	ntext nvarchar char text uniqueidentifier varchar
			if(dbtype.equals("CHAR")||dbtype.equals("NCHAR")||dbtype.equals("NTEXT")||dbtype.equals("NVARCHAR")||dbtype.equals("TEXT")||dbtype.equals("UNIQUEIDENTIFIER")||dbtype.equals("VARCHAR")){
				if(classname.equals("float")||classname.equals("java.lang.Float")){
					istype=true;
				}
			}
			
			
		//int
			if(dbtype.equals("INT")){
				if(classname.equals("int")||classname.equals("java.lang.Integer")||
						classname.equals("long")||classname.equals("java.lang.Long")){
					istype=true;
				}
			}
			
		//image	varbinary binary geography geometry
			if(dbtype.equals("IMAGE")||dbtype.equals("VARBINARY")||dbtype.equals("BINARY")||dbtype.equals("GEOGRAPHY")||dbtype.equals("GEOMETRY")){
				if(classname.equals("[B")||classname.equals("[Ljava.lang.Byte;")){
					istype=true;
				}
			}
		//hierarchyid
			
			
		//bigint
			if(dbtype.equals("BIGINT")){
				if(classname.equals("long")||classname.equals("Ljava.lang.Long")){
					istype=true;
				}
			}
		
			
		//bit
			if(dbtype.equals("BIT")){
				if(classname.equals("boolean")||classname.equals("Ljava.lang.Boolean")){
					istype=true;
				}
			}
			
			
		//date
			if(dbtype.equals("DATE")){
				if(classname.equals("java.util.Date")||classname.equals("java.sql.Date")){
					istype=true;
				}
			}
		//datetime datetime2 smalldatetime timestamp
			if(dbtype.equals("TIMESTAMP")||dbtype.equals("DATETIME")||dbtype.equals("DATETIME2")||dbtype.equals("SMALLDATETIME")){
				if(classname.equals("java.sql.Timestamp")){
					istype=true;
				}
			}
		
		//datetimeoffset 
			if(dbtype.equals("DATETIMEOFFSET")){
				if(classname.equals("microsoft.sql.DateTimeOffset")){
					istype=true;
				}
			}
			
		//decimal money numeric smallmoney
			if(dbtype.equals("DECIMAL")||dbtype.equals("MONEY")||dbtype.equals("NUMERIC")||dbtype.equals("SMALLMONEY")){
				if(classname.equals("java.math.BigDecimal")){
					istype=true;
				}
			}
			
		
		}
		if(!istype){
			try {
				new LoungerDBException()
				.LoungerDBException("数据库连接"+db+"中名为"+tbname+"的表字段"+clumn+"中数据类型与实体类型不符");
			} catch (LoungerDBException e) {
				log.error(e.getMessage(), e);
				System.exit(0);
			}
		}
	}
	
	
}
