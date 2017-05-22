package com.liuzhenhui.commons.utils.springmvc.annotation;

/**
 * @Description: 校验data中参数是否合法
 * @see: ParamDataValidate 此处填写需要参考的类
 * @version 2016年8月30日 上午9:40:12
 * @author qinji.xu
 */
public @interface ParamDataValidate {

	String name(); // 参数名称
	boolean required() default false; // 是否校验必填项
	String regex() default ""; // 是否需要正则
	String errorMsg() default ""; // 正则提示错误信息
}
