package com.ycj.demo.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;

import java.io.File;
import java.io.InputStream;

public class OSSUtil {

    private static final String ACCESS_KEY_ID = "LTAIsh4aGgo3hSbw";
    private static final String ACCESS_KEY_SECRET = "8Bu6FNxGjMZRqGoEbsapnkU3nFTt2o";

    private static final String END_POINT = "oss-cn-hangzhou.aliyuncs.com";

    private static final String BUCKET_NAME = "bucket-demo-01";


    public static void putObject(String objectName, File file, boolean isPublicRead){
        OSSClient ossClient = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        ossClient.putObject(BUCKET_NAME, objectName, file);
        if(isPublicRead) {
            ossClient.setObjectAcl(BUCKET_NAME, objectName, CannedAccessControlList.PublicRead);
        }
        ossClient.shutdown();
    }

    public static void putObject(String objectName, InputStream inputStream, boolean isPublicRead){
        OSSClient ossClient = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        ossClient.putObject(BUCKET_NAME, objectName, inputStream);
        if(isPublicRead) {
            ossClient.setObjectAcl(BUCKET_NAME, objectName, CannedAccessControlList.PublicRead);
        }
        ossClient.shutdown();
    }

    public static void setObjectAcl(String objectName, CannedAccessControlList permission){
        OSSClient ossClient = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        ossClient.setObjectAcl(BUCKET_NAME, objectName, permission);
        ossClient.shutdown();
    }

    public static void deleteObject(String objectName){
        OSSClient ossClient = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        ossClient.deleteObject(BUCKET_NAME, objectName);
        ossClient.shutdown();
    }

}
