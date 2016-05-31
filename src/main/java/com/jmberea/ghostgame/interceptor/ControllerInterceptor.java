package com.jmberea.ghostgame.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class ControllerInterceptor extends HandlerInterceptorAdapter {

	private Logger logger = LogManager.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HandlerMethod method = (HandlerMethod) handler;
		logger.debug("PATH: " + request.getServletPath() + " - METHOD NAME: " + method.getMethod().getName()
				+ " - METHOD: " + request.getMethod());
		return true;
	}

}
