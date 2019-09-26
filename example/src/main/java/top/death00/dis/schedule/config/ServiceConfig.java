package top.death00.dis.schedule.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import redis.clients.jedis.JedisPool;
import top.death00.dis.schedule.aspect.DisScheduleAspect;
import top.death00.dis.schedule.constant.DisScheduleConstant;
import top.death00.dis.schedule.constant.RedisKey;
import top.death00.dis.schedule.manager.IRedisManager;
import top.death00.dis.schedule.manager.RedisManagerImpl;
import top.death00.dis.schedule.service.DisScheduleMongodbServiceImpl;
import top.death00.dis.schedule.service.DisScheduleRecordServiceImpl;
import top.death00.dis.schedule.service.DisScheduleRedisServiceImpl;
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

	@Bean
	public IDisScheduleRecordService disScheduleRecordService() {
		return new DisScheduleRecordServiceImpl(mongoTemplate);
	}

	@Bean
	public IServerConfigService serverConfigService() {
		return new ServerConfigServiceImpl(mongoTemplate);
	}

	@Bean
	public IDisScheduleService disScheduleMongodbService(
		IDisScheduleRecordService disScheduleRecordService,
		IServerConfigService serverConfigService) {
		return new DisScheduleMongodbServiceImpl(serverConfigService, disScheduleRecordService);
	}

	@Bean
	public IRedisManager redisManager() {
		return new RedisManagerImpl(jedisPool);
	}

	@Bean
	public IDisScheduleService disScheduleRedisService(IRedisManager redisManager) {
		return new DisScheduleRedisServiceImpl(redisManager);
	}

	@Bean
	public DisScheduleAspect disScheduleAspect(IDisScheduleService disScheduleRedisService) {
		return new DisScheduleAspect(disScheduleRedisService, environment);
	}

	@PostConstruct
	public void startServer() {
		IServerConfigService serverConfigService = serverConfigService();
		serverConfigService.reload();

		String serverName = environment.getProperty(DisScheduleConstant.SERVER_NAME);
		Preconditions.checkArgument(!Strings.isNullOrEmpty(serverName));

		// 向redis中添加serverName
		IRedisManager redisManager = redisManager();
		redisManager.sadd(RedisKey.DIS_SCHEDULE_SERVER_NAME, serverName);
	}
}
