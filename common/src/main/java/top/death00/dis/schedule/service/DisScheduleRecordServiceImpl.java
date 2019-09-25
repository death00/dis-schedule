package top.death00.dis.schedule.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.mongodb.client.result.UpdateResult;
import java.util.Date;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import top.death00.dis.schedule.domain.DisScheduleRecord;

/**
 * @author death00
 * @date 2019/9/21
 */
public class DisScheduleRecordServiceImpl implements IDisScheduleRecordService {

    private final MongoTemplate mongoTemplate;

    public DisScheduleRecordServiceImpl(MongoTemplate mongoTemplate) {
        Preconditions.checkNotNull(mongoTemplate);
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public UpdateResult insert(
            String name,
            Date taskDate,
            String dateStr,
            String serverName,
            Date curDate) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
        Preconditions.checkNotNull(taskDate);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(dateStr));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(serverName));
        Preconditions.checkNotNull(curDate);

        Query query = new Query(
                Criteria.where(DisScheduleRecord.DATE)
                        .is(taskDate)
                        .and(DisScheduleRecord.NAME)
                        .is(name)
        );

        Update update = new Update();
        update.setOnInsert(DisScheduleRecord.NAME, name);
        update.setOnInsert(DisScheduleRecord.DATE, taskDate);
        update.setOnInsert(DisScheduleRecord.DATE_STR, dateStr);
        update.setOnInsert(DisScheduleRecord.SERVER_NAME, serverName);
        update.setOnInsert(DisScheduleRecord.CREATE_TIMESTAMP, curDate);
        update.setOnInsert(DisScheduleRecord.UPDATE_TIMESTAMP, curDate);

        return mongoTemplate.upsert(query, update, DisScheduleRecord.class);
    }
}
