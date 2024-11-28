package com.yen.springChatRoom.controller;

import com.yen.springChatRoom.bean.OnlineUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

  private final String onlineUserKey = "websocket.onlineUsers";

  @Value("${redis.channel.msgToAll}")
  private String msgToAll;

  @Value("${redis.set.onlineUsers}")
  private String onlineUsers;

  @Value("${redis.channel.userStatus}")
  private String userStatus;

  @Autowired private RedisTemplate<String, String> redisTemplate;

  @GetMapping("/online_user")
  public List<String> getOnlineUser() {

    Set<String> resultSet = redisTemplate.opsForSet().members(onlineUserKey);
    log.info("(getOnlineUser) resultSet = " + resultSet);
    // TODO : optimize below
    OnlineUser onlineUser = new OnlineUser();
    List<String> users = new ArrayList<>();
    resultSet.forEach(
        x -> {
          users.add(x);
        });
    onlineUser.setUsers(users);
    return onlineUser.getUsers();
  }
}
