package com.snail.zion.member.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.snail.zion.member.web.annotation.ControlModelConstraint;
import com.snail.zion.member.web.annotation.EyestyleConstraint;

/**
 * 控制模式参数验证器
 * 陀螺仪 SENSOR = 0 
 * 触控 TOUCH = 1 
 * 两者 BOTH = 2
 * 
 * 
 * 
 * @author vergil
 *
 */
public class ControlModelConstraintValidator implements ConstraintValidator<ControlModelConstraint, Integer> {

	@Override
	public void initialize(ControlModelConstraint constraintAnnotation) {

	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (value == null || value == 0 || value == 1
				|| value == 2) {
			return true;
		}
		return false;
	}

}
