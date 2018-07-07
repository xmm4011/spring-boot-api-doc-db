package top.webdevelop.gull.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import top.webdevelop.gull.apidoc.APIDocDao;
import top.webdevelop.gull.apidoc.APIDocService;

/**
 * Created by xumingming on 2018/6/13.
 */
@Configuration
public class APIDocConfig {
    @Bean
    public APIDocDao apiDocDao(JdbcTemplate jdbcTemplate) {
        return new APIDocDao(jdbcTemplate);
    }

    @Bean
    public APIDocService apiDocService(APIDocDao apiDocDao) {
        return new APIDocService(apiDocDao);
    }
}
