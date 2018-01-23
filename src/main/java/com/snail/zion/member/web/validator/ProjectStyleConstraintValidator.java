package com.snail.zion.member.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.snail.zion.member.web.annotation.EyestyleConstraint;
import com.snail.zion.member.web.annotation.ProjectStyleConstraint;

/**
 * 显示模式参数验证器
 * 360_3D_Left_Right = 0 
 * 360_3D_Up_Down = 1
 * 360_2D = 2 
 * 180_3D_Left_Right = 3
 * 180_3D_Up_Down = 4 
 * 180_2D = 5 
 * 2D = 6
 * 
 * 
 * @author vergil
 *
 */
public class ProjectStyleConstraintValidator implements ConstraintValidator<ProjectStyleConstraint, Integer> {

	@Override
	public void initialize(ProjectStyleConstraint constraintAnnotation) {

	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (value == null || value == 0 || value == 1
				|| value == 2|| value == 3|| value == 4
				|| value == 5|| value == 6) {
			return true;
		}
		return false;
	}

}
