package top.death00.dis.schedule.aspect;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.mongodb.client.result.UpdateResult;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import top.death00.dis.schedule.annotation.DisSchedule;
import top.death00.dis.schedule.constant.DisScheduleConstant;
import top.death00.dis.schedule.service.IDisScheduleRecordService;
import top.death00.dis.schedule.service.IServerConfigService;
import top.death00.dis.schedule.util.TimeUtil;

/**
 * DisSchedule切面
 *
 * @author death00
 * @date 2019/9/24 18:40
 */
@Order(100)
@Aspect
public class DisScheduleAspect {

    //region init

    private final Logger logger = LoggerFactory.getLogger(DisScheduleAspect.class);

    private final IDisScheduleRecordService disScheduleRecordService;

    private final String serverName;

    private final IServerConfigService serverConfigService;

    public DisScheduleAspect(
            IDisScheduleRecordService disScheduleRecordService,
            Environment environment,
            IServerConfigService serverConfigService) {
        Preconditions.checkNotNull(disScheduleRecordService);
        this.disScheduleRecordService = disScheduleRecordService;

        Preconditions.checkNotNull(environment);
        String serverName = environment.getProperty(DisScheduleConstant.SERVER_NAME);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(serverName));
        this.serverName = serverName;

        Preconditions.checkNotNull(serverConfigService);
        this.serverConfigService = serverConfigService;
    }

    //endregion

    /**
     * 方法上有注解SaveLog
     */
    @Pointcut(value = "@annotation(top.death00.dis.schedule.annotation.DisSchedule)")
    public void disScheduleAnnotation() {
    }

    @Around(value = "disScheduleAnnotation() && @annotation(disSchedule)")
    public Object disSchedule(ProceedingJoinPoint pjp, DisSchedule disSchedule) throws Throwable {
        Preconditions.checkNotNull(disSchedule);

        // 当前时间
        Date curDate = TimeUtil.getCurDate();

        // 获取name
        String name = disSchedule.name();
        if (Strings.isNullOrEmpty(name)) {
            // 方法名
            Signature signature = pjp.getSignature();
            name = signature.getName();
        }

        // 时间间隔
        int duration = disSchedule.duration();
        if (duration <= 0) {
            logger.error(
                    "disSchedule fail, duration {} is less or equal 0, name : {}",
                    duration,
                    name
            );
            return null;
        }

        // 时间间隔的单位
        TimeUnit unit = disSchedule.unit().getUnit();
        // 转化为毫秒
        long millis = unit.toMillis(duration);
        // 获取当前任务所属的开始时间
        Date taskDate = TimeUtil.getMillisDate(curDate, (int) millis);

        // 当前服务是否属于线上服务
        if (!serverConfigService.containsServerName(serverName)) {
            logger.info(
                    "disSchedule fail, serverName is invalid, serverName : {} , name : {} , taskDate : {}",
                    serverName,
                    name,
                    TimeUtil.specialFormatToDateStr(taskDate)
            );
            return null;
        }

        try {
            // 抢占分布式锁
            UpdateResult updateResult = disScheduleRecordService.insert(
                    name,
                    taskDate,
                    TimeUtil.formatToDateStr(taskDate),
                    serverName,
                    TimeUtil.getCurDate()
            );
            // 说明没有抢占到锁
            if (updateResult.getUpsertedId() == null) {
                logger.info(
                        "Distributed lock not acquired[1], name : {} , taskDate : {}",
                        name,
                        TimeUtil.specialFormatToDateStr(taskDate)
                );
                return null;
            }
        } catch (DuplicateKeyException e) {
            logger.info(
                    "Distributed lock not acquired[2], name : {} , taskDate : {}",
                    name,
                    TimeUtil.specialFormatToDateStr(taskDate)
            );
            // 说明没有抢占到锁
            return null;
        }

        // 执行正常的方法逻辑
        return pjp.proceed();
    }
}
