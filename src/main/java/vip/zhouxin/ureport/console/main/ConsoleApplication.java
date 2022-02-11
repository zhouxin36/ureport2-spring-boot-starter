package vip.zhouxin.ureport.console.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 测试application 正式不使用，不生效
 *
 * @author xinxingzhou
 * @since 2022/1/23
 */
@SpringBootApplication

public class ConsoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsoleApplication.class, args);
    }


    @Component
    public static class WebConfiguration implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/ureport/**").allowedOrigins("*");
        }
    }

}
