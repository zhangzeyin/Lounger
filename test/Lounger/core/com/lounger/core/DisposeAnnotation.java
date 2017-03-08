package com.lounger.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.lounger.annotation.Column;
import com.lounger.annotation.Database;
import com.lounger.annotation.Id;
import com.lounger.annotation.Immit;
import com.lounger.annotation.Persistence;
import com.lounger.annotation.Service;
import com.lounger.annotation.Table;
import com.lounger.annotation.Timer;
import com.lounger.annotation.WebPath;
import com.lounger.db.IdEmum;
import com.lounger.util.LoungerJob;
import com.lounger.util.MethodParamName;
import com.lounger.web.LoungerException;
import com.lounger.web.MethodType;

/**
 * <pre>
 * 项目名称：Lounger  
 * 类名称：DisposeAnnotation 
 * 类描述：注解处理类 
 * @version
 * </pre>
 */
public class DisposeAnnotation {
	private static Logger log = Logger.getLogger(DisposeAnnotation.class);

	/**
	 * <pre>
	 * load(加载处理)   
	 * 创建时间：2016-12-7 下午4:30:16    
	 * @throws Exception
	 * </pre>
	 */
	public static void load() throws Exception {
		System.out
				.println("===============================LOAD SCAN CLASS BEGIN===============================");

		// 循环所有在配置包的所有class
		for (Class<?> class1 : Parameter.ClassAll) {
			log.info("SCAN CLASS:" + class1.getName());
		
			Persistence PersistenceAnnotation = class1
					.getAnnotation(Persistence.class);
			// 判断该类是否有Persistence注解
			if (PersistenceAnnotation != null) {
				MethodLIstener proxy = new MethodLIstener();
				Parameter.AnnotationClass.put(class1.getName(),
						proxy.getProxy(class1));
				DisposePersistence(class1, PersistenceAnnotation.value());
			}

			Service serviceAnnotation = class1.getAnnotation(Service.class);
			// 判断该类是否有service注解
			if (serviceAnnotation != null) {
				MethodLIstener proxy = new MethodLIstener();
				Parameter.AnnotationClass.put(class1.getName(),
						proxy.getProxy(class1));
				DisposeService(class1, serviceAnnotation.value());
			}
			WebPath webPath = class1.getAnnotation(WebPath.class);
			// 判断该类是否有WebPath注解
			if (webPath != null) {
				Parameter.AnnotationClass.put(class1.getName(),
						class1.newInstance());
				DisposeWebPath(class1, webPath);

			}
			// 判断该类是否有Table注解
			Table table = class1.getAnnotation(Table.class);
			if (table != null) {

				DisposeTable(class1, table);

			}
			Timer Timer = class1.getAnnotation(Timer.class);
			if (Timer != null) {

				DisposeTimer(class1, Timer);

			}
			if (Timer == null) {
				for (Field field : class1.getDeclaredFields()) {
					Immit immitAnnotation = field.getAnnotation(Immit.class);
					if (immitAnnotation != null) {
						DisposeImmit(class1, field, immitAnnotation);

					}
				}
			}
		}
	}

	/**
	 * <pre>
	 * DisposeTimer(处理定时器注解)   
	 * @param class1 类
	 * @param timer Timer注解
	 * </pre>
	 */
	private static void DisposeTimer(Class<?> class1, Timer timer) {
		String timerdate = timer.value();
		if (!timerdate.equals("")) {
			boolean isjob = false;
			Class<?> supclass = class1.getSuperclass();
			if (supclass.equals(LoungerJob.class)) {
				Parameter.JobMap.put(class1, timerdate);
				isjob = true;
			}

			if (!isjob) {
				try {
					new LoungerException().LoungerException(class1
							+ "中没有继承 (oorg.quartz.Job) 类");
				} catch (LoungerException e) {
					log.error(e.getMessage(), e);
					System.exit(0);
				}
			}

		} else {
			try {
				new LoungerException().LoungerException(class1 + "中注解内容不能为“”");
			} catch (LoungerException e) {
				log.error(e.getMessage(), e);
				System.exit(0);
			}
		}

	}

