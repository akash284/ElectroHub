package com.lcwa.electronic.store.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

// methods of this class will be invoked,when any user tried to access the protected apis and he is not authorized person

@Component
public class JwtAuthenticationEntryPoint  implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        // our code
        // ye code tab chalgea jab exceptions(Error)  aari ho

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // custom message bhejna hoto
        PrintWriter writer = response.getWriter();
        writer.print("Access denied!! "+ authException.getMessage());
    }
}
