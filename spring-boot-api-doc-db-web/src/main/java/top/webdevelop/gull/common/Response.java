package top.webdevelop.gull.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by xumingming on 2018/6/13.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean success;
    private Integer code;
    private String message;
    private T data;

    public static <T> Response<T> newInstance() {
        return new Response<>();
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setErrors(Errors errors) {
        this.code = errors.getCode();
        this.message = errors.getMessage();
    }

    public void setErrors(Errors errors, String message) {
        this.code = errors.getCode();
        this.message = message;
    }
}