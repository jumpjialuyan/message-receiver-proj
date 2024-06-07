package com.shm.metro.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/** 
 * <p>字符串操作做共同类</p>
 * @author 张刚学 
 * 2016年10月24日 下午10:14:50 
 */
public class StringUtil {

	/**  
	 * <p>md5加密</p>
	 * @param info 加密内容
	 * @return String:加密后的数据
	 */
	public static String encode(String info) {
		// String info = "410825196509290018";
		try {
			MessageDigest alg = MessageDigest.getInstance("MD5");
			alg.update(info.getBytes());
			byte[] digesta = alg.digest();
			String result = "";
			for (int i = 0; i < digesta.length; i++) {
				int m = digesta[i];
				if (m < 0) {
					m += 256;// 如果是负数就取模
				}
				if (m < 16) {
					result += "0";// 如果长度不够就加"0"
				}
				result = result + Integer.toString(m, 16).toLowerCase() + "";
			}
			return result;
			// System.out.println(result);
			// System.out.println(result.equals("e3af01a8cd9e831dce07f8221ed4dc56"));
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	/**  
	 * <p>打印对象的所有属性</p>
	 * @param o 打印的对象

	 */
	public static void getObjectProperty(Object o) {
		if (o == null) {
			System.out.println("Object is null");
			return;
		}
		Class<?> c = o.getClass();
		Field field[] = c.getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("------ " + " begin ------\n");
			for (Field f : field) {
				sb.append(f.getName());
				sb.append(" : ");
				sb.append("->" + invokeMethod(o, f.getName(), null) + "<-");
				sb.append("\n");
			}
			sb.append("------ " + " end ------\n");
		} catch (Exception e) {

			e.printStackTrace();
		}
		System.out.println(sb.toString());

	}

	/**  
	 * <p>在指定对象中执行选中的属性的get方法</p>
	 * @param owner 要操作的对象
	 * @param methodName 要执行的get方法名称(若要执行getId(),只需要传入"id"即可)
	 * @param args 无
	 * @return Object:get方法的执行结果
	 * @throws Exception 异常
	 */
	public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {
		Class<?> ownerClass = owner.getClass();

		methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
		Method method = null;
		try {
			method = ownerClass.getMethod("get" + methodName);
		} catch (SecurityException e) {

		} catch (NoSuchMethodException e) {
			return " can't find 'get" + methodName + "' method";
		}
		if (method != null) {
			return method.invoke(owner);
		} else {
			return null;
		}
	}

	/**  
	 * <p>判断字符串数据不是Empty(等于null和"")</p>
	 * @param str 要判断的字符串 
	 * @return boolean:true字符串不是Empty，false字符串是Empty
	 */
	public static boolean notEmpty(String str) {
		if (str == null || str.trim().length() == 0)
			return false;
		else
			return true;
	}
	
	/**  
	 * <p>判断字符串数据是不是Empty(等于null和"")</p>
	 * @param str 要判断的字符串 
	 * @return boolean:true字符串是Empty，false字符串不是Empty
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.trim().length() == 0)
			return true;
		else
			return false;
	}

	/**  
	 * <p>验证字符串内容是否为空或超长:错误true，正确：false</p>
	 * @param cellValue 待判断的字符串
	 * @param length 最大长度 
	 * @return boolean:true 字符串为空或超长，false 字符串不为空且不超长
	 */
	public static boolean validateCell(String cellValue, int length) {
		if (StringUtil.isEmpty(cellValue))
			return true;
		if (cellValue.trim().length() >= length)
			return true;
		return false;
	}

	
	/**  
	 * <p>将集合中的所有字符串拼接在一起</p>
	 * @param list 字符串集合
	 * @return String:拼接后的字符串
	 */
	public static String listToStr(List<String> list) {

		String str = "";
		if (list == null || list.size() == 0) {

			return "";
		}
		for (int i = 0; i < list.size(); i++) {

			if (i == list.size() - 1) {

				str += list.get(i);

			} else {

				str += list.get(i) + ",";

			}

		}

		System.out.println("list to String =" + str);
		return str;

	}

	/**  
	 * <p>subString截取字符串，补".."</p>
	 * @param str 待截取的字符串
	 * @param len 保留的长度
	 * @return String:截取后的字符串
	 */
	public static String subStringByLen(String str, int len) {

		if (str == null || str.getBytes().length < len) {

			return str;
		}

		if (str.getBytes().length >= len) {

			str = str.substring(0, len % 2 == 0 ? len / 2 : len / 2 + 1);
			
			str = str + "..";
			return str;
		}

		return str;
	}

	/**  
	 * <p>整理字符串后，替换字符串中的指定字符。如果 str 为 null, 返回 ""；否则返回整理和替换后的字符串。</p>
	 * @param str 源字符串
	 * @param pattern 查找的字符
	 * @param replace 替换的字符
	 * @return String:替换后的字符
	 */
	public static String replace(String str, char pattern, char replace) {
		int s = 0;
		int e = 0;
		if (str == null)
			str = "";
		str = str.trim();
		StringBuffer result = new StringBuffer();
		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + 1;
		}
		result.append(str.substring(s));
		return result.toString();
	}

