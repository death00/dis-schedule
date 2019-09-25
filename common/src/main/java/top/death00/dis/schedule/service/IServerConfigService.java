package top.death00.dis.schedule.service;

import java.util.Map;
import top.death00.dis.schedule.domain.ServerConfig;

/**
 * @author death00
 * @date 2019/9/24 18:49
 */
public interface IServerConfigService {

    /**
     * 刷新内存数据
     */
    void reload();

    /**
     * serverNames是否包含该serverName
     */
    boolean containsServerName(String serverName);

    /**
     * 获取所有
     */
    Map<String, ServerConfig> getAll();
}
