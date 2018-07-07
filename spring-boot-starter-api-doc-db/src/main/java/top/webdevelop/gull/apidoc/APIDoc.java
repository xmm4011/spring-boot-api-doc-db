package top.webdevelop.gull.apidoc;

import lombok.Data;
import lombok.Setter;
import top.webdevelop.gull.utils.UUIDUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xumingming on 2018/3/24.
 */
@Data
public class APIDoc extends APIDocEntity {
    private String url;
    private String action;
    private String contact;
    @Setter
    private List<APIDocField> request = new ArrayList<>();
    @Setter
    private List<APIDocField> response = new ArrayList<>();

    APIDoc() {
        super();
    }

    public APIDoc(String url, String action) {
        this();
        this.id = UUIDUtils.uuid();
        this.url = url;
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        APIDoc apiDoc = (APIDoc) o;

        if (url != null ? !url.equals(apiDoc.url) : apiDoc.url != null) return false;
        return action != null ? action.equals(apiDoc.action) : apiDoc.action == null;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }
}
