package com.association.common.all.util.log;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class OSSHelper {
    private static String endpoint = "oss-cn-shanghai.aliyuncs.com";
    private static String accessKeyId = "LTAI4FhdWLnwVx3AxYhqT3L9";
    private static String accessKeySecret = "7Mns6X77LD4scuEqCZV3yAtDgPXNfv";
    private static String bucketName = "association-eletric-edu";
    private static String absolutePath = "association/";
    private static String urlPrefix = bucketName+"."+endpoint;
    public static Boolean pushFile(File file) {
        return pushFile(file, null , null);
    }

    public static Boolean pushFile(MultipartFile multipartFile,String filename) {
        try {
            return pushFile(null, multipartFile.getInputStream(),filename);
        } catch (IOException e) {
            new ILogger().info("SOMETHING ERROR WHEN PUT OBJECT BY ALIYUN OSS :{}", e.getMessage());
        }
        return Boolean.FALSE;
    }

    public static Boolean pushFile(InputStream inputStream) {
        return pushFile(null, inputStream,null);
    }

    public static OSS buildClientDefault() {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        return ossClient;
    }

    private static Boolean pushFile(File file, InputStream inputStream,String filename) {
        OSS ossClient = buildClientDefault();
        PutObjectRequest putObjectRequest = null;
        if (file != null) {
            putObjectRequest =
                    new PutObjectRequest(bucketName,
                            absolutePath + file.getName(), file);
        } else if (inputStream != null) {
            putObjectRequest =
                    new PutObjectRequest(bucketName,
                            absolutePath +filename, inputStream);
        }
        PutObjectResult result = ossClient.putObject(putObjectRequest);
        ossClient.shutdown();
        return Boolean.TRUE;
    }
    public static String buildUrl(String fileName){
        if(fileName.charAt(0) != '/'){
            fileName ="/" + fileName ;
        }
        return urlPrefix +"/association"+ fileName;
    }
    public static void main(String[] args) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, "association/teste12.yml");
        OSS oss = buildClientDefault();
        URL url =oss.generatePresignedUrl(generatePresignedUrlRequest);
        System.out.println(JSONObject.toJSONString(url));
        if (true){return;}
        String url5 = buildUrl("association/teste12.yml");
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
//    File file = new File("D:\\Documents\\assocation\\admin-parent\\src\\main\\resources\\application.yml");
//    System.out.println(file.getName());
//    MultipartFile multipartFile = new CommonsMultipartFile(new org.apache.commons.fileupload.FileItem("1.txt", "application/json", false, "2.txt", 1234, file) {
//    });
//    multipartFile.getInputStream();
//
    }
}
