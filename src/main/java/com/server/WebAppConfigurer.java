package com.server;
  
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ServletComponentScan
public class WebAppConfigurer 
        extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       
    }

   // @Bean
   /* public Connection druidDataSource() throws SQLException {
    	Connection db =openConnection(); 
        return db;
    }
    */
    
   /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }*/
    
    @Bean
    public RequestContextListener addRequestContextListener(){
    	RequestContextListener request=new RequestContextListener();
    	return request;
    }
    
   /* @Bean  
    public FilterRegistrationBean myFilter() {  
    +
        FilterRegistrationBean myFilter = new FilterRegistrationBean();  
        myFilter.addUrlPatterns("/*");  
        AuthFilter filter=new AuthFilter();
        filter.setJwtTokenUtil(WebApplicationContextUtils.);
        myFilter.setFilter(filter);  
        return myFilter;  
    }  */
   
 
}
