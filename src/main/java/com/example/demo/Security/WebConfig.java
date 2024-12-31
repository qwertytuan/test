// WebConfig.java
package com.example.demo.Security;

import com.example.demo.Security.Filter.CaptchaFilter;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/uploads/**")
                .addResourceLocations("file:static/uploads/");
    }

    @Bean
    public Filter captchaFilter() {
        return new CaptchaFilter();
    }
}