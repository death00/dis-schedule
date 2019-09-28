package top.death00.dis.schedule.config;

import com.google.common.base.Preconditions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import redis.clients.jedis.JedisPool;
import top.death00.dis.schedule.aspect.DisScheduleAspect;
import top.death00.dis.schedule.manager.IRedisManager;
import top.death00.dis.schedule.manager.RedisManagerImpl;
import top.death00.dis.schedule.service.DisScheduleMongodbServiceImpl;
import top.death00.dis.schedule.service.DisScheduleRecordServiceImpl;
import top.death00.dis.schedule.service.IDisScheduleRecordService;
import top.death00.dis.schedule.service.IDisScheduleService;
import top.death00.dis.schedule.service.IServerConfigService;
import top.death00.dis.schedule.service.ServerConfigServiceImpl;

/**
 * @author death00
 * @date 2019/9/24
 */
@Configuration
@EnableScheduling
public class ServiceConfig {

	private final MongoTemplate mongoTemplate;

	private final Environment environment;

	private final JedisPool jedisPool;

	public ServiceConfig(
		MongoTemplate mongoTemplate,
		Environment environment,
		JedisPool jedisPool) {
		Preconditions.checkNotNull(mongoTemplate);
		this.mongoTemplate = mongoTemplate;

		Preconditions.checkNotNull(environment);
		this.environment = environment;

		Preconditions.checkNotNull(jedisPool);
		this.jedisPool = jedisPool;
	}

	//region mongodb

	@Bean
	public IDisScheduleRecordService disScheduleRecordService() {
		return new DisScheduleRecordServiceImpl(mongoTemplate);
	}

	@Bean
	public IServerConfigService serverConfigService() {
		return new ServerConfigServiceImpl(mongoTemplate);
	}

	@Bean
	public IDisScheduleService disScheduleService(
		IDisScheduleRecordService disScheduleRecordService,
		IServerConfigService serverConfigService) {
		return new DisScheduleMongodbServiceImpl(serverConfigService, disScheduleRecordService);
	}

	//endregion

	//region redis

	@Bean
	public IRedisManager redisManager() {
		return new RedisManagerImpl(jedisPool);
	}

//	@Bean
//	public IDisScheduleService disScheduleService(IRedisManager redisManager) {
//		return new DisScheduleRedisServiceImpl(redisManager);
//	}

	//endregion

	@Bean
	public DisScheduleAspect disScheduleAspect(IDisScheduleService disScheduleService) {
		return new DisScheduleAspect(disScheduleService, environment);
	}
}
