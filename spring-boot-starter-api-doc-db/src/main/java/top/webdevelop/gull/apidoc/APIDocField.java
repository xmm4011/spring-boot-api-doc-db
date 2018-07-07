package top.webdevelop.gull.apidoc;

import lombok.Data;
import lombok.Setter;
import top.webdevelop.gull.utils.UUIDUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xumingming on 2018/6/13.
 */
@Data
public class APIDocField extends APIDocEntity {
    private String name;
    @Setter
    private APIDocFieldType type;
    private boolean required;
    @Setter
    private String desc;

    @Setter
    private List<APIDocField> childs = new ArrayList<>();

    APIDocField() {
        super();
    }

    public APIDocField(String name, APIDocFieldType type) {
        this();
        this.id = UUIDUtils.uuid();
        this.name = name;
        this.type = type;
    }

    public APIDocField(String name, APIDocFieldType type, boolean required) {
        this(name, type);
        this.required = required;
    }

    public APIDocField(String name, APIDocFieldType type, boolean required, List<APIDocField> childs) {
        this(name, type, required);
        this.childs = childs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        APIDocField that = (APIDocField) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
