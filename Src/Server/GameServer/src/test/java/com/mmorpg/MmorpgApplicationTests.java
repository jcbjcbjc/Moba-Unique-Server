//package com.game;
//
//import com.game.proto.Message;
//import org.junit.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.io.*;
//
//@SpringBootTest
//class MmorpgApplicationTests {
//
//    @Test
//    void contextLoads() {
//
//    }
//
//
//    public static void main(String[] args) throws IOException {
//
//        Message.NetMessage.Builder builder = Message.NetMessage.newBuilder();
//        Message.NetMessageRequest.Builder builder1 = Message.NetMessageRequest.newBuilder();
//        Message.UserLoginRequest.Builder builder2 = Message.UserLoginRequest.newBuilder();
//        builder2.setPassward("password");
//        builder2.setUser("username");
//        builder1.setUserLogin(builder2);
//        builder.setRequest(builder1);
//        Message.NetMessage build = builder.build();
//        System.out.println(build);
//        // System.out.println(build);
//        // byte[] data = build.toByteArray();
//
//    }
//
//}
