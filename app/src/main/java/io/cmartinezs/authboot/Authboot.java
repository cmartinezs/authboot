package io.cmartinezs.authboot;

import io.cmartinezs.authboot.infra.config.ThymeleafTemplateConfig;
import io.cmartinezs.authboot.infra.properties.EmailServiceProperties;
import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
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
    SpringApplication app = new SpringApplication(Authboot.class);
    String port = System.getenv("PORT");
    app.setDefaultProperties(Collections.singletonMap("server.port", port == null ? "8080" : port));
    app.run(args);
  }
}
