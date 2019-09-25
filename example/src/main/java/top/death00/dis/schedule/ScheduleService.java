package top.death00.dis.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.death00.dis.schedule.annotation.DisSchedule;

/**
 * @author death00
 * @date 2019/9/24 19:17
 */
@Service
public class ScheduleService {

    private final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    @DisSchedule(duration = 1)
    @Scheduled(cron = "0 0/1 * * * ?")
    public void testSchedule() {
        logger.info("输出");
    }
}
