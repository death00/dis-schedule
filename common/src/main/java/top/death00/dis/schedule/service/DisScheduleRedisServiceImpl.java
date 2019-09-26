package top.death00.dis.schedule.service;

import com.google.common.base.Preconditions;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.death00.dis.schedule.constant.RedisKey;
import top.death00.dis.schedule.manager.IRedisManager;
import top.death00.dis.schedule.util.LogUtil;
import top.death00.dis.schedule.util.TimeUtil;

/**
 * redis实现
 *
 * @author death00
 * @date 2019/9/26 17:02
 */
public class DisScheduleRedisServiceImpl implements IDisScheduleService {

	private final Logger logger = LoggerFactory.getLogger(DisScheduleRedisServiceImpl.class);

	private final IRedisManager redisManager;

	public DisScheduleRedisServiceImpl(IRedisManager redisManager) {
		Preconditions.checkNotNull(redisManager);
		this.redisManager = redisManager;
	}

	@Override
	public boolean serverNameIsValid(String serverName) {
		try {
			return redisManager.sismember(RedisKey.DIS_SCHEDULE_SERVER_NAME, serverName);
		} catch (Exception e) {
			logger.error(
				"DisScheduleRedisServiceImpl-serverNameIsValid fail, serverName : {} , exception : {}",
				serverName,
				LogUtil.extractStackTrace(e)
			);
		}

		return false;
	}

	@Override
	public boolean tryGetLock(String taskName, Date taskDate, String serverName) {
		try {
			return redisManager.setNx(
				taskName + "_" + TimeUtil.specialFormatToDateStr(taskDate),
				serverName
			);
		} catch (Exception e) {
			logger.error(
				"DisScheduleRedisServiceImpl-tryGetLock fail, taskName : {} , taskDate : {} , serverName : {} , exception : {}",
				taskName,
				TimeUtil.specialFormatToDateStr(taskDate),
				serverName,
				LogUtil.extractStackTrace(e)
			);
		}

		return false;
	}
}
