package top.death00.dis.schedule.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.death00.dis.schedule.annotation.DisSchedule;
import top.death00.dis.schedule.constant.DisScheduleConstant;
import top.death00.dis.schedule.constant.DisScheduleUnit;
import top.death00.dis.schedule.service.IDisScheduleService;
import top.death00.dis.schedule.util.LogUtil;
import top.death00.dis.schedule.util.ResponseUtil;

/**
 * @author death00
 * @date 2019/9/28 9:21
 */
@RestController("/schedule")
public class ScheduleController {

	//region init

	private final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

	private final IDisScheduleService disScheduleService;

	private final String serverName;

	public ScheduleController(
		IDisScheduleService disScheduleService,
		Environment environment) {
		Preconditions.checkNotNull(disScheduleService);
		this.disScheduleService = disScheduleService;

		Preconditions.checkNotNull(environment);
		String serverName = environment.getProperty(DisScheduleConstant.SERVER_NAME);
		Preconditions.checkArgument(!Strings.isNullOrEmpty(serverName));
		this.serverName = serverName;
	}

	//endregion

	@PostConstruct
	public void init() {
		disScheduleService.addServerName(serverName);
	}

	/**
	 * 重新加载缓存中的信息
	 */
	@RequestMapping("/reload")
	@ResponseBody
	public Map<String, Object> reload() {
		disScheduleService.reload();
		return ResponseUtil.success(null, null);
	}

	/**
	 * 使当前服务生效
	 */
	@RequestMapping("/add/serverName")
	@ResponseBody
	public Map<String, Object> addServerName() {
		disScheduleService.addServerName(serverName);
		disScheduleService.reload();
		return ResponseUtil.success(null, null);
	}

	/**
	 * 使当前服务失效
	 */
	@RequestMapping("/remove/serverName")
	@ResponseBody
	public Map<String, Object> removeServerName() {
		disScheduleService.removeServerName(serverName);
		disScheduleService.reload();
		return ResponseUtil.success(null, null);
	}

	//region 定时调度任务

	@DisSchedule(name = "testSchedule", duration = 1, unit = DisScheduleUnit.MINUTES)
	@Scheduled(cron = "0 0/1 * * * ?")
	public void testSchedule() {
		logger.info("输出");
	}

	//endregion

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Map<String, Object> exceptionHandler(Exception e, Request request) throws Exception {
		// 无权访问抛的异常
		if (e instanceof AccessDeniedException) {
			throw e;
		}

		HttpURI uri = request.getHttpURI();
		String pathQuery = uri == null ? null : uri.getPathQuery();

		logger.error(
			"ScheduleController operation pathQuery: {} throw exception: {}",
			pathQuery,
			LogUtil.extractStackTrace(e)
		);
		return ResponseUtil.fail("异常", null);
	}
}
