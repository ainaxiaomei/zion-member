package com.snail.zion.member.web.filter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.message.MessageBodyWorkers;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.model.AnnotatedMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.snail.zion.member.web.annotation.Validate;
import com.snail.zion.member.web.domain.Error;
import com.snail.zion.member.web.domain.LmsWebResponse;
import com.snail.zion.member.web.validator.StandardValidateContext;
import com.snail.zion.member.web.validator.ValidateContext;
import com.snail.zion.member.web.validator.ValidateResult;

/**
 * 统一参数验证，用于验证实体参数
 * @author vergil
 *.
 */
@Provider
public class LmsValidateFilter implements ContainerRequestFilter {
	
	private static final Logger logger= LoggerFactory.getLogger(LmsValidateFilter.class);
	
	@Context
	private ResourceInfo resourceInfo;
	
	@Context
	private MessageBodyWorkers workers;
	
	@Autowired
	private ApplicationContext appContext;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
        final AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());
        
        boolean shouldValidate = false ;
        Class<?>[] pararmTypes = am.getParameterTypes();
        Map<Annotation,Class> validateTypes = new HashMap<>();
        
        Annotation[][] ans = am.getParameterAnnotations();
		for (int i = 0; i < ans.length; i++) {
			for (int j = 0; j < ans[i].length; j++) {
				if(ans[i][j].annotationType() == Validate.class) {
					shouldValidate = true;
					validateTypes.put(ans[i][j], pararmTypes[i]);
				}
			}
		}
        
        if(shouldValidate) {
        	logger.debug("start validate ");
        	validateTypes.entrySet().forEach((entry)->{
        		
        		Validate validate = (Validate) entry.getKey();
            	Class<?> validatorClass = validate.validator();
            	Class<?> entityClass = entry.getValue();
            	String method = validate.validateMethod();
            	
            	try {
            		
            		if(requestContext instanceof ContainerRequest) {
            			ContainerRequest containerRequest = (ContainerRequest)requestContext;
                    	containerRequest.bufferEntity();
                    	
                    	//指定需要校验的参数为null
                    	if(!containerRequest.hasEntity()) {
                    		LmsWebResponse entity = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"Paramter Error !",null);
            				
            				Error error = Error.instance()
            		    		     .source(Error.SOURCE_LMS)
            		    		     .message("Paramter Can Not Be Null !");
            		    		
            				entity.setError(error);
            				
            				requestContext.abortWith(Response
            	                    .status(Response.Status.INTERNAL_SERVER_ERROR)
            	                    .entity(entity)
            	                    .type(MediaType.APPLICATION_JSON)
            	                    .build());
            				return;
                    	}
                    	
                    	Object o  = containerRequest.readEntity(entityClass);
                    	
                    	ValidateContext context = new StandardValidateContext(o);
                    	Object obj = appContext.getBean(validatorClass);
                    	ValidateResult validateResult = (ValidateResult) validatorClass.getDeclaredMethod(method,ValidateContext.class).invoke(obj, context);
                    	
                    	if(!validateResult.isSuccess()) {
                    		//参数验证未通过
                    		LmsWebResponse entity = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"Paramter Error !",null);
            				
            				Error error = Error.instance()
            		    		     .source(Error.SOURCE_LMS)
            		    		     .message(validateResult.getMessages().toString());
            		    		
            				entity.setError(error);
            				
            				requestContext.abortWith(Response
            	                    .status(Response.Status.INTERNAL_SERVER_ERROR)
            	                    .entity(entity)
            	                    .type(MediaType.APPLICATION_JSON)
            	                    .build());
                    	}
                    	
            		}else {
            			throw new RuntimeException("ContainerRequestContext Is Not A Instance Of ContainerRequest");
            		}
            		
    				
    			} catch (Exception e) {
    				
    				logger.error("Paramter Validate Error : {}",e);
    				LmsWebResponse entity = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"Paramter Validate Error :" + e.getMessage(),null);
    				
    				Error error = Error.instance()
    		    		     .source(Error.SOURCE_LMS)
    		    		     .message("LMS Server Error :" + e.getMessage() == null ? e.getClass().toString() : e.getMessage());
    		    		
    				entity.setError(error);
    				
    				requestContext.abortWith(Response
    	                    .serverError()
    	                    .entity(entity)
    	                    .type(MediaType.APPLICATION_JSON)
    	                    .build());
    			}
        	});
        	
        	
        }
	}
	
//	ByteArrayOutputStream bao = new ByteArrayOutputStream();
//	IOUtils.copy(requestContext.getEntityStream(), bao);
//	
//	MessageBodyReader<RoomEntity> reader = workers.getMessageBodyReader(RoomEntity.class, null, null, requestContext.getMediaType());
//	Type[] t = resourceInfo.getResourceMethod().getGenericParameterTypes();
//	RoomEntity en = reader.readFrom(RoomEntity.class, t[0], null, requestContext.getMediaType(), 
//			requestContext.getHeaders(), requestContext.getEntityStream());
//	
//	requestContext.setEntityStream(new ByteArrayInputStream(bao.toByteArray()));
//	bao.close();
	
}
