package com.yen.mdblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BlogApplication {
  public static void main(String[] args) {

    SpringApplication.run(BlogApplication.class, args);
  }
}
