package com.snail.zion.member.web.filter;

import java.io.IOException;
import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;

/**
 * 权限校验过滤器
 * @author sun
 *
 */
public class LmsAuthorizationFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		requestContext.setSecurityContext(new SecurityContext() {
			
			@Override
			public boolean isUserInRole(String role) {
				return false;
			}
			
			@Override
			public boolean isSecure() {
				return false;
			}
			
			@Override
			public Principal getUserPrincipal() {
				return null;
			}
			
			@Override
			public String getAuthenticationScheme() {
				return null;
			}
		});
	}

}
