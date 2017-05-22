package com.liuzhenhui.commons.utils.springmvc.annotation;

/**
 * @Description: 校验注解，验证参数的属性
 * @see: ParamValidate 此处填写需要参考的类
 * @version 2016年8月30日 上午9:40:46
 * @author qinji.xu
 */
public @interface ParamValidate {

	/**注解校验，使用案例*/
//	@ParamsValidate(value = {
//			@ParamValidate(name = "phone", required = true, regex = Regx.PHONE, errorMsg = "手机号码格式错误"),
//			@ParamValidate(name = "name", required = true),
//			@ParamValidate(name = "money", required = true, regex =Regx.MONEY ,errorMsg = "金额格式错误")})

	String name(); // 参数名称

	boolean required() default false; // 是否校验必填项

	String regex() default ""; // 是否需要正则

	String errorMsg() default ""; // 正则提示错误信息

	ParamDataValidate[] data() default {}; //参数中data层校验
}
