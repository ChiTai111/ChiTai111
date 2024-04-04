package com.kangtao;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;


/*@SpringBootTest*/

public class OSSTest {



    private String accessKey = "pxsG0rzhfnA5tu8nJiKJmlF5Fsr1N75goXXglLmN";
    private String secretKey = "FpbLgDxAl3iE9BYgEoACiELWS5re2WQATpLykTJm";
    private String bucket = "kt-blog-111";
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Test
    public void testOss(){
//构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
// String accessKey = "your access key";
// String secretKey = "your secret key";
// String bucket = "sg-blog";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "2024/kt.png";
        try {
// byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
// ByteArrayInputStream byteInputStream=new
          //  ByteArrayInputStream(uploadBytes);
            InputStream inputStream = new
                    FileInputStream("C:\\Users\\ASUS\\Desktop\\1.png");
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response =
                        uploadManager.put(inputStream,key,upToken,null, null);
//解析上传成功的结果
                DefaultPutRet putRet = new
                        Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {

                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
//ignore
                }
            }
        } catch (Exception ex) {
//ignore
        }
    }
}
