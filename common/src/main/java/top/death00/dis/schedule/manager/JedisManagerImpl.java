package top.death00.dis.schedule.manager;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.Objects;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 基于jedis实现的redisManager
 *
 * @author death00
 * @date 2019/9/26 17:29
 */
public class JedisManagerImpl implements IRedisManager {

	private final JedisPool jedisPool;

	public JedisManagerImpl(JedisPool jedisPool) {
		Preconditions.checkNotNull(jedisPool);
		this.jedisPool = jedisPool;
	}

	@Override
	public boolean sadd(String key, String value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(value));

		try (Jedis jedis = jedisPool.getResource()) {
			// 返回1说明添加成功，返回0说明已经存在
			return jedis.sadd(key, value) == 1L;
		}
	}

	@Override
	public boolean sismember(String key, String value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(value));

		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.sismember(key, value);
		}
	}

	@Override
	public void srem(String key, String value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(value));

		try (Jedis jedis = jedisPool.getResource()) {
			jedis.srem(key, value);
		}
	}

	@Override
	public boolean setNx(String key, String value) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(value));

		try (Jedis jedis = jedisPool.getResource()) {
			String result = jedis.set(key, value, "NX");
			// 返回OK，说明添加成功
			return Objects.equals(result, "OK");
		}
	}
}
