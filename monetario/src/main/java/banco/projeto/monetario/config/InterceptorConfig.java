package banco.projeto.monetario.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import banco.projeto.monetario.interceptor.LoginInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{

    @Override
    public void addInterceptors(InterceptorRegistry ir){
        ir.addInterceptor(new LoginInterceptor())
            .addPathPatterns("/principal", "/conta")
            .excludePathPatterns("/login", "/cadastro");
    }
}
