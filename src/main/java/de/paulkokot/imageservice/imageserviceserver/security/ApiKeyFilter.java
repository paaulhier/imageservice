package de.paulkokot.imageservice.imageserviceserver.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {
    private static final String apiKey = "XEmLhPRPVgtERmKKRcZvwBBfKSgDrdANnzlpeEKEAbNjFhVCKM";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getMethod().equals("GET") && !request.getRequestURI().startsWith("/meta")) {
            filterChain.doFilter(request, response);
            return;
        }
        String apiKeyHeader = request.getHeader("X-Api-Key");
        if (apiKeyHeader == null || !apiKeyHeader.equals(apiKey)) {
            response.setStatus(401);
            return;
        }

        filterChain.doFilter(request, response);
    }
}