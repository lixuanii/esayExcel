package com.lixuan.esayexcel.util;

import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.InputStream;
import java.net.URL;


public class AliYunOssUtil {

    /**
     * bucket所在对应的endpoint
     */
    private static final String ENDPOINT = "your endpoint";

    /**
     * 阿里云账号
     */
    private static final String ACCESS_KEY_ID = "your key id";

    /**
     * 阿里云密钥
     */
    private static final String ACCESS_KEY_SECRET = "your key secret";

    /**
     * bucket名称
     */
    private static final String BUCKET_NAME = "your bucket name";

    private static final OSS oss;

    static {
        oss = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 上传文件到oss
     *
     * @param fileName    文件名
     * @param inputStream 输入流
     */
    public static void uploadToOss(String fileName, InputStream inputStream) {
        try {
            oss.putObject(BUCKET_NAME, fileName, inputStream);
        } finally {
            oss.shutdown();
        }
    }

    /**
     * 获取文件访问路径(此方法所生成的访问路径有效时间到2099年)
     *
     * @param fileName 文件名
     * @return 文件访问路径
     */
    public static String getObjectOssUrl(String fileName) {
        String ossUrl;
        try {
            //生成以get方式访问的签名url，可以通过浏览器直接访问相关内容
            URL url = oss.generatePresignedUrl(BUCKET_NAME, fileName, DateUtil.parse("2099-12-30"));
            ossUrl = url.toString();
        } finally {
            oss.shutdown();
        }
        return ossUrl;
    }


}
