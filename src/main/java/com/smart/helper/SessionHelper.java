package com.smart.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {
	
	public void removeSession() {
		HttpSession session = ((HttpServletRequest) RequestContextHolder.getRequestAttributes()).getSession();
	    session.removeAttribute("message");
	}
	
	
}
