package top.webdevelop.gull.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.webdevelop.gull.apidoc.APIDocSpringHandlerMethodMapping;

/**
 * Created by xumingming on 2018/4/8.
 */
@Configuration
@ConditionalOnProperty(
        prefix = "api.doc",
        name = {"auto"},
        havingValue = "true"
)
@EnableConfigurationProperties({APIDocDataSourceProperties.class, APIDocProperties.class})
public class APIDocAutoConfiguration {
    @Bean
    public APIDocJdbcTemplate apiDocJdbcTemplate(APIDocDataSourceProperties apiDocDataSourceProperties) {
        return new APIDocJdbcTemplate(apiDocDataSourceProperties);
    }

    @Bean
    public APIDocSpringHandlerMethodMapping apiDocSpringHandlerMethodMapping(APIDocProperties apiDocProperties, ApplicationContext applicationContext) {
        return new APIDocSpringHandlerMethodMapping(apiDocProperties, applicationContext);
    }
}
