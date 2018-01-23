package com.snail.zion.member.web.login;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.snail.zion.member.service.shiro.ShiroService;
import com.snail.zion.member.service.shiro.domain.ShiroResponse;
import com.snail.zion.member.web.annotation.LoginCheck;
import com.snail.zion.member.web.domain.Data;
import com.snail.zion.member.web.domain.Error;
import com.snail.zion.member.web.domain.LmsWebResponse;
import com.snail.zion.member.web.domain.User;

import io.reactivex.schedulers.Schedulers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * 用户登录注册服务,用户包含超级管理员,管理员,企业用户。
 * 主播不从lms登录
 
 * @author sun
 *
 */
@Controller
@Path("/v1/lms")
@Api(value = "用户相关服务")
public class LMSUserRestService {
	
	public static final String SESSION_KEY = "session";
	
	private static Logger logger = LoggerFactory.getLogger(LMSUserRestService.class);
	
	@Autowired
	private ShiroService shiro;
	
	@Context
	private HttpServletRequest req;
	
	/**
	 * @author vergil
	 * @param user 用户实体
	 * @param async jersey异步servlet
	 * @return json格式字符串
	 */
	@POST
	@Path("/user/login")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "用户登录" )
	public void login(User user,@Suspended final AsyncResponse async,@Context HttpServletRequest req){
		
		try {
			//登录
			shiro.login(user)
			    .observeOn(Schedulers.io())
			    .subscribe((res)->{
			    	logger.info("Shiro Response {}" , res);
			    	if(ShiroResponse.CODE_SUCCESS.equals(res.getCode())){
			    		
			    		//shiro返回成功
			    		LmsWebResponse  entity = new LmsWebResponse(LmsWebResponse.SUCCESS,"success");
			    		
			    		User record = new User();
			    		record.setRoleIds(res.getData().getRoles());
			    		Data data = new Data();
			    		data.setRecords(new ArrayList<User>(){{add(record);}});
			    		entity.setData(data);
						Response response = Response
								.ok()
								.entity(entity)
								.build();
						
					    //将用户信息保存到redis session中
						user.setRoleIds(res.getData().getRoles());
						user.setToken(res.getData().getToken());
						req.getSession().setAttribute("user", user);
						
						async.resume(response);
			    	}else{
			    		//shiro返回失败,业务错误
			    		LmsWebResponse  entity = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"Shiro Business Error :" + res.getMessage(),null);
			    		Error error = Error.instance()
			    		     .source(Error.SOURCE_SHIRO)
			    		     .code(res.getCode())
			    		     .message(res.getMessage());
			    		
			    		entity.setError(error);
			    		
			    		Response response = Response
								.serverError()
								.entity(entity)
								.build();
			    		async.resume(response);
			    	}
					
				
			},(e)->{
				
				//调用shiro出错,非业务错误
				logger.error("Invoke Shiro Fail:{}", e.getCause());
				LmsWebResponse  res = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"Shiro Fetal Error : " + e.getMessage(),null);
				Error error = Error.instance()
		    		     .source(Error.SOURCE_SHIRO)
		    		     .message(res.getMessage());
		    		
				res.setError(error);
				
				Response response = Response.serverError().entity(res).build();
				async.resume(response);
			});
			
			
		} catch (Exception e) {
			logger.error("Web Layer Error:{}", e);
		    LmsWebResponse  res = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,e.getMessage() == null ? e.getClass().toString() : e.getMessage(),null);
		   
		    Error error = Error.instance()
	    		     .source(Error.SOURCE_LMS)
	    		     .message(e.getMessage() == null ? e.getClass().toString() : e.getMessage());
	    		
			res.setError(error);
		    
		    Response response = Response.serverError().entity(res).build();
		    async.resume(response);
		}
		
		
	}
	
	/**
	 * @author vergil
	 * @param user 用户实体
	 * @param async jersey异步servlet
	 * @return json格式字符串
	 */
	@LoginCheck
	@PUT
	@Path("/user/modify")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "修改用户密码" )
	@ApiResponses(value = { @ApiResponse(code = 401, message = "用户未登录" ) })
	public void modify(User user,@Suspended final AsyncResponse async){
		
		try {
			//修改
			shiro.modifyPassword(user)
			    .observeOn(Schedulers.io())
			    .subscribe((res)->{
			    	logger.info("Shiro Response {}" , res);
			    	if(ShiroResponse.CODE_SUCCESS.equals(res.getCode())){
			    		
			    		//shiro返回成功
						LmsWebResponse  entity = new LmsWebResponse(LmsWebResponse.SUCCESS,"success",null);
						Response response = Response
								.ok()
								.entity(entity)
								.build();
						async.resume(response);
			    	}else{
			    		//shiro返回失败
			    		LmsWebResponse  entity = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"Shiro Business Error :" + res.getMessage(),null);
			    		Error error = Error.instance()
				    		     .source(Error.SOURCE_SHIRO)
				    		     .code(res.getCode())
				    		     .message(res.getMessage());
				    		
				        entity.setError(error);
				        
			    		Response response = Response
								.serverError()
								.entity(entity)
								.build();
			    		async.resume(response);
			    	}
					
				
			},(e)->{
				
				//调用shiro出错,非业务错误
				logger.error("Invoke Shiro Fail:{}", e);
				LmsWebResponse  res = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"Shiro Fetal Error :" + e.getMessage(),null);
				Error error = Error.instance()
		    		     .source(Error.SOURCE_SHIRO)
		    		     .message(res.getMessage());
		    		
				res.setError(error);
				
				Response response = Response.serverError().entity(res).build();
				async.resume(response);
			});
			
			
		} catch (Exception e) {
			logger.error("Web Layer Error:{}", e);
		    LmsWebResponse  res = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,e.getMessage() == null ? e.getClass().toString() : e.getMessage(),null);
		   
		    Error error = Error.instance()
	    		     .source(Error.SOURCE_LMS)
	    		     .message(e.getMessage() == null ? e.getClass().toString() : e.getMessage());
	    		
			res.setError(error);
		    
		    Response response = Response.serverError().entity(res).build();
		    async.resume(response);
		}
		
		
	}
	
	/**
	 * 修改用户信息
	 * @param user
	 * @param async
	 * @return
	 */
	@LoginCheck
	@PUT
	@Path("/user/info/modify")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "修改用户信息" )
	@ApiResponses(value = { @ApiResponse(code = 401, message = "用户未登录" ) })
	public void modifyInfo(User user,@Suspended final AsyncResponse async){
		
		try {
			//修改
			shiro.modify(user)
			.observeOn(Schedulers.io())
			.subscribe((res)->{
				logger.info("Shiro Response {}" , res);
				if(ShiroResponse.CODE_SUCCESS.equals(res.getCode())){
					
					//shiro返回成功
					LmsWebResponse  entity = new LmsWebResponse(LmsWebResponse.SUCCESS,"success",null);
					Response response = Response
							.ok()
							.entity(entity)
							.build();
					async.resume(response);
				}else{
					//shiro返回失败
					LmsWebResponse  entity = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"Shiro Business Error :" + res.getMessage(),null);
					Error error = Error.instance()
			    		     .source(Error.SOURCE_SHIRO)
			    		     .code(res.getCode())
			    		     .message(res.getMessage());
			    		
			    	entity.setError(error);
					
					Response response = Response
							.serverError()
							.entity(entity)
							.build();
					async.resume(response);
				}
				
				
			},(e)->{
				
				//调用shiro出错,非业务错误
				logger.error("Invoke Shiro Fail:{}", e);
				LmsWebResponse  res = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"Shiro Fetal Error :" + e.getMessage(),null);
				Error error = Error.instance()
		    		     .source(Error.SOURCE_SHIRO)
		    		     .message(res.getMessage());
		    		
				res.setError(error);
				
				Response response = Response.serverError().entity(res).build();
				async.resume(response);
			});
			
			
		} catch (Exception e) {
			logger.error("Web Layer Error:{}", e);
		    LmsWebResponse  res = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,e.getMessage() == null ? e.getClass().toString() : e.getMessage(),null);
		   
		    Error error = Error.instance()
	    		     .source(Error.SOURCE_LMS)
	    		     .message(e.getMessage() == null ? e.getClass().toString() : e.getMessage());
	    		
			res.setError(error);
		    
		    Response response = Response.serverError().entity(res).build();
		    async.resume(response);
		}
		
		
	}
	
	
	@POST
	@Path("/user/add")
	@LoginCheck
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "新增用户信息" )
	@ApiResponses(value = { @ApiResponse(code = 401, message = "用户未登录" ) })
	public void add(User user,@Suspended final AsyncResponse async){
		
		try {
			//新增
			shiro.add(user)
			.observeOn(Schedulers.io())
			.subscribe((res)->{
				logger.info("Shiro Response {}" , res);
				if(ShiroResponse.CODE_SUCCESS.equals(res.getCode())){
					
					//shiro返回成功
					LmsWebResponse  entity = new LmsWebResponse(LmsWebResponse.SUCCESS,"success",null);
					Response response = Response
							.ok()
							.entity(entity)
							.build();
					async.resume(response);
				}else{
					//shiro返回失败
					LmsWebResponse  entity = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"Shiro Business Error :" + res.getMessage(),null);
					Error error = Error.instance()
			    		     .source(Error.SOURCE_SHIRO)
			    		     .code(res.getCode())
			    		     .message(res.getMessage());
			    		
			        entity.setError(error);
					
					Response response = Response
							.serverError()
							.entity(entity)
							.build();
					async.resume(response);
				}
				
				
			},(e)->{
				
				//调用shiro出错,非业务错误
				logger.error("Invoke Shiro Fail:{}", e);
				LmsWebResponse  res = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"Shiro Fetal Error :" + e.getMessage(),null);
				Error error = Error.instance()
		    		     .source(Error.SOURCE_SHIRO)
		    		     .message(res.getMessage());
		    		
				res.setError(error);
				
				Response response = Response.serverError().entity(res).build();
				async.resume(response);
			});
			
			
		} catch (Exception e) {
			logger.error("Web Layer Error:{}", e);
		    LmsWebResponse  res = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,e.getMessage() == null ? e.getClass().toString() : e.getMessage(),null);
		   
		    Error error = Error.instance()
	    		     .source(Error.SOURCE_LMS)
	    		     .message(e.getMessage() == null ? e.getClass().toString() : e.getMessage());
	    		
			res.setError(error);
		    
		    Response response = Response.serverError().entity(res).build();
		    async.resume(response);
		}
		
		
	}
	
	@DELETE
	@Path("/user/delete/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	@LoginCheck
	@ApiOperation(value = "删除用户信息" )
	@ApiResponses(value = { @ApiResponse(code = 401, message = "用户未登录" ) })
	public void delete(@PathParam("username")String userName,@Suspended final AsyncResponse async,@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		String token = user.getToken();
		user = new User();
		user.setToken(token);  
		user.setUsername(userName);
		try {
			shiro.delete(user)
			.observeOn(Schedulers.io())
			.subscribe((res)->{
				logger.info("Shiro Response {}" , res);
				if(ShiroResponse.CODE_SUCCESS.equals(res.getCode())){
					
					//shiro返回成功
					LmsWebResponse  entity = new LmsWebResponse(LmsWebResponse.SUCCESS,"success",null);
					Response response = Response
							.ok()
							.entity(entity)
							.build();
					async.resume(response);
				}else{
					//shiro返回失败
					LmsWebResponse  entity = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"Shiro Business Error :" + res.getMessage(),null);
					Error error = Error.instance()
			    		     .source(Error.SOURCE_SHIRO)
			    		     .code(res.getCode())
			    		     .message(res.getMessage());
			    		
			        entity.setError(error);
					
					Response response = Response
							.serverError()
							.entity(entity)
							.build();
					async.resume(response);
				}
				
				
			},(e)->{
				
				//调用shiro出错,非业务错误
				logger.error("Invoke Shiro Fail :{}", e);
				LmsWebResponse  res = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,"Shiro Fetal Error :" + e.getMessage(),null);
				Error error = Error.instance()
		    		     .source(Error.SOURCE_SHIRO)
		    		     .message(res.getMessage());
		    		
				res.setError(error);
				
				
				Response response = Response.serverError().entity(res).build();
				async.resume(response);
			});
			
			
		} catch (Exception e) {
			logger.error("Web Layer Error:{}", e);
		    LmsWebResponse  res = new LmsWebResponse(LmsWebResponse.SERVER_ERROR,e.getMessage() == null ? e.getClass().toString() : e.getMessage(),null);
		   
		    Error error = Error.instance()
	    		     .source(Error.SOURCE_LMS)
	    		     .message(e.getMessage() == null ? e.getClass().toString() : e.getMessage());
	    		
			res.setError(error);
		    
		    Response response = Response.serverError().entity(res).build();
		    async.resume(response);
		}
		
		
	}
}
