package top.webdevelop.gull.controller;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by xumingming on 2018/6/13.
 */
@Data
public class APIDocDescParam implements Serializable {
    @NotNull
    private String desc;
    @NotNull
    private Long version;
}
