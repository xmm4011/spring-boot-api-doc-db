package top.webdevelop.gull.autoconfigure;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by xumingming on 2018/6/5.
 */
public class APIDocJdbcTemplate {
    private static DataSource dataSource = null;
    private static JdbcTemplate jdbcTemplate = null;
    private static APIDocDataSourceProperties apiDocDataSourceProperties;

    public APIDocJdbcTemplate(APIDocDataSourceProperties apiDocDataSourceProperties) {
        APIDocJdbcTemplate.apiDocDataSourceProperties = apiDocDataSourceProperties;
    }

    public static DataSource dataSource() {
        if (dataSource == null) {
            dataSource = DataSourceBuilder.create()
                    .url(APIDocJdbcTemplate.apiDocDataSourceProperties.getUrl())
                    .username(APIDocJdbcTemplate.apiDocDataSourceProperties.getUsername())
                    .password(APIDocJdbcTemplate.apiDocDataSourceProperties.getPassword())
                    .driverClassName(APIDocJdbcTemplate.apiDocDataSourceProperties.getDriverClassName())
                    .build();
        }

        return dataSource;
    }


    public static JdbcTemplate jdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate(dataSource());
        }

        return jdbcTemplate;
    }
}
