package com.lounger.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

import org.apache.log4j.Logger;

import com.lounger.annotation.Table;
import com.lounger.core.Parameter;
import com.lounger.util.BasicFormatterImpl;

/**
 * 
 * <pre>项目名称：Lounger      
 * 类名称：SqlQuery    
 * 类描述：sql处理类    
 * @version
 * </pre>
 */
public class SqlQuery {
	private static Logger log = Logger.getLogger(SqlQuery.class);
	// 数据连接
	private Connection con;
	// sql信息处理
	private PreparedStatement stma;
	// sql
	private String SQL;

	/**
	 * <pre>
	 * delete(根据obj对象中的id删除数据)   
	 * @param obj 数据对象
	 * @return 删除结果
	 * @throws LoungerDBException
	 * @throws SQLException
	 * </pre>
	 */
	public Integer delete(Object obj) throws LoungerDBException, SQLException {
		PreparedStatement st = null;
		Class<?> cla = obj.getClass();
		Table table = obj.getClass().getAnnotation(Table.class);
		if (table != null) {
			String tabname = table.value();
			if (table.value().equals("")) {
				tabname = cla.getName();
				tabname = tabname.substring(tabname.lastIndexOf(".") + 1,
						tabname.length());
			}
			com.lounger.db.Table tab = Parameter.BanTableMap.get(tabname);
			String idname = tab.getColumns().get(tab.getId().getIdName())
					.getFieldNmae();
			try {

				Field d = cla.getDeclaredField(idname);
				d.setAccessible(true);
				String sql = "DELETE FROM " + tabname + " WHERE "
						+ tab.getId().getIdName() + "=?";

				if (Parameter.sqlprint) {
					StackTraceElement Stack = Thread.currentThread()
							.getStackTrace()[7];
					System.out.println(Stack.getClassName() + "."
							+ Stack.getMethodName() + "("
							+ Stack.getLineNumber() + " 行) >> "
							+ new BasicFormatterImpl().format(sql));
				}
				st = con.prepareStatement(sql);
				st.setObject(1, d.get(obj));
				int i = st.executeUpdate();
				return i;
			} catch (Exception e) {
				con.rollback();
				log.error(e.getMessage(), e);
			} finally {
				if (st != null) {
					st.close();
				}
			}
		} else {
			new LoungerDBException().LoungerDBException(cla + " 类不存在@table 注解");
		}
		return null;

	}

	/**
	 * <pre>
	 * update(根据obj对象修改数据)   
	 * @param obj 修改对象
	 * @return 修改结果
	 * @throws LoungerDBException
	 * @throws SQLException
	 * </pre>
	 */
	public Integer update(Object obj) throws LoungerDBException, SQLException {
		PreparedStatement st = null;
		Class<?> cla = obj.getClass();
		Table table = obj.getClass().getAnnotation(Table.class);
		if (table != null) {
			String tabname = table.value();
			if (table.value().equals("")) {
				tabname = cla.getName();
				tabname = tabname.substring(tabname.lastIndexOf(".") + 1,
						tabname.length());
			}
			com.lounger.db.Table tab = Parameter.BanTableMap.get(tabname);
			String idname = tab.getColumns().get(tab.getId().getIdName())
					.getFieldNmae();
			try {
				List<String> clumna = new ArrayList<String>();
				String content = "";
				for (String str : tab.getColumns().keySet()) {
					content += str + "=?,";
					clumna.add(str);
				}
				content = content.substring(0, content.length() - 1);
				Field d = cla.getDeclaredField(idname);
				d.setAccessible(true);
				String sql = "UPDATE " + tabname + " SET " + content
						+ " WHERE " + tab.getId().getIdName() + "=?";
				if (Parameter.sqlprint) {
					StackTraceElement Stack = Thread.currentThread()
							.getStackTrace()[7];
					System.out.println(Stack.getClassName() + "."
							+ Stack.getMethodName() + "("
							+ Stack.getLineNumber() + " 行) >> "
							+ new BasicFormatterImpl().format(sql));
				}
				st = con.prepareStatement(sql);
				for (int i = 0; i < clumna.size(); i++) {
					Field dv = cla.getDeclaredField(tab.getColumns()
							.get(clumna.get(i)).getFieldNmae());
					dv.setAccessible(true);
					st.setObject(i + 1, dv.get(obj));
					if (i == clumna.size() - 1) {

						st.setObject(i + 2, d.get(obj));

					}
				}

				int i = st.executeUpdate();
				return i;
			} catch (Exception e) {
				con.rollback();
				log.error(e.getMessage(), e);
			} finally {
				if (st != null) {
					st.close();
				}
			}
		} else {
			new LoungerDBException().LoungerDBException(cla + " 类不存在@table 注解");
		}
		return null;

	}

