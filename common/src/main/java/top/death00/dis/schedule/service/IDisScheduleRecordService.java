package top.death00.dis.schedule.service;

import com.mongodb.client.result.UpdateResult;
import java.util.Date;

/**
 * @author death00
 * @date 2019/9/21
 */
public interface IDisScheduleRecordService {

    /**
     * 插入数据
     */
    UpdateResult insert(
            String name,
            Date taskDate,
            String dateStr,
            String serverName,
            Date curDate
    );
}
