package com.xingcloud.framework.context.result;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xingcloud.framework.context.annotation.Visible;
import com.xingcloud.util.json.JSONUtil;
import com.xingcloud.util.reflection.ReflectionUtil;

public abstract class AbstractResult implements Result<Serializable> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 提供asResult的抽象方法
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Serializable asResult() {
		String fieldName = null;
		Object value = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Field> fields = ReflectionUtil.getAllFields(getClass());
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			// 如果有visible注解并且visible属性是false则该属性不返回
			Visible visible = field.getAnnotation(Visible.class);
			if (visible != null && visible.visible().equals("false"))
				continue;
			fieldName = field.getName();
			field.setAccessible(true);
			try {
				value = field.get(this);
			} catch (Exception e) {
				Logger.getLogger(getClass()).error(e.getMessage());
			}
			if (value == null) {
				map.put(fieldName, null);
				continue;
			}
			if (Result.class.isAssignableFrom(value.getClass())) {
				try {
					map.put(fieldName, ((Result) value).asResult());
				} catch (Exception e) {
					map.put(fieldName, null);
					Logger.getLogger(getClass()).error(e.getMessage());
				}
				continue;
			}
			map.put(fieldName, value);
		}
		return (Serializable) map;
	}

	public String toString() {
		try {
			return JSONUtil.toJSON(asResult());
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage());
			return "";
		}
	}
}
