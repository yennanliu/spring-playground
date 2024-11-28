package com.yen.springChatRoom.controller;

import com.yen.springChatRoom.bean.ChatMessage;
import com.yen.springChatRoom.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChatController {

  final String onlineUserKey = "websocket.onlineUsers";
  private final String USER_NAME = "username";
  @Value("${redis.channel.msgToAll}")
  private String msgToAll;
  @Value("${redis.set.onlineUsers}")
  private String onlineUsers;
  @Value("${redis.channel.userStatus}")
  private String userStatus;
  @Value("${redis.channel.private}")
  private String privateChannel;
  // private RedisTemplate redisTemplate;
  // TODO : check difference ? RedisTemplate VS RedisTemplate<String, String>
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private SimpMessagingTemplate simpMessagingTemplate;


  /** single mode : read msg from FE, and send to other users (@SendTo("/topic/public")) directly */
  //    @MessageMapping("/chat.sendMessage")
  //    @SendTo("/topic/public")
  //    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
  //
  //        return chatMessage;
  //    }

  /**
   * cluster mode : read msg from FE, but NOT send to other users, instead, send to Redis channel,
   * so the other service on cluster can read/digest the msg
   */
  @MessageMapping("/chat.sendMessage")
  public void sendMessage(@Payload ChatMessage chatMessage) {
    try {

      // test : save msg to redis
      redisTemplate.opsForSet().add(msgToAll, JsonUtil.parseObjToJson(chatMessage));
      // redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(chatMessage)));
      redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(chatMessage));
    } catch (Exception e) {
      log.error("send msg error : " + e.getMessage(), e);
    }
  }

  @MessageMapping("/chat.addUser")
  public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

    log.info("User added in Chatroom:" + chatMessage.getSender());
    try {
      headerAccessor.getSessionAttributes().put(USER_NAME, chatMessage.getSender());
      redisTemplate.opsForSet().add(onlineUsers, chatMessage.getSender());
      redisTemplate.convertAndSend(userStatus, JsonUtil.parseObjToJson(chatMessage));
      // TODO : show online user
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  // TODO : check @DestinationVariable ?
  // @RequestMapping("/app/private/{username}")
  @MessageMapping("/chat.sendPrivateMessage") // This will be called when a user sends a message
  public void handlePrivateMessage(@Payload ChatMessage chatMessage) {

    // log.info("handlePrivateMessage : username = " + username + " message = " + message);
    // save to redis
    // redisTemplate.convertAndSend(userStatus, JsonUtil.parseObjToJson(chatMessage));
    //    redisTemplate
    //        .opsForSet()
    //        .add(privateChannel + "." + username, JsonUtil.parseObjToJson(message));
    //    simpMessagingTemplate.convertAndSendToUser(username, "/topic/private", message);
    // Send the message to the specific user
    log.info(">>> chatMessage  = " + chatMessage);
    //log.info(">>> chatMessage.getReceiver() = " + chatMessage.getReceiver());
    // TODO : fix below hardcode
    simpMessagingTemplate.convertAndSendToUser(
        "zzzz", "/queue/private", chatMessage);
  }
}
