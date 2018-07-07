package top.webdevelop.gull.utils;

import java.util.UUID;

/**
 * Created by xumingming on 2018/6/12.
 */
public class UUIDUtils {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
