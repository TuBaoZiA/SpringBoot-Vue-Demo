package com.ycj.demo.web.admin.util.redis.key;

public interface KeyPrefix {

	public int expireSeconds();

	public String getPrefix();
}
