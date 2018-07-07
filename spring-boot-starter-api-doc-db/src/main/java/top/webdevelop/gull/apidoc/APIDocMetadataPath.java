package top.webdevelop.gull.apidoc;

import lombok.Getter;
import top.webdevelop.gull.autoconfigure.APIDocProperties;

import java.lang.reflect.Method;

/**
 * Created by xumingming on 2018/3/24.
 */
@Getter
public class APIDocMetadataPath {
    private String relativePackage;

    private APIDocMetadataPath(Builder builder) {
        this.relativePackage = builder.method.getDeclaringClass().getPackage().getName()
                .replace(builder.apiDocProperties.getWebRootPackage(), "");
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private APIDocProperties apiDocProperties;
        private Method method;

        public Builder setApiDocProperties(APIDocProperties apiDocProperties) {
            this.apiDocProperties = apiDocProperties;
            return this;
        }

        public Builder setMethod(Method method) {
            this.method = method;
            return this;
        }

        public APIDocMetadataPath build() {
            return new APIDocMetadataPath(this);
        }
    }
}