	/**
	 * <pre>
	 * save(这里用一句话描述这个方法的作用)   
	 * @param obj 保存对象
	 * @return 保存后的对象非自增长id可以获得id
	 * @throws LoungerDBException
	 * @throws SQLException
	 * </pre>
	 */
	public Object save(Object obj) throws LoungerDBException, SQLException {
		PreparedStatement st = null;
		Class<?> cla = obj.getClass();
		Table table = obj.getClass().getAnnotation(Table.class);
		if (table != null) {
			String tabname = table.value();
			if (table.value().equals("")) {
				tabname = cla.getName();
				tabname = tabname.substring(tabname.lastIndexOf(".") + 1,
						tabname.length());
			}
			com.lounger.db.Table tab = Parameter.BanTableMap.get(tabname);

			String idname = tab.getId().getIdName();
			try {
				List<String> clumna = new ArrayList<String>();
				String content = "";
				String values = "";
				for (String str : tab.getColumns().keySet()) {
					if (str.equals(idname)) {
						String fildname = tab.getColumns().get(str)
								.getFieldNmae();
						Id id = tab.getId();
						Field dv = obj.getClass().getDeclaredField(fildname);
						dv.setAccessible(true);
						int Tactics = id.getTactics().ordinal();
						if (Tactics == 0) {
							content += str + ",";
							UUID uuid = UUID.randomUUID();
							values += "'" + uuid.toString() + "',";
							dv.set(obj, uuid.toString());

						} else if (Tactics == 1) {
							content += str + ",";
							PreparedStatement stm = null;
							try {
								stm = con.prepareStatement("SELECT MAX("
										+ idname + ")+1 FROM " + tabname);
								ResultSet rs = stm.executeQuery();
								if (rs != null) {
									while (rs.next()) {
										Object idval = rs.getObject(1);
										values += idval + ",";
										dv.set(obj, idval);
									}
								} else {
									values += "1,";
									dv.set(obj, 1);
								}
							} catch (Exception e) {
								log.error(e.getMessage(), e);
							} finally {
								if (stm != null) {
									stm.close();
								}
							}

						} else if (Tactics == 2) {
							content += str + ",";
							long time = new Date().getTime();
							values += time + ",";
							dv.set(obj, time);
						} else if (Tactics == 3) {
							long threadId = Thread.currentThread().getId();
							Connect connent = Parameter.ThreadMap.get(threadId);
							String databasetype = Parameter.DatabaseType
									.get(connent.getDatabase());
							if (databasetype.equals("mysql")) {
								content += str + ",";
								values += "null,";
							} else if (databasetype.equals("oracle")) {
								content += id.getSequence() + ".NEXTVAL ,";
							} else if (databasetype.equals("sqlserver")) {

							}
						}
					} else {
						content += str + ",";
						values += "?,";
						clumna.add(str);
					}
				}
				content = content.substring(0, content.length() - 1);
				values = values.substring(0, values.length() - 1);
				String sql = "INSERT INTO " + tabname + "(" + content
						+ ") VALUES(" + values + ")";
				if (Parameter.sqlprint) {
					StackTraceElement Stack = Thread.currentThread()
							.getStackTrace()[7];
					System.out.println(Stack.getClassName() + "."
							+ Stack.getMethodName() + "("
							+ Stack.getLineNumber() + " 行) >> "
							+ new BasicFormatterImpl().format(sql));
				}
				st = con.prepareStatement(sql);
				for (int i = 0; i < clumna.size(); i++) {
					Field dv = cla.getDeclaredField(tab.getColumns()
							.get(clumna.get(i)).getFieldNmae());
					dv.setAccessible(true);
					st.setObject(i + 1, dv.get(obj));
				}

			} catch (Exception e) {
				con.rollback();
				log.error(e.getMessage(), e);
			} finally {
				if (st != null) {
					st.close();
				}
			}
		} else {
			new LoungerDBException().LoungerDBException(cla + " 类不存在@table 注解");
		}

		return obj;
	}

	/**
	 * <pre>
	 * OperationSql(加载sql，以及数据赋值)   
	 * @param sql sql
	 * @param attr 占位对应值
	 * @throws SQLException
	 * </pre>
	 */
	public void OperationSql(String sql, Object[] attr) throws SQLException {
		try {
			sql = CCJSqlParserUtil.parse(sql).toString();
			if (Parameter.sqlprint) {
				StackTraceElement Stack = Thread.currentThread()
						.getStackTrace()[7];
				System.out.println(Stack.getClassName() + "."
						+ Stack.getMethodName() + "(" + Stack.getLineNumber()
						+ " 行) >> " + new BasicFormatterImpl().format(sql));
			}
			SQL = sql;
			stma = con.prepareStatement(sql);
			int i = 1;
			if (attr != null) {
				for (Object object : attr) {
					stma.setObject(i, object);
					i++;
				}
			}
		} catch (JSQLParserException e) {
			log.error(e.getMessage(), e);
		}

	}

