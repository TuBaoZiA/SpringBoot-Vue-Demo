package com.ycj.demo.web.admin.component;

import com.ycj.demo.web.admin.util.redis.key.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 */
@Component
public class RedisService {

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	/**
	 * 获取key值
	 * @param prefix
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
		//生成真正的key
		String realKey=prefix.getPrefix()+key;
		return (T) redisTemplate.opsForValue().get(realKey);
	}
	
	/**
	 * 设置值
	 * @param prefix
	 * @param key
	 * @param value
	 * @return
	 */
	public <T> boolean set(KeyPrefix prefix, String key, T value) {
		if(value==null) {
			return false;
		}

		String realKey=prefix.getPrefix()+key;
		int seconds=prefix.expireSeconds();

		if(seconds<=0) {
			redisTemplate.opsForValue().set(realKey, value);
		}else {
			redisTemplate.opsForValue().set(realKey, value, seconds, TimeUnit.SECONDS);
		}
		return true;
	}
	
	/**
	 * 判断key是否存在
	 */
	public boolean hasKey(KeyPrefix prefix,String key) {
		String realKey=prefix.getPrefix()+key;
		return redisTemplate.hasKey(realKey);
	}

	/**
	 * 判断key是否存在
	 */
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}


	
	/**
	 * 删除
	 */
	public void delete(KeyPrefix prefix,String key) {
		String realKey=prefix.getPrefix()+key;
		redisTemplate.delete(realKey);
	}

	/*private static <T> String beanToStr(T value) throws JsonProcessingException {
		if(value==null) {
			return null;
		}
		Class<?> clazz=value.getClass();
		if(clazz==int.class||clazz==Integer.class) {
			return ""+value;
		}else if(clazz==String.class){
			return (String)value;
		}else if(clazz==long.class||clazz==Long.class) {
			return ""+value;
		}else {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(value);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T strToBean(String str,Class<T> clazz) throws IOException {
		if(str==null||str.length()<=0||clazz==null) {
			return null;
		}
		if(clazz==int.class||clazz==Integer.class) {
			return (T) Integer.valueOf(str);
		}else if(clazz==String.class){
			return (T) str;
		}else if(clazz==long.class||clazz==Long.class) {
			return (T) Long.valueOf(str);
		}else {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(str, clazz);
		}
	}*/
 	
}
