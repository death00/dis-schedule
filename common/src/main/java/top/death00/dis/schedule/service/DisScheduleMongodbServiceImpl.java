package top.death00.dis.schedule.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.mongodb.client.result.UpdateResult;
import java.util.Date;
import org.springframework.dao.DuplicateKeyException;
import top.death00.dis.schedule.util.TimeUtil;

/**
 * mongodb实现
 *
 * @author death00
 * @date 2019/9/26 17:02
 */
public class DisScheduleMongodbServiceImpl implements IDisScheduleService {

	private final IServerConfigService serverConfigService;

	private final IDisScheduleRecordService disScheduleRecordService;

	public DisScheduleMongodbServiceImpl(
		IServerConfigService serverConfigService,
		IDisScheduleRecordService disScheduleRecordService) {
		Preconditions.checkNotNull(serverConfigService);
		Preconditions.checkNotNull(disScheduleRecordService);

		this.serverConfigService = serverConfigService;
		this.disScheduleRecordService = disScheduleRecordService;
	}


	@Override
	public void reload() {
		serverConfigService.reload();
	}

	@Override
	public boolean serverNameIsValid(String serverName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(serverName));

		return serverConfigService.containsServerName(serverName);
	}

	@Override
	public boolean tryGetLock(String taskName, Date taskDate, String serverName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(taskName));
		Preconditions.checkNotNull(taskDate);
		Preconditions.checkArgument(!Strings.isNullOrEmpty(serverName));

		try {
			// 抢占分布式锁
			UpdateResult updateResult = disScheduleRecordService.insert(
				taskName,
				taskDate,
				TimeUtil.formatToDateStr(taskDate),
				serverName,
				TimeUtil.getCurDate()
			);
			// 说明没有抢占到锁
			if (updateResult.getUpsertedId() == null) {
				return false;
			}
		} catch (DuplicateKeyException e) {
			// 说明没有抢占到锁
			return false;
		}

		// 抢到锁了
		return true;
	}

	@Override
	public void addServerName(String serverName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(serverName));
		serverConfigService.upsert(serverName, true, TimeUtil.getCurDate());
	}
}
