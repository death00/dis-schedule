package top.death00.dis.schedule.service;

import java.util.Date;

/**
 * 分布式定时调度服务
 *
 * @author death00
 * @date 2019/9/26 16:58
 */
public interface IDisScheduleService {

	/**
	 * serverName是否有效
	 */
	boolean serverNameIsValid(String serverName);

	/**
	 * 尝试获取锁
	 */
	boolean tryGetLock(String taskName, Date taskDate, String serverName);
}
