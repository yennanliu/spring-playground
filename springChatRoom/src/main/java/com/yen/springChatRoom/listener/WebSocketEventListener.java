package com.yen.springChatRoom.listener;

import com.yen.springChatRoom.bean.ChatMessage;
import com.yen.springChatRoom.util.JsonUtil;
import java.net.Inet4Address;
import java.net.InetAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class WebSocketEventListener {

  @Value("${server.port}")
  private String serverPort;

  @Value("${redis.set.onlineUsers}")
  private String onlineUsers;

  @Value("${redis.channel.userStatus}")
  private String userStatus;

  @Autowired private RedisTemplate<String, String> redisTemplate;

  @Autowired private SimpMessageSendingOperations messagingTemplate;

  // connect
  //    @EventListener
  //    public void handleWebSocketConnectListener(SessionConnectedEvent event){
  //
  //        LOGGER.info("Receive a new web socket connection!");
  //    }
  public void handleWebSocketConnectListener(SessionConnectedEvent event) {

    InetAddress localHost;
    try {
      localHost = Inet4Address.getLocalHost();
      log.info(
          "Received a new web socket connection from:"
              + localHost.getHostAddress()
              + ":"
              + serverPort);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  // disconnect
  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    log.info(">>> (handleWebSocketDisconnectListener) headerAccessor = " + headerAccessor);

    String username = (String) headerAccessor.getSessionAttributes().get("username");
    String receiver = (String) headerAccessor.getSessionAttributes().get("receiver");

    if (username != null) {
      log.info("User Disconnected : " + username);
      ChatMessage chatMessage = new ChatMessage();
      chatMessage.setType(ChatMessage.MessageType.LEAVE);
      chatMessage.setSender(username);

      // TODO : double check ??? (for private chat)
      if (receiver != null) {
        chatMessage.setReceiver(receiver);
      }

      try {
        redisTemplate.opsForSet().remove(onlineUsers, username);
        redisTemplate.convertAndSend(userStatus, JsonUtil.parseObjToJson(chatMessage));
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }
}
