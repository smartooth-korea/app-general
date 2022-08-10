package co.smartooth.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 작성일 : 2022-07-18
 * 작성자 : 정주현
 * 기능 : Resources Path Mapping
 * */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		
		registry.addResourceHandler("/**")
	        .addResourceLocations("classpath:/static/")
	        .setCachePeriod(60*60*24);
		
		registry.addResourceHandler("/web/user/**")
	        .addResourceLocations("classpath:/static/")
	        .setCachePeriod(60*60*24);
		
		registry.addResourceHandler("/main/**")
            .addResourceLocations("classpath:/static/")
          .setCachePeriod(60*60*24);

		registry.addResourceHandler("/login/**")
		.addResourceLocations("classpath:/static/")
          .setCachePeriod(60*60*24);
		
		registry.addResourceHandler("/app/user/**")
		.addResourceLocations("classpath:/static/")
          .setCachePeriod(60*60*24);

		registry.addResourceHandler("/app/user/signUp/**")
		.addResourceLocations("classpath:/static/")
		.setCachePeriod(60*60*24);
		
		registry.addResourceHandler("/test/user/**")
		.addResourceLocations("classpath:/static/")
          .setCachePeriod(60*60*24);
	
	}
	
	
}
