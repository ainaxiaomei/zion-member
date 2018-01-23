package com.snail.zion.member.web.validator;

/**
 * lms验证器，是一种组合验证器
 * 使用javax标准的注解来验证bean，但不做为空校验
 * 使用自己的代码来做为空校验
 * 之所以不使用javax来来做bean的为空校验，因为新增bean保证字段不为空但是修改bean可以为空
 * 查询和删除不做统一参数校验
 * @author vergil
 *
 */
public interface Validator {

}
