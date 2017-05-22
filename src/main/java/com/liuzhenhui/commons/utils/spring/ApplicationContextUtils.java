package com.liuzhenhui.commons.utils.spring;

import org.springframework.context.ApplicationContext;

/**
 * @author zhenhui.liu
 * 2017年5月11日 下午2:41:58
 */
public class ApplicationContextUtils {
	private static ApplicationContext applicationContext;

	/**
	 * @author zhenhui.liu
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationContextUtils.applicationContext = applicationContext;
	}

}
