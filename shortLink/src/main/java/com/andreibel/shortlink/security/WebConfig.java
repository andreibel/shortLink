package com.andreibel.shortlink.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring configuration class for customizing web MVC settings.
 * <p>
 * Configures Cross-Origin Resource Sharing (CORS) to allow requests from the specified frontend URL.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * The URL of the frontend application, injected from the application properties.
     * <p>
     * Note: The property 'frontend.url' should be defined in 'application.properties' or 'application.yml'.
     */
    @Value("${frontend.url}")
    String frontEndUrl;

    /**
     * Configures CORS mappings for the application.
     * <p>
     * Allows requests from the specified frontend URL with specified HTTP methods and headers.
     *
     * @param registry the CORS registry to configure
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontEndUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}