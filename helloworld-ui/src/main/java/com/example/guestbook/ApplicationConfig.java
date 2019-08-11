/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.guestbook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * Created by rayt on 5/1/17.
 */
@Configuration
@EnableRedisHttpSession
public class ApplicationConfig {
  @Value("#{systemEnvironment['HELLOWORLDSERVICE_PORT']}")
  private String helloworldServiceEndpoint;

  @Value("#{systemEnvironment['GUESTBOOKSERVICE_PORT']}")
  private String guestbookServiceEndpoint;

  @Value("#{systemEnvironment['REDIS_PORT']}")
  private String redisEndpoint;

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  HelloworldService helloworldService(RestTemplate restTemplate) {
    String endpoint = helloworldServiceEndpoint.trim().replace("tcp:", "http:") + "/hello";
    return new HelloworldService(restTemplate, endpoint);
  }

  @Bean
  GuestbookService guestbookService(RestTemplate restTemplate) {
    String endpoint = guestbookServiceEndpoint.trim().replace("tcp:", "http:") + "/api/messages";
    return new GuestbookService(restTemplate, endpoint);
  }

  @Bean
  @Order(value = 0)
  FilterRegistrationBean sessionRepositoryFilterRegistration(
      SessionRepositoryFilter filter) {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(
        new DelegatingFilterProxy(filter));
    filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));

    return filterRegistrationBean;
  }

  @Bean
  JedisConnectionFactory jedisConnectionFactory() throws URISyntaxException {
    URI uri = new URI(redisEndpoint);
    JedisConnectionFactory factory = new JedisConnectionFactory();
    factory.setHostName(uri.getHost());
    factory.setPort(uri.getPort());

    return factory;
  }

}
