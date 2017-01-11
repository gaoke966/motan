package com.weibo.controller;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * Created by kegao on 2017/1/4.
 */
public class Md5Encoder {
    public static void main(String[] args) {
        Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        String ps = md5PasswordEncoder.encodePassword("guest123","guest");
        System.out.println(ps);
        ps = md5PasswordEncoder.encodePassword("admin966","admin");
        System.out.println(ps);
        ps = md5PasswordEncoder.encodePassword("admin","admin");
        System.out.println(ps);
        ps = md5PasswordEncoder.encodePassword("guest","guest");
        System.out.println(ps);
    }
}
