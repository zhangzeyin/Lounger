package com.lounger.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.log4j.Logger;

import com.lounger.core.Parameter;
import com.lounger.web.LoungerWebException;

/**
 * <pre>
 * <pre>项目名称：Lounger  
 * 类名称：ValueUtil 
 * 类描述： 处理控制层的方法参数注入 
 * @version
 * </pre>
 */
public class ValueUtil {
	private static Logger log = Logger.getLogger(ValueUtil.class);
	static SimpleDateFormat sm1 = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	static SimpleDateFormat sm2 = new SimpleDateFormat("yyyy/MM/dd");
	static SimpleDateFormat sm3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	static SimpleDateFormat sm4 = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * <pre>
	 * GetValue(这里用一句话描述这个方法的作用)   
	 * 创建时间：2016-12-7 下午4:42:20    
	 * @param request
	 * @param response
	 * @param ParamName 参数名称
	 * @param classname 参数类型名称
	 * @param value 
	 * @param values
	 * @return
	 * @throws Exception
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	public static Object GetValue(HttpServletRequest request,
			HttpServletResponse response, String ParamName, String classname,
			String value, String[] values) throws Exception {
		if (value == null) {
			value = request.getParameter(ParamName);
		}
		if (values == null) {
			values = request.getParameterValues(ParamName);
		}
		if (classname.equals("javax.servlet.http.HttpServletRequest"))
			return request;
		if (classname.equals("javax.servlet.http.HttpServletResponse"))
			return response;
		if (classname.equals("java.lang.String")) {
			if (values.length == 1) {

				return value;
			} else if (values.length > 1) {
				String zhi = "";
				for (String string : values) {
					zhi += string + ",";
				}
				return zhi.substring(0, zhi.length() - 1);
			} else {
				return null;
			}
		}

		if (classname.equals("[Ljava.lang.String;"))
			return values;
		if (classname.equals("byte")) {
			if (value != null)
				return Byte.parseByte(value);
			else
				return (byte) 0;
		}

		if (classname.equals("java.lang.Byte")) {
			if (value != null)
				return Byte.parseByte(value);
			else
				return null;
		}
		if (classname.equals("[B")) {
			if (value != null) {
				byte[] bytes = new byte[] {};
				for (int i = 0; i < values.length; i++) {
					bytes[i] = Byte.parseByte(values[i]);
				}
				return bytes;
			} else
				return null;
		}
		if (classname.equals("[Ljava.lang.Byte;")) {
			if (value != null) {
				Byte[] bytes = new Byte[] {};
				for (int i = 0; i < values.length; i++) {
					bytes[i] = Byte.parseByte(values[i]);
				}
				return bytes;
			} else
				return null;
		}
		if (classname.equals("short")) {
			if (value != null)
				return Short.parseShort(value);
			else
				return (short) 0;
		}

		if (classname.equals("java.lang.Short")) {
			if (value != null)
				return Short.parseShort(value);
			else
				return null;
		}
		if (classname.equals("[S")) {
			if (value != null) {
				short[] shorts = new short[] {};
				for (int i = 0; i < values.length; i++) {
					shorts[i] = Short.parseShort(values[i]);
				}
				return shorts;
			} else
				return null;
		}
		if (classname.equals("[Ljava.lang.Short;")) {
			if (value != null) {
				Short[] shorts = new Short[] {};
				for (int i = 0; i < values.length; i++) {
					shorts[i] = Short.parseShort(values[i]);
				}
				return shorts;
			} else
				return null;
		}
		if (classname.equals("int")) {
			if (value != null)
				return Integer.parseInt(value);
			else
				return 0;
		}
		if (classname.equals("java.lang.Integer")) {
			if (value != null)
				return Integer.parseInt(value);
			else
				return null;
		}
		if (classname.equals("[I")) {
			if (value != null) {
				int[] ints = new int[] {};
				for (int i = 0; i < values.length; i++) {
					ints[i] = Integer.parseInt(values[i]);
				}
				return ints;
			} else
				return null;
		}
		if (classname.equals("[Ljava.lang.Integer;")) {
			if (value != null) {
				Integer[] ints = new Integer[] {};
				for (int i = 0; i < values.length; i++) {
					ints[i] = Integer.parseInt(values[i]);
				}
				return ints;
			} else
				return null;
		}
		if (classname.equals("long")) {
			if (value != null)
				return Long.parseLong(value);
			else
				return 0L;
		}
		if (classname.equals("java.lang.Long")) {
			if (value != null)
				return Long.parseLong(value);
			else
				return null;
		}
		if (classname.equals("[J")) {
			if (value != null) {
				long[] longs = new long[] {};
				for (int i = 0; i < values.length; i++) {
					longs[i] = Long.parseLong(values[i]);
				}
				return longs;
			} else
				return null;
		}
		if (classname.equals("[Ljava.lang.Long;")) {
			if (value != null) {
				Long[] longs = new Long[] {};
				for (int i = 0; i < values.length; i++) {
					longs[i] = Long.parseLong(values[i]);
				}
				return longs;
			} else
				return null;
		}
		if (classname.equals("float")) {
			if (value != null)
				return Float.parseFloat(value);
			else
				return 0.0f;
		}
		if (classname.equals("java.lang.Float")) {
			if (value != null)
				return Float.parseFloat(value);
			else
				return null;
		}
		if (classname.equals("[F")) {
			if (value != null) {
				float[] floats = new float[] {};
				for (int i = 0; i < values.length; i++) {
					floats[i] = Float.parseFloat(values[i]);
				}
				return floats;
			} else
				return null;
		}
		if (classname.equals("[Ljava.lang.Float;")) {
			if (value != null) {
				Float[] floats = new Float[] {};
				for (int i = 0; i < values.length; i++) {
					floats[i] = Float.parseFloat(values[i]);
				}
				return floats;
			} else
				return null;
		}
		if (classname.equals("double")) {
			if (value != null)
				return Double.parseDouble(value);
			else
				return 0.0d;
		}
		if (classname.equals("java.lang.Double")) {
			if (value != null)
				return Double.parseDouble(value);
			else
				return null;
		}
		if (classname.equals("[D")) {
			if (value != null) {
				double[] doubles = new double[] {};
				for (int i = 0; i < values.length; i++) {
					doubles[i] = Double.parseDouble(values[i]);
				}
				return doubles;
			} else
				return null;
		}
		if (classname.equals("[Ljava.lang.Double;")) {
			if (value != null) {
				Double[] doubles = new Double[] {};
				for (int i = 0; i < values.length; i++) {
					doubles[i] = Double.parseDouble(values[i]);
				}
				return doubles;
			} else
				return null;
		}

		if (classname
				.equals("[Lorg.apache.commons.fileupload.disk.DiskFileItem;")) {

			if (Parameter.FileUpload != null) {
				List<DiskFileItem> fileItems = null;
				try {
					fileItems = Parameter.FileUpload.parseRequest(request);
				} catch (FileUploadException e1) {
					e1.printStackTrace();
				}
				fileItems.iterator();
				DiskFileItem[] items = null;
				int i = 0;
				for (DiskFileItem item : fileItems) {
					if (item.getFieldName().equals(ParamName)) {
						if (items == null)
							items = new DiskFileItem[fileItems.size()];
						items[i] = item;
						i++;
					}

				}
				return items;
			} else {
				try {
					new LoungerWebException()
							.LoungerWebException("请在xml中定义<FileItemFactory>");
				} catch (LoungerWebException e) {
					log.error(e.getMessage(), e);
				}
			}
		}

		if (classname.equals("org.apache.commons.fileupload.disk.DiskFileItem")) {
			if (Parameter.FileUpload != null) {
				List<DiskFileItem> fileItems = null;
				try {
					fileItems = Parameter.FileUpload.parseRequest(request);
				} catch (FileUploadException e1) {
					e1.printStackTrace();
				}
				fileItems.iterator();
				for (DiskFileItem item : fileItems) {
					if (item.getFieldName().equals(ParamName)) {
						return item;

					}

				}
				return null;
			} else {
				try {
					new LoungerWebException()
							.LoungerWebException("请在xml中定义<FileItemFactory>");
				} catch (LoungerWebException e) {
					log.error(e.getMessage(),e);
				}
			}
		}

		if (classname.equals("boolean")) {
			if (value != null)
				return Boolean.parseBoolean(value);
			else
				return false;
		}
		if (classname.equals("java.lang.Boolean")) {
			if (value != null)
				return Boolean.parseBoolean(value);
			else
				return false;
		}
		if (classname.equals("[Z")) {

			if (value != null) {
				boolean[] booleans = new boolean[] {};
				for (int i = 0; i < values.length; i++) {
					booleans[i] = Boolean.parseBoolean(values[i]);
				}
				return booleans;
			} else
				return null;
		}
		if (classname.equals("[Ljava.lang.Boolean;")) {
			if (value != null) {
				Boolean[] booleans = new Boolean[] {};
				for (int i = 0; i < values.length; i++) {
					booleans[i] = Boolean.parseBoolean(values[i]);
				}
				return booleans;
			} else
				return null;
		}
		if (classname.equals("java.util.Date")) {
			return getDate(value);
		}
		if (classname.equals("[java.util.Date;")) {

			if (value != null) {
				Date[] Dates = new Date[] {};
				for (int i = 0; i < values.length; i++) {
					Dates[i] = getDate(values[i]);
				}
				return Dates;
			} else
				return null;
		}
		if (classname.equals("java.util.Map")) {
			Map<String, Object> nemap = null;
			Map<String, String[]> mapname = request.getParameterMap();
			for (String key : mapname.keySet()) {
				if (key.indexOf("[" + ParamName + "].") == 0) {
					if (nemap == null)
						nemap = new HashMap<String, Object>();
					nemap.put(
							key.substring(key.indexOf(".") + 1, key.length()),
							request.getParameter(key));
				}
			}
			return nemap;
		}

		try {
			Class<?> clas = Class.forName(classname);
			Object obj = clas.newInstance();
			for (Field d : clas.getDeclaredFields()) {
				String FieldValue = request.getParameter(ParamName + "."
						+ d.getName());
				int islog = 0;
				Map<String, String[]> mapname = request.getParameterMap();

				for (String key : mapname.keySet()) {
					if (key.indexOf(ParamName + "." + d.getName()) > -1
							&& new String(ParamName + "." + d.getName())
									.length() < key.length()) {
						islog = 1;

					}
				}
				if (FieldValue != null) {
					d.setAccessible(true);
					d.set(obj,
							GetValue(request, response, ParamName, d.getType()
									.getName(), FieldValue, null));
				} else if (islog == 1) {
					d.setAccessible(true);
					d.set(obj,
							GetValue(request, response,
									ParamName + "." + d.getName(), d.getType()
											.getName(), null, null));
				}

			}
			return obj;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}

		return null;

	}

	public static Date getDate(String value) {
		if (value != null) {
			try {
				if (Pattern
						.matches(
								"[0-9]{4}[/][0-9]{1,2}[/][0-9]{1,2}[ ][0-9]{1,2}[:]{1}[0-9]{1,2}[:][0-9]{1,2}",
								value))
					return sm1.parse(value);
				if (Pattern
						.matches("[0-9]{4}[/][0-9]{1,2}[/][0-9]{1,2}", value))
					return sm2.parse(value);
				if (Pattern
						.matches(
								"[0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}[ ][0-9]{1,2}[:]{1}[0-9]{1,2}[:][0-9]{1,2}",
								value))
					return sm3.parse(value);
				if (Pattern
						.matches("[0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}", value))
					return sm4.parse(value);
				else
					return null;
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}

		return null;
	}
}
