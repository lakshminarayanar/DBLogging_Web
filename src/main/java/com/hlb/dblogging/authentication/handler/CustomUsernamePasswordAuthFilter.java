package com.hlb.dblogging.authentication.handler;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomUsernamePasswordAuthFilter extends	UsernamePasswordAuthenticationFilter  {
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain,
			Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, filterChain,
				authResult);
		String username = super.obtainUsername(request);
		// do something with username
		System.out.println("Authentication is successful..." + username);
	}

}