	/**  
	 * <p>去除字符串两边的空格</p>
	 * @param str 操作的字符串
	 * @return String:去除空格的字符串
	 */
	public static String strTrim(String str) {

		if (str == null) {

			return "";
		}
		return str.trim();

	}

	/**  
	 * <p>将字符串数组转化为Integer数组，字符串中带有"_",处理是将字符串以"_"拆分后把第二个值转化成Integer，再放入Integer集合中</p>
	 * @param list 字符串集合
	 * @return {@code List<Integer>}:Integer集合
	 */
	public static List<Integer> strListToIntList(List<String> list) {
		List<Integer> intList = new ArrayList<Integer>();
		if (list == null) {
			return intList;
		}
		for (int i = 0; i < list.size(); i++) {
			String[] str = list.get(i).split("_");
			intList.add(Integer.parseInt((str[1])));

		}

		return intList;

	}
	/**  
	 * <p>将集合中的所有字符串拼接在一起</p>
	 * @param list 字符串集合
	 * @return String:拼接后的字符串
	 */
	/**  
	 * <p>将Map中的所有字符串拼接成字符串</p>
	 * @param map 字符串Map，格式{@code "Map<String, String>"}
	 * @return String:拼接后的字符串
	 */
	@SuppressWarnings("unchecked")
	public static String mapToString(Map<String, String> map) {
		String parameter = "";
		if (map != null) {

			Iterator<?> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> m = (Map.Entry<String, String>) iterator.next();
				String key = m.getKey();
				String value = m.getValue();
				parameter += "&" + key + "=" + value;
			}

		}
		return parameter;

	}
	
	/**  
	 * <p>获取"2011"到"2030"的字符串集合</p>
	 * @return {@code List<String>}:字符串集合
	 */
	public static List<String> getMatchNo() {

		List<String> list = new ArrayList<>();
		list.add("2011");
		list.add("2012");
		list.add("2013");
		list.add("2014");
		list.add("2015");
		list.add("2016");
		list.add("2023");
		list.add("2018");
		list.add("2019");
		list.add("2020");
		list.add("2021");
		list.add("2022");
		list.add("2023");
		list.add("2024");
		list.add("2025");
		list.add("2026");
		list.add("2027");
		list.add("2028");
		list.add("2029");
		list.add("2030");
		list.add("2031");
		list.add("2032");
		list.add("2033");
		list.add("2034");
		list.add("2035");
		list.add("2036");
		list.add("2037");
		list.add("2038");
		list.add("2039");
		list.add("2030");
		return list;

	}

	/**  
	 * <p>Integer数据转字符串，参数等于null时，返回""</p>
	 * @param str Integer数据，待转换
	 * @return String:转换后的字符串型数据
	 */
	public static String conVertInteger(Integer str) {
		if (str == null) {
			return "";
		} else {
			return str + "";
		}
	}
	/**  
	 * <p>Integer数据转字符串,参数等于null时，返回"0"</p>
	 * @param str Integer数据，待转换
	 * @return String:转换后的字符串型数据
	 */
	public static String conVertInt(Integer str) {
		if (str == null) {
			return "0";
		} else {
			String str1 = str.toString();
			return str1;
		}
	}

	/**  
	 * <p>Double数据转字符串,参数等于null时，返回"0"</p>
	 * @param str Double数据，待转换
	 * @return String:转换后的字符串型数据
	 */
	public static String conVertDouble(Double str) {

		if (str == null) {
			return "0";
		} else {
			String str1 = str.toString();
			return str1;
		}

	}

	/**  
	 * <p>String数据转字符串,参数等于null时，返回""</p>
	 * @param str String数据，待转换
	 * @return String:转换后的字符串型数据
	 */
	public static String conVertString(String str) {
		if (str == null) {
			return "";
		} else {
			return str + "";
		}
	}

	/**
	 * 将Object转化为字符串
	 * @param input
	 * @return
	 */
	public static String convertString(Object input){
		String tmpValue = null;
		if(!(input==null)){
			tmpValue = input.toString();
		}
		return tmpValue;
	}

	/**
	 * 将Object转化为字符串
	 * @param input
	 * @param def
	 * @return
	 */
	public static String convertString(Object input,String def){
		String tmpValue = def;
		if(!(input==null)){
			tmpValue = input.toString();
		}
		return tmpValue;
	}

	/**
	 * 将一个byte数组转换成十六进制字符串
	 * @param in
	 * @return十六进制字符串
	 */
	public static String bytesToHex(byte[] in) {
		final StringBuilder builder = new StringBuilder();
		for(byte b : in) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}

	public static String strReversed(String str){
		if(isEmpty(str)){
			return "";
		}else{
			String[] strArr = str.split("");
			StringBuilder strBuilder = new StringBuilder("");
			for(int i = 0; i< strArr.length; i++){
				strBuilder.append(strArr[strArr.length-(i+1)]);
			}
			return strBuilder.toString();
		}
	}

}
