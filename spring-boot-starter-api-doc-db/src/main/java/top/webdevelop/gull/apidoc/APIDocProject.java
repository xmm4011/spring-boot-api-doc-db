package top.webdevelop.gull.apidoc;

import lombok.Data;
import lombok.Setter;
import top.webdevelop.gull.utils.UUIDUtils;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by xumingming on 2018/6/5.
 */
@Data
public class APIDocProject extends APIDocEntity {
    private String name;

    @Setter
    private Set<APIDocMenu> menus = new TreeSet<>();

    APIDocProject() {
        super();
    }

    public APIDocProject(String name) {
        this();
        this.id = UUIDUtils.uuid();
        this.name = name;
    }

    public void addMenu(APIDocMenu menu) {
        this.menus.add(menu);
    }
}
