package top.webdevelop.gull.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by xumingming on 2018/6/4.
 */
@Data
@ConfigurationProperties("api.doc.datasource")
public class APIDocDataSourceProperties {
    private String url;

    private String driverClassName;

    private String username;

    private String password;
}
