// CaptchaFilter.java
package com.example.demo.Security.Filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


import java.io.IOException;

public class CaptchaFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        session.setAttribute("captcha", "5"); // Simple CAPTCHA value for demonstration
        chain.doFilter(request, response);
    }
}