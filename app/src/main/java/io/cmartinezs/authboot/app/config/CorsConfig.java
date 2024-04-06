package io.cmartinezs.authboot.app.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  public static final String ALLOWED_ORIGIN_PATTERN = "*";
  public static final List<String> ALLOWED_METHODS =
          List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
  public static final List<String> ALLOWED_HEADERS = List.of("*");
  public static final boolean ALLOW_CREDENTIALS = true;

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", getCorsConfiguration());
    return source;
  }

  private static CorsConfiguration getCorsConfiguration() {
    var configuration = new CorsConfiguration();
    configuration.addAllowedOriginPattern(ALLOWED_ORIGIN_PATTERN);
    configuration.setAllowedMethods(ALLOWED_METHODS);
    configuration.setAllowedHeaders(ALLOWED_HEADERS);
    configuration.setAllowCredentials(ALLOW_CREDENTIALS);
    return configuration;
  }
}
