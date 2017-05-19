package com.liuzhenhui.commons.utils.spring;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

/**
 * 通用URL转向jsp处理
 * @author zhenhui.liu
 * 2017年5月11日 下午4:26:07
 */
public class SimpleForwardHttpRequestHandler extends DefaultServletHttpRequestHandler {
	private static final Logger logger = LoggerFactory.getLogger(SimpleForwardHttpRequestHandler.class);

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler#handleRequest(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getServletPath().endsWith(".htm")) {
			String forwardURL = "/WEB-INF/jsp" + request.getServletPath().substring(0, request.getServletPath().length() - 4) + ".jsp";
			logger.debug("forward to {}", forwardURL);
			request.getRequestDispatcher(forwardURL).forward(request, response);
		}
	}

}
