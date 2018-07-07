package top.webdevelop.gull.common;

/**
 * Created by xumingming on 2018/6/13.
 */
public class ResponseGen {
    public static <T> Response<T> genSuccessResult() {
        Response<T> result = Response.newInstance();
        result.setSuccess(true);
        return result;
    }

    public static <T> Response<T> genSuccessResult(T data) {
        Response<T> result = genSuccessResult();
        result.setData(data);
        return result;
    }

    public static <T> Response<T> genErrorResult(Errors errors) {
        Response<T> result = Response.newInstance();
        result.setSuccess(false);
        result.setErrors(errors);

        return result;
    }

    public static <T> Response<T> genErrorResult(Errors errors, String message) {
        Response<T> result = Response.newInstance();
        result.setSuccess(false);
        result.setErrors(errors, message);

        return result;
    }
}
