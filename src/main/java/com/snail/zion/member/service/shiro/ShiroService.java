package com.snail.zion.member.service.shiro;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.rx.rxjava2.RxFlowableInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snail.zion.member.service.shiro.domain.ShiroResponse;
import com.snail.zion.member.web.domain.User;

import io.reactivex.Flowable;

@Service
public class ShiroService implements IShiroService {
	
	private static Logger logger = LoggerFactory.getLogger(ShiroService.class);
	
	@Autowired
	private ShiroConfig shiro;

	public ShiroConfig getShiro() {
		return shiro;
	}

	public void setShiro(ShiroConfig shiro) {
		this.shiro = shiro;
	}


	@Override
	public Flowable<ShiroResponse> login(User user) {
		
		WebTarget target = shiro.login();
		
		if(logger.isDebugEnabled()){
			logger.debug("Invoke Shiro {} {}",target.getUri().getHost(),target.getUri().getPath());
		}
		
		return target.queryParam("requestTime", System.currentTimeMillis())
		      .request()
		      .header("Accept-version", "1.0.0")
			  .rx(RxFlowableInvoker.class)
			  .post(Entity.entity(user, MediaType.APPLICATION_JSON),ShiroResponse.class);
	}

	@Override
	public Flowable<ShiroResponse> modifyPassword(User user) {
		
		WebTarget target = shiro.modifyPassword();
		
		if(logger.isDebugEnabled()){
			logger.debug("Invoke Shiro {} {}",target.getUri().getHost(),target.getUri().getPath());
		}
		
		return target.request()
		      .header("Accept-version", "1.0.0")
			  .rx(RxFlowableInvoker.class)
			  .post(Entity.entity(user, MediaType.APPLICATION_JSON),ShiroResponse.class);
	}

	@Override
	public Flowable<ShiroResponse> add(User user) {
		WebTarget target = shiro.add();
		
		if(logger.isDebugEnabled()){
			logger.debug("Invoke Shiro {} {}",target.getUri().getHost(),target.getUri().getPath());
		}
		
		return target.queryParam("requestTime", System.currentTimeMillis())
			  .request()
		      .header("Accept-version", "1.0.0")
			  .rx(RxFlowableInvoker.class)
			  .post(Entity.entity(user, MediaType.APPLICATION_JSON),ShiroResponse.class);
	}

	@Override
	public Flowable<ShiroResponse> delete(User user) {
		WebTarget target = shiro.delete();

		if (logger.isDebugEnabled()) {
			logger.debug("Invoke Shiro {} {}", target.getUri().getHost(), target.getUri().getPath());
		}

		return target.queryParam("requestTime", System.currentTimeMillis())
				.request()
				.header("Accept-version", "1.0.0")
				.rx(RxFlowableInvoker.class)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON), ShiroResponse.class);
	}

	@Override
	public Flowable<ShiroResponse> modify(User user) {
		WebTarget target = shiro.modify();

		if (logger.isDebugEnabled()) {
			logger.debug("Invoke Shiro {} {}", target.getUri().getHost(), target.getUri().getPath());
		}

		return target.queryParam("requestTime", System.currentTimeMillis())
				.request()
				.header("Accept-version", "1.0.0")
				.rx(RxFlowableInvoker.class)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON), ShiroResponse.class);
	}

	@Override
	public Flowable<ShiroResponse> roleList(User user) {
		
		WebTarget target = shiro.roleList();

		if (logger.isDebugEnabled()) {
			logger.debug("Invoke Shiro {} {}", target.getUri().getHost(), target.getUri().getPath());
		}

		return target.queryParam("requestTime", System.currentTimeMillis())
				.request()
				.header("Accept-version", "1.0.0")
				.rx(RxFlowableInvoker.class)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON), ShiroResponse.class);
	}

	@Override
	public Flowable<ShiroResponse> userList(User user) {

		WebTarget target = shiro.userList();

		if (logger.isDebugEnabled()) {
			logger.debug("Invoke Shiro {} {}", target.getUri().getHost(), target.getUri().getPath());
		}

		return target.queryParam("requestTime", System.currentTimeMillis())
				.request()
				.header("Accept-version", "1.0.0")
				.rx(RxFlowableInvoker.class)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON), ShiroResponse.class);
	}

}
