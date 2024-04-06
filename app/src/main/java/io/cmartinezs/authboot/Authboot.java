package io.cmartinezs.authboot;

import io.cmartinezs.authboot.infra.config.ThymeleafTemplateConfig;
import io.cmartinezs.authboot.infra.properties.email.EmailServiceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * @author Carlos
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties
@Import({EmailServiceProperties.class, ThymeleafTemplateConfig.class})
public class Authboot {
  public static void main(String[] args) {
    new SpringApplication(Authboot.class).run(args);
  }
}
