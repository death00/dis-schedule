package top.death00.dis.schedule.config;

import com.google.common.base.Preconditions;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import top.death00.dis.schedule.aspect.DisScheduleAspect;
import top.death00.dis.schedule.service.DisScheduleRecordServiceImpl;
import top.death00.dis.schedule.service.IDisScheduleRecordService;
import top.death00.dis.schedule.service.IServerConfigService;
import top.death00.dis.schedule.service.ServerConfigServiceImpl;

/**
 * @author death00
 * @date 2019/9/24
 */
@Configuration
@EnableScheduling
public class ServiceConfiguration {

    private final MongoTemplate mongoTemplate;

    private final Environment environment;

    public ServiceConfiguration(
            MongoTemplate mongoTemplate,
            Environment environment) {
        Preconditions.checkNotNull(mongoTemplate);
        this.mongoTemplate = mongoTemplate;

        Preconditions.checkNotNull(environment);
        this.environment = environment;
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
    public DisScheduleAspect disScheduleAspect(
            IDisScheduleRecordService disScheduleRecordService,
            IServerConfigService serverConfigService) {
        return new DisScheduleAspect(disScheduleRecordService, environment, serverConfigService);
    }

    @PostConstruct
    public void startServer() {
        IServerConfigService serverConfigService = serverConfigService();
        serverConfigService.reload();
    }
}
