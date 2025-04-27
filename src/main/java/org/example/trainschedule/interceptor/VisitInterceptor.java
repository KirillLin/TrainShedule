package org.example.trainschedule.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.trainschedule.service.VisitCounterService;
import org.springframework.web.servlet.HandlerInterceptor;

public class VisitInterceptor implements HandlerInterceptor {
    private final VisitCounterService visitCounterService;

    public VisitInterceptor(VisitCounterService visitCounterService) {
        this.visitCounterService = visitCounterService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String url = request.getRequestURI();
        visitCounterService.incrementCount(url);
        return true;
    }
}
