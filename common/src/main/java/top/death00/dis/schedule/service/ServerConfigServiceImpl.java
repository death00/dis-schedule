package top.death00.dis.schedule.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.springframework.data.mongodb.core.MongoTemplate;
import top.death00.dis.schedule.domain.ServerConfig;

/**
 * @author death00
 * @date 2019/9/24 18:50
 */
public class ServerConfigServiceImpl implements IServerConfigService {

	private final MongoTemplate mongoTemplate;

	public ServerConfigServiceImpl(MongoTemplate mongoTemplate) {
		Preconditions.checkNotNull(mongoTemplate);
		this.mongoTemplate = mongoTemplate;
	}

	/**
	 * key为name
	 */
	private volatile ImmutableMap<String, ServerConfig> map;

	@Override
	public void reload() {
		// 查出所有配置
		List<ServerConfig> list = mongoTemplate.findAll(ServerConfig.class);

		Map<String, ServerConfig> map = Maps.newHashMapWithExpectedSize(list.size());
		for (ServerConfig config : list) {
			if (config == null) {
				continue;
			}

			String name = config.getName();
			if (Strings.isNullOrEmpty(name)) {
				continue;
			}

			// 如果服务不存活，则不需要考虑
			if (!config.isAlive()) {
				continue;
			}

			map.put(name, config);
		}

		this.map = ImmutableMap.copyOf(map);
	}

	@Override
	public boolean containsServerName(String serverName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(serverName));
		return this.map.containsKey(serverName);
	}

	@Override
	public Map<String, ServerConfig> getAll() {
		return this.map;
	}
}
