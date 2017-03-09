package com.lounger.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * <pre>项目名称：Lounger  
 * 类名称：MethodParamName 
 * 类描述：获取方法参数名称 
 * @version </pre>
 */
public class MethodParamName {
	private static Logger log = Logger.getLogger(MethodParamName.class);
	/**
	 * 获取方法参数名列表
	 * 
	 * @param clazz
	 * @param m
	 * @return
	 * @throws IOException
	 */
	public static List<String> getMethodParamNames(Class<?> clazz, Method m)
			throws IOException {
		try (InputStream in = clazz.getResourceAsStream("/"
				+ clazz.getName().replace('.', '/') + ".class")) {
			return getMethodParamNames(in, m);
		}

	}

	public static List<String> getMethodParamNames(InputStream in, Method m)
			throws IOException {
		try (InputStream ins = in) {
			return getParamNames(
					ins,
					new EnclosingMetadata(m.getName(), Type
							.getMethodDescriptor(m),
							m.getParameterTypes().length));
		}

	}

	/**
	 * 获取构造器参数名列表
	 * 
	 * @param clazz
	 * @param constructor
	 * @return
	 */
	public static List<String> getConstructorParamNames(Class<?> clazz,
			Constructor<?> constructor) {
		try (InputStream in = clazz.getResourceAsStream("/"
				+ clazz.getName().replace('.', '/') + ".class")) {
			return getConstructorParamNames(in, constructor);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return new ArrayList<String>();
	}

	public static List<String> getConstructorParamNames(InputStream ins,
			Constructor<?> constructor) {
		try (InputStream in = ins) {
			return getParamNames(
					in,
					new EnclosingMetadata(constructor.getName(), Type
							.getConstructorDescriptor(constructor), constructor
							.getParameterTypes().length));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return new ArrayList<String>();
	}

	/**
	 * 获取参数名列表辅助方法
	 * 
	 * @param in
	 * @param m
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static List<String> getParamNames(InputStream in,
			EnclosingMetadata m) throws IOException {
		ClassReader cr = new ClassReader(in);
		ClassNode cn = new ClassNode();
		cr.accept(cn, ClassReader.EXPAND_FRAMES);// 建议EXPAND_FRAMES
		// ASM树接口形式访问
		List<MethodNode> methods = cn.methods;
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < methods.size(); ++i) {
			List<LocalVariable> varNames = new ArrayList<LocalVariable>();
			MethodNode method = methods.get(i);
			// 验证方法签名
			if (method.desc.equals(m.desc) && method.name.equals(m.name)) {
				// System.out.println("desc->"+method.desc+":"+m.desc);
				List<LocalVariableNode> local_variables = method.localVariables;
				for (int l = 0; l < local_variables.size(); l++) {
					String varName = local_variables.get(l).name;
					// index-记录了正确的方法本地变量索引。(方法本地变量顺序可能会被打乱。而index记录了原始的顺序)
					int index = local_variables.get(l).index;
					if (!"this".equals(varName)) // 非静态方法,第一个参数是this
						varNames.add(new LocalVariable(index, varName));
				}
				LocalVariable[] tmpArr = varNames
						.toArray(new LocalVariable[varNames.size()]);
				// 根据index来重排序，以确保正确的顺序
				Arrays.sort(tmpArr);
				for (int j = 0; j < m.size; j++) {
					list.add(tmpArr[j].name);
				}
				break;

			}

		}
		return list;
	}

	/**
	 * 方法本地变量索引和参数名封装
	 * 
	 * @author xby Administrator
	 */
	static class LocalVariable implements Comparable<LocalVariable> {
		public int index;
		public String name;

		public LocalVariable(int index, String name) {
			this.index = index;
			this.name = name;
		}

		public int compareTo(LocalVariable o) {
			return this.index - o.index;
		}
	}

	/**
	 * 封装方法描述和参数个数
	 * 
	 * @author xby Administrator
	 */
	static class EnclosingMetadata {
		// method name
		public String name;
		// method description
		public String desc;
		// params size
		public int size;

		public EnclosingMetadata(String name, String desc, int size) {
			this.name = name;
			this.desc = desc;
			this.size = size;
		}
	}

}