	/**
	 * <pre>
	 * findlist(根据class类型返回数据集合)    
	 * @param cla class
	 * @return 返回的数据集合
	 * @throws SQLException
	 * </pre>
	 */
	public Object findlist(Class<?> cla) throws SQLException {

		try {
			Select select = (Select) CCJSqlParserUtil.parse(SQL);
			PlainSelect plainselect = (PlainSelect) select.getSelectBody();
			List<SelectItem> selectitem = plainselect.getSelectItems();
			ResultSet rs = stma.executeQuery();

			List<Object> listall = new ArrayList<Object>();
			while (rs.next()) {
				Object obj = cla.newInstance();
				for (SelectItem item : selectitem) {
					String key = item.toString();
					if (key.indexOf(" ") > -1) {
						key = key.substring(key.indexOf(" ") + 1, key.length());
					}
					Field field = cla.getDeclaredField(key);
					if (field != null) {
						field.setAccessible(true);
						field.set(obj, rs.getObject(key));
					}
				}
				listall.add(obj);
			}

			if (stma != null) {
				stma.close();
			}
			return listall;
		} catch (Exception e) {
			con.rollback();
			log.error(e.getMessage(), e);
		}

		return null;

	}

	/**
	 * <pre>
	 * findlist(根据SQL中的表名称进行映射赋值)     
	 * @return 返回的数据集合
	 * @throws LoungerDBException
	 * @throws SQLException
	 * </pre>
	 */
	public Object findlist() throws LoungerDBException, SQLException {
		try {
			Select select = (Select) CCJSqlParserUtil.parse(SQL);
			PlainSelect plainselect = (PlainSelect) select.getSelectBody();
			net.sf.jsqlparser.schema.Table table = (net.sf.jsqlparser.schema.Table) plainselect
					.getFromItem();
			com.lounger.db.Table tab = Parameter.BanTableMap.get(table
					.getName());
			if (tab != null) {
				List<SelectItem> selectitem = plainselect.getSelectItems();
				if (selectitem.get(0).toString().equals("*")) {
					List<Object> listall = new ArrayList<Object>();
					ResultSet rs = stma.executeQuery();

					while (rs.next()) {
						Class<?> cla = Class.forName(tab.getClassName());
						Object obj = cla.newInstance();
						for (String object : tab.getColumns().keySet()) {
							tab.getColumns().get(object).getFieldNmae();
							Field field = cla.getDeclaredField(tab.getColumns()
									.get(object).getFieldNmae());
							field.setAccessible(true);
							field.set(obj, rs.getObject(object));
						}
						listall.add(obj);
					}
					if (stma != null) {
						stma.cancel();
					}
					return listall;
				} else {
					List<Object> listall = new ArrayList<Object>();
					ResultSet rs = stma.executeQuery();
					Class<?> cla = Class.forName(tab.getClassName());
					while (rs.next()) {
						Object obj = cla.newInstance();

						for (SelectItem item : selectitem) {
							String key = item.toString();
							if (key.indexOf(" ") > -1) {
								key = key.substring(key.indexOf(" ") + 1,
										key.length());
							}
							Field field = cla.getDeclaredField(key);
							if (field != null) {
								field.setAccessible(true);
								field.set(obj, rs.getObject(key));
							}
						}
						listall.add(obj);
					}
					if (stma != null) {
						stma.cancel();
					}
					return listall;
				}

			} else {
				ResultSet i = stma.executeQuery();
				if (stma != null) {
					stma.cancel();
				}
				return i;
			}

		} catch (Exception e) {
			con.rollback();
			log.error(e.getMessage(), e);
		}

		return con;

	}

	/**
	 * <pre>
	 * executeUpdate(获取sql除查询意外的执行结果)   
	 * @return 返回结果
	 * @throws SQLException
	 * </pre>
	 */
	public int executeUpdate() throws SQLException {
		int i = 0;
		try {
			i = stma.executeUpdate();
		} catch (Exception e) {
			con.rollback();
			log.error(e.getMessage(), e);
		}

		if (stma != null) {
			stma.close();
		}
		return i;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public PreparedStatement getStma() {
		return stma;
	}

	public void setStma(PreparedStatement stma) {
		this.stma = stma;
	}

}
