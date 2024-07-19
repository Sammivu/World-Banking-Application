package com.example.WorldBankingApplication.infrastructure.config;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

//To return request and response
@Component
public class JwtAuthenticationEntrypoint implements AuthenticationEntryPoint {

    @Override
    public void commence (HttpServletRequest request, HttpServletResponse response,
                          AuthenticationException authException) throws IOException {
       // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        PrintWriter writer= response.getWriter();
        writer.println("{\"error\":\"Unauthorized user\"}");
    }
}
