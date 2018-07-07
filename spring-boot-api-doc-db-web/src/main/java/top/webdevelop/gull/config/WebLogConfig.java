package top.webdevelop.gull.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by xumingming on 2018/6/13.
 */
@Aspect
@Configuration
public class WebLogConfig {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("execution(* top.webdevelop.gull.controller*..*(..))")
    public void execute() {
    }

    @Around("execute()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        StringBuilder sb = new StringBuilder();
        sb.append("\n--------------------------------------------------------------------------------");
        sb.append("\nClass        : ").append(pjp.getTarget().getClass().getName())
                .append(".(").append(pjp.getTarget().getClass().getSimpleName()).append(".java:1)");
        sb.append("\nMethod       : ").append(pjp.getSignature().getName());
        sb.append("\nURL          : ").append(request.getRequestURI()).append(Optional.ofNullable(request.getQueryString()).map(x -> "?" + x).orElse(""));
        sb.append("\nParameter    : ").append(Arrays.stream(pjp.getArgs())
                .filter(x -> !(x instanceof HttpServletRequest) && !(x instanceof HttpServletResponse))
                .map(this::toJsonString)
                .collect(Collectors.joining(";\n")));
        long start = System.currentTimeMillis();

        Object result = pjp.proceed();
//        sb.append("\nResponse     : ").append(JSON.toJSONString(result));
        sb.append("\nUse Time     : ").append(System.currentTimeMillis() - start).append("ms");
        sb.append("\n--------------------------------------------------------------------------------\n");
        LOGGER.debug(sb.toString());

        return result;
    }

    public String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
