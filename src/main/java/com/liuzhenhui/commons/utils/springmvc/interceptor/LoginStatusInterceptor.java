/**
 *
 */
package com.liuzhenhui.commons.utils.springmvc.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Constants;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.liuzhenhui.commons.utils.constant.RetCodeConstants;
import com.liuzhenhui.commons.utils.constant.ReturnBean;
import com.liuzhenhui.commons.utils.string.StringUtils;

/**
 * 登录验证拦截器
 *
 * @Description: 这里用一句话描述这个类的作用
 * @see: LoginStatusInterceptor 此处填写需要参考的类
 * @version 2015年12月25日 下午2:19:37
 * @author dong.lian
 */
public class LoginStatusInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(LoginStatusInterceptor.class);
	

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		// 用户登录时分配的token
		String loginKey = request.getHeader("loginKey");
		if (StringUtils.isBlank(loginKey)) {
			// 从请求参数中获取
			loginKey = request.getParameter("loginKey");
		}
		logger.info("interceptor LoginStatusInterceptor loginKey = {}", loginKey);
		if (StringUtils.isBlank(loginKey)) {
			response.getWriter().write(JSON.toJSONString(new ReturnBean<Object>(RetCodeConstants.LOGIN_FAIL,RetCodeConstants.LOGIN_FAIL_DESC)));
			return false;
		}
		
		// 用户登录的userName
		String userName = request.getHeader("userName");
		if (StringUtils.isBlank(userName)) {
			// 从请求参数中获取
			userName = request.getParameter("userName");
		}
		try {
			//校验逻辑
//			boolean flag = operatorCoreFacade.checkLoginStatus(userName,loginKey);
		} catch (Exception e) {
			logger.error("登录失败",e);
			response.getWriter().write(JSON.toJSONString(new ReturnBean<Object>(RetCodeConstants.LOGIN_FAIL, RetCodeConstants.LOGIN_FAIL_DESC)));
			return false;
		}
		request.setAttribute("loginKey", loginKey);
		logger.info("interceptor LoginStatusInterceptor token = {},dataSign success ", loginKey);
		return true;
	}
}
