package com.yen.springChatRoom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// NOTE !!! DO NOT enable below, or will cause websocket connection error
// @ComponentScan(basePackages = "com.yen.springChatRoom.redis.RedisListenerBean") //
// https://blog.csdn.net/automal/article/details/111859409
@SpringBootApplication
public class ChatRoomApplication {

  public static void main(String[] args) {

    SpringApplication.run(ChatRoomApplication.class, args);
  }
}
