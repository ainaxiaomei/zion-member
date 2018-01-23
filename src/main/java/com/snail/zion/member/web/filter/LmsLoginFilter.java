package com.snail.zion.member.web.filter;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

import com.alibaba.fastjson.JSONObject;
import com.snail.zion.member.web.annotation.LoginCheck;
import com.snail.zion.member.web.domain.Error;
import com.snail.zion.member.web.domain.LmsWebResponse;

/**
 * 登录校验过滤器，根据name-binding只过滤
 * 被注解@LoginCheck修饰的方法
 * 
 * @author sun
 *
 */
@Provider
@LoginCheck
public class LmsLoginFilter implements ContainerRequestFilter {

	@Context
	private HttpServletRequest request;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		try {
			
			HttpSession seession = request.getSession(false);
			if (seession == null ||seession.getAttribute("user") == null) {
				LmsWebResponse entity = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"User Not Login !",null);
				
				Error error = Error.instance()
		    		     .source(Error.SOURCE_LMS)
		    		     .message("User Not Login !");
		    		
				entity.setError(error);
				
				requestContext.abortWith(Response
	                    .status(Response.Status.UNAUTHORIZED)
	                    .entity(entity)
	                    .type(MediaType.APPLICATION_JSON)
	                    .build());
			}
			
		} catch (Exception e) {
			LmsWebResponse entity = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"Login Check Error :" + e.getMessage(),null);
			
			Error error = Error.instance()
	    		     .source(Error.SOURCE_LMS)
	    		     .message("Login Check Error :" + e.getMessage() == null ? e.getClass().toString() : e.getMessage());
	    		
			entity.setError(error);
			
			requestContext.abortWith(Response
                    .serverError()
                    .entity(entity)
                    .type(MediaType.APPLICATION_JSON)
                    .build());
		}
		
	}

}
