package com.snail.zion.member.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.snail.zion.member.web.annotation.EyestyleConstraint;

/**
 * 单双眼模式验证器，用于验证房间实体中的eyestyle字段
 * 不做为空校验
 * 
 * @author vergil
 *
 */
public class EyeStyleConstraintValidator implements ConstraintValidator<EyestyleConstraint, Integer> {

	@Override
	public void initialize(EyestyleConstraint constraintAnnotation) {

	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (value == null || value == 0 || value == 1) {
			return true;
		}
		return false;
	}

}
