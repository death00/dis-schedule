package top.death00.dis.schedule.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author death00
 * @date 2019/9/26 17:14
 */
@Configuration
public class RedisConfig {

	private final Environment env;

	public RedisConfig(Environment env) {
		this.env = env;
	}

	//region jedis

	@Bean
	public JedisPool jedisPool() {
		String host = env.getProperty("redis.host");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(host));
		String port = env.getProperty("redis.port");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(port));
		String timeout = env.getProperty("redis.timeout");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(timeout));
		String password = env.getProperty("redis.password");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(password));

		JedisPoolConfig config = jedisPoolConfig();
		return new JedisPool(
			config,
			host.trim(),
			Integer.parseInt(port.trim()),
			Integer.parseInt(timeout.trim()),
			password.trim()
		);
	}

	private JedisPoolConfig jedisPoolConfig() {
		String maxTotal = env.getProperty("redis.max_conn");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(maxTotal));
		String maxIdle = env.getProperty("redis.max_idle_conn");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(maxIdle));
		String lifo = env.getProperty("redis.lifo");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(lifo));
		String block = env.getProperty("redis.block");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(block));

		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(Integer.parseInt(maxIdle.trim()));
		config.setMaxTotal(Integer.parseInt(maxTotal.trim()));
		config.setLifo(Boolean.parseBoolean(lifo.trim()));
		config.setBlockWhenExhausted(Boolean.parseBoolean(block.trim()));

		return config;
	}

	//endregion

	//region redisson

	@Bean
	public RedissonClient redissonClient() {
		String host = env.getProperty("redis.host");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(host));
		String port = env.getProperty("redis.port");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(port));
		String password = env.getProperty("redis.password");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(password));

		Config config = new Config();
		config.useSingleServer()
			.setAddress("redis://" + host + ":" + port).setPassword(password);

		return Redisson.create(config);
	}

	//endregion
}
