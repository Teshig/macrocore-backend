package org.grimjo.macrocore.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // Apply CORS rules from application.yml to ALL endpoints (/**)
    registry.addMapping("/**")
        // Spring automatically pulls the properties (allowed-origins, allowed-methods, etc.)
        .allowedOriginPatterns("*")
        .allowedMethods("*")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}
