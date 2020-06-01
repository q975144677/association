package com.association.admin.app;

import org.apache.commons.lang.StringUtils;
import org.jsoup.internal.StringUtil;

import java.util.*;
public class Main{
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        String token = sc.nextLine();
        String binaryToken = toBinary(token);
        if(binaryToken.length() < 32){
            int len = 32 - binaryToken.length();
            StringBuilder zero = new StringBuilder();
            for(int i = 0 ; i < len ; i ++){
                zero.append(0);
            }
            binaryToken = zero.toString() + binaryToken;
        }
        StringBuilder reverseToken = new StringBuilder();
        char[] chs = binaryToken.toCharArray();
        for(int i = chs.length - 1 ; i >= 0 ; i--){
            reverseToken.append(chs[i]);
        }
          System.out.println(reverseToken.toString());
        char[] tempBinaryToken = reverseToken.toString().toCharArray();
        Long result = 0L ;
        for(int i = tempBinaryToken.length -1  ; i >= 0  ; i --){
            if(tempBinaryToken[i] == '1'){
                result += (long)Math.pow(2,tempBinaryToken.length -1 - i);
            }
        }
        System.out.print(result);
    }
    public static String toBinary(String token){
        if(true){
            return Long.toBinaryString(Long.parseLong(token));
        }

        char[] chs = token.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c : chs){
            sb.append(char2Binary(c));
        }
         System.out.println(sb.toString());
        return sb.toString();
    }
    public static String char2Binary(char c){
        return Integer.toBinaryString(c - '0');
    }
}