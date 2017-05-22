package com.liuzhenhui.commons.utils.springmvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 校验注解
 * @see: ParamsValidate 此处填写需要参考的类
 * @version 2016年8月30日 上午9:41:16
 * @author qinji.xu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ParamsValidate {
	ParamValidate[] value();
}
