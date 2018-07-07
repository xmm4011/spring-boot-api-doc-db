package top.webdevelop.gull.common;

/**
 * Created by xumingming on 2018/6/13.
 */
public enum Errors {
    SYS_ERROR(100001, "系统异常"),
    ARGUMENT_NOT_VALID_ERROR(100002, "参数异常"),
    DATA_EXPIRE_ERROR(100003, "数据过期、请刷新后重新操作");

    Errors(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
