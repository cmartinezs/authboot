package io.cmartinezs.authboot.infra.config;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafTemplateConfig {
  @Bean
  public TemplateEngine emailTemplateEngine() {
    final var templateEngine = new SpringTemplateEngine();
    templateEngine.addTemplateResolver(emailTemplateResolver());
    return templateEngine;
  }

  private ITemplateResolver emailTemplateResolver() {
    final var templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
    templateResolver.setPrefix("templates/mail/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    templateResolver.setCacheable(false);
    return templateResolver;
  }
}
