package top.webdevelop.gull.apidoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringValueResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import top.webdevelop.gull.annotation.APIDocIgnore;
import top.webdevelop.gull.autoconfigure.APIDocProperties;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

/**
 * Created by xumingming on 2018/3/23.
 */

public class APIDocSpringHandlerMethodMapping implements EmbeddedValueResolverAware {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SCOPED_TARGET_NAME_PREFIX = "scopedTarget.";

    private APIDocProperties apiDocProperties;
    private ApplicationContext applicationContext;
    private StringValueResolver embeddedValueResolver;
    private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();
    private DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private APIDocProject apiDocProject;
    private APIDocMetadataGenerator generator;

    public APIDocSpringHandlerMethodMapping(APIDocProperties apiDocProperties, ApplicationContext applicationContext) {
        this.apiDocProperties = apiDocProperties;
        this.applicationContext = applicationContext;
        this.apiDocProject = new APIDocProject(apiDocProperties.getProjectName());
        this.generator = new APIDocMetadataGenerator();
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }

    public void handler() {
        long start = System.currentTimeMillis();

        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);

        for (String beanName : beanNames) {

            if (!beanName.startsWith(SCOPED_TARGET_NAME_PREFIX)) {
                Class<?> beanType = null;
                try {
                    beanType = applicationContext.getType(beanName);
                } catch (Throwable ex) {
                    // An unresolvable bean type, probably from a lazy bean - let's ignore it.
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
                    }
                }
                if (beanType != null && isHandler(beanType)) {
                    detectHandlerMethods(beanName);
                }
            }
        }

        generator.generate(this.apiDocProject);
        logger.info("generate api doc finish !!!, use time: {}", System.currentTimeMillis() - start);
    }

    protected void detectHandlerMethods(final Object handler) {
        Class<?> handlerType = (handler instanceof String ?
                applicationContext.getType((String) handler) : handler.getClass());

        if (handlerType != null) {
            final Class<?> userType = ClassUtils.getUserClass(handlerType);
            Map<Method, RequestMappingInfo> methods = MethodIntrospector.selectMethods(userType,
                    (MethodIntrospector.MetadataLookup<RequestMappingInfo>) method -> {
                        try {
                            return getMappingForMethod(method, userType);
                        } catch (Throwable ex) {
                            throw new IllegalStateException("Invalid mapping on handler class [" +
                                    userType.getName() + "]: " + method, ex);
                        }
                    });

            logger.debug("{} request handler methods found on {}: {}", methods.size(), userType, methods);


            for (Map.Entry<Method, RequestMappingInfo> entry : methods.entrySet()) {
                Method invocableMethod = AopUtils.selectInvocableMethod(entry.getKey(), userType);
                RequestMappingInfo mapping = entry.getValue();
                RequestMappingInfo typeInfo = createRequestMappingInfo(userType);

                if (handler.equals("basicErrorController")
                        || !apiDocProperties.hasIncludeBean(handler.toString())
                        || !apiDocProperties.hasIncludeMethod(invocableMethod.getName())
                        || apiDocProperties.hasExcludeBean(handler.toString())
                        || apiDocProperties.hasExcludeMethod(invocableMethod.getName())
                        || Optional.ofNullable(userType.getAnnotation(APIDocIgnore.class)).map(APIDocIgnore::value).orElse(false)
                        || Optional.ofNullable(invocableMethod.getAnnotation(APIDocIgnore.class)).map(APIDocIgnore::value).orElse(false)) {
                    continue;
                }

                logger.info("APIDoc detect RequestMappingInfo: {}", mapping);
                logger.info("APIDoc detect Method {}", invocableMethod);

                APIDoc apiDoc = APIDocBuilder.newInstance().setParameterNameDiscoverer(parameterNameDiscoverer).setRequestMappingInfo(mapping).setMethod(invocableMethod).build();
                APIDocMetadataPath path = APIDocMetadataPath.newBuilder().setApiDocProperties(apiDocProperties).setMethod(invocableMethod).build();
                buildAPIDocMenu(typeInfo, path, apiDoc);
            }
        }
    }

    private void buildAPIDocMenu(RequestMappingInfo typeInfo, APIDocMetadataPath path, APIDoc apiDoc) {
        String[] splitPackage = path.getRelativePackage().split("\\.");
        String tabMapping = splitPackage.length > 1 ? splitPackage[1] : "default";
        String pageMapping = Optional.ofNullable(typeInfo)
                .map(m -> APIDocBuilder.newInstance().setRequestMappingInfo(m).parseAPIDocUrl())
                .filter(x -> x.length() > 0)
                .orElse("default");
        String menuDesc = Optional.of(apiDoc.getUrl().replace(pageMapping, ""))
                .filter(x -> x.length() > 0)
                .orElse("/");

        APIDocMenu tab = this.apiDocProject.getMenus().stream().filter(i -> i.getMapping().equals(tabMapping)).findFirst().orElse(new APIDocMenu(tabMapping));
        APIDocMenu page = tab.getChilds().stream().filter(i -> i.getMapping().equals(pageMapping)).findFirst().orElse(new APIDocMenu(pageMapping));
        page.addChild(new APIDocMenu(apiDoc, menuDesc));
        tab.addChild(page);
        this.apiDocProject.addMenu(tab);
    }

    private RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = createRequestMappingInfo(method);
        if (info != null) {
            RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
            if (typeInfo != null) {
                info = typeInfo.combine(info);
            }
        }
        return info;
    }

    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        RequestCondition<?> condition = (element instanceof Class ?
                getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
        return (requestMapping != null ? createRequestMappingInfo(requestMapping, condition) : null);
    }

    protected RequestMappingInfo createRequestMappingInfo(
            RequestMapping requestMapping, RequestCondition<?> customCondition) {

        RequestMappingInfo.Builder builder = RequestMappingInfo
                .paths(resolveEmbeddedValuesInPatterns(requestMapping.path()))
                .methods(requestMapping.method())
                .params(requestMapping.params())
                .headers(requestMapping.headers())
                .consumes(requestMapping.consumes())
                .produces(requestMapping.produces())
                .mappingName(requestMapping.name());
        if (customCondition != null) {
            builder.customCondition(customCondition);
        }
        return builder.options(this.config).build();
    }

    protected String[] resolveEmbeddedValuesInPatterns(String[] patterns) {
        if (this.embeddedValueResolver == null) {
            return patterns;
        } else {
            String[] resolvedPatterns = new String[patterns.length];
            for (int i = 0; i < patterns.length; i++) {
                resolvedPatterns[i] = this.embeddedValueResolver.resolveStringValue(patterns[i]);
            }
            return resolvedPatterns;
        }
    }

    private RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        return null;
    }

    private RequestCondition<?> getCustomMethodCondition(Method method) {
        return null;
    }

    private boolean isHandler(Class<?> beanType) {
        return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class) ||
                AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class));
    }
}
