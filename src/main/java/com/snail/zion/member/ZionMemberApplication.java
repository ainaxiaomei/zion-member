package com.snail.zion.member;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Validator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.rx.rxjava2.RxFlowableInvokerProvider;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.snail.zion.member.service.snailcloud.SnailCloudConfig;
import com.snail.zion.member.web.filter.LmsValidateFilter;
import com.snail.zion.member.web.login.LMSUserRestService;

import io.swagger.jaxrs.config.BeanConfig;

@SpringBootApplication
@ComponentScan(basePackages = "com.snail.lms")
@EnableRedisHttpSession // 注册SessionSessionRepositoryFilter的bean
@EnableTransactionManagement
@EnableEurekaClient
public class ZionMemberApplication {
    
	public static void main(String[] args) {
		SpringApplication.run(ZionMemberApplication.class, args);
	}

	@Bean
	@ConfigurationProperties("lms.snailcloud")
	public SnailCloudConfig snailCloudConfig() {
		SnailCloudConfig config = new SnailCloudConfig();
		return config;

	}

	/**
	 * 注册jsr301\jsr349 验证器
	 * 
	 * @return
	 */
	@Bean
	public Validator validator() {
		return new LocalValidatorFactoryBean();

	}

	@Bean
	public Client jerseyClient() {
		Client client = ClientBuilder.newClient();
		client.register(RxFlowableInvokerProvider.class);
		return client;

	}
	
	/**
	 * 
	 * ResourceConfig必须存在spring容器中否则不会 触发spring jersey autoconfig
	 */
	@Bean
	public ResourceConfig resourceConfig() {
		ResourceConfig r = new ResourceConfig();

		Map<String, Object> map = new HashMap<>();
		map.put("jersey.config.server.provider.scanning.recursive", true);
		r.addProperties(map);
		Boolean str = (Boolean) r.getProperty("jersey.config.server.provider.scanning.recursive");
		System.out.println(str);
		// 注册包扫描
		// r.packages("com.snail.lms");//打包运行报错

		r.register(LMSUserRestService.class);

		// 注册文件上传
		r.register(MultiPartFeature.class);

		// 注册过滤器
		r.register(RolesAllowedDynamicFeature.class);
        
		//注冊swagger
		r.register(io.swagger.jaxrs.listing.ApiListingResource.class);
		r.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0.2");
		beanConfig.setSchemes(new String[] { "http" });
		beanConfig.setHost("localhost:8080");
		beanConfig.setBasePath("/");
		beanConfig.setResourcePackage("com.snail.lms.web");
		beanConfig.setScan(true);

		// 注册统一验证
		return r.register(LmsValidateFilter.class);

	}

}