	/**
	 * <pre>
	 * DisposePersistence(处理Persistence注解)   
	 * 创建时间：2017-2-10 上午11:09:57    
	 * 修改时间：2017-2-10 上午11:09:57    
	 * @param class1
	 * @param value
	 * </pre>
	 */
	private static void DisposePersistence(Class<?> class1, String value) {

		if (value.trim().equals("")) {
			value = null;
		}

		Class<?> cls = class1.getSuperclass();
		if (cls != null) {
			if (cls.getName().equals("com.lounger.db.StatePersistence")) {
				Parameter.ImmitMap.put(class1.getName(), value);
			} else {
				try {
					new LoungerException().LoungerException(class1
							+ "中没有继承StatePersistence类");
				} catch (LoungerException e) {
					log.error(e.getMessage(), e);
					System.exit(0);
				}
			}

		} else {
			try {
				new LoungerException().LoungerException(class1
						+ "中没有继承StatePersistence类");
			} catch (LoungerException e) {
				log.error(e.getMessage(), e);
				System.exit(0);
			}

		}

		Database database = class1.getAnnotation(Database.class);
		if (database != null) {

			if (database.value().indexOf(",") > -1) {
				try {
					new LoungerException().LoungerException(class1
							+ "中只能设置一个数据源");
				} catch (LoungerException e) {
					log.error(e.getMessage(), e);
					System.exit(0);
				}

			} else if (Parameter.Database.get(database.value()) == null) {
				try {
					new LoungerException().LoungerException(class1 + "中不存在名为'"
							+ database.value() + "'的数据源");
				} catch (LoungerException e) {
					log.error(e.getMessage(), e);
					System.exit(0);
				}
			} else {
				Method[] mes = class1.getMethods();
				for (Method method : mes) {
					database = method.getAnnotation(Database.class);
					if (database != null) {
						if (database.value().indexOf(",") > -1) {
							try {
								new LoungerException().LoungerException(method
										.toString() + " 中只能设置一个数据源");
							} catch (LoungerException e) {
								log.error(e.getMessage(), e);
								System.exit(0);
							}
						} else if (Parameter.Database.get(database.value()) == null) {
							try {
								new LoungerException().LoungerException(class1
										+ "中不存在名为'" + database.value()
										+ "'的数据源");
							} catch (LoungerException e) {
								log.error(e.getMessage(), e);
								System.exit(0);
							}
						}
					}
				}
			}
		} else {
			if (Parameter.Database.keySet().size() != 1) {
				try {
					new LoungerException().LoungerException(class1
							+ "中没有设置默认数据源");
				} catch (LoungerException e) {
					log.error(e.getMessage(), e);
					System.exit(0);
				}
			}
		}

	}

	/**
	 * <pre>
	 * DisposeTable(处理Table注解)   
	 * 创建时间：2016-12-20 下午9:38:26    
	 * @param class1
	 * @param table
	 * </pre>
	 */
	private static void DisposeTable(Class<?> class1, Table table) {
		String tableName = class1.getName().substring(
				class1.getName().lastIndexOf(".") + 1,
				class1.getName().length());
		if (!table.value().equals("")) {

			tableName = table.value();
		}
		Map<String, com.lounger.db.Column> columns = new HashMap<String, com.lounger.db.Column>();
		com.lounger.db.Id ide = new com.lounger.db.Id();
		int dbsize = Parameter.Database.keySet().size();
		String database = null;

		if (dbsize > 1) {
			Database dbbase = class1.getAnnotation(Database.class);
			if (dbbase != null) {
				database = dbbase.value();
			} else {
				try {
					new LoungerException().LoungerException(class1
							+ "中没有标明使用@Database注解（数据源配置多个，需要指定数据源进行验证）");
				} catch (LoungerException e) {
					log.error(e.getMessage(), e);
					System.exit(0);
				}
			}

		}

		Field[] fieids = class1.getDeclaredFields();
		for (Field field : fieids) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				if (column.value()) {
					if (column.name().equals("")) {
						com.lounger.db.Column col = new com.lounger.db.Column(
								field.getName(), field.getName(), field
										.getType().getName());
						columns.put(col.getColumnNmae(), col);
					} else {
						com.lounger.db.Column col = new com.lounger.db.Column(
								column.name(), field.getName(), field.getType()
										.getName());
						columns.put(col.getColumnNmae(), col);
					}
				}
			} else {
				com.lounger.db.Column col = new com.lounger.db.Column(
						field.getName(), field.getName(), field.getType()
								.getName());
				columns.put(col.getColumnNmae(), col);
			}
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				if (column != null) {
					if (column.value()) {
						if (column.name().equals("")) {
							ide.setIdName(field.getName());
						} else {
							ide.setIdName(column.name());
						}
					}
				} else {
					ide.setIdName(field.getName());
				}
				ide.setIdClass(field.getType().getName());

				IdEmum idemum = id.value();

				boolean idis = false;
				String idtypename = field.getType().getName();
				if (idemum.ordinal() == 0) {
					if (idtypename.equals("java.lang.String")) {
						idis = true;
					}
				} else if (idemum.ordinal() == 1) {
					if (idtypename.equals("long")
							|| idtypename.equals("java.lang.Long")
							|| idtypename.equals("int")
							|| idtypename.equals("java.lang.Integer")) {
						idis = true;
					}
				} else if (idemum.ordinal() == 2) {
					if (idtypename.equals("long")
							|| idtypename.equals("java.lang.Long")) {
						idis = true;
					}
				} else if (idemum.ordinal() == 3) {
					if (idtypename.equals("long")
							|| idtypename.equals("java.lang.Long")
							|| idtypename.equals("int")
							|| idtypename.equals("java.lang.Integer")) {
						idis = true;
					}
					ide.setSequence(id.sequence());
				}

