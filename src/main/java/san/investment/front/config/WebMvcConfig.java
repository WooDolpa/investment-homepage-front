package san.investment.front.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * packageName : san.investment.front.config
 * className : WebMvcConfig
 * user : jwlee
 * date : 2025. 12. 28.
 * description :
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.save.url}")
    private String fileSaveUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded files at /uploads/** URL pattern
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + fileSaveUrl + "/");
    }
}
