package org.heyu.ig.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * json工具类
 * 
 * @author 孙贺宇
 * @version 1.1
 */
public class JsonUtil {

	private static JsonFactory jf;
	private static ObjectMapper mapper;

	public static ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  
		}
		return mapper;
	}

	public static JsonFactory getFactory() {
		if (jf == null) {
			jf = new JsonFactory();
		}
		return jf;
	}
	
	/**
	 * 对象转json
	 * @param obj
	 * @return
	 */
	public static String obj2json(Object obj) {
		if (obj == null) {
			return null;
		}
		JsonGenerator jg = null;
		try {
			jf = getFactory();
			mapper = getMapper();
			StringWriter out = new StringWriter();
			jg = jf.createGenerator(out);
			mapper.writeValue(jg, obj);
			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (jg != null){
					jg.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * json转对象
	 * @param json
	 * @param clz
	 * @return
	 */
	public static Object json2obj(String json, Class<?> clz) {
		try {
			mapper = getMapper();
			return mapper.readValue(json, clz);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static <T> List<T> json2array(String json, Class<?> clz) {
		try {
			mapper = getMapper();
			ArrayType arrayType = mapper.getTypeFactory().constructArrayType(clz);
			T[] datas = mapper.readValue(json, arrayType);
			return new ArrayList<T>(Arrays.asList(datas));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

}