				if (!idis) {
					try {
						new LoungerException().LoungerException(class1
								+ ":@id()注解枚举与属性类型不符");
					} catch (LoungerException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}
				} else {
					ide.setTactics(idemum);

				}

			}
		}
		com.lounger.db.Table tables = new com.lounger.db.Table(tableName,
				class1.getName(), columns, ide, database);
		Parameter.BanTableMap.put(tables.getTableName(), tables);

	}

	/**
	 * <pre>
	 * DisposeWebPath(处理webpath的访问路径)   
	 * 创建时间：2016-12-7 下午4:30:59    
	 * @param class1 要处理的类
	 * @param webPath webpath对象
	 * @throws Exception
	 * </pre>
	 */
	private static void DisposeWebPath(Class<?> class1, WebPath webPath)
			throws Exception {

		if (Pattern.matches("[/]{0,1}[[a-zA-z0-9_-]{1,}[/]{0,1}]{1,}",
				webPath.value())) {
			for (Method method : class1.getMethods()) {
				WebPath webPathmrthod = method.getAnnotation(WebPath.class);
				if (webPathmrthod != null) {
					String value = webPathmrthod.value();
					for (String WebPost : Parameter.WebPostfix.keySet()) {
						if (value.endsWith(WebPost)) {
							value = value.substring(0,
									value.length() - WebPost.length());
							break;
						}
					}

					if (Pattern.matches(
							"[/]{0,1}[[a-zA-z0-9_-]{1,}[/]{0,1}]{1,}", value)) {
						String path = ("/" + webPath.value() + "/" + value)
								.replace("//", "/").replace("//", "/");
						// 存入该方法访问路径
						Parameter.PathMap.put(path, method);
						// 存入该方法的参数名称集合
						Parameter.MethodParameterName.put(path, MethodParamName
								.getMethodParamNames(class1, method));
						com.lounger.annotation.Method methodtype = method
								.getAnnotation(com.lounger.annotation.Method.class);
						if (methodtype == null) {
							Parameter.pathMethodMap.put(path, "");
						} else if (methodtype.value() == MethodType.GET) {
							Parameter.pathMethodMap.put(path, "GET");
						} else if (methodtype.value() == MethodType.POST) {
							Parameter.pathMethodMap.put(path, "POST");
						}

					} else {
						try {
							new LoungerException().LoungerException(class1
									+ ":@WebPath(" + webPathmrthod.value()
									+ ")错误");
						} catch (LoungerException e) {
							log.error(e.getMessage(), e);
							System.exit(0);
						}
					}
				}

			}

		} else {
			try {
				new LoungerException().LoungerException(class1 + ":@WebPath("
						+ webPath.value() + ")错误");
			} catch (LoungerException e) {
				log.error(e.getMessage(), e);
				System.exit(0);
			}
		}

	}

	/**
	 * <pre>
	 * DisposeService()   
	 * @param cla 要处理的类
	 * @param value Service注解的内容
	 * 
	 * <pre>
	 */
	public static void DisposeService(Class<?> cla, String value)
			throws Exception {
		if (value.trim().equals("")) {
			value = null;
		}

		Parameter.ImmitMap.put(cla.getName(), value);

	}

	/**
	 * <pre>
	 * DisposeImmit(service的注入)   
	 * @param class1 处理类
	 * @param field 变量
	 * @param immitAnnotation Immit注解对象
	 * @throws Exception
	 * </pre>
	 */
	public static void DisposeImmit(Class<?> class1, Field field,
			Immit immitAnnotation) throws Exception {
		if (field.getType().isInterface()) {
			field.getType().getGenericInterfaces();
			List<String> infslist = new ArrayList<>();
			for (String service : Parameter.ImmitMap.keySet()) {
				for (Class<?> infsclass : Class.forName(service)
						.getInterfaces()) {
					if (infsclass.getName().equals(field.getType().getName()))
						infslist.add(service);
				}
			}
			if (infslist.size() > 1) {
				int i = 0;
				for (String string : infslist) {
					if (immitAnnotation.value().equals(
							Parameter.ImmitMap.get(string))) {
						i += 1;
						field.setAccessible(true);
						field.set(
								Parameter.AnnotationClass.get(class1.getName()),
								Parameter.AnnotationClass.get(string));
					}
				}
				if (i == 0) {
					try {
						new LoungerException().LoungerException(class1 + ":"
								+ field.getName() + " (未找到" + field.getType()
								+ "的实现类) 该类的实现类较多希望给注解赋值");
					} catch (LoungerException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}

				}
				if (i > 1) {
					try {
						new LoungerException().LoungerException(class1 + ":"
								+ field.getName() + " (未找到" + field.getType()
								+ "的实现类) 存在注解值相同的类");
					} catch (LoungerException e) {
						log.error(e.getMessage(), e);
						System.exit(0);
					}

				}
			} else if (infslist.size() == 1) {
				field.setAccessible(true);
				field.set(Parameter.AnnotationClass.get(class1.getName()),
						Parameter.AnnotationClass.get(infslist.get(0)));
			} else {
				try {
					new LoungerException().LoungerException(class1 + ":"
							+ field.getName() + " (未找到" + field.getType()
							+ "的实现类)");
				} catch (LoungerException e) {
					log.error(e.getMessage(), e);
					System.exit(0);
				}
			}
			field.getType().getInterfaces();
		} else {
			field.setAccessible(true);
			field.set(Parameter.AnnotationClass.get(class1.getName()), field
					.getType().newInstance());
		}

	}
}