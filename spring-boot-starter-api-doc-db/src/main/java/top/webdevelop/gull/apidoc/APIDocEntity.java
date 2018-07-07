package top.webdevelop.gull.apidoc;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xumingming on 2018/6/5.
 */
@Data
abstract class APIDocEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;

    protected Long version;

    protected Date createDateTime;

    protected Date updateDateTime;

    APIDocEntity() {
        super();
    }
}
