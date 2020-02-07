package com.association.common.all.util.log;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

public class SMSHelper {
public static void main(String[] args){
sendCodeAsync("12234","13916619401");
}
    public static void sendCodeAsync(String code , String mobilePhone){
        sendCodeAsync(code,mobilePhone,(v)->{});
    }
    public static void sendCodeAsync(String code , String mobilePhone, Consumer<Boolean> callback){
        Components.defaultThreadPool.execute(() -> {
            Boolean result = sendCodeSync(code, mobilePhone);
            callback.accept(result);
        });
    }
    public static boolean sendCodeSync(String code ,String mobilePhone){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FhdWLnwVx3AxYhqT3L9", "7Mns6X77LD4scuEqCZV3yAtDgPXNfv");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", mobilePhone);
        request.putQueryParameter("SignName", "Ler4J");
        request.putQueryParameter("TemplateCode", "SMS_164513085");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
          //  System.out.println(response.getData());
            ILogger.getInstance().info(response.getData());
            System.out.println(response.getHttpStatus());
            //if()
        } catch (Exception e){
            new ILogger().error("ERROR MESSAGE {} " , e.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
//    public static void main(String[] args) {
//       sendCode("1234","17621227392");
//    }
}
