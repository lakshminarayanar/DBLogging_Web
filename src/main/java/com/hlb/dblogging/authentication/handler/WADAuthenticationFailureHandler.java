package com.hlb.dblogging.authentication.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.DefaultRedirectStrategy;

public class WADAuthenticationFailureHandler  extends DefaultRedirectStrategy {

	public void sendRedirect(HttpServletRequest request,
            HttpServletResponse response, String url) throws IOException {
        String urlWithOriginalQueryString = "/failure.jsf";
        super.sendRedirect(request, response, urlWithOriginalQueryString );
    }
}
