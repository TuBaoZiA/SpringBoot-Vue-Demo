package com.ycj.demo.web.admin.redis.key;

public interface KeyPrefix {

	public int expireSeconds();

	public String getPrefix();
}
