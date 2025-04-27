package org.example.trainschedule.interceptor;

import org.example.trainschedule.service.VisitCounterService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final VisitCounterService visitCounterService;

    public WebConfig(VisitCounterService visitCounterService) {
        this.visitCounterService = visitCounterService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new VisitInterceptor(visitCounterService))
                .addPathPatterns("/api/**");
    }
}
