package com.yen.springChatRoom.service;

import com.yen.springChatRoom.bean.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatService {

  private final String PUBLIC_TOPIC = "/topic/public";

  @Autowired private SimpMessageSendingOperations simpMessageSendingOperations;

  public void sendMsg(@Payload ChatMessage chatMessage) {

    log.info("Send msg by simpMessageSendingOperations:" + chatMessage.toString());
    simpMessageSendingOperations.convertAndSend(PUBLIC_TOPIC, chatMessage);
  }

  public void alertUserStatus(@Payload ChatMessage chatMessage) {

    log.info("Alert user online by simpMessageSendingOperations:" + chatMessage.toString());
    simpMessageSendingOperations.convertAndSend(PUBLIC_TOPIC, chatMessage);
  }
}
