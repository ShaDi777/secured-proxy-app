package ru.shadi777.proxyapplication.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Component
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private final List<HandlerInterceptor> interceptors;

    @Autowired
    public WebConfiguration(List<HandlerInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        for (var interceptor : interceptors) {
            registry.addInterceptor(interceptor);
        }
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
