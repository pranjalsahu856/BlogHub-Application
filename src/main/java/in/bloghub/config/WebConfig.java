package in.bloghub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import in.bloghub.interceptor.SessionAuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private SessionAuthInterceptor sessionAuthInterceptor;

	public WebConfig(SessionAuthInterceptor sessionAuthInterceptor) {
		this.sessionAuthInterceptor = sessionAuthInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionAuthInterceptor)
		.addPathPatterns("/api/**")
		.excludePathPatterns("/api/auth/**","/error", "/favicon.ico");
	}

}
