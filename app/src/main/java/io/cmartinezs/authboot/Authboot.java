package io.cmartinezs.authboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Carlos
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties
public class Authboot {
  public static void main(String[] args) {
    SpringApplication.run(Authboot.class, args);
  }
}
