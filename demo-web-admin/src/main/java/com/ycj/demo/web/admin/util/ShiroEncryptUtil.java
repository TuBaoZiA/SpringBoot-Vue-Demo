package com.ycj.demo.web.admin.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class ShiroEncryptUtil {

    private static final String HASH_ALGORITHM_NAME = "MD5";

    private static final int HASH_ITERATIONS = 2;

    public static String encryptPassBySalt(String password, String salt){
        ByteSource credentialsSalt = ByteSource.Util.bytes(salt);
        return new SimpleHash(HASH_ALGORITHM_NAME, password, credentialsSalt, HASH_ITERATIONS).toString();
    }

}
