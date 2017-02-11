package com.grudus.examshelper.configuration.authenticated.filters;

import com.grudus.examshelper.configuration.authenticated.token.TokenAuthenticationService;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CorsFilter extends GenericFilterBean {


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        String origin = request.getHeader("Origin");

        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "withCredentials, " + TokenAuthenticationService.AUTH_HEADER_NAME);
        response.setHeader("Access-Control-Expose-Headers", TokenAuthenticationService.AUTH_HEADER_NAME);
        response.setHeader("withCredentials", "true");

        if (!request.getMethod().equals("OPTIONS"))
            chain.doFilter(request, response);
    }
}
