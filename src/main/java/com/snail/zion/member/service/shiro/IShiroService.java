package com.snail.zion.member.service.shiro;

import com.snail.zion.member.service.shiro.domain.ShiroResponse;
import com.snail.zion.member.web.domain.User;

import io.reactivex.Flowable;

public interface IShiroService {
	
	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	Flowable<ShiroResponse> login(User user);
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 */
	Flowable<ShiroResponse> modify(User user);
	
	/**
	 * 修改用户密码
	 * @param user
	 * @return
	 */
	Flowable<ShiroResponse> modifyPassword(User user);
	
	/**
	 * 新增用户信息
	 * @param user
	 * @return
	 */
	Flowable<ShiroResponse> add(User user);
	
	
	/**
	 * 删除用户信息
	 * @param user
	 * @return
	 */
	Flowable<ShiroResponse> delete(User user);
	
	/**
	 * 获取角色列表
	 * @param user
	 * @return
	 */
	Flowable<ShiroResponse> roleList(User user);
	
	/**
	 * 查询用户信息
	 * @param user
	 * @return
	 */
	Flowable<ShiroResponse> userList(User user);
	
	
}
