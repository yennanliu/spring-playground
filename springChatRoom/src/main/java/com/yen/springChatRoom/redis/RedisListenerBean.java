package com.yen.springChatRoom.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/** Class for Redis channel conn */
@Slf4j
@Component
public class RedisListenerBean {

  // read setting from config
  @Value("${server.port}")
  private String serverPort;

  @Value("${redis.channel.msgToAll}")
  private String msgToAll;

  @Value("${redis.channel.userStatus}")
  private String userStatus;

  /**
   * Redis channel bean
   *
   * <p>1. listen Redis channel via binding (for example : container.addMessageListener) 2. can do
   * further biz logic in method
   */
  @Bean
  RedisMessageListenerContainer container(
      RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {

    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);

    // listen msgToAll (Redis channel)
    container.addMessageListener(listenerAdapter, new PatternTopic(msgToAll));
    container.addMessageListener(listenerAdapter, new PatternTopic(userStatus));
    log.info("Subscribe Redis channel : " + msgToAll);
    return container;
  }
}
