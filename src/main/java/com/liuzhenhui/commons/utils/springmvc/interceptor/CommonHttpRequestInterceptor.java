package com.liuzhenhui.commons.utils.springmvc.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.liuzhenhui.commons.utils.constant.Constant;
import com.liuzhenhui.commons.utils.constant.RetCodeConstants;
import com.liuzhenhui.commons.utils.constant.ReturnBean;
import com.liuzhenhui.commons.utils.utils.CommUtils;

/**
 * @Description: 前置http请求拦截器
 * @see: CommonHttpRequestInterceptor 此处填写需要参考的类
 * @version 2016年8月29日 上午10:40:09
 * @author qinji.xu
 */
public class CommonHttpRequestInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(CommonHttpRequestInterceptor.class);

	/**
	 * 验证数字签名
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String key = Constant.MD5_SIGN_KEY;

		Map<String, Object> requestParam = getRequestMap(request);

		// 用户版本号
		String appVersion = request.getHeader("appVersion");
		if (null != appVersion) {
			// 从请求参数中获取
			appVersion = request.getParameter("appVersion");
		}
		//设备来源
		String deviceSource  = request.getParameter("deviceSource");
		logger.info("appVersion : {} ,deviceSource : {}",appVersion,deviceSource);
		String dataJson = "";
		dataJson = CommUtils.sortRequestParam(requestParam,true);
		String sign = request.getParameter("sign");
		String url = request.getRequestURL().toString();
		// base64资质文件上传
		boolean fileControl = url.contains("uploadQualificationImg");
		logger.info("interceptor CommonHttpRequestInterceptor requestUrl：url = {},requestParams ：dataJson = {}, sign = {}", url,fileControl?"base64文件上传不展示": dataJson,
				request.getParameter("sign"));

		if (!CommUtils.checkHmac(dataJson, sign, key)) {
			logger.info("interceptor CommonHttpRequestInterceptor url = {}, data = {}, sign = {}, dataSign failed", url, fileControl?"base64文件上传不展示": dataJson, sign);
			ReturnBean<Object> returnBean = new ReturnBean<Object>();
			returnBean.setCode(RetCodeConstants.FAIL);
			returnBean.setMsg("签名验证失败");
			response.getWriter().print(JSON.toJSONString(returnBean));
			setCrosResponse(response);
			return false;
		}

		logger.info("interceptor CommonHttpRequestInterceptor url = {}, data = {}, sign = {}, dataSign success", url, fileControl?"base64文件上传不展示": dataJson, sign);
		return true;
	}

	/**
	 * 组装request参数为map
	 */
	protected Map<String, Object> getRequestMap(HttpServletRequest request) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		Enumeration<String> paramNames = request.getParameterNames();

		for (Enumeration<String> e = paramNames; e.hasMoreElements();) {
			String thisName = e.nextElement().toString();
			String thisValue = request.getParameter(thisName);

			if("sign".equals(thisName)) {
				continue;
			} else {
				paramMap.put(thisName, thisValue);
			}
		}
		return paramMap;
	}
	private void setCrosResponse(HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
    	response.setHeader("Access-Control-Allow-Methods", "POST, GET");
	}
}
