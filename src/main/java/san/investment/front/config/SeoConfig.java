package san.investment.front.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class SeoConfig {

    private String baseUrl;
    private String siteName;
    private String siteDescription;
}
