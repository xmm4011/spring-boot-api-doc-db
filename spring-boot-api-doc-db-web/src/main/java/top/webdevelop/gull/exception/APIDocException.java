package top.webdevelop.gull.exception;


import top.webdevelop.gull.common.Errors;

/**
 * Created by xumingming on 2018/6/13.
 */
public class APIDocException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Errors errors;
    private String detail;

    public APIDocException(Errors errors) {
        super(errors.getMessage());
        this.errors = errors;
        this.detail = errors.getMessage();
    }

    public APIDocException(Errors errors, String detail) {
        super(detail);
        this.errors = errors;
        this.detail = detail;
    }

    public APIDocException(Errors errors, Throwable cause) {
        super(cause);
        this.errors = errors;
        this.detail = errors.getMessage();
    }

    public Errors getErrors() {
        return errors;
    }

    public String getDetail() {
        return detail;
    }
}
