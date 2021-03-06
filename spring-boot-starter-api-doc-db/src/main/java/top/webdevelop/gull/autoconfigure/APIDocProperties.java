package top.webdevelop.gull.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by xumingming on 2018/3/24.
 */
@Data
@ConfigurationProperties("api.doc")
public class APIDocProperties {
    private boolean auto;
    private String projectName;
    private String webRootPackage = "";
    private String includeBeanNames;
    private String includeMethodNames;
    private String excludeBeanNames;
    private String excludeMethodNames;

    public boolean hasIncludeBean(String beanName) {
        return StringUtils.isEmpty(includeMethodNames) || !contains(includeMethodNames, beanName);
    }

    public boolean hasIncludeMethod(String methodName) {
        return StringUtils.isEmpty(includeMethodNames) || !contains(includeMethodNames, methodName);
    }

    public boolean hasExcludeBean(String beanName) {
        return StringUtils.hasText(excludeBeanNames) && contains(excludeBeanNames, beanName);
    }

    public boolean hasExcludeMethod(String methodName) {
        return StringUtils.hasText(excludeMethodNames) && contains(excludeMethodNames, methodName);
    }

    private boolean contains(String names, String name) {
        List<String> strings = Arrays.asList(names.split("[,;]"));
        return strings.contains(name) || strings.stream().anyMatch(pattern -> Pattern.matches(pattern, name));
    }
}
