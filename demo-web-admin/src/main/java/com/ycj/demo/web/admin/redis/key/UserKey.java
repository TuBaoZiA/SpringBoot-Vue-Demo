package com.ycj.demo.web.admin.redis.key;


public class UserKey extends BasePrefix{

	private UserKey(String prefix) {
		super(prefix);
	}

	public static UserKey userInfo=new UserKey("USER-INFO");

}
