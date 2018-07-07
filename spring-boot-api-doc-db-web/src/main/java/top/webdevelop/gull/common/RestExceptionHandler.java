package top.webdevelop.gull.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import top.webdevelop.gull.exception.APIDocException;

/**
 * Created by xumingming on 2018/6/13.
 */
@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler
    @ResponseBody
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseStatus(HttpStatus.OK)
    public <T> Response<T> globalExceptionHandler(Exception e) {
        LOGGER.error("GlobalExceptionHandler", e);
        return ResponseGen.genErrorResult(Errors.SYS_ERROR);
    }

    @ExceptionHandler(APIDocException.class)
    @ResponseBody
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseStatus(HttpStatus.OK)
    public <T> Response<T> searchExceptionHandler(APIDocException e) {
        LOGGER.error("APIDocException, code: " + e.getErrors().getCode(), e);
        return ResponseGen.genErrorResult(e.getErrors());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseStatus(HttpStatus.OK)
    public <T> Response<T> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        LOGGER.error("MethodArgumentNotValidExceptionHandler", e);

        FieldError fieldError = e.getBindingResult().getFieldError();
        return ResponseGen.genErrorResult(Errors.ARGUMENT_NOT_VALID_ERROR, fieldError.getField() + fieldError.getDefaultMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseStatus(HttpStatus.OK)
    public <T> Response<T> bindExceptionHandler(BindException e) {
        LOGGER.error("BindExceptionHandler", e);

        FieldError fieldError = e.getBindingResult().getFieldError();
        return ResponseGen.genErrorResult(Errors.ARGUMENT_NOT_VALID_ERROR, fieldError.getField() + fieldError.getDefaultMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseStatus(HttpStatus.OK)
    public <T> Response<T> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        LOGGER.error("MissingServletRequestParameterExceptionHandler", e);
        return ResponseGen.genErrorResult(Errors.ARGUMENT_NOT_VALID_ERROR, e.getLocalizedMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseStatus(HttpStatus.OK)
    public <T> Response<T> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        LOGGER.error("HttpMessageNotReadableException", e);
        return ResponseGen.genErrorResult(Errors.ARGUMENT_NOT_VALID_ERROR);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseBody
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseStatus(HttpStatus.OK)
    public <T> Response<T> illegalStateException(IllegalStateException e) {
        LOGGER.error("IllegalStateExceptionHandler", e);
        return ResponseGen.genErrorResult(Errors.ARGUMENT_NOT_VALID_ERROR, e.getMessage());
    }

}