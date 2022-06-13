package com.yen.springBootAdvance2;

// https://www.youtube.com/watch?v=FqHO8tiUthQ&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=18

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SpringBootAdvance2ApplicationTests {

	@Autowired
	RabbitTemplate rabbitTemplate;

	/**
	 *   Send msg
	 *
	 *   1) p2p (point to point)
	 *
	 */
	@Test
	void contextLoads() {

		// we need to define our own message (can customize body, header)
		//rabbitTemplate.send(exchange, routeKey, message);

		// we only need to send msg object (object is default body), it will be serialized and sent automatically (to RabbitMQ)
		//rabbitTemplate.convertAndSend(exchange, routeKey, object);

		Map<String, Object> map = new HashMap<>();
		map.put("k1", "v1");
		map.put("data", Arrays.asList("hello", 123, true));

		// object is serialized (default method) and sent to RabbitMQ
		rabbitTemplate.convertAndSend("exchange.direct", "yen.news", map);
	}

	/**
	 *   Receive msg
	 *
	 */
	@Test
	public void receive(){

		Object o = rabbitTemplate.receiveAndConvert("com.yen");
		System.out.println(o.getClass());
		System.out.println(o);

	}

}
