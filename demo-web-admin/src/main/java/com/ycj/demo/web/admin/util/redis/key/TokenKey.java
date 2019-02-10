package com.ycj.demo.web.admin.util.redis.key;


public class TokenKey extends BasePrefix{

	private TokenKey(String prefix, int expireSeconds) {
		super(expireSeconds, prefix);
	}

	public static TokenKey TOKEN=new TokenKey("TOKEN", 30 * 60);

}
