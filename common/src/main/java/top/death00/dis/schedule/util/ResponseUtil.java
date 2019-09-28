package top.death00.dis.schedule.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回给前端的结果
 *
 * @author gyj
 * @date 2019/5/21
 */
public class ResponseUtil {

    private static final String RESULT_CODE = "resultCode";

    private static final String DATA = "data";

    private static final String MESSAGE = "message";

    /**
     * 成功
     */
    public static Map<String, Object> success(String message, Object data) {
        Map<String, Object> map = new HashMap<>(5);
        map.put(RESULT_CODE, 0);
        map.put(DATA, data);
        map.put(MESSAGE, message);
        return map;
    }

    /**
     * 失败
     */
    public static Map<String, Object> fail(String message, Object data) {
        Map<String, Object> map = new HashMap<>(5);
        map.put(RESULT_CODE, 400);
        map.put(DATA, data);
        map.put(MESSAGE, message);
        return map;
    }
}
