package top.webdevelop.gull.apidoc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Setter;
import top.webdevelop.gull.utils.UUIDUtils;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by xumingming on 2018/4/9.
 */
@Data
public class APIDocMenu extends APIDocEntity implements Comparable<APIDocMenu> {
    private String apiDocId;

    @Setter
    private String desc;
    private String mapping;
    private String action;

    @Setter
    private Set<APIDocMenu> childs = new TreeSet<>();

    @Setter
    @JsonIgnore
    private APIDoc apiDoc;

    APIDocMenu() {
        super();
    }

    public APIDocMenu(String mapping) {
        this();
        this.id = UUIDUtils.uuid();
        this.desc = mapping;
        this.mapping = mapping;
    }

    public APIDocMenu(APIDoc apiDoc, String title) {
        this(apiDoc.getUrl());
        this.apiDocId = apiDoc.getId();
        this.desc = title;
        this.action = apiDoc.getAction();
        this.apiDoc = apiDoc;
    }

    public void addChild(APIDocMenu menu) {
        this.childs.add(menu);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        APIDocMenu that = (APIDocMenu) o;

        if (mapping != null ? !mapping.equals(that.mapping) : that.mapping != null) return false;
        return action != null ? action.equals(that.action) : that.action == null;
    }

    @Override
    public int hashCode() {
        int result = mapping != null ? mapping.hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(APIDocMenu o) {
        int i = this.mapping.compareTo(o.mapping);
        if (i != 0 || this.action == null || o.action == null) {
            return i;
        }
        return this.action.compareTo(o.action);
    }
}
