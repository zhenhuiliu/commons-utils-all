package com.liuzhenhui.commons.utils.springmvc.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.liuzhenhui.commons.utils.constant.RetCodeConstants;
import com.liuzhenhui.commons.utils.constant.ReturnBean;
import com.liuzhenhui.commons.utils.springmvc.annotation.ParamValidate;
import com.liuzhenhui.commons.utils.springmvc.annotation.ParamsValidate;
import com.liuzhenhui.commons.utils.string.StringUtils;
/**
 * @Description: 公共校验拦截器，验证请求参数为空性和合法性
 * @see: ValidateInterceptor 此处填写需要参考的类
 * @version 2016年8月30日 上午9:35:34
 * @author qinji.xu
 */
public class ValidateInterceptor extends HandlerInterceptorAdapter {

	private static Logger logger = LoggerFactory.getLogger(ValidateInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain; charset=UTF-8");
		HandlerMethod handlerMethod = null;
		try {
			handlerMethod = (HandlerMethod) handler;
		} catch (Exception e) {
			logger.error("handler-------------------------------:{}", e);
		}
		if (handlerMethod == null) { // 如果请求地址错误则返回404的地址错误
			return super.preHandle(request, response, handler);
		}
		ReturnBean<Object> returnBean = new ReturnBean<Object>();
		Method method = handlerMethod.getMethod();
		ParamsValidate annotation = method.getAnnotation(ParamsValidate.class);
		if (annotation == null) {// 没有注解,直接跳出拦截器
			return super.preHandle(request, response, handler);
		}

		ParamValidate[] paramAttr = annotation.value(); // 获取需校验的参数及提示信息

		if (paramAttr != null) {
			for (ParamValidate paramValidate : paramAttr) {
				String name = paramValidate.name(); // 参数名称
				String errorMsg = paramValidate.errorMsg(); // 正则提示错误信息
				String value = request.getParameter(name); // 参数值
				if (StringUtils.isNotBlank(value)) {
					String regex = paramValidate.regex();
					if (!"".equals(regex)) {// 正则校验
						if (!value.matches(regex)) {
							returnBean.setCode(RetCodeConstants.ERROR);
							returnBean.setMsg(errorMsg);
							response.getWriter().println(JSON.toJSONString(returnBean));
							setCrosResponse(response);
							return false;
						}
					}
				} else if (paramValidate.required()) { // 非空校验
					returnBean.setCode(RetCodeConstants.ERROR);
					returnBean.setMsg("参数: " + name + " 不能为空");
					response.getWriter().println(JSON.toJSONString(returnBean));
					setCrosResponse(response);
					return false;
				}
			}
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}
	
	private void setCrosResponse(HttpServletResponse response){
      response.setHeader("Access-Control-Allow-Origin", "*");
      response.setHeader("Access-Control-Allow-Methods", "POST, GET");
	}
}
