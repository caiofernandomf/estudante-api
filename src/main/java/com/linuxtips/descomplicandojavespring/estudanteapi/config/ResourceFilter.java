package com.linuxtips.descomplicandojavespring.estudanteapi.config;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class ResourceFilter implements Filter {

    double startTime = 0;
    private final MeterRegistry meterRegistry;

    DistributionSummary summary;
    public ResourceFilter(MeterRegistry meterRegistry){
        this.meterRegistry=meterRegistry;

        this.summary =
                DistributionSummary
                .builder("http_request_durantion_seconds")
                .description("Duração do request em segundos") // optional
                .baseUnit("seconds") // optional (1)
                .publishPercentiles(0.5, 0.95) // median and 95th percentile (1)
                 // (2)
                 // optional
                .register(meterRegistry);

    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        startTime= System.nanoTime();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        chain.doFilter(request, response);

        double duration = (System.nanoTime() - startTime)/ TimeUnit.SECONDS.toNanos(1L);

        summary.record(duration);


    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
